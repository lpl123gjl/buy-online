import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default{
  state: {
    hasRoute:false,
    menuList:[],
    permList:[],

    TabsList:[
      {
        title:'首页',
        name:'Index',
        icon:'el-icon-s-home',
        closable:false
      }
    ],
    TabsValue:'Index',
  },
 
  mutations: {
    RESET_TABS:(state)=>{
      state.TabsList=[
        {
          title:'首页',
          name:'Index',
          icon:'el-icon-house'
        }
      ];
      state.TabsValue='Index'
    },
    SET_MENULIST:(state,menus)=>{
        sessionStorage.setItem("menuList",JSON.stringify(menus));
        state.menuList = menus;
    },
    SET_PERMLIST:(state,authorities)=>{
        state.permList = authorities;
    },
    SET_HASROUTE:(state,value)=>{
        state.hasRoute = value;
    },
    ADD_TABS:(state,tab)=>{
      let index = state.TabsList.findIndex((item) => item.name === tab.name);
      if(index === -1){
        state.TabsList.push(tab);
      }
        state.TabsValue = tab.name;
        // Save all tabs to sessionStorage
        sessionStorage.setItem("TabsList", JSON.stringify(state.TabsList));
    },
    SET_ACTIVE_TAB:(state,tabName)=>{
      state.TabsValue = tabName;
    },
    INIT_TABS: (state) => {
      // Initialize TabsList when the app loads
      const tabsFromSession = JSON.parse(sessionStorage.getItem("TabsList"));
      if (tabsFromSession) {
          state.TabsList = tabsFromSession;
      }
  },
  },
 
}
