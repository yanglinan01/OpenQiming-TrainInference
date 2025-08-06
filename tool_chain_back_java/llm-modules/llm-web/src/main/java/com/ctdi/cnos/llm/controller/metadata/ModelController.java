package com.ctdi.cnos.llm.controller.metadata;

import com.ctdi.cnos.llm.base.constant.SystemConstant;
import com.ctdi.cnos.llm.beans.meta.model.ModelTrainParamVO;
import com.ctdi.cnos.llm.beans.meta.model.ModelVO;
import com.ctdi.cnos.llm.feign.metadata.ModelServiceClientFeign;
import com.ctdi.cnos.llm.feign.metadata.ModelTrainParamServiceClientFeign;
import com.ctdi.cnos.llm.response.OperateResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 模型接口
 *
 * @author huangjinhua
 * @since 2024/7/1
 */
@Api(tags = {"模型接口"})
@RestController
@RequestMapping(value = "/model")
@Slf4j
@RequiredArgsConstructor
public class ModelController {

    private final ModelServiceClientFeign modelClient;
    private final ModelTrainParamServiceClientFeign modelTrainParamClient;

    @ApiOperation(value = "分页查询模型列表")
    @PostMapping("/queryPage")
    public OperateResult<Map<String, Object>> queryPage(@RequestBody ModelVO modelVO) {
        try {
            Map<String, Object> result = modelClient.queryPage(modelVO);
            return new OperateResult<>(true, null, result);
        } catch (Exception exception) {
            log.error("分页查询应用列表异常", exception);
            return new OperateResult<>(false, exception.getMessage(), null);
        }
    }

    @ApiOperation(value = "查询模型列表")
    @PostMapping("/queryList")
    public OperateResult<List<ModelVO>> queryList(@RequestBody ModelVO modelVO) {
        try {
            List<ModelVO> result = modelClient.queryList(modelVO);
            if (result != null && !result.isEmpty()) {
                result.get(0).setIsActivate(SystemConstant.YES);
            }
            return new OperateResult<>(true, null, result);
        } catch (Exception exception) {
            log.error("分页查询应用列表异常", exception);
            return new OperateResult<>(false, exception.getMessage(), null);
        }
    }

    @ApiOperation(value = "查询模型训练超参配置列表")
    @GetMapping(value = "/queryTrainParamList")
    public OperateResult<List<ModelTrainParamVO>> queryTrainParamList(ModelTrainParamVO param) {
        try {
            List<ModelTrainParamVO> paramList = modelTrainParamClient.queryList(param);
            return new OperateResult<>(true, null, paramList);
        } catch (Exception ex) {
            log.error("超参查询列表异常", ex);
            return new OperateResult<>(false, ex.getMessage(), null);
        }
    }


}