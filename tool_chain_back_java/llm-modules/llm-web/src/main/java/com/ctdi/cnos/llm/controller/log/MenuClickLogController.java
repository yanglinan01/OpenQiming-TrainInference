package com.ctdi.cnos.llm.controller.log;

import com.ctdi.cnos.llm.base.object.Groups;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.beans.log.menu.MenuClickLogDTO;
import com.ctdi.cnos.llm.beans.log.menu.MenuClickLogVO;
import com.ctdi.cnos.llm.feign.log.MenuClickLogServiceClientFeign;
import com.ctdi.cnos.llm.response.OperateResult;
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
 * 菜单点击日志服务远程数据操作访问接口。
 *
 * @author huangjinhua
 * @since 2024/10/16
 */
@Api(tags = "菜单点击日志接口", value = "MenuClickLogController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/menuClickLog")
public class MenuClickLogController {

    private final MenuClickLogServiceClientFeign serviceClient;

    /**
     * 分页查询符合条件的菜单点击日志列表。
     *
     * @param queryParam 查询对象。
     * @return 分页列表数据。
     */
    @ApiOperation(value = "分页查询符合条件的菜单点击日志数据", notes = "分页查询符合条件的菜单点击日志数据")
    @PostMapping(value = "/queryPage")
    public OperateResult<PageResult<MenuClickLogVO>> queryPage(@Validated(Groups.PAGE.class) @RequestBody QueryParam queryParam) {
        return this.serviceClient.queryPage(queryParam);
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
        return this.serviceClient.queryList(queryParam);
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
        return this.serviceClient.queryById(id);
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
        return this.serviceClient.add(menuClickLogDTO);
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
        return this.serviceClient.updateById(menuClickLogDTO);
    }

    /**
     * 删除指定的菜单点击日志。
     *
     * @param id 指定菜单点击日志主键Id。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "删除菜单点击日志", notes = "根据ID删除指定的菜单点击日志")
    @GetMapping(value = "/deleteById")
    public OperateResult<String> delete(@ApiParam(value = "菜单点击日志ID", required = true, example = "1")
                                        @NotNull(message = "菜单点击日志ID不能为空") @RequestParam("id") Long id) {
        return this.serviceClient.deleteById(id);
    }

}