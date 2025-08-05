package com.ctdi.cnos.llm.beans.train.trainTask;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xuwj09
 * @version 1.0
 * @date 2025/6/10 17:25
 * @Description
 */
@ApiModel(description = "TrainDatasetJoinVo对象")
@Data
public class TrainDatasetJoinVO {
    @ApiModelProperty(value = "ID，修改时必填!", example = "1775389563611901951")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "训练任务id", example = "1775389563611901951")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long taskId;

    @ApiModelProperty(value = "数据集id", example = "1775389563611901951")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long dataSetId;
}
