/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.beans.train.trainTaskDemo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Ai平台集群信息 - 算网调度AI-智算平台接口
 *
 * @author huangjinhua
 * @since 2024/9/26
 */
@Getter
@Setter
public class Strategy implements Serializable {
    private String planId;
    private String candidateId;
    @JSONField(name = "PUE")
    private double pue;

    @JSONField(name = "CO2Data")
    private int cO2Data;
    private String clusterId;
    private String clusterName;
    private int costLevel;
    private String cloudId;
    private String cloudName;
    private String cloudProvince;
    private int cpuCount;
    private int gpuCount;
    private int memoryCount;
    private String gpuVendor;
    private String gpuModel;
}
