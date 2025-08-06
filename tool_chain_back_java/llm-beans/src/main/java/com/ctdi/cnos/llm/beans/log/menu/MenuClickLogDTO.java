package com.ctdi.cnos.llm.beans.log.menu;

import com.ctdi.cnos.llm.base.object.Groups;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 菜单点击日志 DTO对象。
 *
 * @author huangjinhua
 * @since 2024/10/16
 */
@ApiModel(description = "MenuClickLogDTO对象")
@Data
public class MenuClickLogDTO {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    @NotNull(message = "数据验证失败，主键不能为空！", groups = {Groups.UPDATE.class})
    private Long id;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", required = true)
    @NotNull(message = "数据验证失败，用户ID不能为空！")
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
    @ApiModelProperty(value = "点击时间", required = true)
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
}