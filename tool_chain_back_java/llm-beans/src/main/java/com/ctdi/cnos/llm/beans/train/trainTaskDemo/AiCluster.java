/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.beans.train.trainTaskDemo;

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
public class AiCluster implements Serializable {
    /**
     * 集群ID
     */
    private String id;
    /**
     * 集群名称
     */
    private String title;

    /**
     * 集群状态
     */
    private String status;
    /**
     * 省份ID
     */
    private String provinceId;
    /**
     * 省份Code
     */
    private String provinceCode;

    /**
     * 省份名称
     */
    private String provinceName;

    /**
     * 环境
     */
    private String env;
    /**
     * 集群描述
     */
    private String description;
}
