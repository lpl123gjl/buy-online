<template>
<div class="wrapper">
    <!-- 头部分 -->
    <div class="header">
        <i class="fa fa-chevron-left" aria-hidden="true" id="last-link" @click="goBack"></i>
        <p>确认订单</p>
    </div>
    <!-- 地址 -->
    <div class="address">
        <div class="tixing"><i class="fa fa-volume-up" aria-hidden="true"></i><span>&nbsp;
                <b>温馨提示：</b>请适量点餐，避免浪费！</span></div>
        <div class="dingdan">
            订单未支付
        </div>
        <div class="all" id="address-link">
            <div class="address-chose" @click="goToAddress(businessId)">
                <h3>{{deliveryAddress.address}}</h3>
                <i class="fa fa-angle-right" aria-hidden="true"></i>
            </div>

            <h6>{{deliveryAddress.contactName}} {{deliveryAddress.contactSex}} {{deliveryAddress.accountId}}</h6>
        </div>
        <div class="time" @click="showPopup">
            <div class="songda">{{ songda }}</div>
            <div class="expected-time">{{ expectedTime }}&nbsp;&nbsp; <i class="fa fa-angle-right" aria-hidden="true"></i></div>
        </div>
        <div class="overlay" :style="{ display: overlayDisplay }"></div>
        <div class="xuanze" :style="{ display: xuanzeDisplay }">
            <p>请选择预计送达时间</p>
            <div class="popup" :style="{ display: popupDisplay }">
                <div class="pleft">
                    <div class="reserve" v-for="(reserve, index) in reserves" :key="index" @click="selectReserve(reserve)">
                        {{ reserve }}
                    </div>
                </div>
                <div class="pright">
                    <div class="time-option" v-for="(time, index) in times" :key="index" @click="selectTime(time)">
                        {{ time }}
                    </div>
                </div>

            </div>
        </div>
    </div>
    <!-- 店铺名字 -->
    <div class="name">
        <h4>{{business.businessName}}</h4>
        <hr />
    </div>
    <div class="dd">
        订单详情
    </div>
    <div class="details">
        <ul>
            <li v-for="(item,index) in cartArr" :key="index">
                <div class="left">
                    <img :src="item.goods.goodsImg">
                    <div class="left-text">
                        {{ item.goods.goodsName }} x {{ item.quantity }}
                    </div>
                </div>
                <div>
                    &#165; {{item.goods.goodsPrice}}
                </div>
            </li>
        </ul>
    </div>
    <div class="fee">
        <div class="text">配送费</div>
        <div>&#165;{{business.deliveryPrice}}</div>
    </div>

    <!-- 支付 -->
    <footer>
        <div class="allmoney">总计：&#165;{{totalPrice}}</div>
        <div class="pay" id="online-pay-link" @click="toPayment">去支付</div>
    </footer>
</div>
</template>

<script>
export default {
    data() {
        return {
            account: {},
            cartArr: [],
            businessId: 0,
            business: {},
            deliveryAddress: [],
            overlayDisplay: 'none',
            xuanzeDisplay: 'none',
            popupDisplay: 'none',
            expectedTime: '',
            reserve: '',
            songda: '',
            times: ["12:50", "13:05", "13:20", "13:35", "13:50", "14:05", "14:20", "14:35", "14:50", "15:05"],
            reserves: ["立即送达", "预计送达"],
        }
    },
    components: {

    },
    created() {  
        this.businessId = this.$route.query.businessId;
        this.account = this.$getSessionStorage('account')
        this.loadBusinessById();
        this.deliveryAddress = this.$getLocalStorage(this.account.accountId)
        if(this.deliveryAddress==null){
            this.goToAddress(this.businessId)
        }
        // this.loadAddressById(); 
    },
    computed: {
        totalPrice() {
            let s = 0;
            for (let i of this.cartArr) {
                s += i.goods.goodsPrice * i.quantity;
            }
            s += this.business.deliveryPrice;
            return s;
        }
    },
    methods: {
        goBack() {
            this.$router.go(-1);
        },
        goToAddress(id) {
            this.$router.push({
                path: '/addressmange',
                query: {
                    businessId: id
                }
            })
        },
        goToPay() {
            this.$router.push({
                path: 'OnlinePay'
            })
        },
        showPopup() {
            this.overlayDisplay = 'block';
            this.xuanzeDisplay = 'flex';
            this.popupDisplay = 'flex';
        },
        selectTime(time) {
            this.expectedTime = '预计' + time + '送达';
            this.reserve = '预计送达';
            this.overlayDisplay = 'none';
            this.xuanzeDisplay = 'none';
            this.popupDisplay = 'none';
        },
        selectReserve(reserve) {
            this.songda = reserve;
        },
        loadBusinessById() {
            let url = '/business/info/' + this.businessId;
            this.$axios.get(url).then((response) => {
                this.business = response.data.resultdata;

                if (this.account != null) {
                    this.loadCartList();
                }
            }).catch((error) => {
                console.log(error);
            })
        },
        loadAddressById() {
            let url = '/deliveryaddress/listByAccountId/' + this.account.accountId;
            this.$axios.get(url).then((response) => {
                this.deliveryAddress = response.data.resultdata;
            }).catch((error) => {
                console.log(error);
            })
        },
        loadCartList() {
            let url = `/cart/listCart/${this.account.accountId}/${this.businessId}`
            this.$axios.get(url).then((response) => {
                this.cartArr = response.data.resultdata;
            }).catch((error) => {
                console.log(error)
            })
        },
        toPayment() {
            if (this.deliveryAddress == null) {
                this.$message({
                    showClose: true,
                    message: '请选择配送的地址',
                    type: 'warning'
                });
                return;
            }
            let url = '/orders/save';
            let orders = {
                accountId: this.account.accountId,
                businessId: this.businessId,
                orderTotal: this.totalPrice,
                daId: this.deliveryAddress.daId
            };
            this.$axios.post(url, orders).then((response) => {
                if (response.data.code == 20000) {
                    let order_id = response.data.resultdata;
                    this.$router.push({
                        path: '/online-pay',
                        query: {
                            orderId: order_id
                        }
                    })
                } else {
                    this.$message({
                        showClose: true,
                        message: '创建订单失败',
                        type: 'error'
                    });
                }
            }).catch((error)=>{
                console.log(error)
            })
        }
    },
};
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
    left: 0vw;
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

/* 地址 */
.wrapper .address {
    margin-left: -2vw;
    width: 100%;
    margin-top: 13vw;
    background: linear-gradient(to right bottom, #fd746c, #ff9068);
    color: #79859e;
}

.wrapper .address .dingdan {
    font-size: 3.5vw;
    padding: 2vw;
    color: #666;
}

.wrapper .address .tixing {
    line-height: 4vw;
    margin: 3vw 2vw 0 2vw;
    border: 0.3vw solid #7d7b20;
    border-radius: 2vw;
    display: flex;
    justify-content: left;
    align-items: center;
    background-color: #f5f4dc;
}

.wrapper .address .tixing i,
span,
b {
    font-size: 3.5vw;
}

.wrapper .address .all {
    border: 0.3vw solid #7d7b20;
    background-color: #f5f4dc;
    margin: 2vw;
    border-radius: 3vw;
}

.wrapper .address .all .address-chose {
    display: flex;
    justify-content: space-between;
    cursor: pointer;
}

.wrapper .address .all .address-chose h3 {
    font-size: 5.5vw;
    letter-spacing: 0.3vw;
    font-weight: 600;
    padding-bottom: 3vw;
}

.wrapper .address .all .address-chose i {
    font-size: 5.5vw;
    font-weight: 600;
    padding-right: 2vw;
}

.wrapper .address .all h6 {
    font-size: 3.4vw;
    font-size: 500;
    padding-bottom: 1vw;
    padding-left: 1vw;
}

/* 送达时间 */
.overlay {
    display: none;
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    z-index: 999;
    border-radius: 4vw;
}

.wrapper .address .xuanze p {
    padding: 2vw 20vw 2vw 20vw;
    font-size: 4vw;
    background-color: #e4e4e4c3;
}

.wrapper .address .xuanze {
    display: none;
    flex-direction: column;
    margin-left: 8%;
    color: #333;
    height: 50vw;
    background-color: #ffffff9f;
    top: 50vw;
    position: fixed;
    width: 80%;
    z-index: 1000;
}

.wrapper .address .xuanze .popup {
    color: #333;
    display: none;
    justify-content: space-around;
    position: fixed;
    padding-top: 8vw;
}

.wrapper .address .xuanze .popup .pleft {
    border-right: 1px solid #adadad;
    padding-right: 5vw;
}

.wrapper .address .xuanze .popup .pleft .reserve {
    color: #333;
    font-size: 3.5vw;
}

.wrapper .address .xuanze .popup .pleft .reserve:hover {
    background-color: #e4e4e4;
    border-radius: 2vw;
}

.wrapper .address .xuanze .popup .pright {
    height: 40vw;
    overflow: auto;
}

.wrapper .address .xuanze .popup .pright .time-option {
    color: #333;
    font-size: 4vw;
    border-radius: 2vw;
}

.wrapper .address .xuanze .popup .pright .time-option:hover {
    background-color: #e4e4e4;
}

.wrapper .address .time {
    display: flex;
    justify-content: space-between;
    align-items: center;
    cursor: pointer;
}

.wrapper .address .time .expected-time {
    font-size: 3vw;
    color: #042d95;
}

.wrapper .address .time .songda {
    color: #79859e;
    font-size: 3vw;
}

/* 店铺名字 */
.wrapper .name {
    margin: 2vw;
}

.wrapper .name h4 {
    display: flex;
    justify-content: center;
    align-items: center;
    color: #79859e;
    font-weight: 500;
    font-size: 4.5vw;
}

/* 订单详情 */
.wrapper div {
    padding: 1.5vw;
    color: #79859e;
}

.wrapper .dd {
    padding-left: 4vw;
    font-size: 4vw;
}

.wrapper .details {
    margin: 1vw;
}

.wrapper .details h3 {
    font-size: 4vw;
    color: #79859e;
}

.wrapper .details ul {
    list-style: none;
}

.wrapper .details ul li {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.wrapper .details ul li .left {
    display: flex;
    justify-content: left;
    align-items: center;
}

.wrapper .details ul li .left img {
    width: 15vw;
    height: 15vw;
}

.wrapper .details ul li .left .left-text {
    font-size: 3.6vw;
    font-weight: 400;
}

.wrapper .details ul li div {
    padding-right: 2vw;
    font-size: 4vw;
}

.wrapper .fee {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.wrapper .fee .text {
    font-size: 3.4vw;
    color: #79859e;
}

.wrapper .fee div {
    font-size: 3.4vw;
    color: #79859e;
    padding-right: 4vw;
}

/* 支付 */
.wrapper footer {
    width: 100%;
    height: 12vw;
    background-color: #313329;
    position: absolute;
    bottom: 0;
    left: 0;
    display: flex;
    justify-content: space-between;
    position: fixed;
}

.wrapper .allmoney {
    display: flex;
    justify-content: left;
    align-items: center;
    width: 60%;
    color: #e4e4e4;
    font-size: 5vw;
    font-weight: 600;
    padding-left: 10vw;
}

.wrapper .pay {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 40%;
    font-size: 4vw;
    background: linear-gradient(to right bottom, #fd746c, #ff9068);
    color: #79859e;
    border-radius: 4vw;
    cursor: pointer;
}
</style>
