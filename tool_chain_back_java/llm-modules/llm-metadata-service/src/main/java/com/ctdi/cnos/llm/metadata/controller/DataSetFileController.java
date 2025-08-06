package com.ctdi.cnos.llm.metadata.controller;

import com.ctdi.cnos.llm.beans.meta.dataSet.DataSetFile;
import com.ctdi.cnos.llm.metadata.service.DataSetFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据集上传文件(DataSetFile)表控制层
 * @author wangyb
 * @since 2024-05-24 11:22:12
 */
@Api(tags = {"DataSetFileController接口"})
@RestController
@RequestMapping(value = "dataSetFile")
public class DataSetFileController {

    @Autowired
    DataSetFileService dataSetFileService;


    @PostMapping("/add")
    @ApiOperation(value = "新增数据集文件")
    public Map<String, Object> addBatch(@RequestBody DataSetFile dataSetFile) {
        dataSetFileService.add(dataSetFile);
        Map<String, Object> result = new HashMap<>(2);
        result.put("success", true);
        result.put("message", "新增成功");
        return result;
    }


    @GetMapping("/queryByRequestId")
    @ApiOperation(value = "查找DataSetFile集合根据requestId")
    public List<DataSetFile> queryByRequestId(@RequestParam("requestId") BigDecimal requestId) {
        return dataSetFileService.queryByRequestId(requestId);
    }

    @ApiOperation(value = "根据dataSetId查询记录")
    @GetMapping("/queryByDataSetId")
    public DataSetFile queryByDataSetId(@RequestParam("dataSetId")Long dataSetId) {
        return dataSetFileService.queryByDataSetId(dataSetId);
    }


    @GetMapping("/cancelAddDataSet")
    @ApiOperation(value = "取消新增数据集")
    public Map<String, Object> cancelAddDataSet(@RequestParam("requestId") BigDecimal requestId) {
        dataSetFileService.cancelAddDataSet(requestId);
        Map<String, Object> result = new HashMap<>(2);
        result.put("success", true);
        result.put("message", "取消成功");
        return result;
    }


    @GetMapping("/deleteById")
    @ApiOperation(value = "删除数据集文件")
    public Map<String, Object> deleteById(@RequestParam("dataSetFileId") BigDecimal dataSetFileId){
        dataSetFileService.deleteById(dataSetFileId);
        Map<String, Object> result = new HashMap<>(2);
        result.put("success", true);
        result.put("message", "删除成功");
        return result;
    }


}