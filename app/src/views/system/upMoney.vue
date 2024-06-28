<template>
  <div class="wrapper">
        <!-- 头部 -->
        <div class="header">
            <div class="header-left">
               <i @click="back" class="fa fa-chevron-left" aria-hidden="true" />
            <span>我的钱包</span>
            </div>
            <div class="header-right">
                <img src="../../assets/image/setting.png">
            </div>
        </div>

        <!-- 内容部分 -->
        <div class="content">
            <div class="msg">
                <div class="msg-left">
                <img src="../../assets/image/msg.png">
                <p v-show="flag==1">本周账单已出炉 ~</p>
                <p v-show="flag==2">为了保证资金安全，请设置密码</p>
                <p v-show="flag==3">外卖天天减7元</p>
                <p v-show="flag==4">饿了吧钱包能省，多赚，更会花</p>
                </div>
                <div class="msg-right">&#8250;</div>
            </div>
            <div class="money">
                <div class="money-top">
                    <p>总资产(元)</p>
                </div>
                <div class="money-center">
                    <div class="money-center-left">
                    <h3>58.00</h3>
                    <span>&#8250;</span>
                    </div>
                    <div class="money-center-right">
                            <button @click="go">去充值</button>
                    </div>
                </div>
                <div class="money-bottom">
                    <ul>
                        <li>
                            <p>笔笔返</p>
                            <span class="number">0</span>
                        </li>
                        <li>
                            <p>充值红包</p>
                            <span class="number">5<span>张</span></span>
                        </li>
                        <li>
                            <p>畅享金</p>
                            <span style="color: #939393;">查看详细</span>
                        </li>
                    </ul>
                </div>
            </div><!-- 活动部分 -->
            <div class="activity">
                <ul>
                    <li>
                        <img src="../../assets/image/借钱选中.png">
                        <span>借钱</span>
                        <div class="red">五千豆</div>
                    </li>
                    <li>
                        <img src="../../assets/image/我的账单.png">
                        <span>我的账单</span>
                    </li>
                    <li>
                        <img src="../../assets/image/钱.png">
                        <span>笔笔返</span>
                    </li>
                    <li>
                        <img src="../../assets/image/盾牌.png">
                        <span>支付设置</span>
                    </li>
                    <li>
                        <img src="../../assets/image/券包.png">
                        <span>建行生活礼</span>
                    </li>
                    <li>
                        <img src="../../assets/image/奖杯.png">
                        <span>借钱精选</span>
                    </li>
                    <li>
                        <img src="../../assets/image/银行卡.png">
                        <span>干饭神卡</span>
                    </li>
                    <li>
                        <img src="../../assets/image/移动.png">
                        <span>联合会员</span>
                    </li>
                </ul>
            </div>

            <!-- 红包 -->
            <div class="red-card">
                <p>干饭神器·支付省钱</p>
                <div class="red">
                    <div class="biankuang">
                        <div class="biankuang2">
                    <div class="red-content">
                        <div>7<span>元</span> <span class="x">&times;</span> 60<span style="margin-left:1vw ;">张</span></div>
                    </div>
                    </div>
                    </div>
                    <div class="red-right">
                        <div>420<span>元</span></div>
                        <button>领支付红包</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
  data () {
    return {
        flag:1
    }
  },
  components: {

  },
  methods: {
    back(){
        this.$router.back();
    },
    go(){
        this.$router.push('/toMoney')
    },
    run(){
        for(let i =0;i<50;i++){
            setTimeout(() => {
                this.flag++
                if(this.flag==4){
                    this.flag=1
                }
            },3000*i)
        }
    }
  },
  mounted(){
    this.run()
  }
}


</script>
<style scoped>
    .wrapper{
            width: 100%;height: 220vw;
            box-sizing: border-box;
            background: linear-gradient(35deg, #ffe9f7 0%, #c2fffe 100%);
        }

        /* 头部 */
        .wrapper .header{
            width: 100%;height: 16vw;
            display: flex;justify-content: space-between;
            align-items: center;
        }
        .wrapper .header .header-left{
            font-size: 4.3vw;font-weight: 600;display: flex;
        }
        .wrapper .header .header-left i{
            font-size: 5.6vw;padding: 2vw 0 0 3vw;color: #707070;
        }
        .wrapper .header .header-left span{
            padding-left: 4vw;padding-top: 1.7vw;
        }
        .wrapper .header .header-right{
            position: relative;top: 1.7vw;right: 3vw;
        }

        /* 内容部分 */
        .wrapper .content{
            width: 100%;
            box-sizing: border-box;
            padding: 0 3vw;
        }

        /* 消息 */
        .wrapper .content .msg{
            font-size: 4vw;
            display: flex;justify-content: space-between;align-items: center;
            border-radius: 3vw;
            background-color: #ffffffa0;
            box-sizing: border-box;
            padding: 2vw 2vw;
        }
        .wrapper .content .msg .msg-left{
            display: flex;justify-content: center;align-items: center;
        }
        .wrapper .content .msg .msg-left img{
            width: 19px;height: 19px;
        }
        .wrapper .content .msg .msg-left p{
            font-size: 3.6vw;margin-left: 2vw;letter-spacing: 0.3vw;
        }
        .wrapper .content .msg .msg-right{
            font-size: 5vw;color: #707070;padding-bottom: 0.8vw;margin-right: 2vw;
        }

        /* 总资产 */
        .wrapper .content .money{
            width: 100%;padding: 3vw;margin: 2vw 0; box-sizing: border-box;
            background-color: #ffffffcb;border-radius: 3vw;
        }
        .wrapper .content .money .money-top{
            font-size: 3.6vw;
        }
        .wrapper .content .money .money-center{
            width: 100%;
            display: flex;justify-content: space-between;align-items: center;
            margin: 2vw 0 5vw 0;
        }
        .wrapper .content .money .money-center .money-center-left{
            display: flex;
        }
        .wrapper .content .money .money-center .money-center-left h3{
            font-size: 8vw;
        }
        .wrapper .content .money .money-center .money-center-left span{
            font-size: 6.5vw;display: flex;position: relative;bottom: -1vw;left: 2vw;
            color: #939393;
        }
        .wrapper .content .money .money-center .money-center-right button{
            font-size: 4vw;border-radius: 5vw;
            height: 8.3vw;width: 22vw;border: none;outline: none;
            background-image: linear-gradient(to right, #32cfff 0%, #1bf4ff 100%);
            color: #fff;
            margin-top: 0.5vw;
        }
        .wrapper .content .money .money-bottom{
            width: 100%;border-top: 1px solid #ededed;
        }
        .wrapper .content .money .money-bottom ul{
            width: 100%;
            display: flex;justify-content: space-around;
        }
        .wrapper .content .money .money-bottom ul li{
            width: 15vw;
            display: flex;flex-direction: column;
            font-size: 3.2vw;
        }
        .wrapper .content .money .money-bottom ul li:first-of-type{
            position: relative;left: -6vw;
        }
        .wrapper .content .money .money-bottom ul li:last-of-type{
            position: relative;right: -6vw;
        }
        .wrapper .content .money .money-bottom ul li p{
            margin: 1.5vw 0;
        }
        .wrapper .content .money .money-bottom ul li .number{
            font-weight: 600;font-size: 4vw;
        }
        .wrapper .content .money .money-bottom ul li .number span{
            color: #939393;font-weight: 400; font-size: 3.2vw;
            position: relative;bottom: 0.3vw;left: 1vw;
        }

        /* 活动部分 */
        .wrapper .activity{
            width: 100%;height: 35vw;color: #6b6b6b;
            border-radius: 3vw;
            display: flex;align-content: center;justify-content: center;
            background-color: #ffffff7b;
            user-select: none;
            box-sizing: border-box;
            
        }
        .wrapper .activity ul{
            width: 100%;
            display: flex;flex-wrap: wrap;justify-content: space-around;
            margin: 3.8vw 0 2.8vw 0;
        }
        .wrapper .activity ul li{
            width: 20.1vw;height: 16vw;font-size: 3.4vw;
            display: flex;flex-direction: column;align-items: center;
            position: relative;
        }
        .wrapper .activity ul li .red{
            padding: 0.5vw 1.5vw;
            font-size: 2.3vw;color: rgb(255, 255, 255);
            border: 0.4vw solid rgb(255, 81, 81);
            border-radius: 3vw;
            position: absolute;top: -1vw;right: -2vw;
            background-color: #ff2f2f;
        }
        .wrapper .activity img{
            width: 6.5vw;height: 6.5vw;
        }

        /* 红包部分 */
        .wrapper .red-card{
            width: 100%;
            height: 35vw;margin: 2vw 0;
            padding: 3vw;box-sizing: border-box;
            background-color: #ffffffaf;border-radius: 3vw;
            font-size: 3.8vw;font-family: "黑体";
        }
        .wrapper .red-card .red{
            width: 100%;height: 23vw;
            background-image: linear-gradient(-60deg, #dd3e12 0%, #f58300 100%);
            border-radius: 1.5vw;
            padding: 0 0;box-sizing: border-box;
            display: flex;justify-content: left;align-items: center;
            margin-top: 2.5vw;
        }
        .wrapper .red-card .red .biankuang{
            width: 55vw;height: 23vw;
            border-radius: 2vw 9vw 9vw 2vw;
            display: flex;justify-content: center;align-items: center;
            background-color: #d70000;
            overflow: hidden;
        }
        .wrapper .red-card .red .biankuang2{
            width: 55vw;height: 20vw;
            border-radius: 2vw 0 0 2vw;
            display: flex;justify-content: center;align-items: center;
            background-color: #e7e7e796;
            overflow: hidden;
            margin-left: 2vw;
        }
        .wrapper .red-card .red .biankuang .red-content{
            height: 19.5vw;width: 65vw;
            margin: 1vw;
            background-color: #fff;
            display: flex;justify-content: center;align-items: center;
            position: relative;right: -1vw;
            border-radius: 1.5vw 0 0 1.5vw;
            
        }
        .wrapper .red-card .red .red-content div{
            font-size: 8vw;color: rgb(238, 4, 4);font-weight: 600;
        }
        .wrapper .red-card .red .red-content div span{
            font-size: 3.2vw;
        }
        .wrapper .red-card .red .red-content div .x{
            font-size: 4.6vw;position: relative;bottom: 0.8vw;
        }
        .wrapper .red-card .red .red-right{
            display: flex;flex-direction: column;justify-content: center;align-items: center;
            position: relative;left: 3vw;
        }
        .wrapper .red-card .red .red-right div{
            font-size: 7vw;color: #efeb00;font-weight: 600;
        }
        .wrapper .red-card .red .red-right div span{
            font-size: 3vw;font-weight: 500;margin-left: 0.3vw;
        }
        .wrapper .red-card .red .red-right button{
            border: none;border-radius: 5vw;background-color: #f3eb00;color:rgb(197, 0, 0) ;
            height: 6.3vw;font-size: 3.6vw;font-family: "黑体";
        }
</style>