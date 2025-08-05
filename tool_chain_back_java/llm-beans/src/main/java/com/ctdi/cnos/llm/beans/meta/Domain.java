package com.ctdi.cnos.llm.beans.meta;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 域实体类
 *
 *
 */
@Data
@ApiModel("域实体类")
public class Domain implements Serializable {
	private static final long serialVersionUID = -12685702896326562L;
	
	/**
	 *  db_column: id
	 */
	@ApiModelProperty("")
	protected Long id;
	/**
	 *  db_column: code
	 */
	@ApiModelProperty("")
	protected String code;
	/**
	 *  db_column: name
	 */
	@ApiModelProperty("")
	protected String name;
	/**
	 *  db_column: creator_id
	 */
	@ApiModelProperty("")
	protected Long creatorId;
	/**
	 *  db_column: create_date
	 */
	@ApiModelProperty("")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	protected Date createDate;
	/**
	 *  db_column: modifier_id
	 */
	@ApiModelProperty("")
	protected Long modifierId;
	/**
	 *  db_column: modify_date
	 */
	@ApiModelProperty("")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	protected Date modifyDate;
	/**
	 *  db_column: is_valid
	 */
	@ApiModelProperty("")
	protected Integer isValid;
	/**
	 *  db_column: notes
	 */
	@ApiModelProperty("")
	protected String notes;
	/**
	 *  db_column: domain_type_id
	 */
	@ApiModelProperty("")
	protected Integer domainTypeId;

	protected String isValidName;

	protected String domainTypeName;
}
