/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.feign.train;

import com.ctdi.cnos.llm.RemoteConstont;
import com.ctdi.cnos.llm.beans.train.IntentRecognition.IntentRecognizeReq;
import com.ctdi.cnos.llm.beans.train.IntentRecognition.IntentRecognizeResp;
import com.ctdi.cnos.llm.beans.train.IntentRecognition.IntentRecognizeVO;
import com.ctdi.cnos.llm.beans.train.deployTask.*;
import com.ctdi.cnos.llm.beans.train.trainTask.TrainTaskVO;
import com.ctdi.cnos.llm.response.OperateResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 模型部署任务(DeployTask) OpenFeign
 *
 * @author wangyb
 * @since 2024-07-03 15:09:34
 */
@Component
@FeignClient(value = RemoteConstont.TRAIN_SERVICE_NAME, path = "${cnos.server.llm-train-service.contextPath}")
public interface DeployTaskServiceClientFeign {
    /**
     * 任务列表
     *
     * @param vo 任务信息
     * @return List<DeployTaskVO>
     */
    @PostMapping("/deployTask/queryList")
    List<DeployTaskVO> queryList(@RequestBody DeployTaskVO vo);

    /**
     * 意图识别部署任务列表
     *
     * @param vo 任务信息
     * @return List<DeployTaskVO>
     */
    @PostMapping("/deployTask/queryListByCategory")
    List<DeployTaskVO> queryListByCategory(@RequestBody DeployTaskVO vo);

    /**
     * 查询详情
     *
     * @param deployTaskId 任务ID
     * @return DeployTaskVO
     */
    @GetMapping("/deployTask/detail")
    DeployTaskVO detail(@RequestParam("deployTaskId") String deployTaskId);

    /**
     * 根据ID获取部署任务信息
     *
     * @param deployTaskId 部署任务ID
     * @return DeployTask
     */
    @GetMapping("/deployTask/queryById")
    DeployTaskVO querySimpleById(@RequestParam("deployTaskId") String deployTaskId);

    @GetMapping("/deployTask/queryByRegisterId")
    DeployTask querySimpleByRegisterId(@RequestParam("registerId") Long registerId);

    /**
     * 新增任务
     *
     * @param deployTask 任务信息
     * @return OperateResult<String>
     */
    @PostMapping("/deployTask/add")
    Long add(@RequestBody DeployTaskVO deployTask);

    /**
     * 根据ID更新任务
     *
     * @param deployTask 部署任务信息
     * @return 响应
     */
    @PostMapping("/deployTask/updateById")
    OperateResult<String> updateById(@RequestBody DeployTaskVO deployTask);


    /**
     * 回调接口获取部署任务状态
     */
    @GetMapping("/deployTask/callBackDeployStatus")
    void callBackDeployStatus();


    /**
     * 测试部署地址
     * @param task
     * @return
     */
    @GetMapping("/deployTask/handlePostDeployment")
    public String handlePostDeployment(DeployTask task);

    /**
     * 回调接口获取部署任务状态
     */
    @GetMapping("/deployTask/callBackDeploySend")
    void callBackDeploySend();

    /**
     * 删除模型
     *
     * @param deployTaskId 任务ID
     * @return String
     */
    @GetMapping("/deployTask/deleteById")
    OperateResult<String> deleteById(@RequestParam("deployTaskId") String deployTaskId);

    /**
     * 超时部署移除
     *
     * @param offsetDay 超时天数
     * @return string
     */
    @GetMapping("/deployTask/deleteExpiredDeployTask")
    String deleteExpiredDeployTask(@RequestParam(value = "offsetDay", defaultValue = "1") Integer offsetDay);


    /**
     * 更新部署任务的智能体引用状态
     *
     * @param deployTaskAgentStatus 部署任务引用状态对象
     * @return 响应体
     */
    @PostMapping("/deployTask/updateAgentStatus")
    OperateResult<String> updateAgentStatus(@RequestBody DeployTaskAgentStatusCallback deployTaskAgentStatus);

    /**
     * 回调更新部署任务
     * @param deployTaskVO
     * @return
     */
    @PostMapping("/deployTask/callUpdateDeployTask")
    public Map<String, Object> callUpdateDeployTask(@RequestBody DeployTaskVO deployTaskVO);

    /**
     * 远程创建训练模型部署
     *
     * @param deployTaskVO 远程创建训练模型部署
     * @return 响应体
     */
    @PostMapping("/deployTask/add")
    String remoteDeploy(@RequestBody DeployTaskVO deployTaskVO);

    /**
     * 注册模型api
     *
     * @param deployTaskId 部署id
     * @return 注册id
     */
    @GetMapping("/deployTask/addModelRegisterApi")
    String addModelRegisterApi(@RequestParam("deployTaskId") String deployTaskId);


    /**
     * 删除模型api
     *
     * @param deployTaskId 部署id
     * @return
     */
    @GetMapping("/deployTask/deleteModelRegisterApi")
    String deleteModelRegisterApi(@RequestParam("deployTaskId") String deployTaskId);


    /**
     * 获取部署成功模型列表
     *
     * @return 部署成功模型列表
     */
    @GetMapping("/deployTask/queryAllDeployedModel")
    List<DeployTaskVO> queryAllDeployedModel(@RequestParam(value = "projectSpaceId", required = false) String projectSpaceId);

    /**
     * 意图识别Lora模型通用接口
     * @param req
     * @return
     */
    @PostMapping("/deployTask/intentRecognize")
    IntentRecognizeResp intentRecognize(@RequestBody IntentRecognizeVO req);


    /**
     * 查询部署成功的测试模型
     *
     * @return DeployTask
     */
    @GetMapping("/deployTask/queryCompletedTestDeployTask")
    DeployTask queryCompletedTestDeployTask();


    /**
     * 远程 提交部署任务
     *
     * @param deployTaskRemoteParam 提交参数对象
     * @return String
     */
    @PostMapping("/deployTask/submitDeployTask")
    String submitDeployTask(@RequestBody DeployTaskRemoteParam deployTaskRemoteParam);

    /**
     * 远程 获取部署任务状态
     *
     * @param deployTaskRemoteParam 部署提交信息
     * @return String
     */
    @PostMapping("/deployTask/statusDeployTask")
    String statusDeployTask(@RequestBody DeployTaskRemoteParam deployTaskRemoteParam);

    /**
     * 组装提交部署任务参数
     *
     * @param deployTaskId 部署ID
     * @param trainTaskVO  训练任务ID
     * @return JSONObject
     */
    @PostMapping("/deployTask/buildDeployParam")
    DeployTaskSubmitParam buildDeployParam(@RequestParam("deployTaskId") Long deployTaskId, @RequestBody TrainTaskVO trainTaskVO);

    /**
     * 远程 删除部署任务
     *
     * @param deployTaskRemoteParam 部署提交信息
     * @return String
     */
    @PostMapping("/deployTask/deleteDeployTask")
    String deleteDeployTask(@RequestBody DeployTaskRemoteParam deployTaskRemoteParam);


    /**
     * 获取部署任务url
     *
     * @param deployTarget 部署目标
     * @param deployTaskId 部署ID
     * @return String
     */
    @GetMapping("/deployTask/getDeployUrl")
    String getDeployUrl(@RequestParam("deployTarget") String deployTarget, @RequestParam("deployTaskId") Long deployTaskId);

}
