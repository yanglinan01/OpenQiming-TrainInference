package com.ctdi.cnos.llm.beans.meta.dataSet;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ctdi.cnos.llm.base.object.BaseModel;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 数据集(DataSet)实体类
 * @author wangyb
 * @since 2024-05-15 14:00:19
 */

@Data
@ApiModel("DataSet实体类")
@TableName("meta.mm_data_set")
public class DataSet extends BaseModel {
    private static final long serialVersionUID = -43649316699698880L;
    /**
     * id
     */
    @ApiModelProperty(value = "id", example = "id")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId("id")
    private BigDecimal id;
    /**
     * 数据集名称;数据集_20240514_104036
     */
    @ApiModelProperty(value = "数据集名称", example = "数据集_20240514_104036")
    @TableField("data_set_name")
    private String dataSetName;
    /**
     * 数据集版本
     */
    @ApiModelProperty(value = "数据集版本", example = "数据集版本")
    @TableField("data_set_version")
    private String dataSetVersion;
    /**
     * 数据类型;Prompt+Response
     */
    @ApiModelProperty(value = "数据类型;Prompt+Response", example = "数据类型;(1Prompt+Response; 2时序数据; 3意图识别）")
    @TableField("data_type")
    private String dataType;
    /**
     * 对话数据量;1222
     */
    @ApiModelProperty(value = "对话数据量;1222", example = "1222")
    @TableField("pr_count")
    private Long prCount;
    /**
     * 数据集分类
     */
    @ApiModelProperty(value = "数据集分类", example = "数据集分类(1训练数据集; 2测试数据集)")
    @TableField("set_type")
    private String setType;
    /**
     * 保存类型;平台共享存储
     */
    @ApiModelProperty(value = "保存类型;平台共享存储", example = "平台共享存储")
    @TableField("save_type")
    private String saveType;
    /**
     * 保存路径;/tmp/text.json
     */
    @ApiModelProperty(value = "保存路径", example = "/tmp/text.json")
    @TableField("save_path")
    private String savePath;
    /**
     * 模板类型;json模板
     */
    @ApiModelProperty(value = "模板类型", example = "xlsx模板")
    @TableField("template_type")
    private String templateType;
    /**
     * 数据集描述
     */
    @ApiModelProperty(value = "数据集描述", example = "这是xx省客服数据集")
    @TableField("description")
    private String description;
    /**
     * 调用次数;1
     */
    @ApiModelProperty(value = "调用次数", example = "100")
    @TableField("call_count")
    private Long callCount;

    /**
     * 是否有效
     */
    @ApiModelProperty(value = "是否有效", example = "10050001")
    @TableField("is_valid")
    private String isValid;
    /**
     * 是否删除
     */
    @ApiModelProperty(value = "是否删除", example = "10050002")
    @TableField("is_delete")
    private String isDelete;

    @ApiModelProperty(value = "类别", example = "1")
    @TableField("type")
    private String type;

    @ApiModelProperty(value = "归属", example = "1")
    @TableField("belong")
    private String belong;

    @ApiModelProperty(value = "文件大小(单位：B)", example = "1")
    @TableField("file_size")
    private Long fileSize;

    @ApiModelProperty(value = "前端请求Id", example = "1")
    @TableField("request_id")
    private BigDecimal requestId;

    /**
     * 区域编码
     */
    @ApiModelProperty(value = "区域编码", example = "5000000000000000000")
    @TableField("region_code")
    private String regionCode;

    @ApiModelProperty(value = "绑定强化训练任务id", example = "1")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("enhanced_train_task_id")
    private Long enhancedTrainTaskId;

    @ApiModelProperty(value = "绑定测试集评估id", example = "1")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("test_set_evaluation_id")
    private Long testSetEvaluationId;

    @ApiModelProperty(value = "项目id")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField("project_id")
    private Long projectId;

}

