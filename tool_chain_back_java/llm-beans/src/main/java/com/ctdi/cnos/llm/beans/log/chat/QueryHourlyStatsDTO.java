package com.ctdi.cnos.llm.beans.log.chat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 查询指定模型按小时统计的对象。
 *
 * @author laiqi
 * @since 2024/11/26
 */
@Data
public class QueryHourlyStatsDTO {

    @NotNull(message = "数据验证失败，模型ID不能为空！")
    @ApiModelProperty(value = "模型ID")
    private Long modelId;

    @NotNull(message = "数据验证失败，起始时间不能为空！")
    @ApiModelProperty(value = "起始时间")
    private Date startTime;

    @NotNull(message = "数据验证失败，截至时间不能为空！")
    @ApiModelProperty(value = "截至时间")
    private Date endTime;
}