<template>
  <div>
    <!-- 搜索框 -->
    <div style="margin: 10px 0">
      <el-input
        placeholder="请输入昵称"
        suffix-icon="el-icon-s-custom"
        v-model="username"
        style="width: 200px; margin-right: 8px"
      >
      </el-input>
      <el-input
        size="medium"
        placeholder="请输入邮箱"
        suffix-icon="el-icon-s-promotion"
        v-model="email"
        style="width: 200px; margin-right: 8px"
      >
      </el-input>
      <el-input
        size="small"
        placeholder="请输入电话"
        suffix-icon="el-icon-mobile-phone"
        v-model="phone"
        style="width: 200px; margin-right: 8px"
      >
      </el-input
      ><el-button type="primary" icon="el-icon-search" @click="load"
        >搜索</el-button
      >
      <el-button type="warning" icon="el-icon-refresh" @click="reset"
        >重置</el-button
      >
    </div>
    <div style="margin: 10px 0; display: flex">
      <!-- 新增 -->
      <el-button
        type="primary"
        icon="el-icon-circle-plus-outline"
        @click="increase"
        >新增</el-button
      >
      <!-- 删除确认弹窗 -->
      <el-popconfirm
        confirm-button-text="好的"
        cancel-button-text="返回"
        icon="el-icon-info"
        icon-color="red"
        style="margin: 0 10px"
        title="这是一段内容确定要'批量删除'吗？"
        @confirm="delbatch"
      >
        <el-button type="danger" slot="reference" icon="el-icon-delete"
          >批量删除</el-button
        >
      </el-popconfirm>
      <!-- 导入 -->
      <el-upload action="http://localhost:9090/user/import"
          :show-file-list="false"
          accept="xlsx"
          :on-success="handerImportSuccess">
        <el-button
          type="primary"
          icon="el-icon-sold-out"
          >导入</el-button
        >
      </el-upload>
      <!-- 导出 -->
      <el-button
        type="primary"
        icon="el-icon-sell"
        @click="exp"
        style="margin-left: 10px"
        >导出</el-button
      >
    </div>
    <!-- 内容 -->
    <el-table
      :data="tableData"
      :total="total"
      :header-cell-class-name="headerBg"
      @selection-change="handleSelectionChange"
      border
    >
      <!-- 多选 -->
      <el-table-column type="selection" width="40"> </el-table-column>
      <el-table-column prop="nickname" label="昵称" width="100" class="itme">
      </el-table-column>
      <el-table-column prop="username" label="用户名" width="100" class="itme">
      </el-table-column>
      <el-table-column prop="role" label="角色" width="100" class="itme">
      </el-table-column>
      <el-table-column prop="password" label="密码" width="120">
      </el-table-column>
      <el-table-column prop="phone" label="电话"> </el-table-column>
      <el-table-column prop="email" label="邮箱"> </el-table-column>
      <el-table-column prop="birth" label="出生日期"> </el-table-column>
      <el-table-column prop="location" label="地址"> </el-table-column>
      <el-table-column label="操作">
        <template v-slot="scope">
          <el-button size="mini" type="info" @click="handleEdit(scope.row)"
            ><i class="el-icon-edit-outline"></i> 编辑</el-button
          >
          <el-popconfirm
            confirm-button-text="好的"
            cancel-button-text="返回"
            icon="el-icon-info"
            icon-color="red"
            style="margin-left: 3px"
            title="这是一段内容确定删除吗？"
            @confirm="handleDelete(scope.row.id)"
          >
            <el-button size="mini" type="danger" slot="reference"
              ><i class="el-icon-delete"></i> 删除</el-button
            >
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <el-pagination
      background
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="pageNum"
      :page-sizes="[5, 10, 20]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      style="margin: 15px 0"
    >
    </el-pagination>
    <!-- 弹窗 -->
    <el-dialog
      title="新增戏迷信息"
      :visible.sync="dialogVisible"
      :before-close="handleClose"
      width="30%"
    >
      <el-form
        :label-position="labelPosition"
        label-width="80px"
        :model="form"
        :rules="rules"
      >
        <el-form-item label="昵称：" prop="nickname">
          <el-input v-model="form.nickname"></el-input>
        </el-form-item>
        <el-form-item label="用户名：" prop="username">
          <el-input v-model="form.username"></el-input>
        </el-form-item>
        <el-form-item label="角色：" prop="role">
          <el-select v-model="form.role" clearable placeholder="请选择" style="100vw">
            <el-option v-for="item in roles" :key="item.name" :label="item.name" :value="item.flag" width="100">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="密码：" prop="password" autocomplete="off">
          <el-input v-model="form.password"></el-input>
        </el-form-item>
        <el-form-item label="邮箱：">
          <el-input v-model="form.email"></el-input>
        </el-form-item>
        <el-form-item label="电话：">
          <el-input v-model="form.phone"></el-input>
        </el-form-item>
        <el-form-item label="出生日期">
          <el-col :span="11">
            <el-date-picker
              type="date"
              placeholder="选择日期"
              v-model="form.birth"
              style="width: 100%"
            ></el-date-picker>
          </el-col>
        </el-form-item>
        <el-form-item label="选择省份">
          <el-select v-model="form.location" placeholder="请选择省份">
            <el-option
              v-for="option in options"
              :key="option"
              :label="option"
              :value="option"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <el-button @click="closePopup">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "UserBackstage",
  props: [""],
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 5,
      // 搜索框内容
      username: "",
      email: "",
      phone: "",
      // 新增弹窗
      dialogVisible: false,
      labelPosition: "right",
      headerBg: "headerBg",
      form: {
        nickname:'',
        username: "",
        password: "",
        email: "",
        date: "",
        location: "",
      },
      // 省份地区
      options: [
        "北京市",
        "天津市",
        "河北省",
        "山西省",
        "内蒙古自治区",
        "辽宁省",
        "吉林省",
        "黑龙江省",
        "上海市",
        "江苏省",
        "浙江省",
        "安徽省",
        "福建省",
        "江西省",
        "山东省",
        "河南省",
        "湖北省",
        "湖南省",
        "广东省",
        "广西壮族自治区",
        "海南省",
        "重庆市",
        "四川省",
        "贵州省",
        "云南省",
        "西藏自治区",
        "陕西省",
        "甘肃省",
        "青海省",
        "宁夏回族自治区",
        "新疆维吾尔自治区",
        "台湾省",
        "香港特别行政区",
        "澳门特别行政区",
      ],
      // 表单校验
      rules: {
        nickname: [
          { required: true, message: "请输入昵称", trigger: "blur" },
          { min: 3, max: 5, message: "请输入3 到 5 个字符", trigger: "blur" },
        ],
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          { min: 3, max: 5, message: "请输入 3 到 5 个字符", trigger: "blur" },
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { min: 3, max: 8, message: "请输入 3 到 8个字符", trigger: "blur" },
        ],
      },
      // 表单多选
      multipleSelection: [],
      // 角色
      roles:[]
    };
  },
  created() {
    this.load();
  },
  methods: {
    // 分页接口调用,请求查询分页数据
    load() {
      this.request
        .get("/user/page", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            username: this.username,
            email: this.email,
            phone: this.phone,
          },
        })
        .then((res) => {
          // console.log(res.data.total);
          // console.log(res);
          this.tableData = res.data.records;
          this.total = res.data.total;
        });
        
        // 角色请求
      this.request
        .get("/role").then(res => {
          this.roles = res.data
        })
    },
    // 重置
    reset() {
      this.username = "";
      this.email = "";
      this.phone = "";
      this.load();
    },
    // 分页 每页显示条数和页码
    handleSizeChange(pageSize) {
      // console.log(`每页 ${pageSize} 条`);
      this.pageSize = pageSize;
      this.load();
    },
    handleCurrentChange(pageNum) {
      // console.log(`当前页: ${pageNum}`);
      this.pageNum = pageNum;
      this.load();
    },
    // 新增弹窗
    increase() {
      this.dialogVisible = true;
      this.form = {};
    },
    // 弹窗取消
    closePopup() {
      this.dialogVisible = false;
      this.load();
    },
    // 弹窗关闭
    handleClose() {
      this.closePopup();
    },
    // 新增弹窗保存
    save() {
      this.request.post("/user/save", this.form).then((res) => {
        if (res.data) {
          console.log(res.data);
          this.$message.success("保存成功");
          this.dialogVisible = false;
          this.load();
        } else {
          this.$message.error("保存失败");
        }
      });
    },
    // 表单编辑
    handleEdit(row) {
      this.form = row;
      this.dialogVisible = true;
    },
    // 表单(id)删除
    handleDelete(id) {
      this.request.delete("/user/del/" + id).then((res) => {
        if (res.data) {
          // console.log(res);
          this.$message.success("删除成功");
          this.load();
        } else {
          this.$message.error("删除失败");
        }
      });
    },
    // 表单批量选中
    handleSelectionChange(val) {
      // console.log(val);
      this.multipleSelection = val;
    },
    // 批量删除
    delbatch() {
      let ids = this.multipleSelection.map((v) => v.id);
      this.request.delete("/user/del/batch", { data: ids }).then((res) => {
        if (res.data) {
          // console.log(res);
          this.$message.success("批量删除成功");
          this.load();
        } else {
          this.$message.error("批量删除失败");
        }
      });
    },
    // 导出
    exp() {
      window.open("http://localhost:9090/user/export");
    },
    //导入
    handerImportSuccess() {
      this.$message.success("上传成功");
      this.load();
    },
  },
};
</script>

<style>
.el-table {
  margin-top: 10px;
  color: black !important;
}

.cell {
  color: black;
}

.headerBg {
  background-color: #f5c78afa !important;
}

.el-pagination__editor .el-input {
  width: 50px !important;
}
</style>
