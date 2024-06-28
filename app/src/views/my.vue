<template>
  <div class="wrapper">
        <div class="user">
            <img :src="accountImg">
            <div class="user-msg">
                <div class="user-msg-left">
                <div class="name">{{$getSessionStorage('account').accountName}}<img src="../assets/image/会员8.png"></div>
                <div class="action_center">
                    <p style="padding-left:0;">{{$getSessionStorage('account').accountId}}</p><p style="padding-bottom: 0.2vw;">{{ accountSex | get_sex }}</p>
                </div>
               
                </div>
            </div>
        </div>
        <div class="setting">
            <img src="../assets/image/设置 (2).png" @click="go_pwd">
        </div>

        <!-- 会员部分 -->
        <img class="biaoqian" src="../assets/image/给红包.png">
        <div class="member">
            <div class="member-center">
            
            <p>今日社区有好货</p>
            
            </div>
            <div class="member-center">
                <p>给你的家人来一单吧</p>
                <button @click="go_buy">去下单</button>
            </div>
            <div class="member-bottom">
                <i class="fa fa-angle-down" aria-hidden="true" />
            </div>
            <img class="backgroud1" src="../assets/image/吃货豆_基础识别形.png">
            <img class="backgroud2" src="../assets/image/吃货豆_基础识别形.png">
        </div>
        <!-- 订单部分 -->
        <div class="order">
            <div class="order-header">
                <h3>我的订单</h3>
                <div @click="go_order">全部<span>&#8250;</span></div>
            </div>
            <ul>
                <li>
                    <img src="../assets/image/pay.png">
                    <span>待付款</span>
                </li>
                <li>
                    <img src="../assets/image/use.png">
                    <span>待收货/使用</span>
                </li>
                <li>
                    <img src="../assets/image/evaluate.png">
                    <span>待评价</span>
                    <div class="red"></div>
                </li>
                <li>
                    <img src="../assets/image/refund.png">
                    <span>退款售后</span>
                </li>
            </ul>
        </div>

        <!-- like部分 -->
        <div class="like">
                <ul class="like-content">
                    <li>
                        <img src="../assets/image/我的店铺客服.png">
                        <span>客服</span>
                    </li>
                    <li>
                        <img src="../assets/image/like.png">
                        <span>收藏</span>
                    </li>
                    <li>
                        <img src="../assets/image/history.png">
                        <span>浏览记录</span>
                    </li>
                    <a>
                    <li>
                        <img src="../assets/image/地址管理.png">
                        <span>地址管理</span>
                    </li>
                    </a>
                </ul>
        </div>

       

        <div class="like">
                <ul class="like-content">
                    <li @click="go_myself">
                        <img src="../assets/image/个人中心.png">
                        <span>个人中心</span>
                    </li>
                    <li>
                        <img src="../assets/image/评价.png">
                        <span>评价中心</span>
                    </li>
                    <li>
                        <img src="../assets/image/店铺.png">
                        <span>关注店铺</span>
                    </li>
                    <a>
                    <li>
                        <img src="../assets/image/关于.png">
                        <span>关于我们</span>
                    </li>
                    </a>
                </ul>
        </div>

        <div class="exit" @click="back">
            <button>退出登录</button>
        </div>
        <Footer></Footer>
  </div>
</template>

<script>
import Footer from '@/components/Footer.vue';
import Vue from 'vue';
Vue.filter('get_sex',(accountSex)=>{
    if(accountSex==1){
        return '男'
    }else if(accountSex==0){
        return '女'
    }
})
export default {
  data () {
    return {
        accountImg:this.$getSessionStorage('account').accountImg,
        accountSex:this.$getSessionStorage('account').accountSex

    }
  },
  components: {
    Footer,
  },
  methods: {
    go_pwd(){
        this.$router.push('/change-password');
    },
    back(){
        this.$router.push('/login');
    },
    go_buy(){
        this.$router.push('/')
    },
    go_myself(){
        this.$router.push('/myself-information')
    },
    go_order(){
        this.$router.push('/orderlist')
    }
  },

}

</script>
<style scoped>
        .wrapper {
            width: 100%;
            box-sizing: border-box;
            padding: 1.8vw;
        }

        /* 顶部用户信息 */
        .wrapper .user{
            width: 100%;
            height: 26vw;
            display: flex;
            align-items: center;
            user-select: none;

        }
        .wrapper .user img{
            width: 19vw;
            height: 19vw;
            border-radius: 50%;
            margin: 0 3vw 0 2vw;
        }
        .wrapper .user .user-msg{
            display: flex;justify-content: space-between;
            font-size: 4vw;
            color: #000;
        }
        .wrapper .user .user-msg .name{
            font-size: 5vw;font-weight: 600;
            display: flex;margin-left: 1vw;
        }
        .wrapper .user .user-msg .action{
            margin-left: 1.5vw;
            display: flex;
            width: 100%;
            margin-top: 1vw;font-size: 3.7vw;color: #5f5f5f;
        }
        .wrapper .user .user-msg .action_center{
            display: flex;justify-content: center;align-items: center;
        }
        .wrapper .user .user-msg .action_center p{
            margin-right: 4vw;line-height: 5.8vw;
        }
        .wrapper .user .user-msg .action p{
            margin: 0;
            padding: 0 2.3vw 0 2.3vw;
        }
        .wrapper .user .user-msg img{
            width: 8vw;height: 8vw;
        }
        .wrapper .user .user-msg .user-msg-right{
            position: absolute;right: 0;top: 11vw;
            border-radius: 5vw 0 0 5vw;
            font-size: 3.4vw;
            padding: 0 1vw 0 2vw;
            background-color: #ededed;
            display: flex;align-items: center;justify-content: center;
        }
        .wrapper .user .user-msg .user-msg-right .jiantou{
            padding-bottom:0.7vw;
            margin-left: 1vw;
            font-size: 5.5vw;
        }
        .wrapper .setting{
            font-size: 5vw;
            position: fixed;
            right: 3vw;
            top: 3vw;
            opacity: 0.9;
        }

        /* 会员卡 */
        .wrapper .member{
            width: 100%;
            height: 25vw;
            border-radius: 3vw;
            margin: 2vw auto;
            display: flex;
            flex-direction: column;
            justify-content: center;
            position: relative;
            background: linear-gradient(to right bottom,#fbfd60,#f2d4fb);
            color: rgba(117, 92, 4, 0.662);
            user-select: none;
            z-index: 200;
        }
        .wrapper .biaoqian{
            width: 30%;position: absolute;
            right: 5vw;top: 8vw;
            opacity: 0.9;
        }
        .wrapper .member .backgroud1{
            width: 9vw;height: 9vw;
            position: absolute;
            left: 67vw;top: 5vw;
            opacity: 0.2;
        }
        .wrapper .member .backgroud2{
            width: 9vw;height: 9vw;
            position: absolute;
            left: 3vw;bottom: 2vw;
            opacity: 0.2;
        }

        .wrapper .member .member-center{
            width: 100%;
            display: flex;
            justify-content: space-between;
            margin: 1vw 0 1vw 0;
        }
        .wrapper .member .member-center p{
            margin-left: 6vw;
            font-size: 5.6vw;
            letter-spacing: 0.8vw;
            font-family:"STXingkai";
            line-height: 5.8vw;
        }
        .wrapper .member .member-center button{
            font-size: 4.4vw;
            font-weight: 600;
            width: 19vw;height: 9vw;
            border-radius: 5vw;border: none;
            background-color: rgba(255, 255, 105, 0.68);
            color: #403402;
            position: absolute;top: 8vw;right: 5vw;
        }
        .wrapper .member .member-bottom{
            width: 100%;
            display: flex;
            justify-content: center;
            font-size: 5.5vw;
            position: absolute;
            bottom: 0;
        }

        /* like部分 */
        .wrapper .like{
            width: 100%;height: 24vw;box-shadow: 0 0 2px 1px #e7e7e7;
            border-radius: 3vw;
            box-sizing: border-box;padding-top: 2.6vw;
            background-color: #ffffffbb;
            user-select: none;
            margin-bottom: 2vw;
        }
        
        .wrapper .like .like-content{
            width: 100%;
            display: flex;justify-content: space-around;align-items: center;
            margin: 3.6vw 0;
        }
        .wrapper .like .like-content li{
            font-size: 3.4vw;
            width: 17vw;height: 12vw;
            display: flex;flex-direction: column;justify-content: center;align-items: center;
            position: relative;
        }
        .wrapper .like .like-content li img{
            margin-bottom: 1.4vw;
            width: 7.6vw;height: 7.6vw;
        }
        .wrapper .like .like-content li .red{
            padding: 0 1.5vw;
            font-size: 3vw;color: rgb(255, 81, 81);
            border: 0.4vw solid rgb(255, 81, 81);
            border-radius: 3vw;
            position: absolute;top: -2vw;right: 0vw;
            background-color: #fff;
        }


        /* 订单信息部分 */
        .wrapper .order{
            width: 100%;height: 28vw;box-shadow: 0 0 2px 1px #e7e7e7;
            box-sizing: border-box;
            padding: 2vw;
            border-radius: 3vw;
            background-color: #ffffffbb;
            margin: 2vw 0;
            user-select: none;
        }
        .wrapper .order .order-header{
            width: 100%;
            display: flex;justify-content: space-between;
        }
        .wrapper .order .order-header h3{
            font-size: 3.8vw;font-weight: 600;
            margin-left: 1.6vw;
        }
        .wrapper .order .order-header div{
            font-size: 3.2vw;color: #808080;
            margin-right: 1.6vw;
        }
        .wrapper .order .order-header div span{
            font-size: 4.7vw;margin-left: 1vw;
        }
        .wrapper .order ul{
            width: 100%;
            display: flex;justify-content: space-around;
            margin-top: 2.4vw;
        }
        .wrapper .order ul li{
            display: flex;flex-direction: column;align-items: center;
            font-size: 3.6vw;
            position: relative;
        }
        .wrapper .order ul li img{
            margin-bottom: 1.4vw;
            width: 7.6vw;height: 7.6vw;
        }
        .wrapper .order ul li .red{
            width: 2.4vw;height: 2.4vw;background-color: red;border-radius: 50%;
            position: absolute;top: 0;right: 0;
        }


        /* 退出登录 */
        .wrapper .exit{
            width: 100%;margin-bottom: 14vw;
        }
        .wrapper .exit button{
            width: 100%;height: 10vw;
            border: none;background-color: #ffffffbb;border-radius: 2vw;
            box-shadow: 0 0 2px 1px #ededed;
            font-size: 4.6vw;color: #403402;
            user-select: none;

        }
</style>