import request from '@/plugins/request';
import provinceRequest from "@/plugins/request/provinceIndex";

export function psQueryPage (data) {
    return request({
        url: 'projectSpace/queryPage',
        method: 'post',
        data
    });
}
export function psQueryList (data) {
    return request({
        url: 'projectSpace/queryList',
        method: 'post',
        data
    });
}
export function provinceQuery (data) {
    return request({
        url: 'province/queryList',
        method: 'post',
        data
    });
}
export function queryTreeList(type) {
    return request({
        url: `label3cTree/queryTreeList/`+type,
        method: 'get'
    });
}

export function psQueryById(data) {
    return request({
        url: 'projectSpace/queryById',
        method: 'get',
        params: data
    });
}
//新增项目空间
export function  projectSpaceAdd(data) {
    return request({
        url: 'projectSpace/add',
        method: 'post',
        data
    });
}

//项目空间-意图识别获取数据集列表
export function queryListByCategory(data) {
    return request({
        url: '/dataSet/queryListByCategory',
        method: 'get',
        params: data
    })
}
//项目空间-分页查询意图识别训练任务列表
export function queryPageByCategory(data) {
    return request({
        url: '/trainTask/queryPageByCategory',
        method: 'post',
        data
    });
}

//项目空间-模型体验获取自建模型列表
export function spQueryListByCategory(data) {
    return request({
        url: '/deployTask/queryListByCategory',
        method: 'post',
        data
    })
}

//项目空间-意图识别对话
export function intentRecognize(data){
    return request({
        url: '/deployTask/intentRecognize',
        method: 'post',
        data
    })
}
//迭代训练任务
export function iterate(data){
    return request({
        url: '/trainTask/iterate',
        method: 'post',
        data
    })
}
//训练版本启用
export function versionEnable(data){
    return request({
        url: '/trainTask/versionEnable',
        method: 'post',
        data
    })
}
//任务组编辑
export function taskGroupEdit(data){
    return request({
        url: '/taskGroup/edit',
        method: 'post',
        data
    })
}
//任务组删除
export function taskGroupDelete(data){
    return request({
        url: '/taskGroup/delete',
        method: 'post',
        data
    })
}
//任务组部署
export function taskGroupDeploy(data){
    return request({
        url: '/taskGroup/deploy',
        method: 'post',
        data
    })
}
//任务组取消部署
export function taskGroupUnDeploy(data){
    return request({
        url: '/taskGroup/unDeploy',
        method: 'post',
        data
    })
}
