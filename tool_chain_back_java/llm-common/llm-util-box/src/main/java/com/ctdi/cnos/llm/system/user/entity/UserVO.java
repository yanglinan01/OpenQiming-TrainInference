package com.ctdi.cnos.llm.system.user.entity;

import com.ctdi.cnos.llm.base.object.BaseVO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * Vo对象。
 *
 * @author huangjinhua
 * @since 2024/07/22
 */
@ApiModel(description = "UserVo对象")
@Data
@Accessors(chain = true)
public class UserVO extends BaseVO {

    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 中国电信人力编号
     */
    @ApiModelProperty(value = "中国电信人力编号")
    private String employeeNumber;

    /**
     * 真实姓名
     */
    @ApiModelProperty(value = "真实姓名")
    private String name;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String userName;

    /**
     * 区域编码
     */
    @ApiModelProperty(value = "区域编码")
    private String regionCode;

    /**
     * 区域名称
     */
    @ApiModelProperty(value = "区域名称")
    private String regionName;

    /**
     * 权限编码
     */
    @ApiModelProperty(value = "权限编码")
    private String majorCode;

    /**
     * 权限
     */
    @ApiModelProperty(value = "权限")
    private String majorName;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String mobile;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    private String email;

    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称")
    private String deptName;

    /**
     * 公司名称
     */
    @ApiModelProperty(value = "公司名称")
    private String corpName;

    /**
     * 集团下的分公司名称，由corp_name解析二级获得
     */
    @ApiModelProperty(value = "集团下的分公司名称，由corp_name解析二级获得")
    private String groupBranch;

    /**
     * 用户归属（人力编号后2位）
     */
    @ApiModelProperty(value = "用户归属（人力编号后2位）")
    private String belong;

    /**
     * 是否合理
     */
    @ApiModelProperty(value = "是否合理")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long isValid;

    /**
     * 最新活动时间
     */
    @ApiModelProperty(value = "最新活动时间")
    private Date lastActiveTime;

    /**
     * 用户角色，字典USER_ROLE(1管理员，2区域管理员，3普通用户)
     */
    @ApiModelProperty(value = "用户角色，字典USER_ROLE(1管理员，2区域管理员，3普通用户)")
    @JsonIgnore
    private String role;
    /**
     * 是否有系统管理权限,字典YSE_OR_NO（0是，1否）
     */
    @ApiModelProperty(value = "是否有系统管理权限,字典YSE_OR_NO（0是，1否）")
    private String systemAuth;
    /**
     * 是否有工具链平台权限,字典YSE_OR_NO（0是，1否）
     */
    @ApiModelProperty(value = "是否有工具链平台权限,字典YSE_OR_NO（0是，1否）")
    private String toolAuth;

    /**
     * 是否有智能体还开放平台权限,字典YSE_OR_NO（0是，1否）
     */
    @ApiModelProperty(value = "是否有智能体还开放平台权限,字典YSE_OR_NO（0是，1否）")
    private String agentAuth;

    /**
     * 是否是管理员
     */
    @ApiModelProperty(value = "是否是管理员")
    private Boolean isAdmin;

    /**
     * 是否是管理员
     */
    @ApiModelProperty(value = "是否是模拟用户")
    @JsonIgnore
    private Boolean isMock;

    /**
     * 菜单权限，0 无任何权限，1：工具链权限，2：智能体权限，3：工具链权限+智能体权限
     */
    @ApiModelProperty(value = "菜单权限，0 无任何权限，1：工具链权限，2：智能体权限，3：工具链权限+智能体权限")
    private String menuType;

}