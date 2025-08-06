package com.ctdi.cnos.llm.controller.auth;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.ctdi.cnos.llm.annotation.AuthIgnore;
import com.ctdi.cnos.llm.base.constant.MetaDataConstants;
import com.ctdi.cnos.llm.base.constant.SystemConstant;
import com.ctdi.cnos.llm.base.object.Groups;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.beans.meta.model.UserModelResp;
import com.ctdi.cnos.llm.beans.meta.model.UserModelVO;
import com.ctdi.cnos.llm.config.UserConfig;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.feign.metadata.UserModelServiceClientFeign;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.system.auth.AuthClient;
import com.ctdi.cnos.llm.system.auth.AuthUtil;
import com.ctdi.cnos.llm.system.user.entity.User;
import com.ctdi.cnos.llm.system.user.entity.UserDTO;
import com.ctdi.cnos.llm.system.user.entity.UserVO;
import com.ctdi.cnos.llm.system.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangyb
 * @since 2024/4/24 17:01
 */
@Slf4j
@RestController
@Api(tags = {"auth接口"})
@RequestMapping(value = "/web/auth")
@RequiredArgsConstructor
public class AuthWebController {

    private final AuthClient authClient;

    private final UserService userService;
    private static final String NO_AUTH = "您没有操作权限！";
    private static final String NO_USER = "缺失用户信息！";

    private final UserConfig userConfig;
    private final UserModelServiceClientFeign userModelServiceClientFeign;

    @AuthIgnore
    @GetMapping("/getCurrentUserInfo")
    @ApiOperation(value = "获取当前用户信息")
    public OperateResult<UserVO> getCurrentUserInfo(@RequestHeader("Authorization") String token) {
        try {
            OperateResult<UserVO> userInfo = authClient.getUserInfo(token);
            if (userInfo != null && userInfo.isSuccess()) {
                UserVO userVO = AuthUtil.copyUser(userInfo.getData());
                //脱敏，清空数据权限标识
                userVO.setRole(null);
                userVO.setIsAdmin(null);
                //菜单权限
                AuthUtil.menuAuth(userVO);
                userInfo.setData(userVO);
            }
            return userInfo;
        } catch (Exception ex) {
            log.error("当前用户信息查询异常", ex);
            return new OperateResult<>(false, ex.getMessage(), null);
        }
    }


    @PostMapping("/queryPage")
    @ApiOperation(value = "查询用户信息分页")
    public OperateResult<PageResult<UserVO>> queryPage(@Validated(Groups.PAGE.class) @RequestBody QueryParam queryParam) {
        try {
            String modelIdStr = Convert.toStr(queryParam.getFilterMap().get("modelId"));
            String usage = Convert.toStr(queryParam.getFilterMap().get("usage"));
            Set<Long> userIds = new HashSet<>();
            if (CharSequenceUtil.isNotBlank(modelIdStr) && CharSequenceUtil.isNotBlank(usage)) {
                Long modelId = Convert.toLong(modelIdStr);
                QueryParam query = new QueryParam();
                Map<String, Object> map = new HashMap<>();
                map.put("modelId", modelId);
                map.put("usage", usage);
                query.setFilterMap(map);
                List<UserModelVO> data = userModelServiceClientFeign.queryList(query).getData();
                userIds = CollUtil.isEmpty(data) ? null : data.stream().map(UserModelVO::getUserId).collect(Collectors.toSet());
            }

            UserVO currentUser = UserContextHolder.getUser();

            Assert.isTrue(AuthUtil.hasSystemManage(currentUser), NO_AUTH);
            PageResult<UserVO> page = userService.queryPage(queryParam);
            if (CollUtil.isNotEmpty(userIds)) {
                Set<Long> ids = userIds;
                List<UserVO> collect = page.getRows().stream().filter((vo -> !ids.contains(vo.getId()))).collect(Collectors.toList());
                page.setRows(collect);
                page.setTotal(Convert.toLong(collect.size()));
            }

            return new OperateResult<>(true, null, page);
        } catch (Exception ex) {
            log.error("查询用户信息分页异常", ex);
            return new OperateResult<>(false, ex.getMessage(), null);
        }
    }

    @PostMapping("/queryPageByModel")
    @ApiOperation(value = "查询模型下用户信息分页")
    public OperateResult<PageResult<UserModelResp>> queryPageByModel(@Validated(Groups.PAGE.class) @RequestBody QueryParam queryParam) {
        List<UserModelResp> userModelRespList = new ArrayList<>();
        try {
            String modelIdStr = Convert.toStr(queryParam.getFilterMap().get("modelId"));
            //判断权限
            UserVO currentUser = UserContextHolder.getUser();
            Assert.isTrue(AuthUtil.hasSystemManage(currentUser), NO_AUTH);
            //获取筛选用户
            PageResult<UserVO> page = userService.queryPage(queryParam);
            List<UserVO> userVOList = page.getRows();
            if (CollUtil.isNotEmpty(userVOList)) {
                //获取筛选该模型下用户权限
                List<Long> userIdList = userVOList.stream().map(o -> {
                    return o.getId();
                }).collect(Collectors.toList());
                QueryParam query = new QueryParam();
                Map<String, Object> map = new HashMap<>();
                if (CharSequenceUtil.isNotBlank(modelIdStr)) {
                    Long modelId = Convert.toLong(modelIdStr);
                    map.put("modelId", modelId);
                }
                map.put("userIdList", userIdList);
                query.setFilterMap(map);
                List<UserModelVO> data = userModelServiceClientFeign.queryList(query).getData();
                Map<Long, List<UserModelVO>> userModelMap = new HashMap<>();
                if (CollUtil.isNotEmpty(data)) {
                    userModelMap = data.stream().collect(Collectors.groupingBy(UserModelVO::getUserId));
                }
                List<UserModelVO> userModelVOList = new ArrayList<>();
                for (UserVO userVO : userVOList) {
                    //转换出参
                    UserModelResp userModelResp = new UserModelResp();
                    BeanUtils.copyProperties(userVO, userModelResp);
                    userModelResp.setTrainAuth(SystemConstant.NO);
                    userModelResp.setReasonAuth(SystemConstant.NO);
                    //获取用户模型权限
                    userModelVOList = userModelMap.get(userModelResp.getId());
                    if (CollUtil.isNotEmpty(userModelVOList)) {
                        userModelResp.setUserModelVOList(userModelVOList);
                        for (UserModelVO userModelVO : userModelVOList) {
                            //训练模型
                            if (MetaDataConstants.MODEL_AUTH_USAGE_DICT_TRAIN.equals(userModelVO.getUsage())) {
                                userModelResp.setTrainAuth(SystemConstant.YES);
                            }
                            //推理模型
                            if (MetaDataConstants.MODEL_AUTH_USAGE_DICT_REASON.equals(userModelVO.getUsage())) {
                                userModelResp.setReasonAuth(SystemConstant.YES);
                            }
                        }
                    }
                    userModelRespList.add(userModelResp);
                }
            }
            return new OperateResult<>(true, null, new PageResult<>(userModelRespList, page.getTotal()));
        } catch (Exception ex) {
            log.error("查询用户信息分页异常", ex);
            return new OperateResult<>(false, ex.getMessage(), null);
        }
    }

    @GetMapping("/queryById")
    @ApiOperation(value = "查询用户信息详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "ID", paramType = "param")
    })
    public OperateResult<UserVO> queryById(@RequestParam(name = "id") Long id) {
        try {
            UserVO currentUser = UserContextHolder.getUser();
            Assert.isTrue(AuthUtil.hasSystemManage(currentUser), NO_AUTH);
            UserVO userVO = Optional.ofNullable(id).map(userId -> userService.queryById(userId, false)).orElse(null);
            return new OperateResult<>(true, null, userVO);
        } catch (Exception ex) {
            log.error("查询用户信息详情异常", ex);
            return new OperateResult<>(false, ex.getMessage(), null);
        }

    }

    @PostMapping("/addUser")
    @ApiOperation(value = "创建用户信息")
    public OperateResult<UserVO> addUser(@RequestBody User user) {
        boolean success;
        String result;
        try {
            UserVO currentUser = UserContextHolder.getUser();
            Assert.isTrue(AuthUtil.hasSystemManage(currentUser), NO_AUTH);
            Assert.notNull(user, NO_USER);
            Assert.notNull(user.getEmployeeNumber(), "人力账号为空！");
            //人力账号转大写
            user.setEmployeeNumber(user.getEmployeeNumber().toUpperCase());
            UserVO userVO = Optional.ofNullable(user.getEmployeeNumber()).map(employeeNumber -> userService.queryByEmployeeNumber(employeeNumber)).orElse(null);
            //UserVO userVO = Optional.ofNullable(user.getId()).map(userId -> userService.queryById(userId, false)).orElse(null);
            if (userVO != null) {
                user.setId(userVO.getId());
                success = this.buildUpdate(user);
                result = success ? "修改成功" : "修改失败";
            } else {
                user.setId(IdUtil.getSnowflakeNextId());
                this.isValid(user, true);
                user.setIsValid(0L);
                //新增用户默认不开通所有权限, 有设置工具链权限、智能体权限的保留
                AuthUtil.defaultAuth(user);
                success = userService.save(user);
                result = success ? "新增成功！" : "新增失败！";
            }
            return new OperateResult<>(success, result, null);
        } catch (Exception ex) {
            log.error("创建用户信息异常", ex);
            return new OperateResult<>(false, ex.getMessage(), null);
        }

    }


    @PostMapping("/importUser")
    @ApiOperation(value = "导入用户信息")
    public OperateResult<String> importUser(@RequestParam("file") MultipartFile file) {
        boolean success;
        String message;
        try {
            UserVO currentUser = UserContextHolder.getUser();
            Assert.isTrue(AuthUtil.hasSystemManage(currentUser), NO_AUTH);
            int updateCount = 0;
            int addCount = 0;
            int errorCount = 0;
            int invalidCount = 0;
            if (!file.isEmpty()) {
                //文件校验
                String name = file.getOriginalFilename();
                String suffix = FileUtil.getSuffix(name);
                Assert.isTrue("xlsx".equalsIgnoreCase(suffix) || "xls".equalsIgnoreCase(suffix), "当前只支持xlsx，xls的格式文件！");
                //获取配置表头映射
                Map<String, String> excelHeader = userConfig.getUserExcelHeader();
                //读取数据
                ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
                reader.setHeaderAlias(excelHeader);
                //跳过空行
                reader.setIgnoreEmptyRow(true);
                List<Map<String, Object>> mapList = reader.readAll();
                Assert.notEmpty(mapList, "没有识别到用户数据");
                List<User> userList = BeanUtil.copyToList(mapList, User.class);
                Assert.notEmpty(userList, "用户数据映射失败");
                //数据转换
                List<User> list = userList.stream().peek(user -> {
                    if (CharSequenceUtil.isNotBlank(user.getEmployeeNumber())) {
                        //去除换行符、空格，转大写
                        String upperCase = CharSequenceUtil.removeAllLineBreaks(user.getEmployeeNumber()).trim().toUpperCase();
                        user.setEmployeeNumber(upperCase);
                    }
                    user.setAgentAuth(BooleanUtil.toString(CharSequenceUtil.equals(user.getAgentAuth(), "是"), "0", "1"));
                    user.setToolAuth(BooleanUtil.toString(CharSequenceUtil.equals(user.getToolAuth(), "是"), "0", "1"));
                    AuthUtil.defaultAuth(user);
                }).collect(Collectors.toList());
                //获取重复的数据
                Set<String> employeeNumberRepeat = list.stream()
                        .collect(Collectors.groupingBy(p -> p.getEmployeeNumber(), Collectors.counting()))
                        .entrySet()
                        .stream()
                        .filter(e -> e.getValue() > 1)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toSet());
                Assert.isFalse(CollUtil.isNotEmpty(employeeNumberRepeat), "当前文件存在人力账号重复：【{}】，请核查后导入", CollUtil.join(employeeNumberRepeat, ","));

                //根据用户人力账号查询用户信息.有的要做更新
                List<String> employeeNumberList = list.stream().map(User::getEmployeeNumber).collect(Collectors.toList());
                List<User> employeeNumberUser = userService.queryListByEmployeeNumbers(employeeNumberList);
                List<String> exitEmployeeNumber = null;
                if (CollUtil.isNotEmpty(employeeNumberUser)) {
                    exitEmployeeNumber = employeeNumberUser.stream().map(User::getEmployeeNumber).collect(Collectors.toList());
                }
                List<User> notExitUser = new ArrayList<>();
                for (User user : list) {
                    //无效人力编码
                    if (CharSequenceUtil.isBlank(user.getEmployeeNumber())) {
                        invalidCount++;
                        continue;
                    }
                    if (CollUtil.contains(exitEmployeeNumber, user.getEmployeeNumber())) {
                        boolean update = userService.updateByByEmployeeNumber(user);
                        if (update) {
                            updateCount++;
                        } else {
                            errorCount++;
                            log.error("【批量excel导入用户信息】人力编码为：【{}】修改失败!", user.getEmployeeNumber());
                        }
                    } else {
                        notExitUser.add(user);
                    }
                }
                if (CollUtil.isNotEmpty(notExitUser)) {
                    boolean add = userService.saveBatch(notExitUser, 500);
                    if (add) {
                        addCount += notExitUser.size();
                    } else {
                        errorCount += notExitUser.size();
                        List<String> errorList = notExitUser.stream().map(User::getEmployeeNumber).collect(Collectors.toList());
                        log.error("【批量excel导入用户信息】人力编码为：【{}】修改失败!", CollUtil.join(errorList, ","));
                    }
                }
                message = StrUtil.format("【批量excel导入用户信息】excel有效行数为：{}，新增数量为：{}，更新数量为：{}，无效数据：{}，操作失败数量为：{}",
                        list.size(), addCount, updateCount, invalidCount, errorCount);
                log.info(message);
                return OperateResult.successMessage(message);
            }
            return OperateResult.error("缺失文件");
        } catch (Exception ex) {
            log.error("导入用户信息异常", ex);
            return new OperateResult<>(false, ex.getMessage(), null);
        }

    }

    @PostMapping("/updateUser")
    @ApiOperation(value = "修改用户信息")
    public OperateResult<UserVO> updateUser(@RequestBody User user) {
        boolean success;
        String message;
        try {
            UserVO currentUser = UserContextHolder.getUser();
            Assert.isTrue(AuthUtil.hasSystemManage(currentUser), NO_AUTH);
            success = this.buildUpdate(user);
            message = success ? "修改成功" : "修改失败";
            return new OperateResult<>(success, message, null);
        } catch (Exception ex) {
            log.error("修改用户信息异常", ex);
            return new OperateResult<>(false, ex.getMessage(), null);
        }

    }

    @DeleteMapping("/deleteUser")
    @ApiOperation(value = "删除用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", paramType = "param")
    })
    public OperateResult<UserVO> deleteUser(Long userId) {
        String result;
        try {
            UserVO currentUser = UserContextHolder.getUser();
            Assert.isTrue(AuthUtil.hasSystemManage(currentUser), NO_AUTH);
            Assert.notNull(userId, "缺乏用户ID信息");
            boolean update = userService.deleteById(userId);
            result = update ? "删除成功" : "删除失败";
            return new OperateResult<>(true, result, null);
        } catch (Exception ex) {
            log.error("删除用户信息异常", ex);
            return new OperateResult<>(false, ex.getMessage(), null);
        }

    }

    @PostMapping("/changeUserBaseAuth")
    @ApiOperation(value = "修改用户基础权限（工具链、智能体平台）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, paramType = "body"),
            @ApiImplicitParam(name = "toolAuth", value = "是否有工具链权限", defaultValue = "1", paramType = "body"),
            @ApiImplicitParam(name = "agentAuth", value = "是否有智能体平台权限", defaultValue = "1", paramType = "body")
    })
    public OperateResult<UserVO> changeUserBaseAuth(@ApiIgnore @RequestBody User user) {
        boolean success;
        String message;
        try {
            UserVO currentUser = UserContextHolder.getUser();
            Assert.isTrue(AuthUtil.hasSystemManage(currentUser), NO_AUTH);
            if (CharSequenceUtil.isAllBlank(user.getToolAuth(), user.getAgentAuth())) {
                return new OperateResult<>(false, "请输入需要变更的权限！", null);
            }
            this.isValid(user, false);
            User userAuth = new User();
            userAuth.setId(user.getId());
            //权限
            userAuth.setToolAuth(user.getToolAuth());
            userAuth.setAgentAuth(user.getAgentAuth());
            success = this.buildUpdate(userAuth);
            message = success ? "变更权限成功" : "变更权限失败";
            return new OperateResult<>(success, message, null);
        } catch (Exception ex) {
            log.error("变更用户权限异常", ex);
            return new OperateResult<>(false, ex.getMessage(), null);
        }
    }

    @PostMapping("/changeUserAdvancedAuth")
    @ApiOperation(value = "变更用户高级权限（数据权限/系统管理权限）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, paramType = "body"),
            @ApiImplicitParam(name = "role", value = "用户角色", defaultValue = "3", paramType = "body"),
            @ApiImplicitParam(name = "systemAuth", value = "是否有系统管理权限", defaultValue = "1", paramType = "body")
    })
    public OperateResult<UserVO> changeUserAdvancedAuth(@ApiIgnore @RequestBody User user) {
        boolean success;
        String message;
        try {
            UserVO currentUser = UserContextHolder.getUser();
            //超级管理员才允许变更高级权限，系统管理员也不允许
            Assert.isTrue(AuthUtil.isAdmin(currentUser), NO_AUTH);
            if (CharSequenceUtil.isAllBlank(user.getRole(), user.getSystemAuth())) {
                return new OperateResult<>(false, "请输入需要变更的权限！", null);
            }
            this.isValid(user, false);
            User userAuth = new User();
            userAuth.setId(user.getId());
            userAuth.setRole(user.getRole());
            userAuth.setSystemAuth(user.getSystemAuth());
            success = this.buildUpdate(userAuth);
            message = success ? "变更权限成功" : "变更权限失败";
            return new OperateResult<>(success, message, null);
        } catch (Exception ex) {
            log.error("变更用户数据权限异常", ex);
            return new OperateResult<>(false, ex.getMessage(), null);
        }
    }

    @GetMapping("/removeCache")
    @ApiOperation(value = "清除用户缓存")
    public OperateResult<UserVO> removeCache(@RequestParam("token") String token) {
        try {
            authClient.removeCache(token);
            return new OperateResult<>(false, "清除用户缓存成功", null);
        } catch (Exception ex) {
            log.error("清除用户缓存异常", ex);
            return new OperateResult<>(false, ex.getMessage(), null);
        }
    }

    @GetMapping("/queryListByNameAndEmployeeNumber")
    @ApiOperation(value = "根据用户名或员工编号查询用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "用户名", paramType = "param"),
            @ApiImplicitParam(name = "employeeNumber", value = "员工编号", paramType = "param")
    })
    public OperateResult<List<UserVO>> queryListByNameAndEmployeeNumber(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "employeeNumber", required = false) String employeeNumber
    ) {
        try {
            UserDTO userDTO = new UserDTO();
            userDTO.setName(name);
            userDTO.setEmployeeNumber(employeeNumber);
            List<User> userList = userService.queryList(userDTO);
            List<UserVO> userVOList = userList.stream().map(user -> {
                UserVO userVO = new UserVO();
                BeanUtils.copyProperties(user, userVO);
                return userVO;
            }).collect(Collectors.toList());
            return new OperateResult<>(true, "查询成功", userVOList);
        } catch (Exception ex) {
            log.error("查询用户信息异常", ex);
            return new OperateResult<>(false, ex.getMessage(), null);
        }
    }

    @AuthIgnore
    @GetMapping("/getAuthToken")
    @ApiOperation(value = "根据用户名或员工编号查询用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "用户名", paramType = "param"),
            @ApiImplicitParam(name = "employeeNumber", value = "员工编号", paramType = "param")
    })
    public OperateResult<String> getAuthToken(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "employeeNumber", required = false) String employeeNumber,
            @RequestParam(name = "password", required = false) String password,
            @RequestParam(name = "code", required = false) String code
    ) {
        String token = "";
        try {
            UserDTO userDTO = new UserDTO();
            userDTO.setName(name);
            userDTO.setEmployeeNumber(employeeNumber);
            List<User> userList = userService.queryList(userDTO);
            if (CollUtil.isNotEmpty(userList)) {
                token = userList.get(0).getId().toString();
                return new OperateResult<>(true, "查询成功", token);
            }
            return new OperateResult<>(false, "未查询到用户", token);
        } catch (Exception ex) {
            log.error("查询用户信息异常", ex);
            return new OperateResult<>(false, ex.getMessage(), null);
        }
    }

    /**
     * 校验
     *
     * @param user  用户信息
     * @param isAdd 是否是心增
     */
    private void isValid(User user, boolean isAdd) {
        Assert.notNull(user, NO_USER);
        Assert.notNull(user.getId(), "缺失用户ID");
        if (isAdd) {
            Assert.notNull(user.getUserName(), "缺失用户名");
            Assert.notNull(user.getEmployeeNumber(), "缺失人力编码");
            //Assert.notNull(user.getName(), "缺失真实名称");
        }
        if (user.getEmployeeNumber() != null) {
            Assert.isFalse(userService.isExistUser(user.getId(), user.getEmployeeNumber(), null), "人力账号已存在！");
        }
        /*if (user.getUserName() != null || user.getEmployeeNumber() != null) {
            Assert.isFalse(userService.isExistUser(user.getId(), user.getEmployeeNumber(), user.getUserName()), "当前用户(用户名/人力账号)已存在！");
        }*/

    }

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     */
    private boolean buildUpdate(User user) {
        this.isValid(user, false);
        AuthUtil.adminAuth(user);
        if (CharSequenceUtil.isNotBlank(user.getEmployeeNumber())) {
            return userService.updateByByEmployeeNumber(user);
        } else {
            return userService.updateById(user);
        }
    }

}

