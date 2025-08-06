package com.ctdi.cnos.llm.beans.meta.cluster;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.util.Date;

/**
 * 集群指标 实体对象。
 *
 * @author huangjinhua
 * @since 2024/09/25
 */
@FieldNameConstants
@Getter
@Setter
@TableName("meta.mm_cluster_metric")
@ApiModel(value = "ClusterMetric对象", description = "集群指标")
public class ClusterMetric {
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
     * 实体字段名
     */
    @ApiModelProperty(value = "实体字段名")
    @TableField("entry_field")
    private String entryField;

    /**
     * 集群编码 meta.mm_cluster.code
     */
    @ApiModelProperty(value = "集群编码 meta.mm_cluster.code")
    @TableField("cluster_code")
    private String clusterCode;

    /**
     * 分类，资源计数RESOURCE_COUNT；集群资源使用情况RESOURCE_USAGE;集群详情CLUSTER_DETAIL;集群使用趋势CLUSTER_USAGE_TREND;
     */
    @ApiModelProperty(value = "分类，资源计数RESOURCE_COUNT；集群资源使用情况RESOURCE_USAGE;集群详情CLUSTER_DETAIL;集群使用趋势CLUSTER_USAGE_TREND;")
    @TableField("category")
    private String category;

    /**
     * 表达式
     */
    @ApiModelProperty(value = "表达式")
    @TableField("expression")
    private String expression;

    /**
     * 指标单位
     */
    @ApiModelProperty(value = "指标单位")
    @TableField("unit")
    private String unit;
    /**
     * 结果是否是列表（0是，1否）
     */
    @ApiModelProperty(value = "结果是否是列表（0是，1否）")
    @TableField("result_list")
    private String resultList;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @TableField("remarks")
    private String remarks;

    /**
     * 创建时间。
     */
    @ApiModelProperty(value = "创建时间", example = "2024-07-02 16:49:53.950")
    @TableField(value = "create_date", fill = FieldFill.INSERT)
    private Date createDate;

    /**
     * 更新时间。
     */
    @ApiModelProperty(value = "创建时间", example = "2024-07-02 16:49:53.950")
    @TableField(value = "modify_date", fill = FieldFill.INSERT_UPDATE)
    private Date modifyDate;

}