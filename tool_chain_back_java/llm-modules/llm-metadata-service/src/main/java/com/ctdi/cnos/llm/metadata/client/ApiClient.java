/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.metadata.client;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ctdi.cnos.llm.beans.log.MmLog;
import com.ctdi.cnos.llm.beans.meta.order.OrderCallBack;
import com.ctdi.cnos.llm.beans.train.IntentRecognition.Info3cTreeResp;
import com.ctdi.cnos.llm.beans.train.IntentRecognition.IntentRecognitionCorpusReq;
import com.ctdi.cnos.llm.beans.train.IntentRecognition.IntentRecognitionCorpusResp;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.feign.log.LogServiceClientFeign;
import com.ctdi.cnos.llm.metadata.config.ApplicationConfig;
import com.ctdi.cnos.llm.metadata.config.KnowledgeConfig;
import com.ctdi.cnos.llm.metadata.config.LlmOaAccessGrantedCallBackConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

/**
 * 知识库 client
 *
 * @author wangyb
 * @since 2024/7/15
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ApiClient {

    private final LogServiceClientFeign logClient;
    private final KnowledgeConfig knowledgeConfig;

    private final LlmOaAccessGrantedCallBackConfig llmOaConfig;

    private final ApplicationConfig appconfig;
    /**
     *
     * @return 知识库数据集列表
     */
    public String getDocList() {
        JSONObject param = new JSONObject();
        param.put("createBy", UserContextHolder.getUser().getEmployeeNumber());
        param.put("knStatus", 3);
        param.put("isWdd", "true");
        String interfaceName = "获取当前用户知识库数据集文件";
        return executeRequest(knowledgeConfig.isMock(), interfaceName, param, knowledgeConfig.getDocListUrl(), UserContextHolder.getUserId());
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
        if (mock && knowledgeConfig.getMockData().containsKey(interfaceName)) {
            return knowledgeConfig.getMockData().get(interfaceName);
        }
        return getResponse(interfaceName, param, url, userId);
    }

    /**
     * 启明门户权限开通回调接口
     *
     * @param orderDTOs         参数
     * @param userId        用户ID
     * @return 接口返回结果
     */
    public List<BigDecimal> userRoleCallBack(List<OrderCallBack> orderDTOs, Long userId) {
        Map headers = new HashMap();
        headers.put("X-APP-ID", llmOaConfig.getXAppId());
        headers.put("X-APP-KEY", llmOaConfig.getXAppKey());
        headers.put("timestamp", System.currentTimeMillis());
        String interfaceName = "启明门户权限开通回调接口";
        List<BigDecimal> orderIds = new ArrayList<>();
        for (OrderCallBack orderDTO : orderDTOs) {
            JSONObject param = (JSONObject) JSONObject.toJSON(orderDTO);
            try {
                String response = getResponse(interfaceName, param, llmOaConfig.getUrl(), userId);
                if ("成功".equals(response)){
                    orderIds.add(orderDTO.getId());
                }
            } catch (Exception e){
                log.error(e.getMessage(), e);
            }
        }
        return orderIds;
    }

    /**
     * 获取接口响应信息
     * @param interfaceName 接口名称
     * @param param 请求参数
     * @param url url
     * @param userId 用户ID
     * @return 响应结果
     */
    private String getResponse(String interfaceName, JSONObject param, String url, Long userId) {
        return getResponse(interfaceName, param.getInnerMap(), url, userId);
    }


    /**
     * 获取策选接口
     *
     * @return 策选列表
     */
    public String getStrategyList(Long userId) {
        String interfaceName = CharSequenceUtil.format("获取策选-策选列表接口");
        String url = appconfig.getStrategyListUrl();
//        String url = "http://10.143.47.95:18080/getway-route/cnos/v1/ai_application/calculate_v2";
        return getResponse(interfaceName, new HashMap(), url, userId, new HashMap());
    }

    /**
     * 获取接口响应信息
     * @param interfaceName 接口名称
     * @param param 请求参数
     * @param url url
     * @param userId 用户ID
     * @return 响应结果
     */
    private String getResponse(String interfaceName, Map<String, Object> param, String url, Long userId) {
        String responseBody = null;
        int statusCode = 0;
        log.info(interfaceName + " 请求url：" + url + " , 请求参数：" + param);
        MmLog mmLog = this.defaultLog(interfaceName, JSON.toJSONString(param), url, userId);
        long start = System.currentTimeMillis();
        try {
            try (HttpResponse response = HttpRequest.post(url)
                    .body(JSON.toJSONString(param))
                    .contentType(ContentType.JSON.getValue())
                    .execute()) {
                responseBody = response.body();
                statusCode = response.getStatus();
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
     * 获取接口响应信息
     * @param interfaceName 接口名称
     * @param param 请求参数
     * @param url url
     * @param userId 用户ID
     * @return 响应结果
     */
    private String getResponse(String interfaceName, Map<String, Object> param, String url, Long userId, Map headers) {
        String responseBody = null;
        int statusCode = 0;
        log.info(interfaceName + " 请求url：" + url + " , 请求参数：" + param);
        MmLog mmLog = this.defaultLog(interfaceName, JSON.toJSONString(param), url, userId);
        long start = System.currentTimeMillis();
        try {
            try (HttpResponse response = HttpRequest.post(url)
                    .header(headers)
                    .body(JSON.toJSONString(param))
                    .contentType(ContentType.JSON.getValue())
                    .execute()) {
                responseBody = response.body();
                statusCode = response.getStatus();
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
     * 意图识别语料列表查询
     *
     * @param req
     * @param userId
     * @return
     */
    public IntentRecognitionCorpusResp intentionRecognitionCorpusList(IntentRecognitionCorpusReq req, Long userId) {
        String interfaceName = "调用意图识别语料列表接口";
        IntentRecognitionCorpusResp resp = new IntentRecognitionCorpusResp();
        log.info("================ 调用意图识别语料列表接口 ====================");
        String paramStr = JSON.toJSONString(req);
        JSONObject paramJson = JSON.parseObject(paramStr);
        String response = executeRequest(knowledgeConfig.isMock(), interfaceName, paramJson, knowledgeConfig.getOwnIntentionRecognitionCorpusUrl(), userId);
        if (CharSequenceUtil.isNotBlank(response)) {
            resp = JSONObject.parseObject(response, IntentRecognitionCorpusResp.class);
        }
        return resp;
    }

    /**
     * 3c信息查询
     * @param userId
     * @return
     */
    public Info3cTreeResp info3cTreeList(Long userId) {
        String interfaceName = "调用3c信息查询接口";
        Info3cTreeResp resp = new Info3cTreeResp();
        log.info("================ 调用3c信息查询接口 ====================");
        String response = getHttpResponse(interfaceName, new HashMap(), knowledgeConfig.getInfo3cTreeUrl(), userId,new HashMap());
        if (CharSequenceUtil.isNotBlank(response)) {
            resp = JSONObject.parseObject(response, Info3cTreeResp.class);
        }
        return resp;
    }

    private String getHttpResponse(String interfaceName, Map<String, Object> param, String url, Long userId, Map headers) {
        String responseBody = null;
        int statusCode = 0;
        log.info(interfaceName + " 请求url：" + url + " , 请求参数：" + param);
        MmLog mmLog = this.defaultLog(interfaceName, JSON.toJSONString(param), url, userId);
        long start = System.currentTimeMillis();
        try {
            if (knowledgeConfig.isMock() && knowledgeConfig.getMockData().containsKey(interfaceName)) {
                responseBody = knowledgeConfig.getMockData().get(interfaceName);
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
     * ftp下载远程地址
     * @param remoteFilePaths  远程文件地址列表
     * @param tmpDir 数据集保存位置
     * @return 新文件列表
     */
    public List<String> downloadRemoteFile(List<String> remoteFilePaths, String tmpDir) {
        //本地模拟文件下载
        log.info("模拟ftp下载:" + knowledgeConfig.isMock());
        if (knowledgeConfig.isMock()) {
            List<String> newFilePaths = CollUtil.newArrayList();
            for (String remoteFilePath : remoteFilePaths) {

                Path sourcePath = Paths.get(remoteFilePath);
                String newFilePath = sourcePath.getParent().toString() + "/" + UUID.randomUUID() + ".jsonl";
                Path targetPath = Paths.get(newFilePath);
                try {
                    // 复制文件并重命名
                    Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    newFilePaths.add(newFilePath);
                    log.info("文件复制并重命名成功: " + newFilePath);
                } catch (IOException e) {
                    throw new RuntimeException("文件复制或重命名失败:" + newFilePath + e);
                }
            }
            return newFilePaths;
        }

        List<String> newFilePaths = new ArrayList<>();
        String server = knowledgeConfig.getIp();
        int port = knowledgeConfig.getPort();
        String username = knowledgeConfig.getUsername();
        String password = knowledgeConfig.getPassword();

        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            ftpClient.login(username, password);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            for (String remoteFilePath : remoteFilePaths) {
                String newFilePath = tmpDir + File.separator+ UUID.randomUUID() + ".jsonl";
                newFilePaths.add(newFilePath);
                try (FileOutputStream fos = new FileOutputStream(newFilePath)) {
                    //下载远程文件
                    String tempFilePath = knowledgeConfig.getDocDir() + remoteFilePath;
                    boolean success = ftpClient.retrieveFile(tempFilePath, fos);
                    if (success) {
                        log.info("成功下载文件: " + tempFilePath + "; 新文件地址：" + newFilePath);
                    } else {
                        String replacePath = tempFilePath.replace(".jsonl", ".json");
                        boolean flag = ftpClient.retrieveFile(replacePath, fos);
                        if (!flag) {
                            throw new RuntimeException("文件不存在: " + tempFilePath);
                        } else {
                            log.info("成功下载文件: " + tempFilePath + "; 新文件地址：" + newFilePath);
                        }
                    }
                } catch (IOException e) {
                    log.error("下载文件时出现异常: " + e.getMessage());
                    throw new RuntimeException("下载文件时出现异常");
                }
            }

        } catch (IOException e) {
            log.error("FTP连接时出现异常: " + e.getMessage());
            throw new RuntimeException("FTP连接时出现异常");
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return newFilePaths;
    }




}
