package com.ctdi.cnos.llm.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReflectUtil;
import com.ctdi.cnos.llm.base.object.BaseModel;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.exception.DataValidationException;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * 负责Model数据操作、类型转换和关系关联等行为的工具类。。
 *
 * @author laiqi
 * @since 2024/7/15
 */
@Slf4j
public class ModelUtil {

    private static final Validator VALIDATOR;

    static {
        VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
    }

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.##");

    /**
     * 字节转GB
     *
     * @param bytes 字节
     * @return GB格式的字符串。
     * @see cn.hutool.core.io.unit.DataSizeUtil#format(long)
     */
    public static String bytesToGB(long bytes) {
        return DECIMAL_FORMAT.format(bytes / Math.pow(1024, 3));
    }

    /**
     * 字节转GB
     *
     * @param bytes 字节
     * @return GB格式的字符串。
     * @see cn.hutool.core.io.unit.DataSizeUtil#format(long)
     */
    public static double bytesToGB(double bytes, int scale) {
        return NumberUtil.round(bytes / Math.pow(1024, 3), scale).doubleValue();
    }

    /**
     * 拷贝源类型的对象数据到目标类型的对象中，其中源类型和目标类型中的对象字段类型完全相同。
     * NOTE: 该函数主要应用于框架中，Dto和Model之间的copy，特别针对一对一关联的深度copy。
     * 在Dto中，一对一对象可以使用Map来表示，而不需要使用从表对象的Dto。
     *
     * @param source      源类型对象。
     * @param targetClazz 目标类型的Class对象。
     * @param <S>         源类型。
     * @param <T>         目标类型。
     * @return copy后的目标类型对象。
     */
    public static <S, T> T copyTo(S source, Class<T> targetClazz) {
        if (source == null) {
            return null;
        }
        try {
            T target = targetClazz.newInstance();
            BeanUtil.copyProperties(source, target);
            return target;
        } catch (Exception e) {
            log.error("Failed to call MyModelUtil.copyTo", e);
            return null;
        }
    }

    /**
     * 拷贝源类型的集合数据到目标类型的集合中，其中源类型和目标类型中的对象字段类型完全相同。
     * NOTE: 该函数主要应用于框架中，Dto和Model之间的copy，特别针对一对一关联的深度copy。
     * 在Dto中，一对一对象可以使用Map来表示，而不需要使用从表对象的Dto。
     *
     * @param sourceCollection 源类型集合。
     * @param targetClazz      目标类型的Class对象。
     * @param <S>              源类型。
     * @param <T>              目标类型。
     * @return copy后的目标类型对象集合。
     */
    public static <S, T> List<T> copyCollectionTo(Collection<S> sourceCollection, Class<T> targetClazz) {
        List<T> targetList = null;
        if (sourceCollection == null) {
            return targetList;
        }
        targetList = new LinkedList<>();
        if (CollUtil.isNotEmpty(sourceCollection)) {
            for (S source : sourceCollection) {
                try {
                    T target = targetClazz.newInstance();
                    BeanUtil.copyProperties(source, target);
                    targetList.add(target);
                } catch (Exception e) {
                    log.error("Failed to call MyModelUtil.copyCollectionTo", e);
                    return Collections.emptyList();
                }
            }
        }
        return targetList;
    }

    /**
     * 在插入实体对象数据之前，可以调用该方法，初始化通用字段的数据。
     *
     * @param data 实体对象。
     * @param <M>  实体对象类型。
     */
    public static <M> void fillCommonsForInsert(M data) {
        Field createdByField = ReflectUtil.getField(data.getClass(), BaseModel.CREATE_USER_ID_FIELD_NAME);
        Long userId = UserContextHolder.getUserId();
        if (createdByField != null) {
            ReflectUtil.setFieldValue(data, createdByField, userId);
        }
        Field createTimeField = ReflectUtil.getField(data.getClass(), BaseModel.CREATE_TIME_FIELD_NAME);
        if (createTimeField != null) {
            ReflectUtil.setFieldValue(data, createTimeField, new Date());
        }
        Field updatedByField = ReflectUtil.getField(data.getClass(), BaseModel.UPDATE_USER_ID_FIELD_NAME);
        if (updatedByField != null) {
            ReflectUtil.setFieldValue(data, updatedByField, userId);
        }
        Field updateTimeField = ReflectUtil.getField(data.getClass(), BaseModel.UPDATE_TIME_FIELD_NAME);
        if (updateTimeField != null) {
            ReflectUtil.setFieldValue(data, updateTimeField, new Date());
        }
    }

    /**
     * 在更新实体对象数据之前，可以调用该方法，更新通用字段的数据。
     *
     * @param data 实体对象。
     * @param <M>  实体对象类型。
     */
    public static <M> void fillCommonsForUpdate(M data) {
        Field updatedByField = ReflectUtil.getField(data.getClass(), BaseModel.UPDATE_USER_ID_FIELD_NAME);
        if (updatedByField != null) {
            ReflectUtil.setFieldValue(data, updatedByField, UserContextHolder.getUserId());
        }
        Field updateTimeField = ReflectUtil.getField(data.getClass(), BaseModel.UPDATE_TIME_FIELD_NAME);
        if (updateTimeField != null) {
            ReflectUtil.setFieldValue(data, updateTimeField, new Date());
        }
    }

    /**
     * 判断模型对象是否通过校验，没有通过返回具体的校验错误信息。
     *
     * @param model  待校验的模型对象。
     * @param groups Validate绑定的校验组。
     * @return 没有错误返回null，否则返回具体的错误信息。
     */
    public static <T> String getModelValidationError(T model, Class<?>... groups) {
        if (model != null) {
            Set<ConstraintViolation<T>> constraintViolations = VALIDATOR.validate(model, groups);
            if (!constraintViolations.isEmpty()) {
                Iterator<ConstraintViolation<T>> it = constraintViolations.iterator();
                ConstraintViolation<T> constraint = it.next();
                return constraint.getMessage();
            }
        }
        return null;
    }


    /**
     * 校验模型对象并抛出异常。
     *
     * @param model  待校验的模型对象。
     * @param groups Validate绑定的校验组。
     * @throws DataValidationException 如果模型对象未通过校验，则抛出此异常，包含具体的错误信息。
     */
    public static <T> void validateModelAndThrow(T model, Class<?>... groups) {
        String errorMessage = getModelValidationError(model, groups);
        if (errorMessage != null) {
            throw new DataValidationException(errorMessage);
        }
    }
}