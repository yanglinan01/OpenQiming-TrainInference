import request from '@/plugins/request';


//查询用户信息分页
export function authQueryPage(data) {
    return request({
        url: '/web/auth/queryPage',
        method: 'post',
        data
    })
}
//删除用户信息
export function deleteUser(data) {
    return request({
        url: '/web/auth/deleteUser',
        method: 'delete',
        params: data
    })
}
//修改用户基础权限（工具链、智能体平台）
export function changeUserBaseAuth(data) {
    return request({
        url: '/web/auth/changeUserBaseAuth',
        method: 'post',
        data
    })
}

//创建用户接口
export function addUser(data) {
    return request({
        url: '/web/auth/addUser',
        method: 'post',
        data
    })
}

//导入用户信息
export function importUser(data) {
    return request({
        url: '/web/auth/importUser',
        method: 'post',
        data
    })
}

//一键开放
export function oneClickBinding(data) {
    return request({
        url: '/userModel/oneClickBinding',
        method: 'post',
        data
    })
}
//批量绑定模型-用户
export function batchBinding(data) {
    return request({
        url: '/userModel/batchBinding',
        method: 'post',
        data
    })
}
//查询模型下用户信息分页
export function queryPageByModel(data) {
    return request({
        url: '/web/auth/queryPageByModel',
        method: 'post',
        data
    })
}
//查询模型下用户信息分页
export function batchBindingAll(data) {
    return request({
        url: '/userModel/batchBindingAll',
        method: 'post',
        data
    })
}
//分页查询门户工单完整数据
export function queryOrderAccountPage(data) {
    return request({
        url: '/order/queryOrderAccountPage',
        method: 'post',
        data
    })
}
//提交待开通用户
export function submitOrderAccount(data) {
    return request({
        url: '/order/submitOrderAccount',
        method: 'post',
        data
    })
}
