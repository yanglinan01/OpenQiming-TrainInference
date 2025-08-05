package com.ctdi.cnos.llm.train.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.beans.train.IntentRecognition.IntentRecognizeReq;
import com.ctdi.cnos.llm.beans.train.IntentRecognition.IntentRecognizeResp;
import com.ctdi.cnos.llm.beans.train.IntentRecognition.IntentRecognizeVO;
import com.ctdi.cnos.llm.beans.train.deployTask.*;
import com.ctdi.cnos.llm.beans.train.trainTask.TrainTaskVO;

import java.util.List;

/**
 * 部署任务(DeployTask)表服务接口
 *
 * @author wangyb
 * @since 2024-07-01 14:16:01
 */
public interface DeployTaskService {

    /**
     * 查询部署任务列表
     *
     * @param vo vo
     * @return 部署任务列表
     */
    List<DeployTaskVO> queryList(DeployTaskVO vo);

    /**
     * 分页查询部署任务列表
     *
     * @param page page
     * @param vo   vo
     * @return 部署任务列表
     */
    Page<DeployTaskVO> queryList(Page<DeployTaskVO> page, DeployTaskVO vo);


    /**
     * 根据nacos部署url查询部署任务列表
     *
     * @param deployUrl 部署url
     * @return 部署任务列表
     */
    List<DeployTask> queryListByDeployUrlFromNacos(String deployUrl);

    /**
     * 根据id查询部署任务详情
     *
     * @param deployTaskId deployTaskId
     * @return 部署任务详情
     */
    DeployTaskVO queryById(Long deployTaskId);

    /**
     * 新增部署任务
     *
     * @param deployTask 部署任务信息
     * @return 新增结果
     */
    Long save(DeployTaskVO deployTask);

    /**
     * 组装提交部署任务参数
     *
     * @param deployTaskId 部署任务ID
     * @param trainTaskVO  训练任务对象
     * @return DeployTaskSubmitParam
     */
    DeployTaskSubmitParam buildDeployParam(Long deployTaskId, TrainTaskVO trainTaskVO);

    /**
     * 供远程更新部署任务状态
     *
     * @param deployTask 部署任务状态
     * @return boolean
     */
    boolean updateById(DeployTaskVO deployTask);

    /**
     * 新部署任务
     *
     * @param deployTask 部署任务
     * @return boolean
     */
    boolean update(Wrapper<DeployTask> deployTask);

    /**
     * 删除远程部署任务
     *
     * @param id 部署任务ID
     * @return boolean
     */
    boolean deleteById(Long id);

    /**
     * 删除过期的模型
     *
     * @param offsetDay 过期时间
     * @return 删除的modelId
     */
    List<Long> deleteExpiredDeployTask(Integer offsetDay);


    /**
     * 定时回调部署状态
     */
    void callBackDeployStatus();

    /**
     * 定时回调部署发送
     */
    void callBackDeploySend();

    DeployTask querySimpleByRegisterId(Long registerId);


    /**
     * @param deployTaskId 部署任务id
     * @return 注册ID
     */
    String addModelRegisterApi(Long deployTaskId);

    /**
     * @param deployTaskId 模型部署ID
     * @return 删除结果
     */
    String deleteModelRegisterApi(Long deployTaskId);


    /**
     * 查询当前用户所有模型注册API
     *
     * @param pageIndex 当前页
     * @param pageSize  每页条数
     * @return 当前用户所有模型注册API列表
     */
    String queryModelRegisterApi(int pageIndex, int pageSize);

    /**
     * 查询所有部署成功的模型
     *
     * @return 部署成功的模型列表
     */
    List<DeployTaskVO> queryAllDeployedModel(DeployTaskReq deployTaskReq);

    /**
     * 查询部署成功的测试模型
     *
     * @return DeployTask
     */
    DeployTask queryCompletedTestDeployTask();

    /**
     *
     * @param ids ids
     * @param before nacos修改前url
     * @param after nacos修改后url
     */
    void updateBatchDeployTask(List<Long> ids, String before, String after);

    /**
     * 测试部署地址
     * @param task
     * @return
     */
    String handlePostDeployment(DeployTask task);

    int callUpdateDeployTask(DeployTaskVO deployTaskVO);

    /**
     * 意图识别查询部署
     * @param vo
     * @return
     */
    List<DeployTaskVO> queryListByCategory(DeployTaskVO vo);

    /**
     * 意图识别推理
     * @param req
     * @return
     */
    IntentRecognizeResp intentRecognize(IntentRecognizeVO req);

    /**
     * 版本启用
     * @param taskVO
     * @return
     */
    String versionEnable(TrainTaskVO taskVO);
}
