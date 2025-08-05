package com.ctdi.cnos.llm.beans.log.sessioncache;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

@Data
@ApiModel(description = "会话缓存传输对象")
public class SessionCacheDTO {

    @ApiModelProperty(value = "自增长ID", example = "1")
    private BigInteger id;

    @ApiModelProperty(value = "会话ID", required = true, example = "CB8BB8EF5386CD9B330C6E2935F54C74^1745375545677")
    private String sessionId;

    @ApiModelProperty(value = "意图", required = true, example = "天翼看家障碍")
    private String intention;
    @ApiModelProperty(value = "省名称",required = true, example = "安徽")
    private String province;

    @ApiModelProperty(value = "会话次数", required = true, example = "0")
    private Integer sessionNum;

    @ApiModelProperty(value = "上次交互时间", required = true, example = "2025-04-23 10:40:20")
    private Date updateTime;
}
