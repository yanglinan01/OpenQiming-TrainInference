package com.ctdi.cnos.llm.beans.train.trainTask;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xuwj09
 * @version 1.0
 * @date 2024/12/25 13:50
 * @Description
 */
@Data
@ApiModel("智能体项目空间参数")
@Accessors(chain = true)
public class TrainTenantsResp implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID", example = "219d2f38-2c52-48db-a0c4-5bfc70ab6caa")
    private String id;

    @ApiModelProperty(value = "项目空间", example = "默认空间")
    private String name;

    @ApiModelProperty(value = "状态", example = "normal")
    private String status;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date created_at;

    @ApiModelProperty(value = "当前")
    private boolean current;

    @ApiModelProperty(value = "角色", example = "owner")
    private String role;

    @ApiModelProperty(value = "空间描述")
    private String tenant_desc;
}
