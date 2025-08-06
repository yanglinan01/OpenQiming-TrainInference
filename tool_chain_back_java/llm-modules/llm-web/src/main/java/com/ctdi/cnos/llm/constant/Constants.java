package com.ctdi.cnos.llm.constant;

import java.util.regex.Pattern;

public class Constants {

    public static final int CACHE_EXPIRE_TOKEN_CACHE = 3600;

    public static final String AUTH_HEADER_KEY = "Authorization";

    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 知识库同步文件
     */
    public static final String KNOWLEDGE_BASE = "1";

    /**
     * 用户上传文件
     */
    public static final String SELF_UP = "2";

    /**
     * 模型对话结束标识
     */
    public static final String MODEL_CHAT_END = "<#END>";

    /**
     * 模型对话正则
     */
    public static final Pattern MODEL_CHAT_PATTERN = Pattern.compile("<\\|im_start\\|>(.*?)<\\|im_end\\|>");


    public static final String MY_NORM_DATETIME_MINUTE_PATTERN = "yyyy/MM/dd HH:mm";

    /**
     * 部署类型为训练
     */
    public static final String DEPLOY_TYPE_TRAIN = "1";

}