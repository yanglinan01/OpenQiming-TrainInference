/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.metadata.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ReUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.beans.meta.dictionary.DictionaryVO;
import com.ctdi.cnos.llm.beans.meta.prompt.Prompt;
import com.ctdi.cnos.llm.beans.meta.prompt.PromptVO;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.base.constant.MetaDataConstants;
import com.ctdi.cnos.llm.metadata.dao.PromptDao;
import com.ctdi.cnos.llm.metadata.service.DictionaryService;
import com.ctdi.cnos.llm.metadata.service.PromptService;
import com.ctdi.cnos.llm.system.user.entity.UserVO;
import com.ctdi.cnos.llm.utils.DataScopeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * prompt 业务接口实现
 *
 * @author huangjinhua
 * @since 2024/4/2
 */
@Service
@RequiredArgsConstructor
public class PromptServiceImpl implements PromptService {
    private final PromptDao promptDao;

    private final DictionaryService dictionaryService;

    @Override
    public Page<PromptVO> queryList(Page<PromptVO> page, PromptVO prompt) {
        //自定义模板
        if (Objects.equals(prompt.getBelong(), MetaDataConstants.PROMPT_BELONG_DICT_SELF)) {
            prompt.setDataScopeSql(DataScopeUtil.dataScopeSql("a", null));
        }
        return promptDao.queryList(page, prompt);
    }

    @Override
    public List<PromptVO> queryList(PromptVO prompt) {
        //自定义模板
        if (Objects.equals(prompt.getBelong(), MetaDataConstants.PROMPT_BELONG_DICT_SELF)) {
            prompt.setDataScopeSql(DataScopeUtil.dataScopeSql("a", null));
        }
        return promptDao.queryList(prompt);
    }

    @Override
    public PromptVO queryById(Long id) {
        PromptVO vo = promptDao.queryById(id);
        if (Objects.nonNull(vo) && Objects.nonNull(vo.getType())) {
            vo.setTypeName(dictionaryService.getDictItemLabel(MetaDataConstants.PROMPT_TYPE_DICT, vo.getType()));
        }
        return vo;
    }

    @Override
    public Long getPromptCount() {
        String dataScope = DataScopeUtil.dataScopeSql("a", null);
        return promptDao.getPromptCount(dataScope);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(Prompt prompt) {
        if (Objects.isNull(prompt.getId())) {
            prompt.setId(IdUtil.getSnowflakeNextId());
        }
        if (Objects.isNull(prompt.getIdentifier())) {
            //如果是空则根据内容识别变量标识符
            prompt.setIdentifier(this.getIdentifier(prompt.getPromptText()));
        }
        if (Objects.nonNull(prompt.getIdentifier())) {
            String variable = CharSequenceUtil.join(",", this.getVariable(prompt.getIdentifier(), prompt.getPromptText()));
            prompt.setVariable(Objects.isNull(variable) ? "" : variable);
        }
        if (Objects.isNull(prompt.getModelId())) {
            prompt.setModelId(MetaDataConstants.MODEL_ID_DEFAULT);
        }
        if (Objects.isNull(prompt.getBelong())) {
            prompt.setBelong(MetaDataConstants.PROMPT_BELONG_DICT_SELF);
        }
        if (Objects.isNull(prompt.getType())) {
            prompt.setType(MetaDataConstants.PROMPT_TYPE_DICT_SELF);
        }
        UserVO user = UserContextHolder.getUser();
        prompt.setCreatorId(user.getId());
        prompt.setCreateDate(DateUtil.date());
        prompt.setRegionCode(user.getRegionCode());
        prompt.setModifierId(user.getId());
        prompt.setModifyDate(DateUtil.date());
        return promptDao.insert(prompt);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateById(Prompt prompt) {
        if (Objects.isNull(prompt.getModelId())) {
            prompt.setModelId(MetaDataConstants.MODEL_ID_DEFAULT);
        }
        if (Objects.isNull(prompt.getIdentifier())) {
            //如果是空则根据内容识别变量标识符
            prompt.setIdentifier(this.getIdentifier(prompt.getPromptText()));
        }
        if (Objects.nonNull(prompt.getIdentifier())) {
            String variable = CharSequenceUtil.join(",", this.getVariable(prompt.getIdentifier(), prompt.getPromptText()));
            prompt.setVariable(Objects.isNull(variable) ? "" : variable);
        }
        UserVO user = UserContextHolder.getUser();
        prompt.setModifierId(user.getId());
        prompt.setModifyDate(DateUtil.date());
        return promptDao.updateById(prompt);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return promptDao.deleteById(id);
    }


    /**
     * 根据prompt内容获取标识符内的变量
     *
     * @param context prompt内容
     * @return String 标识符
     */
    @Override
    public String getIdentifier(String context) {
        String identifier = "";
        if (Objects.nonNull(context)) {
            //获取字典
            List<DictionaryVO> dictList = dictionaryService.getDictListByDictType(MetaDataConstants.PROMPT_IDENTIFIER_DICT);
            if (dictList != null && !dictList.isEmpty()) {
                List<String> list = null;
                for (DictionaryVO dictionary : dictList) {
                    list = ReUtil.findAll(dictionary.getDictItemExtField1(), context, 1);
                    if (CollUtil.isNotEmpty(list)) {
                        identifier = dictionary.getDictItemValue();
                        break;
                    }
                }
            }
        }
        return identifier;
    }

    /**
     * 根据标识符获取标识符内的变量
     *
     * @param identifier 变量标识符
     * @param context    prompt内容
     * @return List<String>
     */
    public List<String> getVariable(String identifier, String context) {
        List<String> list = Collections.emptyList();
        if (CharSequenceUtil.isBlank(identifier)) {
            return list;
        }
        DictionaryVO dictionary = dictionaryService.getDictItem(MetaDataConstants.PROMPT_IDENTIFIER_DICT, identifier);
        if (dictionary != null) {
            list = ReUtil.findAll(dictionary.getDictItemExtField1(), context, 1);
        }
        if (CollUtil.isEmpty(list)) {
            list = Collections.emptyList();
        }
        return list;
    }
}
