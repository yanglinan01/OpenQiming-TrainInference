package com.ctdi.cnos.llm.beans.meta.model;

import com.ctdi.cnos.llm.system.user.entity.UserVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author xuwj09
 * @version 1.0
 * @date 2024/12/24 10:33
 * @Description 模型管理权限分配返回
 */
@ApiModel(description = "模型管理权限分配返回")
@Data
public class UserModelResp extends UserVO {


    @ApiModelProperty(value = "训练权限 0：是 1：否 字典YES_OR_NO")
    private String trainAuth;

    @ApiModelProperty(value = "推理权限 0：是 1：否 字典YES_OR_NO")
    private String reasonAuth;

    /**
     * 用户权限列表
     */
    @ApiModelProperty(value = "用户权限列表")
    private List<UserModelVO> userModelVOList;
}
