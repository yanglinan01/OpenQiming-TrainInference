package com.ctdi.cnos.llm.beans.register.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhangxue
 * @description
 * @data 2024/4/17 15:25
 */
@Data
@SuppressWarnings("all")
@ApiModel(value = "Api分页查询入参")
public class DcoosApiQueryReq {

    /**
     * 接口名称
     */
    @ApiModelProperty(value = "接口名称")
    private String name;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String creatorId;

    /**
     * 页码数
     */
    @ApiModelProperty(value = "页码数")
    private Integer pageNum;

    /**
     * 页面条数
     */
    @ApiModelProperty(value = "页面条数")
    private Integer pageSize;
}
