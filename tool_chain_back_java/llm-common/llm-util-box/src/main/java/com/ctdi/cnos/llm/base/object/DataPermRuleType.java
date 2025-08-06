package com.ctdi.cnos.llm.base.object;

import com.ctdi.cnos.llm.base.constant.SystemConstant;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据权限规则类型常量类。
 *
 * @author laiqi
 * @since 2024/7/12
 */
public final class DataPermRuleType {

    /**
     * 查看全部。
     */
    public static final String TYPE_ALL = SystemConstant.USER_ADMIN_CODE;

    /**
     * 仅查看当前区域。
     */
    public static final String TYPE_REGION_ONLY = SystemConstant.USER_REGION_LEADER_CODE;

    /**
     * 仅查看当前用户。
     */
    public static final String TYPE_USER_ONLY = SystemConstant.USER_ORDINARY_CODE;

    private static final Map<Object, String> DICT_MAP = new HashMap<>(3);
    static {
        DICT_MAP.put(TYPE_ALL, "查看全部");
        DICT_MAP.put(TYPE_USER_ONLY, "仅查看当前用户");
        DICT_MAP.put(TYPE_REGION_ONLY, "仅查看当前区域");
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
    private DataPermRuleType() {
    }
}