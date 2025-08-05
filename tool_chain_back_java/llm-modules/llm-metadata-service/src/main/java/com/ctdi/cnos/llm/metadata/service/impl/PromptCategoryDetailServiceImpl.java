package com.ctdi.cnos.llm.metadata.service.impl;


import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.base.object.LambdaQueryWrapperX;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.base.service.BaseService;
import com.ctdi.cnos.llm.beans.meta.prompt.PromptCategoryDetail;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.metadata.dao.PromptCategoryDetailDao;
import com.ctdi.cnos.llm.metadata.service.PromptCategoryDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 意图识别数据集详情(PromptCategoryDetail)表服务实现类
 * @author wangyb
 * @since 2024-08-16 15:13:16
 */
@Service("promptCategoryDetailService")
@RequiredArgsConstructor
public class PromptCategoryDetailServiceImpl extends BaseService<PromptCategoryDetailDao, PromptCategoryDetail, PromptCategoryDetail> implements PromptCategoryDetailService {

    @Override
    public Page<PromptCategoryDetail> queryListByDataSetFileId(BigDecimal dataSetFileId, Long currentPage, Long pageSize) {
        LambdaQueryWrapper<PromptCategoryDetail> queryWrapper = new LambdaQueryWrapper<PromptCategoryDetail>()
                .eq(PromptCategoryDetail::getDataSetFileId, dataSetFileId)
                .orderByAsc(PromptCategoryDetail::getCreateDate)
                .select(PromptCategoryDetail::getId, PromptCategoryDetail::getDataSetFileId,
                        PromptCategoryDetail::getQuestionRole, PromptCategoryDetail::getPrompt,
                        PromptCategoryDetail::getCategory);

        Page<PromptCategoryDetail> page = new Page<>(currentPage, pageSize);
        return baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addBatch(List<PromptCategoryDetail> list, BigDecimal dataSetFileId) {
        try {
            list.forEach( item -> {
                item.setId(new BigDecimal(IdUtil.getSnowflakeNextId()));
                item.setDataSetFileId(dataSetFileId);
                item.setCreatorId(new BigDecimal(UserContextHolder.getUser().getId()));
                item.setModifierId(new BigDecimal(UserContextHolder.getUser().getId()));
            });
            saveBatch(list);
        } catch (Exception e) {
            throw new RuntimeException("意图识别文件保存异常, 请检查文件格式是否正确");
        }
    }


    @Override
    public void configureQueryWrapper(LambdaQueryWrapperX<PromptCategoryDetail> wrapper, QueryParam queryParam) {

    }
}