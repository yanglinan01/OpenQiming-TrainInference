package com.ctdi.cnos.llm.controller.metadata;

import com.ctdi.cnos.llm.beans.meta.prompt.PromptResp;
import com.ctdi.cnos.llm.feign.metadata.PromptRespServiceClientFeign;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.util.MessageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 问答详情(PromptResp)表控制层
 * @author wangyb
 * @since 2024-05-15 14:06:52
 */
@Api(tags = {"PromptRespController接口"})
@RestController
@RequestMapping(value = "promptResp")
@Slf4j
public class PromptRespController {

    @Autowired
    PromptRespServiceClientFeign promptRespServiceClientFeign;

    @PostMapping("/queryByDataSetId")
    @ApiOperation(value = "查找问答对根据数据集ID")
    public OperateResult<PromptResp> queryByDataSetId(BigDecimal dataSetId) {
        try {
            PromptResp promptResp = promptRespServiceClientFeign.queryByDataSetId(dataSetId);
            return new OperateResult<>(true, "查找问答对根据数据集ID成功", promptResp);
        } catch (Exception e) {
            log.error("查找问答对根据数据集ID异常", e);
            return new OperateResult<>(false, MessageUtils.getMessage(e.getMessage()), null);
        }
    }


    @GetMapping("/deleteByDataSetId")
    @ApiOperation(value = "删除问答对根据数据集ID")
    public OperateResult<Map<String, Object>> deleteByDataSetId(BigDecimal dataSetId) {
        try {
            promptRespServiceClientFeign.deleteByDataSetId(dataSetId);
            return new OperateResult<>(true, "删除问答对根据数据集ID成功", null);
        } catch (Exception e) {
            log.error("删除问答对根据数据集ID异常", e);
            return new OperateResult<>(false, MessageUtils.getMessage(e.getMessage()), null);
        }
    }


}

