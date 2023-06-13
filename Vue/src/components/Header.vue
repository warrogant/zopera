<template>
  <div>
    <!-- 收缩按钮 -->
    <div style="flex: 1; float: left; font-size: 20px; width: 40px">
      <span
        :class="collapseBtnClass"
        style="cursor: pointer"
        @click="collapse"></span>
      <div style="margin-top: -28px; margin-left: 60px; width: 200px">
        <!-- 页签 -->
        <el-breadcrumb separator="/">
          <el-breadcrumb-item :to="'/'">首页</el-breadcrumb-item>
          <el-breadcrumb-item>{{currentPathName}}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
    </div>
    <!-- 用户信息 -->
    <div style="float: right; margin-right: 20vw">
      <el-dropdown style="cursor: pointer; width: 100px">
        <div style="display: inline-block;width: 200px;text-align: center;">
          <img :src="user.avator" alt="" style="width: 32px;height: 32px;;border-radius: 50%; margin-right:10px;background-color: #fff;" >
          <span style="margin-right: 10px; font-size: 16px; color: black"
          >{{user.nickname}}</span
        ><i
          class="el-icon-arrow-down"
          style="margin-right: 15px; color: black"></i>
        </div>
        <el-dropdown-menu slot="dropdown" style="width: 120px;text-align: left;margin:2vh -3vw;">
          <el-dropdown-item><router-link to="/person"><i class="el-icon-s-tools"></i>个人信息</router-link> </el-dropdown-item>
          <!-- <el-dropdown-item><router-link to="/password">修改密码</router-link> </el-dropdown-item> -->
          <el-dropdown-item>
              <span style="text-decoration: none;" @click="outLogin"><i class="el-icon-switch-button"></i>退出</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
export default {
    name:'HeaderB',
    props:{
        collapseBtnClass: String,
        collapse: Function,
        user: Object
    },
    data() {
      return {
        // 获取浏览器本地存储中的userInfo
        // user:localStorage.getItem('userInfo') ? JSON.parse(localStorage.getItem('userInfo')) : {}
      }
    },
    methods: {
      // 退出登录
      outLogin(){
      // localStorage.removeItem('userInfo')
        this.$router.push('/login')
        this.$store.commit('logout')
      }
    },
    computed:{
      currentPathName(){
        return this.$store.state.currentPathName  //取出路由信息
      },
    },  
    watch:{
      currentPathName(newVal){
        console.log(newVal);
      }
    },
};
</script>

<style>
/* 页签斜杠 */
.el-breadcrumb__separator {
  color: black !important;
}
</style>
