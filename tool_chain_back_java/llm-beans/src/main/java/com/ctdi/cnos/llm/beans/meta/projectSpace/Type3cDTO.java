package com.ctdi.cnos.llm.beans.meta.projectSpace;

import com.ctdi.cnos.llm.base.object.Groups;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 字典类型表 DTO对象。
 *
 * @author 
 * @since 2025/06/10
 */
@ApiModel(description = "Type3cDTO对象")
@Data
public class Type3cDTO {

	private Integer id;
	
    /**
     * 类型编码
     */
	@ApiModelProperty(value = "类型编码", required = true)
	@NotBlank(message = "数据验证失败，类型编码不能为空！")			
	private String type;
	
    /**
     * 类型名称
     */
	@ApiModelProperty(value = "类型名称", required = true)
	@NotBlank(message = "数据验证失败，类型名称不能为空！")			
	private String typeName;
	
	private Long creatorId;
	
	private Date createDate;
	
	private Long modifierId;
	
	private Date modifyDate;
	
}