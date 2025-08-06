/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.train.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.common.core.exception.ServiceException;
import com.ctdi.cnos.llm.base.constant.MetaDataConstants;
import com.ctdi.cnos.llm.base.constant.SystemConstant;
import com.ctdi.cnos.llm.base.constant.TrainConstants;
import com.ctdi.cnos.llm.base.object.StatType;
import com.ctdi.cnos.llm.beans.meta.cluster.ClusterVO;
import com.ctdi.cnos.llm.beans.meta.dataSet.DataSet;
import com.ctdi.cnos.llm.beans.meta.dataSet.DataSetAndPrInfoVO;
import com.ctdi.cnos.llm.beans.meta.dictionary.DictionaryVO;
import com.ctdi.cnos.llm.beans.meta.model.ModelVO;
import com.ctdi.cnos.llm.beans.meta.operationCenter.BarCharts;
import com.ctdi.cnos.llm.beans.train.deployTask.DeployTask;
import com.ctdi.cnos.llm.beans.train.deployTask.DeployTaskVO;
import com.ctdi.cnos.llm.beans.train.trainEval.TrainTaskEval;
import com.ctdi.cnos.llm.beans.train.trainTask.*;
import com.ctdi.cnos.llm.config.RemoteHostConfig;
import com.ctdi.cnos.llm.config.WebConfig;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.entity.HostInfo;
import com.ctdi.cnos.llm.feign.metadata.ClusterServiceClientFeign;
import com.ctdi.cnos.llm.feign.metadata.DataSetServiceClientFeign;
import com.ctdi.cnos.llm.feign.metadata.DictionaryServiceClientFeign;
import com.ctdi.cnos.llm.feign.metadata.ModelServiceClientFeign;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.system.user.entity.UserVO;
import com.ctdi.cnos.llm.train.client.ApiClient;
import com.ctdi.cnos.llm.train.config.ApplicationConfig;
import com.ctdi.cnos.llm.train.dao.*;
import com.ctdi.cnos.llm.train.service.DeployTaskService;
import com.ctdi.cnos.llm.train.service.TaskGroupService;
import com.ctdi.cnos.llm.train.service.TrainDatasetJoinService;
import com.ctdi.cnos.llm.train.service.TrainTaskService;
import com.ctdi.cnos.llm.util.FileUtils;
import com.ctdi.cnos.llm.util.StringUtils;
import com.ctdi.cnos.llm.utils.DataScopeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 训练任务 业务实现
 *
 * @author huangjinhua
 * @since 2024/5/15
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainTaskServiceImpl implements TrainTaskService {

    private final DictionaryServiceClientFeign dictionaryClient;
    private final ModelServiceClientFeign modelClient;
    private final DataSetServiceClientFeign dataSetClient;
    private final ClusterServiceClientFeign clusterClient;
    private final ApiClient apiClient;

    private final TrainTaskDao taskDao;
    private final DeployTaskDao deployDao;
    private final TrainTaskEvalDao taskEvalDao;

    private final ApplicationConfig config;

    private final TrainDatasetJoinService trainDatasetJoinService;

    private final RemoteHostConfig remoteHostConfig;

    private final DataSetServiceClientFeign dataSetServiceClientFeign;

    private final WebConfig webConfig;

    private final TaskGroupDao taskGroupDao;

    @Override
    public Page<TrainTaskVO> queryList(Page<TrainTaskVO> page, TrainTaskVO task) {
        taskDao.queryList(page, task);
        List<TrainTaskVO> records = page.getRecords();
        this.translate(false, records.toArray(new TrainTaskVO[0]));
        // 封装部署任务
        if (CollUtil.isNotEmpty(records)) {
            List<Long> trainIdList = records.stream().map(TrainTaskVO::getId).collect(Collectors.toList());
            Map<Long, List<DeployTaskVO>> deployMap = this.queryMapByTrainTasks(trainIdList, null, task.getDeployBelong());
            records.forEach(item -> item.setDeployTaskList(deployMap.get(item.getId())));
        }
        return page;
    }

    @Override
    public Page<TrainTaskVO> queryListByCategory(Page<TrainTaskVO> page, TrainTaskVO task) {
        taskDao.queryListByCategory(page, task);
        List<TrainTaskVO> records = page.getRecords();
        List<Long> ids = records.stream().map(o -> {
            return o.getId();
        }).collect(Collectors.toList());
        TrainTaskVO query = new TrainTaskVO();
        query.setIdsOrGroupIds(ids);
        List<TrainTaskVO> taskList = taskDao.queryList(task);

        this.translate(false, taskList.toArray(new TrainTaskVO[0]));
        // 封装部署任务
        if (CollUtil.isNotEmpty(taskList)) {
            List<Long> trainIdList = taskList.stream().map(TrainTaskVO::getId).collect(Collectors.toList());
            Map<Long, List<DeployTaskVO>> deployMap = this.queryMapByTrainTasks(trainIdList, null, task.getDeployBelong());
            taskList.forEach(item -> item.setDeployTaskList(deployMap.get(item.getId())));
        }
        for (TrainTaskVO vo : records) {
            if(TrainConstants.TASK_OR_GROUP_GROUP.equals(vo.getTaskOrGroup())){
                Map<String, String> deployTaskStatus = dictionaryClient.getDictItemMap(TrainConstants.DEPLOY_TASK_STATUS_DICT);
                vo.setStatusName(deployTaskStatus.get(vo.getStatus()));
            }
            for (TrainTaskVO o : taskList) {
                if (Objects.nonNull(o.getGroupId()) && Objects.equals(vo.getId(), o.getGroupId())) {
                    List<TrainTaskVO> children = vo.getChildren();
                    if (CollUtil.isEmpty(children)) {
                        children = new ArrayList<>();
                    }
                    o.setTaskOrGroup(TrainConstants.TASK_OR_GROUP_TASK);
                    String versionEnableName="";
                    if(SystemConstant.YES.equals(o.getVersionEnable())){
                        versionEnableName="(启用中)";
                    }
                    o.setStatusName(o.getStatusName()+versionEnableName);
                    children.add(o);
                    vo.setChildren(children);
                }
                if (Objects.equals(vo.getId(), o.getId())) {
                    o.setTaskOrGroup(vo.getTaskOrGroup());
                    BeanUtil.copyProperties(o, vo);
                }
            }
        }
        return page;
    }

    @Override
    public List<TrainTaskVO> queryList(TrainTaskVO task) {
        List<TrainTaskVO> taskList = taskDao.queryList(task);
        this.translate(false, taskList.toArray(new TrainTaskVO[0]));
        if (SystemConstant.YES.equals(task.getResourceOccupy()) && CollUtil.isNotEmpty(taskList)) {
            //资源使用情况
            List<Long> trainIdList = taskList.stream().map(TrainTaskVO::getId).collect(Collectors.toList());
            Map<Long, List<DeployTaskVO>> deployMap = this.queryMapByTrainTasks(trainIdList, CollUtil.newArrayList(TrainConstants.DEPLOY_TASK_STATUS_DEPLOYING, TrainConstants.TRAIN_TASK_STATUS_COMPLETED), task.getDeployBelong());
            taskList.forEach(item -> item.setDeployTaskList(deployMap.get(item.getId())));
        }
        return taskList;
    }


    @Override
    public TrainTaskVO queryById(Long id, String deployBelong) {
        log.info("TrainTaskServiceImpl -> queryById  " + " id :{}" + id + "; deployBelong :{}" + deployBelong);
        TrainTaskVO vo = taskDao.queryById(id);
        log.info("TrainTaskServiceImpl -> queryById  " + " vo :{}" + JSON.toJSONString(vo));
        if (vo != null) {
            this.translate(true, vo);
            // 封装部署任务
            Map<Long, List<DeployTaskVO>> deployMap = this.queryMapByTrainTasks(CollUtil.newArrayList(id), null, deployBelong);
            vo.setDeployTaskList(deployMap.get(id));
        }
        log.info("TrainTaskServiceImpl -> queryById  封装部署任务后" + " vo :{}" + JSON.toJSONString(vo));
        return vo;
    }

    @Override
    public TrainTask getTrainTaskById(Long id) {
        return taskDao.selectById(id);
    }

    @Override
    public Map<String, Long> statusCount(String isAll) {
        String dataScopeSql = null;
        if (!CharSequenceUtil.equals(isAll, SystemConstant.YES)) {
            dataScopeSql = DataScopeUtil.dataScopeSql("a", null);
        }
        List<TrainTaskVO> list = taskDao.statusCount(dataScopeSql);
        return Optional.ofNullable(list).map(value -> list.stream().collect(Collectors.toMap(TrainTaskVO::getStatus, TrainTaskVO::getStatusCount))).orElse(MapUtil.empty());
    }


    @Override
    public List<BarCharts> countTrainTaskGroupByProvince(StatType type) {
        LocalDate currentDate = LocalDate.now();
        LocalDate preMonthDate = getpreMonthDate(currentDate);
        return taskDao.countTrainTaskGroupByProvince(StatType.MONTH.equals(type) ? 0 : 1, preMonthDate, currentDate);
    }


    /**
     * 获取给定日期的前一个月的日期
     *
     * @param date 给定的日期
     * @return 前一个月的日期
     */
    public static LocalDate getpreMonthDate(LocalDate date) {
        // 使用 YearMonth 计算前一个月
        YearMonth preMonth = YearMonth.from(date).minusMonths(1);
        // 获取前一个月的最后一天
        LocalDate preMonthDate = preMonth.atEndOfMonth();
        return preMonthDate;
    }

    @Override
    public Long countByDataSetId(Long dataSetId, String status) {
        return taskDao.countByDataSetId(dataSetId, status);
    }


    @Override
    public boolean existByName(String name) {
        LambdaQueryWrapper<TrainTask> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Objects.nonNull(name), TrainTask::getName, name);
        return taskDao.exists(wrapper);
    }

    /**
     * 新增或者迭代
     *
     * @param taskVO 训练任务对象
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insertOrIterate(TrainTaskVO taskVO) throws Exception {
        String result=TrainConstants.HTTP_SUCCESS_SUBMIT;
        TrainTask task = BeanUtil.copyProperties(taskVO, TrainTask.class);
        this.handleInsertData(task);
        // 获取模型信息
        ModelVO model = modelClient.queryById(task.getModelId());
        // 模型接口标记
        task.setSubmitStatus(SystemConstant.NO);
        task.setTrainTarget(model.getTrainTarget());
        //如果是意图识别训练，数据集关系录入关系表,合并数据集，写入提示词，上传
        if (TrainConstants.MODEL_TRAIN_TYPE_IR.equals(task.getType())) {
            this.irDataSetMerge(taskVO, task);
        }
        //走新增还是迭代逻辑
        if (ObjectUtil.isNotNull(taskVO.getGroupId())) {
            //有组id则迭代
            result=this.iterate(taskVO, task);
        }
        taskDao.insert(task);
        return result;
    }

    @Override
    public boolean checkIterateCount(TrainTaskVO taskVO) {
        boolean flag = true;
        if (ObjectUtil.isNotNull(taskVO.getGroupId())) {
            LambdaQueryWrapper<TrainTask> trainWrapper = Wrappers.lambdaQuery();
            trainWrapper.eq(TrainTask::getGroupId, taskVO.getGroupId());
            List<TrainTask> trainTaskList = taskDao.selectList(trainWrapper);
            if (CollUtil.isNotEmpty(trainTaskList)) {
                if (trainTaskList.size() > 2) {
                    flag = false;
                }
            }
        }
        return flag;
    }

    @Override
    public boolean checkProjectGroupCount(TrainTaskVO taskVO) {
        boolean flag = true;
        List<String> typeList=new ArrayList<>();
        typeList.add(TrainConstants.MODEL_TRAIN_TYPE_LORA);
        typeList.add(TrainConstants.MODEL_TRAIN_TYPE_SFT);
        typeList.add(TrainConstants.MODEL_TRAIN_TYPE_RL_LORA);
        typeList.add(TrainConstants.MODEL_TRAIN_TYPE_QLORA);
        if (ObjectUtil.isNotNull(taskVO.getProjectId())
                && StringUtils.isNotEmpty(taskVO.getType()) && typeList.contains(taskVO.getType())) {
            TrainTaskVO vo=new TrainTaskVO();
            vo.setProjectId(taskVO.getProjectId());
            vo.setType(String.join(",",typeList));
            List<TrainTaskVO> voList=taskDao.queryListByCategory(vo);
            if (CollUtil.isNotEmpty(voList)) {
                if (voList.size() > 2) {
                    flag = false;
                }
            }
        }
        return flag;
    }

    /**
     * 迭代逻辑
     *
     * @param taskVO
     * @param task
     */
    private String iterate(TrainTaskVO taskVO, TrainTask task) {
        //判断组id在表里是否存在，不存在group表建新组，并且将原有改名v1，设置为启用，新建改名v2
        TaskGroup taskGroup = taskGroupDao.selectById(taskVO.getGroupId());
        if (Objects.isNull(taskGroup)) {
            //判断是否第一次迭代，因为第一次迭代没有创建组，所以找不到原训练任务，需要前端给
            TrainTask originTrainTask = taskDao.selectById(taskVO.getOriginTaskId());
            if (Objects.nonNull(originTrainTask)) {
                Date date = DateUtil.date();
                TaskGroup newTaskGroup = TaskGroup.builder()
                        .id(taskVO.getGroupId())
                        .name(originTrainTask.getName())
                        .regionCode(UserContextHolder.getUser().getRegionCode())
                        .projectId(originTrainTask.getProjectId())
                        .modelId(originTrainTask.getModelId())
                        .parentId(originTrainTask.getParentId())
                        .trainTarget(originTrainTask.getTrainTarget())
                        .type(originTrainTask.getType())
                        .classify(originTrainTask.getClassify())
                        .type(originTrainTask.getType())
                        .creatorId(UserContextHolder.getUserId())
                        .createDate(date)
                        .modifierId(UserContextHolder.getUserId())
                        .createDate(date)
                        .build();
                //查看部署状态
                LambdaQueryWrapper<DeployTask> deployWrapper = Wrappers.lambdaQuery();
                deployWrapper.eq(DeployTask::getModelId, taskVO.getOriginTaskId());
                List<DeployTask> deployTaskList = deployDao.selectList(deployWrapper);
                if (CollUtil.isNotEmpty(deployTaskList)) {
                    newTaskGroup.setDeployStatus(deployTaskList.get(0).getStatus());
                }
                taskGroupDao.insert(newTaskGroup);
                //原有的处理
                originTrainTask.setGroupId(taskVO.getGroupId());
                originTrainTask.setName(originTrainTask.getName() + "_v1");
                originTrainTask.setVersionEnable(SystemConstant.YES);
                originTrainTask.setVersionNum(1L);
                taskDao.updateById(originTrainTask);
                //新增的
                task.setName(newTaskGroup.getName() + "_v2");
                task.setGroupId(taskVO.getGroupId());
                task.setVersionNum(2L);
                task.setVersionEnable(SystemConstant.NO);
            }
        } else {
            //组id在表里存在则，改名最大版本号加一
            LambdaQueryWrapper<TrainTask> trainWrapper = Wrappers.lambdaQuery();
            trainWrapper.eq(TrainTask::getGroupId, taskVO.getGroupId());
            List<TrainTask> trainTaskList = taskDao.selectList(trainWrapper);
            if (CollUtil.isNotEmpty(trainTaskList)) {
                if(trainTaskList.size()>2){
                    return "迭代版本最多三个，请删除之前版本！";
                }
                Long MaxVersionNum = trainTaskList.stream()
                        .max(Comparator.comparingInt(o -> o.getVersionNum().intValue()))
                        .get().getVersionNum();
                Long currVersionMax = MaxVersionNum + 1;
                task.setName(taskGroup.getName() + "_v" + currVersionMax);
                task.setGroupId(taskVO.getGroupId());
                task.setVersionNum(currVersionMax);
                task.setVersionEnable(SystemConstant.NO);
            }
        }
        return TrainConstants.HTTP_SUCCESS_SUBMIT;
    }

    /**
     * 意图识别多文件合并上传处理
     */
    private void irDataSetMerge(TrainTaskVO taskVO, TrainTask task) throws Exception {
        //数据集默认任务id
        task.setDataSetId(task.getId());
        //新增关系表数据
        List<TrainDatasetJoin> trainDatasetJoins = new ArrayList<>();
        for (BigDecimal datasetId : taskVO.getDataSetIds()) {
            TrainDatasetJoin trainDatasetJoin = new TrainDatasetJoin();
            trainDatasetJoin.setId(IdUtil.getSnowflakeNextId());
            trainDatasetJoin.setTaskId(task.getId());
            trainDatasetJoin.setDataSetId(datasetId);
        }
        trainDatasetJoinService.insertBatch(trainDatasetJoins);
        //获取所有数据集的路径
        log.info("-----------------0.1.taskVO.getDataSetIds():{}-------------------",taskVO.getDataSetIds());
        DataSetAndPrInfoVO dataSetAndPrInfoVO = new DataSetAndPrInfoVO();
        dataSetAndPrInfoVO.setIds(taskVO.getDataSetIds());
        dataSetAndPrInfoVO.setProjectId(taskVO.getProjectId());
        List<DataSet> dataSetList = dataSetServiceClientFeign.queryListByVo(dataSetAndPrInfoVO);
        log.info("-----------------0.1.taskVO.dataSetList():{}-------------------",JSONObject.toJSONString(dataSetList));
        List<String> mergeFilePaths = dataSetList.stream().map(o -> {
            return o.getSavePath();
        }).collect(Collectors.toList());
        //提取文件,合并数据
        JSONArray allFileConversations = new JSONArray();
        for (String mergeFilePath : mergeFilePaths) {
            int lastIndex = mergeFilePath.lastIndexOf("/");
            log.info("-----------------0.1.irDataSetMerge.mergeFilePath:{}-------------------",mergeFilePath);
            log.info("-----------------0.2.irDataSetMerge.lastIndex:{}-------------------",lastIndex);
            String fileName = mergeFilePath.substring(lastIndex + 1);
            log.info("-----------------0.3.irDataSetMerge.fileName:{}-------------------",fileName);
            HostInfo host41 = remoteHostConfig.getHosts().get("host41").setFilename(fileName);
            List<String> lines = new FileUtils().downloadFileToLines(host41);
            log.info("-----------------0.irDataSetMerge.lines:{}-------------------",JSONObject.toJSONString(lines));
            JSONArray jsonArray = JSONArray.parseArray(String.join("", lines));
            allFileConversations.addAll(jsonArray);
        }
        log.info("-----------------1.irDataSetMerge.allFileConversations:{}-------------------",JSONObject.toJSONString(allFileConversations));
        //拼接写入提示词
        Set<String> set = new HashSet<>();
        for (Object obj : allFileConversations) {
            JSONObject conversationObj = (JSONObject) obj;
            JSONArray conversations = (JSONArray) conversationObj.get("conversations");
            String businessProduct = "";
            String intentClassification = "";
            for (Object o : conversations) {
                JSONObject jsonObject = (JSONObject) o;
                if ("businessProduct".equals(jsonObject.get("role"))) {
                    businessProduct = (String) jsonObject.get("content");
                }
                if ("intentClassification".equals(jsonObject.get("role"))) {
                    intentClassification = (String) jsonObject.get("content");
                }
                if (StringUtils.isNotEmpty(businessProduct) && StringUtils.isNotEmpty(intentClassification)) {
                    set.add(businessProduct + ":" + intentClassification);
                }
            }
        }
        //合并
        Map<String, List<String>> map = new HashMap<>();
        for (String entry : set) {
            String key = entry.split(":")[0];
            String value = entry.split(":")[1];
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(value); // 先添加，然后手动去重（如果需要）
        }
        int i = 1;
        StringBuilder sb = new StringBuilder(MetaDataConstants.INTENTION_RECOGNITION_INSTRUCTION_PREFIX);
        for (String key : map.keySet()) {
            sb.append("- 业务类型").append(i).append(": ").append(key).append(" ");
            for (String value : map.get(key)) {
                sb.append("  - ").append(value).append("    ");
            }
        }
        sb.append(MetaDataConstants.INTENTION_RECOGNITION_INSTRUCTION_SUFFIX);
        //写入文件上传
        for (Object obj : allFileConversations) {
            JSONObject conversationObj = (JSONObject) obj;
            JSONArray conversations = (JSONArray) conversationObj.get("conversations");
            for (Object o : conversations) {
                JSONObject jsonObject = (JSONObject) o;
                if ("instruction".equals(jsonObject.get("role"))) {
                    jsonObject.put("content", sb.toString());
                }
            }
        }
        log.info("-----------------2.irDataSetMerge.allFileConversations:{}-------------------",JSONObject.toJSONString(allFileConversations));
        String content = JSONObject.toJSONString(allFileConversations);
        log.info("-----------------3.irDataSetMerge.content:{}-------------------",content);
        String fileName = task.getId() + ".json";
        String mergeFilePath = webConfig.getDatasetUploadDir() + fileName;
        FileUtil.writeString(content, mergeFilePath, "UTF-8"); // 写入文件，指定编码为UTF-8
        task.setInstruction(sb.toString());
        MultipartFile multipartFile = new FileUtils().convertFileToMultipartFile(mergeFilePath);
        HostInfo host41 = remoteHostConfig.getHosts().get("host41").setFilePath(mergeFilePath).setFilename(fileName);
        HostInfo host112 = remoteHostConfig.getHosts().get("host112").setFilePath(mergeFilePath.replace(remoteHostConfig.getFilePrefix112(), "")).setFilename(fileName);
        new FileUtils().uploadRemoteFile(host41, multipartFile);
        new FileUtils().uploadRemoteFile(host112, multipartFile);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateById(TrainTask task) {
        task.setModifyDate(DateUtil.date());
        return taskDao.updateById(task);
    }


    @Override
    public int modifyStatusList() {
        LambdaQueryWrapper<TrainTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TrainTask::getSubmitStatus, SystemConstant.YES)
                .and(wrapper -> wrapper
                        .in(TrainTask::getStatus, CollUtil.newArrayList(TrainConstants.TRAIN_TASK_STATUS_TRAINING, TrainConstants.TRAIN_TASK_STATUS_WAITING)));
        queryWrapper.select(TrainTask::getId, TrainTask::getName, TrainTask::getStatus, TrainTask::getTrainTarget, TrainTask::getCreatorId, TrainTask::getCreateDate);
        List<TrainTask> trainTasks = taskDao.selectList(queryWrapper);
        int modify = 0;
        if (CollUtil.isNotEmpty(trainTasks)) {
            List<TrainTask> statusTaskList = apiClient.statusTrainTask(trainTasks);
            // 批量更新
            if (CollUtil.isNotEmpty(statusTaskList)) {
                List<List<TrainTask>> partition = CollUtil.split(statusTaskList, 200);
                for (List<TrainTask> itemList : partition) {
                    int count = taskDao.updateStatusBatch(itemList);
                    if (count == 1) {
                        modify += itemList.size();
                    }
                }
            }
        }
        return modify;
    }

    /**
     * 定时发送限流5条
     *
     * @return
     */
    @Override
    public int trainTaskSend() {
        // 查询等待状态时间排序前五的
        LambdaQueryWrapper<TrainTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TrainTask::getStatus, TrainConstants.TRAIN_TASK_STATUS_WAITING);
        queryWrapper.orderByAsc(TrainTask::getCreateDate);
        queryWrapper.last("limit " + config.getMaxTrainTaskLimit());
        List<TrainTask> trainTasks = taskDao.selectList(queryWrapper);
        //取当中未发送的发送
        log.info("---------------------查询等待状态时间排序前五的:{}---------------------------", trainTasks.size());
        List<TrainTask> noSendTask = trainTasks.stream().filter(o -> {
            return SystemConstant.NO.equals(o.getSubmitStatus());
        }).collect(Collectors.toList());
        log.info("---------------------取当中未发送的发送,{}---------------------------", noSendTask.size());
        for (TrainTask task : noSendTask) {
            // 更新数据集调用次数
            dataSetClient.callIncr(Convert.toStr(task.getDataSetId()));
            log.info("---------------------更新数据集调用次数---------------------------");
            // 下发任务
            TrainTaskVO taskVO = this.queryById(task.getId(), TrainConstants.DEPLOY_BELONG_TOOL);
            log.info("---------------------下发任务---------------------------");
            // 组装信息
            ModelVO model = modelClient.queryById(task.getModelId());
            Map<String, Object> requestMap = this.buildTaskParam(taskVO, model);
            if (MapUtil.isEmpty(requestMap)) {
                log.warn("任务无有效数据参数：{}", task.getId());
                return 0;
            }
            log.info("---------------------组装信息---------------------------");
            // 调用训练接口
            String responseBody = apiClient.submitTrainTask(requestMap, task.getCreatorId(), task.getTrainTarget());
            log.info("---------------------调用训练接口---------------------------");
            TrainTask submit = this.clientResponseWarp(responseBody, task.getId(), "submit");
            if (submit != null) {
                submit.setSubmitStatus(SystemConstant.YES);
                taskDao.updateById(submit);
            }
        }
        return noSendTask.size();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(TrainTask task) {
        int result = 0;
        if (task != null && task.getId() != null && SystemConstant.NO.equals(task.getSubmitStatus())) {
            return taskDao.deleteById(task.getId());
        }
        //判断已发送状态
        if (task != null && task.getId() != null && SystemConstant.YES.equals(task.getSubmitStatus())) {
            // 调用远程接口删除训练完成的模型
            String body = apiClient.deleteTrainTask(task.getId(), task.getCreatorId(), task.getTrainTarget());
            // 调用成功后终止任务
            TrainTask stop = this.clientResponseWarp(body, task.getId(), "delete");
            if (stop != null) {
                //删除部署任务
                LambdaQueryWrapper<DeployTask> queryDeployWrapper = new LambdaQueryWrapper<>();
                queryDeployWrapper.eq(DeployTask::getModelId, task.getId());
                List<DeployTask> deployTasks = deployDao.selectList(queryDeployWrapper);
                if (CollUtil.isNotEmpty(deployTasks)) {
                    deployTasks.forEach(deployTask -> {
                        String response = apiClient.deleteDeployTask(deployTask.getId(), deployTask.getDeployTarget(), UserContextHolder.getUserId(), "训练任务部署");
                        String statusResult = Convert.toStr(JSONPath.eval(response, "$.status"), null);
                        String infoResult = Convert.toStr(JSONPath.eval(response, "$.info"), null);
                        if (CharSequenceUtil.isNotEmpty(response) && TrainConstants.SUCCESS_SUBMIT.equalsIgnoreCase(statusResult)) {
                            log.info("{}删除远程部署成功, {}", deployTask.getId(), infoResult);
                        } else {
                            log.error("{}删除远程部署失败, {}", deployTask.getId(), infoResult);
                        }
                        deployDao.deleteById(deployTask.getId());
                    });
                }
                //queryDeployWrapper.eq(DeployTask::getCreatorId,UserContextHolder.getUserId());

                //删除评估任务
                taskEvalDao.delete(TrainTaskEval::getTrainTaskId, task.getId());
                return taskDao.deleteById(stop);
            }
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int stop(TrainTask task) {
        if (task != null && task.getId() != null) {
            // 调用远程接口终止训练完成的模型
            String body = apiClient.stopTrainTask(String.valueOf(task.getId()), task.getCreatorId(), task.getTrainTarget());
            // 调用成功后终止任务
            TrainTask stop = this.clientResponseWarp(body, task.getId(), "stop");
            if (stop != null) {
                return taskDao.updateById(stop);
            }
        }
        return 0;
    }

    @Override
    public Boolean checkUserTrainTaskCount(Long userId) {
        Long count = taskDao.selectCount(new LambdaQueryWrapper<TrainTask>()
                .eq(TrainTask::getCreatorId, userId)
                .isNull(TrainTask::getProjectId)
                .notIn(TrainTask::getStatus, TrainConstants.TRAIN_TASK_STATUS_FAILED));
        return TrainConstants.TRAIN_TASK_SINGLE_COUNT_LIMIT > count;
    }

    /**
     * 处理新增数据
     *
     * @param task 任务
     */
    private void handleInsertData(TrainTask task) {
        task.setStatus(TrainConstants.TRAIN_TASK_STATUS_WAITING);

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
        Map<String, String> modelTrainClassify = dictionaryClient.getDictItemMap(MetaDataConstants.MODEL_TRAIN_CLASSIFY_DICT);
        for (TrainTaskVO vo : tasks) {
            if (vo != null) {
                vo.setStatusName(trainTaskStatus.get(vo.getStatus()));
                vo.setMethodName(trainTaskMethod.get(vo.getMethod()));
                vo.setClassifyName(modelTrainClassify.get(vo.getClassify()));

                if (isDetail && CharSequenceUtil.isNotBlank(vo.getParamStr())) {
                    vo.setParam(JSON.parseArray(vo.getParamStr(), TrainTaskParamVO.class));
                }
                String trainingLoss = vo.getTrainingLoss();
                if (isDetail) {
                    vo.setTrainingLossData(buildTrainingLoss(trainingLoss));
                }
            }

        }
    }

    /**
     * 训练Loss数据，按照trainingLoss值进行组装
     *
     * @param trainingLoss 训练Loss
     * @return 组装前端展示的数据。
     */
    private JSONObject buildTrainingLoss(String trainingLoss) {
        if (CharSequenceUtil.isNotBlank(trainingLoss)) {
            List<String> loss = CharSequenceUtil.splitTrim(trainingLoss, ",");
            int[] x = new int[loss.size()];
            IntStream.range(0, loss.size()).forEach(i -> x[i] = i + 1);
            JSONObject trainingLossData = new JSONObject();
            trainingLossData.put("xAxisData", x);
            trainingLossData.put("seriesData", loss);
            return trainingLossData;
        }
        return null;
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


    /**
     * 组装下发任务数据
     *
     * @param task  任务信息
     * @param model 基础模型信息
     * @return Map<String, Object>
     */
    private Map<String, Object> buildTaskParam(TrainTaskVO task, ModelVO model) {
        // 组装数据
        Map<String, Object> requestMap = MapUtil.newHashMap(true);
        if (task == null) {
            return Collections.emptyMap();
        }
        if (model == null || CharSequenceUtil.isBlank(model.getAlias())) {
            log.warn("{}绑定关联的模型:{} 的别名为空！", task.getId(), task.getModelId());
            return Collections.emptyMap();
        }
        requestMap.put("model_name", model.getAlias());
        requestMap.put("task_id", Convert.toStr(task.getId()));
        // 获取数据集信息
        DataSet dataSet = dataSetClient.queryById(String.valueOf(task.getDataSetId()));
        if (dataSet != null && CharSequenceUtil.isNotBlank(dataSet.getSavePath())) {
            requestMap.put("dataset", FileUtil.getName(dataSet.getSavePath()));
        } else {
            requestMap.put("dataset", task.getId() + ".json");
            log.warn("{}绑定的数据集缺失文件路径！", task.getId());
        }
        if (CharSequenceUtil.isNotBlank(task.getParamStr())) {
            task.setParam(JSON.parseArray(task.getParamStr(), TrainTaskParamVO.class));
        }

        // 封装超参数据
        List<TrainTaskParamVO> paramList = task.getParam();
        if (CollUtil.isNotEmpty(paramList)) {
            paramList.forEach(param -> requestMap.put(param.getBeKey(), this.convertToObject(param.getType(), param.getDefaultValue())));
        }
        // 训练方法
        DictionaryVO dictItem = dictionaryClient.getDictItemInfo(TrainConstants.TRAIN_TASK_METHOD_DICT, task.getMethod());
        log.info("---------------1.dictionaryClient.getDictItemInfo:method:{},dictItem:{}--------------", task.getMethod(), dictItem);
        if (dictItem != null) {
            log.info("---------------2.dictionaryClient.getDictItemInfo:method:{},dictItem:{}--------------", task.getMethod(), JSONObject.toJSONString(dictItem));
            requestMap.put("strategy_train", dictItem.getDictItemExtField1());
        }

        //强化学习参数
        if (MetaDataConstants.MODEL_TRAIN_CLASSIFY_LEARN.equals(task.getClassify())) {
            //adapter_name_or_path是之前lora训练的id
            //model_template暂时等于model_name就行
            requestMap.put("adapter_name_or_path", Convert.toStr(task.getParentId()));
            requestMap.put("model_template", model.getAlias());
        }

        return requestMap;
    }

    /**
     * 提交任务、删除终止任务响应报文解析
     *
     * @param responseBody 响应的报文体
     * @param taskId       任务ID
     * @param operation    操作 （submit 提交任务，stop 停止任务，delete 删除任务）
     * @return TrainTask
     */
    private TrainTask clientResponseWarp(String responseBody, Long taskId, String operation) {
        if (CharSequenceUtil.isBlank(responseBody)) {
            return null;
        }
        TrainTask task = null;
        if ("submit".equalsIgnoreCase(operation)) {
            String submitStatus = Convert.toStr(JSONPath.eval(responseBody, "$.status"), null);
            String submitInfo = Convert.toStr(JSONPath.eval(responseBody, "$.info"), null);
            // 只有状态为success 才说明推送成功
            task = new TrainTask();
            task.setId(taskId);
            task.setModifyDate(DateUtil.date());
            if (!TrainConstants.SUCCESS_SUBMIT.equalsIgnoreCase(submitStatus)) {
                task.setStatus(TrainConstants.TRAIN_TASK_STATUS_FAILED);
                task.setResult(submitInfo);
            }
            // 记录是否已提交到k8s接口
            task.setSubmitStatus(SystemConstant.YES);
            return task;
        } else if ("stop".equalsIgnoreCase(operation) || "delete".equalsIgnoreCase(operation)) {
            String deleteStatus = Convert.toStr(JSONPath.eval(responseBody, "$.status"), null);
            String deleteInfo = Convert.toStr(JSONPath.eval(responseBody, "$.info"), null);
            if (TrainConstants.SUCCESS_SUBMIT.equalsIgnoreCase(deleteStatus)) {
                task = new TrainTask();
                task.setId(taskId);
                task.setStatus(TrainConstants.TRAIN_TASK_STATUS_FAILED);
                task.setResult(deleteInfo);
                return task;
            } else {
                throw new RuntimeException("大模型删除任务接口异常，任务ID：" + taskId + " 失败信息：" + deleteInfo);
            }
        }
        return null;
    }

    /**
     * 查询部署任务列表
     *
     * @param trainTaskIdList 训练任务ID列表
     * @return 部署任务列表
     */
    private Map<Long, List<DeployTaskVO>> queryMapByTrainTasks(List<Long> trainTaskIdList, List<String> deployStatus, String deployBelong) {
        LambdaQueryWrapper<DeployTask> deployQueryWrapper = new LambdaQueryWrapper<>();
        deployQueryWrapper.select(DeployTask::getId, DeployTask::getModelId, DeployTask::getDeployUrl, DeployTask::getStatus, DeployTask::getDeployTarget,
                DeployTask::getResult, DeployTask::getCreatorId, DeployTask::getCreateDate, DeployTask::getRegisterStatus, DeployTask::getDeployBelong,
                DeployTask::getProjectSpaceId, DeployTask::getDeployType, DeployTask::getSubmitStatus, DeployTask::getAgentStatus, DeployTask::getRegisterId);
        deployQueryWrapper.in(DeployTask::getModelId, trainTaskIdList);
        deployQueryWrapper.in(CollUtil.isNotEmpty(deployStatus), DeployTask::getStatus, deployStatus);
        deployQueryWrapper.eq(StringUtils.isNotEmpty(deployBelong), DeployTask::getDeployBelong, deployBelong);
        //添加权限
        /*String dataScope = DataScopeUtil.dataScopeSql(null, null);
        deployQueryWrapper.apply(CharSequenceUtil.isNotBlank(dataScope), "1=1" + dataScope);*/
        List<DeployTask> deployTasks = deployDao.selectList(deployQueryWrapper);
        if (CollUtil.isNotEmpty(deployTasks)) {
            //排序，超级管理员优先看到的是自己创建的部署任务
            List<DeployTask> deployTaskList = deployTasks.stream()
                    .sorted(Comparator.comparing(DeployTask::getCreateDate)
                            .thenComparing(deployTask -> deployTask.getCreatorId().equals(UserContextHolder.getUserId()) ? 0 : 1))
                    .collect(Collectors.toList());
            List<DeployTaskVO> deployVOList = BeanUtil.copyToList(deployTaskList, DeployTaskVO.class, new CopyOptions(DeployTaskVO.class, true));
            Map<Long, List<DeployTaskVO>> map = new HashMap<>();
            for (DeployTaskVO task : deployVOList) {
                task.setStatusName(dictionaryClient.getDictItemLabel(TrainConstants.DEPLOY_TASK_STATUS_DICT, task.getStatus()));
                OperateResult<ClusterVO> clusterVO = clusterClient.queryByCode(task.getDeployTarget());
                if (clusterVO.isSuccess() && clusterVO.getData() != null) {
                    task.setDeployTargetName(clusterVO.getData().getName());
                }
                map.computeIfAbsent(task.getModelId(), k -> new ArrayList<>()).add(task);
            }
            return map;
        }
        return Collections.emptyMap();
    }
}