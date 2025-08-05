package com.ctdi.cnos.llm.feign.metadata;

import com.ctdi.cnos.llm.RemoteConstont;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.beans.meta.cluster.ClusterDTO;
import com.ctdi.cnos.llm.beans.meta.cluster.ClusterVO;
import com.ctdi.cnos.llm.beans.meta.metric.*;
import com.ctdi.cnos.llm.beans.train.trainTaskDemo.Strategy;
import com.ctdi.cnos.llm.response.OperateResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 集群资源服务远程数据操作访问接口。
 *
 * @author huangjinhua
 * @since 2024/09/24
 */
@Component
@FeignClient(value = RemoteConstont.METADATA_SERVICE_NAME, path = "${cnos.server.llm-metadata-service.contextPath}")
public interface ClusterServiceClientFeign {

    /**
     * 集群统计汇总信息。
     *
     * @param queryParam 查询对象。
     * @return 集群统计汇总信息。
     */
    @ApiOperation(value = "集群统计汇总信息", notes = "集群统计汇总信息")
    @PostMapping(value = "/stat")
    OperateResult<ClusterResourceStatVo> stat(@RequestBody QueryParam queryParam);


    /**
     * 分页查询符合条件的集群资源列表。
     *
     * @param queryParam 查询对象。
     * @return 分页列表数据。
     */
    @PostMapping(value = "/cluster/queryPage")
    OperateResult<PageResult<ClusterVO>> queryPage(@RequestBody QueryParam queryParam);

    /**
     * 查询符合条件的集群资源列表。
     *
     * @param queryParam 查询对象。
     * @return 列表数据。
     */
    @PostMapping(value = "/cluster/queryList")
    OperateResult<List<ClusterVO>> queryList(@RequestBody QueryParam queryParam);

    /**
     * 查询指定集群资源数据。
     *
     * @param id 指定集群资源主键Id。
     * @return 单条数据。
     */
    @GetMapping(value = "/cluster/queryById")
    OperateResult<ClusterVO> queryById(@RequestParam("id") Long id);

    /**
     * 查询指定Code集群资源数据。
     *
     * @param code 指定集群资源Code。
     * @return 单条数据。
     */
    @GetMapping(value = "/cluster/queryByCode")
    OperateResult<ClusterVO> queryByCode(@RequestParam("code") String code);

    /**
     * 添加集群资源操作。
     *
     * @param clusterDTO 添加集群资源对象。
     * @return 应答结果对象。
     */
    @PostMapping(value = "/cluster/add")
    OperateResult<String> add(@RequestBody ClusterDTO clusterDTO);

    /**
     * 更新集群资源操作。
     *
     * @param clusterDTO 更新集群资源对象。
     * @return 应答结果对象。
     */
    @PostMapping(value = "/cluster/updateById")
    OperateResult<String> updateById(@RequestBody ClusterDTO clusterDTO);

    /**
     * 删除指定的集群资源。
     *
     * @param id 指定集群资源主键Id。
     * @return 应答结果对象。
     */
    @GetMapping(value = "/cluster/deleteById")
    OperateResult<String> deleteById(@RequestParam("id") Long id);

    /**
     * 集群使用趋势查询。
     *
     * @param duration 使用趋势时长。
     * @return 趋势结果。
     */
    @GetMapping(value = "/cluster/clusterUsageTrend")
    OperateResult<ClusterUsageTrend> clusterUsageTrend(@RequestParam("duration") Integer duration,@RequestParam(value = "clusterCode", required = false) String clusterCode);

    /**
     * 集群采集详情信息。
     *
     * @param clusterCode 集群编码。
     * @return 集群详情。
     */
    @GetMapping(value = "/cluster/clusterDetail")
    OperateResult<List<ClusterDetail>> clusterDetail(@RequestParam(value = "clusterCode",required = false) String clusterCode);

    /**
     * 测选详情信息。
     *
     * @param strategyCode 集群编码。
     * @return 集群详情。
     */
    @GetMapping(value = "/cluster/queryStrategyList")
    List<Strategy> strategyDetail(@RequestParam(value = "strategyCode",required = false) String strategyCode);

    /**
     * 使用资源明细查询
     *
     * @param clusterStatParam
     * @return
     */
    @PostMapping(value = "/cluster/getUsingResourceInfo")
    OperateResult<ResourceUsageDetail> getUsingResourceInfo(@RequestBody ClusterStatParam clusterStatParam);

    /**
     * 集群采集资源总览
     * @return 资源总览
     */
    @GetMapping(value = "/cluster/resourceCount")
    OperateResult<ResourceCount> resourceCount();
    /**
     * 集群采集资源总览饼图
     *
     * @param clusterCode 集群编码
     * @return 资源总览饼图
     */
    @GetMapping(value = "/cluster/resourceUsage")
    OperateResult<ResourceUsage> resourceUsage(@RequestParam(value = "clusterCode",required = false) String clusterCode);

}