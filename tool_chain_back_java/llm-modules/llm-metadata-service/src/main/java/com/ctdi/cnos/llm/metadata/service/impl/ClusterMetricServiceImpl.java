package com.ctdi.cnos.llm.metadata.service.impl;

import com.ctdi.cnos.llm.base.object.LambdaQueryWrapperX;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.base.service.BaseService;
import com.ctdi.cnos.llm.beans.meta.cluster.ClusterMetric;
import com.ctdi.cnos.llm.beans.meta.cluster.ClusterMetricVO;
import com.ctdi.cnos.llm.metadata.dao.ClusterMetricDao;
import com.ctdi.cnos.llm.metadata.service.ClusterMetricService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 集群指标 数据操作服务类
 *
 * @author huangjinhua
 * @since 2024/09/25
 */
@RequiredArgsConstructor
@Service("clusterMetricService")
public class ClusterMetricServiceImpl extends BaseService<ClusterMetricDao, ClusterMetric, ClusterMetricVO> implements ClusterMetricService {

    @Override
    public void configureQueryWrapper(LambdaQueryWrapperX<ClusterMetric> wrapper, QueryParam queryParam) {
        ClusterMetric filter = queryParam.getFilterDto(ClusterMetric.class);
        wrapper.eqIfPresent(ClusterMetric::getCode, filter.getCode());
        wrapper.eqIfPresent(ClusterMetric::getCategory, filter.getCategory());
        wrapper.eqIfPresent(ClusterMetric::getClusterCode, filter.getClusterCode());
        wrapper.eqIfPresent(ClusterMetric::getEntryField, filter.getEntryField());
    }
}