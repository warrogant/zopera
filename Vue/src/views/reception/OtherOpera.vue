<template>
    <div class="other">
        <span>其它戏曲</span>
        <div class="other_list">
            <!--  v-show="selectedItem === video.name"  -->
            <div v-for="(opera, index) in videos" :key="index" class="other_item" @click="viewopera(opera.id)">
                <div class="other_cover">
                    <!-- <img :src="video.cover" alt=""> -->
                    <img :src="opera.pic" :alt="opera.name">
                </div>
                <div class="other_info">
                    <h3 class="other_title">{{ opera.name }}</h3>
                </div>
            </div>
        </div>

    </div>
</template>

<script>
export default {
    name: 'OtherOpera',
    data() {
        return {
            videos:[],
            // operaType:'其它',
        }
    },
    created() {
        this.load()
    },
    methods: {
        load() {
            this.request.get('/reception/operaVideoType/其它').then(res => {
                this.videos = res.slice(0, 6)
                console.log(res);
            })
        },
        // 点击跳转至视频播放页面
        viewopera(id) {
            this.$router.push({ path: '/reception/videoopera', query: { id: id } })
            // console.log(id);
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
// 其它
.other {
    border-radius: 15px;
    margin: 60px auto 10px;
    width: 70vw;
    // background-color: #c27d7d;

    span {
        display: block;
        text-align: center;
        font-family: KaiTi;
        font-size: 36px;
        line-height: 36px;
        font-weight: normal;
        text-transform: uppercase;
        letter-spacing: 0em;
        color: #000000;
    }

    .other_list {
        display: grid;
        grid-template-columns: repeat(6, 1fr);
        grid-gap: 20px;
        margin: 20px 0;

        .other_item {
            cursor: pointer;
            border-radius: 15px;

            .other_cover {
                position: relative;
                padding-bottom: 56.25%;
                overflow: hidden;
                border-radius: 15px;

                img {
                    position: absolute;
                    top: 0;
                    left: 0;
                    width: 100%;
                    height: 100%;
                    // object-fit: cover;
                }
            }

            .other_info {
                display: flex;
                justify-content: center;
                align-items: center;
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

                .other_title {
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
            }
        }
    }
}
</style>