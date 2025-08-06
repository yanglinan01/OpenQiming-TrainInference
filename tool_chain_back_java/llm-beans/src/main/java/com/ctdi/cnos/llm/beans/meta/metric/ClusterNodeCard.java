package com.ctdi.cnos.llm.beans.meta.metric;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author yuyong
 * @date 2024/9/25 11:25
 */
@Getter
@Setter
public class ClusterNodeCard {

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 所用卡数
     */
    private List<String> gpuIds;
}
