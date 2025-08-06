package com.ctdi.cnos.llm.dcossApi.service;

import com.ctdi.cnos.llm.response.OperateResult;

import java.util.Map;

/**
 * @Author hanfulei
 * @Date 2024/4/18 15:52
 * @Version 1.0
 **/
public interface DcossToolService {

    String invoke(String guid, Map<String, Object> params);
}
