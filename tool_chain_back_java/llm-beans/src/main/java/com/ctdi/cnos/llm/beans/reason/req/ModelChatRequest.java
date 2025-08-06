package com.ctdi.cnos.llm.beans.reason.req;

import cn.hutool.core.util.IdUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 大模型websocket请求对象。
 *
 * @author laiqi
 * @since 2024/6/7
 */
@Setter
@Getter
@ApiModel("启明网络大模型聊天请求对象")
public class ModelChatRequest {
    /**
     * 身份识别码。数据EOP平台的识别码。此处使用X-APP-ID。
     */
    @ApiModelProperty(value = "身份识别码。数据EOP平台的识别码。此处使用X-APP-ID。", hidden = true)
    private String uid;

    /**
     * 时间戳。距离1970-01-01毫秒数
     */
    @ApiModelProperty(value = "时间戳。距离1970-01-01毫秒数", hidden = true)
    private long timestamp = System.currentTimeMillis();

    /**
     * 请求序列号，uuid4格式生成，该次请求的唯一标识。
     */
    @ApiModelProperty(value = "请求序列号，uuid4格式生成，该次请求的唯一标识。", hidden = true)
    private String seqid = IdUtil.randomUUID();

    /**
     * 会话识别码，uuid4格式生成，用于区分不同会话
     */
    @ApiModelProperty(value = "会话识别码，uuid4格式生成，用于区分不同会话", hidden = true)
    private String session_id;

    /**
     * “true”：打开流式输出
     * “false”：关闭流式输出
     */
    @ApiModelProperty(value = "流式输出，true表示打开流失输出;false表示关闭流失输出。默认是true", example = "true")
    private String stream = "true";

    /**
     * 省份识别码，用于区分不同省份调用方。识别码与省份之间的映射关系详见附录一
     */
    @ApiModelProperty(value = "省份识别码，用于区分不同省份调用方。识别码与省份之间的映射关系详见附录一", required = true, example = "911010000000000000000000", hidden = true)
    private String prov;

    /**
     * 用户问题
     */
    @ApiModelProperty(value = "用户问题", required = true, example = "先有蛋还是先有鸡")
    private String param1;

    /**
     * 预留字段
     */
    @ApiModelProperty(value = "预留字段", example = "")
    private String param2;

    /**
     * 预留字段
     */
    @ApiModelProperty(value = "预留字段", example = "")
    private String param3;

    /**
     * 预留字段
     */
    @ApiModelProperty(value = "预留字段", example = "")
    private String param4;

    /**
     * 预留字段
     */
    @ApiModelProperty(value = "预留字段", example = "")
    private String param5;

    /**
     * 预留字段
     */
    @ApiModelProperty(value = "预留字段", example = "")
    private String param6;

    /**
     * 场景码。
     */
    @ApiModelProperty(value = "场景码。", required = true, example = "8", hidden = true)
    private int scene;
}