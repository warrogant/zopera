<template>
  <el-menu :default-openeds="opens" default-active="1-4-1" class="el-menu-vertical-demo" active-text-color="#FF4500"
    text-color="#000" :collapse-transition="false" :collapse="isCollapse" router
    style="height: 100vh; border-right-color: transparent">
    <!-- 侧边栏logo -->
    <div style="height: 60px; line-height: 60px; text-align: center;cursor: pointer;">
      <router-link to="/home">
        <img src="@/assets/logo.png" alt="后台管理" style="width: 32px; height: 32px; margin-left: -6px;" />&nbsp;
        <b v-show="logoText">后台管理</b>
      </router-link>
    </div>
    <!-- 侧边栏内容 -->
    <fragment v-for="item in menus" :key="item.id">
      <fragment v-if="item.path">
        <el-menu-item :index="item.path"><svg class="icon" aria-hidden="true">
            <use v-bind:xlink:href="item.icon"></use>
          </svg>&nbsp;<span slot="title">{{ item.name }}</span>
        </el-menu-item>
      </fragment>
      <fragment v-else>
        <el-submenu :index="item.id + ''">
          <template slot="title">
            <svg class="icon" aria-hidden="true">
              <use v-bind:xlink:href="item.icon"></use>
            </svg>&nbsp;<span slot="title">{{ item.name }}</span>
          </template>
          <fragment v-for="subItem in item.children" :key="subItem.id">
            <el-menu-item :index="subItem.path"><svg class="icon" aria-hidden="true">
                <use v-bind:xlink:href="subItem.icon"></use>
              </svg>&nbsp;<span slot="title">{{ subItem.name }}</span>
            </el-menu-item>
          </fragment>
        </el-submenu>
      </fragment>
    </fragment>
  </el-menu>
</template>

<script>
export default {
  name: 'AsideB',
  props: ['isCollapse', 'logoText'],
  data() {
    return {
      menus: localStorage.getItem('menus') ? JSON.parse(localStorage.getItem('menus')) : [],
      opens: localStorage.getItem('menus') ? JSON.parse(localStorage.getItem('menus')).map(v => v.id + '') : [],
    };
  },
};
</script>

<style>
/* 侧边栏内容 */
.el-menu {
  background-color: #8ee3e9 !important;
  text-align: left;
  /* color: #fafafa; */
  /* 渐变 */
  /* background:linear-gradient(180deg, rgba(236,44,100,0.00) 0%, rgba(236,44,100,0.00) 2%, #C7492C 100%)!important; */
}

.el-submenu__title i {
  color: black !important;
}

.icon {
  width: 16px;
  height: 16px;
  vertical-align: -0.15em;
  fill: currentColor;
  overflow: hidden;
}
</style>
