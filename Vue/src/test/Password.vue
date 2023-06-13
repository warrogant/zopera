<template>
  <el-card style="width: 500px;">
    <el-form label-width="120px" size="small" :model="form" :rules="rules" ref="pass">

      <el-form-item label="原密码" prop="password">
        <!--  v-model="form.password"  -->
        <el-input v-model="form.password" autocomplete="off" show-password></el-input>
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
        <el-input v-model="form.newPassword" autocomplete="off" show-password></el-input>
      </el-form-item>
      <el-form-item label="确认新密码" prop="ChackPassword">
        <el-input v-model="form.ChackPassword" autocomplete="off" show-password></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="save">确 定</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>
export default {
  name: "PassWord",
  data() {
    return {
      form: {}, 
      user: localStorage.getItem("userInfo") ? JSON.parse(localStorage.getItem("userInfo")) : {},
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
      }
    }
  },
  created() {
    this.form.username = this.user.username
  },
  methods: {
    save() {
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
  }
}
</script>

