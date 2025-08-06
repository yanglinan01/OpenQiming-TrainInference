package com.ctdi.cnos.llm.metadata.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ctdi.cnos.llm.base.constant.ActionType;
import com.ctdi.cnos.llm.base.constant.EvaluationConstant;
import com.ctdi.cnos.llm.base.constant.MetaDataConstants;
import com.ctdi.cnos.llm.base.constant.SystemConstant;
import com.ctdi.cnos.llm.base.constant.TrainConstants;
import com.ctdi.cnos.llm.base.mapper.BaseModelMapper;
import com.ctdi.cnos.llm.base.object.ExcelDataModel;
import com.ctdi.cnos.llm.base.object.LambdaQueryWrapperX;
import com.ctdi.cnos.llm.base.object.PageParam;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.base.object.SortingField;
import com.ctdi.cnos.llm.base.service.BaseService;
import com.ctdi.cnos.llm.beans.meta.dataSet.DataSet;
import com.ctdi.cnos.llm.beans.meta.evaluation.PrTestSetEvaluation;
import com.ctdi.cnos.llm.beans.meta.evaluation.PrTestSetEvaluationDTO;
import com.ctdi.cnos.llm.beans.meta.evaluation.PrTestSetEvaluationDetail;
import com.ctdi.cnos.llm.beans.meta.evaluation.PrTestSetEvaluationDetailVO;
import com.ctdi.cnos.llm.beans.meta.evaluation.PrTestSetEvaluationVO;
import com.ctdi.cnos.llm.beans.reason.req.CustomerModelStartChatRequest;
import com.ctdi.cnos.llm.beans.train.deployTask.DeployTaskRemoteParam;
import com.ctdi.cnos.llm.beans.train.deployTask.DeployTaskSubmitParam;
import com.ctdi.cnos.llm.beans.train.trainTask.TrainTask;
import com.ctdi.cnos.llm.beans.train.trainTask.TrainTaskVO;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.exception.MyRuntimeException;
import com.ctdi.cnos.llm.feign.train.DeployTaskServiceClientFeign;
import com.ctdi.cnos.llm.feign.train.TrainTaskServiceClientFeign;
import com.ctdi.cnos.llm.metadata.config.ApplicationConfig;
import com.ctdi.cnos.llm.metadata.dao.PrTestSetEvaluationDao;
import com.ctdi.cnos.llm.metadata.service.DataSetService;
import com.ctdi.cnos.llm.metadata.service.PrTestSetEvaluationDetailService;
import com.ctdi.cnos.llm.metadata.service.PrTestSetEvaluationService;
import com.ctdi.cnos.llm.response.ErrorCodeEnum;
import com.ctdi.cnos.llm.response.OperateResult;
import com.dtflys.forest.Forest;
import com.dtflys.forest.http.ForestResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 问答对测试数据集评估 数据操作服务类
 *
 * @author laiqi
 * @since 2024/09/03
 */
@RequiredArgsConstructor
@Service("prTestSetEvaluationService")
@Slf4j
public class PrTestSetEvaluationServiceImpl extends BaseService<PrTestSetEvaluationDao, PrTestSetEvaluation, PrTestSetEvaluationVO> implements PrTestSetEvaluationService {

    private final DataSetService dataSetService;

    private final TrainTaskServiceClientFeign trainTaskServiceClientFeign;

    private final DeployTaskServiceClientFeign deployTaskServiceClientFeign;

    private final PrTestSetEvaluationDetailService prTestSetEvaluationDetailService;

    private final PrTestSetEvaluationDao prTestSetEvaluationDao;

    private final ApplicationConfig config;


    /**
     * 调用身份
     */
    private static final String SOURCE = "数据集评估";

    @Override
    public void configureQueryWrapper(LambdaQueryWrapperX<PrTestSetEvaluation> wrapper, QueryParam queryParam) {
        PrTestSetEvaluation filter = queryParam.getFilterDto(PrTestSetEvaluation.class);
        // 根据模型任务ID、数据集ID进行过滤
        wrapper.eqIfPresent(PrTestSetEvaluation::getModelTaskId, filter.getModelTaskId());
        wrapper.eqIfPresent(PrTestSetEvaluation::getDataSetId, filter.getDataSetId());
        wrapper.eqIfPresent(PrTestSetEvaluation::getDeployStatus, filter.getDeployStatus());
        wrapper.eqIfPresent(PrTestSetEvaluation::getSendStatus, filter.getSendStatus());
        if (queryParam.getSortingFields() != null && !queryParam.getSortingFields().isEmpty()) {
            for (SortingField sortingField : queryParam.getSortingFields()) {
                if (sortingField.getFieldName() == null) continue;
                switch (sortingField.getFieldName()) {
                    case "create_date": // 处理 createTime 排序
                        if (sortingField.getAsc()) {
                            wrapper.orderByAsc(PrTestSetEvaluation::getCreateDate);
                        } else {
                            wrapper.orderByDesc(PrTestSetEvaluation::getCreateDate);
                        }
                }
            }
        }
    }

    @Override
    protected List<PrTestSetEvaluationVO> convertToVoList(List<PrTestSetEvaluation> modelList, BaseModelMapper<PrTestSetEvaluationVO, PrTestSetEvaluation> modelMapper) {
        List<PrTestSetEvaluationVO> results = super.convertToVoList(modelList, modelMapper);
        Set<Long> modelTaskIds = results.stream().map(PrTestSetEvaluationVO::getModelTaskId).collect(Collectors.toSet());
        Set<Long> dataSetIds = results.stream().map(PrTestSetEvaluationVO::getDataSetId).collect(Collectors.toSet());
        Map<Long, TrainTaskVO> trainTaskMap = modelTaskIds.stream()
                .map(trainTaskServiceClientFeign::detail)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(TrainTaskVO::getId, Function.identity(), (existing, replacement) -> existing));
        Map<Long, DataSet> dataSetMap = dataSetIds.stream()
                .map(dataSetId -> dataSetService.queryById(String.valueOf(dataSetId)))
                .filter(Objects::nonNull)
                .filter(dataSet -> dataSet.getId() != null)
                .collect(Collectors.toMap(dataSet -> dataSet.getId().longValue(), Function.identity(), (existing, replacement) -> existing));
        for (PrTestSetEvaluationVO result : results) {
            result.setModelTaskName(Opt.ofNullable(trainTaskMap.get(result.getModelTaskId())).map(TrainTaskVO::getName).get());
            result.setDataSetName(Opt.ofNullable(dataSetMap.get(result.getDataSetId())).map(DataSet::getDataSetName).get());
        }
        return results;
    }

    @Override
    protected PrTestSetEvaluationVO convertToVo(PrTestSetEvaluation model, BaseModelMapper<PrTestSetEvaluationVO, PrTestSetEvaluation> modelMapper) {
        PrTestSetEvaluationVO result = super.convertToVo(model, modelMapper);
        result.setModelTaskName(Opt.ofNullable(trainTaskServiceClientFeign.detail(model.getModelTaskId())).map(TrainTaskVO::getName).get());
        result.setDataSetName(Opt.ofNullable(dataSetService.queryById(String.valueOf(model.getDataSetId()))).map(DataSet::getDataSetName).get());
        return result;
    }

    @Override
    public ExcelDataModel export(Long id) {
        PrTestSetEvaluationVO prTestSetEvaluationVo = queryById(id, true);
        Assert.notNull(prTestSetEvaluationVo, ErrorCodeEnum.INVALID_DATA_MODEL.getErrorMessage());
        TrainTaskVO trainTaskVO = trainTaskServiceClientFeign.detail(prTestSetEvaluationVo.getModelTaskId());
        Assert.notNull(trainTaskVO, ErrorCodeEnum.INVALID_DATA_MODEL.getErrorMessage());
        DataSet dataSet = dataSetService.queryById(String.valueOf(prTestSetEvaluationVo.getDataSetId()));
        Assert.notNull(dataSet, ErrorCodeEnum.INVALID_DATA_MODEL.getErrorMessage());
        String fileName = trainTaskVO.getName() + "_" + dataSet.getDataSetName() + ".xls";
        QueryParam queryParam = new QueryParam();
        queryParam.setFilterMap(MapUtil.of(PrTestSetEvaluationDetail.Fields.testSetEvaluationId, id));
        List<PrTestSetEvaluationDetailVO> rows = prTestSetEvaluationDetailService.queryList(queryParam);
        Map<String, String> headerAlias = PrTestSetEvaluationDetailVO.NORMAL_HEADER_ALIAS;
        if (CharSequenceUtil.equals(EvaluationConstant.TEST_SET_EVALUATION_TYPE_LEAN, prTestSetEvaluationVo.getType())) {
            headerAlias = PrTestSetEvaluationDetailVO.ENHANCED_HEADER_ALIAS;
        }
        return new ExcelDataModel(fileName, headerAlias, rows);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insert(PrTestSetEvaluation prTestSetEvaluation) {
        Assert.notNull(prTestSetEvaluation.getModelTaskId(), "模型信息为空！");
        // 根据模型ID获取训练任务
        TrainTask trainTask = trainTaskServiceClientFeign.getTrainTaskById(prTestSetEvaluation.getModelTaskId());
        Assert.notNull(trainTask, "找不到模型！");
        Assert.isTrue(TrainConstants.TRAIN_TASK_STATUS_COMPLETED.equals(trainTask.getStatus()), "模型训练任务未完成不可进行数据集评估！");
        prTestSetEvaluation.setId(IdUtil.getSnowflakeNextId());
        prTestSetEvaluation.setDeployTarget(trainTask.getTrainTarget());
        TrainTaskVO trainTaskVO = new TrainTaskVO();
        BeanUtil.copyProperties(trainTask, trainTaskVO);
        //todo 临时添加测试集评估数量限制,现版本页面添加时不做实际逻辑
        if (false) {
            DeployTaskSubmitParam param = deployTaskServiceClientFeign.buildDeployParam(prTestSetEvaluation.getId(), trainTaskVO);
            DeployTaskRemoteParam submitParam = new DeployTaskRemoteParam();
            submitParam.setParam(param)
                    .setUserId(UserContextHolder.getUserId())
                    .setDeployTarget(prTestSetEvaluation.getDeployTarget())
                    .setSource(SOURCE);
            String response = deployTaskServiceClientFeign.submitDeployTask(submitParam);
            String resultStatus = Convert.toStr(JSONPath.eval(response, "$.status"), null);
            if (TrainConstants.SUCCESS_SUBMIT.equalsIgnoreCase(resultStatus)) {
                prTestSetEvaluation.setStatus(MetaDataConstants.DATA_SET_EVALUATION_STATUS_WAITING);
                prTestSetEvaluation.setDeployStatus(TrainConstants.DEPLOY_TASK_STATUS_WAITING);
            } else {
                String resultInfo = Convert.toStr(JSONPath.eval(response, "$.info"), null);
                prTestSetEvaluation.setDeployStatus(TrainConstants.DEPLOY_TASK_STATUS_FAILED);
                prTestSetEvaluation.setStatus(MetaDataConstants.DATA_SET_EVALUATION_STATUS_FAILED);
                prTestSetEvaluation.setResult(resultInfo);
            }
            return this.save(prTestSetEvaluation);
        }
        return this.save(prTestSetEvaluation);
    }


    @Override
    public int callbackDeployEvalStatus() {
        int count = 0;
        LambdaQueryWrapper<PrTestSetEvaluation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PrTestSetEvaluation::getDeployStatus, TrainConstants.DEPLOY_TASK_STATUS_WAITING);
        queryWrapper.eq(PrTestSetEvaluation::getStatus, MetaDataConstants.DATA_SET_EVALUATION_STATUS_WAITING);
        queryWrapper.eq(PrTestSetEvaluation::getSendStatus, SystemConstant.YES);
        queryWrapper.orderByAsc(PrTestSetEvaluation::getCreateDate);
        List<PrTestSetEvaluation> evaluationList = prTestSetEvaluationDao.selectList(queryWrapper);
        if (CollUtil.isNotEmpty(evaluationList)) {
            for (PrTestSetEvaluation evaluation : evaluationList) {
                DeployTaskRemoteParam param = new DeployTaskRemoteParam()
                        .setDeployTaskId(evaluation.getId())
                        .setDeployTarget(evaluation.getDeployTarget())
                        .setSource(SOURCE)
                        .setUserId(UserContextHolder.getUserId());
                String responseStatus = deployTaskServiceClientFeign.statusDeployTask(param);
                String deployStatus = Convert.toStr(JSONPath.eval(responseStatus, "$.status"), null);
                String infoStatus = Convert.toStr(JSONPath.eval(responseStatus, "$.info"), "");
                if (CharSequenceUtil.isNotBlank(deployStatus)) {
                    String status = TrainConstants.INTERFACE_DEPLOY_STATUS_MAP.getStr(deployStatus);
                    if (CharSequenceUtil.isNotBlank(status)) {
                        LambdaUpdateWrapper<PrTestSetEvaluation> updateWrapper = new LambdaUpdateWrapper<>();
                        updateWrapper.set(PrTestSetEvaluation::getDeployStatus, status)
                                .set(PrTestSetEvaluation::getModifyDate, DateUtil.date())
                                .set(PrTestSetEvaluation::getResult, infoStatus)
                                .eq(PrTestSetEvaluation::getId, evaluation.getId());
                        if (TrainConstants.DEPLOY_TASK_STATUS_COMPLETED.equals(status)) {
                            updateWrapper.set(PrTestSetEvaluation::getDeployUrl, deployTaskServiceClientFeign.getDeployUrl(evaluation.getDeployTarget(), evaluation.getId()));
                            updateWrapper.set(PrTestSetEvaluation::getDeployFinishDate, DateUtil.date());
                        }
                        prTestSetEvaluationDao.update(null, updateWrapper);
                        count += 1;
                    }

                }
            }
        }
        return count;

    }

    @SneakyThrows
    @Override
    public OperateResult<String> upload(Long id, MultipartFile uploadFile) {
        PrTestSetEvaluation prTestSetEvaluation = getById(id);
        List<Map<String, Object>> maps = ExcelUtil.getReader(uploadFile.getInputStream()).readAll();
        int size = maps.get(0).size();
        if (EvaluationConstant.TEST_SET_EVALUATION_TYPE_LEAN.equals(prTestSetEvaluation.getType()) && size == 6) {
            for (int i = 0; i < maps.size(); i++) {
                try {
                    Map<String, Object> row = maps.get(i);
                    Long detailId = Convert.toLong(row.get("ID"));
                    Object bigModelAnswer = row.get("回答1");
                    Object bigModelAnswer2 = row.get("回答2");
                    Object userFeedbackName = row.get("选择回答");
                    PrTestSetEvaluationDetail prTestSetEvaluationDetail = new PrTestSetEvaluationDetail();
                    prTestSetEvaluationDetail.setId(detailId);
                    prTestSetEvaluationDetail.setReasoningResponse(bigModelAnswer.toString());
                    prTestSetEvaluationDetail.setReasoningTwoResponse(bigModelAnswer2.toString());
                    prTestSetEvaluationDetailService.updateById(prTestSetEvaluationDetail);
                    if (ObjUtil.isNotNull(userFeedbackName)) {
                        String userFeedbackNameStr = Convert.toStr(userFeedbackName);
                        if ("回答1".equals(userFeedbackNameStr)) {
                            prTestSetEvaluationDetailService.action(detailId, ActionType.Q1);
                        } else if ("回答2".equals(userFeedbackNameStr)) {
                            prTestSetEvaluationDetailService.action(detailId, ActionType.Q2);
                        } else {
                            log.warn("用户反馈数据有误:{}", userFeedbackName);
                        }
                    }
                } catch (Exception e) {
                    log.error("更新第{}行时出错: {}", i, e.getMessage(), e);
                }
            }
            return OperateResult.success("上传修改成功!");
        } else if (EvaluationConstant.TEST_SET_EVALUATION_TYPE_GENERAL.equals(prTestSetEvaluation.getType()) && size == 6) {
            for (int i = 0; i < maps.size(); i++) {
                try {
                    Map<String, Object> row = maps.get(i);
                    Long detailId = Convert.toLong(row.get("ID"));
                    Object userFeedbackName = row.get("用户反馈");
                    if (ObjectUtil.isNotNull(userFeedbackName)) {
                        String userFeedbackNameStr = Convert.toStr(userFeedbackName);
                        if ("赞".equals(userFeedbackNameStr)) {
                            prTestSetEvaluationDetailService.like(detailId);
                        } else if ("踩".equals(userFeedbackNameStr)) {
                            prTestSetEvaluationDetailService.dislike(detailId);
                        } else {
                            log.warn("用户反馈数据有误:{}", userFeedbackName);
                        }
                    }
                } catch (Exception e) {
                    log.error("更新第{}行时出错: {}", i, e.getMessage(), e);
                }
            }
            return OperateResult.success("上传修改成功!");
        }
        throw new MyRuntimeException("文件数据有误!");
    }


    /**
     * 更新 PrTestSetEvaluation
     *
     * @param testSetEvaluation PrTestSetEvaluation
     */
    @Override
    public void update(PrTestSetEvaluation testSetEvaluation) {
        Assert.isTrue(null != testSetEvaluation.getId(), "测试集评估ID不能为空");
        LambdaUpdateWrapper<PrTestSetEvaluation> updateWrapper = Wrappers.lambdaUpdate(PrTestSetEvaluation.class)
                .eq(PrTestSetEvaluation::getId, testSetEvaluation.getId())
                .set(PrTestSetEvaluation::getModifyDate, DateUtil.date())
                .set(CharSequenceUtil.isNotBlank(testSetEvaluation.getStatus()), PrTestSetEvaluation::getStatus, testSetEvaluation.getStatus())
                .set(PrTestSetEvaluation::getBuilt, testSetEvaluation.getBuilt());
        prTestSetEvaluationDao.update(null, updateWrapper);

    }


    @Override
    public void batchChatByTestDataSet() {
        //获取部署完成且等待评估
        LambdaQueryWrapper<PrTestSetEvaluation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PrTestSetEvaluation::getDeployStatus, TrainConstants.DEPLOY_TASK_STATUS_COMPLETED)
                .eq(PrTestSetEvaluation::getStatus, MetaDataConstants.DATA_SET_EVALUATION_STATUS_WAITING)
                .le(PrTestSetEvaluation::getDeployFinishDate, DateUtil.offsetSecond(DateUtil.date(), -60));

        queryWrapper.orderByAsc(PrTestSetEvaluation::getCreateDate).last("limit 1");
        PrTestSetEvaluation dataSetEval = prTestSetEvaluationDao.selectOne(queryWrapper);
        if (dataSetEval != null) {
            // 评估中
            dataSetEval.setStatus(MetaDataConstants.DATA_SET_EVALUATION_STATUS_EVALUATING);
            this.update(dataSetEval);
            //获取数据集的问答对
            List<PrTestSetEvaluationVO> list = this.queryByDeployTaskId(dataSetEval.getId());
            if (CollUtil.isEmpty(list)) {
                log.error("当前评估任务：{}，对应的数据集 {} 数据为空！", dataSetEval.getId(), dataSetEval.getDataSetId());
                return;
            }
            log.info("模型数据集评估{}请求地址【{}】", dataSetEval.getId(), dataSetEval.getDeployUrl());
            int count = 0;
            try {
                //默认访问成功
                int httpCode = 200;
                for (int i = 0; i < list.size(); i++) {
                    PrTestSetEvaluationVO vo = list.get(i);
                    // 更新详情
                    PrTestSetEvaluationDetail evaluationDetail = new PrTestSetEvaluationDetail();
                    evaluationDetail.setId(IdUtil.getSnowflakeNextId());
                    evaluationDetail.setTestSetEvaluationId(vo.getId());
                    evaluationDetail.setPromptResponseDetailId(vo.getPromptResponseDetailId());
                    // 封装推理参数
                    String requestParam = this.reasonParam(dataSetEval.getId(), vo.getPrompt(), dataSetEval.getTemperature(), dataSetEval.getMaxTokens());
                    //推理
                    ForestResponse<?> reason = this.reasonRequest(requestParam, dataSetEval.getDeployUrl());
                    // 提取推理答案
                    if (reason != null) {
                        httpCode = reason.statusCode();
                        if (reason.isSuccess()) {
                            String content = ModelChatServiceImpl.extractAnswerByCustomerModel(reason.getContent());
                            evaluationDetail.setReasoningResponse(content);
                            evaluationDetail.setReasoningDate(DateUtil.date());
                        } else {
                            log.error("数据集评估推理异常：{},{}", Convert.toStr(reason.getContent(), ""), reason.getException());
                        }
                    }

                    //强化学习的需要问两次
                    if (EvaluationConstant.TEST_SET_EVALUATION_TYPE_LEAN.equals(dataSetEval.getType())) {
                        ForestResponse<?> reasonTwo = this.reasonRequest(requestParam, dataSetEval.getDeployUrl());
                        // 提取推理答案
                        if (reasonTwo != null) {
                            httpCode = reasonTwo.statusCode();
                            if (reasonTwo.isSuccess()) {
                                String content = ModelChatServiceImpl.extractAnswerByCustomerModel(reasonTwo.getContent());
                                //设置问答2
                                evaluationDetail.setReasoningTwoResponse(content);
                                evaluationDetail.setReasoningTwoDate(DateUtil.date());
                            } else {
                                log.error("强化学习二问数据集评估推理异常：{},{}", Convert.toStr(reasonTwo.getContent(), ""), reasonTwo.getException());
                            }
                        }
                    }
                    //删除了模型后做问答显示404时，将不再请求
                    if (HttpStatus.HTTP_NOT_FOUND == httpCode) {
                        break;
                    }
                    //只有有回答的时候才写入
                    if (CharSequenceUtil.isNotBlank(evaluationDetail.getReasoningResponse()) || CharSequenceUtil.isNotBlank(evaluationDetail.getReasoningTwoResponse())) {
                        prTestSetEvaluationDetailService.save(evaluationDetail);
                        count++;
                    }
                }
            } catch (Exception e) {
                log.error("数据集评估推理异常（catch)：{},{}", e.getMessage(), e);
            } finally {
                // 删除模型
                DeployTaskRemoteParam param = new DeployTaskRemoteParam()
                        .setDeployTaskId(dataSetEval.getId())
                        .setDeployTarget(dataSetEval.getDeployTarget())
                        .setSource(SOURCE)
                        .setUserId(UserContextHolder.getUserId());
                String result = deployTaskServiceClientFeign.deleteDeployTask(param);
                log.info("删除模型评估部署任务：{},远程删除结果{}", dataSetEval.getId(), result);
                // 评估完成, 更新状态
                String status = count > 0 ? MetaDataConstants.DATA_SET_EVALUATION_STATUS_COMPLETED : MetaDataConstants.DATA_SET_EVALUATION_STATUS_FAILED;
                dataSetEval.setStatus(status);
                this.update(dataSetEval);

            }
        }
    }


    @Override
    public boolean deleteById(Serializable id) {
        PrTestSetEvaluationVO vo = super.queryById(id, false);
        if (vo != null) {
            //等待评估、正在评估的都删一下正在部署的模型
            if (CharSequenceUtil.containsAnyIgnoreCase(vo.getStatus(),
                    MetaDataConstants.DATA_SET_EVALUATION_STATUS_WAITING, MetaDataConstants.DATA_SET_EVALUATION_STATUS_EVALUATING)) {
                DeployTaskRemoteParam param = new DeployTaskRemoteParam()
                        .setDeployTaskId(vo.getId())
                        .setDeployTarget(vo.getDeployTarget())
                        .setSource(SOURCE)
                        .setUserId(UserContextHolder.getUserId());
                String result = deployTaskServiceClientFeign.deleteDeployTask(param);
                log.info("删除模型评估部署任务：{},远程删除结果{}", id, result);
            }

            // 删除详情
            prTestSetEvaluationDetailService.deleteByEvaluationId(id);
        }
        return super.deleteById(id);
    }

    @Override
    public int submitEvaluationDeploymentTask() {
        //获取当前k8s在部署的评测任务数量
        int curEvaluationCount = this.getCurEvaluationCount();
        Integer limit= Integer.valueOf(config.getMaxEvaluationTaskLimit());
        if (curEvaluationCount < limit) {
            List<PrTestSetEvaluation> pendingTaskSubmission = prTestSetEvaluationDao.queryPrTestSetEvaluationListBySendStatus(SystemConstant.NO, limit - curEvaluationCount);
            AtomicReference<TrainTaskVO> trainTaskVO =new AtomicReference<>();
            pendingTaskSubmission.forEach(item -> {
                //下发评测任务到k8s
                Assert.notNull(item.getModelTaskId(), "模型信息为空！");
                log.info("[下发评测任务到k8s] pendingTaskSubmission：{}", JSON.toJSONString(item));
                log.info("[下发评测任务到k8s] pendingTaskSubmission.getModelTaskId：{}", item.getModelTaskId());
                trainTaskVO.set(trainTaskServiceClientFeign.queryVoById(item.getModelTaskId()));
                log.info("[下发评测任务到k8s] trainTaskVO：{}", JSON.toJSONString(trainTaskVO));
                Assert.notNull(trainTaskVO, "模型信息为空！");
                DeployTaskSubmitParam param = deployTaskServiceClientFeign.buildDeployParam(item.getId(), trainTaskVO.get());
                DeployTaskRemoteParam submitParam = new DeployTaskRemoteParam();
                submitParam.setParam(param)
                        .setUserId(item.getCreatorId())
                        .setDeployTarget(item.getDeployTarget())
                        .setSource(SOURCE);
                log.info("下发评测任务到k8s：{}", JSON.toJSONString(submitParam));
                String response = deployTaskServiceClientFeign.submitDeployTask(submitParam);
                log.info("下发评测任务到k8s---执行结果：{}", JSON.toJSONString(response));
                String resultStatus = Convert.toStr(JSONPath.eval(response, "$.status"), null);
                if (TrainConstants.SUCCESS_SUBMIT.equalsIgnoreCase(resultStatus)) {
                    item.setStatus(MetaDataConstants.DATA_SET_EVALUATION_STATUS_WAITING);
                    item.setDeployStatus(TrainConstants.DEPLOY_TASK_STATUS_WAITING);
                } else {
                    String resultInfo = Convert.toStr(JSONPath.eval(response, "$.info"), null);
                    item.setDeployStatus(TrainConstants.DEPLOY_TASK_STATUS_FAILED);
                    item.setStatus(MetaDataConstants.DATA_SET_EVALUATION_STATUS_FAILED);
                    item.setResult(resultInfo);
                }
                item.setSendStatus(SystemConstant.YES);
                this.updateById(item);
            });
            return pendingTaskSubmission.size();
        }
        return 0;
    }

    public int getCurEvaluationCount() {
        QueryParam queryParam = new QueryParam();
        PrTestSetEvaluationDTO dto = new PrTestSetEvaluationDTO();
        dto.setDeployStatus(TrainConstants.DEPLOY_TASK_STATUS_WAITING);
        dto.setSendStatus(SystemConstant.YES);
        queryParam.setFilterDto(dto);
        queryParam.setSortingFields(CollUtil.newArrayList(SortingField.asc("create_date")));
        queryParam.setPageParam(PageParam.of(10));
        List<PrTestSetEvaluationVO> queryPage = this.queryPage(queryParam).getRows();
        return queryPage.size();
    }

    private List<PrTestSetEvaluationVO> queryByDeployTaskId(Long id) {
        return prTestSetEvaluationDao.queryByDeployTaskId(id);
    }


    /**
     * 推理参数
     *
     * @return
     */
    private String reasonParam(Long model, String prompt, Float temperature, Integer maxTokens) {
        CustomerModelStartChatRequest request = new CustomerModelStartChatRequest();
        request.setModel(model.toString());
        request.setTemperature(temperature);
        request.setMax_tokens(maxTokens);
        request.setStream(false);
        request.setMessages(CollUtil.newArrayList(new CustomerModelStartChatRequest.Message().setContent(prompt)));
        request.getMessages().add(0, CustomerModelStartChatRequest.createSystemMessage(null));
        return JSON.toJSONString(request);
    }

    /**
     * 推理参数
     *
     * @return
     */
    private ForestResponse<?> reasonRequest(String param, String url) {
        try {
            //推理
            return Forest.post(url)
                    .contentTypeJson()
                    .addBody(param)
                    //.connectTimeout(2, TimeUnit.MINUTES)
                    //.readTimeout(3, TimeUnit.MINUTES)
                    .executeAsResponse();
        } catch (Exception e) {
            log.error("数据集评估问答推理异常: {}", e.getMessage());
        }
        return null;
    }

}