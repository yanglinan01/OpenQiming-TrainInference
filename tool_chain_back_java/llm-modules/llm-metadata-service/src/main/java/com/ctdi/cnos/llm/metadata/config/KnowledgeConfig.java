/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.metadata.config;

import cn.hutool.core.map.MapUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * 知识库配置文件
 *
 * @author wangyb
 * @since 2024/7/15
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "knowledge-base")
public class KnowledgeConfig {


    /**
     * 知识库访问地址ip:port
     */
    private String host;
    /**
     * 提交任务接口地址
     */
    private String docListUrl;

    /**
     * 远程知识库文件夹
     */
    private String docDir = "";

    /**
     * 模拟大模型接口
     */
    private boolean mock;

    /**
     * 模拟数据(key:中文的接口名称,value:模拟数据)
     */
    private Map<String, String> mockData = MapUtil.newHashMap();

    /**
     * 知识库ip
     */
    private String ip;

    /**
     * 知识库端口
     */
    private int port;

    /**
     * 知识库用户名
     */
    private String username;

    /**
     * 知识库密码
     */
    private String password;

    /**
     * 知识库意图识别语料
     */
    private String ownIntentionRecognitionCorpusUrl;

    /**
     * 知识库3c信息查询
     */
    private String info3cTreeUrl;

}