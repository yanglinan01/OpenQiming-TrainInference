/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.metadata.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.base.constant.MetaDataConstants;
import com.ctdi.cnos.llm.beans.meta.model.Model;
import com.ctdi.cnos.llm.beans.meta.model.ModelVO;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.metadata.dao.ModelDao;
import com.ctdi.cnos.llm.metadata.dao.ModelTrainDao;
import com.ctdi.cnos.llm.metadata.dao.ModelTrainParamDao;
import com.ctdi.cnos.llm.metadata.service.DictionaryService;
import com.ctdi.cnos.llm.metadata.service.ModelService;
import com.ctdi.cnos.llm.system.province.serivce.ProvinceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 基础模型 业务实现
 *
 * @author huangjinhua
 * @since 2024/5/14
 */

@Service
@RequiredArgsConstructor
public class ModelServiceImpl implements ModelService {

    private final ModelDao modelDao;
    private final ModelTrainParamDao modelTrainParamDao;
    private final ModelTrainDao modelTrainDao;
    private final DictionaryService dictionaryService;
    private final ProvinceService provinceService;

    @Override
    public Page<ModelVO> queryPage(Page<ModelVO> page, ModelVO modelVO) {
        modelDao.queryList(page, modelVO);
        this.translate(ArrayUtil.toArray(page.getRecords(), ModelVO.class));
        return page;
    }

    @Override
    public List<ModelVO> queryList(ModelVO modelVO) {
        List<ModelVO> list = modelDao.queryList(modelVO);
        if (CollUtil.isNotEmpty(list)) {
            this.translate(ArrayUtil.toArray(list, ModelVO.class));
        }
        return list;
    }

    @Override
    public ModelVO queryById(Long id) {
        ModelVO modelVO = modelDao.queryById(id);
        this.translate(modelVO);
        return modelVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(Model model) {
        Long userId = UserContextHolder.getUser().getId();
        if (Objects.isNull(model.getId())) {
            model.setId(IdUtil.getSnowflakeNextId());
        }
        if (Objects.isNull(model.getCreatorId())) {
            model.setCreatorId(userId);
        }
        if (Objects.isNull(model.getCreateDate())) {
            model.setCreateDate(DateUtil.date());
        }
        if (Objects.isNull(model.getModifierId())) {
            model.setModifierId(userId);
        }
        if (Objects.isNull(model.getModifyDate())) {
            model.setModifyDate(DateUtil.date());
        }
        return modelDao.insert(model);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateById(Model model) {
        Long userId = UserContextHolder.getUser().getId();
        if (Objects.isNull(model.getModifierId())) {
            model.setModifierId(userId);
        }
        model.setModifyDate(DateUtil.date());
        return modelDao.updateById(model);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        modelTrainParamDao.deleteByModelId(id);
        modelTrainDao.deleteByModelId(id);
        return modelDao.deleteById(id);
    }

    private void translate(ModelVO... models) {
        Map<String, String> modelType = dictionaryService.getDictItemMap(MetaDataConstants.MODEL_TYPE_DICT);
        Map<String, String> modelBelong = dictionaryService.getDictItemMap(MetaDataConstants.MODEL_BELONG_DICT);
        for (ModelVO vo : models) {
            if (vo != null) {
                if (CharSequenceUtil.isNotBlank(vo.getType())) {
                    vo.setTypeName(modelType.get(vo.getType()));
                }
                if (CharSequenceUtil.isNotBlank(vo.getBelong())) {
                    vo.setBelongName(modelBelong.get(vo.getBelong()));
                }
                if (CharSequenceUtil.isNotBlank(vo.getRegionCode())) {
                    vo.setRegionName(provinceService.getNameByCode(vo.getRegionCode()));
                }
                if (CharSequenceUtil.isNotBlank(vo.getTrainMethod())) {
                    vo.setTrainMethodList(CharSequenceUtil.split(vo.getTrainMethod(), ","));
                }
            }

        }
    }
}
