package com.ctdi.cnos.llm.system.province.serivce.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ctdi.cnos.llm.base.constant.CacheConstant;
import com.ctdi.cnos.llm.base.object.LambdaQueryWrapperX;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.base.service.BaseService;
import com.ctdi.cnos.llm.cache.ctg.CtgCache;
import com.ctdi.cnos.llm.system.province.dao.ProvinceDao;
import com.ctdi.cnos.llm.system.province.entity.Province;
import com.ctdi.cnos.llm.system.province.entity.ProvinceVO;
import com.ctdi.cnos.llm.system.province.serivce.ProvinceService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 省份表 数据操作服务类
 *
 * @author huangjinhua
 * @since 2024/10/16
 */
@Slf4j
public class ProvinceServiceImpl extends BaseService<ProvinceDao, Province, ProvinceVO> implements ProvinceService {

    private final CtgCache ctgCache;

    public ProvinceServiceImpl(ProvinceDao baseMapper) {
        super.baseMapper = baseMapper;
        ctgCache = SpringUtil.getBean(CtgCache.class);
    }

    @Override
    public void configureQueryWrapper(LambdaQueryWrapperX<Province> wrapper, QueryParam queryParam) {
        Province filter = queryParam.getFilterDto(Province.class);
    }

    @Override
    public Map<String, ProvinceVO> queryMapToProvinceVO(List<String> codeList, List<String> abbreviationList, Function<ProvinceVO, String> keyColumn) {
        List<ProvinceVO> provinceList = this.queryListByCodeOrAbbreviation(codeList, abbreviationList);
        if (CollUtil.isEmpty(provinceList)) {
            return MapUtil.empty();
        }
        return provinceList.stream().collect(Collectors.toMap(keyColumn, Function.identity()));
    }

    @Override
    public Map<String, String> queryMapToName(List<String> codeList, List<String> abbreviationList, Function<ProvinceVO, String> keyColumn) {
        List<ProvinceVO> provinceList = this.queryListByCodeOrAbbreviation(codeList, abbreviationList);
        if (CollUtil.isEmpty(provinceList)) {
            return MapUtil.empty();
        }
        return provinceList.stream().collect(Collectors.toMap(keyColumn, ProvinceVO::getName));
    }

    @Override
    public String getNameByCode(String code) {
        Map<String, String> map = this.queryMapToName(CollUtil.newArrayList(code), null, ProvinceVO::getCode);
        return map.get(code);
    }

    /**
     * 查询全量，缓存，取指定数据
     *
     * @param codeList
     * @param abbreviationList
     * @return
     */
    private List<ProvinceVO> queryListByCodeOrAbbreviation(List<String> codeList, List<String> abbreviationList) {
        List<ProvinceVO> list = CollUtil.newArrayList();
        LambdaQueryWrapper<Province> wrapper = new LambdaQueryWrapper<>();
        List<Province> provinces = ctgCache.getCache(CacheConstant.PROVINCE_CACHE_NAME, CacheConstant.PROVINCE_CACHE_KEY);
        if (CollUtil.isEmpty(provinces)) {
            provinces = super.baseMapper.selectList(wrapper);
            if (CollUtil.isNotEmpty(provinces)) {
                ctgCache.set(CacheConstant.PROVINCE_CACHE_NAME, CacheConstant.PROVINCE_CACHE_KEY, provinces, CacheConstant.CACHE_EXPIRE_1_DAY);
            }
        } else {
            log.info("读取缓存【{}】，key：{}", CacheConstant.PROVINCE_CACHE_NAME, CacheConstant.PROVINCE_CACHE_KEY);
        }
        if (CollUtil.isNotEmpty(provinces)) {
            list = convertToVoList(provinces, null);
            if (CollUtil.isNotEmpty(codeList)) {
                return list.stream().filter(item -> CollUtil.contains(codeList, item.getCode())).collect(Collectors.toList());
            } else if (CollUtil.isNotEmpty(abbreviationList)) {
                return list.stream().filter(item -> CollUtil.contains(abbreviationList, item.getAbbreviation())).collect(Collectors.toList());
            } else {
                return list;
            }
        }
        return list;
    }
}