<template>
  <div class="homepage">
    <!-- 回到顶部 -->
    <!-- <el-backtop :visibility-height='100'>
      <div style="height: 100%; width: 100%; background-color: #f2f5f6;box-shadow: 0 0 6px rgba(0,0,0, .12);text-align: center;line-height: 40px;color: blue;">
        <i class="el-icon-caret-top"></i>
      </div>
    </el-backtop> -->
    <el-row :gutter="12" style="margin-top: 3vh;">
      <el-col :span="6">
        <el-card shadow="hover" style="color:red;text-align: center;font-weight: bold;">
          用户注册量：&nbsp; <span>{{ countUser }}</span>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" style="color:blue;text-align: center;font-weight: bold;">
          戏曲总数：&nbsp; <span>{{ countOpera }}</span>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" style="color:orange;text-align: center;font-weight: bold;">
          戏曲播放量：&nbsp; <span>{{ sumPlayback }}</span>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" style="color:green;text-align: center;font-weight: bold;">
          活动组织数量：&nbsp; <span>{{ countActivity }}</span>
        </el-card>
      </el-col>
    </el-row>
    <!-- 图表显示 -->
    <div class="echartsclass">
      <div style="height:60vh;width:50%;margin-top: 5vh;" ref="registration"></div>
      <div id="operaatotal" style="height:80vh;width:50%;margin-top: 5vh;" ref="operaatotal"></div>
    </div>
    <div class="echartsclass_b">
      <div id="playback" style="height:70vh;width:60%;margin-top: 5vh;" ref="playback"></div>
      <div id="zoneaction" style="height:60vh;width:50%;margin-top: 5vh;" ref="zoneaction"></div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts';
export default {
  name: 'HomePage',
  data() {
    return {
      countUser: '',
      countOpera: '',
      sumPlayback: '',
      countActivity: ''
    }
  },
  created() {
    this.load()
  },
  mounted() {
    this.registration()
    this.operaatotal()
    this.playback()
    this.zoneaction()
  },
  methods: {
    load() {
      this.request.get('/echarts/countUser').then(res => {
        this.countUser = res.data
      })
      this.request.get('/echarts/countOpera').then(res => {
        this.countOpera = res.data
      })
      this.request.get('/echarts/sumPlayback').then(res => {
        this.sumPlayback = res.data
      })
      this.request.get('/echarts/countActivity').then(res => {
        this.countActivity = res.data
      })
    },
    // 用户注册量（柱状图）
    registration() {
      let option = {
        title: {
          text: '各季度用户注册量统计图',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        xAxis: {
          type: 'category',
          // boundaryGap: false,
          data: ["第一季度", "第二季度", "第三季度", "第四季度"]
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '',
            type: 'bar',
            data: []
          },
        ]
      };
      let mychart = echarts.init(this.$refs.registration)
      this.request.get('/echarts/members').then(res => {
        // option.xAxis.data = res.data.x
        option.series[0].data = res.data
        // option.series[1].data = res.data
        mychart.setOption(option)
      })
    },
    // 戏曲类型总数（饼图）
    operaatotal() {
      let mychart = echarts.init(this.$refs.operaatotal)
      // let mychart = this.$charts.init(document.getElementById("operaatotal"))
      var option = {
        title: {
          text: '各类型戏曲数量统计图',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          bottom: 'bottom'
        },
        toolbox: {
          show: true,
          feature: {
            mark: { show: true },
            dataView: { show: true, readOnly: false },
            restore: { show: true },
            saveAsImage: { show: true }
          }
        },
        series: [
          {
            name: '',
            type: 'pie',
            radius: [50, 250],
            center: ['50%', '50%'],
            label: {            //饼图图形上的文本标签
              normal: {
                show: true,
                position: 'inner', //标签的位置
                textStyle: {
                  fontWeight: 300,
                  fontSize: 14,    //文字的字体大小
                  color: "#fff"
                },
                formatter: '{d}%'
              }
            },
            roseType: 'area',
            itemStyle: {
              borderRadius: 8
            },
            data: []
          }
        ]
      };
      this.request.get("/echarts/SelectOperaTypeCount").then(res => {
        // console.log(res);
        option.series[0].data = [
          { name: "德靖木偶剧", value: res.data[0].totalCount },
          { name: "马隘壮戏", value: res.data[1].totalCount },
          { name: "田林壮戏", value: res.data[2].totalCount },
          { name: "隆林壮戏", value: res.data[3].totalCount },
          { name: "富宁壮戏", value: res.data[4].totalCount },
          { name: "广南壮戏", value: res.data[5].totalCount },
          { name: "师公戏", value: res.data[6].totalCount },
        ]
        option && mychart.setOption(option);
      })
      option && mychart.setOption(option);
    },
    // 戏曲播放量（折现+柱状图）
    playback() {
      let option = {
        title: {
          text: '各类型戏曲播放总量统计图',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        xAxis: {
          type: 'category',
          // boundaryGap: false,
          data: ["德靖木偶剧", "马隘壮戏", "田林壮戏", "富宁壮戏", "广南壮戏", "师公戏", "隆林壮戏", "其他戏曲"]
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '',
            type: 'bar',
            data: []
          },
          {
            name: '',
            type: 'line',
            data: []
          },
        ]
      };
      let mychart = echarts.init(this.$refs.playback)
      this.request.get('/echarts/SelectOperaTypePlayback').then(res => {
        option.series[0].data = res.data.map(item => item.totalPlayback);
        option.series[1].data = res.data.map(item => item.totalPlayback);
        option.xAxis.data = res.data.map(item => item.operaType);
        mychart.setOption(option)
      })
    },
    // 活动组织数量(饼图)
    zoneaction() {
      var pieOption = {
        title: {
          text: '各季度活动发布数量统计饼图',
          subtext: '统计图',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            type: 'pie',
            radius: '60%',
            label: {            //饼图图形上的文本标签
              normal: {
                show: true,
                position: 'inner', //标签的位置
                textStyle: {
                  fontWeight: 300,
                  fontSize: 14,    //文字的字体大小
                  color: "#fff"
                },
                formatter: '{d}%'
              }
            },
            data: [],  // 填空
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      };

      var pieDom = document.getElementById('zoneaction');
      var pieChart = echarts.init(pieDom);

      this.request.get("/echarts/EventRelease").then(res => {
        // console.log(res);
        pieOption.series[0].data = [
          { name: "第一季度", value: res.data[0] },
          { name: "第二季度", value: res.data[1] },
          { name: "第三季度", value: res.data[2] },
          { name: "第四季度", value: res.data[3] },
        ]
        pieChart.setOption(pieOption)
      })
    },
  }
}
</script>

<style lang="less">
.homepage {
  height: 100vh;

  .echartsclass {
    display: flex;
    justify-content: space-around;
    align-items: center;

    div {
      margin: 5vh;
    }
  }

  .echartsclass_b {
    display: flex;
    justify-content: space-around;
    align-items: center;

    div {
      margin: 5vh;
    }

    // background-color: #db7f7f;

  }
}
</style>