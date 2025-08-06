package com.ctdi.cnos.llm.log.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.beans.log.MmModelMonitorStatistics;
import com.ctdi.cnos.llm.beans.log.model.req.MmModelMonitorIntfListReq;
import com.ctdi.cnos.llm.beans.log.model.req.MmModelMonitorIntfReq;
import com.ctdi.cnos.llm.beans.log.model.req.MmModelMonitorModelReq;
import com.ctdi.cnos.llm.beans.log.model.rsp.MmModelMonitorModelListVO;
import com.ctdi.cnos.llm.beans.log.model.rsp.MmModelMonitorModelVO;
import com.ctdi.cnos.llm.beans.train.trainTask.TrainTaskQuery;
import com.ctdi.cnos.llm.beans.train.trainTask.TrainTaskVO;
import com.ctdi.cnos.llm.cache.ctg.CtgCache;
import com.ctdi.cnos.llm.feign.train.TrainTaskServiceClientFeign;
import com.ctdi.cnos.llm.log.constant.ModelConstants;
import com.ctdi.cnos.llm.log.constant.ModelEnum;
import com.ctdi.cnos.llm.log.constant.TrainConstants;
import com.ctdi.cnos.llm.log.dao.MmModelMonitorIntfDao;
import com.ctdi.cnos.llm.log.dao.MmModelMonitorStatisticsDao;
import com.ctdi.cnos.llm.log.service.MmModelService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static com.ctdi.cnos.llm.log.constant.ModelConstants.*;

/**
 * @author caojunhao
 * @DATE 2024/7/4
 */
@Service
@RequiredArgsConstructor
public class MmModelServiceImpl implements MmModelService {

    @Autowired
    private MmModelMonitorStatisticsDao mmModelMonitorStatisticsDao;

    @Autowired
    private MmModelMonitorIntfDao intfDao;

    @Resource(name = "modelMonitorTaskExecutor")
    private ThreadPoolTaskExecutor modelMonitorTaskExecutor;

//    @Autowired
//    private CacheManager cacheManager;

    private final CtgCache ctgCache;

    @Resource
    private TrainTaskServiceClientFeign trainTaskClient;

    @ApiOperation(value = "日志概括")
    @Override
    public MmModelMonitorStatistics queryStatistics() {
        List<TrainTaskVO> taskList = queryMyModel(com.ctdi.cnos.llm.base.constant.TrainConstants.DEPLOY_BELONG_TOOL);
        if (taskList.size() == 0) {
            MmModelMonitorStatistics statistics = new MmModelMonitorStatistics();
            statistics.setTokenTotal(0L);
            statistics.setTokenInput(0L);
            statistics.setTokenOutput(0L);
            statistics.setIntfTotal(0L);
        }
        List<Long> taskIds = taskList.stream().map(TrainTaskVO::getId).collect(Collectors.toList());
        return mmModelMonitorStatisticsDao.queryLastStatistics(taskIds);
    }

    @Override
    public List<MmModelMonitorModelVO> queryModelRequest(MmModelMonitorModelReq req) throws ExecutionException, InterruptedException {
        if (req.getTaskId() == null) {
            return new ArrayList<>();
        }
        // 构建查询条件
        QueryParams queryParams = buildQueryParams(null, null == req.getModelCallType() ? ModelEnum.MODEL_CALL_TYPE_ALL.getCode() : req.getModelCallType(), req.getTaskId(), req.getStartTime(), req.getEndTime());

        // 开启线程
        List<Future<List<MmModelMonitorModelVO>>> futures = startThreads(req.getEndTime(), queryParams);

        return this.filterTime(futures, req.getStartTime(), req.getEndTime());
    }

    @Override
    public List<MmModelMonitorModelVO> queryModelRequestChart(MmModelMonitorIntfReq req) throws ExecutionException, InterruptedException {
        if (null == req.getTaskId()) {
            return new ArrayList<>();
        }
        // 构建查询条件
        QueryParams queryParams = buildQueryParams(null == req.getIntfCallType() ? ModelEnum.INTF_CALL_TYPE_ALL.getCode() : req.getIntfCallType(), null, req.getTaskId(), req.getStartTime(), req.getEndTime());

        //开启线程
        List<Future<List<MmModelMonitorModelVO>>> futures = startThreads(req.getEndTime(), queryParams);

        return this.filterTime(futures, req.getStartTime(), req.getEndTime());
    }

    @Override
    public Page<MmModelMonitorModelListVO> queryIntfPage(Page<MmModelMonitorModelListVO> page, MmModelMonitorIntfListReq req,String deployBelong) throws Exception {
        if (req.getIntfCallType() == null) {
            req.setIntfCallType(ModelEnum.INTF_CALL_TYPE_ALL.getCode());
        }
        req.setTaskId(req.getTaskId());

        //统计时间
        Page<MmModelMonitorModelListVO> rsp = intfDao.queryList(page, req);

        if (rsp.getRecords().size() > 0) {
            //缓存模型名称
            //翻译
            rsp.getRecords().forEach(item -> {
                if (item.getTaskId() != null) {
                    item.setTaskName(getTaskNameById(item.getTaskId(),deployBelong));
                }
                if (item.getIntfCallType() != null) {
                    if (item.getIntfCallType().equals(ModelEnum.INTF_CALL_TYPE_SUCCESS.getCode())) {
                        item.setIntfCallTypeStr(ModelEnum.INTF_CALL_TYPE_SUCCESS.getDesc());
                    } else if (item.getIntfCallType().equals(ModelEnum.INTF_CALL_TYPE_FAIL.getCode())) {
                        item.setIntfCallTypeStr(ModelEnum.INTF_CALL_TYPE_FAIL.getDesc());
                    }
                }
            });
        }
        return rsp;
    }

    /**
     * 查询自己的模型
     */
    private List<TrainTaskVO> queryMyModel(String deployBelong) {
        //查询自己的模型
        TrainTaskQuery trainTaskQuery = new TrainTaskQuery();
        trainTaskQuery.setStatus(TrainConstants.TRAIN_TASK_STATUS_COMPLETED);
        trainTaskQuery.setDeployBelong(deployBelong);
        List<TrainTaskVO> trainTaskVOS = trainTaskClient.queryList(trainTaskQuery);
        if (trainTaskVOS != null) {
            return trainTaskVOS;
        }
        return new ArrayList<>();
    }

    private List<Future<List<MmModelMonitorModelVO>>> startThreads(String endTime, QueryParams queryParams) throws ExecutionException, InterruptedException {
        final boolean isIntf = null == queryParams.getModelType();
        List<Future<List<MmModelMonitorModelVO>>> futures = new ArrayList<>();
        // 先查询今天的，不走缓存
        if (queryParams.getToday().equals(queryParams.getEndDay())) {
            Callable<List<MmModelMonitorModelVO>> todayTask = () -> {
                if (isIntf) {
                    return mmModelMonitorStatisticsDao.queryIntfRequest(formatterDay(queryParams.getToday()) + TIME_SUFFIX, endTime, queryParams.getTaskId(), queryParams.getCallType());
                } else {
                    return mmModelMonitorStatisticsDao.queryModelRequest(formatterDay(queryParams.getToday()) + TIME_SUFFIX, endTime, queryParams.getTaskId(), queryParams.getCallType());
                }
            };
            futures.add(modelMonitorTaskExecutor.submit(todayTask));
        }

        // 按天获取缓存
        for (int i = queryParams.getStartDay(); i <= queryParams.getEndDay(); i++) {
            if (queryParams.getToday().equals(i)) {
                break;
            }
            String queryDay = formatterDay(i);
            Callable<List<MmModelMonitorModelVO>> task = () -> {
                if (isIntf) {
                    return mmModelMonitorStatisticsDao.queryIntfRequest(queryDay + TIME_SUFFIX, queryDay + TIME_SUFFIX_END, queryParams.getTaskId(), queryParams.getCallType());
                } else {
                    return mmModelMonitorStatisticsDao.queryModelRequest(queryDay + TIME_SUFFIX, queryDay + TIME_SUFFIX_END, queryParams.getTaskId(), queryParams.getCallType());
                }
            };

            String cacheKey = getCacheKey(queryParams.getTaskId(), queryParams.getCallType(), queryDay);
            String cacheType = isIntf ? ModelConstants.MODEL_MONITOR_INTF_CACHE_KEY : ModelConstants.MODEL_MONITOR_MODEL_CACHE_KEY;
            List<MmModelMonitorModelVO> result = ctgCache.getCache(cacheType, cacheKey);

            if (result == null) {
                result = modelMonitorTaskExecutor.submit(task).get();
                ctgCache.set(cacheType, cacheKey, result, 60 * 60 * 24 * 7);
            }
            List<MmModelMonitorModelVO> finalResult = result;
            futures.add(modelMonitorTaskExecutor.submit(() -> finalResult));
        }

        return futures;
    }


    /**
     * 从缓存根据id获取name，没有的话重新查一次
     *
     * @param taskId
     * @return
     */
    private String getTaskNameById(Long taskId,String deployBelong) {
        String taskName = ctgCache.getCache(ModelConstants.TRAIN_TASK_CACHE_KEY, String.valueOf(taskId));
        if (taskName != null) {
            return taskName;
        }

        // 如果缓存中没有找到或者没有缓存，需要重新查询并刷新缓存
        TrainTaskQuery trainTaskQuery = new TrainTaskQuery();
        trainTaskQuery.setStatus(TrainConstants.TRAIN_TASK_STATUS_COMPLETED);
        trainTaskQuery.setDeployBelong(deployBelong);
        List<TrainTaskVO> trainTaskVOS = trainTaskClient.queryList(trainTaskQuery);
        if (trainTaskVOS != null && trainTaskVOS.size() > 0) {
            trainTaskVOS.forEach(task -> {
                ctgCache.set(ModelConstants.TRAIN_TASK_CACHE_KEY, String.valueOf(task.getId()), task.getName());
            });
            return trainTaskVOS.stream()
                    .filter(trainTaskVO -> trainTaskVO.getId().equals(taskId))
                    .findFirst()
                    .map(TrainTaskVO::getName)
                    .orElse("未知名称：" + taskId);
        }

        return "未知名称：" + taskId;  // 如果查询不到，返回null或者根据需要处理
    }

    /**
     * 时间筛选
     * sort
     *
     * @param futures
     * @param startTime
     * @param endTime
     */
    private List<MmModelMonitorModelVO> filterTime(List<Future<List<MmModelMonitorModelVO>>> futures, String startTime, String endTime) {
        List<MmModelMonitorModelVO> combinedResults = new ArrayList<>();
        for (Future<List<MmModelMonitorModelVO>> future : futures) {
            try {
                combinedResults.addAll(future.get());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        // 时间筛选
        combinedResults = combinedResults.stream()
                .filter(result -> {
                    try {
                        Date startDate = ModelConstants.formatter.parse(startTime);
                        Date endDate = ModelConstants.formatter.parse(endTime);
                        Date modelCallDate = result.getModelCallDate();

                        return modelCallDate.compareTo(startDate) >= 0 && modelCallDate.compareTo(endDate) <= 0;
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
        // 排序
        combinedResults.sort(Comparator.comparing(MmModelMonitorModelVO::getModelCallDate));
        return combinedResults;
    }

    private LocalDateTime getDateCondition(Integer modelCallDate) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDateTime;

        switch (modelCallDate) {
            case 1:
                startDateTime = now.toLocalDate().atStartOfDay();
                break;
            case 2:
                startDateTime = now.minusDays(1).toLocalDate().atStartOfDay();
                break;
            case 3:
                startDateTime = now.minusDays(2).toLocalDate().atStartOfDay();
                break;
            case 7:
                startDateTime = now.minusDays(6).toLocalDate().atStartOfDay();
                break;
            case 15:
                startDateTime = now.minusDays(14).toLocalDate().atStartOfDay();
                break;
            default:
                startDateTime = now.minusDays(modelCallDate).toLocalDate().atStartOfDay();
        }

        return startDateTime;
    }

    public Integer getDateInFormat(LocalDateTime startDateTime) {
        try {
            return getDateInFormat(startDateTime, "yyyyMMdd");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取日期，优先使用 传入的日期
     *
     * @param startDateTime 传入的日期
     * @param format
     * @return
     * @throws Exception
     */
    public Integer getDateInFormat(LocalDateTime startDateTime, String format) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return Integer.valueOf(startDateTime.format(formatter));
    }

    /**
     * 格式化日期
     *
     * @param queryDay
     * @return
     */
    private String formatterDay(int queryDay) {
        LocalDate date = LocalDate.parse(queryDay + "", inputFormatter);
        String outputDate = date.format(outputFormatter);
        return outputDate;
    }

    private String getCacheKey(Long taskId, Integer modelCallType, String day) {
        return taskId + "_" + modelCallType + "_" + day;
    }

    private QueryParams buildQueryParams(Integer callType, Integer modelType, Long taskId, String startTime, String endTime) {
        QueryParams queryParams = new QueryParams();
        queryParams.setCallType(callType);
        queryParams.setModelType(modelType);
        queryParams.setTaskId(taskId);
        queryParams.setToday(getDateInFormat(LocalDateTime.now()));
        queryParams.setStartDay(getDateInFormat(LocalDateTime.parse(startTime, localDateTimeFormatter)));
        queryParams.setEndDay(getDateInFormat(LocalDateTime.parse(endTime, localDateTimeFormatter)));
        return queryParams;
    }

    private static class QueryParams {
        private Integer callType;
        private Integer modelType;
        private Long taskId;
        private Integer today;
        private Integer startDay;
        private Integer endDay;

        // Getters and setters
        public Integer getCallType() {
            return callType;
        }

        public void setCallType(Integer callType) {
            this.callType = callType;
        }

        public Integer getModelType() {
            return modelType;
        }

        public void setModelType(Integer modelType) {
            this.modelType = modelType;
        }

        public Long getTaskId() {
            return taskId;
        }

        public void setTaskId(Long taskId) {
            this.taskId = taskId;
        }

        public Integer getToday() {
            return today;
        }

        public void setToday(Integer today) {
            this.today = today;
        }

        public Integer getStartDay() {
            return startDay;
        }

        public void setStartDay(Integer startDay) {
            this.startDay = startDay;
        }

        public Integer getEndDay() {
            return endDay;
        }

        public void setEndDay(Integer endDay) {
            this.endDay = endDay;
        }
    }
}

