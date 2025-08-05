package com.ctdi.cnos.llm.metadata.controller;

import com.ctdi.cnos.llm.annotation.AuthIgnore;
import com.ctdi.cnos.llm.base.constant.ApplicationConstant;
import com.ctdi.cnos.llm.base.object.Groups;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.beans.meta.cluster.Cluster;
import com.ctdi.cnos.llm.beans.meta.cluster.ClusterDTO;
import com.ctdi.cnos.llm.beans.meta.cluster.ClusterVO;
import com.ctdi.cnos.llm.beans.meta.metric.ClusterDetail;
import com.ctdi.cnos.llm.beans.meta.metric.ClusterResourceStatItem;
import com.ctdi.cnos.llm.beans.meta.metric.ClusterResourceStatVo;
import com.ctdi.cnos.llm.beans.meta.metric.ClusterStatParam;
import com.ctdi.cnos.llm.beans.meta.metric.ClusterUsageTrend;
import com.ctdi.cnos.llm.beans.meta.metric.ResourceCount;
import com.ctdi.cnos.llm.beans.meta.metric.ResourceUsage;
import com.ctdi.cnos.llm.beans.meta.metric.ResourceUsageDetail;
import com.ctdi.cnos.llm.beans.train.trainTaskDemo.Strategy;
import com.ctdi.cnos.llm.metadata.extra.cluster.ClusterResourceStatItemProcessorFactory;
import com.ctdi.cnos.llm.metadata.service.ClusterService;
import com.ctdi.cnos.llm.response.ErrorCodeEnum;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.util.ModelUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 集群资源 控制器类。
 *
 * @author huangjinhua
 * @since 2024/09/24
 */
@Api(tags = "集群资源接口", value = "ClusterController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/cluster")
public class ClusterController {

    private final ClusterService service;


    /**
     * 集群统计汇总信息。
     *
     * @param queryParam 查询对象。
     * @return 集群统计汇总信息。
     */
    @SuppressWarnings("unchecked")
    @ApiOperation(value = "集群统计汇总信息", notes = "集群统计汇总信息")
    @PostMapping(value = "/stat")
    public OperateResult<ClusterResourceStatVo> stat(@RequestBody QueryParam queryParam) {
        // 使用CompletableFuture异步获取四种类型的集群资源统计数据：资源数量、资源使用情况、集群详情和集群使用趋势。
        // 等待所有异步任务完成，将成功的结果填充到ClusterResourceStatVo对象中，并最终返回一个成功的结果对象。
        ClusterResourceStatVo result = new ClusterResourceStatVo();
        ClusterStatParam filterDto = queryParam.getFilterDto(ClusterStatParam.class);
        CompletableFuture<OperateResult<?>> resourceCountRsFuture = CompletableFuture.supplyAsync(() -> ClusterResourceStatItemProcessorFactory.call(ClusterResourceStatItem.RESOURCE_COUNT, filterDto));
        CompletableFuture<OperateResult<?>> resourceUsageRsFuture = CompletableFuture.supplyAsync(() -> ClusterResourceStatItemProcessorFactory.call(ClusterResourceStatItem.RESOURCE_USAGE, filterDto));
        CompletableFuture<OperateResult<?>> clusterDetailsRsFuture = CompletableFuture.supplyAsync(() -> ClusterResourceStatItemProcessorFactory.call(ClusterResourceStatItem.CLUSTER_DETAIL, filterDto));
        return CompletableFuture.allOf(resourceCountRsFuture, resourceUsageRsFuture, clusterDetailsRsFuture)
                .thenApply(v -> {
                    if (resourceCountRsFuture.join().isSuccess()) {
                        result.setResourceCounts((ResourceCount) resourceCountRsFuture.join().getData());
                    }
                    if (resourceUsageRsFuture.join().isSuccess()) {
                        result.setResourceUsage((ResourceUsage) resourceUsageRsFuture.join().getData());
                    }
                    if (clusterDetailsRsFuture.join().isSuccess()) {
                        result.setClusterDetails((List<ClusterDetail>) clusterDetailsRsFuture.join().getData());
                    }
                    return OperateResult.success(result);
                }).join();

    }


    /**
     * 分页查询符合条件的集群资源列表。
     *
     * @param queryParam 查询对象。
     * @return 分页列表数据。
     */
    @ApiOperation(value = "分页查询符合条件的集群资源数据", notes = "分页查询符合条件的集群资源数据")
    @PostMapping(value = "/queryPage")
    public OperateResult<PageResult<ClusterVO>> queryPage(@Validated(Groups.PAGE.class) @RequestBody QueryParam queryParam) {
        return OperateResult.success(service.queryPage(queryParam));
    }

    /**
     * 查询符合条件的集群资源列表。
     *
     * @param queryParam 查询对象。
     * @return 列表数据。
     */
    @ApiOperation(value = "列表查询符合条件的集群资源数据", notes = "列表查询符合条件的集群资源数据")
    @PostMapping(value = "/queryList")
    public OperateResult<List<ClusterVO>> queryList(@RequestBody QueryParam queryParam) {
        return OperateResult.success(service.queryList(queryParam));
    }

    /**
     * 查询指定集群资源数据。
     *
     * @param id 指定集群资源主键Id。
     * @return 单条数据。
     */
    @ApiOperation(value = "查询指定ID的集群资源数据", notes = "通过集群资源ID获取具体的集群资源数据")
    @GetMapping(value = "/queryById")
    public OperateResult<ClusterVO> queryById(@ApiParam(value = "集群资源ID", required = true, example = "1")
                                              @NotNull(message = "集群资源ID不能为空") @RequestParam("id") Long id) {
        return OperateResult.success(service.queryById(id, true));
    }

    /**
     * 查询指定code的集群资源数据。
     *
     * @param code 指定集群资源code。
     * @return 单条数据。
     */
    @ApiOperation(value = "查询指定code的集群资源数据", notes = "查询指定code的集群资源数据")
    @GetMapping(value = "/queryByCode")
    @AuthIgnore
    public OperateResult<ClusterVO> queryByCode(@ApiParam(value = "集群资源code", required = true, example = "1")
                                              @NotNull(message = "集群资源code不能为空") @RequestParam("code") String code) {
        return OperateResult.success(service.queryByCode(code));
    }

    /**
     * 添加集群资源操作。
     *
     * @param clusterDTO 添加集群资源对象。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "创建集群资源", notes = "根据请求体中的集群资源信息创建")
    @ApiOperationSupport(ignoreParameters = {"id"})
    @PostMapping(value = "/add")
    public OperateResult<String> add(@Validated(Groups.ADD.class) @RequestBody ClusterDTO clusterDTO) {
        Cluster cluster = ModelUtil.copyTo(clusterDTO, Cluster.class);
        return service.save(cluster) ? OperateResult.successMessage(ApplicationConstant.SAVE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_SAVE_FAILED);
    }

    /**
     * 更新集群资源操作。
     *
     * @param clusterDTO 更新集群资源对象。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "更新集群资源", notes = "根据ID更新指定的集群资源信息")
    @PostMapping(value = "/updateById")
    public OperateResult<String> updateById(@Validated(Groups.UPDATE.class) @RequestBody ClusterDTO clusterDTO) {
        Cluster cluster = ModelUtil.copyTo(clusterDTO, Cluster.class);
        return service.updateById(cluster) ? OperateResult.successMessage(ApplicationConstant.UPDATE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_SAVE_FAILED);
    }

    /**
     * 删除指定的集群资源。
     *
     * @param id 指定集群资源主键Id。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "删除集群资源", notes = "根据ID删除指定的集群资源")
    @GetMapping(value = "/deleteById")
    public OperateResult<String> deleteById(@ApiParam(value = "集群资源ID", required = true, example = "1")
                                            @NotNull(message = "集群资源ID不能为空") @RequestParam("id") Long id) {
        return service.deleteById(id) ? OperateResult.successMessage(ApplicationConstant.DELETE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
    }

    /**
     * 集群使用趋势查询。
     *
     * @param duration 使用趋势时长。
     * @return 趋势结果。
     */
    @SuppressWarnings("unchecked")
    @ApiOperation(value = "集群使用趋势查询", notes = "集群使用趋势查询")
    @GetMapping(value = "/clusterUsageTrend")
    public OperateResult<ClusterUsageTrend> clusterUsageTrend(@Validated @ApiParam(value = "使用趋势时长", required = true, example = "1")
                                                              @Min(value = 1, message = "数据验证失败，使用趋势时长不能小于1天！")
                                                              @Max(value = 30, message = "数据验证失败，使用趋势时长不能大于30天！")
                                                              @NotNull(message = "使用趋势时长不能为空") @RequestParam("duration") Integer duration,
                                                              @ApiParam(value = "集群编码", example = "QD")
                                                              @RequestParam(value = "clusterCode",required = false) String clusterCode) {
        ClusterStatParam filterDto = new ClusterStatParam().setUsageTrendDuration(duration).setClusterCode(clusterCode);
        return (OperateResult<ClusterUsageTrend>) ClusterResourceStatItemProcessorFactory.call(ClusterResourceStatItem.CLUSTER_USAGE_TREND, filterDto);
    }

    /**
     * 集群采集详情信息。
     *
     * @param clusterCode 集群编码。
     * @return 集群采集详情信息。
     */
    @SuppressWarnings("unchecked")
    @ApiOperation(value = "集群采集详情信息", notes = "集群采集详情信息")
    @GetMapping(value = "/clusterDetail")
    public OperateResult<List<ClusterDetail>> clusterDetail(@ApiParam(value = "集群编码", required = false, example = "GZ")
                                                            @RequestParam(value = "clusterCode", required = false) String clusterCode) {
        ClusterStatParam filterDto = new ClusterStatParam().setClusterCode(clusterCode);
        return (OperateResult<List<ClusterDetail>>) ClusterResourceStatItemProcessorFactory.call(ClusterResourceStatItem.CLUSTER_DETAIL, filterDto);
    }


    @ApiOperation(value = "查询测选列表", notes = "查询测选列表")
    @GetMapping("/queryStrategyList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", paramType = "param")
    })
    public List<Strategy> queryStrategyList(@RequestParam(name = "name", required = false) String name) {
        Strategy aiCluster = new Strategy();
        return service.queryStrategyList(aiCluster);
    }

    /**
     * 查询使用资源信息
     *
     * @param clusterStatParam
     * @return
     */
    @ApiOperation(value = "使用资源展示", notes = "使用资源展示")
    @PostMapping(value = "/getUsingResourceInfo")
    public OperateResult<ResourceUsageDetail> getUsingResourceInfo(@RequestBody ClusterStatParam clusterStatParam) {
        return (OperateResult<ResourceUsageDetail>) ClusterResourceStatItemProcessorFactory.call(ClusterResourceStatItem.RESOURCE_USAGE_DETAIL, clusterStatParam);
    }


    /**
     * 集群采集资源总览
     *
     * @return 资源总览
     */
    @SuppressWarnings("unchecked")
    @ApiOperation(value = "集群采集资源总览", notes = "集群采集资源总览")
    @GetMapping(value = "/resourceCount")
    public OperateResult<ResourceCount> resourceCount() {
        return (OperateResult<ResourceCount>) ClusterResourceStatItemProcessorFactory.call(ClusterResourceStatItem.RESOURCE_COUNT, null);
    }

    /**
     * 集群采集资源总览饼图
     *
     * @param clusterCode 集群编码
     * @return 资源总览饼图
     */
    @SuppressWarnings("unchecked")
    @ApiOperation(value = "集群采集资源总览饼图", notes = "集群采集资源总览饼图")
    @GetMapping(value = "/resourceUsage")
    public OperateResult<ResourceUsage> resourceUsage(@ApiParam(value = "集群编码", example = "GZ")
                                                      @RequestParam(value = "clusterCode", required = false) String clusterCode) {
        ClusterStatParam filterDto = new ClusterStatParam().setClusterCode(clusterCode);
        return (OperateResult<ResourceUsage>) ClusterResourceStatItemProcessorFactory.call(ClusterResourceStatItem.RESOURCE_USAGE, filterDto);
    }


}