<template>
  <div>
    <div class="w1200">
      <div class="video">
        <div class="backrouter" @click="prev()"><svg class="icon" aria-hidden="true">
            <!-- v-bind: -->
            <use xlink:href="#opera-xingzhuanggongnengtubiao-"></use>
          </svg>返回</div>
        <div class="video_header">
          <h3>{{ video.name }}</h3>
          <span> <i class="el-icon-video-play"></i>&ensp;{{ video.playback }}</span>
          <span> <i class="el-icon-timer"></i>&ensp;{{ video.createTime | datetime }}</span>
          <span @click="opentvideoUrl(video.url)">下载视频</span>

          <span v-if="!isfavorite" @click="addfavorite(user.id, video.id)"><svg class="icon" aria-hidden="true">
              <!-- v-bind: -->
              <use xlink:href="#opera-shoucang"></use>
            </svg>收藏</span>
          <span v-else>
            <el-popconfirm confirm-button-text='是的' cancel-button-text='在考虑一会儿' icon="el-icon-info" icon-color="red"
              title="您是要取消收藏吗？" @confirm="delfavorite(user.id, video.id)">
              <button slot="reference"><svg class="icon" aria-hidden="true">
                  <!-- v-bind: -->
                  <use xlink:href="#opera-yidongduan_yishoucang"></use>
                </svg>已收藏</button>
            </el-popconfirm>
          </span>
      <div class="introduce">
        {{video.introduce}}
      </div>
        </div>
        <video-player class="video-player vjs-custom-skin" ref="videoPlayer" name="videoPlayer" id="video"
          :playsinline="true" :options="playerOptions" @waiting="onPlayerWaiting($event)"
          @timeupdate="onPlayerTimeupdate($event)" @ready="playerReadied"></video-player>
      </div>
      <CommentView :foreignId="foreignId" :foreignType="foreignType"></CommentView>
      <el-dialog title="" :visible.sync="dialogFormVisibleLogin" width="30%" :before-close="handleClose"
        class="backLogin">
        <p>当前尚未登录，前往<u><router-link to="/login">登录页面</router-link></u></p>
      </el-dialog>
    </div>
    <FooterReception></FooterReception>

  </div>
</template>
<script>
import { videoPlayer } from "vue-video-player";
import "video.js/dist/video-js.css";
import "vue-video-player/src/custom-theme.css";
import CommentView from '@/views/reception/CommentView.vue'
import FooterReception from '@/views/reception/Footer.vue'
export default {
  name: "showVideo",
  // inject:['reload'],  //注入依赖
  components: {
    videoPlayer, CommentView, FooterReception
  },
  data() {
    return {
      video: '',
      videoUrl: '',
      foreignId: '',
      foreignType: 1,
      videoTitle: '',
      playTime: '',
      current: '',
      isfavorite: false,
      user: localStorage.getItem('userInfo') ? JSON.parse(localStorage.getItem('userInfo')) : {},
      dialogFormVisibleLogin: false,
      playerOptions: {
        playbackRates: [0.5, 1.0, 1.5, 2.0], // 可选的播放速度
        autoplay: false, // 如果为true,浏览器准备好时开始回放。
        muted: false, // 默认情况下将会消除任何音频。
        loop: false, // 是否视频一结束就重新开始。
        preload: "auto", // 建议浏览器在<video>加载元素后是否应该开始下载视频数据。auto浏览器选择最佳行为,立即开始加载视频（如果浏览器支持）
        language: "zh-CN",
        aspectRatio: "16:9", // 将播放器置于流畅模式，并在计算播放器的动态大小时使用该值。值应该代表一个比例 - 用冒号分隔的两个数字（例如"16:9"或"4:3"）
        fluid: true, // 当true时，Video.js player将拥有流体大小。换句话说，它将按比例缩放以适应其容器。
        sources: [
          {
            type: "video/mp4", // 类型
            src: "http://localhost:9090/file/7db7a046a12045ec819f2c6c85958474.mp4" // url地址
          }
        ],
        poster: "", // 封面地址
        notSupportedMessage: "此视频暂无法播放，请稍后再试", // 允许覆盖Video.js无法播放媒体源时显示的默认信息。
        controlBar: {
          timeDivider: true, // 当前时间和持续时间的分隔符
          durationDisplay: true, // 显示持续时间
          remainingTimeDisplay: false, // 是否显示剩余时间功能
          fullscreenToggle: true // 是否显示全屏按钮
        },
      },
    };
  },
  beforeRouteLeave() {
    this.$refs.videoPlayer.player.pause()
  },
  created() {
    this.load()
  },
  methods: {
    prev() {
      // this.$router.back()
      this.$router.go(-1)
      setInterval(() => {
        location.reload();
      }, 1000);
    },
    load() {
      this.id = this.$route.query.id,
        this.request.get('/reception/operavideo/' + this.id).then(res => {
          // console.log(res.data);
          this.video = res.data
          this.playerOptions.sources[0].src = res.data.url
          this.playerOptions.poster = res.data.pic
        })
      this.selectfavorite()
      // console.log(comments);
      this.foreignId = this.id
      // this.foreignType = 1
    },
    // 点击下载
    opentvideoUrl(url) {
      if (this.user.username) {
        window.open(url)
      } else {
        this.dialogFormVisibleLogin = true
      }
    },
    // DOM元素上的readyState更改导致播放停止
    onPlayerWaiting(player) {
      let time = localStorage.getItem("cacheTime")
      if (player.cache_.currentTime - Number(time) > 0.1) {
        this.current = Number(time)
        this.playerReadied(player)
      } else {
        this.current = player.cache_.currentTime
      }
    },

    // // 当前播放位置发生变化时触发。
    onPlayerTimeupdate(player) {
      this.playTime = player.cache_.currentTime
      let playTime = player.cache_.currentTime
      setTimeout(function () {
        localStorage.setItem("cacheTime", playTime)
      }, 500)

      let time = localStorage.getItem("cacheTime")
      if (player.cache_.currentTime - Number(time) > 2) {
        this.current = Number(time)
        this.playerReadied(player)
      } else {
        this.current = player.cache_.currentTime
      }
    },

    //将侦听器绑定到组件的就绪状态。与事件监听器的不同之处在于，如果ready事件已经发生，它将立即触发该函数。。
    playerReadied(player) {
      // console.log('example player 1 readied', player);
      player.currentTime(this.current)
    },
    handleClose(done) {
      done();
    },
    selectfavorite() {
      this.request.get('/user/favorite', {
        params: {
          userId: this.user.id,
          operaId: this.id
        }
      }).then(res => {
        this.isfavorite = res.data.data.length > 0;
      })
    },
    // 添加收藏
    addfavorite(userId, operaId) {
      if (this.user.username) {
        this.request.post('/user/favorite/add?userId=' + userId + '&operaId=' + operaId).then(res => {
          if (res) {
            this.$message.success('收藏成功')
            this.load();
          }
        })
      } else {
        this.dialogFormVisibleLogin = true
        this.$message.error('请先登录后在进行操作')
      }
    },
    // 取消收藏
    delfavorite(userId, operaId) {
      this.request.delete('/user/favorite/del?userId=' + userId + '&operaId=' + operaId).then(res => {
        if (res) {
          this.$message.success('取消收藏成功')
          this.load()
        }
      })
    }
  }
};
</script>

<style lang="less" scoped>
.video {
  position: relative;
  height: 70vh;

  // background-color: #fff;
  .video_header {
    margin: 10px auto 0;
    width: 50vw;
    // background-color: #fff;
    line-height: 35px;
    font-family: KaiTi;
    font-weight: normal;
    letter-spacing: 0em;
    color: #3D3D3D;
    text-align-last: left;

    h3 {
      // 内容溢出不显示
      display: -webkit-box;
      -webkit-box-orient: vertical;
      -webkit-line-clamp: 1;
      overflow: hidden;
      text-overflow: ellipsis;
    }

    span {
      // float: left;
      margin-left: 15px;

      button {
        margin-left: -20px;
        border: none;
        outline: none;
      }
    }

    :nth-child(4) {
      cursor: pointer;
      color: rgb(5, 72, 255)
    }

    // :nth-child(4) :hover {
    //   color: #3D3D3D;
    //   background-color: #03f7ff;
    // }
  }

  .introduce{
  // width: 0vw -160px;
  font-size: 16px;
  line-height: 24px;    
  display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 1;
    overflow: hidden;
    text-overflow: ellipsis;
  // background-color: #ff0000;
}
  .video-player {
    width: 50vw;
    height: 40vh;
    // width: 100%;
    // height: 100%;
    margin: 20px auto 0;
  }
  // 设置vue-video-player视频铺满不留黑边
  ::v-deep .video-js .vjs-tech {
    object-fit: fill;
  }

  ::v-deep .vjs-poster {
    background-size: cover;
  }

  /*这里用了第三方vue-video-player插件，但这个插件有bug，设置globalSetting:{controls:true}隐藏进度条不生效，故可设置插件样式进行隐藏vjs-progress-contro*/
  .vjs-progress-control {
    visibility: hidden; // 隐藏进度条
    pointer-events: none;
  }

  .vjs-custom-skin>.video-js .vjs-big-play-button {
    background-color: rgba(0, 0, 0, 0.45);
    font-size: 3.0em;
    /* border-radius: 50%; */
    height: 1.5em !important;
    line-height: 1.5em !important;
    margin-top: -1em !important;
  }
}

.backrouter {
  position: absolute;
  cursor: pointer;
}

.icon {
  width: 20px;
  height: 20px;
  vertical-align: middle;
  fill: currentColor;
  // fill: #ac3333;
  overflow: hidden;
  cursor: pointer;

  // background-color: #fff;
  // 修改图标样式，不行就把原来的颜色删了在改
  // fill: #f5f5f5;
  :active {
    fill: #ff0000;
  }
}

.backLogin {
  u {
    font-size: 24px;
    color: aqua
  }
}
</style>