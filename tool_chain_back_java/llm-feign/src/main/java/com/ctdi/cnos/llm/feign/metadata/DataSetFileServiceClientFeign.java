package com.ctdi.cnos.llm.feign.metadata;


import com.ctdi.cnos.llm.RemoteConstont;
import com.ctdi.cnos.llm.beans.meta.dataSet.DataSetFile;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author wangyb
 * @version 1.0.0
 * @ClassName DataSetFileServiceClientFeign.java
 * @Description TODO
 * @createTime 2024-5-17-10:55:00
 */

@Component
@FeignClient(value = RemoteConstont.METADATA_SERVICE_NAME, path = "${cnos.server.llm-metadata-service.contextPath}")
public interface DataSetFileServiceClientFeign {


    @PostMapping("/dataSetFile/add")
    @ApiOperation(value = "新增数据集文件")
    void add(@RequestBody DataSetFile dataSetFile);


    @GetMapping("/dataSetFile/queryByRequestId")
    @ApiOperation(value = "查找DataSetFile集合根据requestId")
    List<DataSetFile> queryByRequestId(@RequestParam("requestId") BigDecimal requestId);


    @GetMapping("/dataSetFile/cancelAddDataSet")
    @ApiOperation(value = "取消新增数据集")
    Map<String, Object> cancelAddDataSet(@RequestParam("requestId") BigDecimal requestId);


    @GetMapping("/dataSetFile/deleteById")
    @ApiOperation(value = "删除数据集文件")
    Map<String, Object> deleteById(@RequestParam("dataSetFileId") BigDecimal dataSetFileId);

    @GetMapping("/dataSetFile/queryByDataSetId")
    DataSetFile queryByDataSetId(@RequestParam("dataSetId")Long dataSetId);
}