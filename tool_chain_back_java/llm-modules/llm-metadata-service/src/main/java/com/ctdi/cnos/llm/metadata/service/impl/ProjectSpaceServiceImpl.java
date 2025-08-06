package com.ctdi.cnos.llm.metadata.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.base.constant.ApplicationConstant;
import com.ctdi.cnos.llm.base.constant.SystemConstant;
import com.ctdi.cnos.llm.base.object.LambdaQueryWrapperX;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.base.service.BaseService;
import com.ctdi.cnos.llm.beans.meta.cluster.ClusterMetric;
import com.ctdi.cnos.llm.beans.meta.dataSet.DataSet;
import com.ctdi.cnos.llm.beans.meta.dictionary.DictionaryVO;
import com.ctdi.cnos.llm.beans.meta.projectSpace.*;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.metadata.dao.*;
import com.ctdi.cnos.llm.metadata.service.DictionaryService;
import com.ctdi.cnos.llm.metadata.service.ProjectSpaceService;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.system.auth.AuthUtil;
import com.ctdi.cnos.llm.system.province.dao.ProvinceDao;
import com.ctdi.cnos.llm.system.province.entity.Province;
import com.ctdi.cnos.llm.system.user.dao.UserDao;
import com.ctdi.cnos.llm.system.user.entity.User;
import com.ctdi.cnos.llm.system.user.entity.UserVO;
import com.ctdi.cnos.llm.util.DataValidatorUtils;
import com.ctdi.cnos.llm.util.ModelUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 项目空间信息表 数据操作服务类
 *
 * @author 
 * @since 2025/06/05
 */
@Slf4j
@RequiredArgsConstructor
@Service("projectSpaceService")
public class ProjectSpaceServiceImpl extends BaseService<ProjectSpaceDao, ProjectSpace, ProjectSpaceVO> implements ProjectSpaceService {
    // 集团编码
    private static final String GROUP = "911010000000000000000000";
    private final ProjectSpaceDao projectSpaceDao;
    private final ProjectUserDao projectUserDao;
    private final ProjectUserRoleDao  projectUserRoleDao;
    private final DictionaryService  dictionaryService;
    private final UserDao userDao;
    private final Label3cTreeDao label3cTreeDao;
    private final ProvinceDao provinceDao;

    @Override
    public void configureQueryWrapper(LambdaQueryWrapperX<ProjectSpace> wrapper, QueryParam queryParam) {
        ProjectSpace filter = queryParam.getFilterDto(ProjectSpace.class);
        wrapper.eqIfPresent(ProjectSpace::getProjectId, filter.getProjectId());
        wrapper.likeIfPresent(ProjectSpace::getProjectName, filter.getProjectName());
        wrapper.eqIfPresent(ProjectSpace::getProjectRegion, filter.getProjectRegion());
        wrapper.eqIfPresent(ProjectSpace::getStatus, filter.getStatus());
        wrapper.eqIfPresent(ProjectSpace::getProjectLeader, filter.getProjectLeader());
        wrapper.eqIfPresent(ProjectSpace::getProjectLabel, filter.getProjectLabel());
        wrapper.eqIfPresent(ProjectSpace::getDescription, filter.getDescription());
    }

    private void validateAndConvert(ProjectSpaceDTO projectSpaceDTO, ProjectSpace projectSpace, boolean isUpdate) {
        UserVO user = UserContextHolder.getUser();

        if (Objects.isNull(user) || !user.getIsAdmin()) {
            throw new IllegalArgumentException("非管理员用户不能操作项目空间信息");
        }
        if (!isUpdate) {
            DataValidatorUtils.ensureColumnValueValid(
                    projectSpaceDTO.getProjectName(),
                    ProjectSpace::getProjectName,
                    this::selectByProjectName,
                    "项目空间名称不能有重复"
            );
        }

        if (!user.getRegionCode().equals(GROUP) && !user.getRegionCode().equals(projectSpace.getProjectRegion())) {
            throw new IllegalArgumentException("区域管理员只能操作对应区域的项目空间");
        }
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public OperateResult<String> saveProjectSpace(ProjectSpaceDTO projectSpaceDTO) {
        try {
            ProjectSpace projectSpace = ModelUtil.copyTo(projectSpaceDTO, ProjectSpace.class);
            List<ProjectSpaceVO.UserAndRole> userAndRoles = projectSpaceDTO.getUserAndRoles();
            validateAndConvert(projectSpaceDTO, projectSpace, false);
            projectSpaceDao.insert(projectSpace);
            Long projectId = projectSpace.getProjectId();
            for (ProjectSpaceVO.UserAndRole userAndRole : userAndRoles) {
                for (Long userId :userAndRole.getUserIds()) {
                    ProjectUser projectUser = new ProjectUser();
                    projectUser.setProjectId(projectId);
                    projectUser.setUserId(userId);
                    projectUser.setStatus("1");
                    projectUserDao.insert(projectUser);
                    ProjectUserRole projectUserRole = new ProjectUserRole();
                    projectUserRole.setProjectId(projectId);
                    projectUserRole.setUserId(userId);
                    projectUserRole.setStatus("1");
                    projectUserRole.setRoleId(userAndRole.getRoleId());
                    projectUserRoleDao.insert(projectUserRole);
                }
            }
            return OperateResult.successMessage(ApplicationConstant.SAVE_MESSAGE);
        } catch (RuntimeException e) {
            log.error("保存项目空间信息失败,{}", e.getMessage());
            return OperateResult.error("保存项目空间信息失败:" + e.getMessage());
        }

    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public OperateResult<String> updateProjectSpaceById(ProjectSpaceDTO projectSpaceDTO) {
        try {
            ProjectSpace projectSpace = ModelUtil.copyTo(projectSpaceDTO, ProjectSpace.class);
            List<ProjectSpaceVO.UserAndRole> userAndRoles = projectSpaceDTO.getUserAndRoles();
            validateAndConvert(projectSpaceDTO, projectSpace, true);
            projectSpaceDao.updateById(projectSpace);
            projectUserDao.delete( new LambdaQueryWrapper<ProjectUser>()
                    .eq(ProjectUser::getProjectId, projectSpace.getProjectId()));
            projectUserRoleDao.delete(new LambdaQueryWrapper<ProjectUserRole>()
                    .eq(ProjectUserRole::getProjectId, projectSpace.getProjectId()));
            for (ProjectSpaceVO.UserAndRole userAndRole : userAndRoles) {
                for (Long userId :userAndRole.getUserIds()) {
                    ProjectUser projectUser = new ProjectUser();
                    projectUser.setProjectId(projectSpace.getProjectId());
                    projectUser.setUserId(userId);
                    projectUser.setStatus("1");
                    projectUserDao.insert(projectUser);
                    ProjectUserRole projectUserRole = new ProjectUserRole();
                    projectUserRole.setProjectId(projectSpace.getProjectId());
                    projectUserRole.setUserId(userId);
                    projectUserRole.setStatus("1");
                    projectUserRole.setRoleId(userAndRole.getRoleId());
                    projectUserRoleDao.insert(projectUserRole);
                }
            }
            return OperateResult.successMessage(ApplicationConstant.SAVE_MESSAGE);
        } catch (Exception e) {
            log.error("更新项目空间信息失败,{}", e.getMessage());
            return OperateResult.error("更新项目空间信息失败:" + e.getMessage());
        }
    }

    @Override
    public ProjectSpaceVO queryProjectSpaceById(Long id, boolean b) {
        ProjectSpace projectSpace = projectSpaceDao.selectById(id);
        if (projectSpace != null) {
            ProjectSpaceVO projectSpaceVO = new ProjectSpaceVO();
            BeanUtils.copyProperties(projectSpace, projectSpaceVO);
            String projectLabel = projectSpaceVO.getProjectLabel();
            String projectRegion = projectSpaceVO.getProjectRegion();
            wrapVo(projectSpaceVO, projectRegion, projectLabel);
            Long projectId = projectSpace.getProjectId();
            List<ProjectUserRole> projectUserRoleList = projectUserRoleDao.selectList("project_id", projectId);
            Map<Long, List<ProjectUserRole>> roleCollects
                    = projectUserRoleList.stream().collect(Collectors.groupingBy(ProjectUserRole::getRoleId));
            List<ProjectSpaceVO.UserAndRole> userAndRoles = new ArrayList<>();
            if (!MapUtils.isEmpty(roleCollects)) {
                for (Long roleId : roleCollects.keySet()) {
                    if (roleId == 1) {
                        projectSpaceVO.setAdminUserNames(roleCollects.get(roleId).stream().map(ProjectUserRole::getUserId).map(userId -> {
                            User user = userDao.selectById(userId);
                            return user.getName();
                        }).collect(Collectors.joining(",")));
                    }
                    ProjectSpaceVO.UserAndRole userAndRole = new ProjectSpaceVO.UserAndRole();
                    userAndRole.setRoleId(roleId);
                    userAndRole.setRoleName(dictionaryService.getDictItemLabel("PROJECT_ROLE_TYPE", roleId+""));
                    userAndRole.setUserIds(roleCollects.get(roleId).stream().map(ProjectUserRole::getUserId).collect(Collectors.toList()));
                    userAndRoles.add(userAndRole);
                }
            }
            projectSpaceVO.setUserAndRoles(userAndRoles);
            return projectSpaceVO;
        }
        return null;
    }

    private void wrapVo(ProjectSpaceVO projectSpaceVO, String projectRegion, String projectLabel) {
        List<Province> code = provinceDao.selectList("code", projectRegion);
        if (CollUtil.isNotEmpty(code)) {
            projectSpaceVO.setProjectRegionName(code.get(0).getName());
        }
        if (StringUtils.isNotBlank(projectLabel)) {
            List<String> labelTrees = Arrays.stream(projectLabel.split(",")).map(item -> {
                List<Label3cTree> labels = label3cTreeDao.selectList("code", item);
                return CollectionUtil.isNotEmpty(labels) ? labels.get(0).getName() : null;
            }).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(labelTrees)) {
                String labels = String.join(",", labelTrees);
                projectSpaceVO.setProjectLabelName(labels);
            }
        }

    }

    @Override
    public PageResult<ProjectSpaceVO> queryProjectSpacePage(QueryParam queryParam) {
        UserVO currentUser = UserContextHolder.getUser();
        ProjectSpaceDTO projectSpaceDTO = queryParam.getFilterDto(ProjectSpaceDTO.class);
        if (AuthUtil.hasSystemManage(currentUser)) {
            // 1.系统管理员 OR 2.区域管理员
            // 20250626 经过产品讨论，暂不考虑区域管理员，统一按照系统管理员实现
            // todo 如果实现区域管理员，需要考虑：1个人空间；2区域内项目空间；3区域外，当前区域管理员加入的项目空间
//            if (!currentUser.getRegionCode().equals(GROUP)) {
//                projectSpaceDTO.setProjectRegion(currentUser.getRegionCode());
//                queryParam.setFilterDto(projectSpaceDTO);
//            }
            PageResult<ProjectSpace> projectSpacePageResult = projectSpaceDao.selectPage(queryParam.getPageParam(), getLambdaQueryWrapper(queryParam));
            List<ProjectSpaceVO> projectSpaceVOList = convertToVoList(projectSpacePageResult.getRows(), null);
            return PageResult.makeResponseData(projectSpaceVOList, projectSpacePageResult.getTotal());
        }else {
            // 3.普通用户
            List<ProjectUserRole> projectUsers = projectUserRoleDao.selectList("user_id", currentUser.getId());
            List<Long> projectIds = projectUsers.stream().map(ProjectUserRole::getProjectId).collect(Collectors.toList());
            LambdaQueryWrapperX<ProjectSpace> wrapper = getLambdaQueryWrapper(queryParam);
            if (projectIds.isEmpty()) {
                wrapper.eq(ProjectSpace::getProjectType, 2);
            } else {
                wrapper.inIfPresent(ProjectSpace::getProjectId, projectIds);
                if (judgeQueryParam(queryParam)) {
                    wrapper.or().eq(ProjectSpace::getProjectType, 2);
                }
            }
            Page<ProjectSpace> page = new Page<>(queryParam.getPageParam().getPageNum(), queryParam.getPageParam().getPageSize());
            Page<ProjectSpace> projectSpacePage = projectSpaceDao.selectPage(page, wrapper);
            List<ProjectSpaceVO> projectSpaceVOList = convertToVoList(projectSpacePage.getRecords(), null);
            return PageResult.makeResponseData(projectSpaceVOList, projectSpacePage.getTotal());
        }
    }



    @Override
    public ProjectSpace selectByProjectName(LambdaQueryWrapper<ProjectSpace> projectSpaceLambdaQueryWrapper) {
        return projectSpaceDao.selectOne(projectSpaceLambdaQueryWrapper);
    }

    @Override
    public List<ProjectSpaceVO> queryProjectSpaceList(QueryParam queryParam) {
        UserVO currentUser = UserContextHolder.getUser();
        ProjectSpaceDTO projectSpaceDTO = queryParam.getFilterDto(ProjectSpaceDTO.class);
        if (AuthUtil.hasSystemManage(currentUser)) {
            // 1.系统管理员 OR 2.区域管理员
            // 20250626 经过产品讨论，暂不考虑区域管理员，统一按照系统管理员实现
            // todo 如果实现区域管理员，需要考虑：1个人空间；2区域内项目空间；3区域外，当前区域管理员加入的项目空间
//            if (!currentUser.getRegionCode().equals(GROUP)) {
//                projectSpaceDTO.setProjectRegion(currentUser.getRegionCode());
//                queryParam.setFilterDto(projectSpaceDTO);
//            }
            List<ProjectSpace> projectSpaces = projectSpaceDao.selectList(getLambdaQueryWrapper(queryParam));
            return convertToVoList(projectSpaces, null);
        }else {
            // 3.普通用户
            List<ProjectUserRole> projectUsers = projectUserRoleDao.selectList("user_id", currentUser.getId());
            List<Long> projectIds = projectUsers.stream().map(ProjectUserRole::getProjectId).collect(Collectors.toList());
            LambdaQueryWrapperX<ProjectSpace> wrapper = getLambdaQueryWrapper(queryParam);
            if (projectIds.isEmpty()) {
                wrapper.eq(ProjectSpace::getProjectType, 2);
            } else {
                wrapper.inIfPresent(ProjectSpace::getProjectId, projectIds);
                if (judgeQueryParam(queryParam)) {
                    wrapper.or().eq(ProjectSpace::getProjectType, 2);
                }
            }
            List<ProjectSpace> projectSpaces = projectSpaceDao.selectList(wrapper);
            return convertToVoList(projectSpaces, null);

        }
    }

    private boolean judgeQueryParam(QueryParam queryParam) {
        Map<String, Object> filterMap = queryParam.getFilterMap();
        Set<String> keySets = filterMap.keySet();
        if (!keySets.isEmpty()) {
            for(String key : keySets) {
                if (!Objects.isNull(filterMap.get(key))) {
                    return false;
                }
            }
        }
        return true;
    }
}
