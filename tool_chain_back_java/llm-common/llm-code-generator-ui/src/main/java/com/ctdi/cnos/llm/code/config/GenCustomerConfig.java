package com.ctdi.cnos.llm.code.config;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

/**
 * 自定义 的配置。
 *
 * @author laiqi
 * @since 2024/7/5
 */
@Data
public class GenCustomerConfig implements Serializable {

    /**
     * 所在包
     */
    private String packageName;
    /**
     * 类名
     */
    private String className;

    /**
     * 父类
     */
    private String superClass;

    /**
     * 是否需要序列化
     */
    private boolean serialVersionUID;

    /**
     * 导入包
     */
    private Set<String> importPackages = new TreeSet<>();

    /**
     * Feign Client 的引用。
     * @see @FeignClient(value = RemoteConstont.${feignClientConfig.feignClientRef}_SERVICE_NAME
     */
    private String feignClientRef;

}