/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.metadata.controller;

import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.annotation.AuthIgnore;
import com.ctdi.cnos.llm.base.constant.MetaDataConstants;
import com.ctdi.cnos.llm.base.constant.SystemConstant;
import com.ctdi.cnos.llm.beans.meta.model.Model;
import com.ctdi.cnos.llm.beans.meta.model.ModelVO;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.metadata.service.ModelService;
import com.ctdi.cnos.llm.system.auth.AuthUtil;
import com.ctdi.cnos.llm.system.user.entity.UserVO;
import com.ctdi.cnos.llm.util.CommonResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 模型 控制层
 *
 * @author huangjinhua
 * @since 2024/5/14
 */
@Api(tags = {"模型广场"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/model")
public class ModelController {

    private final ModelService modelService;

    @ApiOperation(value = "分页查询模型列表", notes = "分页查询模型列表")
    @PostMapping("/queryPage")
    public Map<String, Object> queryPage(@RequestBody ModelVO modelVO) {
        Page<ModelVO> page = new Page<>(modelVO.getCurrentPage(), modelVO.getPageSize());
        List<OrderItem> orderItemList = new ArrayList<>();
        String enableUsage = null;
        if (CharSequenceUtil.isNotBlank(modelVO.getOptimizable())) {
            orderItemList.add(OrderItem.asc("a.optimizable"));
        } else if (CharSequenceUtil.isNotBlank(modelVO.getTrainable())) {
            orderItemList.add(OrderItem.asc("a.trainable"));
            enableUsage = MetaDataConstants.MODEL_AUTH_USAGE_DICT_TRAIN;
        } else if (CharSequenceUtil.isNotBlank(modelVO.getExperience())) {
            orderItemList.add(OrderItem.asc("a.experience"));
            enableUsage = MetaDataConstants.MODEL_AUTH_USAGE_DICT_REASON;
        } else if (CharSequenceUtil.isNotBlank(modelVO.getReasonable())) {
            orderItemList.add(OrderItem.asc("a.reasonable"));
            enableUsage = MetaDataConstants.MODEL_AUTH_USAGE_DICT_REASON;
        } else if (CharSequenceUtil.isNotBlank(modelVO.getDeployable())) {
            orderItemList.add(OrderItem.asc("a.deployable"));
        } else if (CharSequenceUtil.isNotBlank(modelVO.getPublishable())) {
            orderItemList.add(OrderItem.asc("a.publishable"));
        } else {
            enableUsage = MetaDataConstants.MODEL_AUTH_USAGE_DICT_REASON;
        }
        orderItemList.add(OrderItem.asc("a.create_date"));
        page.addOrder(orderItemList);
        //过滤权限
        UserVO user = UserContextHolder.getUser();
        if (SystemConstant.YES.equals(modelVO.getUserScope()) && !AuthUtil.isMock(user) && !AuthUtil.isAdmin(user)) {
            modelVO.setCurrentUserId(user.getId());
            modelVO.setEnableUsage(enableUsage);
        }
        modelService.queryPage(page, modelVO);
        Map<String, Object> result = new HashMap<>(2);
        result.put("total", page.getTotal());
        result.put("rows", page.getRecords());
        return result;
    }

    @ApiOperation(value = "查询模型列表", notes = "查询模型列表")
    @PostMapping("/queryList")
    @AuthIgnore
    public List<ModelVO> queryList(@RequestBody ModelVO modelVO) {
        String enableUsage = null;
        if (CharSequenceUtil.isNotBlank(modelVO.getTrainable())) {
            enableUsage = MetaDataConstants.MODEL_AUTH_USAGE_DICT_TRAIN;
        } else if (CharSequenceUtil.isNotBlank(modelVO.getExperience())) {
            enableUsage = MetaDataConstants.MODEL_AUTH_USAGE_DICT_REASON;
        } else if (CharSequenceUtil.isNotBlank(modelVO.getReasonable())) {
            enableUsage = MetaDataConstants.MODEL_AUTH_USAGE_DICT_REASON;
        } else {
            enableUsage = MetaDataConstants.MODEL_AUTH_USAGE_DICT_REASON;
        }
        //过滤权限
        UserVO user = UserContextHolder.getUser();
        if (SystemConstant.YES.equals(modelVO.getUserScope()) && !AuthUtil.isMock(user) && !AuthUtil.isAdmin(user)) {
            modelVO.setCurrentUserId(user.getId());
            modelVO.setEnableUsage(enableUsage);
        }
        return modelService.queryList(modelVO);
    }

    @ApiOperation(value = "查询模型详情", notes = "查询模型详情")
    @GetMapping("/queryById")
    @AuthIgnore
    public Model queryById(@RequestParam("id") Long id) {
        return modelService.queryById(id);
    }

    @ApiOperation(value = "新增模型", notes = "新增模型")
    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody Model model) {
        if (Objects.isNull(model.getName())) {
            return CommonResponseUtil.responseMap(false, "模型名称不能为空！");
        }
        if (Objects.isNull(model.getAlias())) {
            return CommonResponseUtil.responseMap(false, "模型别名不能为空！");
        }
        if (Objects.isNull(model.getBelong())) {
            return CommonResponseUtil.responseMap(false, "模型归属不能为空！");
        }
        modelService.insert(model);
        return CommonResponseUtil.responseMap(true, "新增成功！");
    }


    @ApiOperation(value = "修改模型", notes = "修改模型")
    @PostMapping("/updateById")
    public Map<String, Object> updateById(@RequestBody Model model) {
        if (Objects.isNull(model.getId())) {
            return CommonResponseUtil.responseMap(false, "模型的ID不能为空！");
        }
        if (Objects.isNull(model.getName())) {
            return CommonResponseUtil.responseMap(false, "模型名称不能为空！");
        }
        modelService.updateById(model);
        return CommonResponseUtil.responseMap(true, "修改成功！");
    }

    @ApiOperation(value = "删除模型", notes = "删除模型")
    @DeleteMapping("/deleteById")
    public Map<String, Object> deleteById(@RequestParam("id") Long id) {
        if (Objects.isNull(id)) {
            return CommonResponseUtil.responseMap(false, "模型的ID不能为空！");
        }
        modelService.deleteById(id);
        return CommonResponseUtil.responseMap(true, "删除成功！");
    }

}
