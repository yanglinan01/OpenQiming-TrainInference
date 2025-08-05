package com.ctdi.cnos.llm.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.ctdi.cnos.llm.base.aop.MybatisDataPermissionAspect;
import com.ctdi.cnos.llm.base.listener.LoadDataPermissionInfoListener;
import com.ctdi.cnos.llm.cache.ctg.CtgCache;
import com.ctdi.cnos.llm.system.auth.AuthClient;
import com.ctdi.cnos.llm.system.province.dao.ProvinceDao;
import com.ctdi.cnos.llm.system.province.serivce.ProvinceService;
import com.ctdi.cnos.llm.system.province.serivce.impl.ProvinceServiceImpl;
import com.ctdi.cnos.llm.system.user.dao.UserDao;
import com.ctdi.cnos.llm.system.user.dao.UserLoginLogDao;
import com.ctdi.cnos.llm.system.user.service.UserLoginLogService;
import com.ctdi.cnos.llm.system.user.service.UserService;
import com.ctdi.cnos.llm.system.user.service.impl.UserLoginLogServiceImpl;
import com.ctdi.cnos.llm.system.user.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 用户服务自动配置类。
 *
 * @author laiqi
 * @since 2024/7/25
 */
@Slf4j
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ConditionalOnProperty(prefix = "user", value = "enable", matchIfMissing = true) // 允许使用 user.enable=false 禁用用户
@EnableConfigurationProperties(UserConfig.class)
public class UserAutoConfiguration {

    /**
     * 用户服务实现
     *
     * @param userDao 用户dao
     * @return 用户服务实现。
     */
    @Bean
    @ConditionalOnMissingBean
    public UserService userService(CtgCache ctgCache, UserDao userDao) {
        log.info("开启了用户服务，可以在上下文环境中使用");
        return new UserServiceImpl(ctgCache, userDao);
    }

    /**
     * 登录日志服务实现
     *
     * @param userLoginLogDao 用户登录日志
     * @return 用户登录日志服务实现
     */
    @Bean
    @ConditionalOnMissingBean
    public UserLoginLogService userLoginLogService(UserLoginLogDao userLoginLogDao) {
        log.info("开启了用户服务，可以在上下文环境中使用");
        return new UserLoginLogServiceImpl(userLoginLogDao);
    }

    /**
     *
     * @param provinceDao
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public ProvinceService provinceService(ProvinceDao provinceDao) {
        log.info("开启了用户服务，可以在上下文环境中使用");
        return new ProvinceServiceImpl(provinceDao);
    }

    /**
     * 鉴权接口
     *
     * @param config              用户配置类
     * @param ctgCache            缓存
     * @param userService         用户服务实现。
     * @param userLoginLogService 用户登录日志服务实现。
     * @return 鉴权对象
     */
    @Bean
    @ConditionalOnMissingBean
    public AuthClient authClient(UserConfig config, CtgCache ctgCache, UserService userService, UserLoginLogService userLoginLogService) {
        return new AuthClient(config, userService, userLoginLogService, ctgCache);
    }

    /**
     * 加载数据权限信息
     *
     * @param mybatisPlusInterceptor mybatisPlus拦截器
     * @return ApplicationReadyEvent对象
     */
    @ConditionalOnProperty(prefix = "user", name = "data-permission.enable", havingValue = "true", matchIfMissing = false)
    @ConditionalOnMissingBean
    @Bean
    public LoadDataPermissionInfoListener loadDataPermissionInfoListener(MybatisPlusInterceptor mybatisPlusInterceptor) {
        log.info("开启了用户服务中的数据权限拦截");
        return new LoadDataPermissionInfoListener(mybatisPlusInterceptor);
    }

    /**
     * 配置Mybatis数据权限切面。
     * <p>
     * 允许通过配置项 `user.dataPermission.override` 来控制是否启用数据权限的重写逻辑。
     * 如果将该配置项的值设为false，则禁用数据权限的重写功能。
     *
     * @return MybatisDataPermissionAspect 的实例
     */
    @ConditionalOnProperty(prefix = "user", name = "data-permission.override", havingValue = "true", matchIfMissing = false)
    @ConditionalOnMissingBean
    @Bean
    public MybatisDataPermissionAspect mybatisDataPermissionAspect() {
        log.info("开启了用户服务中的数据权限拦截重写实现");
        // 在这里，通过条件注解控制Bean的创建，确保了只有在配置项允许的情况下才会启用数据权限重写逻辑。
        // 添加异常处理逻辑的建议需要在MybatisDataPermissionAspect的实现中进行，因此这里不作展示。
        return new MybatisDataPermissionAspect();
    }

}