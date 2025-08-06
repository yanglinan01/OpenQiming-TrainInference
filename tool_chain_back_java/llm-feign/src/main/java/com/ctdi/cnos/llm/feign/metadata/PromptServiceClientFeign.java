package com.ctdi.cnos.llm.feign.metadata;


import com.ctdi.cnos.llm.RemoteConstont;
import com.ctdi.cnos.llm.beans.meta.prompt.Prompt;
import com.ctdi.cnos.llm.beans.meta.prompt.PromptVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * prompt OpenFeign
 *
 * @author huangjinhua
 */
@Component
@FeignClient(value = RemoteConstont.METADATA_SERVICE_NAME, path = "${cnos.server.llm-metadata-service.contextPath}")
public interface PromptServiceClientFeign {

    /**
     * 分页查询prompt列表
     *
     * @param pageSize    页大小
     * @param currentPage 当前页
     * @param name        prompt名称
     * @param belong      prompt归属
     * @param types       prompt类别,多个以逗号（,）分隔
     * @return Map<String, Object>
     */
    @GetMapping(value = "/prompt/queryPage")
    Map<String, Object> queryPage(@RequestParam(name = "pageSize", required = false, defaultValue = "20") long pageSize,
                                  @RequestParam(name = "currentPage", required = false, defaultValue = "1") long currentPage,
                                  @RequestParam(name = "name", required = false) String name,
                                  @RequestParam(name = "belong") String belong,
                                  @RequestParam(name = "types", required = false) String types);

    /**
     * 查询prompt列表
     *
     * @param name   prompt名称
     * @param belong prompt归属
     * @param types  prompt类别,多个以逗号（,）分隔
     * @return List<Prompt>
     */
    @GetMapping(value = "/prompt/queryList")
    List<PromptVO> queryList(@RequestParam(name = "name", required = false) String name,
                             @RequestParam(name = "belong") String belong,
                             @RequestParam(name = "types", required = false) String types);

    /**
     * prompt详情
     *
     * @param id id
     * @return prompt
     */
    @GetMapping(value = "/prompt/detail")
    PromptVO detail(@RequestParam("id") Long id);

    /**
     * 根据当前用户获取prompt数量
     *
     * @return Long
     */
    @GetMapping("/prompt/getPromptCountByUserId")
    Long getPromptCountByUserId();

    /**
     * 新增prompt
     *
     * @param prompt prompt
     * @return map
     */
    @PostMapping(value = "/prompt/add")
    Map<String, Object> add(@RequestBody Prompt prompt);

    /**
     * 根据prompt 内容获取变量标识符
     *
     * @param promptText prompt
     * @return String
     */
    @PostMapping(value = "/prompt/getIdentifier")
    String getIdentifier(@RequestBody String promptText);

    /**
     * 根据prompt 内容获取变量
     *
     * @param promptText prompt
     * @return List<String>
     */
    @PostMapping(value = "/prompt/getVariable")
    List<String> getVariable(@RequestBody String promptText);

    /**
     * 修改prompt
     *
     * @param prompt prompt
     * @return map
     */
    @PostMapping("/prompt/updateById")
    Map<String, Object> update(@RequestBody Prompt prompt);

    /**
     * 删除prompt
     *
     * @param id id
     * @return map
     */
    @DeleteMapping("/prompt/deleteById")
    Map<String, Object> deleteById(@RequestParam("id") Long id);
}
