package com.ctdi.cnos.llm.metadata.controller;

import com.ctdi.cnos.llm.base.constant.ApplicationConstant;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.base.object.Groups;
import com.ctdi.cnos.llm.beans.meta.order.OrderNode;
import com.ctdi.cnos.llm.beans.meta.order.OrderNodeDTO;
import com.ctdi.cnos.llm.beans.meta.order.OrderNodeVO;
import com.ctdi.cnos.llm.metadata.service.OrderNodeService;
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
@Api(tags = "接口", value = "OrderNodeController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/orderNode")
public class OrderNodeController {
	
    private final OrderNodeService service;
	
    /**
     * 分页查询符合条件的列表。
     *
     * @param queryParam 查询对象。
     * @return 分页列表数据。
     */
	@ApiOperation(value = "分页查询符合条件的数据", notes = "分页查询符合条件的数据")
    @PostMapping(value = "/queryPage")
    public OperateResult<PageResult<OrderNodeVO>> queryPage(@Validated(Groups.PAGE.class) @RequestBody QueryParam queryParam) {
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
    public OperateResult<List<OrderNodeVO>> queryList(@RequestBody QueryParam queryParam) {
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
    public OperateResult<OrderNodeVO> queryById(@ApiParam(value = "ID", required = true, example = "1")
                          @NotNull(message = "ID不能为空") @RequestParam("id") Long id) {
        return OperateResult.success(service.queryById(id, true));
    }

    /**
     * 添加操作。
     *
     * @param orderNodeDTO 添加对象。
     * @return 应答结果对象。
     */
	@ApiOperation(value = "创建", notes = "根据请求体中的信息创建")
	@ApiOperationSupport(ignoreParameters = {"id"})
    @PostMapping(value = "/add")
    public OperateResult<String> add(@Validated(Groups.ADD.class) @RequestBody OrderNodeDTO orderNodeDTO) {
		OrderNode orderNode = ModelUtil.copyTo(orderNodeDTO, OrderNode.class);
        return service.save(orderNode) ? OperateResult.successMessage(ApplicationConstant.SAVE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_SAVE_FAILED);
    }
	
    /**
     * 更新操作。
     *
     * @param orderNodeDTO 更新对象。
     * @return 应答结果对象。
     */
	@ApiOperation(value = "更新", notes = "根据ID更新指定的信息")
    @PostMapping(value = "/updateById")
    public OperateResult<String> updateById(@Validated(Groups.UPDATE.class) @RequestBody OrderNodeDTO orderNodeDTO) {
		OrderNode orderNode = ModelUtil.copyTo(orderNodeDTO, OrderNode.class);
		return service.updateById(orderNode) ? OperateResult.successMessage(ApplicationConstant.UPDATE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_SAVE_FAILED);
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

}
