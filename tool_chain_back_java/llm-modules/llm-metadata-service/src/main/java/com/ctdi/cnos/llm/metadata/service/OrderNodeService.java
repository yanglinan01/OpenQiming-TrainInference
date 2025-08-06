package com.ctdi.cnos.llm.metadata.service;

import com.ctdi.cnos.llm.base.service.IBaseService;
import com.ctdi.cnos.llm.beans.meta.order.OrderNode;
import com.ctdi.cnos.llm.beans.meta.order.OrderNodeVO;
import com.ctdi.cnos.llm.beans.meta.order.OrderUser;
import com.ctdi.cnos.llm.beans.meta.order.OrderUserVO;

import java.util.List;

/**
 *  数据操作服务接口。
 *
 * @author xuwj09
 * @since 2024/12/06
 */
public interface OrderNodeService extends IBaseService<OrderNode, OrderNodeVO> {

    int insertBatch(List<OrderNode> orderNodeList);

    public List<OrderNodeVO> queryByOrderIdList(OrderNodeVO orderNodeVo);
}
