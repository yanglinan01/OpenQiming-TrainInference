package com.ctdi.cnos.llm.config;

import lombok.Getter;
import lombok.Setter;

/**
 * 插件配置。
 *
 * @author laiqi
 * @since 2024/7/22
 */
@Setter
@Getter
public class PluginProperties {
    /**
     * 插件列表URL地址。
     */
    private String pluginListUrl;

    /**
     * 认证Token。
     */
    private String authToke;
}