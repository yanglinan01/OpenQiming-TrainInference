/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.beans.meta.metric;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

/**
 * Metric 信息，指标（标签）信息
 *
 * @author huangjinhua
 * @since 2024/9/23
 */
@Getter
@Setter
public class Metric {
    /**
     * 指标名称
     */
    @JSONField(name = "__name__")
    private String metricName;
    private String container;
    private String endpoint;
    /**
     * 实例节点
     */
    private String instance;
    private String job;
    @JSONField(name = "minor_number")
    private String minorNumber;
    private String name;
    private String namespace;
    /**
     * pod
     */
    private String pod;
    private String service;
    private String uuid;
}
