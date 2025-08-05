/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.beans.meta.metric;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Metric 采集值
 *
 * @author huangjinhua
 * @since 2024/9/23
 */
@Getter
@Setter
public class MetricValue<T> {
    /**
     * 采集时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date time;
    /**
     * 采集值
     */
    private T value;

}