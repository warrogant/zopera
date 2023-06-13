<template>
    <div class="myopera">
        <div class="w1200">
            <div class="user-profile">
                <div class="user-profile_header">
                    <div class="user-profile_header_l">
                        <div class="user-profile_header_avator"
                            :style="{ background: 'url(' + user.avator + ') center/cover no-repeat' }"
                            :data-background="user.avator">
                        </div>
                    </div>
                    <div class="user-profile_header_r">
                        <h3 v-if="user.username"><span>{{ user.nickname }}</span>
                            <router-link to="/reception/PersonInfo"> <button><i
                                        class="el-icon-edit"></i>&nbsp;编辑个人信息</button></router-link>
                        </h3>
                        <div class="header_r_t">
                            <span><strong>{{ favoriteCount }}</strong> 收藏</span>
                            <span><strong>{{ total }}</strong> 上传</span>
                            <span><strong>{{ totalCulture }}</strong> 活动</span>
                        </div>
                    </div>
                </div>
                <div class="user-profile_center">
                    <div class="user-profile_center_top">
                        <span v-for="(text, index) in textList" :key="index" @click="showContent(text)"
                            :class="{ 'custom-underline': selectedOption === text }">
                            {{ text }}</span>
                        <el-button type="primary" class="ml-5" @click="increaseOpera" v-if="selectedOption === '上传的戏曲'">上传戏曲
                            <i class="el-icon-plus"></i></el-button>
                        <el-button type="primary" class="ml-5" @click="increaseActivity"
                            v-if="selectedOption === '发布的活动'">发布活动&nbsp;&nbsp;<i class="el-icon-plus"></i></el-button>
                    </div>
                    <div class="user-profile_center_content">
                        <div v-if="selectedOption === '收藏的戏曲'">
                            <div class="recommend_list">
                                <div v-for="(video, index) in videos" :key="index" class="video_item"
                                    @click="viewopera(video.id)">
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

                        <div v-if="selectedOption === '上传的戏曲'">
                            <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"
                                @selection-change="handleSelectionChange">
                                <el-table-column prop="name" label="戏曲名称" @click="viewOperas()"></el-table-column>
                                <el-table-column prop="pic" label="戏曲封面">
                                    <template slot-scope="scope">
                                        <el-button type="text" @click="dialogPicVisible(scope.row.pic)">预览封面</el-button>
                                    </template>
                                </el-table-column>
                                <el-table-column prop="operaType" label="戏曲类别"></el-table-column>
                                <el-table-column prop="introduce" label="简介">
                                    <template slot-scope="scope">
                                        <el-button type="text"
                                            @click="dialogContentVisibled(scope.row.introduce)">查看内容</el-button>
                                    </template>
                                </el-table-column>
                                <el-table-column prop="state" label="审核结果" width="200" align="center">
                                    <template slot-scope="scope">
                                        <div v-if="scope.row.state == 0">
                                            <el-button type="warning">审核中......</el-button>
                                        </div>
                                        <div v-if="scope.row.state == 1">
                                            <el-button type="success">审核通过</el-button>
                                        </div>
                                        <div v-if="scope.row.state == 2">
                                            <el-button type="danger">审核不通过</el-button>
                                        </div>
                                    </template>
                                </el-table-column>
                                <el-table-column prop="stateInfo" label="审核信息"></el-table-column>
                            </el-table>

                            <div style="margin: 10px 0">
                                <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                                    :current-page="pageNum" :page-sizes="[5, 10, 20]" :page-size="pageSize"
                                    layout="total, sizes, prev, pager, next, jumper" :total="total">
                                </el-pagination>
                            </div>
                        </div>

                        <div v-if="selectedOption === '发布的活动'">
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
                                        </svg>&nbsp;<span>{{ activit.count }}</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                        <span v-if="activit.state == 0">
                                            <el-button type="warning">审核中......</el-button>
                                        </span>
                                        <span v-if="activit.state == 1">
                                            <el-button type="success">审核通过</el-button>
                                        </span>
                                        <span v-if="activit.state == 2">
                                            <el-button type="danger">审核不通过</el-button>&nbsp;
                                            <span>原因：{{ activit.stateInfo }}</span>
                                        </span>
                                    </div>
                                </div>
                            </div>

                            <div style="margin: 10px 0">
                                <el-pagination @size-change="handleSizeChangeCulture"
                                    @current-change="handleCurrentChangeCulture" :current-page="pageNumCulture"
                                    :page-sizes="[5, 10, 20]" :page-size="pageSizeCulture"
                                    layout="total, sizes, prev, pager, next, jumper" :total="totalCulture">
                                </el-pagination>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- 封面  弹窗  -->
            <el-dialog title="封面预览" :visible.sync="dialogPicVisibled" width="25%" :before-close="handleClose"
                style="height: 100vh;">
                <img :src="form.pic" alt="" style="width: 100%;height: 100%;  margin: 0 auto;">
                <span slot="footer" class="dialog-footer">
                    <el-button @click="dialogPicVisibled = false">取 消</el-button>
                    <el-button type="primary" @click="dialogPicVisibled = false">确 定</el-button>
                </span>
            </el-dialog>
            <!-- 内容  弹窗  -->
            <el-dialog title="简介详情" :visible.sync="dialogContentVisible" width="25%" :before-close="handleClose"
                style="height: 100vh; line-height: 20px;">
                <span>{{ form.introduce }}</span>
                <span slot="footer" class="dialog-footer">
                    <el-button @click="dialogContentVisible = false">取 消</el-button>
                    <el-button type="primary" @click="dialogContentVisible = false">确 定</el-button>
                </span>
            </el-dialog>
            <!-- 上传 弹窗 -->
            <el-dialog title="戏曲信息" :visible.sync="dialogVisible" :before-close="handleClose" width="35%">
                <el-form :label-position="labelPosition" label-width="80px" :model="uploadForm">
                    <el-form-item label="戏曲封面">
                        <el-upload ref="upload" action="" list-type="picture-card" :on-preview="handlePictureCardPreview"
                            :on-remove="handleRemove" :on-change="UploadImage" :limit="1" :file-list="imageFileList"
                            :auto-upload="false" :on-success="handleAvatarSuccess" :on-exceed="handleExceed">
                            <!-- <img v-if="showPicUrl" :src="form.picUrl"> -->
                            <i class="el-icon-plus" v-if="imageFileList"></i>
                            <template #tip>
                                <div style="font-size: 12px;color: #919191;">
                                    单次限制上传一张图片,仅上传jpg/png/jpeg文件
                                </div>
                            </template>
                        </el-upload>
                    </el-form-item>
                    <el-form-item label="上传者：" prop="uploader">
                        <el-input v-model="uploadForm.uploader" disabled></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-upload action="http://localhost:9090/file/upload" :show-file-list="true" :limit="1"
                            :file-list="videoFileList" :on-success="handleFileUploadSuccess" style="display: inline-block">
                            <el-button type="primary" class="ml-5">上传戏曲 <i class="el-icon-top"></i></el-button>
                        </el-upload>
                    </el-form-item>
                    <el-form-item label="名称：" prop="name">
                        <el-input v-model="uploadForm.name"></el-input>
                    </el-form-item>
                    <el-form-item label="类别：" prop="operaType">
                        <el-select v-model="uploadForm.operaType" placeholder="请选择">
                            <el-option v-for="item in types" :key="item.value" :value="item.value"
                                :disabled="item.disabled">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="介绍：" prop="introduce">
                        <el-input v-model="uploadForm.introduce" type="textarea" maxlength="500" show-word-limit></el-input>
                    </el-form-item>
                </el-form>
                <el-button @click="handleClose">取 消</el-button>
                <el-button type="primary" @click="save">确 定</el-button>
            </el-dialog>

            <!-- 发布活动 弹窗 -->
            <el-dialog title="活动信息" :visible.sync="dialogActivityVisible" :before-close="handleClose" width="35%">
                <el-form :label-position="labelPosition" label-width="80px" :model="formActivity" :rules="rules">
                    <el-form-item label="活动封面">
                        <el-upload ref="upload" action="" list-type="picture-card" :on-preview="handleActivityPictureCardPreview"
                            :on-remove="handleActivityRemove" :on-change="UploadActivityImage" :limit="1" :file-list="fileList"
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
                        <el-input v-model="formActivity.sponsor" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="名称" prop="name">
                        <el-input v-model="formActivity.name"></el-input>
                    </el-form-item>
                    <el-form-item label="内容" prop="content">
                        <!-- <el-input v-model=""></el-input> -->
                        <el-input type="textarea" :rows="2" placeholder="请输入内容" v-model="formActivity.content"
                            maxlength="2000" show-word-limit></el-input>
                    </el-form-item>
                    <el-form-item label="开始时间" prop="startTime">
                        <el-col :span="5">
                            <el-date-picker v-model="formActivity.startTime" type="date" placeholder="选择日期时间"
                                value-format="yyyy-MM-dd">
                            </el-date-picker>
                        </el-col>
                    </el-form-item>
                    <el-form-item label="结束时间" prop="endTime">
                        <el-col :span="6">
                            <el-date-picker v-model="formActivity.endTime" type="date" placeholder="选择日期时间"
                                value-format="yyyy-MM-dd">
                            </el-date-picker>
                        </el-col>
                    </el-form-item>
                    <el-form-item label="地点" prop="location">
                        <el-cascader size="large" :options="options" v-model="formActivity.selectedOptions"
                            style="width: 100%;" @change="addressChange(formActivity.selectedOptions)">
                        </el-cascader>
                        <!-- <el-option v-for="option in options" :key="option" :label="option" :value="option"></el-option> -->
                    </el-form-item>
                    <el-form-item prop="detail">
                        <el-input type="textarea" placeholder="详细地址" v-model="formActivity.detail" clearable maxlength="100"
                            show-word-limit></el-input>
                    </el-form-item>
                </el-form>
                <el-button @click="handleClose">取 消</el-button>
                <el-button type="primary" @click="saveActivity">确 定</el-button>
            </el-dialog>
        </div>
        <FooterReception />
    </div>
</template>

<script>
import FooterReception from '@/views/reception/Footer.vue'
import { regionData, CodeToText } from 'element-china-area-data'
export default {
    name: 'MyOpera',
    components: { FooterReception },
    inject: ['reload'],
    data() {
        return {
            user: localStorage.getItem('userInfo') ? JSON.parse(localStorage.getItem('userInfo')) : {},
            options: regionData,
            textList: [
                '收藏的戏曲',
                '上传的戏曲',
                '发布的活动'
            ],
            pageNum: 1,
            pageSize: 5,
            total: 0,
            pageNumCulture: 1,
            pageSizeCulture: 5,
            totalCulture: 0,
            favoriteCount: 0,
            state: 3,
            videos: [],
            tableData: [],
            activities: [],
            imageFileList: [],
            videoFileList: [],
            fileList: [],
            form: {
                uploader: '',
                name: '',
                url: '',
                pic: '',
                operaType: '',
                introduce: '',
            },
            uploadForm: {
                uploader: '',
                name: '',
                url: '',
                pic: '',
                operaType: '',
                introduce: '',
            },
            formActivity: {
                name: '',
                sponsor: '',
                content: '',
                startTime: '',
                endTime: '',
                coverUrl: '',
                location: '',
                selectedOptions: [],
                detail: '',
                state: 0,
            },
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
            count: 1000, // 你的数字
            selectedOption: '',
            dialogVisible: false,
            dialogActivityVisible: false,
            dialogContentVisible: false,
            dialogPicVisibled: false,
            labelPosition: 'right',
        }
    },
    created() {
        if (this.user.username) {
            this.load()
            // 默认选择第一个选项
            if (this.textList.length > 0) {
                this.selectedOption = this.textList[0]; // 设置初始值为第一个选项的名称
            }
        } else {
            this.$message.error('请登录后在进行操作')
        }
    },
    methods: {
        load() {
            this.findFavoriteOperaById()
            this.findOperaUploader()
            this.findCultureSponsorPage()
        },
        // 点击展示不同内容
        showContent(option) {
            if (this.selectedOption === option) {
                // 点击同一个选项，取消下划线
                this.selectedOption = '';
            } else {
                // 点击不同的选项，显示下划线
                this.selectedOption = option;
            }
        },
        // 收藏戏曲查询
        findFavoriteOperaById(id) {
            id = this.user.id
            // console.log(this.user.id);
            this.request.get('/reception/findFavoriteOperaById/' + id).then((result) => {
                this.videos = result.data
                this.favoriteCount = result.data.length
                // console.log(this.favoriteCount);
                // console.log(this.videos.playback);
            })
        },
        // 上传的戏曲查询
        findOperaUploader(uploader) {
            uploader = this.user.username
            // console.log(uploader);
            this.request.get('/reception/findOperaUploaderPage/', {
                params: {
                    pageNum: this.pageNum,
                    pageSize: this.pageSize,
                    uploader: uploader,
                }
            }).then((result) => {
                this.tableData = result.data.records
                this.total = result.data.total
                // console.log(result.data.records);
                // console.log(this.videos.playback);
            })
        },
        
        // 点击跳转至视频播放页面
        viewOperas(id) {
            this.$router.push({ path: '/reception/videoopera', query: { id: id } })
            this.request.get('/reception/updateOperaVideoByIdCount/' + id).then((result) => {
                this.videos.playback = result
            })
        },
        // 发布的活动查询
        findCultureSponsorPage(sponsor) {
            sponsor = this.user.username
            // console.log(sponsor);
            this.request.get('/reception/findActivityPage/', {
                params: {
                    pageNum: this.pageNumCulture,
                    pageSize: this.pageSizeCulture,
                    sponsor: sponsor,
                    state: this.state
                }
            }).then((result) => {
                this.activities = result.data.records
                this.totalCulture = result.data.total
                // console.log(this.activities);
                // console.log(result.data.records);
                // console.log(this.videos.playback);
            })
        },
        // 点击跳转至活动页面
        activitopera(id) {
            this.request.get('/comment/updateActivityByIdCount/' + id).then((result) => {
                this.activities.count = result
                // this.reload()
            });
            // 将活动的id传递出去
            this.$router.push({ path: '/reception/ActivitView', query: { id: id } })
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
        // 新增弹窗
        increaseOpera() {
            this.dialogVisible = true;
            this.uploadForm = {};
            this.uploadForm.uploader = this.user.username
        },
        // 新增弹窗
        increaseActivity() {
            this.dialogActivityVisible = true;
            this.formActivity = {};
            this.formActivity.sponsor = this.user.username
        },
        // 弹窗关闭
        handleClose() {
            this.closePopup();
            this.reload()
        },
        // 弹窗取消
        closePopup() {
            this.dialogVisible = false;
            this.dialogActivityVisible = false
            this.dialogContentVisible = false
            this.dialogPicVisibled = false,
                this.load();
        },
        dialogContentVisibled(introduce) {
            this.dialogContentVisible = true
            this.form.introduce = introduce
            // console.log(this.picContent);
            // console.log(picContent);
        },
        dialogPicVisible(pic) {
            this.dialogPicVisibled = true
            this.form.pic = pic
            // console.log(this.picContent);
            // console.log(picContent);
        },
        handleSelectionChange(val) {
            // console.log(val)
            this.multipleSelection = val
        },
        // 分页
        // 上传
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
        // 文件上传成功
        handleFileUploadSuccess(res) {
            // console.log(res.msg)
            if (res.msg != '文件格式不为MP4') {
                this.uploadForm.url = res.data
                this.$message.success("上传成功")
            } else {
                this.$message.error("上传失败，" + res.msg)
            }
            this.load()
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
                this.uploadForm.pic = response.data
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
        // 图片 上传
        UploadActivityImage(file) {
            //console.log(file);
            let fd = new FormData()
            fd.append('file', file.raw) //传给后台接收的名字 file
            this.request.post('/file/avatorupload', fd, { headers: { 'Content-Type': 'multipart/form-data' } }).then(response => {
                //上传成功后返回的数据,
                console.log("上传图片到:" + response.data);
                this.$message.success('上传图片成功')
                // 将图片地址给到表单.
                this.formActivity.coverUrl = response.data
            })
            this.showPicUrl = false
        },
        // handleActivityAvatarSuccess(res){
        //     if (res.code == '200') {
        //         this.$message.success('替换成功')
        //         // console.log(res.data);
        //         this.formActivity.coverUrl = res.data
        //     } else {
        //         this.$message.error('替换失败，文件格式不正确')
        //     }
        // },
        //移除图片功能
        handleActivityRemove(file, fileList) {
            console.log(file, fileList)
        },
        //预览图片功能
        handleActivityPictureCardPreview(file) {
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
                this.uploadForm.pic = res.data
            } else {
                this.$message.error('替换失败，文件格式不正确')
            }
        },
        // 超出限制时
        handleExceed(files, imageFileList) {
            this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + imageFileList.length} 个文件`);
        },
        // 戏曲弹窗保存
        save() {
            this.request.post("/file/operaSave", this.uploadForm).then((res) => {
                // console.log(res.data);
                if (res.data) {
                    // console.log('弹窗数据' + res.data);
                    // this.$refs.videoUpload.submit();
                    this.$message.success("保存成功");
                    this.dialogVisible = false;
                    this.load();
                } else {
                    this.$message.error("保存失败");
                }
            });
        },

        // 新增弹窗保存
        saveActivity() {
            // this.formActivity.location = JSON.stringify(this.formActivity.selectedOptions)
            this.request.post("/reception/ActivitySave", this.formActivity).then((res) => {
                if (res.code === '200') {
                    // console.log(res.data);
                    this.$message.success("保存成功");
                    // this.formActivity = {}
                    this.dialogActivityVisible = false;
                    this.load()
                } else {
                    this.$message.error("保存失败");
                    this.dialogActivityVisible = false;
                }
            });
        },
        // 发布的活动
        handleSizeChangeCulture(pageSizeCulture) {
            // console.log(pageSize)
            this.pageSizeCulture = pageSizeCulture
            this.load()
        },
        handleCurrentChangeCulture(pageNumCulture) {
            // console.log(pageNum)
            this.pageNumCulture = pageNumCulture
            this.load()
        },
        addressChange(arr) {
            // console.log(arr);
            this.formActivity.location = `${CodeToText[arr[0]]}/${CodeToText[arr[1]]}/${CodeToText[arr[2]]}`
        },
       
    },
    computed: {
        // 数据超出999替换成999+
        formattedCount() {
            // <p>{{ formattedCount }}</p>
            if (this.count > 999) {
                return '999+';
            } else {
                return this.count;
            }
        }
    }
}
</script>

<style lang="less" scoped>
.user-profile {
    // display: flex;
    // align-items: center;
    margin: 20px 0;
    // background-color: #fff;

    .user-profile_header {
        display: flex;
        justify-content: center;
        align-items: center;
        // background-color: #e5c4c4;
        width: 100%;

        .user-profile_header_l {
            width: 20%;
            // background-color: #e74747;

            .user-profile_header_avator {
                // position: relative;
                width: 150px;
                height: 150px;
                border-radius: 50%;
                background: attr(data-background) center/cover no-repeat;
                background-size: 100% 100%;
                margin: 0 auto;

            }
        }

        .user-profile_header_r {
            width: 80%;
            text-align-last: left;
            line-height: 62px;
            // background-color: #000;

            h3 {
                position: relative;
                // display: flex;
                // justify-content: space-evenly;
                font-family: Source Han Sans CN;
                font-size: 24px;
                font-weight: bold;
                letter-spacing: 0em;
                color: #3D3D3D;
                border-bottom: 1px solid #3D3D3D;

                // background-color: #746e6e;

                button {
                    position: absolute;
                    top: 14px;
                    right: 0;
                    outline: none;
                    cursor: pointer;
                    border: 1px solid #3D3D3D;
                    border-radius: 15px;
                    line-height: 32px;
                    font-size: 16px;
                    margin-right: 0;
                }
            }

            .header_r_t {
                display: flex;
                width: 30%;
                margin: 10px 0;

                // background-color: #6e5151;
                span {
                    display: inline-block;
                    margin: 0 auto;
                    width: 5em;
                    line-height: 32px;
                    font-size: 16px;
                    padding: 0 20px;
                    cursor: pointer;

                    // background-color: #fff;
                    strong {
                        display: block;
                        margin: 0 auto;
                        text-align: center;
                    }
                }

                :nth-child(2) {
                    // background-color: #000;
                    border-left: 1px #3D3D3D solid;
                    border-right: 1px #3D3D3D solid;
                }
            }
        }
    }

    .user-profile_center {
        width: 100%;
        // height: 120px;
        margin: 30px 0 10px;
        line-height: 32px;
        // background-color: #fff;
        text-align-last: left;

        .user-profile_center_top {
            margin: 10px 0;
            width: 50%;

            // background-color: #3D3D3D;
            span {
                padding: 0 20px;
                font-family: Source Han Sans CN;
                font-weight: bold;
                letter-spacing: 0em;
                color: #3D3D3D;
                cursor: pointer;

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

        .user-profile_center_content {
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
</style>