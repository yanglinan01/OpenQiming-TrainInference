package com.ctdi.cnos.llm.job.train;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.ctdi.cnos.llm.feign.train.DeployTaskServiceClientFeign;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 模型部署
 *
 * @author wangyb
 * @since 2024/7/9
 */
@Component("deployJob")
@RequiredArgsConstructor
@Slf4j
public class DeployJob {


    private final DeployTaskServiceClientFeign deployClient;


    /**
     * 定时获取大模型部署状态_k8s
     */
    public void callBackDeployStatus() throws Exception {
        deployClient.callBackDeployStatus();
    }

    /**
     * 定时发送大模型部署状态_k8s
     */
    public void callBackDeploySend() throws Exception {
        deployClient.callBackDeploySend();
    }

    /**
     * 删除超时的部署模型
     * @param offsetDay 超时时间
     */
    public void deleteExpiredDeployTask(Integer offsetDay) {
        if(offsetDay == null) {
            offsetDay = 1;
        }
        DateTime start = DateUtil.date();
        String taskMsg = deployClient.deleteExpiredDeployTask(offsetDay);
        DateTime end = DateUtil.date();
        long range = DateUtil.between(start, end, DateUnit.SECOND, true);
        if (CharSequenceUtil.isNotBlank(taskMsg)) {
            log.info(DateUtil.format(start, DatePattern.NORM_DATETIME_PATTERN) + taskMsg + "耗时为(s)：" + range);
        } else {
            log.info(DateUtil.format(start, DatePattern.NORM_DATETIME_PATTERN) + " 当前时间无需要删除超时的部署模型");
        }
    }


}
