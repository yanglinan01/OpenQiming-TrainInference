import request from '@/plugins/request';
import provinceRequest from '@/plugins/request/provinceIndex';


//分页查询工作流列表
export function modelQueryPage(data) {
    return provinceRequest({
        url: '/console/api/apps',
        method: 'get',
        params: data
    })
}

//新增工作流
export function workFlowAdd(data) {
    return provinceRequest({
        url: '/console/api/apps',
        method: 'post',
        data
    });
}