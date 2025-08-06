//package com.ctdi.cnos.llm.train.listener;
//
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.core.collection.ListUtil;
//import cn.hutool.core.convert.Convert;
//import cn.hutool.core.lang.Opt;
//import com.alibaba.fastjson.JSONObject;
//import com.ctdi.cnos.llm.beans.train.deployTask.DeployTask;
//import com.ctdi.cnos.llm.beans.train.trainTask.TrainTaskVO;
//import com.ctdi.cnos.llm.context.UserContextHolder;
//import com.ctdi.cnos.llm.event.ActionConfigEvent;
//import com.ctdi.cnos.llm.system.entity.UserVO;
//import com.ctdi.cnos.llm.system.service.UserService;
//import com.ctdi.cnos.llm.train.client.ApiClient;
//import com.ctdi.cnos.llm.train.service.DeployTaskService;
//import com.ctdi.cnos.llm.train.service.TrainTaskService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.ApplicationListener;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
///**
// * @author wangyb
// * @date 2024/9/14 0014 17:06
// * @description DeployNacosListener
// */
//@Component
//@Slf4j
//@RequiredArgsConstructor
//public class DeployNacosListener implements ApplicationListener<ActionConfigEvent> {
//
//    private final DeployTaskService deployTaskService;
//    private final TrainTaskService trainTaskService;
//    private final UserService userService;
//    private final ApiClient apiClient;
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void onApplicationEvent(ActionConfigEvent event) {
//        log.info("接收事件");
//        Map<String, HashMap> propertyMap = event.getPropertyMap();
//        propertyMap.forEach((key, value) -> {
//            if ("train.deploy-host.GZ".equalsIgnoreCase(key)) {
//                String before = Convert.toStr(value.get("before"));
//                String after = Convert.toStr(value.get("after"));
//                log.info("train.deploy-host.GZ before: " + before);
//                log.info("train.deploy-host.GZ after: " + after);
//
//                //更新数据库表
//                List<Long> ids = this.updateDeployUrlByNacos(before, after);
//                log.info("贵州执行的任务有【{}】", ids.toString());
//            } else if ("train.deploy-host.QD".equalsIgnoreCase(key)) {
//                String before = Convert.toStr(value.get("before"));
//                String after = Convert.toStr(value.get("after"));
//                log.info("train.deploy-host.QD before: " + before);
//                log.info("train.deploy-host.QD after: " + after);
//
//                //更新数据库表
//                List<Long> ids = this.updateDeployUrlByNacos(before, after);
//                log.info("青岛执行的任务有【{}】", ids.toString());
//            }
//        });
//    }
//
//
//    /**
//     * @param before nacos修改前部署url
//     * @param after  nacos修改后部署url
//     * @return 执行的部署列表
//     */
//    private List<Long> updateDeployUrlByNacos(String before, String after) {
//        List<DeployTask> deployTasks = deployTaskService.queryListByDeployUrlFromNacos(before);
//        if (CollUtil.isNotEmpty(deployTasks)) {
//
//            List<Long> ids = deployTasks.stream().map(DeployTask::getId).collect(Collectors.toList());
//            deployTaskService.updateBatchDeployTask(ids, before, after);
//
//            //更新智能体部署地址
//            deployTasks.forEach(deployTask -> {
//                JSONObject object = new JSONObject();
//                object.put("task_id", deployTask.getId());
//                object.put("model_name", Opt.ofNullable(trainTaskService.queryById(deployTask.getModelId())).map(TrainTaskVO::getName).get());
//                object.put("employee_number", Opt.ofNullable(userService.queryById(deployTask.getCreatorId(), true)).map(UserVO::getEmployeeNumber).get());
//                object.put("endpoint_url", after + "/v1");
//                try {
//                    String result = apiClient.createAgentDeploy(object, UserContextHolder.getUserId());
//                    log.info("智能体模型部署deployId: {}创建详情：【{}】", deployTask.getId(), result);
//                } catch (Exception e) {
//                    log.error("智能体模型部署deployId: {}, 网络异常", deployTask.getId());
//                }
//            });
//
//            return ids;
//        }
//        return ListUtil.empty();
//    }
//
//}
