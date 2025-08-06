package com.ctdi.cnos.llm.train.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONPath;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ctdi.cnos.llm.base.constant.TrainConstants;
import com.ctdi.cnos.llm.base.object.LambdaQueryWrapperX;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.base.service.BaseService;
import com.ctdi.cnos.llm.beans.meta.dictionary.DictionaryVO;
import com.ctdi.cnos.llm.beans.meta.model.ModelVO;
import com.ctdi.cnos.llm.beans.train.trainEval.TrainTaskEval;
import com.ctdi.cnos.llm.beans.train.trainEval.TrainTaskEvalSubmitParam;
import com.ctdi.cnos.llm.beans.train.trainEval.TrainTaskEvalVO;
import com.ctdi.cnos.llm.beans.train.trainTask.TrainTaskVO;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.feign.metadata.DictionaryServiceClientFeign;
import com.ctdi.cnos.llm.feign.metadata.ModelServiceClientFeign;
import com.ctdi.cnos.llm.train.client.ApiClient;
import com.ctdi.cnos.llm.train.dao.TrainTaskEvalDao;
import com.ctdi.cnos.llm.train.service.TrainTaskEvalService;
import com.ctdi.cnos.llm.train.service.TrainTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 训练任务c-eval 评估 数据操作服务类
 *
 * @author huangjinhua
 * @since 2024/09/04
 */
@RequiredArgsConstructor
@Service("trainTaskEvalService")
public class TrainTaskEvalServiceImpl extends BaseService<TrainTaskEvalDao, TrainTaskEval, TrainTaskEvalVO> implements TrainTaskEvalService {

    private final DictionaryServiceClientFeign dictionaryClient;

    private final TrainTaskService trainTaskService;

    private final ModelServiceClientFeign modelClient;

    private final ApiClient apiClient;

    @Override
    public void configureQueryWrapper(LambdaQueryWrapperX<TrainTaskEval> wrapper, QueryParam queryParam) {
        //TrainTaskEval filter = queryParam.getFilterDto(TrainTaskEval.class);
    }

    @Override
    public TrainTaskEvalVO getLastEvalByTrainTaskId(Long trainTaskId) {
        LambdaQueryWrapper<TrainTaskEval> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TrainTaskEval::getTrainTaskId, trainTaskId);
        queryWrapper.in(TrainTaskEval::getStatus, TrainConstants.TRAIN_TASK_EVAL_STATUS_COMPLETED, TrainConstants.TRAIN_TASK_EVAL_STATUS_EVALUATING);
        queryWrapper.orderByDesc(TrainTaskEval::getCreateDate);
        List<TrainTaskEval> trainTaskEvals = super.baseMapper.selectList(queryWrapper);
        if (CollUtil.isNotEmpty(trainTaskEvals)) {
            //取第一条
            TrainTaskEvalVO trainTaskEvalVO = super.convertToVo(trainTaskEvals.get(0), null);
            String label = dictionaryClient.getDictItemLabel(TrainConstants.MODEL_C_EVAL_STATUS_DICT, trainTaskEvalVO.getStatus());
            trainTaskEvalVO.setStatusName(label);
            return trainTaskEvalVO;
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(TrainTaskEval taskEval) {
        TrainTaskVO trainTaskVO = trainTaskService.queryById(taskEval.getTrainTaskId(),TrainConstants.DEPLOY_BELONG_TOOL);
        Assert.isTrue(ObjectUtil.isNotNull(trainTaskVO), "模型找不到，可能已被删除！");
        Assert.isTrue(TrainConstants.TRAIN_TASK_STATUS_COMPLETED.equals(trainTaskVO.getStatus()), "模型训练任务未完成不可进行C-eval评估！");
        taskEval.setId(IdUtil.getSnowflakeNextId());
        taskEval.setEvalTarget(trainTaskVO.getTrainTarget());
        TrainTaskEvalSubmitParam evalParam = this.buildEvalParam(taskEval, trainTaskVO);
        String response = apiClient.submitTrainEvalTask(evalParam, UserContextHolder.getUserId(), taskEval.getEvalTarget());
        String resultStatus = Convert.toStr(JSONPath.eval(response, "$.status"), null);
        String resultInfo = Convert.toStr(JSONPath.eval(response, "$.info"), null);
        if (TrainConstants.SUCCESS_SUBMIT.equalsIgnoreCase(resultStatus)) {
            taskEval.setStatus(TrainConstants.TRAIN_TASK_EVAL_STATUS_EVALUATING);
            return super.save(taskEval);
        } else {
            taskEval.setStatus(TrainConstants.TRAIN_TASK_EVAL_STATUS_FAILED);
            taskEval.setEvalInfo(resultInfo);
            return super.save(taskEval);
        }
    }


    private TrainTaskEvalSubmitParam buildEvalParam(TrainTaskEval trainTaskEval, TrainTaskVO trainTaskVO) {
        //封装下发任务参数
        TrainTaskEvalSubmitParam submitParam = new TrainTaskEvalSubmitParam();
        submitParam.setTaskId(trainTaskEval.getId());
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
        submitParam.setLoraTaskId(trainTaskEval.getTrainTaskId());
        submitParam.setModelTemplate(Optional.ofNullable(modelVO).map(ModelVO::getAlias).orElse(null));
        return submitParam;
    }
}
