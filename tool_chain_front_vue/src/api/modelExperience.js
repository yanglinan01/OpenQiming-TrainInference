import request from '@/plugins/request';

//大模型体验
export function largeModelExperienceSend(data, modelId) {
    return request({
        url: '/model/startChat',
        method: 'post',
        headers: {
            'Model-Id': modelId
        },
        data
    });
}

//根据字典类型查询字典项列表
export function getDictListByDictType(data) {
    return request({
        url: '/dictionary/getDictListByDictType',
        method: 'get',
        params: data
    })
}

//查询应用列表
export function queryList(data) {
    return request({
        url: 'applicationSquare/queryList',
        method: 'get',
        params: data
    })
}

export function queryModelExperienceList(data) {
    return request({
        url: '/model/queryList',
        method: 'post',
        data
    })
}

export function modelChatLogQueryPage(data) {
    return request({
        url: 'modelChatLog/queryPage',
        method: 'post',
        data
    });
}

export function deployTaskQueryList(data) {
    return request({
        url: 'deployTask/queryList',
        method: 'post',
        data
    })
}
export function customerModelChat(data){
    return request({
        url: 'model/customerModelChat',
        method: 'post',
        data
    })
}

export function llmTimeModelPrediction(data){
    return request({
        url: 'model/llmTimeModelPrediction',
        method: 'post',
        data
    })
}
//对话
export function conversation(data){
    return request({
        url: 'dialog/conversation',
        method: 'post',
        data
    })
}
