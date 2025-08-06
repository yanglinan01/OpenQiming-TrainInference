package com.ctdi.cnos.llm.metadata.service;


import com.ctdi.cnos.llm.beans.meta.dataSet.DataSetFile;

import java.math.BigDecimal;
import java.util.List;

/**
 * 数据集上传文件(DataSetFile)表服务接口
 * @author wangyb
 * @since 2024-05-24 11:22:12
 */
public interface DataSetFileService {

    /**
     * 新增数据集文件
     * @param dataSetFile dataSetFile
     */
    void add(DataSetFile dataSetFile);

    /**
     * 根据请求Id查询数据集文件列表
     * @param requestId 新增数据集弹窗id
     * @return 数据集文件列表
     */
    List<DataSetFile> queryByRequestId(BigDecimal requestId);

    /**
     * 取消新增数据集,删除临时的数据集文件列表
     * @param requestId 新增数据集弹窗id
     */
    void cancelAddDataSet(BigDecimal requestId);

    /**
     * 根据id删除数据集文件
     * @param id id
     */
    void deleteById(BigDecimal id);

    /**
     * 根据savePath删除数据集文件
     * @param savePath savePath
     */
    void deleteBySavePath(String savePath);

    /**
     * 上传文件至远程机器
     * @param localFilePath 本地文件绝对路径
     */
//    void uploadRemoteFile(String localFilePath);

    /**
     * 删除远程机器文件
     * @param filePath 文件绝对路径
     */
//    void deleteRemoteFile(String filePath);

    /**
     * 根据savePath查询数据集文件
     * @param savePath savePath
     * @return DataSetFile
     */
    DataSetFile queryBySavePath(String savePath);



    void deleteByRequestId(BigDecimal requestId);


    DataSetFile queryByDataSetId(Long dataSetId);
}