package com.ctdi.cnos.llm.base.object;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctdi.cnos.llm.util.MybatisPlusUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 查询参数。
 *
 * @author laiqi
 * @since 2024/7/3
 */
@Data
@ApiModel(value = "查询参数", description = "查询参数对象，包含分页信息、过滤条件、排序规则等")
public class QueryParam {

    /**
     * 忽略用户权限
     */
    public static final String IGNORE_AUTH = "IGNORE_AUTH";

    /**
     * 用于数据过滤的DTO对象。
     */
    @ApiModelProperty(value = "用于数据过滤的DTO对象。")
    private Map<String, Object> filterMap;

    /**
     * 分页对象。
     */
    @NotNull(groups = {Groups.PAGE.class}, message = "数据验证失败，分页对象不能为空！")
    @Valid
    @ApiModelProperty(value = "分页对象。")
    private PageParam pageParam;

    /**
     * 排序字段对象。
     */
    @ApiModelProperty(value = "排序字段对象。")
    private List<SortingField> sortingFields;

    /**
     * 是否包含关联字典数据
     */
    @ApiModelProperty(value = "是否包含关联字典数据。默认不关联", example = "true")
    private Boolean withDict = false;

    // /**
    //  * 数据权限规则。
    //  * 0：查看全部
    //  * 1：仅查看当前用户
    //  */
    // @ApiModelProperty(value = "数据权限规则。默认仅查看当前用户(1),查看全部(0)", example = "1", notes = "0：查看全部;1：仅查看当前用户")
    // private int dataPermRuleType = DataPermRuleType.TYPE_USER_ONLY;

    /**
     * 将内部的过滤Map对象转换为指定类型的Dto对象并返回。
     *
     * @param filterClazz Dto对象的Class对象。
     * @param <T>         Dto对象的类型。
     * @return 如果filterMap字段为空，则返回空对象。
     */
    public <T> T getFilterDto(Class<T> filterClazz) {
        if (filterMap == null) {
            return ReflectUtil.newInstance(filterClazz);
        }
        return BeanUtil.toBeanIgnoreError(this.filterMap, filterClazz);
    }

    /**
     * 设置内部过滤的对象到filterMap。
     *
     * @param filterDto Dto对象条件。
     */
    public void setFilterDto(Object filterDto) {
        this.filterMap = BeanUtil.beanToMap(filterDto);
    }

    /**
     * 将内部的分页对象转换为MybatisPlus分页对象并返回。
     *
     * @return MybatisPlus分页对象。
     */
    public <T> Page<T> convertMpPage() {
        if (pageParam == null) {
            return null;
        }
        return MybatisPlusUtil.buildPage(pageParam);
    }

    public void ignoreAuth() {
        this.getFilterMap().put(IGNORE_AUTH, true);
    }
}