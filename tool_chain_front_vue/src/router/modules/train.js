import BasicLayout from '@/layouts/basic-layout';

const meta = {
    auth: true
};

export default {
    path: '/train',
    meta,
    component: BasicLayout,
    redirect: { name: 'tasks' },
    children: [
        {
            path: 'tasks',
            name: 'tasks',
            meta: {
                ...meta,
                title: '模型训练',
                closable: true
            },
            component: () => import('@/pages/train/tasks')
        },
        {
            path: 'parameters',
            name: 'parameters',
            meta: {
                ...meta,
                title: '创建训练任务'
            },
            component: () => import('@/pages/train/parameters')
        },
        {
            path: 'parametersDemo',
            name: 'parametersDemo',
            meta: {
                ...meta,
                title: '创建大训练任务'
            },
            component: () => import('@/pages/train/parametersDemo')
        },
        {
            path: 'tasksDemo',
            name: 'tasksDemo',
            meta: {
                ...meta,
                title: '大模型训练',
                closable: true
            },
            component: () => import('@/pages/train/tasksDemo')
        }
    ]
};
