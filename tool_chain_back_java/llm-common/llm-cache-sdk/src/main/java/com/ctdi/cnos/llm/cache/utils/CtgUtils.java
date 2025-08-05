/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.cache.utils;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * CtgUtils
 *
 * @author huangjinhua
 * @since 2024/7/22
 */
@Slf4j
public class CtgUtils {
    public static String converObject2String(Object obj) {
        if (obj == null) {
            obj = "";
        }
        return JSONObject.toJSONString(obj, JSONWriter.Feature.WriteMapNullValue, JSONWriter.Feature.WriteNonStringValueAsString);
    }


    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Object o) throws IllegalArgumentException {
        if (o == null)
            return true;
        if ((o instanceof String)) {
            if (((String) o).length() == 0)
                return true;
        } else if ((o instanceof Collection)) {
            if (((Collection) o).isEmpty())
                return true;
        } else if (o.getClass().isArray()) {
            if (Array.getLength(o) == 0)
                return true;
        } else if ((o instanceof Map)) {
            if (((Map) o).isEmpty())
                return true;
        }
        return false;
    }

    public static boolean isNotEmpty(Object o) {
        return !isEmpty(o);
    }

    public static boolean isBasicType(Class<?> type) {
        if (type.isPrimitive())
            return true;

        if (type == String.class) {
            return true;
        } else if (type == Integer.class) {
            return true;
        } else if (type == Long.class) {
            return true;
        } else if (type == Double.class) {
            return true;
        } else if (type == Float.class) {
            return true;
        } else if (type == Short.class) {
            return true;
        } else if (type == Byte.class) {
            return true;
        } else if (type == Boolean.class) {
            return true;
        }

        return false;
    }


    public static String getStrFromObj(Object value) {
        if (isBasicType(value.getClass())) {
            return value.toString();
        }
        return converObject2String(value);
    }

}
