package com.ctdi.cnos.llm.metadata.service.impl;

import cn.hutool.core.util.IdUtil;
import com.ctdi.cnos.llm.base.object.LambdaQueryWrapperX;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.base.service.BaseService;
import com.ctdi.cnos.llm.beans.meta.projectSpace.Type3c;
import com.ctdi.cnos.llm.beans.meta.projectSpace.Type3cDTO;
import com.ctdi.cnos.llm.beans.meta.projectSpace.Type3cVO;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.metadata.dao.Type3cDao;
import com.ctdi.cnos.llm.metadata.service.Type3cService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 字典类型表 数据操作服务类
 *
 * @author 
 * @since 2025/06/10
 */
@RequiredArgsConstructor
@Service("type3cService")
public class Type3cServiceImpl extends BaseService<Type3cDao, Type3c, Type3cVO> implements Type3cService {

    private final Type3cDao type3cDao;

    @Override
    public void configureQueryWrapper(LambdaQueryWrapperX<Type3c> wrapper, QueryParam queryParam) {
        Type3c filter = queryParam.getFilterDto(Type3c.class);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void addBatch(List<Type3cDTO> type3cDTOList) {
        if (!type3cDTOList.isEmpty()) {
            List<Type3c> type3cList = type3cDTOList.stream().map(item -> {
                Type3c type3c = new Type3c();
                type3c.setType(item.getType());
                type3c.setTypeName(item.getTypeName());
                return type3c;
            }).collect(Collectors.toList());
            saveBatch(type3cList);
        }

    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteAll() {
        type3cDao.deleteAll();
    }
}
