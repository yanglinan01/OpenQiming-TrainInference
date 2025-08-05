package com.ctdi.llmtc.xtp.traininfer.constant;

public enum StatusEnum {
    DATASET_NOT_EXIST("数据集不存在"),
    YML_GENERATE_FAIL("YAML文件创建失败"),
    CREATE_VS_FAIL("虚拟服务创建失败"),
    DEL_VS_FAIL("虚拟服务删除失败"),
    DEL_INFER_RES_FAIL("删除推理集群资源失败"),
    INFER_YML_GENERATE_FAIL("推理YAML文件提交失败"),
    TASK_SUBMIT_FAIL("任务提交失败"),
    IR_FILE_NOT_EXIST("意图训练结果文件不存在");

    StatusEnum(String msg) {
        this.msg = msg;
    };

    /**
     * 消息名
     */
    private final String msg;

    public String getMsg() {
        return msg;
    }

}
