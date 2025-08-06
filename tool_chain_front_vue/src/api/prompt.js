import request from '@/plugins/request';

//获取当前用户信息
export function getCurrentUserInfo(data) {
    return request({
        url: '/web/auth/getCurrentUserInfo',
        method: 'get',
        params: data
    })
}
//根据当前用户获取prompt数量
export function getPromptCountByUserId(data) {
    return request({
        url: '/prompt/getPromptCountByUserId',
        method: 'get',
        params: data
    })
}
//创建Prompt模板
export function promptAdd(data) {
    return request({
        url: '/prompt/add',
        method: 'post',
        data
    })
}
//编辑Prompt模板
export function promptUpdateById(data) {
    return request({
        url: '/prompt/updateById',
        method: 'post',
        data
    })
}
//分页查询prompt列表
export function promptQueryPage(data) {
    return request({
        url: '/prompt/queryPage',
        method: 'get',
        params: data
    })
}
//根据字典类型查询字典项列表
export function getDictListByDictType(data) {
    return request({
        url: '/dictionary/getDictListByDictType',
        method: 'get',
        params: data
    })
}
//查询模型列表
export function modelQueryList(data) {
    return request({
        url: '/model/queryList',
        method: 'post',
        data
    })
}
//删除prompt
export function deleteById(data) {
    return request({
        url: '/prompt/deleteById',
        method: 'delete',
        params: data
    })
}
//查询prompt详情
export function detail(data) {
    return request({
        url: '/prompt/detail',
        method: 'get',
        params: data
    })
}
//调用大模型优化接口
export function optimize(data) {
    return request({
        url: '/question/optimize',
        method: 'post',
        data
    })
}
//根据 prompt 内容获取变量标识符
export function getIdentifier(data) {
    return request({
        url: '/prompt/getIdentifier',
        method: 'post',
        data
    })
}
//根据当前用户获取可用模型、prompt模版、模型训练任务、可用插件数量
export function getMyAbilitiesByUserId(data) {
    return request({
        url: '/statistics/getMyAbilitiesByUserId',
        method: 'get',
        params: data
    })
}