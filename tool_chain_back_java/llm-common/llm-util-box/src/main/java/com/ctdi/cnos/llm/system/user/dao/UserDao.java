package com.ctdi.cnos.llm.system.user.dao;

import com.ctdi.cnos.llm.base.dao.BaseDaoMapper;
import com.ctdi.cnos.llm.system.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 数据操作访问接口。
 *
 * @author huangjinhua
 * @since 2024/07/22
 */
@Mapper
public interface UserDao extends BaseDaoMapper<User> {

    /**
     * 获取平台有效用户总数
     * @return 用户平台有效总数
     */
    long countPlatformUser();

    /**
     * 获取平台覆盖公司数(有效用户)
     * @return 平台覆盖的公司数量
     */
    long countCompany();

    int updateBatchAuthByEmployeeNumbers(List<User> userList);

}