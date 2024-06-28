<template>
    <el-menu class="el-menu-vertical-demo" active-text-color="#2ed2dd" text-color="#333" background-color="#fff">
            <router-link :to="'/index'">
                <el-menu-item index="Index">
                <template slot="title">
                    <i class="el-icon-s-home"></i>
                    <span>首页</span>
                </template>
            </el-menu-item>
            </router-link>
            
            <!-- 系统管理菜单 -->
            <el-submenu :index="item.name" v-for="(item,index) in menus" :key="index">
                <template slot="title">
                    <i :class="item.icon"></i>
                    <span>{{ item.title }}</span>
                </template>
                <!-- 系统管理子菜单 -->
                <router-link :to="childrenItem.path" v-for="(childrenItem,childrenIndex) in item.children" :key="childrenIndex">
                    <el-menu-item :index="childrenItem.name" @click="selectMenu(childrenItem)">
                        <template slot="title">
                            <i :class="childrenItem.icon"></i>
                            <span>{{ childrenItem.title }}</span>
                        </template>
                    </el-menu-item>
                </router-link>
            </el-submenu>
    </el-menu>
</template>


<script>
export default {
    data() {
        return {
            menus:[]
        }
    },
    components: {

    },
    created(){
        this.menus = JSON.parse(sessionStorage.getItem('menuList'))
    },
    methods: {
        selectMenu(child){
            let tab = {
                title:child.title,
                name:child.name,
                icon:child.icon,
                closable:true
            }
            this.$store.commit('ADD_TABS',tab);
        }
    },
}

</script>
<style scoped>
    .el-menu{
        height: 100%;
        
    }
    a{
        text-decoration: none;
    }
    .el-menu-item{
        border-right: solid 1px #e6e6e6;
    }
</style>