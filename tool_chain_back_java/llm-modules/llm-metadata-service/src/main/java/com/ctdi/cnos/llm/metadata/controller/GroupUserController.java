package com.ctdi.cnos.llm.metadata.controller;

import com.ctdi.cnos.llm.base.constant.ApplicationConstant;
import com.ctdi.cnos.llm.base.object.Groups;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.beans.meta.group.GroupUser;
import com.ctdi.cnos.llm.beans.meta.group.GroupUserDTO;
import com.ctdi.cnos.llm.beans.meta.group.GroupUserVO;
import com.ctdi.cnos.llm.metadata.service.GroupUserService;
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
 * 用户组_用户关系 控制器类。
 *
 * @author wangyb
 * @since 2024/09/23
 */
@Api(tags = "用户组_用户关系接口", value = "GroupUserController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/groupUser")
public class GroupUserController {
	
    private final GroupUserService service;
	
    /**
     * 分页查询符合条件的用户组_用户关系列表。
     *
     * @param queryParam 查询对象。
     * @return 分页列表数据。
     */
	@ApiOperation(value = "分页查询符合条件的用户组_用户关系数据", notes = "分页查询符合条件的用户组_用户关系数据")
    @PostMapping(value = "/queryPage")
    public OperateResult<PageResult<GroupUserVO>> queryPage(@Validated(Groups.PAGE.class) @RequestBody QueryParam queryParam) {
        return OperateResult.success(service.queryPage(queryParam));
    }
	
    /**
     * 查询符合条件的用户组_用户关系列表。
     *
     * @param queryParam 查询对象。
     * @return 列表数据。
     */
	@ApiOperation(value = "列表查询符合条件的用户组_用户关系数据", notes = "列表查询符合条件的用户组_用户关系数据")
    @PostMapping(value = "/queryList")
    public OperateResult<List<GroupUserVO>> queryList(@RequestBody QueryParam queryParam) {
        return OperateResult.success(service.queryList(queryParam));
    }

    /**
     * 查询指定用户组_用户关系数据。
     *
     * @param id 指定用户组_用户关系主键Id。
     * @return 单条数据。
     */
	@ApiOperation(value = "查询指定ID的用户组_用户关系数据", notes = "通过用户组_用户关系ID获取具体的用户组_用户关系数据")
	@GetMapping(value = "/queryById")
    public OperateResult<GroupUserVO> queryById(@ApiParam(value = "用户组_用户关系ID", required = true, example = "1")
                          @NotNull(message = "用户组_用户关系ID不能为空") @RequestParam("id") Long id) {
        return OperateResult.success(service.queryById(id, true));
    }

    /**
     * 添加用户组_用户关系操作。
     *
     * @param groupUserDTO 添加用户组_用户关系对象。
     * @return 应答结果对象。
     */
	@ApiOperation(value = "创建用户组_用户关系", notes = "根据请求体中的用户组_用户关系信息创建")
	@ApiOperationSupport(ignoreParameters = {"id"})
    @PostMapping(value = "/add")
    public OperateResult<String> add(@Validated(Groups.ADD.class) @RequestBody GroupUserDTO groupUserDTO) {
		GroupUser groupUser = ModelUtil.copyTo(groupUserDTO, GroupUser.class);
        return service.save(groupUser) ? OperateResult.successMessage(ApplicationConstant.SAVE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_SAVE_FAILED);
    }
	
    /**
     * 更新用户组_用户关系操作。
     *
     * @param groupUserDTO 更新用户组_用户关系对象。
     * @return 应答结果对象。
     */
	@ApiOperation(value = "更新用户组_用户关系", notes = "根据ID更新指定的用户组_用户关系信息")
    @PostMapping(value = "/updateById")
    public OperateResult<String> updateById(@Validated(Groups.UPDATE.class) @RequestBody GroupUserDTO groupUserDTO) {
		GroupUser groupUser = ModelUtil.copyTo(groupUserDTO, GroupUser.class);
		return service.updateById(groupUser) ? OperateResult.successMessage(ApplicationConstant.UPDATE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_SAVE_FAILED);
    }


    /**
     * 查询用户组中的数据列表
     *
     * @return 用户组中的数据列表
     */
    @ApiOperation(value = "查询用户组中的用户列表", notes = "查询用户组中的用户列表")
    @GetMapping(value = "/queryGroupUserIds")
    public List<GroupUserVO> queryGroupUserIds() {
        return service.queryGroupUserIds();
    }


    /**
     * 根据用户组ID查找用户列表
     *
     * @param groupId 户组id
     * @return 用户列表
     */
    @ApiOperation(value = "根据用户组ID查找用户列表", notes = "根据用户组ID查找用户列表")
    @GetMapping(value = "/queryUserIdsByGroupId")
    public List<GroupUser> queryUserIdsByGroupId(@ApiParam(value = "用户组ID", required = true, example = "1")
                          @NotNull(message = "用户组ID不能为空") @RequestParam("groupId") Long groupId) {
        return service.queryUserIdsByGroupId(groupId);
    }



}
