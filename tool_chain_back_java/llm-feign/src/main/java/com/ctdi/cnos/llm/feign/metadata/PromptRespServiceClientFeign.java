package com.ctdi.cnos.llm.feign.metadata;


import com.ctdi.cnos.llm.RemoteConstont;
import com.ctdi.cnos.llm.beans.meta.prompt.PromptResp;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author wangyb
 * @version 1.0.0
 * @ClassName PromptRespServiceClientFeign.java
 * @Description TODO
 * @createTime 2024-5-17-10:55:00
 */

@Component
@FeignClient(value = RemoteConstont.METADATA_SERVICE_NAME, path = "${cnos.server.llm-metadata-service.contextPath}")
public interface PromptRespServiceClientFeign {

    /**
     * 查找问答对根据数据集ID
     * @param dataSetId dataSetId
     * @return PromptResp
     */
    @PostMapping("/promptResp/queryByDataSetId")
    @ApiOperation(value = "查找问答对根据数据集ID")
    PromptResp queryByDataSetId(BigDecimal dataSetId);


    /**
     * 删除问答对根据数据集ID
     * @param dataSetId dataSetId
     * @return Map<String, Object>
     */
    @GetMapping("/promptResp/deleteByDataSetId")
    @ApiOperation(value = "删除问答对根据数据集ID")
    Map<String, Object> deleteByDataSetId(BigDecimal dataSetId);

}
