package com.ctdi.cnos.llm.metadata.dao;

import com.ctdi.cnos.llm.base.dao.BaseDaoMapper;
import com.ctdi.cnos.llm.beans.meta.order.OrderUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 *  数据操作访问接口。
 *
 * @author xuwj09
 * @since 2024/12/06
 */
@Mapper
public interface OrderUserDao extends BaseDaoMapper<OrderUser> {

    int insertBatch(List<OrderUser> orderUserList);
}
