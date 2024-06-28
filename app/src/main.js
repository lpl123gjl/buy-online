//入口文件，项目中需要用到所有插件和库，都在此导入
import Vue from 'vue'
import App from './App.vue'
import { name } from './base.js'
import {
    getCurDate,
    setSessionStorage,
    getSessionStorage,
    removeSessionStorage,
    setLocalStorage,
    getLocalStorage,
    removeLocalStorage
} from './common.js';
import 'font-awesome/css/font-awesome.min.css'
//如果导入的文件名是index.js可以省略，书写为'./router'
import router from './router/index.js'
import VueRouter from 'vue-router'
import axios from 'axios';
import qs from 'qs';
import store from './store';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css'
import filter from './filter';
import BaiduMap from 'vue-baidu-map'
//您的百度地图API密钥
Vue.use(BaiduMap, { ak: 'DJFsnZFHw4E0mLGWYiH8l869oB99MFFm' })

//安装插件要生效，必须使用
Vue.use( VueRouter)
Vue.use(ElementUI)
Object.keys(filter).forEach((key,index)=>{
    Vue.filter(key,filter[key]);
})


setSessionStorage("name", name);
axios.defaults.baseURL='/sys'
 
//将导入的公共方法，挂在到Vue实例上。
Vue.prototype.$axios = axios;
Vue.prototype.$qs = qs;
Vue.prototype.$getCurDate = getCurDate;
Vue.prototype.$setSessionStorage = setSessionStorage;
Vue.prototype.$getSessionStorage = getSessionStorage;
Vue.prototype.$removeSessionStorage = removeSessionStorage;
Vue.prototype.$setLocalStorage = setLocalStorage;
Vue.prototype.$getLocalStorage = getLocalStorage;
Vue.prototype.$removeLocalStorage = removeLocalStorage;
//生产信息的提示，关闭掉
Vue.config.productionTip = false


router.beforeEach((to,from,next)=>{
    let account = getSessionStorage('account');
    if(to.path=='/buycar' || to.path=='/my' || to.path=='/orderlist'){
        if(account==null){
            router.push('/Login').catch((e)=>{})
        }
    }
        next()  
})



//入口文件中定义一个Vue实例。vm
new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app')