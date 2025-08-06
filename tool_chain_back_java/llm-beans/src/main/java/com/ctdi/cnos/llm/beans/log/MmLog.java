package com.ctdi.cnos.llm.beans.log;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 日志记录(MmLog)实体类
 *
 * @author yuy
 * @since 2024-04-03 15:09:11
 */

@Data
@ApiModel("日志记录实体类")
public class MmLog {
    private static final long serialVersionUID = 957826957439763341L;
    /**
     * 主键
     */
    @ApiModelProperty("id")
    private BigDecimal id;
    /**
     * 接口名称
     */
    @ApiModelProperty("接口名称")
    private String interfaceName;
    /**
     * 请求参数
     */
    @ApiModelProperty("请求参数")
    private String requestParams;

    /**
     * 响应参数
     */
    @ApiModelProperty("响应参数")
    private String responseParams;

    /**
     * 请求时间
     */
    @ApiModelProperty("请求时间")
    private Date requestTime;

    /**
     * 响应时间
     */
    @ApiModelProperty("响应时间")
    private Date responseTime;

    /**
     * 持续时间
     */
    @ApiModelProperty("持续时间")
    private Long duration;

    /**
     * 状态码
     */
    @ApiModelProperty("状态码")
    private String statusCode;

    /**
     * 错误信息
     */
    @ApiModelProperty("错误信息")
    private String errorMessage;

    /**
     * 客户端IP地址
     */
    @ApiModelProperty("客户端IP地址")
    private String clientIp;

    /**
     * 接口地址
     */
    @ApiModelProperty("接口地址")
    private String interfaceUrl;

    /**
     * 服务器IP地址
     */
    @ApiModelProperty("服务器IP地址")
    private String serverIp;

    @ApiModelProperty("创建人id")
    private String creatorId;

    @ApiModelProperty("创建时间")
    private Date createDate;

    @ApiModelProperty("修改人id")
    private String modifierId;

    @ApiModelProperty("修改日期")
    private Date modifyDate;

}
