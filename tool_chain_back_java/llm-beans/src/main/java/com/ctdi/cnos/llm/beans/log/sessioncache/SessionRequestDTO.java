package com.ctdi.cnos.llm.beans.log.sessioncache;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SessionRequestDTO {
    @ApiModelProperty("省份名称")
    private String province;

    @ApiModelProperty("会话ID")
    private String sessionId;

    @ApiModelProperty("意图")
    private String intention;
}