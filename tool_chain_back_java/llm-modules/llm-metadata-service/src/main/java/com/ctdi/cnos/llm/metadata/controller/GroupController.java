package com.ctdi.cnos.llm.metadata.controller;

import com.ctdi.cnos.llm.base.constant.ApplicationConstant;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.base.object.Groups;
import com.ctdi.cnos.llm.beans.meta.group.Group;
import com.ctdi.cnos.llm.beans.meta.group.GroupDTO;
import com.ctdi.cnos.llm.beans.meta.group.GroupVO;
import com.ctdi.cnos.llm.metadata.service.GroupService;
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
 * 用户组 控制器类。
 *
 * @author wangyb
 * @since 2024/09/23
 */
@Api(tags = "用户组接口", value = "GroupController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/group")
public class GroupController {
	
    private final GroupService service;
	
    /**
     * 分页查询符合条件的用户组列表。
     *
     * @param queryParam 查询对象。
     * @return 分页列表数据。
     */
	@ApiOperation(value = "分页查询符合条件的用户组数据", notes = "分页查询符合条件的用户组数据")
    @PostMapping(value = "/queryPage")
    public OperateResult<PageResult<GroupVO>> queryPage(@Validated(Groups.PAGE.class) @RequestBody QueryParam queryParam) {
        List<GroupVO> list =  service.queryPage(queryParam).getRows();
        return OperateResult.success(service.queryPage(queryParam));
    }
	
    /**
     * 查询符合条件的用户组列表。
     *
     * @param queryParam 查询对象。
     * @return 列表数据。
     */
	@ApiOperation(value = "列表查询符合条件的用户组数据", notes = "列表查询符合条件的用户组数据")
    @PostMapping(value = "/queryList")
    public OperateResult<List<GroupVO>> queryList(@RequestBody QueryParam queryParam) {
        List<GroupVO> list =  service.queryList(queryParam);
        return OperateResult.success(service.queryList(queryParam));
    }

    /**
     * 查询指定用户组数据。
     *
     * @param id 指定用户组主键Id。
     * @return 单条数据。
     */
	@ApiOperation(value = "查询指定ID的用户组数据", notes = "通过用户组ID获取具体的用户组数据")
	@GetMapping(value = "/queryById")
    public OperateResult<GroupVO> queryById(@ApiParam(value = "用户组ID", required = true, example = "1")
                          @NotNull(message = "用户组ID不能为空") @RequestParam("id") Long id) {
        return OperateResult.success(service.queryById(id, true));
    }

    /**
     * 添加用户组操作。
     *
     * @param groupDTO 添加用户组对象。
     * @return 应答结果对象。
     */
	@ApiOperation(value = "创建用户组", notes = "根据请求体中的用户组信息创建")
	@ApiOperationSupport(ignoreParameters = {"id"})
    @PostMapping(value = "/add")
    public OperateResult<String> add(@Validated(Groups.ADD.class) @RequestBody GroupDTO groupDTO) {
		Group group = ModelUtil.copyTo(groupDTO, Group.class);
        return service.save(group) ? OperateResult.successMessage(ApplicationConstant.SAVE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_SAVE_FAILED);
    }
	
    /**
     * 更新用户组操作。
     *
     * @param groupDTO 更新用户组对象。
     * @return 应答结果对象。
     */
	@ApiOperation(value = "更新用户组", notes = "根据ID更新指定的用户组信息")
    @PostMapping(value = "/updateById")
    public OperateResult<String> updateById(@Validated(Groups.UPDATE.class) @RequestBody GroupDTO groupDTO) {
		Group group = ModelUtil.copyTo(groupDTO, Group.class);
		return service.updateById(group) ? OperateResult.successMessage(ApplicationConstant.UPDATE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_SAVE_FAILED);
    }


}
