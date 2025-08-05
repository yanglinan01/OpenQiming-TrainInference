package com.ctdi.cnos.llm.base.constant;

/**
 * excel 头字段
 * @author wangyb
 * @date 2024/9/20 0020 14:18
 * @description SheetHeaderConstant
 */
public interface SheetHeaderConstant {

    //问答对
    String TEST_PROMPT_RESPONSE_ONE = "问题ID";
    String TEST_PROMPT_RESPONSE_TWO = "问题";
    String TEST_PROMPT_RESPONSE_THREE = "答案";
    String TRAIN_PROMPT_RESPONSE_ONE = "问题ID";
    String TRAIN_PROMPT_RESPONSE_TWO = "问答轮数";
    String TRAIN_PROMPT_RESPONSE_THREE = "问题";
    String TRAIN_PROMPT_RESPONSE_FOUR = "答案";

    //意图识别
    String TEST_PROMPT_CATEGORY_ONE = "提问角色";
    String TEST_PROMPT_CATEGORY_TWO = "常见的询问问题";
    String TEST_PROMPT_CATEGORY_THREE = "意图分类";
    String TRAIN_PROMPT_CATEGORY_ONE = "提问角色";
    String TRAIN_PROMPT_CATEGORY_TWO = "常见的询问问题";
    String TRAIN_PROMPT_CATEGORY_THREE = "意图分类";

    //时序
    String TEST_PROMPT_SEQUENTIAL_ONE = "链路编号";
    String TEST_PROMPT_SEQUENTIAL_TWO = "电路名称";
    String TEST_PROMPT_SEQUENTIAL_THREE = "A端设备编码";
    String TEST_PROMPT_SEQUENTIAL_FOUR = "A端设备编码";

}
