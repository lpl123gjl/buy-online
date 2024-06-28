<template>
    <div class="wrapper">
          <div class="header">
              <i class="fa fa-angle-left" aria-hidden="true" />
              <p>订单列表</p>
          </div>
          <!-- 全部订单 已支付 未支付 -->
          <ul class="whole">
              <li><a @click="goToAboutPage()">全部订单</a></li>
              <li><a @click="goToAboutPage1()">已支付</a></li>
              <li><a class="special">未支付</a></li>
          </ul>
          <hr>
  
          <!-- 订单详情 -->
  
          <ul class="list">
              <li v-for="(item,index) in ordersArray" :key="index" id="kkk" v-if="item.state==0">
                  
                <div class="headline">
                      <i class="fa fa-shopping-cart" aria-hidden="true" />
                      <p>{{ item.business.businessName | fmtTitle }}</p>
                      <a @click="topay(item.orderId)" :class="item.state==1?'aaa':'bbb'">{{item.state==1?'已支付':'未支付'}}</a>
                  </div>
  
                  <ul class="photo">
                      <li v-for="(odItem,odIndex) in item.odList" :key="odIndex">
                          <div class="collect">
                              <img :src=odItem.goods.goodsImg>
                              <p>{{ odItem.goods.goodsName | fmtTitle2}}</p>
                              <p>价格：{{ odItem.goods.goodsPrice }}</p>
                              <p>× {{ odItem.quantity }}</p>
                          </div>
                      </li>
                  </ul>
                  <div class="transfer" >
                      <p>配送费:</p>
                      <h5>&#165;{{ item.business.deliveryPrice }}</h5>
                  </div>
                  <div class="money" >
                      <div class="money1">
                          <p class="spe">交易金额:&nbsp;&nbsp;&#165;{{ item.orderTotal }}</p>
                          <p v-if="item.updated.minute<10 && item.updated.second<10">订单生成时间：{{ item.updated.year }}年{{ item.updated.monthValue}}月{{ item.updated.dayOfMonth }}日{{ item.updated.hour }}:0{{ item.updated.minute }}:0{{ item.updated.second }}</p>   
                            <p v-else-if="item.updated.minute>=10 && item.updated.second<10">订单生成时间：{{ item.updated.year }}年{{ item.updated.monthValue}}月{{ item.updated.dayOfMonth }}日{{ item.updated.hour }}:{{ item.updated.minute }}:0{{ item.updated.second }}</p>  
                            <p v-else-if="item.updated.minute<10 && item.updated.second>=10">订单生成时间：{{ item.updated.year }}年{{ item.updated.monthValue}}月{{ item.updated.dayOfMonth }}日{{ item.updated.hour }}:0{{ item.updated.minute }}:{{ item.updated.second }}</p>
                                <p v-if="item.updated.minute>=10 && item.updated.second>=10">订单生成时间：{{ item.updated.year }}年{{ item.updated.monthValue}}月{{ item.updated.dayOfMonth }}日{{ item.updated.hour }}:{{ item.updated.minute }}:{{ item.updated.second }}</p>    
                        </div>
                  </div>
              </li>
          </ul>
      <Footer></Footer>
    </div>
  </template>
  
  <script>
  import Footer from '@/components/Footer.vue';
  export default {
    data () {
      return {
          ordersArray:[],
          account:{},
      }
    },
    components: {
      Footer,
    },
    created(){
      this.account=this.$getSessionStorage('account');
      this.loadOrderListByAccountId();
    },
    methods: {
      //修改状态为已支付
      topay(orderId){
        this.$router.push({
                path: '/online-pay',
                query: {
                    orderId: orderId
        }
            });
      },
      loadOrderListByAccountId(){
          let url = "/orders/list/"+this.account.accountId;
        //   let url = "/OrdersHandler/listByState1?accountId="+this.account.accountId
          this.$axios.get(url).then((response)=>{
              if(response.data.code==20000){
                  this.ordersArray = response.data.resultdata;
                  console.log(response.data.resultdata)
                  for(let orders of this.ordersArray){
                      orders.isShowDetailet = false;                    
                  }
              }else{
                  this.$message({
                  showClose: true,
                  message: '订单数据失败',
                  type: 'error'
                  });
              }
          }).catch(e=>{
              console.log(e);
          })
      },
      goToAboutPage(){
          this.$router.push('/orderlist');
      },
      goToAboutPage1(){
          this.$router.push('/orderlistpayed');
      },
      pay(){
          this.show=true
          this.opa=50
      },
      real(){
          this.show=false
          this.opa=100
      }
    },
  }
  
  </script>
  <style scoped>
  .wrapper{
      padding-bottom: 10vw;
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
  .wrapper .header{
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
          .wrapper .header p{
              font-size: 5.5vw;
              color: #999;
          }
          .wrapper .header i{
              color: #999;
              font-size: 6vw;
              position: fixed;
              left: 4vw;
              top: 3vw;
          }
          .wrapper .whole{
              width: 100%;
              height: 10vw;
              display: flex;
              justify-content: space-around;
              align-items: center;
              margin: 13vw 0 0 0;
          }
          .wrapper .whole li{
              width: 14vw;
              height: 5vw;
              text-align: center;
              line-height: 5vw;
          }
          .wrapper .whole li a{
              font-size: 3.3vw;
              color: #999;
              padding-bottom:1.8vw ;
              font-weight: 700;
          }
          .wrapper .whole li a.special{
              color: rgb(243, 240, 51);
              font-weight: 700;
              border-bottom: 0.6vw solid yellow;
          }
          .wrapper hr{
              border: 0.5vw solid #ddd;
              margin-top: -1.3vw;
          }
          .wrapper .list{ 
              width: 90%;
              display: flex;
              flex-direction: column;
              margin:  5vw auto;
              border-radius: 1vw;
  
          }
          .wrapper .list #kkk{
              box-shadow: 0 0 0.9vw 0.8vw #dddcdc;
              margin: 0 0 3vw 0;
          }
          .wrapper hr{
              margin-bottom: 7vw;
          }
          .wrapper .list .headline{
              width: 100%;
              height: 12vw;
              display: flex;
              justify-content: space-between;
              align-items: center;
              border-bottom: #dddcdc 0.1vw solid;
          }
          .wrapper .list .headline i{
              color: #999;
              font-size: 5vw;
              margin-left: 3.5vw;
          }
          .wrapper .list .headline .aaa{
              width: 15vw;
              height: 6vw;
              font-size: 3.5vw;
              color: rgb(52, 9, 20);
              text-align: center;
              line-height: 6vw;
              background-color: rgb(171, 237, 55);
              margin-right: 2.5vw;
              border-radius: 1.3vw;
          }
          .wrapper .list .headline p{
              font-size: 4vw;
              color: #999;
              margin-left: -15vw;
              letter-spacing: 0.1vw;
          }
          .wrapper .list .headline .bbb{
              width: 15vw;
              height: 6vw;
              font-size: 3.5vw;
              color: #f36;
              text-align: center;
              line-height: 6vw;
              background-color: rgb(248, 207, 218);
              margin-right: 2.5vw;
              border-radius: 1.3vw;
          }
          .wrapper .list .photo{
              width: 100%;
              height: 50vw;
              display: flex;
              align-items: center;
              border-bottom: 0.1vw solid #dddcdc;
          }
          .wrapper .list .photo .collect img{
              width: 23vw;
              height: 23vw;
              margin: 2vw 4vw;
          }
          .wrapper .list .photo .collect p{
              font-size: 3.5vw;
              color: #999;
              text-align: center;
              
          }
          .wrapper .list .money p.spe {
              font-size: 3.5vw;
              color: #999;
              font-weight: 700;
          }
          .wrapper .list .money p{
              font-size: 3.5vw;
              color: #999;
          }
          .wrapper .list .money .money1{
              margin: 2vw 2vw;
              
          }
         
          .wrapper .transfer{
              display: flex;
              justify-content: left;
              align-items: center;
              border-bottom: 0.1vw solid #dddcdc;
          }
          .wrapper .transfer p{
              margin: 2vw 2vw;
              color: #999;
              font-size: 3.8vw;
          }
          .wrapper .transfer h5{
              color: #999;
              font-size: 3.4vw;
          }
  </style>