import BasicLayout from '@/layouts/basic-layout';

const meta = {
    auth: true
};

export default {
    path: '/Home',
    meta,
    component: BasicLayout,
    children: [
        {
            path: '/home',
            name: 'home',
            meta: {
                ...meta,
                title: '首页',
                closable: false
            },
            component: () => import('@/pages/home')
        }
    ]
};
