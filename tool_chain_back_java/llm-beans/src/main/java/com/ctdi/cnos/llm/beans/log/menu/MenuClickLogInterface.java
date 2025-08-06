package com.ctdi.cnos.llm.beans.log.menu;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 菜单点击日志 实体对象。--外部调用接口对象
 *
 * @author huangjinhua
 * @since 2024/10/16
 */
@Getter
@Setter
@ApiModel(value = "MenuClickLog 接口对象", description = "菜单点击日志")
public class MenuClickLogInterface {

    /**
     * 中国电信人力编号
     */
    @ApiModelProperty(value = "中国电信人力编号", required = true)
    private String employeeNumber;

    /**
     * 菜单url
     */
    @ApiModelProperty(value = "菜单url")
    private String menuUrl;

    /**
     * 菜单名称
     */
    @ApiModelProperty(value = "菜单名称")
    private String menuName;


    /**
     * 来源（tool：工具链；agent：智能体）
     */
    @ApiModelProperty(value = "来源（tool：工具链；agentplatform：智能体）", required = true)
    private String source;
}