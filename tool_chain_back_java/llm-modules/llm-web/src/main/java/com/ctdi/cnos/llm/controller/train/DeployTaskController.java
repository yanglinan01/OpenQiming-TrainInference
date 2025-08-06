package com.ctdi.cnos.llm.controller.train;

import com.alibaba.fastjson.JSONObject;
import com.ctdi.cnos.llm.annotation.AuthIgnore;
import com.ctdi.cnos.llm.base.constant.TrainConstants;
import com.ctdi.cnos.llm.beans.train.IntentRecognition.IntentRecognizeReq;
import com.ctdi.cnos.llm.beans.train.IntentRecognition.IntentRecognizeResp;
import com.ctdi.cnos.llm.beans.train.IntentRecognition.IntentRecognizeVO;
import com.ctdi.cnos.llm.beans.train.deployTask.DeployTaskVO;
import com.ctdi.cnos.llm.constant.Constants;
import com.ctdi.cnos.llm.feign.train.DeployTaskServiceClientFeign;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.util.MessageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 部署任务(DeployTask)表控制层
 *
 * @author wangyb
 * @since 2024-07-03 14:16:01
 */
@Api(tags = {"DeployTaskController接口"})
@RestController
@RequestMapping(value = "/deployTask")
@RequiredArgsConstructor
@Slf4j
public class DeployTaskController {

    private final DeployTaskServiceClientFeign deployTaskServiceClientFeign;


    @PostMapping("/queryList")
    @ApiOperation(value = "查询部署模型列表", notes = "查询部署模型列表")
    public OperateResult<List<DeployTaskVO>> queryList(@RequestBody DeployTaskVO vo) {
        try {
            return new OperateResult<>(true, null, deployTaskServiceClientFeign.queryList(vo));
        } catch (Exception e) {
            log.error("查询部署模型列表异常", e);
            return new OperateResult<>(false, MessageUtils.getMessage(e.getMessage()), null);
        }
    }

    @PostMapping("/queryListByCategory")
    @ApiOperation(value = "查询意图识别部署模型列表", notes = "查询意图识别部署模型列表")
    public OperateResult<List<DeployTaskVO>> queryListByCategory(@RequestBody DeployTaskVO vo) {
        try {
            return new OperateResult<>(true, null, deployTaskServiceClientFeign.queryListByCategory(vo));
        } catch (Exception e) {
            log.error("查询部署模型列表异常", e);
            return new OperateResult<>(false, MessageUtils.getMessage(e.getMessage()), null);
        }
    }

    @GetMapping("/detail")
    @ApiOperation(value = "部署模型详情", notes = "部署模型详情")
    public OperateResult<DeployTaskVO> detail(@RequestParam("deployTaskId") String deployTaskId) {
        try {
            return new OperateResult<>(true, null, deployTaskServiceClientFeign.detail(deployTaskId));
        } catch (Exception e) {
            log.error("部署模型详情异常", e);
            return new OperateResult<>(false, MessageUtils.getMessage(e.getMessage()), null);
        }
    }

    @GetMapping("/queryById")
    @ApiOperation(value = "部署模型简单详情", notes = "部署模型简单详情")
    public OperateResult<DeployTaskVO> queryById(@RequestParam("deployTaskId") String deployTaskId) {
        try {
            return new OperateResult<>(true, null, deployTaskServiceClientFeign.detail(deployTaskId));
        } catch (Exception e) {
            log.error("部署模型简单详情异常", e);
            return new OperateResult<>(false, MessageUtils.getMessage(e.getMessage()), null);
        }
    }


    @PostMapping("/add")
    @ApiOperation(value = "新增部署模型", notes = "新增部署模型")
    public OperateResult<Long> add(@RequestBody DeployTaskVO deployTask) {
        try {
            deployTask.setDeployType(Constants.DEPLOY_TYPE_TRAIN);
            deployTask.setDeployBelong(TrainConstants.DEPLOY_BELONG_TOOL);
            return new OperateResult<>(true, null, deployTaskServiceClientFeign.add(deployTask));
        } catch (Exception e) {
            log.error("新增部署模型异常", e);
            return new OperateResult<>(false, MessageUtils.getMessage(e.getMessage()), null);
        }
    }


    @PostMapping("/updateById")
    @ApiOperation(value = "供远程更新部署任务状态", notes = "供远程更新部署任务状态")
    @AuthIgnore
    public OperateResult<String> updateById(@RequestBody DeployTaskVO deployTask) {
        try {
            return deployTaskServiceClientFeign.updateById(deployTask);
        } catch (Exception e) {
            log.error("新增部署模型异常", e);
            return new OperateResult<>(false, MessageUtils.getMessage(e.getMessage()), null);
        }
    }

    @GetMapping("/deleteById")
    @ApiOperation(value = "删除部署模型", notes = "删除部署模型")
    public OperateResult<String> deleteById(@RequestParam("deployTaskId") String deployTaskId) {
        try {
            return deployTaskServiceClientFeign.deleteById(deployTaskId);
        } catch (Exception e) {
            log.error("删除部署模型异常", e);
            return new OperateResult<>(false, MessageUtils.getMessage(e.getMessage()), null);
        }
    }

    /**
     * 意图识别Lora模型通用接口
     * @param req
     * @return
     */
    @PostMapping("/intentRecognize")
    public OperateResult<String> intentRecognize(@RequestBody IntentRecognizeVO req){
        try {
            IntentRecognizeResp resp=deployTaskServiceClientFeign.intentRecognize(req);
            if(resp.getCode() == 0){
                return new OperateResult<>(true, "成功", JSONObject.toJSONString(resp));
            }else{
                return new OperateResult<>(false, resp.getMessage(), JSONObject.toJSONString(resp));
            }
        } catch (Exception e) {
            log.error("意图识别Lora模型通用接口异常", e);
            return new OperateResult<>(false, MessageUtils.getMessage(e.getMessage()), null);
        }
    }
}

