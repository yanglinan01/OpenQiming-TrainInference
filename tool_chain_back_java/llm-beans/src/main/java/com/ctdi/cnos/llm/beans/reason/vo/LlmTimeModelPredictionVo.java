package com.ctdi.cnos.llm.beans.reason.vo;

import com.alibaba.fastjson.JSONObject;
import com.ctdi.cnos.llm.beans.meta.prompt.PromptSequentialDetail;
import com.ctdi.cnos.llm.beans.reason.req.LlmTimeModelPredictionDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * 时序大模型体验预测响应。
 *
 * @author laiqi
 * @since 2024/8/15
 */
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Setter
@Getter
@ApiModel("时序大模型体验响应")
public class LlmTimeModelPredictionVo {

    /**
     * 原请求头对象。
     */
    @ApiModelProperty(value = "请求体")
    private final LlmTimeModelPredictionDTO request;

    /**
     * {
     *   xAxis: {
     *     data: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10',
     *       '11', '12', '13', '14', '15', '16', '17', '18', '19', '20'],
     *     startValue: '1',
     *   },
     *   series: [
     *     {
     *       name: '原始数据',
     *       data: ['120', '200', '150', '80', '', '', '', '', '', '',],
     *       startXAxis: '0'
     *     },
     *     {
     *       name: '预测数据',
     *       data: ['', '', '', '', '', '', '80', '70', '110', '130'],
     *       startXAxis: '200'
     *     }
     *   ]
     * }
     */
    @ApiModelProperty(value = "流入echarts数据")
    private JSONObject inEcharts;

    /**
     * 格式如inEcharts
     */
    @ApiModelProperty(value = "流出echarts数据")
    private JSONObject outEcharts;

    /**
     * 电路预测数据列表
     */
    @ApiModelProperty(value = "电路预测数据列表")
    private List<PromptSequentialDetail> predictionData;
}