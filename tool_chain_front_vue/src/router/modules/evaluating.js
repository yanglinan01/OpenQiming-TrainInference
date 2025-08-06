import BasicLayout from '@/layouts/basic-layout';

const meta = {
    auth: true
};

export default {
    path: '/evaluating',
    meta,
    component: BasicLayout,
    redirect: { name: 'index' },
    children: [
        {
            path: 'index',
            name: 'index',
            meta: {
                ...meta,
                title: '大模型评测',
                closable: true
            },
            component: () => import('@/pages/evaluating/indexCopy')
        },
        {
            path: 'evaluaDetail',
            name: 'evaluaDetail',
            meta: {
                ...meta,
                title: '模型评估',
                closable: true
            },
            component: () => import('@/pages/evaluating/evaluaDetail')
        },
    ]
};
