package com.ctdi.cnos.llm.metadata.controller;

import com.ctdi.cnos.llm.base.constant.ApplicationConstant;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.base.object.Groups;
import com.ctdi.cnos.llm.beans.meta.projectSpace.ProjectRoleUsersDTO;
import com.ctdi.cnos.llm.beans.meta.projectSpace.ProjectUserRole;
import com.ctdi.cnos.llm.beans.meta.projectSpace.ProjectUserRoleDTO;
import com.ctdi.cnos.llm.beans.meta.projectSpace.ProjectUserRoleVO;
import com.ctdi.cnos.llm.metadata.service.ProjectUserRoleService;
import com.ctdi.cnos.llm.response.ErrorCodeEnum;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.util.ModelUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 项目空间用户角色关联信息表 控制器类。
 *
 * @author 
 * @since 2025/06/05
 */
@Api(tags = "项目空间用户角色关联信息表接口", value = "ProjectUserRoleController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/projectUserRole")
public class ProjectUserRoleController {
	
    private final ProjectUserRoleService service;

    /**
     * 分页查询符合条件的项目空间用户角色关联信息表列表。
     * @param pageSize 页码
     * @param currentPage 当前页
     * @param projectId 项目空间id
     * @param roleId 角色id
     * @param userName 用户名
     * @return 分页结果
     */
	@ApiOperation(value = "分页查询符合条件的项目空间用户角色关联信息表数据", notes = "分页查询符合条件的项目空间用户角色关联信息表数据")
    @GetMapping(value = "/queryPage")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "页大小，默认为20", paramType = "param"),
            @ApiImplicitParam(name = "currentPage", value = "当前页，默认为1", paramType = "param"),
            @ApiImplicitParam(name = "projectId", value = "项目空间ID", required = true, paramType = "param"),
            @ApiImplicitParam(name = "roleId", value = "角色ID",  paramType = "param"),
            @ApiImplicitParam(name = "name", value = "真实姓名", paramType = "param")
    })
    public OperateResult<PageResult<ProjectUserRoleVO>> queryPage(
            @RequestParam(name = "pageSize", required = false, defaultValue = "20") long pageSize,
            @RequestParam(name = "currentPage", required = false, defaultValue = "1") long currentPage,
            @RequestParam(name = "projectId") Long projectId,
            @RequestParam(name = "roleId", required = false) Long roleId,
            @RequestParam(name = "name",required = false) String name
    ) {
        return OperateResult.success(service.queryPagePage(pageSize, currentPage,projectId, roleId, name));
    }
	
    /**
     * 查询符合条件的项目空间用户角色关联信息表列表。
     *
     * @param queryParam 查询对象。
     * @return 列表数据。
     */
	@Deprecated
    @ApiOperation(value = "列表查询符合条件的项目空间用户角色关联信息表数据", notes = "列表查询符合条件的项目空间用户角色关联信息表数据")
    @PostMapping(value = "/queryList")
    public OperateResult<List<ProjectUserRoleVO>> queryList(@RequestBody QueryParam queryParam) {
        return OperateResult.success(service.queryList(queryParam));
    }

    /**
     * 查询指定项目空间用户角色关联信息表数据。
     *
     * @param id 指定项目空间用户角色关联信息表主键Id。
     * @return 单条数据。
     */
    @ApiOperation(value = "查询指定ID的项目空间用户角色关联信息表数据", notes = "通过项目空间用户角色关联信息表ID获取具体的项目空间用户角色关联信息表数据")
	@GetMapping(value = "/queryById")
    public OperateResult<ProjectUserRoleVO> queryById(@ApiParam(value = "项目空间用户角色关联信息表ID", required = true, example = "1")
                          @NotNull(message = "项目空间用户角色关联信息表ID不能为空") @RequestParam("id") Long id) {
        return OperateResult.success(service.queryById(id, true));
    }

    /**
     * 添加项目空间用户角色关联信息表操作。
     *
     * @param projectUserRoleDTO 添加项目空间用户角色关联信息表对象。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "创建项目空间用户角色关联信息表", notes = "根据请求体中的项目空间用户角色关联信息表信息创建")
	@ApiOperationSupport(ignoreParameters = {"id"})
    @PostMapping(value = "/add")
    public OperateResult<String> add(@Validated(Groups.ADD.class) @RequestBody ProjectUserRoleDTO projectUserRoleDTO) {
		ProjectUserRole projectUserRole = ModelUtil.copyTo(projectUserRoleDTO, ProjectUserRole.class);
        return service.saveProjectUserRole(projectUserRole);
    }

    /**
     * 批量添加项目空间用户角色关联信息表操作。
     *
     * @param projectRoleUsersDTO 添加项目空间用户角色关联信息表对象。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "批量添加项目空间用户角色关联信息表", notes = "根据请求体中的项目空间用户角色关联信息表信息创建")
    @ApiOperationSupport(ignoreParameters = {"id"})
    @PostMapping(value = "/addBatch")
    public OperateResult<String> addBatch(@RequestBody ProjectRoleUsersDTO projectRoleUsersDTO) {
        return service.addBatch(projectRoleUsersDTO);
    }
	
    /**
     * 更新项目空间用户角色关联信息表操作。
     *
     * @param projectUserRoleDTO 更新项目空间用户角色关联信息表对象。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "更新项目空间用户角色关联信息表", notes = "根据ID更新指定的项目空间用户角色关联信息表信息")
    @PostMapping(value = "/updateById")
    public OperateResult<String> updateById(@Validated(Groups.UPDATE.class) @RequestBody ProjectUserRoleDTO projectUserRoleDTO) {
		ProjectUserRole projectUserRole = ModelUtil.copyTo(projectUserRoleDTO, ProjectUserRole.class);
		return service.updateByProjectUserRole(projectUserRole);
    }

    /**
     * 删除指定的项目空间用户角色关联信息表。
     *
     * @param projectUserRoleDTO 指定项目空间用户角色关联信息DTO。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "删除项目空间用户角色关联信息表", notes = "根据ID删除指定的项目空间用户角色关联信息表")
    @DeleteMapping(value = "/deleteById")
    public OperateResult<String> deleteById(@RequestBody ProjectUserRoleDTO projectUserRoleDTO) {
        ProjectUserRole projectUserRole = ModelUtil.copyTo(projectUserRoleDTO, ProjectUserRole.class);
        return service.deleteByEntity(projectUserRole) ? OperateResult.successMessage(ApplicationConstant.DELETE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
    }

    @GetMapping(value = "/judgeManager")
    public OperateResult<Boolean> judgeManager(@RequestParam(name = "projectId") Long projectId, @RequestParam(name = "userId") Long userId) {
        return OperateResult.success(service.judgeManager(projectId, userId));
    }

}
