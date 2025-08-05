package com.ctdi.cnos.llm.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author wangyb
 * @date 2024/11/28
 * @description 机器信息
 */

@Data
@Accessors(chain = true)
public class HostInfo {

    @NotNull(message = "数据验证失败，ip不能为空！")
    private String ip;

    @NotNull(message = "数据验证失败，port不能为空！")
    private Integer port;

    @NotNull(message = "数据验证失败，username不能为空！")
    private String username;

    @NotNull(message = "数据验证失败，password不能为空！")
    private String password;

    private String directory;

    private String filename;

    private String filePath;
    private boolean flag;
}
