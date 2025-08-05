/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.train.client;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.ctdi.cnos.llm.base.constant.TrainConstants;
import com.ctdi.cnos.llm.beans.log.MmLog;
import com.ctdi.cnos.llm.beans.meta.cluster.ClusterVO;
import com.ctdi.cnos.llm.beans.train.IntentRecognition.IntentRecognizeReq;
import com.ctdi.cnos.llm.beans.train.deployTask.DeployTaskSubmitParam;
import com.ctdi.cnos.llm.beans.train.deployTask.IrDeployRegisterIntentReq;
import com.ctdi.cnos.llm.beans.train.deployTask.IrDeployUnregisterIntentReq;
import com.ctdi.cnos.llm.beans.train.trainEval.TrainTaskEvalSubmitParam;
import com.ctdi.cnos.llm.beans.train.trainTask.TrainTask;
import com.ctdi.cnos.llm.beans.train.zhisuan.AiApplicationOrderV2Param;
import com.ctdi.cnos.llm.beans.train.zhisuan.AtomicAbilityFinishParam;
import com.ctdi.cnos.llm.feign.log.LogServiceClientFeign;
import com.ctdi.cnos.llm.feign.metadata.ClusterServiceClientFeign;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.train.config.ApplicationConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/**
 * 大模型接口 client
 *
 * @author huangjinhua
 * @since 2024/5/23
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ApiClient {

    private final LogServiceClientFeign logClient;
    private final ApplicationConfig config;

    private final ClusterServiceClientFeign clusterClient;

    private static final String TRAIN_HOST = "train";
    private static final String DEPLOY_HOST = "deploy";
    private static final String REASON_HOST = "reason";

    /**
     * 根据用途查询接口hostUrl
     *
     * @param code
     * @param use
     * @return
     */
    private String getHostUrlByCode(String code, String use) {
        OperateResult<ClusterVO> result = clusterClient.queryByCode(code);
        log.info("=======result 【{}】=======", JSON.toJSONString(result));
        if (result.isSuccess()) {
            ClusterVO data = result.getData();
            if (TRAIN_HOST.equals(use)) {
                return data.getTrainHostUrl();
            } else if (DEPLOY_HOST.equals(use)) {
                return data.getDeployHostUrl();
            } else if (REASON_HOST.equals(use)) {
                return data.getReasonHostUrl();
            }
        }
        return null;
    }

    /**
     * 智算任务开始
     *
     * @param param       大模型所需的参数
     * @param userId      日志记录的用户ID
     * @return String
     */
    public String aiApplicationOrderV2Task(AiApplicationOrderV2Param param, Long userId) {
        String interfaceName = "智算任务开始接口";
        String url = config.getAiApplicationOrderV2Url();
        String paramStr = JSON.toJSONString(param);
        JSONObject paramJson = JSON.parseObject(paramStr);
        return postResponse(interfaceName, paramJson, url, userId);
    }

    /**
     * 智算任务结束
     *
     * @param param       大模型所需的参数
     * @param userId      日志记录的用户ID
     * @return String
     */
    public String atomicAbilityFinishTask(AtomicAbilityFinishParam param, Long userId) {
        String interfaceName = "智算任务结束接口";
        String url = config.getAtomicAbilityFinishUrl();
        String paramStr = JSON.toJSONString(param);
        JSONObject paramJson = JSON.parseObject(paramStr);
        return postResponse(interfaceName, paramJson, url, userId);
    }

    /**
     * 训练任务下发
     *
     * @param param       大模型所需的参数
     * @param userId      日志记录的用户ID
     * @param trainTarget 模型训练目标（GZ：贵州，QD青岛）
     * @return String
     */
    public String submitTrainTask(Map<String, Object> param, Long userId, String trainTarget) {
        String interfaceName = "调用大模型训练下发接口";
        String url = this.getHostUrlByCode(trainTarget, TRAIN_HOST) + config.getSubmitTrainUrl();
        return postResponse(interfaceName, param, url, userId);
    }

    /**
     * 训练任务状态查询
     *
     * @param taskList 任务列表
     * @return String
     */
    public List<TrainTask> statusTrainTask(List<TrainTask> taskList) {
        String interfaceName = "调用大模型训练状态查询接口";
        List<TrainTask> result = new ArrayList<>();
        taskList.forEach(task -> {
            log.info("=====task.getTrainTarget【{}】=====TRAIN_HOST 【{}】====================", task.getTrainTarget(), TRAIN_HOST);
            String url = this.getHostUrlByCode(task.getTrainTarget(), TRAIN_HOST) + config.getStatusTrainUrl() + "/" + task.getId();
            String response = getResponse(interfaceName, null, url, task.getCreatorId(), new HashMap());
            if (CharSequenceUtil.isNotBlank(response)) {
                String resultStatus = Convert.toStr(JSONPath.eval(response, "$.status"), null);
                if (CharSequenceUtil.isNotBlank(resultStatus)) {
                    TrainTask trainTask = new TrainTask();
                    trainTask.setStatus(TrainConstants.INTERFACE_TRAIN_STATUS_MAP.getStr(resultStatus));
                    trainTask.setId(task.getId());
                    trainTask.setModifyDate(DateUtil.date());
                    result.add(trainTask);
                }
            }
        });
        return result;
    }


    /**
     * 大模型训练任务删除接口
     *
     * @param taskId      任务ID
     * @param userId      日志记录的用户ID
     * @param trainTarget 模型训练目标（GZ：贵州，QD青岛）
     * @return String
     */
    public String deleteTrainTask(Long taskId, Long userId, String trainTarget) {
        String interfaceName = "调用大模型训练任务删除接口";
        String url = this.getHostUrlByCode(trainTarget, TRAIN_HOST) + config.getModifyTrainUrl() + "/" + taskId;
        return getResponse(interfaceName, null, url, userId, new HashMap());

    }


    /**
     * 大模型训练任务终止接口
     *
     * @param taskId      任务ID
     * @param userId      日志记录的用户ID
     * @param trainTarget 模型训练目标
     * @return String
     */
    public String stopTrainTask(String taskId, Long userId, String trainTarget) {
        String interfaceName = "调用大模型训练任务终止接口";
        String url = this.getHostUrlByCode(trainTarget, TRAIN_HOST) + config.getModifyTrainUrl() + "/" + taskId;
        return getResponse(interfaceName, null, url, userId, new HashMap());
    }


    /**
     * 模型评估C-eval提交
     *
     * @param param      eavl评估任务下发参数
     * @param userId     用户ID
     * @param evalTarget 评估目标
     * @return 接口返回结果
     */
    public String submitTrainEvalTask(TrainTaskEvalSubmitParam param, Long userId, String evalTarget) {
        String interfaceName = "调用模型评估C-eval提交接口";
        String url = this.getHostUrlByCode(evalTarget, TRAIN_HOST) + config.getEvalTrainUrl();
        String paramStr = JSON.toJSONString(param);
        JSONObject paramJson = JSON.parseObject(paramStr);
        return postResponse(interfaceName, paramJson, url, userId);
    }

    /**
     * 删除模型通知智能体
     *
     * @param param  请求参数
     * @param userId 用户ID
     * @return 响应结果 {"result": "success"}
     */
    public String deleteAgentDeploy(JSONObject param, Long userId) {
        String interfaceName = "删除模型通知智能体接口";
        String url = config.getDeleteAgentDeployUrl();
        return postResponse(interfaceName, param, url, userId);
    }


    /**
     * 创建模型通知智能体
     *
     * @param param  请求参数
     * @param userId 用户ID
     * @return 响应结果 {"result": "success"}
     */
    public String createAgentDeploy(JSONObject param, Long userId) {
        String interfaceName = "创建模型通知智能体接口";
        String url = config.getCreateAgentDeployUrl();
        return postResponse(interfaceName, param, url, userId);
    }

    /**
     * 提交模型推理部署k8s
     *
     * @param param        部署任务下发参数
     * @param userId       用户ID
     * @param deployTarget 部署目标
     * @param source       调用来源（训练任务部署、评估任务部署）
     * @return 接口返回结果
     */
    public String submitDeployTask(DeployTaskSubmitParam param, Long userId, String deployTarget, String source) {
        String interfaceName = CharSequenceUtil.format("调用大模型模型推理部署任务接口-调用来源{}", source);
        String url = this.getHostUrlByCode(deployTarget, DEPLOY_HOST) + config.getSubmitDeployUrl();
        String paramStr = JSON.toJSONString(param);
        JSONObject paramJson = JSON.parseObject(paramStr);
        return postResponse(interfaceName, paramJson, url, userId);

    }

    /**
     * 删除部署任务_k8s
     *
     * @param deployTaskId 推理部署任务ID
     * @param userId       用户ID
     * @param source       调用来源（训练任务部署、评估任务部署）
     * @return 接口返回结果
     */
    public String deleteDeployTask(Long deployTaskId, String deployTarget, Long userId, String source) {
        String interfaceName = CharSequenceUtil.format("调用大模型模型推理部署任务删除接口-调用来源{}", source);
        String url = this.getHostUrlByCode(deployTarget, DEPLOY_HOST) + config.getDeleteDeployUrl() + "/" + deployTaskId;
        return getResponse(interfaceName, null, url, userId, new HashMap());
    }

    /**
     * 获取部署状态_k8s
     *
     * @param deployTaskId 推理部署任务ID
     * @param userId       用户ID
     * @param source       调用来源（（训练任务部署、评估任务部署）
     * @return 部署状态
     */
    public String statusDeployTask(Long deployTaskId, String deployTarget, Long userId, String source) {
        String interfaceName = CharSequenceUtil.format("调用大模型模型推理部署任务状态接口-调用来源{}", source);
        String url = this.getHostUrlByCode(deployTarget, DEPLOY_HOST) + config.getStatusDeployUrl() + "/" + deployTaskId;
        return getResponse(interfaceName, null, url, userId, new HashMap());
    }

    /**
     * 获取接口的推理接口地址
     *
     * @param deployTarget 部署任务 接口目标
     * @return 接口返回结果
     */
    public String buildDeployReasonUrl(String deployTarget, Long deployTaskId) {
        return this.getHostUrlByCode(deployTarget, REASON_HOST) + "/" + deployTaskId + config.getReasonUrl();
    }

    /**
     * 新增模型注册API
     *
     * @param userId 用户ID
     * @param map    请求参数
     * @return 请求返回
     */
    public String addModelRegisterApi(Long userId, Map<String, Object> map) {
        String interfaceName = "新增模型注册API";
        String url = config.getAddModelRegisterApiUrl();
        return postResponse(interfaceName, map, url, userId);
    }

    /**
     * 删除模型注册API
     *
     * @param userId 用户ID
     * @param apiId  api注册ID
     * @return 请求返回
     */
    public String deleteModelRegisterApi(Long userId, String apiId) {
        String interfaceName = "删除模型注册API";
        String url = config.getDeleteModelRegisterApiUrl() + apiId;
        return getResponse(interfaceName, null, url, userId, new HashMap());
    }

    /**
     * 查询当前用户所有模型注册API
     *
     * @param userId    用户ID
     * @param pageIndex 当前页
     * @param pageSize  每页条数
     * @param createBy  创建者工号
     * @return 当前用户所有模型注册API列表
     */
    public String queryModelRegisterApi(Long userId, int pageIndex, int pageSize, String createBy) {
        String interfaceName = "查询当前用户所有模型注册API";
        String url = String.format("%s?pageIndex=%s&pageSize=%s&createBy=%s", config.getQueryModelRegisterApiUrl(),
                pageIndex, pageSize, createBy);
        return getResponse(interfaceName, null, url, userId, new HashMap());
    }

    /**
     * 获取算网调度AI-智算平台接口（AI集群列表查询）
     *
     * @param userId 用户ID
     * @return AI集群列表
     */
    public String getAiClusterList(Long userId) {
        String interfaceName = CharSequenceUtil.format("获取算网调度AI-智算平台AI集群列表接口");
        String url = config.getAiClusterListUrl();
        return getResponse(interfaceName, null, url, userId, new HashMap());
    }

    /**
     * 模型发布推送智能体审批
     *
     * @param param  请求参数
     * @param userId 用户ID
     * @param authorization header参数传递
     * @return 响应结果 {"result": "success"}
     */
    public String publishModelPushAgent(JSONObject param, Long userId, String authorization) {
        String interfaceName = "模型发布推送智能体接口";
        String url = config.getPublishModelPushAgentUrl();
        Map header = new HashMap();
        header.put("Authorization", authorization);
        return postResponse(interfaceName, param, url, userId, header);
    }

    /**
     * 获取接口响应(body参数是Json)
     *
     * @param interfaceName 接口名称
     * @param param         参数
     * @param url           url
     * @param userId        用户ID
     * @return string
     */
    private String postResponse(String interfaceName, JSONObject param, String url, Long userId) {
        return postResponse(interfaceName, param.getInnerMap(), url, userId);
    }


    private String postResponse(String interfaceName, Map<String, Object> param, String url, Long userId) {
        String responseBody = null;
        int statusCode = 0;
        log.info("{} 请求url：{},请求参数：{}", interfaceName, url, JSON.toJSONString(param));
        MmLog mmLog = this.defaultLog(interfaceName, JSON.toJSONString(param), url, userId);
        long start = System.currentTimeMillis();
        try {
            if (config.isMock() && config.getMockData().containsKey(interfaceName)) {
                responseBody = config.getMockData().get(interfaceName);
            } else {
                try (HttpResponse response = HttpRequest.post(url)
                        .body(JSON.toJSONString(param))
                        .contentType(ContentType.JSON.getValue())
                        .execute()) {
                    responseBody = response.body();
                    statusCode = response.getStatus();
                }
            }
            long end = System.currentTimeMillis();
            mmLog.setDuration(end - start);
            mmLog.setResponseParams(responseBody);
            mmLog.setResponseTime(new Date());
            mmLog.setStatusCode(String.valueOf(statusCode));
            log.info(mmLog.getInterfaceName() + " 响应状态码：" + statusCode + " 响应体：" + responseBody);

        } catch (Exception ex) {
            log.error(interfaceName + "异常", ex);
            mmLog.setStatusCode("500");
            mmLog.setErrorMessage(ex.getMessage());
        } finally {
            // 调用接口日志记录
            logClient.addLog(mmLog);
        }
        return responseBody;
    }

    private String postResponse(String interfaceName, Map<String, Object> param, String url, Long userId, Map headers) {
        String responseBody = null;
        int statusCode = 0;
        log.info("{} 请求url：{},请求参数：{}", interfaceName, url, JSON.toJSONString(param));
        MmLog mmLog = this.defaultLog(interfaceName, JSON.toJSONString(param), url, userId);
        long start = System.currentTimeMillis();
        try {
            if (config.isMock() && config.getMockData().containsKey(interfaceName)) {
                responseBody = config.getMockData().get(interfaceName);
            } else {
                try (HttpResponse response = HttpRequest.post(url)
                        .body(JSON.toJSONString(param))
                        .addHeaders(CollUtil.isNotEmpty(headers) ? headers : new HashMap<>())
                        .contentType(ContentType.JSON.getValue())
                        .execute()) {
                    responseBody = response.body();
                    statusCode = response.getStatus();
                }
            }
            long end = System.currentTimeMillis();
            mmLog.setDuration(end - start);
            mmLog.setResponseParams(responseBody);
            mmLog.setResponseTime(new Date());
            mmLog.setStatusCode(String.valueOf(statusCode));
            log.info(mmLog.getInterfaceName() + " 响应状态码：" + statusCode + " 响应体：" + responseBody);

        } catch (Exception ex) {
            log.error(interfaceName + "异常", ex);
            mmLog.setStatusCode("500");
            mmLog.setErrorMessage(ex.getMessage());
        } finally {
            // 调用接口日志记录
            logClient.addLog(mmLog);
        }
        return responseBody;
    }


    private String getResponse(String interfaceName, Map<String, Object> param, String url, Long userId, Map headers) {
        String responseBody = null;
        int statusCode = 0;
        log.info(interfaceName + " 请求url：" + url + " , 请求参数：" + param);
        MmLog mmLog = this.defaultLog(interfaceName, JSON.toJSONString(param), url, userId);
        long start = System.currentTimeMillis();
        try {
            if (config.isMock() && config.getMockData().containsKey(interfaceName)) {
                responseBody = config.getMockData().get(interfaceName);
            } else {
                try (HttpResponse response = HttpRequest
                        .get(url)
                        .addHeaders(CollUtil.isNotEmpty(headers) ? headers : new HashMap<>())
                        .execute()) {
                    responseBody = response.body();
                    statusCode = response.getStatus();
                }
            }
            long end = System.currentTimeMillis();
            mmLog.setDuration(end - start);
            mmLog.setResponseParams(responseBody);
            mmLog.setResponseTime(new Date());
            mmLog.setStatusCode(String.valueOf(statusCode));
            log.info(mmLog.getInterfaceName() + " 响应状态码：" + statusCode + " 响应体：" + responseBody);

        } catch (Exception ex) {
            log.error(interfaceName + "异常", ex);
            mmLog.setStatusCode("500");
            mmLog.setErrorMessage(ex.getMessage());
        } finally {
            // 调用接口日志记录
            logClient.addLog(mmLog);
        }
        return responseBody;
    }

    /**
     * 默认日志对象封装
     *
     * @param interfaceName 接口名称
     * @param param         请求参数
     * @param url           请求url
     * @param userId        用户ID
     * @return MmLog
     */
    private MmLog defaultLog(String interfaceName, String param, String url, Long userId) {
        MmLog mmLog = logClient.dataAssembly(Convert.toStr(userId, null), Convert.toStr(userId, null), interfaceName);
        if (mmLog == null) {
            mmLog = new MmLog();
            mmLog.setId(new BigDecimal(IdUtil.getSnowflakeNextId()));
        }
        mmLog.setRequestParams(JSON.toJSONString(param));
        mmLog.setInterfaceUrl(url);
        return mmLog;
    }

    /**
     * 执行接口请求(包含Mock数据)
     *
     * @param mock          是否mock
     * @param interfaceName 接口名称
     * @param param         参数
     * @param url           请求地址
     * @param userId        用户ID
     * @return 接口返回结果
     */
    public String executeRequest(boolean mock, String interfaceName, JSONObject param, String url, Long userId) {
        if (mock && config.getMockData().containsKey(interfaceName)) {
            return config.getMockData().get(interfaceName);
        }
        return postResponse(interfaceName, param, url, userId);
    }

    /**
     * 执行接口请求(包含Mock数据)
     *
     * @param mock          是否mock
     * @param interfaceName 接口名称
     * @param param         参数
     * @param url           请求地址
     * @param userId        用户ID
     * @return 接口返回结果
     */
    public String executeGetRequest(boolean mock, String interfaceName, JSONObject param, String url, Long userId) {
        if (mock && config.getMockData().containsKey(interfaceName)) {
            return config.getMockData().get(interfaceName);
        }
        return getResponse(interfaceName, param, url, userId, new HashMap());
    }


    /**
     * 获取智能体项目空间
     *
     * @param Authorization
     * @param userId
     * @return
     */
    public String getTenantsTask(String Authorization, Long userId) {
        String interfaceName = CharSequenceUtil.format("获取智能体项目空间");
        String url = config.getAgentTenantsApiUrl();
        Map map = new HashMap();
        map.put("Authorization", Authorization);
        return getResponse(interfaceName, map, url, userId, map);
    }

    /**
     * 动态注册意图识别Lora模型接口
     * @param param
     * @param userId
     * @return
     */
    public String registerIntent(IrDeployRegisterIntentReq param, Long userId) {
        String interfaceName = "动态注册意图识别Lora模型接口";
        String url = config.getRegisterIntentUrl();
        String paramStr = JSON.toJSONString(param);
        JSONObject paramJson = JSON.parseObject(paramStr);
        return postResponse(interfaceName, paramJson, url, userId);
    }

    /**
     * 卸载意图识别Lora模型接口
     * @param param
     * @param userId
     * @return
     */
    public String unregisterIntent(IrDeployUnregisterIntentReq param, Long userId) {
        String interfaceName = "卸载意图识别Lora模型接口";
        String url = config.getUnregisterIntentUrl();
        String paramStr = JSON.toJSONString(param);
        JSONObject paramJson = JSON.parseObject(paramStr);
        return postResponse(interfaceName, paramJson, url, userId);
    }

    /**
     * 意图识别Lora模型通用接口
     * @param param
     * @param userId
     * @return
     */
    public String intentRecognize(IntentRecognizeReq param, Long userId) {
        String interfaceName = "意图识别Lora模型通用接口";
        String url = config.getIntentRecognizeUrl();
        String paramStr = JSON.toJSONString(param);
        JSONObject paramJson = JSON.parseObject(paramStr);
        return postResponse(interfaceName, paramJson, url, userId);
    }

    /**
     * 通知意图识别load文件
     * @param taskId
     * @param userId
     * @return
     */
    public String intentSync(Long taskId, Long userId,String deployTarget) {
        String interfaceName = "通知意图识别load文件";
        String url = this.getHostUrlByCode(deployTarget, DEPLOY_HOST)+config.getIntentSyncUrl()+"/"+taskId;
        return getResponse(interfaceName, new HashMap<>(), url, userId,new HashMap<>());
    }


}
