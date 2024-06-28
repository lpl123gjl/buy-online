<template>
  <div class="container">
         <header :style="{opacity: opa +'%'}">
            <i class="fa fa-angle-left" aria-hidden="true" @click="goBack" />
            <p>修改密码</p>
        </header>
        
        <div class="telephone" :style="{opacity: opa +'%'}">
            <p>+86<i class="fa fa-chevron-right" aria-hidden="true" /></p>
            <input type="text" placeholder="请输入您的手机号码" v-model="number">
        </div>
        <div class="password" :style="{opacity: opa +'%'}">
            <span >新密码：</span> <input type="password" class="new-password1" placeholder="请输入您的新密码" v-model="newpassword"/><br>
         <hr>   
            <span>确认密码：</span><input type="password" class="new-password" placeholder="请再次确认您的密码" v-model="confirmpassword"/>
            <hr>
        </div>
        <div class="button" :style="{opacity: opa +'%'}">
            <button @click="Reg()">立即注册</button>
        </div>
        <div v-if="show1" class="pop">
            <p>请输入正确的手机号码</p>
            <button class="real" @click="confirm()">确认</button>
        </div>
        <div v-if="show2" class="pop">
            <p>您输入的密码不得多余18位且不得少于6位</p>
            <button class="real" @click="confirm()">确认</button>
        </div>
        <div v-if="show3" class="pop">
            <p>您输入的密码与之前不符</p>
            <button class="real" @click="confirm()">确认</button>
        </div>
        <div v-if="show4" class="pop">
            <p>修改成功!</p>
            <button class="real" @click="confirm()">确认</button>
        </div>
    <Footer></Footer>
  </div>
</template>

<script>
import Footer from '@/components/Footer.vue';
export default {
  data () {
    return {
        show1:false,
        show2:false,
        show3:false,
        show4:false,
        number:'',
        opa:100,
        newpassword:'',
        confirmpassword:'',
    }
  },
  components: {
    Footer,
  },
  methods: {
    goBack(){
      this.$router.go(-1);
    },

    Reg(){
        if(this.number.length!=11){
            this.show1=true,
            this.opa=50
        }
        else if(this.newpassword.length<6 || this.newpassword.length>18){
            this.show2=true,
            this.opa=50
        }
        else if(this.confirmpassword!=this.newpassword){
            this.show3=true,
            this.opa=50
        }
        else{
            this.show4=true,
            this.opa=50
        }
    },
    confirm(){
            this.show1=false
            this.show2=false
            this.show3=false
            this.show4=false
            this.opa=100
        },
  },
}

</script>
<style scoped>
.container .pop{
    width: 50%;
    height: 20vw;
    position: absolute;
    font-size: 3.7vw;
    left: 22%;
    bottom: -90vw;
    font-weight: 700;
    text-align: center;
    color: #2c3e50;
    border: 0.1vw solid #ff9068;
    border-radius: 1vw;
    padding: 3.5vw;
}
.container .pop .real{
    width: 100%;
    height: 10vw;
    position: absolute;
    right: 0;
    bottom: 0;

}
.container{width: 100%;height: 12vw;position: relative;}
        .container header{width: 100%;height: 10vw;background: linear-gradient(to right,#403A3E,#BE5869);display: flex;
                        justify-content: center;align-items: center;}
        .container header p{color: #999;font-size: 5vw;}
        .container header .fa-angle-left{
            position: fixed;left: 2vw;top: 2vw;font-size: 7vw;color: #79859e;
        }

        /*修改密码*/
        .container .password{
            width: 100%;margin-top: 2vw;
            
        }
        .container .password .new-password{
            border: 0;
            margin: 2vw 2vw;
            margin-left: 6vw;
        }
        .container .password span{
            margin-left: 3vw;color: #999;
            font-size: 3.5vw;
        }
       
        .container .password .new-password1{
            color: #999;width: 60%;border: 0;
            margin-left:8.5vw ;height: 8vw;
            
        }
        
        .container button{
            width: 100%;
            height: 11vw;
            border: 0;
            background: linear-gradient(to right bottom,#fd746c,#ff9068);
            color: rgb(0, 0, 0);
            font-size: 4vw;
            letter-spacing: 0.5vw;
            border-radius: 1vw;
            margin: 0 auto;
        }
        .container .telephone{
            width: 100%;
            height: 13vw;
            display: flex;
            align-items: center;
            border-bottom: 0.1vw solid #dddcdc;
            margin-top: 0vw;
        }
        .container .telephone p{
            font-size: 3.5vw;
            color: #999;
            margin-left: 4vw;
        }
        .container .telephone i{
            margin-left: 3vw;
            font-size: 3vw;
        }
        .container .telephone input{
            margin-left: 11.6vw;
            border: 0            
        }
</style>