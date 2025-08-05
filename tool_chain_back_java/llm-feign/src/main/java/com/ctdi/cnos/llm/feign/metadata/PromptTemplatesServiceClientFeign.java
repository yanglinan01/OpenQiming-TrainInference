package com.ctdi.cnos.llm.feign.metadata;


import com.ctdi.cnos.llm.RemoteConstont;
import com.ctdi.cnos.llm.beans.meta.prompt.PromptTemplates;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author wangyb
 * @version 1.0.0
 * @ClassName PromptTemplatesServiceClientFeign.java
 * @Description TODO
 * @createTime 2024-4-10-10:55:00
 */

@Component
@FeignClient(value = RemoteConstont.METADATA_SERVICE_NAME, path = "${cnos.server.llm-metadata-service.contextPath}")
public interface PromptTemplatesServiceClientFeign {

    /**
     * 查询PromptTemplates详情
     * @param id id
     * @return PromptTemplates对象
     */
    @GetMapping("/promptTemplates/queryById/{id}")
    PromptTemplates queryById(@PathVariable("id") BigDecimal id);

    /**
     * 查询PromptTemplates列表
     * @param promptTemplates promptTemplates对象
     * @return List<PromptTemplates>
     */
    @PostMapping("/promptTemplates/queryList")
    List<PromptTemplates> queryList(@RequestBody PromptTemplates promptTemplates);

    /**
     * 分页查询PromptTemplates列表
     * @param pageSize  每页条数
     * @param currentPage 当前页
     * @param promptTemplates 查询条件
     * @return List<PromptTemplates>
     */
    @PostMapping("/promptTemplates/queryPage")
    Map<String, Object> queryPage(
            @RequestParam(name = "pageSize", required = false, defaultValue = "20") long pageSize,
            @RequestParam(name = "currentPage", required = false, defaultValue = "1")long currentPage,
            @RequestBody PromptTemplates promptTemplates);

    /**
     * 删除PromptTemplates对象
     * @param id id
     */
    @GetMapping("/promptTemplates/deleteById/{id}")
    Map<String, Object> deleteById(@PathVariable("id") BigDecimal id);

    /**
     * 新增PromptTemplates对象
     * @param promptTemplates promptTemplates对象
     */
    @PostMapping("/promptTemplates/insert")
    Map<String, Object> insert(@RequestBody PromptTemplates promptTemplates);

    /**
     * 更新PromptTemplates对象
     * @param promptTemplates promptTemplates对象
     */
    @PostMapping("/promptTemplates/updateById")
    Map<String, Object> updateById(@RequestBody PromptTemplates promptTemplates);

}
