package com.ctdi.cnos.llm.beans.register;

import lombok.Data;

import java.util.List;

/**
 * SwaggerParameter实体类
 *
 * @author wangyb
 * @since 2024-04-17 09:46:34
 */
@Data
public class SwaggerParameter {
    private String name;

    private String description;

    private String required;

    private String type;

    private SwaggerParameter items;

    private List<SwaggerParameter> properties;
}
