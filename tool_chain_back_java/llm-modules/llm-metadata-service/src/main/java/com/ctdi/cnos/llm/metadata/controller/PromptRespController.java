package com.ctdi.cnos.llm.metadata.controller;


import com.ctdi.cnos.llm.beans.meta.prompt.PromptResp;
import com.ctdi.cnos.llm.metadata.service.PromptRespService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 问答详情(PromptResp)表控制层
 * @author wangyb
 * @since 2024-05-15 14:06:52
 */
@Api(tags = {"PromptRespController接口"})
@RestController
@RequestMapping(value = "promptResp")
public class PromptRespController {

    @Autowired
    PromptRespService promptRespService;

    @PostMapping("/queryByDataSetId")
    @ApiOperation(value = "查找问答对根据数据集ID")
    public PromptResp queryByDataSetId(BigDecimal dataSetId) {
        return promptRespService.queryByDataSetId(dataSetId);
    }

    @GetMapping("/deleteByDataSetId")
    @ApiOperation(value = "删除问答对根据数据集ID")
    public Map<String, Object> deleteByDataSetId(BigDecimal dataSetId) {
        promptRespService.deleteByDataSetId(dataSetId);
        Map<String, Object> result = new HashMap<>(2);
        result.put("success", true);
        result.put("message", "删除成功");
        return result;
    }


}

