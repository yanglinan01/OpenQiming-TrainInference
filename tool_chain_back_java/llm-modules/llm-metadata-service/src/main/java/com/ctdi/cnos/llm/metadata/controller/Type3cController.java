package com.ctdi.cnos.llm.metadata.controller;

import com.ctdi.cnos.llm.annotation.AuthIgnore;
import com.ctdi.cnos.llm.base.constant.ApplicationConstant;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.base.object.Groups;
import com.ctdi.cnos.llm.beans.meta.projectSpace.Type3c;
import com.ctdi.cnos.llm.beans.meta.projectSpace.Type3cDTO;
import com.ctdi.cnos.llm.beans.meta.projectSpace.Type3cVO;
import com.ctdi.cnos.llm.metadata.service.Type3cService;
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
 * 字典类型表 控制器类。
 *
 * @author 
 * @since 2025/06/10
 */
@Api(tags = "字典类型表接口", value = "Type3cController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/type3c")
public class Type3cController {
	
    private final Type3cService service;

    /**
     * 查询符合条件的字典类型表列表。
     *
     * @param queryParam 查询对象。
     * @return 列表数据。
     */
	@ApiOperation(value = "列表查询符合条件的字典类型表数据", notes = "列表查询符合条件的字典类型表数据")
    @PostMapping(value = "/queryList")
    public OperateResult<List<Type3cVO>> queryList(@RequestBody QueryParam queryParam) {
        return OperateResult.success(service.queryList(queryParam));
    }

    /**
     * 查询指定字典类型表数据。
     *
     * @param id 指定字典类型表主键Id。
     * @return 单条数据。
     */
	@ApiOperation(value = "查询指定ID的字典类型表数据", notes = "通过字典类型表ID获取具体的字典类型表数据")
	@GetMapping(value = "/queryById")
    public OperateResult<Type3cVO> queryById(@ApiParam(value = "字典类型表ID", required = true, example = "1")
                          @NotNull(message = "字典类型表ID不能为空") @RequestParam("id") Long id) {
        return OperateResult.success(service.queryById(id, true));
    }

    @AuthIgnore
    @PostMapping("/addBatch")
    @ApiOperation(value = "批量新增3c类型")
    void addBatch(@RequestBody List<Type3cDTO> Type3cDTOList) {
        service.addBatch(Type3cDTOList);
    }


    @AuthIgnore
    @PostMapping("/deleteAll")
    @ApiOperation(value = "删除所有类型")
    void deleteAll() {
        service.deleteAll();
    }

}
