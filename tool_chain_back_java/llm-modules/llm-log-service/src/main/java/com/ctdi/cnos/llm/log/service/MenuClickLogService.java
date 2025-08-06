package com.ctdi.cnos.llm.log.service;

import com.ctdi.cnos.llm.base.object.StatType;
import com.ctdi.cnos.llm.base.service.IBaseService;
import com.ctdi.cnos.llm.beans.log.menu.MenuClickLog;
import com.ctdi.cnos.llm.beans.log.menu.MenuClickLogVO;
import com.ctdi.cnos.llm.beans.meta.operationCenter.BarCharts;

import java.util.List;

/**
 * 菜单点击日志 数据操作服务接口。
 *
 * @author huangjinhua
 * @since 2024/10/16
 */
public interface MenuClickLogService extends IBaseService<MenuClickLog, MenuClickLogVO> {

    /**
     * 根据统计类型统计省份维度数据画图
     *
     * @param type day：当天；month：当月；all 累计
     * @return List<BarCharts>
     */
    List<BarCharts> queryChart(StatType type);


    /**
     * 获取累计的菜单点击数量
     *
     * @return 总点击量
     */
    Long getTotalSum();

}