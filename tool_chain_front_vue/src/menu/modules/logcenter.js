const pre = '/logCenter/';

export default {
    path: '/logCenter',
    title: '日志中心',
    header: 'system-setting',
    icon: 'ios-paper',
    children: [
        {
            path: `${pre}modelMonitoring`,
            title: '模型监控'
        },
    ]
}
