package com.ctdi.cnos.llm.beans.log.model.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author caojunhao
 * @DATE 2024/7/5
 */
@Data
@ApiModel("模型统计调用 req")
public class MmModelMonitorModelReq implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "任务id，不传查询全部")
    private Long taskId;

    @ApiModelProperty(value = "模型输入输出类型：默认0 " +
            "0：全部   1：模型输入   2：模型输出")
    private Integer modelCallType;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;
}
