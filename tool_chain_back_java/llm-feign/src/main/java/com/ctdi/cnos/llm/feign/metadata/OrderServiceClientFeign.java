package com.ctdi.cnos.llm.feign.metadata;

import com.ctdi.cnos.llm.RemoteConstont;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.beans.meta.order.OrderDTO;
import com.ctdi.cnos.llm.beans.meta.order.OrderVO;
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
 * 服务远程数据操作访问接口。
 *
 * @author xuwj09
 * @since 2024/12/06
 */
@Component
@FeignClient(value = RemoteConstont.METADATA_SERVICE_NAME, path = "${cnos.server.llm-metadata-service.contextPath}")
public interface OrderServiceClientFeign {

    /**
     * 分页查询符合条件的列表。
     *
     * @param queryParam 查询对象。
     * @return 分页列表数据。
     */
    @PostMapping(value = "/order/queryPage")
    OperateResult<PageResult<OrderVO>> queryPage(@RequestBody QueryParam queryParam);

    /**
     * 查询符合条件的列表。
     *
     * @param queryParam 查询对象。
     * @return 列表数据。
     */
    @PostMapping(value = "/order/queryList")
    OperateResult<List<OrderVO>> queryList(@RequestBody QueryParam queryParam);

    /**
     * 查询指定数据。
     *
     * @param id 指定主键Id。
     * @return 单条数据。
     */
	@GetMapping(value = "/order/queryById")
    OperateResult<OrderVO> queryById(@RequestParam("id") Long id);

    /**
     * 添加操作。
     *
     * @param orderDTO 添加对象。
     * @return 应答结果对象。
     */
    @PostMapping(value = "/order/add")
    OperateResult<String> add(@RequestBody OrderDTO orderDTO);
	
    /**
     * 更新操作。
     *
     * @param orderDTO 更新对象。
     * @return 应答结果对象。
     */
    @PostMapping(value = "/order/updateById")
    OperateResult<String> updateById(@RequestBody OrderDTO orderDTO);

    /**
     * 删除指定的。
     *
     * @param id 指定主键Id。
     * @return 应答结果对象。
     */
    @GetMapping(value = "/order/deleteById")
    OperateResult<String> deleteById(@RequestParam("id") Long id);

    /**
     * 同步工单用户数据
     * @param orderDTO
     * @return
     */
    @PostMapping(value = "/order/syncOrder")
    public OperateResult<String> syncOrder(@RequestBody OrderDTO orderDTO);

    /**
     * 分页查询门户工单完整数据
     * @param queryParam
     * @return
     */
    @PostMapping(value = "/order/queryOrderAccountPage")
    OperateResult<PageResult<OrderVO>> queryOrderAccountPage(@RequestBody QueryParam queryParam);

    /**
     * 提交待开通用户
     * @param orderVO
     * @return
     */
    @PostMapping(value = "/order/submitOrderAccount")
    public OperateResult<String> submitOrderAccount(@RequestBody List<OrderVO> orderVO);

}