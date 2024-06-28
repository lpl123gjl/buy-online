<template>
  <div class="wrapper">

    <header>
            <p>用户登录</p>
        </header>

        <!-- Login Form -->
        <ul class="form-box">
            <li>
                <div class="title">手机号码：</div>
                <div class="content">
                    <input id="phone" type="text" placeholder="请输入手机号码" v-model="account.accountId"/>
                </div>
            </li>
            <li>
                <div class="title">密码：</div>
                <div class="content">
                    <input id="password" type="password" placeholder="请输入账号密码" v-model="account.password"/>
                </div>
            </li>
        </ul>
        <div class="button-login">
            <button @click="login">登录</button>
        </div>
        <div class="button-register">
            <button @click="register">注册</button>
        </div>

    <!--页脚-->
   <Footer></Footer>
  </div>
</template>

<script>
import Footer from '@/components/Footer.vue';

export default {
  data () {
    return {
     account:{
        accountId:'',
        password:''
     }
    }
  },
  components: {
    Footer,
  },
  methods:{
    register(){
        this.$router.push('/register');
    },
    login(){
        let url = '/account/login';
        // let url = '/AccountHandler/login'
        console.log("参数：",this.$qs.stringify(this.account));
        this.$axios.post(url,this.$qs.stringify(this.account))
        .then((response)=>{
            if(response.data.code==20000){
                let account = response.data.resultdata;
                this.$setSessionStorage('account',account);
                this.$router.back();
                this.$setSessionStorage('flag',false)
            }else{
                this.$message({
                message: response.data.message,
                type: 'warning'
        });
            }
        }).catch((error)=>{
            console.log(error)
        })
    }
  }

}

</script>

<style scoped>

header {
    height: 15vw;
    text-align: center;
    color: #333;
    font-size: 5vw;
    margin-bottom: 5vw;
    background: linear-gradient(to right,#403A3E,#BE5869);
    align-items: center;display: flex;justify-content: center;
}

.form-box {
    list-style: none;
    padding: 0;
    margin: 0;
}

.form-box li {
    margin-bottom: 5vw;
}

.title {
    font-size: 4vw;
    color: #333;
    margin-bottom: 2vw;
    margin-left: 2vw;
}

.content input {
    width: 93vw;
    padding: 3vw;
    font-size: 4vw;
    border: 0.3vw solid #ccc;
    border-radius: 2vw;
}

.button-login button {
    width: 100%;
    padding: 5vw;
    font-size: 4vw;
    color: #fff;
    background-color: #BE5869;
    border: none;
    border-radius: 2vw;
    cursor: pointer;
}

.button-register button {
    width: 100%;
    padding: 5vw;
    font-size: 4vw;
    color: #BE5869;
    background-color: #fff;
    border: 0.5vw solid #BE5869;
    border-radius: 2vw;
    cursor: pointer;
    margin-top: 2vw;
}
</style>