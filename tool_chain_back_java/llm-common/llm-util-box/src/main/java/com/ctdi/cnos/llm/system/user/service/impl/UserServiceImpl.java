package com.ctdi.cnos.llm.system.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ctdi.cnos.llm.base.constant.CacheConstant;
import com.ctdi.cnos.llm.base.constant.SystemConstant;
import com.ctdi.cnos.llm.base.object.LambdaQueryWrapperX;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.base.service.BaseService;
import com.ctdi.cnos.llm.cache.ctg.CtgCache;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.system.user.dao.UserDao;
import com.ctdi.cnos.llm.system.user.entity.User;
import com.ctdi.cnos.llm.system.user.entity.UserDTO;
import com.ctdi.cnos.llm.system.user.entity.UserVO;
import com.ctdi.cnos.llm.system.user.service.UserService;
import com.ctdi.cnos.llm.util.ModelUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 数据操作服务类
 *
 * @author huangjinhua
 * @since 2024/07/22
 */
public class UserServiceImpl extends BaseService<UserDao, User, UserVO> implements UserService {

    private final CtgCache ctgCache;

    public UserServiceImpl(CtgCache ctgCache, UserDao baseMapper) {
        this.ctgCache = ctgCache;
        super.baseMapper = baseMapper;
    }

    @Override
    public void configureQueryWrapper(LambdaQueryWrapperX<User> wrapper, QueryParam queryParam) {
        User filter = queryParam.getFilterDto(User.class);
        wrapper.eqIfPresent(User::getId, filter.getId());
        wrapper.likeIfPresent(User::getName, filter.getName());
        wrapper.likeIfPresent(User::getUserName, filter.getUserName());
        wrapper.likeIfPresent(User::getEmployeeNumber, filter.getEmployeeNumber());
    }

    @Override
    public UserVO queryByUserName(String userName) {
        User user = baseMapper.selectOne(User::getUserName, userName);
        return ModelUtil.copyTo(user, UserVO.class);
    }

    @Override
    public UserVO queryByEmployeeNumber(String employeeNumber) {
        User user = baseMapper.selectOne(User::getEmployeeNumber, employeeNumber);
        return ModelUtil.copyTo(user, UserVO.class);
    }

    @Override
    public UserVO queryByEmployeeNumberForCache(String employeeNumber) {
        UserVO userVO = ctgCache.getCache(CacheConstant.USER_CACHE_NAME, employeeNumber);
        if (userVO == null) {
            User user = baseMapper.selectOne(User::getEmployeeNumber, employeeNumber);
            userVO = ModelUtil.copyTo(user, UserVO.class);
            if (userVO != null) {
                ctgCache.set(CacheConstant.USER_CACHE_NAME, employeeNumber, userVO, CacheConstant.CACHE_EXPIRE_1_DAY);
            }
        }
        return userVO;
    }

    @Override
    public List<User> queryListByEmployeeNumbers(List<String> employeeNumberList) {
        List<User> result = new ArrayList<>();
        List<List<String>> partList = CollUtil.split(employeeNumberList, 300);
        for (List<String> list : partList) {
            LambdaQueryWrapper<User> userQuery = new LambdaQueryWrapper<>();
            userQuery.in(User::getEmployeeNumber, list);
            result.addAll(baseMapper.selectList(userQuery));
        }
        return result;
    }

    @Override
    public List<User> queryValidList(UserDTO userDTO) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.isNotNull(User::getLastActiveTime)
                .like(CharSequenceUtil.isNotBlank(userDTO.getEmployeeNumber()), User::getEmployeeNumber, userDTO.getEmployeeNumber())
                .like(CharSequenceUtil.isNotBlank(userDTO.getName()), User::getName, userDTO.getName())
                .and(i -> i.eq(User::getToolAuth, SystemConstant.YES).or().eq(User::getAgentAuth, SystemConstant.YES));
        return baseMapper.selectList(wrapper);
    }


    @Override
    public List<User> queryList(UserDTO userDTO) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(CharSequenceUtil.isNotBlank(userDTO.getEmployeeNumber()), User::getEmployeeNumber, userDTO.getEmployeeNumber())
                .like(CharSequenceUtil.isNotBlank(userDTO.getName()), User::getName, userDTO.getName());
        return baseMapper.selectList(wrapper);
    }

    @Override
    public boolean isExistUser(Long userId, String employeeNumber, String userName) {
        boolean isExist;
        User user;
        if (CharSequenceUtil.isNotBlank(employeeNumber)) {
            user = baseMapper.selectOne(User::getEmployeeNumber, employeeNumber);
            isExist = (user != null && !user.getId().equals(userId));
            if (isExist) {
                return true;
            }
        }
        if (CharSequenceUtil.isNotBlank(userName)) {
            user = baseMapper.selectOne(User::getUserName, userName);
            isExist = (user != null && !user.getId().equals(userId));
            if (isExist) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean updateByByEmployeeNumber(User user) {
        if (CharSequenceUtil.isNotBlank(user.getEmployeeNumber())) {
            LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(user.getId() != null, User::getId, user.getId());
            updateWrapper.eq(CharSequenceUtil.isNotBlank(user.getEmployeeNumber()), User::getEmployeeNumber, user.getEmployeeNumber());
            int update = baseMapper.update(user, updateWrapper);
            return update > 0;
        }
        return false;
    }

    @Override
    public long countPlatformUser() {
        return baseMapper.countPlatformUser();
    }

    @Override
    public long countCompany() {
        return baseMapper.countCompany();
    }

    @Override
    public int updateBatchAuthByEmployeeNumbers(List<User> userList) {
        Integer i = 0;
        DateTime date = DateUtil.date();
        if (CollectionUtils.isNotEmpty(userList)) {
            userList.forEach(o -> {
                if (Objects.nonNull(UserContextHolder.getUser())) {
                    o.setModifierId(UserContextHolder.getUser().getId());
                }
                o.setModifyDate(date);
            });
            int pageNo = 1;
            List<User> newlist = new ArrayList<>();
            while (true) {
                if (pageNo * SystemConstant.BATCH_SIZE <= userList.size()) {
                    newlist = userList.subList((pageNo - 1) * SystemConstant.BATCH_SIZE, SystemConstant.BATCH_SIZE * pageNo);
                } else {
                    newlist = userList.subList((pageNo - 1) * SystemConstant.BATCH_SIZE, userList.size());
                }
                if (CollectionUtils.isNotEmpty(newlist)) {
                    i = i + baseMapper.updateBatchAuthByEmployeeNumbers(newlist);
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
}