package com.ctdi.cnos.llm.log.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ctdi.cnos.llm.base.object.LambdaQueryWrapperX;
import com.ctdi.cnos.llm.base.object.QueryParam;
import com.ctdi.cnos.llm.base.object.StatType;
import com.ctdi.cnos.llm.base.service.BaseService;
import com.ctdi.cnos.llm.beans.log.menu.MenuClickLog;
import com.ctdi.cnos.llm.beans.log.menu.MenuClickLogVO;
import com.ctdi.cnos.llm.beans.meta.operationCenter.BarCharts;
import com.ctdi.cnos.llm.log.dao.MenuClickLogDao;
import com.ctdi.cnos.llm.log.service.MenuClickLogService;
import com.ctdi.cnos.llm.system.province.entity.ProvinceVO;
import com.ctdi.cnos.llm.system.province.serivce.ProvinceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 菜单点击日志 数据操作服务类
 *
 * @author huangjinhua
 * @since 2024/10/16
 */
@RequiredArgsConstructor
@Service("menuClickLogService")
public class MenuClickLogServiceImpl extends BaseService<MenuClickLogDao, MenuClickLog, MenuClickLogVO> implements MenuClickLogService {

    private final ProvinceService provinceService;

    @Override
    public void configureQueryWrapper(LambdaQueryWrapperX<MenuClickLog> wrapper, QueryParam queryParam) {
        MenuClickLog filter = queryParam.getFilterDto(MenuClickLog.class);
    }

    @Override
    public List<BarCharts> queryChart(StatType type) {
        List<BarCharts> list = CollUtil.newArrayList();
        Date beginDate = null;
        boolean isQuery = false;
        if (StatType.DAY == type) {
            beginDate = DateUtil.beginOfDay(DateUtil.date());
            isQuery = true;
        } else if (StatType.MONTH == type) {
            beginDate = DateUtil.beginOfMonth(DateUtil.date());
            isQuery = true;
        } else if (StatType.ALL == type) {
            isQuery = true;
        }
        //获取31省，HQ归其他
        Map<String, String> map = provinceService.queryMapToName(null, null, ProvinceVO::getAbbreviation);
        //组装全量数据
        for (String belong : map.keySet()) {
            BarCharts charts = new BarCharts();
            if (CharSequenceUtil.equalsAnyIgnoreCase("HQ", belong)) {
                charts.setAbbreviation("OTHER");
                charts.setProvince("其他");
            } else {
                charts.setAbbreviation(belong);
                charts.setProvince(map.get(belong));
            }
            charts.setCount(0);
            list.add(charts);
        }

        Map<String, Integer> countMap = new HashMap<>(32);
        if (isQuery) {
            LambdaQueryWrapper<MenuClickLog> wrapper = new LambdaQueryWrapper<>();
            wrapper.ge(beginDate != null, MenuClickLog::getClickDate, beginDate);
            wrapper.groupBy(MenuClickLog::getUserBelong);
            wrapper.orderByDesc(MenuClickLog::getCount);
            wrapper.select(MenuClickLog::getUserBelong, MenuClickLog::getCount);
            List<MenuClickLog> menuClickLogList = super.baseMapper.selectList(wrapper);
            if (CollUtil.isNotEmpty(menuClickLogList)) {
                for (MenuClickLog log : menuClickLogList) {
                    if (CharSequenceUtil.equalsAnyIgnoreCase("HQ", log.getUserBelong())
                            || CharSequenceUtil.isBlank(map.get(log.getUserBelong()))) {
                        countMap.merge("OTHER", log.getCount(), Integer::sum);
                    } else {
                        countMap.put(log.getUserBelong(), log.getCount());
                    }
                }
            }
            if (MapUtil.isNotEmpty(countMap)) {
                list = list.stream().peek(log -> {
                    Integer sum = countMap.get(log.getAbbreviation());
                    if (sum != null) {
                        log.setCount(sum);
                    }
                }).collect(Collectors.toList());
            }

        }

        //数据排序
        list.sort(Comparator.comparing(BarCharts::getCount).reversed().thenComparing(BarCharts::getAbbreviation));
        return list;
    }

    @Override
    public Long getTotalSum() {
        LambdaQueryWrapper<MenuClickLog> wrapper = new LambdaQueryWrapper<>();
        return super.baseMapper.selectCount(wrapper);
    }
}