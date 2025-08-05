import request from '@/plugins/request';


//分页查询部署模型列表
export function deployTaskqueryPage(data) {
    return request({
        url: 'deployTask/queryPage',
        method: 'post',
        data
    })
}
//部署模型详情
export function deployTaskDetail(data) {
    return request({
        url: '/deployTask/detail',
        method: 'get',
        params: data
    })
}
//删除模型
export function deleteById(data) {
    return request({
        url: '/deployTask/deleteById',
        method: 'get',
        params: data
    })
}
//根据字典类型查询字典项列表
export function getDictListByDictType(data) {
    return request({
        url: '/dictionary/getDictListByDictType',
        method: 'get',
        params: data
    })
}
//查询模型列表
export function modelQueryList(data) {
    return request({
        url: '/model/queryList',
        method: 'post',
        data
    })
}
//查询训练任务列表
export function trainTaskQueryList(data) {
    return request({
        url: '/trainTask/queryList',
        method: 'post',
        data
    })
}
//分页查询部署机器列表
export function serverQueryPage(data) {
    return request({
        url: '/deployServer/queryPage',
        method: 'post',
        data
    })
}
//获取服务详情
export function getUsageDetail(data) {
    return request({
        url: '/deployServer/getUsageDetail',
        method: 'get',
        params: data
    })
}
//新增部署模型
export function deployTaskAdd(data) {
    return request({
        url: '/deployTask/add',
        method: 'post',
        data
    })
}
//查询地区列表
export function queryAreaList() {
    return request({
        url: '/deployArea/queryAreaList',
        method: 'get'
    })
}
//条件查询部署机器列表
export function deployServerQueryList(data) {
    return request({
        url: '/deployServer/queryList',
        method: 'post',
        data
    })
}
//条件查询部署机器列表
export function getDeployServerCardByIp(data) {
    return request({
        url: '/deployServerCard/getDeployServerCardByIp',
        method: 'get',
        params: data
    })
}

export function  updateOnlineStatus(data){
    return request({
        url: '/deployTask/updateOnlineStatus',
        method: 'get',
        params: data
    })
}
