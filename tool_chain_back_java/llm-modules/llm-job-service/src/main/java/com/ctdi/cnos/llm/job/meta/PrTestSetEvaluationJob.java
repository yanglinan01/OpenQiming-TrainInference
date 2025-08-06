package com.ctdi.cnos.llm.job.meta;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import com.ctdi.cnos.llm.base.object.PageParam;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.base.object.SortingField;
import com.ctdi.cnos.llm.beans.meta.evaluation.PrTestSetEvaluationVO;
import com.ctdi.cnos.llm.feign.metadata.PrTestSetEvaluationServiceClientFeign;
import com.ctdi.cnos.llm.feign.train.DeployTaskServiceClientFeign;
import com.ctdi.cnos.llm.feign.train.TrainTaskServiceClientFeign;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 问答对测试数据集评估
 *
 * @author wangyb
 * @since 2024/7/9
 */
@Component("prTestSetEvaluationJob")
@RequiredArgsConstructor
@Slf4j
public class PrTestSetEvaluationJob {


    private final PrTestSetEvaluationServiceClientFeign evaluationServiceClientFeign;

    private final DeployTaskServiceClientFeign deployTaskServiceClientFeign;

    private final TrainTaskServiceClientFeign trainTaskServiceClientFeign;

    /**
     * 批量进行问答对测试数据集对话
     */
    public void batchChatByTestDataSet() {
        evaluationServiceClientFeign.batchChatByTestDataSet();
    }


    /**
     * 回调获取评估部署任务状态
     */
    public void callbackDeployEvalStatus() {
        String evalStatus = evaluationServiceClientFeign.callbackDeployEvalStatus();
        log.info("【回调获取评估部署任务状态】定时任务执行完毕！信息为：" + evalStatus);
    }

    /**
     * 提交评估部署任务
     */
    public void submitEvaluationDeploymentTask() {
        log.info("提交评估部署任务定时任务开始");
        String submit = evaluationServiceClientFeign.submitEvaluationDeploymentTask();
        log.info("提交评估部署任务定时任务执行完毕！信息为：" + submit);
    }

}
