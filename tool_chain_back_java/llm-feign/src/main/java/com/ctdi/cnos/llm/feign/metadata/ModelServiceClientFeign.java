package com.ctdi.cnos.llm.feign.metadata;

import com.ctdi.cnos.llm.RemoteConstont;
import com.ctdi.cnos.llm.beans.meta.model.Model;
import com.ctdi.cnos.llm.beans.meta.model.ModelVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 模型  OpenFeign
 *
 * @author huangjinhua
 * @since 2024/7/1
 */
@Component
@FeignClient(value = RemoteConstont.METADATA_SERVICE_NAME, path = "${cnos.server.llm-metadata-service.contextPath}")
public interface ModelServiceClientFeign {


    /**
     * 分页查询模型列表
     *
     * @param modelVO 模型信息
     * @return Map<String, Object>
     */
    @PostMapping("/model/queryPage")
    Map<String, Object> queryPage(@RequestBody ModelVO modelVO);

    /**
     * 查询模型列表
     *
     * @param modelVO 模型信息
     * @return List<ModelVO>
     */
    @PostMapping(value = "/model/queryList")
    List<ModelVO> queryList(@RequestBody ModelVO modelVO);

    /**
     * 根据ID查询模型详情
     *
     * @param id 模型ID
     * @return ModelVO
     */
    @GetMapping("/model/queryById")
    ModelVO queryById(@RequestParam("id") Long id);

    /**
     * 新增列表
     *
     * @param model 模型信息
     * @return Map<String, Object>
     */
    @PostMapping(value = "/model/add")
    Map<String, Object> add(@RequestBody Model model);

    /**
     * 新增列表
     *
     * @param model 模型信息
     * @return Map<String, Object>
     */
    @PostMapping(value = "/model/updateById")
    Map<String, Object> updateById(@RequestBody Model model);
}
