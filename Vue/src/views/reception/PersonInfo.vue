<template>
    <div>
        <div class="w1200">
            <div class="user-profile">
                <div class="user-profile_center">
                    <div class="user-profile_header_r">
                        <h3><span>个人信息编辑</span></h3>
                    </div>
                    <div class="user-profile_center_top">

                        <div class="backrouter" @click="prev()"><svg class="icon" aria-hidden="true">
                                <!-- v-bind: -->
                                <use xlink:href="#opera-xingzhuanggongnengtubiao-"></use>
                            </svg>返回
                        </div>
                        <span v-for="(text, index) in textList" :key="index" @click="showContent(text)"
                            :class="{ 'custom-underline': selectedOption === text }">
                            {{ text }}</span>
                    </div>
                    <div class="user-profile_center_content">
                        <div v-if="selectedOption === '个人信息修改'">
                            <el-card style="width: 500px;">
                                <el-form :label-position="labelPosition" label-width="80px" :model="form">
                                    <el-upload class="avatar-uploader" action="http://localhost:9090/file/avatorupload"
                                        :show-file-list="false" :on-success="handleAvatarSuccess">
                                        <div class="avatar">
                                            <img v-if="form.avator" :src="form.avator">
                                            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                                        </div>
                                    </el-upload>
                                    <el-form-item label="昵称：" prop="nickname">
                                        <el-input v-model="form.nickname"></el-input>
                                    </el-form-item>
                                    <el-form-item label="用户名：" disabled prop="username">
                                        <el-input v-model="form.username" disabled></el-input>
                                    </el-form-item>
                                    <!-- <el-form-item label="密码：" prop="password" autocomplete="off">
                                        <el-input v-model="form.password"></el-input>
                                    </el-form-item> -->
                                    <el-form-item label="邮箱：">
                                        <el-input v-model="form.email"></el-input>
                                    </el-form-item>
                                    <el-form-item label="电话：">
                                        <el-input v-model="form.phone"></el-input>
                                    </el-form-item>
                                    <el-form-item label="出生日期">
                                        <el-col :span="11">
                                            <el-date-picker type="date" placeholder="选择日期" v-model="form.birth"
                                                style="width: 100%"></el-date-picker>
                                        </el-col>
                                    </el-form-item>
                                    <el-form-item label="选择所在地区" prop="location">
                                        <el-cascader size="large" :options="options" v-model="form.selectedOptions"
                                            @change="addressChange(form.selectedOptions)">
                                        </el-cascader>
                                    </el-form-item>
                                    <el-form-item>
                                        <el-button type="primary" @click="save">确 定</el-button>
                                        <el-button type="primary" @click="back">取 消</el-button>
                                    </el-form-item>
                                </el-form>
                            </el-card>
                        </div>
                        <div v-if="selectedOption === '修改密码'">
                            <el-card style="width: 500px;">
                                <el-form label-width="120px" size="small" :model="form" :rules="rules" ref="pass">
                                    <el-form-item label="原密码" prop="password">
                                        <el-input v-model="form.password" autocomplete="off" show-password
                                            disabled></el-input>
                                    </el-form-item>
                                    <el-form-item label="新密码" prop="newPassword">
                                        <el-input v-model="form.newPassword" autocomplete="off" show-password></el-input>
                                    </el-form-item>
                                    <el-form-item label="确认新密码" prop="ChackPassword">
                                        <el-input v-model="form.ChackPassword" autocomplete="off" show-password></el-input>
                                    </el-form-item>
                                    <el-form-item>
                                        <el-button type="primary" @click="editPassword">确 定</el-button>
                                    </el-form-item>
                                </el-form>
                            </el-card>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
// provinceAndCityData, regionData, regionDataPlus, CodeToText, TextToCode, provinceAndCityDataPlus
import { regionData, CodeToText } from 'element-china-area-data'
export default {
    name: 'PersonInfo',
    data() {
        return {
            form: { selectedOptions: [], },
            user: localStorage.getItem('userInfo') ? JSON.parse(localStorage.getItem('userInfo')) : {},
            // 表单验证
            rules: {
                password: [
                    { required: true, message: '请输入原密码', trigger: 'blur' },
                    {
                        min: 3,
                        max: 16, message: '请输入 3 到 16 位的密码', trigger: 'blur'
                    }
                ],
                newpassword: [
                    { required: true, message: "请输入新密码", trigger: "blur" },
                    {
                        min: 3,
                        max: 16,
                        message: "请输入 3 到 16 位的密码",
                        trigger: "blur",
                    },
                ],
                checkpassword: [
                    { required: true, trigger: "blur" },
                    {
                        min: 3,
                        max: 16,
                        message: "请输入 3 到 16 位的密码",
                        trigger: "blur",
                    },
                ],
            },
            // 省份地区
            options: regionData,
            labelPosition: "right",
            textList: [
                '个人信息修改',
                '修改密码',
            ],
            count: 1000, // 你的数字
            selectedOption: '',
        }
    },
    created() {
        // 默认选择第一个选项
        if (this.textList.length > 0) {
            this.selectedOption = this.textList[0]; // 设置初始值为第一个选项的名称
        }
        this.getUser().then(res => {
            this.form = res
        })
    },
    methods: {
        prev() {
            // this.$router.back()
            this.$router.go(-1)
            // this.$router.push('/reception/home');
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
        async getUser() {
            return (await this.request.get('/user/nickname/' + this.user.nickname)).data
        },
        // 编辑个人信息
        save() {
            this.request.post("/user/save", this.form).then((res) => {
                if (res.code === '200') {
                    this.$message.success("保存成功");
                    this.$emit('refreshUser')
                    // 更新浏览器存储的用户信息
                    this.getUser().then(res => {
                        res.token = JSON.parse(localStorage.getItem("userInfo")).token
                        localStorage.setItem("userInfo", JSON.stringify(res))
                    })
                } else {
                    this.$message.error("保存失败");
                }
            });
        },
        // 修改密码
        editPassword() {
            this.$refs.pass.validate((valid) => {
                if (valid) {
                    this.request.post("/user/modifyPassword", this.form).then(res => {
                        if (res.code === '200') {
                            this.$message.success("修改成功")
                            this.$store.commit("logout")
                        } else {
                            this.$message.error(res.msg)
                        }
                    })
                }
            })
        },
        back() {
            this.form = JSON.parse(localStorage.getItem('userInfo'))
        },
        // 上传头像
        handleAvatarSuccess(res) {
            if (res.code == '200') {
                this.$message.success('替换成功')
                console.log(res.data);
                this.form.avator = res.data
            } else {
                this.$message.error('替换失败，文件格式不正确')
            }
        },
        addressChange(arr) {
            this.form.location = `${CodeToText[arr[0]]}/${CodeToText[arr[1]]}/${CodeToText[arr[2]]}`
            console.log(this.form.location);
        },
    },
}
</script>

<style lang="less" scoped>
.avatar-uploader {
    text-align: center;
    padding-bottom: 10px;

    .el-upload {
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
    }

    .el-upload:hover {
        border-color: #409EFF;
    }

    .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 138px;
        height: 138px;
        line-height: 138px;
        text-align: center;
    }

    .avatar {
        width: 138px;
        height: 138px;
        display: block;
        margin: 0 100%;

        img {
            display: block;
            width: 100%;
            height: 100%;
            background-size: cover !important;
        }
    }
}



.user-profile {
    // display: flex;
    // align-items: center;
    margin: 20px 0;
    // background-color: #fff;

    .user-profile_center {
        width: 100%;
        // height: 120px;
        margin: 30px 0 10px;
        line-height: 32px;
        // background-color: #fff;
        text-align-last: left;

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
        }

        .user-profile_center_top {
            margin: 10px 0;
            width: 50%;
            position: relative;

            .backrouter {
                position: absolute;
                cursor: pointer;
            }

            span {
                padding: 0 20px;
                font-family: Source Han Sans CN;
                font-weight: bold;
                letter-spacing: 0em;
                color: #3D3D3D;
                cursor: pointer;
            }

            span:nth-of-type(1) {
                margin-left: 50px;
            }

            .custom-underline {
                text-decoration-line: underline;
                /* 下划线 */
                text-decoration-color: red;
                /* 下划线颜色 */
                text-decoration-style: dashed;
                /* 下划线样式，可以是 solid、dashed、dotted 等 */
                text-decoration-thickness: 2px;
                /* 下划线粗细，可以是具体的像素值或者 thin、medium、thick */
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
}
</style>