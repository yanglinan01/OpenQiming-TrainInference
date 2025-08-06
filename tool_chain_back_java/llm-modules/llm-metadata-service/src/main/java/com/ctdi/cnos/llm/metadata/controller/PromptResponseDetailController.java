package com.ctdi.cnos.llm.metadata.controller;

import com.ctdi.cnos.llm.beans.meta.prompt.PromptResponseDetail;
import com.ctdi.cnos.llm.metadata.service.PromptResponseDetailService;
import com.ctdi.cnos.llm.util.CommonResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author yuyong
 * @date 2024/8/19 11:19
 */
@Api(tags = "问答测试数据集详情接口", value = "PromptResponseDetailController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/promptResponseDetail")
public class PromptResponseDetailController {

    private final PromptResponseDetailService service;


    /**
     * 问答测试数据集批量新增
     * @param paramList
     * @return
     */
    @ApiOperation(value = "批量问答测试数据集新增", notes = "批量问答测试数据集新增")
    @PostMapping("/addBatch")
    public Map<String, Object> addBatch(@RequestBody List<PromptResponseDetail> paramList) {
        service.insertBatch(paramList);
        return CommonResponseUtil.responseMap(true, "批量新增问答测试数据集成功！");
    }
}
