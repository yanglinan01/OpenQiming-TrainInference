package com.ctdi.cnos.llm.interceptor;

import com.ctdi.cnos.common.core.utils.ServletUtils;
import com.ctdi.cnos.common.core.utils.StringUtils;
import com.ctdi.cnos.common.core.utils.ip.IpUtils;
import com.ctdi.cnos.llm.context.WebSocketAuthorizationThreadLocal;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Component
public class FeignAuthInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {

        HttpServletRequest httpServletRequest = ServletUtils.getRequest();
        if (StringUtils.isNotNull(httpServletRequest)) {
            Map<String, String> headers = ServletUtils.getHeaders(httpServletRequest);

            String authentication = (String)headers.get("Authorization");
            if (StringUtils.isNotEmpty(authentication)) {
                requestTemplate.header("Authorization", new String[]{authentication});
            }

            requestTemplate.header("X-Forwarded-For", new String[]{IpUtils.getIpAddr(ServletUtils.getRequest())});
        }
        else {
            String wsAuthorization = WebSocketAuthorizationThreadLocal.getWsAuthorization();
            if (StringUtils.isNotEmpty(wsAuthorization)) {
                requestTemplate.header("Authorization", wsAuthorization);
            }
        }
    }
}