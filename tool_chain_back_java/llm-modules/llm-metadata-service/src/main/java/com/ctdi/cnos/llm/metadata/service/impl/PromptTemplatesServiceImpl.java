package com.ctdi.cnos.llm.metadata.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.beans.meta.prompt.PromptTemplates;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.metadata.dao.PromptTemplatesDao;
import com.ctdi.cnos.llm.metadata.service.PromptTemplatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * 提示词模板表(promptTemplates)表服务实现类
 * @author wangyb
 * @since 2024-04-02 15:09:11
 */
@Service
public class PromptTemplatesServiceImpl implements PromptTemplatesService {

    @Autowired
    private PromptTemplatesDao promptTemplatesDao;

    @Override
    public int deleteById(BigDecimal id) {
        if (promptTemplatesDao.queryById(id) == null) {
            throw new RuntimeException(id + "查不到对应的promptTemplates!!!");
        }
        return promptTemplatesDao.deleteById(id);
    }

    @Override
    public int insert(PromptTemplates promptTemplates) {
        promptTemplates.setId(new BigDecimal(IdUtil.getSnowflakeNextId()));
        this.checkParam(promptTemplates);
        promptTemplates.setModifierId(new BigDecimal(UserContextHolder.getUser().getId()));
        promptTemplates.setCreatorId(new BigDecimal(UserContextHolder.getUser().getId()));
        return promptTemplatesDao.insert(promptTemplates);

    }

    @Override
    public PromptTemplates queryById(BigDecimal id) {
        return promptTemplatesDao.queryById(id);
    }

    @Override
    public int updateById(PromptTemplates promptTemplates) {
        if (promptTemplatesDao.queryById(promptTemplates.getId()) == null) {
            throw new RuntimeException(promptTemplates.getId() + "查不到对应promptTemplates!!!");
        }

        this.checkParam(promptTemplates);
        promptTemplates.setModifierId(new BigDecimal(UserContextHolder.getUser().getId()));
        return promptTemplatesDao.updateById(promptTemplates);
    }

    @Override
    public List<PromptTemplates> queryList(PromptTemplates promptTemplates) {
        return promptTemplatesDao.queryList(promptTemplates);
    }

    @Override
    public Page<PromptTemplates> queryList(Page<PromptTemplates> page, PromptTemplates promptTemplates) {
        return promptTemplatesDao.queryList(page, promptTemplates);
    }

    /**
     * 新增, 修改PromptTemplates参数校验
     * @param promptTemplates promptTemplates参数
     */
    public void checkParam(PromptTemplates promptTemplates) {
        if (Objects.isNull(promptTemplates.getId())) {
            throw new RuntimeException("id不能为空!!!");
        }

        if (Objects.isNull(promptTemplates.getTemplateName())) {
            throw new RuntimeException("模板名称不能为空!!!");
        }

        if (Objects.isNull(promptTemplates.getTemplateText())) {
            throw new RuntimeException("模板内容不能为空!!!");
        }
    }


}

