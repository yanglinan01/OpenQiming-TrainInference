export default {
    path: '/systemManage',
    title: '系统管理',
    header: 'system-setting',
    icon: 'md-contact',
    children: [
        {
            path: `/userManage/userManageWeb`,
            title: '用户管理'
        },
        {
            path: `/userManage/baseModelManage`,
            title: '模型管理'
        },
    ]
}
