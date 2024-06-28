<template>
  <div class="wrapper" >
    <div class="head" :style="{opacity: opa +'%'}">
            <i class="fa fa-angle-left" aria-hidden="true" @click="goBack"/>
            <h4 style="font-weight: 500;">新增送货地址</h4>
        </div>

        <div class="contact" :style="{opacity: opa +'%'}">
            <p>门牌号</p>
            <input type="text" placeholder="例1层101室" v-model="board">
        </div>

        <div class="sex" :style="{opacity: opa +'%'}">
            <p>详细地址</p>
            <input type="text" placeholder="请输入详细地址" v-model="deliveryAddress.address"></input>
        </div>

        <div class="telephone" :style="{opacity: opa +'%'}">
            <p>联系人</p>
            <input type="text" placeholder="请输入收货人姓名" v-model="deliveryAddress.contactName">
            <input type="radio" class="r" name="sex" value="1" v-model="deliveryAddress.contactSex"/><span class="new">先生</span>
            <input type="radio" class="r" name='sex' value="0" v-model="deliveryAddress.contactSex"/><span class="new">女士</span>
        </div>

        <div class="location" :style="{opacity: opa +'%'}">
            <p>手机号&nbsp;&nbsp;</p>
            
            <input type="text" placeholder="请填写收货手机号码" v-model="deliveryAddress.contactTel">
        </div>

        <button @click="addAddress" :style="{opacity: opa +'%'}">保存地址</button>
    <Footer></Footer>
  </div>
</template>

<script>
import Footer from '@/components/Footer.vue';
export default {
  data () {
    return {
        show:false,
        show1:false,
        name:'',
        board:'',
        opa:100,
        number:'',
        home:'',
        company:'',
        school:'',
        parent:'',
        businessId:0,
        account:{},
        deliveryAddress:{
            accountId:'',
            contactName:'',
            contactTel:'',
            contactSex:1,
            address:'',
        }
    }
  },
  components: {
    Footer,
  },
  created(){
    this.account = this.$getSessionStorage('account');
    this.businessId = this.$route.query.businessId;
    this.deliveryAddress.accountId = this.account.accountId;
  },
  methods: {
    addAddress(){
        if(this.deliveryAddress.contactName == ''){
            this.$message({
                message: "联系人不能为空",
                type: 'warning'
                });
        return
        }
        if(this.deliveryAddress.address == ''){
            this.$message({
                message: "地址不能为空",
                type: 'warning'
                });
        return
        }
        if(this.deliveryAddress.contactTel == ''){
            this.$message({
                message: "手机号不能为空",
                type: 'warning'
                });
        return
        }
        if(this.deliveryAddress.board == ''){
            this.$message({
                message: "门牌号不能为空",
                type: 'warning'
                });
        return
        }
        let url = "/deliveryaddress/save";
        this.deliveryAddress.address += this.board; 
        this .$axios.post(url,this.deliveryAddress).then((response)=>{
            if(response.data.code == 20000){
                this.$message({
                message: "添加地址成功",
                type: 'success'
                });
                this.$router.push({path:'/addressmange',query:{businessId : this.businessId}})
            }else{
                this.deliveryAddress.address = '';
                this.deliveryAddress.contactName = '';
                this.deliveryAddress.contactTel = '';
                this.board = '';
                this.$message({
                message: "添加地址失败",
                type: 'warning'
                });    
            }
        }).catch((error)=>{
            console.log(error)
        })
    },
    goBack(){
        this.$router.go(-1);
    },

    selected1(){
        this.home='active'
        this.company=''
        this.school=''
        this.parent=''
    },
    selected2(){
        this.company='active'
        this.home=''
        this.school=''
        this.parent=''
    },
    selected3(){
        this.school='active'
        this.home=''
        this.company=''
        this.parent=''
    },
    selected4(){
        this.parent='active'
        this.home=''
        this.school=''
        this.company=''
    },
    confirm(){
        if(this.number.length!=11){
            this.show=true
            this.opa=50
        }
        else if(this.board===null || this.board==='' || this.board.value===0 || this.name===null || this.name==='' || this.name.value===0){
            this.show1=true
            this.opa=50

        }
        else{
            this.show2=true
            this.opa=50
        }
    },
    real(){
        this.show=false
        this.show1=false
        this.show2=false
        this.opa=100
    }
  },
}

</script>
<style scoped>
.wrapper p{
    width: 18vw;
    display: flex;
    flex: 18vw,1,1;
}
.wrapper input{
    border: none;background-color: #ffffff00;
    height: 7vw;outline: none;
    
}
.wrapper .pop{
    width: 50%;
    height: 20vw;
    position: absolute;
    font-size: 3.7vw;
    left: 24%;
    bottom: 46%;
    font-weight: 700;
    text-align: center;
    color: #2c3e50;
    border: 0.1vw solid #ff9068;
    border-radius: 1vw;
    padding: 3.5vw;
    background-color: #dfdddd;
}
.wrapper .pop .real{
    width: 100%;
    height: 10vw;
    position: absolute;
    right: 0;
    bottom: 0;

}       
        .wrapper .head{
            width: 100%;
            height: 12vw;
            background: linear-gradient(to right,#403A3E,#BE5869);
            color: #fff;
            font-size: 4.8vw;
            position: fixed;
            left: 0;
            top: 0;
            z-index: 100;
            display: flex;
            justify-content: center;
            align-items: center;
        }
        .wrapper .head p{
            font-size: 5vw;
            color: #fff;
            letter-spacing: 0.5vw;
        }
        .wrapper .head i{
            color: #fff;
            font-size: 6vw;
            position: fixed;
            left: 4vw;
            top: 3vw;
        }
        .wrapper .contact{
            width: 100%;
            height: 13vw;
            display: flex;
            align-items: center;
            margin-top: 12vw;
            border-bottom: 0.1vw solid #dddcdc;
        }
        .wrapper .contact p{
            margin-left: 4vw;
            color: #2c3e50;
            font-weight: 700;
            font-size: 3.5vw;
        }
        .wrapper .sex{
            width: 100%;
            height: 13vw;
            display: flex;
            align-items: center;
            border-bottom: 0.1vw solid #dddcdc;
        }
        .wrapper .sex p{
            font-size: 3.5vw;
            color: #2c3e50;
            font-weight: 700;
            margin-left: 4vw;
        }
        .wrapper .sex .s{
            margin-right: 2vw;
        }
        .wrapper .telephone{
            width: 100%;
            height: 13vw;
            display: flex;
            align-items: center;
            border-bottom: 0.1vw solid #dddcdc;
        }
        .wrapper .telephone p{
            font-size: 3.5vw;
            font-weight: 700;
            color: #2c3e50;
            
            margin-left: 4vw;
        }
        .wrapper .location{
            width: 100%;
            height: 13vw;
            display: flex;
            align-items: center;
            border-bottom: 0.1vw solid #dddcdc;
        }
        .wrapper .location p{
            font-size: 3.5vw;
            color: #2c3e50;
            font-weight: 700;
            margin-left: 4vw;
        }
        .wrapper button{
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
        .wrapper .sex ul{
            display: flex;
            justify-content: space-around;
            align-items: center;
        }
        .wrapper .sex ul li{
            width: 12vw;
            height: 6vw;
            background-color: #fff;
            margin-left: 3.5vw;
            font-size: 3.2vw;
            color: #666;
            line-height: 6vw;
            text-align: center;
            border: 0.1vw solid #000;
            border-radius: 1.5vw;
        }
        .wrapper .sex ul li{
            background-color: #fff;
        }
        .wrapper .sex ul .active{
            background-color: #f7a5b2;
        }
        .wrapper .telephone .new{
            font-size: 3vw;
            margin-left: 1vw;
        }
</style>