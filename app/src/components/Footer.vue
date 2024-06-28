<template>
    <div class="wrapper">
      <!-- APP底部导航栏 -->
      <ul class="footer">
        <li @click="goHome">
          <i class="fa fa-home" aria-hidden="true"></i>
          <p>首页</p>  
        </li>
        <li @click="goBuyCar">
          <i class="fa fa-cart-arrow-down" aria-hidden="true"></i>
          <p>购物车</p> 
        </li>
        <li @click="goOrder">
          <i class="fa fa-file" aria-hidden="true"></i>
          <p>订单</p>
        </li>
        <li @click="goMy">
          <i class="fa fa-user-circle" aria-hidden="true"></i>
          <p>我的</p>
        </li>
      </ul>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        currentTab: 'home',
        flag:true
      }
    },
    components: {
  
    },

    methods: {
      goHome() {
        this.$router.push('/').catch(e=>{});
      },
      goBuyCar() {
        this.$router.push('/buycar').catch(e=>{});
      },
      goOrder() {
        this.$router.push('/orderlist').catch(e=>{});
      },
      goMy() {

          this.$router.push('/my').catch(e=>{});
        
        
      },

    },
  }
  </script>
  
  
 
  <style scoped>
   
       /* 底部导航栏 */
       .wrapper .footer{
              width: 100%;
              height: 14vw;
              background-color: white;
              border: 1px solid #ddd;
              position: fixed;
              left: 0;
              bottom: 0;
              display: flex;
              justify-content: space-around;
              align-items: center;
          }
          .wrapper .footer li{
              display: flex;
              flex-direction: column;
              justify-content: center;
              align-items: center;
              color: #a1a1a1;
          }
          .wrapper .footer li p{
              font-size: 2.8vw;
          }
          .wrapper .footer li i{
              font-size: 5vw;
          }
  </style>