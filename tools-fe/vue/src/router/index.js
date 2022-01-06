import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

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
    icon: 'svg-name'             the icon show in the sidebar
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
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    path: '/music',
    component: () => import('@/views/music/index'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '看板', icon: 'dashboard' }
    }]
  }
]

export const asyncRoutes = [
  {
    path: '/shortUrl',
    component: Layout,
    redirect: '/shortUrl/query',
    meta: {
      title: '短链接服务',
      icon: 'global',
      roles: ['user','admin']
    },
    children: [{
      path: 'add',
      name: 'add',
      component: () => import('@/views/shortUrl/index'),
      meta: {
        title: '短链接生成',
        icon: 'url',
        roles: ['user','admin']
      }
    },
      {
        path: 'query',
        name: 'query',
        component: () => import('@/views/shortUrl/query'),
        meta: {
          title: '短链接管理',
          icon: 'search',
          roles: ['user','admin']
        }
      },
      {
        path: 'statistics/:id',
        name: 'statistics',
        hidden: true,
        component: () => import('@/views/shortUrl/statistics'),
        meta: {
          title: '链接统计',
          icon: 'search',
          roles: ['user','admin']
        }
      }]
  },
  {
    path: '/file',
    component: Layout,
    redirect: '/file/imagelist',
    meta: {
      title: '文件管理',
      icon: 'file',
      roles: ['user','admin']
    },
    children: [{
      path: 'imagelist',
      name: 'imageList',
      component: () => import('@/views/file/imageList'),
      meta: {
        title: '图床',
        icon: 'image',
        roles: ['user','admin']
      }
    },
      {
        path: 'fileList',
        name: 'fileList',
        component: () => import('@/views/file/fileList'),
        meta: {
          title: '网盘',
          icon: 'disk',
          roles: ['user','admin']
        }
      }]
  },
  {
    path: '/blog',
    component: Layout,
    redirect: '/blog/list',
    meta: {
      title: '博客管理',
      icon: 'blog',
      roles: ['editor', 'admin']
    },
    children: [{
      path: 'list',
      name: 'articleList',
      component: () => import('@/views/blog/index'),
      meta: {
        title: '文章列表',
        icon: 'article',
        roles: ['editor', 'admin']
      }
    },
      {
        path: 'edit',
        name: 'editArticle',
        component: () => import('@/views/blog/edit'),
        hidden: true,
        meta: {
          title: '编辑文章',
          icon: 'create-course',
          roles: ['editor', 'admin']
        }
      },
      {
        path: 'add',
        name: 'addArticle',
        component: () => import('@/views/blog/edit'),
        meta: {
          title: '新建文章',
          icon: 'create-course',
          roles: ['editor', 'admin']
        }
      },
      {
        path: 'friends',
        name: 'friendsLink',
        component: () => import('@/views/blog/friends'),
        meta: {
          title: '友链管理',
          icon: 'friend-link',
          roles: ['user', 'admin']
        }
      },
      {
        path: 'timeline',
        name: 'timeline',
        component: () => import('@/views/blog/timeline'),
        meta: {
          title: '博客大事件',
          icon: 'timeline',
          roles: ['admin']
        }
      },
    ]
  },
  {
    path: '/constant',
    component: Layout,
    redirect: '/constant/query',
    children: [{
      path: 'query',
      name: 'constantQuery',
      component: () => import('@/views/constant/query'),
      meta: {
        title: '全局变量管理',
        icon: 'setting',
        roles: ['admin']
      }
    }]
  },
  {
    path: '/api',
    component: Layout,
    redirect: '/api/query',
    children: [{
      path: 'query',
      name: 'akQuery',
      component: () => import('@/views/apikey/index'),
      meta: {
        title: 'API管理',
        icon: 'function',
        roles: ['user','admin']
      },
    }]
  },
  {
    path: '/user/info',
    component: Layout,
    redirect: '/user/info/edit',
    hidden: true,
    children: [{
      path: 'edit',
      name: 'userInfoEdit',
      component: () => import('@/views/user/info'),
      meta: {
        title: '个人信息',
        icon: 'user',
        roles: ['user','admin']
      }
    }]
  },
  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },
  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  mode: 'history', // require service support
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
