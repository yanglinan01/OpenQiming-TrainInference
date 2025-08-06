package com.ctdi.cnos.llm.metadata.controller;

import com.ctdi.cnos.llm.base.constant.ApplicationConstant;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.base.object.Groups;
import com.ctdi.cnos.llm.beans.meta.projectSpace.ProjectUser;
import com.ctdi.cnos.llm.beans.meta.projectSpace.ProjectUserDTO;
import com.ctdi.cnos.llm.beans.meta.projectSpace.ProjectUserVO;
import com.ctdi.cnos.llm.metadata.service.ProjectUserService;
import com.ctdi.cnos.llm.response.ErrorCodeEnum;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.util.ModelUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 项目空间用户关联信息表 控制器类。
 *
 * @author 
 * @since 2025/06/05
 */
@Deprecated
@Api(tags = "项目空间用户关联信息表接口", value = "ProjectUserController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/projectUser")
public class ProjectUserController {
	
    private final ProjectUserService service;
	

    @ApiOperation(value = "分页查询符合条件的项目空间用户关联信息表数据", notes = "分页查询符合条件的项目空间用户关联信息表数据")
    @PostMapping(value = "/queryPage")
    public OperateResult<PageResult<ProjectUserVO>> queryPage(@Validated(Groups.PAGE.class) @RequestBody QueryParam queryParam) {
        return OperateResult.success(service.queryPage(queryParam));
    }
	
    /**
     * 查询符合条件的项目空间用户关联信息表列表。
     *
     * @param queryParam 查询对象。
     * @return 列表数据。
     */
    @ApiOperation(value = "列表查询符合条件的项目空间用户关联信息表数据", notes = "列表查询符合条件的项目空间用户关联信息表数据")
    @PostMapping(value = "/queryList")
    public OperateResult<List<ProjectUserVO>> queryList(@RequestBody QueryParam queryParam) {
        return OperateResult.success(service.queryList(queryParam));
    }

    /**
     * 查询指定项目空间用户关联信息表数据。
     *
     * @param id 指定项目空间用户关联信息表主键Id。
     * @return 单条数据。
     */
	@ApiOperation(value = "查询指定ID的项目空间用户关联信息表数据", notes = "通过项目空间用户关联信息表ID获取具体的项目空间用户关联信息表数据")
	@GetMapping(value = "/queryById")
    public OperateResult<ProjectUserVO> queryById(@ApiParam(value = "项目空间用户关联信息表ID", required = true, example = "1")
                          @NotNull(message = "项目空间用户关联信息表ID不能为空") @RequestParam("id") Long id) {
        return OperateResult.success(service.queryById(id, true));
    }

    /**
     * 添加项目空间用户关联信息表操作。
     *
     * @param projectUserDTO 添加项目空间用户关联信息表对象。
     * @return 应答结果对象。
     */
	@ApiOperation(value = "创建项目空间用户关联信息表", notes = "根据请求体中的项目空间用户关联信息表信息创建")
	@ApiOperationSupport(ignoreParameters = {"id"})
    @PostMapping(value = "/add")
    public OperateResult<String> add(@Validated(Groups.ADD.class) @RequestBody ProjectUserDTO projectUserDTO) {
		ProjectUser projectUser = ModelUtil.copyTo(projectUserDTO, ProjectUser.class);
        return service.save(projectUser) ? OperateResult.successMessage(ApplicationConstant.SAVE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_SAVE_FAILED);
    }
	
    /**
     * 更新项目空间用户关联信息表操作。
     *
     * @param projectUserDTO 更新项目空间用户关联信息表对象。
     * @return 应答结果对象。
     */
	@ApiOperation(value = "更新项目空间用户关联信息表", notes = "根据ID更新指定的项目空间用户关联信息表信息")
    @PostMapping(value = "/updateById")
    public OperateResult<String> updateById(@Validated(Groups.UPDATE.class) @RequestBody ProjectUserDTO projectUserDTO) {
		ProjectUser projectUser = ModelUtil.copyTo(projectUserDTO, ProjectUser.class);
		return service.updateById(projectUser) ? OperateResult.successMessage(ApplicationConstant.UPDATE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_SAVE_FAILED);
    }

    /**
     * 删除指定的项目空间用户关联信息表。
     *
     * @param id 指定项目空间用户关联信息表主键Id。
     * @return 应答结果对象。
     */
	@ApiOperation(value = "删除项目空间用户关联信息表", notes = "根据ID删除指定的项目空间用户关联信息表")
    @GetMapping(value = "/deleteById")
    public OperateResult<String> deleteById(@ApiParam(value = "项目空间用户关联信息表ID", required = true, example = "1")
                          @NotNull(message = "项目空间用户关联信息表ID不能为空") @RequestParam("id") Long id) {
		return service.deleteById(id) ? OperateResult.successMessage(ApplicationConstant.DELETE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
    }

}
