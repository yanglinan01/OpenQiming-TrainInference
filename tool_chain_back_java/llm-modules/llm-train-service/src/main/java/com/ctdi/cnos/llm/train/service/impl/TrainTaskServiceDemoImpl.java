/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.train.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.base.constant.SystemConstant;
import com.ctdi.cnos.llm.base.constant.TrainConstants;
import com.ctdi.cnos.llm.beans.meta.model.ModelVO;
import com.ctdi.cnos.llm.beans.train.trainTaskDemo.*;
import com.ctdi.cnos.llm.beans.train.zhisuan.AiApplicationOrderV2Param;
import com.ctdi.cnos.llm.beans.train.zhisuan.AtomicAbilityFinishParam;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.feign.metadata.DictionaryServiceClientFeign;
import com.ctdi.cnos.llm.feign.metadata.ModelServiceClientFeign;
import com.ctdi.cnos.llm.system.user.entity.UserVO;
import com.ctdi.cnos.llm.train.client.ApiClient;
import com.ctdi.cnos.llm.train.dao.TrainTaskDemoDao;
import com.ctdi.cnos.llm.train.service.TrainTaskDemoService;
import com.ctdi.cnos.llm.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 训练任务demo 业务实现
 *
 * @author huangjinhua
 * @since 2024/9/20
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainTaskServiceDemoImpl implements TrainTaskDemoService {

    private final DictionaryServiceClientFeign dictionaryClient;
    private final ModelServiceClientFeign modelClient;

    private final TrainTaskDemoDao taskDao;

    private final ApiClient apiClient;


    @Override
    public Page<TrainTaskVO> queryList(Page<TrainTaskVO> page, TrainTaskVO task) {
        taskDao.queryList(page, task);
        List<TrainTaskVO> records = page.getRecords();
        this.translate(false, records.toArray(new TrainTaskVO[0]));
        return page;
    }

    @Override
    public List<TrainTaskVO> queryList(TrainTaskVO task) {
        List<TrainTaskVO> taskList = taskDao.queryList(task);
        this.translate(false, taskList.toArray(new TrainTaskVO[0]));
        return taskList;
    }

    @Override
    public List<AiCluster> queryAiClusterList(AiCluster aiCluster) {
        List<AiCluster> list = null;
        String aiClusterList = apiClient.getAiClusterList(UserContextHolder.getUserId());
        if (CharSequenceUtil.isNotBlank(aiClusterList)) {
            list = JSON.parseArray(aiClusterList, AiCluster.class);
            if (CollUtil.isNotEmpty(list) && aiCluster != null) {
                if (CharSequenceUtil.isNotBlank(aiCluster.getTitle())) {
                    list = list.stream()
                            .filter(item -> CharSequenceUtil.containsIgnoreCase(item.getTitle(), aiCluster.getTitle()))
                            .collect(Collectors.toList());
                }
            }
        }
        return list;
    }

    @Override
    public TrainTaskVO queryById(Long id) {
        TrainTaskVO vo = taskDao.queryById(id);
        if (vo != null) {
            this.translate(true, vo);
        }
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(TrainTask task) {
        this.handleInsertData(task);
        // 获取模型信息
        ModelVO model = modelClient.queryById(task.getModelId());
        // 模型接口标记
        task.setSubmitStatus(SystemConstant.YES);
        task.setTrainTarget(model.getTrainTarget());
        task.setStatus(TrainConstants.TRAIN_TASK_STATUS_TRAINING);
        taskDao.insert(task);
        return 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateById(TrainTask task) {
        task.setModifyDate(DateUtil.date());
        task.setModifierId(UserContextHolder.getUserId());
        return taskDao.updateById(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(TrainTask task) {
        LocalDateTime localDateTime=LocalDateTime.now().minusMinutes(2);

        // 创建一个空的UpdateWrapper，表示没有条件
        UpdateWrapper<TrainTask> updateWrapper = new UpdateWrapper<>();

        // 使用UpdateWrapper设置更新条件，这里你可以选择没有任何查询条件
        updateWrapper.set("status", TrainConstants.TRAIN_TASK_STATUS_COMPLETED);

        // 添加时间条件：只更新 create_date 距离当前时间超过 3 分钟的记录
        updateWrapper.le("create_date", localDateTime);

        int i=taskDao.update(null, updateWrapper);

        // 执行更新操作
        return i;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(TrainTask task) {
        return taskDao.deleteById(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int stop(TrainTask task) {
        task.setStatus(TrainConstants.TRAIN_TASK_STATUS_FAILED);
        return taskDao.updateById(task);
    }

    @Override
    public Boolean checkUserTrainTaskCount(Long userId) {
        Long count = taskDao.selectCount(new LambdaQueryWrapper<TrainTask>()
                .eq(TrainTask::getCreatorId, userId)
                .notIn(TrainTask::getStatus, TrainConstants.TRAIN_TASK_STATUS_FAILED));
        return TrainConstants.TRAIN_TASK_SINGLE_COUNT_LIMIT > count;
    }

    /**
     * 智算任务结束
     *
     * @param trainTaskList
     */
    private void sendAtomicAbilityFinish(List<TrainTask> trainTaskList) {
        if(CollUtil.isNotEmpty(trainTaskList)){
            AtomicAbilityFinishParam atomicAbilityFinishParam = new AtomicAbilityFinishParam();
            for (TrainTask trainTask : trainTaskList) {
                if(StringUtils.isNotEmpty(trainTask.getOrderId())){
                    atomicAbilityFinishParam.setOrderId(trainTask.getOrderId());
                    apiClient.atomicAbilityFinishTask(atomicAbilityFinishParam, trainTask.getCreatorId());
                }
            }
        }
    }

    /**
     * 处理新增数据
     *
     * @param task 任务
     */
    private void handleInsertData(TrainTask task) {
        UserVO userVO = UserContextHolder.getUser();
        if (Objects.isNull(task.getId())) {
            task.setId(IdUtil.getSnowflakeNextId());
        }
        if (CollUtil.isNotEmpty(task.getParam())) {
            task.setParamStr(JSON.toJSONString(task.getParam()));
        }

        if (Objects.nonNull(userVO)) {
            task.setRegionName(userVO.getRegionName());
            task.setRegionCode(userVO.getRegionCode());
            task.setCreatorId(userVO.getId());
            task.setModifierId(userVO.getId());
        }
        task.setCreateDate(DateUtil.date());
        task.setModifyDate(DateUtil.date());
    }

    private void translate(boolean isDetail, TrainTaskVO... tasks) {
        Map<String, String> trainTaskStatus = dictionaryClient.getDictItemMap(TrainConstants.TRAIN_TASK_STATUS_DICT);
        Map<String, String> trainTaskMethod = dictionaryClient.getDictItemMap(TrainConstants.TRAIN_TASK_METHOD_DICT);
        for (TrainTaskVO vo : tasks) {
            if (vo != null) {
                vo.setStatusName(trainTaskStatus.get(vo.getStatus()));
                vo.setMethodName(trainTaskMethod.get(vo.getMethod()));
                if (isDetail && CharSequenceUtil.isNotBlank(vo.getParamStr())) {
                    vo.setParam(JSON.parseArray(vo.getParamStr(), TrainTaskParamVO.class));
                }
            }

        }
    }

    private Object convertToObject(String type, Object value) {
        if (Objects.nonNull(value)) {
            switch (type) {
                case "int":
                    return Convert.toInt(value);
                case "double":
                    return Convert.toDouble(value);
                case "float":
                case "long":
                    return Convert.toBigDecimal(value);
                case "boolean":
                    return Convert.toBool(value);
                case "string":
                    return Convert.toStr(value);
                default:
                    return value;
            }
        }
        return null;
    }
}