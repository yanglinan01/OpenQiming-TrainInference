import request from '@/plugins/request';

//分页查询符合条件的项目空间用户角色关联信息表数据
export function projectUserRoleQueryPage(data) {
    return request({
        url: '/projectUserRole/queryPage',
        method: 'get',
        params: data
    })
}
//创建项目空间用户角色关联信息表
export function projectUserRoleAddBatch(data) {
    return request({
        url: '/projectUserRole/addBatch',
        method: 'post',
        data
    })
}
//删除项目空间用户角色关联信息表
export function projectUserRoleDeleteById(data) {
    return request({
        url: '/projectUserRole/deleteById',
        method: 'delete',
        data
    })
}
//更新项目空间用户角色关联信息表
export function projectUserRoleUpdateById(data) {
    return request({
        url: '/projectUserRole/updateById',
        method: 'post',
        data
    })
}
//判断当前用户是否是项目空间管理员
export function projectUserRoleJudgeManager(data) {
    return request({
        url: '/projectUserRole/judgeManager',
        method: 'get',
        params: data
    })
}
//根据用户名或员工编号查询用户信息
export function queryListByNameAndEmployeeNumber(data) {
    return request({
        url: '/web/auth/queryListByNameAndEmployeeNumber',
        method: 'get',
        params: data
    })
}