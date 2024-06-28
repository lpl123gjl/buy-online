<template>
  <el-container>
    <el-aside width="200px">
      <NavMenu></NavMenu>
    </el-aside>
    <el-container>
      <el-header>
        <strong>智慧社区后台管理系统V1.0.0</strong>
        <div class="header-userinfo">
          <el-avatar size="medium" :src="user.avatar"></el-avatar>
          <el-dropdown>
            <span class="el-dropdown-link">
              {{ user.username }}<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>个人中心</el-dropdown-item>
              <el-dropdown-item @click.native="setPwd">修改密码</el-dropdown-item>
              <el-dropdown-item divided @click.native="logout">退出系统</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-header>
      <el-main>
        <Tabs></Tabs>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import NavMenu from '@/components/NavMenu.vue'
import Tabs from '@/components/Tabs.vue';
export default {
  data() {
    return {
      user: {
        username: 'Byterain',
        avatar: "https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png",
      },
    }
  },
  components: {
    NavMenu,
    Tabs
  },
  created(){
    this.getUserInfo();
  },
  methods: {
    getUserInfo(){
        //请求服务器获得
        this.$axios.get('/api/userinfo').then(response=>{
            this.user = response.data.resultdata
            if(!this.$getSessionStorage('user')){
              this.$setSessionStorage('user',response.data.resultdata.business)
            }
        }).catch(e=>{
            console.log(e);
        })
       },
     
    setPwd(){
      this.$router.push('/setpwd');
    },
    logout(){
      this.$axios.get('/logout').then((response)=>{
        this.$store.commit('SET_TOKEN','');
        this.$store.commit('SET_MENULIST',[]);
        this.$store.commit('SET_PERMLIST',[]);
        this.$store.commit('SET_HASROUTE',false);
        this.$store.commit('RESET_TABS');
        sessionStorage.clear();
        localStorage.clear();

        this.$router.push('/login');
      }).catch((error)=>{
        console.log(error)
      })
    },
  },
}

</script>
<style scoped>
.el-container {
  padding: 0;
  margin: 0;
  height: 100%;
}

.el-aside {
  background-color: #efefef;
}

.el-header {
  background-image: linear-gradient(-225deg, #a1f6ff 0%, #98faff89 48%, #61ddffc2 100%);
  color: #292929;
  text-align: left;
  font-size: 16px;
  line-height:
    60px;
}
.el-dropdown-link{
  color: #363636;
}

.header-userinfo {
  float: right;
  width: 150px;
  display: flex;
  justify-content: space-around;
  align-items: center;
}

.el-main {
  color: #333;
  padding: 0;
}

.el-avatar {
  margin-right: 5px;
}
</style>