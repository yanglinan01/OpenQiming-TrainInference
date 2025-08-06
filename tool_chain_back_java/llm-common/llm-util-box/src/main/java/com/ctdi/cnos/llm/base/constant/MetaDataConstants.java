/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.base.constant;

import java.util.regex.Pattern;

/**
 * MetaDataConstants 常量
 *
 * @author huangjinhua
 * @since 2024/4/22
 */
public interface MetaDataConstants {
    /**
     * 字典状态是否禁用 0正常，1禁用
     */
    String DICT_STATUS_IS_VALID_TRUE = "0";
    String DICT_STATUS_IS_VALID_FALSE = "1";

    /**
     * prompt 类别字典编码
     */
    String PROMPT_TYPE_DICT = "PROMPT_TYPE";

    /**
     * prompt 变量标识符字典编码
     */
    String PROMPT_IDENTIFIER_DICT = "PROMPT_IDENTIFIER";

    /**
     * prompt 归属字典编码--自定义模板
     */
    String PROMPT_BELONG_DICT_SELF = "2";
    /**
     * prompt 模版框架，缺省值-1
     */
    Long MODEL_ID_DEFAULT = -1L;
    /**
     * prompt 类别，缺省值-1，用于标识自定义模版标签
     */
    String PROMPT_TYPE_DICT_SELF = "-1";

    String YES = "10050001";
    String NO = "10050002";

    /**
     * 数据集系统创建
     */
    String DATASET_BELONG_DICT_SYSTEM = "1";

    /**
     * 数据集用户创建
     */
    String DATASET_BELONG_DICT_SELF = "2";

    /**
     * 应用广场-应用类型 字典编码
     */
    String APPLICATION_TYPE_DICT = "APPLICATION_TYPE";

    /**
     * 应用广场-应用场景 字典编码
     */
    String APPLICATION_SCENE_DICT = "APPLICATION_SCENE";

    /**
     * 知识库
     */
    String KNOWLEDGE_BASE = "1";

    /**
     * 模型对话正则
     */
    Pattern MODEL_CHAT_PATTERN = Pattern.compile("<\\|im_start\\|>(.*?)<\\|im_end\\|>");

    String MY_NORM_DATETIME_MINUTE_PATTERN = "yyyy/MM/dd HH:mm";

    /**
     * 数据集评估状态--waiting 等待中
     */
    String DATA_SET_EVALUATION_STATUS_WAITING = "waiting";

    /**
     * 数据集评估状态--evaluating 评估中
     */
    String DATA_SET_EVALUATION_STATUS_EVALUATING = "evaluating";

    /**
     * 数据集评估状态--completed 评估完成
     */
    String DATA_SET_EVALUATION_STATUS_COMPLETED = "completed";

    /**
     * 数据集评估状态--failed 评估失败
     */
    String DATA_SET_EVALUATION_STATUS_FAILED = "failed";

    /**
     * 训练部署类型-训练
     */
    String DEPLOY_TYPE_TRAIN = "1";

    /**
     * 训练部署类型-测试
     */
    String DEPLOY_TYPE_TEST = "2";

    /**
     * 接口响应成功
     */
    String API_SUCCESS_RESPONSE = "Succeeded";

    /**
     * 运营中心接口响应成功
     */
    String API_SUCCESS = "0";

    /**
     * 模型训练分类字典
     */
    String MODEL_TRAIN_CLASSIFY_DICT = "MODEL_TRAIN_CLASSIFY";

    /**
     * 模型训练分类- 强化学习
     */
    String MODEL_TRAIN_CLASSIFY_LEARN = "learn";

    /**
     * 模型训练参数-特定（有模型ID）
     */
    String MODEL_TRAIN_SPECIFICAL = "specifical";

    /**
     * 模型训练参数-通用（无模型ID）
     */
    String MODEL_TRAIN_NORMAL = "normal";

    String TEMPLATE_TYPE_XLSX = "xlsx";
    String TEMPLATE_TYPE_JSONL = "jsonl";

    /**
     * 模型类型字典
     */
    String MODEL_TYPE_DICT = "MODEL_TYPE";

    /**
     * 模型归属字典
     */
    String MODEL_BELONG_DICT = "MODEL_BELONG";

    /**
     * 模型权限用途字典
     */
    String MODEL_AUTH_USAGE_DICT = "MODEL_AUTH_USAGE";


    /**
     * 模型权限用途字典-模型训练
     */
    String MODEL_AUTH_USAGE_DICT_TRAIN = "1";
    /**
     * 模型权限用途字典-模型推理
     */
    String MODEL_AUTH_USAGE_DICT_REASON = "2";

    /**
     * 工单审核中
     */
    String ORDER_REVIEW_STATUS_DICT_PROCESS="1";

    /**
     * 工单审核结束
     */
    String ORDER_REVIEW_STATUS_DICT_END="2";

    /**
     * 工单工具链权限状态	开通权限：1
     */
    String ORDER_PERMISSION_STATE_DICT_OPEN="1";

    /**
     * 工单工具链权限状态	关闭权限：2
     */
    String ORDER_PERMISSION_STATE_DICT_CLOSE="2";

    /**
     * 工单涉及系统 训推工具链
     */
    String ORDER_REFERENCE_SYSTEM_TOOL="1";

    /**
     * 工单涉及系统 智能体工具链
     */
    String ORDER_REFERENCE_SYSTEM_AGENT="2";

    /**
     * 意图识别提示词
     */
    String INTENTION_RECOGNITION_INSTRUCTION_PREFIX="角色设定：  " +
            "你是一个电信客服专家，具备丰富的行业知识和实战经验，擅长精准识别用户在与AI客服多轮对话中的业务类型和意图。" +
            "任务目标：  " +
            "根据“用户与AI客服的多轮对话内容”，**仅判断用户最后一轮发言**中所表达的**业务类型和主题意图**，并按照指定格式输出。  " +
            "判断要求：  " +
            "1. 如果用户在继续当前主题对话，输出该主题的业务类型与意图。  " +
            "2. 如果用户话题发生跳转（如从宽带变为手机问题），请识别其**新业务类型和主题**。  " +
            "3. 如果用户在闲聊（如夸客服、问天气等），请判断为“闲聊”，并输出一句合适的闲聊回复。  " +
            "4. **只允许在下列业务类型与主题中进行选择，禁止新增或编造。**" +
            "业务类型与主题清单（禁止改写或新增）：";

    String INTENTION_RECOGNITION_INSTRUCTION_SUFFIX="输出格式（必须严格遵守）：  你必须只输出如下结构的 JSON 内容：- 如果是具体业务问题，" +
            "输出：{\"type\": \"业务类型\", \"subject\": \"主题\"}" +
            "- 如果是闲聊类内容，输出：" +
            "{\"type\": \"闲聊\", \"subject\": \"你与用户的自然闲聊回应\"}" +
            "输出完 JSON 后必须**立即停止生成，不得继续回复**任何说明或解释。" +
//            "示例参考（必须学习风格和格式）：" +
//            "示例1：" +
//            "用户：我家宽带今天不能上网了，是怎么回事" +
//            "输出：" +
//            "{\"type\": \"宽带业务\", \"subject\": \"宽带不能上网\"}" +
//            "示例2：" +
//            "用户与AI客服多轮对话，最后用户说：还有啊，我的手机今天信号也很差，手机上不了网" +
//            "输出：" +
//            "```json" +
//            "{\"type\": \"手机业务\", \"subject\": \"手机上网故障\"}" +
//            "示例3：" +
//            "用户：你声音好听呀，是AI小姐姐吗？" +
//            "输出：" +
//            "{\"type\": \"闲聊\", \"subject\": \"谢谢夸奖呀，我是AI助手，有什么可以帮您？\"}" +
//            "示例4：" +
//            "用户：你们营业厅几点下班？我想去换手机卡" +
//            "输出：" +
//            "{\"type\": \"其他\", \"subject\": \"其他\"}" +
//            "示例5：" +
//            "用户：麻烦看下这个单子卡单的啥时候能下来" +
//            "输出：" +
//            "{\"type\": \"卡单\", \"subject\": \"卡单\"}" +
            "提示结束。";

}