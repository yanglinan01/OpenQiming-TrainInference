/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.metadata.config;


import lombok.Getter;
import lombok.Setter;

import java.util.Map;


/**
 * metadata 配置
 *
 * @author huangjinhua
 * @since 2024/10/17
 */
@Getter
@Setter
public class OperationCenter {

    /**
     * 是否模拟接口
     */
    private boolean mock;
    /**
     * 模拟数据
     */
    private Map<String, String> mockData;
    /**
     * 智能体host
     */
    private String agentHost;

    /**
     * 注册api + 能力榜单 host
     */
    private String workflowApiHost;

}
