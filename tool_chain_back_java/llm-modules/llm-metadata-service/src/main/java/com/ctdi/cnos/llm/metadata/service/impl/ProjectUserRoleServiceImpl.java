package com.ctdi.cnos.llm.metadata.service.impl;

import cn.hutool.db.meta.JdbcType;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctc.wstx.util.StringUtil;
import com.ctdi.cnos.llm.base.constant.ApplicationConstant;
import com.ctdi.cnos.llm.base.object.LambdaQueryWrapperX;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.base.object.QueryWrapperX;
import com.ctdi.cnos.llm.base.service.BaseService;
import com.ctdi.cnos.llm.beans.meta.projectSpace.*;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.metadata.dao.ProjectUserDao;
import com.ctdi.cnos.llm.metadata.dao.ProjectUserRoleDao;
import com.ctdi.cnos.llm.metadata.service.DictionaryService;
import com.ctdi.cnos.llm.metadata.service.ProjectUserRoleService;
import com.ctdi.cnos.llm.response.ErrorCodeEnum;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.system.auth.AuthUtil;
import com.ctdi.cnos.llm.system.user.entity.User;
import com.ctdi.cnos.llm.system.user.entity.UserVO;
import com.ctdi.cnos.llm.system.user.service.UserService;
import com.ctdi.cnos.llm.util.ModelUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.JDBCType;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 项目空间用户角色关联信息表 数据操作服务类
 *
 * @author 
 * @since 2025/06/05
 */
@RequiredArgsConstructor
@Service("projectUserRoleService")
public class ProjectUserRoleServiceImpl extends BaseService<ProjectUserRoleDao, ProjectUserRole, ProjectUserRoleVO> implements ProjectUserRoleService {

    private final UserService userService;
    private final DictionaryService dictionaryService;
    private final ProjectUserRoleDao projectUserRoleDao;
    private final ProjectUserDao projectUserDao;
    private final ProjectUserServiceImpl projectUserService;

    @Override
    public void configureQueryWrapper(LambdaQueryWrapperX<ProjectUserRole> wrapper, QueryParam queryParam) {
        ProjectUserRole filter = queryParam.getFilterDto(ProjectUserRole.class);
    }

    @Override
    public PageResult<ProjectUserRoleVO> queryPagePage(long pageSize, long currentPage, Long projectId, Long roleId, String name) {

        // 1. 如果用户姓名为空，则直接按 roleId 查询所有
        List<Long> userIds = new ArrayList<>();
        if (!StringUtils.isBlank(name)) {
            // 2. 模糊查询用户表，得到匹配的 userIds
            List<User> users = userService.list(new LambdaQueryWrapperX<User>()
                    .likeIfPresent(User::getName, name));
            userIds = users.stream().map(User::getId).collect(Collectors.toList());
            if (userIds.isEmpty()) {
                return PageResult.makeResponseData(new ArrayList<>(), 0L);
            }
        }

        Page<ProjectUserRole> page = new Page<>(currentPage, pageSize);
        Page<ProjectUserRole> resultPage = projectUserRoleDao.selectByCondition(projectId, roleId, userIds, page);

        List<ProjectUserRoleVO> voList = convertToVoList(resultPage.getRecords(), null);
        voList.forEach(vo -> {
            Long userId = vo.getUserId();
            UserVO userVO = userService.queryById(userId, false);
            if (null != userVO) {
                vo.setName(userVO.getName());
                vo.setEmployeeNumber(userVO.getEmployeeNumber());
            }
            Long tempRoleId = vo.getRoleId();
            vo.setRoleName(dictionaryService.getDictItemLabel("PROJECT_ROLE_TYPE", tempRoleId+""));
        });
        return PageResult.makeResponseData(voList, resultPage.getTotal());
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean deleteByEntity(ProjectUserRole projectUserRole) {
        if (projectUserRoleDao.delete(projectUserRole) > 0) {
            projectUserDao.deleteByProjectIdAndUserId(projectUserRole.getProjectId(), projectUserRole.getUserId());
            return true;
        }
        return false;
    }

    @Override
    public OperateResult<String> saveProjectUserRole(ProjectUserRole projectUserRole) {
        try {
            checkRole(projectUserRole);
            this.save(projectUserRole);
            return OperateResult.successMessage(ApplicationConstant.SAVE_MESSAGE);
        } catch (Exception e) {
            log.error("保存项目空间用户角色失败,{}", e);
            return OperateResult.error("保存项目空间用户角色关联信息失败：" + e.getMessage());
        }
    }

    private void checkRole(ProjectUserRole projectUserRole) {
        if (Objects.isNull(projectUserRole.getProjectId())) {
            throw new IllegalArgumentException("项目空间ID不能为空");
        }
        if (Objects.isNull(projectUserRole.getUserId())) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        UserVO user = UserContextHolder.getUser();
        if (!AuthUtil.hasSystemManage(user)) {
            LambdaQueryWrapperX<ProjectUserRole> wrapper = new LambdaQueryWrapperX<ProjectUserRole>()
                    .eq(ProjectUserRole::getProjectId, projectUserRole.getProjectId())
                    .eq(ProjectUserRole::getUserId, user.getId());
            ProjectUserRole tempUserRole = projectUserRoleDao.selectOne(wrapper);
            if (Objects.isNull(tempUserRole) ||tempUserRole.getRoleId() != 1) {
                throw new IllegalArgumentException("用户不是管理员，不能添加项目空间用户角色");
            }
        }
    }

    @Override
    public OperateResult<String> updateByProjectUserRole(ProjectUserRole projectUserRole) {
        try {
            checkRole(projectUserRole);
            projectUserRoleDao.updateByEntity(projectUserRole);
            return OperateResult.successMessage(ApplicationConstant.UPDATE_MESSAGE);
        } catch (Exception e) {
            log.error("更新项目空间用户角色失败,{}", e);
            return OperateResult.error("更新项目空间用户角色关联信息失败：" + e.getMessage());
        }
    }

    @Override
    public Boolean judgeManager(Long projectId, Long userId) {
        User user = userService.getById(userId);
        List<ProjectUserRole> projectUserRoles = projectUserRoleDao.selectList(new LambdaQueryWrapperX<ProjectUserRole>()
                .eq(ProjectUserRole::getProjectId, projectId)
                .eq(ProjectUserRole::getUserId, userId)
                .eq(ProjectUserRole::getRoleId, 1));
        return AuthUtil.hasSystemManage(user) || !projectUserRoles.isEmpty();

    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public OperateResult<String> addBatch(ProjectRoleUsersDTO projectRoleUsersDTO) {
        try {
            List<Long> userIds = projectRoleUsersDTO.getUserIds();
            if (userIds == null || userIds.isEmpty()) {
                return OperateResult.error("用户ID不能为空");
            }
            QueryWrapper<ProjectUserRole> wrapper = new QueryWrapper<ProjectUserRole>()
                    .eq("project_id", projectRoleUsersDTO.getProjectId())
                            .in("user_id", userIds);
            List<ProjectUserRole> projectUserRoleList = projectUserRoleDao.selectList(wrapper);
            if (!projectUserRoleList.isEmpty()) {
                return OperateResult.error("项目空间用户角色管理信息表已存在,请重新选择用户");
            }

            List<ProjectUserRole> projectUserRoles = projectRoleUsersDTO.getUserIds().stream().map(userId -> {
                ProjectUserRole projectUserRole = new ProjectUserRole();
                projectUserRole.setProjectId(projectRoleUsersDTO.getProjectId());
                projectUserRole.setRoleId(projectRoleUsersDTO.getRoleId());
                projectUserRole.setUserId(userId);
                return projectUserRole;
            }).collect(Collectors.toList());
            List<ProjectUser> projectUsers = projectUserRoles.stream().map(projectUserRole -> {
                ProjectUser projectUser = new ProjectUser();
                projectUser.setProjectId(projectUserRole.getProjectId());
                projectUser.setUserId(projectUserRole.getUserId());
                projectUser.setStatus("1");
                return projectUser;
            }).collect(Collectors.toList());
            projectUserService.saveBatch(projectUsers);
            saveBatch(projectUserRoles);
            return OperateResult.success(ApplicationConstant.SAVE_MESSAGE);
        } catch (Exception e) {
            throw new RuntimeException("批量保存项目空间用户角色管理信息表失败，{}",  e);
        }
    }
}
