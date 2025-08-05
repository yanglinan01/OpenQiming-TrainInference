package com.ctdi.cnos.llm.beans.meta.dataSet;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 数据集上传文件(DataSetFile)实体类
 * @author wangyb
 * @since 2024-05-24 11:22:12
 */

@Data
@ApiModel("DataSetFile实体类")
@TableName("meta.mm_data_set_file")
public class DataSetFile {
    private static final long serialVersionUID = 236038719667138873L;
    /**
     * id
     */
    @TableId("id")
    @ApiModelProperty(value = "id", example = "example")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal id;
    /**
     * 请求id;数据集_20240514_104036
     */
    @ApiModelProperty(value = "请求id;数据集_20240514_104036", example = "example")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal requestId;
    /**
     * 文件类型
     */
    @ApiModelProperty(value = "文件类型", example = "example")
    private String templateType;
    /**
     * 数据类型;Prompt+Response
     */
    @ApiModelProperty(value = "保存地址", example = "example")
    private String savePath;
    /**
     * 对话数据量;1222
     */
    @ApiModelProperty(value = "文件大小", example = "example")
    private Long fileSize;
    /**
     * 数据集分类
     */
    @ApiModelProperty(value = "对话数", example = "example")
    private Long prCount;
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
    /**
     * 是否有效
     */
    @ApiModelProperty(value = "是否有效", example = "example")
    private String isValid;
    /**
     * 是否删除
     */
    @ApiModelProperty(value = "是否删除", example = "example")
    private String isDelete;

    /**
     * 归属(0知识库;1用户上传)
     */
    @ApiModelProperty(value = "归属(1知识库;2用户上传)", example = "1")
    private String belong;


}

