package com.ctdi.cnos.llm.beans.train.deployTask;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 部署任务(DeployTask)请求类
 *
 * @author wangyy
 * @since 2024-12-18
 */

@Data
@ApiModel("DeployTaskReq")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DeployTaskReq {

    /**
     * 项目空间id
     */
    @ApiModelProperty(value = "项目空间id", example = "1")
    private String projectSpaceId;

}
