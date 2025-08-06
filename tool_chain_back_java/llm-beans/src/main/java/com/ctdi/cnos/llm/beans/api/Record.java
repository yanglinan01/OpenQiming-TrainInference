package com.ctdi.cnos.llm.beans.api;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

/**
 * @author yuyong
 * @date 2024/5/10 9:42
 */
@Data
@ApiModel("访问记录表")
public class Record {

    private Integer id;

    private String sessionId;

    private String seqId;

    private String scene;

    private String question;

    private String prov;

    private String prompt;

    private String answer;

    private Date date;
}
