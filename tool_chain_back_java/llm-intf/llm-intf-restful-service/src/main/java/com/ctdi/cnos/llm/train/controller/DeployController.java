package com.ctdi.cnos.llm.train.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ctdi.cnos.llm.annotation.AuthIgnore;
import com.ctdi.cnos.llm.base.constant.ApplicationConstant;
import com.ctdi.cnos.llm.base.constant.TrainConstants;
import com.ctdi.cnos.llm.beans.log.MmLog;
import com.ctdi.cnos.llm.beans.train.DeployRegistryReq;
import com.ctdi.cnos.llm.beans.train.deployTask.*;
import com.ctdi.cnos.llm.feign.log.LogServiceClientFeign;
import com.ctdi.cnos.llm.feign.train.DeployTaskServiceClientFeign;
import com.ctdi.cnos.llm.response.ErrorCodeEnum;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.util.MessageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wangyb
 * @date 2024/7/31 15:49
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/deploy")
@Api(tags = {"模型部署"})
@Slf4j
public class DeployController {


    private final DeployTaskServiceClientFeign deployTaskClient;
    private final LogServiceClientFeign logServiceClientFeign;


    @AuthIgnore
    @PostMapping("/updateCitedStatus")
    //@PostMapping("/callbackDeployAgentStatus")
    @ApiOperation(value = "远程变更训练模型部署引用状态", notes = "远程变更训练模型部署引用状态")
    public OperateResult<String> callbackDeployAgentStatus(@RequestBody DeployTaskAgentStatusCallback deployAgentStatus) {
        String interfaceUrl = "intf-restful-service/deploy/updateCitedStatus";
        if (deployAgentStatus.getDeployTaskId() == null) {
            String errorMessage = "deployTaskId不能为空";
            OperateResult<String> result = OperateResult.error(errorMessage);
            this.recordLog(0L, interfaceUrl, "远程变更训练模型部署引用状态接口", "0", JSON.toJSONString(deployAgentStatus), result.getMessage());
            return result;
        }
        try {
            if(CharSequenceUtil.isNotBlank(deployAgentStatus.getCitedStatus()) && CharSequenceUtil.equals(deployAgentStatus.getCitedStatus(),"1")){
                //citedStatus 是否被智能体引用(1是; 0否)
                deployAgentStatus.setAgentStatus(true);
            }
            log.info("远程变更训练模型部署引用状态:" + JSON.toJSONString(deployAgentStatus));
            long start = System.currentTimeMillis();
            OperateResult<String> result = deployTaskClient.updateAgentStatus(deployAgentStatus);
            long end = System.currentTimeMillis();
            this.recordLog(end - start, interfaceUrl, "远程变更训练模型部署引用状态接口", "0", JSON.toJSONString(deployAgentStatus), JSON.toJSONString(result));
            return result;
        } catch (Exception exception) {
            log.error("远程变更训练模型部署引用状态异常", exception);
            this.recordLog(0L, interfaceUrl, "远程变更训练模型部署引用状态接口", "", JSON.toJSONString(deployAgentStatus), MessageUtils.getMessage(exception.getMessage()));
            return OperateResult.error(MessageUtils.getMessage(exception.getMessage()));
        }
    }

    @PostMapping("/modelRegisterApi")
    @ApiOperation(value = "模型注册API", notes = "模型注册API")
    public OperateResult<String> modelRegisterApi(@RequestBody DeployRegistryReq req) {
        String deployTaskId = req.getDeployTaskId();
        String status = req.getStatus();
        Assert.isTrue("1".equals(status) || "0".equals(status), "状态值请设置为(1上线|0下线)");
        MmLog mmLog = logServiceClientFeign.dataAssembly("", "", "1".equals(status) ? "新增模型注册API" : "删除模型注册API");
        try {
            log.info("模型部署回调接口入参为:" + JSONObject.toJSONString(req));
            long start = System.currentTimeMillis();
            String result = "1".equals(status) ? deployTaskClient.addModelRegisterApi(deployTaskId):deployTaskClient.deleteModelRegisterApi(deployTaskId);
            long end = System.currentTimeMillis();
            mmLog.setDuration(end - start);
            mmLog.setRequestParams(JSONObject.toJSONString(req));
            mmLog.setResponseParams(result);
            mmLog.setResponseTime(new Date());
            mmLog.setStatusCode("0");
            mmLog.setInterfaceUrl("intf-restful-service/deploy/" + ("1".equals(status) ? "addModelRegisterApi" : "deleteModelRegisterApi"));
            logServiceClientFeign.addLog(mmLog);
            return new OperateResult<>(true, "操作成功", result);
        } catch (Exception exception) {
            log.error("1".equals(status) ? "新增模型注册API异常" : "删除模型注册API异常", exception);
            return new OperateResult<>(false, MessageUtils.getMessage(exception.getMessage()), null);
        }
    }


    @GetMapping("/queryAllDeployedModel")
    @ApiOperation(value = "查询所有部署模型", notes = "查询所有部署模型")
    public OperateResult<List<DeployTaskVO>> queryAllDeployedModel(DeployTaskReq deployTaskReq) {
        MmLog mmLog = logServiceClientFeign.dataAssembly("", "", "查询所有部署模型");
        try {
            long start = System.currentTimeMillis();
            List<DeployTaskVO> result = deployTaskClient.queryAllDeployedModel(deployTaskReq.getProjectSpaceId());
            long end = System.currentTimeMillis();
            mmLog.setDuration(end - start);
            mmLog.setRequestParams("");
            mmLog.setResponseParams(Convert.toStr(result));
            mmLog.setResponseTime(new Date());
            mmLog.setStatusCode("0");
            mmLog.setInterfaceUrl("intf-restful-service/deploy/queryAllDeployedModel");
            logServiceClientFeign.addLog(mmLog);
            return new OperateResult<>(true, "操作成功", result);
        } catch (Exception exception) {
            log.error("查询所有部署模型异常", exception);
            return new OperateResult<>(false, MessageUtils.getMessage(exception.getMessage()), null);
        }
    }

    @AuthIgnore
    @PostMapping("/remotedeploy")
    @ApiOperation(value = "远程创建训练模型部署", notes = "远程创建训练模型部署")
    public OperateResult<String> remoteDeploy(@RequestBody DeployTaskVO deployTaskVO) {
        // 日志记录
        String interfaceUrl = "intf-restful-service/deploy/remotedeploy";
        if (deployTaskVO.getModelId() == null) {
            String errorMessage = "modelId";
            OperateResult<String> result = OperateResult.error(errorMessage);
            this.recordLog(0L, interfaceUrl, "远程创建训练模型部署接口", "0", JSON.toJSONString(deployTaskVO), result.getMessage());
            return result;
        }
        try {
            log.info("远程创建训练模型部署:" + JSON.toJSONString(deployTaskVO));
            deployTaskVO.setDeployBelong("2");
            long start = System.currentTimeMillis();
            String result = deployTaskClient.remoteDeploy(deployTaskVO);
            long end = System.currentTimeMillis();
            this.recordLog(end - start, interfaceUrl, "远程创建训练模型部署接口", "0", JSON.toJSONString(deployTaskVO), JSON.toJSONString(result));
            return null != result ? OperateResult.successMessage(ApplicationConstant.UPDATE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_SAVE_DEFAULT);
        } catch (Exception exception) {
            log.error("远程创建训练模型部署异常", exception);
            this.recordLog(0L, interfaceUrl, "远程创建训练模型部署接口", "", JSON.toJSONString(deployTaskVO), MessageUtils.getMessage(exception.getMessage()));
            return OperateResult.error(MessageUtils.getMessage(exception.getMessage()));
        }
    }

    private void recordLog(long duration, String interfaceUrl, String interfaceName, String statusCode, String requestParams, String responseParams) {
        try {
            MmLog mmLog = logServiceClientFeign.dataAssembly("", "", interfaceName);
            mmLog.setDuration(duration);
            mmLog.setRequestParams(requestParams);
            mmLog.setResponseParams(responseParams);
            mmLog.setResponseTime(new Date());
            mmLog.setStatusCode(statusCode);
            mmLog.setInterfaceUrl(interfaceUrl);
            logServiceClientFeign.addLog(mmLog);
        } catch (Exception e) {
            log.error("记录日志异常", e);
        }
    }

    @AuthIgnore
    @PostMapping("/deployCallbackIR")
    public OperateResult<String> deployCallbackIR(@RequestBody DeployTaskCallback deployTaskCallback) {
        MmLog mmLog = logServiceClientFeign.dataAssembly("", "", "意图识别部署回调接口");
        try {
            log.info("回调接口入参为:{}", JSON.toJSONString(deployTaskCallback));
            long stime = System.currentTimeMillis();
            String infoStatus=deployTaskCallback.getResult();
            DeployTaskVO deployTaskVO=deployTaskClient.querySimpleById(deployTaskCallback.getId().toString());

            String status=deployTaskCallback.getStatus();
            /*if (CharSequenceUtil.equals(TrainConstants.DEPLOY_TASK_STATUS_COMPLETED, status)) {
                deployTaskVO.setDeployUrl(deployTaskCallback.getDeployUrl());
                String testResult = deployTaskClient.handlePostDeployment(deployTaskVO);
                if (CharSequenceUtil.isBlank(testResult)) {
                    deployTaskVO.setDeployUrl("");
                    status = TrainConstants.DEPLOY_TASK_STATUS_FAILED;
                    infoStatus = "模型推理, 连通性测试异常";
                }
            }*/

            if(deployTaskVO != null){
                DeployTaskVO vo=new DeployTaskVO();
                vo.setStatus(status);
                vo.setResult(infoStatus);
                vo.setDeployUrl(deployTaskVO.getDeployUrl());
                vo.setId(deployTaskVO.getId());
                Map<String,Object> result=deployTaskClient.callUpdateDeployTask(vo);

                long etime = System.currentTimeMillis();
                mmLog.setDuration(etime - stime);
                mmLog.setRequestParams(JSON.toJSONString(deployTaskCallback));
                mmLog.setResponseParams(result.toString());
                mmLog.setResponseTime(new Date());
                mmLog.setStatusCode("0");
                mmLog.setInterfaceUrl("intf-restful-service/deploy/deployCallbackIR");
                logServiceClientFeign.addLog(mmLog);
                return new OperateResult<>(MapUtil.getBool(result, "success"), MapUtil.getStr(result, "message"), null);
            } else{
                return new OperateResult<>(false, "不存在该意图识别模型", null);
            }
        } catch (Exception exception) {
            log.error("修改部署任务异常", exception);
            return OperateResult.error(MessageUtils.getMessage(exception.getMessage()));
        }
    }


}
