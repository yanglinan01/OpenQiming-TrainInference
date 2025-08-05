package com.ctdi.cnos.llm.log.service.impl;

import com.ctdi.cnos.llm.log.constant.MessageContentConstants;
import com.ctdi.cnos.llm.log.service.MmTrainLogService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yuyong
 * @date 2024/11/18 16:20
 */
@Slf4j
@Service
public class MmTrainLogServiceImpl implements MmTrainLogService {

    @Autowired
    RestHighLevelClient client;

    @Value("${es.indexName:}")
    private String indexName;

    @Value("${es.namespace:}")
    private String namespace;

    @Override
    public Map<String, Object> queryTrainLog(int pageNum, int pageSize, Long taskId) {
        log.info("join in queryTrainLog............................:" + indexName);

        // 返回结果
//        List<JSONObject> resultList = new ArrayList<>();
        Map<String, Object> resultMap = new HashMap<>();
        List<String> resultList = new ArrayList<>();
        // 构建查询条件
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("kubernetes.namespace", namespace))
                .must(QueryBuilders.wildcardQuery("kubernetes.pod.name", "*" + taskId + "*"));

        // 构建对message字段的查询条件，要求完整包含loss或者eval_loss
        BoolQueryBuilder messageQuery = QueryBuilders.boolQuery()
                .should(QueryBuilders.matchPhraseQuery("message", MessageContentConstants.LOSS))
                .should(QueryBuilders.matchPhraseQuery("message", MessageContentConstants.EVAL_LOSS));

        // 将message字段的查询条件添加到主查询条件中
        boolQuery.must(messageQuery);

        // 创建 Scroll 请求，并设置分页参数
        SearchRequest scrollRequest = new SearchRequest(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQuery)
                // 设置起始位置，根据页码和每页大小计算
                .from((pageNum - 1) * pageSize)
                // 设置每页返回的文档数量
                .size(pageSize);
        scrollRequest.source(searchSourceBuilder);
        scrollRequest.scroll(TimeValue.timeValueMinutes(1)); // 设置 scroll 的有效时间

        // 执行 Scroll 请求
        SearchResponse response = null;
        try {
            response = client.search(scrollRequest, RequestOptions.DEFAULT);
            if (response!= null) {
                log.info("根据训练任务id为: " + taskId + "从 es 查询数据结果为:" + (response.getHits().getHits().length > 0? response.getHits().getHits() : "无数据"));
                for (SearchHit hit : response.getHits()) {
                    Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                    // 只提取message字段的值
                    if (sourceAsMap.containsKey("message")) {
                        resultList.add((String) sourceAsMap.get("message"));
//                        String message = (String) sourceAsMap.get("message");
//                        if ((message.contains(MessageContentConstants.LOSS) && message.indexOf(MessageContentConstants.LOSS) == message.lastIndexOf(MessageContentConstants.LOSS)) ||
//                                (message.contains(MessageContentConstants.EVAL_LOSS) && message.indexOf(MessageContentConstants.EVAL_LOSS) == message.lastIndexOf(MessageContentConstants.EVAL_LOSS))) {
//                            resultList.add(message);
//                        }
//                        if (message.contains(MessageContentConstants.LOSS) || message.contains(MessageContentConstants.EVAL_LOSS)) {
//                            resultList.add(message);
//                        }
                    }
//                    JSONObject jsonObject = new JSONObject(sourceAsMap);
//                    resultList.add(jsonObject);
                }
            } else {
                log.info("根据训练任务id为: " + taskId + "从 es 查询数据未成功，返回空结果");
            }
        } catch (IOException e) {
            // 抛出自定义业务异常
            throw new EsQueryException("从es查询出错,错误信息为: " + e.getMessage());
        } finally {
            // 清理Scroll上下文，释放资源
            try {
                clearScrollContext(response);
            } catch (Exception e) {
                // 再次记录清理过程中可能出现的错误
                log.error("最终清理Scroll上下文出错,错误信息为: " + e.getMessage());
            }
        }
        resultMap.put("record", resultList);
        // 获取并设置准确的总数量
        if (response!= null) {
            long totalHits = response.getHits().getTotalHits().value;
            resultMap.put("total", totalHits);
        } else {
            resultMap.put("total", 0);
        }
        return resultMap;
    }

    private void clearScrollContext(SearchResponse response) {
        if (response!= null && response.getScrollId()!= null) {
            ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
            clearScrollRequest.addScrollId(response.getScrollId());
            try {
                client.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                log.error("清理Scroll上下文失败: " + e.getMessage());
            }
        }
    }

    // 自定义业务异常类
    public static class EsQueryException extends RuntimeException {
        public EsQueryException(String message) {
            super(message);
        }
    }
}
