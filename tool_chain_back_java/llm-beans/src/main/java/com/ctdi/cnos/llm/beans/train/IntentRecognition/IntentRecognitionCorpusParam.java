package com.ctdi.cnos.llm.beans.train.IntentRecognition;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author xuwj09
 * @version 1.0
 * @date 2025/6/5 14:41
 * @Description 意图识别语料属性
 */
@ApiModel(description = "意图识别语料属性")
@Data
public class IntentRecognitionCorpusParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "搜索值", example = "jrw,zhw")
    private String searchValue;

    @ApiModelProperty(value = "角色", example = "")
    private String roleName;

    @ApiModelProperty(value = "创建人", example = "张三")
    private String createBy;

    @ApiModelProperty(value = "创建时间", example = "")
    private Date createTime;

    @ApiModelProperty(value = "更新人", example = "张三")
    private String updateBy;

    @ApiModelProperty(value = "更新时间", example = "")
    private Date updateTime;

    @ApiModelProperty(value = "备注", example = "111")
    private String remark;


    @ApiModelProperty(value = "页号", example = "1")
    private Integer pageNum;

    @ApiModelProperty(value = "每页数量", example = "10")
    private Integer pageSize;

    @ApiModelProperty(value = "orderBy", example = "")
    private String orderBy;

    @ApiModelProperty(value = "params", example = "")
    private Object params;

    @ApiModelProperty(value = "id", example = "")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal id;

    @ApiModelProperty(value = "一级专业", example = "接入网")
    private String firstLabelName;


    @ApiModelProperty(value = "二级专业", example = "装维")
    private String secondLabelName;

    @ApiModelProperty(value = "文件名", example = "意图识别语料模板（省公司填写）.xlsx")
    private String excelName;

    @ApiModelProperty(value = "专业筛选，根据3c信息取code，拼接:一级code,二级code", example = "jrw,zhw")
    private String professionalLabel;

    @ApiModelProperty(value = "状态 0：未审核，1：已审核，2：未通过", example = "0")
    private Integer status;

    @ApiModelProperty(value = "创建时间", example = "")
    private Date createDate;

    @ApiModelProperty(value = "更新时间", example = "接入网")
    private Date updateDate;



    @ApiModelProperty(value = "应用编码", example = "zwyclszyg")
    private String applyCode;

    @ApiModelProperty(value = "文件名", example = "装维预处理数字员工")
    private String applyName;

    @ApiModelProperty(value = "业务/产品编码", example = "kdyw")
    private String businessProductCode;

    @ApiModelProperty(value = "业务/产品名称", example = "宽带业务")
    private String businessProduct;

    @ApiModelProperty(value = "意图分类编码", example = "kdbnsw")
    private String intentClassificationCode;

    @ApiModelProperty(value = "意图分类", example = "宽带不能上网")
    private String intentClassification;

    @ApiModelProperty(value = "用户问题", example = "我家宽带突然不能上网了")
    private String userAnswer;

    @ApiModelProperty(value = "0:全网通用/1:省专用", example = "0")
    private Integer openScope;
}
