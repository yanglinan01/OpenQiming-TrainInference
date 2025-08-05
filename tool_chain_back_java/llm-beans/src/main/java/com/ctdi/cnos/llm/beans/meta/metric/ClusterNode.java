/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.beans.meta.metric;

import lombok.Getter;
import lombok.Setter;

/**
 * 节点情况
 *
 * @author huangjinhua
 * @since 2024/9/23
 */
@Getter
@Setter
public class ClusterNode {
    /**
     * 节点名称
     */
    private String name;
    /**
     * 节点IP
     */
    private String ip;
    /**
     * 是否是训练
     */
    private boolean train;
    /**
     * 是否是推理
     */
    private boolean inference;

}
