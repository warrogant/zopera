<template>
    <div class="backtop" style="height: 800px;overflow-y: auto">
    <div v-for="item in 500" :key="item">{{item}}
    </div>
    //target属性的值指向的就是外层你滚动区域的类名
    /*visibility-height需要的是一个number，所以需要v-bind绑定；
    *补充（v-bind缩写:）
    */
    <el-backtop target=".backtop" :visibility-height=400></el-backtop>
    </div>

  </template>
  <script>
  export default {
    data() {
      return {
      };
    },
    computed: {},
    methods: {
    },
  };
  </script>
  <style scoped>
  
  </style>
  