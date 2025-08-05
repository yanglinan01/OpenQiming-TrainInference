package com.ctdi.cnos.llm.dcossApi.dao;

import com.ctdi.cnos.llm.beans.register.DcoosApi;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author hanfulei
 * @Date 2024/4/18 15:42
 * @Version 1.0
 **/
@Mapper
public interface DcossToolMapper {

//    String dcossApiurl(String guid);
    DcoosApi selectByPrimaryKey(String id);
}
