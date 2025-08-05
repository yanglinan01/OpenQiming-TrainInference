package com.ctdi.cnos.llm.feign.log;

import com.ctdi.cnos.llm.RemoteConstont;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.base.object.StatType;
import com.ctdi.cnos.llm.beans.log.menu.MenuClickLogDTO;
import com.ctdi.cnos.llm.beans.log.menu.MenuClickLogInterface;
import com.ctdi.cnos.llm.beans.log.menu.MenuClickLogVO;
import com.ctdi.cnos.llm.beans.meta.operationCenter.BarCharts;
import com.ctdi.cnos.llm.response.OperateResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 菜单点击日志服务远程数据操作访问接口。
 *
 * @author huangjinhua
 * @since 2024/10/16
 */
@Component
@FeignClient(value = RemoteConstont.LOG_SERVICE_NAME, path = "${cnos.server.llm-log-service.contextPath}")
public interface MenuClickLogServiceClientFeign {

    /**
     * 分页查询符合条件的菜单点击日志列表。
     *
     * @param queryParam 查询对象。
     * @return 分页列表数据。
     */
    @PostMapping(value = "/menuClickLog/queryPage")
    OperateResult<PageResult<MenuClickLogVO>> queryPage(@RequestBody QueryParam queryParam);

    /**
     * 查询符合条件的菜单点击日志列表。
     *
     * @param queryParam 查询对象。
     * @return 列表数据。
     */
    @PostMapping(value = "/menuClickLog/queryList")
    OperateResult<List<MenuClickLogVO>> queryList(@RequestBody QueryParam queryParam);

    /**
     * 查询指定菜单点击日志数据。
     *
     * @param id 指定菜单点击日志主键Id。
     * @return 单条数据。
     */
    @GetMapping(value = "/menuClickLog/queryById")
    OperateResult<MenuClickLogVO> queryById(@RequestParam("id") Long id);

    /**
     * 根据统计类型统计省份维度数据画图。
     *
     * @param type DAY：当天；MONTH：当月；ALL 累计
     * @return 统计数据
     */
    @GetMapping(value = "/menuClickLog/queryChart")
    OperateResult<List<BarCharts>> queryChart(@RequestParam("type") StatType type);

    /**
     * 获取累计的菜单点击数量
     *
     * @return 统计数据
     */
    @GetMapping(value = "/menuClickLog/getTotalSum")
    OperateResult<Long> getTotalSum();

    /**
     * 添加菜单点击日志操作。
     *
     * @param menuClickLogDTO 添加菜单点击日志对象。
     * @return 应答结果对象。
     */
    @PostMapping(value = "/menuClickLog/add")
    OperateResult<String> add(@RequestBody MenuClickLogDTO menuClickLogDTO);

    /**
     * 添加菜单点击日志操作。
     *
     * @param menuClickLogInterface 添加菜单点击日志对象。
     * @return 应答结果对象。
     */
    @PostMapping(value = "/menuClickLog/interfaceAdd")
    OperateResult<String> interfaceAdd(@RequestBody MenuClickLogInterface menuClickLogInterface);

    /**
     * 更新菜单点击日志操作。
     *
     * @param menuClickLogDTO 更新菜单点击日志对象。
     * @return 应答结果对象。
     */
    @PostMapping(value = "/menuClickLog/updateById")
    OperateResult<String> updateById(@RequestBody MenuClickLogDTO menuClickLogDTO);

    /**
     * 删除指定的菜单点击日志。
     *
     * @param id 指定菜单点击日志主键Id。
     * @return 应答结果对象。
     */
    @GetMapping(value = "/menuClickLog/deleteById")
    OperateResult<String> deleteById(@RequestParam("id") Long id);

}