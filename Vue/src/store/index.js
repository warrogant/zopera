import router, { resetRouter } from '@/router'
import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    currentPathName:''
  },
  getters: {
  },
  mutations: {
    setPath(state){
      state.currentPathName = localStorage.getItem('currentPathName')
    },
    logout(){
      // 清空缓存
      localStorage.removeItem('userInfo')
      localStorage.removeItem('menus')
      router.push('/login')
      // 重置路由
      resetRouter()
    }
  },
  actions: {
  },
  modules: {
  }
})
