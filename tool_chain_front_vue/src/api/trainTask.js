import request from '@/plugins/request';

//分页查询训练任务列表
export function trainTasksQueryPage(data) {
    return request({
        url: '/trainTask/queryPage',
        method: 'post',
        data
    })
}

//查询训练任务详情
export function getTrainTasksDetail(data) {
    return request({
        url: '/trainTask/detail',
        method: 'get',
        params: data
    })
}

//删除训练任务
export function deleteById(data) {
    return request({
        url: '/trainTask/deleteById',
        method: 'delete',
        params: data
    })
}

//终止训练任务
export function terminateById(data) {
    return request({
        url: '/trainTask/stopById',
        method: 'get',
        params: data
    });
}
//查询训练模型信息
export function queryModelList(data) {
    return request({
        url: '/baseModel/queryModelList',
        method: 'post',
        data
    });
}
//查询超参信息
export function queryParamListByModelId(data) {
    return request({
        url: '/model/queryTrainParamList',
        method: 'get',
        params: data
    });
}
//新增训练任务
export function trainTaskAdd(data) {
    return request({
        url: '/trainTask/add',
        method: 'post',
        data
    });
}
//查询模型列表
export function queryTrainModelList(data) {
    return request({
        url: '/model/queryList',
        method: 'post',
        data
    });
}

//更新训练任务信息
export function trainTaskUpdateById(data) {
    return request({
        url: '/trainTask/updateById',
        method: 'post',
        data
    });
}


export function checkUserTrainTaskCount(params){
    return request({
        url: '/trainTask/checkUserTrainTaskCount',
        method: 'get',
        params
    });
}
//查询训练任务列表
export function queryTrainTaskList(data) {
    return request({
        url: '/trainTask/queryList',
        method: 'post',
        data
    });
}

//集群详情信息
export function clusterDetail(data) {
    return request({
        url: '/cluster/clusterDetail',
        method: 'get',
        params: data
    })
}

export function trainTaskPublish(data) {
    return request({
        url: '/trainTask/publish',
        method: 'post',
        data
    });
}

export function getTenants() {
    return request({
        url: '/trainTask/getTenants',
        method: 'get',
    })
}


//删除训练任务Demo
export function deleteByIdDemo(data) {
    return request({
        url: '/trainTaskDemo/deleteById',
        method: 'delete',
        params: data
    })
}
//分页查询训练任务列表Demo
export function trainTasksQueryPageDemo(data) {
    return request({
        url: '/trainTaskDemo/queryPage',
        method: 'get',
        params: data
    })
}
//新增训练任务Demo
export function trainTaskAddDemo(data) {
    return request({
        url: '/trainTaskDemo/add',
        method: 'post',
        data
    });
}
//集群详情信息demo
export function clusterStrategyDetail() {
    return request({
        url: '/cluster/clusterStrategyDetail',
        method: 'get',
    })
}
//查询训练任务详情demo
export function getTrainTasksDetailDemo(data) {
    return request({
        url: '/trainTaskDemo/detail',
        method: 'get',
        params: data
    })
}
//更新训练任务信息demo
export function trainTaskUpdateByIdDemo(data) {
    return request({
        url: '/trainTaskDemo/updateById',
        method: 'post',
        data
    });
}
