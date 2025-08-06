package com.ctdi.cnos.llm.system.province.dao;

import com.ctdi.cnos.llm.base.dao.BaseDaoMapper;
import com.ctdi.cnos.llm.system.province.entity.Province;
import org.apache.ibatis.annotations.Mapper;

/**
 * 省份表 数据操作访问接口。
 *
 * @author huangjinhua
 * @since 2024/10/16
 */
@Mapper
public interface ProvinceDao extends BaseDaoMapper<Province> {

}
