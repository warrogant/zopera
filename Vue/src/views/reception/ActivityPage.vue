<template>
    <div class="activity">
        <div class="w1200">
            <!-- <el-backtop></el-backtop>  -->
            <el-backtop target=".w1200"></el-backtop>
            <div class="activity_add">
                <span>活动组织</span>
                <svg class="icon addicon" aria-hidden="true" @click="addActivity">
                    <use xlink:href="#opera-iconset0104"></use>
                </svg>
            </div>
            <div class="activity_list">
                <div class="activity_title" v-for="(activit, index) in activities" :key="index"
                    @click="activitopera(activit.id)">
                    <img :src="activit.coverUrl" alt="" style="float: left; margin-right: 20px;">
                    <div class="activity_title_t">
                        <h1>{{ activit.name }}</h1>
                        <p>{{ activit.content }}</p>
                    </div>
                    <div class="activity_title_b">
                        <svg class="icon" aria-hidden="true">
                            <!-- v-bind: -->
                            <use xlink:href="#opera-24gf-user2"></use>
                        </svg>&nbsp;<span>{{ activit.sponsor }}</span>
                        <span>{{ activit.publishTime | datetime }}</span>
                        <svg class="icon" aria-hidden="true">
                            <!-- v-bind: -->
                            <use xlink:href="#opera-dianjiliang"></use>
                        </svg>&nbsp;<span>{{ activit.count }}</span>
                        <span :class="{ expired: isExpired }" class="ex" v-if="activit.endTime <= currentTime">
                            <svg class="iconYiguoqi" aria-hidden="true">
                                <use xlink:href="#opera-yiguoqi"></use>
                            </svg>
                        </span>
                    </div>
                </div>
            </div>
        </div>
        <div style="padding: 10px 0">
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="pageNum"
                :page-sizes="[5, 10, 20]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper"
                :total="total">
            </el-pagination>
        </div>
        <FooterReception />

        <!-- 弹窗 -->
        <el-dialog title="发布活动信息" :visible.sync="dialogVisible" :before-close="handleClose" width="30%">
            <el-form :label-position="labelPosition" label-width="80px" :model="form" :rules="rules">
                <el-form-item label="活动封面">
                    <!-- <img src="@/assets/img/404.png" alt="" style="width: 130px;
                    height: 130px;"> -->
                    <el-upload ref="upload" action="" list-type="picture-card" :on-preview="handlePictureCardPreview"
                        :on-remove="handleRemove" :on-change="UploadImage" :limit="1" :file-list="fileList"
                        :auto-upload="false">
                        <i class="el-icon-plus"></i>
                        <template #tip>
                            <div style="font-size: 12px;color: #919191;">
                                单次限制上传一张图片
                            </div>
                        </template>
                    </el-upload>
                    <!-- <el-input v-model="form.coverUrl">
                    </el-input> -->
                </el-form-item>
                <el-form-item label="发起人" prop="sponsor">
                    <el-input v-model="form.sponsor" disabled></el-input>
                </el-form-item>
                <el-form-item label="名称" prop="name">
                    <el-input v-model="form.name"></el-input>
                </el-form-item>
                <el-form-item label="内容" prop="content">
                    <!-- <el-input v-model=""></el-input> -->
                    <el-input type="textarea" :rows="2" placeholder="请输入内容" maxlength="2000" show-word-limit v-model="form.content"></el-input>
                </el-form-item>
                <el-form-item label="开始时间" prop="startTime">
                    <el-col :span="5">
                        <el-date-picker v-model="form.startTime" type="datetime" placeholder="选择日期时间">
                        </el-date-picker>
                    </el-col>
                </el-form-item>
                <el-form-item label="结束时间" prop="endtime">
                    <el-col :span="6">
                        <el-date-picker v-model="form.endTime" type="datetime" placeholder="选择日期时间">
                        </el-date-picker>
                    </el-col>
                </el-form-item>
                <el-form-item label="地点" prop="location">
                    <el-cascader size="large" maxlength="10" show-word-limit :options="options" v-model="form.selectedOptions" style="width: 100%;"
                        @change="addressChange(form.selectedOptions)">
                    </el-cascader>
                    <!-- <el-option v-for="option in options" :key="option" :label="option" :value="option"></el-option> -->
                </el-form-item>
                <el-form-item prop="detail">
                    <el-input type="textarea" maxlength="200" show-word-limit placeholder="详细地址" v-model="form.detail" clearable></el-input>
                </el-form-item>
            </el-form>
            <el-button @click="closePopup">取 消</el-button>
            <el-button type="primary" @click="save">确 定</el-button>
        </el-dialog>
    </div>
</template>

<script>
import FooterReception from '@/views/reception/Footer.vue'
// provinceAndCityData, regionData, regionDataPlus, CodeToText, TextToCode, provinceAndCityDataPlus
import { regionData, CodeToText } from 'element-china-area-data'

export default {
    name: 'ActivityPage',
    components: { FooterReception },
    data() {
        return {
            options: regionData,
            fileList: [],
            dialogImageUrl: '',
            publishTime: '',
            pageNum: 1,
            pageSize: 5,
            total: 0,
            sponsor: '',
            state: 0,
            // 当前时间
            currentTime: '',
            endTimes: [],
            form: {
                name: '',
                sponsor: '',
                content: '',
                startTime: '',
                endTime: '',
                coverUrl: '',
                location: '',
                selectedOptions: [],
                detail: '',
                state: 0
            },
            user: localStorage.getItem('userInfo') ? JSON.parse(localStorage.getItem('userInfo')) : {},
            dialogVisible: false,
            labelPosition: "right",
            activities: [],
            // 表单校验
            rules: {
                name: [
                    { required: true, message: "请输入活动名称", trigger: "blur" },
                    { min: 3, max: 30, message: "请输入3 到 30个字符", trigger: "blur" },
                ],
                content: [
                    { required: true, message: "请输入活动内容", trigger: "blur" },
                    { min: 5, max: 1000, message: "请输入 5 到 1000 个字符", trigger: "blur" },
                ],
            },
            isExpired: true
        }
    },
    inject: ['reload'],
    created() {
        this.load()
        this.getCurrentTime()
    },
    methods: {
        getCurrentTime() {
            const currentDate = new Date();
            // 定义日期格式选项
            const options = {
                year: 'numeric', // 年份，例如：2022
                month: '2-digit', // 月份，例如：02
                day: '2-digit' // 日期，例如：31
            };
            this.currentTime = currentDate.toLocaleDateString('zh-CN', options).replace(/\//g, '-');
            // console.log(this.currentTime);

        },
        load() {
            this.sponsor = this.user.username
            this.form.sponsor = this.sponsor
            this.state = 1
            this.request.get('/reception/findActivityPage/', {
                params: {
                    pageNum: this.pageNum,
                    pageSize: this.pageSize,
                    state: this.state,
                }
            }).then((result) => {
                // console.log(result.data.records);
                this.activities = result.data.records
                this.total = result.data.total
                result.data.records.forEach((record) => {
                    this.endTimes = record.endTime
                });
                // console.log(this.videos.playback);
            })
        },
        activitopera(id) {
            // console.log(id);
            this.request.get('/comment/updateActivityByIdCount/' + id).then((result) => {
                // console.log(id);
                // console.log(result);
                this.activities.count = result
                // this.reload()
            });
            // 将活动的id传递出去
            this.$router.push({ path: '/reception/ActivitView', query: { id: id } })

        },
        addActivity() {
            if (this.user.username) {
                this.dialogVisible = true;
            } else {
                this.dialogVisible = false;
                this.$message.error('请登录后在进行操作')
            }

        },

        // 新增弹窗
        increase() {
            this.dialogVisible = true;
            this.form = {};
        },
        // 弹窗取消
        closePopup() {
            this.dialogVisible = false;
        },
        // 弹窗关闭
        handleClose() {
            this.closePopup();
        },
        save() {
            // this.form.location = JSON.stringify(this.form.selectedOptions)
            this.request.post("/reception/ActivitySave", this.form).then((res) => {
                if (res.code === '200') {
                    // console.log(res.data);
                    this.$message.success("保存成功");
                    this.form = {}
                    this.dialogVisible = false;
                    this.load()
                } else {
                    this.$message.error("保存失败");
                    this.dialogVisible = false;
                }
            });
        },
        //上传图片的方法
        UploadImage(file) {
            //console.log(file);
            let fd = new FormData()
            fd.append('file', file.raw) //传给后台接收的名字 file
            this.request.post('/file/avatorupload', fd, { headers: { 'Content-Type': 'multipart/form-data' } }).then(response => {
                //上传成功后返回的数据,
                console.log("上传图片到:" + response.data);
                this.$message.success('上传图片成功')
                // 将图片地址给到表单.
                this.form.coverUrl = response.data
            })

        },
        //移除图片功能
        handleRemove(file, fileList) {
            console.log(file, fileList)

        },
        //预览图片功能
        handlePictureCardPreview(file) {
            // console.log(file.url);
            this.dialogVisible = true
            this.dialogImageUrl = file.url

        },
        //提交后重置上传界面表单
        resetForm(formName) {
            this.$refs.upload.clearFiles()
            this.$refs[formName].resetFields()
        },
        addressChange(arr) {
            console.log(arr);
            this.form.location = `${CodeToText[arr[0]]}/${CodeToText[arr[1]]}/${CodeToText[arr[2]]}`
            console.log(this.form.location);
        },
        // 分页
        handleSizeChange(pageSize) {
            // console.log(pageSize)
            this.pageSize = pageSize
            this.load()
        },
        handleCurrentChange(pageNum) {
            // console.log(pageNum)
            this.pageNum = pageNum
            this.load()
        },
    }
}



</script>

<style lang="less" scoped>
.activity {
    padding: 30px 0;
    // background-color: #fff;

    .activity_add {
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

    .activity_list {
        display: grid;
        grid-template-columns: repeat(1, 1fr);
        grid-gap: 20px;
        margin: 20px 0;
        // background-color: #fd9292;
        border-radius: 15px;

        img {
            width: 256px;
            height: 218px;
            border-radius: 15px;
        }

        .activity_title {
            height: 190px;
            // background-color: #fff;
            margin: 15px;
            cursor: pointer;

            .activity_title_t {
                height: 150px;
                width: 100vw-150px;
                // background-color: rgb(245, 164, 164);
                text-align: left;

                h1 {
                    font-family: Source Han Sans CN;
                    font-size: 24px;
                    line-height: 64px;
                    font-weight: bold;
                    letter-spacing: 0em;
                    color: #3D3D3D;
                    // 超出不显示
                    display: -webkit-box;
                    -webkit-box-orient: vertical;
                    -webkit-line-clamp: 1;
                    overflow: hidden;
                    text-overflow: ellipsis;
                }

                p {
                    font-family: KaiTi;
                    font-size: 24px;
                    font-weight: normal;
                    line-height: 38px;
                    letter-spacing: 0em;
                    color: #9F9A9A;
                    // 超出不显示
                    display: -webkit-box;
                    -webkit-box-orient: vertical;
                    -webkit-line-clamp: 2;
                    overflow: hidden;
                    text-overflow: ellipsis;
                }
            }

            .activity_title_b {
                height: 40px;
                // background-color: #fff;
                text-align: left;
                line-height: 40px;

                .expired,
                .ex {
                    margin: 10px 100px
                }

                span {
                    display: inline-block;
                    margin-left: 5px;
                    font-family: KaiTi;
                    font-size: 16px;
                    font-weight: normal;
                    line-height: 16px;
                    letter-spacing: 0em;
                    color: #9F9A9A;
                    vertical-align: middle;

                    // display: -webkit-box;
                    -webkit-box-orient: vertical;
                    -webkit-line-clamp: 1;
                    overflow: hidden;
                    text-overflow: ellipsis;
                }

                span:nth-of-type(2) {
                    margin: 0 40px;
                }
            }
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

.addicon {
    width: 64px;
    height: 64px;
    vertical-align: middle;
    fill: currentColor;
    overflow: hidden;
}

.iconYiguoqi {
    width: 64px;
    height: 64px;
    vertical-align: middle;
    fill: currentColor;
    overflow: hidden;

}
</style>