package com.ctdi.cnos.llm.metadata.controller;

import com.ctdi.cnos.llm.base.constant.ActionType;
import com.ctdi.cnos.llm.base.constant.ApplicationConstant;
import com.ctdi.cnos.llm.base.object.Groups;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.beans.meta.evaluation.PrTestSetEvaluationDetail;
import com.ctdi.cnos.llm.beans.meta.evaluation.PrTestSetEvaluationDetailDTO;
import com.ctdi.cnos.llm.beans.meta.evaluation.PrTestSetEvaluationDetailVO;
import com.ctdi.cnos.llm.metadata.service.PrTestSetEvaluationDetailService;
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
 * 问答对测试数据集评估详情 操作控制器类。
 *
 * @author laiqi
 * @since 2024/09/03
 */
@Api(tags = "问答对测试数据集评估详情接口", value = "PrTestSetEvaluationDetailController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/prTestSetEvaluationDetail")
public class PrTestSetEvaluationDetailController {

    private final PrTestSetEvaluationDetailService service;

    /**
     * 分页列出符合过滤条件的问答对测试数据集评估详情列表。
     *
     * @param queryParam 查询对象。
     * @return 应答结果对象，包含查询结果集。
     */
    @ApiOperation(value = "获取分页查询的问答对测试数据集评估详情列表信息", notes = "获取分页查询的问答对测试数据集评估详情信息")
    @PostMapping(value = "/queryPage")
    public PageResult<PrTestSetEvaluationDetailVO> queryPage(@Validated(Groups.PAGE.class) @RequestBody QueryParam queryParam) {
        PrTestSetEvaluationDetailDTO dto = queryParam.getFilterDto(PrTestSetEvaluationDetailDTO.class);
        ModelUtil.validateModelAndThrow(dto, Groups.PAGE.class);
        return service.queryPage(queryParam);
    }

    /**
     * 列出符合过滤条件的问答对测试数据集评估详情列表。
     *
     * @param queryParam 查询对象。
     * @return 应答结果对象，包含查询结果集。
     */
    @ApiOperation(value = "获取查询的问答对测试数据集评估详情列表信息", notes = "获取查询的问答对测试数据集评估详情信息")
    @PostMapping(value = "/queryList")
    public List<PrTestSetEvaluationDetailVO> queryList(@Validated(Groups.QUERY.class) @RequestBody QueryParam queryParam) {
        PrTestSetEvaluationDetailDTO dto = queryParam.getFilterDto(PrTestSetEvaluationDetailDTO.class);
        ModelUtil.validateModelAndThrow(dto, Groups.QUERY.class);
        return service.queryList(queryParam);
    }

    /**
     * 问答对测试数据集评估反馈是否完成
     *
     * @param testSetEvaluationId 测试数据集评估ID
     * @return
     */
    @ApiOperation(value = "问答对测试数据集评估反馈是否完成", notes = "根据测试数据集评估ID判断问答对测试数据集评估反馈是否完成")
    @GetMapping(value = "/isComplete")
    public OperateResult<Boolean> isComplete(@ApiParam(value = "测试数据集评估ID", required = true, example = "1")
                                             @Validated @NotNull(message = "测试数据集评估ID不能为空") @RequestParam("testSetEvaluationId") Long testSetEvaluationId) {
        Boolean complete = service.isComplete(testSetEvaluationId);
        return OperateResult.success(complete, complete ? "系统检测到已完成人工评估" : "系统检测到还未完成人工评估");
    }

    /**
     * 查看问答对测试数据集评估详情对象详情。
     *
     * @param id 指定问答对测试数据集评估详情主键Id。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "根据ID获取问答对测试数据集评估详情详情", notes = "通过问答对测试数据集评估详情ID获取具体的问答对测试数据集评估详情信息")
    @GetMapping(value = "/queryById")
    public PrTestSetEvaluationDetailVO queryById(@ApiParam(value = "问答对测试数据集评估详情ID", required = true, example = "1")
                                                 @NotNull(message = "问答对测试数据集评估详情ID不能为空") @RequestParam("id") Long id) {
        return service.queryById(id, true);
    }

    /**
     * 添加问答对测试数据集评估详情操作。
     *
     * @param prTestSetEvaluationDetailDto 添加问答对测试数据集评估详情对象。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "创建问答对测试数据集评估详情", notes = "根据请求体中的问答对测试数据集评估详情信息创建")
    @ApiOperationSupport(ignoreParameters = {"id"})
    @PostMapping(value = "/add")
    public OperateResult<String> add(@Validated(Groups.ADD.class) @RequestBody PrTestSetEvaluationDetailDTO prTestSetEvaluationDetailDto) {
        PrTestSetEvaluationDetail prTestSetEvaluationDetail = ModelUtil.copyTo(prTestSetEvaluationDetailDto, PrTestSetEvaluationDetail.class);
        return service.save(prTestSetEvaluationDetail) ? OperateResult.success(ApplicationConstant.SAVE_MESSAGE, null) : OperateResult.error(ErrorCodeEnum.DATA_SAVE_FAILED);
    }

    /**
     * 更新问答对测试数据集评估详情操作。
     *
     * @param prTestSetEvaluationDetailDto 更新问答对测试数据集评估详情对象。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "更新问答对测试数据集评估详情", notes = "根据ID更新指定的问答对测试数据集评估详情信息")
    @PostMapping(value = "/updateById")
    public OperateResult<String> updateById(@Validated(Groups.UPDATE.class) @RequestBody PrTestSetEvaluationDetailDTO prTestSetEvaluationDetailDto) {
        PrTestSetEvaluationDetail prTestSetEvaluationDetail = ModelUtil.copyTo(prTestSetEvaluationDetailDto, PrTestSetEvaluationDetail.class);
        return service.updateById(prTestSetEvaluationDetail) ? OperateResult.successMessage(ApplicationConstant.UPDATE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_SAVE_FAILED);
    }

    /**
     * 删除指定的问答对测试数据集评估详情。
     *
     * @param id 指定问答对测试数据集评估详情主键Id。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "删除问答对测试数据集评估详情", notes = "根据ID删除指定的问答对测试数据集评估详情")
    @GetMapping(value = "/deleteById")
    public OperateResult<String> deleteById(@ApiParam(value = "问答对测试数据集评估详情ID", required = true, example = "1")
                                            @NotNull(message = "问答对测试数据集评估详情ID不能为空") @RequestParam("id") Long id) {
        return service.deleteById(id) ? OperateResult.successMessage(ApplicationConstant.DELETE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
    }

    /**
     * 对指定的问答对测试数据集评估详情进行点赞。
     *
     * @param id 指定问答对测试数据集评估详情主键Id。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "进行点赞", notes = "进行点赞")
    @GetMapping(value = "/like")
    public OperateResult<String> like(@ApiParam(value = "问答对测试数据集评估详情ID", required = true, example = "1")
                                      @NotNull(message = "问答对测试数据集评估详情ID不能为空") @RequestParam("id") Long id) {
        return service.like(id) ? OperateResult.successMessage("点赞成功") : OperateResult.error("点赞失败");
    }

    /**
     * 对指定的问答对测试数据集评估详情进行点踩。
     *
     * @param id 指定问答对测试数据集评估详情主键Id。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "进行点踩", notes = "进行点踩")
    @GetMapping(value = "/dislike")
    public OperateResult<String> dislike(@ApiParam(value = "问答对测试数据集评估详情ID", required = true, example = "1")
                                         @NotNull(message = "问答对测试数据集评估详情ID不能为空") @RequestParam("id") Long id) {
        return service.dislike(id) ? OperateResult.successMessage("点踩成功") : OperateResult.error("点踩失败");
    }

    /**
     * 对指定的问答对测试数据集评估详情进行选择回答1。
     *
     * @param id 指定问答对测试数据集评估详情主键Id。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "选择回答1", notes = "选择回答1")
    @GetMapping(value = "/q1")
    public OperateResult<String> q1(@ApiParam(value = "问答对测试数据集评估详情ID", required = true, example = "1")
                                         @NotNull(message = "问答对测试数据集评估详情ID不能为空") @RequestParam("id") Long id) {
        return service.action(id, ActionType.Q1) ? OperateResult.successMessage("选择回答1成功") : OperateResult.error("选择回答1失败");
    }

    /**
     * 对指定的问答对测试数据集评估详情进行选择回答2。
     *
     * @param id 指定问答对测试数据集评估详情主键Id。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "选择回答2", notes = "选择回答2")
    @GetMapping(value = "/q2")
    public OperateResult<String> q2(@ApiParam(value = "问答对测试数据集评估详情ID", required = true, example = "1")
                                    @NotNull(message = "问答对测试数据集评估详情ID不能为空") @RequestParam("id") Long id) {
        return service.action(id, ActionType.Q2) ? OperateResult.successMessage("选择回答2成功") : OperateResult.error("选择回答2失败");
    }
}