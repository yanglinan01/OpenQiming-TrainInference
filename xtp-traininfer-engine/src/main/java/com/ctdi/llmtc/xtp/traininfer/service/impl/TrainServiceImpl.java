package com.ctdi.llmtc.xtp.traininfer.service.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.ctdi.llmtc.xtp.traininfer.beans.param.DPOTask;
import com.ctdi.llmtc.xtp.traininfer.beans.param.EvalTask;
import com.ctdi.llmtc.xtp.traininfer.beans.param.InferenceTask;
import com.ctdi.llmtc.xtp.traininfer.beans.param.TrainTask;
import com.ctdi.llmtc.xtp.traininfer.beans.req.TrainReq;
import com.ctdi.llmtc.xtp.traininfer.constant.ModelConstants;
import com.ctdi.llmtc.xtp.traininfer.constant.StatusEnum;
import com.ctdi.llmtc.xtp.traininfer.service.TrainService;
import com.ctdi.llmtc.xtp.traininfer.util.*;
import com.ctdi.llmtc.xtp.traininfer.util.validator.Groups;
import com.ctdi.llmtc.xtp.traininfer.util.validator.ValidationUtils;
import io.fabric8.kubernetes.api.model.HasMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author ctdi
 * @since 2025/6/5
 */
@Service
@Slf4j
public class TrainServiceImpl implements TrainService {

    /** k8s工具实例 */
    public static final K8sUtil k8sUtil = K8sUtil.getInstance();

    @Autowired
    private ModelPathUtil modelPathUtil;

    @Override
    public OperateResult<String> trainSubmit(TrainReq trainReq) {
        if (StrUtil.isNotEmpty(trainReq.getAdapterNameOrPath())) {
            trainReq.setOp(ModelConstants.OP_DPO);
            ValidationUtils.validate(trainReq, Groups.DPO.class);
        } else {
            trainReq.setOp(ModelConstants.OP_TRAIN);
            ValidationUtils.validate(trainReq, Groups.TRAIN.class);
        }

        String dataset = trainReq.getDataset();
        String suffix = dataset.substring(dataset.lastIndexOf('.') + 1);
        String fn = dataset.replace("." + suffix, "");

        String matchedDs = ModelConstants.SYSTEM_DATASET.stream()
                .filter(dataset::contains)
                .findFirst()
                .orElse(null);

        if (StrUtil.isNotEmpty(matchedDs)) {
            trainReq.setDataset(matchedDs);
        } else {
            if (!DatasetUtil.convertDataset(modelPathUtil, fn, suffix)) {
                log.error("submit train error. {}", StatusEnum.DATASET_NOT_EXIST.getMsg());
                return OperateResult.fail(StatusEnum.DATASET_NOT_EXIST);
            }
            trainReq.setDataset(fn);
        }

        if (!generateYml(trainReq)) {
            log.error("submit train error. {}", StatusEnum.YML_GENERATE_FAIL.getMsg());
            return OperateResult.fail(StatusEnum.YML_GENERATE_FAIL);
        }

        if (!createYml(trainReq)) {
            log.error("submit train error. {}", StatusEnum.TASK_SUBMIT_FAIL.getMsg());
            return OperateResult.fail(StatusEnum.TASK_SUBMIT_FAIL);
        }
        return OperateResult.success();
    }

    @Override
    public OperateResult<String> delTrain(String taskId) {
        if (ModelConstants.X86_ARCH.equals(getRunEnv()) || !ModelConstants.IS_VOLCANO) {
            String yamlFileName = modelPathUtil.getYamlFilePath(taskId, ModelConstants.OP_TRAIN) + "/" + ModelConstants.K8S_CONFIG_FILE;
            if (!FileUtil.exist(yamlFileName)) {
                yamlFileName = modelPathUtil.getYamlFilePath(taskId, ModelConstants.OP_DPO) + "/" + ModelConstants.K8S_CONFIG_FILE;
            }

            if (!FileUtil.exist(yamlFileName)) {
                FileUtil.delTrainFile(modelPathUtil, taskId); // python找不到目录也试图删除下
                log.info("delete fail, yml file not found. yamlFileName: {}", yamlFileName);
                return OperateResult.success(); // TODO python逻辑是这样的
            }
            k8sUtil.deleteFromYml(ModelConstants.OP_TRAIN, yamlFileName);
        } else {
            k8sUtil.deleteVolcanoJob(taskId);
        }
        FileUtil.delTrainFile(modelPathUtil, taskId);
        return OperateResult.success();
    }

    @Override
    public OperateResult<String> delInfer(String taskId) {
        k8sUtil.delVs(taskId);
        String yamlFileName = modelPathUtil.getYamlFilePath(taskId, ModelConstants.OP_INFER) + "/" + ModelConstants.K8S_CONFIG_FILE;
        k8sUtil.deleteFromYml(ModelConstants.OP_INFER, yamlFileName);
        k8sUtil.deleteHPA(ModelConstants.NS_INFERENCE, taskId);
        FileUtil.delInferFile(modelPathUtil, taskId);
        return OperateResult.success();
    }

    @Override
    public OperateResult<String> eval(TrainReq trainReq) {
        ValidationUtils.validate(trainReq, Groups.EVAL.class);

        String taskId = trainReq.getTaskId();
        trainReq.setOp(ModelConstants.OP_EVAL);
        if (!generateYml(trainReq)) {
            log.error("submit eval error. {}", StatusEnum.YML_GENERATE_FAIL.getMsg());
            return OperateResult.fail(StatusEnum.YML_GENERATE_FAIL);
        }

        String yamlFileName = modelPathUtil.getYamlFilePath(taskId, trainReq.getOp()) + "/" + ModelConstants.K8S_CONFIG_FILE;
        List<HasMetadata> metadataList = k8sUtil.createFromYml(yamlFileName);
        if (ObjUtil.isEmpty(metadataList)) {
            log.error("submit eval error. {}", StatusEnum.TASK_SUBMIT_FAIL.getMsg());
            FileUtil.delEvalFile(modelPathUtil, taskId);
            return OperateResult.fail(StatusEnum.TASK_SUBMIT_FAIL);
        }
        return OperateResult.success();
    }

    @Override
    public OperateResult<String> inferSubmit(TrainReq trainReq) {
        trainReq.setOp(ModelConstants.OP_INFER);
        if (!generateYml(trainReq)) {
            log.error("submit train error. {}", StatusEnum.INFER_YML_GENERATE_FAIL.getMsg());
            return OperateResult.fail(StatusEnum.INFER_YML_GENERATE_FAIL);
        }

        String taskId = trainReq.getTaskId();
        if (!k8sUtil.createVs(taskId)) {
            return OperateResult.fail(StatusEnum.CREATE_VS_FAIL);
        }

        String yamlFileName = modelPathUtil.getYamlFilePath(taskId, trainReq.getOp()) + "/" + ModelConstants.K8S_CONFIG_FILE;
        List<HasMetadata> metadataList = k8sUtil.createFromYml(yamlFileName);
        if (ObjUtil.isEmpty(metadataList)) {
            k8sUtil.delVs(taskId);
            return OperateResult.fail(StatusEnum.INFER_YML_GENERATE_FAIL);
        }
        k8sUtil.createHPA(ModelConstants.NS_INFERENCE, taskId);
        return OperateResult.success();
    }

    @Override
    public String inferStatus(String taskId) {
        return k8sUtil.getPodStatus(ModelConstants.NS_INFERENCE,
                ModelConstants.INFER_DEPLOY_PREFIX + taskId);
    }

    @Override
    public OperateResult<String> intentSync(String taskId) {
        String srcIntentPath = modelPathUtil.getModelFilePath(taskId);
        log.info("sync intent file start. src_intent_path: {}", srcIntentPath);
        if (!FileUtil.exist(srcIntentPath)) {
            return OperateResult.fail(StatusEnum.IR_FILE_NOT_EXIST);
        }

        String destIntentPath = modelPathUtil.getIntentSyncPath(taskId);
        if (FileUtil.exist(destIntentPath)) {
            FileUtil.rmTree(destIntentPath);
            log.info("delete existing IR file: {}", destIntentPath);
        }

        FileUtil.copyTree(srcIntentPath, destIntentPath);
        log.info("sync intent file success. srcIntentPath: {}", srcIntentPath);
        return OperateResult.success();
    }

    public boolean generateYml(TrainReq trainReq) {
        try {
            String op = trainReq.getOp();
            String taskId = trainReq.getTaskId();
            String taskDirPath = modelPathUtil.getYamlFilePath(taskId, op);
            if (!FileUtil.exist(taskDirPath)) {
                FileUtil.mkdir(taskDirPath);
            }

            switch (trainReq.getOp()) {
                case ModelConstants.OP_DPO -> {
                    DPOTask dpoTask = new DPOTask();
                    BeanUtils.copyProperties(trainReq, dpoTask);
                    genK8sTmpFile(dpoTask);
                    genParamFile(dpoTask);
                }
                case ModelConstants.OP_TRAIN -> {
                    TrainTask trainTask = new TrainTask();
                    BeanUtils.copyProperties(trainReq, trainTask);
                    genK8sTmpFile(trainTask);
                    genParamFile(trainTask);
                }
                case ModelConstants.OP_EVAL -> {
                    EvalTask evalTask = new EvalTask();
                    BeanUtils.copyProperties(trainReq, evalTask);
                    genK8sTmpFile(evalTask);
                    genParamFile(evalTask);
                }
                case ModelConstants.OP_INFER -> {
                    InferenceTask inferTask = new InferenceTask();
                    BeanUtils.copyProperties(trainReq, inferTask);
                    genK8sTmpFile(inferTask);
                    genParamFile(inferTask);
                }
            }
        } catch (Exception e) {
            log.info("generateYml error.", e);
            return false;
        }
        return true;
    }

    public boolean createYml(TrainReq trainReq) {
        try {
            String op = trainReq.getOp();
            String yamlFileName = modelPathUtil.getYamlFilePath(trainReq.getTaskId(), op) + "/" + ModelConstants.K8S_CONFIG_FILE;
            if (ModelConstants.X86_ARCH.equals(getRunEnv()) || !ModelConstants.IS_VOLCANO) {
                k8sUtil.createFromYml(yamlFileName);
            } else {
                InputStream inputStream = new FileInputStream(yamlFileName);
                Yaml yaml = new Yaml();
                Iterable<Object> docs = yaml.loadAll(inputStream);
                // 遍历 YAML 文档，找到 Service 部分
                for (Object doc : docs) {
                    Map<String, Object> yamlDoc = (Map<String, Object>) doc;
                    String apiVersion = (String)yamlDoc.get("apiVersion");
                    String kind = (String)yamlDoc.get("kind");
                    if ("batch.volcano.sh/v1alpha1".equals(apiVersion) && "Job".equals(kind)) {
                        k8sUtil.createVcCrd(ModelConstants.NS_TRAIN, JSONUtil.toJsonStr(yamlDoc));
                    } else if ("Service".equals(kind)) {
                        k8sUtil.createService(ModelConstants.NS_TRAIN, yamlDoc);
                    }
                }
            }
        } catch (Exception e) {
            log.info("createYml error.", e);
            return false;
        }
        return true;
    }

    public void genK8sTmpFile(DPOTask dpoTask) {
        String modelName = dpoTask.getModelName();

        String trainK8sTplName = ModelConstants.TRAIN_K8S_TPL_CONF.get(ModelConstants.TRAIN_OTHER_K8S_TPL);
        if (ModelConstants.QWEN_2_72B.equals(modelName)) {
            trainK8sTplName = ModelConstants.TRAIN_K8S_TPL_CONF.get(ModelConstants.TRAIN_DISTRIBUTED_K8S_TPL);
        }

        // 写入到输出文件
        String trainK8sTplPath = modelPathUtil.getTplDir(ModelConstants.OP_DPO) + trainK8sTplName;
        String dpoDirPath = modelPathUtil.getDpoDirPath(dpoTask.getTaskId());
        String outputYamlPath = dpoDirPath + "/" + ModelConstants.K8S_CONFIG_FILE;
        YmlUtil.genK8sConfig(dpoTask.getTaskId(), trainK8sTplPath, outputYamlPath);

    }

    public void genParamFile(DPOTask dpoTask) throws IOException {
        dpoTask.setOutputDir(ModelConstants.SAVE_DIR + dpoTask.getTaskId());
        dpoTask.setAdapterNameOrPath(ModelConstants.SAVE_DIR + dpoTask.getAdapterNameOrPath());

        // model_name_or_path，取决于model_name
        String modelName = dpoTask.getModelName();
        String deepspeed = ModelConstants.DEEPSPEED_CONF2.get(modelName);
        dpoTask.setDeepspeed(deepspeed);

        String modelNameOrPath = ModelConstants.MODEL_CONF.get(modelName);
        dpoTask.setModelNameOrPath(modelNameOrPath);

        String trainParamTplName = ModelConstants.TRAIN_PARAM_TPL_CONF.get(ModelConstants.TRAIN_METHOD_DPO);

        // 写入到输出文件
        String trainParamTplPath = modelPathUtil.getTplDir(ModelConstants.OP_DPO) + trainParamTplName;
        String dpoDirPath = modelPathUtil.getDpoDirPath(dpoTask.getTaskId());
        String outputYamlPath = dpoDirPath + "/" + ModelConstants.TRAIN_PARAM_CONFIG_FILE;
        Map<String, Object> updatesMap = JSONUtil.convertValue(dpoTask);

        // TODO 下面这块逻辑应该不需要，带验证
        //# 针对全参训练后进行dpo的情况 删除lora adapter参数
        //if data['adapter_name_or_path'] == '/app/saves/output_dir/':
        //del data['adapter_name_or_path']

        YmlUtil.genParamConfig(updatesMap, trainParamTplPath, outputYamlPath);
    }

    public void genK8sTmpFile(TrainTask trainTask) {
        boolean strategyDistributed = trainTask.getStrategyDistributed();
        String strategyTrain = trainTask.getStrategyTrain();
        String modelName = trainTask.getModelName();

        String trainK8sTplName = ModelConstants.TRAIN_K8S_TPL_CONF.get(ModelConstants.TRAIN_OTHER_K8S_TPL);
        if (strategyDistributed || ModelConstants.TRAIN_METHOD_FULL.equals(strategyTrain)) {
            trainK8sTplName = ModelConstants.TRAIN_K8S_TPL_CONF.get(ModelConstants.TRAIN_DISTRIBUTED_K8S_TPL);
        } else if (ModelConstants.QWEN_2_72B.equals(modelName)
                || ModelConstants.QWEN_2_5_72B_INSTRUCT.equals(modelName)) {
            trainK8sTplName = ModelConstants.TRAIN_K8S_TPL_CONF.get(ModelConstants.TRAIN_DISTRIBUTED_K8S_TPL);
        }

        // 写入到输出文件
        String trainK8sTplPath = modelPathUtil.getTplDir(ModelConstants.OP_TRAIN) + trainK8sTplName;
        String trainDirPath = modelPathUtil.getTrainDirPath(trainTask.getTaskId());
        String outputYamlPath = trainDirPath + "/" + ModelConstants.K8S_CONFIG_FILE;
        YmlUtil.genK8sConfig(trainTask.getTaskId(), trainK8sTplPath, outputYamlPath);
    }

    public void genParamFile(TrainTask trainTask) throws IOException {
        trainTask.setOutputDir(ModelConstants.SAVE_DIR + trainTask.getTaskId());

        // deepspeed文件路径，取决于strategy_deepspeed
        String deepspeed = ModelConstants.DEEPSPEED_CONF.get(trainTask.getStrategyDeepspeed());
        trainTask.setDeepspeed(deepspeed);

        // model_name_or_path，取决于model_name
        // 暂不提供qwen1.5_14b, qwen2_72b以外的选项
        // 在qwen2_72b的情况，强制启用zero3
        String modelName = trainTask.getModelName();
        String modelNameOrPath = ModelConstants.MODEL_CONF.get(modelName);
        if (ModelConstants.QWEN_2_72B.equals(modelName) || ModelConstants.QWEN_2_5_72B_INSTRUCT.equals(modelName)) {
            trainTask.setDeepspeed(ModelConstants.DEEPSPEED_CONF.get(3));
        }
        trainTask.setModelNameOrPath(modelNameOrPath);

        // 模板选择，取决于distributed，
        String strategyTrain = trainTask.getStrategyTrain();
        // 目前由于deepspeed torchrun适配单GPU 不对是否启用deepspeed进行区分 统一采用ds模板运行单机单卡 单机双卡 多机多卡
        String trainParamTplName = ModelConstants.TRAIN_PARAM_TPL_CONF.get(strategyTrain);
        if (ModelConstants.TRAIN_METHOD_FULL.equals(strategyTrain)
                && ModelConstants.QWEN_2_1_5B.equals(modelName)) {
            trainParamTplName = ModelConstants.TRAIN_PARAM_TPL_CONF.get(ModelConstants.TRAIN_METHOD_FULL);
        }

        // 写入到输出文件
        String trainParamTplPath = modelPathUtil.getTplDir(ModelConstants.OP_TRAIN) + trainParamTplName;
        String trainDirPath = modelPathUtil.getTrainDirPath(trainTask.getTaskId());
        String outputYamlPath = trainDirPath + "/" + ModelConstants.TRAIN_PARAM_CONFIG_FILE;
        Map<String, Object> updatesMap = JSONUtil.convertValue(trainTask);
        YmlUtil.genParamConfig(updatesMap, trainParamTplPath, outputYamlPath);
    }


    public void genK8sTmpFile(EvalTask evalTask) {
        String modelTemplate = evalTask.getModelTemplate();
        String evalK8sTplName = ModelConstants.EVAL_K8S_TPL_CONF.get(ModelConstants.EVAL_OTHER_K8S_TPL);
        if (ModelConstants.QWEN_2_72B.equals(modelTemplate)) {
            evalK8sTplName = ModelConstants.EVAL_K8S_TPL_CONF.get(ModelConstants.EVAL_8GPU_K8S_TPL);
        }

        // 写入到输出文件
        String trainK8sTplPath = modelPathUtil.getTplDir(ModelConstants.OP_EVAL) + evalK8sTplName;
        String evalDirPath = modelPathUtil.getEvalDirPath(evalTask.getTaskId());
        String outputYamlPath = evalDirPath + "/" + ModelConstants.K8S_CONFIG_FILE;
        YmlUtil.genK8sConfig(evalTask.getTaskId(), trainK8sTplPath, outputYamlPath);

    }

    public void genParamFile(EvalTask evalTask) throws IOException {
        String modelName = evalTask.getModelName();

        String modelNameOrPath = ModelConstants.MODEL_CONF.get(modelName);
        evalTask.setModelNameOrPath(modelNameOrPath);
        evalTask.setTemplate("qwen");

        // 对于非基模模型 直接传入用户想要用来推理的已训练任务的taskid
        if (StrUtil.isEmpty(modelNameOrPath)) {
            evalTask.setModelNameOrPath(ModelConstants.SAVE_DIR + modelName);
        }
        evalTask.setSaveDir(ModelConstants.SAVE_DIR + evalTask.getTaskId() + "/eval");

        // 将lora路径进行转换，若不合并lora,该参数不会生效
        evalTask.setAdapterNameOrPath(ModelConstants.SAVE_DIR + evalTask.getLoraTaskId());


        boolean useLora = evalTask.getUseLora();
        String evalParamTplName = ModelConstants.EVAL_PARAM_TPL_CONF.get(ModelConstants.METHOD_OTHER);
        if (useLora) {
            evalParamTplName = ModelConstants.EVAL_PARAM_TPL_CONF.get(ModelConstants.METHOD_USER_LORA);
        }

        // 写入到输出文件
        String evalParamTplPath = modelPathUtil.getTplDir(ModelConstants.OP_EVAL) + evalParamTplName;
        String trainDirPath = modelPathUtil.getEvalDirPath(evalTask.getTaskId());
        String outputYamlPath = trainDirPath + "/" + ModelConstants.EVAL_PARAM_CONFIG_FILE;
        Map<String, Object> updatesMap = JSONUtil.convertValue(evalTask);
        YmlUtil.genParamConfig(updatesMap, evalParamTplPath, outputYamlPath);
    }

    public void genK8sTmpFile(InferenceTask inferTask) {
        String modelTemplate = inferTask.getModelTemplate();
        String inferK8sTplName = ModelConstants.INFER_K8S_TPL_CONF.get(modelTemplate);

        // 写入到输出文件
        String inferK8sTplPath = modelPathUtil.getTplDir(ModelConstants.OP_INFER) + inferK8sTplName;
        String inferDirPath = modelPathUtil.getInferenceDirPath(inferTask.getTaskId());
        String outputYamlPath = inferDirPath + "/" + ModelConstants.K8S_CONFIG_FILE;
        YmlUtil.genK8sConfig(inferTask.getTaskId(), inferK8sTplPath, outputYamlPath);
    }

    public void genParamFile(InferenceTask inferTask) throws IOException {
        String modelName = inferTask.getModelName();

        String modelNameOrPath = ModelConstants.MODEL_CONF.get(modelName);
        inferTask.setModelNameOrPath(modelNameOrPath);
        inferTask.setTemplate("qwen");

        // 对于非基模模型 直接传入用户想要用来推理的已训练任务的taskid
        if (StrUtil.isEmpty(modelNameOrPath)) {
            inferTask.setModelNameOrPath(ModelConstants.SAVE_DIR + modelName);
        }
        // 将lora路径进行转换，若不合并lora,该参数不会生效
        inferTask.setAdapterNameOrPath(ModelConstants.SAVE_DIR + inferTask.getLoraTaskId());

        boolean useLora = inferTask.getUseLora();
        String inferParamTplName = ModelConstants.INFER_PARAM_TPL_CONF.get(ModelConstants.METHOD_OTHER);
        if (useLora) {
            inferParamTplName = ModelConstants.INFER_PARAM_TPL_CONF.get(ModelConstants.METHOD_USER_LORA);
        }

        // 写入到输出文件
        String inferParamTplPath = modelPathUtil.getTplDir(ModelConstants.OP_INFER) + inferParamTplName;
        String inferDirPath = modelPathUtil.getInferenceDirPath(inferTask.getTaskId());
        String outputYamlPath = inferDirPath + "/" + ModelConstants.INFER_PARAM_CONFIG_FILE;
        Map<String, Object> updatesMap = JSONUtil.convertValue(inferTask);
        YmlUtil.genParamConfig(updatesMap, inferParamTplPath, outputYamlPath);
    }

    private String getRunEnv() {
        String arch = System.getProperty("os.arch");
        log.info("Current systemt arch is: {}", arch);
        if ("aarch64".equals(arch)) {
            return ModelConstants.ARM_ARCH;
        } else {
            return ModelConstants.X86_ARCH;
        }
    }
}
