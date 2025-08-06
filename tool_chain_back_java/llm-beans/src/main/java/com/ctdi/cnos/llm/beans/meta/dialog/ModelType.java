package com.ctdi.cnos.llm.beans.meta.dialog;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 模型类型。
 *
 * @author laiqi
 * @since 2024/9/14
 */
@ApiModel(description = "模型类型")
@Getter
@RequiredArgsConstructor
public enum ModelType {
    SYSTEM_MODEL("系统模型", 1),
    KNOWLEDGE_ASSISTANT("知识助手", 2),
    CUSTOM_MODEL("自建模型", 3),
    TIME_LLM_MODEL("时序大横型", 4);

    /**
     * 描述。
     */
    private final String description;

    /**
     * 字典值。
     */
    private final Integer dictValue;
}