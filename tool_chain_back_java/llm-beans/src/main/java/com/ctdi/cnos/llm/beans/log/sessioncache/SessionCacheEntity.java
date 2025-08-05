package com.ctdi.cnos.llm.beans.log.sessioncache;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

@Data
@TableName("log.session_cache ")
@ApiModel(description = "会话缓存实体类")
public class SessionCacheEntity {

    @ApiModelProperty(value = "主键ID", example = "c61a14e9-d9ee-42de-b061-cfd73076543e")
    @TableId(value = "id", type = IdType.ASSIGN_UUID) //
    private String id;

    @ApiModelProperty(value = "会话ID", example = "CB8BB8EF5386CD9B330C6E2935F54C74^1745375545677")
    @TableField(value = "session_id") // 映射到 session_id 字段
    private String sessionId;

    @ApiModelProperty(value = "意图", example = "天翼看家障碍")
    @TableField(value = "intention") // 映射到 intention 字段
    private String intention;

    @ApiModelProperty(value = "会话次数", example = "0")
    @TableField(value = "session_num") // 映射到 session_num 字段
    private Integer sessionNum;

    @ApiModelProperty(value = "上次交互时间", example = "2025-04-23 10:40:20")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE) // 自动填充 update_time 字段
    private Date updateTime;
    @ApiModelProperty("省份名称")
    @TableField(value = "province")
    private String province;
}
