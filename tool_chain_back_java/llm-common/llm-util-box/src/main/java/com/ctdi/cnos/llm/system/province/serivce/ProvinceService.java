package com.ctdi.cnos.llm.system.province.serivce;

import com.ctdi.cnos.llm.base.service.IBaseService;
import com.ctdi.cnos.llm.system.province.entity.Province;
import com.ctdi.cnos.llm.system.province.entity.ProvinceVO;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 省份表 数据操作服务接口。
 *
 * @author huangjinhua
 * @since 2024/10/16
 */
public interface ProvinceService extends IBaseService<Province, ProvinceVO> {

    /**
     * 查询省份形成map对象 -- 省份编码
     *
     * @param codeList         省份编码列表
     * @param abbreviationList 省份缩写列表
     * @param keyColumn        列字段
     * @return {code：ProvinceVO}
     */
    Map<String, ProvinceVO> queryMapToProvinceVO(List<String> codeList, List<String> abbreviationList, Function<ProvinceVO, String> keyColumn);


    /**
     * 查询省份形成map对象 -- 省份编码
     *
     * @param codeList         省份编码列表
     * @param abbreviationList 省份缩写列表
     * @param keyColumn        列字段
     * @return {code：name}
     */
    Map<String, String> queryMapToName(List<String> codeList, List<String> abbreviationList, Function<ProvinceVO, String> keyColumn);


    /**
     * 根据编码查询省份名称
     *
     * @param code 省份编码
     * @return String
     */
    String getNameByCode(String code);

}
