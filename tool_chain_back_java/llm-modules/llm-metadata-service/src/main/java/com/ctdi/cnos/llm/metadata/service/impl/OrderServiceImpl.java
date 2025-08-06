package com.ctdi.cnos.llm.metadata.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ctdi.cnos.llm.base.constant.MetaDataConstants;
import com.ctdi.cnos.llm.base.constant.SystemConstant;
import com.ctdi.cnos.llm.base.object.LambdaQueryWrapperX;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.base.service.BaseService;
import com.ctdi.cnos.llm.beans.meta.order.*;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.metadata.client.ApiClient;
import com.ctdi.cnos.llm.metadata.dao.OrderDao;
import com.ctdi.cnos.llm.metadata.service.OrderNodeService;
import com.ctdi.cnos.llm.metadata.service.OrderService;
import com.ctdi.cnos.llm.metadata.service.OrderUserService;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.system.user.entity.User;
import com.ctdi.cnos.llm.system.user.service.UserService;
import com.ctdi.cnos.llm.util.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 数据操作服务类
 *
 * @author xuwj09
 * @since 2024/12/06
 */
@RequiredArgsConstructor
@Service("orderService")
public class OrderServiceImpl extends BaseService<OrderDao, Order, OrderVO> implements OrderService {

    private final OrderDao orderDao;

    private final OrderUserService orderUserService;

    private final OrderNodeService orderNodeService;

    private final UserService userService;

    private final ApiClient apiClient;

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    public void configureQueryWrapper(LambdaQueryWrapperX<Order> wrapper, QueryParam queryParam) {
        Order filter = queryParam.getFilterDto(Order.class);
        wrapper.eqIfPresent(Order::getId, filter.getId());
        wrapper.eqIfPresent(Order::getOrderCode, filter.getOrderCode());
        wrapper.eqIfPresent(Order::getReviewStatus, filter.getReviewStatus());
    }

    /**
     * 入工单三张表
     *
     * @param order
     */
    @Override
    public void insertAll(Order order) {
        DateTime date = DateUtil.date();
        BigDecimal orderId = new BigDecimal(IdUtil.getSnowflakeNextId());
        order.setId(orderId);
        if (Objects.nonNull(UserContextHolder.getUser())) {
            order.setCreatorId(new BigDecimal(UserContextHolder.getUser().getId()));
            order.setModifierId(new BigDecimal(UserContextHolder.getUser().getId()));
        }
        order.setCreateDate(date);
        order.setModifyDate(date);
        order.setReviewStatus(MetaDataConstants.ORDER_REVIEW_STATUS_DICT_PROCESS);
        order.setReviewTime(date);
        //新增工单表
        orderDao.insert(order);
        List<OrderUser> orderUserList = order.getAccountInfoList();
        //新增开通账户表
        if (CollectionUtils.isNotEmpty(orderUserList)) {
            orderUserList.forEach(o -> {
                o.setOrderCode(order.getOrderCode());
                o.setOrderId(orderId);
            });
            orderUserService.insertBatch(orderUserList);
        }
        //新增流程
        List<OrderNode> orderNodeList = order.getFlowProcessingList();
        if (CollectionUtils.isNotEmpty(orderNodeList)) {
            orderNodeList.forEach(o -> {
                o.setOrderCode(order.getOrderCode());
                o.setOrderId(orderId);
            });
            orderNodeService.insertBatch(orderNodeList);
        }
    }

    /**
     * 同步门户权限
     *
     * @param order
     * @return
     */
    @Override
    public OperateResult<String> syncOrder(Order order) {
        String msg = MetaDataConstants.API_SUCCESS_RESPONSE;
        try {
            order.setReviewStatus(MetaDataConstants.ORDER_REVIEW_STATUS_DICT_PROCESS);
            List<OrderUser> orderUserList = order.getAccountInfoList();
            this.insertAll(order);
            //判断是否需要更新用户表
            if (CollectionUtils.isNotEmpty(orderUserList)) {
                List<OrderUser> insertOrderUser = BeanUtil.copyToList(orderUserList, OrderUser.class);
                List<String> employeeNumberList = orderUserList.stream().map(o -> {
                    return o.getToolChainOAName();
                }).collect(Collectors.toList());
                List<User> userList = userService.queryListByEmployeeNumbers(employeeNumberList);
                //过滤已入库用户
                if (CollectionUtils.isNotEmpty(userList)) {
                    List<String> filterEmployeeNumberList = userList.stream().map(o -> {
                        return o.getEmployeeNumber();
                    }).collect(Collectors.toList());
                    insertOrderUser = orderUserList.stream().filter(o -> {
                        return !filterEmployeeNumberList.contains(o.getToolChainOAName());
                    }).collect(Collectors.toList());
                }
                //转换新增用户
                List<User> insertUserList = this.orderUserToUser(insertOrderUser);
                userService.saveBatch(insertUserList);
            }
            return OperateResult.successMessage(msg);
        } catch (Exception e) {
            msg = "message:" + e.getMessage() + "stackTrace:" + e.getStackTrace();
            log.error(msg);
            return OperateResult.error(msg);
        }
    }

    /**
     * 分页查询待审核用户
     *
     * @param queryParam
     * @return
     */
    @Override
    public PageResult<OrderVO> queryOrderAccountPage(QueryParam queryParam) {
        PageResult<OrderVO> orderPageResult = this.queryPage(queryParam);
        List<OrderVO> orderVoList = orderPageResult.getRows();
        if (CollectionUtils.isNotEmpty(orderVoList)) {
            List<BigDecimal> idList = orderVoList.stream().map(o -> {
                return o.getId();
            }).collect(Collectors.toList());
            //查询开通账户列表
            OrderUserVO orderUserVO = new OrderUserVO();
            orderUserVO.setOrderIdList(idList);
            List<OrderUserVO> orderUserList = orderUserService.queryByOrderIdList(orderUserVO);
            Map<BigDecimal, List<OrderUserVO>> orderUserMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(orderUserList)) {
                orderUserMap = orderUserList.stream().collect(Collectors.groupingBy(OrderUserVO::getOrderId));
            }

            //查询处理流水列表
            OrderNodeVO orderNodeVO = new OrderNodeVO();
            orderNodeVO.setOrderIdList(idList);
            List<OrderNodeVO> orderNodeVOList = orderNodeService.queryByOrderIdList(orderNodeVO);
            Map<BigDecimal, List<OrderNodeVO>> orderNodeMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(orderNodeVOList)) {
                orderNodeMap = orderNodeVOList.stream().collect(Collectors.groupingBy(OrderNodeVO::getOrderId));
            }
            //查询现有用户
            List<String> toolChainOANameList = orderUserList.stream().map(o -> {
                return o.getToolChainOAName();
            }).distinct().collect(Collectors.toList());
            List<User> userList = userService.queryListByEmployeeNumbers(toolChainOANameList);
            Map<String, User> userMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(userList)) {
                userMap = userList.stream()
                        .collect(Collectors.toMap(User::getEmployeeNumber, p -> p, (p1, p2) -> p1));
            }
            //拼接成待开通账户
            for (OrderVO orderVO : orderVoList) {
                List<OpenAccountInfoVo> openAccountInfoVoList = new ArrayList<>();
                orderVO.setOpenAccountInfoVoList(openAccountInfoVoList);
                List<OrderUserVO> orderUserVOList = orderUserMap.get(orderVO.getId());
                if (CollectionUtils.isNotEmpty(orderUserVOList)) {
                    orderVO.setAccountInfoList(orderUserVOList);
                    for (OrderUserVO userVO : orderUserVOList) {
                        OpenAccountInfoVo openAccountInfoVo = new OpenAccountInfoVo();
                        BeanUtil.copyProperties(userVO, openAccountInfoVo);
                        User user = userMap.get(userVO.getToolChainOAName());
                        BeanUtil.copyProperties(user, openAccountInfoVo);
                        openAccountInfoVo.setCreateDate(orderVO.getCreateDate());
                        //开通需求
                        this.setPermissionState(openAccountInfoVo, orderVO, orderUserVO);
                        openAccountInfoVoList.add(openAccountInfoVo);
                    }
                }
                List<OrderNodeVO> orderNodeVOS = orderNodeMap.get(orderVO.getId());
                if (CollectionUtils.isNotEmpty(orderNodeVOS)) {
                    orderVO.setFlowProcessingList(orderNodeVOS);
                }
            }
        }
        return PageResult.makeResponseData(orderVoList, orderPageResult.getTotal());
    }

    /**
     * 提交待审核用户权限
     *
     * @param orderVOList
     * @return
     */
    @Override
    public String submitOrderAccount(List<OrderVO> orderVOList) {
        List<OpenAccountInfoVo> openAccountInfoVoList = new ArrayList<>();
        for (OrderVO orderVO : orderVOList) {
            if (CollectionUtils.isNotEmpty(orderVO.getOpenAccountInfoVoList())) {
                openAccountInfoVoList.addAll(orderVO.getOpenAccountInfoVoList());
            }
        }
        //过滤重复用户取创建日期最大的
        List<OpenAccountInfoVo> filterOpenAccountInfoVoList = openAccountInfoVoList.stream()
                .sorted(Comparator.comparing(OpenAccountInfoVo::getCreateDate, Comparator.reverseOrder()))
                .filter(CommonUtils.distinctByKey(p -> p.getToolChainOAName())).collect(Collectors.toList());
        //更新用户权限
        List<User> userList = filterOpenAccountInfoVoList.stream().map(o -> {
            User user = new User();
            user.setAgentAuth(o.getAgentPermissionState());
            user.setToolAuth(o.getToolPermissionState());
            user.setEmployeeNumber(o.getToolChainOAName());
            return user;
        }).collect(Collectors.toList());
        userService.updateBatchAuthByEmployeeNumbers(userList);
        return MetaDataConstants.API_SUCCESS_RESPONSE;
    }

    /**
     * 根据前台给的权限修改回调的权限
     *
     * @param orderVOList
     */
    private static void transPermissionState(List<OrderVO> orderVOList) {
        for (OrderVO orderVO : orderVOList) {
            List<OpenAccountInfoVo> openAccountInfoVoList = orderVO.getOpenAccountInfoVoList();
            if (CollUtil.isNotEmpty(openAccountInfoVoList)) {
                for (OpenAccountInfoVo openAccountInfoVo : openAccountInfoVoList) {
                    List<OrderUserVO> orderUserVOList = orderVO.getAccountInfoList();
                    if(CollUtil.isNotEmpty(orderUserVOList)){
                        for(OrderUserVO orderUserVO:orderUserVOList){
                            if(openAccountInfoVo.getToolChainOAName().equals(orderUserVO.getToolChainOAName())){
                                if(MetaDataConstants.ORDER_REFERENCE_SYSTEM_TOOL.equals(orderVO.getReferenceSystem())){
                                    orderUserVO.setPermissionState(SystemConstant.YES.equals(openAccountInfoVo.getToolPermissionState())?
                                            MetaDataConstants.ORDER_PERMISSION_STATE_DICT_OPEN:MetaDataConstants.ORDER_PERMISSION_STATE_DICT_CLOSE);
                                }
                                if(MetaDataConstants.ORDER_REFERENCE_SYSTEM_AGENT.equals(orderVO.getReferenceSystem())){
                                    orderUserVO.setPermissionState(SystemConstant.YES.equals(openAccountInfoVo.getAgentPermissionState())?
                                            MetaDataConstants.ORDER_PERMISSION_STATE_DICT_OPEN:MetaDataConstants.ORDER_PERMISSION_STATE_DICT_CLOSE);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 调用回调接口
     *
     * @param orderVOList
     * @return
     */
    private List<BigDecimal> userRoleCallBackAsync(List<OrderVO> orderVOList) {
        List<OrderCallBack> list = new ArrayList<>();
        for (OrderVO orderVO : orderVOList) {
            OrderCallBack orderDTO = new OrderCallBack();
            BeanUtils.copyProperties(orderVO, orderDTO);
            orderDTO.setAccountInfoList(new ArrayList<>());
            orderDTO.setFlowProcessingList(new ArrayList<>());
            for (OrderUserVO orderUserVO : orderVO.getAccountInfoList()) {
                OrderUserCallBack orderUserDTO = new OrderUserCallBack();
                BeanUtils.copyProperties(orderUserVO, orderUserDTO);
                orderDTO.getAccountInfoList().add(orderUserDTO);
            }
            for (OrderNodeVO orderNodeVO : orderVO.getFlowProcessingList()) {
                OrderNodeCallBack orderNodeDTO = new OrderNodeCallBack();
                BeanUtils.copyProperties(orderNodeVO, orderNodeDTO);
                orderDTO.getFlowProcessingList().add(orderNodeDTO);
            }
            list.add(orderDTO);
        }
        List<BigDecimal> success = apiClient.userRoleCallBack(list, UserContextHolder.getUserId());
        if (success.size() > 0) {
            LambdaUpdateWrapper<Order> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(Order::getUploadStatus, SystemConstant.YES);
            updateWrapper.set(Order::getReviewTime, DateUtil.date());
            updateWrapper.in(Order::getId, success);
            orderDao.update(null, updateWrapper);
        }
        return success;
    }

    private void setPermissionState(OpenAccountInfoVo openAccountInfoVo, OrderVO orderVO, OrderUserVO orderUserVO) {
        //最低优先级，默认开通
        openAccountInfoVo.setToolPermissionState(SystemConstant.YES);
        openAccountInfoVo.setAgentPermissionState(SystemConstant.YES);
        //其次优先级，获取待开通
        if (MetaDataConstants.ORDER_REFERENCE_SYSTEM_TOOL.equals(orderVO.getReferenceSystem())) {
            if (MetaDataConstants.ORDER_PERMISSION_STATE_DICT_OPEN.equals(orderUserVO.getPermissionState())) {
                openAccountInfoVo.setToolPermissionState(SystemConstant.YES);
            }
            if (MetaDataConstants.ORDER_PERMISSION_STATE_DICT_CLOSE.equals(orderUserVO.getPermissionState())) {
                openAccountInfoVo.setToolPermissionState(SystemConstant.NO);
            }
        }
        if (MetaDataConstants.ORDER_REFERENCE_SYSTEM_AGENT.equals(orderVO.getReferenceSystem())) {
            if (MetaDataConstants.ORDER_PERMISSION_STATE_DICT_OPEN.equals(orderUserVO.getPermissionState())) {
                openAccountInfoVo.setAgentPermissionState(SystemConstant.YES);
            }
            if (MetaDataConstants.ORDER_PERMISSION_STATE_DICT_CLOSE.equals(orderUserVO.getPermissionState())) {
                openAccountInfoVo.setAgentPermissionState(SystemConstant.NO);
            }
        }

    }


    /**
     * 通开账号列表转用户
     *
     * @param orderUserList
     * @return
     */
    private List<User> orderUserToUser(List<OrderUser> orderUserList) {
        DateTime date = DateUtil.date();
        List<User> userList = orderUserList.stream().map(o -> {
            User user = new User();
            user.setId(IdUtil.getSnowflakeNextId());
            user.setEmployeeNumber(o.getToolChainOAName());
            user.setName(o.getToolChainName());
            user.setUserName(o.getToolChainName());
            user.setRegionName(o.getProvinceCompany());
            user.setCorpName(o.getProvinceCompany());
            user.setMobile(o.getToolChainPhone());
            if (Objects.nonNull(UserContextHolder.getUser())) {
                user.setCreatorId(UserContextHolder.getUser().getId());
                user.setModifierId(UserContextHolder.getUser().getId());
            }
            if (CharSequenceUtil.isNotBlank(user.getEmployeeNumber())) {
                int index = user.getEmployeeNumber().indexOf("@");
                if (index > -1) {
                    String belong = CharSequenceUtil.sub(user.getEmployeeNumber(), index + 1, user.getEmployeeNumber().length());
                    user.setBelong(belong);
                }
            }
            user.setLastActiveTime(date);
            user.setCreateDate(date);
            user.setModifyDate(date);
            return user;
        }).collect(Collectors.toList());
        return userList;
    }
}
