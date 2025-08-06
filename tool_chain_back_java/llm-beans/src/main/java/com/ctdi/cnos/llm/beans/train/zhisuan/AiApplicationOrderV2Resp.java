package com.ctdi.cnos.llm.beans.train.zhisuan;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author xuwj09
 * @version 1.0
 * @date 2025/1/9 17:20
 * @Description
 */
@Data
@ApiModel("智算开始出参")
@Accessors(chain = true)
public class AiApplicationOrderV2Resp implements Serializable {
    private static final long serialVersionUID = 1L;

    private String orderId;
    private String orderType;
    private String serviceId;
    private String platformId;
    private String createdTime;
    private String modifiedTime;

    private List<AiApplicationOrderV2DetailResp> detail;
}
