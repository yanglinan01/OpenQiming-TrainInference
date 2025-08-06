import request from '@/plugins/request';

//获取知识库语料接口
export function intentionRecognitionCorpusList(data) {
    return request({
        url: '/dataSet/intentionRecognitionCorpusList',
        method: 'post',
        data
    })
}
//意图识别数据集创建接口
export function addIntentionRecognition(data) {
    return request({
        url: '/dataSet/addIntentionRecognition',
        method: 'post',
        data
    })
}
//分页获取意图识别数据集接口
export function queryPageByCategory(data) {
    return request({
        url: '/dataSet/queryPageByCategory',
        method: 'get',
        params: data
    })
}
//新增DataSet
export function add(data) {
    return request({
        url: '/dataSet/projSpace/add',
        method: 'post',
        data
    })
}
//新增知识库数据集
export function addFromKnowledgeBase(data) {
    return request({
        url: '/dataSet/projSpace/addFromKnowledgeBase',
        method: 'post',
        data
    })
}