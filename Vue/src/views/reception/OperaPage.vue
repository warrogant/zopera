<template>
    <div class="operavideo">
        <div class="w1200">
            <div class="opera_add">
                <span>戏曲视频</span>
                <!--  -->
                <svg class="icon addicon" aria-hidden="true" @click="addOperaVideo">
                    <use xlink:href="#opera-shipinshangchuan"></use>
                </svg>
            </div>
            <div class="video_header">
                <!-- <a href="/reception/operapage"></a>  -->
                <span @click="load()">类型</span>
                <button v-for="(operaType, index) in type" :key="index" @click="changeVideoList(operaType)"
                    :class="{ 'custom-underline': isOperaType === operaType }">{{ operaType }}</button>
            </div>
            <div class="video_list">
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
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="pageNum"
                :page-sizes="[8, 16, 20, 24]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper"
                :total="total">
            </el-pagination>
        </div>

        <!-- 上传 弹窗 -->
        <el-dialog title="戏曲信息" :visible.sync="dialogVisible" :before-close="handleClose" width="35vw">
            <el-form :label-position="labelPosition" label-width="80px" :model="form">
                <el-form-item label="戏曲封面">
                    <el-upload ref="upload" action="" list-type="picture-card" :on-preview="handlePictureCardPreview"
                        :on-remove="handleRemove" :on-change="UploadImage" :limit="1" :file-list="imageFileList"
                        :auto-upload="false" :on-success="handleAvatarSuccess" :on-exceed="handleExceed">
                        <i class="el-icon-plus"></i>
                        <template #tip>
                            <div style="font-size: 12px;color: #919191;">
                                单次限制上传一张图片,只能上传jpg/png/jpeg文件
                            </div>
                        </template>
                    </el-upload>
                </el-form-item>
                <el-form-item label="上传者：" prop="uploader">
                    <el-input v-model="form.uploader" disabled></el-input>
                </el-form-item>
                <el-form-item>
                    <el-upload action="http://localhost:9090/file/upload" :show-file-list="true" :limit="1"
                        :file-list="videoFileList" :on-success="handleFileUploadSuccess"
                        style="display: inline-block;text-align: left;">
                        <el-button type="primary" class="ml-5">上传戏曲 <i class="el-icon-top"></i></el-button>
                    </el-upload>
                </el-form-item>
                <el-form-item label="名称：" prop="name">
                    <el-input v-model="form.name"></el-input>
                </el-form-item>
                <el-form-item label="类别：" prop="operaType">
                    <el-select v-model="form.operaType" placeholder="请选择">
                        <el-option v-for="item in types" :key="item.value" :value="item.value" :disabled="item.disabled">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="介绍：" prop="introduce">
                    <el-input v-model="form.introduce" type="textarea"></el-input>
                </el-form-item>
            </el-form>
            <el-button @click="handleClose">取 消</el-button>
            <el-button type="primary" @click="save">确 定</el-button>
        </el-dialog>
        <FooterReception />
    </div>
</template>

<script>
import FooterReception from '@/views/reception/Footer.vue'
export default {
    name: 'OperaPage',
    inject: ['reload'],
    components: { FooterReception },
    data() {
        return {
            type: ['师公戏', '富宁壮戏', '隆林壮戏', '田林壮戏', '马隘壮戏', '德靖木偶剧', '广南壮戏', '其它'],
            operaType: '',
            videos: [],
            isOperaType: '',
            pageNum: 1,
            pageSize: 8,
            total: 0,
            name: '',
            state: 1,
            onSelect: this.$route.query.onSelect,
            form: {
                uploader: '',
                name: '',
                url: '',
                pic: '',
                operaType: '',
                introduce: '',
            },
            types:
                [{
                    value: '师公戏',
                    disabled: false
                }, {
                    value: '富宁壮戏',
                    disabled: false
                }, {
                    value: '隆林壮戏',
                    disabled: false
                }, {
                    value: '田林壮戏',
                    disabled: false
                }, {
                    value: '马隘壮戏',
                    disabled: false
                }, {
                    value: '德靖木偶剧',
                    disabled: false
                }, {
                    value: '广南壮戏',
                    disabled: false
                }, {
                    value: '其它',
                    disabled: false
                }],
            value: '',
            imageFileList: [],
            videoFileList: [],
            dialogVisible: false,
            labelPosition: 'right',
            user: localStorage.getItem('userInfo') ? JSON.parse(localStorage.getItem('userInfo')) : {},
        }
    },
    created() {
        this.load()
    },
    methods: {
        load() {
            if (this.onSelect !== null) {
                this.name = this.onSelect
                this.findFavoriteOperaPage()
                //   console.log(name);
            }else{
                this.onSelect=''
                this.findFavoriteOperaPage()
            }
        },
        findFavoriteOperaPage() {
            this.request.get('/reception/findFavoriteOperaPage', {
                params: {
                    pageNum: this.pageNum,
                    pageSize: this.pageSize,
                    operaType: this.operaType,
                    name: this.name,
                    state: this.state
                }
            }).then(res => {
                this.videos = res.data.records
                this.total = res.data.total
            })
        },
        changeVideoList(operaType) {
            this.operaType = operaType
            this.searchValue = ''
            this.findFavoriteOperaPage()
                // this.reload()
            // this.reload()
            this.isOperaType = (this.isOperaType === operaType) ? null : operaType;
        },
        // 点击跳转至视频播放页面
        viewopera(id) {
            this.request.get('/reception/updateOperaVideoByIdCount/' + id).then((result) => {
                this.videos.playback = result
            })
            this.$router.push({ path: '/reception/videoopera', query: { id: id } })
        },

        // 分页
        handleSizeChange(pageSize) {
            this.pageSize = pageSize
            this.load()
        },
        handleCurrentChange(pageNum) {
            this.pageNum = pageNum
            this.load()
        },
        addOperaVideo() {
            if (this.user.username) {
                this.dialogVisible = true;
                this.form.uploader = this.user.username
            } else {
                this.dialogVisible = false;
                this.$message.error('请登录后在进行操作')
            }
        },
        // 上传
        // 文件上传成功
        handleFileUploadSuccess(res) {
            // console.log(res.msg)
            if (res.msg != '文件格式不为MP4') {
                this.form.url = res.data
                this.$message.success("上传成功")
            } else {
                this.$message.error("上传失败，" + res.msg)
            }
            this.load()
        },
        // 新增弹窗
        increase() {
            this.dialogVisible = true;
            this.form = {};
            this.form.uploader = this.user.username
        },
        // 弹窗关闭
        handleClose() {
            this.closePopup();
            this.reload()
        },
        // 弹窗取消
        closePopup() {
            this.dialogVisible = false;
            this.load();
        },
        // 新增弹窗保存
        save() {
            this.request.post("/file/operaSave", this.form).then((res) => {
                // console.log(res.data);
                if (res.data) {
                    console.log('弹窗数据' + res.data);
                    // this.$refs.videoUpload.submit();
                    this.$message.success("保存成功");
                    this.dialogVisible = false;
                    this.load();
                } else {
                    this.$message.error("保存失败");
                }
            });
        },
        // 图片 上传
        UploadImage(file) {
            //console.log(file);
            let fd = new FormData()
            fd.append('file', file.raw) //传给后台接收的名字 file
            this.request.post('/file/avatorupload', fd, { headers: { 'Content-Type': 'multipart/form-data' } }).then(response => {
                //上传成功后返回的数据,
                console.log("上传图片到:" + response.data);
                this.$message.success('上传图片成功')
                // 将图片地址给到表单.
                this.form.pic = response.data
            })
            this.showPicUrl = false
        },
        //移除图片功能
        handleRemove(file, imageFileList) {
            console.log(file, imageFileList)

        },
        //预览图片功能
        handlePictureCardPreview(file) {
            // console.log(file.url);
            this.dialogVisible = true
            this.dialogImageUrl = file.url
            // this.dialogImageUrl = ''
        },
        //提交后重置上传界面表单
        resetForm(formName) {
            this.$refs.upload.clearFiles()
            this.$refs[formName].resetFields()
        },
        // 上传封面
        handleAvatarSuccess(res) {
            // console.log(res.data);
            // console.log(res.msg);
            if (res.code == '200') {
                this.$message.success('替换成功')
                // console.log(res.data);
                this.form.pic = res.data
            } else {
                this.$message.error('替换失败，文件格式不正确')
            }
        },
        // 超出限制时
        handleExceed(files, imageFileList) {
            this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + imageFileList.length} 个文件`);
        },
    }
}

</script>

<style lang="less" scoped>
.operavideo {
    padding: 40px 0 20px;

    // background-color: #fff;
    .opera_add {
        height: 70px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        // background-color: red;
        border-bottom: 3px solid #f67a7a;
        margin-bottom: 10px;

        span {
            font-family: Source Han Sans CN;
            font-size: 24px;
            line-height: 64px;
            font-weight: bold;
            letter-spacing: 0em;
            color: #3D3D3D;
        }

        svg {
            cursor: pointer;

        }
    }

    .video_header {

        display: flex;
        justify-content: space-around;
        align-items: center;
        font-size: 0;

        // background-color: #eb6c6c;
        span {
            display: block;
            width: 65px;
            font-size: 16px;
            line-height: 30px;
            font-weight: bold;
            cursor: pointer;
            // vertical-align: top;
            // background-color: #fff;
        }

        button {
            display: inline-block;
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
            text-decoration-line: underline;
            /* 下划线 */
            text-decoration-color: red;
            /* 下划线颜色 */
            text-decoration-style: dashed;
            /* 下划线样式，可以是 solid、dashed、dotted 等 */
            text-decoration-thickness: 3px;
            /* 下划线粗细，可以是具体的像素值或者 thin、medium、thick */
        }
    }

    .video_list {
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
                // position: absolute;
                // bottom: 0;
                // left: 0;
                width: 100%;
                padding: 10px;
                margin-top: 10px;
                // background-color: rgba(0, 0, 0, 0.7);
                color: #000000;
                font-size: 14px;
                line-height: 1.5;
                text-align: center;
                // opacity: 0;
                // transition: opacity 0.3s;

                border-bottom-left-radius: 15px;
                border-bottom-right-radius: 15px;

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

            // &:hover .video_info {
            //   opacity: 0.88;
            // }
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

.addicon {
    width: 64px;
    height: 64px;
    vertical-align: middle;
    fill: currentColor;
    overflow: hidden;
}
</style>