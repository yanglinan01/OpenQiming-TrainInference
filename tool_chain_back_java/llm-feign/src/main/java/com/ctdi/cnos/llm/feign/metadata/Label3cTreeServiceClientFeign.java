package com.ctdi.cnos.llm.feign.metadata;

import com.ctdi.cnos.llm.RemoteConstont;
import com.ctdi.cnos.llm.beans.meta.projectSpace.Label3cTreeDTO;
import com.ctdi.cnos.llm.beans.train.IntentRecognition.Info3cLabelTree;
import com.ctdi.cnos.llm.response.OperateResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 标签树表服务远程数据操作访问接口。
 *
 * @author 
 * @since 2025/06/10
 */
@Component
@FeignClient(value = RemoteConstont.METADATA_SERVICE_NAME, path = "${cnos.server.llm-metadata-service.contextPath}")
public interface Label3cTreeServiceClientFeign {


    /**
     * 查询符合条件的标签树表列表。
     *
     * @param type 关联的字典类型ID。
     * @return 列表数据。
     */
    @GetMapping(value = "/label3cTree/queryTreeList/{type}")
    OperateResult<List<Info3cLabelTree>> queryTreeList(@PathVariable("type") String type);


    @PostMapping("/label3cTree/addBatch")
    @ApiOperation(value = "批量新增3c标签")
    void addBatch(@RequestBody List<Label3cTreeDTO> Label3cTreeDTOList);


    @PostMapping("/label3cTree/deleteAll")
    @ApiOperation(value = "删除所有")
    void deleteAll();

}