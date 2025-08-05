package com.ctdi.cnos.llm.util;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * http连接工具类
 */
@Slf4j
public class HttpUtil {

    /**
     * 制定返回类型POST请求
     *
     * @param urlPath      请求地址
     * @param requestParam 请求参数
     * @param clazz        返回实体类型
     * @param <T>
     * @return
     */
    public static <T> T doPostFormEntity(String urlPath, Map<String, Object> requestParam, Class<T> clazz) {
        RestTemplate restTemplate = HttpUtil.getTemplateInstance();
        log.info("http请求开始，路径:{}", urlPath);
        HttpHeaders headers = new HttpHeaders();
        // 设置请求参数类型
        headers.set("Content-Type","application/json;charset=UTF-8");
        // 参数与请求头
        HttpEntity<?> request = new HttpEntity<Object>(requestParam, headers);
        ResponseEntity<T> entity = restTemplate.postForEntity(urlPath, request, clazz);
        log.info("http请求成功，body:{}", entity.getBody());
        return entity.getBody();
    }

    /**
     * @param urlPath
     * @param authorization
     * @param requestParam
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T doPostHerderParamEntity(String urlPath, String authorization, Map<String, Object> requestParam, Class<T> clazz) {
        RestTemplate restTemplate = HttpUtil.getTemplateInstance();
        log.info("http请求开始，路径:{}", urlPath);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorization);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> request = new HttpEntity<>(requestParam, headers);
        ResponseEntity<T> entity = restTemplate.postForEntity(urlPath, request, clazz);
        log.info("http请求成功，body:{}", entity.getBody());
        return entity.getBody();
    }

    /**
     * 指定返回类型get请求
     *
     * @param urlPath      请求地址
     * @param requestParam 请求参数
     * @param clazz        返回值类型
     * @param <T>
     * @return
     */
    public static <T> T doGetEntity(String urlPath, Map<String, Object> requestParam, Class<T> clazz) {
        RestTemplate restTemplate = HttpUtil.getTemplateInstance();
        log.info("http请求开始，路径:{}", urlPath);
        ResponseEntity<T> entity = restTemplate.getForEntity(urlPath, clazz, requestParam);
        log.info("http请求成功，body:{}", entity.getBody());
        return entity.getBody();
    }

    public static <T> T doGetObject(String urlPath, Map<String, Object> requestParam, Class<T> clazz) {
        RestTemplate restTemplate = HttpUtil.getTemplateInstance();
        log.info("http请求开始，路径:{}", urlPath);
        return restTemplate.getForObject(urlPath, clazz, requestParam);
    }


    /**
     * 返回JSONObject POST请求
     *
     * @param urlPath
     * @param requestParam
     * @return
     */
    public static String doPostForm(String urlPath, Map<String, Object> requestParam) {
        RestTemplate restTemplate = HttpUtil.getTemplateInstance();
        log.info("http请求开始，路径:{}", urlPath);

        // 设置请求头为表单形式
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 构建请求体
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        for (Map.Entry<String, Object> entry : requestParam.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().toString();
            requestBody.add(key, value);
        }

        // 发送POST请求
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> entity = restTemplate.postForEntity(urlPath, requestEntity, String.class);

        // 检查请求响应状态码
        if (HttpStatus.OK.value() != entity.getStatusCodeValue()) {
            log.error("Http请求失败！");
            Map<String, String> res = new HashMap<>();
            res.put("code", "1");
            res.put("message", "Http请求失败");
            return JSONObject.toJSONString(res);
        }
        return entity.getBody();

    }

    /**
     * 返回JSONObject POST请求
     *
     * @param urlPath
     * @param paramList
     * @return
     */
    //post请求
    public static JSONObject doPostForListParam(String urlPath, List<Map<String, Object>> paramList) {
        RestTemplate restTemplate = HttpUtil.getTemplateInstance();
        log.info("http请求开始，路径:{}", urlPath);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            //  一定要设置header
            HttpHeaders headers = new HttpHeaders();
            String requestParam = objectMapper.writeValueAsString(paramList);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<?> request = new HttpEntity<Object>(requestParam, headers);
            ResponseEntity<String> entity = restTemplate.postForEntity(urlPath, request, String.class);
            if (HttpStatus.OK.value() != entity.getStatusCodeValue()) {
                log.error("Http请求失败！");
                Map<String, String> res = new HashMap<>();
                res.put("code", "1");
                res.put("message", "Http请求失败");
                return JSONObject.parseObject(String.valueOf(res));

            }
            log.info("http请求成功，body:{}", entity.getBody());
            return JSONObject.parseObject(entity.getBody());
        } catch (JsonProcessingException e) {
            Map<String, String> res = new HashMap<>();
            res.put("code", "1");
            res.put("message", "参数转换json失败");
            return JSONObject.parseObject(String.valueOf(res));
        }
    }

    public static String doPost(String url, Map<String, String> param) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建参数列表
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, param.get(key)));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
                httpPost.setEntity(entity);
            }
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }


    /**
     * 返回String  get请求
     *
     * @param urlPath
     * @return
     */
    //Get请求
    public static String doGetString(String urlPath) {
        RestTemplate restTemplate = HttpUtil.getTemplateInstance();
        log.info("http请求开始，路径:{}", urlPath);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> exchange = restTemplate.exchange(urlPath, HttpMethod.GET, entity, String.class);
        if (HttpStatus.OK.value() != exchange.getStatusCodeValue()) {
            log.error("Http请求失败！");

        }
        log.info("http请求成功，body:{}", entity.getBody());
        return exchange.getBody();
    }

    /**
     * 返回String  get请求
     *
     * @param urlPath
     * @return
     */
    //Get请求
    public static <T> T doGetForHeaderParam(String urlPath, HttpHeaders headers, Class<T> clazz) {
        log.info("http请求开始，路径:{}", urlPath);
        RestTemplate restTemplate = HttpUtil.getTemplateInstance();
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<T> exchange = restTemplate.exchange(urlPath, HttpMethod.GET, entity, clazz);
        log.info("http请求成功，body:{}", entity.getBody());
        return exchange.getBody();
    }

    /**
     * 生成RestTemplate对象
     *
     * @return
     */
    public static RestTemplate getTemplateInstance() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(15000);
        factory.setReadTimeout(150000);
        return new RestTemplate(factory);
    }

    public static String getClientIp() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String clientIp = "";
        if(null != attributes){
            HttpServletRequest request = attributes.getRequest();
            clientIp = request.getHeader("X-Forwarded-For");
            if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
                clientIp = request.getHeader("Proxy-Client-IP");
            }
            if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
                clientIp = request.getHeader("WL-Proxy-Client-IP");
            }
            if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
                clientIp = request.getRemoteAddr();
            }
        }
        return clientIp;
    }

    public static String getServerIp() {
        String ip = "";
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            if(null != inetAddress){
                ip = inetAddress.getHostAddress();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ip;
    }

}

