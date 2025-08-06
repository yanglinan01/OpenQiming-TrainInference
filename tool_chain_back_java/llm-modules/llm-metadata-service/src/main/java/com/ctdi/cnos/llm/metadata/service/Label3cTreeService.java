package com.ctdi.cnos.llm.metadata.service;

import com.ctdi.cnos.llm.base.service.IBaseService;
import com.ctdi.cnos.llm.beans.meta.projectSpace.Label3cTree;
import com.ctdi.cnos.llm.beans.meta.projectSpace.Label3cTreeDTO;
import com.ctdi.cnos.llm.beans.meta.projectSpace.Label3cTreeVO;
import com.ctdi.cnos.llm.beans.train.IntentRecognition.Info3cLabelTree;

import java.util.List;

/**
 * 标签树表 数据操作服务接口。
 *
 * @author 
 * @since 2025/06/10
 */
public interface Label3cTreeService extends IBaseService<Label3cTree, Label3cTreeVO> {

    List<Info3cLabelTree> queryTreeList(String type);

    void addBatch(List<Label3cTreeDTO> label3cTreeDTOList);

    void deleteAll();
}
