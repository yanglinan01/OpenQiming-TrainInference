package com.ctdi.cnos.llm.beans.register;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * api生成模板(DcoosApiTemplate)实体类
 *
 * @author wangyb
 * @since 2024-04-17 14:17:17
 */

@Data
@ApiModel("DcoosApiTemplate实体类")
public class DcoosApiTemplate {
    private static final long serialVersionUID = -60599577295295935L;
    /**
     * id
     */
    @ApiModelProperty(value = "id", example = "1")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal id;
    /**
     * 模板名称
     */
    @ApiModelProperty(value = "模板名称", example = "FormData请求DCOOS模板")
    private String name;
    /**
     * 模板编码
     */
    @ApiModelProperty(value = "模板编码", example = "swaggerTemplateBuildFormData")
    private String code;
    /**
     * 模板内容
     */
    @ApiModelProperty(value = "模板内容", example = "string")
    private String body;
    /**
     * 响应体
     */
    @ApiModelProperty(value = "响应体", example = "json")
    private String responseBody;
    /**
     * 是否有效(数据字典:是10050001;10050001否)
     */
    @ApiModelProperty(value = "是否有效(数据字典:是10050001;10050001否)", example = "10050001")
    private String isValid;
    /**
     * 创建人id
     */
    @ApiModelProperty(value = "创建人id", example = "-1")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal creatorId;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", example = "2024-04-16 16:49:53.950")
    private Date createDate;
    /**
     * 修改人id
     */
    @ApiModelProperty(value = "修改人id", example = "-1")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigDecimal modifierId;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间", example = "2024-04-16 16:49:53.950")
    private Date modifyDate;
    /**
     * 是否删除(数据字典:是10050001;10050001否)
     */
    @ApiModelProperty(value = "是否删除(数据字典:是10050001;10050001否)", example = "10050002")
    private String isDelete;


}

