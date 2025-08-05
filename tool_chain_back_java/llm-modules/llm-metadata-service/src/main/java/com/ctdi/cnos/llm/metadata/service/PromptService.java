package com.ctdi.cnos.llm.metadata.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.beans.meta.prompt.Prompt;
import com.ctdi.cnos.llm.beans.meta.prompt.PromptVO;

import java.util.List;

/**
 * prompt 业务接口
 *
 * @author huangjinhua
 * @since 2024/4/2
 */
public interface PromptService {
    /**
     * 分页查询prompt
     *
     * @param page   分页参数
     * @param prompt 查询对象
     * @return page
     */
    Page<PromptVO> queryList(Page<PromptVO> page, PromptVO prompt);

    /**
     * 全量查询prompt
     *
     * @param prompt 查询对象
     * @return List<Prompt>
     */
    List<PromptVO> queryList(PromptVO prompt);

    /**
     * 查询prompt详情
     *
     * @param id id
     * @return Prompt
     */
    PromptVO queryById(Long id);

    /**
     * 根据用户ID统计prompt 数量
     *
     * @return Long
     */
    Long getPromptCount();

    /**
     * 新增prompt
     *
     * @param prompt prompt对象
     * @return int
     */
    int insert(Prompt prompt);

    /**
     * 根据ID更新prompt
     *
     * @param prompt prompt对象
     * @return int
     */
    int updateById(Prompt prompt);

    /**
     * 根据ID删除prompt
     *
     * @param id prompt id
     * @return int
     */
    int deleteById(Long id);

    /**
     * 根据prompt内容获取标识符内的变量
     *
     * @param context prompt内容
     * @return String 标识符
     */
    String getIdentifier(String context);

    /**
     * 根据标识符获取标识符内的变量
     *
     * @param identifier 变量标识符
     * @param context    prompt内容
     * @return List<String>
     */
    List<String> getVariable(String identifier, String context);
}
