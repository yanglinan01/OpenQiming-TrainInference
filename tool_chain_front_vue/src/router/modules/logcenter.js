import BasicLayout from '@/layouts/basic-layout';

const meta = {
    auth: true
};

export default {
    path: '/logCenter',
    meta,
    component: BasicLayout,
    redirect: { name: 'modelMonitoring' },
    children: [
        {
            path: 'modelMonitoring',
            name: 'modelMonitoring',
            meta: {
                ...meta,
                title: '模型监控',
                closable: true
            },
                component: () => import('@/pages/logCenter/modelMonitoring')
        },
        {
            path: 'logDetail',
            name: 'logDetail',
            meta: {
                ...meta,
                title: '调用日志',
                closable: true
            },
            component: () => import('@/pages/logCenter/logDetail')
        },
        {
            path: 'modelMonitor',
            name: 'modelMonitor',
            meta: {
                ...meta,
                title: '模型监测',
                closable: true
            },
            component: () => import('@/pages/logCenter/modelMonitor')
        },
        {
            path: 'modelMonitorList',
            name: 'modelMonitorList',
            hidden: true,
            meta: {
                ...meta,
                title: '模型监测数据列表',
                closable: true
            },
            component: () => import('@/pages/logCenter/modelMonitorList')
        },
        {
            path: 'businessMonitor',
            name: 'businessMonitor',
            meta: {
                ...meta,
                title: '业务监测',
                closable: true
            },
            component: () => import('@/pages/logCenter/businessMonitor')
        }
    ]
};
