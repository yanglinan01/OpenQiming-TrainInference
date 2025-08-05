package com.ctdi.cnos.llm.metadata.controller;

import com.ctdi.cnos.llm.annotation.AuthIgnore;
import com.ctdi.cnos.llm.base.constant.ApplicationConstant;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.base.object.Groups;
import com.ctdi.cnos.llm.beans.meta.projectSpace.Label3cTree;
import com.ctdi.cnos.llm.beans.meta.projectSpace.Label3cTreeDTO;
import com.ctdi.cnos.llm.beans.meta.projectSpace.Label3cTreeVO;
import com.ctdi.cnos.llm.beans.train.IntentRecognition.Info3cLabelTree;
import com.ctdi.cnos.llm.metadata.service.Label3cTreeService;
import com.ctdi.cnos.llm.response.ErrorCodeEnum;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.util.ModelUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 标签树表 控制器类。
 *
 * @author 
 * @since 2025/06/10
 */
@Api(tags = "标签树表接口", value = "Label3cTreeController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/label3cTree")
public class Label3cTreeController {
	
    private final Label3cTreeService service;

	
    /**
     * 查询符合条件的标签树表列表。
     *
     * @param typeId 关联的字典类型ID。
     * @return 列表数据。
     */
	@ApiOperation(value = "列表查询符合条件的标签树表数据", notes = "列表查询符合条件的标签树表数据")
    @GetMapping(value = "/queryTreeList/{type}")
    public OperateResult<List<Info3cLabelTree>> queryTreeList(@PathVariable("type") String type) {
        return OperateResult.success(service.queryTreeList(type));
    }


    @AuthIgnore
    @PostMapping("/addBatch")
    @ApiOperation(value = "批量新增3c标签")
    void addBatch(@RequestBody List<Label3cTreeDTO> Label3cTreeDTOList) {
        service.addBatch(Label3cTreeDTOList);
    }


    @AuthIgnore
    @PostMapping("/deleteAll")
    @ApiOperation(value = "删除所有标签")
    void deleteAll() {
        service.deleteAll();
    }


}
