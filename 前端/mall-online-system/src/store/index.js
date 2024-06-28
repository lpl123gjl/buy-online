import Vue from 'vue'
import Vuex from 'vuex'
import menus from './modules/menu'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    token:'',
  },
  getters: {
  },
  mutations: {
    SET_TOKEN:(state,tokenValue)=>{
      state.token = tokenValue
      sessionStorage.setItem('TOKEN',JSON.stringify(tokenValue));
    }
  },
  actions: {
  },
  modules: {
    menus,
  }
})
