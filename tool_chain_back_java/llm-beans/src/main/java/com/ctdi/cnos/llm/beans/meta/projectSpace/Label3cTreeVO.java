package com.ctdi.cnos.llm.beans.meta.projectSpace;

import com.ctdi.cnos.llm.base.object.BaseVO;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 标签树表 Vo对象。
 *
 * @author 
 * @since 2025/06/10
 */
@ApiModel(description = "Label3cTreeVO对象")
@Data
public class Label3cTreeVO {

	private Integer id;
	
    /**
     * 关联的字典类型ID
     */
    @ApiModelProperty(value = "关联的字典类型")
	private String type;
	
    /**
     * 标签编码
     */
    @ApiModelProperty(value = "标签编码")
	private String code;
	
    /**
     * 标签名称
     */
    @ApiModelProperty(value = "标签名称")
	private String name;
	
    /**
     * 层级
     */
    @ApiModelProperty(value = "层级")
	private String level;
	
    /**
     * 父级ID，-1表示顶级
     */
    @ApiModelProperty(value = "父级ID，-1表示顶级")
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