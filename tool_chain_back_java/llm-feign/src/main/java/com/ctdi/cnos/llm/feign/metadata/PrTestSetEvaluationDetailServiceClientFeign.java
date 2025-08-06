package com.ctdi.cnos.llm.feign.metadata;

import com.ctdi.cnos.llm.RemoteConstont;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.beans.meta.evaluation.PrTestSetEvaluationDetailDTO;
import com.ctdi.cnos.llm.beans.meta.evaluation.PrTestSetEvaluationDetailVO;
import com.ctdi.cnos.llm.response.OperateResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 问答对测试数据集评估详情服务远程数据操作访问接口。
 *
 * @author laiqi
 * @since 2024/09/03
 */
@Component
@FeignClient(value = RemoteConstont.METADATA_SERVICE_NAME, path = "${cnos.server.llm-metadata-service.contextPath}")
public interface PrTestSetEvaluationDetailServiceClientFeign {

    /**
     * 分页列出符合过滤条件的问答对测试数据集评估详情列表。
     *
     * @param queryParam 查询对象。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping(value = "/prTestSetEvaluationDetail/queryPage")
    PageResult<PrTestSetEvaluationDetailVO> queryPage(@RequestBody QueryParam queryParam);

    /**
     * 列出符合过滤条件的问答对测试数据集评估详情列表。
     *
     * @param queryParam 查询对象。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping(value = "/prTestSetEvaluationDetail/queryList")
    List<PrTestSetEvaluationDetailVO> queryList(@RequestBody QueryParam queryParam);

    /**
     * 问答对测试数据集评估反馈是否完成
     * @param testSetEvaluationId 测试数据集评估ID
     * @return
     */
    @GetMapping(value = "/prTestSetEvaluationDetail/isComplete")
    OperateResult<Boolean> isComplete(@RequestParam("testSetEvaluationId") Long testSetEvaluationId);

    /**
     * 查看问答对测试数据集评估详情对象详情。
     *
     * @param id 指定问答对测试数据集评估详情主键Id。
     * @return 应答结果对象。
     */
	@GetMapping(value = "/prTestSetEvaluationDetail/queryById")
    PrTestSetEvaluationDetailVO queryById(@RequestParam("id") Long id);

    /**
     * 添加问答对测试数据集评估详情操作。
     *
     * @param prTestSetEvaluationDetailDto 添加问答对测试数据集评估详情对象。
     * @return 应答结果对象。
     */
    @PostMapping(value = "/prTestSetEvaluationDetail/add")
    OperateResult<String> add(@RequestBody PrTestSetEvaluationDetailDTO prTestSetEvaluationDetailDto);
	
    /**
     * 更新问答对测试数据集评估详情操作。
     *
     * @param prTestSetEvaluationDetailDto 更新问答对测试数据集评估详情对象。
     * @return 应答结果对象。
     */
    @PostMapping(value = "/prTestSetEvaluationDetail/updateById")
    OperateResult<String> updateById(@RequestBody PrTestSetEvaluationDetailDTO prTestSetEvaluationDetailDto);

    /**
     * 删除指定的问答对测试数据集评估详情。
     *
     * @param id 指定问答对测试数据集评估详情主键Id。
     * @return 应答结果对象。
     */
    @GetMapping(value = "/prTestSetEvaluationDetail/deleteById")
    OperateResult<String> deleteById(@RequestParam("id") Long id);

    /**
     * 对指定的问答对测试数据集评估详情进行点赞。
     *
     * @param id 指定问答对测试数据集评估详情主键Id。
     * @return 应答结果对象。
     */
    @GetMapping(value = "/prTestSetEvaluationDetail/like")
    OperateResult<String> like(@RequestParam("id") Long id);

    /**
     * 对指定的问答对测试数据集评估详情进行点踩。
     *
     * @param id 指定问答对测试数据集评估详情主键Id。
     * @return 应答结果对象。
     */
    @GetMapping(value = "/prTestSetEvaluationDetail/dislike")
    OperateResult<String> dislike(@RequestParam("id") Long id);

    /**
     * 对指定的问答对测试数据集评估详情进行选择回答1。
     *
     * @param id 指定问答对测试数据集评估详情主键Id。
     * @return 应答结果对象。
     */
    @GetMapping(value = "/prTestSetEvaluationDetail/q1")
    OperateResult<String> q1(@RequestParam("id") Long id);

    /**
     * 对指定的问答对测试数据集评估详情进行选择回答2。
     *
     * @param id 指定问答对测试数据集评估详情主键Id。
     * @return 应答结果对象。
     */
    @GetMapping(value = "/prTestSetEvaluationDetail/q2")
    OperateResult<String> q2(@RequestParam("id") Long id);
}