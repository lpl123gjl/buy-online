<template>
    <div class="wrapper">
        <!--header部分-->
        <div class="header">
            <div class="location-text" style="padding-top: 4vw;">
                <i class="fa fa-location-arrow" aria-hidden="true" /><button @click="openBaiduMap" />
                {{ addressComponent.province }}{{ addressComponent.city }}{{ addressComponent.district }}{{
                    addressComponent.street }}
                    <span v-if="isAddressShow">点击去定位</span>
            </div>
            <div class="weather">
                <img src="../assets/image/云社区.png">
                <span>天气：晴</span>
            </div>
        </div>

        <!--搜索栏-->
        <div class="search" ref="fixedBox">
            <div class="search-top">
                <div class="search-box">
                    <i class="fa fa-search" aria-hidden="true" />
                    <input type="text" placeholder="搜索商家名称或商品名称">
                </div>
            </div>
        </div>

        <!--商品的分类信息-->
        <ul class="category-ul">
            <li v-for="(item, index) in category_list" :key="item.categoryId" ref="category"
                @click="toBusiness_List(item.categoryId)">
                <img :src="item.categoryCover">
                <p>{{ item.categoryName }}</p>
            </li>
        </ul>

        <!--横幅广告-->
        <div class="banner" id="business-list-link"">
        <h3>万里学院</h3>
        <p>万里GO购物</p>
        <a>立即下单 &gt;</a>
    </div>

    <!--超级会员-->
    <div class=" superNumber">
            <div class="left">
                <img src="../assets/image/new1.png">
                <h3>超级会员</h3>
                <p>&#8226;每月享超级权益</p>
            </div>
            <div class="right">
                立即开通 &gt;
            </div>
        </div>

        <!--推荐商家-->
        <div class="show-business">
            <div class="show-business-line"></div>
            <p>推荐商家</p>
            <div class="show-business-line"></div>
        </div>

        <!--推荐方式-->
        <ul class="showType">
            <li>综合排序<i class="fa fa-caret-down" aria-hidden="true" /></li>
            <li>距离最近<i class="fa fa-street-view" aria-hidden="true" /></li>
            <li>销量最高<i class="fa fa-fire" aria-hidden="true" /></li>
            <li>筛选<i class="fa fa-filter" aria-hidden="true" /></li>
        </ul>

        <!--商家列表-->
        <ul class="business" v-for="(item, index) in business_list" :key="index">
            <li id="business-detail-link" @click="toBuy(item.businessId)">
                <img :src="item.businessImg">
                <div class="jiaobiao" v-if="item.quantity != 0 && item.quantity != undefined">{{ item.quantity }}</div>
                <div class="business-info">
                    <div class="business-info-h">
                        <h3>{{ item.businessName }}</h3>
                        <div class="business-info-like">
                            <i class="fa fa-heart-o" />

                        </div>
                    </div>
                    <div class="business-info-star">
                        <div class="business-info-star-left">
                            <div class="star">

                                <img src="../assets/image/star (1).png" style="height: 4vw;width: 4vw;">
                                <img src="../assets/image/star (1).png" style="height: 4vw;width: 4vw;">
                                <img src="../assets/image/star (1).png" style="height: 4vw;width: 4vw;">
                                <img src="../assets/image/star (1).png" style="height: 4vw;width: 4vw;">
                                <img src="../assets/image/star (1).png" style="height: 4vw;width: 4vw;">

                            </div>
                            <p>5.0 月售345单</p>
                            <p>&#165;{{ item.starPrice }}起送 | &#165;5配送 </p>
                        </div>

                        <div class="business-info-star-right">
                            <p>美团专送</p>
                            <span>26分钟 2.8km</span>
                        </div>
                    </div>
                    <div class="business-info-deliver">
                        <span>8.5元无门槛券</span>
                    </div>
                    <div class="business-info-explain">
                        <div>{{ item.businessExplain }}</div>
                        <span>近30天有236人复购</span>

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
    data() {
        return {
            category_list: [],
            business_list: [],
            account: {},
            cartArr: [],
            addressComponentStr:'',
            isAddressShow:true,
        }
    },
    components: {
        Footer,
    },
    methods: {
        toList() {
            this.$router.push('/businessList')
        },
        toBuy(id) {
            this.$router.push({ path: '/businessDetail', query: { businessId: id } })
        },
        getCategory_List() {
            let url = "/category/list"
            // let url = "/CategoryHandler/query"
            this.$axios.get(url).then((response) => {
                this.category_list = response.data.resultdata
                console.log(response.data.resultdata);
            }).catch((error) => {
                console.log(error);
            })
        },
        getBusiness_List() {
            let url = "/business/list"
            // let url = "/BusinessHandler/query?categoryId=0"
            this.$axios.get(url).then((response) => {
                this.business_list = response.data.resultdata
                console.log(response.data.resultdata);

                if (this.account != null) {
                    this.loadCartList();
                }
            }).catch((error) => {
                console.log(error);
            })
        },
        toBusiness_List(id) {
            this.$router.push({
                path: '/BusinessList',
                query: {
                    categoryId: id
                }
            })
        },
        loadCartList() {
            let url = '/cart/listCart/' + this.account.accountId;
            this.$axios.get(url).then((response) => {
                let cartArr = response.data.resultdata
                for (let i in this.business_list) {
                    this.business_list[i].quantity = 0;
                    for (let j in cartArr) {
                        if (this.business_list[i].businessId == cartArr[j].businessId) {
                            this.business_list[i].quantity += cartArr[j].quantity
                        }
                    }
                }
                this.business_list.sort()
            }).catch((error) => {
                console.log(error)
            })
        }, 
        getAddressBybaidu() {
            // 从sessionStorage中读取addressComponent数据
            const addressComponentStr = sessionStorage.getItem('addressComponent');

            // 检查addressComponentStr是否为空或未定义
            if (!addressComponentStr) {
                // 处理空字符串或未定义的情况
                this.addressComponent = {}; // 提供一个默认的JSON对象
            } else {
                // 将addressComponentStr转换回JavaScript对象
                this.isAddressShow = false
                this.addressComponent = JSON.parse(addressComponentStr);
            }
        },
        // 新增方法：打开百度地图页面
        openBaiduMap() {

            // 使用Vue Router进行页面跳转
            this.$router.push({ path: '/baidu', query: { accountId: this.accountId } })
        },

    },
    created() {
        this.getCategory_List();
        this.getBusiness_List();
        this.account = this.$getSessionStorage('account')
        this.getAddressBybaidu()
    },
    mounted() { //挂载完毕的钩子函数，设置首页搜索栏不滚动
        document.onscroll = () => {
            //获取滚动条位置
            let s1 = document.documentElement.scrollTop
            let s2 = document.body.scrollTop
            let scroll = s1 == 0 ? s2 : s1
            //获取视口宽度
            let width = document.documentElement.clientWidth

            //获取顶部固定块
            let search = this.$refs.fixedBox
            //判断滚动条超过视口宽度的12%时，搜索块变固定定位
            if (scroll > width * 0.12) {
                search.style.position = 'fixed'
                search.style.left = '0'
                search.style.top = '0'
            } else {
                search.style.position = 'static'
            }
        }
    },
    destroyed() {
        //当切换到其他组件时，就不需要document滚动条事件，所以将此事件去掉
        document.onscroll = null
    },

}
</script>

<style scoped>
.wrapper {
    width: 100%;
    height: 100%;
    margin-bottom: 14vw;
}

/*头部*/
.wrapper .header {
    width: 100%;
    height: 10vw;
    background: linear-gradient(to right, #403A3E, #BE5869);
    display: flex;
    align-items: center;
    justify-content: space-between;
}

.wrapper .header .location-text {
    font-size: 4vw;
    font-weight: 600;
    color: #d2d2d2;
    margin: 0 1vw 3vw;
}

.wrapper .header .weather {
    margin-right: 2vw;
    color: #393d48;
    align-items: center;
}

.wrapper .header .weather img {
    height: 6vw;
    width: 6vw;
}

.wrapper .header .location-text .fa-angle-down {
    margin-left: 1.5vw;
}

/*搜索栏*/
.wrapper .search {
    width: 100%;
    height: 15vw;
    margin-top: -2vw;
}

.wrapper .search .search-top {
    width: 100%;
    height: 15vw;
    background: linear-gradient(to right, #403A3E, #BE5869);
    display: flex;
    justify-content: center;
    align-items: center;
}

.wrapper .search .search-box {
    width: 90%;
    height: 9vw;
    background-color: #fff;
    border-radius: 1vw;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 3.5vw;
    color: #79859e;
    padding: 0 10vw;
    box-sizing: border-box;
}

.wrapper .search .search-box input {
    width: 100%;
    height: 4.5vw;
    line-height: 4.5vw;
    font-size: 4vw;
    border: none;
    outline: none;
    margin-left: 2vw;
}

/*展示栏*/
.wrapper .category-ul {
    width: 100%;
    height: 40vw;
    display: flex;
    flex-wrap: wrap;
    justify-content: space-around;
    align-content: center;
}

.wrapper .category-ul li {
    width: 18vw;
    height: 18vw;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    user-select: none;
    cursor: pointer;
}

.wrapper .category-ul li img {
    width: 12vw;
    height: 10.3vw;
}

.wrapper .category-ul li p {
    font-size: 3.2vw;
    color: #79859e;
}

.wrapper .category-ul li:hover {
    background-color: #bec9e1;
    border-radius: 2vw;
}

/*广告栏*/
.wrapper .banner {
    width: 95%;
    margin: 0 auto;
    height: 29vw;
    background-image: url(../assets/image/index_banner.png);
    background-repeat: no-repeat;
    background-size: cover;
    box-sizing: border-box;
    padding: 3vw 6vw;
}

.wrapper .banner h3 {
    font-size: 5vw;
    margin-bottom: 1.8vw;
}

.wrapper .banner p {
    font-size: 3.4vw;
    color: #79859e;
    margin-bottom: 1.8vw;
}

.wrapper .banner a {
    font-size: 3.6vw;
    color: #ff3d8d;
    ;
    cursor: pointer;
}

/*超级会员*/
.wrapper .superNumber {
    width: 95%;
    margin: 0 auto;
    height: 11.5vw;
    background: linear-gradient(to right bottom, #fffc5fd5, #fdad00a8);
    margin-top: 1.3vw;
    justify-content: space-around;
    align-items: center;
    border-radius: 1vw;
    color: #817e7e;
    display: flex;
}

.wrapper .superNumber .left {
    display: flex;
    align-items: center;
    margin-left: 4vw;
    user-select: none;
}

.wrapper .superNumber .left img {
    width: 6vw;
    height: 6vw;
    margin-right: 2vw;
}

.wrapper .superNumber .left h3 {
    font-size: 4vw;
    margin-right: 2vw;
}

.wrapper .superNumber .left p {
    font-size: 3vw;
}

.wrapper .superNumber .right {
    font-size: 3.4vw;
    margin-right: 4vw;
}

/*推荐商家*/
.wrapper .show-business {
    width: 100%;
    height: 14vw;
    display: flex;
    justify-content: center;
    align-items: center;
}

.wrapper .show-business .show-business-line {
    width: 30vw;
    height: 0.3vw;
    background-color: #fff200;
}

.wrapper .show-business p {
    font-size: 4vw;
    margin: 0.4vw;
    color: #79859e;
}

/*推荐方式*/
.wrapper .showType {
    width: 100%;
    height: 5vw;
    display: flex;
    justify-content: space-around;
    align-items: center;
}

.wrapper .showType li {
    font-size: 3.5vw;
    color: #79859e;
}

/*商家列表*/
.wrapper .business {
    width: 100%;
    margin-bottom: 2vw;
    margin-top: 2vw;
    justify-content: center;
    display: flex;
}

.wrapper .business li {
    width: 98%;
    box-sizing: border-box;
    padding: 2.5vw;
    user-select: none;
    border: 0.5vw solid #e2d1c3;
    display: flex;
    border-radius: 2vw;
    align-items: center;
    position: relative;
}


.wrapper .business li img {
    width: 28vw;
    height: 28vw;
    border-radius: 2vw;
    display: flex;
}

.wrapper .business li .jiaobiao {
    width: 5vw;
    height: 5vw;
    background-color: rgb(240, 51, 51);
    position: absolute;
    top: 2vw;
    left: 27vw;
    text-align: center;
    border-radius: 50%;
    font-size: 4.3vw;
    padding: 0.3vw 0;
    box-sizing: border-box;
    color: #eaeaea;
}

.wrapper .business li .business-info {
    width: 100%;
    box-sizing: border-box;
    padding: 0.3vw;
    margin-left: 2vw;

}

.wrapper .business li .business-info .business-info-h {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.wrapper .business li .business-info .business-info-h h3 {
    font-size: 5vw;
}

.wrapper .business li .business-info .business-info-h .business-info-like {
    width: 1.6px;
    height: 3.4vw;
    color: #ff3d8d;
    font-size: 4vw;
    margin-right: 4vw;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.wrapper .business li .business-info .business-info-star {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 3.1vw;

}

.wrapper .business li .business-info .business-info-star .business-info-star-left {
    font-size: 3vw;
    display: flex;
    flex-flow: wrap;
}

.wrapper .business li .business-info .business-info-star .business-info-star-left .star {
    display: flex;
}

.wrapper .business li .business-info .business-info-star .business-info-star-left p {
    color: #666;
    margin-left: 1vw;
}

.wrapper .business li .business-info .business-info-star .business-info-star-right {
    text-align: center;
    flex-wrap: wrap;
}

.wrapper .business li .business-info .business-info-star .business-info-star-right p {
    border: 0.4vw solid #ffe601;
    border-radius: 0.5vw;
    color: #f6c608;
    font-size: 2.8vw;
    width: 15vw;
    margin-left: 3vw;
}

.wrapper .business li .business-info .business-info-star .business-info-star-right span {
    font-size: 2.5vw;
}

.wrapper .business li .business-info .business-info-deliver {
    display: flex;
    flex-direction: column;

}

.wrapper .business li .business-info .business-info-deliver p {

    color: #666;
    font-size: 2.6vw;
    margin-bottom: 1vw;
}

.wrapper .business li .business-info .business-info-deliver span {
    background-color: #ff9494;
    font-size: 2.6vw;
    border: 1px solid #bcbcbc;
    border-radius: 0.5vw;

    color: #d40d2e;
    display: flex;
    width: 25vw;
    text-align: center;
    justify-content: center;
}

.wrapper .business li .business-info .business-info-explain {
    display: flex;
    justify-content: left;
    align-items: center;
}

.wrapper .business li .business-info .business-info-explain div {
    border: 0.1vw solid #e2d1c3;
    font-size: 3vw;
    border-radius: 1vw;
    padding: 0 2vw;
    margin-top: 1vw;

}

.wrapper .business li .business-info .business-info-explain span {
    border: 0.1vw solid #e2d1c3;
    color: #f6c608;
    font-size: 3vw;
    border-radius: 1vw;
    padding: 0 2vw;
    margin-top: 1vw;
    margin-left: 1vw;
}
</style>
