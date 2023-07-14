import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/* Router Modules */
// import componentsRouter from './modules/components'
// import chartsRouter from './modules/charts'
// import tableRouter from './modules/table'
// import nestedRouter from './modules/nested'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    noCache: true                if set true, the page will no be cached(default is false)
    affix: true                  if set true, the tag will affix in the tags-view
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect/index')
      }
    ]
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('@/views/login/index'),
    // component: () => import('@/views/profile/index'),
    hidden: true
  },
  {
    path: '/auth-redirect',
    name: 'auth-redirect',
    component: () => import('@/views/login/auth-redirect'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        component: () => import('@/views/dashboard/index'),
        name: '首页',
        meta: { title: '首页', icon: 'dashboard' }
      }
    ]
  }
]

/**
 * asyncRoutes
 * the routes that need to be dynamically loaded based on user roles
 */
export const asyncRoutes = [
  {
    path: '/settings',
    component: Layout,
    meta: {
      title: '基础配置', icon: 'nested',
      roles: ['超级管理员', '库存管理员', '出入库管理员']
    },
    children: [
      {
        path: 'system',
        component: () => import('@/views/settings/system/index'), // Parent router-view
        meta: { title: '系统设定', roles: ['超级管理员'] },
        children: [
          {
            path: 'user',
            component: () => import('@/views/settings/system/user'),
            meta: { title: '用户管理', roles: ['超级管理员'] }
          },
          {
            path: 'role',
            component: () => import('@/views/settings/system/role'),
            meta: { title: '角色设定', roles: ['超级管理员'] }
          }
        ]
      },
      {
        path: 'warehouse',
        // component: Layout,
        component: () => import('@/views/settings/warehouse/index'), // Parent router-view
        meta: { title: '仓库设定', roles: ['超级管理员', '库存管理员', '出入库管理员'] },
        children: [
          {
            path: 'area',
            component: () => import('@/views/settings/warehouse/area'),
            meta: { title: '货区管理', roles: ['超级管理员', '库存管理员', '出入库管理员'] }
          },
          {
            path: 'shelve',
            component: () => import('@/views/settings/warehouse/shelve'),
            meta: { title: '货架管理', roles: ['超级管理员', '库存管理员', '出入库管理员'] }
          },
          {
            path: 'goods',
            component: () => import('@/views/settings/warehouse/goods'),
            meta: { title: '货物管理', roles: ['超级管理员', '库存管理员', '出入库管理员'] }
          },
          {
            path: 'supplier',
            component: () => import('@/views/settings/warehouse/supplier'),
            meta: { title: '供应商管理', roles: ['超级管理员', '库存管理员', '出入库管理员'] }
          },
          {
            path: 'department',
            component: () => import('@/views/settings/warehouse/department'),
            meta: { title: '科室设定', roles: ['超级管理员', '库存管理员', '出入库管理员'] }
          },
          {
            path: 'logistics',
            component: () => import('@/views/settings/warehouse/logistics'),
            meta: { title: '物流设定', roles: ['超级管理员', '库存管理员', '出入库管理员'] }
          }
        ]
      }] },
  {
    path: '/procure',
    component: Layout,
    meta: {
      title: '采购管理', roles: ['超级管理员', '库存管理员', '审批人员', '医院调拨员'], icon: 'tree'
    },
    children: [
      {
        path: 'order',
        component: () => import('@/views/procure/order'), // Parent router-view
        meta: { title: '创建采购单', roles: ['超级管理员', '库存管理员', '医院调拨员'] }
      },
      {
        path: 'search',
        name: 'scanPurchaseOrder',
        component: () => import('@/views/procure/search'),
        meta: { title: '查看采购单', roles: ['超级管理员', '库存管理员', '审批人员', '医院调拨员'] }
      },
      {
        path: 'approve',
        component: () => import('@/views/procure/approve'),
        meta: { title: '审批', roles: ['超级管理员', '审批人员'] }
      },
      {
        path: 'reserve',
        component: () => import('@/views/procure/reserve'),
        meta: { title: '预约入库', roles: ['超级管理员', '库存管理员'] }
      }
    ]
  },
  {
    path: '/inStorage',
    component: Layout,
    meta: {
      title: '入库管理', icon: 'table',
      roles: ['超级管理员', '库存管理员', '出入库管理员', '审批人员', '工作人员']
    },
    children: [
      {
        path: 'create',
        component: () => import('@/views/inStorage/create'),
        meta: { title: '创建入库单', roles: ['超级管理员', '库存管理员', '出入库管理员'] }
      },
      {
        path: 'search',
        name: 'scanInOrder',
        component: () => import('@/views/inStorage/search'),
        meta: { title: '查看入库单', roles: ['超级管理员', '库存管理员', '出入库管理员', '审批人员'] }
      },
      {
        path: 'preInspection',
        component: () => import('@/views/inStorage/preInspection'),
        meta: { title: '预检入库', roles: ['超级管理员', '审批人员'] }
      },
      {
        path: 'confirmReceipt',
        component: () => import('@/views/inStorage/confirmReceipt'),
        meta: { title: '确认收货', roles: ['超级管理员', '审批人员'] }
      },
      {
        path: 'onShell',
        component: () => import('@/views/inStorage/onShell'),
        meta: { title: '上架', roles: ['超级管理员', '出入库管理员', '工作人员'] }
      }
    ]
  },
  {
    path: '/transfer',
    component: Layout,
    meta: {
      title: '内配管理', roles: ['超级管理员', '库存管理员', '审批人员', '工作人员'], icon: 'edit'
    },
    children: [
      {
        path: 'createTransfer',
        name: 'createTransfer',
        component: () => import('@/views/transfer/create'), // Parent router-view
        hidden: true,
        meta: { title: '创建内配单', roles: ['超级管理员', '库存管理员'] }
      },
      {
        path: 'searchTransfer',
        component: () => import('@/views/transfer/search'),
        meta: { title: '查看内配单', roles: ['超级管理员', '库存管理员', '审批人员'] }
      },
      {
        path: 'approveTransfer',
        component: () => import('@/views/transfer/approve'),
        meta: { title: '审批', roles: ['超级管理员', '审批人员'] }
      },
      {
        path: 'finishTransfer',
        component: () => import('@/views/transfer/finish'),
        meta: { title: '确认完成', roles: ['超级管理员', '库存管理员', '工作人员'] }
      }
    ]
  },
  {
    path: '/out',
    component: Layout,
    meta: {
      title: '出库管理', roles: ['超级管理员', '库存管理员', '出入库管理员', '审批人员', '工作人员', '医院调拨员'], icon: 'el-icon-s-home'
    },
    children: [
      {
        path: 'createOut',
        component: () => import('@/views/out/create'), // Parent router-view
        meta: { title: '调拨创建', roles: ['超级管理员', '库存管理员', '出入库管理员', '医院调拨员'] }
      },
      {
        path: 'searchOut',
        component: () => import('@/views/out/search'),
        meta: { title: '查看出库单', roles: ['超级管理员', '库存管理员', '出入库管理员', '审批人员'] }
      },
      {
        path: 'handleOut',
        component: () => import('@/views/out/handle'),
        meta: { title: '操作', roles: ['超级管理员', '库存管理员', '出入库管理员', '审批人员', '工作人员'] }
      },
      {
        path: 'finishOut',
        component: () => import('@/views/out/finish'),
        meta: { title: '完成确认', roles: ['超级管理员', '出入库管理员'] }
      }
    ]
  },
  {
    path: '/check',
    component: Layout,
    meta: {
      title: '盘点管理', icon: 'search',
      roles: ['超级管理员', '库存管理员', '出入库管理员', '工作人员', '审批人员']
    },
    children: [
      {
        path: 'publishOrder',
        component: () => import('@/views/check/publishOrder'),
        meta: { title: '创建盘点任务', roles: ['超级管理员', '库存管理员'] }
      },
      {
        path: 'confirmReceipt',
        component: () => import('@/views/check/count'),
        meta: { title: '上传盘点结果', roles: ['超级管理员', '工作人员'] }
      },
      {
        path: 'onShell',
        component: () => import('@/views/check/examine'),
        meta: { title: '审查盘点任务', roles: ['超级管理员', '审批人员'] }
      },
      {
        path: 'statistics',
        component: () => import('@/views/check/statistics'),
        meta: { title: '统计', roles: ['超级管理员', '库存管理员', '出入库管理员'] }
      }
    ]
  },
  {
    path: '/warning',
    component: Layout,
    meta: {
      title: '预警管理', icon: 'el-icon-warning',
      roles: ['超级管理员', '库存管理员', '出入库管理员']
    },
    children: [
      {
        path: 'rule',
        component: () => import('@/views/warning/rule'),
        meta: { title: '预警规则', roles: ['超级管理员', '库存管理员', '出入库管理员'] }
      },
      {
        path: 'record',
        component: () => import('@/views/warning/record'),
        meta: { title: '预警记录', roles: ['超级管理员', '库存管理员', '出入库管理员'] }
      }
    ]
  },
  {
    path: '/visual',
    component: Layout,
    meta: {
      title: '仓库可视化', icon: 'nested',
      roles: ['超级管理员', '库存管理员', '出入库管理员', '工作人员']
    },
    children: [
      {
        path: 'new',
        // hidden: true,
        component: () => import('@/views/visual/new'),
        meta: { title: '仓库可视化', roles: ['超级管理员', '库存管理员', '出入库管理员', '工作人员'] }
      }
    ]
  }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
