/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.base.constant;

/**
 * 系统常量
 *
 * @author huangjinhua
 * @since 2024/8/2
 */
public interface SystemConstant {
    /**
     * 用户数据权限-管理员
     */
    String USER_ADMIN_CODE = "1";
    /**
     * 用户数据权限-区域管理员
     */
    String USER_REGION_LEADER_CODE = "2";

    /**
     * 用户数据权限-用户
     */
    String USER_ORDINARY_CODE = "3";

    /**
     * 是否-是
     */
    String YES = "0";

    /**
     * 是否-否
     */
    String NO = "1";

    /**
     * 默认用户数据过滤字段名
     */
    String DEFAULT_USER_COLUMNS = "creator_id";
    /**
     * 默认区域数据过滤字段名
     */
    String DEFAULT_REGION_COLUMNS = "region_code";
    /**
     * 默认项目数据过滤字段名
     */
    String DEFAULT_PROJECT_COLUMNS = "project_id";
    /**
     * 默认系统用户0
     */
    Long DEFAULT_SYSTEM_USERID = 0L;

    /**
     * 消息内容对象默认的角色标识值。
     * user表示发送者是用户。
     */
    String CONVERSATION_CONTENT_DEFAULT_ROLE_VALUE = "user";

    /**
     * 消息内容对象系统的角色标识值。
     * system表示提示词。
     * You are a helpful assistant. 你是电信的启明大模型，是一个专业的AI助手，你擅长回答用户的QA问题，并且你的回复无害、详细且准确。
     */
    String CONVERSATION_CONTENT_SYSTEM_ROLE_VALUE = "system";

    /**
     * 批量插入数量
     */
    int BATCH_SIZE=100;
}