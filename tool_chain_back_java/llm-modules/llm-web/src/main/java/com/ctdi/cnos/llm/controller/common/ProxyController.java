package com.ctdi.cnos.llm.controller.common;

import com.ctdi.cnos.llm.config.WebConfig;
import com.ctdi.cnos.llm.response.OperateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 代理服务 操作访问接口。
 *
 * @author laiqi
 * @since 2024/07/22
 */
@Api(tags = "代理服务接口", value = "ProxyController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/proxy")
public class ProxyController {

    private final WebConfig config;

    /**
     * 代理获取插件列表信息。
     *
     * @return 应答结果对象，包含查询结果集。
     */
    @ApiOperation(value = "获取插件列表信息", notes = "获取插件列表信息")
    @PostMapping(value = "/plugin/queryList")
    public OperateResult<String> pluginQueryList() {
        String body = "[{\"id\":\"code1\",\"name\":\"插件1\",\"description\":{\"zh_Hans\":\"这是中文描述1\"}},{\"id\":\"code2\",\"name\":\"插件2\",\"description\":{\"zh_Hans\":\"这是中文描述2\"}}]";
        /* body = HttpUtil.createGet(config.getPluginProperties().getPluginListUrl())
                .header(Constants.AUTH_HEADER_KEY, config.getPluginProperties().getAuthToke())
                .contentType(ContentType.JSON.getValue())
                .execute().body(); */
        return OperateResult.success(body);
    }
}