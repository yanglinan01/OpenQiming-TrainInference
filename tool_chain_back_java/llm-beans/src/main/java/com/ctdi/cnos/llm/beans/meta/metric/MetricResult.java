/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.beans.meta.metric;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Metric 采集指标结果
 *
 * @author huangjinhua
 * @since 2024/9/23
 */
@Getter
@Setter
@Accessors(chain = true)
public class MetricResult<T> {

    /**
     * 节点标识
     */
    private String instance;
    /**
     * 采集指标
     */
    private Metric metric;

    /**
     * 采集值信息列表
     */
    private List<MetricValue<T>> value;
}
