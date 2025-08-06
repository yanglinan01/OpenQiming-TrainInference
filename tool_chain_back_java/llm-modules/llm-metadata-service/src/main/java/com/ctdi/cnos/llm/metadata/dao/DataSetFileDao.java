package com.ctdi.cnos.llm.metadata.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.ctdi.cnos.llm.beans.meta.dataSet.DataSetFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据集上传文件(DataSetFile)表数据库访问层
 * @author wangyb
 * @since 2024-05-24 11:22:12
 */
@Mapper
public interface DataSetFileDao extends BaseMapper<DataSetFile> {

}

