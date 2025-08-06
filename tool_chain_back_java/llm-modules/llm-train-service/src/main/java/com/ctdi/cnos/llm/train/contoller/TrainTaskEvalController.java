package com.ctdi.cnos.llm.train.contoller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.ctdi.cnos.llm.annotation.AuthIgnore;
import com.ctdi.cnos.llm.base.constant.ApplicationConstant;
import com.ctdi.cnos.llm.base.object.Groups;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.beans.train.trainEval.TrainTaskEval;
import com.ctdi.cnos.llm.beans.train.trainEval.TrainTaskEvalCallback;
import com.ctdi.cnos.llm.beans.train.trainEval.TrainTaskEvalDTO;
import com.ctdi.cnos.llm.beans.train.trainEval.TrainTaskEvalVO;
import com.ctdi.cnos.llm.response.ErrorCodeEnum;
import com.ctdi.cnos.llm.response.OperateResult;
import com.ctdi.cnos.llm.train.service.TrainTaskEvalService;
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

/**
 * 训练任务c-eval 评估 操作控制器类。
 *
 * @author huangjinhua
 * @since 2024/09/04
 */
@Api(tags = "训练任务c-eval 评估接口", value = "TrainTaskEvalController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/trainTaskEval")
public class TrainTaskEvalController {

    private final TrainTaskEvalService service;

    /**
     * 分页列出符合过滤条件的训练任务c-eval 评估列表。
     *
     * @param queryParam 查询对象。
     * @return 应答结果对象，包含查询结果集。
     */
    @ApiOperation(value = "获取分页查询的训练任务c-eval 评估列表信息", notes = "获取分页查询的训练任务c-eval 评估信息")
    @PostMapping(value = "/queryPage")
    public PageResult<TrainTaskEvalVO> queryPage(@Validated(Groups.PAGE.class) @RequestBody QueryParam queryParam) {
        return service.queryPage(queryParam);
    }

    /**
     * 列出符合过滤条件的训练任务c-eval 评估列表。
     *
     * @param queryParam 查询对象。
     * @return 应答结果对象，包含查询结果集。
     */
    @ApiOperation(value = "获取查询的训练任务c-eval 评估列表信息", notes = "获取查询的训练任务c-eval 评估信息")
    @PostMapping(value = "/queryList")
    public List<TrainTaskEvalVO> queryList(@RequestBody QueryParam queryParam) {
        return service.queryList(queryParam);
    }

    /**
     * 查看训练任务c-eval 评估对象详情。
     *
     * @param id 指定训练任务c-eval 评估主键Id。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "根据ID获取训练任务c-eval 评估详情", notes = "通过训练任务c-eval 评估ID获取具体的训练任务c-eval 评估信息")
    @GetMapping(value = "/queryById")
    public TrainTaskEvalVO queryById(@ApiParam(value = "训练任务c-eval 评估ID", required = true, example = "1")
                                     @NotNull(message = "训练任务c-eval 评估ID不能为空") @RequestParam("id") Long id) {
        return service.queryById(id, true);
    }

    /**
     * 根据模型ID（训练任务）获取训练任务c-eval 最新评估详情
     *
     * @param trainTaskId 训练任务c-eval 评估模型ID。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "根据模型ID（训练任务）获取训练任务c-eval 最新评估详情", notes = "根据模型ID（训练任务）获取训练任务c-eval 最新评估详情")
    @GetMapping(value = "/getLastEvalByTrainTaskId")
    public TrainTaskEvalVO getLastEvalByTrainTaskId(@ApiParam(value = "训练任务c-eval 评估模型ID", required = true, example = "1")
                                                    @NotNull(message = "训练任务c-eval 评估模型ID不能为空") @RequestParam("trainTaskId") Long trainTaskId) {
        return service.getLastEvalByTrainTaskId(trainTaskId);
    }

    /**
     * 添加训练任务c-eval 评估操作。
     *
     * @param trainTaskEvalDto 添加训练任务c-eval 评估对象。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "创建训练任务c-eval 评估", notes = "根据请求体中的训练任务c-eval 评估信息创建")
    @ApiOperationSupport(ignoreParameters = {"id"})
    @PostMapping(value = "/add")
    public OperateResult<String> add(@Validated(Groups.ADD.class) @RequestBody TrainTaskEvalDTO trainTaskEvalDto) {
        TrainTaskEval trainTaskEval = ModelUtil.copyTo(trainTaskEvalDto, TrainTaskEval.class);
        Assert.notNull(trainTaskEvalDto.getTrainTaskId(), "请选择模型！");
        //一个模型只能评估一次(正在评估+评估完成)
        TrainTaskEvalVO lastEval = service.getLastEvalByTrainTaskId(trainTaskEval.getTrainTaskId());
        Assert.isNull(lastEval, "当前模型已做c-eval 评估，请勿重复做评估！");
        return service.save(trainTaskEval) ? OperateResult.successMessage(ApplicationConstant.SAVE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_SAVE_FAILED);
    }

    /**
     * 更新训练任务c-eval 评估操作。
     *
     * @param trainTaskEvalDto 更新训练任务c-eval 评估对象。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "更新训练任务c-eval 评估", notes = "根据ID更新指定的训练任务c-eval 评估信息")
    @PostMapping(value = "/updateById")
    public OperateResult<String> updateById(@Validated(Groups.UPDATE.class) @RequestBody TrainTaskEvalDTO trainTaskEvalDto) {
        TrainTaskEval trainTaskEval = ModelUtil.copyTo(trainTaskEvalDto, TrainTaskEval.class);
        return service.updateById(trainTaskEval) ? OperateResult.successMessage(ApplicationConstant.UPDATE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_SAVE_FAILED);
    }

    /**
     * 回调更新训练任务c-eval 评估操作。
     *
     * @param evalCallback 更新训练任务c-eval 评估对象。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "回调更新训练任务c-eval 评估值", notes = "根据ID更新指定的训练任务c-eval 评估值信息")
    @PostMapping(value = "/callbackUpdateById")
    @AuthIgnore
    public OperateResult<String> callbackUpdateById(@Validated(Groups.UPDATE.class) @RequestBody TrainTaskEvalCallback evalCallback) {
        TrainTaskEval eval = new TrainTaskEval();
        BeanUtil.copyProperties(evalCallback,eval);
        eval.setId(evalCallback.getTaskId());
        eval.setEvalInfo(evalCallback.getInfo());
        /*eval.setStem(evalCallback.getStem());
        eval.setSocialScience(evalCallback.getSocialScience());
        eval.setHumanity(evalCallback.getHumanity());
        eval.setOther(evalCallback.getOther());
        eval.setStatus(evalCallback.getStatus());
        eval.setAverage(evalCallback.getAverage());*/
        return service.updateById(eval) ? OperateResult.successMessage("回调更新训练任务c-eval 评估值成功") : OperateResult.error("回调更新训练任务c-eval 评估值失败");
    }

    /**
     * 删除指定的训练任务c-eval 评估。
     *
     * @param id 指定训练任务c-eval 评估主键Id。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "删除训练任务c-eval 评估", notes = "根据ID删除指定的训练任务c-eval 评估")
    @GetMapping(value = "/deleteById")
    public OperateResult<String> deleteById(@ApiParam(value = "训练任务c-eval 评估ID", required = true, example = "1")
                                            @NotNull(message = "训练任务c-eval 评估ID不能为空") @RequestParam("id") Long id) {
        return service.deleteById(id) ? OperateResult.successMessage(ApplicationConstant.DELETE_MESSAGE) : OperateResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
    }

}
