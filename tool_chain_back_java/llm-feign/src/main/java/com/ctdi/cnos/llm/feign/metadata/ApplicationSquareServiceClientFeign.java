package com.ctdi.cnos.llm.feign.metadata;


import com.ctdi.cnos.llm.RemoteConstont;
import com.ctdi.cnos.llm.beans.meta.application.ApplicationSquareVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 应用广场 OpenFeign
 *
 * @author huangjinhua
 * @since 2024/6/11
 */
@Component
@FeignClient(value = RemoteConstont.METADATA_SERVICE_NAME, path = "${cnos.server.llm-metadata-service.contextPath}")
public interface ApplicationSquareServiceClientFeign {

    /**
     * 分页查询应用列表
     *
     * @param pageSize    页大小，默认为20
     * @param currentPage 当前页，默认为1
     * @param appVO       应用信息
     * @return Map<String, Object>
     */
    @GetMapping("/applicationSquare/queryPage")
    Map<String, Object> queryPage(@RequestParam(name = "pageSize", required = false, defaultValue = "20") long pageSize,
                                  @RequestParam(name = "currentPage", required = false, defaultValue = "1") long currentPage,
                                  @SpringQueryMap ApplicationSquareVO appVO);

    /**
     * 查询应用列表
     *
     * @param appVO 应用信息
     * @return List<ApplicationSquareVO>
     */
    @GetMapping("/applicationSquare/queryList")
    List<ApplicationSquareVO> queryList(@SpringQueryMap ApplicationSquareVO appVO);

    /**
     * 查询应用详情
     *
     * @param id 应用ID
     * @return ApplicationSquareVO
     */
    @GetMapping("/applicationSquare/queryById")
    ApplicationSquareVO queryById(@RequestParam("id") Long id);

}
