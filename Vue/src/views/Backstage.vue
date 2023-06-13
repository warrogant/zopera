<template>
  <div class="home">
    <el-container style="height: 100vh">
      <!-- 侧边栏 -->
      <el-aside :width="sideWidth + 'px'">
        <Aside :isCollapse="isCollapse" :logoText="logoText" ></Aside>
      </el-aside>
      <!-- 表格数据 -->
      <el-container>
        <!-- 顶部导航栏 -->
        <el-header>
          <Header :collapseBtnClass="collapseBtnClass" :collapse="collapse" :user="user"></Header>
        </el-header>
        <!-- 主体内容 -->
        <el-main style="margin-top: 20px">
          <router-view @refreshUser="getUser" />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import Aside from '@/components/Aside.vue';
import Header from '@/components/Header.vue';
export default {
  name: 'BackstageHome',
  data() {
    return {
      collapseBtnClass: 'el-icon-s-fold',
      isCollapse: false,
      sideWidth: 200,
      logoText: true,
      currentPage4: 4,
      user: {},
      menu:[]
    };
  },
  components: { Aside, Header },
  created() {
    // 从后台获取最新的数据
    this.getUser()
  },
  methods: {
    // 收缩按钮
    collapse() {
      this.isCollapse = !this.isCollapse;
      if (this.isCollapse) {
        this.sideWidth = 65;
        this.logoText = false;
        this.collapseBtnClass = 'el-icon-s-unfold';
      } else {
        this.sideWidth = 200;
        this.logoText = true;
        this.collapseBtnClass = 'el-icon-s-fold';
      }
    },
    getUser() {
      let nickname = localStorage.getItem('userInfo') ? JSON.parse(localStorage.getItem('userInfo')).nickname : ''
      // 后台获取user数据
      this.request.get("/user/nickname/" + nickname).then((res) => {
        // 重新赋值
        // console.log(res.data);
        this.user = res.data
      })
    },
  }
};
</script>
<style scoped>
.home {
  height: 100vh;
}

.el-header {
  position: fixed !important;
  top: 0;
  z-index: 2 !important;
  width: 100vw !important;
  background-color: #f5c78afa;
  color: black !important;
  line-height: 40px !important;
  height: 40px !important;
  text-align: right !important;
  font-size: 12px !important;
}

/* 侧边栏 */
.el-aside {
  height: 100vh;
  /* color: #fafafa; */
  box-shadow: 0.3em 0.3em 1em rgba(0, 0, 0, 0.3);
}

/* 表格主体内容 */
.el-main .el-input {
  width: 200px !important;
  margin-right: 10px;
}

/* 表格颜色 */
/* .el-table .warning-row {
  background: oldlace;
}

.el-table .success-row {
  background: #f0f9eb;
} */
</style>
