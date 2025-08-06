package com.ctdi.cnos.llm.advise;



import com.ctdi.cnos.llm.annotation.EnableResponseAdvice;
import com.ctdi.cnos.llm.annotation.IgnoreResponseAdvice;
import com.ctdi.cnos.llm.response.CommonResponse;
import com.ctdi.cnos.llm.util.CommonResponseUtil;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * <h1>通用响应拦截</h1>
 *
 *
 */
@RestControllerAdvice(basePackages = "com.ctdi.cnos.llm")
public class CommonResponseDataAdvice implements ResponseBodyAdvice<Object> {

    /**
     * <h2>判断是否需要对响应进行处理</h2>
     * @param methodParameter
     * @param aClass
     * @return
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        if(methodParameter.getMethod().isAnnotationPresent(EnableResponseAdvice.class)){
            return true;
        }

        if(methodParameter.getDeclaringClass().isAnnotationPresent(EnableResponseAdvice.class) &&
                !methodParameter.getMethod().isAnnotationPresent(IgnoreResponseAdvice.class)){
            return true;
        }
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object response, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (response instanceof CommonResponse){
            return response;
        }
        return CommonResponseUtil.success(response);
    }
}
