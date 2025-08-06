package com.ctdi.cnos.llm.metadata.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ctdi.cnos.llm.base.constant.MetaDataConstants;
import com.ctdi.cnos.llm.base.object.PageParam;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.base.object.SortingField;
import com.ctdi.cnos.llm.beans.meta.dataSet.DataSetAndPrInfoVO;
import com.ctdi.cnos.llm.beans.meta.model.ModelVO;
import com.ctdi.cnos.llm.beans.meta.prompt.PromptSequentialDetail;
import com.ctdi.cnos.llm.beans.reason.req.LlmTimeModelPredictionDTO;
import com.ctdi.cnos.llm.beans.reason.vo.LlmTimeModelPredictionVo;
import com.ctdi.cnos.llm.metadata.service.DataSetService;
import com.ctdi.cnos.llm.metadata.service.ModelChatService;
import com.ctdi.cnos.llm.metadata.service.ModelService;
import com.ctdi.cnos.llm.metadata.service.PromptSequentialDetailService;
import com.dtflys.forest.Forest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 。
 *
 * @author laiqi
 * @since 2024/9/4
 */
@RequiredArgsConstructor
@Slf4j
@Service("modelChatService")
public class ModelChatServiceImpl implements ModelChatService {

    private final ModelService modelService;

    private final PromptSequentialDetailService promptSequentialDetailService;

    private final DataSetService dataSetService;

    @Override
    public LlmTimeModelPredictionVo llmTimeModelPrediction(LlmTimeModelPredictionDTO request) {
        // 1、查询数据集对应的电路数量
        // Long circuitCount = promptSequentialDetailService.countCircuitByDataSetId(request.getDataSetId());
        // Assert.isFalse(circuitCount != 1, "该数据集模型对应的电路数据存在多条或者不存在！");

        // DataSetAndPrInfoVO dataSet = dataSetServiceClientFeign.queryById(String.valueOf(request.getDataSetId()));
        // BigDecimal dataSetFileId = dataSet.getDataSetFileId();


        DataSetAndPrInfoVO dataSet = dataSetService.queryTestById(BigDecimal.valueOf(request.getDataSetId()));
        Assert.notNull(dataSet, "该数据集不存在！");

        BigDecimal dataSetFileId = dataSet.getDataSetFileId();
        Assert.notNull(dataSetFileId, "该数据集对应的文件对象不存在！");

        // 2、查询模型
        Long modelId = request.getModelId();
        ModelVO model = modelService.queryById(modelId);
        Assert.notNull(model, "时序大模型不存在！");

        // 3、查询数据集文件对应的电路数据(需要限制查询数量)
        QueryParam queryParam = new QueryParam();
        queryParam.setPageParam(PageParam.of(1440));
        queryParam.setFilterMap(MapUtil.of("dataSetFileId", dataSetFileId));
        queryParam.setSortingFields(CollUtil.newArrayList(SortingField.asc("circuitId"), SortingField.desc("tCtime")));
        PageResult<PromptSequentialDetail> pageResult = promptSequentialDetailService.queryPage(queryParam);
        // 特别注意：查询最近1440点的数据之后，为了拼装，需要再倒序一下。
        List<PromptSequentialDetail> promptSequentialDetails = CollUtil.reverse(pageResult.getRows());
        // 4、组装请求体
        JSONObject experienceBody = extractLlmTimeModelPredictionBody(promptSequentialDetails);
        Assert.notNull(experienceBody, "该数据集模型对应的电路数据有误！");
        log.info("组装时序预测接口请求：{}", experienceBody);
        // 5、发送请求
        String experienceData = null;
        List<PromptSequentialDetail> predictionData = CollUtil.newLinkedList();
        try {
            experienceData = request.isTest() ? ResourceUtil.readUtf8Str("llmTimeModelPrediction.json") : Forest.post(model.getEndpoint()).contentTypeJson().addBody(experienceBody).executeAsString();
            log.info("时序预测接口返回：{}", experienceData);
            // 6、解析响应预测数据(前端需要隐藏电路ID)
            predictionData = extractLlmTimeModelPredictionResponse(experienceData);
        } catch (Exception e) {
            log.error("时序预测接口异常：{}", e.getMessage(), e);
        }
        // 7、组装echarts数据
        JSONObject inEcharts = buildLlmTimeModelEcharts(promptSequentialDetails, predictionData, request.getDuration(), PromptSequentialDetail::getDInFlux);
        JSONObject outEcharts = buildLlmTimeModelEcharts(promptSequentialDetails, predictionData, request.getDuration(), PromptSequentialDetail::getDOutFlux);
        LlmTimeModelPredictionVo llmTimeModelPrediction = new LlmTimeModelPredictionVo(request);
        llmTimeModelPrediction.setPredictionData(predictionData);
        llmTimeModelPrediction.setInEcharts(inEcharts);
        llmTimeModelPrediction.setOutEcharts(outEcharts);
        return llmTimeModelPrediction;
    }


    /**
     * 提取自建模型体验接口答案。
     *
     * @param text 文本
     * @return 答案。
     */
    public static String extractAnswerByCustomerModel(String text) {
        StringBuilder responseMessageBuffer = new StringBuilder();
        JSONObject jsonObject = JSON.parseObject(text);
        JSONArray choices = jsonObject.getJSONArray("choices");
        for (int i = 0; i < choices.size(); i++) {
            JSONObject item = choices.getJSONObject(i);
            String content = item.getJSONObject("message").getString("content");
            responseMessageBuffer.append(StrUtil.removeAllLineBreaks(content));
        }
        return responseMessageBuffer.toString();
    }

    /**
     * 构建时序大模型体验的echarts。
     *
     * @param rawData        原始数据
     * @param predictionData 预测数据
     * @param duration       预测时长
     * @return echarts
     */
    private JSONObject buildLlmTimeModelEcharts(List<PromptSequentialDetail> rawData, List<PromptSequentialDetail> predictionData, Integer duration, Function<PromptSequentialDetail, String> mapper) {
        int subCount = duration * 12;
        JSONObject echarts = new JSONObject();
        JSONObject xAxis = new JSONObject();
        JSONArray series = new JSONArray();
        List<String> rowDataCollectTimes = extractFlowData(rawData, item -> DateUtil.format(item.getTCtime(), MetaDataConstants.MY_NORM_DATETIME_MINUTE_PATTERN), subCount);
        List<String> predictionDataCollectTimes = extractFlowData(predictionData, item -> DateUtil.format(item.getTCtime(), MetaDataConstants.MY_NORM_DATETIME_MINUTE_PATTERN), subCount);
        xAxis.put("data", CollUtil.unionAll(rowDataCollectTimes, predictionDataCollectTimes));
        String startValue = CollUtil.getFirst(rowDataCollectTimes);
        xAxis.put("startValue", startValue);
        echarts.put("xAxis", xAxis);

        JSONObject rawDataJson = new JSONObject();
        JSONObject predictionDataJson = new JSONObject();

        rawDataJson.put("name", "原始数据");
        predictionDataJson.put("name", "预测数据");

        rawDataJson.put("startXAxis", startValue);
        // 为了重叠，将预测数据起始时间设置为原始数据最后一条数据的时间
        predictionDataJson.put("startXAxis", CollUtil.getLast(rowDataCollectTimes));

        List<String> inRawData = extractFlowData(rawData, mapper, subCount);
        rawDataJson.put("data", inRawData);
        List<String> outPredictionData = extractFlowData(predictionData, mapper, subCount);
        List<String> nullRawData = IntStream.range(0, inRawData.size() - 1)
                .mapToObj(i -> "")
                .collect(Collectors.toList());
        // 为了重叠，将预测数据延申一条出来为原始数据最后一条数据
        nullRawData.add(CollUtil.getLast(inRawData));
        predictionDataJson.put("data", CollUtil.unionAll(nullRawData, outPredictionData));

        series.add(rawDataJson);
        series.add(predictionDataJson);
        echarts.put("series", series);
        return echarts;
    }

    /**
     * 提取时序大模型预测接口流量数据。
     *
     * @param data     数据
     * @param subCount 提取的数量
     * @return 提取的数据
     */
    private static List<String> extractFlowData(List<PromptSequentialDetail> data, Function<PromptSequentialDetail, String> mapper, int subCount) {
        int size = data.size();
        if (size <= subCount) {
            return data.stream().map(mapper).collect(Collectors.toList());
        }
        return data.subList(size - subCount, size)
                .stream().map(mapper).collect(Collectors.toList());
    }

    /**
     * 提取时序大模型预测接口流量请求body。
     *
     * @param circuits 单个电路的信息列表。
     * @return 流量请求body。
     */
    public static JSONObject extractLlmTimeModelPredictionBody(List<PromptSequentialDetail> circuits) {
        if (CollUtil.isEmpty(circuits)) {
            return null;
        }
        PromptSequentialDetail first = CollUtil.getFirst(circuits);
        String circuitid = first.getCircuitId();
        String cirname = first.getCirName();
        String k_devid = first.getKDevId();
        String k_intfid = first.getKIntfId();
        String devidIp = first.getDevidIp();
        String bdevid = first.getBDevId();
        String bintfdescr = first.getBIntfDescr();
        String bdevidIp = first.getBDevidIp();
        String d_cirbw = first.getDCirbw();

        List<String> d_influxs = CollUtil.newArrayList();
        List<String> d_outfluxs = CollUtil.newArrayList();
        List<String> d_influxratios = CollUtil.newArrayList();
        List<String> d_outfluxratios = CollUtil.newArrayList();
        List<String> t_ctimes = CollUtil.newArrayList();
        for (PromptSequentialDetail circuit : circuits) {
            d_influxs.add(circuit.getDInFlux());
            d_outfluxs.add(circuit.getDOutFlux());
            d_influxratios.add(circuit.getDInFluxRatio());
            d_outfluxratios.add(circuit.getDOutFluxRatio());
            t_ctimes.add(DateUtil.format(circuit.getTCtime(), MetaDataConstants.MY_NORM_DATETIME_MINUTE_PATTERN));
        }
        JSONObject body = new JSONObject();
        body.put("circuitid", CollUtil.newLinkedList(circuitid));
        body.put("cirname", CollUtil.newLinkedList(cirname));
        body.put("k_devid", CollUtil.newLinkedList(k_devid));
        body.put("k_intfid", CollUtil.newLinkedList(k_intfid));
        body.put("devidIp", CollUtil.newLinkedList(devidIp));
        body.put("bdevid", CollUtil.newLinkedList(bdevid));
        body.put("bintfdescr", CollUtil.newLinkedList(bintfdescr));
        body.put("bdevidIp", CollUtil.newLinkedList(bdevidIp));
        body.put("d_cirbw", CollUtil.newLinkedList(d_cirbw));

        body.put("d_influx", CollUtil.newLinkedList(d_influxs));
        body.put("d_outflux", CollUtil.newLinkedList(d_outfluxs));
        body.put("d_influxratio", CollUtil.newLinkedList(d_influxratios));
        body.put("d_outfluxratio", CollUtil.newLinkedList(d_outfluxratios));
        body.put("t_ctime", t_ctimes);
        return body;
    }

    /**
     * 提取时序大模型预测接口流量响应。
     *
     * @param response 接口流量响应体。
     * @return 转换为对应的数据列表。
     */
    public static List<PromptSequentialDetail> extractLlmTimeModelPredictionResponse(String response) {
        List<PromptSequentialDetail> predictionData = CollUtil.newLinkedList();
        JSONObject jsonObject = JSON.parseObject(response);
        // 链路编号
        String circuitid = jsonObject.getJSONArray("circuitid").getString(0);
        // 电路名称
        String cirname = jsonObject.getJSONArray("cirname").getString(0);
        // A端设备编码
        String k_devid = jsonObject.getJSONArray("k_devid").getString(0);
        // A端端口
        String k_intfid = jsonObject.getJSONArray("k_intfid").getString(0);
        // A端设备IP
        String devidIp = jsonObject.getJSONArray("devidIp").getString(0);
        // B端设备编码
        String bdevid = jsonObject.getJSONArray("bdevid").getString(0);
        // B端端口
        String bintfdescr = jsonObject.getJSONArray("bintfdescr").getString(0);
        // B端设备IP
        String bdevidIp = jsonObject.getJSONArray("bdevidIp").getString(0);
        // 带宽
        String d_cirbw = jsonObject.getJSONArray("d_cirbw").getString(0);


        // 流入流速
        JSONArray d_influx = jsonObject.getJSONArray("d_influx").getJSONArray(0);
        // 流出流速
        JSONArray d_outflux = jsonObject.getJSONArray("d_outflux").getJSONArray(0);
        // 流入带宽利用率
        JSONArray d_influxratio = jsonObject.getJSONArray("d_influxratio").getJSONArray(0);
        // 流入带宽利用率
        JSONArray d_outfluxratio = jsonObject.getJSONArray("d_outfluxratio").getJSONArray(0);
        // 采集时间
        JSONArray t_ctime = jsonObject.getJSONArray("t_ctime");
        int size = d_influx.size();
        // 截取最后256个数据
        int startIndex = size > 256 ? size - 256 : 0;
        Integer serialNumber = 0;
        for (int i = startIndex; i < size; i++) {
            PromptSequentialDetail circuit = new PromptSequentialDetail();
            circuit.setSerialNumber(++serialNumber);
            // 前端需要隐藏
            circuit.setCircuitId("******");
            circuit.setCirName(cirname);
            circuit.setKDevId(k_devid);
            circuit.setKIntfId(k_intfid);
            circuit.setDevidIp(devidIp);
            circuit.setBDevId(bdevid);
            circuit.setBIntfDescr(bintfdescr);
            circuit.setBDevidIp(bdevidIp);
            circuit.setDCirbw(d_cirbw);

            circuit.setDInFlux(d_influx.getString(i));
            circuit.setDOutFlux(d_outflux.getString(i));
            circuit.setDInFluxRatio(d_influxratio.getString(i));
            circuit.setDOutFluxRatio(d_outfluxratio.getString(i));
            circuit.setTCtime(DateUtil.parse(t_ctime.getString(i), MetaDataConstants.MY_NORM_DATETIME_MINUTE_PATTERN));
            predictionData.add(circuit);
        }
        return predictionData;
    }
}