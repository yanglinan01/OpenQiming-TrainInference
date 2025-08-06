package com.ctdi.cnos.llm.controller.metadata;

import com.ctdi.cnos.llm.base.object.Groups;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.beans.meta.cluster.ClusterDTO;
import com.ctdi.cnos.llm.beans.meta.cluster.ClusterVO;
import com.ctdi.cnos.llm.beans.meta.metric.*;
import com.ctdi.cnos.llm.beans.train.trainTaskDemo.Strategy;
import com.ctdi.cnos.llm.feign.metadata.ClusterServiceClientFeign;
import com.ctdi.cnos.llm.response.OperateResult;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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


/**
 * 集群资源服务远程数据操作访问接口。
 *
 * @author huangjinhua
 * @since 2024/09/24
 */
@Api(tags = "集群资源接口", value = "ClusterController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/cluster")
public class ClusterController {

    private final ClusterServiceClientFeign serviceClient;

    /**
     * 分页查询符合条件的集群资源列表。
     *
     * @param queryParam 查询对象。
     * @return 分页列表数据。
     */
    @ApiOperation(value = "分页查询符合条件的集群资源数据", notes = "分页查询符合条件的集群资源数据")
    @PostMapping(value = "/queryPage")
    public OperateResult<PageResult<ClusterVO>> queryPage(@Validated(Groups.PAGE.class) @RequestBody QueryParam queryParam) {
        return this.serviceClient.queryPage(queryParam);
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
        return this.serviceClient.queryList(queryParam);
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
        return this.serviceClient.queryById(id);
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
        return this.serviceClient.add(clusterDTO);
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
        return this.serviceClient.updateById(clusterDTO);
    }

    /**
     * 删除指定的集群资源。
     *
     * @param id 指定集群资源主键Id。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "删除集群资源", notes = "根据ID删除指定的集群资源")
    @GetMapping(value = "/deleteById")
    public OperateResult<String> delete(@ApiParam(value = "集群资源ID", required = true, example = "1")
                                        @NotNull(message = "集群资源ID不能为空") @RequestParam("id") Long id) {
        return this.serviceClient.deleteById(id);
    }


    /**
     * 集群使用趋势查询。
     *
     * @param duration 使用趋势时长。
     * @return 趋势结果。
     */
    @ApiOperation(value = "集群使用趋势查询", notes = "集群使用趋势查询")
    @GetMapping(value = "/clusterUsageTrend")
    public OperateResult<ClusterUsageTrend> clusterUsageTrend(@Validated @ApiParam(value = "使用趋势时长", required = true, example = "1")
                                                              @Min(value = 1, message = "数据验证失败，使用趋势时长不能小于1天！")
                                                              @Max(value = 30, message = "数据验证失败，使用趋势时长不能大于30天！")
                                                              @NotNull(message = "使用趋势时长不能为空") @RequestParam("duration") Integer duration,
                                                              @ApiParam(value = "集群编码", example = "QD")
                                                              @RequestParam(value = "clusterCode",required = false) String clusterCode) {
        return this.serviceClient.clusterUsageTrend(duration, clusterCode);
    }

    /**
     * 集群采集详情信息。
     *
     * @param clusterCode 集群编码。
     * @return 集群采集详情信息。
     */
    @ApiOperation(value = "集群采集详情信息", notes = "集群采集详情信息")
    @GetMapping(value = "/clusterDetail")
    public OperateResult<List<ClusterDetail>> clusterDetail(@ApiParam(value = "集群编码", required = false, example = "GZ")
                                                            @RequestParam(value = "clusterCode", required = false) String clusterCode) {
        return this.serviceClient.clusterDetail(clusterCode);
    }

    /**
     * 策选情信息。
     *
     * @param strategyCode 策选编码。
     * @return 策选详情信息。
     */
    @ApiOperation(value = "策选详情信息", notes = "策选详情信息")
    @GetMapping(value = "/clusterStrategyDetail")
    public OperateResult<List<Strategy>> strategyDetail(@ApiParam(value = "策选编码", required = false, example = "GZ")
                                                            @RequestParam(value = "strategyCode", required = false) String strategyCode) {
        OperateResult resp = new OperateResult();
        resp.setSuccess(true);
        resp.setData(this.serviceClient.strategyDetail(strategyCode));
        return resp;
    }

    /**
     * 使用资源展示查询
     *
     * @param clusterStatParam
     * @return
     */
    @ApiOperation(value = "使用资源展示查询", notes = "使用资源展示查询")
    @PostMapping(value = "/getUsingResourceInfo")
    public OperateResult<ResourceUsageDetail> getUsingResourceInfo(@RequestBody ClusterStatParam clusterStatParam) {
        return this.serviceClient.getUsingResourceInfo(clusterStatParam);
    }


    /**
     * 集群采集资源总览
     *
     * @return 资源总览
     */
    @ApiOperation(value = "集群采集资源总览", notes = "集群采集资源总览")
    @GetMapping(value = "/resourceCount")
    public OperateResult<ResourceCount> resourceCount() {
        return this.serviceClient.resourceCount();
    }


    /**
     * 集群采集资源总览饼图
     *
     * @param clusterCode 集群编码
     * @return 资源总览饼图
     */
    @ApiOperation(value = "集群采集资源总览饼图", notes = "集群采集资源总览饼图")
    @GetMapping(value = "/resourceUsage")
    public OperateResult<ResourceUsage> resourceUsage(@ApiParam(value = "集群编码", required = false, example = "GZ")
                                                      @RequestParam(value = "clusterCode", required = false) String clusterCode) {
        return this.serviceClient.resourceUsage(clusterCode);
    }
}