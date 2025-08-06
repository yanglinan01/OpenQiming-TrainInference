package com.ctdi.cnos.llm.metadata.controller;

import com.ctdi.cnos.llm.annotation.AuthIgnore;
import com.ctdi.cnos.llm.base.constant.ApplicationConstant;
import com.ctdi.cnos.llm.base.object.ExcelDataModel;
import com.ctdi.cnos.llm.base.object.Groups;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.beans.meta.evaluation.PrTestSetEvaluation;
import com.ctdi.cnos.llm.beans.meta.evaluation.PrTestSetEvaluationDTO;
import com.ctdi.cnos.llm.beans.meta.evaluation.PrTestSetEvaluationVO;
import com.ctdi.cnos.llm.metadata.service.PrTestSetEvaluationService;
import com.ctdi.cnos.llm.response.ErrorCodeEnum;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.util.ModelUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.io.IOException;
import java.util.List;

/**
 * 问答对测试数据集评估 操作控制器类。
 *
 * @author laiqi
 * @since 2024/09/03
 */
@Api(tags = "问答对测试数据集评估接口", value = "PrTestSetEvaluationController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/prTestSetEvaluation")
public class PrTestSetEvaluationController {

    private final PrTestSetEvaluationService service;

    /**
     * 分页列出符合过滤条件的问答对测试数据集评估列表。
     *
     * @param queryParam 查询对象。
     * @return 应答结果对象，包含查询结果集。
     */
    @ApiOperation(value = "获取分页查询的问答对测试数据集评估列表信息", notes = "获取分页查询的问答对测试数据集评估信息")
    @PostMapping(value = "/queryPage")
    public PageResult<PrTestSetEvaluationVO> queryPage(@Validated(Groups.PAGE.class) @RequestBody QueryParam queryParam) {
        PrTestSetEvaluationDTO dto = queryParam.getFilterDto(PrTestSetEvaluationDTO.class);
        ModelUtil.validateModelAndThrow(dto, Groups.PAGE.class, Default.class);
        return service.queryPage(queryParam);
    }

    /**
     * 列出符合过滤条件的问答对测试数据集评估列表。
     *
     * @param queryParam 查询对象。
     * @return 应答结果对象，包含查询结果集。
     */
    @ApiOperation(value = "获取查询的问答对测试数据集评估列表信息", notes = "获取查询的问答对测试数据集评估信息")
    @PostMapping(value = "/queryList")
    public List<PrTestSetEvaluationVO> queryList(@RequestBody QueryParam queryParam) {
        PrTestSetEvaluationDTO dto = queryParam.getFilterDto(PrTestSetEvaluationDTO.class);
        ModelUtil.validateModelAndThrow(dto, Groups.QUERY.class, Default.class);
        return service.queryList(queryParam);
    }

    /**
     * 查看问答对测试数据集评估对象详情。
     *
     * @param id 指定问答对测试数据集评估主键Id。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "根据ID获取问答对测试数据集评估详情", notes = "通过问答对测试数据集评估ID获取具体的问答对测试数据集评估信息")
    @GetMapping(value = "/queryById")
    public PrTestSetEvaluationVO queryById(@ApiParam(value = "问答对测试数据集评估ID", required = true, example = "1")
                                           @NotNull(message = "问答对测试数据集评估ID不能为空") @RequestParam("id") Long id) {
        return service.queryById(id, true);
    }

    /**
     * 添加问答对测试数据集评估操作。
     *
     * @param prTestSetEvaluationDto 添加问答对测试数据集评估对象。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "创建问答对测试数据集评估", notes = "根据请求体中的问答对测试数据集评估信息创建")
    @ApiOperationSupport(ignoreParameters = {"id"})
    @PostMapping(value = "/add")
    public OperateResult<String> add(@Validated(Groups.ADD.class) @RequestBody PrTestSetEvaluationDTO prTestSetEvaluationDto) {
        PrTestSetEvaluation prTestSetEvaluation = ModelUtil.copyTo(prTestSetEvaluationDto, PrTestSetEvaluation.class);
        return service.insert(prTestSetEvaluation) ? OperateResult.successMessage(ApplicationConstant.SAVE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_SAVE_FAILED);
    }

    /**
     * 更新问答对测试数据集评估操作。
     *
     * @param prTestSetEvaluationDto 更新问答对测试数据集评估对象。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "更新问答对测试数据集评估", notes = "根据ID更新指定的问答对测试数据集评估信息")
    @PostMapping(value = "/updateById")
    public OperateResult<String> updateById(@Validated(Groups.UPDATE.class) @RequestBody PrTestSetEvaluationDTO prTestSetEvaluationDto) {
        PrTestSetEvaluation prTestSetEvaluation = ModelUtil.copyTo(prTestSetEvaluationDto, PrTestSetEvaluation.class);
        return service.updateById(prTestSetEvaluation) ? OperateResult.successMessage(ApplicationConstant.UPDATE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_SAVE_FAILED);
    }

    /**
     * 删除指定的问答对测试数据集评估。
     *
     * @param id 指定问答对测试数据集评估主键Id。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "删除问答对测试数据集评估", notes = "根据ID删除指定的问答对测试数据集评估")
    @GetMapping(value = "/deleteById")
    public OperateResult<String> deleteById(@ApiParam(value = "问答对测试数据集评估ID", required = true, example = "1")
                                            @NotNull(message = "问答对测试数据集评估ID不能为空") @RequestParam("id") Long id) {
        return service.deleteById(id) ? OperateResult.successMessage(ApplicationConstant.DELETE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
    }

    /**
     * 导出指定问答对测试数据集评估详情。
     *
     * @param id 指定问答对测试数据集评估主键Id。
     */
    @AuthIgnore
    @SneakyThrows
    @ApiOperation(value = "导出问答对测试数据集评估详情", notes = "根据ID导出指定的问答对测试数据集评估详情")
    @GetMapping(value = "/export")
    public ExcelDataModel export(@ApiParam(value = "问答对测试数据集评估ID", required = true, example = "1")
                                 @NotNull(message = "问答对测试数据集评估ID不能为空") @RequestParam("id") Long id) {
        return service.export(id);
    }

    /**
     * 文件上传操作。
     *
     * @param id         问答对测试数据集评估ID。
     * @param uploadFile 上传文件对象。
     */
    @ApiOperation(value = "文件上传操作", notes = "文件上传操作")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public OperateResult<String> upload(
            @ApiParam(value = "问答对测试数据集评估ID", required = true, example = "1")
            @NotNull(message = "问答对测试数据集评估ID不能为空") @RequestParam("id") Long id,
            @RequestParam("uploadFile") MultipartFile uploadFile) throws IOException {
        return service.upload(id, uploadFile);
    }


    @AuthIgnore
    @ApiOperation(value = "根据测试数据集批量问答", notes = "根据测试数据集批量问答")
    @GetMapping(value = "/batchChatByTestDataSet")
    public void batchChatByTestDataSet() {
        service.batchChatByTestDataSet();
    }

    @AuthIgnore
    @ApiOperation(value = "回调获取评估部署任务状态", notes = "回调获取评估部署任务状态")
    @GetMapping(value = "/callbackDeployEvalStatus")
    public String callbackDeployEvalStatus() {
        int evalStatus = service.callbackDeployEvalStatus();
        return "回调获取评估部署任务状态数量：" + evalStatus;
    }

    @AuthIgnore
    @ApiOperation(value = "提交评估部署任务", notes = "提交评估部署任务")
    @PostMapping(value = "/submitEvaluationDeploymentTask")
    public String submitEvaluationDeploymentTask() {
        int curEvaluationCount = service.submitEvaluationDeploymentTask();
        return "提交评估部署任务数量：" + curEvaluationCount;
    }

}