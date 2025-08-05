import request from '@/plugins/request';
//分页查询日志[rows:数据清单,total:总条数]
export function queryPage(data) {
    return request({
        url: '/log/queryPage',
        method: 'post',
        data
    });
}
