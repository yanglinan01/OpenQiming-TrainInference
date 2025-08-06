package com.ctdi.cnos.llm.metadata.dao;

import com.ctdi.cnos.llm.base.dao.BaseDaoMapper;
import com.ctdi.cnos.llm.beans.meta.cluster.ClusterMetric;
import org.apache.ibatis.annotations.Mapper;

/**
 * 集群指标 数据操作访问接口。
 *
 * @author huangjinhua
 * @since 2024/09/25
 */
@Mapper
public interface ClusterMetricDao extends BaseDaoMapper<ClusterMetric> {

}
