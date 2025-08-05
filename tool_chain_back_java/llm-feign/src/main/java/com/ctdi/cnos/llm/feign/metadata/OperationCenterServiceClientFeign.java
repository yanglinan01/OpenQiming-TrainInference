package com.ctdi.cnos.llm.feign.metadata;

import com.ctdi.cnos.llm.RemoteConstont;
import com.ctdi.cnos.llm.base.object.StatType;
import com.ctdi.cnos.llm.beans.meta.operationCenter.AbilityCallSituation;
import com.ctdi.cnos.llm.beans.meta.operationCenter.BarCharts;
import com.ctdi.cnos.llm.beans.meta.operationCenter.CallRank;
import com.ctdi.cnos.llm.beans.meta.operationCenter.OperationCenterStatVO;
import com.ctdi.cnos.llm.response.OperateResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 集群资源服务远程数据操作访问接口。
 *
 * @author huangjinhua
 * @since 2024/09/24
 */
@Component
@FeignClient(value = RemoteConstont.METADATA_SERVICE_NAME, path = "${cnos.server.llm-metadata-service.contextPath}")
public interface OperationCenterServiceClientFeign {


    /**
     * 平台应用总量
     *
     * @return 平台应用总量信息。
     */
    @GetMapping(value = "/operationCenter/agentTotal")
    OperateResult<OperationCenterStatVO> agentTotal();


    /**
     * 平台应用柱状图
     *
     * @param type 统计类型 DAY：当天；MONTH：当月；ALL 累计
     * @return List<BarCharts>
     */
    @GetMapping(value = "/operationCenter/agentCharts")
    OperateResult<List<BarCharts>> agentCharts(@RequestParam("type") StatType type);

    /**
     * 能力调用情况
     * @return
     */
    @PostMapping(value = "/operationCenter/queryCallQtyRank")
//    OperateResult<Map<String, List<AbilityCallSituation>>> queryCallQtyRank(@RequestParam("source") String source);
    OperateResult<List<AbilityCallSituation>> queryCallQtyRank(@RequestParam("source") String source);

    /**
     * 能力调用榜单
     *
     * @param source 来源   工具链: toolChain  智能体: agentplatform
     * @return List<CallRank>
     */
    @GetMapping(value = "/operationCenter/callRank")
    OperateResult<List<CallRank>> callRank(@RequestParam("source") String source);

    /**
     * api接口注册数量
     * @return
     */
    @ApiOperation(value = "api接口注册数量", notes = "api接口注册数量")
    @GetMapping(value = "/operationCenter/apiTotal")
    OperateResult<OperationCenterStatVO> apiTotal();
}