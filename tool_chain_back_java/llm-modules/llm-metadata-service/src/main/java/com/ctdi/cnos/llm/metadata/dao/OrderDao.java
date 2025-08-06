package com.ctdi.cnos.llm.metadata.dao;

import com.ctdi.cnos.llm.base.dao.BaseDaoMapper;
import com.ctdi.cnos.llm.beans.meta.order.Order;
import org.apache.ibatis.annotations.Mapper;

/**
 *  数据操作访问接口。
 *
 * @author xuwj09
 * @since 2024/12/06
 */
@Mapper
public interface OrderDao extends BaseDaoMapper<Order> {

    public int updateByOrderId(Order order);

}
