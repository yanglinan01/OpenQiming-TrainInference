package com.ctdi.cnos.llm.entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author wangyb
 * @date 2024/4/8 0008 15:58
 * @description 大模型请求参数
 */
@Data
@Builder
public class RequestInfo {

    //模型："/workspace/H_workspace/Qwen/Qwen-14B-Chat"
    private String model;
    //消息：{"role": "user", "content": prompt}
    private List<Message> messages;
    //最大token（"max_tokens": 200,）数：200
    private int maxTokens;
    //结束符：["", ""]
    private List<String> stop;
    //温度：0.1
    private double temperature;

    // Constructors, getters, and setters

    @Data
    @Builder
    public static class Message {
        //角色："role": "user"
        private String role;
        //"content": prompt内容（你将看到一个问题和关于这个问题的已知信息。从已......）
        private String content;

    }
}
