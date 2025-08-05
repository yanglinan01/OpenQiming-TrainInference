package com.ctdi.cnos.llm.metadata.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ctdi.cnos.llm.base.constant.SystemConstant;
import com.ctdi.cnos.llm.base.object.LambdaQueryWrapperX;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.base.service.BaseService;
import com.ctdi.cnos.llm.beans.meta.order.OrderNode;
import com.ctdi.cnos.llm.beans.meta.order.OrderNodeVO;
import com.ctdi.cnos.llm.beans.meta.order.OrderUser;
import com.ctdi.cnos.llm.beans.meta.order.OrderUserVO;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.metadata.dao.OrderNodeDao;
import com.ctdi.cnos.llm.metadata.service.OrderNodeService;
import com.ctdi.cnos.llm.util.ModelUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *  数据操作服务类
 *
 * @author xuwj09
 * @since 2024/12/06
 */
@RequiredArgsConstructor
@Service("orderNodeService")
public class OrderNodeServiceImpl extends BaseService<OrderNodeDao, OrderNode, OrderNodeVO> implements OrderNodeService {

    private final OrderNodeDao orderNodeDao;

    @Override
    public void configureQueryWrapper(LambdaQueryWrapperX<OrderNode> wrapper, QueryParam queryParam) {
        OrderNode filter = queryParam.getFilterDto(OrderNode.class);
    }

    @Override
    public int insertBatch(List<OrderNode> orderNodeList) {
        Integer i = 0;
        DateTime date= DateUtil.date();
        if (CollectionUtils.isNotEmpty(orderNodeList)) {
            orderNodeList.forEach(o->{
                o.setId(new BigDecimal(IdUtil.getSnowflakeNextId()));
                if(Objects.nonNull(UserContextHolder.getUser())){
                    o.setCreatorId(new BigDecimal(UserContextHolder.getUser().getId()));
                    o.setModifierId(new BigDecimal(UserContextHolder.getUser().getId()));
                }
                o.setCreateDate(date);
                o.setModifyDate(date);
            });
            int pageNo = 1;
            List<OrderNode> newlist = new ArrayList<>();
            while (true) {
                if (pageNo * SystemConstant.BATCH_SIZE <= orderNodeList.size()) {
                    newlist = orderNodeList.subList((pageNo - 1) * SystemConstant.BATCH_SIZE, SystemConstant.BATCH_SIZE * pageNo);
                } else {
                    newlist = orderNodeList.subList((pageNo - 1) * SystemConstant.BATCH_SIZE, orderNodeList.size());
                }
                if (CollectionUtils.isNotEmpty(newlist)) {
                    i = i + orderNodeDao.insertBatch(newlist);
                }
                //执行插入操作
                if (CollectionUtils.isEmpty(newlist) || newlist.size() < SystemConstant.BATCH_SIZE) {
                    break;
                }
                pageNo++;
            }
        }
        return i;
    }

    /**
     * 通过同工单集合查询处理流水列表
     * @param orderUserVo
     * @return
     */
    @Override
    public List<OrderNodeVO> queryByOrderIdList(OrderNodeVO orderUserVo) {
        LambdaQueryWrapper<OrderNode> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(OrderNode::getOrderId, orderUserVo.getOrderIdList());
        wrapper.orderByDesc(OrderNode::getCreateDate);
        List<OrderNode> orderNodeList = baseMapper.selectList(wrapper);
        List<OrderNodeVO> orderNodeVOList= ModelUtil.copyCollectionTo(orderNodeList, OrderNodeVO.class);
        return orderNodeVOList;
    }
}
