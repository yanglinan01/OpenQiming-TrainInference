package com.ctdi.cnos.llm.base.dao;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.handlers.StrictFill;
import com.ctdi.cnos.llm.base.object.BaseModel;
import com.ctdi.cnos.llm.context.UserContextHolder;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 如果没有显式的对通用参数进行赋值，这里会对通用参数进行填充、赋值。
 *
 * @author laiqi
 * @since 2024/8/7
 */
@Component
public class DefaultDBFieldHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Long userId = UserContextHolder.getUserId();
        DateTime now = DateTime.now();
        List<StrictFill<?, ?>> strictFills = CollUtil.newArrayList(
                StrictFill.of(BaseModel.CREATE_TIME_FIELD_NAME, Date.class, now),
                StrictFill.of(BaseModel.CREATE_USER_ID_FIELD_NAME, Long.class, userId),
                StrictFill.of(BaseModel.UPDATE_TIME_FIELD_NAME, Date.class, now),
                StrictFill.of(BaseModel.UPDATE_USER_ID_FIELD_NAME, Long.class, userId));
        strictInsertFill(findTableInfo(metaObject), metaObject, strictFills);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 当前登录用户不为空，更新人为空，则当前登录用户为更新人
        // 更新时间为空，则以当前时间为更新时间
        List<StrictFill<?, ?>> strictFills = CollUtil.newArrayList(
                StrictFill.of(BaseModel.UPDATE_TIME_FIELD_NAME, Date.class, DateTime.now()),
                StrictFill.of(BaseModel.UPDATE_USER_ID_FIELD_NAME, Long.class, UserContextHolder.getUserId()));
        strictUpdateFill(findTableInfo(metaObject), metaObject, strictFills);
    }
}