/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.beans.train.trainTask;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 训练任务超参 --- 对象要跟 ModelTrainParam 的属性名一致
 *
 * @author huangjinhua
 * @since 2024/5/15
 */
@Data
@ApiModel("训练任务VO")
public class TrainTaskParamVO implements Serializable {
    @ApiModelProperty(value = "前端显示名称", example = "迭代轮次")
    private String displayName;
    @ApiModelProperty(value = "后端使用的键名", example = "custom_epoch")
    private String beKey;
    @ApiModelProperty(value = "前端使用的键名", example = "epoch")
    private String feKey;
    @ApiModelProperty(value = "参数类型", example = "int")
    private String type;
    @ApiModelProperty(value = "默认值", example = "1")
    private Object defaultValue;
}
