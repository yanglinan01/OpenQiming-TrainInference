package com.ctdi.cnos.llm.beans.meta.projectSpace;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 标签树表 DTO对象。
 *
 * @author 
 * @since 2025/06/10
 */
@ApiModel(description = "Label3cTreeDTO对象")
@Data
public class Label3cTreeDTO {

	private Integer id;
	
    /**
     * 关联的字典类型ID
     */
	@ApiModelProperty(value = "关联的字典", required = true)
	private String type;
	
    /**
     * 标签编码
     */
	@ApiModelProperty(value = "标签编码", required = true)
	@NotBlank(message = "数据验证失败，标签编码不能为空！")			
	private String code;
	
    /**
     * 标签名称
     */
	@ApiModelProperty(value = "标签名称", required = true)
	@NotBlank(message = "数据验证失败，标签名称不能为空！")			
	private String name;
	
    /**
     * 层级
     */
	@ApiModelProperty(value = "层级", required = true)
	@NotBlank(message = "数据验证失败，层级不能为空！")			
	private String level;
	
    /**
     * 父级ID，-1表示顶级
     */
	@ApiModelProperty(value = "父级ID，-1表示顶级", required = true)
	private Integer parentId;
	
    /**
     * 备注说明
     */
    @ApiModelProperty(value = "备注说明")
	private String remark;
	
	private Long creatorId;
	
	private Date createDate;
	
	private Long modifierId;
	
	private Date modifyDate;
	
}