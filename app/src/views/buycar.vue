<template>
    <div class="wrapper">
        <div class="header">
            <div @click="back" class="back">
                &lsaquo;
            </div>
            <h3>购物车</h3>
        </div>
        <span v-if="cartList.length==0" style="display: flex;justify-content: center;margin-top: 13vw;font-size: 6vw;color: #606266;">您还未添加商品</span>
        <div v-if="cartList.length>0" class="goods" v-for="item in cartList">
            <div class="store">
                <i class="fa fa-home" aria-hidden="true" />
                <p>{{ item.businessName }}</p>
            </div>
            <div class="foods" v-for="goods in item.goodsList">
                <ul class="food">
                    <li>
                        <img :src="goods.goodsImg">
                        <div class="food-msg">
                            <div class="food-msg-left">
                                <h3>{{ goods.goodsName }}</h3>
                                <p>{{ goods.goodsPrice }}元</p>
                            </div>
                            <div class="food-msg-right">
                                <p>&times;{{ goods.count }}</p>
                            </div>
                        </div>
                    </li>
                </ul>
                
            </div>
            <div @click="go_confirm(item.businessId)" class="pay" id="confirm-order-link">
                    <p>去支付</p>
                </div>
        </div>

        <Footer></Footer>
    </div>
</template>

<script>
import Footer from '../components/Footer.vue';
export default {
    data() {
        return {
            accounr: {},
            cartList: [],
            allSelected: false // 是否全选标识
        }
    },
    components: {
        Footer,
    },
    created() {
        this.account = this.$getSessionStorage('account')
        this.loadCartList()
    },
    methods: {
        loadCartList() {
            let url = '/cart/list/' + this.account.accountId
            this.$axios.get(url).then((response) => {
                if (response.data.code == 20000) {
                    this.cartList = response.data.resultdata
                    console.log(this.cartList)
                }
            }).catch((error) => {
                console.log(error)
            })
        },

        go_confirm(id) {


            // 将选中的商品传递给'/confirm-order'页面
            this.$router.push({
                path: '/confirm-order',
                query: {
                    businessId: id
        }
            });
        },
        back() {
            this.$router.back();
        }
    },
}

</script>
<style scoped>
.wrapper {
    width: 100%;
}

/* 头部部分 */
.wrapper .header {
    width: 100%;
    height: 13vw;
    background: linear-gradient(to right, #403A3E, #BE5869);
    border-bottom: 1px solid #c1c1c1;
    display: flex;
    align-items: center;
}

.wrapper .header .back {
    font-size: 8vw;
    color: #5b5b5ba9;
    margin-left: 3.5vw;
    position: fixed;
}

.wrapper .header h3 {
    font-size: 4.8vw;
    color: #999;
    font-weight: 500;
    margin: 0 auto;
}

/* 商品信息部分 */
.wrapper .goods {
    width: 95%;
    display: flex;
    flex-direction: column;
    border: 1px solid #c8c8c8;
    border-radius: 2vw;
    box-shadow: 0.8vw 0.9vw 0.8vw -1vw #929292;
    margin: 0 auto;
    margin-top: 2vw;
}

/* 商家 */
.wrapper .goods .store {
    width: 100%;
    height: 17vw;
    border-bottom: 1px solid #c4c4c4;
    display: flex;
    align-items: center;
    padding-left: 4.5vw;
    font-size: 3.8vw;
    color: #4e4e4e;
    box-sizing: border-box;
}

.wrapper .goods .store i {
    font-size: 4.8vw;
}

.wrapper .goods .store p {
    margin-left: 2vw;
}

/* 食物信息 */
.wrapper .goods .foods {
    width: 100%;

}

.wrapper .goods .foods .food {
    width: 95%;
    margin: 0 auto;
}

.wrapper .goods .foods .food li {
    width: 95%;
    height: 20vw;
    display: flex;
    border-radius: 2vw;
    box-shadow: 0.5vw 0.3vw 2vw 0.1vw #e4e4e4;
    background-color: #fdfdfd;
    margin: 2vw 2vw;
    user-select: none;
}

.wrapper .goods .foods .food li img {
    width: 20vw;
    height: 20vw;
    border-radius: 2vw;
}

.wrapper .goods .foods .food li .food-msg {
    width: 100%;
    margin: 0 5vw 0 2vw;
    display: flex;
    align-items: center;
    justify-content: space-between;
}

.wrapper .goods .foods .food li .food-msg .food-msg-left {
    font-size: 3.6vw;
    color: #4e4e4e;
    line-height: 5.8vw;
}

.wrapper .goods .foods .food li .food-msg .food-msg-left h3 {
    font-size: 4vw;
    font-weight: 600;
}

.wrapper .goods .foods .food li .food-msg .food-msg-right {
    display: flex;
    flex-direction: column;
    align-items: center;
}

.wrapper .goods .foods .food li .food-msg .food-msg-right input {
    width: 3.4vw;
    height: 3.4vw;
}

.wrapper .goods .foods .food li .food-msg .food-msg-right p {
    margin-top: 1.5vw;
    font-size: 4vw;
}

/* 前往支付 */
.wrapper .pay {
    width: 16vw;
    height: 8vw;
    background-color: #77d6ff;
    border-radius: 1.5vw;
    color: #fff;
    font-size: 4vw;
    font-weight: 500;
    position: relative;
    left: 72vw;
    margin: 2vw;
    user-select: none;
    cursor: pointer;

}

.wrapper .pay p {
    padding: 0.8vw 2vw;
}
</style>