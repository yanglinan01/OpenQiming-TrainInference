package com.ctdi.cnos.llm.metadata.controller;

import com.ctdi.cnos.llm.annotation.AuthIgnore;
import com.ctdi.cnos.llm.base.constant.ApplicationConstant;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.base.object.Groups;
import com.ctdi.cnos.llm.beans.meta.order.Order;
import com.ctdi.cnos.llm.beans.meta.order.OrderDTO;
import com.ctdi.cnos.llm.beans.meta.order.OrderVO;
import com.ctdi.cnos.llm.metadata.service.OrderService;
import com.ctdi.cnos.llm.response.ErrorCodeEnum;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.util.ModelUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *  控制器类。
 *
 * @author xuwj09
 * @since 2024/12/06
 */
@Api(tags = "接口", value = "OrderController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/order")
public class OrderController {
	
    private final OrderService service;
	
    /**
     * 分页查询符合条件的列表。
     *
     * @param queryParam 查询对象。
     * @return 分页列表数据。
     */
	@ApiOperation(value = "分页查询符合条件的数据", notes = "分页查询符合条件的数据")
    @PostMapping(value = "/queryPage")
    public OperateResult<PageResult<OrderVO>> queryPage(@Validated(Groups.PAGE.class) @RequestBody QueryParam queryParam) {
        return OperateResult.success(service.queryPage(queryParam));
    }
	
    /**
     * 查询符合条件的列表。
     *
     * @param queryParam 查询对象。
     * @return 列表数据。
     */
	@ApiOperation(value = "列表查询符合条件的数据", notes = "列表查询符合条件的数据")
    @PostMapping(value = "/queryList")
    public OperateResult<List<OrderVO>> queryList(@RequestBody QueryParam queryParam) {
        return OperateResult.success(service.queryList(queryParam));
    }

    /**
     * 查询指定数据。
     *
     * @param id 指定主键Id。
     * @return 单条数据。
     */
	@ApiOperation(value = "查询指定ID的数据", notes = "通过ID获取具体的数据")
	@GetMapping(value = "/queryById")
    public OperateResult<OrderVO> queryById(@ApiParam(value = "ID", required = true, example = "1")
                          @NotNull(message = "ID不能为空") @RequestParam("id") Long id) {
        return OperateResult.success(service.queryById(id, true));
    }

    /**
     * 添加操作。
     *
     * @param orderDTO 添加对象。
     * @return 应答结果对象。
     */
	@ApiOperation(value = "创建", notes = "根据请求体中的信息创建")
	@ApiOperationSupport(ignoreParameters = {"id"})
    @PostMapping(value = "/add")
    public OperateResult<String> add(@Validated(Groups.ADD.class) @RequestBody OrderDTO orderDTO) {
		Order order = ModelUtil.copyTo(orderDTO, Order.class);
        return service.save(order) ? OperateResult.successMessage(ApplicationConstant.SAVE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_SAVE_FAILED);
    }
	
    /**
     * 更新操作。
     *
     * @param orderDTO 更新对象。
     * @return 应答结果对象。
     */
	@ApiOperation(value = "更新", notes = "根据ID更新指定的信息")
    @PostMapping(value = "/updateById")
    public OperateResult<String> updateById(@Validated(Groups.UPDATE.class) @RequestBody OrderDTO orderDTO) {
		Order order = ModelUtil.copyTo(orderDTO, Order.class);
		return service.updateById(order) ? OperateResult.successMessage(ApplicationConstant.UPDATE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_SAVE_FAILED);
    }

    /**
     * 删除指定的。
     *
     * @param id 指定主键Id。
     * @return 应答结果对象。
     */
	@ApiOperation(value = "删除", notes = "根据ID删除指定的")
    @GetMapping(value = "/deleteById")
    public OperateResult<String> deleteById(@ApiParam(value = "ID", required = true, example = "1")
                          @NotNull(message = "ID不能为空") @RequestParam("id") Long id) {
		return service.deleteById(id) ? OperateResult.successMessage(ApplicationConstant.DELETE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
    }

    @AuthIgnore
    @ApiOperation(value = "同步门户权限工单信息", notes = "同步门户权限工单信息")
    @PostMapping(value = "/syncOrder")
    public OperateResult<String> syncOrder(@RequestBody OrderDTO orderDTO) {
        Order order = ModelUtil.copyTo(orderDTO, Order.class);
        return service.syncOrder(order);
    }

    @ApiOperation(value = "分页查询门户工单完整数据", notes = "分页查询门户工单完整数据")
    @PostMapping(value = "/queryOrderAccountPage")
    public OperateResult<PageResult<OrderVO>> queryOrderAccountPage(@Validated(Groups.PAGE.class) @RequestBody QueryParam queryParam) {
        return OperateResult.success(service.queryOrderAccountPage(queryParam));
    }

    @ApiOperation(value = "提交待开通用户", notes = "提交待开通用户")
    @PostMapping(value = "/submitOrderAccount")
    public OperateResult<String> submitOrderAccount(@RequestBody List<OrderVO> orderVO){
        return OperateResult.successMessage(service.submitOrderAccount(orderVO));
    }
}
