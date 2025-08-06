/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.metadata.controller;

import cn.hutool.core.collection.CollUtil;
import com.ctdi.cnos.llm.base.constant.MetaDataConstants;
import com.ctdi.cnos.llm.beans.meta.model.ModelTrainParam;
import com.ctdi.cnos.llm.beans.meta.model.ModelTrainParamVO;
import com.ctdi.cnos.llm.metadata.service.ModelTrainParamService;
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
 * 模型训练超参 控制层
 *
 * @author huangjinhua
 * @since 2024/5/14
 */
@Api(tags = {"模型训练超参"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/modelTrainParam")
public class ModelTrainParamController {

    private final ModelTrainParamService modelTrainParamService;

    @ApiOperation(value = "查询模型训练超参配置列表", notes = "查询模型训练超参配置列表")
    @GetMapping("/queryList")
    public List<ModelTrainParamVO> queryList(ModelTrainParamVO paramVO) {
        if (Objects.nonNull(paramVO.getModelId())) {
            paramVO.setType(MetaDataConstants.MODEL_TRAIN_SPECIFICAL);
        }
        if (MetaDataConstants.MODEL_TRAIN_NORMAL.equals(paramVO.getType())) {
            paramVO.setModelId(null);
        }
        return modelTrainParamService.queryList(paramVO);
    }

    @ApiOperation(value = "新增模型训练超参配置", notes = "新增模型训练超参配置")
    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody ModelTrainParam param) {
        if (Objects.isNull(param.getModelTrainId())) {
            return CommonResponseUtil.responseMap(false, "超参配置的模型训练关系ID[modelTrainId]不能为空！");
        }
        if (Objects.isNull(param.getDisplayName())) {
            return CommonResponseUtil.responseMap(false, "超参配置的前端显示名[displayName]不能为空！");
        }
        modelTrainParamService.insert(param);
        return CommonResponseUtil.responseMap(true, "新增模型训练超参配置成功！");
    }

    @ApiOperation(value = "批量模型训练新增超参配置", notes = "批量模型训练新增超参配置")
    @PostMapping("/addBatch")
    public Map<String, Object> addBatch(@RequestBody List<ModelTrainParam> paramList) {
        if (CollUtil.isNotEmpty(paramList)) {
            for (ModelTrainParam param : paramList) {
                if (Objects.isNull(param.getModelTrainId())) {
                    return CommonResponseUtil.responseMap(false, "超参配置的模型训练关系ID[modelTrainId]不能为空！");
                }
                if (Objects.isNull(param.getDisplayName())) {
                    return CommonResponseUtil.responseMap(false, "超参配置的前端显示名[displayName]不能为空！");
                }
            }
        }

        modelTrainParamService.insertBatch(paramList);
        return CommonResponseUtil.responseMap(true, "批量新增模型训练超参配置成功！");
    }

    @ApiOperation(value = "修改模型训练超参配置", notes = "修改模型训练超参配置")
    @PostMapping("/updateById")
    public Map<String, Object> updateById(@RequestBody ModelTrainParam param) {
        if (Objects.isNull(param.getId())) {
            return CommonResponseUtil.responseMap(false, "超参配置的ID[id]不能为空！");
        }
        if (Objects.isNull(param.getModelTrainId())) {
            return CommonResponseUtil.responseMap(false, "超参配置的模型训练关系ID[modelTrainId]不能为空！");
        }
        if (Objects.isNull(param.getDisplayName())) {
            return CommonResponseUtil.responseMap(false, "超参配置的前端显示名[displayName]不能为空！");
        }

        modelTrainParamService.updateById(param);
        return CommonResponseUtil.responseMap(true, "修改模型训练配置成功！");
    }

    @ApiOperation(value = "删除模型训练超参配置", notes = "删除模型训练超参配置")
    @DeleteMapping("/deleteById")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "超参配置ID", paramType = "param")
    })
    public Map<String, Object> deleteById(@RequestParam("id") Long id) {
        if (Objects.isNull(id)) {
            return CommonResponseUtil.responseMap(false, "超参配置的ID[id]不能为空！");
        }
        modelTrainParamService.deleteById(id);
        return CommonResponseUtil.responseMap(true, "删除模型训练超参配置成功！");
    }

    @ApiOperation(value = "根据模型ID删除模型训练超参配置", notes = "根据模型ID删除模型训练超参配置")
    @DeleteMapping("/deleteByModelId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "modelId", value = "模型ID", paramType = "param")
    })
    public Map<String, Object> deleteByModelId(@RequestParam("modelId") Long modelId) {
        if (Objects.isNull(modelId)) {
            return CommonResponseUtil.responseMap(false, "超参配置的ID[modelId]不能为空！");
        }
        modelTrainParamService.deleteByModelId(modelId);
        return CommonResponseUtil.responseMap(true, "根据模型ID删除模型训练超参配置成功！");
    }
}
