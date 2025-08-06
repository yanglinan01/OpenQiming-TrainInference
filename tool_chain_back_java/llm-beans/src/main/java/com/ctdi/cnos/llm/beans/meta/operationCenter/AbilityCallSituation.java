package com.ctdi.cnos.llm.beans.meta.operationCenter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author yuyong
 * @date 2024/10/17 14:02
 */
@Data
@ApiModel("能力调用情况")
@Accessors(chain = true)
public class AbilityCallSituation {

    /**
     * 排名
     */
    private Integer rank;

    /**
     * 调用量
     */
    @ApiModelProperty(value = "调用量")
    private Integer requestCount;

//    /**
//     * 创建人
//     */
//    @ApiModelProperty(value = "创建人")
//    private String createBy;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 省份
     */
    @ApiModelProperty(value = "省份")
    private String province;

    /**
     * 省份名称
     */
    @ApiModelProperty(value = "省份名称")
    private String provinceName;

    /**
     * 公司名称
     */
    @ApiModelProperty(value = "公司名称")
    private String company;

    /**
     * 用户
     */
    @ApiModelProperty(value = "用户")
    private String userName;
}
