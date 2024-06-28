<template>
  <div style="text-align: center;">
  <h2>您好，{{ userInfo.username }} 请您重新设置登录密码</h2>
  <el-form ref="pwdForm" :model="pwdForm" label-width="100pw" :rules="rules">
    <el-form-item label="旧密码" prop="password">
        <el-input type="password" v-model="pwdForm.password"/>
    </el-form-item>    
    <el-form-item label="新密码" prop="newPassword">
        <el-input type="password" v-model="pwdForm.newPassword"/>
    </el-form-item>  
    <el-form-item label="确认密码" prop="checkPassword">
        <el-input type="password" v-model="pwdForm.checkPassword"/>
    </el-form-item>  
    <el-form-item>
        <el-button type="primary" @click="submit('pwdForm')">提交</el-button>
    </el-form-item>
</el-form>
  </div>
</template>

<script>
export default {
  data () {
    var validatePwd = (rule,value,callback) =>{
        if(value == ''){
            callback(new Error('请再次输入新密码'));
        }else if(value !== this.pwdForm.newPassword){
            callback(new Error('两次输入新密码不一致'));
        }else{
            callback();
        }
    }
    return {
        userInfo:{},
        pwdForm:{
            password:'',
            newPassword:'',
            checkPassword:''
        },
        rules:{
            password:[
                {required:true,message:'请输入旧密码',trigger:'blur'},
            ],
            newPassword:[
                {required:true,message:'请输入新密码',trigger:'blur'},
                {min:6,max:12,message:'新密码长度应为6-12个字符',trigger:'blur'}
            ],
            checkPassword:[
                {required:true,validator:validatePwd,trigger:'blur'}
            ]
        }
    }
  },
  components: {

  },
  created(){
    this.loadUserInfo();
  },
  methods: {
    loadUserInfo(){
      this.$axios.get('/user/info').then((response)=>{
        this.userInfo = response.data.resultdata;
      }).catch((error)=>{
        console.log(error)
      })
    },
    submit(formName){
        this.$refs[formName].validate((valid)=>{
            if(valid){
                let url = '/api/updatePass'
                this.$axios.post(url,this.$qs.stringify(this.pwdForm)).then((response)=>{
                    this.$message({
                    message: '修改成功',
                    type: 'success'
                    });
                    this.pwdForm.password='';
                    this.pwdForm.newPassword='';
                    this.pwdForm.checkPassword=''
                }).catch((error)=>{
                    console.log(error)
                    this.pwdForm.password='';
                    this.pwdForm.newPassword='';
                    this.pwdForm.checkPassword=''
                })
            }else{
                return false;
            }
        })
    }
  },
}

</script>
<style scoped>
.el-form{
    width: 430px;
    margin: 50px auto;
}

</style>