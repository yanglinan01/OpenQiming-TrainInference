package com.ctdi.cnos.llm.controller.metadata;

import com.ctdi.cnos.llm.base.object.Groups;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.beans.meta.model.UserModelDTO;
import com.ctdi.cnos.llm.beans.meta.model.UserModelVO;
import com.ctdi.cnos.llm.feign.metadata.UserModelServiceClientFeign;
import com.ctdi.cnos.llm.response.OperateResult;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;


/**
 * 用户模型关系表服务远程数据操作访问接口。
 *
 * @author wangyb
 * @since 2024/11/14
 */
@Api(tags = "用户模型关系表接口", value = "UserModelController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/userModel")
public class UserModelController {

    private final UserModelServiceClientFeign serviceClient;

    /**
     * 分页查询符合条件的用户模型关系表列表。
     *
     * @param queryParam 查询对象。
     * @return 分页列表数据。
     */
    @ApiOperation(value = "分页查询符合条件的用户模型关系表数据", notes = "分页查询符合条件的用户模型关系表数据")
    @PostMapping(value = "/queryPage")
    public OperateResult<PageResult<UserModelVO>> queryPage(@Validated(Groups.PAGE.class) @RequestBody QueryParam queryParam) {
        return serviceClient.queryPage(queryParam);
    }


    /**
     * 查询符合条件的用户模型关系表列表。
     *
     * @param queryParam 查询对象。
     * @return 列表数据。
     */
    @ApiOperation(value = "列表查询符合条件的用户模型关系表数据", notes = "列表查询符合条件的用户模型关系表数据")
    @PostMapping(value = "/queryList")
    public OperateResult<List<UserModelVO>> queryList(@RequestBody QueryParam queryParam) {
        return serviceClient.queryList(queryParam);
    }


    /**
     * 查询指定用户模型关系表数据。
     *
     * @param id 指定用户模型关系表主键Id。
     * @return 单条数据。
     */
    @ApiOperation(value = "查询指定ID的用户模型关系表数据", notes = "通过用户模型关系表ID获取具体的用户模型关系表数据")
    @GetMapping(value = "/queryById")
    public OperateResult<UserModelVO> queryById(@ApiParam(value = "用户模型关系表ID", required = true, example = "1")
                                                @NotNull(message = "用户模型关系表ID不能为空") @RequestParam("id") Long id) {
        return serviceClient.queryById(id);
    }


    /**
     * 添加用户模型关系表操作。
     *
     * @param userModelDTO 添加用户模型关系表对象。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "添加用户模型关系表对象", notes = "添加用户模型关系表对象")
    @ApiOperationSupport(ignoreParameters = {"id"})
    @PostMapping(value = "/add")
    public OperateResult<String> add(@Validated(Groups.ADD.class) @RequestBody UserModelDTO userModelDTO) {
        return serviceClient.add(userModelDTO);
    }


    /**
     * 批量修改用户绑定的模型
     *
     * @param modelIds 模型ID集合
     * @param userId   用户ID
     * @param usage    用户模型权限
     * @return OperateResult<String>
     */
    @ApiOperation(value = "批量修改用户绑定的模型", notes = "批量修改用户绑定的模型")
    @PostMapping(value = "/updateUserModel/{userId}/{usage}")
    public OperateResult<String> updateUserModel(@RequestBody Set<Long> modelIds,
                                                 @PathVariable("userId") Long userId,
                                                 @PathVariable("usage") String usage) {
        return serviceClient.updateUserModel(modelIds, userId, usage);
    }

    /**
     * 批量绑定模型-用户
     *
     * @param userModelDTO 绑定关系
     * @return OperateResult<String>
     */
    @ApiOperation(value = "批量绑定模型-用户", notes = "批量绑定模型-用户")
    @PostMapping(value = "/batchBinding")
    OperateResult<String> batchBinding(@RequestBody UserModelDTO userModelDTO) {
        return serviceClient.batchBinding(userModelDTO);
    }

    /**
     * 批量绑定模型-用户
     *
     * @param userModelDTOList 批量绑定推理和训推模型
     * @return OperateResult<String>
     */
    @ApiOperation(value = "批量绑定推理和训推模型-用户", notes = "批量绑定推理和训推模型-用户")
    @PostMapping(value = "/batchBindingAll")
    OperateResult<String> batchBindingAll(@RequestBody List<UserModelDTO> userModelDTOList) {
        return serviceClient.batchBindingAll(userModelDTOList);
    }

    /**
     * 一键绑定模型-用户
     *
     * @param userModelDTO 绑定关系
     * @return OperateResult<String>
     */
    @ApiOperation(value = "一键绑定模型-用户", notes = "一键绑定模型-用户")
    @PostMapping(value = "/oneClickBinding")
    OperateResult<String> oneClickBinding(@RequestBody UserModelDTO userModelDTO) {
        return serviceClient.oneClickBinding(userModelDTO);
    }

    /**
     * 一键取消绑定模型-用户
     *
     * @param userModelDTO 绑定对象
     * @return OperateResult<String>
     */
    @ApiOperation(value = "一键取消绑定模型-用户", notes = "一键取消绑定模型-用户")
    @PostMapping(value = "/oneClickCancelBinding")
    OperateResult<String> oneClickCancelBinding(@RequestBody UserModelDTO userModelDTO) {
        return serviceClient.oneClickCancelBinding(userModelDTO);
    }

    /**
     * 删除用户模型关系表
     *
     * @param userId 用户ID
     * @param modeId 模型ID
     * @param usage  用户模型权限
     * @return OperateResult<String>
     */
    @ApiOperation(value = "删除用户模型关系表", notes = "删除用户模型关系表")
    @GetMapping(value = "/delete/{userId}/{modeId}/{usage}")
    public OperateResult<String> delete(@NotNull(message = "用户ID不能为空") @PathVariable("userId") Long userId,
                                        @NotNull(message = "模型ID不能为空") @PathVariable("modeId") Long modeId,
                                        @NotNull(message = "用户模型权限") @PathVariable("usage") String usage) {
        return serviceClient.delete(userId, modeId, usage);
    }

}