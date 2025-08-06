package com.ctdi.cnos.llm.metadata.service;

import com.ctdi.cnos.llm.base.service.IBaseService;
import com.ctdi.cnos.llm.beans.meta.projectSpace.Type3c;
import com.ctdi.cnos.llm.beans.meta.projectSpace.Type3cDTO;
import com.ctdi.cnos.llm.beans.meta.projectSpace.Type3cVO;

import java.util.List;

/**
 * 字典类型表 数据操作服务接口。
 *
 * @author 
 * @since 2025/06/10
 */
public interface Type3cService extends IBaseService<Type3c, Type3cVO> {

    void addBatch(List<Type3cDTO> type3cDTOList);

    void deleteAll();
}
