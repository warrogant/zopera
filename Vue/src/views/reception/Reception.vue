<template>
    <div class="reception">
        <!-- 回到顶部 -->
        <el-backtop :bottom="200" :visibility-height='40'>
            <div
                style="height: 100%; width: 100%; background-color: #f2f5f6;box-shadow: 0 0 6px rgba(0,0,0, .12);text-align: center;line-height: 40px;color: blue;">
                <i class="el-icon-caret-top"></i>
            </div>
        </el-backtop>
        <el-container class="wrapper">
            <!-- 头部 -->
            <el-header>
                <div class="header_l" >
                    <router-link to="/reception/home">
                    <svg class="icon" aria-hidden="true">
                        <use xlink:href="#opera-yinle1"></use>
                    </svg>
                    <span> ZHUANG <br> OPERA </span>
                </router-link>
                </div>
                <!-- 搜索栏 -->
                <div class="header_c">
                    <el-input placeholder="请输入戏曲名称" clearable size="medium" suffix-icon="el-icon-search"
                        v-model="searchValue">
                    </el-input>
                    <button @click="onSelect(searchValue)">搜索</button>
                </div>
                <div class="header_r" style="display: inline-block;width: 200px;text-align: center;">
                    <div v-if="!user.nickname">
                        <input type="button" value="登录/注册" onclick="location.href='/login'">
                    </div>
                    <div v-else>
                        <!-- 用户信息 -->
                        <el-dropdown style="display: flex; justify-content: center;align-items: center; cursor: pointer;">
                            <div style="display: inline-block;width: 200px;text-align: center;">
                                <img :src="user.avator" alt=""
                                    style="width: 48px;height: 48px;;border-radius: 50%; margin-right:10px;background-color: #fff;">
                                <span style="margin-right: 5px; font-size: 16px; color: black">{{ user.nickname }}</span>
                                <i class="el-icon-arrow-down" style="margin-right: 15px; color: black"></i>
                            </div>
                            <el-dropdown-menu slot="dropdown" style="width: 150px;text-align: left;margin: 0 auto;">
                                <el-dropdown-item><router-link to="/reception/personInfo"><i class="el-icon-s-tools"></i>
                                        <span>编辑个人信息</span></router-link>
                                </el-dropdown-item>
                                <el-dropdown-item>
                                    <router-link to="/reception/myOpera"><i class="el-icon-service"></i>
                                        <span>我的戏曲</span>
                                    </router-link>
                                </el-dropdown-item>
                                <el-dropdown-item>
                                    <span style="text-decoration: none;" @click="outLogin"><i
                                            class="el-icon-switch-button"></i>
                                        退出登录</span>
                                </el-dropdown-item>
                            </el-dropdown-menu>
                        </el-dropdown>

                    </div>
                </div>
            </el-header>
            <!-- 主体 -->
            <el-container>
                <el-aside width="200px">
                    <ul>
                        <router-link to="/reception/home">
                            <li><svg class="icon" aria-hidden="true">
                                    <use xlink:href="#opera-shouye_"></use>
                                </svg><span>首页</span></li>
                        </router-link>
                        <router-link to="/reception/operapage">
                            <li><svg class="icon" aria-hidden="true">
                                    <use xlink:href="#opera-shipin1"></use>
                                </svg><span>戏曲</span></li>
                        </router-link>
                        <router-link to="/reception/toplist">
                            <li> <svg class="icon" aria-hidden="true">
                                    <use xlink:href="#opera-paihangbang-"></use>
                                </svg><span>排行榜</span></li>
                        </router-link>
                        <router-link to="/reception/activity">
                            <li><svg class="icon" aria-hidden="true">
                                    <use xlink:href="#opera-banzuhuodong"></use>
                                </svg><span>活动广场</span></li>
                        </router-link>
                        <router-link to="/reception/culture">
                            <li><svg class="icon" aria-hidden="true">
                                    <use xlink:href="#opera-zhishi"></use>
                                </svg> <span>戏曲文化</span></li>
                        </router-link>
                        <router-link to="/reception/myopera">
                            <li><svg class="icon" aria-hidden="true">
                                    <use xlink:href="#opera-yinle2"></use>
                                </svg><span>我的戏曲</span></li>
                        </router-link>
                    </ul>
                </el-aside>
                <el-container>
                    <!-- 主页内容 -->
                    <el-main>
                        <router-view></router-view>
                    </el-main>
                </el-container>
            </el-container>

        </el-container>
    </div>
</template>

<script>
// import Swiper from '/swiper/vue'
export default {
    name: 'ReceptionR',
    inject: ['reload'],
    props: {
        // user: Object
    },
    data() {
        return {
            searchValue: '',
            user: localStorage.getItem('userInfo') ? JSON.parse(localStorage.getItem('userInfo')) : {}

        }
    },
    methods: {
        // 退出登录
        outLogin() {
            // this.$router.push('/login')
            // localStorage.removeItem('userInfo')
            this.$store.commit('logout')
            this.$message.success('退出成功')
            this.reload()
        },
        onSelect(onSelect){
            // console.log(onSelect);
            this.$router.push({ path: '/reception/operapage', query: { onSelect: onSelect } })
            this.searchValue = ''
            this.reload()
        },
    },
}

</script>

<style lang="less" scoped>
.reception .el-container {
    // width: 100vw;
    // overflow: hidden;
    // height: 100vh;

    // 头
    .el-header {
        // position: relative;
        display: flex;
        justify-content: space-around;
        align-items: center;
        height: 15vh !important;
        border-bottom: 2px solid #000;
        background-color: #E9EEF3;

        .header_l {
            // position: absolute;
            // left: 3vh;
            // top: 2vh;
            width: 150px;
            height: 80px;
            border-radius: 15px;
            opacity: 1;
            text-align: center;
            background: #ffffff;
            cursor: pointer;

            // logo
            .icon {
                display: inline-block;
                font-size: 32px;
                width: 2em;
                height: 2em;
                margin-top: 5px;
                vertical-align: -10px;
                overflow: hidden;
                white-space: nowrap;
            }

            // 文字
            span {
                display: inline-block;
                font-family: KaiTi;
                font-size: 24px;
                line-height: 1em;
                font-weight: normal;
                text-align: center;
                text-transform: uppercase;
                letter-spacing: 0em;
                // text-transform: uppercase;
                // letter-spacing: 0em;
            }
        }

        // 中间部分
        .header_c {
            // position: absolute;
            display: flex;
            justify-content: space-around;
            align-items: center;
            width: 630px;
            height: 50px;
            // background-color: #df1e1e;

            // 搜索栏
            .el-input {
                width: 500px;
                // height: 80px;
                border-radius: 15px;
            }

            button {
                // flex: 1;
                width: 80px;
                height: 40px;
                border-radius: 15px;
                cursor: pointer;
                background: #64E6F4;
                box-sizing: border-box;
                border: 1px solid #666767
            }
        }

        // 右边
        .header_r {
            display: flex;
            justify-content: center;
            align-items: center;
            width: 150px;
            height: 50px;
            line-height: 50px;
            border-radius: 15px;
            opacity: 1;
            text-align: center;
            cursor: pointer;
            // background: #d38686;

            img {
                display: inline-block;
                width: 32px;
                height: 32px;
                border-radius: 50%;
                margin-right: 10px;
                background-color: #fff;
            }

            span {
                display: inline-block;
                width: 100px;
                // font-size: 16px;
                height: 2rem;
                line-height: 2rem;
                // 文本超出限制
                white-space: nowrap;
                overflow: hidden;
                text-align-last: left;
                // 文字局中
                vertical-align: middle;
                text-overflow: ellipsis;
                // background-color: #fff;
            }

            i {
                display: inline-block;
                margin-right: 15px;
                color: black;
            }

            input {
                width: 150px;
                height: 50px;
                border-radius: 15px;
                opacity: 1;
                background: #F6CA74;
                cursor: pointer;
                box-sizing: border-box;
                /* 首页/搜索框颜色 */
                border: 1px solid #666767;
            }
        }

    }

    // 侧边栏
    .el-aside {
        text-align: center;
        width: 200px;
        background-color: #D3DCE6;
        color: #333;
        text-align: center;

        ul {
            width: 200px;
            // background-color: #d48686;

            li {
                display: inline-block;
                width: 150px;
                height: 50px;
                border-radius: 15px;
                margin: 10px 0;
                // 上下局中
                vertical-align: middle;
                text-align: left;
                padding-left: 20px;
                line-height: 50px;
                color: #000;
                // background-color: #ffffff;

                span {
                    padding-left: 5px;
                }

                .icon {
                    width: 32px;
                    height: 32px;
                    vertical-align: middle;
                    fill: currentColor;
                    overflow: hidden;
                }
            }

            li:hover {
                background-color: #fff;
                cursor: pointer;
                color: #64E6F4;
            }

        }
    }

    // 主体内容
    .el-main {
        background-color: #E9EEF3;
        color: #333;
        text-align: center;
        line-height: 160px;
        padding: 0px 16px !important;
        height: calc(100vh - 15vh);
        overflow-y: auto;
    }



}</style>