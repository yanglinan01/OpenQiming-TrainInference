package com.ctdi.cnos.llm.feign.metadata;

import com.ctdi.cnos.llm.RemoteConstont;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.beans.meta.projectSpace.Type3cDTO;
import com.ctdi.cnos.llm.beans.meta.projectSpace.Type3cVO;
import com.ctdi.cnos.llm.response.OperateResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 字典类型表服务远程数据操作访问接口。
 *
 * @author 
 * @since 2025/06/10
 */
@Component
@FeignClient(value = RemoteConstont.METADATA_SERVICE_NAME, path = "${cnos.server.llm-metadata-service.contextPath}")
public interface Type3cServiceClientFeign {



    /**
     * 查询符合条件的字典类型表列表。
     *
     * @param queryParam 查询对象。
     * @return 列表数据。
     */
    @PostMapping(value = "/type3c/queryList")
    OperateResult<List<Type3cVO>> queryList(@RequestBody QueryParam queryParam);

    /**
     * 查询指定字典类型表数据。
     *
     * @param id 指定字典类型表主键Id。
     * @return 单条数据。
     */
	@GetMapping(value = "/type3c/queryById")
    OperateResult<Type3cVO> queryById(@RequestParam("id") Long id);

    @PostMapping("/type3c/addBatch")
    @ApiOperation(value = "批量新增3c类型")
    void addBatch(@RequestBody List<Type3cDTO> Type3cDTOList);


    @PostMapping("/type3c/deleteAll")
    @ApiOperation(value = "删除所有类型")
    void deleteAll();

}