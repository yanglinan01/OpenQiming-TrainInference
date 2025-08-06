package com.ctdi.cnos.llm.beans.meta.cluster;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 集群指标 Vo对象。
 *
 * @author huangjinhua
 * @since 2024/09/25
 */
@ApiModel(description = "ClusterMetricVO对象")
@Data
public class ClusterMetricVO {

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
     * 实体字段名
     */
    @ApiModelProperty(value = "实体字段名")
    private String entryField;

    /**
     * 集群编码 meta.mm_cluster.code
     */
    @ApiModelProperty(value = "集群编码 meta.mm_cluster.code")
    private String clusterCode;

    /**
     * 分类，资源计数RESOURCE_COUNT；集群资源使用情况RESOURCE_USAGE;集群详情CLUSTER_DETAIL;集群使用趋势CLUSTER_USAGE_TREND;
     */
    @ApiModelProperty(value = "分类，资源计数RESOURCE_COUNT；集群资源使用情况RESOURCE_USAGE;集群详情CLUSTER_DETAIL;集群使用趋势CLUSTER_USAGE_TREND;")
    private String category;

    /**
     * 表达式
     */
    @ApiModelProperty(value = "表达式")
    private String expression;

    /**
     * 指标单位
     */
    @ApiModelProperty(value = "指标单位")
    private String unit;


    /**
     * 结果是否是列表（0是，1否）
     */
    @ApiModelProperty(value = "结果是否是列表（0是，1否）")
    private String resultList;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remarks;

    /**
     * 创建时间。
     */
    @ApiModelProperty(value = "创建时间", example = "2024-07-02 16:49:53.950")
    private Date createDate;

    /**
     * 更新时间。
     */
    @ApiModelProperty(value = "创建时间", example = "2024-07-02 16:49:53.950")
    private Date modifyDate;

}