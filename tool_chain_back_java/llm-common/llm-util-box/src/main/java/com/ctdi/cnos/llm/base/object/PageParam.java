package com.ctdi.cnos.llm.base.object;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Controller参数中的分页请求对象
 *
 * @author laiqi
 * @since 2024/7/3
 */
@ApiModel("分页请求对象")
@Data
public class PageParam {

    /**
     * 默认分页页码
     */
    public static final int DEFAULT_PAGE_NUM = 1;
    /**
     * 默认每页条数
     */
    public static final int DEFAULT_PAGE_SIZE = 10;
    /**
     * 默认每页条数最大值
     */
    public static final int DEFAULT_MAX_SIZE = 100;

    /**
     * 每页条数 - 不分页
     */
    public static final Integer PAGE_SIZE_NONE = -1;
    /**
     * 默认分页对象
     */
    public static final PageParam DEFAULT_PAGE_PARAM = new PageParam();

    static {
        DEFAULT_PAGE_PARAM.setPageNum(DEFAULT_PAGE_NUM);
        DEFAULT_PAGE_PARAM.setPageSize(DEFAULT_PAGE_SIZE);
    }

    /**
     * 默认分页对象
     */
    public static final Page<T> DEFAULT_PAGE = new Page<>(DEFAULT_PAGE_NUM, DEFAULT_PAGE_SIZE);

    /**
     * 页码，从 1 开始。
     */
    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码最小值为 1")
    @ApiModelProperty(value = "页码，从 1 开始", example = "1")
    private Integer pageNum;

    /**
     * 每页大小。
     */
    @NotNull(message = "每页条数不能为空")
    @Min(value = 1, message = "每页条数最小值为 1")
    @Max(value = DEFAULT_MAX_SIZE, message = "每页条数最大值为 " + DEFAULT_MAX_SIZE)
    @ApiModelProperty(value = "每页大小。", example = "10")
    private Integer pageSize;

    /**
     * 创建分页对象
     * @param pageNum
     * @param pageSize
     * @return
     */
    public static PageParam of(Integer pageNum, Integer pageSize) {
        PageParam pageParam = new PageParam();
        pageParam.setPageNum(pageNum);
        pageParam.setPageSize(pageSize);
        return pageParam;
    }

    /**
     * 创建分页对象
     * @param pageSize
     * @return
     */
    public static PageParam of(Integer pageSize) {
        return of(DEFAULT_PAGE_NUM, pageSize);
    }
}