<template>
    <div class="wrapper">

        <header>
            <div @click="back" class="back">
                &lsaquo;
            </div>
            <p>商家列表</p>
        </header>


        <!--列表展示-->
        <div class="show">

            <ul v-for="(item,index) in business_list" :key="item.businessId" @click="toBusiness(item.businessId)">
                <li>
                    <img :src="item.businessImg">
                    <div class="jiaobiao" v-if="item.quantity!=0 && item.quantity != undefined">{{ item.quantity }}</div>
                    <div class="show-right">
                        <div class="header">
                            <span>天天红包</span>
                            <h3>{{item.businessName}}</h3>
                        </div>
                        <div class="star">
                            <p>&#165;{{item.starPrice}}起送 |  {{item.deliveryPrice}}配送 </p>
                        </div>
                        <div class="fire">
                            <img src="../../assets/image/Fire (solid）.png">
                            <span>近7日661人下单</span>
                        </div>
                        <div class="show-explain">{{item.businessExplain}}</div>
                    </div>
                </li>
            </ul>

        </div>
        <Footer></Footer>
    </div>
</template>

<script>
import Footer from '@/components/Footer.vue';
export default {
    data() {
        return {
            categoryId:0,
            business_list:[],
            account:{}
        }
    },
    components: {
        Footer,
    },
    methods: {
        back() {
            this.$router.back();
        },
        loadBusinessListByCategoryId(){
            this.categoryId = this.$route.query.categoryId;
            let url = "/business/listByCategoryId/"+this.categoryId;
            // let url = "/BusinessHandler/query?categoryId="+this.categoryId;
            this.$axios.get(url).then((response)=>{
                this.business_list = response.data.resultdata;
                if(this.account!=null){
                    this.cartList();
                }
                
            }).catch((error)=>{
                console.log(error);
            })
        },
        toBusiness(id){
            this.$router.push({path:'/businessDetail',query:{businessId:id}})
        },
        cartList(){
            let url = '/cart/listCart/'+this.account.accountId;
            this.$axios.get(url).then((response)=>{
                let cartArr =  response.data.resultdata;
                
                for(let i in this.business_list){
                    this.business_list[i].quantity = 0;
                    for(let j in cartArr){
                        if(this.business_list[i].businessId == cartArr[j].businessId){
                            this.business_list[i].quantity += cartArr[j].quantity;
                        }
                    }
                }
                this.business_list.sort();
            }).catch((error)=>{
                console.log(error)
            })
        }
    },
    created(){
        this.loadBusinessListByCategoryId();
        this.account = this.$getSessionStorage('account')
    }
}

</script>
<style scoped>
/*头部样式*/
.wrapper {
    position: relative;
    margin-bottom: 14vw;
}

.wrapper header {
    height: 14vw;
    background: linear-gradient(to right, #403A3E, #BE5869);
    display: flex;
    justify-content: center;
    align-items: center;
}

.wrapper header .back {
    font-size: 8vw;
    color: #5b5b5ba9;
    margin-left: 3.5vw;
    position: fixed;
    left: 0;
}

.wrapper header p {
    color: #79859e;
    font-size: 5vw;
}

/*食堂选项*/
.wrapper .check {
    display: flex;
    justify-content: center;
    margin: 3vw auto;

}

.wrapper .check ul {
    display: flex;
    justify-content: space-around;
    width: 100%;
    background-color: #f9f8f4;
    height: 10vw;
    align-items: center;
}

/*商家列表*/
.wrapper .show {
    display: flex;
    flex-direction: column;
}



.wrapper .show ul li {
    display: flex;
    justify-content: left;
    border-bottom: 0.3vw solid #e2d1c3;
    padding: 2vw 1vw;
    position: relative;
}

.wrapper .show ul li img {
    width: 18vw;
    height: 18vw;
    border-radius: 2vw;
    margin-left: 2vw;
    margin-left: 2vw;
}
.wrapper .show ul li .jiaobiao{
    width: 5vw;height: 5vw;background-color: rgb(240, 51, 51);
    position: absolute;top: 0;left: 18vw;
    text-align: center;border-radius: 50%;
    font-size: 4.3vw;padding: 0.3vw 0;box-sizing: border-box;
    color: #eaeaea;
}

.wrapper .show ul li .show-right {
    width: 100%;
    margin-left: 5vw;
}

.wrapper .show ul li .show-right .header {
    display: flex;
    align-items: center;
}

.wrapper .show ul li .show-right .header span {
    background-color: #e72727;
    color: #fff;
    border-radius: 2vw;
    font-size: 2.6vw;
    width: 15vw;
    height: 5vw;
    text-align: center;line-height: 5.4vw;
}

.wrapper .show ul li .show-right .header h3 {
    font-size: 4.5vw;
    color: #0c0b0b;
    margin-left: 2vw;
}

.wrapper .show ul li .show-right p {
    color: #666;
    margin-top: 1vw;
    font-size: 3vw;
}

.wrapper .show ul li .show-right .star {
    display: flex;
    justify-content: space-between;
    margin-bottom: 2vw;
}

.wrapper .show ul li .show-right .star span {
    margin-right: 3vw;
    color: #666;
    font-size: 3vw;
}

.wrapper .show ul li .show-right .fire {
    background-color: #fdbc08;
    display: flex;
    width: 29.5vw;
    border-radius: 1vw;
    margin-bottom: 2vw;
}

.wrapper .show ul li .show-right .fire img {
    height: 4vw;
    width: 4vw;
}

.wrapper .show ul li .show-right .fire span {
    font-size: 3vw;
    color: #919191;
    background-color: rgb(252, 240, 18);

}

.wrapper .show ul li .show-right .show-explain {
    font-size: 3.5vw;
}

.wrapper .show ul li:hover {
    background: #ddd;
    cursor: pointer;
}
</style>