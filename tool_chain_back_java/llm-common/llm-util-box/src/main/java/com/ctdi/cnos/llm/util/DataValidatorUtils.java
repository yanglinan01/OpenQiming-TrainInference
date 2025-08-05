package com.ctdi.cnos.llm.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * <b>请输入名称</b>
 * <p>
 * 描述<br/>
 * 作用：；<br/>
 * 限制：；<br/>
 * </p>
 *
 * @author wan.liang(79274)
 * @date 2025/6/6 15:02
 */
public class DataValidatorUtils {


    /**
     * 确保列值在数据库中有效
     * 该方法用于检查给定的值是否在指定的数据库列中存在如果不存在，将抛出异常
     *
     * @param valueToCheck 需要检查的值
     * @param columnExtractor 列提取器，用于指定要检查的列
     * @param queryWrapperExtractor 查询执行器，用于执行数据库查询
     * @param errorMessage 错误消息格式字符串，当检查失败时使用
     * @param <T> 实体类类型
     * @param <R> 列值类型
     * @param <V> 要检查的值的类型
     */
    public static <T, R, V> void ensureColumnValueValid(V valueToCheck,
                                                        SFunction<T, R> columnExtractor,
                                                        SFunction<LambdaQueryWrapper<T>, T> queryWrapperExtractor,
                                                        String errorMessage) {
        if (valueToCheck != null) {
            LambdaQueryWrapper<T> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.select(columnExtractor);
            queryWrapper.eq(columnExtractor, valueToCheck);
            queryWrapper.last("limit 1");

            T entity = queryWrapperExtractor.apply(queryWrapper);
            if (entity != null ) {
                throw new IllegalArgumentException(errorMessage);
            }
        }
    }

}
