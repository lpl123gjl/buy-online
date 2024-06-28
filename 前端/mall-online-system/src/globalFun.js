import Vue from 'vue'
//mixin作用就是多个组件可以共享数据和方法。mixin中定义的方法，
//会并入到其它所有的组件中，可以直接使用
Vue.mixin({
    //验证权限的方法
    methods:{
        //参数：perm验证的权限编码  sys:user:save
        hasAuth(perm){
            //获得当前登录用户的权限列表
            let authority = this.$store.state.menus.permList
            return authority.indexOf(perm) > -1;
        }
    }
})