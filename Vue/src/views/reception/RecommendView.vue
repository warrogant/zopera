<template>
    <div class="recommend">
        <div class="recommend_header">
            <span @click="load">戏曲推荐</span>
            <!-- <button v-for="(item, index) in type" :key="index" @click="changeVideoList(index)">{{ item }}</button> -->
            <button v-for="(operaType, index) in types" :key="index"
            @click="showContent(operaType)" :class="{ 'custom-underline': isOperaType === operaType }">
                {{ operaType }}
            </button>
        </div>
        <div class="recommend_list">
            <!--  v-show="selectedItem === video.name"  -->
            <div v-for="(video, index) in videos" :key="index" class="video_item" @click="viewopera(video.id)">
                <div class="video_cover">
                    <!-- <img :src="video.cover" alt=""> -->
                    <img :src="video.pic" alt="">
                </div>
                <div class="video_info">
                    <h3 class="video_title">{{ video.name }}</h3>
                    <p class="video_desc">{{ video.introduce }}</p>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'RecommendView',
    data() {
        return {
            types: ['师公戏', '富宁壮戏', '隆林壮戏', '田林壮戏', '马隘壮戏', '德靖木偶剧', '广南壮戏', '其它'],
            videos: [],
            isOperaType:'',
        }
    },
    created() {
        this.load()
    },
    methods: {
        load() {
            this.request.get('/reception/operalimit').then(res => {
                console.log(res); 
                this.videos = res
            })
        },
        showContent(operaType) {
            this.request.get('/reception/operatype/' + operaType).then(result => {
                // console.log(result);
                this.videos = result
            })
            this.isOperaType = (this.isOperaType === operaType) ? null : operaType;
        },
        // 点击跳转至视频播放页面
        viewopera(id) {
            this.$router.push({ path: '/reception/videoopera', query: { id: id } })
            console.log(id);
            this.request.get('/reception/updateOperaVideoByIdCount/' + id).then((result) => {
                this.videos.playback = result
                // console.log(result.data);
                // console.log(this.videos.playback);
            })
        },
    },
}
</script>

<style lang="less" scoped>
// 推荐
.recommend {
    border-radius: 15px;
    margin: 0 auto 10px;
    width: 70vw;
    // height: 370px;
    // background-color: #e78282;

    padding: 40px 0 20px;

    // background-color: #fff;
    .recommend_header {

        display: flex;
        justify-content: space-around;
        align-items: center;
        font-size: 0;
        // background-color: #eb6c6c;

        span {
            display: block;
            line-height: 36px;
            cursor: pointer;
            font-family: KaiTi;
            font-size: 36px;
            font-weight: normal;
            text-transform: uppercase;
            letter-spacing: 0em;
            color: #000000;
            // background-color: #fff;
        }

        button {
            display: inline-block;
            // text-decoration: none;
            // 去除按钮默认样式
            background-color: transparent;
            border: none;
            font-size: 16px;
            line-height: 30px;
            padding: 0 10px;
            margin-right: 25px;
            font-style: normal;
            cursor: pointer;
            border-radius: 3px;

            &.active {
                color: #fff;
                background: #ff641e;
            }
        }

        button:hover {
            color: aqua;
            background-color: #fff;
            border-radius: 15px;
        }
    }

    .recommend_list {
        display: grid;
        grid-template-columns: repeat(4, 1fr);
        grid-gap: 20px;
        margin: 20px 0;

        .video_item {
            cursor: pointer;
            border-radius: 15px;

            .video_cover {
                position: relative;
                width: 100%;
                height: 0;
                padding-bottom: 56.25%;
                overflow: hidden;
                border-radius: 15px;

                img {
                    position: absolute;
                    top: 0;
                    left: 0;
                    width: 100%;
                    height: 100%;
                    object-fit: cover;
                }
            }

            .video_info {
                width: 100%;
                padding: 10px;
                margin-top: 10px;
                color: #000000;
                font-size: 14px;
                line-height: 1.5;
                text-align: center;
                border-bottom-left-radius: 15px;
                border-bottom-right-radius: 15px;
                // background-color: rgba(0, 0, 0, 0.7);

                .video_title {
                    display: -webkit-box;
                    -webkit-box-orient: vertical;
                    -webkit-line-clamp: 1;
                    overflow: hidden;
                    text-overflow: ellipsis;
                    // 字体样式
                    font-size: 16px;
                    font-weight: bold;
                    margin-bottom: 5px;
                }

                .video_desc {
                    display: -webkit-box;
                    -webkit-box-orient: vertical;
                    -webkit-line-clamp: 2;
                    overflow: hidden;
                    text-overflow: ellipsis;
                }
            }
        }
    }
}
.custom-underline {
    text-decoration-line: underline;
    /* 下划线 */
    text-decoration-color: red;
    /* 下划线颜色 */
    text-decoration-style: dashed;
    /* 下划线样式，可以是 solid、dashed、dotted 等 */
    text-decoration-thickness: 3px;
    /* 下划线粗细，可以是具体的像素值或者 thin、medium、thick */
}
</style>