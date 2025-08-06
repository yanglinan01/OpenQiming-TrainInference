package com.ctdi.cnos.llm.metadata.dao;

import com.ctdi.cnos.llm.base.dao.BaseDaoMapper;
import com.ctdi.cnos.llm.beans.meta.cluster.Cluster;
import org.apache.ibatis.annotations.Mapper;

/**
 * 集群资源 数据操作访问接口。
 *
 * @author huangjinhua
 * @since 2024/09/24
 */
@Mapper
public interface ClusterDao extends BaseDaoMapper<Cluster> {

}
