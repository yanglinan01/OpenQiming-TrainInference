import request from '@/plugins/request';
export function resourceCount0(data) {
    return request({
        url: 'cluster/resourceCount',
        method: 'post',
        data
    })
}
//部署模型详情
export function resourceCount() {
    return request({
        url: 'cluster/resourceCount',
        method: 'get',
    })
}

export function resourceUsage() {
    return request({
        url: 'cluster/resourceUsage',
        method: 'get',
    })
}


export function clusterDetail(data) {
    return request({
        url: 'cluster/clusterDetail',
        method: 'get',
        params: data
    })
}


export function clusterUsageTrend(data) {
    return request({
        url: 'cluster/clusterUsageTrend',
        method: 'get',
        params: data
    })
}

export function getUsingResourceInfo(data) {
    return request({
        url: 'cluster/getUsingResourceInfo',
        method: 'post',
        data
    })
}
