package com.ctdi.cnos.llm.controller.train;

import com.ctdi.cnos.llm.base.object.Groups;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.beans.train.trainEval.TrainTaskEvalDTO;
import com.ctdi.cnos.llm.beans.train.trainEval.TrainTaskEvalVO;
import com.ctdi.cnos.llm.feign.train.TrainTaskEvalServiceClientFeign;
import com.ctdi.cnos.llm.response.OperateResult;
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
 * 训练任务c-eval 评估服务远程数据操作访问接口。
 *
 * @author huangjinhua
 * @since 2024/09/04
 */
@Api(tags = "训练任务c-eval 评估接口", value = "TrainTaskEvalController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/trainTaskEval")
public class TrainTaskEvalController {

    private final TrainTaskEvalServiceClientFeign serviceClient;

    /**
     * 分页列出符合过滤条件的训练任务c-eval 评估列表。
     *
     * @param queryParam 查询对象。
     * @return 应答结果对象，包含查询结果集。
     */
    @ApiOperation(value = "获取分页查询的训练任务c-eval 评估列表信息", notes = "获取分页查询的训练任务c-eval 评估信息")
    @PostMapping(value = "/queryPage")
    public OperateResult<PageResult<TrainTaskEvalVO>> queryPage(@Validated(Groups.PAGE.class) @RequestBody QueryParam queryParam) {
        PageResult<TrainTaskEvalVO> data = this.serviceClient.queryPage(queryParam);
        return OperateResult.success(data);
    }

    /**
     * 列出符合过滤条件的训练任务c-eval 评估列表。
     *
     * @param queryParam 查询对象。
     * @return 应答结果对象，包含查询结果集。
     */
    @ApiOperation(value = "获取查询的训练任务c-eval 评估列表信息", notes = "获取查询的训练任务c-eval 评估信息")
    @PostMapping(value = "/queryList")
    public OperateResult<List<TrainTaskEvalVO>> queryList(@RequestBody QueryParam queryParam) {
        List<TrainTaskEvalVO> data = this.serviceClient.queryList(queryParam);
        return OperateResult.success(data);
    }

    /**
     * 查看训练任务c-eval 评估对象详情。
     *
     * @param id 指定训练任务c-eval 评估主键Id。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "根据ID获取训练任务c-eval 评估详情", notes = "通过训练任务c-eval 评估ID获取具体的训练任务c-eval 评估信息")
    @GetMapping(value = "/queryById")
    public OperateResult<TrainTaskEvalVO> queryById(@ApiParam(value = "训练任务c-eval 评估ID", required = true, example = "1")
                                                    @NotNull(message = "训练任务c-eval 评估ID不能为空") @RequestParam("id") Long id) {
        TrainTaskEvalVO data = this.serviceClient.queryById(id);
        return OperateResult.success(data);
    }

    /**
     * 根据模型ID（训练任务）获取训练任务c-eval 最新评估详情
     *
     * @param trainTaskId 训练任务c-eval 评估模型ID。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "根据模型ID（训练任务）获取训练任务c-eval 最新评估详情", notes = "根据模型ID（训练任务）获取训练任务c-eval 最新评估详情")
    @GetMapping(value = "/getLastEvalByTrainTaskId")
    public OperateResult<TrainTaskEvalVO> getLastEvalByTrainTaskId(@ApiParam(value = "训练任务c-eval 评估模型ID", required = true, example = "1")
                                                                   @NotNull(message = "训练任务c-eval 评估模型ID不能为空") @RequestParam("trainTaskId") Long trainTaskId) {
        TrainTaskEvalVO data = this.serviceClient.getLastEvalByTrainTaskId(trainTaskId);
        return OperateResult.success(data);
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
        return this.serviceClient.add(trainTaskEvalDto);
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
        return this.serviceClient.updateById(trainTaskEvalDto);
    }

    /**
     * 删除指定的训练任务c-eval 评估。
     *
     * @param id 指定训练任务c-eval 评估主键Id。
     * @return 应答结果对象。
     */
    @ApiOperation(value = "删除训练任务c-eval 评估", notes = "根据ID删除指定的训练任务c-eval 评估")
    @GetMapping(value = "/deleteById")
    public OperateResult<String> delete(@ApiParam(value = "训练任务c-eval 评估ID", required = true, example = "1")
                                        @NotNull(message = "训练任务c-eval 评估ID不能为空") @RequestParam("id") Long id) {
        return this.serviceClient.deleteById(id);
    }

}