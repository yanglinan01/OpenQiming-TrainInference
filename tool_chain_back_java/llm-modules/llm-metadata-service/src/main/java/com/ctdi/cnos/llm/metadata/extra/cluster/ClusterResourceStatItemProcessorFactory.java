package com.ctdi.cnos.llm.metadata.extra.cluster;

import cn.hutool.extra.spring.SpringUtil;
import com.ctdi.cnos.llm.base.extra.factory.AbstractFactory;
import com.ctdi.cnos.llm.beans.meta.metric.ClusterResourceStatItem;
import com.ctdi.cnos.llm.beans.meta.metric.ClusterStatParam;
import com.ctdi.cnos.llm.exception.MyRuntimeException;
import com.ctdi.cnos.llm.response.OperateResult;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 集群指标项处理工厂。
 *
 * @author laiqi
 * @since 2024/9/23
 */
@Component
public class ClusterResourceStatItemProcessorFactory extends AbstractFactory<ClusterResourceStatItem, BaseClusterResourceStatItemProcessor> {

    public ClusterResourceStatItemProcessorFactory(List<BaseClusterResourceStatItemProcessor> elements) {
        super(elements);
    }

    /**
     * 根据类型获取元素。
     *
     * @param type 类型
     * @return 元素
     */
    public static OperateResult<?> call(ClusterResourceStatItem type, ClusterStatParam params) {
        return SpringUtil.getBean(ClusterResourceStatItemProcessorFactory.class).get(type).orElseThrow(() -> new MyRuntimeException("未找到对应的元素" + type)).doExecute(params);
    }
}