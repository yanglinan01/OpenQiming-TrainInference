/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * 用户配置
 *
 * @author huangjinhua
 * @since 2024/7/10
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "user")
public class UserConfig {

    /**
     * 用户信息获取地址
     */
    private String loginUrl;

    /**
     * 是否启用
     */
    private boolean enable = true;

    /**
     * 数据权限配置项
     */
    private DataPermission dataPermission;


    /**
     * 是否启用登录更新用户信息
     */
    private boolean loginUpdate = true;

    /**
     * 是否启用使用用户ID做为token
     */
    private boolean idToToken = true;

    /**
     * excel批量导入用户列头对应关系（{"excel列名":"user对象属性"}）
     * {"人力账号":"employeeNumber"}
     */
    private Map<String,String> userExcelHeader;


    /**
     * 数据权限配置。
     */
    @Setter
    @Getter
    static class DataPermission {
        /**
         * 是否启用数据权限。
         */
        private boolean enable = false;

        /**
         * 是否启用数据权限的重写逻辑。
         */
        private boolean override = false;
    }
}