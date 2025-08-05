package com.ctdi.cnos.llm.dcossApi.controller;

import com.ctdi.cnos.llm.dcossApi.service.DcossToolService;
import com.ctdi.cnos.llm.response.OperateResult;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author hanfulei
 * @Date 2024/4/18 15:42
 * @Version 1.0
 **/
@Slf4j
@RestController
@SuppressWarnings("all")
@RequestMapping(value = "/dcossTool")
@Api(tags = {"dcoss调用工具链httpClient接口的统一实现"})
public class DcossToolController {

    @Autowired
    private DcossToolService dcossToolService;

    @PostMapping(value = "http/{key}")
    public OperateResult<String> dcossInvokeTool(@PathVariable("key") @NotBlank String key,
                                                 @RequestBody Map<String, Object> params) {
//        xml调用http
        try {
//            if (params.isEmpty()) {
//                return new OperateResult(false, "入参不可为空", null);
//            }

            String result = dcossToolService.invoke(key, params);
            return new OperateResult(true, "调用成功", result);
        } catch (Exception e) {
            log.error("调用异常", e);
            return new OperateResult(false, e.getMessage(), null);
        }
    }


}


