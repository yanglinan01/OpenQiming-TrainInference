package com.ctdi.cnos.llm.log.dao;

import com.ctdi.cnos.llm.base.dao.BaseDaoMapper;
import com.ctdi.cnos.llm.beans.log.menu.MenuClickLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 菜单点击日志 数据操作访问接口。
 *
 * @author huangjinhua
 * @since 2024/10/16
 */
@Mapper
public interface MenuClickLogDao extends BaseDaoMapper<MenuClickLog> {

}
