package com.ctdi.cnos.llm.metadata.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.base.object.LambdaQueryWrapperX;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.base.service.BaseService;
import com.ctdi.cnos.llm.beans.meta.prompt.PromptSequentialDetail;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.metadata.dao.PromptSequentialDetailDao;
import com.ctdi.cnos.llm.metadata.service.PromptSequentialDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author yuyong
 * @date 2024/8/15 15:43
 */
@Service("promptSequentialDetailService")
@Slf4j
@RequiredArgsConstructor
public class PromptSequentialDetailServiceImpl extends BaseService<PromptSequentialDetailDao, PromptSequentialDetail, PromptSequentialDetail> implements PromptSequentialDetailService {

    @Override
    public void add(PromptSequentialDetail promptSequentialDetail) {
        baseMapper.insert(promptSequentialDetail);
    }


    @Override
    public Page<PromptSequentialDetail> queryListByDataSetFileId(BigDecimal dataSetFileId, Long currentPage, Long pageSize) {
        LambdaQueryWrapper<PromptSequentialDetail> queryWrapper = new LambdaQueryWrapper<PromptSequentialDetail>()
                .eq(PromptSequentialDetail::getDataSetFileId, dataSetFileId)
                .orderByAsc(PromptSequentialDetail::getCreateDate);

        Page<PromptSequentialDetail> page = new Page<>(currentPage, pageSize);
        return baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Long countCircuitByDataSetId(Long dataSetId) {
        return baseMapper.countCircuitByDataSetId(dataSetId);
    }

    @Override
    public void configureQueryWrapper(LambdaQueryWrapperX<PromptSequentialDetail> wrapper, QueryParam queryParam) {
        PromptSequentialDetail filter = queryParam.getFilterDto(PromptSequentialDetail.class);
        // 根据数据集id
        wrapper.eqIfPresent(PromptSequentialDetail::getDataSetFileId, filter.getDataSetFileId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertBatch(List<PromptSequentialDetail> list, BigDecimal dataSetFileId) {
        if (CollUtil.isNotEmpty(list)) {
            list.forEach( item -> {
                item.setId(new BigDecimal(IdUtil.getSnowflakeNextId()));
                item.setDataSetFileId(dataSetFileId);
                item.setCreatorId(new BigDecimal(UserContextHolder.getUser().getId()));
                item.setModifierId(new BigDecimal(UserContextHolder.getUser().getId()));
                item.setCreateDate(DateUtil.date());
                item.setModifyDate(DateUtil.date());
                item.setDataSetFileId(dataSetFileId);
            });
        }
        return saveBatch(list) ? 1 : 0;
    }

    @Override
    public void deleteByDataSetFileId(BigDecimal dataSetFileId) {
        baseMapper.deleteByDataSetFileId(dataSetFileId);
    }

//    private PromptSequentialDetail handleInsertData(PromptSequentialDetail detail, BigDecimal dataSetFileId){
//        BigDecimal userId = BigDecimal.valueOf(UserContextHolder.getUser().getId());
//        if (Objects.isNull(detail.getId())) {
//            detail.setId(BigDecimal.valueOf(IdUtil.getSnowflakeNextId()));
//        }
//
//        if (Objects.nonNull(userId)) {
//            detail.setCreatorId(userId);
//            detail.setModifierId(userId);
//        }
//        detail.setCreateDate(DateUtil.date());
//        detail.setModifyDate(DateUtil.date());
//        detail.setDataSetFileId(dataSetFileId);
//
//        return detail;
//    }
}