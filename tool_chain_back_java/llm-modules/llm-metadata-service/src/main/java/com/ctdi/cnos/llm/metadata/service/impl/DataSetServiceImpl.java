package com.ctdi.cnos.llm.metadata.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.base.constant.CommonConstant;
import com.ctdi.cnos.llm.base.constant.MetaDataConstants;
import com.ctdi.cnos.llm.base.object.PageParam;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.beans.meta.dataSet.DataSet;
import com.ctdi.cnos.llm.beans.meta.dataSet.DataSetAndPrInfoVO;
import com.ctdi.cnos.llm.beans.meta.dataSet.DataSetFile;
import com.ctdi.cnos.llm.beans.meta.evaluation.PrTestSetEvaluation;
import com.ctdi.cnos.llm.beans.meta.evaluation.PrTestSetEvaluationDetailVO;
import com.ctdi.cnos.llm.beans.meta.prompt.PromptCategoryDetail;
import com.ctdi.cnos.llm.beans.meta.prompt.PromptResp;
import com.ctdi.cnos.llm.beans.meta.prompt.PromptResponseDetail;
import com.ctdi.cnos.llm.beans.meta.prompt.PromptSequentialDetail;
import com.ctdi.cnos.llm.beans.train.IntentRecognition.Info3cTreeResp;
import com.ctdi.cnos.llm.beans.train.IntentRecognition.IntentRecognitionCorpusParam;
import com.ctdi.cnos.llm.beans.train.IntentRecognition.IntentRecognitionCorpusReq;
import com.ctdi.cnos.llm.beans.train.IntentRecognition.IntentRecognitionCorpusResp;
import com.ctdi.cnos.llm.config.RemoteHostConfig;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.entity.HostInfo;
import com.ctdi.cnos.llm.exception.MyRuntimeException;
import com.ctdi.cnos.llm.metadata.client.ApiClient;
import com.ctdi.cnos.llm.metadata.dao.DataSetDao;
import com.ctdi.cnos.llm.metadata.dao.PrTestSetEvaluationDao;
import com.ctdi.cnos.llm.metadata.dao.PromptRespDao;
import com.ctdi.cnos.llm.metadata.service.*;
import com.ctdi.cnos.llm.util.FileUtils;
import com.ctdi.cnos.llm.util.StringUtils;
import com.ctdi.cnos.llm.utils.DataScopeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static com.ctdi.cnos.llm.base.constant.MetaDataConstants.*;

/**
 * 数据集(DataSet)表服务实现类
 *
 * @author wangyb
 * @since 2024-05-15 14:06:51
 */
@Service("dataSetService")
@Slf4j
@RequiredArgsConstructor
public class DataSetServiceImpl implements DataSetService {


    private final DataSetDao dataSetDao;

    private final PromptRespService promptRespService;

    private final PromptRespDao promptRespDao;

    private final DataSetFileService dataSetFileService;

    private final PromptResponseDetailService promptResponseDetailService;

    private final PromptSequentialDetailService promptSequentialDetailService;

    private final PromptCategoryDetailService promptCategoryDetailService;

    private final PrTestSetEvaluationDetailService prTestSetEvaluationDetailService;

    private final PrTestSetEvaluationDao prTestSetEvaluationDao;

    private final RemoteHostConfig remoteHostConfig;


    private final ApiClient apiClient;

    private final Queue<BigDecimal> idQueue = new LinkedList<>();

    @Override
    public Page<DataSet> queryList(Page<DataSet> page, DataSetAndPrInfoVO dataSet) {
        //自定义模板只能看自己创建的
        if (Objects.equals(dataSet.getBelong(), PROMPT_BELONG_DICT_SELF)) {
            dataSet.setDataScopeSql(DataScopeUtil.dataScopeSql("mds", null));
        }
        return dataSetDao.queryList(page, dataSet);
    }


    @Override
    public List<DataSet> queryList(DataSetAndPrInfoVO dataSet) {
        //自定义模板只能看自己创建的
        if (Objects.equals(dataSet.getBelong(), PROMPT_BELONG_DICT_SELF)) {
            dataSet.setDataScopeSql(DataScopeUtil.dataScopeSql("mds", null));
        }
        return dataSetDao.queryList(dataSet);
    }

    /**
     * 分页查询意图识别数据集和问答对详情
     *
     * @param page    page
     * @param dataSet dataSet
     * @return 数据集和问答对详情列表
     */
    @Override
    public Page<DataSet> queryListByCategory(Page<DataSet> page, DataSetAndPrInfoVO dataSet) {
        //自定义模板只能看自己创建的
        if (Objects.equals(dataSet.getBelong(), PROMPT_BELONG_DICT_SELF)) {
            dataSet.setDataScopeSql(DataScopeUtil.dataProjectScopeSql("mds", null, null, dataSet.getProjectId()));
        }
        return dataSetDao.queryList(page, dataSet);
    }

    @Override
    public List<DataSet> queryListByCategory(DataSetAndPrInfoVO dataSet) {
        //自定义模板只能看自己创建的
        if (Objects.equals(dataSet.getBelong(), PROMPT_BELONG_DICT_SELF)) {
            dataSet.setDataScopeSql(DataScopeUtil.dataProjectScopeSql("mds", null, null, dataSet.getProjectId()));
        }
        return dataSetDao.queryList(dataSet);
    }


    @Override
    public DataSetAndPrInfoVO queryVOById(BigDecimal id, String dataType, String setType, Long currentPage, Long pageSize) {
        switch (setType) {
            //训练
            case CommonConstant.TRAIN_DATA_SET: {
                return this.queryProRespById(id);
            }

            //强化学习
            case CommonConstant.ENHANCE_DATA_SET: {
                DataSetAndPrInfoVO vo = new DataSetAndPrInfoVO();
                DataSet dataSet = dataSetDao.selectById(id);
                if (null != dataSet && null != dataSet.getTestSetEvaluationId()) {
                    BeanUtil.copyProperties(dataSet, vo);
                    Map<String, Object> filterMap = new LinkedHashMap<>();
                    filterMap.put("testSetEvaluationId", vo.getTestSetEvaluationId());

                    PageParam pageParam = new PageParam();
                    pageParam.setPageNum(Convert.toInt(currentPage));
                    pageParam.setPageSize(Convert.toInt(pageSize));

                    QueryParam queryParam = new QueryParam();
                    queryParam.setFilterMap(filterMap);
                    queryParam.setPageParam(pageParam);
                    PageResult<PrTestSetEvaluationDetailVO> result = prTestSetEvaluationDetailService.queryPage(queryParam);

                    if (null != result) {
                        vo.setContextList(result.getRows());
                        vo.setTotal(result.getTotal());
                    }
                }
                return vo;
            }

            //测试
            case CommonConstant.TEST_DATA_SET: {
                DataSetAndPrInfoVO vo = dataSetDao.queryTestById(id);
                if (null == vo) {
                    return null;
                }

                switch (dataType) {
                    //prompt_response
                    case CommonConstant.PROMPT_RESPONSE:
                        Map<String, Object> prompt = this.queryProRespTestById(vo.getDataSetFileId(), currentPage, pageSize);
                        vo.setContextList((List<Object>) prompt.get("rows"));
                        vo.setTotal((Long) prompt.get("total"));
                        vo.setSavePath("");
                        return vo;
                    //时序数据集
                    case CommonConstant.PROMPT_SEQUENTIAL:
                        Map<String, Object> sequential = this.queryProSeqTestById(vo.getDataSetFileId(), currentPage, pageSize);
                        vo.setContextList((List<Object>) sequential.get("rows"));
                        vo.setTotal((Long) sequential.get("total"));
                        vo.setSavePath("");
                        return vo;
                    //意图识别
                    case CommonConstant.PROMPT_CATEGORY:
                        Map<String, Object> category = this.queryProCtgTestById(vo.getDataSetFileId(), currentPage, pageSize);
                        vo.setContextList((List<Object>) category.get("rows"));
                        vo.setTotal((Long) category.get("total"));
                        vo.setSavePath("");
                        return vo;
                    default:
                        throw new MyRuntimeException("请选择合适的数据类型");
                }
            }

            default: {
                throw new MyRuntimeException("请选择合适的数据集类型");
            }
        }
    }


    /**
     * prompt_response 训练
     *
     * @param id 数据集id
     * @return 前10条数据集详情
     */
    private DataSetAndPrInfoVO queryProRespById(BigDecimal id) {
        DataSetAndPrInfoVO vo = dataSetDao.queryById(id);
        if (null != vo) {
            try {
                List<Object> list = JSON.parseArray(vo.getContext());
                vo.setContextList(list);
            } catch (Exception e) {
                throw new RuntimeException("数据集文件内容格式错误, 请重新上传");
            }
        }
        return vo;
    }


    /**
     * 获取问答对测试详情
     *
     * @param dataSetFileId 数据集文件ID
     * @param currentPage   当前页
     * @param pageSize      每页条数
     * @return 问答对测试详情分页
     */
    private Map<String, Object> queryProRespTestById(BigDecimal dataSetFileId, Long currentPage, Long pageSize) {
        Map<String, Object> result = MapUtil.newHashMap();
        if (null != dataSetFileId) {
            Page<PromptResponseDetail> page = promptResponseDetailService.queryListByDataSetFileId(dataSetFileId, currentPage, pageSize);
            if (null != page) {
                result.put("total", page.getTotal());
                result.put("rows", page.getRecords());
            }
        }
        return result;
    }


    /**
     * 获取时序测试详情
     *
     * @param dataSetFileId 数据集文件ID
     * @param currentPage   当前页
     * @param pageSize      每页条数
     * @return 时序测试详情分页
     */
    private Map<String, Object> queryProSeqTestById(BigDecimal dataSetFileId, Long currentPage, Long pageSize) {
        Map<String, Object> result = MapUtil.newHashMap();
        if (null != dataSetFileId) {
            Page<PromptSequentialDetail> page = promptSequentialDetailService.queryListByDataSetFileId(dataSetFileId, currentPage, pageSize);
            if (null != page) {
                result.put("total", page.getTotal());
                result.put("rows", page.getRecords());
            }
        }
        return result;
    }


    /**
     * 获取意图识别数据集测试详情
     *
     * @param dataSetFileId 数据集文件ID
     * @param currentPage   当前页
     * @param pageSize      每页条数
     * @return 意图识别数据集测试详情分页
     */
    private Map<String, Object> queryProCtgTestById(BigDecimal dataSetFileId, Long currentPage, Long pageSize) {
        Map<String, Object> result = MapUtil.newHashMap();
        if (null != dataSetFileId) {
            Page<PromptCategoryDetail> page = promptCategoryDetailService.queryListByDataSetFileId(dataSetFileId, currentPage, pageSize);
            if (null != page) {
                result.put("total", page.getTotal());
                result.put("rows", page.getRecords());
            }
        }
        return result;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(DataSet dataSet) {
        Assert.isTrue(StrUtil.isNotBlank(dataSet.getDataType()) && StrUtil.isNotBlank(dataSet.getSetType()), "数据类型或数据集类型不能为空");
        //强化学习
        log.info("=========dataType: {}, setType: {} =========", dataSet.getDataType(), dataSet.getSetType());
        if (dataSet.getSetType().equals(CommonConstant.ENHANCE_DATA_SET)) {
            Assert.isTrue(null != dataSet.getEnhancedTrainTaskId(), "请输入绑定数据集的模型训练ID");
            dataSet.setTemplateType(MetaDataConstants.TEMPLATE_TYPE_XLSX);
            dataSet.setRegionCode(UserContextHolder.getUser().getRegionCode());
            dataSet.setBelong(MetaDataConstants.DATASET_BELONG_DICT_SELF);
            dataSetDao.insert(dataSet);
        } else {
            List<DataSetFile> voList = dataSetFileService.queryByRequestId(dataSet.getRequestId());
            try {
                if (CollectionUtils.isNotEmpty(voList)) {
                    if (voList.size() == 1) {
                        this.executeAdd(voList.get(0), dataSet);
                    } else {
                        for (int i = 0; i < voList.size(); i++) {
                            dataSet.setDataSetName(dataSet.getDataSetName() + "_" + (i + 1));
                            this.executeAdd(voList.get(i), dataSet);
                            int index = dataSet.getDataSetName().lastIndexOf("_");
                            dataSet.setDataSetName(dataSet.getDataSetName().substring(0, index));
                        }
                    }
                } else {
                    throw new RuntimeException("请先上传数据文件");
                }
            } catch (RuntimeException e) {
                dataSetFileService.deleteByRequestId(dataSet.getRequestId());
                //删除远程文件
                voList.forEach(item -> {
                    this.deleteRemoteHostFileByPath(item.getSavePath());
                });


            }
        }

    }

    private void deleteRemoteHostFileByPath(String filePath) {
        new FileUtils().deleteRemoteFile(remoteHostConfig.getHosts().get("host41").setFilename(FileUtils.getFileName(filePath)));
        new FileUtils().deleteRemoteFile(remoteHostConfig.getHosts().get("host112").setFilename(FileUtils.getFileName(filePath)));
    }

    public void executeAdd(DataSetFile item, DataSet dataSet) {
        long fileSize = Optional.ofNullable(item.getFileSize()).orElse(Convert.toLong(0));
        dataSet.setFileSize(fileSize);

        String templateType = Optional.ofNullable(item.getTemplateType()).orElse("");
        dataSet.setTemplateType(templateType);

        BigDecimal dataSetId = new BigDecimal(IdUtil.getSnowflakeNextId());
        dataSet.setId(dataSetId);
        BigDecimal currentId = new BigDecimal(UserContextHolder.getUser().getId());
        String regionCode = UserContextHolder.getUser().getRegionCode();
        dataSet.setRegionCode(regionCode);
        dataSet.setIsDelete(NO);
        dataSet.setIsValid(YES);
        dataSet.setCallCount(Convert.toLong(0));
        dataSet.setBelong(DATASET_BELONG_DICT_SELF);
        String savePath = Optional.ofNullable(item.getSavePath()).orElse("");
        dataSet.setSavePath(savePath);
        dataSet.setPrCount(item.getPrCount());
        dataSet.setRequestId(item.getRequestId());
        isValidSumFile(fileSize, savePath);
        dataSetDao.insert(dataSet);
        promptRespService.addPromptResp(savePath, dataSetId, templateType, currentId, dataSet.getSetType(), dataSet.getDataType());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(BigDecimal dataSetId) {
        DataSet dataSet = dataSetDao.selectById(dataSetId);
        if (null == dataSet) {
            throw new RuntimeException("当前数据集不存在, dataSetId:" + dataSetId);
        }
        this.delete(dataSet);
        dataSetDao.deleteById(dataSetId);
//        new File(dataSet.getSavePath()).delete();
        if (!dataSet.getDataType().equals(CommonConstant.PROMPT_SEQUENTIAL)) {
            try {
                this.deleteRemoteHostFileByPath(dataSet.getSavePath());
            } catch (Exception e) {
                throw new RuntimeException("远程文件删除异常, 请联系管理员");
            }
        }
    }

    private void delete(DataSet dataSet) {
        DataSetFile dataSetFile = dataSetFileService.queryBySavePath(dataSet.getSavePath());
        switch (dataSet.getSetType()) {
            case CommonConstant.TRAIN_DATA_SET: //训练
                promptRespService.deleteByDataSetId(dataSet.getId());
                if (null != dataSetFile) {
                    dataSetFileService.deleteById(dataSetFile.getId());
                }
                break;
            case CommonConstant.TEST_DATA_SET: //测试
                if (null != dataSetFile) {
                    dataSetFileService.deleteById(dataSetFile.getId());
                    promptRespService.deleteByDataSetId(dataSet.getId());
                    promptResponseDetailService.deleteByDataSetFileId(dataSetFile.getId());
                }
                break;
            case CommonConstant.ENHANCE_DATA_SET: //强化
                //修改构建状态
                LambdaUpdateWrapper<PrTestSetEvaluation> updateWrapper = new LambdaUpdateWrapper<PrTestSetEvaluation>()
                        .eq(PrTestSetEvaluation::getId, dataSet.getTestSetEvaluationId())
                        .set(PrTestSetEvaluation::getBuilt, false);
                prTestSetEvaluationDao.update(null, updateWrapper);
                break;
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized void callIncr(BigDecimal dataSetId) {
        idQueue.offer(dataSetId);
        processRequests();
    }


    private void processRequests() {
        while (!idQueue.isEmpty()) {
            BigDecimal dataSetId = idQueue.poll();
            this.exeIncr(dataSetId);
        }
    }


    public void exeIncr(BigDecimal dataSetId) {
        try {
            dataSetDao.callIncr(dataSetId);
        } catch (Exception e) {
            idQueue.offer(dataSetId); //失败重试
            log.info("dataSetId: {}执行失败", dataSetId);
        }
    }


    /**
     * 校验当前用户上传文件大小, 历史总大小
     *
     * @param size     上传文件大小(单位: B)
     * @param savePath 文件绝对路径
     */
    public void isValidSumFile(long size, String savePath) {
        //单个文件不能大于100MB
        log.info("上传文件大小为：" + size + "B");
        if (size / 1048576 > 100) {
            deleteBakFile(savePath);
            throw new RuntimeException("上传文件大小不能大于100MB!");
        }

        List<DataSet> list = this.queryListByCreator();
        if (CollectionUtils.isNotEmpty(list)) {
            long total = list.stream().mapToLong(DataSet::getFileSize).sum() + size;
            //上传总大小不能大于500MB
            log.info("历史上传文件总大小为：" + (total / 1048576) + "MB");
            if (total / 1048576 > 500) {
                deleteBakFile(savePath);
                throw new RuntimeException("历史上传文件总大小不能大于500MB!");
            }
        }
    }


    /**
     * 根据用户返回数据集列表
     *
     * @return DataSetList
     */
    private List<DataSet> queryListByCreator() {
        LambdaQueryWrapper<DataSet> wrapper = new LambdaQueryWrapper<DataSet>()
                .eq(DataSet::getCreatorId, UserContextHolder.getUser().getId());
        return dataSetDao.selectList(wrapper);
    }

    private void deleteBakFile(String filePath) {
        File file = new File(filePath);
        // 检查文件是否存在
        if (file.exists()) {
            // 尝试删除文件
            if (file.delete()) {
                log.info("文件删除成功！");
            } else {
                log.info("文件删除失败！");
            }
        } else {
            log.error("文件不存在，无法删除。");
        }
    }


    @Override
    public void update(DataSet dataSet) {
        dataSetDao.updateById(dataSet);
    }

    @Override
    public String getDocList() {
        String docList = apiClient.getDocList();
        if (StrUtil.isNotBlank(docList)) {
            JSONObject jsonObject = JSON.parseObject(docList);
            JSONArray newArray = new JSONArray();
            JSONArray array = jsonObject.getJSONArray("data");
            for (int i = 0; i < array.size(); i++) {
                JSONObject item = array.getJSONObject(i);
                if (StrUtil.isNotBlank(item.getString("ftpPath")) || "null".equalsIgnoreCase(item.getString("ftpPath"))) {
                    newArray.add(item);
                }
            }
            return newArray.toString();
        }
        return "";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addFromKnowledgeBase(DataSetAndPrInfoVO vo, String dataSetDir) {
        List<String> remoteFilePaths = vo.getRemoteFilePath();
        String tmpDir = System.getProperty("user.dir");
        List<String> newFilePaths = apiClient.downloadRemoteFile(remoteFilePaths, tmpDir);

        //合并文件结果
        Map<String, Object> map = FileUtils.mergeJSONFilesResult(dataSetDir, newFilePaths);
        String mergeFilePath = (String) map.get("mergeFilePath");
        int prCount = Convert.toInt(map.get("prCount"));
        String context = (String) map.get("context");
        Long fileSize = new File(mergeFilePath).length();

        //新增promptResp
        BigDecimal dataSetId = new BigDecimal(IdUtil.getSnowflakeNextId());
        BigDecimal currentId = new BigDecimal(UserContextHolder.getUser().getId());
        PromptResp promptResp = new PromptResp();
        promptResp.setId(new BigDecimal(IdUtil.getSnowflakeNextId()));
        promptResp.setDataSetId(dataSetId);
        promptResp.setCreatorId(currentId);
        promptResp.setModifierId(currentId);
        promptResp.setContext(context);
        promptRespDao.add(promptResp);

        //新增dataSet
        DataSet dataSet = new DataSet();
        BeanUtils.copyProperties(vo, dataSet);
        dataSet.setId(dataSetId);
        dataSet.setPrCount(Convert.toLong(prCount));
        dataSet.setFileSize(Convert.toLong(fileSize));
        dataSet.setSavePath(mergeFilePath);
        dataSet.setTemplateType("jsonl");
        dataSet.setBelong(DATASET_BELONG_DICT_SELF);
        dataSet.setDataType(CommonConstant.PROMPT_RESPONSE);
        dataSet.setSetType(CommonConstant.TRAIN_DATA_SET);
        dataSet.setRegionCode(UserContextHolder.getUser().getRegionCode());
        dataSetDao.insert(dataSet);

        //新增数据集文件
        DataSetFile dataSetFile = new DataSetFile();
        dataSetFile.setId(new BigDecimal(IdUtil.getSnowflakeNextId()));
        dataSetFile.setTemplateType("jsonl");
        dataSetFile.setSavePath(mergeFilePath);
        dataSetFile.setFileSize(Convert.toLong(fileSize));
        dataSetFile.setPrCount(Convert.toLong(prCount));
        dataSetFile.setCreatorId(currentId);
        dataSetFile.setModifierId(currentId);
        dataSetFile.setBelong(KNOWLEDGE_BASE);
        dataSetFileService.add(dataSetFile);


        try {
            String fileName = StrUtil.subAfter(mergeFilePath, "/", true);
            MultipartFile multipartFile = new FileUtils().convertFileToMultipartFile(mergeFilePath);
            HostInfo host41 = remoteHostConfig.getHosts().get("host41").setFilePath(mergeFilePath).setFilename(fileName);;
            HostInfo host112 = remoteHostConfig.getHosts().get("host112").setFilePath(mergeFilePath.replace(remoteHostConfig.getFilePrefix112(), ""));
            new FileUtils().uploadRemoteFile(host41, multipartFile);
            new FileUtils().uploadRemoteFile(host112, multipartFile);

        } catch (IOException e) {
            log.info("知识库数据集文件上传网络异常");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addProjSpaceFromKB(DataSetAndPrInfoVO vo, String dataSetDir) {
        List<String> remoteFilePaths = vo.getRemoteFilePath();
        String tmpDir = System.getProperty("user.dir");
        List<String> newFilePaths = apiClient.downloadRemoteFile(remoteFilePaths, tmpDir);

        //合并文件结果
        Map<String, Object> map = FileUtils.mergeJSONFilesResult(dataSetDir, newFilePaths);
        String mergeFilePath = (String) map.get("mergeFilePath");
        int prCount = Convert.toInt(map.get("prCount"));
        String context = (String) map.get("context");
        Long fileSize = new File(mergeFilePath).length();

        //新增promptResp
        BigDecimal dataSetId = new BigDecimal(IdUtil.getSnowflakeNextId());
        BigDecimal currentId = new BigDecimal(UserContextHolder.getUser().getId());
        PromptResp promptResp = new PromptResp();
        promptResp.setId(new BigDecimal(IdUtil.getSnowflakeNextId()));
        promptResp.setDataSetId(dataSetId);
        promptResp.setCreatorId(currentId);
        promptResp.setModifierId(currentId);
        promptResp.setContext(context);
        promptRespDao.add(promptResp);

        //新增dataSet
        DataSet dataSet = new DataSet();
        BeanUtils.copyProperties(vo, dataSet);
        dataSet.setId(dataSetId);
        dataSet.setPrCount(Convert.toLong(prCount));
        dataSet.setFileSize(Convert.toLong(fileSize));
        dataSet.setSavePath(mergeFilePath);
        dataSet.setTemplateType("jsonl");
        dataSet.setBelong(DATASET_BELONG_DICT_SELF);
        dataSet.setDataType(CommonConstant.PROMPT_RESPONSE);
        dataSet.setSetType(CommonConstant.TRAIN_DATA_SET);
        dataSet.setRegionCode(UserContextHolder.getUser().getRegionCode());
        dataSet.setProjectId(vo.getProjectId());
        dataSetDao.insert(dataSet);

        //新增数据集文件
        DataSetFile dataSetFile = new DataSetFile();
        dataSetFile.setId(new BigDecimal(IdUtil.getSnowflakeNextId()));
        dataSetFile.setTemplateType("jsonl");
        dataSetFile.setSavePath(mergeFilePath);
        dataSetFile.setFileSize(Convert.toLong(fileSize));
        dataSetFile.setPrCount(Convert.toLong(prCount));
        dataSetFile.setCreatorId(currentId);
        dataSetFile.setModifierId(currentId);
        dataSetFile.setBelong(KNOWLEDGE_BASE);
        dataSetFileService.add(dataSetFile);


        try {
            String fileName = StrUtil.subAfter(mergeFilePath, "/", true);
            MultipartFile multipartFile = new FileUtils().convertFileToMultipartFile(mergeFilePath);
            HostInfo host41 = remoteHostConfig.getHosts().get("host41").setFilePath(mergeFilePath).setFilename(fileName);
            HostInfo host112 = remoteHostConfig.getHosts().get("host112").setFilePath(mergeFilePath.replace(remoteHostConfig.getFilePrefix112(), ""));
            new FileUtils().uploadRemoteFile(host41, multipartFile);
            new FileUtils().uploadRemoteFile(host112, multipartFile);

        } catch (IOException e) {
            log.info("知识库数据集文件上传网络异常");
        }
    }

    @Override
    public void addIntentionRecognition(DataSetAndPrInfoVO vo) {
        //数据集转成对应格式生成文件
        //转换格式
        List<IntentRecognitionCorpusParam> list = vo.getIntentRecognitionCorpusList();
        List<Map> mapList=new ArrayList<>();
        for (IntentRecognitionCorpusParam param : list) {
            Map conversationMap=new HashMap();
            List<Map> conversationList = new ArrayList<>();
            Map instructionMap = new HashMap();
            instructionMap.put("role", "instruction");
            instructionMap.put("content", "");
            conversationList.add(instructionMap);
            Map userMap = new HashMap();
            userMap.put("role", "user");
            userMap.put("content", param.getUserAnswer());
            conversationList.add(userMap);
            Map assistantMap = new HashMap();
            Map assistantContentMap = new HashMap();
            assistantContentMap.put("type", param.getSecondLabelName());
            assistantContentMap.put("subject", param.getIntentClassification());
            assistantMap.put("role", "assistant");
            assistantMap.put("content", JSONObject.toJSONString(assistantContentMap));
            conversationList.add(assistantMap);
            Map businessProductMap = new HashMap();
            businessProductMap.put("role", "businessProduct");
            businessProductMap.put("content", param.getBusinessProduct());
            conversationList.add(businessProductMap);
            Map applyNameMap = new HashMap();
            applyNameMap.put("role", "applyName");
            applyNameMap.put("content", param.getApplyName());
            conversationList.add(applyNameMap);
            Map intentClassificationMap=new HashMap();
            intentClassificationMap.put("role", "intentClassification");
            intentClassificationMap.put("content", param.getIntentClassification());
            conversationList.add(intentClassificationMap);
            conversationMap.put("conversations",conversationList);
            mapList.add(conversationMap);
        }
        //生成文件
        String content=JSONObject.toJSONString(mapList);
//        List<String> lines = mapList.stream().map(o -> {
//            return JSONObject.toJSONString(o);
//        }).collect(Collectors.toList());
//        for(int i=0;i<lines.size()-1;i++){
//            lines.set(i,lines.get(i)+",");
//        }
//        lines.add(0,"[[");
//        lines.add("]]");
//        String content = String.join(System.lineSeparator(), lines); // 将列表转换为字符串，每行之间用换行符分隔
        String fileName=UUID.randomUUID() + ".json";
        String mergeFilePath = vo.getUploadDir() + fileName;
        FileUtil.writeString(content, mergeFilePath , "UTF-8"); // 写入文件，指定编码为UTF-8


        List<IntentRecognitionCorpusParam> listTen = list.subList(0, Math.min(list.size(), 10));
        String context = JSONObject.toJSONString(listTen);
        //新增promptResp
        BigDecimal dataSetId = new BigDecimal(IdUtil.getSnowflakeNextId());
        BigDecimal currentId = new BigDecimal(UserContextHolder.getUser().getId());
        PromptResp promptResp = new PromptResp();
        promptResp.setId(new BigDecimal(IdUtil.getSnowflakeNextId()));
        promptResp.setDataSetId(dataSetId);
        promptResp.setCreatorId(currentId);
        promptResp.setModifierId(currentId);
        promptResp.setContext(context);
        promptRespDao.add(promptResp);


        //新增dataSet
        DataSet dataSet = new DataSet();
        BeanUtils.copyProperties(vo, dataSet);
        dataSet.setId(dataSetId);
        dataSet.setPrCount(Convert.toLong(list.size()));
        dataSet.setSavePath(mergeFilePath);
        dataSet.setTemplateType("json");
        dataSet.setBelong(StringUtils.isNotEmpty(vo.getBelong())?vo.getBelong():DATASET_BELONG_DICT_SELF);
        dataSet.setDataType(StringUtils.isNotEmpty(vo.getDataType())?vo.getDataType():CommonConstant.PROMPT_CATEGORY);
        dataSet.setSetType(StringUtils.isNotEmpty(vo.getSetType())?vo.getSetType():CommonConstant.TRAIN_DATA_SET);
        dataSet.setRegionCode(UserContextHolder.getUser().getRegionCode());
        dataSetDao.insert(dataSet);

        //新增数据集文件
        DataSetFile dataSetFile = new DataSetFile();
        dataSetFile.setId(new BigDecimal(IdUtil.getSnowflakeNextId()));
        dataSetFile.setTemplateType("json");
        dataSetFile.setSavePath(mergeFilePath);
        dataSetFile.setFileSize(Convert.toLong(list.size()));
        dataSetFile.setCreatorId(currentId);
        dataSetFile.setModifierId(currentId);
        dataSetFile.setBelong(StringUtils.isNotEmpty(vo.getOrigin())?vo.getOrigin():KNOWLEDGE_BASE);
        dataSetFileService.add(dataSetFile);


        try {
            MultipartFile multipartFile = new FileUtils().convertFileToMultipartFile(mergeFilePath);
            HostInfo host41 = remoteHostConfig.getHosts().get("host41").setFilePath(mergeFilePath).setFilename(fileName);
            HostInfo host112 = remoteHostConfig.getHosts().get("host112").setFilePath(mergeFilePath.replace(remoteHostConfig.getFilePrefix112(), "")).setFilename(fileName);
            new FileUtils().uploadRemoteFile(host41, multipartFile);
            new FileUtils().uploadRemoteFile(host112, multipartFile);

        } catch (IOException e) {
            log.info("知识库数据集文件上传网络异常");
        }
    }

    @Override
    public DataSet queryById(String id) {
        return dataSetDao.queryById(new BigDecimal(id));
    }


    @Override
    public DataSetAndPrInfoVO queryTestById(BigDecimal dataSetId) {
        return dataSetDao.queryTestById(dataSetId);
    }

    @Override
    public IntentRecognitionCorpusResp intentionRecognitionCorpusList(IntentRecognitionCorpusReq req) {
        Long userId = UserContextHolder.getUserId();
        IntentRecognitionCorpusResp resp = apiClient.intentionRecognitionCorpusList(req, userId);
        return resp;
    }

    @Override
    public Info3cTreeResp info3cTreeList() {
        Long userId = UserContextHolder.getUserId();
        Info3cTreeResp resp = apiClient.info3cTreeList(userId);
        return resp;
    }


}
