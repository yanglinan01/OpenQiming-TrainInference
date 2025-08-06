package com.ctdi.cnos.llm.feign.metadata;

import com.ctdi.cnos.llm.RemoteConstont;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.beans.meta.projectSpace.ProjectRoleUsersDTO;
import com.ctdi.cnos.llm.beans.meta.projectSpace.ProjectUserRoleDTO;
import com.ctdi.cnos.llm.beans.meta.projectSpace.ProjectUserRoleVO;
import com.ctdi.cnos.llm.response.OperateResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 项目空间用户角色关联信息表服务远程数据操作访问接口。
 *
 * @author 
 * @since 2025/06/05
 */
@Component
@FeignClient(value = RemoteConstont.METADATA_SERVICE_NAME, path = "${cnos.server.llm-metadata-service.contextPath}")
public interface ProjectUserRoleServiceClientFeign {

    /**
     * 分页查询符合条件的项目空间用户角色关联信息表列表。
     *
     * @param pageSize 页码
     * @param currentPage 当前页
     * @param projectId 项目空间id
     * @param roleId 角色id
     * @param userName 用户名。
     * @return 分页列表数据。
     */
    @ApiOperation(value = "分页查询符合条件的项目空间用户角色关联信息表数据", notes = "分页查询符合条件的项目空间用户角色关联信息表数据")
    @GetMapping(value = "/projectUserRole/queryPage")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "页大小，默认为20", paramType = "param"),
            @ApiImplicitParam(name = "currentPage", value = "当前页，默认为1", paramType = "param"),
            @ApiImplicitParam(name = "projectId", value = "项目空间ID", required = true, paramType = "param"),
            @ApiImplicitParam(name = "roleId", value = "角色ID",  paramType = "param"),
            @ApiImplicitParam(name = "userName", value = "真实姓名", paramType = "param")
    })
    OperateResult<PageResult<ProjectUserRoleVO>> queryPage(
            @RequestParam(name = "pageSize", required = false, defaultValue = "20") long pageSize,
            @RequestParam(name = "currentPage", required = false, defaultValue = "1") long currentPage,
            @RequestParam(name = "projectId") Long projectId,
            @RequestParam(name = "roleId", required = false) Long roleId,
            @RequestParam(name = "name", required = false) String name
    );

    /**
     * 查询符合条件的项目空间用户角色关联信息表列表。
     *
     * @param queryParam 查询对象。
     * @return 列表数据。
     */
    @PostMapping(value = "/projectUserRole/queryList")
    OperateResult<List<ProjectUserRoleVO>> queryList(@RequestBody QueryParam queryParam);

    /**
     * 查询指定项目空间用户角色关联信息表数据。
     *
     * @param id 指定项目空间用户角色关联信息表主键Id。
     * @return 单条数据。
     */
	@GetMapping(value = "/projectUserRole/queryById")
    OperateResult<ProjectUserRoleVO> queryById(@RequestParam("id") Long id);

    /**
     * 添加项目空间用户角色关联信息表操作。
     *
     * @param projectUserRoleDTO 添加项目空间用户角色关联信息表对象。
     * @return 应答结果对象。
     */
    @PostMapping(value = "/projectUserRole/add")
    OperateResult<String> add(@RequestBody ProjectUserRoleDTO projectUserRoleDTO);
	
    /**
     * 更新项目空间用户角色关联信息表操作。
     *
     * @param projectUserRoleDTO 更新项目空间用户角色关联信息表对象。
     * @return 应答结果对象。
     */
    @PostMapping(value = "/projectUserRole/updateById")
    OperateResult<String> updateById(@RequestBody ProjectUserRoleDTO projectUserRoleDTO);

    /**
     * 删除指定的项目空间用户角色关联信息表。
     *
     * @param projectUserRoleDTO 指定项目空间用户角色关联信息表主键Id。
     * @return 应答结果对象。
     */
    @DeleteMapping(value = "/projectUserRole/deleteById")
    OperateResult<String> deleteById(@RequestBody ProjectUserRoleDTO projectUserRoleDTO);

    /**
     * 判断当前用户是否是项目空间管理员
     * @param projectId 项目空间id
     * @param userId 用户id
     * @return 是否
     */
    @GetMapping(value = "/projectUserRole/judgeManager")
    OperateResult<Boolean> judgeManager(@RequestParam(value = "projectId") Long projectId, @RequestParam(value = "userId") Long userId);

    @PostMapping(value = "/projectUserRole/addBatch")
    OperateResult<String> addBatch(@RequestBody ProjectRoleUsersDTO projectRoleUsersDTO);
}