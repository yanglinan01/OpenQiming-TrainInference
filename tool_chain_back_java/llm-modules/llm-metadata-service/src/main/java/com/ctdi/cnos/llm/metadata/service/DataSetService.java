package com.ctdi.cnos.llm.metadata.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.beans.meta.dataSet.DataSet;
import com.ctdi.cnos.llm.beans.meta.dataSet.DataSetAndPrInfoVO;
import com.ctdi.cnos.llm.beans.train.IntentRecognition.Info3cTreeResp;
import com.ctdi.cnos.llm.beans.train.IntentRecognition.IntentRecognitionCorpusParam;
import com.ctdi.cnos.llm.beans.train.IntentRecognition.IntentRecognitionCorpusReq;
import com.ctdi.cnos.llm.beans.train.IntentRecognition.IntentRecognitionCorpusResp;

import java.math.BigDecimal;
import java.util.List;

/**
 * 数据集(DataSet)表服务接口
 * @author wangyb
 * @since 2024-05-15 14:06:51
 */
public interface DataSetService{

    /**
     * 分页查询数据集和问答对详情
     * @param page page
     * @param dataSet dataSet
     * @return 数据集和问答对详情列表
     */
    Page<DataSet> queryList(Page<DataSet> page, DataSetAndPrInfoVO dataSet);

    /**
     * 查询数据集和问答对详情
     * @param dataSet dataSet
     * @return 数据集和问答对详情列表
     */
    List<DataSet> queryList(DataSetAndPrInfoVO dataSet);

    /**
     * 分页查询意图识别数据集和问答对详情
     * @param page page
     * @param dataSet dataSet
     * @return 数据集和问答对详情列表
     */
    Page<DataSet> queryListByCategory(Page<DataSet> page, DataSetAndPrInfoVO dataSet);

    /**
     * 查询数据集和问答对详情
     * @param dataSet dataSet
     * @return 数据集和问答对详情列表
     */
    List<DataSet> queryListByCategory(DataSetAndPrInfoVO dataSet);

    /**
     * 删除数据集根据数据集ID
     * @param dataSetId dataSetId
     */
    void deleteById(BigDecimal dataSetId);

    /**
     * 调用数据集统计
     * @param dataSetId dataSetId
     */
    void callIncr(BigDecimal dataSetId);


    /**
     *
     * @param id dataSetId
     * @param dataType 数据类型(Prompt+response; 时序数据 ...)
     * @param setType 数据(训练集; 数据集 ...)
     * @param currentPage 当前页
     * @param pageSize 每页条数
     * @return 数据集合详情
     */
    DataSetAndPrInfoVO queryVOById(BigDecimal id, String dataType, String setType, Long currentPage, Long pageSize);

    /**
     * 新增数据集
     * @param dataSet dataSet
     */
    void add(DataSet dataSet);

    /**
     * 更新数据集
     * @param dataSet dataSet
     */
    void update(DataSet dataSet);

    /**
     * @return 获取远程数据集列表
     */
    String getDocList();

    /**
     * 保存远程数据集文件
     * @param vo vo
     */
    void addFromKnowledgeBase(DataSetAndPrInfoVO vo, String uploadDir);

    /**
     * 保存远程数据集文件
     * @param vo vo
     */
    void addProjSpaceFromKB(DataSetAndPrInfoVO vo, String uploadDir);

    /**
     * 上传意图识别数据集
     * @param list
     */
    void addIntentionRecognition(DataSetAndPrInfoVO vo);

    DataSet queryById(String id);

    DataSetAndPrInfoVO queryTestById(BigDecimal dataSetId);

    /**
     * 获取意图识别语料
     * @param req
     * @return
     */
    IntentRecognitionCorpusResp intentionRecognitionCorpusList(IntentRecognitionCorpusReq req);

    /**
     * 3c信息查询
     * @return
     */
    Info3cTreeResp info3cTreeList();
}
