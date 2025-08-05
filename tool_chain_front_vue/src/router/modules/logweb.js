import BasicLayout from "@/layouts/basic-layout";

const meta = {
    auth: true
};

export default {
    path: '/logpage',
    meta,
    component: BasicLayout,
    redirect: { name: 'logweb' },
    children: [
        {
            path: 'logweb',
            name: 'logweb',
            meta: {
                ...meta,
                title: '操作日志',
                closable: false
            },
            component: () => import('@/pages/logpage/logweb')
        }
    ]

};
