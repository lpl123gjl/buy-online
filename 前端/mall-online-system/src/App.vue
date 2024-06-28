<template>
    <router-view/>
</template>
<script>
  export default{
    name:'App',
    watch:{
      $route(to,from){
        if(to.path!='/login'){
          let obj = {
            name : to.name,
            title : to.meta.title,
            icon : to.meta.icon,
            closable:true
          }
          this.$store.commit('ADD_TABS',obj)
        }
      }
    },
    mounted(){
    this.$store.commit('INIT_TABS');
  },
  }
</script>
<style>
  html,body{
    height: 100%;
    margin: 0;
    padding: 0;
    font-size: 14px;
    font-family: 'Microsoft Yahei';
    color:#000;
    text-align: left;
  }
</style>
