package com.ctdi.cnos.llm.beans.meta.projectSpace;

import com.ctdi.cnos.llm.base.object.BaseVO;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 字典类型表 Vo对象。
 *
 * @author 
 * @since 2025/06/10
 */
@ApiModel(description = "Type3cVO对象")
@Data
public class Type3cVO {

	private Integer id;
	
    /**
     * 类型编码
     */
    @ApiModelProperty(value = "类型编码")
	private String type;
	
    /**
     * 类型名称
     */
    @ApiModelProperty(value = "类型名称")
	private String typeName;
	
	private String creatorId;
	
	private Date createDate;
	
	private String modifierId;
	
	private Date modifyDate;
	
}