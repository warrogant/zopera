<template>
  <div class="toplist">
    <div class="w1200">
      <div class="top_header">
        <!-- 戏曲排行榜 -->
        <div class="header_l">
          <img
            src="@/assets/img/排行榜.jpg"
            alt="your_image_description"
            class="header_img"
          />
          <span>戏曲排行榜</span>
        </div>
        <!-- <div class="header_r"><svg class="icon" aria-hidden="true">
            <use xlink:href="#opera-shoucang"></use>
          </svg>&nbsp;收藏
        </div> -->
      </div>
      <div class="top_content">
        <div class="operalist">
          戏曲列表 <span>{{ createrTime }}</span>
        </div>
        <div class="top_content_header">
          <div class="top_content_header_item">排名</div>
          <!-- <div class="top_content_header_item">封面</div> -->
          <div class="top_content_header_item">曲目</div>
          <div class="top_content_header_item">类别</div>
          <div class="top_content_header_item">播放量</div>
          <div class="top_content_header_item"></div>
        </div>
        <div class="top_content_list">
          <div
            class="top_content_list_item"
            v-for="(item, index) in topList"
            :key="item.id"
          >
            <div class="top_content_list_item_rank" @click="viewopera(item.id)">
              {{ index + 1 }}
            </div>
            <!-- <div class="top_content_list_item_img"></div> -->
            <div class="top_content_list_item_name" @click="viewopera(item.id)">
              {{ item.name }}
            </div>
            <div
              class="top_content_list_item_singer"
              @click="viewopera(item.id)"
            >
              {{ item.operaType }}
            </div>
            <div
              class="top_content_list_item_score"
              @click="viewopera(item.id)"
            >
              {{ item.playback }}
            </div>
            <div class="top_content_list_item_score">
              <span @click="ondownload(item.url)">
                <svg class="icon" aria-hidden="true">
                  <use xlink:href="#opera-xiazai"></use>
                </svg>
              </span>

              <!-- 已收藏 -->
              <span
                v-if="iscang.includes(item.id)"
                @click="delfavorite(user.id, item.id)"
              >
                <svg
                  :class="{ 'icon-visible': isfavorite }"
                  aria-hidden="true"
                  class="icon"
                >
                  <use xlink:href="#opera-yidongduan_yishoucang"></use>
                </svg>
              </span>
              <!-- 收藏 -->
              <span v-else @click="addfavorite(user.id, item.id, index)">
                <!--  ref="currentSpan" -->
                <svg
                  :class="{ 'icon-visible': !isfavorite }"
                  aria-hidden="true"
                  class="icon"
                >
                  <use xlink:href="#opera-shoucang1"></use>
                </svg>
              </span>
              <!-- 分享 -->
              <!-- <span
                ><svg class="icon" aria-hidden="true">
                  <use xlink:href="#opera-fenxiang"></use>
                </svg>
              </span> -->
            </div>
          </div>
        </div>
      </div>
    </div>
    <FooterReception />
  </div>
</template>

<script>
import FooterReception from "@/views/reception/Footer.vue";
export default {
  name: "TopList",
  components: { FooterReception },
  inject: ["reload"],
  data() {
    return {
      createrTime: new Date().toLocaleString(),
      topList: [],
      operaIds: [],
      iscang: [],
      isfavorite: false,
      user: localStorage.getItem("userInfo")
        ? JSON.parse(localStorage.getItem("userInfo"))
        : {},
      isshoucang: false,
    };
  },
  created() {
    this.load();
  },
  methods: {
    async load() {
      await this.request.get("/reception/operaVideoList").then((res) => {
        // console.log(res);
        this.topList = res;
        this.getUserIdFindFavorite();
      });
    },
    // 下载
    ondownload(url) {
      
      if (this.user.username) {
      window.open(url);
    }else{
       this.$message.error('请登录后在进行操作')
    }
    },
    // 根据用户ID查询用户已收藏的戏曲
    getUserIdFindFavorite() {
      this.request.get("/user/selectByUserId/" + this.user.id).then((res) => {
        this.operaIds = res.data;
        // console.log(res.data);
        // console.log(this.reload.operaId);
        // console.log(this.topList.id)

        // 执行收藏操作
        this.topList.forEach((topItem) => {
          this.operaIds.forEach((reloadItem) => {
            if (topItem.id === reloadItem.operaId) {
              this.iscang.push(topItem.id);
              // this.isshoucang = true;
              // console.log(topItem.id);
              // console.log(reloadItem.operaId);
            }
          });
        });
      });
    },
    isNumberInArray() {
      return this.iscang.includes(this.number);
    },
    // 添加收藏
    addfavorite(userId, operaId, index) {
      // console.log(this.$refs.currentSpan[index]);
      // const item = this.topList.find(item => item.id === operaId);
      // console.log(index);
      if (this.user.username) {
      if (index >= 0) {
        this.request
          .post("/user/favorite/add?userId=" + userId + "&operaId=" + operaId)
          .then((res) => {
            if (this.isfavorite !== res) {
              this.$message.success("收藏成功");
              this.load();
              // this.isfavorite = true
              // console.log('收藏成功' + index);
              // console.log(this.topList);
            }
          });
      }
    }else{
       this.$message.error('请登录后在进行操作')
    }
    },
    // 取消收藏
    delfavorite(userId, operaId) {
      this.request
        .delete("/user/favorite/del?userId=" + userId + "&operaId=" + operaId)
        .then((res) => {
          if (this.isfavorite !== res) {
            this.$message({
              duration: 2000,
              message: "取消收藏成功",
              type: "success",
            });
            this.load();
            this.reload();
            //this.isfavorite = false
            // this.$message.success('取消收藏成功')
            // this.reload();
            // window.location.reload();
            // console.log('取消收藏成功'+res);
            // console.log(this.isfavorite);
          }
        });
      // console.log(this.topList);
    },
    viewopera(id) {
      this.$router.push({ path: "/reception/videoopera", query: { id: id } });
      this.request
        .get("/reception/updateOperaVideoByIdCount/" + id)
        .then((result) => {
          // this.videos.playback = result;
          // console.log(this.videos.playback);
          // this.videos.playback = result;
          console.log(result);
        });
    },
  },
};
</script>

<style lang="less" scoped>
/* 排行榜样式 */
.toplist {
  // background-color: #fff;
  padding: 30px 0;

  .top_header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    .header_l {
      font-size: 32px;
      font-weight: bold;
      display: flex;
      align-items: center;

      // 图片
      img {
        width: 200px;
        height: 200px;
        margin-right: 10px;
        border-radius: 15px;
      }

      // 排行榜文字
      span {
        // display: block;
        // width: 200px;
        // height: 200px;
        // margin-right: 10px;
        // text-transform: uppercase;
        font-family: KaiTi;
        font-size: 36px;
        font-weight: normal;
        letter-spacing: 0em;
        color: #3d3d3d;

        // background-color: #000;
        i {
          font-size: 16px;
        }
      }
    }

    .header_r {
      width: 134px;
      height: 50px;
      border-radius: 15px;
      // opacity: 1;
      background: #b3b3b3;
      text-align: center;
      color: #fff;
      font-size: 20px;
      line-height: 50px;
      cursor: pointer;
    }

    .header_r:hover {
      background-color: #f90;
      color: #fff;
    }
  }

  .top_content {
    // text-align-last: left;

    .operalist {
      // width: 330px;
      height: 40px;
      text-align: left;
      line-height: 40px;
      margin: 20px 0 10px;
      // 字体样式
      font-family: KaiTi;
      font-size: 36px;
      font-weight: normal;
      letter-spacing: 0em;
      color: #3d3d3d;

      // background-color: #ffffff;
      span {
        font-family: KaiTi;
        font-size: 24px;
        font-weight: normal;
        letter-spacing: 0em;

        color: #b3b3b3;
      }
    }

    .top_content_header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      height: 40px;
      line-height: 40px;
      background-color: #f5f5f5;
      font-size: 16px;
      font-weight: bold;
      padding: 0 20px;

      .top_content_header_item {
        text-align: center;
      }

      .top_content_header_item:not(:first-child) {
        flex: 1;
      }

      .top_content_header_item:nth-of-type(1) {
        // text-align-last: left;
        width: 150px;
        // background-color: #fff;
      }

      .top_content_header_item:nth-last-of-type(1) {
        // display: none;
        visibility: hidden;
        background-color: #ac3333;
      }
    }

    .top_content_list {
      .top_content_list_item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        height: 60px;
        line-height: 60px;
        border-bottom: 1px solid #f5f5f5;
        padding: 0 20px;

        .top_content_list_item_rank {
          // flex: 1;
          width: 150px;
          text-align: center;
          font-size: 20px;
          font-weight: bold;
        }

        // .top_content_list_item_img {
        //   width: 50px;
        //   height: 50px;
        //   border-radius: 15px;
        //   background-color: #000;
        //   flex: 1;
        //   text-align: center;
        // }

        .top_content_list_item_name {
          flex: 1;
          text-align: center;
          // 内容溢出不显示
          display: -webkit-box;
          -webkit-box-orient: vertical;
          -webkit-line-clamp: 1;
          overflow: hidden;
          text-overflow: ellipsis;
        }

        .top_content_list_item_singer {
          flex: 1;
          text-align: center;
        }

        .top_content_list_item_score {
          flex: 1;
          text-align: center;
          font-size: 20px;
          font-weight: bold;
          color: #f60;
        }

        :nth-last-of-type(1) {
          display: flex;
          justify-content: space-evenly;
          align-items: center;
          // background-color: #fff;
        }
      }

      span {
        cursor: pointer;
        visibility: hidden;
      }

      :hover span {
        visibility: visible;
      }

      > div:hover {
        cursor: pointer;
        background-color: #fff;
      }
    }
  }

  .icon {
    width: 30px;
    height: 30px;
    vertical-align: middle;
    // fill: currentColor;
    // fill: #020202;
    overflow: hidden;
  }

  .icon-visible {
    fill: #524b4b;
  }
}
</style>

 