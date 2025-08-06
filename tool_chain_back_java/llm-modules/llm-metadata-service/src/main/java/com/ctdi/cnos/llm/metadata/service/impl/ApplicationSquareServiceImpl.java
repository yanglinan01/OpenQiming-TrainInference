/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.metadata.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.beans.meta.application.ApplicationSquare;
import com.ctdi.cnos.llm.beans.meta.application.ApplicationSquareVO;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.base.constant.MetaDataConstants;
import com.ctdi.cnos.llm.metadata.dao.ApplicationSquareDao;
import com.ctdi.cnos.llm.metadata.service.ApplicationSquareService;
import com.ctdi.cnos.llm.metadata.service.DictionaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 应用广场 业务实现
 *
 * @author huangjinhua
 * @since 2024/6/11
 */

@Service
@RequiredArgsConstructor
public class ApplicationSquareServiceImpl implements ApplicationSquareService {

    private final ApplicationSquareDao appDao;

    private final DictionaryService dictionaryService;


    @Override
    public Page<ApplicationSquareVO> queryPage(Page<ApplicationSquareVO> page, ApplicationSquareVO app) {
        appDao.queryList(page, app);
        this.translate(page.getRecords().toArray(new ApplicationSquareVO[0]));
        return page;
    }

    @Override
    public List<ApplicationSquareVO> queryList(ApplicationSquareVO app) {
        List<ApplicationSquareVO> list = appDao.queryList(app);
        this.translate(list.toArray(new ApplicationSquareVO[0]));
        return list;
    }


    @Override
    public ApplicationSquareVO queryById(Long id) {
        ApplicationSquareVO appVO = appDao.queryById(id);
        this.translate(appVO);
        return appVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(ApplicationSquare app) {
        Long userId = UserContextHolder.getUserId();
        if (Objects.isNull(app.getId())) {
            app.setId(IdUtil.getSnowflakeNextId());
        }
        if (Objects.isNull(app.getCreatorId())) {
            app.setCreatorId(userId);
        }
        if (Objects.isNull(app.getCreateDate())) {
            app.setCreateDate(DateUtil.date());
        }
        if (Objects.isNull(app.getModifierId())) {
            app.setModifierId(userId);
        }
        if (Objects.isNull(app.getModifyDate())) {
            app.setModifyDate(DateUtil.date());
        }
        return appDao.insert(app);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateById(ApplicationSquare app) {
        Long userId = UserContextHolder.getUserId();
        if (Objects.isNull(app.getModifierId())) {
            app.setModifierId(userId);
        }
        app.setModifyDate(DateUtil.date());
        return appDao.updateById(app);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        appDao.deleteById(id);
        return 0;
    }


    private void translate(ApplicationSquareVO... apps) {
        Map<String, String> appType = dictionaryService.getDictItemMap(MetaDataConstants.APPLICATION_TYPE_DICT);
        Map<String, String> appScene = dictionaryService.getDictItemMap(MetaDataConstants.APPLICATION_SCENE_DICT);
        for (ApplicationSquareVO vo : apps) {
            if (vo != null) {
                vo.setTypeName(appType.get(vo.getType()));
                vo.setSceneName(appScene.get(vo.getScene()));
            }
        }
    }
}
