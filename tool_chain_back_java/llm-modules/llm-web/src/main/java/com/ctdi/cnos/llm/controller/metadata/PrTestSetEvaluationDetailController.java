package com.ctdi.cnos.llm.controller.metadata;

import com.ctdi.cnos.llm.base.constant.ActionType;
import com.ctdi.cnos.llm.base.constant.EvaluationConstant;
import com.ctdi.cnos.llm.base.object.Groups;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.beans.meta.evaluation.PrTestSetEvaluationDetailDTO;
import com.ctdi.cnos.llm.beans.meta.evaluation.PrTestSetEvaluationDetailVO;
import com.ctdi.cnos.llm.feign.metadata.PrTestSetEvaluationDetailServiceClientFeign;
import com.ctdi.cnos.llm.response.ErrorCodeEnum;
import com.ctdi.cnos.llm.response.OperateResult;
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
 * 问答对测试数据集评估详情服务远程数据操作访问接口。
 *
 * @author laiqi
 * @since 2024/09/03
 */
@Api(tags = "问答对测试数据集评估详情接口", value = "PrTestSetEvaluationDetailController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/prTestSetEvaluationDetail")
public class PrTestSetEvaluationDetailController {

    private final PrTestSetEvaluationDetailServiceClientFeign serviceClient;

    /**
     * 分页列出符合过滤条件的问答对测试数据集评估详情列表。
     *
     * @param queryParam 查询对象。
     * @return 应答结果对象，包含查询结果集。
     */
    @ApiOperation(value = "获取分页查询的问答对测试数据集评估详情列表信息", notes = "获取分页查询的问答对测试数据集评估详情信息")
    @PostMapping(value = "/queryPage")
    public OperateResult<PageResult<PrTestSetEvaluationDetailVO>> queryPage(@Validated(Groups.PAGE.class) @RequestBody QueryParam queryParam) {
        PageResult<PrTestSetEvaluationDetailVO> data = this.serviceClient.queryPage(queryParam);
        return OperateResult.success(data);
    }

    /**
     * 列出符合过滤条件的问答对测试数据集评估详情列表。
     *
     * @param queryParam 查询对象。
     * @return 应答结果对象，包含查询结果集。
     */
    @ApiOperation(value = "获取查询的问答对测试数据集评估详情列表信息", notes = "获取查询的问答对测试数据集评估详情信息")
    @PostMapping(value = "/queryList")
    public OperateResult<List<PrTestSetEvaluationDetailVO>> queryList(@RequestBody QueryParam queryParam) {
        List<PrTestSetEvaluationDetailVO> data = this.serviceClient.queryList(queryParam);
        return OperateResult.success(data);
    }

    /**
     * 问答对测试数据集评估反馈是否完成
     * @param testSetEvaluationId 测试数据集评估ID
     * @return
     */
    @ApiOperation(value = "问答对测试数据集评估反馈是否完成", notes = "根据测试数据集评估ID判断问答对测试数据集评估反馈是否完成")
    @GetMapping(value = "/isComplete")
    public OperateResult<Boolean> isComplete(@ApiParam(value = "测试数据集评估ID", required = true, example = "1")
                                             @Validated @NotNull(message = "测试数据集评估ID不能为空") @RequestParam("testSetEvaluationId") Long testSetEvaluationId) {
        return this.serviceClient.isComplete(testSetEvaluationId);
    }

    /**
     * 查看问答对测试数据集评估详情对象详情。
     *
     * @param id 指定问答对测试数据集评估详情主键Id。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "根据ID获取问答对测试数据集评估详情详情", notes = "通过问答对测试数据集评估详情ID获取具体的问答对测试数据集评估详情信息")
    @GetMapping(value = "/queryById")
    public OperateResult<PrTestSetEvaluationDetailVO> queryById(@ApiParam(value = "问答对测试数据集评估详情ID", required = true, example = "1")
                                                                @NotNull(message = "问答对测试数据集评估详情ID不能为空") @RequestParam("id") Long id) {
        PrTestSetEvaluationDetailVO data = this.serviceClient.queryById(id);
        return OperateResult.success(data);
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
        return this.serviceClient.add(prTestSetEvaluationDetailDto);
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
        return this.serviceClient.updateById(prTestSetEvaluationDetailDto);
    }

    /**
     * 删除指定的问答对测试数据集评估详情。
     *
     * @param id 指定问答对测试数据集评估详情主键Id。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "删除问答对测试数据集评估详情", notes = "根据ID删除指定的问答对测试数据集评估详情")
    @GetMapping(value = "/deleteById")
    public OperateResult<String> delete(@ApiParam(value = "问答对测试数据集评估详情ID", required = true, example = "1")
                                        @NotNull(message = "问答对测试数据集评估详情ID不能为空") @RequestParam("id") Long id) {
        return this.serviceClient.deleteById(id);
    }

    /**
     * 点赞、点踩、回答1、回答2。
     *
     * @param id 指定问答对测试数据集评估详情主键Id。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "点赞、点踩、回答1、回答2", notes = "点赞、点踩、回答1、回答2")
    @GetMapping(value = "/action")
    public OperateResult<String> action(@ApiParam(value = "问答对测试数据集评估详情ID", required = true, example = "1")
                                        @NotNull(message = "问答对测试数据集评估详情ID不能为空") @RequestParam("id") Long id,
                                        @ApiParam(value = "操作", required = true, example = "1")
                                        @NotNull(message = "操作不能为空") @RequestParam("action") Integer action) {
        PrTestSetEvaluationDetailVO prTestSetEvaluationDetailVO = this.serviceClient.queryById(id);
        String type = prTestSetEvaluationDetailVO.getType();
        if (EvaluationConstant.TEST_SET_EVALUATION_TYPE_GENERAL.equals(type)) {
            if (ActionType.LIKE.getValue() == action) {
                return this.serviceClient.like(id);
            } else if (ActionType.DISLIKE.getValue() == action) {
                return this.serviceClient.dislike(id);
            }
        }
        else if (EvaluationConstant.TEST_SET_EVALUATION_TYPE_LEAN.equals(type)) {
            if (ActionType.Q1.getValue() == action) {
                return this.serviceClient.q1(id);
            }
            else if (ActionType.Q2.getValue() == action) {
                return this.serviceClient.q2(id);
            }
        }
        return OperateResult.error(ErrorCodeEnum.INVALID_ARGUMENT_FORMAT);
    }

}