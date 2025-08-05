import request from '@/plugins/request/provinceIndex';
//agent和工作流查询列表
export function appsCards(data) {
    return request({
        url: '/console/api/apps',
        method: 'get',
        params: data
    })
}
//插件查询列表
export function toolProviders() {
    return request({
        url: '/console/api/workspaces/current/tool-providers',
        method: 'get'
    })
}
//修改卡片名称
export function changeName(id,param) {
    return request({
        url: '/console/api/apps/'+id,
        method: 'put',
        data:param
    })
}
