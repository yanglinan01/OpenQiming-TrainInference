package com.ctdi.cnos.llm.beans.meta.metric;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 集群资源查询条件参数。
 *
 * @author huangjinhua
 * @since 2024/9/23
 */
@ApiModel(description = "集群资源查询条件参数")
@Data
@Accessors(chain = true)
public class ClusterStatParam {

    /**
     * 集群编码
     */
    private String clusterCode;

    /**
     * 任务ID（训练ID/部署ID）
     */
    private Long taskId;

    /**
     * 任务类型（train:训练； interface:部署）
     * type = train 时 taskId = mm_train_task.id,
     * type = interface 时 taskId = mm_deploy_task.id
     */
    private String taskType;

    /**
     * 集群使用趋势 时长
     */
    @ApiModelProperty(value = "使用趋势时长", required = true, example = "1")
    private Integer usageTrendDuration;
}