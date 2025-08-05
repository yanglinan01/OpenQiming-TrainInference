package com.ctdi.cnos.llm.feign.register;

import com.ctdi.cnos.llm.RemoteConstont;
import com.ctdi.cnos.llm.beans.register.DcoosApiTemplate;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author wangyb
 * @description
 * @data 2024/4/20 16:56
 */
@Component
@FeignClient(value = RemoteConstont.REGISTER_SERVICE_NAME, path = "${cnos.server.llm-register-service.contextPath}")
public interface DcoosApiTemplateServiceClientFeign {


    @GetMapping("/dcoosApiTemplate/queryByCode/{code}")
    DcoosApiTemplate queryByCode(@PathVariable("code") String code);


    @GetMapping("/dcoosApiTemplate/queryById/{id}")
    DcoosApiTemplate queryById(@PathVariable("id") BigDecimal id);


    @PostMapping("/dcoosApiTemplate/queryList")
    List<DcoosApiTemplate> queryList(@RequestBody DcoosApiTemplate dcoosApiTemplate);


    @PostMapping("/dcoosApiTemplate/queryPage")
    Map<String, Object> queryPage(@RequestParam(name = "pageSize", required = false, defaultValue = "20") long pageSize,
                                  @RequestParam(name = "currentPage", required = false, defaultValue = "1")long currentPage,
                                  @RequestBody DcoosApiTemplate dcoosApiTemplate);


    @GetMapping("/dcoosApiTemplate/deleteById/{id}")
    Map<String, Object> deleteById(@PathVariable("id") BigDecimal id);


    @PostMapping("/dcoosApiTemplate/insert")
    Map<String, Object> insert(@RequestBody DcoosApiTemplate dcoosApiTemplate);


    @PostMapping("/dcoosApiTemplate/updateById")
    Map<String, Object> updateById(@RequestBody DcoosApiTemplate dcoosApiTemplate);
}
