/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.metadata.client;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.ctdi.cnos.llm.base.constant.MetaDataConstants;
import com.ctdi.cnos.llm.beans.meta.metric.ClusterNode;
import com.ctdi.cnos.llm.beans.meta.metric.ClusterNodeCard;
import com.ctdi.cnos.llm.beans.meta.metric.Metric;
import com.ctdi.cnos.llm.beans.meta.metric.MetricResult;
import com.ctdi.cnos.llm.beans.meta.metric.MetricValue;
import com.dtflys.forest.Forest;
import com.dtflys.forest.http.ForestRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 资源管理 ApiClient
 *
 * @author huangjinhua
 * @since 2024/9/23
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class MetricApiClient {

    /**
     * prometheus 监控指标查询uri
     */
    private static final String API_QUERY_URI = "/api/v1/query";
    /**
     * prometheus 监控指标范围查询uri
     */
    private static final String API_QUERY_RANGE_URI = "/api/v1/query_range";
    /**
     * k8s 节点查询接口uri
     */
    private static final String NODES_URI = "/nodes/info";
    /**
     * 推理任务所占节点及所用卡数
     */
    private static final String TASK_URI = "/task/info";

    /**
     * 查询多个指标数据，一个指标对应一个多个值（多个值不会覆盖） - 每个指标都有
     *
     * @param metricList  CollUtil.newArrayList(
     *                    "sum(nvidia_gpu_memory_total_bytes)",//GPU显存总量
     *                    "sum(nvidia_gpu_memory_used_bytes)") //GPU显存使用量
     * @param resourceUrl 青岛：http://10.214.8.14:20000 贵州： http://10.238.190.45:20000
     * @return 指标值信息  {"sum(nvidia_gpu_memory_used_bytes)":[{"metric":{"container":"nvidia-exporter","endpoint":"http-metrics","instance":"10.214.8.16:9445","job":"nvidia-exporter","minor_number":"0","name":"NVIDIA A10","namespace":"monitoring","pod":"nvidia-exporter-vgxhg","service":"nvidia-exporter","uuid":"GPU-47d3b890-840b-ab6b-0e26-3bcdbf4e01d0"},"value":[{"time":1727090531000,"value":"325878939648"}]}]}
     */
    public <T> Map<String, List<MetricResult<T>>> queryMultiMetric(List<String> metricList, String resourceUrl, Class<T> clazz) {
        Map<String, List<MetricResult<T>>> map = MapUtil.newHashMap(true);
        if (CollUtil.isNotEmpty(metricList) && CharSequenceUtil.isNotBlank(resourceUrl)) {
            resourceUrl = resourceUrl + API_QUERY_URI;
            for (String queryMetric : metricList) {
                List<MetricResult<T>> list = CollUtil.newArrayList();
                String response = Forest.get(resourceUrl).addQuery("query", queryMetric).executeAsString();
                log.info("{}指标查询返回值：{}", queryMetric, response);
                Object eval = JSONPath.eval(response, "$.data.result");
                if (eval instanceof JSONArray) {
                    JSONArray array = (JSONArray) eval;
                    for (int i = 0; i < array.size(); i++) {
                        MetricResult<T> metricResult = new MetricResult<>();
                        JSONObject item = array.getJSONObject(i);
                        Metric metric = JSON.toJavaObject(item.getJSONObject("metric"), Metric.class);
                        metricResult.setMetric(metric);
                        metricResult.setInstance(metric.getInstance());
                        List<MetricValue<T>> metricValueList = CollUtil.newArrayList();
                        JSONArray value = item.getJSONArray("value");
                        JSONArray values = item.getJSONArray("values");
                        if (value != null && !value.isEmpty()) {
                            MetricValue<T> metricValue = new MetricValue<>();
                            metricValue.setTime(DateUtil.date(value.getTimestamp(0).getTime() * 1000L));
                            metricValue.setValue(value.getObject(1, clazz));
                            metricValueList.add(metricValue);
                        } else if (values != null && !values.isEmpty()) {
                            for (int j = 0; j < values.size(); j++) {
                                JSONArray valueItem = values.getJSONArray(j);
                                MetricValue<T> metricValue = new MetricValue<>();
                                metricValue.setTime(DateUtil.date(valueItem.getTimestamp(0).getTime() * 1000L));
                                metricValue.setValue(valueItem.getObject(1, clazz));
                                metricValueList.add(metricValue);
                            }
                        }
                        metricResult.setValue(metricValueList);
                        list.add(metricResult);
                    }
                }
                map.put(queryMetric, list);
            }
        }
        return map;
    }


    /**
     * 查询多个指标数据，一个指标对应一个值（多个值会覆盖） - 没有值的会丢弃
     *
     * @param metricList  CollUtil.newArrayList(
     *                    "sum(nvidia_gpu_memory_total_bytes)",//GPU显存总量
     *                    "sum(nvidia_gpu_memory_used_bytes)") //GPU显存使用量
     * @param resourceUrl 青岛：http://10.214.8.14:20000 贵州： http://10.238.190.45:20000
     * @return 指标值信息  {"sum(nvidia_gpu_memory_used_bytes)":"325878939648"}
     */
    public <T> Map<String, T> queryMultiMetricValue(List<String> metricList, String resourceUrl, Class<T> clazz) {
        Map<String, T> map = MapUtil.newHashMap(true);
        if (CollUtil.isNotEmpty(metricList) && CharSequenceUtil.isNotBlank(resourceUrl)) {
            resourceUrl = resourceUrl + API_QUERY_URI;
            for (String queryMetric : metricList) {
                String response = Forest.get(resourceUrl).addQuery("query", queryMetric).executeAsString();
                log.info("{}指标查询返回值：{}", queryMetric, response);
                Object eval = JSONPath.eval(response, "$.data.result");
                T resultValue = null;
                if (eval instanceof JSONArray) {
                    JSONArray array = (JSONArray) eval;
                    for (int i = 0; i < array.size(); i++) {
                        JSONObject item = array.getJSONObject(i);
                        JSONArray value = item.getJSONArray("value");
                        if (value != null && !value.isEmpty()) {
                            resultValue = value.getObject(1, clazz);
                        }
                    }
                }
                map.put(queryMetric, resultValue);
            }
        }
        return map;
    }

    /**
     * 查询单个指标数据，一个指标对应多个采集数据
     *
     * @param metric      nvidia_gpu_memory_total_bytes //GPU显存总量
     * @param resourceUrl 青岛：http://10.214.8.14:20000 贵州： http://10.238.190.45:20000
     * @return 指标值信息  [{"time":1727090531000,"value":"325878939648"}]
     */
    public <T> List<MetricValue<T>> querySingleMetricValue(String metric, String resourceUrl, Class<T> clazz) {
        List<MetricValue<T>> metricValueList = CollUtil.newArrayList();
        if (CharSequenceUtil.isNotBlank(metric) && CharSequenceUtil.isNotBlank(resourceUrl)) {
            resourceUrl = resourceUrl + API_QUERY_URI;
            String response = Forest.get(resourceUrl).addQuery("query", metric).executeAsString();
            log.info("{}指标查询返回值：{}", metric, response);
            Object eval = JSONPath.eval(response, "$.data.result");
            if (eval instanceof JSONArray) {
                JSONArray array = (JSONArray) eval;
                for (int i = 0; i < array.size(); i++) {
                    JSONObject item = array.getJSONObject(i);
                    JSONArray value = item.getJSONArray("value");
                    JSONArray values = item.getJSONArray("values");
                    if (value != null && !value.isEmpty()) {
                        MetricValue<T> metricValue = new MetricValue<>();
                        metricValue.setTime(DateUtil.date(value.getTimestamp(0).getTime() * 1000L));
                        metricValue.setValue(value.getObject(1, clazz));
                        metricValueList.add(metricValue);
                    } else if (values != null && !values.isEmpty()) {
                        for (int j = 0; j < values.size(); j++) {
                            JSONArray valueItem = values.getJSONArray(j);
                            MetricValue<T> metricValue = new MetricValue<>();
                            metricValue.setTime(DateUtil.date(valueItem.getTimestamp(0).getTime() * 1000L));
                            metricValue.setValue(valueItem.getObject(1, clazz));
                            metricValueList.add(metricValue);
                        }
                    }
                }
            }
        }
        return metricValueList;
    }

    /**
     * 查询集群所有节点以及每个节点对应的标签
     *
     * @param resourceUrl 青岛：http://10.214.8.14:20000 贵州： http://10.238.190.45:20000
     * @return 指标值信息  [{"name":"node01","train":"true","inference":"false"}]
     */
    public List<ClusterNode> queryNodeTrainAndInference(String resourceUrl) {
        List<ClusterNode> clusterNodeList = CollUtil.newArrayList();
        if (CharSequenceUtil.isNotBlank(resourceUrl)) {
            resourceUrl = resourceUrl + NODES_URI;
            String response = Forest.get(resourceUrl).executeAsString();
            String status = Convert.toStr(JSONPath.eval(response, "$.status"));
            if (CharSequenceUtil.isNotBlank(response) && CharSequenceUtil.equals(status, MetaDataConstants.API_SUCCESS_RESPONSE)) {
                log.info("{}集群所有节点以及每个节点对应的标签返回值：{}", resourceUrl, response);
                Object data = JSONPath.eval(response, "$.data");
                List<JSONObject> jsonObjectList = Convert.toList(JSONObject.class, data);
                for (int i = 0; i < jsonObjectList.size(); i++) {
                    JSONObject jsonObj = jsonObjectList.get(i);
                    if (!jsonObj.isEmpty()) {
                        ClusterNode node = new ClusterNode();
                        node.setName(jsonObj.getString("node_name"));
                        node.setIp(jsonObj.getString("node_ip"));
                        JSONObject nodeLabels = jsonObj.getJSONObject("node_labels");
                        if (!jsonObj.isEmpty()) {
                            node.setTrain(nodeLabels.getBooleanValue("train"));
                            node.setInference(nodeLabels.getBooleanValue("inference"));
                        }
                        clusterNodeList.add(node);
                    }
                }
            } else {
                log.error("{}集群所有节点以及每个节点对应的标签异常：{}", resourceUrl, response);
            }
        }
        return clusterNodeList;
    }

    /**
     * 范围查询单个指标数据，一个指标对应多个采集数据
     *
     * @param metric      nvidia_gpu_memory_total_bytes //GPU显存总量
     * @param start       开始时间
     * @param end         结束时间
     * @param step        步长(3600s, 5m, 1h...)
     * @param resourceUrl 青岛：http://10.214.8.14:20000/api/v1/query 贵州： http://10.238.190.45:20000/api/v1/query
     * @return 指标值信息  [{"time":1727090531000,"value":"325878939648"}]
     */
    public <T> List<MetricValue<T>> queryRange(String metric, Date start, Date end, String step, String resourceUrl, Class<T> clazz) {
        List<MetricValue<T>> metricValueList = CollUtil.newArrayList();
        if (CharSequenceUtil.isNotBlank(metric) && CharSequenceUtil.isNotBlank(resourceUrl)) {
            resourceUrl = resourceUrl + API_QUERY_RANGE_URI;
            ForestRequest<?> query = Forest.get(resourceUrl).addQuery("query", metric);
            if (Objects.nonNull(start)) {
                query.addQuery("start", start.toInstant().getEpochSecond());
            }
            if (Objects.nonNull(end)) {
                query.addQuery("end", end.toInstant().getEpochSecond());
            }
            if (CharSequenceUtil.isNotEmpty(step)) {
                query.addQuery("step", step);
            }
            String response = query.executeAsString();
            log.info("{}范围指标查询返回值：{}", metric, response);
            Object eval = JSONPath.eval(response, "$.data.result");
            if (eval instanceof JSONArray) {
                JSONArray array = (JSONArray) eval;
                for (int i = 0; i < array.size(); i++) {
                    JSONObject item = array.getJSONObject(i);
                    JSONArray value = item.getJSONArray("value");
                    JSONArray values = item.getJSONArray("values");
                    if (value != null && !value.isEmpty()) {
                        MetricValue<T> metricValue = new MetricValue<>();
                        metricValue.setTime(DateUtil.date(value.getTimestamp(0).getTime() * 1000L));
                        metricValue.setValue(value.getObject(1, clazz));
                        metricValueList.add(metricValue);
                    } else if (values != null && !values.isEmpty()) {
                        for (int j = 0; j < values.size(); j++) {
                            JSONArray valueItem = values.getJSONArray(j);
                            MetricValue<T> metricValue = new MetricValue<>();
                            metricValue.setTime(DateUtil.date(valueItem.getTimestamp(0).getTime() * 1000L));
                            metricValue.setValue(valueItem.getObject(1, clazz));
                            metricValueList.add(metricValue);
                        }
                    }
                }
            }
        }
        return metricValueList;
    }

    /**
     * 两个指标值列表，使用提供的函数进行自定义操作
     * <pre>{@code
     *         // 示例数据
     *         List<MetricValue<Double>> list1 = IntStream.range(0, 10)
     *                 .mapToObj(i -> {
     *                     MetricValue<Double> metricValue = new MetricValue<>();
     *                     metricValue.setTime(new Date(System.currentTimeMillis() - i * 3600 * 1000));
     *                     metricValue.setValue((double) i);
     *                     return metricValue;
     *                 })
     *                 .collect(Collectors.toList());
     *         System.out.println(JSON.toJSONString(list1));
     *
     *         List<MetricValue<Double>> list2 = IntStream.range(0, 10)
     *                 .mapToObj(i -> {
     *                     MetricValue<Double> metricValue = new MetricValue<>();
     *                     metricValue.setTime(new Date(System.currentTimeMillis() - i * 3600 * 1000));
     *                     metricValue.setValue((double) (i + 10));
     *                     return metricValue;
     *                 })
     *                 .collect(Collectors.toList());
     *         System.out.println(JSON.toJSONString(list2));
     *         // 累加
     *         List<MetricValue<Double>> aggregatedValues = aggregateValues(list1, list2, Double::sum);
     *         // // 打印结果
     *         aggregatedValues.forEach(doubleMetricValue -> System.out.println(JSON.toJSONString(doubleMetricValue)));
     * }</pre>
     *
     * @param list1    列表1
     * @param list2    列表2
     * @param function 处理函数
     * @param <T>      值类型
     * @return 累加后的指标值列表
     */
    public static <T> List<MetricValue<T>> aggregateValues(List<MetricValue<T>> list1, List<MetricValue<T>> list2, BiFunction<T, T, T> function) {
        if (CollUtil.isEmpty(list1)) {
            return list2;
        }
        if (CollUtil.isEmpty(list2)) {
            return list1;
        }

        // 确定两个列表长度的最小值
        int size = Math.min(list1.size(), list2.size());

        // 截取较长列表的最后 `size` 个元素
        List<MetricValue<T>> truncatedList1 = list1.size() > size ? list1.subList(list1.size() - size, list1.size()) : list1;
        List<MetricValue<T>> truncatedList2 = list2.size() > size ? list2.subList(list2.size() - size, list2.size()) : list2;

        // 使用流式 API 进行累加
        return IntStream.range(0, size)
                .mapToObj(i -> {
                    MetricValue<T> metricValue1 = truncatedList1.get(i);
                    MetricValue<T> metricValue2 = truncatedList2.get(i);

                    // 创建新的 MetricValue 对象并累加值
                    MetricValue<T> aggregatedValue = new MetricValue<>();
                    aggregatedValue.setTime(metricValue1.getTime());
                    aggregatedValue.setValue(function.apply(metricValue1.getValue(), metricValue2.getValue()));
                    return aggregatedValue;
                })
                .collect(Collectors.toList());
    }

    /**
     * 获取训练推理任务所占节点及所用卡数
     *
     * @param resourceUrl
     * @param bodyJson
     * @return
     */
    public List<ClusterNodeCard> queryNodeCardTrainAndInference(String resourceUrl, JSONObject bodyJson) {
        List<ClusterNodeCard> clusterNodeList = CollUtil.newArrayList();
        if (CharSequenceUtil.isNotBlank(resourceUrl)) {
            resourceUrl = resourceUrl + TASK_URI;
            String response = Forest.post(resourceUrl).contentTypeJson().addBody(bodyJson).executeAsString();
            String status = Convert.toStr(JSONPath.eval(response, "$.status"));
            if (CharSequenceUtil.isNotBlank(response) && CharSequenceUtil.equals(status, MetaDataConstants.API_SUCCESS_RESPONSE)) {
                log.info("{}集群训练推理任务所占节点及所用卡数返回值：{}", resourceUrl, response);
                Object data = JSONPath.eval(response, "$.data");
                List<JSONObject> jsonObjectList = Convert.toList(JSONObject.class, data);
                for (int i = 0; i < jsonObjectList.size(); i++) {
                    JSONObject jsonObj = jsonObjectList.get(i);
                    if (!jsonObj.isEmpty()) {
                        ClusterNodeCard node = new ClusterNodeCard();
                        node.setNodeName(jsonObj.getString("node_name"));
                        node.setGpuIds(jsonObj.getJSONArray("gpu_ids").toJavaList(String.class));
                        clusterNodeList.add(node);
                    }
                }
            } else {
                log.error("{}集群训练推理任务所占节点及所用卡数异常：{}", resourceUrl, response);
            }
        }
        return clusterNodeList;
    }

    /**
     * 默认日志对象封装
     *
     * @param interfaceName 接口名称
     * @param param         请求参数
     * @param url           请求url
     * @param userId        用户ID
     * @return MmLog
     */
/*    private MmLog defaultLog(String interfaceName, String param, String url, Long userId) {
        MmLog mmLog = logServiceClientFeign.dataAssembly(Convert.toStr(userId, null), Convert.toStr(userId, null), interfaceName);
        if (mmLog == null) {
            mmLog = new MmLog();
            mmLog.setId(new BigDecimal(IdUtil.getSnowflakeNextId()));
        }
        mmLog.setRequestParams(JSON.toJSONString(param));
        mmLog.setInterfaceUrl(url);
        return mmLog;
    }*/
}