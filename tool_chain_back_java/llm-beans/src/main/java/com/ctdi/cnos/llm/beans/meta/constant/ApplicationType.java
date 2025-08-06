package com.ctdi.cnos.llm.beans.meta.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 应用类型常量类。
 *
 * @author laiqi
 * @since 2024/7/16
 */
public final class ApplicationType {

    /**
     * 大模型。
     */
    public static final int BIG_MODEL = 1;

    /**
     * 知识助手。
     */
    public static final int KNOWLEDGE_ASSISTANT = 2;

    /**
     * 自建模型。
     */
    public static final int SELF_BUILT_MODEL = 3;


    private static final Map<Object, String> DICT_MAP = new HashMap<>(3);

    static {
        DICT_MAP.put(BIG_MODEL, "大模型");
        DICT_MAP.put(KNOWLEDGE_ASSISTANT, "知识助手");
        DICT_MAP.put(SELF_BUILT_MODEL, "自建模型");
    }

    /**
     * 判断参数是否为当前常量字典的合法取值范围。
     *
     * @param value 待验证的参数值。
     * @return 合法返回true，否则false。
     */
    public static boolean isValid(Integer value) {
        return value != null && DICT_MAP.containsKey(value);
    }

    /**
     * 私有构造函数，明确标识该常量类的作用。
     */
    private ApplicationType() {
    }
}