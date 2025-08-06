package com.ctdi.cnos.llm.beans.log.menu;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 菜单点击日志 Vo对象。
 *
 * @author huangjinhua
 * @since 2024/10/16
 */
@ApiModel(description = "MenuClickLogVO对象")
@Data
public class MenuClickLogVO {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

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
     * 点击时间
     */
    @ApiModelProperty(value = "点击时间")
    private Date clickDate;

    /**
     * 区域编码
     */
    @ApiModelProperty(value = "区域编码")
    private String regionCode;

    /**
     * 用户归属（人力账号后两位）
     */
    @ApiModelProperty(value = "用户归属（人力账号后两位）")
    private String userBelong;


    /**
     * 来源（tool：工具链；agent：智能体）
     */
    @ApiModelProperty(value = "来源（tool：工具链；agentplatform：智能体）")
    private String source;

    /**
     * 分组统计
     */
    @ApiModelProperty(value = "分组统计")
    private Integer count;

    /**
     * 用户归属名称
     */
    @ApiModelProperty(value = "用户归属名称")
    private String userBelongName;
}