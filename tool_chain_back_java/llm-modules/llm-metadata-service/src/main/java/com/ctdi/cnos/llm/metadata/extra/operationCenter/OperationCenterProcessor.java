/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.metadata.extra.operationCenter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.ctdi.cnos.llm.base.constant.MetaDataConstants;
import com.ctdi.cnos.llm.base.object.StatType;
import com.ctdi.cnos.llm.beans.meta.operationCenter.AbilityCallSituation;
import com.ctdi.cnos.llm.beans.meta.operationCenter.BarCharts;
import com.ctdi.cnos.llm.beans.meta.operationCenter.CallRank;
import com.ctdi.cnos.llm.beans.meta.operationCenter.OperationCenterStatVO;
import com.ctdi.cnos.llm.metadata.client.OperationCenterApiClient;
import com.ctdi.cnos.llm.system.province.entity.ProvinceVO;
import com.ctdi.cnos.llm.system.province.serivce.ProvinceService;
import com.ctdi.cnos.llm.system.user.entity.UserVO;
import com.ctdi.cnos.llm.system.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * OperationCenterProcess
 *
 * @author yinxinzhanchi
 * @since 2024/10/17
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OperationCenterProcessor {

    private final OperationCenterApiClient operationCenterApiClient;

    private final ProvinceService provinceService;

    private final UserService userService;

    /**
     * 平台应用总量接口
     *
     * @return OperationCenterStatVO
     */
    public OperationCenterStatVO agentTotal() {
        String response = operationCenterApiClient.agentTotal();
        OperationCenterStatVO statVO = new OperationCenterStatVO();
        statVO.setAgentCount(Convert.toLong(JSONPath.eval(response, "$.agent"), 0L));
        statVO.setWorkflowCount(Convert.toLong(JSONPath.eval(response, "$.workflow"), 0L));
        return statVO;
    }


    /**
     * 平台应用柱状图
     *
     * @param type 周期类型 DAY：当天；MONTH：当月；ALL 累计 (暂不支持当天)
     * @return List<BarCharts>
     */
    public List<BarCharts> agentCharts(StatType type) {
        String chartsResponse = operationCenterApiClient.agentCharts(type);
        List<BarCharts> list = new ArrayList<>();

        //获取31省，HQ归其他
        Map<String, String> map = provinceService.queryMapToName(null, null, ProvinceVO::getAbbreviation);
        //组装全量数据
        for (String belong : map.keySet()) {
            BarCharts charts = new BarCharts();
            if (CharSequenceUtil.equalsAnyIgnoreCase("HQ", belong)) {
                charts.setAbbreviation("OTHER");
                charts.setProvince("其他");
            } else {
                charts.setAbbreviation(belong);
                charts.setProvince(map.get(belong));
            }
            charts.setCount(0);
            list.add(charts);
        }

        Map<String, Integer> countMap = new HashMap<>(32);
        if (CharSequenceUtil.isNotBlank(chartsResponse)) {
            Object chartsData = JSONPath.eval(chartsResponse, "$.data");
            if (ObjectUtil.isNotNull(chartsData)) {
                Map<String, Integer> agentMap = Convert.toMap(String.class, Integer.class, chartsData);
                for (String itemKey : agentMap.keySet()) {
                    if (CharSequenceUtil.equalsAnyIgnoreCase("HQ", itemKey)
                            || CharSequenceUtil.isBlank(map.get(itemKey))) {
                        countMap.merge("OTHER", agentMap.get(itemKey), Integer::sum);
                    } else {
                        countMap.put(itemKey, agentMap.get(itemKey));
                    }
                }
            }
        }
        if (MapUtil.isNotEmpty(countMap)) {
            list = list.stream().peek(log -> {
                Integer sum = countMap.get(log.getAbbreviation());
                if (sum != null) {
                    log.setCount(sum);
                }
            }).collect(Collectors.toList());
        }
        //数据排序
        list.sort(Comparator.comparing(BarCharts::getCount).reversed().thenComparing(BarCharts::getAbbreviation));
        return list;
    }

    /**
     * 能力调用榜单
     *
     * @param source 来源   工具链: toolChain  智能体: agentplatform
     * @return List<CallRank>
     */
    public List<CallRank> callRank(String source) {
        List<CallRank> list = new ArrayList<>();
        String callRankResponse = operationCenterApiClient.callRank(source);
        if (CharSequenceUtil.isNotBlank(callRankResponse)) {
            JSONObject json = JSON.parseObject(callRankResponse);
            JSONArray callRankData = json.getJSONArray("data");
            if (callRankData != null && !callRankData.isEmpty()) {
                //获取所有省份
                Map<String, String> provincMap = provinceService.queryMapToName(null, null, ProvinceVO::getAbbreviation);
                for (int i = 0; i < callRankData.size(); i++) {
                    JSONObject item = callRankData.getJSONObject(i);
                    CallRank rank = new CallRank();
                    rank.setRank(i + 1)
                            .setName(item.getString("name"))
                            .setCallCount(item.getLong("requestCount"));
                    //获取用户信息
                    String createBy = item.getString("createBy");
                    UserVO userVO = userService.queryByEmployeeNumberForCache(createBy);
                    if (userVO != null) {
                        rank.setProvince(userVO.getBelong());
                        String provinceName = provincMap.get(userVO.getBelong());
                        rank.setProvinceName(CharSequenceUtil.isBlank(provinceName) || CharSequenceUtil.equals(userVO.getBelong(), "HQ") ? "其他" : provinceName);
                        rank.setCompany(userVO.getGroupBranch());
                        rank.setDeveloper(userVO.getName());
                    }
                    list.add(rank);
                }
            }
        }
        return list;
    }

    /**
     * 能力调用情况
     *
     * @return
     */
//    public Map<String, List<AbilityCallSituation>> queryCallQtyRank(String source) {
    public List<AbilityCallSituation> queryCallQtyRank(String source) {
        // 查询调用榜单
        String resultResp = operationCenterApiClient.queryCallQtyRank(source);

        List<AbilityCallSituation> resultList = dealAbilityCallSituation(resultResp);

//        // 查询应用调用榜单
//        String applyResult = operationCenterApiClient.queryCallQtyRank("");
//
//        List<AbilityCallSituation> applyList = dealAbilityCallSituation(applyResult);
//
//        Map<String, List<AbilityCallSituation>> resultMap = new HashMap<>();
//
//        if (modelList!= null) {
//            resultMap.put("modelList", modelList);
//        }
//
//        if (applyList!= null) {
//            resultMap.put("applyList", applyList);
//        }

        return resultList;
    }

    private List<AbilityCallSituation> dealAbilityCallSituation(String response) {
        List<AbilityCallSituation> abilityCallSituationList = CollUtil.newArrayList();
        String status = Convert.toStr(JSONPath.eval(response, "$.code"));
        if (CharSequenceUtil.isNotBlank(response) && CharSequenceUtil.equals(status, MetaDataConstants.API_SUCCESS)) {
            Map<String, String> provincMap = provinceService.queryMapToName(null, null, ProvinceVO::getAbbreviation);
            log.info("获取调用榜单返回值：{}", response);
            Object data = JSONPath.eval(response, "$.data");
            List<JSONObject> jsonObjectList = Convert.toList(JSONObject.class, data);
            for (int i = 0; i < jsonObjectList.size(); i++) {
                JSONObject jsonObj = jsonObjectList.get(i);
                if (!jsonObj.isEmpty()) {
                    AbilityCallSituation situation = new AbilityCallSituation();
                    //获取用户信息
                    String createBy = jsonObj.getString("createBy");
                    UserVO userVO = userService.queryByEmployeeNumber(createBy);
                    if (userVO != null) {
                        String provinceName = provincMap.get(userVO.getBelong());
                        situation.setUserName(userVO.getName());
                        situation.setProvince(userVO.getBelong());
                        situation.setProvinceName(CharSequenceUtil.isBlank(provinceName) || CharSequenceUtil.equals(userVO.getBelong(), "HQ") ? "其他" : provinceName);
                        situation.setCompany(userVO.getGroupBranch());
                    }
                    situation.setRequestCount(jsonObj.getInteger("requestCount"));
                    situation.setName(jsonObj.getString("name"));
                    abilityCallSituationList.add(situation);
                }
            }
            // 按照 requestCount 进行倒序排序
            Collections.sort(abilityCallSituationList, (s1, s2) -> s2.getRequestCount().compareTo(s1.getRequestCount()));
            // 重新设置 rank
            int rank = 1;
            for (AbilityCallSituation situation : abilityCallSituationList) {
                situation.setRank(rank++);
            }
        } else {
            log.error("获取调用榜单异常：{}", response);
        }
        return abilityCallSituationList;
    }

    /**
     * api接口注册数量
     *
     * @return
     */
    public OperationCenterStatVO apiTotal() {
        OperationCenterStatVO statVO = new OperationCenterStatVO();
        String response = operationCenterApiClient.apiTotal();
        if (CharSequenceUtil.isNotBlank(response)) {
            statVO.setApiCount(Convert.toLong(JSONPath.eval(response, "$.data").toString(), 0L));
        }
        return statVO;
    }

}
