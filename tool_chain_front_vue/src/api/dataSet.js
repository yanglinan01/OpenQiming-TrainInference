import request from '@/plugins/request';

//查询dataSet列表
export function queryList(data) {
    return request({
        url: '/dataSet/queryList',
        method: 'get',
        params: data
    })
}
//分页查询dataSet列表
export function queryPage(data) {
    return request({
        url: '/dataSet/queryPage',
        method: 'get',
        params: data
    })
}
//删除DataSet
export function deleteById(data) {
    return request({
        url: '/dataSet/deleteById',
        method: 'get',
        params: data
    });
}
//查询DataSet详情
export function queryById(data) {
    return request({
        url: '/dataSet/queryById',
        method: 'get',
        params: data
    })
}
//下载问答对xlsx模版
export function download(data) {
    return request({
        url: '/dataSet/downloadTemp',
        method: 'get',
        responseType: 'blob',
        params: data
    })
}
//上传问答对文件
export function uploadTemp(data) {
    return request({
        url: '/dataSet/uploadTemp',
        method: 'post',
        data
    })
}

//新增DataSet
export function add(data) {
    return request({
        url: '/dataSet/add',
        method: 'post',
        data
    })
}
//取消新增数据集
export function cancelAddDataSet(data) {
    return request({
        url: '/dataSet/cancelAddDataSet',
        method: 'get',
        params: data
    })
}
//取消单个数据集文件
export function deleteByDataSetFileId(data) {
    return request({
        url: '/dataSet/deleteByDataSetFileId',
        method: 'get',
        params: data
    })
}
//修改DataSet
export function updateData(data) {
    return request({
        url: 'dataSet/update',
        method: 'post',
        data
    })
}
//获取当前用户远程数据集列表
export function getDocList() {
    return request({
        url: 'dataSet/getDocList',
        method: 'get'
    })
}

//新增知识库数据集
export function addFromKnowledgeBase(data) {
    return request({
        url: 'dataSet/addFromKnowledgeBase',
        method: 'post',
        data
    })
}
