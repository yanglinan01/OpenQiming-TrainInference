/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.beans.train.deployTask;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.LongCodec;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 部署任务提交参数
 *
 * @author huangjinhua
 * @since 2024/9/11
 */
@Getter
@Setter
@Accessors(chain = true)
public class DeployTaskSubmitParam {
    /**
     * 部署任务ID
     */
    @JSONField(name = "task_id", serializeUsing = ToStringSerializer.class, deserializeUsing = LongCodec.class)
    private Long taskId;

    /**
     * 模型名称，在使用全参微调模型时填入全参微调训练id，在使用lora、qlora的微调模型或直接推理基模时作为基模参数
     */
    @JSONField(name = "model_name")
    private String modelName;

    /**
     * 是否合并训练过的lora进行推理
     */
    @JSONField(name = "use_lora")
    private boolean useLora;


    /**
     * 要使用的已经训练好的lora（qlora视同lora）对应的训练任务id，在use_lora为true时生效
     */
    @JSONField(name = "lora_task_id", serializeUsing = ToStringSerializer.class, deserializeUsing = LongCodec.class)
    private Long loraTaskId;

    /**
     * 是否量化
     */
    @JSONField(name = "quantization")
    private boolean quantization;
    /**
     * 全参使用的模型
     */
    @JSONField(name = "model_template")
    private String modelTemplate;


}
