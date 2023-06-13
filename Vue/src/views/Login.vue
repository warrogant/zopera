<template>
  <div class="login">
    <div class="login_l">
      <div class="login_l_text">
        <div class="login_l_text_title">
          <i class="iconfont opera-yinle1"></i>
          <span> ZHUANG OPREA</span>
        </div>
        <div class="login_l_text_wri">
          <p>
            壮戏植根于民族生活土壤之中，<br />
            是壮族人民创造的历史悠久、<br />独具特色的剧种，<br />
            是东南亚地区的民族文化交流的桥梁。
          </p>
        </div>
      </div>
    </div>
    <div class="login_r">
      <div class="login_r_text">
        <div class="login_r_text_title">
          <i class="iconfont opera-yinle1"></i>
          <span> ZHUANG OPREA</span>
        </div>
        <div class="login_r_text_from">
          <span>登录</span>
          <div class="login-userForm">
            <el-form :model="userForm" status-icon :rules="rules" ref="userForm" label-width="100px">
              <el-form-item label="" prop="username">
                <el-input type="text" prefix-icon="el-icon-user" v-model="userForm.username" autocomplete="off"
                  size="medium" placeholder="用户名或邮箱"></el-input>
              </el-form-item>
              <el-form-item label="" prop="password">
                <el-input type="password" prefix-icon="el-icon-key" v-model="userForm.password" autocomplete="off"
                  size="medium" show-password placeholder="密码"></el-input>
              </el-form-item>
              <!-- <a href="#">忘记密码？</a> -->
              <el-form-item>
                <!-- <input type="text" @keydown.enter="onenter"> -->
                <el-button type="primary" @click="submitForm()"><b>登录</b>
                </el-button>
              </el-form-item>
              <div class="login_resiger">
                <svg class="icon" aria-hidden="true">
                  <use xlink:href="#opera-zhuce"></use>
                </svg>
                <a @click="$router.push('/register')">注册账号</a>
              </div>
            </el-form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { setRouter } from '@/router';
export default {
  name: "LoginZ",
  data() {
    var validateUserName = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入用户名"));
      } else if (value.length < 3 || value.length > 16) {
        callback(new Error("用户名应为3到16个字符"));
      } else {
        callback();
      }
    };
    var validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入密码"));
      } else if (value.length < 3 || value.length > 20) {
        callback(new Error("密码应为3到20个字符"));
      } else {
        callback();
      }
    };
    return {
      userForm: {
        password: "",
        username: "",
      },
      rules: {
        username: [{ validator: validateUserName, trigger: "blur" }],
        password: [{ validator: validatePass, trigger: "blur" }],
      },
    };
  },
  created() {
    // 监听全局回车
    // window.addEventListener('keydown', this.submitForm());     
    window.addEventListener('keydown', this.handleEnter);
  },
  destroyed() {
    // 销毁监听回车事件，避免内存泄漏
    window.removeEventListener('keydown', this.handleEnter);
  },
  methods: {
    handleEnter(e) {
      // 执行确认操作
      if (e.code === 'Enter') {
        this.submitForm()
      }
    },
    // 登录请求
    submitForm() {
      this.$refs.userForm.validate((valid) => {
        if (valid) {
          this.request.post('/user/login', this.userForm).then(res => {
            if (res.code === '200') {
              //将用户信息存储到浏览器本地存储中
              localStorage.setItem('userInfo', JSON.stringify(res.data))
              localStorage.setItem('menus', JSON.stringify(res.data.menus))

              // 动态设置当前用户的路由
              setRouter()
              this.$router.push('/')
              this.$message.success('登录成功')

              // 登录用户角色
              if (res.data.role === 'ROLE_USER') {
                this.$router.push('/reception/home')
              } else {
                this.$router.push('/home')
              }
            } else {
              // 提示自定义异常错误
              this.$message.error(res.msg)
            }
          })
        }
      });
    },
  },
};
</script>

<style lang="less" scoped>
.login {
  position: relative;
  background: linear-gradient(180deg,
      rgba(236, 44, 100, 0) 0%,
      rgba(236, 44, 100, 0) 2%,
      #c7492c 100%) !important;
  width: 100vw;
  height: 100vh;

  .login_l {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 65vw;
    height: 100vh;
    background-color: #fff;
    background: url(@/assets/img/VCG211121928942.png) no-repeat;
    background-size: cover;

    .login_l_text {

      // height: 50vh;
      // width: 500px;
      // opacity: 0.8;
      // background-color: #C7492C;
      // border-radius: 15px;
      .login_l_text_title {
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

      .login_l_text_wri {
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

  .login_r {
    position: absolute;
    top: 0;
    right: 0;
    width: 35vw;
    height: 100vh;

    .login_r_text {
      height: 70vh;
      width: 25vw;
      margin: 5vh auto;
      border-radius: 15px;
      // background-color: #6e6060;

      .login_r_text_title {
        height: 20vh;

        i {
          display: block;
          text-align: center;
          //   margin-top: -40px;
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

      .login_r_text_from {
        display: flex;
        // justify-content: center;
        align-items: center;
        flex-direction: column;
        height: 45vh;
        width: 25vw;
        margin: 5vh auto;
        border-radius: 15px;
        // opacity: 0.5;
        background-color: #7e6f6f;

        span {
          display: block;
          margin: 10px 0;
          font-family: KaiTi;
          font-size: 48px;
          font-weight: normal;
          text-align: center;
          text-transform: uppercase;
          letter-spacing: 0em;
          color: #90ff00;
          // background-color: #fff;
        }

        // 登录表单
        .login-userForm {
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

          // 登录按钮
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
          .login_resiger {
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
