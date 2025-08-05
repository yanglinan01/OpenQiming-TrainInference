/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.metadata.controller;

import com.ctdi.cnos.llm.base.constant.MetaDataConstants;
import com.ctdi.cnos.llm.beans.meta.model.ModelTrain;
import com.ctdi.cnos.llm.metadata.service.ModelTrainService;
import com.ctdi.cnos.llm.util.CommonResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 模型训练配置 控制层
 *
 * @author huangjinhua
 * @since 2024/7/3
 */
@Api(tags = {"模型训练配置"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/modelTrain")
public class ModelTrainController {

    private final ModelTrainService modelTrainService;

    @ApiOperation(value = "查询模型训练配置列表", notes = "查询模型训练配置列表")
    @GetMapping("/queryList")
    public List<ModelTrain> queryList(ModelTrain modelTrain) {
        return modelTrainService.queryList(modelTrain);
    }

    @ApiOperation(value = "新增模型训练配置", notes = "新增模型训练配置")
    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody ModelTrain modelTrain) {
        if (Objects.isNull(modelTrain.getType()) || Objects.nonNull(modelTrain.getModelId())) {
            //默认定制化
            modelTrain.setType(MetaDataConstants.MODEL_TRAIN_SPECIFICAL);
        }
        if (MetaDataConstants.MODEL_TRAIN_SPECIFICAL.equals(modelTrain.getType()) && Objects.isNull(modelTrain.getModelId())) {
            return CommonResponseUtil.responseMap(false, "定制化类型的模型ID[modelId]不能为空！");
        }
        if (Objects.isNull(modelTrain.getTrainType())) {
            return CommonResponseUtil.responseMap(false, "训练类型[trainType]不能为空！");
        }
        if (Objects.isNull(modelTrain.getTrainMethod())) {
            return CommonResponseUtil.responseMap(false, "训练方法[trainMethod]不能为空！");
        }
        modelTrainService.insert(modelTrain);
        return CommonResponseUtil.responseMap(true, "新增模型训练配置成功！");
    }


    @ApiOperation(value = "修改模型训练配置", notes = "修改模型训练配置")
    @PostMapping("/updateById")
    public Map<String, Object> updateById(@RequestBody ModelTrain modelTrain) {
        if (Objects.isNull(modelTrain.getId())) {
            return CommonResponseUtil.responseMap(false, "超参配置的ID[id]不能为空！");
        }
        /*if (Objects.isNull(modelTrain.getModelId())) {
            return CommonResponseUtil.responseMap(false, "超参配置的模型ID[modelId]不能为空！");
        }*/

        modelTrainService.updateById(modelTrain);
        return CommonResponseUtil.responseMap(true, "修改模型训练配置成功！");
    }

    @ApiOperation(value = "删除模型训练配置", notes = "删除模型训练配置")
    @DeleteMapping("/deleteById")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "超参配置ID", paramType = "param")
    })
    public Map<String, Object> deleteById(@RequestParam("id") Long id) {
        if (Objects.isNull(id)) {
            return CommonResponseUtil.responseMap(false, "超参配置的ID[id]不能为空！");
        }
        modelTrainService.deleteById(id);
        return CommonResponseUtil.responseMap(true, "删除模型训练配置成功！");
    }

    @ApiOperation(value = "根据模型ID删除模型训练配置", notes = "根据模型ID删除模型训练配置")
    @DeleteMapping("/deleteByModelId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "modelId", value = "模型ID", paramType = "param")
    })
    public Map<String, Object> deleteByModelId(@RequestParam("modelId") Long modelId) {
        if (Objects.isNull(modelId)) {
            return CommonResponseUtil.responseMap(false, "超参配置的ID[modelId]不能为空！");
        }
        modelTrainService.deleteByModelId(modelId);
        return CommonResponseUtil.responseMap(true, "根据模型ID删除模型训练配置成功！");
    }
}
