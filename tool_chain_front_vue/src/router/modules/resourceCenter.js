import BasicLayout from '@/layouts/basic-layout';

const meta = {
    auth: true
};


export default {
    path: '/resource',
    meta,
    component: BasicLayout,
    redirect: { name: 'manage' },
    children: [
        {
            path: 'manage',
            name: 'manage',
            meta: {
                ...meta,
                title: '资源中心',
                closable: true
            },
            component: () => import('@/pages/resourceCenter/manage')
        },
    ]
};
