package com.ctdi.cnos.llm.train.contoller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ctdi.cnos.llm.annotation.AuthIgnore;
import com.ctdi.cnos.llm.base.constant.ApplicationConstant;
import com.ctdi.cnos.llm.base.constant.SystemConstant;
import com.ctdi.cnos.llm.base.constant.TrainConstants;
import com.ctdi.cnos.llm.beans.train.IntentRecognition.IntentRecognizeReq;
import com.ctdi.cnos.llm.beans.train.IntentRecognition.IntentRecognizeResp;
import com.ctdi.cnos.llm.beans.train.IntentRecognition.IntentRecognizeVO;
import com.ctdi.cnos.llm.beans.train.deployTask.*;
import com.ctdi.cnos.llm.beans.train.trainTask.TrainTaskVO;
import com.ctdi.cnos.llm.response.ErrorCodeEnum;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.train.client.ApiClient;
import com.ctdi.cnos.llm.train.service.DeployTaskService;
import com.ctdi.cnos.llm.util.CommonResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 部署任务(DeployTask)表控制层
 *
 * @author wangyb
 * @since 2024-07-01 14:16:01
 */
@Api(tags = {"DeployTaskController接口"})
@RestController
@RequestMapping(value = "/deployTask")
@RequiredArgsConstructor
public class DeployTaskController {

    private final DeployTaskService deployTaskService;

    private final ApiClient apiClient;


    @PostMapping("/queryList")
    @ApiOperation(value = "查询部署模型列表", notes = "查询部署模型列表")
    public List<DeployTaskVO> queryList(@RequestBody DeployTaskVO vo) {
        return deployTaskService.queryList(vo);
    }

    @PostMapping("/queryListByCategory")
    @ApiOperation(value = "查询意图识别部署模型列表", notes = "查询意图识别部署模型列表")
    public List<DeployTaskVO> queryListByCategory(@RequestBody DeployTaskVO vo) {
        return deployTaskService.queryListByCategory(vo);
    }

    @GetMapping("/detail")
    @ApiOperation(value = "部署模型详情", notes = "部署模型详情")
    public DeployTaskVO detail(@RequestParam("deployTaskId") String deployTaskId) {
        return deployTaskService.queryById(Convert.toLong(deployTaskId));
    }

    @AuthIgnore
    @GetMapping("/queryById")
    @ApiOperation(value = "部署模型简单详情", notes = "部署模型简单详情")
    public DeployTaskVO querySimpleById(@RequestParam("deployTaskId") String deployTaskId) {
        return deployTaskService.queryById(Convert.toLong(deployTaskId));
    }

    @AuthIgnore
    @GetMapping("/queryByRegisterId")
    @ApiOperation(value = "部署模型简单详情", notes = "部署模型简单详情")
    public DeployTask querySimpleByRegisterId(@RequestParam("registerId") Long registerId) {
        return deployTaskService.querySimpleByRegisterId(registerId);
    }


    @AuthIgnore
    @PostMapping("/add")
    @ApiOperation(value = "新增部署模型", notes = "新增部署模型")
    public Long add(@RequestBody DeployTaskVO deployTask) {
        return deployTaskService.save(deployTask);
    }


    @PostMapping("/updateById")
    @ApiOperation(value = "更新部署任务状态", notes = "更新部署任务状态")
    @AuthIgnore
    public OperateResult<String> updateById(@RequestBody DeployTaskVO deployTask) {
        if (CharSequenceUtil.isBlank(deployTask.getDeployTaskId()) || CharSequenceUtil.isBlank(deployTask.getStatus())) {
            return OperateResult.error("部署任务ID或状态不能为空！");
        }
        final ArrayList<String> list = CollUtil.newArrayList(TrainConstants.DEPLOY_TASK_STATUS_DEPLOYING, TrainConstants.DEPLOY_TASK_STATUS_COMPLETED, TrainConstants.DEPLOY_TASK_STATUS_FAILED);
        Assert.isTrue(list.contains(deployTask.getStatus()), "状态请设置为(deploying | failed | completed)");
        Assert.isTrue(ObjectUtil.isNotEmpty(deployTaskService.queryById(deployTask.getId())), "部署任务不存在");
        return deployTaskService.updateById(deployTask) ? OperateResult.successMessage(ApplicationConstant.UPDATE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_SAVE_DEFAULT);
    }


    @GetMapping("/deleteById")
    @ApiOperation(value = "删除部署模型", notes = "删除部署模型")
    public OperateResult<String> deleteById(@RequestParam("deployTaskId") Long id) {
        DeployTask deployTask = deployTaskService.queryById(id);
        if (ObjectUtil.isEmpty(deployTask)) {
            return OperateResult.error("部署任务ID不存在");
        }

        if (SystemConstant.YES.equals(deployTask.getAgentStatus())) {
            throw new RuntimeException("当前模型已被智能体引用, 无法被删除, 请先解除绑定");
        }

        if (SystemConstant.YES.equals(deployTask.getRegisterStatus())) {
            throw new RuntimeException("当前模型已被注册到DCOOS, 无法被删除, 请先下线");
        }

        List<String> status = CollUtil.newArrayList(TrainConstants.DEPLOY_TASK_STATUS_DEPLOYING);

        if (StrUtil.isNotBlank(deployTask.getStatus()) && status.contains(deployTask.getStatus())) {
            return OperateResult.error("正在部署中的任务无法被删除");
        }
        return deployTaskService.deleteById(id) ? OperateResult.successMessage(ApplicationConstant.DELETE_MESSAGE) : OperateResult.error("删除失败！");
    }

    @PostMapping("/deleteTestById")
    @ApiOperation(value = "删除测试评估临时部署模型", notes = "删除测试评估临时部署模型")
    public OperateResult<String> deleteTestById(@RequestParam("deployTaskId") Long id) {
        return deployTaskService.deleteById(id) ? OperateResult.successMessage(ApplicationConstant.DELETE_MESSAGE) : OperateResult.error("删除失败！");
    }


    @ApiOperation(value = "定时查询部署状态", notes = "定时查询部署状态")
    @GetMapping("/callBackDeployStatus")
    @AuthIgnore
    public void callBackDeployStatus() {
        deployTaskService.callBackDeployStatus();
    }

    @ApiOperation(value = "测试部署地址", notes = "测试部署地址")
    @GetMapping("/handlePostDeployment")
    @AuthIgnore
    public String handlePostDeployment(DeployTask task) {
        String result=deployTaskService.handlePostDeployment(task);
        return result;
    }


    @ApiOperation(value = "定时查询部署发送", notes = "定时查询部署发送")
    @GetMapping("/callBackDeploySend")
    @AuthIgnore
    public void callBackDeploySend() {
        deployTaskService.callBackDeploySend();
    }


    @AuthIgnore
    @PostMapping("/updateAgentStatus")
    @ApiOperation(value = "修改模型引用状态", notes = "修改模型引用状态")
    public OperateResult<String> updateAgentStatus(@RequestBody DeployTaskAgentStatusCallback deployTaskAgentStatusCallback) {
        LambdaUpdateWrapper<DeployTask> updateWrapper = new LambdaUpdateWrapper<DeployTask>()
                .eq(DeployTask::getId, deployTaskAgentStatusCallback.getDeployTaskId())
                .eq(DeployTask::getStatus, TrainConstants.DEPLOY_TASK_STATUS_COMPLETED)
                .set(DeployTask::getAgentStatus, deployTaskAgentStatusCallback.isAgentStatus() ? SystemConstant.YES : SystemConstant.NO)
                .set(DeployTask::getModifyDate, DateUtil.date());
        deployTaskService.update(updateWrapper);
        return OperateResult.successMessage("修改模型引用状态成功");
    }

    @AuthIgnore
    @PostMapping("/callUpdateDeployTask")
    @ApiOperation(value = "回调更新部署任务", notes = "回调更新部署任务")
    public Map<String, Object> callUpdateDeployTask(@RequestBody DeployTaskVO deployTaskVO){
        deployTaskService.callUpdateDeployTask(deployTaskVO);
        return CommonResponseUtil.responseMap(true, "回调更新部署任务成功");
    }


    @GetMapping("/deleteExpiredDeployTask")
    @ApiOperation(value = "删除超时的部署模型", notes = "删除超时的部署模型")
    @AuthIgnore
    public String deleteExpiredDeployTask(@RequestParam(value = "offsetDay", defaultValue = "1") Integer offsetDay) {
        List<Long> deployTaskIds = deployTaskService.deleteExpiredDeployTask(offsetDay);
        if (ObjectUtil.isNotEmpty(deployTaskIds)) {
            return "删除超时" + offsetDay + "天的部署模型：" + JSON.toJSONString(deployTaskIds);
        }
        return null;
    }


    @GetMapping("/addModelRegisterApi")
    @ApiOperation(value = "新增模型注册API", notes = "新增模型注册API")
    public String addModelRegisterApi(@RequestParam("deployTaskId") String deployTaskId) {
        return deployTaskService.addModelRegisterApi(Convert.toLong(deployTaskId));
    }

    @GetMapping("/deleteModelRegisterApi")
    @ApiOperation(value = "删除模型注册API", notes = "删除模型注册API")
    public String deleteModelRegisterApi(@RequestParam("deployTaskId") String deployTaskId) {
        return deployTaskService.deleteModelRegisterApi(Convert.toLong(deployTaskId));
    }


    @GetMapping("/queryModelRegisterApi")
    @ApiOperation(value = "查询当前用户所有模型注册API", notes = "查询当前用户所有模型注册API")
    public JSONArray queryModelRegisterApi(@RequestParam(value = "pageIndex", defaultValue = "1") int pageIndex,
                                           @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return JSON.parseArray(deployTaskService.queryModelRegisterApi(pageIndex, pageSize));
    }

    @GetMapping("/queryAllDeployedModel")
    @ApiOperation(value = "查询所有部署模型", notes = "查询所有部署模型")
    public List<DeployTaskVO> queryAllDeployedModel(@RequestParam(value = "projectSpaceId", required = false) String projectSpaceId) {
        DeployTaskReq deployTaskReq = new DeployTaskReq();
        deployTaskReq.setProjectSpaceId(projectSpaceId);
        return deployTaskService.queryAllDeployedModel(deployTaskReq);
    }

    /**
     * 意图识别Lora模型通用接口
     * @param req
     * @return
     */
    @PostMapping("/intentRecognize")
    @ApiOperation(value = "意图识别Lora模型通用接口", notes = "意图识别Lora模型通用接口")
    public IntentRecognizeResp intentRecognize(@RequestBody IntentRecognizeVO req){
        return deployTaskService.intentRecognize(req);
    }


    @GetMapping("/queryCompletedTestDeployTask")
    @ApiOperation(value = "查询部署成功的测试模型", notes = "查询部署成功的测试模型")
    @AuthIgnore
    public DeployTask queryCompletedTestDeployTask() {
        return deployTaskService.queryCompletedTestDeployTask();
    }

    @ApiOperation(value = "提交部署任务", notes = "提交部署任务")
    @PostMapping("/submitDeployTask")
    @AuthIgnore
    public String submitDeployTask(@RequestBody DeployTaskRemoteParam deployTaskRemoteParam) {
        return apiClient.submitDeployTask(deployTaskRemoteParam.getParam(), deployTaskRemoteParam.getUserId(),
                deployTaskRemoteParam.getDeployTarget(), deployTaskRemoteParam.getSource());
    }

    @ApiOperation(value = "组装提交部署任务参数", notes = "组装提交部署任务参数")
    @PostMapping("/buildDeployParam")
    @AuthIgnore
    public DeployTaskSubmitParam buildDeployParam(@RequestParam("deployTaskId") Long deployTaskId, @RequestBody TrainTaskVO trainTaskVO) {
        return deployTaskService.buildDeployParam(deployTaskId, trainTaskVO);
    }

    @ApiOperation(value = "删除部署任务", notes = "删除部署任务")
    @PostMapping("/deleteDeployTask")
    @AuthIgnore
    public String deleteDeployTask(@RequestBody DeployTaskRemoteParam deployTaskParam) {
        return apiClient.deleteDeployTask(deployTaskParam.getDeployTaskId(), deployTaskParam.getDeployTarget(), deployTaskParam.getUserId(), deployTaskParam.getSource());
    }

    @ApiOperation(value = "状态部署任务", notes = "状态部署任务")
    @PostMapping("/statusDeployTask")
    @AuthIgnore
    public String statusDeployTask(@RequestBody DeployTaskRemoteParam deployTaskParam) {
        return apiClient.statusDeployTask(deployTaskParam.getDeployTaskId(), deployTaskParam.getDeployTarget(), deployTaskParam.getUserId(), deployTaskParam.getSource());
    }

    @ApiOperation(value = "获取部署任务url", notes = "获取部署任务url")
    @GetMapping("/getDeployUrl")
    @AuthIgnore
    public String getDeployUrl(@RequestParam("deployTarget") String deployTarget, @RequestParam("deployTaskId") Long deployTaskId) {
        return apiClient.buildDeployReasonUrl(deployTarget, deployTaskId);
    }
}
