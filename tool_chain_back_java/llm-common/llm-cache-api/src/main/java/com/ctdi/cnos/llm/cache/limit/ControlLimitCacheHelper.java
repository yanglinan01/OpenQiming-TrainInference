package com.ctdi.cnos.llm.cache.limit;


import com.ctdi.cnos.llm.cache.ctg.CtgCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ControlLimitCacheHelper {

    @Autowired
    private CtgCache ctgCache;

    public boolean tryAcquire(String key, Long limit, int expireTime) {
        // 使用Redis的INCR命令原子地增加计数器的值，并返回增加后的值
        Long current = ctgCache.incr(key);
        // 检查计数器的值是否超过了阈值
        if (current > limit) {
            // 如果超过阈值，则拒绝请求，并尝试将计数器的值减一（因为上一步已经加了一）
            // 注意：这里减一操作可能失败，因为其他请求可能同时也在操作这个计数器
            // 但这并不影响限流的效果，因为即使减一失败，计数器的值也仍然超过了阈值
            ctgCache.decr(key);
            return false;
        } else {
            // 设置计数器的过期时间，实现滑动时间窗口的效果
            ctgCache.pexpire(key, expireTime);
            return true;
        }
    }

    public void tryDecr(String key) {
        ctgCache.decr(key);
    }
}
