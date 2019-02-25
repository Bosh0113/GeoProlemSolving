// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import Vuex from 'vuex';
import App from './App'
import router from './router'
import iView from 'iview'
import axios from 'axios'
import VueAxios from 'vue-axios'
import $ from 'jquery'
import 'iview/dist/styles/iview.css'
import store from './store/store.js'
//引入index.js文件夹
// import './mock/index'
//引入vue-charts
import VCharts from 'v-charts'
import * as socketApi from './api/socket'
import MuseUI from 'muse-ui'
import 'muse-ui/dist/muse-ui.css'
//引入vue-clipboards组件
import VueClipboards from 'vue-clipboards'

//leaflet
import L from 'leaflet'
import 'leaflet/dist/leaflet.css'
import VueDraggable from 'vue-draggable'
// this part resolve an issue where the markers would not appear
delete L.Icon.Default.prototype._getIconUrl;
//请求带上cookie以防session丢失
axios.defaults.withCredentials=true;

L.Icon.Default.mergeOptions({
  iconRetinaUrl: require('leaflet/dist/images/marker-icon-2x.png'),
  iconUrl: require('leaflet/dist/images/marker-icon.png'),
  shadowUrl: require('leaflet/dist/images/marker-shadow.png')
});

Vue.prototype.socketApi = socketApi
Vue.config.productionTip = false
Vue.use(iView)
Vue.use(Vuex)
Vue.use(VCharts)
Vue.use(MuseUI)
// 使用vue-clipboards
Vue.use(VueClipboards);

Vue.use(VueDraggable)

Vue.use(VueAxios,axios)
axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';
Vue.prototype.$axios = axios;
// Vue.prototype.socketApi = socketApi;
//mock.js的配置
// process.env.MOCK && require('@/mock')

/* eslint-disable no-new */
// 实例化对象
new Vue({
  el: '#app',
  store,
  router,
  components: { App },
  template: '<App/>'
})
