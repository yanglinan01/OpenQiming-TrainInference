package com.ctdi.cnos.llm.train.contoller;

import com.ctdi.cnos.llm.base.constant.TrainConstants;
import com.ctdi.cnos.llm.beans.train.trainTask.TaskGroupVO;
import com.ctdi.cnos.llm.train.service.TaskGroupService;
import com.ctdi.cnos.llm.util.CommonResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author xuwj09
 * @version 1.0
 * @date 2025/6/26 11:22
 * @Description 训练任务组接口
 */
@Api(tags = {"训练任务组接口"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/taskGroup")
@Slf4j
public class TaskGroupController {

    private final TaskGroupService taskGroupService;

    /**
     * 编辑组逻辑
     *
     * @param taskGroupVO
     * @return
     */
    @ApiOperation(value = "编辑训练任务组", notes = "编辑训练任务组")
    @PostMapping("/edit")
    public Map<String, Object> edit(@RequestBody TaskGroupVO taskGroupVO) {
        try {
            String result = taskGroupService.edit(taskGroupVO);
            return TrainConstants.HTTP_SUCCESS_SUBMIT.equals(result)?
                    CommonResponseUtil.responseMap(true, "编辑版本成功!"):CommonResponseUtil.responseMap(false, result);
        } catch (Exception e) {
            log.error("编辑版本失败:{}", e.getMessage());
            return CommonResponseUtil.responseMap(false, "编辑版本失败！");
        }
    }

    /**
     * 删除任务组逻辑
     *
     * @param taskGroupVO
     * @return
     */
    @ApiOperation(value = "删除训练任务组", notes = "删除训练任务组")
    @PostMapping("/delete")
    public Map<String, Object> delete(@RequestBody TaskGroupVO taskGroupVO) {
        try {
            String result = taskGroupService.delete(taskGroupVO);
            return TrainConstants.HTTP_SUCCESS_SUBMIT.equals(result)?
                    CommonResponseUtil.responseMap(true, "删除版本成功！"):CommonResponseUtil.responseMap(false, result);
        } catch (Exception e) {
            log.error("删除版本失败:{}", e.getMessage());
            return CommonResponseUtil.responseMap(false, "删除版本失败！");
        }
    }

    @ApiOperation(value = "部署训练任务组", notes = "部署训练任务组")
    @PostMapping("/deploy")
    public Map<String, Object> deploy(@RequestBody TaskGroupVO taskGroupVO) {
        try {
            String result = taskGroupService.deploy(taskGroupVO);
            return TrainConstants.HTTP_SUCCESS_SUBMIT.equals(result)?
                    CommonResponseUtil.responseMap(true, "部署成功！"):CommonResponseUtil.responseMap(false, result);
        } catch (Exception e) {
            log.error("部署版本失败:{}", e.getMessage());
            return CommonResponseUtil.responseMap(false, "部署版本失败！");
        }
    }

    @ApiOperation(value = "取消部署训练任务组", notes = "部署训练任务组")
    @PostMapping("/unDeploy")
    public Map<String, Object> unDeploy(@RequestBody TaskGroupVO taskGroupVO) {
        try {
            String result = taskGroupService.unDeploy(taskGroupVO);
            return TrainConstants.HTTP_SUCCESS_SUBMIT.equals(result)?
                    CommonResponseUtil.responseMap(true, "取消部署成功！"):CommonResponseUtil.responseMap(false, result);
        } catch (Exception e) {
            log.error("取消部署版本失败:{}", e.getMessage());
            return CommonResponseUtil.responseMap(false, "取消部署版本失败！");
        }
    }
}
