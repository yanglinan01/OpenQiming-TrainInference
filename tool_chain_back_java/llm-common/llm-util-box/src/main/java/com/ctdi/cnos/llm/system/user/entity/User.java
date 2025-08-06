package com.ctdi.cnos.llm.system.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ctdi.cnos.llm.base.object.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 实体对象。
 *
 * @author huangjinhua
 * @since 2024/07/22
 */
@Getter
@Setter
@TableName("meta.mm_user")
@ApiModel(value = "User对象", description = "")
public class User extends BaseModel {

    @ApiModelProperty(value = "ID", required = true)
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "中国电信人力编号", required = true)
    @TableField("employee_number")
    private String employeeNumber;

    @ApiModelProperty(value = "真实姓名", required = true)
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "用户名", required = true)
    @TableField("user_name")
    private String userName;

    @ApiModelProperty(value = "区域编码")
    @TableField("region_code")
    private String regionCode;

    @ApiModelProperty(value = "区域名称")
    @TableField("region_name")
    private String regionName;

    @ApiModelProperty(value = "权限编码")
    @TableField("major_code")
    private String majorCode;

    @ApiModelProperty(value = "权限")
    @TableField("major_name")
    private String majorName;

    @ApiModelProperty(value = "手机号")
    @TableField("mobile")
    private String mobile;

    @ApiModelProperty(value = "邮箱")
    @TableField("email")
    private String email;

    @ApiModelProperty(value = "部门名称")
    @TableField("dept_name")
    private String deptName;

    @ApiModelProperty(value = "公司名称")
    @TableField("corp_name")
    private String corpName;

    @ApiModelProperty(value = "集团下的分公司名称，由corp_name解析二级获得")
    @TableField("group_branch")
    private String groupBranch;
    @ApiModelProperty(value = "用户归属（人力编号后2位）")
    @TableField("belong")
    private String belong;

    @ApiModelProperty(value = "是否合理")
    @TableField("is_valid")
    private Long isValid;

    @ApiModelProperty(value = "最新活动时间")
    @TableField("last_active_time")
    private Date lastActiveTime;

    @ApiModelProperty(value = "用户角色，字典USER_ROLE(1管理员，2区域管理员，3普通用户)")
    @TableField("role")
    private String role;

    @ApiModelProperty(value = "是否有系统管理权限,字典YSE_OR_NO（0是，1否）")
    @TableField("system_auth")
    private String systemAuth;
    @ApiModelProperty(value = "是否有工具链平台权限,字典YSE_OR_NO（0是，1否）")
    @TableField("tool_auth")
    private String toolAuth;

    @ApiModelProperty(value = "是否有智能体还开放平台权限,字典YSE_OR_NO（0是，1否）")
    @TableField("agent_auth")
    private String agentAuth;
}