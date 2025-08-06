package com.ctdi.cnos.llm.feign.train;

import com.ctdi.cnos.llm.RemoteConstont;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.beans.train.trainEval.TrainTaskEvalCallback;
import com.ctdi.cnos.llm.beans.train.trainEval.TrainTaskEvalDTO;
import com.ctdi.cnos.llm.beans.train.trainEval.TrainTaskEvalVO;
import com.ctdi.cnos.llm.response.OperateResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 训练任务c-eval 评估服务远程数据操作访问接口。
 *
 * @author huangjinhua
 * @since 2024/09/04
 */
@Component
@FeignClient(value = RemoteConstont.TRAIN_SERVICE_NAME, path = "${cnos.server.llm-train-service.contextPath}")
public interface TrainTaskEvalServiceClientFeign {

    /**
     * 分页列出符合过滤条件的训练任务c-eval 评估列表。
     *
     * @param queryParam 查询对象。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping(value = "/trainTaskEval/queryPage")
    PageResult<TrainTaskEvalVO> queryPage(@RequestBody QueryParam queryParam);

    /**
     * 列出符合过滤条件的训练任务c-eval 评估列表。
     *
     * @param queryParam 查询对象。
     * @return 应答结果对象，包含查询结果集。
     */
    @PostMapping(value = "/trainTaskEval/queryList")
    List<TrainTaskEvalVO> queryList(@RequestBody QueryParam queryParam);

    /**
     * 查看训练任务c-eval 评估对象详情。
     *
     * @param id 指定训练任务c-eval 评估主键Id。
     * @return 应答结果对象。
     */
    @GetMapping(value = "/trainTaskEval/queryById")
    TrainTaskEvalVO queryById(@RequestParam("id") Long id);


    /**
     * 根据模型ID（训练任务）获取训练任务c-eval 最新评估详情
     *
     * @param trainTaskId 训练任务c-eval 评估模型ID。
     * @return 应答结果对象。
     */
    @GetMapping(value = "/trainTaskEval/getLastEvalByTrainTaskId")
    TrainTaskEvalVO getLastEvalByTrainTaskId(@RequestParam("trainTaskId") Long trainTaskId);

    /**
     * 添加训练任务c-eval 评估操作。
     *
     * @param trainTaskEvalDto 添加训练任务c-eval 评估对象。
     * @return 应答结果对象。
     */
    @PostMapping(value = "/trainTaskEval/add")
    OperateResult<String> add(@RequestBody TrainTaskEvalDTO trainTaskEvalDto);

    /**
     * 更新训练任务c-eval 评估操作。
     *
     * @param trainTaskEvalDto 更新训练任务c-eval 评估对象。
     * @return 应答结果对象。
     */
    @PostMapping(value = "/trainTaskEval/updateById")
    OperateResult<String> updateById(@RequestBody TrainTaskEvalDTO trainTaskEvalDto);

    /**
     * 回调更新训练任务c-eval 评估操作。
     *
     * @param trainTaskEvalCallback 更新训练任务c-eval 评估对象。
     * @return 应答结果对象。
     */
    @PostMapping(value = "/trainTaskEval/callbackUpdateById")
    OperateResult<String> callbackUpdateById(@RequestBody TrainTaskEvalCallback trainTaskEvalCallback);

    /**
     * 删除指定的训练任务c-eval 评估。
     *
     * @param id 指定训练任务c-eval 评估主键Id。
     * @return 应答结果对象。
     */
    @GetMapping(value = "/trainTaskEval/deleteById")
    OperateResult<String> deleteById(@RequestParam("id") Long id);

}