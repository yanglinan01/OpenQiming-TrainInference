package com.ctdi.cnos.llm.feign.metadata;

import com.ctdi.cnos.llm.RemoteConstont;
import com.ctdi.cnos.llm.beans.meta.model.ModelTrainParamVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


/**
 * 模型超参  OpenFeign
 *
 * @author huangjinhua
 * @since 2024/7/1
 */
@Component
@FeignClient(value = RemoteConstont.METADATA_SERVICE_NAME, path = "${cnos.server.llm-metadata-service.contextPath}")
public interface ModelTrainParamServiceClientFeign {
    /**
     * 查询模型超参列表
     *
     * @param param 超参信息
     * @return List<ModelTrainParam>
     */
    @GetMapping(value = "/modelTrainParam/queryList")
    List<ModelTrainParamVO> queryList(@SpringQueryMap ModelTrainParamVO param);

}
