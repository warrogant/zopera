<template>
  <div class="register">
    <div class="register_l">
      <div class="register_l_text">
        <div class="register_l_text_title">
          <i class="iconfont opera-yinle1"></i>
          <span> ZHUANG OPREA</span>
        </div>
        <div class="register_l_text_wri">
          <p>
            壮戏植根于民族生活土壤之中， <br />是壮族人民创造的历史悠久、
            <br />独具特色的剧种， <br />是东南亚地区的民族文化交流的桥梁。
          </p>
        </div>
      </div>
    </div>
    <div class="register_r">
      <div class="register_r_text">
        <div class="register_r_text_title">
          <i class="iconfont opera-yinle1"></i>
          <span> ZHUANG OPREA</span>
        </div>
        <div class="register_r_text_from">
          <span>注册</span>
          <div class="register-userForm">
            <el-form
              :model="userForm"
              status-icon
              :rules="rules"
              ref="userForm"
              label-width="100px"
            >
              <el-form-item label="" prop="username">
                <el-input
                  type="text"
                  v-model="userForm.username"
                  autocomplete="off"
                  size="medium"
                  placeholder="用户名"
                ></el-input>
              </el-form-item>
              <el-form-item label="" prop="nickname">
                <el-input
                  type="text"
                  v-model="userForm.nickname"
                  autocomplete="off"
                  size="medium"
                  placeholder="昵称"
                ></el-input>
              </el-form-item>
              <el-form-item label="" prop="password">
                <el-input
                  type="password"
                  show-password
                  v-model="userForm.password"
                  autocomplete="off"
                  size="medium"
                  placeholder="密码"
                ></el-input>
              </el-form-item>
              <el-form-item label="" prop="checkpassword">
                <el-input
                  type="password"
                  show-password
                  v-model="userForm.checkpassword"
                  autocomplete="off"
                  placeholder="确认密码"
                ></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="submitForm()"
                  ><b>注册</b>
                </el-button>
              </el-form-item>
              <div class="register_login">
                <svg class="icon" aria-hidden="true">
                  <use xlink:href="#opera-xingzhuanggongnengtubiao-"></use>
                </svg>
                <a @click="$router.push('/login')">已有账号，返回登录</a>
              </div>
            </el-form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "RegisterB",
  data() {
    var validateCheckPassword = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请再次输入密码"));
      } else if (value !== this.userForm.password) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    };
    return {
      // 表单数据
      userForm: {
        username: "",
        nickname: "",
        password: "",
        checkpassword: "",
      },
      // 表单验证
      rules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          { min: 3, max: 8, message: "请输入 3 到 8 个字符", trigger: "blur" },
        ],
        nickname: [
          { required: true, message: "请输入昵称", trigger: "blur" },
          { min: 3, max: 8, message: "请输入 3 到 8 个字符", trigger: "blur" },
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          {
            min: 3,
            max: 16,
            message: "请输入 3 到 16 个字符",
            trigger: "blur",
          },
        ],
        checkpassword: [
          { validator: validateCheckPassword, trigger: "blur" },
          {
            min: 3,
            max: 16,
            message: "请输入 3 到 16 个字符",
            trigger: "blur",
          },
        ],
      },
    };
  },
  methods: {
    // 提交表单
    submitForm() {
      this.$refs["userForm"].validate((valid) => {
        if (valid) {
          //   alert("submit!");
          this.request.post("/user/register", this.userForm).then((res) => {
            if (res.code === "200") {
              this.$message.success("注册成功");
              this.userForm = {}
              this.$router.push('/login')
            } else {
              // 提示自定义异常错误
              this.$message.error(res.msg);
            }
          });
        }
      });
    },
  },
};
</script>

<style lang="less" scoped>
.register {
  position: relative;
  background: linear-gradient(
    180deg,
    rgba(236, 44, 100, 0) 0%,
    rgba(236, 44, 100, 0) 2%,
    #c7492c 100%
  ) !important;
  width: 100vw;
  height: 100vh;

  .register_l {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 65vw;
    height: 100vh;
    background-color: #fff;
    background: url(@/assets/img/VCG211124548306.png) no-repeat;
    background-size: cover;

    .register_l_text {
      // height: 50vh;
      // width: 500px;
      // opacity: 0.8;
      // background-color: #C7492C;
      // border-radius: 15px;
      .register_l_text_title {
        width: 78%;
        height: 40%;
        margin: 15px auto;

        i {
          display: block;
          text-align: center;
          margin-top: -40px;
          margin-bottom: -20px;
        }

        .opera-yinle1 {
          font-size: 128px;
          color: #90ff00;
        }

        span {
          display: block;
          text-align: center;
          color: #90ff00;
          line-height: 20px;
          font-size: 36px;
        }
      }

      .register_l_text_wri {
        width: 100%;
        height: 60%;
        text-align: center;
        font-family: KaiTi;
        font-size: 36px;
        font-weight: normal;
        margin: 0 auto;
        color: #fff;
      }
    }
  }

  .register_r {
    position: absolute;
    top: 0;
    right: 0;
    width: 35vw;
    height: 100vh;

    .register_r_text {
      height: 70vh;
      width: 25vw;
      margin: 8vh auto;
      border-radius: 15px;
      // background-color: #6e6060;

      .register_r_text_title {
        height: 20vh;

        i {
          display: block;
          text-align: center;
          margin-top: -40px;
          margin-bottom: -20px;
        }

        .opera-yinle1 {
          font-size: 100px;
          color: #000000;
        }

        span {
          display: block;
          text-align: center;
          color: #000000;
          line-height: 20px;
          font-size: 24px;
        }
      }

      .register_r_text_from {
        display: flex;
        // justify-content: center;
        align-items: center;
        flex-direction: column;
        height: 50vh;
        width: 23vw;
        margin: 5vh auto;
        border-radius: 15px;
        // opacity: 0.5;
        background-color: #817777;

        span {
          display: block;
          // margin: 10px 0;
          font-family: KaiTi;
          font-size: 48px;
          font-weight: normal;
          text-align: center;
          text-transform: uppercase;
          letter-spacing: 0em;
          color: #90ff00;
          // background-color: #fff;
        }

        // 注册表单
        .register-userForm {
          position: relative;
          top: 0px;
          left: 0;
          width: 350px;
          // background-color: #e34e4e;
          text-align: center;

          .el-input {
            position: relative;
            top: 0;
            left: -50px;
            margin: 5px;
            width: 250px;
          }

          // 注册按钮
          .el-button {
            position: relative;
            top: 0;
            left: -50px;
            margin: 5px;
            width: 250px;
            background-color: #90ff00;
            border: 0px solid #fff;

            b {
              font-weight: lighter;
              font-size: 20px;
              color: #000000;

              letter-spacing: 6px;
            }
          }

          a {
            display: block;
            // width: 120px;
            margin: 15px 0;
            color: #fff;
            letter-spacing: 2px;
          }

          // 注册账号
          .register_login {
            height: 32px;
            width: 245px;
            margin: 0 auto;
            // float: left;
            // text-align: center;
            // background-color: #cd2323;

            svg {
              display: block;
              float: left;
              height: 32px;
              width: 32px;
              text-align: center;
              padding: 5px 0;
              // background-color: #fff;
            }

            a {
              text-align: left;
              padding: 5px;
              color: #ffffff;
              cursor: pointer;
              // font-weight: lighter;
            }
          }
        }
      }
    }
  }
}
</style>
