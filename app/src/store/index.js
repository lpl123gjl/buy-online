import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    msg:'hello Vuex'
  },
  getters: {
  },
  mutations: {
    SET_MSG:(state,value)=>{
      state.msg = value
    }
  },
  actions: {
    SET_MSG2:({commit},value)=>{
      commit('SET_MSG',value);
    }
  },
  modules: {
  }
})
