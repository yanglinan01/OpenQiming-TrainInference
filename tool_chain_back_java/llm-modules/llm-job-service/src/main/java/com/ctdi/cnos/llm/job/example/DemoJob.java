/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.job.example;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 样例job
 *
 * @author huangjinhua
 * @since 2024/6/4
 */
@Component("demoJob")
@Slf4j
public class DemoJob {
    /**
     * 多参数方法
     *
     * @param s 参数
     * @param b 参数
     * @param l 参数 参数要携带L结尾
     * @param d 参数 参数要携带D结尾
     * @param i 参数
     */
    public void multipleParams(String s, Boolean b, Long l, Double d, Integer i) {
        log.info("{}执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", DateUtil.date(), s, b, l, d, i);
    }

    /**
     * 有参数方法
     *
     * @param params 参数
     */
    public void params(String params) {
        log.info("{}执行有参方法：{}", DateUtil.date(), params);
    }

    /**
     * 无参参数方法
     */
    public void noParams() {
        log.info("{}执行无参方法", DateUtil.date());
    }
}
