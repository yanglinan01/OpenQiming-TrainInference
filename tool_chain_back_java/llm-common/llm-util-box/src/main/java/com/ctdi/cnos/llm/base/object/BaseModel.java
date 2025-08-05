package com.ctdi.cnos.llm.base.object;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.ctdi.cnos.llm.base.annotation.UserFilterColumn;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体对象的公共基类，所有子类均必须包含基类定义的数据表字段和实体对象字段。
 *
 * @author laiqi
 * @since 2024/7/2
 */
@ApiModel("实体对象的公共基类")
@Data
public class BaseModel implements Serializable {

    /**
     * 整个工程的实体对象中，创建者Id字段的Java对象名。
     */
    public static final String CREATE_USER_ID_FIELD_NAME = "creatorId";
    /**
     * 整个工程的实体对象中，创建时间字段的Java对象名。
     */
    public static final String CREATE_TIME_FIELD_NAME = "createDate";
    /**
     * 整个工程的实体对象中，更新者Id字段的Java对象名。
     */
    public static final String UPDATE_USER_ID_FIELD_NAME = "modifierId";
    /**
     * 整个工程的实体对象中，更新时间字段的Java对象名。
     */
    public static final String UPDATE_TIME_FIELD_NAME = "modifyDate";

    /**
     * 整个工程的实体对象中，区域编码字段的Java对象名。
     */
    public static final String REGION_CODE_FIELD_NAME = "regionCode";

    /**
     * 整个工程的实体对象中，创建者Id字段的列名。
     */
    public static final String CREATE_USER_ID_NAME = "creator_id";
    /**
     * 整个工程的实体对象中，创建时间字段的列名。
     */
    public static final String CREATE_TIME_NAME = "create_date";
    /**
     * 整个工程的实体对象中，更新者Id字段的列名。
     */
    public static final String UPDATE_USER_ID_NAME = "modifier_id";
    /**
     * 整个工程的实体对象中，更新时间字段的列名。
     */
    public static final String UPDATE_TIME_NAME = "modify_date";
    /**
     * 整个工程的实体对象中，区域编码字段的列名。
     */
    public static final String REGION_CODE_NAME = "region_code";


    /**
     * 创建者Id。
     */
    @UserFilterColumn
    @ApiModelProperty(value = "创建者Id", example = "0")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField(value = "creator_id", fill = FieldFill.INSERT)
    private Long creatorId;

    /**
     * 创建时间。
     */
    @ApiModelProperty(value = "创建时间", example = "2024-07-02 16:49:53.950")
    @TableField(value = "create_date", fill = FieldFill.INSERT)
    private Date createDate;

    /**
     * 更新者Id。
     */
    @ApiModelProperty(value = "更新者Id", example = "0")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField(value = "modifier_id", fill = FieldFill.INSERT_UPDATE)
    private Long modifierId;

    /**
     * 更新时间。
     */
    @ApiModelProperty(value = "创建时间", example = "2024-07-02 16:49:53.950")
    @TableField(value = "modify_date", fill = FieldFill.INSERT_UPDATE)
    private Date modifyDate;
}