package com.ctdi.cnos.llm.train.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.base.object.StatType;
import com.ctdi.cnos.llm.beans.meta.operationCenter.BarCharts;
import com.ctdi.cnos.llm.beans.train.trainTask.TrainTask;
import com.ctdi.cnos.llm.beans.train.trainTask.TrainTaskVO;

import java.util.List;
import java.util.Map;

/**
 * 训练任务 service
 *
 * @author huangjinhua
 * @since 2024/5/14
 */

public interface TrainTaskService {
    /**
     * 分页查询训练任务
     *
     * @param page 分页参数
     * @param task 查询对象
     * @return page
     */
    Page<TrainTaskVO> queryList(Page<TrainTaskVO> page, TrainTaskVO task);

    /**
     * 全量查询训练任务
     *
     * @param task 查询对象
     * @return List<TrainTask>
     */
    List<TrainTaskVO> queryList(TrainTaskVO task);


    /**
     * 查询训练任务
     *
     * @param id 训练任务ID
     * @return TrainTask
     */
    TrainTaskVO queryById(Long id,String deployBelong);

    /**
     * 查询训练任务
     *
     * @param id 训练任务ID
     * @return TrainTask
     */
    TrainTask getTrainTaskById(Long id);


    /**
     * 查询各状态的训练任务数量，默认走权限
     *
     * @param isAll 是否查全量，不做数据过滤 0是，1否
     * @return Map<String, Long> status: count
     */
    Map<String, Long> statusCount(String isAll);


    /**
     * 根据数据集ID、任务状态查询用到的训练任务数量（不区分用户）
     *
     * @param dataSetId 数据集ID
     * @param status    任务状态（可空）
     * @return Long
     */
    Long countByDataSetId(Long dataSetId, String status);

    /**
     * 查询训练任务
     *
     * @param name 训练任务名称
     * @return TrainTask
     */
    boolean existByName(String name);

    /**
     * 新增训练任务
     *
     * @param taskVO 训练任务对象
     * @return int
     */
    String insertOrIterate(TrainTaskVO taskVO) throws Exception;

    /**
     * 更新训练任务
     *
     * @param task 训练任务对象
     * @return int
     */
    int updateById(TrainTask task);

    /**
     * 定时任务更新状态的训练任务
     *
     * @return List<TrainTask>
     */
    int modifyStatusList();

    /**
     * 定时任务训练发送
     *
     * @return List<TrainTask>
     */
    int trainTaskSend();

    /**
     * 删除训练任务
     *
     * @param task 训练任务
     * @return int
     */
    int delete(TrainTask task);


    /**
     * 终止训练任务
     *
     * @param task 训练任务
     * @return int
     */
    int stop(TrainTask task);

    /**
     * 校验用户训练任务数量满足情况
     *
     * @return 是否满足
     */
    Boolean checkUserTrainTaskCount(Long userId);


    /**
     * 统计各省模型构建数量
     *
     * @param type 统计类型，DAY：当天；MONTH：当月；ALL 累计
     * @return 各省模型构建数量
     */
    List<BarCharts> countTrainTaskGroupByProvince(StatType type);

    /**
     * 检查迭代版本数量
     * @param taskVO
     * @return
     */
    boolean checkIterateCount(TrainTaskVO taskVO);

    /**
     * 检查项目任务组数量
     * @param taskVO
     * @return
     */
    boolean checkProjectGroupCount(TrainTaskVO taskVO);

    /**
     * 查询意图识别训练任务列表
     * @param page
     * @param task
     * @return
     */
    Page<TrainTaskVO> queryListByCategory(Page<TrainTaskVO> page, TrainTaskVO task);
}