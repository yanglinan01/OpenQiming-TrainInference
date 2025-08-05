package com.ctdi.cnos.llm.beans.meta.cluster;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ctdi.cnos.llm.base.object.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 集群资源 实体对象。
 *
 * @author huangjinhua
 * @since 2024/09/24
 */
@Getter
@Setter
@TableName("meta.mm_cluster")
@ApiModel(value = "Cluster对象", description = "集群资源")
public class Cluster extends BaseModel {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    @TableField("name")
    private String name;

    /**
     * 编码
     */
    @ApiModelProperty(value = "编码")
    @TableField("code")
    private String code;

    /**
     * 省份
     */
    @ApiModelProperty(value = "省份")
    @TableField("province")
    private String province;

    /**
     * Prometheus监控服务地址http://ip:port
     */
    @ApiModelProperty(value = "Prometheus监控服务地址http://ip:port")
    @TableField("prometheus_host_url")
    private String prometheusHostUrl;

    /**
     * 训练任务服务地址http://ip:port
     */
    @ApiModelProperty(value = "训练任务服务地址http://ip:port")
    @TableField("train_host_url")
    private String trainHostUrl;

    /**
     * 部署任务服务地址http://ip:port
     */
    @ApiModelProperty(value = "部署任务服务地址http://ip:port")
    @TableField("deploy_host_url")
    private String deployHostUrl;

    /**
     * 推理服务地址http://ip:port
     */
    @ApiModelProperty(value = "推理服务地址http://ip:port")
    @TableField("reason_host_url")
    private String reasonHostUrl;

    /**
     * 获取集群节点信息服务地址http://ip:port
     */
    @ApiModelProperty(value = "获取集群节点信息服务地址http://ip:port")
    @TableField("node_host_url")
    private String nodeHostUrl;

}