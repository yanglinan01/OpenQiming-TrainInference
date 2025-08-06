package com.ctdi.cnos.llm.metadata.client;

import com.ctdi.cnos.llm.base.object.StatType;
import com.ctdi.cnos.llm.metadata.config.ApplicationConfig;
import com.ctdi.cnos.llm.metadata.config.OperationCenter;
import com.dtflys.forest.Forest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 运营中心 ApiClient
 *
 * @author yuyong
 * @date 2024/10/17 10:53
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class OperationCenterApiClient {

    private final ApplicationConfig config;


    /**
     * 平台应用总量接口
     */
    private static final String AGENT_TOTAL_URI = "/interface/api/statistics/app-count";
    /**
     * 平台应用柱状图数据接口
     */
    private final String AGENT_PROVINCE_CHARTS_URI = "/interface/api/statistics/app-count-province";
    /**
     * 能力调用 榜单接口
     */
    private final String CALL_RAND_URI = "/ApiController/callQtyRank";
    /**
     * API注册数量
     */
    private final String API_URI = "/ApiController/getCount";


    private OperationCenter operationCenterConfig() {
        return config.getOperationCenter();
    }

    /**
     * 平台应用总量接口
     *
     * @return String
     */
    public String agentTotal() {
        OperationCenter operationCenter = operationCenterConfig();
        String url = operationCenter.getAgentHost() + AGENT_TOTAL_URI;
        log.info("请求url：{},是否是模拟数据：{}", url, operationCenter.isMock());
        return operationCenter.isMock() ? operationCenter.getMockData().get("平台应用总量接口") : Forest.get(url).executeAsString();
    }


    /**
     * 平台应用柱状图
     *
     * @param type 统计类型
     * @return String
     */
    public String agentCharts(StatType type) {
        //接口获取数据
        OperationCenter operationCenter = operationCenterConfig();
        String url = operationCenter.getAgentHost() + AGENT_PROVINCE_CHARTS_URI;
        log.info("请求url：{}，type：{},是否是模拟数据：{}", url, type, operationCenter.isMock());
        return operationCenter.isMock() ? operationCenter.getMockData().get("平台应用柱状图") : Forest.get(url).addQuery("type", type).executeAsString();
    }

    /**
     * 查询能力调用情况
     *
     * @param source
     * @return
     */
    public String queryCallQtyRank(String source) {
        OperationCenter operationCenter = operationCenterConfig();
        String resourceUrl = operationCenter.getWorkflowApiHost() + CALL_RAND_URI;
        log.info("请求url：{}，来源类型：{},是否是模拟数据：{}", resourceUrl, source, operationCenter.isMock());
        return operationCenter.isMock() ? operationCenter.getMockData().get("能力调用榜单") : Forest.get(resourceUrl).addQuery("source", source).executeAsString();
    }

    /**
     * 能力调用榜单
     *
     * @param source 来源   工具链: toolChain  智能体: agentplatform
     * @return String
     */
    public String callRank(String source) {
        //接口获取数据
        OperationCenter operationCenter = operationCenterConfig();
        String url = operationCenter.getWorkflowApiHost() + CALL_RAND_URI;
        log.info("请求url：{}，来源类型：{},是否是模拟数据：{}", url, source, operationCenter.isMock());
        return operationCenter.isMock() ? operationCenter.getMockData().get("能力调用榜单") : Forest.get(url).addQuery("source", source).executeAsString();
    }

    /**
     * api接口注册数量
     *
     * @return api信息
     */
    public String apiTotal() {
        OperationCenter operationCenter = operationCenterConfig();
        String totalCount = "";
        String url = operationCenter.getWorkflowApiHost() + API_URI;
        log.info("请求url：{},是否是模拟数据：{}", url, operationCenter.isMock());
        return operationCenter.isMock() ? operationCenter.getMockData().get("注册接口数量") : Forest.get(url).executeAsString();
    }

}
