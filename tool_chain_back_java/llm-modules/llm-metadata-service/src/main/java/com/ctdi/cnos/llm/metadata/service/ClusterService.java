package com.ctdi.cnos.llm.metadata.service;

import com.ctdi.cnos.llm.base.service.IBaseService;
import com.ctdi.cnos.llm.beans.meta.cluster.Cluster;
import com.ctdi.cnos.llm.beans.meta.cluster.ClusterVO;
import com.ctdi.cnos.llm.beans.train.trainTaskDemo.Strategy;

import java.util.List;

/**
 * 集群资源 数据操作服务接口。
 *
 * @author huangjinhua
 * @since 2024/09/24
 */
public interface ClusterService extends IBaseService<Cluster, ClusterVO> {

    /**
     * 根据编码查询集群信息
     * @param code 编码
     * @return 集群信息
     */
    ClusterVO queryByCode(String code);

    List<Strategy>  queryStrategyList(Strategy strategy);
}
