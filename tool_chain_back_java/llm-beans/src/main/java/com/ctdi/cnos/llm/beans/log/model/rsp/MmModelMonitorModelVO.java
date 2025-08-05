package com.ctdi.cnos.llm.beans.log.model.rsp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author caojunhao
 * @DATE 2024/7/5
 */
@Data
@ApiModel("模型调用统计 VO")
public class MmModelMonitorModelVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "模型调用token数/调用次数")
    private Long totalToken;
    @ApiModelProperty(value = "模型调用时间")
    private Date modelCallDate;
}
