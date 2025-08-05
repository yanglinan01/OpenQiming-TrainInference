package com.ctdi.cnos.llm.beans.meta.evaluation;

import cn.hutool.core.util.StrUtil;
import com.ctdi.cnos.llm.base.constant.ActionType;
import com.ctdi.cnos.llm.base.constant.EvaluationConstant;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 问答对测试数据集评估详情 Vo对象。
 *
 * @author laiqi
 * @since 2024/09/04
 */
@FieldNameConstants
@ApiModel(description = "PrTestSetEvaluationDetailVo对象")
@Data
public class PrTestSetEvaluationDetailVO {

    /**
     * 普通的表头别名
     */
    public static final Map<String, String> NORMAL_HEADER_ALIAS = new LinkedHashMap<>(6);

    /**
     * 强化的表头别名
     */
    public static final Map<String, String> ENHANCED_HEADER_ALIAS = new LinkedHashMap<>(6);

    /**
     * 普通的状态字典
     */
    public static final Map<String, String> NORMAL_STATUS_DICT_MAP = new LinkedHashMap<>(2);

    /**
     * 强化的状态字典
     */
    public static final Map<String, String> ENHANCED_STATUS_DICT_MAP = new LinkedHashMap<>(2);

    static {
        NORMAL_HEADER_ALIAS.put(Fields.index, "序号");
        NORMAL_HEADER_ALIAS.put(Fields.id, "ID");
        NORMAL_HEADER_ALIAS.put(Fields.problem, "用户问题");
        NORMAL_HEADER_ALIAS.put(Fields.bigModelAnswer, "大模型答案");
        NORMAL_HEADER_ALIAS.put(Fields.standardAnswer, "标准答案");
        NORMAL_HEADER_ALIAS.put(Fields.userFeedbackName, "用户反馈");

        ENHANCED_HEADER_ALIAS.put(Fields.index, "序号");
        ENHANCED_HEADER_ALIAS.put(Fields.id, "ID");
        ENHANCED_HEADER_ALIAS.put(Fields.problem, "用户问题");
        ENHANCED_HEADER_ALIAS.put(Fields.bigModelAnswer, "回答1");
        ENHANCED_HEADER_ALIAS.put(Fields.bigModelAnswer2, "回答2");
        ENHANCED_HEADER_ALIAS.put(Fields.userFeedbackName, "选择回答");

        NORMAL_STATUS_DICT_MAP.put(ActionType.LIKE.getValue() + "", "赞");
        NORMAL_STATUS_DICT_MAP.put(ActionType.DISLIKE.getValue() + "", "踩");

        ENHANCED_STATUS_DICT_MAP.put(ActionType.Q1.getValue() + "", "回答1");
        ENHANCED_STATUS_DICT_MAP.put(ActionType.Q2.getValue() + "", "回答2");
    }

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 序号
     */
    @ApiModelProperty(value = "序号")
    private Integer index;

    /**
     * 用户问题
     */
    @ApiModelProperty(value = "用户问题")
    private String problem;

    /**
     * 大模型答案1(推理答案1)
     */
    @ApiModelProperty(value = "大模型答案(推理答案)")
    private String bigModelAnswer;

    /**
     * 大模型答案1(推理答案2)
     */
    @ApiModelProperty(value = "大模型答案(推理答案)")
    private String bigModelAnswer2;

    /**
     * 标准答案
     */
    @ApiModelProperty(value = "标准答案")
    private String standardAnswer;

    /**
     * 用户反馈
     */
    @ApiModelProperty(value = "用户反馈")
    private String userFeedback;

    /**
     * 用户反馈
     */
    @ApiModelProperty(value = "用户反馈")
    private String userFeedbackName;

    @ApiModelProperty(value = "评估类型(0强化; 1普通)")
    private String type;

    /**
     * 将用户反馈转换为字典
     *
     * @param evaluationDetailVO 用户反馈
     * @return 转换值
     */
    public static String convertStatus(PrTestSetEvaluationDetailVO evaluationDetailVO) {
        String userFeedback = evaluationDetailVO.getUserFeedback();
        String type = evaluationDetailVO.getType();
        if (StrUtil.isEmptyIfStr(userFeedback)) {
            return null;
        }
        return (EvaluationConstant.TEST_SET_EVALUATION_TYPE_LEAN.equals(type) ? ENHANCED_STATUS_DICT_MAP : NORMAL_STATUS_DICT_MAP).getOrDefault(userFeedback, "未知");
    }

}