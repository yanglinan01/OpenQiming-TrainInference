package com.ctdi.cnos.llm.train.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.ctdi.cnos.llm.base.constant.SystemConstant;
import com.ctdi.cnos.llm.base.constant.TrainConstants;
import com.ctdi.cnos.llm.beans.log.chat.ModelChatLog;
import com.ctdi.cnos.llm.beans.log.chat.ModelChatLogDTO;
import com.ctdi.cnos.llm.beans.log.chat.ModelChatLogVO;
import com.ctdi.cnos.llm.beans.meta.dictionary.DictionaryVO;
import com.ctdi.cnos.llm.beans.meta.model.Model;
import com.ctdi.cnos.llm.beans.meta.model.ModelVO;
import com.ctdi.cnos.llm.beans.reason.req.CustomerModelStartChatRequest;
import com.ctdi.cnos.llm.beans.train.IntentRecognition.IntentRecognizeReq;
import com.ctdi.cnos.llm.beans.train.IntentRecognition.IntentRecognizeResp;
import com.ctdi.cnos.llm.beans.train.IntentRecognition.IntentRecognizeVO;
import com.ctdi.cnos.llm.beans.train.deployTask.*;
import com.ctdi.cnos.llm.beans.train.trainTask.TaskGroup;
import com.ctdi.cnos.llm.beans.train.trainTask.TrainTask;
import com.ctdi.cnos.llm.beans.train.trainTask.TrainTaskVO;
import com.ctdi.cnos.llm.config.RemoteHostConfig;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.feign.log.ModelChatLogServiceClientFeign;
import com.ctdi.cnos.llm.feign.metadata.DictionaryServiceClientFeign;
import com.ctdi.cnos.llm.feign.metadata.ModelServiceClientFeign;
import com.ctdi.cnos.llm.system.user.entity.User;
import com.ctdi.cnos.llm.system.user.entity.UserVO;
import com.ctdi.cnos.llm.system.user.service.UserService;
import com.ctdi.cnos.llm.train.client.ApiClient;
import com.ctdi.cnos.llm.train.config.ApplicationConfig;
import com.ctdi.cnos.llm.train.dao.DeployTaskDao;
import com.ctdi.cnos.llm.train.dao.TaskGroupDao;
import com.ctdi.cnos.llm.train.dao.TrainTaskDao;
import com.ctdi.cnos.llm.train.service.DeployTaskService;
import com.ctdi.cnos.llm.train.service.TrainTaskService;
import com.ctdi.cnos.llm.util.StringUtils;
import com.ctdi.cnos.llm.utils.DataScopeUtil;
import com.dtflys.forest.Forest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;

/**
 * 部署任务(DeployTask)表服务实现类
 *
 * @author wangyb
 * @since 2024-07-01 14:16:01
 */
@Service("deployTaskService")
@RequiredArgsConstructor
@Slf4j
public class DeployTaskServiceImpl implements DeployTaskService {

    private final DeployTaskDao deployTaskDao;

    private final DictionaryServiceClientFeign dictionaryClient;

    private final ApiClient apiClient;

    private final ApplicationConfig config;

    private final ModelChatLogServiceClientFeign modelChatLogClient;


    private final ModelServiceClientFeign modelClient;

    private final TrainTaskService trainTaskService;

    private final UserService userService;

    private final TaskGroupDao taskGroupDao;

    private final TrainTaskDao taskDao;

    private final RemoteHostConfig remoteHostConfig;

    private static final String SOURCE = "训练任务部署";


    @Override
    public List<DeployTaskVO> queryList(DeployTaskVO vo) {
        //只查权限允许的
        String dataScope = DataScopeUtil.dataScopeSql("a", null);
        vo.setDataScopeSql(dataScope);
        List<DeployTaskVO> list = deployTaskDao.queryList(vo);
        this.convertDeployTaskList(list);
        return list;
    }

    /**
     * 意图识别查询部署
     *
     * @param vo
     * @return
     */
    @Override
    public List<DeployTaskVO> queryListByCategory(DeployTaskVO vo) {
        //只查权限允许的
        String dataScope = DataScopeUtil.dataProjectScopeSql("a", "project_space_id", null, vo.getProjectSpaceId());
        vo.setDataScopeSql(dataScope);
        List<DeployTaskVO> list = deployTaskDao.queryList(vo);
        this.convertDeployTaskList(list);
        return list;
    }


    @Override
    public Page<DeployTaskVO> queryList(Page<DeployTaskVO> page, DeployTaskVO vo) {
        //只查权限允许的
        String dataScope = DataScopeUtil.dataScopeSql("a", null);
        vo.setDataScopeSql(dataScope);
        Page<DeployTaskVO> resultPage = deployTaskDao.queryList(page, vo);
        this.convertDeployTaskList(resultPage.getRecords());
        return resultPage;
    }

    @Override
    public List<DeployTask> queryListByDeployUrlFromNacos(String deployUrl) {
        LambdaUpdateWrapper<DeployTask> wrapper = new LambdaUpdateWrapper<DeployTask>()
                .eq(DeployTask::getStatus, TrainConstants.DEPLOY_TASK_STATUS_COMPLETED)
                .like(DeployTask::getDeployUrl, deployUrl)
                .orderByDesc(DeployTask::getModifyDate);
        return deployTaskDao.selectList(wrapper);
    }


    @Override
    public DeployTaskVO queryById(Long deployTaskId) {
        DeployTaskVO vo = deployTaskDao.queryById(deployTaskId);
        if (ObjectUtil.isNotNull(vo)) {
            this.convertDeployTaskVO(vo);
        }
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(DeployTaskVO deployTask) {
        // 校验模型是否存在
        TrainTaskVO trainTaskVO = trainTaskService.queryById(deployTask.getModelId(), deployTask.getDeployBelong());
        Assert.isTrue(ObjectUtil.isNotNull(trainTaskVO), "模型找不到，可能已被删除！");
        Assert.isTrue(TrainConstants.TRAIN_TASK_STATUS_COMPLETED.equals(trainTaskVO.getStatus()), "模型训练任务未完成不可部署！");
        UserVO user = UserContextHolder.getUser();

        // 原web流程
        if (StringUtils.isEmpty(deployTask.getDeployBelong()) || "1".equals(deployTask.getDeployBelong())) {

            //校验部署情况
            this.checkDeploy(user.getId(), deployTask.getModelId(), deployTask.getDeployBelong());
            if (ObjectUtil.isNull(deployTask.getId())) {
                deployTask.setId(IdUtil.getSnowflakeNextId());
            }
            deployTask.setRegionCode(user.getRegionCode());
            //标记部署信息
            deployTask.setDeployTarget(trainTaskVO.getTrainTarget());

            deployTask.setStatus(TrainConstants.DEPLOY_TASK_STATUS_WAITING);
            if (StringUtils.isEmpty(deployTask.getSubmitStatus())) {
                deployTask.setSubmitStatus(SystemConstant.NO);
            }
            deployTask.setProjectSpaceId(trainTaskVO.getProjectId() != null ? trainTaskVO.getProjectId().toString() : null);

            boolean insert = SqlHelper.retBool(deployTaskDao.insert(deployTask));
            if (insert) {
                //初始化对话日志
                ModelChatLogDTO dto = new ModelChatLogDTO();
                dto.setId(IdUtil.getSnowflakeNextId());
                dto.setModelChatType(TrainConstants.SELF);
                dto.setModelChatId(deployTask.getModelId());
                dto.setSendTime(DateUtil.date());
                dto.setResponseTime(DateUtil.date());
                modelChatLogClient.add(dto);
                return Convert.toLong(deployTask.getId());
            }
            return null;
        } else if ("2".equals(deployTask.getDeployBelong())) {
            // 1220新增流程，外部调用
            // 区分为，项目空间和模型广场
            if (StringUtils.isEmpty(deployTask.getProjectSpaceId())) {
                // 模型广场
                //校验部署情况
//                this.checkDeploy(user.getId(), deployTask.getModelId(), deployTask.getDeployBelong());
                deployTask.setId(IdUtil.getSnowflakeNextId());
                deployTask.setRegionCode(user.getRegionCode());
                //标记部署信息
                deployTask.setDeployTarget(trainTaskVO.getTrainTarget());

                //封装下发任务参数
                DeployTaskSubmitParam submitParam = this.buildDeployParam(deployTask.getId(), trainTaskVO);
                String response = apiClient.submitDeployTask(submitParam, user.getId(), deployTask.getDeployTarget(), SOURCE);
                String resultStatus = Convert.toStr(JSONPath.eval(response, "$.status"), null);
                if (TrainConstants.SUCCESS_SUBMIT.equalsIgnoreCase(resultStatus)) {
                    deployTask.setStatus(TrainConstants.DEPLOY_TASK_STATUS_WAITING);
                } else {
                    String resultInfo = Convert.toStr(JSONPath.eval(response, "$.info"), null);
                    deployTask.setStatus(TrainConstants.DEPLOY_TASK_STATUS_FAILED);
                    deployTask.setResult(resultInfo);
                }
                //标记下发
                deployTask.setSubmitStatus(SystemConstant.YES);
                // todo 更换为保存导moudle表
                Model insertEntity = new Model();
                // 通知k8s使用的该id，保持一致才能callback
                insertEntity.setId(deployTask.getId());
                insertEntity.setName(deployTask.getModelName());
                // todo 字典
                insertEntity.setBelong("2");
                // 将 long 转为字符串，并截取最后四位
                String numberStr = String.valueOf(insertEntity.getId());
                numberStr = numberStr.substring(numberStr.length() - 4);
                insertEntity.setAlias(numberStr);
                insertEntity.setStatus("0");
                insertEntity.setMaxTokens(1000);
                insertEntity.setOptimizable("1");
                insertEntity.setTrainable("1");
                insertEntity.setReasonable("1");
                insertEntity.setDeployable("1");
                insertEntity.setPublishable("1");
                insertEntity.setDescription(deployTask.getDescription());
                insertEntity.setAccessParameters(deployTask.getAccessParameters());
                insertEntity.setModelId(deployTask.getModelId());
                insertEntity.setExperienceShape("0");
                insertEntity.setTrainTarget(deployTask.getDeployTarget());
                insertEntity.setExperienceImpl(3);
                insertEntity.setType("1");
                insertEntity.setDeployStatus(deployTask.getStatus());

                Map<String, Object> result = modelClient.add(insertEntity);
                result.get("success");
                boolean insert = (boolean) result.get("success");
                if (insert) {
                    //初始化对话日志
                    ModelChatLogDTO dto = new ModelChatLogDTO();
                    dto.setId(IdUtil.getSnowflakeNextId());
                    dto.setModelChatType(TrainConstants.SELF);
                    dto.setModelChatId(deployTask.getModelId());
                    dto.setSendTime(DateUtil.date());
                    dto.setResponseTime(DateUtil.date());
                    modelChatLogClient.add(dto);
                    return Convert.toLong(deployTask.getId());
                }
                return null;
            } else {
                // 项目空间
                //校验部署情况
//                this.checkDeploy(user.getId(), deployTask.getModelId(),deployTask.getDeployBelong());
                deployTask.setId(IdUtil.getSnowflakeNextId());
                deployTask.setRegionCode(user.getRegionCode());
                //标记部署信息
                deployTask.setDeployTarget(trainTaskVO.getTrainTarget());

                //封装下发任务参数
                DeployTaskSubmitParam submitParam = this.buildDeployParam(deployTask.getId(), trainTaskVO);
                String response = apiClient.submitDeployTask(submitParam, user.getId(), deployTask.getDeployTarget(), SOURCE);
                String resultStatus = Convert.toStr(JSONPath.eval(response, "$.status"), null);
                if (TrainConstants.SUCCESS_SUBMIT.equalsIgnoreCase(resultStatus)) {
                    deployTask.setStatus(TrainConstants.DEPLOY_TASK_STATUS_WAITING);
                } else {
                    String resultInfo = Convert.toStr(JSONPath.eval(response, "$.info"), null);
                    deployTask.setStatus(TrainConstants.DEPLOY_TASK_STATUS_FAILED);
                    deployTask.setResult(resultInfo);
                }
                //标记下发
                deployTask.setSubmitStatus(SystemConstant.YES);
                boolean insert = SqlHelper.retBool(deployTaskDao.insert(deployTask));
                if (insert) {
                    //初始化对话日志
                    ModelChatLogDTO dto = new ModelChatLogDTO();
                    dto.setId(IdUtil.getSnowflakeNextId());
                    dto.setModelChatType(TrainConstants.SELF);
                    dto.setModelChatId(deployTask.getModelId());
                    dto.setSendTime(DateUtil.date());
                    dto.setResponseTime(DateUtil.date());
                    modelChatLogClient.add(dto);
                    return Convert.toLong(deployTask.getId());
                }
                return null;
            }

        } else {
            return null;
        }
    }


    @Override
    public DeployTaskSubmitParam buildDeployParam(Long deployTaskId, TrainTaskVO trainTaskVO) {
        //封装下发任务参数
        DeployTaskSubmitParam submitParam = new DeployTaskSubmitParam();
        submitParam.setTaskId(deployTaskId);
        // 其他的 为基模的名称
        ModelVO modelVO = modelClient.queryById(trainTaskVO.getModelId());
        boolean useLora = false;
        if (CharSequenceUtil.equals(TrainConstants.TRAIN_TASK_METHOD_DICT_FULL, trainTaskVO.getMethod())) {
            //全参 训练ID
            submitParam.setModelName(String.valueOf(trainTaskVO.getId()));
        } else {
            if (modelVO != null) {
                submitParam.setModelName(modelVO.getAlias());
                DictionaryVO dictItemInfo = dictionaryClient.getDictItemInfo(TrainConstants.TRAIN_TASK_METHOD_DICT, trainTaskVO.getMethod());
                if (dictItemInfo != null) {
                    useLora = CharSequenceUtil.isNotBlank(dictItemInfo.getDictItemExtField1()) && dictItemInfo.getDictItemExtField1().toLowerCase().contains("lora");
                }
            }
        }
        submitParam.setUseLora(useLora);
        submitParam.setLoraTaskId(trainTaskVO.getId());
        submitParam.setQuantization(false);
        submitParam.setModelTemplate(Optional.ofNullable(modelVO).map(ModelVO::getAlias).orElse(null));
        return submitParam;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(DeployTaskVO deployTask) {
        LambdaUpdateWrapper<DeployTask> wrapper = new LambdaUpdateWrapper<DeployTask>()
                .eq(DeployTask::getId, Convert.toLong(deployTask.getId()))
                .set(DeployTask::getStatus, deployTask.getStatus())
                .set(CharSequenceUtil.isNotBlank(deployTask.getResult()), DeployTask::getResult, deployTask.getResult())
                .set(CharSequenceUtil.isNotBlank(deployTask.getDeployUrl()), DeployTask::getDeployUrl, deployTask.getDeployUrl())
                .set(CharSequenceUtil.isNotBlank(deployTask.getRegisterId()), DeployTask::getRegisterId, deployTask.getRegisterId())
                .set(CharSequenceUtil.isNotBlank(deployTask.getAgentStatus()), DeployTask::getAgentStatus, deployTask.getAgentStatus())
                .set(CharSequenceUtil.isNotBlank(deployTask.getRegisterStatus()), DeployTask::getRegisterStatus, deployTask.getRegisterStatus())
                .set(DeployTask::getModifyDate, DateUtil.date());
        return SqlHelper.retBool(deployTaskDao.update(null, wrapper));
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(Long id) {
        //远程删除部署镜像
        DeployTask deployTask = deployTaskDao.queryById(id);
        TrainTaskVO trainTaskVO = trainTaskService.queryById(deployTask.getModelId(), deployTask.getDeployBelong());
        Assert.notNull(deployTask, "当前任务不存在！");
        if (SystemConstant.YES.equals(deployTask.getSubmitStatus())) {
            if (TrainConstants.MODEL_TRAIN_TYPE_IR.equals(trainTaskVO.getType())) {
                IrDeployUnregisterIntentReq req = new IrDeployUnregisterIntentReq();
                req.setIntent_type(deployTask.getId().toString());
                String response = apiClient.unregisterIntent(req, UserContextHolder.getUserId());
                String statusResult = Convert.toStr(JSONPath.eval(response, "$.message"), null);
                String infoResult = Convert.toStr(JSONPath.eval(response, "$.data"), null);
                if (CharSequenceUtil.isNotEmpty(response) && TrainConstants.IR_SUCCESS_SUBMIT.equalsIgnoreCase(statusResult)) {
                    log.info("{}删除远程部署成功, {}", deployTask.getId(), infoResult);
                } else {
                    log.error("{}删除远程部署失败, {}", deployTask.getId(), infoResult);
                }
            } else {
                String response = apiClient.deleteDeployTask(deployTask.getId(), deployTask.getDeployTarget(), UserContextHolder.getUserId(), SOURCE);
                String statusResult = Convert.toStr(JSONPath.eval(response, "$.status"), null);
                String infoResult = Convert.toStr(JSONPath.eval(response, "$.info"), null);
                if (CharSequenceUtil.isNotEmpty(response) && TrainConstants.SUCCESS_SUBMIT.equalsIgnoreCase(statusResult)) {
                    log.info("{}删除远程部署成功, {}", deployTask.getId(), infoResult);
                } else {
                    log.error("{}删除远程部署失败, {}", deployTask.getId(), infoResult);
                }
            }
        }
        boolean flag = SqlHelper.retBool(deployTaskDao.deleteById(deployTask.getId()));
        return flag;
    }


    /**
     * 创建agent部署
     *
     * @param deployTask deployTask
     */
    private void createAgentDeploy(DeployTask deployTask) {
        String url = deployTask.getDeployUrl();
        Assert.isTrue(CharSequenceUtil.isNotBlank(url), "部署地址不能为空");

        //"http://11.11.8.14:238/1831661094859829248/v1/chat/completions";
        // 截取 http://11.11.8.14:238/
        String deployUrl = url.substring(0, url.indexOf("/", url.indexOf("//") + 2) + 1).trim();
        Assert.isTrue(CharSequenceUtil.isNotBlank(deployUrl), "部署地址格式错误,请检查格式是否为http://ip:port/xxx");

        JSONObject object = new JSONObject();
        object.put("task_id", deployTask.getId());
        object.put("model_name", Opt.ofNullable(trainTaskService.queryById(deployTask.getModelId(), deployTask.getDeployBelong())).map(TrainTaskVO::getName).get());
        object.put("employee_number", Opt.ofNullable(userService.queryById(deployTask.getCreatorId(), true)).map(UserVO::getEmployeeNumber).get());
        object.put("endpoint_url", deployUrl + "v1");
        try {
            String result = apiClient.createAgentDeploy(object, UserContextHolder.getUserId());
            log.info("智能体模型部署deployId: {}创建详情：【{}】", deployTask.getId(), result);
        } catch (Exception e) {
            log.error("智能体模型部署deployId: {}, 网络异常", deployTask.getId());
        }
    }


    /**
     * 删除agent部署
     *
     * @param deployTask deployTask
     */
    private void deleteAgentDeploy(DeployTask deployTask) {
        JSONObject object = new JSONObject();
        object.put("task_id", Convert.toStr(deployTask.getId()));
        object.put("model_name", Opt.ofNullable(trainTaskService.queryById(deployTask.getModelId(), deployTask.getDeployBelong())).map(TrainTaskVO::getName).get());
        try {
            String result = apiClient.deleteAgentDeploy(object, UserContextHolder.getUserId());
            log.info("智能体模型部署deployId: {}删除详情：【{}】", deployTask.getId(), result);
        } catch (Exception e) {
            log.info("智能体模型部署deployId: {}删除, 网络异常", deployTask.getId());
        }
    }

    @Override
    public List<Long> deleteExpiredDeployTask(Integer offsetDay) {
        List<Long> ids = new ArrayList<>();
        LambdaQueryWrapper<DeployTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeployTask::getAgentStatus, SystemConstant.NO); //未被智能体引用的模型
        queryWrapper.eq(DeployTask::getRegisterStatus, SystemConstant.NO); //未DCOOS注册的模型
        queryWrapper.eq(DeployTask::getStatus, TrainConstants.DEPLOY_TASK_STATUS_COMPLETED);
        List<DeployTask> deployTaskList = deployTaskDao.selectList(queryWrapper);
        if (CollUtil.isNotEmpty(deployTaskList)) {
            List<Long> deployIdList = deployTaskList.stream().map(DeployTask::getModelId).collect(Collectors.toList());
            ModelChatLogVO logVo = new ModelChatLogVO();
            logVo.setModelIdList(deployIdList);
            logVo.setOffsetDay(offsetDay);
            List<ModelChatLog> modelChatLogList = modelChatLogClient.queryLastSendTimeByModelId(logVo);

            if (CollUtil.isNotEmpty(modelChatLogList)) {
                List<Long> timeoutModelList = modelChatLogList.stream().map(ModelChatLog::getModelChatId).collect(Collectors.toList());
                List<DeployTask> timeoutDeployTask = deployTaskList.stream().filter(deploy -> CollUtil.contains(timeoutModelList, deploy.getModelId())).collect(Collectors.toList());
                timeoutDeployTask.forEach(deployTask -> {
                    TrainTaskVO trainTaskVO = trainTaskService.queryById(deployTask.getModelId(), deployTask.getDeployBelong());
                    String statusResult;
                    String infoResult;
                    String response;
                    if (TrainConstants.MODEL_TRAIN_TYPE_IR.equals(trainTaskVO.getType())) {
                        IrDeployUnregisterIntentReq req = new IrDeployUnregisterIntentReq();
                        req.setIntent_type(deployTask.getId().toString());
                        response = apiClient.unregisterIntent(req, UserContextHolder.getUserId());
                        statusResult = Convert.toStr(JSONPath.eval(response, "$.message"), null);
                        infoResult = Convert.toStr(JSONPath.eval(response, "$.data"), null);
                    } else {
                        //调用接口
                        response = apiClient.deleteDeployTask(deployTask.getId(), deployTask.getDeployTarget(), UserContextHolder.getUserId(), SOURCE);
                        statusResult = Convert.toStr(JSONPath.eval(response, "$.status"), null);
                        infoResult = Convert.toStr(JSONPath.eval(response, "$.info"), null);
                    }

                    if (CharSequenceUtil.isNotEmpty(response)
                            && (TrainConstants.SUCCESS_SUBMIT.equalsIgnoreCase(statusResult) || TrainConstants.IR_SUCCESS_SUBMIT.equalsIgnoreCase(statusResult))) {
                        log.info("系统移除超时部署成功：【{}】,信息：{}", deployTask.getId(), infoResult);
                        //获取近期未用的且智能体未引用的DeployTaskId
                        LambdaUpdateWrapper<DeployTask> updateWrapper = new LambdaUpdateWrapper<>();
                        updateWrapper.set(DeployTask::getStatus, TrainConstants.DEPLOY_TASK_STATUS_REMOVED);
                        updateWrapper.set(DeployTask::getResult, offsetDay + "天未用,被系统移除, 请重新部署");
                        updateWrapper.set(DeployTask::getModifierId, UserContextHolder.getUserId());
                        updateWrapper.set(DeployTask::getModifyDate, DateUtil.date());
                        updateWrapper.eq(DeployTask::getId, deployTask.getId());
                        updateWrapper.eq(DeployTask::getAgentStatus, SystemConstant.NO);
                        deployTaskDao.update(null, updateWrapper);
                        this.deleteAgentDeploy(deployTask);  //模型过期,删除智能体模型
                        this.deleteModelRegisterApi(deployTask.getId());  //模型过期,删除dcoos模型
                        //更新项目空间部署状态
                        if (StringUtils.isNotEmpty(deployTask.getProjectSpaceId())) {
                            TaskGroup taskGroup = TaskGroup.builder()
                                    .id(Long.valueOf(deployTask.getProjectSpaceId()))
                                    .deployStatus(TrainConstants.DEPLOY_TASK_STATUS_REMOVED)
                                    .build();
                            taskGroupDao.updateById(taskGroup);
                        }
                        ids.add(deployTask.getId());
                    } else {
                        log.error("系统移除超时部署失败：【{}】,信息：{}", deployTask.getId(), infoResult);
                    }
                });

            }
        }
        return ids;
    }


    public void convertDeployTaskList(List<DeployTaskVO> list) {
        Map<String, String> deployTaskStatus = dictionaryClient.getDictItemMap(TrainConstants.DEPLOY_TASK_STATUS_DICT);
        list.forEach(v -> v.setStatusName(deployTaskStatus.get(v.getStatus())));
    }

    @Override
    public boolean update(Wrapper<DeployTask> updateWrapper) {
        return SqlHelper.retBool(deployTaskDao.update(null, updateWrapper));
    }

    @Override
    public int callUpdateDeployTask(DeployTaskVO deployTaskVO) {
        LambdaUpdateWrapper<DeployTask> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(DeployTask::getStatus, deployTaskVO.getStatus())
                .set(DeployTask::getModifyDate, DateUtil.date())
                .set(DeployTask::getResult, deployTaskVO.getResult())
                .set(DeployTask::getDeployUrl, deployTaskVO.getDeployUrl());
        updateWrapper.eq(DeployTask::getId, deployTaskVO.getId());
        return deployTaskDao.update(null, updateWrapper);
    }

    /**
     * 定时任务限流发送
     */
    @Override
    public void callBackDeploySend() {
        //查找
        LambdaQueryWrapper<DeployTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeployTask::getStatus, TrainConstants.TRAIN_TASK_STATUS_WAITING);
        queryWrapper.orderByAsc(DeployTask::getCreateDate);
        queryWrapper.last("limit " + config.getMaxDeployTaskLimit());
        List<DeployTask> deployTasks = deployTaskDao.selectList(queryWrapper);
        //取当中未发送的发送
        List<DeployTask> noSendTask = deployTasks.stream().filter(o -> {
            return SystemConstant.NO.equals(o.getSubmitStatus());
        }).collect(Collectors.toList());
        for (DeployTask deployTask : noSendTask) {
            TrainTaskVO trainTaskVO = trainTaskService.queryById(deployTask.getModelId(), deployTask.getDeployBelong());
            if (TrainConstants.MODEL_TRAIN_TYPE_IR.equals(trainTaskVO.getType())) {
                //下发lora文件
                String result = apiClient.intentSync(trainTaskVO.getId(), deployTask.getCreatorId(), "GZ");
                String resultStatus = Convert.toStr(JSONPath.eval(result, "$.status"), null);
                if (TrainConstants.SUCCESS_SUBMIT.equalsIgnoreCase(resultStatus)) {
                    //封装下发任务参数
                    IrDeployRegisterIntentReq req = new IrDeployRegisterIntentReq();
                    req.setIntent_type(deployTask.getId().toString());
                    req.setModel(trainTaskVO.getId().toString());
                    req.setPrompt(trainTaskVO.getInstruction());
                    String response = apiClient.registerIntent(req, deployTask.getCreatorId());
                    resultStatus = Convert.toStr(JSONPath.eval(response, "$.message"), null);
                    if (TrainConstants.IR_SUCCESS_SUBMIT.equalsIgnoreCase(resultStatus)) {
                        deployTask.setStatus(TrainConstants.DEPLOY_TASK_STATUS_COMPLETED);
                    } else {
                        String resultInfo = Convert.toStr(JSONPath.eval(response, "$.data"), null);
                        deployTask.setStatus(TrainConstants.DEPLOY_TASK_STATUS_FAILED);
                        deployTask.setResult(resultInfo);
                    }
                } else {
                    String resultInfo = Convert.toStr(JSONPath.eval(result, "$.info"), null);
                    deployTask.setStatus(TrainConstants.DEPLOY_TASK_STATUS_FAILED);
                    deployTask.setResult(resultInfo);
                }
            } else {
                //封装下发任务参数
                DeployTaskSubmitParam submitParam = this.buildDeployParam(deployTask.getId(), trainTaskVO);
                String response = apiClient.submitDeployTask(submitParam, deployTask.getCreatorId(), deployTask.getDeployTarget(), SOURCE);
                String resultStatus = Convert.toStr(JSONPath.eval(response, "$.status"), null);
                if (TrainConstants.SUCCESS_SUBMIT.equalsIgnoreCase(resultStatus)) {
                    deployTask.setStatus(TrainConstants.DEPLOY_TASK_STATUS_WAITING);
                } else {
                    String resultInfo = Convert.toStr(JSONPath.eval(response, "$.info"), null);
                    deployTask.setStatus(TrainConstants.DEPLOY_TASK_STATUS_FAILED);
                    deployTask.setResult(resultInfo);
                }
            }

            //标记下发
            deployTask.setSubmitStatus(SystemConstant.YES);
            deployTask.setModifyDate(DateUtil.date());
            deployTaskDao.updateById(deployTask);
        }
    }

    @Override
    public void callBackDeployStatus() {
        LambdaQueryWrapper<DeployTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeployTask::getStatus, TrainConstants.DEPLOY_TASK_STATUS_WAITING)
                .eq(DeployTask::getSubmitStatus, SystemConstant.YES);
        queryWrapper.select(DeployTask::getId, DeployTask::getModelId, DeployTask::getDeployTarget, DeployTask::getCreatorId, DeployTask::getDeployUrl, DeployTask::getDeployBelong);
        List<DeployTask> list = deployTaskDao.selectList(queryWrapper);

        if (CollUtil.isNotEmpty(list)) {
            ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            list.forEach(task -> executorService.submit(() -> {
                try {
                    String response = apiClient.statusDeployTask(task.getId(), task.getDeployTarget(), UserContextHolder.getUserId(), SOURCE);
                    String resultStatus = Convert.toStr(JSONPath.eval(response, "$.status"), null);
                    String infoStatus = Convert.toStr(JSONPath.eval(response, "$.info"), "");

                    if (CharSequenceUtil.isNotBlank(resultStatus)) {
                        LambdaUpdateWrapper<DeployTask> updateWrapper = new LambdaUpdateWrapper<>();
                        String status = TrainConstants.INTERFACE_DEPLOY_STATUS_MAP.getStr(resultStatus);
                        if (CharSequenceUtil.equals(TrainConstants.DEPLOY_TASK_STATUS_COMPLETED, status) && StringUtils.isEmpty(task.getDeployUrl())) {
                            String deployUrl = apiClient.buildDeployReasonUrl(task.getDeployTarget(), task.getId());
                            task.setDeployUrl(deployUrl);
                            String result = handlePostDeployment(task);
                            if (CharSequenceUtil.isBlank(result)) {
                                task.setDeployUrl("");
                                status = TrainConstants.DEPLOY_TASK_STATUS_FAILED;
                                infoStatus = "模型推理, 连通性测试异常";
                            }
                        }

                        updateWrapper.set(DeployTask::getStatus, status)
                                .set(DeployTask::getModifyDate, DateUtil.date())
                                .set(DeployTask::getResult, infoStatus)
                                .set(DeployTask::getDeployUrl, task.getDeployUrl());
                        updateWrapper.eq(DeployTask::getId, task.getId());
                        deployTaskDao.update(null, updateWrapper);

                        //判断部署任务是否是任务组，如果是更新任务组部署状态
                        TrainTask trainTask = taskDao.selectById(task.getModelId());
                        if (trainTask != null && trainTask.getGroupId() != null) {
                            TaskGroup taskGroup = new TaskGroup();
                            taskGroup.setId(trainTask.getGroupId());
                            taskGroup.setDeployStatus(status);
                            taskGroupDao.updateById(taskGroup);
                        }
                    }
                } catch (Exception e) {
                    log.error("处理任务失败：", e);
                }
            }));

            executorService.shutdown();
        }

        // model分支
        // ModelVO queryParam = new ModelVO();
        // queryParam.setStatus("1");
        //  List<ModelVO> modelList = modelClient.queryList(queryParam);
        // if (CollUtil.isNotEmpty(modelList)) {
        //     ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        //    modelList.forEach(task -> executorService.submit(() -> {
        //         try {
        //             String response = apiClient.statusDeployTask(task.getId(), task.getTrainTarget(), UserContextHolder.getUserId(), SOURCE);
        //             String resultStatus = Convert.toStr(JSONPath.eval(response, "$.status"), null);
        // model没有信息存储
//                    String infoStatus = Convert.toStr(JSONPath.eval(response, "$.info"), "");

        //             if (CharSequenceUtil.isNotBlank(resultStatus)) {
        //                  LambdaUpdateWrapper<DeployTask> updateWrapper = new LambdaUpdateWrapper<>();
        //                String status = TrainConstants.INTERFACE_DEPLOY_STATUS_MAP.getStr(resultStatus);

        //                Model updateParam = new Model();
        //                 updateParam.setId(task.getId());
        //                 updateParam.setEndpoint(task.getEndpoint());
        //                 if (CharSequenceUtil.equals(TrainConstants.DEPLOY_TASK_STATUS_COMPLETED, status)) {
        //                     String deployUrl = apiClient.buildDeployReasonUrl(task.getTrainTarget(), task.getId());
        //                     task.setEndpoint(deployUrl);
        // 测试流程需要用到 id modelid url createid
        //                     DeployTask testTask = new DeployTask();
        //                      testTask.setId(task.getId());
        //                     testTask.setModelId(task.getModelId());
        //                     testTask.setCreatorId(task.getCreatorId());
        //                     String result = handlePostDeployment(testTask);
        //                     if (CharSequenceUtil.isBlank(result)) {
        //                         task.setEndpoint("");
        //                        status = TrainConstants.DEPLOY_TASK_STATUS_FAILED;
//     //                            infoStatus = "模型推理, 连通性测试异常";
        //                         updateParam.setStatus("1");
        //                    }
        //                    updateParam.setStatus("0");
        //                }
        //                updateParam.setDeployStatus(status);
        //                 modelClient.updateById(updateParam);
        //             }
        //         } catch (Exception e) {
        //             log.error("处理任务失败：", e);
        //         }
        //     }));

        //     executorService.shutdown();
        // }
    }


    /**
     * 测试模型能否推理
     *
     * @param task DeployTask
     * @return 测试结果
     */
    @Override
    public String handlePostDeployment(DeployTask task) {
        long startTime = System.currentTimeMillis();
        long maxWaitTime = config.getMaxTimeAfterDeploy(); // 2分钟的时间限制，单位：毫秒
        while (System.currentTimeMillis() - startTime <= maxWaitTime) {
            try {
                Thread.sleep(config.getSleepAfterDeploy()); // 部署完后休眠
                String result = modelChatTest(task);  // 测试模型是否可用
                return result;
            } catch (InterruptedException e) {
                log.error("线程被中断，无法完成任务同步：", e);
                Thread.currentThread().interrupt(); // 恢复线程的中断状态，以便上层逻辑能够感知到中断
            }
        }

        if (System.currentTimeMillis() - startTime > maxWaitTime) {
            log.warn("超出{}分钟时间限制，终止任务同步循环。", (maxWaitTime / 60000));
        }

        return null;
    }


    /**
     * 模型对话
     *
     * @param deployTask deployTask
     * @return 模型对话结果
     */
    public String modelChatTest(DeployTask deployTask) {
        CustomerModelStartChatRequest request = new CustomerModelStartChatRequest();
        request.setModel(deployTask.getId().toString());
        request.setTemperature(0.8F);
        request.setTop_p(0.8F);
        request.setStream(false);
        request.setMessages(CollUtil.newArrayList(new CustomerModelStartChatRequest.Message().setContent("测试")));
        String chatRequestBody = JSON.toJSONString(request);
        log.info("模型推理测试：url【{}】【{}】", deployTask.getDeployUrl(), chatRequestBody);
        return Opt.ofNullable(deployTask.getDeployUrl())
                .map(url -> {
                    try {
                        return Forest.post(url).contentTypeJson().addBody(chatRequestBody).executeAsString();
                    } catch (Exception e) {
                        log.error("模型连通性测试异常【{}】", e.getMessage());
                        return "";
                    }
                })
                .orElse("");
    }


    @Override
    public DeployTask querySimpleByRegisterId(Long registerId) {
        return deployTaskDao.selectOne(Wrappers.lambdaQuery(DeployTask.class).eq(DeployTask::getRegisterId, registerId));
    }


    public void convertDeployTaskVO(DeployTaskVO vo) {
        Map<String, String> deployTaskStatus = dictionaryClient.getDictItemMap(TrainConstants.DEPLOY_TASK_STATUS_DICT);
        if (vo.getStatus().equals(TrainConstants.DEPLOY_TASK_STATUS_FAILED)) {
            vo.setRemarks(TrainConstants.DEPLOY_TASK_STATUS_FAILED_REMARK + vo.getResult());
        } else {
            vo.setRemarks(getRemarkByStatus(vo.getStatus()));
        }
        vo.setStatusName(deployTaskStatus.get(vo.getStatus()));

    }

    private String getRemarkByStatus(String status) {
        switch (status) {
            case "deploying":
                return TrainConstants.DEPLOY_TASK_STATUS_DEPLOYING_REMARK;
            case "completed":
                return TrainConstants.DEPLOY_TASK_STATUS_COMPLETED_REMARK;
            case "waiting":
                return TrainConstants.DEPLOY_TASK_STATUS_WAITING_REMARK;
        }
        return "";
    }


    /**
     * 校验是否重复部署和超过部署数量
     *
     * @param userId  当前用户id
     * @param modelId 模型id
     */
    private void checkDeploy(Long userId, Long modelId, String deployBelong) {
        LambdaQueryWrapper<TrainTask> trainTask = new LambdaQueryWrapper<TrainTask>();
        trainTask.eq(TrainTask::getModelId, modelId);
        List<TrainTask> trainTaskList = taskDao.selectList(trainTask);
        if (CollUtil.isNotEmpty(trainTaskList)) {
            TrainTask task = trainTaskList.get(0);
            if (Objects.isNull(task.getGroupId())) {
                LambdaQueryWrapper<DeployTask> userDeploy = new LambdaQueryWrapper<DeployTask>()
                        .eq(DeployTask::getCreatorId, userId)
                        .eq(DeployTask::getDeployType, TrainConstants.DEPLOY_TYPE_TRAIN)
                        .eq(StringUtils.isNotEmpty(deployBelong), DeployTask::getDeployBelong, deployBelong);

                List<DeployTask> userDeployList = deployTaskDao.selectList(userDeploy);
                List<Long> deployIdList = userDeployList.stream().map(DeployTask::getId).collect(Collectors.toList());
                LambdaQueryWrapper<TrainTask> tt = new LambdaQueryWrapper<TrainTask>();
                tt.in(TrainTask::getModelId, deployIdList);
                List<TrainTask> ttList = taskDao.selectList(tt);
                List<TrainTask> filterList = ttList.stream().filter(o -> {
                    return Objects.isNull(o.getProjectId());
                }).collect(Collectors.toList());
                Assert.isFalse(filterList.size() >= 2, "每个用户最多部署2个模型, 请删除其他模型再试");
            }
        }

        LambdaQueryWrapper<DeployTask> userDeployModel = new LambdaQueryWrapper<DeployTask>()
                .eq(DeployTask::getCreatorId, userId)
                .eq(DeployTask::getModelId, modelId)
                .eq(DeployTask::getDeployType, TrainConstants.DEPLOY_TYPE_TRAIN)
                .eq(StringUtils.isNotEmpty(deployBelong), DeployTask::getDeployBelong, deployBelong);
        Long userDeployModelCount = deployTaskDao.selectCount(userDeployModel);
        Assert.isFalse(userDeployModelCount > 0, "当前模型已部署, 请勿重复部署！");

        LambdaQueryWrapper<DeployTask> deployModel = new LambdaQueryWrapper<DeployTask>()
                .eq(DeployTask::getModelId, modelId)
                .eq(DeployTask::getDeployType, TrainConstants.DEPLOY_TYPE_TRAIN)
                .eq(StringUtils.isNotEmpty(deployBelong), DeployTask::getDeployBelong, deployBelong);
        Long deployModelCount = deployTaskDao.selectCount(deployModel);
        Assert.isFalse(deployModelCount > 0, "当前模型已被其他人部署, 请勿重复部署！");

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String addModelRegisterApi(Long deployTaskId) {
        DeployTask deployTask = deployTaskDao.selectById(deployTaskId);
        if (null != deployTask) {
            //api注册
            Map<String, Object> map = MapUtil.newHashMap();
            map.put("url", config.getIntfRestfulServiceUrl() + "/" + Convert.toStr(deployTaskId)); //intf-restful 对外地址
            map.put("type", TrainConstants.POST);
            map.put("source", TrainConstants.TOOL_CHAIN);
            map.put("name", Opt.ofNullable(trainTaskService.queryById(deployTask.getModelId(), deployTask.getDeployBelong())).map(TrainTaskVO::getName).get() + Convert.toStr(deployTaskId));
            map.put("createBy", UserContextHolder.getUser().getEmployeeNumber());
            String response = apiClient.addModelRegisterApi(deployTask.getCreatorId(), map);
            log.info("注册接口返回【{}】", response);
            if (CharSequenceUtil.isNotBlank(response)) {
                String code = JSON.parseObject(response).getString("code");
                String msg = JSON.parseObject(response).getString("msg");
                if ("0".equals(code)) {
                    DeployTaskVO vo = new DeployTaskVO();
                    String registerId = JSON.parseObject(response).getString("data");
                    deployTask.setRegisterId(registerId);
//                    deployTask.setAgentStatus(SystemConstant.YES);
                    deployTask.setRegisterStatus(SystemConstant.YES);
                    BeanUtil.copyProperties(deployTask, vo);
                    //修改部署注册ID
                    this.updateById(vo);
                    log.info("新增模型api注册, deployTaskId: {}, registerId: {}", deployTaskId, registerId);
                    return registerId;
                } else {
                    log.info("系统异常: 该接口已存在, deployTaskId: {}", deployTaskId);
                    return msg;
                }
            } else {
                return "远程新增模型注册API没有返回信息";
            }
        }
        return "部署模型不存在";
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public String deleteModelRegisterApi(Long deployTaskId) {
        DeployTask deployTask = deployTaskDao.selectById(deployTaskId);
        if (null != deployTask) {
            String response = apiClient.deleteModelRegisterApi(deployTask.getCreatorId(), deployTask.getRegisterId());
            Integer code = Convert.toInt(JSONPath.eval(response, "$.code"));
            String msg = Convert.toStr(JSONPath.eval(response, "$.msg"));
            if (0 == code) {
                DeployTaskVO vo = new DeployTaskVO();
                deployTask.setRegisterId("-1");
                deployTask.setRegisterStatus(SystemConstant.NO);
                BeanUtil.copyProperties(deployTask, vo);
                this.updateById(vo);
                log.info("删除模型api注册, deployTaskId: {}", deployTaskId);
            } else {
                log.info("系统异常: 对不起接口ID不存在, deployTaskId: {}", deployTaskId);
            }
            return msg;
        }
        return "部署模型不存在";
    }


    @Override
    public String queryModelRegisterApi(int pageIndex, int pageSize) {
        return apiClient.queryModelRegisterApi(UserContextHolder.getUserId(), pageIndex, pageSize, UserContextHolder.getUser().getEmployeeNumber());
    }

    @Override
    public List<DeployTaskVO> queryAllDeployedModel(DeployTaskReq deployTaskReq) {
        DeployTask deployTask = new DeployTask();
        if (CharSequenceUtil.isBlank(deployTaskReq.getProjectSpaceId())) {
            deployTask.setCreatorId(UserContextHolder.getUserId());
        } else {
            deployTask.setProjectSpaceId(deployTaskReq.getProjectSpaceId());
        }
        List<DeployTaskVO> list = deployTaskDao.queryAllDeployedModel(deployTask);
        if (null != list) {
            list.forEach(item -> item.setEmployeeNumber(UserContextHolder.getUser().getEmployeeNumber()));
        }
        return list;
    }

    @Override
    public DeployTask queryCompletedTestDeployTask() {
        return deployTaskDao.queryCompletedTestDeployTask();
    }

    @Override
    public void updateBatchDeployTask(List<Long> ids, String before, String after) {
        deployTaskDao.updateBatchDeployTask(ids, before, after);
    }

    /**
     * 意图识别Lora模型通用接口
     *
     * @param req
     * @return
     */
    @Override
    public IntentRecognizeResp intentRecognize(IntentRecognizeVO req) {
        IntentRecognizeReq intentRecognizeReq = new IntentRecognizeReq();
        BeanUtil.copyProperties(req, intentRecognizeReq);

        Map param = req.getParams();
        if (CollectionUtil.isEmpty(param)) {
            param = new HashMap();
        }
        UserVO userVO = UserContextHolder.getUser();
        param.put("area", userVO.getRegionName());
        intentRecognizeReq.setParams(param);
        String result = apiClient.intentRecognize(intentRecognizeReq, userVO.getId());
        IntentRecognizeResp resp = JSONObject.parseObject(result, IntentRecognizeResp.class);

        try {
            if (resp.getCode() == 0 && "3".equals(req.getApplicationType())) {
                ModelChatLogDTO chatLog = new ModelChatLogDTO();
                chatLog.setModelChatId(Long.valueOf(req.getIntent_type()));
                chatLog.setModelChatType(Integer.valueOf(req.getApplicationType()));
                chatLog.setSessionId(req.getSessionId());
                chatLog.setSendUserId(UserContextHolder.getUserId());
                if (CollectionUtil.isNotEmpty(req.getMessages())) {
                    chatLog.setSendMessage(req.getMessages().get(0).getContent());
                }
                Map data = resp.getData();
                if (data.containsKey("choices")) {
                    JSONArray choices = (JSONArray) data.get("choices");
                    if (CollectionUtil.isNotEmpty(choices)) {
                        JSONObject choice = (JSONObject) choices.get(0);
                        JSONObject message = (JSONObject) choice.get("message");
                        String content = (String) message.get("content");
                        chatLog.setResponseMessage(content);
                    }
                }
                if (data.containsKey("usage")) {
                    JSONObject usage = (JSONObject) data.get("usage");
                    Integer completion_tokens = (Integer) usage.get("completion_tokens");
                    Integer prompt_tokens = (Integer) usage.get("prompt_tokens");
                    chatLog.setTokens(prompt_tokens, completion_tokens);
                }
                modelChatLogClient.add(chatLog);
            }
        } catch (Exception e) {
            log.error("写入聊天历史表失败：error:{},trace:{}", e.getMessage(), e.getStackTrace());
        }
        return resp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String versionEnable(TrainTaskVO taskVO) {
        log.info("-----------开始启用版本训练任务------------");
        //判断是否部署，若无部署只更改状态，若部署需要停止前版本并且启动当前版本，更新状态
        Long groupId = taskVO.getGroupId();
        if (ObjectUtil.isNotNull(groupId)) {
            //查找训练任务组
            TaskGroup taskGroup = taskGroupDao.selectById(groupId);
            //查询部署任务
            LambdaQueryWrapper<TrainTask> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(TrainTask::getGroupId, groupId);
            List<TrainTask> trainTaskList = taskDao.selectList(wrapper);
            if (ObjectUtil.isNotNull(taskGroup)
                    && StringUtils.isNotEmpty(taskGroup.getDeployStatus())) {
                if (CollUtil.isNotEmpty(trainTaskList)) {
                    for (TrainTask item : trainTaskList) {
                        if (Objects.equals(item.getId(), taskVO.getId())
                                && !TrainConstants.TRAIN_TASK_STATUS_COMPLETED.equals(item.getStatus())) {
                            return "当前训练未完成，无法开启！";
                        }
                    }
                    TrainTask oldTrainTask = trainTaskList.stream().filter(item ->
                            Objects.equals(item.getVersionEnable(), SystemConstant.YES)).findFirst().orElse(null);
                    boolean delFlag = true;
                    Long deployTaskId = null;
                    DeployTaskVO deployTaskVO = new DeployTaskVO();
                    if (oldTrainTask != null) {
                        Long oldTrainId = oldTrainTask.getId();
                        LambdaQueryWrapper<DeployTask> deployWrapper = Wrappers.lambdaQuery();
                        deployWrapper.eq(DeployTask::getModelId, oldTrainId);
                        List<DeployTask> deployTaskList = deployTaskDao.selectList(deployWrapper);
                        if (CollUtil.isNotEmpty(deployTaskList)) {
                            DeployTask deployTask = deployTaskList.get(0);
                            List<String> status = CollUtil.newArrayList(TrainConstants.DEPLOY_TASK_STATUS_DEPLOYING);
                            if (StrUtil.isNotBlank(deployTask.getStatus()) && status.contains(deployTask.getStatus())) {
                                return "正在部署中的任务无法被删除";
                            }
                            //删除老部署任务
                            deployTaskId = deployTask.getId();
                            log.info("-----------开始删除之前版本部署任务------------");
                            delFlag = this.deleteById(deployTaskId);
                            log.info("-----------结束删除之前版本部署任务------------");
                            //设置当前部署任务已经被引用
                            deployTaskVO.setAgentStatus(deployTask.getAgentStatus());
                        }
                    }
                    if (delFlag) {
                        //部署新任务
                        deployTaskVO.setModelId(taskVO.getId());
                        deployTaskVO.setDeployBelong(TrainConstants.DEPLOY_BELONG_TOOL);
                        if (deployTaskId != null) {
                            deployTaskVO.setId(deployTaskId);
                        }
                        log.info("-----------开始创建当前版本部署任务------------");
                        this.save(deployTaskVO);
                        log.info("-----------结束创建当前版本部署任务------------");
                        //修改当前任务组部署状态等待中
                        TaskGroup taskGroupDeploy = new TaskGroup();
                        taskGroupDeploy.setId(taskGroup.getId());
                        taskGroupDeploy.setDeployStatus(TrainConstants.DEPLOY_TASK_STATUS_WAITING);
                        taskGroupDao.updateById(taskGroupDeploy);
                    }
                }
            }
            //修改当前训练任务启用状态,组内其他任务未启用
            LambdaUpdateWrapper<TrainTask> versionWrapper = new LambdaUpdateWrapper<>();
            versionWrapper.set(TrainTask::getVersionEnable, SystemConstant.NO);
            versionWrapper.eq(taskVO.getGroupId() != null, TrainTask::getGroupId, taskVO.getGroupId());
            taskDao.update(null, versionWrapper);
            TrainTask trainTask = new TrainTask();
            trainTask.setId(taskVO.getId());
            trainTask.setVersionEnable(SystemConstant.YES);
            taskDao.updateById(trainTask);
        }
        log.info("-----------结束启用版本训练任务------------");
        return TrainConstants.HTTP_SUCCESS_SUBMIT;
    }


}
