package com.ctdi.cnos.llm.beans.train.zhisuan;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author xuwj09
 * @version 1.0
 * @date 2025/1/9 17:27
 * @Description
 */
@Data
@ApiModel("智算开始详情出参")
@Accessors(chain = true)
public class AiApplicationOrderV2DetailResp implements Serializable {
    private static final long serialVersionUID = 1L;

    private String planId;
    private String candidateId;
    private String platformId;
    private String orderId;
}
