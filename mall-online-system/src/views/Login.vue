<template>
  <el-row>
    <el-col :span="7">
    <h2>智慧社区后台管理系统</h2>
    <el-image style="width: 180px; height: 180px;" :src="require('@/assets/logo.png')"></el-image>
    <p>万里学院大数据与软件工程学院提供技术支持</p>
    <p>基于Vue+SpringBoot技术实现</p>
  </el-col>

  <el-col :span="1">
    <el-divider direction="vertical"></el-divider>
  </el-col>
  <el-col :span=8>
    <el-form ref="loginForm" :model="loginObject" label-width="80px" label-position="right" :rules="rules">
      <el-form-item label="用户名" style="width: 360px;" prop="username">
        <el-input v-model="loginObject.username" clearable/>
      </el-form-item>
      <el-form-item label="密码" style="width: 360px;" prop="password">
        <el-input v-model="loginObject.password" show-password />
      </el-form-item>
      <el-form-item label="验证码" style="width: 360px;" prop="code">
        <el-input v-model="loginObject.code" style="width: 140px; float: left;" maxlength="5" />
        <el-image :src="captchaImg" class="captchaImg" @click="loadCaptcha"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitLoginForm('loginForm')">登录</el-button>
        <el-button >忘记密码</el-button>
      </el-form-item>
    </el-form>
  </el-col>
    

  </el-row>
 
</template>

<script>
export default {
  data () {
    return {
      captchaImg:'',
      loginObject: {
        username: '',
        password: '',
        code:'',
        key:'',
      },
      rules:{
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
        ],
        password:[
          { required: true, message: '请输入密码', trigger: 'blur' }
        ],
        code:[
          { required: true, message: '请输入验证码', trigger: 'blur' },
        ]
      }
    }
  },
  components: {

  },
  created(){
    this.loadCaptcha();
  },
  methods: {
    loadCaptcha(){
      let url = '/api/captcha';
      this.$axios.get(url).then((response)=>{
        this.captchaImg = response.data.resultdata.captcha;
        this.loginObject.key = response.data.resultdata.key;
      }).catch((error)=>{
        console.log(error);
      });
    },
    submitLoginForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let url = '/login'
          this.$axios.post(url,this.$qs.stringify(this.loginObject)).then((response)=>{
            const jwt = response.headers['token'];
            this.$store.commit('SET_TOKEN',jwt);
            this.$router.push("/index");
          }).catch((error)=>{
            console.log(error)
            this.loadCaptcha();
          })
        } else {
          return false;
        }
      });
    },
    
  },
}

</script>
<style scoped>
  .el-row {
    background-color: #fafafa;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    text-align: center;
  }
  h2 {
    font-size: 25px;
    color: #409EFF;
  }
  p {
    font-size: 16px;
    color: #606266;
  }
  .el-divider {
    height: 200px;
  }
  .captchaImg{
    float:left;
    margin-left: 10px;
    border-radius: 4px;
    border: 1px solid #eee;
  }
</style>