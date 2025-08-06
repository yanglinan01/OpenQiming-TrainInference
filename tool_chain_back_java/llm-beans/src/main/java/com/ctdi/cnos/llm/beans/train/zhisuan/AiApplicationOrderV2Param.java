package com.ctdi.cnos.llm.beans.train.zhisuan;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author xuwj09
 * @version 1.0
 * @date 2025/1/9 17:19
 * @Description
 */
@Data
@ApiModel("智算开始入参")
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiApplicationOrderV2Param implements Serializable {
    private static final long serialVersionUID = 1L;

    private String planId;

    private String candidateId;
}
