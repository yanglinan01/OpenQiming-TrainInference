package com.ctdi.cnos.llm.controller.metadata;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import com.ctdi.cnos.llm.annotation.AuthIgnore;
import com.ctdi.cnos.llm.base.constant.MetaDataConstants;
import com.ctdi.cnos.llm.base.object.ExcelDataModel;
import com.ctdi.cnos.llm.base.object.Groups;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.beans.meta.evaluation.PrTestSetEvaluationDTO;
import com.ctdi.cnos.llm.beans.meta.evaluation.PrTestSetEvaluationDetail;
import com.ctdi.cnos.llm.beans.meta.evaluation.PrTestSetEvaluationDetailVO;
import com.ctdi.cnos.llm.beans.meta.evaluation.PrTestSetEvaluationVO;
import com.ctdi.cnos.llm.config.RemoteHostConfig;
import com.ctdi.cnos.llm.config.WebConfig;
import com.ctdi.cnos.llm.exception.MyRuntimeException;
import com.ctdi.cnos.llm.feign.metadata.PrTestSetEvaluationDetailServiceClientFeign;
import com.ctdi.cnos.llm.feign.metadata.PrTestSetEvaluationServiceClientFeign;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.util.ExcelUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.google.common.collect.Iterables;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 问答对测试数据集评估服务远程数据操作访问接口。
 *
 * @author laiqi
 * @since 2024/09/03
 */
@Api(tags = "问答对测试数据集评估接口", value = "PrTestSetEvaluationController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/prTestSetEvaluation")
@Slf4j
public class PrTestSetEvaluationController {

	private final PrTestSetEvaluationServiceClientFeign serviceClient;

	private final PrTestSetEvaluationDetailServiceClientFeign detailServiceClient;

    private final WebConfig config;

    private final RemoteHostConfig remoteHostConfig;

    /**
     * 分页列出符合过滤条件的问答对测试数据集评估列表。
     *
     * @param queryParam 查询对象。
     * @return 应答结果对象，包含查询结果集。
     */
	@ApiOperation(value = "获取分页查询的问答对测试数据集评估列表信息", notes = "获取分页查询的问答对测试数据集评估信息")
    @PostMapping(value = "/queryPage")
    public OperateResult<PageResult<PrTestSetEvaluationVO>> queryPage(@Validated(Groups.PAGE.class) @RequestBody QueryParam queryParam) {
		PageResult<PrTestSetEvaluationVO> data = this.serviceClient.queryPage(queryParam);
        return OperateResult.success(data);
    }

    /**
     * 列出符合过滤条件的问答对测试数据集评估列表。
     *
     * @param queryParam 查询对象。
     * @return 应答结果对象，包含查询结果集。
     */
	@ApiOperation(value = "获取查询的问答对测试数据集评估列表信息", notes = "获取查询的问答对测试数据集评估信息")
    @PostMapping(value = "/queryList")
    public OperateResult<List<PrTestSetEvaluationVO>> queryList(@RequestBody QueryParam queryParam) {
		List<PrTestSetEvaluationVO> data = this.serviceClient.queryList(queryParam);
        return OperateResult.success(data);
    }	

    /**
     * 查看问答对测试数据集评估对象详情。
     *
     * @param id 指定问答对测试数据集评估主键Id。
     * @return 应答结果对象。
     */
	@ApiOperation(value = "根据ID获取问答对测试数据集评估详情", notes = "通过问答对测试数据集评估ID获取具体的问答对测试数据集评估信息")
	@GetMapping(value = "/queryById")
    public OperateResult<PrTestSetEvaluationVO> queryById(@ApiParam(value = "问答对测试数据集评估ID", required = true, example = "1")
                          @NotNull(message = "问答对测试数据集评估ID不能为空") @RequestParam("id") Long id) {
		PrTestSetEvaluationVO data = this.serviceClient.queryById(id);
        return OperateResult.success(data);
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
		return this.serviceClient.add(prTestSetEvaluationDto);
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
		return this.serviceClient.updateById(prTestSetEvaluationDto);
    }

    /**
     * 删除指定的问答对测试数据集评估。
     *
     * @param id 指定问答对测试数据集评估主键Id。
     * @return 应答结果对象。
     */
	@ApiOperation(value = "删除问答对测试数据集评估", notes = "根据ID删除指定的问答对测试数据集评估")
    @GetMapping(value = "/deleteById")
    public OperateResult<String> delete(@ApiParam(value = "问答对测试数据集评估ID", required = true, example = "1")
                          @NotNull(message = "问答对测试数据集评估ID不能为空") @RequestParam("id") Long id) {
        return this.serviceClient.deleteById(id);
    }

    /**
     * 导出指定问答对测试数据集评估详情。
     *
     * @param id 指定问答对测试数据集评估主键Id。
     * @param response HttpServletResponse对象。
     */
    @AuthIgnore
    @SneakyThrows
    @ApiOperation(value = "导出问答对测试数据集评估详情", notes = "根据ID导出指定的问答对测试数据集评估详情")
    @GetMapping(value = "/export")
    public void export(@ApiParam(value = "问答对测试数据集评估ID", required = true, example = "1")
                         @NotNull(message = "问答对测试数据集评估ID不能为空") @RequestParam("id") Long id,
                         HttpServletResponse response) {
        ExcelDataModel excelDataModel = this.serviceClient.export(id);
        ExcelUtil.exportExcel(excelDataModel, response);
    }


    @ApiOperation(value = "导出强化问答对测试数据集为文件", notes = "导出强化问答对测试数据集为文件")
    @GetMapping(value = "/exportToFile/{id}")
    public OperateResult<Map<String, Object>> exportToFile(@ApiParam(value = "强化问答对测试数据集评估ID", required = true, example = "1")
                                  @NotNull(message = "强化问答对测试数据集评估ID不能为空") @PathVariable("id") String id) {

        PrTestSetEvaluationVO vo = this.serviceClient.queryById(Convert.toLong(id));

        Assert.isTrue(null != vo, "评估任务不存在,请检查");
        Assert.isTrue(Boolean.FALSE.equals(vo.getBuilt()), "请不要重复构建强化学习数据集");
        Assert.isTrue(vo.getStatus().equals(MetaDataConstants.DATA_SET_EVALUATION_STATUS_COMPLETED), "请选择评估成功的强化学习数据集");

        ExcelDataModel excelDataModel = this.serviceClient.export(Convert.toLong(id));

        if (config.isValidateEnhanceFeedback()) {
            log.info("=====开启强化数据集用户反馈校验======");
            QueryParam queryParam = new QueryParam();
            queryParam.setFilterMap(MapUtil.of(PrTestSetEvaluationDetail.Fields.testSetEvaluationId, id));
            List<PrTestSetEvaluationDetailVO> list = this.detailServiceClient.queryList(queryParam);
            if (CollUtil.isNotEmpty(list)) {
                this.validateEnhanceUserFeedback(list);
            }
        }
        String filePath = ExcelUtil.exportExcelToFile(config.getDatasetUploadDir(), remoteHostConfig, excelDataModel.getHeaderAlias(), excelDataModel.getRows());

        Map<String, Object> map = new HashMap<>(2);
        map.put("savePath", filePath);
        map.put("enhancedTrainTaskId", Convert.toStr(vo.getModelTaskId()));
        map.put("testSetEvaluationId", Convert.toStr(id));
        map.put("prCount", Iterables.size(excelDataModel.getRows()));

        // 标记为已构建强化学习
        PrTestSetEvaluationDTO prTestSetEvaluationDto = new PrTestSetEvaluationDTO();
        prTestSetEvaluationDto.setId(vo.getId());
        prTestSetEvaluationDto.setBuilt(true);
        this.serviceClient.updateById(prTestSetEvaluationDto);
        return new OperateResult<>(true, null, map);
    }


    /**
     * 文件上传操作。
     *
     * @param id  问答对测试数据集评估ID。
     * @param uploadFile 上传文件对象。
     */
    @ApiOperation(value = "文件上传操作", notes = "文件上传操作")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public OperateResult<String> upload(
            @ApiParam(value = "问答对测试数据集评估ID", required = true, example = "1")
            @NotNull(message = "问答对测试数据集评估ID不能为空") @RequestParam("id") Long id,
            @RequestParam("uploadFile") MultipartFile uploadFile) throws IOException {
        return this.serviceClient.upload(id, uploadFile);
    }



    /**
     * 校验强化训练数据集构建
     * @param rows
     */
    private void validateEnhanceUserFeedback(List<PrTestSetEvaluationDetailVO> rows) {
        List<String> errors = new ArrayList<>();

        for (PrTestSetEvaluationDetailVO row : rows) {
            if (row.getUserFeedback() == null || row.getUserFeedback().trim().isEmpty()) {
                errors.add("强化训练数据集反馈表【序号】 " + row.getIndex() + " 的【选择回答】 的值为空, 请填写【回答1 或 回答2】");
            }
        }

        if (!errors.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder();
            for (String error : errors) {
                errorMessage.append(error).append("\n");
            }
            throw new MyRuntimeException(errorMessage.toString());
        }
    }


}