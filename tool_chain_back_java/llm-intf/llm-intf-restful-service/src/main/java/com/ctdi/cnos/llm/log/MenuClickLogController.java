package com.ctdi.cnos.llm.log;

import com.alibaba.fastjson.JSON;
import com.ctdi.cnos.llm.annotation.AuthIgnore;
import com.ctdi.cnos.llm.base.object.Groups;
import com.ctdi.cnos.llm.beans.log.menu.MenuClickLogInterface;
import com.ctdi.cnos.llm.feign.log.MenuClickLogServiceClientFeign;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.util.MessageUtils;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huangjinhua
 * @since 2024/10/16
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/menuClickLog")
@Api(tags = {"菜单点击日志记录接口"})
@Slf4j
public class MenuClickLogController {


    private final MenuClickLogServiceClientFeign menuClickLogServiceClientFeign;

    /**
     * 新增菜单点击日志接口
     *
     * @param menuClickLog 菜单点击日志对象
     * @return OperateResult<String>
     */
    @AuthIgnore
    @PostMapping("/addInterface")
    public OperateResult<String> addInterface(@Validated(Groups.ADD.class) @RequestBody MenuClickLogInterface menuClickLog) {
        try {
            log.info("新增菜单点击日志接口:{}", JSON.toJSONString(menuClickLog));
            return menuClickLogServiceClientFeign.interfaceAdd(menuClickLog);
        } catch (Exception exception) {
            log.error("新增菜单点击日志接口", exception);
            return OperateResult.error(MessageUtils.getMessage(exception.getMessage()));
        }
    }
}