package com.ctdi.cnos.llm.beans.train.deployTask;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 部署任务(DeployTask)实体类
 *
 * @author wangyb
 * @since 2024-07-01 14:21:48
 */

@Data
@ApiModel("DeployTaskVO")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DeployTaskVO extends DeployTask {
    @ApiModelProperty("模型部署Id")
    private String deployTaskId;

    @ApiModelProperty("部署目标位置")
    private String deployTargetName;

    @ApiModelProperty(value = "权限sql")
    @JsonIgnore
    private String dataScopeSql;

    @ApiModelProperty(value = "模型名称（训练任务名称）")
    private String modelName;

    @ApiModelProperty(value = "部署状态名称", example = "部署中")
    private String statusName;

    @ApiModelProperty(value = "模型训练方法")
    private String modelTrainMethod;

    @ApiModelProperty(value = "基模名称")
    private String baseModelName;

    @ApiModelProperty(value = "基模Id")
    private String baseModelId;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "人力工号")
    private String employeeNumber;

    @ApiModelProperty(value = "部署任务归属 1：工具链 2：项目空间")
    private String deployBelong;

    @ApiModelProperty(value = "项目空间id")
    private String projectSpaceId;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "项目空间id")
    private String spaceId;

    @ApiModelProperty(value = "出入参 ")
    private String accessParameters;

    @ApiModelProperty(value = "训练类型 ")
    private String trainType;
}