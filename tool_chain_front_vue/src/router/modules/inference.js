import BasicLayout from '@/layouts/basic-layout';

const meta = {
    auth: true
};

export default {
    path: '/inference',
    meta,
    component: BasicLayout,
    redirect: { name: 'data' },
    children: [
        {
            path: 'deploy',
            name: 'deploy',
            meta: {
                ...meta,
                title: '模型部署',
                closable: true
            },
            component: () => import('@/pages/inference/deploy')
        },
        {
            path: 'deployServer',
            name: 'deployServer',
            meta: {
                ...meta,
                title: '模型部署服务器选择'
            },
            component: () => import('@/pages/inference/deployServer')
        }
    ]
};
