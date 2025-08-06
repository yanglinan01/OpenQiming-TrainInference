import request from '@/plugins/request';
import provinceRequest from "@/plugins/request/provinceIndex";

//大模型体验
export function menuClickLogAdd(data) {
    return request({
        url: '/menuClickLog/add',
        method: 'post',
        data
    });
}
//按省份维度统计各省构建应用数量统计
export function agentCharts(data) {
    return request({
        url: '/operationCenter/agentCharts',
        method: 'get',
        params: data
    })
}
//按省份维度统计统计各省模型构建数量
export function modelCharts(data) {
    return request({
        url: '/operationCenter/modelCharts',
        method: 'get',
        params: data
    })
}
//能力调用榜单
export function callRank(data) {
    return request({
        url: '/operationCenter/callRank',
        method: 'get',
        params: data
    })
}
export function platformStat(data) {
    return request({
        url: 'operationCenter/platformStat',
        method: 'get',
        params: data
    })
}

export function userLoginChart(data) {
    return request({
        url: 'operationCenter/userLoginChart',
        method: 'get',
        params: data
    })
}

export function useLiveChart(data) {
    return request({
        url: 'operationCenter/useLiveChart',
        method: 'get',
        params: data
    })
}
