import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken } from '@/utils/auth' // get token from cookie
import getPageTitle from '@/utils/get-page-title'
import ro from "element-ui/src/locale/lang/ro";
import {baseURL} from "./utils/request";
import {login} from "./api/user";
import {setToken} from "./utils/auth";

NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = ['/music', '/404'] // no redirect whitelist

router.beforeEach(async (to, from, next) => {
  // start progress bar
  NProgress.start()

  // set page title
  document.title = getPageTitle(to.meta.title)

  // determine whether the user has logged in
  const hasToken = getToken()

  if (hasToken) {
    if (to.path === '/login') {
      // if is logged in, redirect to the home page
      next({ path: '/' })
      NProgress.done()
    } else {
        // determine whether the user has obtained his permission roles through getInfo
        const hasRoles = store.getters.roles && store.getters.roles.length > 0
        if (hasRoles) {
          next()
        } else {
          try {

            // get user info
            // note: roles must be a object array! such as: ['admin'] or ,['developer','editor']
            const {
              roles
            } = await store.dispatch('user/getInfo')

            //
            // generate accessible routes map based on roles
            const accessRoutes = await store.dispatch('permission/generateRoutes', JSON.parse(roles))

            // dynamically add accessible routes
            router.addRoutes(accessRoutes)
            //
            //           // hack method to ensure that addRoutes is complete
            //           // set the replace: true, so the navigation will not leave a history record
            next({
              ...to,
              replace: true
            })
          } catch (error) {
            // remove token and go to login page to component-login
            await store.dispatch('user/resetToken')
            Message.error(error || 'Has Error')
            next(`/login?redirect=${to.path}`)
            NProgress.done()
          }
        }
      }
  } else {
    /* has no token */

    if (whiteList.indexOf(to.path) !== -1) {
      // in the free login whitelist, go directly
      next()
    } else {
      console.log('前往登录', from, to)
      if (to.query && to.query.ticket) {
        await login({ticket : to.query.ticket}).then(res => {
          setToken(res.data.token)
        })
        next(to.query.redirect ? to.query.redirect : '/')
      } else {
        window.location.href = `https://sso.edchu.cn?redirect=${to.path}&login=https://admin.edchu.cn/login`
      }
      // other pages that do not have permission to access are redirected to the login page.
      // next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
