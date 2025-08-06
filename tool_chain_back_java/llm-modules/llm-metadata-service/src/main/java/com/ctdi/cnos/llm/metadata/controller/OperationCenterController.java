package com.ctdi.cnos.llm.metadata.controller;

import com.ctdi.cnos.llm.base.object.StatType;
import com.ctdi.cnos.llm.beans.meta.operationCenter.AbilityCallSituation;
import com.ctdi.cnos.llm.beans.meta.operationCenter.BarCharts;
import com.ctdi.cnos.llm.beans.meta.operationCenter.CallRank;
import com.ctdi.cnos.llm.beans.meta.operationCenter.OperationCenterStatVO;
import com.ctdi.cnos.llm.metadata.extra.operationCenter.OperationCenterProcessor;
import com.ctdi.cnos.llm.response.OperateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 集群资源 控制器类。
 *
 * @author huangjinhua
 * @since 2024/10/17
 */
@Api(tags = "运营中心", value = "运营中心 控制类")
@RequiredArgsConstructor
@RestController
@RequestMapping("/operationCenter")
public class OperationCenterController {

    private final OperationCenterProcessor operationCenterProcessor;

    /**
     * 平台应用总量
     *
     * @return 平台应用总量信息。
     */
    @ApiOperation(value = "平台应用总量", notes = "平台应用总量")
    @GetMapping(value = "/agentTotal")
    public OperateResult<OperationCenterStatVO> agentTotal() {
        return OperateResult.success(operationCenterProcessor.agentTotal());
    }


    /**
     * 平台应用柱状图
     *
     * @param type 统计类型
     * @return List<BarCharts>
     */
    @ApiOperation(value = "平台应用柱状图", notes = "平台应用柱状图")
    @GetMapping(value = "/agentCharts")
    public OperateResult<List<BarCharts>> agentCharts(@ApiParam(value = "统计类型，DAY：当天；MONTH：当月；ALL 累计", required = true, example = "1")
                                                      @NotNull(message = "统计类型不能为空") @RequestParam("type") StatType type) {
        return OperateResult.success(operationCenterProcessor.agentCharts(type));

    }

//    /**
//     * 查询能力调用情况
//     * @return
//     */
//    @ApiOperation(value = "查询能力调用情况", notes = "查询能力调用情况")
//    @PostMapping(value = "/queryCallQtyRank")
//    public OperateResult<Map<String, List<AbilityCallSituation>>> queryCallQtyRank(@ApiParam(value = "来源 工具链: toolChain  智能体: agentplatform", required = true, example = "toolChain")
//                                                                                       @NotNull(message = "统计类型不能为空") @RequestParam("source") String source){
//        Map<String, List<AbilityCallSituation>> resultMap =  operationCenterProcessor.queryCallQtyRank(source);
//        return OperateResult.success(operationCenterProcessor.queryCallQtyRank(source));
//    }

    /**
     * 查询能力调用情况
     * @return
     */
    @ApiOperation(value = "查询能力调用情况", notes = "查询能力调用情况")
    @PostMapping(value = "/queryCallQtyRank")
    public OperateResult<List<AbilityCallSituation>> queryCallQtyRank(@ApiParam(value = "来源 工具链: toolChain  智能体: agentplatform", required = true, example = "toolChain")
                                                                                   @NotNull(message = "统计类型不能为空") @RequestParam("source") String source){
        return OperateResult.success(operationCenterProcessor.queryCallQtyRank(source));
    }

    /**
     * 能力调用榜单
     *
     * @param source 来源   工具链: toolChain  智能体: agentplatform
     * @return List<CallRank>
     */

    @ApiOperation(value = "能力调用榜单", notes = "能力调用榜单")
    @GetMapping(value = "/callRank")
    public OperateResult<List<CallRank>> callRank(@ApiParam(value = "来源 工具链: toolChain  智能体: agentplatform", required = true, example = "toolChain")
                                                  @NotNull(message = "统计类型不能为空") @RequestParam("source") String source) {
        return OperateResult.success(operationCenterProcessor.callRank(source));

    }

    /**
     * api接口注册数量
     * @return
     */
    @ApiOperation(value = "api接口注册数量", notes = "api接口注册数量")
    @GetMapping(value = "/apiTotal")
    public OperateResult<OperationCenterStatVO> apiTotal() {
        return OperateResult.success(operationCenterProcessor.apiTotal());
    }

}