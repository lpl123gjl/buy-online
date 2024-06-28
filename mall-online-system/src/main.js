import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui'
import "element-ui/lib/theme-chalk/index.css"
import myaxios from '@/api'
import qs from 'qs'
import global from './globalFun'

import {
  getCurDate,
  setSessionStorage,
  getSessionStorage,
  removeSessionStorage,
  setLocalStorage,
  getLocalStorage,
  removeLocalStorage
} from './common.js';
import filter from './filter';

Object.keys(filter).forEach((key,index)=>{
  Vue.filter(key,filter[key]);
})

Vue.config.productionTip = false
Vue.use(ElementUI)

Vue.prototype.$axios = myaxios;
Vue.prototype.$qs = qs

Vue.prototype.$getCurDate = getCurDate;
Vue.prototype.$setSessionStorage = setSessionStorage;
Vue.prototype.$getSessionStorage = getSessionStorage;
Vue.prototype.$removeSessionStorage = removeSessionStorage;
Vue.prototype.$setLocalStorage = setLocalStorage;
Vue.prototype.$getLocalStorage = getLocalStorage;
Vue.prototype.$removeLocalStorage = removeLocalStorage;



new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
