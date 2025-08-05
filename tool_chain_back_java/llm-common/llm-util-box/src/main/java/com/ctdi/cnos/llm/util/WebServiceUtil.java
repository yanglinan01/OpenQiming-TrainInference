/**
 * Copyright (c) 2024, CCSSOFT All Rights Reserved.
 */
package com.ctdi.cnos.llm.util;

import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.service.model.BindingInfo;
import org.apache.cxf.service.model.BindingOperationInfo;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;

/**
 * WebService 通用工具类
 *
 * @author huangjinhua
 * @since 2024/4/16
 */
@Slf4j
@Component
public class WebServiceUtil {

    /**
     * 调用webservice接口
     *
     * @param requestUrl 请求url
     * @param funCode    方法名
     * @param params     参数
     * @return String
     */
    public static String call(String requestUrl, String funCode, Object... params) {
        // 创建动态客户端
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient(requestUrl);
        Object[] objects;
        try {
            // 下面一段处理 WebService接口和实现类namespace不同的情况
            // CXF动态客户端在处理此问题时，会报No operation was found with the name的异常
            Endpoint endpoint = client.getEndpoint();
            QName qName = new QName(endpoint.getService().getName().getNamespaceURI(), funCode);
            BindingInfo bindingInfo = endpoint.getEndpointInfo().getBinding();
            if (bindingInfo.getOperation(qName) == null) {
                for (BindingOperationInfo operationInfo : bindingInfo.getOperations()) {
                    if (funCode.equals(operationInfo.getName().getLocalPart())) {
                        qName = operationInfo.getName();
                        break;
                    }
                }
            }
            objects = client.invoke(qName, params);
            return objects[0].toString();
        } catch (Exception e) {
            log.error("调用webService接口异常：", e);
            throw new RuntimeException("调用webService接口异常：" + e.getMessage());
        }
    }

    /**
     * 调用webservice接口
     *
     * @param url 请求webservice接口url
     * @param xml xml请求报文
     * @return String
     */
    public static String call(String url, String xml) {
        try {
            return HttpUtil.post(url, xml);
        } catch (Exception e) {
            log.error("调用webService接口异常：", e);
            throw new RuntimeException("调用webService接口异常：" + e.getMessage());
        }
    }

    /**
     * 发布 webservice 接口
     *
     * @param address        webservice接口发布地址
     * @param interfaceClass 接口类
     * @param implementor    接口实现类
     */
    public static void publish(String address, Class<?> interfaceClass, Class<?> implementor) {
        try {
            JaxWsServerFactoryBean factoryBean = new JaxWsServerFactoryBean();
            // 设置暴露地址
            factoryBean.setAddress(address);
            // 接口类
            factoryBean.setServiceClass(interfaceClass);
            // 设置实现类
            factoryBean.setServiceBean(implementor.newInstance());
            factoryBean.create();
            log.info("web service started! published at " + address);
        } catch (Exception e) {
            log.error("发布webService接口异常：", e);
            throw new RuntimeException("发布webService接口异常：" + e.getMessage());
        }
    }


    /**
     * 测试
     *
     * @param args
     */
    public static void main(String[] args) {
        /*String address = "http://localhost:8080/myservice";
        WebServiceUtil.publish(address, MyWebService.class, MyWebServiceImpl.class);*/

        String xml = "param";
        String resultXml = WebServiceUtil.call("http://localhost:8080/myservice?wsdl",
                "sayHello",
                xml);
        System.out.println(resultXml);

    }
}
