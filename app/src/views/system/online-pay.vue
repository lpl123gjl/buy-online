<template>
    <div class="wrapper">
        <!-- 头部分 -->
        <div class="header">
            <i class="fa fa-chevron-left" aria-hidden="true" @click="goBack"></i>
            <p>在线支付</p>
        </div>
        <div class="Main">
            <!-- 订单信息 -->
            <div class="xinxi">订单信息</div>
            <div class="address">
                <p class="name">{{ orders.business.businessName }}&nbsp;&nbsp;<i class="fa fa-caret-down" aria-hidden="true"></i></p>
                <div>￥{{orders.orderTotal}}</div>  
            </div>
            <div class="orders">
                <div class="img">
                    <img :src="orders.business.businessImg">
                </div>    
                <div class="orders_right">
                    <ul class="orderMsg" v-for="item in orders.odList">
                    <li>
                        <p>{{ item.goods.goodsName }} x {{ item.quantity}}</p>
                        <p style="position: absolute;right: 4vw;color: rgb(255, 58, 58);">&#165; {{ item.goods.goodsPrice * item.quantity }}</p>
                    </li>
                </ul>
                <div class="pei"><p>配送费</p><span>&#165; {{ orders.business.deliveryPrice }}</span></div>
                
                </div>
            </div>
            <div class="pay-method">
                <div class="alipay">
                    <img src="../../assets/image/alipay.png" />
                    <input class="checkbox" type="checkbox" id="Checkbox1" @click="updateCheckbox('Checkbox1')"
                        v-model="checkbox1" />
                </div>
            </div>
            <div class="pay-method">
                <div class="alipay">
                    <img src="../../assets/image/wechat.png" />
                    <input class="checkbox" type="checkbox" id="Checkbox2" @click="updateCheckbox('Checkbox2')"
                        v-model="checkbox2" />
                </div>
                <button @click="payfor(orderId)">支付</button>
            </div>

            <!-- 底部导航 APP -->
            <Footer></Footer>
            <div id="paymentPopup" v-show="showPaymentPopup"
                style="display: none; position: fixed; z-index: 1; left: 0; top: 0; width: 100%; height: 100%; overflow: auto; background-color: rgba(0,0,0,0.4);">
                <div
                    style="background-color: #fefefe; margin: 15% auto; padding: 20px; border: 1px solid #888; width: 30%;">
                    <p id="paymentMessage">{{ paymentMessage }}</p>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import Footer from "@/components/Footer";
export default {
    data() {
        return {
            checkbox1: true,
            checkbox2: false,
            showPaymentPopup: false,
            paymentMessage: '正在支付...',
            orders:{},
            orderId:0
        }
    },
    components: {
        Footer,
    },
    created(){
        this.orderId = this.$route.query.orderId;
        this.loadPayment();
    },
    methods: {
        goBack() {
      this.$router.go(-1);
    },
        updateCheckbox(clickedId) {
            if (clickedId == 'Checkbox1') {
                this.checkbox2 = false;
            } else if (clickedId == 'Checkbox2') {
                this.checkbox1 = false;
            }
        },
        payfor(orderId) {
            let url= "/orders/pay/"+orderId;
          this.$axios.post(url).then((response)=>{
              if(response.data.code==20000){
                this.showPaymentPopup = true;

            setTimeout(() => {
                this.$message({
                  showClose: true,
                  message: '订单支付成功',
                  type: 'success'
                  });
                this.$router.push('/orderlistpayed');
            }, 1000);
                 
                  
                  //重新加载订单列表数据
                  this.loadOrderListByAccountId();
              }else{
                  this.$message({
                  showClose: true,
                  message: '订单支付失败',
                  type: 'error'
                  });
              }
          }).catch(e=>{
              console.log(e);
          })
            
        },
        loadPayment(){
            let url = "/orders/info/"+this.orderId;
            // let url = "/OrdersHandler/info?ordersId="+this.orderId;
            this.$axios.get(url).then((response)=>{
                if(response.data.code == 20000){
                    this.orders = response.data.resultdata
                    console.log(this.orders)
                }else{
                    this.$message({
                        showClose:true,
                        message:'订单数据获取失败',
                        type:'error'
                    });
                }
            }).catch((error)=>{
                console.log(error)
            })
        }
    },
}

</script>
<style scoped>
/* 头部分 */
.wrapper .header {
    width: 100%;
    height: 12vw;
    background: linear-gradient(to right, #403A3E, #BE5869);
    color: #79859e;
    font-size: 5vw;
    position: fixed;
    left: 0;
    top: 0;
    display: flex;
    align-items: center;
}

.wrapper .header i {
    display: flex;
    justify-content: left;
    padding-left: 3vw;
    cursor: pointer;
}

.wrapper .header p {
    font-weight: 500;
    padding-left: 33vw;
}

/* 支付方式 */
.wrapper .Main {
    margin-top: 12vw;
}

.wrapper .Main .pay-method .alipay .checkbox:checked {
    background: #74dbe9
}

.wrapper .Main .pay-method .alipay .checkbox {
    width: 6vw;
    height: 6vw;
    background-color: #ffffff;
    border: solid 1px #74dbe9;
    border-radius: 50%;
    font-size: 0.8vw;
    position: relative;
    display: inline-block;
    cursor: default;
    line-height: 10vw;
    appearance: none;
}

.wrapper .Main .pay-method .alipay .checkbox:checked::after {
    content: '\2713';
    top: -2vw;
    left: 1.5vw;
    position: absolute;
    color: #fff;
    font-weight: 600;
}

.wrapper .Main .xinxi {
    padding: 3vw;
    background-color: #e7e5e570;
    color: #666;
    display: flex;
    border-radius: 2vw;
    justify-content: center;
}

.wrapper .Main .address {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 3vw;
}

.wrapper .Main .address .name {
    color: #919191;
    font-size: 4.8vw;
}

.wrapper .Main .address div {
    color: #f13434;
    font-size: 4.8vw;
}
.wrapper .Main .orders img{
    width: 15vw;height: 15vw;
    margin-left: 4vw;
}
.wrapper .Main .orders{
    display: flex;
}
.wrapper .Main .orders .img{
    display: flex;
    align-items: center;
}
.wrapper .Main .orders .orders_right{
    margin: 3vw 0 3vw 6vw;
}
.wrapper .Main .orders .orders_right ul li{
    display: flex;
}
.wrapper .Main .orders .orders_right ul li p{
    margin: 1vw 0;text-align: center;
    font-size: 3.8vw;
}
.wrapper .Main .orders .pei{
    display: flex;position: absolute;right: 4vw;
    font-size: 3.8vw;margin-top: 2vw;
}
.wrapper .Main .orders .pei span{
    color: rgb(255, 58, 58);margin-left: 2vw;margin-top: 0.6vw;
}

.wrapper .Main .pay-method img {
    width: 40vw;
    height: 10vw;
    padding: 2vw
}

.wrapper .Main .pay-method .alipay {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-right: 3vw;
    padding-top: 3vw;
}

.wrapper .Main button {
    width: 90%;
    height: 10vw;
    background: linear-gradient(to right bottom, #fd746c, #ff9068);
    color: #777;
    border: 1px solid #913974;
    display: flex;
    justify-content: center;
    align-items: center;
    margin: 10vw 0 0 5vw;
    border-radius: 2vw;
    font-size: 4.6vw;
}
</style>