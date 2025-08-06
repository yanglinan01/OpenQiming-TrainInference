/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.metadata.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ctdi.cnos.llm.beans.meta.model.ModelTrainParam;
import com.ctdi.cnos.llm.beans.meta.model.ModelTrainParamVO;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.metadata.dao.ModelTrainDao;
import com.ctdi.cnos.llm.metadata.dao.ModelTrainParamDao;
import com.ctdi.cnos.llm.metadata.service.ModelTrainParamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 模型训练超参 业务实现
 *
 * @author huangjinhua
 * @since 2024/5/14
 */

@Service
@RequiredArgsConstructor
public class ModelTrainParamServiceImpl implements ModelTrainParamService {

    private final ModelTrainParamDao modelTrainParamDao;
    private final ModelTrainDao modelTrainDao;

    @Override
    public List<ModelTrainParamVO> queryList(ModelTrainParamVO paramVO) {
        List<ModelTrainParamVO> modelTrainParams = modelTrainParamDao.queryList(paramVO);
        if (CollUtil.isNotEmpty(modelTrainParams)) {
            modelTrainParams.forEach(item -> {
                if (CharSequenceUtil.isNotBlank(item.getCheckValueStr())) {
                    item.setCheckValue(JSON.parseArray(item.getCheckValueStr()));
                }
                item.setDefaultValue(this.convertToObject(item.getType(), item.getDefaultValueStr()));
            });
        }

        return modelTrainParams;
    }

    @Override
    public ModelTrainParamVO queryById(Long id) {
        return modelTrainParamDao.queryById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(ModelTrainParam param) {
        this.handleInsertData(param);
        return modelTrainParamDao.insert(param);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertBatch(List<ModelTrainParam> list) {
        if (CollUtil.isNotEmpty(list)) {
            list.forEach(this::handleInsertData);
        }
        return modelTrainParamDao.insertBatch(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateById(ModelTrainParam param) {
        Long userId = UserContextHolder.getUser().getId();
        if (Objects.isNull(param.getModifierId())) {
            param.setModifierId(userId);
        }
        if (CharSequenceUtil.isNotBlank(param.getType())) {
            JSONArray checkValueJson = param.getCheckValue();
            if (CollUtil.isNotEmpty(checkValueJson)) {
                param.setCheckValueStr(JSON.toJSONString(checkValueJson));
            }
            Object defaultValueObj = param.getDefaultValue();
            if (Objects.nonNull(defaultValueObj)) {
                param.setDefaultValueStr(this.convertToStr(param.getType(), defaultValueObj));
            }
        }
        param.setModifyDate(DateUtil.date());
        return modelTrainParamDao.updateById(param);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return modelTrainParamDao.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByModelId(Long modelId) {
        return modelTrainParamDao.deleteByModelId(modelId);
    }

    /**
     * 处理新增数据
     *
     * @param param 超参
     * @return ModelTrainParam
     */
    private ModelTrainParam handleInsertData(ModelTrainParam param) {
        Long userId = UserContextHolder.getUser().getId();
        if (Objects.isNull(param.getId())) {
            param.setId(IdUtil.getSnowflakeNextId());
        }
        if (CharSequenceUtil.isNotBlank(param.getType())) {
            JSONArray checkValueJson = param.getCheckValue();
            if (CollUtil.isNotEmpty(checkValueJson)) {
                param.setCheckValueStr(JSON.toJSONString(checkValueJson));
            }
            Object defaultValueObj = param.getDefaultValue();
            if (Objects.nonNull(defaultValueObj)) {
                param.setDefaultValueStr(this.convertToStr(param.getType(), defaultValueObj));
            }
        }
        if (Objects.nonNull(userId)) {
            param.setCreatorId(userId);
            param.setModifierId(userId);
        }
        param.setCreateDate(DateUtil.date());
        param.setModifyDate(DateUtil.date());
        return param;
    }

    private String convertToStr(String type, Object value) {
        if (Objects.nonNull(value)) {
            String valueStr = Convert.toStr(value);
            switch (type) {
                case "int":
                case "float":
                    return Convert.toBigDecimal(value).toPlainString();
                case "boolean":
                    return valueStr.toLowerCase();
                default:
                    return valueStr;
            }
        }
        return null;
    }

    private Object convertToObject(String type, Object value) {
        if (Objects.nonNull(value)) {
            switch (type) {
                case "int":
                    return Convert.toInt(value);
                case "double":
                    return Convert.toDouble(value);
                case "float":
                    return Convert.toBigDecimal(value);
                case "boolean":
                    return Convert.toBool(value);
                case "string":
                    return Convert.toStr(value);
                default:
                    return value;
            }
        }
        return null;
    }

}
