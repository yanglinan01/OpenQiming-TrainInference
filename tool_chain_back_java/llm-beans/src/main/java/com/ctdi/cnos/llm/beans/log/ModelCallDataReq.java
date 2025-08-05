package com.ctdi.cnos.llm.beans.log;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author HuangGuanSheng
 * @date 2024-07-04 14:52
 */
@Data
@ApiModel("模型调用数据入参")
public class ModelCallDataReq {
    @ApiModelProperty("任务ID")
    private Long taskId;
    @ApiModelProperty("输入token数")
    private Long modelInputToken;
    @ApiModelProperty("输出token数")
    private Long modelOutputToken;
    @ApiModelProperty("调用状态0:成功,1:失败")
    private Integer successFlag;
    @ApiModelProperty("备注")
    private String remark;
}
