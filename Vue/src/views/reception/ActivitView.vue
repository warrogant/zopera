<template>
  <div class="activit">
    <div class="backrouter" @click="prev">
      <svg class="icon" aria-hidden="true">
        <use xlink:href="#opera-xingzhuanggongnengtubiao-"></use>
      </svg>返回
    </div>
    <div class="w1200">
      <div class="activit_header">
        <p>{{ activity.name }}</p>
        <div class="activit_header_t">
          <svg class="icon" aria-hidden="true">
            <use xlink:href="#opera-24gf-user2"></use>
          </svg>&nbsp;<span>{{ activity.sponsor }}</span>&nbsp;&nbsp;&nbsp;&nbsp;
          <span><i class="el-icon-timer"></i>
            {{ activity.publishTime | datetime }}</span>&nbsp;&nbsp;&nbsp;&nbsp;
          <svg class="icon" aria-hidden="true">
            <use xlink:href="#opera-dianjiliang"></use>
          </svg>&nbsp;<span>{{ activity.count }}</span>
        </div>
        <img :src="activity.coverUrl" alt="" />
      </div>
      <div class="activit_list">
        <h3>{{ activity.name }}</h3>
        <p>{{ activity.content }}</p>
      </div>
      <div class="activit_footer">
        <p>
          <i class="el-icon-time"></i> 活动时间：{{
            activity.startTime | datetime
          }}&nbsp;&nbsp;&nbsp;&nbsp;至&nbsp;&nbsp;&nbsp;&nbsp;{{
            activity.endTime | datetime
          }}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <i class="el-icon-location-information"></i>地点：{{
            activity.location
          }}{{ activity.detail }}
        </p>
      </div>
      <CommentView :foreignId="foreignId" :foreignType="foreignType"></CommentView>
    </div>
    <FooterReception />
  </div>
</template>

<script>
import FooterReception from "@/views/reception/Footer.vue";
import CommentView from "@/views/reception/CommentView.vue";
export default {
  name: "ActivitView",
  components: { FooterReception, CommentView },
  data() {
    return {
      foreignId: '',
      foreignType:2,
      activity: [],
      labelPosition: "right",
      user: localStorage.getItem("userInfo")
        ? JSON.parse(localStorage.getItem("userInfo"))
        : {},
    };
  },
  created() {
    this.load();
  },
  methods: {
    prev() {
      // this.$router.back()
      this.$router.go(-1);
    },
    load() {
      (this.id = this.$route.query.id),
        this.request.get("/reception/operaActivity/" + this.id).then((res) => {
          console.log(res.data);
          this.activity = res.data;
        });
      this.request
        .get("/comment/findActivityCountById/" + this.id)
        .then((res) => {
          console.log(res);
          this.activity.count = res.data;
        });
      this.foreignId = this.id;
    },
  },
};
</script>

<style lang="less" scoped>
.activit {
  position: relative;

  .backrouter {
    position: absolute;
    left: 10%;
    top: 1.5%;
    cursor: pointer;
  }

  .activit_header {
    width: 70vw;
    margin-top: 20px;

    p {
      font-family: Source Han Sans CN;
      font-size: 36px;
      line-height: 32px;
      padding: 20px;
      font-weight: bold;
      letter-spacing: 0em;
      vertical-align: middle;
      color: #3d3d3d;
    }

    // background-color: #fa8888;
    .activit_header_t {
      width: 50vw;
      margin: 10px auto 20px;
      text-align-last: left;
      font-family: KaiTi;
      font-size: 16px;
      line-height: 32px;
      font-weight: normal;
      // background-color: #fff;

      span {
        // line-height: 24px;
        letter-spacing: 0em;
        color: #000000;
        // background-color: #e96e6e;
      }
    }

    img {
      display: block;
      width: 50vw;
      height: 50vh;
      object-fit: cover;
      margin: 0 auto;
    }
  }

  .activit_list {
    position: relative;
    width: 70vw;
    //英文和数字换行
    word-break: break-all;

    // height: 100vh;
    // background-color: #e87c7c;
    p {
      display: block;
      width: 50vw;
      // background-color: #fff;
      margin: 0 auto;
      text-align: left;
      font-size: 24px;
      text-indent: 2em;
      line-height: 40px;
      margin-bottom: 30px;
    }
  }

  .activit_footer {
    width: 70vw;
    // 内容溢出不显示
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 1;
    overflow: hidden;
    text-overflow: ellipsis;

    // background-color: #fff;
    p {
      display: block;
      width: 50vw;
      margin: 0 auto;
      text-align-last: left;
      // background-color: #d1ed97;
    }
  }
}

.icon {
  width: 1em;
  height: 1em;
  vertical-align: middle;
  fill: rgb(12, 173, 232);
  overflow: hidden;
  // background-color: #fff;
  // 修改图标样式，不行就把原来的颜色删了在改
  // fill: #f5f5f5;
}
</style>