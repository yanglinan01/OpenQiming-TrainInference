import request from '@/plugins/request';


//查询模型选择
export function getLogModelList(data) {
    return request({
        url: 'trainTask/queryList',
        method: 'get',
        params: data
    })
}


//查询日志概览
export function queryStatistics(data) {
    return request({
        url: 'logModel/queryStatistics',
        method: 'post',
        data
    })
}

//模型调用统计
export function queryModelRequest(data) {
    return request({
        url: 'logModel/queryModelRequest',
        method: 'post',
        data
    })
}

//接口调用统计 统计图
export function queryModelRequestChart(data) {
    return request({
        url: 'logModel/queryModelRequestChart',
        method: 'post',
        data
    })
}

//接口调用统计 列表
export function queryModelRequestList(data) {
    return request({
        url: 'logModel/queryModelRequestList',
        method: 'post',
        data
    })
}

export function queryMmTrainLogList(data) {
    return request({
        url: '/logModel/queryMmTrainLogList',
        method: 'post',
        data
    })
}

//查询模型调用汇总信息(指定模型)
export function getModelCallSummary(data) {
    return request({
        url: '/modelChatLog/getModelCallSummary',
        method: 'get',
        params: data
    })
}
//按小时统计的Token数量(指定模型和时间范围)
export function getHourlyTokenStats(data) {
    return request({
        url: '/modelChatLog/getHourlyTokenStats',
        method: 'post',
        data
    })
}
//按小时统计的调用数量(指定模型和时间范围)。
export function getHourlyCallStats(data) {
    return request({
        url: '/modelChatLog/getHourlyCallStats',
        method: 'post',
        data
    })
}
//根据模型和时间范围获取对话日志(指定模型和时间范围)
export function queryModelChatLog(data) {
    return request({
        url: '/modelChatLog/queryModelChatLog',
        method: 'post',
        data
    })
}
