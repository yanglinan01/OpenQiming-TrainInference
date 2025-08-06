import provinceRequest from '@/plugins/request/provinceIndex';


//分页查询插件列表
export function pluginQueryList() {
    return provinceRequest({
        url: '/console/api/workspaces/current/tool-providers',
        method: 'get'
    })
}

//新增插件
export function pluginAdd(data) {
    return provinceRequest({
        url: 'plugin/add',
        method: 'post',
        data
    });
}