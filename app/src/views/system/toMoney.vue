<template>
  <div class="wrapper">
        <!-- 头部 -->
        <div class="header">
            <div class="header-left">
                    <i @click="back" class="fa fa-chevron-left" aria-hidden="true" />
                <span>账户余额</span>
            </div>
            <div class="header-right">
                <img src="../../assets/image/三点.png">
            </div>
        </div>
        <!-- 余额 -->
        <div class="money">
            <div class="money-top">
                <p>余额(元)</p>
            </div>
            <div class="money-center">
                <h3>58.00</h3>
            </div>
            <div class="money-bottom">
                <p>去提现<span style="font-size: 5vw;">&#8250;</span></p>
            </div>
        </div>
        <!-- 充值 -->
        <div class="up">
            <div class="up-header">
                <p>余额账户充值</p>
            </div>
            <ul class="up-content">
                <li :class="{ active: selectedAmount === 10 }" @click="selectAmount(10)">10<span>元</span></li>
                <li :class="{ active: selectedAmount === 30 }" @click="selectAmount(30)">30<span>元</span></li>
                <li :class="{ active: selectedAmount === 99 }" @click="selectAmount(99)">99<span>元</span></li>
                <li :class="{ active: selectedAmount === 188 }" @click="selectAmount(188)">188<span>元</span></li>
                <li :class="{ active: selectedAmount === 288 }" @click="selectAmount(288)">288<span>元</span></li>
                <li :class="{ active: selectedAmount === x }" @click="selectAmount(x)" style="font-size: 4vw;font-weight: 500;">自定义金额</li>
            </ul>
        </div>

        <!-- 按钮 -->
        <footer class="go">
            <div class="go-top">
                <input type="checkbox" v-model="gou">
                <span>本人阅读并同意</span>
                <span style="color: #000;">《饿了吧充值须知》</span>
            </div>
            <button id="myBtn" @click="go">开通并充值</button>
        </footer>
        <!-- 弹窗 -->
        <div id="myModal" class="modal" v-if="flag">

            <!-- 弹窗内容 -->
            <div class="modal-content">
                <span class="close" @click="go_close">&times;</span>
                <p v-if='gou'>卡内余额不足！</p>
                <p v-else>请阅读并勾选充值须知！</p>
            </div>

        </div>
    </div>
</template>

<script>
export default {
  data () {
    return {
        gou:'',
        flag:false,
        x:0,
        selectedAmount: 0
    }
  },
  components: {

  },
  methods: {
    selectAmount(amount) {
      this.selectedAmount = amount;
    },
    back(){
        this.$router.back();
    },
    go(){
        this.flag = true;
    },
    go_close(){
        this.flag = false;
    }
  },
}
</script>
<style scoped>
  .wrapper {
        width: 100%;
        height: 220vw;
    }

    /* 头部 */
    .wrapper .header {
        width: 100%;
        height: 16vw;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .wrapper .header .header-left {
        font-size: 4.3vw;
        font-weight: 600;
        display: flex;
    }

    .wrapper .header .header-left i {
        font-size: 5.6vw;
        padding: 2vw 0 0 3vw;
        color: #707070;
    }

    .wrapper .header .header-left span {
        padding-left: 4vw;
        padding-top: 1.7vw;
    }

    .wrapper .header .header-right {
        position: relative;
        top: 1.3vw;
        right: 3vw;
    }

    /* 余额 */
    .wrapper .money {
        width: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        margin: 3vw 0;
    }

    .wrapper .money .money-top {
        font-size: 3.4vw;
        color: #5d5d5d;
    }

    .wrapper .money .money-center {
        font-size: 7.2vw;
        margin: 1.2vw 0 0 0;
    }

    .wrapper .money .money-bottom {
        font-size: 3.4vw;
        color: #5d5d5d;
    }

    .wrapper .money .money-bottom p span {
        margin-left: 1vw;
    }

    /* 充值 */
    .wrapper .up {
        width: 100%;
    }

    .wrapper .up .up-header {
        width: 100%;
        margin-bottom: 3vw;
        padding-left: 3vw;
        box-sizing: border-box;
        font-size: 4.3vw;
        font-weight: 600;
    }

    .wrapper .up .up-content {
        display: flex;
        flex-wrap: wrap;
        justify-content: center;
        align-items: center;
        box-sizing: border-box;
    }

    .wrapper .up .up-content li {
        width: 30vw;
        height: 23vw;
        margin: 1vw;
        background-color: #f6f6f6;
        border-radius: 1vw;
        font-size: 6.8vw;
        text-align: center;
        line-height: 23vw;
        font-weight: 600;
        box-sizing: border-box;
        border: 0.5vw solid #fff;
    }

    .wrapper .up .up-content li span {
        font-size: 3.4vw;
        font-weight: 500;
        color: #292929;
        box-sizing: border-box;
    }

    .wrapper .up .up-content .active {
        box-sizing: border-box;
        border: 0.5vw solid #53a9ff;
        background-color: #f6f6f670;
    }

    /* 按钮 */
    .wrapper .go {
        width: 100%;
        padding: 2vw 3vw;
        box-sizing: border-box;
        position: absolute;
        bottom: 6vw;
    }

    .wrapper .go .go-top {
        font-size: 3.6vw;
        margin-bottom: 2.5vw;
        display: flex;
        align-items: center;
    }

    .wrapper .go .go-top span {
        color: #5d5d5d;
        padding-bottom: 0.8vw;
        margin-left: 1vw;
    }

    .wrapper .go button {
        width: 100%;
        height: 10vw;
        background-color: rgb(32, 181, 255);
        border: none;
        border-radius: 5vw;
        color: #fff;
        font-size: 4.6vw;
        font-weight: 600;
    }

    /* 弹窗 (background) */
    .modal {
        display: block;
        /* 默认隐藏 */
        position: fixed;
        /* 固定定位 */
        z-index: 1;
        /* 设置在顶层 */
        left: 0;
        top: 0;
        width: 100%;
        height: 100%;
        overflow: auto;
        background-color: rgb(0, 0, 0);
        background-color: rgba(0, 0, 0, 0.4);
    }

    /* 弹窗内容 */
    .modal-content {
        background-color: #fefefe;
        margin: 65% auto;
        padding: 10vw 5vw 10vw 5vw;
        border: 1px solid #888;
        width: 80%;
        border-radius: 2vw;
        font-size: 5vw;
    }

    /* 关闭按钮 */
    .close {
        color: #aaa;
        float: right;
        font-size: 8vw;
        font-weight: bold;
        position: relative;
        bottom: 1vw;
    }

    .close:hover.close:focus {
        color: black;
        text-decoration: none;
    }
</style>