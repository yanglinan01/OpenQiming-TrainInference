import request from '@/plugins/request';

//获取公告内容
export function getAnnouncementContent(data) {
    return request({
        url: '/web/announcement/getAnnouncementContent',
        method: 'get',
        params: data
    })
}
