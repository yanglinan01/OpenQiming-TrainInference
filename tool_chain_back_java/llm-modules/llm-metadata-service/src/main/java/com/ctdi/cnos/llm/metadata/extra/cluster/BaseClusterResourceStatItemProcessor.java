package com.ctdi.cnos.llm.metadata.extra.cluster;

import com.ctdi.cnos.llm.base.extra.factory.AbstractElement;
import com.ctdi.cnos.llm.beans.meta.metric.ClusterResourceStatItem;
import com.ctdi.cnos.llm.beans.meta.metric.ClusterStatParam;
import com.ctdi.cnos.llm.response.OperateResult;

/**
 * 基础 集群指标项 处理器。
 *
 * @author laiqi
 * @since 2024/9/23
 */
public abstract class BaseClusterResourceStatItemProcessor extends AbstractElement<ClusterResourceStatItem, ClusterStatParam, OperateResult<?>> {
    public BaseClusterResourceStatItemProcessor(ClusterResourceStatItem type) {
        super(type);
    }
}