import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '@/views/Login.vue'
import Home from '@/views/Home.vue'
import NotFound from '@/views/NotFound.vue'
import axios from '@/api/index.js'
import store from '@/store'


Vue.use(VueRouter)



const routes = [
  {
    path: '/',
    name: 'home',
    component: Home,
    children:[
      {
        path:'/index',
        name:'Index',
        meta:{
          title:'首页',
          icon:'el-icon-s-home',
        },
        component: ()=> import("@/views/Index.vue")
      },
      {
        path: '/setpwd',
        name: 'setpwd',
        meta:{
          title:'修改密码',
          icon:'el-icon-lock',
        },
        component: ()=> import("@/views/SetPwd.vue")
      },
      
    ]
  },
  
  {
    path: '/login',
    name: 'login',
    component: Login
  },
  {
    path: '/notfound',
    name: 'NotFound',
    component: NotFound
  },

]

const router = new VueRouter({
  routes
})

//设置路由守卫
router.beforeEach((to, from, next) => {
  //菜单和路由是否配置加载过
  let hasRoute = store.state.menus.hasRoute
  let menus = store.state.menus.menuList
  //路由守卫会拦截所有路由跳转，需要对跳转路径进行过滤。
  if (to.path == '/login') {
    next(); //如果是跳转是登录页面，就放行
  }else if(to.path =='/' || to.path ==''){
    next('/index')
  }
  else if (!hasRoute) { //hasRoute是false，表示第一次加载和配置菜单和路由
    let JSONRoutes = router.options.routes;
    console.log("加载和配置菜单路由---");
    axios.get('/menu/nav').then(response => {
      //使用vuex调用SET_MENULIST的方法存储菜单:
      console.log('222',response.data.resultdata.nav);
      store.commit('SET_MENULIST', response.data.resultdata.nav)
      store.commit('SET_PERMLIST', response.data.resultdata.authoritys)
      //修改hasRoute将设置True，表示已经加载配置过菜单
      store.commit('SET_HASROUTE', true);

      response.data.resultdata.nav.forEach(menu=>{
        if(menu.children){
          menu.children.forEach(child=>{
            let route = menuToRoute(child)
            if(route){
              JSONRoutes[0].children.push(route)
            }
          })
        }
      })
      
      router.matcher = new VueRouter().matcher
      router.addRoutes(JSONRoutes)
      next({ path: to.path })
    }).catch(e => { console.log(e); })
    
  } else {
    console.log("菜单和路由配置加载过了---");
    next();
  }
})

const menuToRoute = (child)=>{
  if(!child.component){
    return null
  }
  let route = {
    path: child.path,
    name: child.name,
    meta:{
      icon:child.icon,
      title:child.title,
    }
  }
  route.component = () => import("@/views/"+child.component+".vue");
  return route;
}

export default router
