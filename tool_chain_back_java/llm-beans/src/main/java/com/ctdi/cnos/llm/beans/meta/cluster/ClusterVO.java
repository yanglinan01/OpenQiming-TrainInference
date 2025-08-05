package com.ctdi.cnos.llm.beans.meta.cluster;

import com.ctdi.cnos.llm.base.object.BaseVO;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 集群资源 Vo对象。
 *
 * @author huangjinhua
 * @since 2024/09/24
 */
@ApiModel(description = "ClusterVO对象")
@Data
public class ClusterVO extends BaseVO {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 编码
     */
    @ApiModelProperty(value = "编码")
    private String code;

    /**
     * 省份
     */
    @ApiModelProperty(value = "省份")
    private String province;

    /**
     * Prometheus监控服务地址http://ip:port
     */
    @ApiModelProperty(value = "Prometheus监控服务地址http://ip:port")
    private String prometheusHostUrl;

    /**
     * 训练任务服务地址http://ip:port
     */
    @ApiModelProperty(value = "训练任务服务地址http://ip:port")
    private String trainHostUrl;

    /**
     * 部署任务服务地址http://ip:port
     */
    @ApiModelProperty(value = "部署任务服务地址http://ip:port")
    private String deployHostUrl;

    /**
     * 推理服务地址http://ip:port
     */
    @ApiModelProperty(value = "推理服务地址http://ip:port")
    private String reasonHostUrl;

    /**
     * 获取集群节点信息服务地址http://ip:port
     */
    @ApiModelProperty(value = "获取集群节点信息服务地址http://ip:port")
    private String nodeHostUrl;

}