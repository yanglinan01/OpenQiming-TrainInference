package com.ctdi.cnos.llm.log.constant;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author caojunhao
 * @DATE 2024/7/8
 */
public class ModelConstants {
    public static final String MODEL_NAME = "modelName";


    /**
     * 缓存key
     */
    public static final String MODEL_MONITOR_MODEL_CACHE_KEY = "modelMonitorModelCacheKey";
    public static final String MODEL_MONITOR_INTF_CACHE_KEY = "modelMonitorIntfCacheKey";
    public static final String MODEL_NAME_CACHE_KEY = "modelMonitor";

    public static final String TRAIN_TASK_CACHE_KEY = "trainTaskCacheKey";

    /**
     * 时间后缀
     */
    public static final String TIME_SUFFIX = " 00:00:00";
    public static final String TIME_SUFFIX_END = " 23:59:59";

    public static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static DateTimeFormatter localDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-M-d HH:mm:ss");

    public static DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
}
