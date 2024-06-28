<template>
    <div class="wrapper">
        <!--头部-->
        <header>
            <a>
                <i class="fa fa-angle-left" aria-hidden="true" @click="goBack"/>
            </a>
            <p>个人信息</p>
        </header>

        <!--内容-->
        <div class="content">
            <div class="head">
                <span>头像</span>
                <div class="head-right" @click="openModal">
                    <a id="myBtn">
                        <img src="../../assets/image/tou1.jpg">
                    </a>
                    <i class="fa fa-angle-right" aria-hidden="true" />
                </div>

            </div>
            <div class="name">
                <span>姓名</span>
                <div class="name-right" @click="toName">
                    <span>徐广燎</span>
                    <a >
                        <i class="fa fa-angle-right" aria-hidden="true" />
                    </a>
                </div>

            </div>
            <div class="phone">
                <span>手机号</span>
                <div class="phone-right" @click="toPhone">
                    <span>1234565</span>
                    <a >
                        <i class="fa fa-angle-right" aria-hidden="true" />
                    </a>
                </div>
            </div>
            <div class="sex">
                <span>性别</span>
                <div class="sex-right" @click="toSex">
                    <span>女</span>
                    <a >
                        <i class="fa fa-angle-right" aria-hidden="true" />
                    </a>

                </div>
            </div>
        </div>

        
        <Footer></Footer>

         <!-- 弹窗 -->
         <div id="myModal" class="modal"  v-if="modalOpen">

<!-- 弹窗内容 -->
<div class="modal-content">
    <div class="all">
        <div class="background">

            <p>拍一张</p>
            <p>从手机相册选择</p>
            <p style="padding-top: 2vw; border: none;font-size: 3vw;color: #a3a3a3;">使用默认头像库</p>
            <ul>
                <li><img src="../../assets/image/头像3.png"></li>
                <li><img src="../../assets/image/头像3.png"></li>
                <li><img src="../../assets/image/头像3.png"></li>
                <li><img src="../../assets/image/头像3.png"></li>
                <li><img src="../../assets/image/头像3.png"></li>
            </ul>
        </div>
    </div>
    <div class="close" @click="closeModal">
        <span>取消</span>
    </div>
</div>
</div>

    </div>
</template>

<script>
import Footer from '@/components/Footer.vue';

export default {
    data() {
        return{
            modalOpen: false,
        };
    },
    methods: {
        openModal() {
            this.modalOpen = true;
        },
        closeModal() {
            this.modalOpen = false;
        },
        toName(){
            this.$router.push('/change-name')
        },
        toPhone(){
            this.$router.push('/change-phone')
        },
        toSex(){
            this.$router.push('/change-sex')
        },
        goBack() {
            this.$router.go(-1);
        },
    },

    components: {
        Footer,
    },
}
</script>
<style scoped>
.wrapper {
    width: 100%;
    height: 100%;
    position: relative;
}

.wrapper header {
    width: 100%;
    height: 10vw;
    background: linear-gradient(to right, #403A3E, #BE5869);
    display: flex;
    justify-content: center;
    align-items: center;
}

.wrapper header p {
    color: #79859e;
    font-size: 5vw;
}

.wrapper header .fa-angle-left {
    position: fixed;
    left: 2vw;
    top: 2vw;
    font-size: 7vw;
    color: #79859e;
}

/*内容样式*/
.wrapper .content {
    width: 100%;
    display: flex;
    flex-direction: column;
}

.wrapper .content span {
    margin-left: 5vw;
    color: #919191;
    font-size: 5vw;
}

.wrapper .content i {
    font-size: 5vw;
    margin: auto 3vw;
}

.wrapper .content .head {
    width: 100%;
    height: 12vw;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.wrapper .content .head .head-right {
    display: flex;
    align-items: center;
}

.wrapper .content .head img {
    height: 10vw;
    width: 10vw;
}

.wrapper .content .head i {
    font-size: 5vw;
    margin: auto 3vw;
}

.wrapper .content .name {
    width: 100%;
    height: 12vw;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.wrapper .content .phone {
    width: 100%;
    height: 12vw;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.wrapper .content .sex {
    width: 100%;
    height: 12vw;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

/* 弹窗 (background) */
.modal {
    display: block;
    position: fixed;
    z-index: 1;
    left: 0;
    bottom: 0; /* 修改为 bottom: 0 */
    width: 100%;
    height: 100%;
    overflow: auto;
    background-color: rgba(0, 0, 0, 0.4);
}

/* 弹窗内容 */
.modal-content {
    background-color: #d0d0d0;
    border: 1px solid #888;
    width: 80%;
    max-width: 600px;
    height: auto;
    border-radius: 4vw 4vw 0 0; /* 修改为只有上部分有圆角 */
    font-size: 5vw;
    box-sizing: border-box;
    position: fixed;
    bottom: 0; /* 修改为 bottom: 0 */
    left: 50%;
    transform: translateX(-50%); /* 修改为只水平居中 */
}

.modal-content .all {
    width: 100%;
    background-color: #fff;
    border-radius: 4vw 4vw 0 0; /* 保持上部分圆角 */
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    text-align: center;
}

.modal-content .all .background {
    width: 100%;
    box-sizing: border-box;

}

.modal-content .all .background p {
    width: 100%;
    border-bottom: 1px solid #dddddd;
    padding: 3vw 0;
    font-size: 4.5vw;
    font-family: "黑体";
}

.modal-content .all .background ul {
    display: flex;
    justify-content: space-around;
}

.modal-content .all .background ul li img {
    width: 9vw;
    height: 9vw;
}

/* 关闭按钮 */
.close {
    color: #aaa;
    font-size: 4vw;
    font-weight: 500;
    position: relative;
    margin: 0 auto;
    bottom: 0vw;
    display: flex;
    justify-content: center;
    background-color: #fff;
    padding: 3vw 0;
    box-sizing: border-box;
}

.close:hover.close:focus {
    color: black;
    text-decoration: none;
}
</style>