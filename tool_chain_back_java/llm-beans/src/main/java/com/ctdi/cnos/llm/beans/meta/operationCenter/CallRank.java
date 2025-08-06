/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.beans.meta.operationCenter;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 能力调用榜单
 *
 * @author huangjinhua
 * @since 2024/10/17
 */
@Getter
@Setter
@Accessors(chain = true)
public class CallRank {

    /**
     * 排名
     */
    private Integer rank;
    /**
     * 模型名称、应用名称
     */
    private String name;

    /**
     * 省份
     */
    private String province;

    /**
     * 省份名称
     */
    private String provinceName;

    /**
     * 公司名称
     */
    private String company;

    /**
     * 调用量
     */
    private Long callCount;

    /**
     * 用户
     */
    private String developer;
}
