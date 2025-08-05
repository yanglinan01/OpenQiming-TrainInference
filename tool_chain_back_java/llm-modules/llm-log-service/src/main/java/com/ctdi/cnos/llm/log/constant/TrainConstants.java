package com.ctdi.cnos.llm.log.constant;

/**
 * @author caojunhao
 * @DATE 2024/7/11
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

}
