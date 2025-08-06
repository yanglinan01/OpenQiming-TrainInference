package com.ctdi.cnos.llm.metadata.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.CharSequenceUtil;
import com.ctdi.cnos.llm.base.constant.ApplicationConstant;
import com.ctdi.cnos.llm.base.object.Groups;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.beans.meta.model.ModelVO;
import com.ctdi.cnos.llm.beans.meta.model.UserModel;
import com.ctdi.cnos.llm.beans.meta.model.UserModelDTO;
import com.ctdi.cnos.llm.beans.meta.model.UserModelVO;
import com.ctdi.cnos.llm.metadata.service.ModelService;
import com.ctdi.cnos.llm.metadata.service.UserModelService;
import com.ctdi.cnos.llm.response.ErrorCodeEnum;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.system.user.entity.UserVO;
import com.ctdi.cnos.llm.system.user.service.UserService;
import com.ctdi.cnos.llm.util.ModelUtil;
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
 * 用户模型关系表 控制器类。
 *
 * @author wangyb
 * @since 2024/11/14
 */
@Api(tags = "用户模型关系表接口", value = "UserModelController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/userModel")
public class UserModelController {

    private final UserModelService service;

    private final ModelService modelService;

    private final UserService userService;

    /**
     * 分页查询符合条件的用户模型关系表列表。
     *
     * @param queryParam 查询对象。
     * @return 分页列表数据。
     */
    @ApiOperation(value = "分页查询符合条件的用户模型关系表数据", notes = "分页查询符合条件的用户模型关系表数据")
    @PostMapping(value = "/queryPage")
    public OperateResult<PageResult<UserModelVO>> queryPage(@Validated(Groups.PAGE.class) @RequestBody QueryParam queryParam) {
        return OperateResult.success(service.queryPage(queryParam));
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
        return OperateResult.success(service.queryList(queryParam));
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
        return OperateResult.success(service.queryById(id, true));
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
        ModelVO modelVO = modelService.queryById(userModelDTO.getModelId());
        UserVO userVO = userService.queryById(userModelDTO.getUserId(), false);
        Assert.isTrue(null != modelVO, "模型不存在！");
        Assert.isTrue(null != userVO, "用户不存在！");
        QueryParam queryParam = new QueryParam();
        queryParam.setFilterDto(userModelDTO);
        List<UserModelVO> vos = service.queryList(queryParam);
        if (CollUtil.isNotEmpty(vos)) {
            return OperateResult.successMessage(ApplicationConstant.SAVE_MESSAGE);
        }

        UserModel userModel = ModelUtil.copyTo(userModelDTO, UserModel.class);
        return service.save(userModel) ? OperateResult.successMessage(ApplicationConstant.SAVE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_SAVE_FAILED);
    }


    /**
     * 批量修改用户绑定的模型
     *
     * @param modelIds 模型ID集合
     * @param userId   用户ID
     * @param usage    模型权限用途
     * @return OperateResult<String>
     */
    @ApiOperation(value = "批量修改用户绑定的模型", notes = "批量修改用户绑定的模型")
    @PostMapping(value = "/updateUserModel/{userId}/{usage}")
    public OperateResult<String> updateUserModel(@RequestBody Set<Long> modelIds,
                                                 @PathVariable("userId") Long userId,
                                                 @PathVariable("usage") String usage) {
        return service.updateUserModel(userId, modelIds, usage) ? OperateResult.successMessage(ApplicationConstant.UPDATE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_SAVE_FAILED);
    }


    /**
     * 批量绑定模型-用户
     *
     * @param userModelDTO 绑定对象
     * @return OperateResult<String>
     */
    @ApiOperation(value = "批量绑定模型-用户", notes = "批量绑定模型-用户")
    @PostMapping(value = "/batchBinding")
    public OperateResult<String> batchBinding(@RequestBody UserModelDTO userModelDTO) {
        Assert.notNull(userModelDTO, "绑定对象不存在！");
        Assert.isTrue(CharSequenceUtil.isNotBlank(userModelDTO.getUsage()), "用途不能为空！");
        Assert.isTrue((userModelDTO.getUserId() != null && userModelDTO.getModelId() == null)
                || (userModelDTO.getUserId() == null && userModelDTO.getModelId() != null), "用户ID 与 模型ID 只能二选一！");

        if (userModelDTO.getUserId() != null) {
            //如果userId 不为空，那modelIdList 一定不能为空
            Assert.isTrue(CollUtil.isNotEmpty(userModelDTO.getModelIdList()), "用户ID不为空时，模型ID列表不能为空！");
            return service.batchBinding(userModelDTO.getUsage(), userModelDTO.getUserId(), userModelDTO.getModelIdList(), null, null, false) ? OperateResult.successMessage(ApplicationConstant.UPDATE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_SAVE_FAILED);
        } else if (userModelDTO.getModelId() != null) {
            //如果modelId 不为空，那userIdList 一定不能为空
            Assert.isTrue(CollUtil.isNotEmpty(userModelDTO.getUserIdList()), "模型ID不为空时，用户ID列表不能为空！");
            return service.batchBinding(userModelDTO.getUsage(), null, null, userModelDTO.getModelId(), userModelDTO.getUserIdList(), false) ? OperateResult.successMessage(ApplicationConstant.UPDATE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_SAVE_FAILED);
        }
        return OperateResult.error(ErrorCodeEnum.DATA_SAVE_FAILED);
    }

    /**
     * 批量绑定推理和训推模型-用户
     *
     * @param userModelDTOList 绑定对象
     * @return OperateResult<String>
     */
    @ApiOperation(value = "批量绑定推理和训推模型-用户", notes = "批量绑定推理和训推模型-用户")
    @PostMapping(value = "/batchBindingAll")
    public OperateResult<String> batchBindingAll(@RequestBody List<UserModelDTO> userModelDTOList) {
        userModelDTOList.forEach(userModelDTO->{
            Assert.notNull(userModelDTO, "绑定对象不存在！");
            Assert.isTrue(CharSequenceUtil.isNotBlank(userModelDTO.getUsage()), "用途不能为空！");
            Assert.isTrue((userModelDTO.getUserId() != null && userModelDTO.getModelId() == null)
                    || (userModelDTO.getUserId() == null && userModelDTO.getModelId() != null), "用户ID 与 模型ID 只能二选一！");
        });
        OperateResult<String> result=service.batchBindingAll(userModelDTOList);
        return result;
    }


    /**
     * 一键绑定模型-用户
     *
     * @param userModelDTO 绑定对象
     * @return OperateResult<String>
     */
    @ApiOperation(value = "一键绑定模型-用户", notes = "一键绑定模型-用户")
    @PostMapping(value = "/oneClickBinding")
    public OperateResult<String> oneClickBinding(@RequestBody UserModelDTO userModelDTO) {
        Assert.notNull(userModelDTO, "绑定对象不存在！");
        Assert.isTrue((userModelDTO.getUserId() != null && userModelDTO.getModelId() == null)
                || (userModelDTO.getUserId() == null && userModelDTO.getModelId() != null), "用户ID 与 模型ID 只能二选一！");
        if (userModelDTO.getUserId() != null) {
            return service.oneClickBinding(userModelDTO.getUserId(), null) ? OperateResult.successMessage("一键用户绑定模型成功") : OperateResult.error("一键用户绑定模型失败");
        } else if (userModelDTO.getModelId() != null) {
            return service.oneClickBinding(null, userModelDTO.getModelId()) ? OperateResult.successMessage("一键模型绑定用户成功") : OperateResult.error("一键模型绑定用户失败");
        }
        return OperateResult.error(ErrorCodeEnum.DATA_SAVE_FAILED);
    }


    /**
     * 一键取消绑定模型-用户
     *
     * @param userModelDTO 绑定对象
     * @return OperateResult<String>
     */
    @ApiOperation(value = "一键取消绑定模型-用户", notes = "一键取消绑定模型-用户")
    @PostMapping(value = "/oneClickCancelBinding")
    public OperateResult<String> oneClickCancelBinding(@RequestBody UserModelDTO userModelDTO) {
        Assert.notNull(userModelDTO, "绑定对象不存在！");
        Assert.isTrue((userModelDTO.getUserId() != null && userModelDTO.getModelId() == null)
                || (userModelDTO.getUserId() == null && userModelDTO.getModelId() != null), "用户ID 与 模型ID 只能二选一！");
        if (userModelDTO.getUserId() != null) {
            return service.oneClickCancelBinding(userModelDTO.getUserId(), null) ? OperateResult.successMessage("一键用户取消绑定模型成功") : OperateResult.error("一键用户取消绑定模型失败");
        } else if (userModelDTO.getModelId() != null) {
            return service.oneClickCancelBinding(null, userModelDTO.getModelId()) ? OperateResult.successMessage("一键模型取消绑定用户成功") : OperateResult.error("一键模型取消绑定用户失败");
        }
        return OperateResult.error(ErrorCodeEnum.DATA_SAVE_FAILED);
    }


    /**
     * 删除用户模型关系表
     *
     * @param userId 用户ID
     * @param modeId 模型ID
     * @param usage  模型权限用途
     * @return OperateResult<String>
     */
    @ApiOperation(value = "删除用户模型关系表", notes = "删除用户模型关系表")
    @GetMapping(value = "/delete/{userId}/{modeId}/{usage}")
    public OperateResult<String> delete(@NotNull(message = "用户ID不能为空") @PathVariable("userId") Long userId,
                                        @NotNull(message = "模型ID不能为空") @PathVariable("modeId") Long modeId,
                                        @NotNull(message = "模型权限用途不能为空") @PathVariable("usage") String usage) {
        return service.delete(userId, modeId, usage) ? OperateResult.successMessage(ApplicationConstant.DELETE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
    }


}
