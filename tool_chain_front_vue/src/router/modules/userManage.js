import BasicLayout from "@/layouts/basic-layout";

const meta = {
    auth: true
};

export default {
    path: '/userManage',
    meta,
    component: BasicLayout,
    redirect: { name: 'userManageWeb' },
    children: [
        {
            path: 'userManageWeb',
            name: 'userManageWeb',
            meta: {
                ...meta,
                title: '用户管理',
                closable: false
            },
            component: () => import('@/pages/userManage/userManageWeb')
        },
        {  path: 'permissionAssignment',
            name: 'permissionAssignment',
            meta: {
                ...meta,
                title: '权限分配',
                closable: true
            },
            component: () => import('@/pages/userManage/permissionAssignment')
        },
        {
            path: 'baseModelManage',
            name: 'baseModelManage',
            meta: {
                ...meta,
                title: '模型管理'
            },
            component: () => import('@/pages/userManage/baseModelManage')
        }
    ]

};
