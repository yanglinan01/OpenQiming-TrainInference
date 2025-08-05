package com.ctdi.cnos.llm.feign.metadata;

import com.ctdi.cnos.llm.RemoteConstont;
import com.ctdi.cnos.llm.base.object.ExcelDataModel;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.beans.meta.evaluation.PrTestSetEvaluationDTO;
import com.ctdi.cnos.llm.beans.meta.evaluation.PrTestSetEvaluationVO;
import com.ctdi.cnos.llm.response.OperateResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 问答对测试数据集评估服务远程数据操作访问接口。
 *
 * @author laiqi
 * @since 2024/09/03
 */
@Component
@FeignClient(value = RemoteConstont.METADATA_SERVICE_NAME, path = "${cnos.server.llm-metadata-service.contextPath}")
public interface PrTestSetEvaluationServiceClientFeign {

    /**
     * 分页列出符合过滤条件的问答对测试数据集评估列表。
     *
     * @param queryParam 查询对象。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping(value = "/prTestSetEvaluation/queryPage")
    PageResult<PrTestSetEvaluationVO> queryPage(@RequestBody QueryParam queryParam);

    /**
     * 列出符合过滤条件的问答对测试数据集评估列表。
     *
     * @param queryParam 查询对象。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping(value = "/prTestSetEvaluation/queryList")
    List<PrTestSetEvaluationVO> queryList(@RequestBody QueryParam queryParam);

    /**
     * 查看问答对测试数据集评估对象详情。
     *
     * @param id 指定问答对测试数据集评估主键Id。
     * @return 应答结果对象。
     */
    @GetMapping(value = "/prTestSetEvaluation/queryById")
    PrTestSetEvaluationVO queryById(@RequestParam("id") Long id);

    /**
     * 添加问答对测试数据集评估操作。
     *
     * @param prTestSetEvaluationDto 添加问答对测试数据集评估对象。
     * @return 应答结果对象。
     */
    @PostMapping(value = "/prTestSetEvaluation/add")
    OperateResult<String> add(@RequestBody PrTestSetEvaluationDTO prTestSetEvaluationDto);

    /**
     * 更新问答对测试数据集评估操作。
     *
     * @param prTestSetEvaluationDto 更新问答对测试数据集评估对象。
     * @return 应答结果对象。
     */
    @PostMapping(value = "/prTestSetEvaluation/updateById")
    OperateResult<String> updateById(@RequestBody PrTestSetEvaluationDTO prTestSetEvaluationDto);

    /**
     * 删除指定的问答对测试数据集评估。
     *
     * @param id 指定问答对测试数据集评估主键Id。
     * @return 应答结果对象。
     */
    @GetMapping(value = "/prTestSetEvaluation/deleteById")
    OperateResult<String> deleteById(@RequestParam("id") Long id);

    /**
     * 下载指定问答对测试数据集评估详情。
     *
     * @param id 指定问答对测试数据集评估主键Id。
     * @return 应答结果对象。
     */
    @GetMapping(value = "/prTestSetEvaluation/export")
    ExcelDataModel export(@RequestParam("id") Long id);

    /**
     * 文件上传操作。
     *
     * @param id         问答对测试数据集评估ID。
     * @param uploadFile 上传文件对象。
     */
    @PostMapping(value = "/prTestSetEvaluation/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    OperateResult<String> upload(
            @RequestParam("id") Long id,
            @RequestPart("uploadFile") MultipartFile uploadFile) throws IOException;


    /**
     * 批量进行测试数据集推理
     */
    @GetMapping(value = "/prTestSetEvaluation/batchChatByTestDataSet")
    void batchChatByTestDataSet();


    /**
     * 回调获取评估部署任务状态
     *
     * @return String
     */
    @GetMapping(value = "/prTestSetEvaluation/callbackDeployEvalStatus")
    String callbackDeployEvalStatus();

    @PostMapping(value = "/prTestSetEvaluation/submitEvaluationDeploymentTask")
    String submitEvaluationDeploymentTask();
}