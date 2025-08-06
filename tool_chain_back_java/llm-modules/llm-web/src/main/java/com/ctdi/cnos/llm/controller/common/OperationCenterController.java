/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.controller.common;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.NumberUtil;
import com.ctdi.cnos.llm.base.constant.SystemConstant;
import com.ctdi.cnos.llm.base.constant.TrainConstants;
import com.ctdi.cnos.llm.base.object.StatType;
import com.ctdi.cnos.llm.beans.meta.operationCenter.AbilityCallSituation;
import com.ctdi.cnos.llm.beans.meta.operationCenter.BarCharts;
import com.ctdi.cnos.llm.beans.meta.operationCenter.CallRank;
import com.ctdi.cnos.llm.beans.meta.operationCenter.OperationCenterStatVO;
import com.ctdi.cnos.llm.feign.log.MenuClickLogServiceClientFeign;
import com.ctdi.cnos.llm.feign.log.UserLoginLogServiceClientFeign;
import com.ctdi.cnos.llm.feign.metadata.OperationCenterServiceClientFeign;
import com.ctdi.cnos.llm.feign.train.TrainTaskServiceClientFeign;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.system.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 运营中心 控制接口
 *
 * @author huangjinhua
 * @since 2024/10/16
 */
@Api(tags = {"运营中心接口"})
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/operationCenter")
@Slf4j
public class OperationCenterController {

    private final MenuClickLogServiceClientFeign menuClickLogServiceClient;

    private final TrainTaskServiceClientFeign trainTaskServiceClient;

    private final UserLoginLogServiceClientFeign userLoginLogServiceClient;

    private final UserService userService;

    private final OperationCenterServiceClientFeign operationCenterServiceClient;


    /**
     * 运营中心总览
     *
     * @return 统计数据
     */
    @ApiOperation(value = "运营中心总览", notes = "运营中心总览")
    @GetMapping(value = "/platformStat")
    public OperateResult<OperationCenterStatVO> platformStat() {
        OperationCenterStatVO statVO = new OperationCenterStatVO();
        Map<String, Long> trainStatusCount = trainTaskServiceClient.statusCount(SystemConstant.YES);
        OperationCenterStatVO agentTotal = Optional.ofNullable(operationCenterServiceClient.agentTotal().getData()).orElse(new OperationCenterStatVO());
        OperationCenterStatVO apiTotal = Optional.ofNullable(operationCenterServiceClient.apiTotal().getData()).orElse(new OperationCenterStatVO());
        //平台总使用量
        statVO.setPlatformUsedCount(NumberUtil.nullToZero(this.menuClickLogServiceClient.getTotalSum().getData()))
                .setPlatformUserCount(userService.countPlatformUser())
                .setCompanyCount(userService.countCompany())
                .setModelCount(NumberUtil.nullToZero(trainStatusCount.get(TrainConstants.TRAIN_TASK_STATUS_COMPLETED)))
                .setAgentCount(NumberUtil.nullToZero(agentTotal.getAgentCount()))
                .setWorkflowCount(NumberUtil.nullToZero(agentTotal.getWorkflowCount()))
                .setApiCount(NumberUtil.nullToZero(apiTotal.getApiCount()));

        return OperateResult.success(statVO);
    }

    /**
     * 统计平台用户登录数量。
     *
     * @param type day：日活；month：月活；
     * @return 统计数据
     */
    @ApiOperation(value = "统计平台用户登录数量", notes = "统计平台用户登录数量")
    @GetMapping(value = "/userLoginChart")
    public OperateResult<Map<String, Long>> userLoginChart(@ApiParam(value = "统计类型，DAY：日活；MONTH：月活", required = true, example = "DAY")
                                                           @NotNull(message = "统计类型不能为空") @RequestParam("type") StatType type) {
        return this.userLoginLogServiceClient.queryChart(type);
    }

    /**
     * 按省份维度统计各省(包括专业公司)的活跃度。
     *
     * @param type DAY：当天；MONTH：当月；ALL 累计
     * @return 统计数据
     */
    @ApiOperation(value = "按省份维度统计各省(包括专业公司)的活跃度", notes = "按省份维度统计各省(包括专业公司)的活跃度")
    @GetMapping(value = "/useLiveChart")
    public OperateResult<List<BarCharts>> useLiveChart(@ApiParam(value = "统计类型，DAY：当天；MONTH：当月；ALL 累计", required = true, example = "DAY")
                                                       @NotNull(message = "统计类型不能为空") @RequestParam("type") StatType type) {
        return this.menuClickLogServiceClient.queryChart(type);
    }


    /**
     * 按省份维度统计各省构建应用数量统计
     *
     * @param type DAY：当天；MONTH：当月；ALL 累计
     * @return 统计数据
     */
    @ApiOperation(value = "按省份维度统计各省构建应用数量统计", notes = "按省份维度统计各省构建应用数量统计")
    @GetMapping(value = "/agentCharts")
    public OperateResult<List<BarCharts>> agentCharts(@ApiParam(value = "统计类型，DAY：当天；MONTH：当月；ALL 累计", required = true, example = "DAY")
                                                      @NotNull(message = "统计类型不能为空") @RequestParam("type") StatType type) {
        Assert.isFalse(StatType.DAY.equals(type),"暂不支持类型{}",type);
        return this.operationCenterServiceClient.agentCharts(type);
    }

    /**
     * 按省份维度统计统计各省模型构建数量
     *
     * @param type DAY：当天；MONTH：当月；ALL 累计
     * @return 统计数据
     */
    @ApiOperation(value = "按省份维度统计统计各省模型构建数量", notes = "按省份维度统计统计各省模型构建数量")
    @GetMapping("/modelCharts")
    public OperateResult<List<BarCharts>> modelCharts(@ApiParam(value = "统计类型，DAY：当天；MONTH：当月；ALL 累计", required = true, example = "DAY")
                                                          @NotNull(message = "统计类型不能为空") @RequestParam("type") StatType type) {
        Assert.isFalse(StatType.DAY.equals(type),"暂不支持类型{}",type);
        return OperateResult.success(this.trainTaskServiceClient.countTrainTaskGroupByProvince(type));
    }
    //    /**
//     * 查询能力调用情况
//     * @return
//     */
//    @ApiOperation(value = "查询能力调用情况", notes = "查询能力调用情况")
//    @PostMapping(value = "/queryCallQtyRank")
//    public OperateResult<Map<String, List<AbilityCallSituation>>> queryCallQtyRank(){
//        return operationCenterServiceClient.queryCallQtyRank();
//    }

    /**
     * 查询能力调用情况
     * @return
     */
    @ApiOperation(value = "查询能力调用情况", notes = "查询能力调用情况")
    @PostMapping(value = "/queryCallQtyRank")
    public OperateResult<List<AbilityCallSituation>> queryCallQtyRank(@ApiParam(value = "来源 工具链: toolChain  智能体: agentplatform", required = true, example = "toolChain")
                                                                      @NotNull(message = "统计类型不能为空") @RequestParam("source") String source){
        return operationCenterServiceClient.queryCallQtyRank(source);
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
        return this.operationCenterServiceClient.callRank(source);

    }





}