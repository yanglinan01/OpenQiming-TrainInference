package com.ctdi.cnos.llm.beans.meta.prompt;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yuyong
 * @date 2024/8/15 11:00
 */
@Data
@ApiModel("时序测试数据集详情实体类")
@TableName("meta.mm_prompt_sequential_detail")
public class PromptSequentialDetail {

    /**
     * 序号。给前端使用。
     */
    @ApiModelProperty(value = "serialNumber", example = "1")
    @TableField(exist = false)
    private Integer serialNumber;

    /**
     * id
     */
    @TableId("id")
    @ApiModelProperty(value = "id", example = "example")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal id;
    /**
     * 数据集文件id;数据集_20240514_104036
     */
    @ApiModelProperty(value = "数据集文件id;数据集_20240514_104036", example = "example")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal dataSetFileId;

    /**
     * 链路编号
     */
    @ApiModelProperty(value = "链路编号", example = "example")
    private String circuitId;

    /**
     * 电路名称
     */
    @ApiModelProperty(value = "电路名称", example = "example")
    private String cirName;

    /**
     * A端设备编码
     */
    @ApiModelProperty(value = "A端设备编码", example = "example")
    private String kDevId;

    /**
     * A端端口
     */
    @ApiModelProperty(value = "A端端口", example = "example")
    private String kIntfId;

    /**
     * A端设备IP
     */
    @ApiModelProperty(value = "A端设备IP", example = "example")
    private String devidIp;

    /**
     * b端设备编码
     */
    @ApiModelProperty(value = "B端设备编码", example = "example")
    private String bDevId;

    /**
     * b端端口号
     */
    @ApiModelProperty(value = "B端端口号", example = "example")
    private String bIntfDescr;

    /**
     * b端设备IP
     */
    @ApiModelProperty(value = "B端设备IP", example = "example")
    private String bDevidIp;

    /**
     * 带宽
     */
    @ApiModelProperty(value = "带宽", example = "example")
    private String dCirbw;

    /**
     * 流入流速
     */
    @ApiModelProperty(value = "流入流速", example = "example")
    private String dInFlux;

    /**
     * 流出流速
     */
    @ApiModelProperty(value = "流出流速", example = "example")
    private String dOutFlux;

    /**
     * 流入带宽利用率
     */
    @ApiModelProperty(value = "流入带宽利用率", example = "example")
    private String dInFluxRatio;

    /**
     * 流出带宽利用率
     */
    @ApiModelProperty(value = "流出带宽利用率", example = "example")
    private String dOutFluxRatio;

    /**
     * 采集时间
     */
    @ApiModelProperty(value = "采集时间", example = "example")
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
    private Date tCtime;

    /**
     * 保留字段
     */
    @ApiModelProperty(value = "保留字段", example = "example")
    private String param0;

    /**
     * 保留字段
     */
    @ApiModelProperty(value = "保留字段", example = "example")
    private String param1;

    /**
     * 保留字段
     */
    @ApiModelProperty(value = "文件类型", example = "example")
    private String param2;

    /**
     * 保留字段
     */
    @ApiModelProperty(value = "保留字段", example = "example")
    private String param3;

    /**
     * 保留字段
     */
    @ApiModelProperty(value = "保留字段", example = "example")
    private String param4;

    /**
     * 保留字段
     */
    @ApiModelProperty(value = "保留字段", example = "example")
    private String param5;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态", example = "example")
    private String status;

    /**
     * 创建人id;1
     */
    @ApiModelProperty(value = "创建人id;1", example = "example")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal creatorId;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", example = "example")
    private Date createDate;
    /**
     * 修改人id
     */
    @ApiModelProperty(value = "修改人id", example = "example")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal modifierId;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间", example = "example")
    private Date modifyDate;
}