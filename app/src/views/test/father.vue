<template>
  <div>
    <h3>父组件</h3>
    <h1>{{ $store.state.msg }}</h1>
    <child school="蟹皇堡" address="比奇堡" :password="p+1" @apps="get_value"></child>
  </div>
</template>

<script>
import child from '@/views/test/child'
export default {
  data () {
    return {
        p:123
    }
  },
  components: { 
    child
  },
  methods: {
    get_value(value){
        console.log('子组件传递参数：'+value)
    }
  },
}

</script>
<style scoped>
</style>