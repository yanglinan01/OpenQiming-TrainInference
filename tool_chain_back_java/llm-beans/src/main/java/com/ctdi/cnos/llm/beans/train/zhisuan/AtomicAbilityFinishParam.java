package com.ctdi.cnos.llm.beans.train.zhisuan;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author xuwj09
 * @version 1.0
 * @date 2025/1/9 17:31
 * @Description
 */
@Data
@ApiModel("智算结束入参")
@Accessors(chain = true)
public class AtomicAbilityFinishParam implements Serializable {
    private static final long serialVersionUID = 1L;

    private String orderId;
}
