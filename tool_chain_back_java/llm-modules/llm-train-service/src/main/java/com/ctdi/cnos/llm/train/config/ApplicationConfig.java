/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.train.config;

import cn.hutool.core.map.MapUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * 训练 配置文件
 *
 * @author huangjinhua
 * @since 2024/5/23
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "train")
public class ApplicationConfig {
    /**
     * 是否启用模拟大模型接口
     */
    private boolean mock;
    /**
     * 训练任务 - 提交任务接口地址
     */
    private String submitTrainUrl;

    /**
     * 训练任务 - 终止、停止任务接口地址
     */
    private String modifyTrainUrl;

    /**
     * 训练任务 - 训练任务状态接口地址
     */
    private String statusTrainUrl;
    /**
     * 模型评估 - c-eval任务提交接口地址
     */
    private String evalTrainUrl;

    /**
     * 模型部署 -提交部署
     */
    private String submitDeployUrl;

    /**
     * 模型部署 - 删除部署
     */
    private String deleteDeployUrl;

    /**
     * 模型部署 - 部署状态
     */
    private String statusDeployUrl;

    /**
     * 模型推理 -部署上线后推理地址
     */
    private String reasonUrl;


    /**
     * 训练任务 -  模拟大模型接口
     */
    private boolean deployMock;

    /**
     * 模拟数据(key:中文的接口名称,value:模拟数据)
     */
    private Map<String, String> mockData = MapUtil.newHashMap();


    /**
     * 新增模型给注册api
     */
    private String addModelRegisterApiUrl;


    /**
     * 删除模型给注册api
     */
    private String deleteModelRegisterApiUrl;

    /**
     *
     */
    private String queryModelRegisterApiUrl;


    /**
     * 删除模型通知智能体
     */
    private String deleteAgentDeployUrl;

    /**
     * 创建模型通知智能体
     */
    private String createAgentDeployUrl;

    /**
     * 模型发布推送智能体审批
     */
    private String publishModelPushAgentUrl;

    /**
     * 对外接口地址
     */
    private String intfRestfulServiceUrl;


    /**
     * 部署后休眠时间
     */
    private Long sleepAfterDeploy;


    /**
     * 部署最大等待时间
     */
    private Long maxTimeAfterDeploy;


    /**
     * 算网调度AI-智算平台接口 AI集群查询
     */
    private String aiClusterListUrl;

    /**
     * 策选接口 策选查询
     */
    private String strategyListUrl;

    /**
     * 获取项目空间
     */
    private String agentTenantsApiUrl;

    /**
     * 训练任务通知智算开始url
     */
    private String aiApplicationOrderV2Url;

    /**
     * 训练任务通知智算结束url
     */
    private String atomicAbilityFinishUrl;

    /**
     * 发送训练中任务最大数
     */
    private String maxTrainTaskLimit;

    /**
     * 发送部署中任务最大数
     */
    private String maxDeployTaskLimit;

    /**
     * 动态注册意图识别Lora模型
     */
    private String registerIntentUrl;

    /**
     * 获取已注册意图识别类型
     */
    private String listRegisteredIntentsUrl;

    /**
     * 卸载意图识别Lora模型
     */
    private String unregisterIntentUrl;

    /**
     * 意图识别Lora模型通用接口
     */
    private String intentRecognizeUrl;

    /**
     * 通知意图识别load文件
     */
    private String intentSyncUrl;
}
