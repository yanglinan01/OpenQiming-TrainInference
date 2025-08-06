package com.ctdi.cnos.llm.beans.log.sessioncache;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "会话缓存展示对象")
public class SessionCacheVO {

    @ApiModelProperty(value = "会话ID", example = "CB8BB8EF5386CD9B330C6E2935F54C74^1745375545677")
    private String sessionId;

    @ApiModelProperty(value = "意图", example = "天翼看家障碍")
    private String intention;

    @ApiModelProperty(value = "会话次数", example = "0")
    private Integer sessionNum;

    @ApiModelProperty(value = "上次交互时间", example = "2025-04-23 10:40:20")
    private Date updateTime;
    @ApiModelProperty("省份名称")
    private String province;
}
