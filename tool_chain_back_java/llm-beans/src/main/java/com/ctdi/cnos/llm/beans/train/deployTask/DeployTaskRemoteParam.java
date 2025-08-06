/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.beans.train.deployTask;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 部署任务远程接口参数（提交、删除、状态等）
 *
 * @author huangjinhua
 * @since 2024/9/11
 */
@Getter
@Setter
@Accessors(chain = true)
public class DeployTaskRemoteParam {

    /**
     * 部署任务ID
     */
    private Long deployTaskId;
    /**
     * 提交参数
     */
    private DeployTaskSubmitParam param;
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 部署目标
     */
    private String deployTarget;

    /**
     * 接口来源
     */
    private String source;


}
