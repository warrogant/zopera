<template>
    <div class="operaculture">
        <div class="w1200">
            <div class="operaculture_title">
                <div class="opera_item" v-for="(opera, index) in operas" :key="index" :class="color[index]" @click="onCulture(opera)">
                    <p>{{ opera }}</p>
                </div>
            </div>
            <div class="operaculture_video">
                <div class="operaculture_video_header">
                    <span> 戏曲视频</span><span><router-link to="/reception/operapage"> 更多视频<i
                                class="el-icon-d-arrow-right"></i></router-link></span>
                </div>
                <div class="operaculture_video_list">
                    <div v-for="(video, index) in videos" :key="index" class="video_item" @click="viewopera(video.id)">
                        <div class="video_cover">
                            <img :src="video.pic" alt="">
                        </div>
                        <div class="video_info">
                            <h3 class="video_title">{{ video.name }}</h3>
                            <p class="video_desc">{{ video.introduce }}</p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="operaculture_tree">
                <div class="operaculture_tree_header">
                    其它戏曲
                </div>
                <div class="operaculture_tree_list">
                    <div class="opera_item" v-for="(opera, index) in otheropera" :key="index" :class="color[index]" @click="onCulture(opera)">
                        <p>{{ opera }}</p>
                    </div>
                </div>
            </div>
        </div>
        <FooterReception />
    </div>
</template>

<script>
import FooterReception from '@/views/reception/Footer.vue'
export default {
    name: 'OperaCulture',
    components: { FooterReception },
    data() {
        return {
            operas: ['师公戏', '富宁壮戏', '隆林壮戏', '田林壮戏', '马隘壮戏', '德靖木偶剧', '广南壮戏'],
            color: ['color1', 'color2', 'color3', 'color4', 'color5', 'color6', 'color7', 'color8', 'color9', 'color10', 'color11', 'color12', 'color13', 'color14', 'color15'],
            videos: [],
            otheropera: [
                '京剧', '豫剧', '评剧', '越剧', '粤剧', '黄梅戏', '晋剧', '沪剧'
            ]

        }
    },
    created() {
        this.load()
    },
    methods: {
        load() {
            this.SelectOperaLimit8()
            // this.selectCultureFindAll()
        },
        SelectOperaLimit8() {
            this.request.get('/reception/SelectOperaLimit8').then(result => {
                // console.log(result);
                this.videos = result
            })
        },
        // selectCultureFindAll(){
        //     this.request.get('/reception/cultureFindAll').then(result => {
        //         console.log(result.data);
        //         // this.videos = result
        //     })
        // },
        onCulture(opera){
            this.$router.push({ path: '/reception/CultureView', query: { opera: opera } })
            // console.log(opera);
            this.request.post('/reception/updateOperaCultureByIdHits/' + opera).then((result) => {
                // this.videos.playback = result
                // console.log(this.videos.playback);
                console.log(result);
            })
        },
        // 点击跳转至视频播放页面
        viewopera(id) {
            this.$router.push({ path: '/reception/videoopera', query: { id: id } })
            // console.log(id);
            this.request.get('/reception/updateOperaVideoByIdCount/' + id).then((result) => {
                this.videos.playback = result
                console.log(this.videos.playback);
            })
        },
    },
}
</script>

<style lang="less" scoped>
.operaculture {
    padding: 30px 0;
    // background-color: #fff;

    .operaculture_title {

        .opera_item {
            display: inline-block;
            cursor: pointer;
            width: 150px;
            height: 100px;
            border-radius: 15px;
            margin: 0 60px;
            background: #F4B5D8;
            // 文字
            font-family: Source Han Sans CN;
            font-size: 24px;
            line-height: 100px;
            font-weight: normal;
            letter-spacing: 0em;
            vertical-align: middle;
            color: #3D3D3D;
        }

        .color1 {
            background-color: #F4B5D8;
        }

        .color2 {
            background-color: #D3F9B6;
        }

        .color3 {
            background-color: #FFE89E;
        }

        .color4 {
            background-color: #FCAE7D;
        }

        .color5 {
            background-color: #B6F9F0;
        }

        .color6 {
            background-color: #9EC2FF;
        }

        .color7 {
            background-color: #FB7D7D;
        }

        .color8 {
            background-color: #92b327;
        }
    }

    .operaculture_video {
        .operaculture_video_header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            // background-color: #e68080;
            height: 90px;
            padding: 20px 0 3px;
            line-height: 90px;
            // text-align: left;
            font-family: Source Han Sans CN;
            font-size: 32px;
            font-weight: normal;
            letter-spacing: 0em;
            color: #3D3D3D;
            border-bottom: 2px solid #3D3D3D;

            span:nth-of-type(2) {
                font-size: 20px;
                padding-right: 10px;
                cursor: pointer;
            }
        }

        .operaculture_video_list {
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

    .operaculture_tree {
        .operaculture_tree_header {

            display: flex;
            justify-content: space-between;
            align-items: center;
            // background-color: #e68080;
            height: 90px;
            padding: 20px 0 3px;
            line-height: 90px;
            // text-align: left;
            font-family: Source Han Sans CN;
            font-size: 32px;
            font-weight: normal;
            letter-spacing: 0em;
            color: #3D3D3D;
            border-bottom: 2px solid #3D3D3D;
        }

        .operaculture_tree_list {

            .opera_item {
                display: inline-block;
                cursor: pointer;
                width: 150px;
                height: 100px;
                border-radius: 15px;
                margin: 0 60px;
                background: #F4B5D8;
                // 文字
                font-family: Source Han Sans CN;
                font-size: 24px;
                line-height: 100px;
                font-weight: normal;
                letter-spacing: 0em;
                vertical-align: middle;
                color: #3D3D3D;
            }

            .color1 {
                background-color: #F4B5D8;
            }

            .color2 {
                background-color: #D3F9B6;
            }

            .color3 {
                background-color: #FFE89E;
            }

            .color4 {
                background-color: #FCAE7D;
            }

            .color5 {
                background-color: #B6F9F0;
            }

            .color6 {
                background-color: #9EC2FF;
            }

            .color7 {
                background-color: #FB7D7D;
            }

            .color8 {
                background-color: #92b327;
            }

            .color9 {
                background-color: #F4B5D8;
            }

            .color10 {
                background-color: #D3F9B6;
            }

            .color11 {
                background-color: #FFE89E;
            }

            .color12 {
                background-color: #FCAE7D;
            }

            .color13 {
                background-color: #B6F9F0;
            }

            .color14 {
                background-color: #9EC2FF;
            }

            .color15 {
                background-color: #FB7D7D;
            }

        }
    }
}
</style>