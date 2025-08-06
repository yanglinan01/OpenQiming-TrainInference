package com.ctdi.cnos.llm.beans.log.menu;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
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
 * 菜单点击日志 实体对象。
 *
 * @author huangjinhua
 * @since 2024/10/16
 */
@Getter
@Setter
@TableName("log.mm_menu_click_log")
@ApiModel(value = "MenuClickLog对象", description = "菜单点击日志")
public class MenuClickLog {
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
     * 菜单url
     */
    @ApiModelProperty(value = "菜单url")
    @TableField("menu_url")
    private String menuUrl;

    /**
     * 菜单名称
     */
    @ApiModelProperty(value = "菜单名称")
    @TableField("menu_name")
    private String menuName;

    /**
     * 点击时间
     */
    @ApiModelProperty(value = "点击时间", required = true)
    @TableField("click_date")
    private Date clickDate;

    /**
     * 区域编码
     */
    @ApiModelProperty(value = "区域编码")
    @TableField("region_code")
    private String regionCode;

    /**
     * 用户归属（人力账号后两位）
     */
    @ApiModelProperty(value = "用户归属（人力账号后两位）")
    @TableField("user_belong")
    private String userBelong;


    /**
     * 来源（tool：工具链；agent：智能体）
     */
    @ApiModelProperty(value = "来源（tool：工具链；agentplatform：智能体）")
    @TableField("source")
    private String source;

    /**
     * 分组统计
     */
    @ApiModelProperty(value = "分组统计")
    @TableField(value = "count(*)", insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private Integer count;


}