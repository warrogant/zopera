<template>
    <div class="carousel">
        <div class="w1200">
            <el-carousel :interval="10000" height="400px" type="card">
                <el-carousel-item v-for="item in notices" :key="item.id"
                :style="{ background: 'url(' + item.picUrl + ') center/cover no-repeat' }"
                :data-background="item.picUrl">
                    <h3 class="medium" @click="clickCarousel(item.id)">{{ item.picTitle }}</h3>
                </el-carousel-item>
            </el-carousel>
        </div>
    </div>
</template>

<script>
export default {
    name: 'CarouselPage',
    data() {
        return {
            notices: [],
        }
    },
    created() {
        this.load()
    },
    methods: {
        load() {
            // 返回前5条数据
            this.request.get('/notice/noticeLimit').then(res => {
                // console.log(res.data);
                this.notices = res.data
            })
        },
        clickCarousel(id){
            this.$router.push({ path: '/reception/CarouselView', query: { id: id } })
            // 点击量自增
            this.request.post('/notice/updateNoticeByIdHits/' + id).then((result) => {
                // console.log(result);
                this.notices.playback = result
                // console.log(id);
                // this.notices.count = result
                // this.reload()
            });
        },
        // 点击跳转至视频播放页面
        // viewopera(id) {
        //     this.$router.push({ path: '/reception/videoopera', query: { id: id } })
        //     this.request.get('/reception/updateOperaVideoByIdCount/' + id).then((result) => {
        //         this.videos.playback = result
        //     })
        // },
    },
}
</script>

<style lang="less" scoped>
// 轮播图
.el-carousel {
    border-radius: 15px;
    margin: 20px auto 0;
    width: 100vw - 160px;

    // background-color: #fff;
    //   .el-carousel__item 
    .el-carousel__item {
        background: attr(data-background) no-repeat;
        // background-image: url('var(imageUrl)');
        background-size: 100% 100%;
        border-radius: 15px;
    }

    // 图内文字
    .el-carousel__item h3 {
        display: block;
        width: 600px;
        height: 200px;
        // background-color: #f17f7f;
        color: #000000;
        font-size: 32px;
        opacity: 0.75;
        line-height: 200px;
        margin: 80px auto;
    }

    // 轮播图单数样式
    // .el-carousel__item:nth-child(2n) {
    //     background-color: #99a9bf;
    // }

    // 轮播图双数样式
    // .el-carousel__item:nth-child(2n+1) {
    //     background-color: #d3dce6;
    // }

}

</style>