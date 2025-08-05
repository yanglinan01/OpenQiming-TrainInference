package com.ctdi.cnos.llm.base.listener;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.ctdi.cnos.llm.base.constant.DataPermissionRule;
import com.ctdi.cnos.llm.base.interceptor.MybatisDataPermissionInterceptor;
import com.ctdi.cnos.llm.util.MybatisPlusUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

/**
 * 应用服务启动监听器。
 * 目前主要功能是调用DataPermissionRule中的loadInfoWithDataPermission方法，
 * 将标记实体数据权限中的过滤字段数据加载到缓存，以提升系统运行时效率。
 * 以及添加数据权限拦截器。
 *
 * @author laiqi
 * @since 2024/7/29
 */
@RequiredArgsConstructor
public class LoadDataPermissionInfoListener implements ApplicationListener<ApplicationReadyEvent> {

    private final MybatisPlusInterceptor mybatisPlusInterceptor;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        DataPermissionRule.loadInfoWithDataPermission();
        // 添加数据权限拦截器(需要在分页拦截器之前添加)
        MybatisDataPermissionInterceptor dataPermissionInterceptor = new MybatisDataPermissionInterceptor();
        MybatisPlusUtil.addInterceptor(mybatisPlusInterceptor, dataPermissionInterceptor, 0);
    }
}