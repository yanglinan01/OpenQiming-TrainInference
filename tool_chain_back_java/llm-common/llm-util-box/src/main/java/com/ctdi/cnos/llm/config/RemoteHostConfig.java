package com.ctdi.cnos.llm.config;

import com.ctdi.cnos.llm.entity.HostInfo;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 远程数据集文件配置
 *
 * @author wangyb
 * @since 2024/7/15
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "remote")
public class RemoteHostConfig {

    private Map<String, HostInfo> hosts;

    private String filePrefix112;

    /**
     * lora模型在nfs中路径，意图识别部署用
     */
    private String nfs;


}