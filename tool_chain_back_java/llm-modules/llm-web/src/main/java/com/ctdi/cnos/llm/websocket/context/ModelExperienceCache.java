package com.ctdi.cnos.llm.websocket.context;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.ctdi.cnos.llm.beans.meta.application.ApplicationSquareVO;
import com.ctdi.cnos.llm.beans.train.deployTask.DeployTask;
import com.ctdi.cnos.llm.cache.ctg.CtgCache;
import com.ctdi.cnos.llm.feign.train.DeployTaskServiceClientFeign;
import com.ctdi.cnos.llm.websocket.util.WebSocketUtil;
import org.springframework.web.socket.WebSocketSession;

import java.util.Objects;

/**
 * 模型体验涉及到的缓存。应用广场和大模型等两种资源
 *
 * @author laiqi
 * @since 2024/6/19
 */
public class ModelExperienceCache {

    /**
     * 默认缓存12小时
     */
    private static CtgCache ctgCache;

    /**
     * 获取CtgCache
     *
     * @return
     */
    public synchronized static CtgCache getCtgCache() {
        if (ctgCache == null) {
            ctgCache = SpringUtil.getBean(CtgCache.class);
        }
        return ctgCache;
    }

    /**
     * 应用广场模板(缓存12小时)
     */
    private static TimedCache<Long, ApplicationSquareVO> applicationSquareCaches = CacheUtil.newTimedCache(DateUnit.HOUR.getMillis() * 12);

    /**
     * 模型推理部署信息缓存(缓存12小时)
     */
    private static TimedCache<Long, DeployTask> modelDeployTaskCaches = CacheUtil.newTimedCache(DateUnit.HOUR.getMillis() * 12);


    /**
     * 缓存的KEY
     */
    private static final String CACHE_KEY = "applicationSquareCaches";

    /**
     * 根据id获取应用广场(WebSocket环境)
     *
     * @param useCtgCache 是否使用CtgCache
     * @param id
     * @param session
     * @return
     */
    public static ApplicationSquareVO getByIdWithWebSocket(Boolean useCtgCache, Long id, WebSocketSession session) {
        if (useCtgCache) {
            Object data = getCtgCache().get(CACHE_KEY, String.valueOf(id));
            if (Objects.isNull(data)) {
                data = WebSocketUtil.queryApplicationSquareById(WebSocketUtil.getAuthorization(session, false), id);
                getCtgCache().set(CACHE_KEY, String.valueOf(id), data);
            }
            return (ApplicationSquareVO) data;
        }
        ApplicationSquareVO applicationSquareVO = applicationSquareCaches.get(id, false);
        if (Objects.isNull(applicationSquareVO)) {
            applicationSquareVO = WebSocketUtil.queryApplicationSquareById(WebSocketUtil.getAuthorization(session, false), id);
            if (ObjUtil.isNotNull(applicationSquareVO)) {
                applicationSquareCaches.put(id, applicationSquareVO);
            }
        }
        return applicationSquareVO;
    }


    /**
     * 根据id获取模型推理部署信息(HTTP环境)
     *
     * @param useCtgCache 是否使用CtgCache
     * @param id
     * @return
     */
    public static DeployTask getModelDeployTaskByIdWithHttp(Boolean useCtgCache, Long id) {
        if (useCtgCache) {
            Object data = getCtgCache().get(CACHE_KEY, String.valueOf(id));
            if (Objects.isNull(data)) {
                DeployTaskServiceClientFeign clientFeign = SpringUtil.getBean(DeployTaskServiceClientFeign.class);
                data = clientFeign.querySimpleById(String.valueOf(id));
                getCtgCache().set(CACHE_KEY, String.valueOf(id), data);
            }
            return (DeployTask) data;
        }
        return modelDeployTaskCaches.get(id, false, () -> {
            DeployTaskServiceClientFeign clientFeign = SpringUtil.getBean(DeployTaskServiceClientFeign.class);
            return clientFeign.querySimpleById(id.toString());
        });
    }
}