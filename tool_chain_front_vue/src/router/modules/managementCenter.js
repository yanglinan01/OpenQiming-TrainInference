import BasicLayout from '@/layouts/basic-layout';

const meta = {
    auth: true
};

export default {
    path: '/managementCenter',
    meta,
    component: BasicLayout,
    redirect: { name: 'data' },
    children: [
        {
            path: 'data',
            name: 'data',
            meta: {
                ...meta,
                title: '数据管理',
                closable: true
            },
            component: () => import('@/pages/managementCenter/data')
        },
        {
            path: 'dataset',
            name: 'dataset',
            meta: {
                ...meta,
                title: '创建数据集',
                closable: true
            },
            component: () => import('@/pages/managementCenter/dataset')
        },
        {
            path: 'tagging',
            name: 'tagging',
            meta: {
                ...meta,
                title: '数据标注',
                closable: true
            },
            component: () => import('@/pages/managementCenter/tagging')
        },
        {
            path: 'datasetDetails',
            name: 'datasetDetails',
            meta: {
                ...meta,
                title: '数据集详情',
                closable: true
            },
            component: () => import('@/pages/managementCenter/datasetDetails')
        },

    ]
};
