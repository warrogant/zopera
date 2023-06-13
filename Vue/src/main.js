import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import '@/assets/css/base.css'
import '@/assets/font/iconfont'
import '@/assets/font/iconfont.css'
import Fragment from 'vue-fragment'
// import * as echarts from 'echarts';
// element-ui
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
// 清除默认样式
import '@/assets/css/base.css'
import request from './utils/request'
/* 引入资源请求插件 */
import VueResource from 'vue-resource'
import less from 'less'
// 时间格式化 
import dayjs from 'dayjs'


Vue.config.productionTip = false
Vue.use(ElementUI,{size:'small'})
// 全局注册
Vue.prototype.request = request
Vue.prototype.dayjs = dayjs;
/* 使用VueResource插件 */
Vue.use(VueResource)
Vue.use(less)
Vue.use(Fragment.Plugin)
// Vue.use(echarts)

Vue.filter('dateFormat', function(value) {
  // 对传入的时间进行格式化处理
  // 这里假设 value 是一个字符串，格式为 'YYYY-MM-DD'
  // + ' ' + date.toLocaleTimeString()
  const date = new Date(value)
  // const date = this.dayjs(value).format("YYYY-MM-DD");
  return date.toLocaleDateString() 
})

Vue.filter('datetime', function(value) {
  // 对传入的时间进行格式化处理
  // 这里假设 value 是一个字符串，格式为 'YYYY-MM-DD HH:mm:ss'
  const date = new Date(value)
  return date.toLocaleDateString() + ' ' + date.toLocaleTimeString()
})


Vue.filter('formatData', function(cellValue){
		
  //把传过来的日期进行回炉重造一下，又创建了一个js的 Date对象，进行重新构造，转为String字符串
  //最终返回 s 就可以了
  var s =	new Date(cellValue).toLocaleString();
    return s;

    // 测试参数含义：不知道参数是什么含义的就打印出来
    // console.log(row)     	//拿到具体信息,只需要row.XXX 就可以了
    // console.log(column)  	//拿到列的信息
    // console.log(cellValue) //拿到单元格数据，拿到时间 2022-03-18T01:46:08.000+00:00
    // console.log(index)     //拿到索引
    })

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')



