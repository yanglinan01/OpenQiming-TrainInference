package com.ctdi.cnos.llm.metadata.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ctdi.cnos.llm.base.constant.SystemConstant;
import com.ctdi.cnos.llm.base.object.LambdaQueryWrapperX;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.base.service.BaseService;
import com.ctdi.cnos.llm.beans.meta.order.OrderUser;
import com.ctdi.cnos.llm.beans.meta.order.OrderUserDTO;
import com.ctdi.cnos.llm.beans.meta.order.OrderUserVO;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.metadata.dao.OrderUserDao;
import com.ctdi.cnos.llm.metadata.service.OrderUserService;
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
 * 数据操作服务类
 *
 * @author xuwj09
 * @since 2024/12/06
 */
@RequiredArgsConstructor
@Service("orderUserService")
public class OrderUserServiceImpl extends BaseService<OrderUserDao, OrderUser, OrderUserVO> implements OrderUserService {

    private final OrderUserDao orderUserDao;

    @Override
    public void configureQueryWrapper(LambdaQueryWrapperX<OrderUser> wrapper, QueryParam queryParam) {
        OrderUser filter = queryParam.getFilterDto(OrderUser.class);
    }

    @Override
    public int insertBatch(List<OrderUser> orderUserList) {
        Integer i = 0;
        DateTime date = DateUtil.date();
        if (CollectionUtils.isNotEmpty(orderUserList)) {
            orderUserList.forEach(o -> {
                o.setId(new BigDecimal(IdUtil.getSnowflakeNextId()));
                if (Objects.nonNull(UserContextHolder.getUser())) {
                    o.setCreatorId(new BigDecimal(UserContextHolder.getUser().getId()));
                    o.setModifierId(new BigDecimal(UserContextHolder.getUser().getId()));
                }
                o.setCreateDate(date);
                o.setModifyDate(date);
            });
            int pageNo = 1;
            List<OrderUser> newlist = new ArrayList<>();
            while (true) {
                if (pageNo * SystemConstant.BATCH_SIZE <= orderUserList.size()) {
                    newlist = orderUserList.subList((pageNo - 1) * SystemConstant.BATCH_SIZE, SystemConstant.BATCH_SIZE * pageNo);
                } else {
                    newlist = orderUserList.subList((pageNo - 1) * SystemConstant.BATCH_SIZE, orderUserList.size());
                }
                if (CollectionUtils.isNotEmpty(newlist)) {
                    i = i + orderUserDao.insertBatch(newlist);
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
     * 通过同工单集合查询开通账户列表
     * @param orderUserVo
     * @return
     */
    @Override
    public List<OrderUserVO> queryByOrderIdList(OrderUserVO orderUserVo) {
        LambdaQueryWrapper<OrderUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(OrderUser::getOrderId, orderUserVo.getOrderIdList());
        wrapper.orderByDesc(OrderUser::getCreateDate);
        List<OrderUser> orderUserList = baseMapper.selectList(wrapper);
        List<OrderUserVO> orderUserVOList=ModelUtil.copyCollectionTo(orderUserList, OrderUserVO.class);
        return orderUserVOList;
    }
}
