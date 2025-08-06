package com.ctdi.cnos.llm.base.constant;

/**
 * 公用常量
 *
 * @author wangyb
 * @since 2024/8/19
 */
public interface CommonConstant {
    /**
     * 问答对
     */
    String PROMPT_RESPONSE = "1";

    /**
     * 时序
     */
    String PROMPT_SEQUENTIAL = "2";

    /**
     * 意图识别
     */
    String PROMPT_CATEGORY = "3";

    /**
     * 训练数据集
     */
    String TRAIN_DATA_SET = "1";

    /**
     * 测试数据集
     */
    String TEST_DATA_SET = "2";

    /**
     * 强化学习数据集
     */
    String ENHANCE_DATA_SET = "3";

    /**
     * 最大问答对测试条数
     */
    Integer MAX_PR_DATA_POINTS = 300;

    /**
     * 最小问答对测试条数
     */
    Integer MIN_PR_DATA_POINTS = 2;

    /**
     * 时序最小测试数据条数
     */
    Integer MIN_DATA_POINTS = 1024;

}
