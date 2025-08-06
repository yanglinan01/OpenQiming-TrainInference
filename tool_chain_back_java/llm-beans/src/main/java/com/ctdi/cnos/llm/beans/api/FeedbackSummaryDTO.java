package com.ctdi.cnos.llm.beans.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 网络大模型赞踩统计请求对象。
 *
 * @author laiqi
 * @since 2024/9/26
 */
@ApiModel(value = "网络大模型赞踩统计请求对象")
@Data
public class FeedbackSummaryDTO {

    @ApiModelProperty(name = "开始时间", required = true, example = "2024-06-19 00:00:00")
    @NotNull(message = "开始时间不能为空")
    private Date startTime;

    @ApiModelProperty(name = "结束时间", required = true, example = "2024-06-19 23:59:59")
    @NotNull(message = "结束时间不能为空")
    private Date endTime;

    /**
     * 接口调用序列标识
     */
    private String seqId;
}