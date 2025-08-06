package com.ctdi.cnos.llm.beans.train.deployTask;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ctdi.cnos.llm.base.object.BaseModel;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
@ApiModel("DeployTask实体类")
@TableName("train.mm_deploy_task")
public class DeployTask extends BaseModel {
    private static final long serialVersionUID = -23657993574643454L;
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", example = "1806606870")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 模型ID
     */
    @ApiModelProperty(value = "模型ID", example = "1806606870")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long modelId;

    /**
     * 部署状态
     */
    @ApiModelProperty(value = "部署状态", example = "completed")
    private String status;


    /**
     * 模型部署地址
     */
    @ApiModelProperty(value = "模型部署地址", example = "模型部署地址")
    private String deployUrl;

    /**
     * 区域编码
     */
    @ApiModelProperty(value = "区域编码", example = "5000000000000000000")
    private String regionCode;

    /**
     * 是否被智能体引用(0是; 1否)
     */
    @ApiModelProperty(value = "智能体是否引用，字典YES_OR_NO", example = "1")
    private String agentStatus;

    /**
     * 结果信息
     */
    @ApiModelProperty(value = "结果信息", example = "2024-07-01 14:07:06")
    private String result;

    /**
     * 部署接口目标，GZ：贵州，QD青岛
     */
    @ApiModelProperty(value = "部署接口目标，GZ：贵州，QD青岛，mate.mm_cluster.code", example = "k8s")
    private String deployTarget;
    /**
     * 是否已提交到k8s队列，0是，1否 字典YES_OR_NO
     */
    @ApiModelProperty(value = "是否已提交到k8s队列，0是，1否 字典YES_OR_NO", example = "1")
    private String submitStatus;

    /**
     * dcoos能力注册ID
     */
    @ApiModelProperty(value = "dcoos能力注册ID", example = "1")
    private String registerId;


    /**
     * 部署类型(1训练; 2测试)
     */
    @ApiModelProperty(value = "部署类型(1训练; 2测试)", example = "1")
    private String deployType;

    /**
     * 模型是否注册, 字典YES_OR_NO
     */
    @ApiModelProperty(value = "模型是否注册, 字典YES_OR_NO", example = "1")
    private String registerStatus;

    /**
     * 部署任务归属 1：工具链 2：项目空间
     */
    @ApiModelProperty(value = "部署任务归属 1：工具链 2：项目空间", example = "1")
    private String deployBelong;

    /**
     * 项目空间id
     */
    @ApiModelProperty(value = "项目空间id", example = "1")
    private String projectSpaceId;


}