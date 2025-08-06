package com.ctdi.cnos.llm.feign.metadata;

import com.ctdi.cnos.llm.RemoteConstont;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.beans.meta.prompt.PromptSequentialDetail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * 时序测试数据集详情服务远程数据操作访问接口。
 *
 * @author laiqi
 * @since 2024/08/16
 */
@Component
@FeignClient(value = RemoteConstont.METADATA_SERVICE_NAME, path = "${cnos.server.llm-metadata-service.contextPath}")
public interface PromptSequentialDetailServiceClientFeign {

    /**
     * 分页列出符合过滤条件的时序测试数据集详情列表。
     *
     * @param queryParam 查询对象。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping(value = "/promptSequentialDetail/queryPage")
    PageResult<PromptSequentialDetail> queryPage(@RequestBody QueryParam queryParam);

    /**
     * 列出符合过滤条件的时序测试数据集详情列表。
     *
     * @param queryParam 查询对象。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping(value = "/promptSequentialDetail/queryList")
    List<PromptSequentialDetail> queryList(@RequestBody QueryParam queryParam);

    /**
     * 统计符合过滤条件的时序测试数量。
     *
     * @param dataSetId 数据集id。
     * @return 应答结果对象，包含查询结果集。
     */
    @GetMapping(value = "/promptSequentialDetail/countCircuitByDataSetId")
    Long countCircuitByDataSetId(@RequestParam("dataSetId") Long dataSetId);

}