<template>
    <div class="wrapper">
      <!-- 头部分 -->
      <div class="header">
        <i class="fa fa-chevron-left" aria-hidden="true" @click="goBack"></i>
        <p>餐品详情</p>
      </div>
      <!-- 商品图片 -->
      <div class="img">
        <img :src="goods.goodsImg" alt="商品图片" />
      </div>
      <!-- 商品信息 -->
      <div class="infor">
        <div class="buy">
          <p>{{ goods.goodsName }}</p>
        </div>
        <div class="caiping" style="padding-top: 2vw">
          <p class="money"><a>￥</a>{{ goods.goodsPrice }}</p>
          <div class="add">
            <i class="fa fa-minus" aria-hidden="true" v-show="goods.quantity > 0" @click="minus(goods.goodsId)"></i>
            <p v-show="goods.quantity > 0">{{ goods.quantity }}</p>
            <i class="fa fa-plus" aria-hidden="true" @click="add(goods.goodsId)"></i>
          </div>
  
        </div>
      </div>
      <!-- 商品详情 -->
      <div class="details">
        <span>商品详情</span>
        <hr />
        <div class="detal">
          <div class="detal-first">
            <p>掌柜描述：</p>
          </div>
          <div class="neirong">
            <p>{{ goods.goodsExplain }}</p>
          </div>
        </div>
      </div>
  
      <!-- 商品评论 -->
      <div class="conmment">
        <p>用户评论({{ comments.length }})</p>
        <!-- 用户评论 -->
        <div class="yonghu" v-for="comment in comments" :key="comment.commId">
          <div class="yonghu-information">
            <img :src="comment.accountImg" alt="用户头像" />
            <div class="yonghu-name">
              <a>{{ comment.accountName }}</a>
              <div class="star">
                <i class="fa fa-star" aria-hidden="true" v-for="n in comment.score" :key="n"></i>
              </div>
            </div>
            <span style="font-size: 3vw;margin-left: 20vw;">{{ comment.created | fmtDate}}</span>
          </div>
          <div class="ping">
            {{ comment.details }}
          </div>
          <img v-if="comment.commImage" style="width: 30%; height: 30%; padding-left: 2vw; padding-top: 2vw;" :src="comment.commImage"
            alt="评论图片" />
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        goods: {
          goodsId: null,
          goodsImg: '',
          goodsName: '',
          goodsPrice: 0,
          goodsExplain: '',
          quantity: 0, // 初始化 quantity 属性
          businessId: null,
        },
        comments: [],
        businessId: null,
        account: null,
      };
    },
  
    created() {
      const goodsId = this.$route.query.goodsId;
      this.loadGoodsDetail(goodsId);
      this.loadComments(goodsId);
      this.account = this.$getSessionStorage('account'); // 获取登录用户信息
    },
    methods: {
      goBack() {
        this.$router.go(-1);
      },
      loadGoodsDetail(goodsId) {
        const url = `/goods/info/${goodsId}`;
        this.$axios
          .get(url)
          .then((response) => {
            this.goods = response.data.resultdata;
            this.businessId = response.data.resultdata.businessId;
            this.loadCart();
          })
          .catch((e) => {
            console.error(e);
          });
      },
      loadComments(goodsId) {
        const url = `/comment/list/${goodsId}`;
        this.$axios
          .get(url)
          .then((response) => {
            this.comments = response.data.resultdata;
            console.log(this.comments);
            this.comments.forEach(comment => {
              this.loadAccount(comment.accountId).then(account => {
                this.$set(comment, 'accountImg', account.accountImg);
                this.$set(comment, 'accountName', account.accountName);
                
              });
            });
          })
          .catch((e) => {
            console.error(e);
          });
      },
      loadAccount(accountId) {
        const url = `/account/info/${accountId}`;
        return this.$axios
          .get(url)
          .then((response) => {
            return response.data.resultdata;
          })
          .catch((e) => {
            console.error(e);
          });
      },
      loadCart() {
        let url = `/cart/listCart/${this.account.accountId}/${this.businessId}`;
        this.$axios.get(url).then((response) => {
          let cartArray = response.data.resultdata;
          for (let cart of cartArray) {
            if (this.goods.goodsId == cart.goodsId) {
              this.$set(this.goods, 'quantity', cart.quantity);
            }
          }
          console.log('购物车数据:', this.goods.quantity);
        }).catch(e => { console.log(e); });
      },
  
      add(goodsId) {
        if (this.account == null) {
          this.$router.push('/login').catch(() => { });
          return;
        }
        if (this.goods.goodsId === goodsId) {
          if (this.goods.quantity == 0) { // 数量是0，表示该商品用户是第一次购买
            this.saveCart();
          } else { // 反之数量就不是0，说明该商品已经有数量
            this.updateCart(1);
          }
        }
      },
  
      minus(goodsId) {
        if (this.goods.goodsId === goodsId) {
          if (this.goods.quantity > 1) {
            // 数量是大于1，减去1件商品
            this.updateCart(-1);
          } else {
            this.removeCart();
          }
        }
      },
  
      saveCart() {
        let url = "/cart/save";
        let cart = {
          goodsId: this.goods.goodsId,
          businessId: this.businessId,
          accountId: this.account.accountId,
          quantity: this.goods.quantity
        };
        this.$axios.post(url, cart)
          .then((response) => {
            if (response.data.code == 20000) {
              this.$set(this.goods, 'quantity', 1);
              console.log('购物车保存', this.goods.quantity); // 添加调试日志
            } else {
              this.$message({
                showClose: true,
                message: '购物车数据保存失败',
                type: 'error'
              });
            }
          })
          .catch(e => { console.log(e); });
      },
  
      updateCart(n) {
        let url = "/cart/update";
        let cart = {
          goodsId: this.goods.goodsId,
          businessId: this.businessId,
          accountId: this.account.accountId,
          quantity: this.goods.quantity + n
        };
        this.$axios.post(url, cart)
          .then((response) => {
            if (response.data.code == 20000) {
              this.$set(this.goods, 'quantity', this.goods.quantity + n);
              console.log('更改', this.goods.quantity); // 添加调试日志
            } else {
              this.$message({
                showClose: true,
                message: '购物车数据更新失败',
                type: 'error'
              });
            }
          })
          .catch(e => { console.log(e); });
      },
  
      removeCart() {
        let url = "/cart/remove";
        let cart = {
          goodsId: this.goods.goodsId,
          businessId: this.businessId,
          accountId: this.account.accountId,
        };
        this.$axios.post(url, cart)
          .then((response) => {
            if (response.data.code == 20000) {
              this.$set(this.goods, 'quantity', 0);
              console.log('全部删除');
            } else {
              this.$message({
                showClose: true,
                message: '购物车数据删除失败',
                type: 'error'
              });
            }
          })
          .catch(e => { console.log(e); });
      }
    },
  };
  </script>
  
  <style scoped>
  .wrapper {
    margin-right: 2vw;
  }
  
  .wrapper .header {
    width: 100%;
    height: 12vw;
    background: linear-gradient(to right, #403A3E, #BE5869);
    color: #79859e;
    font-size: 5vw;
    position: fixed;
    left: 0;
    top: 0;
    z-index: 100;
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
    letter-spacing: 2vw;
  }
  
  /* 商品图片 */
  .wrapper .img {
    width: 100%;
    padding-top: 12vw;
    padding-bottom: 4vw;
  }
  
  .wrapper .img img {
    width: 100%;
    height: 60vw;
  }
  
  /* 商品信息 */
  .wrapper .infor {
    padding-top: 3vw;
    border-bottom: 3vw solid #f6f3f3;
  }
  
  .wrapper .infor .number {
    padding-left: 2vw;
  }
  
  .wrapper .infor .number p {
    font-weight: 100;
    font-size: 1.8vw;
    padding-top: 2vw;
    color: #333;
  }
  
  .wrapper .infor .number .intro {
    display: flex;
    justify-content: left;
    align-items: center;
    padding-top: 2vw;
  }
  
  .wrapper .infor .number .intro span {
    background-color: #f9d4da;
    color: #333;
    font-size: 1.8vw;
    padding: 0 1.5vw 0 1.5vw;
    margin-right: 1.5vw;
  }
  
  .wrapper .infor .number .num {
    display: flex;
    justify-content: left;
    align-items: center;
    padding-top: 1vw;
  }
  
  .wrapper .infor .number .num div {
    font-size: 1.6vw;
    color: #333;
    background-color: #eaeaea;
    padding: 0 1.5vw 0 1.5vw;
    margin-right: 1.5vw;
  }
  
  .wrapper .infor .buy {
    width: 100%;
    display: flex;
    justify-content: left;
    align-items: center;
    margin-right: 1vw;
    padding-left: 2vw;
  }
  
  .wrapper .infor .buy div {
    color: red;
    font-size: 1.5vw;
    background-color: pink;
    padding: 0 1vw 0 1vw;
  }
  
  .wrapper .infor .buy p {
    font-size: 4.8vw;
    font-weight: 600;
    padding-left: 3vw;
  }
  
  .wrapper .caiping {
    margin: 1vw 2vw 3vw 2vw;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  .wrapper .caiping .add {
    display: flex;
    justify-content: right;
    align-items: center;
    line-height: 6vw;
  }
  
  .wrapper .caiping .add p {
    padding: 1vw 2vw 0 2vw;
  }
  
  .wrapper .caiping .money {
    font-weight: 600;
    font-size: 6.0vw;
    color: #ff032d;
  }
  
  .wrapper .caiping a {
    font-size: 5vw;
    font-weight: 600;
  }
  
  .wrapper .caiping span {
    font-weight: normal;
    font-size: 1.8vw;
    color: #777;
    position: absolute;
    justify-content: left;
    align-items: center;
    padding-top: 1.5vw;
  }
  
  .wrapper .caiping .add i {
    padding: 1vw 3vw 1vw 3vw;
    background-color: #BE5869;
    cursor: pointer;
    border-radius: 1vw;
  }
  
  /* 详情 */
  .wrapper .details {
    padding: 2vw;
    border-bottom: 3vw solid #f6f3f3;
  }
  
  .wrapper .details span {
    color: #333;
    font-size: 4vw;
    padding: 3vw 0 3vw 0;
  }
  
  .wrapper .details hr {
    color: #f6f3f3;
  }
  
  .wrapper .details .detal {
    display: flex;
    justify-content: left;
    align-items: top;
  }
  
  .wrapper .details .detal .detal-first {
    color: #777;
    font-size: 3.6vw;
    padding-left: 2vw;
    padding-bottom: 2vw;
  }
  
  .wrapper .details .detal .detal-first p {
    padding-top: 2vw;
    width: 20vw;
  }
  
  .wrapper .details .detal .neirong {
    color: #333;
    font-size: 3.6vw;
    padding-bottom: 2vw;
  }
  
  .wrapper .details .detal .neirong p {
    padding-top: 2vw;
  }
  
  /* 用户评论 */
  .wrapper .conmment {
    padding: 2vw;
  }
  
  .wrapper .conmment p {
    font-size: 3.8vw;
    padding-bottom: 2vw;
    color: #333;
  }
  
  .wrapper .conmment div {
    font-size: 3vw;
    padding-top: 1vw;
    padding-left: 1vw;
    margin-right: 1vw;
    line-height: 5vw;
  }
  
  .wrapper .conmment .ping {
    letter-spacing: 0.3vw;
    font-size: 3.4vw;
  }
  
  .wrapper .conmment .yonghu {
    padding-bottom: 3vw;
    border: 0.5vw solid #f6f3f3;
  }
  
  .wrapper .conmment .yonghu .yonghu-information {
    display: flex;
    align-items: center;
    
    
  }
  
  .wrapper .conmment .yonghu .yonghu-information img {
    width: 10vw;
    height: 10vw;
    border-radius: 5vw;
    display: flex;
  }
  
  .wrapper .conmment .yonghu .yonghu-information .yonghu-name {
    display: flex;flex-direction: column;
  }
  
  .wrapper .conmment .yonghu .yonghu-information span {
    color: #777;
    font-size: 1.8vw;
    padding-right: 2vw;
    display: flex;
  }
  
  .wrapper .conmment .yonghu .yonghu-information .yonghu-name a {
    font-size: 3.4vw;
    font-weight: 400;
    padding-left: 1vw;
  
    letter-spacing: 0.3vw;
  }
  
  .wrapper .conmment .yonghu .yonghu-information .yonghu-name .star {
    font-size: 3vw;
  }
  
  .wrapper .conmment .yonghu .yonghu-information .yonghu-name .star .fa-star-o {
    color: rgb(243, 243, 135);
    margin-right: 0.5vw;
  }
  
  .wrapper .conmment .yonghu .yonghu-information .yonghu-name .star .fa-star {
    color: rgb(243, 243, 135);
    margin-right: 0.5vw;
  }
  
   </style>