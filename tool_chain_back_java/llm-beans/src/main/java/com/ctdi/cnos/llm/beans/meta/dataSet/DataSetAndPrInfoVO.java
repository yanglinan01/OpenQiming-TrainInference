package com.ctdi.cnos.llm.beans.meta.dataSet;

import com.ctdi.cnos.llm.beans.train.IntentRecognition.IntentRecognitionCorpusParam;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author wangyb
 * @date 2024/5/16 0016 9:21
 * @description DataSetAndPrInfoVO
 */
@Data
@ApiModel("数据集问答对详情vo")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DataSetAndPrInfoVO extends DataSet {

    @ApiModelProperty(value = "类别，以逗号(,)分隔")
    private String types;

    @ApiModelProperty(value = "20条问答对详情")
    private String context;
    private List<?> contextList;

    @ApiModelProperty(value = "远程数据集列表")
    private List<String> remoteFilePath;

    @ApiModelProperty(value = "数据集保存目录")
    private String uploadDir;

    @ApiModelProperty(value = "权限sql")
    @JsonIgnore
    private String dataScopeSql;

    private Object detailList;

    private BigDecimal dataSetFileId;

    @ApiModelProperty(value = "条数")
    private Long total;

    @ApiModelProperty(value = "数据类型")
    private List<String> dataTypes;

    @ApiModelProperty(value = "数据集id集合")
    private List<BigDecimal> ids;

    @ApiModelProperty(value = "知识来源")
    private String origin;

    private List<IntentRecognitionCorpusParam> IntentRecognitionCorpusList;

}
