<template>
  <div class="wrapper">
    <!-- 头部分 -->
    <div class="header">
      <a><i class="fa fa-chevron-left" aria-hidden="true" @click="goBack" /></a>
      <p>修改手机号</p>
    </div>
    修改性别
    <div class="name">
      <div class="name-modify">输入手机号：</div>
      <div class="content">
        <input type="text" placeholder="请输入新的手机号" v-model="newPhoneNumber" />
      </div>
    </div>
    <div class="button">
      <button @click="updatePhoneNumber">修改</button>
    </div>
    <!-- 弹窗组件 -->
    <Footer></Footer>
    <transition name="modal">
      <div class="modal-mask" v-if="showModal">
        <div class="modal-wrapper">
          <div class="modal-container">
            <div class="modal-body">
              <p>{{ modalContent }}</p>
            </div>
            <div class="modal-footer">
              <button @click="showModal = false">确定</button>
            </div>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import Footer from '@/components/Footer.vue';
export default {
  data() {
  return {
    newPhoneNumber: '',
    showModal: false, // 控制弹窗的显示和隐藏
    modalContent: '' // 存储弹窗内容
  }
},
  components: {
    Footer,
  },
  methods: {
    goBack() {
      this.$router.go(-1);
    },
    updatePhoneNumber() {
  const phoneNumberRegex = /^[0-9]{11}$/;
  if (phoneNumberRegex.test(this.newPhoneNumber)) {
    this.showModal = true;
    this.modalContent = '修改成功';
    // 在这里可以添加更新手机号的逻辑
  } else {
    this.showModal = true;
    this.modalContent = '手机号输入有误';
  }
},
  },
};
</script>

<style scoped>
.modal-mask {
  position: fixed;
  z-index: 9998;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-container {
  width: 30vw; /* 设置固定宽度 */
  height: 20vw; /* 设置固定高度 */
  position: relative; /* 添加 position 属性 */
  margin: 0 auto;
  padding: 20px 30px;
  background-color: #fff;
  border-radius: 5px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.33);
  transition: all 0.3s ease;
}

.modal-body {
  margin: 20px 0;
  text-align: center; /* 文本居中 */
}

.modal-footer {
  position: absolute; /* 使用绝对定位 */
  bottom: 20px; /* 距离底部 20px */
  left: 50%; /* 横向居中 */
  transform: translateX(-50%); /* 通过 transform 实现居中 */
}

/* 其他样式保持不变 */

.modal-enter-from {
  opacity: 0;
}

.modal-leave-to {
  opacity: 0;
}

.modal-enter-from .modal-container,
.modal-leave-to .modal-container {
  -webkit-transform: scale(1.1);
  transform: scale(1.1);
}
/* 头部分 */
.wrapper .header {
  width: 100%;
  height: 12vw;
  background: linear-gradient(to right, #403a3e, #be5869);
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
  margin-top: 7vw;
  padding: 12vw 0 0 0;
  box-sizing: border-box;
  padding: 4vw 3vw 0 3vw;
  display: flex;
  align-items: center;
}
.wrapper .name .name-modify {
  font-size: 4vw;
  font-weight: 600;
  color: #79859e;
}
.wrapper .name .content {
  flex: 1;
  border-bottom: 1px solid #e0e0e0;
}
.wrapper .name .content input {
  border: none;
  outline: none;
  width: 100%;
  height: 4vw;
  font-size: 3.5vw;
  color: #79859e;
}
.wrapper .button {
  width: 100%;
  box-sizing: border-box;
  padding: 3vw 2vw 0 2vw;
  margin-top: 3vw;
}
.wrapper .button button {
  width: 100%;
  height: 10vw;
  font-size: 3.8vw;
  font-weight: 500;
color: #79859e;
background: linear-gradient(to right bottom, #fd746c, #ff9068);
border-radius: 2vw;
border: none;
outline: none;
cursor: pointer;
}
</style>