package com.ctdi.cnos.llm.log.constant;

/**
 * @author caojunhao
 * @DATE 2024/7/8
 */
public enum ModelEnum {
    
    INTF_CALL_TYPE_SUCCESS(0, "成功"),
    INTF_CALL_TYPE_FAIL(1, "失败"),

    INTF_CALL_TYPE_ALL(2, "全部"),

    MODEL_CALL_TYPE_OUTPUT(2, "模型输出"),
    MODEL_CALL_TYPE_INPUT(1, "模型输入"),

    MODEL_CALL_TYPE_ALL(0, "全部"),

    

    ;


    ModelEnum(Integer code, String desc)
    {
        this.code = code;
        this.desc = desc;
    }

    Integer code;
    String desc;

    public static ModelEnum getByCode(String code)
    {
        for (ModelEnum modelEnum : ModelEnum.values())
        {
            if (modelEnum.getCode().equals(code))
            {
                return modelEnum;
            }
        }
        return null;
    }

    public Integer getCode()
    {
        return code;
    }

    public String getDesc()
    {
        return desc;
    }
}
