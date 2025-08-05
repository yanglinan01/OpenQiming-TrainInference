/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.base.constant;

import cn.hutool.core.lang.Dict;

/**
 * 训练常量
 *
 * @author huangjinhua
 * @since 2024/5/15
 */
public interface TrainConstants {


    /**
     * 训练任务状态字典类型
     */
    String TRAIN_TASK_STATUS_DICT = "TRAIN_TASK_STATUS";
    /**
     * 训练任务方法字典类型
     */
    String TRAIN_TASK_METHOD_DICT = "TRAIN_TASK_METHOD";

    /**
     * 模型评估C-eval状态字典类型
     */
    String MODEL_C_EVAL_STATUS_DICT = "MODEL_C_EVAL_STATUS";

    /**
     * 训练任务方法字典类型 - 全参
     */
    String TRAIN_TASK_METHOD_DICT_FULL = "1";

    /**
     * 训练任务状态--waiting 排队中
     */
    String TRAIN_TASK_STATUS_WAITING = "waiting";

    /**
     * 训练任务状态--training 训练中
     */
    String TRAIN_TASK_STATUS_TRAINING = "training";

    /**
     * 训练任务状态--completed 训练完成
     */
    String TRAIN_TASK_STATUS_COMPLETED = "completed";

    /**
     * 训练任务状态--failed 训练失败
     */
    String TRAIN_TASK_STATUS_FAILED = "failed";

    /**
     * 部署任务状态字典类型
     */
    String DEPLOY_TASK_STATUS_DICT = "DEPLOY_TASK_STATUS";
    /**
     * 训推目标
     */
    String TRAIN_ = "MODEL_TRAIN_CLASSIFY";

    /**
     * 部署任务状态--deploying 部署中
     */
    String DEPLOY_TASK_STATUS_WAITING = "waiting";

    /**
     * 部署任务状态--deploying 部署中
     */
    String DEPLOY_TASK_STATUS_DEPLOYING = "deploying";

    /**
     * 部署任务状态--completed 部署完成
     */
    String DEPLOY_TASK_STATUS_COMPLETED = "completed";

    /**
     * 部署任务状态--failed 部署失败
     */
    String DEPLOY_TASK_STATUS_FAILED = "failed";

    /**
     * 部署任务状态--removed 系统移除
     */
    String DEPLOY_TASK_STATUS_REMOVED = "removed";

    /**
     * 评估任务-C-eval状态--evaluating 评估中
     */
    String TRAIN_TASK_EVAL_STATUS_EVALUATING = "evaluating";

    /**
     * 评估任务-C-eval状态--completed 评估完成
     */
    String TRAIN_TASK_EVAL_STATUS_COMPLETED = "completed";

    /**
     * 评估任务-C-eval状态--failed 评估失败
     */
    String TRAIN_TASK_EVAL_STATUS_FAILED = "failed";

    /**
     * 评估任务-C-eval状态--error 评估出错
     */
    String TRAIN_TASK_EVAL_STATUS_ERROR = "error";

    /**
     * 部署任务状态--deploying 部署中
     */
    String DEPLOY_TASK_STATUS_WAITING_REMARK = "请等待, 正在排队";

    /**
     * 部署任务状态--deploying 部署中
     */
    String DEPLOY_TASK_STATUS_DEPLOYING_REMARK = "请等待, 正在进行模型部署";

    /**
     * 部署任务状态--completed 部署完成
     */
    String DEPLOY_TASK_STATUS_COMPLETED_REMARK = "已完成部署";

    /**
     * 部署任务状态--failed 部署失败
     */
    String DEPLOY_TASK_STATUS_FAILED_REMARK = "部署失败: ";

    /**
     * 部署任务状态--removed 系统移除
     */
    String DEPLOY_TASK_STATUS_REMOVED_REMARK = "未用，被系统移除, 请重新部署";

    /**
     * 省份父Id
     */
    Integer PROVINCE_PARENT_ID = 100000;

    /**
     * 删除
     */
    String DELETE = "-1";

    /**
     * 启用
     */
    String ENABLE = "1";

    /**
     * 禁用
     */
    String DISABLE = "0";

    /**
     * 单个人总训练任务限制个数。
     * 注意：不包含失败的。
     */
    Long TRAIN_TASK_SINGLE_COUNT_LIMIT = 5L;

    /**
     * 大模型
     */
    Integer LLM = 1;

    /**
     * 知识助手
     */
    Integer ASSISTANT = 2;

    /**
     * 自建模型
     */
    Integer SELF = 3;

    /**
     * 是
     */
    String YES = "1";

    /**
     * 否
     */
    String NO = "0";


    /**
     * 任务推送成功
     */
    String SUCCESS_SUBMIT = "Succeeded";

    /**
     * 意图识别任务推送成功
     */
    String IR_SUCCESS_SUBMIT = "success";

    /**
     * 意图识别任务推送成功
     */
    String HTTP_SUCCESS_SUBMIT = "success";

    /**
     * 训练部署类型-训练
     */
    String DEPLOY_TYPE_TRAIN = "1";

    /**
     * 训练部署类型-测试
     */
    String DEPLOY_TYPE_TEST = "2";

    /**
     * 训练任务状态
     * Completed、Running、Pending、Failed、Error
     */
    Dict INTERFACE_TRAIN_STATUS_MAP = Dict.create()
            .set("Pending", TRAIN_TASK_STATUS_WAITING)
            .set("Running", TRAIN_TASK_STATUS_TRAINING)
            .set("Completed", TRAIN_TASK_STATUS_COMPLETED)
            .set("Failed", TRAIN_TASK_STATUS_FAILED)
            .set("Error", TRAIN_TASK_STATUS_FAILED);


    /**
     * 部署任务状态
     * running时即可问答
     * Pending、Running、Succeeded、Failed、Unknown、Error
     */
    Dict INTERFACE_DEPLOY_STATUS_MAP = Dict.create()
            .set("Pending", DEPLOY_TASK_STATUS_WAITING)
            .set("Running", DEPLOY_TASK_STATUS_COMPLETED)
            .set("Completed", DEPLOY_TASK_STATUS_FAILED)
            .set("Succeeded", DEPLOY_TASK_STATUS_FAILED)
            .set("Failed", DEPLOY_TASK_STATUS_FAILED)
            .set("Unknown", DEPLOY_TASK_STATUS_FAILED)
            .set("Error", DEPLOY_TASK_STATUS_FAILED);


    String SET_TYPE_TRAIN = "1";
    String SET_TYPE_TEST = "2";
    String POST = "post";
    String TOOL_CHAIN = "toolChain";

    /**
     * 部署任务所属 1：工具链
     */
    String DEPLOY_BELONG_TOOL="1";

    /**
     * 部署任务所属 2：项目空间
     */
    String DEPLOY_BELONG_PROJECT_SPACE="2";

    /**
     * 训练任务类型：意图识别
     */
    String MODEL_TRAIN_TYPE_IR="IR";

    /**
     * 训练任务类型：lora
     */
    String MODEL_TRAIN_TYPE_LORA="LORA";

    /**
     * 训练任务类型：sft
     */
    String MODEL_TRAIN_TYPE_SFT="SFT";

    /**
     * 训练任务类型：RL-LOAD
     */
    String MODEL_TRAIN_TYPE_RL_LORA="RL-LORA";

    /**
     * 训练任务类型：QLORA
     */
    String MODEL_TRAIN_TYPE_QLORA="QLORA";

    /**
     * 训练任务类型：任务
     */
    String TASK_OR_GROUP_TASK = "task";

    /**
     * 训练任务类型：任务组
     */
    String TASK_OR_GROUP_GROUP = "group";
}