package com.ctdi.cnos.llm.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;


/**
 * @author xuwj09
 * @version 1.0
 * @date 2025/6/11 15:11
 * @Description
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "web")
public class WebConfig {

    /**
     * 数据集下载目录
     */
    private String datasetDownloadDir;

    /**
     * 数据集上传目录
     */
    private String datasetUploadDir;

    /**
     * 知识库合并用的本地目录
     */
    private String datasetMergeDir;

    /**
     * 是否开启模型体验日志。
     */
    private boolean enableModelChatLog = true;

    /**
     * 是否开启强化数据集返回校验
     */
    private boolean validateEnhanceFeedback = true;

    /**
     * 插件配置
     */
    @NestedConfigurationProperty
    private PluginProperties pluginProperties;
}
