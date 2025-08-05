package com.ctdi.cnos.llm.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 用户登录日志 实体对象。
 *
 * @author laiqi
 * @since 2024/10/15
 */
@Getter
@Setter
@TableName("log.mm_user_login_log")
@ApiModel(value = "UserLoginLog对象", description = "用户登录日志")
public class UserLoginLog {
    /**
     * 主键
     */
	@ApiModelProperty(value = "主键", required = true)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户ID
     */
	@ApiModelProperty(value = "用户ID", required = true)
    @TableField("user_id")
    private Long userId;

    /**
     * 登录日期
     */
	@ApiModelProperty(value = "登录日期", required = true)
    @TableField("login_date")
    private Date loginDate;

    /**
     * 区域编码
     */
    @ApiModelProperty(value = "区域编码")
    @TableField("region_code")
    private String regionCode;

}