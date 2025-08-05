package com.ctdi.cnos.llm.base.object;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * VO对象的公共基类。
 *
 * @author laiqi
 * @since 2024/7/2
 */
@ApiModel("VO对象的公共基类")
@Data
public class BaseVO implements Serializable {

    /**
     * 创建者Id。
     */
    @ApiModelProperty(value = "创建者Id", example = "0")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long creatorId;

    /**
     * 创建时间。
     */
    @ApiModelProperty(value = "创建时间", example = "2024-07-02 16:49:53.950")
    private Date createDate;

    /**
     * 更新者Id。
     */
    @ApiModelProperty(value = "更新者Id", example = "0")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long modifierId;

    /**
     * 更新时间。
     */
    @ApiModelProperty(value = "创建时间", example = "2024-07-02 16:49:53.950")
    private Date modifyDate;
}