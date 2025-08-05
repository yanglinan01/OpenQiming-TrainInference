package com.ctdi.cnos.llm.beans.log.model.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author caojunhao
 * @DATE 2024/7/8
 */
@Data
@ApiModel("接口调用统计 统计图 req")
public class MmModelMonitorIntfReq implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "任务id，不传查询全部")
    private Long taskId;
    @ApiModelProperty(value = "接口调用类型：默认2 " +
            "接口调用状态;0:成功,1:失败,2:全部")
    private Integer intfCallType;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;
}
