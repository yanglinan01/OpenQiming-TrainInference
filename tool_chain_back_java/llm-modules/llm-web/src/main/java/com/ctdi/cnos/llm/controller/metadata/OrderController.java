package com.ctdi.cnos.llm.controller.metadata;

import com.ctdi.cnos.llm.base.object.Groups;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.beans.meta.order.OrderVO;
import com.ctdi.cnos.llm.feign.metadata.OrderServiceClientFeign;
import com.ctdi.cnos.llm.response.OperateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author xuwj09
 * @version 1.0
 * @date 2024/12/18 17:12
 * @Description
 */
@Api(tags = {"门户权限工单接口"})
@RestController
@RequestMapping(value = "/order")
@Slf4j
@RequiredArgsConstructor
public class OrderController {

    private final OrderServiceClientFeign serviceClient;

    @ApiOperation(value = "分页查询门户工单完整数据", notes = "分页查询门户工单完整数据")
    @PostMapping(value = "/queryOrderAccountPage")
    public OperateResult<PageResult<OrderVO>> queryOrderAccountPage(@Validated(Groups.PAGE.class) @RequestBody QueryParam queryParam) {
        return serviceClient.queryOrderAccountPage(queryParam);
    }

    @ApiOperation(value = "提交待开通用户", notes = "提交待开通用户")
    @PostMapping(value = "/submitOrderAccount")
    public OperateResult<String> submitOrderAccount(@RequestBody List<OrderVO> orderVO){
        return serviceClient.submitOrderAccount(orderVO);
    }
}
