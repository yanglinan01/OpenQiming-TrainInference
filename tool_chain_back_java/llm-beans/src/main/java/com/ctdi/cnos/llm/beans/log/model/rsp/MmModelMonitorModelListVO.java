package com.ctdi.cnos.llm.beans.log.model.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author caojunhao
 * @DATE 2024/7/8
 */
@Data
@ApiModel("接口调用统计列表")
public class MmModelMonitorModelListVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "训练任务id")
    private Long taskId;

    @ApiModelProperty(value = "模型名称")
    private String taskName;

    @ApiModelProperty(value = "接口调用时间")
    private Date intfCallDate;

    @ApiModelProperty(value = "接口调用状态;0:成功,1:失败")
    private Integer intfCallType;

    @ApiModelProperty(value = "接口调用状态")
    private String intfCallTypeStr;

    @ApiModelProperty(value = "备注：成功失败原因")
    private String remark;
}
