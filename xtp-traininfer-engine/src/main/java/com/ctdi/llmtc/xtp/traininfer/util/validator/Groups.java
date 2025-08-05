package com.ctdi.llmtc.xtp.traininfer.util.validator;

public interface Groups {

    /**
     * 普通训练
     */
    interface TRAIN {
    }

    /**
     * 强化训练
     */
    interface DPO {
    }

    /**
     * 评估
     */
    interface EVAL {
    }

    /**
     * 推理
     */
    interface INFERENCE {
    }

}
