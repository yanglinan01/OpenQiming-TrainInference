package com.ctdi.cnos.llm.metadata.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.annotation.AuthIgnore;
import com.ctdi.cnos.llm.beans.meta.dataSet.DataSet;
import com.ctdi.cnos.llm.beans.meta.dataSet.DataSetAndPrInfoVO;
import com.ctdi.cnos.llm.beans.train.IntentRecognition.Info3cTreeResp;
import com.ctdi.cnos.llm.beans.train.IntentRecognition.IntentRecognitionCorpusParam;
import com.ctdi.cnos.llm.beans.train.IntentRecognition.IntentRecognitionCorpusReq;
import com.ctdi.cnos.llm.beans.train.IntentRecognition.IntentRecognitionCorpusResp;
import com.ctdi.cnos.llm.metadata.service.DataSetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据集(DataSet)表控制层
 *
 * @author wangyb
 * @since 2024-05-15 14:06:51
 */
@Api(tags = {"DataSetController接口"})
@RestController
@RequestMapping(value = "/dataSet")
public class DataSetController {

    @Autowired
    DataSetService dataSetService;


    @ApiOperation(value = "分页查询数据集列表", notes = "分页查询数据集列表")
    @GetMapping("/queryPage")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "页大小，默认为20", paramType = "param"),
            @ApiImplicitParam(name = "currentPage", value = "当前页，默认为1", paramType = "param"),
            @ApiImplicitParam(name = "dataSetName", value = "dataSet名称", paramType = "param"),
            @ApiImplicitParam(name = "belong", value = "dataSet归属(1系统;2用户)", required = true, paramType = "param"),
            @ApiImplicitParam(name = "types", value = "dataSet类别,多个以逗号（,）分隔", paramType = "param"),
            @ApiImplicitParam(name = "dataTypes", value = "数据类型集合", paramType = "param")
    })
    public Map<String, Object> queryPage(@RequestParam(name = "pageSize", required = false, defaultValue = "20") long pageSize,
                                         @RequestParam(name = "currentPage", required = false, defaultValue = "1") long currentPage,
                                         @RequestParam(name = "dataSetName", required = false) String dataSetName,
                                         @RequestParam(name = "belong") String belong,
                                         @RequestParam(name = "setType", required = false) String setType,
                                         @RequestParam(name = "types", required = false) String types,
                                         @RequestParam(name = "enhancedTrainTaskId", required = false) String enhancedTrainTaskId,
                                         @RequestParam(name = "dataTypes", required = false) List<String> dataTypes) {
        Page<DataSet> page = new Page<>(currentPage, pageSize);
        page.addOrder(OrderItem.desc("modify_date"));
        DataSetAndPrInfoVO dataSet = new DataSetAndPrInfoVO();
        dataSet.setDataSetName(dataSetName);
        dataSet.setBelong(belong);
        dataSet.setSetType(setType);
        dataSet.setTypes(types);
        dataSet.setEnhancedTrainTaskId(Convert.toLong(enhancedTrainTaskId));
        dataSet.setDataTypes(dataTypes);
        dataSetService.queryList(page, dataSet);
        Map<String, Object> result = new HashMap<>(2);
        result.put("total", page.getTotal());
        result.put("rows", page.getRecords());
        return result;
    }

    @ApiOperation(value = "分页查询意图识别数据集列表", notes = "分页查询数据集列表")
    @GetMapping("/queryPageByCategory")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "页大小，默认为20", paramType = "param"),
            @ApiImplicitParam(name = "currentPage", value = "当前页，默认为1", paramType = "param"),
            @ApiImplicitParam(name = "dataSetName", value = "dataSet名称", paramType = "param"),
            @ApiImplicitParam(name = "belong", value = "dataSet归属(1系统;2用户)", required = true, paramType = "param"),
            @ApiImplicitParam(name = "types", value = "dataSet类别,多个以逗号（,）分隔", paramType = "param"),
            @ApiImplicitParam(name = "dataTypes", value = "数据类型集合", paramType = "param"),
            @ApiImplicitParam(name = "projectId", value = "项目id", paramType = "projectId")
    })
    public Map<String, Object> queryPageByCategory(@RequestParam(name = "pageSize", required = false, defaultValue = "20") long pageSize,
                                         @RequestParam(name = "currentPage", required = false, defaultValue = "1") long currentPage,
                                         @RequestParam(name = "dataSetName", required = false) String dataSetName,
                                         @RequestParam(name = "belong") String belong,
                                         @RequestParam(name = "setType", required = false) String setType,
                                         @RequestParam(name = "types", required = false) String types,
                                         @RequestParam(name = "enhancedTrainTaskId", required = false) String enhancedTrainTaskId,
                                         @RequestParam(name = "dataTypes", required = false) List<String> dataTypes,
                                         @RequestParam(name = "projectId", required = false) Long projectId) {
        Page<DataSet> page = new Page<>(currentPage, pageSize);
        page.addOrder(OrderItem.desc("modify_date"));
        DataSetAndPrInfoVO dataSet = new DataSetAndPrInfoVO();
        dataSet.setDataSetName(dataSetName);
        dataSet.setBelong(belong);
        dataSet.setSetType(setType);
        dataSet.setTypes(types);
        dataSet.setEnhancedTrainTaskId(Convert.toLong(enhancedTrainTaskId));
        dataSet.setDataTypes(dataTypes);
        dataSet.setProjectId(projectId);
        dataSetService.queryListByCategory(page, dataSet);
        Map<String, Object> result = new HashMap<>(2);
        result.put("total", page.getTotal());
        result.put("rows", page.getRecords());
        return result;
    }

    @ApiOperation(value = "查询数据集列表", notes = "查询数据集列表")
    @GetMapping("/queryList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataSetName", value = "dataSet名称", paramType = "param"),
            @ApiImplicitParam(name = "belong", value = "dataSet归属(1系统;2用户)", paramType = "param"),
            @ApiImplicitParam(name = "types", value = "dataSet类别,多个以逗号（,）分隔", paramType = "param"),
            @ApiImplicitParam(name = "dataType", value = "数据类型;(1Prompt+Response; 2时序数据; 3意图识别）", paramType = "param"),
            @ApiImplicitParam(name = "setType", value = "数据集分类(1训练集; 2测试集)", paramType = "param"),
            @ApiImplicitParam(name = "enhancedTrainTaskId", value = "强化数据集绑定的训练任务", paramType = "enhancedTrainTaskId"),
            @ApiImplicitParam(name = "dataTypes", value = "数据类型集合", paramType = "param")
    })
    public List<DataSet> queryList(@RequestParam(name = "dataSetName", required = false) String dataSetName,
                                              @RequestParam(name = "belong", required = false) String belong,
                                              @RequestParam(name = "types", required = false) String types,
                                              @RequestParam(name = "dataType", required = false) String dataType,
                                              @RequestParam(name = "setType", required = false) String setType,
                                              @RequestParam(name = "enhancedTrainTaskId", required = false) String enhancedTrainTaskId,
                                              @RequestParam(name = "dataTypes", required = false) List<String> dataTypes) {
        DataSetAndPrInfoVO dataSet = new DataSetAndPrInfoVO();
        dataSet.setDataSetName(dataSetName);
        dataSet.setBelong(belong);
        dataSet.setTypes(types);
        dataSet.setDataType(dataType);
        dataSet.setSetType(setType);
        dataSet.setEnhancedTrainTaskId(Convert.toLong(enhancedTrainTaskId));
        dataSet.setDataTypes(dataTypes);
        return dataSetService.queryList(dataSet);
    }

    @ApiOperation(value = "查询意图识别数据集列表", notes = "查询意图识别数据集列表")
    @GetMapping("/queryListByCategory")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataSetName", value = "dataSet名称", paramType = "param"),
            @ApiImplicitParam(name = "belong", value = "dataSet归属(1系统;2用户)", paramType = "param"),
            @ApiImplicitParam(name = "types", value = "dataSet类别,多个以逗号（,）分隔", paramType = "param"),
            @ApiImplicitParam(name = "dataType", value = "数据类型;(1Prompt+Response; 2时序数据; 3意图识别）", paramType = "param"),
            @ApiImplicitParam(name = "setType", value = "数据集分类(1训练集; 2测试集)", paramType = "param"),
            @ApiImplicitParam(name = "enhancedTrainTaskId", value = "强化数据集绑定的训练任务", paramType = "enhancedTrainTaskId"),
            @ApiImplicitParam(name = "dataTypes", value = "数据类型集合", paramType = "param"),
            @ApiImplicitParam(name = "projectId", value = "项目id", paramType = "projectId")
    })
    public List<DataSet> queryListByCategory(@RequestParam(name = "dataSetName", required = false) String dataSetName,
                                   @RequestParam(name = "belong", required = false) String belong,
                                   @RequestParam(name = "types", required = false) String types,
                                   @RequestParam(name = "dataType", required = false) String dataType,
                                   @RequestParam(name = "setType", required = false) String setType,
                                   @RequestParam(name = "enhancedTrainTaskId", required = false) String enhancedTrainTaskId,
                                   @RequestParam(name = "dataTypes", required = false) List<String> dataTypes,
                                   @RequestParam(name = "projectId", required = false) Long projectId) {
        DataSetAndPrInfoVO dataSet = new DataSetAndPrInfoVO();
        dataSet.setDataSetName(dataSetName);
        dataSet.setBelong(belong);
        dataSet.setTypes(types);
        dataSet.setDataType(dataType);
        dataSet.setSetType(setType);
        dataSet.setEnhancedTrainTaskId(Convert.toLong(enhancedTrainTaskId));
        dataSet.setDataTypes(dataTypes);
        dataSet.setProjectId(projectId);
        return dataSetService.queryListByCategory(dataSet);
    }

    @ApiOperation(value = "查询多参数据集列表", notes = "查询多参数据集列表")
    @PostMapping("/queryListByVo")
    public List<DataSet> queryListByVo(@RequestBody DataSetAndPrInfoVO dataSet) {
        return dataSetService.queryList(dataSet);
    }


    @GetMapping("/queryVOById")
    @ApiOperation(value = "查询DataSetVO详情")
    @AuthIgnore
    public DataSetAndPrInfoVO queryVOById(@RequestParam(name = "id") String id,
                                          @RequestParam(name = "dataType") String dataType,
                                          @RequestParam(name = "setType") String setType,
                                          @RequestParam(name = "currentPage", required = false, defaultValue = "1") long currentPage,
                                          @RequestParam(name = "pageSize", required = false, defaultValue = "20") long pageSize) {

        Assert.isTrue(StrUtil.isNotBlank(id)
                && StrUtil.isNotBlank(dataType)
                && StrUtil.isNotBlank(setType), "数据集id, 数据类型, 数据集类型类型不能为空");

        return dataSetService.queryVOById(new BigDecimal(id), dataType, setType, currentPage, pageSize);
    }

    @GetMapping("/queryById")
    @ApiOperation(value = "查询DataSet详情")
    @AuthIgnore
    public DataSet queryId(@RequestParam("id") String id) {
        return dataSetService.queryById(id);
    }

    @GetMapping("/deleteById")
    @ApiOperation(value = "删除数据集")
    public Map<String, Object> deleteById(@RequestParam(name = "dataSetId") String dataSetId) {
        dataSetService.deleteById(new BigDecimal(dataSetId));
        Map<String, Object> result = new HashMap<>(2);
        result.put("success", true);
        result.put("message", "删除成功");
        return result;
    }


    @PostMapping("/callIncr")
    @ApiOperation(value = "数据集调用+1")
    public Map<String, Object> callIncr(@RequestParam(name = "dataSetId") String dataSetId) {
        dataSetService.callIncr(new BigDecimal(dataSetId));
        Map<String, Object> result = new HashMap<>(2);
        result.put("success", true);
        result.put("message", "调用成功");
        return result;
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增DataSet")
    public Map<String, Object> add(@RequestBody DataSet dataSet) {
        dataSetService.add(dataSet);
        Map<String, Object> result = new HashMap<>(2);
        result.put("success", true);
        result.put("message", "新增成功");
        return result;
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改数据集")
    public Map<String, Object> update(@RequestBody DataSet dataSet) {
        dataSetService.update(dataSet);
        Map<String, Object> result = new HashMap<>(2);
        result.put("success", true);
        result.put("message", "修改成功");
        return result;
    }


    @GetMapping("/getDocList")
    @ApiOperation(value = "获取当前用户远程数据集列表")
    public String getDocList() {
        return dataSetService.getDocList();
    }


    @PostMapping("/addFromKnowledgeBase")
    @ApiOperation(value = "新增数据集从知识库")
    public void addFromKnowledgeBase(@RequestBody DataSetAndPrInfoVO vo) {
        Assert.isTrue(ObjUtil.isNotNull(vo.getRemoteFilePath()), "请选择数据集文件");
        Assert.isTrue(ObjUtil.isNotNull(vo.getDataSetName()), "请输入数据集名称");
        Assert.isTrue(ObjUtil.isNotNull(vo.getUploadDir()), "数据集上传目录不能为空");
        dataSetService.addFromKnowledgeBase(vo, vo.getUploadDir());
    }

    @PostMapping("/projSpace/addFromKnowledgeBase")
    @ApiOperation(value = "新增项目空间数据集从知识库")
    public void addProjSpaceFromKB(@RequestBody DataSetAndPrInfoVO vo) {
        Assert.isTrue(ObjUtil.isNotNull(vo.getRemoteFilePath()), "请选择数据集文件");
        Assert.isTrue(ObjUtil.isNotNull(vo.getDataSetName()), "请输入数据集名称");
        Assert.isTrue(ObjUtil.isNotNull(vo.getUploadDir()), "数据集上传目录不能为空");
        Assert.isTrue(ObjUtil.isNotNull(vo.getProjectId()), "项目ID不能为空");
        dataSetService.addProjSpaceFromKB(vo, vo.getUploadDir());
    }

    @PostMapping("/addIntentionRecognition")
    @ApiOperation(value = "新增意图识别数据集从知识库")
    public void addIntentionRecognition(@RequestBody DataSetAndPrInfoVO vo) {
        dataSetService.addIntentionRecognition(vo);
    }

    @PostMapping("/intentionRecognitionCorpusList")
    @ApiOperation(value = "意图识别语料查询")
    public IntentRecognitionCorpusResp intentionRecognitionCorpusList(@RequestBody IntentRecognitionCorpusReq req) {
        return dataSetService.intentionRecognitionCorpusList(req);
    }

    @PostMapping("/info3cTreeList")
    @ApiOperation(value = "3c信息查询")
    public Info3cTreeResp info3cTreeList() {
        return dataSetService.info3cTreeList();
    }

}
