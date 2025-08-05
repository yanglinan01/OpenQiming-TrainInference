package com.ctdi.cnos.llm.feign.metadata;

import com.ctdi.cnos.llm.RemoteConstont;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.beans.meta.cluster.ClusterMetricDTO;
import com.ctdi.cnos.llm.beans.meta.cluster.ClusterMetricVO;
import com.ctdi.cnos.llm.response.OperateResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 集群指标服务远程数据操作访问接口。
 *
 * @author huangjinhua
 * @since 2024/09/25
 */
@Component
@FeignClient(value = RemoteConstont.METADATA_SERVICE_NAME, path = "${cnos.server.llm-metadata-service.contextPath}")
public interface ClusterMetricServiceClientFeign {

    /**
     * 分页查询符合条件的集群指标列表。
     *
     * @param queryParam 查询对象。
     * @return 分页列表数据。
     */
    @PostMapping(value = "/clusterMetric/queryPage")
    OperateResult<PageResult<ClusterMetricVO>> queryPage(@RequestBody QueryParam queryParam);

    /**
     * 查询符合条件的集群指标列表。
     *
     * @param queryParam 查询对象。
     * @return 列表数据。
     */
    @PostMapping(value = "/clusterMetric/queryList")
    OperateResult<List<ClusterMetricVO>> queryList(@RequestBody QueryParam queryParam);

    /**
     * 查询指定集群指标数据。
     *
     * @param id 指定集群指标主键Id。
     * @return 单条数据。
     */
	@GetMapping(value = "/clusterMetric/queryById")
    OperateResult<ClusterMetricVO> queryById(@RequestParam("id") Long id);

    /**
     * 添加集群指标操作。
     *
     * @param clusterMetricDTO 添加集群指标对象。
     * @return 应答结果对象。
     */
    @PostMapping(value = "/clusterMetric/add")
    OperateResult<String> add(@RequestBody ClusterMetricDTO clusterMetricDTO);

    /**
     * 更新集群指标操作。
     *
     * @param clusterMetricDTO 更新集群指标对象。
     * @return 应答结果对象。
     */
    @PostMapping(value = "/clusterMetric/updateById")
    OperateResult<String> updateById(@RequestBody ClusterMetricDTO clusterMetricDTO);

    /**
     * 删除指定的集群指标。
     *
     * @param id 指定集群指标主键Id。
     * @return 应答结果对象。
     */
    @GetMapping(value = "/clusterMetric/deleteById")
    OperateResult<String> deleteById(@RequestParam("id") Long id);

}