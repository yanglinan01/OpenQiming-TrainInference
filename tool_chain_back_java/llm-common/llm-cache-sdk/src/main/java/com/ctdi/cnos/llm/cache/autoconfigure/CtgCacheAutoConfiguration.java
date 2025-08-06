package com.ctdi.cnos.llm.cache.autoconfigure;

import com.ctdi.cnos.llm.cache.aspect.AsyncCtgCacheService;
import com.ctdi.cnos.llm.cache.aspect.CtgCacheAspect;
import com.ctdi.cnos.llm.cache.aspect.CtgCacheSupport;
import com.ctdi.cnos.llm.cache.ctg.CtgCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @author ccyy8
 * @since 2022-09-20 09:32
 */
@Configuration
public class CtgCacheAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(AsyncCtgCacheService.class)
    public AsyncCtgCacheService asyncCtgCacheService(){
        return new AsyncCtgCacheService();
    }

    @Bean
    @ConditionalOnMissingBean(CtgCacheAspect.class)
    public CtgCacheAspect ctgCacheAspect(){
        return new CtgCacheAspect();
    }

    @Bean
    @ConditionalOnMissingBean(CtgCacheSupport.class)
    public CtgCacheSupport ctgCacheSupport(){
        return new CtgCacheSupport();
    }

    @Bean
    @ConditionalOnMissingBean(CtgCache.class)
    public CtgCache ctgCache(){
        return new CtgCache();
    }

    @Bean
    @ConditionalOnMissingBean(ThreadPoolTaskScheduler.class)
    public ThreadPoolTaskScheduler threadPoolTaskScheduler(){
        return new ThreadPoolTaskScheduler();
    }
}
