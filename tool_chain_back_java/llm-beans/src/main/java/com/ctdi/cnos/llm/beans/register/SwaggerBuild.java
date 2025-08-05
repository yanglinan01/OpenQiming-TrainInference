package com.ctdi.cnos.llm.beans.register;

import lombok.Data;

/**
 * @author wangyb
 * @create 2023/8/21 11:03
 * @描述
 */
@Data
public class SwaggerBuild {
    private String requestSchemaInfo;
    private String responseSchemaInfo;

    private String swaggerTitle;
    private String swaggerDescription;
    private String swaggerTags;
    private String apiResourcePath;
    private String requestMethod;

    private SwaggerParameter requestParameter;
    private SwaggerParameter responseParameter;
}
