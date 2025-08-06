package com.ctdi.cnos.llm.metadata.service.impl;

import com.ctdi.cnos.llm.base.object.LambdaQueryWrapperX;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.base.service.BaseService;
import com.ctdi.cnos.llm.beans.meta.projectSpace.Label3cTree;
import com.ctdi.cnos.llm.beans.meta.projectSpace.Label3cTreeDTO;
import com.ctdi.cnos.llm.beans.meta.projectSpace.Label3cTreeVO;
import com.ctdi.cnos.llm.beans.meta.projectSpace.ProjectUserRole;
import com.ctdi.cnos.llm.beans.train.IntentRecognition.Info3cLabelTree;
import com.ctdi.cnos.llm.metadata.dao.Label3cTreeDao;
import com.ctdi.cnos.llm.metadata.service.Label3cTreeService;
import com.ctdi.cnos.llm.util.ModelUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 标签树表 数据操作服务类
 *
 * @author 
 * @since 2025/06/10
 */
@RequiredArgsConstructor
@Service("label3cTreeService")
public class Label3cTreeServiceImpl extends BaseService<Label3cTreeDao, Label3cTree, Label3cTreeVO> implements Label3cTreeService {

    private final Label3cTreeDao  label3cTreeDao;

    @Override
    public void configureQueryWrapper(LambdaQueryWrapperX<Label3cTree> wrapper, QueryParam queryParam) {
        Label3cTree filter = queryParam.getFilterDto(Label3cTree.class);
    }

    @Override
    public List<Info3cLabelTree> queryTreeList(String  type) {
        List<Label3cTree> label3cTreeList = label3cTreeDao.selectList("type", type);
        return convertToInfo3cLabelTree(label3cTreeList);
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void addBatch(List<Label3cTreeDTO> label3cTreeDTOList) {
        if (!label3cTreeDTOList.isEmpty()) {
            List<Label3cTree> label3cTrees = label3cTreeDTOList.stream().map(label3cTreeDTO -> ModelUtil.copyTo(label3cTreeDTO, Label3cTree.class))
                    .collect(Collectors.toList());
            saveBatch(label3cTrees);
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteAll() {
        label3cTreeDao.deleteAll();
    }


    private List<Info3cLabelTree> convertToInfo3cLabelTree(List<Label3cTree> list) {
        // 按父id分组
        Map<Integer, List<Label3cTree>> parentMap =
                list.stream().collect(Collectors.groupingBy(Label3cTree::getParentId));
        // 获取顶级节点
        List<Label3cTree> rootNodes = parentMap.getOrDefault(-1, new ArrayList<>());

        return rootNodes.stream().map(node ->
                convertNode(node, parentMap)).collect(Collectors.toList());
    }

    private Info3cLabelTree convertNode(Label3cTree node, Map<Integer, List<Label3cTree>> parentMap) {
        Info3cLabelTree  info3cLabelTree = new Info3cLabelTree();
        info3cLabelTree.setId(new BigDecimal(node.getId()));
        info3cLabelTree.setCode(node.getCode());
        info3cLabelTree.setName(node.getName());
        info3cLabelTree.setLevel(node.getLevel());
        info3cLabelTree.setParentId(node.getParentId());
        info3cLabelTree.setRemark(node.getRemark());

        List<Label3cTree> children = parentMap.getOrDefault(node.getId(), new ArrayList<>());
        if (!children.isEmpty()) {
            info3cLabelTree.setChildren(children.stream().map(child ->
                    convertNode(child, parentMap)).collect(Collectors.toList()));
        } else {
            info3cLabelTree.setChildren(new ArrayList<>());
        }
        return info3cLabelTree;
    }
}
