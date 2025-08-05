package com.ctdi.cnos.llm.beans.meta.dialog;

import com.alibaba.fastjson.JSONObject;
import com.ctdi.cnos.llm.base.constant.SystemConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * 消息内容对象。
 *
 * @author laiqi
 * @since 2024/9/14
 */
@Accessors(chain = true)
@ApiModel(description = "消息内容对象")
@Data
public class ConversationContent {
    /**
     * 实际的消息内容。
     */
    @ApiModelProperty(value = "实际消息内容", required = true)
    @NotBlank(message = "数据验证失败，实际消息内容不能为空！")
    private String content;

    /**
     * 内容类型。
     * text表示文本内容。
     */
    @ApiModelProperty(value = "内容类型", required = true)
    private ConversationContentType contentType = ConversationContentType.TEXT;

    /**
     * 角色标识。
     * user表示发送者是用户。
     */
    @ApiModelProperty(value = "角色标识", required = true)
    private String role = SystemConstant.CONVERSATION_CONTENT_DEFAULT_ROLE_VALUE;

    /**
     * 扩展信息，用于携带额外的参数。
     */
    @ApiModelProperty(value = "扩展信息，用于携带额外的参数")
    private JSONObject ext;
}