<template>
    <div class="wrapper">
        <!-- 头部分 -->
        <div class="header">
            <i class="fa fa-chevron-left" aria-hidden="true" @click="goBack"></i>
            <p>修改用户名</p>
        </div>
        <div class="name">
            <div class="name-modify">输入用户名：</div>
            <div class="content">
                <input type="text" v-model="username" placeholder="请输入新的用户名" />
            </div>
        </div>
        <div class="button">
            <button @click="modifyUsername">修改</button>
        </div>
        <Footer></Footer>
        <transition name="fade">
            <div class="overlay" v-show="showOverlay"></div>
        </transition>
        <transition name="slide-down">
            <div class="alert" v-show="showAlert">{{ alertMessage }}</div>
        </transition>
    </div>
</template>

<script>
import Footer from '@/components/Footer'
export default {
    data() {
        return {
            username: '',
            showAlert: false,
            showOverlay: false,
            alertMessage: ''
        }
    },
    components: {
        Footer,
    },
    methods: {
        goBack() {
            this.$router.go(-1);
        },
        modifyUsername() {
            if (this.username === '') {
                this.alertMessage = '用户名为空，请输入用户名';
            } else {
                this.alertMessage = '修改成功';
                this.username = '';
            }
            this.showAlert = true;
            this.showOverlay = true;
            setTimeout(() => {
                this.showAlert = false;
                this.showOverlay = false;
            }, 2000);
        }
    },
}

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
    left: 0;
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

/* 修改用户名 */
.wrapper .name {
    width: 100%;
    margin-top: 13vw;
    padding: 2vw 3vw 2vw 3vw;
    display: flex;
    border: 1px solid #eee;
    align-items: center;
    line-height: 4vw;
}

.wrapper .name .name-modify {
    padding: 1vw;
    font-size: 4.5vw;
    font-weight: 500;
    color: #666
}
.wrapper .name .content {
    flex: 1;
}

.wrapper .name .content input {
    border: none;
    outline: none;
    width: 100%;
    height: 4.5vw;
    font-size: 4vw;
    color: #777;
}

.wrapper .button {
    width: 100%;
    box-sizing: border-box;
    padding: 3vw 2vw 0 2vw;
    margin-top: 3vw;
}

.wrapper .button button {
    width: 100%;
    height: 12vw;
    font-size: 4.5vw;
    font-weight: 500;
    color: #666;
    background: linear-gradient(to right bottom, #fd746c, #ff9068);
    border-radius: 2vw;
    border: none;
    outline: none;
    cursor: pointer;
}.fade-enter-active, .fade-leave-active {
    transition: opacity .5s;
}
.fade-enter, .fade-leave-to {
    opacity: 0;
}
.slide-down-enter-active, .slide-down-leave-active {
    transition: transform .5s;
}
.slide-down-enter, .slide-down-leave-to {
    transform: translateY(-100%);
}
.overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, .5);
    z-index: 999;
}
.alert {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    padding: 12px;
    background: #f44336;
    color: #fff;
    text-align: center;
    z-index: 1000;
}
</style>
/* 底部导航 */
</style>