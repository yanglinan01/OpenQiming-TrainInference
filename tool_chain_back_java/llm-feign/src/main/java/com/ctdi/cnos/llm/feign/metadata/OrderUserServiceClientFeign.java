package com.ctdi.cnos.llm.feign.metadata;

import com.ctdi.cnos.llm.RemoteConstont;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.beans.meta.order.OrderUserDTO;
import com.ctdi.cnos.llm.beans.meta.order.OrderUserVO;
import com.ctdi.cnos.llm.response.OperateResult;
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
public interface OrderUserServiceClientFeign {

    /**
     * 分页查询符合条件的列表。
     *
     * @param queryParam 查询对象。
     * @return 分页列表数据。
     */
    @PostMapping(value = "/orderUser/queryPage")
    OperateResult<PageResult<OrderUserVO>> queryPage(@RequestBody QueryParam queryParam);

    /**
     * 查询符合条件的列表。
     *
     * @param queryParam 查询对象。
     * @return 列表数据。
     */
    @PostMapping(value = "/orderUser/queryList")
    OperateResult<List<OrderUserVO>> queryList(@RequestBody QueryParam queryParam);

    /**
     * 查询指定数据。
     *
     * @param id 指定主键Id。
     * @return 单条数据。
     */
	@GetMapping(value = "/orderUser/queryById")
    OperateResult<OrderUserVO> queryById(@RequestParam("id") Long id);

    /**
     * 添加操作。
     *
     * @param orderUserDTO 添加对象。
     * @return 应答结果对象。
     */
    @PostMapping(value = "/orderUser/add")
    OperateResult<String> add(@RequestBody OrderUserDTO orderUserDTO);
	
    /**
     * 更新操作。
     *
     * @param orderUserDTO 更新对象。
     * @return 应答结果对象。
     */
    @PostMapping(value = "/orderUser/updateById")
    OperateResult<String> updateById(@RequestBody OrderUserDTO orderUserDTO);

    /**
     * 删除指定的。
     *
     * @param id 指定主键Id。
     * @return 应答结果对象。
     */
    @GetMapping(value = "/orderUser/deleteById")
    OperateResult<String> deleteById(@RequestParam("id") Long id);

}