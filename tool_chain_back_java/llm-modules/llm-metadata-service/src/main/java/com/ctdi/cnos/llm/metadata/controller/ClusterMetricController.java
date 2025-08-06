package com.ctdi.cnos.llm.metadata.controller;

import com.ctdi.cnos.llm.base.constant.ApplicationConstant;
import com.ctdi.cnos.llm.base.object.Groups;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.beans.meta.cluster.ClusterMetric;
import com.ctdi.cnos.llm.beans.meta.cluster.ClusterMetricDTO;
import com.ctdi.cnos.llm.beans.meta.cluster.ClusterMetricVO;
import com.ctdi.cnos.llm.metadata.service.ClusterMetricService;
import com.ctdi.cnos.llm.response.ErrorCodeEnum;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.util.ModelUtil;
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

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 集群指标 控制器类。
 *
 * @author huangjinhua
 * @since 2024/09/25
 */
@Api(tags = "集群指标接口", value = "ClusterMetricController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/clusterMetric")
public class ClusterMetricController {

    private final ClusterMetricService service;

    /**
     * 分页查询符合条件的集群指标列表。
     *
     * @param queryParam 查询对象。
     * @return 分页列表数据。
     */
    @ApiOperation(value = "分页查询符合条件的集群指标数据", notes = "分页查询符合条件的集群指标数据")
    @PostMapping(value = "/queryPage")
    public OperateResult<PageResult<ClusterMetricVO>> queryPage(@Validated(Groups.PAGE.class) @RequestBody QueryParam queryParam) {
        return OperateResult.success(service.queryPage(queryParam));
    }

    /**
     * 查询符合条件的集群指标列表。
     *
     * @param queryParam 查询对象。
     * @return 列表数据。
     */
    @ApiOperation(value = "列表查询符合条件的集群指标数据", notes = "列表查询符合条件的集群指标数据")
    @PostMapping(value = "/queryList")
    public OperateResult<List<ClusterMetricVO>> queryList(@RequestBody QueryParam queryParam) {
        return OperateResult.success(service.queryList(queryParam));
    }

    /**
     * 查询指定集群指标数据。
     *
     * @param id 指定集群指标主键Id。
     * @return 单条数据。
     */
    @ApiOperation(value = "查询指定ID的集群指标数据", notes = "通过集群指标ID获取具体的集群指标数据")
    @GetMapping(value = "/queryById")
    public OperateResult<ClusterMetricVO> queryById(@ApiParam(value = "集群指标ID", required = true, example = "1")
                                                    @NotNull(message = "集群指标ID不能为空") @RequestParam("id") Long id) {
        return OperateResult.success(service.queryById(id, true));
    }

    /**
     * 添加集群指标操作。
     *
     * @param clusterMetricDTO 添加集群指标对象。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "创建集群指标", notes = "根据请求体中的集群指标信息创建")
    @ApiOperationSupport(ignoreParameters = {"id"})
    @PostMapping(value = "/add")
    public OperateResult<String> add(@Validated(Groups.ADD.class) @RequestBody ClusterMetricDTO clusterMetricDTO) {
        ClusterMetric clusterMetric = ModelUtil.copyTo(clusterMetricDTO, ClusterMetric.class);
        return service.save(clusterMetric) ? OperateResult.successMessage(ApplicationConstant.SAVE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_SAVE_FAILED);
    }

    /**
     * 更新集群指标操作。
     *
     * @param clusterMetricDTO 更新集群指标对象。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "更新集群指标", notes = "根据ID更新指定的集群指标信息")
    @PostMapping(value = "/updateById")
    public OperateResult<String> updateById(@Validated(Groups.UPDATE.class) @RequestBody ClusterMetricDTO clusterMetricDTO) {
        ClusterMetric clusterMetric = ModelUtil.copyTo(clusterMetricDTO, ClusterMetric.class);
        return service.updateById(clusterMetric) ? OperateResult.successMessage(ApplicationConstant.UPDATE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_SAVE_FAILED);
    }

    /**
     * 删除指定的集群指标。
     *
     * @param id 指定集群指标主键Id。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "删除集群指标", notes = "根据ID删除指定的集群指标")
    @GetMapping(value = "/deleteById")
    public OperateResult<String> deleteById(@ApiParam(value = "集群指标ID", required = true, example = "1")
                                            @NotNull(message = "集群指标ID不能为空") @RequestParam("id") Long id) {
        return service.deleteById(id) ? OperateResult.successMessage(ApplicationConstant.DELETE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
    }

}
