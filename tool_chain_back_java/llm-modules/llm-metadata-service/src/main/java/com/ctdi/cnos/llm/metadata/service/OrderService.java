package com.ctdi.cnos.llm.metadata.service;

import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.base.service.IBaseService;
import com.ctdi.cnos.llm.beans.meta.order.Order;
import com.ctdi.cnos.llm.beans.meta.order.OrderVO;
import com.ctdi.cnos.llm.response.OperateResult;

import java.util.List;

/**
 *  数据操作服务接口。
 *
 * @author xuwj09
 * @since 2024/12/06
 */
public interface OrderService extends IBaseService<Order, OrderVO> {

    OperateResult<String> syncOrder(Order order);

    PageResult<OrderVO> queryOrderAccountPage(QueryParam queryParam);

    String submitOrderAccount(List<OrderVO> orderVOList);

    void insertAll(Order order);
}
