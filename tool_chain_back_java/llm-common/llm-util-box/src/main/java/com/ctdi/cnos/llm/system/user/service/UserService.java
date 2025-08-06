package com.ctdi.cnos.llm.system.user.service;

import com.ctdi.cnos.llm.base.service.IBaseService;
import com.ctdi.cnos.llm.system.user.entity.User;
import com.ctdi.cnos.llm.system.user.entity.UserDTO;
import com.ctdi.cnos.llm.system.user.entity.UserVO;

import java.util.List;

/**
 * 数据操作服务接口。
 *
 * @author huangjinhua
 * @since 2024/07/22
 */
public interface UserService extends IBaseService<User, UserVO> {
    /**
     * 根据用户名查询用户
     *
     * @param userName 用户名
     * @return UserVO
     */
    UserVO queryByUserName(String userName);

    /**
     * 根据人力账号查询用户
     *
     * @param employeeNumber 人力账号
     * @return UserVO
     */
    UserVO queryByEmployeeNumber(String employeeNumber);


    /**
     * 根据人力账号查询用户并缓存
     *
     * @param employeeNumber 人力账号
     * @return UserVO
     */
    UserVO queryByEmployeeNumberForCache(String employeeNumber);

    /**
     * 根据人力账号列表查询用户列表
     *
     * @param employeeNumberList 人力账号列表
     * @return List<UserVO>
     */
    List<User> queryListByEmployeeNumbers(List<String> employeeNumberList);


    /**
     * 查询有效用户列表（有工具链权限，有登录过）
     *
     * @return List<UserVO>
     */
    List<User> queryValidList(UserDTO userDTO);


    /**
     * 查询用户列表
     *
     * @return List<UserVO>
     */
    List<User> queryList(UserDTO userDTO);

    /**
     * 是否存在用户名相同的用户
     *
     * @param userId         用户id
     * @param employeeNumber 人力账号
     * @param userName       用户名
     * @return 布尔值
     */
    boolean isExistUser(Long userId, String employeeNumber, String userName);

    /**
     * 根据人力账号更新账户信息
     *
     * @param user 用户
     * @return 布尔值
     */
    boolean updateByByEmployeeNumber(User user);

    /**
     * 获取平台有效用户总数
     *
     * @return 用户平台有效总数
     */
    long countPlatformUser();

    /**
     * 获取平台覆盖公司数(有效用户)
     *
     * @return 平台覆盖公司数
     */
    long countCompany();

    /**
     * 通过oa员工号批量更新
     * @param userList
     * @return
     */
    int updateBatchAuthByEmployeeNumbers(List<User> userList);


}