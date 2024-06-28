<template>
    
        <div class="wrapper" id="app">
            <header>
                <a><i class="fa fa-chevron-left" aria-hidden="true" @click="goBack"/></a>
                <div class="header">
                    <img src="../../assets/image/查找.png">
                    <img src="../../assets/image/收藏.png">
                    <img src="../../assets/image/聊天.png">
                </div>
            </header>

            <div class="show">
                <div class="show-text">
                    <h3>{{ business.businessName }}</h3>
                    <i class="fa fa-star" aria-hidden="true" />
                    <span style="color: #fd746c;">4.1</span>
                    <span>&#165;{{business.starPrice}}起送 | &#165;{{business.deliveryPrice}}配送 </span>
                    <span>月销800+</span><br>
                    
                </div>
                <img :src=business.businessImg>
            </div>

            <!--红包-->
            
            <div class="tell">
         
            </div>
            <hr>
            <div class="recommend">
                <img src="../../assets/image/推荐菜饺子.jpg">
            </div>

            <div class="buy">
                <div class="check">
                    <ul>
                        <li class="active">推荐</li>
                      
                    </ul>
                </div>
                <div class="list">
                    <ul>
                        <li  v-for="(item,index) in goods" :key="item.goodsId" >
                            <div class="list-left" id="product-details-link">
                                <img :src= item.goodsImg @click="goToProductDetails(item.goodsId)">
                                <div class="list-left-text">
                                    <h3>{{item.goodsName}}</h3>
                                    <p>{{item.goodsExplain}}</p>
                                    <p style="color: red;">￥{{item.goodsPrice}}</p>
                                    
                                </div>

                            </div>
                            <div class="list-right">
                                <button @click="removeCart(index)" v-if="item.quantity > 0">-</button>
                                <input type="text" v-model="item.quantity"  v-if="item.quantity > 0"/>
                                <button @click="add(index)">+</button>
                            </div>
                        </li>
                    </ul>

                    
                    
                </div>
            </div>


            <!--页脚-->
            <footer>
                <div class="car">
                    <img src="../../assets/image/购物车 (2).png">
                    <p class="g_number" v-if="totalQuantity!=0">{{totalQuantity}}</p>
                </div>
                <div class="text">
                    <p class="all-money">商品:￥{{totalPrice}}</p>
                    <p class="road-money">另需配送费￥{{business.deliveryPrice}}元</p>
                </div>
                <div class="pay" id="confirm-order-link" style="background-color: #cecece;" v-show="totalPrice < business.starPrice">
                    <p>￥{{ business.starPrice }} 起送</p>
                </div>
                <div class="pay" id="confirm-order-link" @click="toPay(businessId)" v-show="totalPrice >= business.starPrice" style="background: linear-gradient(to right bottom, #fd746c, #ff9068);">
                    <p>去结账{{totalPrice>0?totalPrice+business.deliveryPrice:0}}元</p>
                </div>
            </footer>


        </div>
  
</template>

<script>

 export default {
 
    data() {
return {
    star:3.0,
    businessId:0,
    business:{},
    goods: [],
    account:{},
};
},
methods:{
add(index){
    if(this.account==null){
       this.$router.push('/login').catch(e=>error);
        return;
    }
    if(this.goods[index].quantity==0){
        this.saveCart(index);
    }else{ //反之数量就不是0
        this.updateCart(index,1);
    }
},
minus(index){
    if(this.account==null){
       this.$router.push('/login').catch(e=>error);
        return;
    }
    if(this.goods[index].quantity>1){
        this.updateCart(index,-1);
    }else{
        this.removeCart(index);
    }
},
//请求服务器保存购物车
saveCart(index){
    let url ="/cart/save";
    let cart={
        goodsId:this.goods[index].goodsId,
        businessId:this.businessId,
        accountId:this.account.accountId,
        quantity:1,
    }
    this.$axios.post(url,cart)
    .then((response)=>{
        if(response.data.code==20000){
            this.goods[index].quantity=1
        }else{
            this.$message({
                showClose: true,
                message: '购物车数据保存失败',
                type: 'error'
      });
        }
    })
    .catch(e=>{console.log(e);})
},
//请求服务器更新购物车数据
updateCart(index,n){
    let url = "/cart/update";
    let cart={
        goodsId:this.goods[index].goodsId,
        businessId:this.businessId,
        accountId:this.account.accountId,
        quantity:this.goods[index].quantity+n,
    }
    this.$axios.post(url,cart)
    .then((response)=>{
        if(response.data.code==20000){
            this.goods[index].quantity+=n
        }else{
            this.$message({
                showClose:true,
                message:'购物车数据更新失败',
                type:'error',
            })
        }
    })
    .catch(e=>{console.log(e);})
},
removeCart(index){
    let url = "/cart/remove";
    let cart={
        goodsId:this.goods[index].goodsId,
        businessId:this.businessId,
        accountId:this.account.accountId,
    }

    this.$axios.post(url,cart)
    .then((response)=>{
        if(response.data.code==20000){
            this.goods[index].quantity=0
        }else{
            this.$message({
                showClose:true,
                message:'购物车数据删除失败',
                type:'error',
            })
        }
    })
    .catch(e=>{console.log(e);})
},

total(){
    let sum=0.0;
    for(let f of this.goods){
        sum+=f.goodsPrice*f.quantity;
    }
    return sum;
},
 
  goToProductDetails(goodsId) {
      this.$router.push({ path: '/product-details', query: { goodsId: goodsId , starPrice : this.business.starPrice} });
    },
  toPay(){
    this.$router.push({path:'/confirm-order/',query:{businessId:this.businessId}});
  },
  goBack() {
        this.$router.back();
    },
   

  
  loadBusinessInfoByBusinessId() {

      let url = "/business/info/" + this.businessId;
    //   let url ="BusinessHandler/info?businessId="+this.businessId;
      this.$axios.get(url)
          .then((response) => {
              this.business = response.data.resultdata;
              this.loadGoodsByBusinessId();   
          })
          .catch(e => {
            console.log(error)
          })

  },
  loadGoodsByBusinessId(){
    let url = '/goods/listByBusinessId/'+this.businessId;
    // let url = "/GoodsHandler/listByBusinessId?businessId="+this.businessId;
    this.$axios.get(url)
    .then((response)=>{
        let tempArray=response.data.resultdata;
        for(let i=0;i<tempArray.length;i++){
            tempArray[i].quantity=0;
        }
        this.goods=tempArray;
        if(this.account != null){
                this.loadCart();
              }
    }).catch(e=>error)

  },
  loadCart(){
    let url=`/cart/listCart/${this.account.accountId}/${this.businessId}`;
    // let url =`/CartHandler/listCart?accountId=${this.account.accountId}&businessId=${this.businessId}`;
    this.$axios.get(url).then((response)=>{
        let cartArray = response.data.resultdata;
        for(let goods of this.goods){
            for(let cart of cartArray){
                if(goods.goodsId == cart.goodsId){
                    goods.quantity = cart.quantity
                }
            }
        }
    }).catch(e=>{console.log(e)})
  }

},
created() {
    this.businessId = this.$route.query.businessId;
    this.loadBusinessInfoByBusinessId();
    this.account=this.$getSessionStorage('account');
},

computed:{
    totalPrice() {
        let sum = 0.0; //累加所有饺子价格
        for (let f of this.goods) {
            sum += f.goodsPrice * f.quantity;
        }
        return sum;
    },
    payPrice() {
        return this.totalPrice + this.star;
    },
    totalQuantity(){
        let n=0;
        for(let item of this.goods){
            n+=item.quantity;
        }
        return n;
    }

},
watch:{
    goods:{
        handler(newVal,oldVal){
            for(let i in newVal){
                this.goods[i].quantity=newVal[i].quantity<=0?0:newVal[i].quantity;
            }
        },
        deep:true
    }
}


};


</script>
<style scoped>
.wrapper {
    width: 100%;
    height: 100%;
    position: relactive;
}

.wrapper header {
    width: 100%;
    height: 12vw;
    background: linear-gradient(to right, #403A3E, #BE5869);
    color: #79859e;
    font-size: 5vw;
    justify-content: space-between;
    display: flex;
    align-items: center;
}

.wrapper header .header img {
    width: 5vw;
    margin: auto 2vw;
}

.wrapper header p {
    color: #79859e;
    font-size: 5vw;
    font-weight: 500;
    padding-left: 33vw;
}

.wrapper header a i {
    display: flex;
    justify-content: left;
    padding-left: 3vw;
    cursor: pointer;
}

/*展示栏*/
.wrapper .show {
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: space-between;

}

.wrapper .show i {
    color: #BE5869;
    font-size: 2.5vw;
}

.wrapper .show img {
    width: 22vw;
    height: 22vw;
    margin: 5vw 8vw 0 5vw;
    border-radius: 2vw;
}

.wrapper .show .show-text {
    margin-left: 3vw;
}

.wrapper .show .show-text h3 {
    font-size: 5vw;
    color: #000;
    margin-bottom: 4vw;
}

.wrapper .show .show-text span {
    color: #666;
    margin-top: 1vw;
    font-size: 3.2vw;

}

/*红包*/
.wrapper .red-bog {
    font-size: 3.2vw;
    display: flex;
    margin: 1vw 2vw;
    color: #fff;
}

.wrapper .red-bog font {
    font-size: 4.5vw;
    text-align: center;
}

.wrapper .red-bog .first {
    background-color: #ee2c2c;
    border-radius: 1vw;
    height: 6vw;
    width: 55vw;
    margin-left: 0vw;
    display: flex;
    text-align: center;
}

.wrapper .red-bog .first #red {
    background-color: #f89e9e;
    width: 8vw;
    height: 6vw;
    display: flex;
    border-bottom-right-radius: 4vw;
    text-align: center;
    align-items: center;
}

.wrapper .red-bog .first #money {
    border-right: 0.5vw dashed #fff;
}

.wrapper .red-bog .first .text {
    display: flex;
    justify-content: right;
    align-items: center;
    width: 8vw;

}

.wrapper .red-bog .second {
    background-color: #BE5869;
    width: 12vw;
    text-align: center;
    border-radius: 1vw;
    margin-left: 1vw;
}

.wrapper .red-bog #change {
    border-right: 0.5vw dashed #fff;
}

/*公告*/
.wrapper .tell {
    margin:0 3vw 2vw 3vw;
}

.wrapper .tell p {
    font-size: 4vw;
    color: #000;
}

.wrapper .tell span {
    font-size: 2.6vw;
    color: #44484e;
    text-indent: 4vw;
    display: inline-block;
    margin-top: 2vw
}

/*推荐*/
.wrapper .recommend {
    text-align: center;
}

.wrapper .recommend img {
    width: 100%;
    width: 95vw;
    border-radius: 5vw;
}


/*商品列表*/
.wrapper .buy {
    display: flex;
    justify-content: space-between;
}

.wrapper .buy .check {
    text-align: center;
    font-size: 4vw;
    width: 20%;
    background-color: #edecec;
}

.wrapper .buy .check li {
    height: 15vw;
    display: flex;
    align-items: center;
    justify-content: center;
}

.wrapper .buy .check .active {
    background-color: #fff;
}


.wrapper .list {
    width: 80%;
    display: flex;
    justify-content: center;
    flex-direction: column;
}

.wrapper .list ul {
    width: 100%;
    
    
}

.wrapper .list ul li {
    display: flex;
    justify-content: space-between;
    border-bottom: 1px solid #e4e25e;
    margin-bottom: 2vw;
    position: relative;
}

.wrapper .list ul li .list-left {
    display: flex;
    
    justify-content: left;
}

.wrapper .list ul li .list-left img {
    height: 20vw;
    width: 20vw;
    margin-left: 2vw;
    text-align: center;
}

.wrapper .list ul li .list-left .list-left-text {
    margin-left: 2vw;
}

.wrapper .list ul li .list-left .list-left-text h3 {
    font-size: 4vw;
    margin-top: 1vw;
}

.wrapper .list ul li .list-left .list-left-text p {
    color: #666;
    margin-left: 1vw;
    margin-top: 1vw;
    font-size: 3vw;
    display: block;
    width: 39vw;
}

.wrapper .list ul li .list-left .money {
    font-size: 3vw;
    border-radius: 1vw;
}

.wrapper .list ul li .list-left .money .left {
    border-right: 0.5vw solid #fff;
    border-bottom-right-radius: 1vw;
}

.wrapper .list ul li .list-left .money .pay {
    font-size: 3vw;
    color: #fff;
    border: 0.4vw solid #BE5869;
    background-color: #e28080;
}

.wrapper .list ul li .list-right {
    display: flex;
    font-weight: bolder;
    position: absolute;bottom: 4vw;right: 3vw;
}
.wrapper .list ul li .list-right button {
    border: none;
    background-color: #BE5869;
    font-weight: bolder;
    font-size: 3vw;
    width: 4vw; /* 添加这一行 */
    height: 4vw; /* 添加这一行 */
    border-radius: 2vw;
    display: flex;justify-content: center;align-items: center;
}
.wrapper .list ul li .list-right input{
    width: 6vw;height: 4vw;
    border: none;
    text-align: center;
    font-weight: bolder;
    font-size: 4vw;
}


.wrapper footer {
    width: 100%;
    height: 12vw;
    background-color: #403e3e;
    position: absolute;
    bottom: 0;
    left: 0;
    display: flex;
    justify-content: space-between;
    position: fixed;
}

.wrapper footer .car {
    height: 15vw;
    width: 15vw;
    border-radius: 9vw;
    background-color: #f2f2d5;
    justify-content: center;
    align-items: center;
    display: flex;
    overflow: visible;
    border: 1vw solid #000;
    position: absolute;
    bottom: 1vw;
    left: 3vw;
}
.wrapper footer .car .g_number{
    position: absolute;top: -2vw;right: -1vw;
    background-color: rgb(255, 76, 76);
    border-radius: 50%;
    width: 6vw;height: 6vw;
    display: block;
    text-align: center;
    color: #fefe00;
    font-size: 4.4vw;padding-top: 0.5vw;box-sizing: border-box;padding-left: 0.3vw;
}
.wrapper footer .car img {
    height: 9vw;
    width: 9vw;
   
}

.wrapper footer .text {
    margin-left: 20vw;
    color: #bdc3dd;
}

.wrapper footer .text .all-money{
    font-size: 5vw;
}

.wrapper footer .text .road-money {
    font-size: 2vw;
}

.wrapper footer .pay {
    font-size: 5vw;
    display: flex;
    justify-content: center;
    align-items: center;
    width: 40vw;
    color: #000000;
    border-radius: 4vw;
    padding: 2vw;box-sizing: border-box;
}
</style>