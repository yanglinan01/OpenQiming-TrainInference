package com.ctdi.cnos.llm.metadata.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ctdi.cnos.llm.base.object.LambdaQueryWrapperX;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.base.service.BaseService;
import com.ctdi.cnos.llm.beans.meta.cluster.Cluster;
import com.ctdi.cnos.llm.beans.meta.cluster.ClusterVO;
import com.ctdi.cnos.llm.beans.train.trainTaskDemo.AiCluster;
import com.ctdi.cnos.llm.beans.train.trainTaskDemo.Strategy;
import com.ctdi.cnos.llm.cache.ctg.CtgCache;
import com.ctdi.cnos.llm.context.UserContextHolder;
import com.ctdi.cnos.llm.metadata.client.ApiClient;
import com.ctdi.cnos.llm.metadata.dao.ClusterDao;
import com.ctdi.cnos.llm.metadata.service.ClusterService;
import com.ctdi.cnos.llm.base.constant.CacheConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 集群资源 数据操作服务类
 *
 * @author huangjinhua
 * @since 2024/09/24
 */
@RequiredArgsConstructor
@Service("clusterService")
@Slf4j
public class ClusterServiceImpl extends BaseService<ClusterDao, Cluster, ClusterVO> implements ClusterService {

    private final CtgCache ctgCache;

    private final ApiClient apiClient;

    @Override
    public void configureQueryWrapper(LambdaQueryWrapperX<Cluster> wrapper, QueryParam queryParam) {
        Cluster filter = queryParam.getFilterDto(Cluster.class);
    }

    @Override
    public ClusterVO queryByCode(String code) {
        if (CharSequenceUtil.isNotBlank(code)) {
            //获取缓存
            ClusterVO clusterVO = ctgCache.getCache(CacheConstant.CLUSTER_CACHE_NAME, code);
            if (clusterVO != null) {
                // log.info("获取缓存：{}", JSON.toJSONString(clusterVO));
                return clusterVO;
            }
            //查询数据
            LambdaQueryWrapper<Cluster> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Cluster::getCode, code);

            //code唯一索引
            Cluster cluster = super.baseMapper.selectOne(Cluster::getCode, code);
            buildRelationForData(cluster, null, false);
            clusterVO = convertToVo(cluster, null);

            //设置缓存
            if (clusterVO != null) {
                ctgCache.set(CacheConstant.CLUSTER_CACHE_NAME, code, clusterVO, CacheConstant.CACHE_EXPIRE_1_DAY);
            }
            return clusterVO;

        }
        return null;
    }

    @Override
    public List<Strategy> queryStrategyList(Strategy strategy) {
        List<Strategy> list = null;
        String strategyListString = apiClient.getStrategyList(UserContextHolder.getUserId());
        if (CharSequenceUtil.isNotBlank(strategyListString)) {
            // 直接从JSON中提取resources数组并转换为List<Strategy>
            list = JSON.parseObject(strategyListString)
                    .getJSONArray("resources")
                    .toJavaList(Strategy.class);
        }
        return list;
    }

    @Override
    public boolean updateById(Cluster entity) {
        //移除缓存
        ClusterVO clusterVO = super.queryById(entity.getId(), false);
        if (clusterVO != null) {
            ctgCache.remove(CacheConstant.CLUSTER_CACHE_NAME, clusterVO.getCode());
        }
        return super.updateById(entity);
    }

    @Override
    public boolean deleteById(Serializable id) {
        //移除缓存
        ClusterVO clusterVO = super.queryById(id, false);
        if (clusterVO != null) {
            ctgCache.remove(CacheConstant.CLUSTER_CACHE_NAME, clusterVO.getCode());
        }
        return super.deleteById(id);
    }
}