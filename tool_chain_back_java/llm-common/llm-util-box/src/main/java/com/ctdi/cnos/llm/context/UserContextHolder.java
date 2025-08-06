package com.ctdi.cnos.llm.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.ctdi.cnos.common.core.text.Convert;
import com.ctdi.cnos.common.core.utils.StringUtils;
import com.ctdi.cnos.llm.system.user.entity.UserVO;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserContextHolder {
    private static final TransmittableThreadLocal<Map<String, Object>> THREAD_LOCAL = new TransmittableThreadLocal();

    public UserContextHolder() {
    }

    public static void set(String key, Object value) {
        Map<String, Object> map = getLocalMap();
        map.put(key, value == null ? "" : value);
    }

    public static String get(String key) {
        Map<String, Object> map = getLocalMap();
        return Convert.toStr(map.getOrDefault(key, ""));
    }

    public static <T> T get(String key, Class<T> clazz) {
        Map<String, Object> map = getLocalMap();
        return StringUtils.cast(map.getOrDefault(key, (Object) null));
    }

    public static Map<String, Object> getLocalMap() {
        Map<String, Object> map = (Map) THREAD_LOCAL.get();
        if (map == null) {
            map = new ConcurrentHashMap();
            THREAD_LOCAL.set(map);
        }

        return (Map) map;
    }

    public static void setLocalMap(Map<String, Object> threadLocalMap) {
        THREAD_LOCAL.set(threadLocalMap);
    }

    public static Long getUserId() {
        return Convert.toLong(get("user_id"), 0L);
    }

    public static void setUserId(Long account) {
        set("user_id", account);
    }

    public static String getUserName() {
        return get("user_name");
    }

    public static void setUserName(String username) {
        set("user_name", username);
    }

    public static UserVO getUser() {
        return get("user_key", UserVO.class);
    }

    public static void setUser(UserVO userVOKey) {
        set("user_key", userVOKey);
    }

    public static void clear() {
        THREAD_LOCAL.remove();
    }

}