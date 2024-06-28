<template>
  <div>
    <h3>子组件</h3>
    <p>学校：{{ school }} 地址：{{ address }}</p>
    <p>学校：{{ school }} 地址：{{ address }}</p>
    <p>密码: {{ password }}</p>
    <h3 style="color: red;">{{ $store.state.msg }}</h3>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             
    <button @click="send">发送参数</button>
    <button @click="set_msg">修改msg</button>
    <button @click="set_msg2">修改msg_异步</button>
  </div>
</template>

<script>
export default {
  data () {
    return {
        text:'熊猫大侠'
    }
  },
  components: {

  },
  props:["school",'address','password'],
  methods: {
    send(){
        this.$emit('apps',this.text);
    },
    set_msg(){
        this.$store.commit('SET_MSG','修改MSG')
    },
    set_msg2(){
        this.$store.dispatch('SET_MSG2','异步修改MSG')
    }
  },
}

</script>
<style scoped>
</style>