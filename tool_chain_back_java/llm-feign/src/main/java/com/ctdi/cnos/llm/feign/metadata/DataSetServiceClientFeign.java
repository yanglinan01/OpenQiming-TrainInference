package com.ctdi.cnos.llm.feign.metadata;


import com.ctdi.cnos.llm.RemoteConstont;
import com.ctdi.cnos.llm.beans.meta.dataSet.DataSet;
import com.ctdi.cnos.llm.beans.meta.dataSet.DataSetAndPrInfoVO;
import com.ctdi.cnos.llm.beans.train.IntentRecognition.Info3cTreeResp;
import com.ctdi.cnos.llm.beans.train.IntentRecognition.IntentRecognitionCorpusReq;
import com.ctdi.cnos.llm.beans.train.IntentRecognition.IntentRecognitionCorpusResp;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author wangyb
 * @version 1.0.0
 * @ClassName DataSetServiceClientFeign.java
 * @Description TODO
 * @createTime 2024-5-17-10:55:00
 */

@Component
@FeignClient(value = RemoteConstont.METADATA_SERVICE_NAME, path = "${cnos.server.llm-metadata-service.contextPath}")
public interface DataSetServiceClientFeign {


    @GetMapping("/dataSet/queryPage")
    Map<String, Object> queryPage(@RequestParam(name = "pageSize", required = false, defaultValue = "20") long pageSize,
                                  @RequestParam(name = "currentPage", required = false, defaultValue = "1") long currentPage,
                                  @RequestParam(name = "dataSetName", required = false) String dataSetName,
                                  @RequestParam(name = "belong") String belong,
                                  @RequestParam(name = "setType", required = false) String setType,
                                  @RequestParam(name = "types", required = false) String types,
                                  @RequestParam(name = "enhancedTrainTaskId", required = false) String enhancedTrainTaskId,
                                  @RequestParam(name = "dataTypes", required = false) List<String> dataTypes);

    @GetMapping("/dataSet/queryPageByCategory")
    Map<String, Object> queryPageByCategory(@RequestParam(name = "pageSize", required = false, defaultValue = "20") long pageSize,
                                  @RequestParam(name = "currentPage", required = false, defaultValue = "1") long currentPage,
                                  @RequestParam(name = "dataSetName", required = false) String dataSetName,
                                  @RequestParam(name = "belong") String belong,
                                  @RequestParam(name = "setType", required = false) String setType,
                                  @RequestParam(name = "types", required = false) String types,
                                  @RequestParam(name = "enhancedTrainTaskId", required = false) String enhancedTrainTaskId,
                                  @RequestParam(name = "dataTypes", required = false) List<String> dataTypes,
                                  @RequestParam(name = "projectId", required = false) Long projectId);


    @GetMapping("/dataSet/queryList")
    List<DataSet> queryList(@RequestParam(name = "dataSetName", required = false) String dataSetName,
                                       @RequestParam(name = "belong", required = false) String belong,
                                       @RequestParam(name = "types", required = false) String types,
                                       @RequestParam(name = "dataType", required = false) String dataType,
                                       @RequestParam(name = "setType", required = false) String setType,
                                       @RequestParam(name = "enhancedTrainTaskId", required = false) String enhancedTrainTaskId,
                                       @RequestParam(name = "dataTypes", required = false) List<String> dataTypes);

    @GetMapping("/dataSet/queryListByCategory")
    List<DataSet> queryListByCategory(@RequestParam(name = "dataSetName", required = false) String dataSetName,
                                             @RequestParam(name = "belong", required = false) String belong,
                                             @RequestParam(name = "types", required = false) String types,
                                             @RequestParam(name = "dataType", required = false) String dataType,
                                             @RequestParam(name = "setType", required = false) String setType,
                                             @RequestParam(name = "enhancedTrainTaskId", required = false) String enhancedTrainTaskId,
                                             @RequestParam(name = "dataTypes", required = false) List<String> dataTypes,
                                             @RequestParam(name = "projectId", required = false) Long projectId);


    @PostMapping("/dataSet/queryListByVo")
    List<DataSet> queryListByVo(@RequestBody DataSetAndPrInfoVO dataSet);

    @GetMapping("/dataSet/queryVOById")
    DataSetAndPrInfoVO queryVOById(@RequestParam(name = "id") String id,
                                   @RequestParam(name = "dataType") String dataType,
                                   @RequestParam(name = "setType") String setType,
                                   @RequestParam(name = "currentPage", required = false, defaultValue = "1") long currentPage,
                                   @RequestParam(name = "pageSize", required = false, defaultValue = "20") long pageSize);


    @GetMapping("/dataSet/queryById")
    DataSetAndPrInfoVO queryById(@RequestParam(name = "id") String id);



    /**
     * 删除数据集
     *
     * @param dataSetId dataSetId
     * @return Map<String, Object>
     */
    @GetMapping("/dataSet/deleteById")
    Map<String, Object> deleteById(@RequestParam(name = "dataSetId") String dataSetId);


    /**
     * 数据集调用+1
     *
     * @param dataSetId dataSetId
     * @return Map<String, Object>
     */
    @PostMapping("/dataSet/callIncr")
    Map<String, Object> callIncr(@RequestParam(name = "dataSetId") String dataSetId);


    @PostMapping(value = "/dataSet/add")
    String add(@RequestBody DataSet dataSet);

    @PostMapping(value = "/dataSet/update")
    Map<String, Object> update(@RequestBody DataSet dataSet);


    @GetMapping("/dataSet/downloadTemp")
    @ApiOperation(value = "下载问答对模板")
    ResponseEntity<Resource> downloadTemp(String param);


    @GetMapping("/dataSet/getDocList")
    @ApiOperation(value = "获取当前用户远程数据集列表")
    String getDocList();

    @PostMapping("/dataSet/addFromKnowledgeBase")
    @ApiOperation(value = "新增数据集从知识库")
    void addFromKnowledgeBase(@RequestBody DataSetAndPrInfoVO vo);

    @PostMapping("/dataSet/projSpace/addFromKnowledgeBase")
    @ApiOperation(value = "新增项目空间数据集从知识库")
    void addProjSpaceFromKB(@RequestBody DataSetAndPrInfoVO vo);

    @PostMapping("/dataSet/addIntentionRecognition")
    @ApiOperation(value = "新增意图识别数据集从知识库")
    public void addIntentionRecognition(@RequestBody DataSetAndPrInfoVO vo);

    @PostMapping("/dataSet/intentionRecognitionCorpusList")
    @ApiOperation(value = "意图识别语料查询")
    IntentRecognitionCorpusResp intentionRecognitionCorpusList(@RequestBody IntentRecognitionCorpusReq req);

    @PostMapping("/dataSet/info3cTreeList")
    @ApiOperation(value = "3c信息查询")
    Info3cTreeResp info3cTreeList();
}
