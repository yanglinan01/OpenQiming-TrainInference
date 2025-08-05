package com.ctdi.cnos.llm.metadata.extra.conversation.processor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.fastjson.JSONObject;
import com.ctdi.cnos.llm.base.constant.MetaDataConstants;
import com.ctdi.cnos.llm.base.object.PageParam;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.base.object.SortingField;
import com.ctdi.cnos.llm.beans.log.chat.ModelChatLogDTO;
import com.ctdi.cnos.llm.beans.meta.prompt.PromptSequentialDetail;
import com.ctdi.cnos.llm.beans.meta.dialog.ConversationBodyDTO;
import com.ctdi.cnos.llm.beans.meta.model.ModelVO;
import com.ctdi.cnos.llm.beans.meta.dataSet.DataSetAndPrInfoVO;
import com.ctdi.cnos.llm.metadata.extra.conversation.BaseConversationProcessor;
import com.ctdi.cnos.llm.metadata.service.DataSetService;
import com.ctdi.cnos.llm.metadata.service.ModelService;
import com.ctdi.cnos.llm.metadata.service.PromptSequentialDetailService;
import com.dtflys.forest.Forest;
import com.dtflys.forest.http.ForestRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * 时序大模型预测(内部使用)。
 *
 * @see <a href="">时序大模型预测接口流量.md</a>
 *
 * @author laiqi
 * @since 2024/9/14
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class TimeLlmModelConversationProcessor extends BaseConversationProcessor<ModelVO> {

    /**
     * 必填参数
     * dataSetId：数据集ID
     * duration：预测时长
     */
    private static final Set<String> REQUIRED_PARAMS = CollUtil.newHashSet("dataSetId", "duration");

    private final ModelService modelService;

    private final DataSetService dataSetService;

    private final PromptSequentialDetailService promptSequentialDetailService;

    /**
     * 线程本地变量，用于存储当前对话的数据。
     */
    public static final ThreadLocal<List<PromptSequentialDetail>> PROMPT_SEQUENTIAL_DETAIL_THREAD_LOCAL = ThreadUtil.createThreadLocal(false);

    @Override
    public boolean match(ConversationBodyDTO conversationBody) {
        return false;
    }

    @Override
    protected boolean validate(ConversationBodyDTO conversationBody) {
        JSONObject params = conversationBody.getParams();
        Assert.notNull(params, "参数对象不能为空！");
        Assert.isTrue(CollUtil.containsAll(params.keySet(), REQUIRED_PARAMS), "参数对象缺少必要的参数！" + REQUIRED_PARAMS);
        ModelVO model = getCacheModel(conversationBody);
        Assert.notNull(model, "查询模型不存在！");
        Assert.notBlank(model.getEndpoint(), "该模型接口地址有误！");
        return true;
    }

    @Override
    protected ModelVO buildModel(ConversationBodyDTO conversationBody) {
        String modelId = conversationBody.getModel();
        return modelService.queryById(Long.valueOf(modelId));
    }

    @Override
    protected ForestRequest<?> convertForestRequest(ConversationBodyDTO conversationBody, ModelVO model) {
        JSONObject body = new JSONObject();
        // 1、查询数据集对应的dataSetFileId
        JSONObject params = conversationBody.getParams();
        BigDecimal dataSetId = params.getBigDecimal("dataSetId");
        DataSetAndPrInfoVO dataSet = dataSetService.queryTestById(dataSetId);
        Assert.notNull(dataSet, "该数据集不存在！");

        BigDecimal dataSetFileId = dataSet.getDataSetFileId();
        Assert.notNull(dataSetFileId, "该数据集对应的文件对象不存在！");
        // 2、查询数据集文件对应的电路数据(需要限制查询数量)
        QueryParam queryParam = new QueryParam();
        queryParam.setPageParam(PageParam.of(1440));
        queryParam.setFilterMap(MapUtil.of("dataSetFileId", dataSetFileId));
        queryParam.setSortingFields(CollUtil.newArrayList(SortingField.asc("circuitId"), SortingField.desc("tCtime")));
        PageResult<PromptSequentialDetail> pageResult = promptSequentialDetailService.queryPage(queryParam);
        // 特别注意：查询最近1440点的数据之后，为了拼装，需要再倒序一下。
        List<PromptSequentialDetail> promptSequentialDetails = CollUtil.reverse(pageResult.getRows());
        PROMPT_SEQUENTIAL_DETAIL_THREAD_LOCAL.set(promptSequentialDetails);
        // 3、组装请求体
        JSONObject experienceBody = extractLlmTimeModelPredictionBody(promptSequentialDetails);
        Assert.notNull(experienceBody, "该数据集模型对应的电路数据有误！");
        return Forest.post(model.getEndpoint()).contentTypeJson().addBody(body);
    }

    @Override
    protected String extractedContent(String original) {
        // 不做处理
        return null;
    }

    @Override
    protected void saveChatLog(ModelChatLogDTO chatLog) {
        // 不做入库处理
    }

    /**
     * 提取时序大模型预测接口流量请求body。
     *
     * @param circuits 单个电路的信息列表。
     * @return 流量请求body。
     */
    private static JSONObject extractLlmTimeModelPredictionBody(List<PromptSequentialDetail> circuits) {
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
     * 移除线程变量。
     * 需要调用方在调用完process()方法后调用。
     */
    public static void removeThreadLocal(){
        PROMPT_SEQUENTIAL_DETAIL_THREAD_LOCAL.remove();
    }

}