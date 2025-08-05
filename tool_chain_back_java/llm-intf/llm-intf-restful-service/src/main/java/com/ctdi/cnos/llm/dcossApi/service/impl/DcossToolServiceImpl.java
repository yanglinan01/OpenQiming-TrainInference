package com.ctdi.cnos.llm.dcossApi.service.impl;

import cn.hutool.core.convert.Convert;
import com.ctdi.cnos.llm.beans.register.DcoosApi;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.dcossApi.dao.DcossToolMapper;
import com.ctdi.cnos.llm.dcossApi.service.DcossToolService;
import com.ctdi.cnos.llm.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author hanfulei
 * @Date 2024/4/18 15:57
 * @Version 1.0
 **/
@Slf4j
@Service
@SuppressWarnings("all")
public class DcossToolServiceImpl implements DcossToolService {


    @Resource
    private DcossToolMapper dcoosApiMapper;

    @Override
    public String invoke(String guid, Map<String, Object> params) {

        try {

            DcoosApi dcoosApi = dcoosApiMapper.selectByPrimaryKey(guid);
            String apiUrl = dcoosApi.getApiUrl();
            String methodType = dcoosApi.getMethodType();
            String userId = Convert.toStr(UserContextHolder.getUser().getId());
            if ("post".equalsIgnoreCase(methodType)) {
                return HttpUtil.doPostHerderParamEntity(apiUrl,userId , params, String.class);
            }
            if ("get".equalsIgnoreCase(methodType)) {
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", userId);
                return HttpUtil.doGetForHeaderParam(apiUrl, headers, String.class);
            }else {
                return "httpClient接口类型不支持";
            }
        } catch (Exception exception) {
            log.error("httpClient接口调试异常", exception);
            return "httpClient接口调试异常";
        }
    }

}
