<template>
  <el-tabs v-model="TabsValue" type="card" @tab-click="clickTab" @tab-remove="removeTab">
    <el-tab-pane v-for="item in TabsList" :key="item.name" :name="item.name" :closable="item.closable">
        <span slot="label"><i :class="item.icon"></i> {{ item.title }}</span>
    </el-tab-pane>
  </el-tabs>
</template>

<script>
export default {
  data () {
    return {

    }
  },
  computed:{
    TabsList:{
        get(){
            return this.$store.state.menus.TabsList;
        },
        set(val){
            this.$store.state.menus.TabsList = val;
        }
    },
    TabsValue:{
        get(){
            return this.$store.state.menus.TabsValue;
        },
        set(val){
            this.$store.state.menus.TabsValue = val;
        }
    }
  },
  components: {

  },
 
  methods: {
    clickTab(target){
        this.$router.push({name:target.name});    
    },
    removeTab(targetName){
        if(targetName === 'Index'){
            return
        }
        let tabs = this.$store.state.menus.TabsList;
        let activeName = this.$store.state.menus.TabsValue;
        if(targetName === activeName){
            tabs.forEach((item,index)=>{
                if(item.name === targetName){
                    let nextTab = tabs[index + 1] || tabs[index - 1];
                    if(nextTab){
                        activeName = nextTab.name
                    }
                }
            })
        }
        this.TabsValue = activeName;

        this.TabsList = tabs.filter((tab) => tab.name != targetName);
        
        this.$router.push({name:activeName})
       
        
    }
  },
}

</script>
<style scoped>
</style>