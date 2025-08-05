import request from '@/plugins/request';

//获取分页查询的问答对测试数据集评估列表信息
export function queryPageList(data) {
    return request({
        url: '/prTestSetEvaluation/queryPage',
        method: 'post',
        data
    });
}
//删除问答对测试数据集评估
export function deleteById(data) {
    return request({
        url: '/prTestSetEvaluation/deleteById',
        method: 'get',
        params: data
    })
}

//获取分页查询的问答对测试数据集评估详情列表信息
export function queryPageDetail(data) {
    return request({
        url: '/prTestSetEvaluationDetail/queryPage',
        method: 'post',
        data
    });
}
//点赞、点踩问答对测试数据集评估详情
export function action(data) {
    return request({
        url: '/prTestSetEvaluationDetail/action',
        method: 'get',
        params: data
    });
}

//导出问答对测试数据集评估详情
export function exportTable(data) {
    return request({
        url: '/prTestSetEvaluation/export',
        method: 'get',
        responseType: 'blob',
        params: data
    })
}

//创建问答对测试数据集评估
export function addEvaluation(data) {
    return request({
        url: '/prTestSetEvaluation/add',
        method: 'post',
        data
    });
}
//查询评估详情
export function getLastEvalByTrainTaskId(data) {
    return request({
        url: '/trainTaskEval/getLastEvalByTrainTaskId',
        method: 'get',
        params: data
    });
}

//开始eval评估
export function trainTaskEvalAdd(data) {
    return request({
        url: '/trainTaskEval/add',
        method: 'post',
        data
    });
}
//问答对测试数据集评估反馈是否完成
export function isComplete(data) {
    return request({
        url: '/prTestSetEvaluationDetail/isComplete',
        method: 'get',
        params: data
    })
}
//文件上传操作
export function upload(data) {
    return request({
        url: '/prTestSetEvaluation/upload',
        method: 'post',
        data
    })
}
//导出强化问答对测试数据集为文件
export function exportToFile(id) {
    return request({
        url: '/prTestSetEvaluation/exportToFile/'+id,
        method: 'get'
    })
}
