import request from '@/plugins/request';
import provinceRequest from '@/plugins/request/provinceIndex';


//查询多智能体
export function queryMultiAgentList(data) {
    return provinceRequest({
        url: '/console/api/apps',
        method: 'get',
        params: data
    })
}


//添加多智能体
export function addMultiAgent(data) {
    return provinceRequest({
        url: '/console/api/apps',
        method: 'post',
        data
    })
}