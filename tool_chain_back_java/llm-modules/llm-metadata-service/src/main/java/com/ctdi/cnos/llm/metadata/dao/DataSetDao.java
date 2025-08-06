package com.ctdi.cnos.llm.metadata.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.beans.meta.dataSet.DataSet;
import com.ctdi.cnos.llm.beans.meta.dataSet.DataSetAndPrInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 数据集(DataSet)表数据库访问层
 * @author wangyb
 * @since 2024-05-15 14:06:52
 */
@Mapper
public interface DataSetDao extends BaseMapper<DataSet> {

    Page<DataSet> queryList(Page<DataSet> page, @Param("dataSet") DataSet dataSet);

    List<DataSet> queryList(@Param("dataSet") DataSet dataSet);

    //void deleteById(@Param("dataSetId") BigDecimal dataSetId, @Param("creatorId") BigDecimal creatorId);

    void callIncr(@Param("dataSetId")BigDecimal dataSetId);

    DataSetAndPrInfoVO queryById(@Param("id")BigDecimal id);

    DataSetAndPrInfoVO queryTestById(@Param("id") BigDecimal id);
}

