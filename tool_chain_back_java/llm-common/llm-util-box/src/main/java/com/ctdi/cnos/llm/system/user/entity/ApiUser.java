package com.ctdi.cnos.llm.system.user.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wangyb
 * @date 2024/4/23 0023 16:59
 * @description ApiUser
 */
@Data
@ApiModel("用户信息")
public class ApiUser {

    /**
     *
     * @param id id
     * @param username 用户名
     * @param name 真实姓名
     */
    public ApiUser(String id, String username, String name, String regionName, String regionCode, String employeeNumber) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.regionName = regionName;
        this.regionCode = regionCode;
        this.employeeNumber = employeeNumber;
    }

    public ApiUser(){

    }


    @ApiModelProperty(value = "id", example = "id")
    private String id;

    @ApiModelProperty(value = "中国电信人力编号", example = "xxxx@UE")
    private String employeeNumber;

    @ApiModelProperty(value = "系统中用户名", example = "lxxxxxxssss")
    private String username;

    @ApiModelProperty(value = "真实姓名", example = "xxxx")
    private String name;

    @ApiModelProperty(value = "区域", example = "集团; 北京")
    private String regionName;

    @ApiModelProperty(value = "区域编码", example = "91101000xxxx00000000")
    private String regionCode;

    @ApiModelProperty(value = "权限", example = "[ \"ALL\" ]")
    private String majorName;

    @ApiModelProperty(value = "权限编码", example = "[ \"ALL\" ]")
    private String majorCode;

    @ApiModelProperty(value = "手机号", example = "193xxxxxxx79")
    private String mobile;

    @ApiModelProperty(value = "邮箱", example = "xx@chinatelecom.cn")
    private String email;

    @ApiModelProperty(value = "部门名称", example = "运营商研发部-业务产品中心")
    private String deptName;

    @ApiModelProperty(value = "公司名称", example = "xx集团-中国xx有限公司xx分公司-中国xx公司-xx营业部-xx局")
    private String corpName;

    @ApiModelProperty(value = "细节", example = "测试用户")
    private String discDetail;

    @ApiModelProperty(value = "创建时间", example = "2023-02-2401:02:19")
    private String createTime;

    @ApiModelProperty(value = "创建人", example = "xxxxxxxxxxxxxxx")
    private String createUser;

    @ApiModelProperty(value = "修改人", example = "xxxxxxxxxxxxxxx")
    private String modifier;

    @ApiModelProperty(value = "修改时间", example = "2023-02-2401:02:19")
    private String updateTime;

    @ApiModelProperty(value = "是否合理", example = "")
    private String isValid;

    @ApiModelProperty(value = "场景", example = "")
    private String sence;

    @ApiModelProperty(value = "上次登录", example = "")
    private String lastLogin;

}
