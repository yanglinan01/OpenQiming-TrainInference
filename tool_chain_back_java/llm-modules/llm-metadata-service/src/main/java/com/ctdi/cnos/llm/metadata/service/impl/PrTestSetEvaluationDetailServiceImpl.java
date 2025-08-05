package com.ctdi.cnos.llm.metadata.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.base.constant.ActionType;
import com.ctdi.cnos.llm.base.object.LambdaQueryWrapperX;
import com.ctdi.cnos.llm.base.object.PageResult;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.base.service.BaseService;
import com.ctdi.cnos.llm.beans.meta.prompt.PromptResponseDetail;
import com.ctdi.cnos.llm.beans.meta.evaluation.PrTestSetEvaluation;
import com.ctdi.cnos.llm.beans.meta.evaluation.PrTestSetEvaluationDetail;
import com.ctdi.cnos.llm.beans.meta.evaluation.PrTestSetEvaluationDetailVO;
import com.ctdi.cnos.llm.beans.meta.evaluation.PrTestSetEvaluationScore;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.metadata.dao.PrTestSetEvaluationDetailDao;
import com.ctdi.cnos.llm.metadata.service.PrTestSetEvaluationDetailService;
import com.ctdi.cnos.llm.metadata.service.PrTestSetEvaluationScoreService;
import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.toolkit.MPJWrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.stream.IntStream;

/**
 * 问答对测试数据集评估详情 数据操作服务类
 *
 * @author laiqi
 * @since 2024/09/03
 */
@RequiredArgsConstructor
@Service("prTestSetEvaluationDetailService")
public class PrTestSetEvaluationDetailServiceImpl extends BaseService<PrTestSetEvaluationDetailDao, PrTestSetEvaluationDetail, PrTestSetEvaluationDetailVO> implements PrTestSetEvaluationDetailService {

    private final PrTestSetEvaluationScoreService prTestSetEvaluationScoreService;

    @Override
    public void configureQueryWrapper(LambdaQueryWrapperX<PrTestSetEvaluationDetail> wrapper, QueryParam queryParam) {
        PrTestSetEvaluationDetail filter = queryParam.getFilterDto(PrTestSetEvaluationDetail.class);
        // 测试数据集评估ID 查询
        wrapper.eqIfPresent(PrTestSetEvaluationDetail::getTestSetEvaluationId, filter.getTestSetEvaluationId());
    }

    @Override
    public MPJBaseJoin<PrTestSetEvaluationDetail> configureJoinWrapper(QueryParam queryParam) {
        PrTestSetEvaluationDetail filter = queryParam.getFilterDto(PrTestSetEvaluationDetail.class);
        return MPJWrappers.lambdaJoin(this.entityClass)
                .selectAs(PrTestSetEvaluationDetail::getId, PrTestSetEvaluationDetailVO::getId)
                .selectAs(PrTestSetEvaluationDetail::getReasoningResponse, PrTestSetEvaluationDetailVO::getBigModelAnswer)
                .selectAs(PrTestSetEvaluationDetail::getReasoningTwoResponse, PrTestSetEvaluationDetailVO::getBigModelAnswer2)
                .selectAs(PromptResponseDetail::getPrompt, PrTestSetEvaluationDetailVO::getProblem)
                .selectAs(PromptResponseDetail::getResponse, PrTestSetEvaluationDetailVO::getStandardAnswer)
                .selectAs(PrTestSetEvaluationScore::getAction, PrTestSetEvaluationDetailVO::getUserFeedback)
                .selectAs(PrTestSetEvaluation::getType, PrTestSetEvaluationDetailVO::getType)
                .leftJoin(PromptResponseDetail.class, PromptResponseDetail::getId, PrTestSetEvaluationDetail::getPromptResponseDetailId)
                .leftJoin(PrTestSetEvaluationScore.class, PrTestSetEvaluationScore::getEvaluationDetailId, PrTestSetEvaluationDetail::getId)
                .leftJoin(PrTestSetEvaluation.class, PrTestSetEvaluation::getId, PrTestSetEvaluationDetail::getTestSetEvaluationId)
                // 测试数据集评估ID 查询
                .eqIfExists(PrTestSetEvaluationDetail::getTestSetEvaluationId, filter.getTestSetEvaluationId())
                .eqIfExists(PrTestSetEvaluationDetail::getId, filter.getId())
                .orderByAsc(PrTestSetEvaluationDetail::getId);
    }

    @Override
    public PageResult<PrTestSetEvaluationDetailVO> queryPage(QueryParam queryParam) {
        Page<PrTestSetEvaluationDetailVO> page = selectJoinListPage(queryParam.convertMpPage(), PrTestSetEvaluationDetailVO.class, configureJoinWrapper(queryParam));
        List<PrTestSetEvaluationDetailVO> results = page.getRecords();
        int start = PageUtil.getStart((int) page.getCurrent() - 1, (int) page.getSize());
        // 生成一个从0到rows.size()-1的整数流
        IntStream.range(0, results.size())
                .forEach(index -> {
                    PrTestSetEvaluationDetailVO vo = results.get(index);
                    // 设置序号
                    vo.setIndex(start + index + 1); // 序号从1开始
                    // 设置用户反馈
                    vo.setUserFeedbackName(PrTestSetEvaluationDetailVO.convertStatus(vo));
                });
        return PageResult.makeResponseData(results, page.getTotal());
    }

    @Override
    public List<PrTestSetEvaluationDetailVO> queryList(QueryParam queryParam) {
        List<PrTestSetEvaluationDetailVO> results = selectJoinList(PrTestSetEvaluationDetailVO.class, configureJoinWrapper(queryParam));
        // 生成一个从0到rows.size()-1的整数流
        IntStream.range(0, results.size())
                .forEach(index -> {
                    PrTestSetEvaluationDetailVO vo = results.get(index);
                    // 设置序号
                    vo.setIndex(index + 1); // 序号从1开始
                    // 设置用户反馈
                    vo.setUserFeedbackName(PrTestSetEvaluationDetailVO.convertStatus(vo));
                });
        return results;
    }

    @Override
    public PrTestSetEvaluationDetailVO queryById(Serializable id, Boolean withDict) {
        QueryParam queryParam = new QueryParam();
        PrTestSetEvaluationDetail evaluationDetail = new PrTestSetEvaluationDetail();
        evaluationDetail.setId((Long) id);
        queryParam.setFilterDto(evaluationDetail);
        PrTestSetEvaluationDetailVO vo = selectJoinOne(PrTestSetEvaluationDetailVO.class, configureJoinWrapper(queryParam));
        // 设置用户反馈
        vo.setUserFeedbackName(PrTestSetEvaluationDetailVO.convertStatus(vo));
        return vo;
    }

    @Override
    public boolean like(Long id) {
        return action(id, ActionType.LIKE);
    }

    @Override
    public boolean dislike(Long id) {
        return action(id, ActionType.DISLIKE);
    }

    /**
     * @param id
     * @param actionType
     * @return
     */
    @Override
    public boolean action(Long id, ActionType actionType) {
        QueryParam queryParam = new QueryParam();
        queryParam.setFilterMap(MapUtil.of(PrTestSetEvaluationScore.Fields.evaluationDetailId, id));
        List<PrTestSetEvaluationScore> results = prTestSetEvaluationScoreService.queryList(queryParam);
        Assert.isFalse(CollUtil.isNotEmpty(results) && results.size() > 1, "存在多条" + actionType.getDesc() + "记录~");
        PrTestSetEvaluationScore result = ObjUtil.defaultIfNull(CollUtil.getFirst(results), new PrTestSetEvaluationScore());
        Assert.isFalse(ObjUtil.isNotNull(result.getId()) && actionType.getValue() == result.getAction(), "已有其他人比您抢先一步" + actionType.getDesc() + "了~");
        result.setEvaluationDetailId(id);
        result.setAction(actionType.getValue());
        result.setUserId(UserContextHolder.getUserId());
        result.setActionDate(DateTime.now());
        return prTestSetEvaluationScoreService.saveOrUpdate(result);
    }

    @Override
    public boolean deleteByEvaluationId(Serializable testSetEvaluationId) {
        // 删除点赞记录
        prTestSetEvaluationScoreService.deleteByEvaluationId(testSetEvaluationId);
        // 删除详情记录
        remove(Wrappers.lambdaQuery(PrTestSetEvaluationDetail.class).eq(PrTestSetEvaluationDetail::getTestSetEvaluationId, testSetEvaluationId));
        return false;
    }

    @Override
    public Boolean isComplete(Long testSetEvaluationId) {
        return baseMapper.isComplete(testSetEvaluationId);
    }
}