import request from '@/plugins/request';
import provinceRequest from '@/plugins/request/provinceIndex';


//分页查询应用列表
export function agentQueryPage(data) {
    return provinceRequest({
        url: '/console/api/apps',
        method: 'get',
        params: data
    })
}
//分页查询应用列表
export function applicationQueryPage(data) {
    return request({
        url: 'applicationSquare/queryPage',
        method: 'get',
        params: data
    })
}
//分页查询模型列表
export function modelQueryPage(data) {
    return request({
        url: '/model/queryPage',
        method: 'post',
        data
    })
}
//新增agent
export function agentAdd(data) {
    return provinceRequest({
        url: '/console/api/apps',
        method: 'post',
        data
    });
}
