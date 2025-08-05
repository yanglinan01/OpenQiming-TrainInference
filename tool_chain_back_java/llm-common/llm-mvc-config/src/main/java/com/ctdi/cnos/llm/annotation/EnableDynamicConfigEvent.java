package com.ctdi.cnos.llm.annotation;


import com.ctdi.cnos.llm.config.DynamicConfigListenerConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link DynamicConfigListenerConfig} 开启配置变化监听器
 *
 * @author wangyb
 * @since 2024/9/14
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(DynamicConfigListenerConfig.class)
public @interface EnableDynamicConfigEvent {

}
