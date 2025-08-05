package com.ctdi.cnos.llm.metadata.service;

import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.base.service.IBaseService;
import com.ctdi.cnos.llm.beans.meta.order.OrderUser;
import com.ctdi.cnos.llm.beans.meta.order.OrderUserVO;

import java.util.List;

/**
 *  数据操作服务接口。
 *
 * @author xuwj09
 * @since 2024/12/06
 */
public interface OrderUserService extends IBaseService<OrderUser, OrderUserVO> {

    int insertBatch(List<OrderUser> orderUserList);

    public List<OrderUserVO> queryByOrderIdList(OrderUserVO orderUserVo);
}
