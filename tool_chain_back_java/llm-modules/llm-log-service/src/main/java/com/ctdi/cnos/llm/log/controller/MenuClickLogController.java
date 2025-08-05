package com.ctdi.cnos.llm.log.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import com.ctdi.cnos.llm.annotation.AuthIgnore;
import com.ctdi.cnos.llm.base.constant.ApplicationConstant;
import com.ctdi.cnos.llm.base.object.Groups;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.base.object.StatType;
import com.ctdi.cnos.llm.beans.log.menu.MenuClickLog;
import com.ctdi.cnos.llm.beans.log.menu.MenuClickLogDTO;
import com.ctdi.cnos.llm.beans.log.menu.MenuClickLogInterface;
import com.ctdi.cnos.llm.beans.log.menu.MenuClickLogVO;
import com.ctdi.cnos.llm.beans.meta.operationCenter.BarCharts;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.log.service.MenuClickLogService;
import com.ctdi.cnos.llm.response.ErrorCodeEnum;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.system.user.entity.UserVO;
import com.ctdi.cnos.llm.system.user.service.UserService;
import com.ctdi.cnos.llm.util.ModelUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 菜单点击日志 控制器类。
 *
 * @author huangjinhua
 * @since 2024/10/16
 */
@Api(tags = "菜单点击日志接口", value = "MenuClickLogController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/menuClickLog")
public class MenuClickLogController {

    private final MenuClickLogService service;

    private final UserService userService;

    /**
     * 分页查询符合条件的菜单点击日志列表。
     *
     * @param queryParam 查询对象。
     * @return 分页列表数据。
     */
    @ApiOperation(value = "分页查询符合条件的菜单点击日志数据", notes = "分页查询符合条件的菜单点击日志数据")
    @PostMapping(value = "/queryPage")
    public OperateResult<PageResult<MenuClickLogVO>> queryPage(@Validated(Groups.PAGE.class) @RequestBody QueryParam queryParam) {
        return OperateResult.success(service.queryPage(queryParam));
    }

    /**
     * 查询符合条件的菜单点击日志列表。
     *
     * @param queryParam 查询对象。
     * @return 列表数据。
     */
    @ApiOperation(value = "列表查询符合条件的菜单点击日志数据", notes = "列表查询符合条件的菜单点击日志数据")
    @PostMapping(value = "/queryList")
    public OperateResult<List<MenuClickLogVO>> queryList(@RequestBody QueryParam queryParam) {
        return OperateResult.success(service.queryList(queryParam));
    }

    /**
     * 查询指定菜单点击日志数据。
     *
     * @param id 指定菜单点击日志主键Id。
     * @return 单条数据。
     */
    @ApiOperation(value = "查询指定ID的菜单点击日志数据", notes = "通过菜单点击日志ID获取具体的菜单点击日志数据")
    @GetMapping(value = "/queryById")
    public OperateResult<MenuClickLogVO> queryById(@ApiParam(value = "菜单点击日志ID", required = true, example = "1")
                                                   @NotNull(message = "菜单点击日志ID不能为空") @RequestParam("id") Long id) {
        return OperateResult.success(service.queryById(id, true));
    }

    /**
     * 根据统计类型统计省份维度数据画图。
     *
     * @param type DAY：当天；MONTH：当月；ALL 累计
     * @return 统计数据
     */
    @ApiOperation(value = "根据统计类型统计省份维度数据画图", notes = "根据统计类型统计省份维度数据画图")
    @GetMapping(value = "/queryChart")
    public OperateResult<List<BarCharts>> queryChart(@ApiParam(value = "统计类型，DAY：当天；MONTH：当月；ALL 累计", required = true, example = "DAY")
                                                     @NotNull(message = "统计类型不能为空") @RequestParam("type") StatType type) {
        return OperateResult.success(service.queryChart(type));
    }

    /**
     * 获取累计的菜单点击数量
     *
     * @return 统计数据
     */
    @ApiOperation(value = "获取累计的菜单点击数量", notes = "获取累计的菜单点击数量")
    @GetMapping(value = "/getTotalSum")
    public OperateResult<Long> getTotalSum() {
        return OperateResult.success(service.getTotalSum());
    }


    /**
     * 添加菜单点击日志操作。
     *
     * @param menuClickLogDTO 添加菜单点击日志对象。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "创建菜单点击日志", notes = "根据请求体中的菜单点击日志信息创建")
    @ApiOperationSupport(ignoreParameters = {"id"})
    @PostMapping(value = "/add")
    public OperateResult<String> add(@Validated(Groups.ADD.class) @RequestBody MenuClickLogDTO menuClickLogDTO) {
        MenuClickLog menuClickLog = ModelUtil.copyTo(menuClickLogDTO, MenuClickLog.class);
        UserVO user = UserContextHolder.getUser();
        menuClickLog.setClickDate(DateUtil.date());
        menuClickLog.setSource("tool");
        menuClickLog.setId(IdUtil.getSnowflakeNextId());
        if (user != null) {
            menuClickLog.setUserId(user.getId());
            menuClickLog.setUserBelong(user.getBelong());
            menuClickLog.setRegionCode(user.getRegionCode());
        }
        return service.save(menuClickLog) ? OperateResult.successMessage(ApplicationConstant.SAVE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_SAVE_FAILED);
    }

    /**
     * 添加菜单点击日志操作。
     *
     * @param menuClickLogInterface 添加菜单点击日志对象。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "接口调用创建菜单点击日志", notes = "接口调用创建菜单点击日志")
    @ApiOperationSupport(ignoreParameters = {"id"})
    @PostMapping(value = "/interfaceAdd")
    @AuthIgnore
    public OperateResult<String> interfaceAdd(@Validated(Groups.ADD.class) @RequestBody MenuClickLogInterface menuClickLogInterface) {
        MenuClickLog menuClickLog = ModelUtil.copyTo(menuClickLogInterface, MenuClickLog.class);
        Assert.notNull(menuClickLogInterface.getEmployeeNumber(), "当前用户人力账号（employeeNumber）为空");
        //解析获取用户的区域编码、用户ID、用户归属、
        UserVO userVO = userService.queryByEmployeeNumberForCache(menuClickLogInterface.getEmployeeNumber());
        Assert.notNull(userVO, "无法查询到人力账号为{}的用户", menuClickLogInterface.getEmployeeNumber());
        menuClickLog.setUserId(userVO.getId());
        menuClickLog.setRegionCode(userVO.getRegionCode());
        menuClickLog.setUserBelong(userVO.getBelong());


        menuClickLog.setClickDate(DateUtil.date());
        menuClickLog.setId(IdUtil.getSnowflakeNextId());
        return service.save(menuClickLog) ? OperateResult.successMessage(ApplicationConstant.SAVE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_SAVE_FAILED);
    }

    /**
     * 更新菜单点击日志操作。
     *
     * @param menuClickLogDTO 更新菜单点击日志对象。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "更新菜单点击日志", notes = "根据ID更新指定的菜单点击日志信息")
    @PostMapping(value = "/updateById")
    public OperateResult<String> updateById(@Validated(Groups.UPDATE.class) @RequestBody MenuClickLogDTO menuClickLogDTO) {
        MenuClickLog menuClickLog = ModelUtil.copyTo(menuClickLogDTO, MenuClickLog.class);
        return service.updateById(menuClickLog) ? OperateResult.successMessage(ApplicationConstant.UPDATE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_SAVE_FAILED);
    }

    /**
     * 删除指定的菜单点击日志。
     *
     * @param id 指定菜单点击日志主键Id。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "删除菜单点击日志", notes = "根据ID删除指定的菜单点击日志")
    @GetMapping(value = "/deleteById")
    public OperateResult<String> deleteById(@ApiParam(value = "菜单点击日志ID", required = true, example = "1")
                                            @NotNull(message = "菜单点击日志ID不能为空") @RequestParam("id") Long id) {
        return service.deleteById(id) ? OperateResult.successMessage(ApplicationConstant.DELETE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
    }

}