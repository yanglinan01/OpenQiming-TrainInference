package com.ctdi.cnos.llm.controller.metadata;

import cn.hutool.core.collection.CollUtil;
import com.ctdi.cnos.llm.base.constant.ApplicationConstant;
import com.ctdi.cnos.llm.base.object.Groups;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.response.ErrorCodeEnum;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.system.province.entity.Province;
import com.ctdi.cnos.llm.system.province.entity.ProvinceDTO;
import com.ctdi.cnos.llm.system.province.entity.ProvinceVO;
import com.ctdi.cnos.llm.system.province.serivce.ProvinceService;
import com.ctdi.cnos.llm.util.ModelUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 省份表 控制器类。
 *
 * @author huangjinhua
 * @since 2024/10/16
 */
@Api(tags = "省份表接口", value = "ProvinceController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/province")
public class ProvinceController {

    private final ProvinceService service;

    /**
     * 分页查询符合条件的省份表列表。
     *
     * @param queryParam 查询对象。
     * @return 分页列表数据。
     */
    @ApiOperation(value = "分页查询符合条件的省份表数据", notes = "分页查询符合条件的省份表数据")
    @PostMapping(value = "/queryPage")
    public OperateResult<PageResult<ProvinceVO>> queryPage(@Validated(Groups.PAGE.class) @RequestBody QueryParam queryParam) {
        return OperateResult.success(service.queryPage(queryParam));
    }

    /**
     * 查询符合条件的省份表列表。
     *
     * @param queryParam 查询对象。
     * @return 列表数据。
     */
    @ApiOperation(value = "列表查询符合条件的省份表数据", notes = "列表查询符合条件的省份表数据")
    @PostMapping(value = "/queryList")
    public OperateResult<List<ProvinceVO>> queryList(@RequestBody QueryParam queryParam) {
        return OperateResult.success(service.queryList(queryParam));
    }

    /**
     * 查询指定省份表数据。
     *
     * @param id 指定省份表主键Id。
     * @return 单条数据。
     */
    @ApiOperation(value = "查询指定ID的省份表数据", notes = "通过省份表ID获取具体的省份表数据")
    @GetMapping(value = "/queryById")
    public OperateResult<ProvinceVO> queryById(@ApiParam(value = "省份表ID", required = true, example = "1")
                                               @NotNull(message = "省份表ID不能为空") @RequestParam("id") Long id) {
        return OperateResult.success(service.queryById(id, true));
    }

    /**
     * 查询省份形成map对象。code/abbreviation: ProvinceVO
     *
     * @param provinceVO 查询对象。
     * @return map数据。
     */
    @ApiOperation(value = "查询省份形成map对象", notes = "查询省份形成map对象")
    @PostMapping(value = "/queryMapToProvinceVO")
    public OperateResult<Map<String, ProvinceVO>> queryMapToProvinceVO(@RequestBody(required = false) ProvinceVO provinceVO) {
        if (provinceVO != null && CollUtil.isNotEmpty(provinceVO.getCodeList())) {
            return OperateResult.success(service.queryMapToProvinceVO(provinceVO.getCodeList(), null, ProvinceVO::getCode));
        } else if (provinceVO != null && CollUtil.isNotEmpty(provinceVO.getAbbreviationList())) {
            return OperateResult.success(service.queryMapToProvinceVO(null, provinceVO.getAbbreviationList(), ProvinceVO::getAbbreviation));
        }
        //默认返回按code 的map
        return OperateResult.success(service.queryMapToProvinceVO(null, null, ProvinceVO::getCode));
    }

    /**
     * 查询省份形成map对象。code/abbreviation: name
     *
     * @param provinceVO 查询对象。
     * @return map数据
     */
    @ApiOperation(value = "查询省份形成map对象", notes = "查询省份形成map对象")
    @PostMapping(value = "/queryMapToName")
    public OperateResult<Map<String, String>> queryMapToName(@RequestBody(required = false) ProvinceVO provinceVO) {
        if (provinceVO != null && CollUtil.isNotEmpty(provinceVO.getCodeList())) {
            return OperateResult.success(service.queryMapToName(provinceVO.getCodeList(), null, ProvinceVO::getCode));
        } else if (provinceVO != null && CollUtil.isNotEmpty(provinceVO.getAbbreviationList())) {
            return OperateResult.success(service.queryMapToName(null, provinceVO.getAbbreviationList(), ProvinceVO::getAbbreviation));
        }
        //默认返回按code 的map
        return OperateResult.success(service.queryMapToName(null, null, ProvinceVO::getCode));
    }

    /**
     * 添加省份表操作。
     *
     * @param provinceDTO 添加省份表对象。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "创建省份表", notes = "根据请求体中的省份表信息创建")
    @ApiOperationSupport(ignoreParameters = {"id"})
    @PostMapping(value = "/add")
    public OperateResult<String> add(@Validated(Groups.ADD.class) @RequestBody ProvinceDTO provinceDTO) {
        Province province = ModelUtil.copyTo(provinceDTO, Province.class);
        return service.save(province) ? OperateResult.successMessage(ApplicationConstant.SAVE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_SAVE_FAILED);
    }

    /**
     * 更新省份表操作。
     *
     * @param provinceDTO 更新省份表对象。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "更新省份表", notes = "根据ID更新指定的省份表信息")
    @PostMapping(value = "/updateById")
    public OperateResult<String> updateById(@Validated(Groups.UPDATE.class) @RequestBody ProvinceDTO provinceDTO) {
        Province province = ModelUtil.copyTo(provinceDTO, Province.class);
        return service.updateById(province) ? OperateResult.successMessage(ApplicationConstant.UPDATE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_SAVE_FAILED);
    }

    /**
     * 删除指定的省份表。
     *
     * @param id 指定省份表主键Id。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "删除省份表", notes = "根据ID删除指定的省份表")
    @GetMapping(value = "/deleteById")
    public OperateResult<String> deleteById(@ApiParam(value = "省份表ID", required = true, example = "1")
                                            @NotNull(message = "省份表ID不能为空") @RequestParam("id") Long id) {
        return service.deleteById(id) ? OperateResult.successMessage(ApplicationConstant.DELETE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
    }

}
