
import Vue from 'vue'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import locale from 'element-ui/lib/locale/lang/en' // lang i18n

import '@/styles/index.scss' // global css

import 'element-ui/lib/theme-chalk/display.css';

import App from './App'
import store from './store'
import router from './router'

import echarts from 'echarts'
import 'echarts/map/js/china'

import '@/icons' // icon
import '@/permission' // permission control

import VueClipboard from "vue-clipboard2";
import permission from './directive/permission/index'//引入权限控制组件

import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'

/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online! ! !
 */

// set ElementUI lang to EN
// Vue.use(ElementUI, { locale })
// 如果想要中文版 element-ui，按如下方式声明
Vue.use(ElementUI)

Vue.use(VueClipboard)

Vue.use(permission)

Vue.use(mavonEditor)

Vue.prototype.$echarts = echarts

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
