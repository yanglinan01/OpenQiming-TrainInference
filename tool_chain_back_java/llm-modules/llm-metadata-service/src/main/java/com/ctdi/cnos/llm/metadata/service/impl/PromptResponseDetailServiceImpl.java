package com.ctdi.cnos.llm.metadata.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.base.object.LambdaQueryWrapperX;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.base.service.BaseService;
import com.ctdi.cnos.llm.beans.meta.prompt.PromptResponseDetail;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.metadata.dao.PromptResponseDetailDao;
import com.ctdi.cnos.llm.metadata.service.PromptResponseDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * @author yuyong
 * @date 2024/8/15 15:40
 */
@Service("promptResponseDetailService")
@Slf4j
@RequiredArgsConstructor
public class PromptResponseDetailServiceImpl extends BaseService<PromptResponseDetailDao, PromptResponseDetail, PromptResponseDetail> implements PromptResponseDetailService {

    @Override
    public void add(PromptResponseDetail promptResponseDetail) {
        baseMapper.insert(promptResponseDetail);
    }

    @Override
    public int insertBatch(List<PromptResponseDetail> list) {
        if (CollUtil.isNotEmpty(list)) {
            list.forEach(this::handleInsertData);
        }
        return baseMapper.insertBatch(list);
    }

    private PromptResponseDetail handleInsertData(PromptResponseDetail detail){
        BigDecimal userId = BigDecimal.valueOf(UserContextHolder.getUser().getId());
        if (Objects.isNull(detail.getId())) {
            detail.setId(BigDecimal.valueOf(IdUtil.getSnowflakeNextId()));
        }

        if (Objects.nonNull(userId)) {
            detail.setCreatorId(userId);
            detail.setModifierId(userId);
        }
        detail.setCreateDate(DateUtil.date());
        detail.setModifyDate(DateUtil.date());

        return detail;
    }

    @Override
    public Page<PromptResponseDetail> queryListByDataSetFileId(BigDecimal dataSetFileId, Long currentPage, Long pageSize) {
        LambdaQueryWrapper<PromptResponseDetail> queryWrapper = new LambdaQueryWrapper<PromptResponseDetail>()
                .eq(PromptResponseDetail::getDataSetFileId, dataSetFileId)
                .orderByAsc(PromptResponseDetail::getQuestionId)
                .orderByAsc(PromptResponseDetail::getRank)
                .select(PromptResponseDetail::getId, PromptResponseDetail::getQuestionId,
                        PromptResponseDetail::getRank, PromptResponseDetail::getPrompt,
                        PromptResponseDetail::getResponse, PromptResponseDetail::getDataSetFileId);

        Page<PromptResponseDetail> page = new Page<>(currentPage, pageSize);
        return baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addBatch(List<PromptResponseDetail> list, BigDecimal dataSetFileId) {
        list.forEach( item -> {
            item.setId(new BigDecimal(IdUtil.getSnowflakeNextId()));
            item.setDataSetFileId(dataSetFileId);
            item.setCreatorId(new BigDecimal(UserContextHolder.getUser().getId()));
            item.setModifierId(new BigDecimal(UserContextHolder.getUser().getId()));
        });
        saveBatch(list);
    }

    @Override
    public void deleteByDataSetFileId(BigDecimal dataSetFileId) {
        baseMapper.deleteByDataSetFileId(dataSetFileId);
    }

    @Override
    public void configureQueryWrapper(LambdaQueryWrapperX<PromptResponseDetail> wrapper, QueryParam queryParam) {

    }
}