package com.ctdi.cnos.llm.metadata.service;

import com.ctdi.cnos.llm.base.object.ExcelDataModel;
import com.ctdi.cnos.llm.base.service.IBaseService;
import com.ctdi.cnos.llm.beans.meta.evaluation.PrTestSetEvaluation;
import com.ctdi.cnos.llm.beans.meta.evaluation.PrTestSetEvaluationVO;
import com.ctdi.cnos.llm.response.OperateResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * 问答对测试数据集评估 数据操作服务接口。
 *
 * @author laiqi
 * @since 2024/09/03
 */
public interface PrTestSetEvaluationService extends IBaseService<PrTestSetEvaluation, PrTestSetEvaluationVO> {

    /**
     * 导出指定问答对测试数据集评估详情。
     *
     * @param id 指定问答对测试数据集评估主键Id。
     */
    ExcelDataModel export(Long id);

    /**
     * 新增测试数据集评估
     *
     * @param prTestSetEvaluation prTestSetEvaluation
     * @return 新增结果
     */
    boolean insert(PrTestSetEvaluation prTestSetEvaluation);

    /**
     * 根据测试集批量进行问答对话
     */
    void batchChatByTestDataSet();

    /**
     * 回调获取评估部署任务状态
     *
     * @return
     */
    int callbackDeployEvalStatus();

    /**
     * 上传文件
     *
     * @param id
     * @param uploadFile
     * @return
     */
    OperateResult<String> upload(Long id, MultipartFile uploadFile);

    /**
     * 更新testSetEvaluation
     *
     * @param testSetEvaluation testSetEvaluation
     */
    void update(PrTestSetEvaluation testSetEvaluation);

    int submitEvaluationDeploymentTask();
}