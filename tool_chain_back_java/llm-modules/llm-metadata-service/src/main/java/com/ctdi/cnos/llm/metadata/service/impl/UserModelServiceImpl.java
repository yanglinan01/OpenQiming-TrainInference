package com.ctdi.cnos.llm.metadata.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.ctdi.cnos.llm.base.constant.ApplicationConstant;
import com.ctdi.cnos.llm.base.constant.MetaDataConstants;
import com.ctdi.cnos.llm.base.object.LambdaQueryWrapperX;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.base.service.BaseService;
import com.ctdi.cnos.llm.beans.meta.dictionary.DictionaryVO;
import com.ctdi.cnos.llm.beans.meta.model.Model;
import com.ctdi.cnos.llm.beans.meta.model.ModelVO;
import com.ctdi.cnos.llm.beans.meta.model.UserModel;
import com.ctdi.cnos.llm.beans.meta.model.UserModelDTO;
import com.ctdi.cnos.llm.beans.meta.model.UserModelVO;
import com.ctdi.cnos.llm.metadata.dao.UserModelDao;
import com.ctdi.cnos.llm.metadata.service.DictionaryService;
import com.ctdi.cnos.llm.metadata.service.ModelService;
import com.ctdi.cnos.llm.metadata.service.UserModelService;
import com.ctdi.cnos.llm.response.ErrorCodeEnum;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.system.user.entity.User;
import com.ctdi.cnos.llm.system.user.entity.UserDTO;
import com.ctdi.cnos.llm.system.user.service.UserService;
import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.toolkit.MPJWrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.AopContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户模型关系表 数据操作服务类
 *
 * @author wangyb
 * @since 2024/11/14
 */
@RequiredArgsConstructor
@Service("UserModelService")
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class UserModelServiceImpl extends BaseService<UserModelDao, UserModel, UserModelVO> implements UserModelService {

    private final UserModelDao UserModelDao;

    private final ModelService modelService;

    private final DictionaryService dictionaryService;

    private final UserService userService;

    @Override
    public void configureQueryWrapper(LambdaQueryWrapperX<UserModel> wrapper, QueryParam queryParam) {
        UserModel filter = queryParam.getFilterDto(UserModel.class);
    }

    @Override
    public List<UserModelVO> queryList(QueryParam queryParam) {
        return selectJoinList(UserModelVO.class, configureJoinWrapper(queryParam));
    }


    @Override
    public PageResult<UserModelVO> queryPage(QueryParam queryParam) {
        Page<UserModelVO> page = selectJoinListPage(queryParam.convertMpPage(), UserModelVO.class, configureJoinWrapper(queryParam));
        List<UserModelVO> results = page.getRecords();
        return PageResult.makeResponseData(results, page.getTotal());
    }


    @Override
    public MPJBaseJoin<UserModel> configureJoinWrapper(QueryParam queryParam) {
        UserModelVO filter = queryParam.getFilterDto(UserModelVO.class);
        return MPJWrappers.lambdaJoin(this.entityClass)
                .selectAs(UserModel::getId, UserModel::getId)
                .selectAs(UserModel::getUserId, UserModel::getUserId)
                .selectAs(User::getUserName, UserModelVO::getUserName)
                .selectAs(User::getEmployeeNumber, UserModelVO::getEmployeeNumber)
                .selectAs(UserModel::getModelId, UserModel::getModelId)
                .selectAs(UserModel::getUsage, UserModel::getUsage)
                .selectAs(Model::getName, UserModelVO::getModelName)
                .leftJoin(User.class, User::getId, UserModel::getUserId)
                .leftJoin(Model.class, Model::getId, UserModel::getModelId)
                //查询条件id, userId, employeeNumber, modelName, usage
                .eqIfExists(UserModel::getId, filter.getId())
                .eqIfExists(UserModel::getUsage, filter.getUsage())
                .eqIfExists(UserModel::getUserId, filter.getUserId())
                .eqIfExists(UserModel::getModelId, filter.getModelId())
                .likeIfExists(User::getUserName, filter.getUserName())
                .likeIfExists(Model::getName, filter.getModelName())
                .eqIfExists(User::getEmployeeNumber, filter.getEmployeeNumber())
                .orderByAsc(User::getEmployeeNumber);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserModel(Long userId, Set<Long> modelIds, String usage) {
        QueryParam queryParam = new QueryParam();
        UserModelDTO dto = new UserModelDTO();
        dto.setUserId(userId);
        dto.setUsage(usage);
        queryParam.setFilterDto(dto);
        List<UserModelVO> vos = this.queryList(queryParam);
        if (CollUtil.isNotEmpty(vos)) {
            //删除旧模型
            Set<Long> oldModelIds = vos.stream().map(UserModelVO::getModelId).collect(Collectors.toSet());

            //没传绑定的modelIds, 删除用户所有绑定的模型
            if (CollUtil.isEmpty(modelIds)) {
                removeByIds(vos.stream().map(UserModelVO::getId).collect(Collectors.toSet()));
            } else {
                Set<Long> ids = vos.stream().filter(vo -> !modelIds.contains(vo.getModelId())).map(UserModelVO::getId).collect(Collectors.toSet());
                removeByIds(ids);

                //新增模型
                Set<Long> addIds = modelIds.stream().filter(newId -> !oldModelIds.contains(newId)).collect(Collectors.toSet());
                if (CollUtil.isEmpty(addIds)) {
                    return true;
                }
                return saveBatch(this.idsToItem(userId, addIds, usage));
            }

        } else {
            if (CollUtil.isNotEmpty(modelIds)) {
                return saveBatch(this.idsToItem(userId, modelIds, usage));
            }
        }
        return true;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchBinding(String usage, Long userId, Set<Long> modelIdList, Long modelId, Set<Long> userIdList, boolean deleteBindingAll) {
        List<UserModel> result = new ArrayList<>();
        LambdaQueryWrapper<UserModel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserModel::getUsage, usage);
        if (userId != null && CollUtil.isNotEmpty(modelIdList)) {
            //删除旧的绑定数据
            queryWrapper.eq(UserModel::getUserId, userId)
                    .in(!deleteBindingAll, UserModel::getModelId, modelIdList);
            super.baseMapper.delete(queryWrapper);
            //封装新数据
            for (Long modelItemId : modelIdList) {
                UserModel item = new UserModel();
                item.setId(IdUtil.getSnowflakeNextId())
                        .setUsage(usage)
                        .setUserId(userId)
                        .setModelId(modelItemId);
                result.add(item);
            }

        } else if (modelId != null && CollUtil.isNotEmpty(userIdList)) {
            //删除旧的绑定数据
            queryWrapper.eq(UserModel::getModelId, modelId)
                    .in(!deleteBindingAll, UserModel::getUserId, userIdList);
            super.baseMapper.delete(queryWrapper);
            //封装新数据
            for (Long userItemId : userIdList) {
                UserModel item = new UserModel();
                item.setId(IdUtil.getSnowflakeNextId())
                        .setUsage(usage)
                        .setUserId(userItemId)
                        .setModelId(modelId);
                result.add(item);
            }
        }
        return saveBatch(result);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean oneClickBinding(Long userId, Long modelId) {
        List<UserModel> result = new ArrayList<>();
        //获取所有的字典值
        List<DictionaryVO> usageList = dictionaryService.getDictListByDictType(MetaDataConstants.MODEL_AUTH_USAGE_DICT);
        LambdaQueryWrapper<UserModel> queryWrapper = new LambdaQueryWrapper<>();
        if (CollUtil.isNotEmpty(usageList) && userId != null) {
            //删除旧的绑定数据
            queryWrapper.eq(UserModel::getUserId, userId);
            super.baseMapper.delete(queryWrapper);
            //查询所有的模型
            List<ModelVO> modelList = modelService.queryList(null);
            if (CollUtil.isNotEmpty(modelList)) {
                List<Long> modelIdList = modelList.stream().filter(modelVO -> CharSequenceUtil.isNotBlank(modelVO.getEndpoint())).map(ModelVO::getId).collect(Collectors.toList());
                //封装新数据
                for (DictionaryVO dictionary : usageList) {
                    for (Long modelItemId : modelIdList) {
                        UserModel item = new UserModel();
                        item.setId(IdUtil.getSnowflakeNextId())
                                .setUsage(dictionary.getDictItemValue())
                                .setUserId(userId)
                                .setModelId(modelItemId);
                        result.add(item);
                    }
                }
            }
        } else if (CollUtil.isNotEmpty(usageList) && modelId != null) {
            //删除旧的绑定数据
            queryWrapper.eq(UserModel::getModelId, modelId);
            super.baseMapper.delete(queryWrapper);
            //查询所有用户
            List<User> userList = userService.queryList(new UserDTO());
            if (CollUtil.isNotEmpty(userList)) {
                List<Long> userIdList = userList.stream().map(User::getId).collect(Collectors.toList());
                //封装新数据
                for (DictionaryVO dictionary : usageList) {
                    for (Long userItemId : userIdList) {
                        UserModel item = new UserModel();
                        item.setId(IdUtil.getSnowflakeNextId())
                                .setUsage(dictionary.getDictItemValue())
                                .setUserId(userItemId)
                                .setModelId(modelId);
                        result.add(item);
                    }
                }
            }
        }
        return saveBatch(result);
    }

    @Override
    public boolean oneClickCancelBinding(Long userId, Long modelId) {
        LambdaQueryWrapper<UserModel> queryWrapper = new LambdaQueryWrapper<>();
        if (userId != null) {
            //删除绑定数据
            queryWrapper.eq(UserModel::getUserId, userId);
        } else if (modelId != null) {
            //删除绑定数据
            queryWrapper.eq(UserModel::getModelId, modelId);
        }
        return SqlHelper.retBool(baseMapper.delete(queryWrapper));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OperateResult<String> batchBindingAll(List<UserModelDTO> userModelDTOList) {
        for (UserModelDTO userModelDTO : userModelDTOList) {
            if (userModelDTO.getUserId() != null) {
                //如果userId 不为空，那modelIdList 一定不能为空
                cn.hutool.core.lang.Assert.isTrue(CollUtil.isNotEmpty(userModelDTO.getModelIdList()), "用户ID不为空时，模型ID列表不能为空！");
                ((UserModelServiceImpl) AopContext.currentProxy()).batchBinding(userModelDTO.getUsage(), userModelDTO.getUserId(), userModelDTO.getModelIdList(), null, null, false);
            } else if (userModelDTO.getModelId() != null) {
                //如果modelId 不为空，那userIdList 一定不能为空
//                cn.hutool.core.lang.Assert.isTrue(CollUtil.isNotEmpty(userModelDTO.getUserIdList()), "模型ID不为空时，用户ID列表不能为空！");
                if(CollUtil.isNotEmpty(userModelDTO.getUserIdList())){
                    ((UserModelServiceImpl) AopContext.currentProxy()).batchBinding(userModelDTO.getUsage(), null, null, userModelDTO.getModelId(), userModelDTO.getUserIdList(), false);
                }
            }
        }
        return OperateResult.successMessage(ApplicationConstant.UPDATE_MESSAGE);
    }


    @Override
    public boolean delete(Long userId, Long modeId, String usage) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("user_id", userId);
        map.put("model_id", modeId);
        map.put("usage", usage);
        return removeByMap(map);
    }


    /**
     * 模型ID转UserModel
     *
     * @param userId 用户ID
     * @param ids    模型ID
     * @param usage  模型用途
     * @return UserModelSet
     */
    private Set<UserModel> idsToItem(Long userId, Set<Long> ids, String usage) {
        Assert.notEmpty(ids, "模型ID不能为空");
        Set<UserModel> UserModels = new HashSet<>();
        ids.forEach(modelId -> {
                    UserModel item = new UserModel();
                    item.setUserId(userId);
                    item.setUsage(usage);
                    item.setModelId(modelId);
                    UserModels.add(item);
                }
        );
        return UserModels;
    }
}
