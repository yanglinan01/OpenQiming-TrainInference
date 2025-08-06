const pre = '/managementCenter/';

export default {
    path: '/managementCenter',
    title: '数据中心',
    header: 'system-setting',
    icon: 'md-cog',
    children: [
        {
            path: `${pre}data`,
            title: '数据集管理'
        },
    ]
}
