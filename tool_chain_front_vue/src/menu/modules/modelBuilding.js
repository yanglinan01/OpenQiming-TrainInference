export default {
    path: '/modelBuilding',
    title: '模型构建',
    header: 'system-setting',
    icon: 'md-build',
    children: [
        {
            path: `/train/tasks`,
            title: '模型训练'
        },
        {
            path: `/evaluating/index`,
            title: '模型测评'
        },
    ]
}
