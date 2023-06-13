import Vue from "vue";
import Router from "vue-router";
import store from "@/store";

Vue.use(Router);

const routes = [
  {
    path: "/login",
    name: "login",
    component: () => import("../views/Login.vue"),
  },
  {
    path: "/register",
    name: "register",
    component: () => import("../views/Register.vue"),
  },
  {
    path: "/reception",
    name: "reception",
    component: () => import("../views/reception/Reception.vue"),
    redirect: "/reception/home",
    children: [
      {
        path: "home",
        name: "home",
        component: () => import("../views/reception/Home.vue"),
      },
      {
        path: "operapage",
        name: "operapage",
        component: () => import("../views/reception/OperaPage.vue"),
      },
      {
        path: "videoopera",
        name: "videoopera",
        component: () => import("../views/reception/VideoOpera.vue"),
      },
      // {
      //   path: "AsideReception",
      //   name: "AsideReception",
      //   component: () => import("../views/reception/AsideReception.vue"),
      // },
      {
        path: "toplist",
        name: "toplist",
        component: () => import("../views/reception/TopList.vue"),
      },
      {
        path: "activity",
        name: "activity",
        component: () => import("../views/reception/ActivityPage.vue"),
      },
      {
        path: "ActivitView",
        name: "ActivitView",
        component: () => import("../views/reception/ActivitView.vue"),
      },
      {
        path: "culture",
        name: "culture",
        component: () => import("../views/reception/OperaCulture.vue"),
      },
      {
        path: "CultureView",
        name: "CultureView",
        component: () => import("../views/reception/CultureView.vue"),
      },
      {
        path: "myopera",
        name: "myopera",
        component: () => import("../views/reception/MyOpera.vue"),
      },
      {
        path: "CommentView",
        name: "CommentView",
        component: () => import("../views/reception/CommentView.vue"),
      },
      {
        path: "CarouselView",
        name: "CarouselView",
        component: () => import("../views/reception/CarouselView.vue"),
      },
      {
        path: "PersonInfo",
        name: "PersonInfo",
        component: () => import("../views/reception/PersonInfo.vue"),
      },
      // OperaReview
    ],
  },
  {
    path: "/404",
    name: "404",
    component: () => import("../views/404.vue"),
  },
  {
    path: "/VideoDemo",
    name: "VideoDemo",
    component: () => import("../test/VideoDemo.vue"),
  },
];

const router = new Router({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

// 提供一个重置路由的方法
export const resetRouter = () => {
  router.matcher = new Router({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
  })
}

// 设置动态路由
export const setRouter = () => {
  const storeMenus = localStorage.getItem("menus");
  if (storeMenus) {
    // 获取当前的路由对象名称数组
    const currentRouteNames = router.getRoutes().map((v) => v.name);
    if (!currentRouteNames.includes("Backstage")) {
      // 拼装动态路由
      const BackstageRoute = {
        path: "/",
        name: "Backstage",
        component: () => import("../views/Backstage.vue"),
        redirect: "/home",
        children: [
          {
            path: "person",
            name: "个人信息",
            component: () => import("../views/Person.vue"),
          },
        ],
      };
      const menus = JSON.parse(storeMenus);
      menus.forEach((item) => {
          // 当且仅当path不为空的时候才去设置路由
        if (item.path) {
          let itemMenu = {
            path: item.path.replace("/", ""),
            name: item.name,
            component: () => import("../views/" + item.pagePath + ".vue"),
          };
          BackstageRoute.children.push(itemMenu);
        } else if (item.children.length) {
          item.children.forEach((item) => {
            if (item.path) {
              let itemMenu = {
                path: item.path.replace("/", ""),
                name: item.name,
                component: () => import("../views/" + item.pagePath + ".vue"),
              };
              BackstageRoute.children.push(itemMenu);
            }
          });
        }
      });
      // 动态添加到现在的路由对象中去
      router.addRoute(BackstageRoute);
    }
  }
};

// 重置我就再set一次路由
setRouter();

// 路由守卫
router.beforeEach((to, from, next) => {
  localStorage.setItem("currentPathName", to.name); //取出
  store.commit("setPath"); //store数据更新
  // 找不到路由
  if (!to.matched.length) {
  const storeBackstage = localStorage.getItem("menus");
    if (storeBackstage) {
      next('/404'); //放行
    } else {
      next("/login"); //放行
    }
  }
  next()
});

// 捕获路由异常
const originalPush = Router.prototype.push
Router.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

export default router;
