package com.ctdi.cnos.llm.controller.metadata;

import com.ctdi.cnos.llm.base.object.Groups;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.beans.meta.projectSpace.Label3cTreeDTO;
import com.ctdi.cnos.llm.beans.meta.projectSpace.Label3cTreeVO;
import com.ctdi.cnos.llm.beans.train.IntentRecognition.Info3cLabelTree;
import com.ctdi.cnos.llm.feign.metadata.Label3cTreeServiceClientFeign;
import com.ctdi.cnos.llm.response.OperateResult;
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
 * 标签树表服务远程数据操作访问接口。
 *
 * @author 
 * @since 2025/06/10
 */
@Api(tags = "标签树表接口", value = "Label3cTreeController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/label3cTree")
public class Label3cTreeController {

	private final Label3cTreeServiceClientFeign serviceClient;

    /**
     * 查询符合条件的标签树表列表。
     *
     * @param typeId 关联的字典类型ID。
     * @return 列表数据。
     */
	@ApiOperation(value = "列表查询符合条件的标签树表数据", notes = "列表查询符合条件的标签树表数据")
    @GetMapping(value = "/queryTreeList/{type}")
    public OperateResult<List<Info3cLabelTree>> queryTreeList(@PathVariable("type") String type) {
		return this.serviceClient.queryTreeList(type);
    }	



}