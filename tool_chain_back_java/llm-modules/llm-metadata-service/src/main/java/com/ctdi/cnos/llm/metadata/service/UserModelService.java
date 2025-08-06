package com.ctdi.cnos.llm.metadata.service;

import com.ctdi.cnos.llm.base.service.IBaseService;
import com.ctdi.cnos.llm.beans.meta.model.UserModel;
import com.ctdi.cnos.llm.beans.meta.model.UserModelDTO;
import com.ctdi.cnos.llm.beans.meta.model.UserModelVO;
import com.ctdi.cnos.llm.response.OperateResult;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Set;

/**
 * 用户模型关系表 数据操作服务接口。
 *
 * @author wangyb
 * @since 2024/11/14
 */
public interface UserModelService extends IBaseService<UserModel, UserModelVO> {

    boolean updateUserModel(Long userId, Set<Long> modelIds, String usage);

    boolean delete(Long userId, Long modeId, String usage);

    /**
     * 批量绑定
     *
     * @param usage            用法
     * @param userId           用户ID
     * @param modelIdList      模型ID 列表
     * @param modelId          模型id
     * @param userIdList       用户ID列表
     * @param deleteBindingAll 是否按用户ID/模型ID w维度全量删除
     * @return boolean
     */
    boolean batchBinding(String usage, Long userId, Set<Long> modelIdList, Long modelId, Set<Long> userIdList, boolean deleteBindingAll);

    /**
     * 一键批量绑定
     *
     * @param userId  用户ID
     * @param modelId 模型id
     * @return boolean
     */
    boolean oneClickBinding(Long userId, Long modelId);

    /**
     * 一键批量取消绑定
     *
     * @param userId  用户ID
     * @param modelId 模型id
     * @return boolean
     */
    boolean oneClickCancelBinding(Long userId, Long modelId);

    /**
     * 批量绑定
     * @param userModelDTOList
     * @return
     */
    OperateResult<String> batchBindingAll(@RequestBody List<UserModelDTO> userModelDTOList);
}
