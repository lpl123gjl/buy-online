<template>
  <div class="wrapper">
    <!-- 头部分 -->
    <div class="header">
      <a><i class="fa fa-chevron-left" aria-hidden="true" @click="goBack" /></a>
      <p>修改性别</p>
    </div>
    修改性别
    <div class="name">
      <div class="name-modify">输入性别：</div>
      <div class="content">
        <input type="text" placeholder="请输入新的性别" v-model="newGender" />
      </div>
    </div>
    <div class="button">
      <button @click="showModal">修改</button>
    </div>
    <Footer></Footer>

    <!-- 弹窗组件 -->
    <div class="modal" v-if="modalVisible">
      <div class="modal-content">
        <span class="close" @click="closeModal()">&times;</span>
        <p>{{ modalMessage }}</p>
      </div>
    </div>
  </div>
</template>
  <div class="wrapper">
     <!-- 头部分 -->
     <div class="header">
            <a href="myself-information.html"><i class="fa fa-chevron-left" aria-hidden="true" @click="goBack"/></a>
            <p>修改性别</p>
        </div>
        修改性别
        <div class="name">
                <div class="name-modify">输入性别：</div>
                <div class="content">
                    <input type="text" placeholder="请输入新的性别"/>
                </div>
            </div>
        <div class="button">
            <button>修改</button>
        </div>
    <Footer></Footer>
    <div class="modal" v-if="modalVisible">
      <div class="modal-content">
        <span class="close" @click="closeModal">&times;</span>
        <p>{{ modalMessage }}</p>
      </div>
    </div>
  </div>
</template>

<script>
import Footer from '@/components/Footer.vue';
export default {
  data() {
    return {
      newGender: '',
      modalVisible: false,
      modalMessage: ''
    }
  },
  components: {
    Footer,
  },
  methods: {
    goBack() {
      this.$router.go(-1);
    },
    showModal() {
      if (this.newGender === '男' || this.newGender === '女') {
        this.modalMessage = '修改成功';
      } else {
        this.modalMessage = '请不要乱填性别';
      }
      this.modalVisible = true;
    },
    closeModal() {
      this.modalVisible = false;
      this.newGender = '';
    }
  },
  watch: {
    newGender(newValue) {
      if (newValue !== '男' && newValue !== '女') {
        this.newGender = '';
      }
    }
  }
}

</script>
<style scoped>
/* 弹窗样式 */
.modal {
  display: block;
  position: fixed;
  z-index: 1;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  overflow: auto;
  background-color: rgba(0, 0, 0, 0.4);
}

.modal-content {
  background-color: #fefefe;
  margin: 15% auto;
  padding: 20px;
  border: 1px solid #888;
  width: 80%;
}

.close {
  color: #aaa;
  float: right;
  font-size: 28px;
  font-weight: bold;
}

.close:hover,
.close:focus {
  color: black;
  text-decoration: none;
  cursor: pointer;
}

/* 头部分 */
.wrapper .header{ width:100%;height:12vw;background: linear-gradient(to right,#403A3E,#BE5869);color:#79859e;font-size: 5vw;
            position:fixed;left:0;top:0;
            display:flex;align-items: center;}
        .wrapper .header i{display: flex;justify-content: left;padding-left: 3vw;cursor: pointer;}
        .wrapper .header p{ font-weight: 500;padding-left: 33vw;}

        /* 修改用户名 */
        .wrapper .name{width:100%; margin-top:7vw; padding:12vw 0 0 0;box-sizing:border-box;padding:4vw 3vw 0 3vw;
            display:flex;align-items:center;}
        .wrapper .name .name-modify{font-size:4vw;font-weight:600;color:#79859e};
        .wrapper .name .content{flex:1;border-bottom: 1px solid #e0e0e0;}
        .wrapper .name .content input{ border: none;outline:none;width:100%;height:4vw;font-size:3.5vw;color: #79859e;}
        .wrapper .button{width:100%;box-sizing:border-box;padding:3vw 2vw 0 2vw;margin-top: 3vw;}
        .wrapper .button button{width:100%;height:10vw;font-size:3.8vw;font-weight:500;color: #79859e;
            background: linear-gradient(to right bottom,#fd746c,#ff9068);border-radius:2vw;border:none;outline:none;cursor: pointer;}
</style>