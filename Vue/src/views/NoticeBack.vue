<template>
  <div>
    <!-- 搜索框 -->
    <div style="margin: 10px 0">
      <el-input placeholder="请输入公告标题" suffix-icon="el-icon-edit" v-model="picTitle"
        style="width: 200px; margin-right: 8px"></el-input>
      <el-button type="primary" icon="el-icon-search" @click="load">搜索</el-button>
      <el-button type="warning" icon="el-icon-refresh" @click="reset">重置</el-button>
    </div>
    <div style="margin: 10px 0; display: flex">
      <!-- 新增 -->
      <el-button type="primary" icon="el-icon-circle-plus-outline" @click="increase">新增</el-button>
      <!-- 删除确认弹窗 -->
      <el-popconfirm confirm-button-text="好的" cancel-button-text="返回" icon="el-icon-info" icon-color="red"
        style="margin: 0 10px" title="这是一段内容确定要'批量删除'吗？" @confirm="delbatch">
        <el-button type="danger" slot="reference" icon="el-icon-delete">批量删除</el-button>
      </el-popconfirm>
    </div>
    <!-- 内容 -->
    <el-table :data="tableData" :total="total" :header-cell-class-name="headerBg"
      @selection-change="handleSelectionChange" border :default-sort="{ prop: 'date', order: 'descending' }">
      <!-- 多选 -->
      <el-table-column type="selection" width="40"> </el-table-column>
      <el-table-column prop="id" label="ID" width="100" class="itme" sortable>
      </el-table-column>
      <el-table-column prop="picUrl" label="公告图片地址" width="250" class="itme">
      </el-table-column>
      <el-table-column prop="picTitle" label="公告标题" width="200" class="itme">
      </el-table-column>
      <el-table-column prop="picContent" label="公告内容" width="120" class="itme">
        <template slot-scope="scope">
          <el-button type="text" @click="dialogContentVisibled(scope.row.picContent)">查看内容</el-button>
        </template>
      </el-table-column>
      <el-table-column prop="publisher" label="发布人" width="100" class="itme">
      </el-table-column>
      <el-table-column prop="createTime" label="发布时间" width="250" class="itme">
        <template slot-scope="scope">
          {{ scope.row.createTime }}
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template v-slot="scope">
          <el-button size="mini" type="info" @click="handleEdit(scope.row)"><i class="el-icon-edit-outline"></i>
            编辑</el-button>
          <el-popconfirm confirm-button-text="好的" cancel-button-text="返回" icon="el-icon-info" icon-color="red"
            style="margin-left: 3px" title="这是一段内容确定删除吗？" @confirm="handleDelete(scope.row.id)">
            <el-button size="mini" type="danger" slot="reference"><i class="el-icon-delete"></i> 删除</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange"
      :current-page="pageNum" :page-sizes="[5, 10, 20]" :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper" :total="total" style="margin: 15px 0">
    </el-pagination>
    <!-- 新增 弹窗 -->
    <el-dialog title="公告信息" :visible.sync="dialogVisible" :before-close="handleClose" width="30%">
      <el-form :label-position="labelPosition" label-width="80px" :model="form">
        <el-form-item label="公告图片">
          <!-- <em :style="{ background: 'url(' + form.picUrl + ') center/cover no-repeat' }" :data-background="form.picUrl"></em> -->
          <el-upload ref="upload" action="" list-type="picture-card" :on-preview="handlePictureCardPreview"
            :on-remove="handleRemove" :on-change="UploadImage" :limit="1" :file-list="fileList" :auto-upload="false"
            :on-success="handleAvatarSuccess">
            <img v-if="showPicUrl" :src="form.picUrl">
            <i class="el-icon-plus" v-if="!showPicUrl"></i>
            <template #tip>
              <div style="font-size: 12px;color: #919191;">
                单次限制上传一张图片
              </div>
            </template>
          </el-upload>
        </el-form-item>
        <el-form-item label="发布人：" prop="publisher">
          <el-input v-model="form.publisher" disabled></el-input>
          <!-- <el-input v-model="user.username"></el-input> -->
        </el-form-item>
        <el-form-item label="标题：" prop="picTitle">
          <el-input v-model="form.picTitle" maxlength="50" ></el-input>
        </el-form-item>
        <el-form-item label="内容：" prop="picContent">
          <el-input v-model="form.picContent" type="textarea" maxlength="2000" show-word-limit></el-input>
        </el-form-item>
      </el-form>
      <el-button @click="closePopup">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </el-dialog>
    <!-- 内容  弹窗  -->
    <el-dialog title="公告内容" :visible.sync="dialogContentVisible" width="30%" :before-close="handleClose">
      <span>{{ picContent }}</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogContentVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogContentVisible = false">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "NoticeBack",
  props: [""],
  data() {
    return {
      tableData: [],
      fileList: [],
      picContent: '',
      total: 0,
      pageNum: 1,
      pageSize: 5,
      // 搜索框内容
      picTitle: "",
      // 新增弹窗
      dialogVisible: false,
      dialogContentVisible: false,
      labelPosition: "right",
      headerBg: "headerBg",
      // 菜单默认展开设置
      expends: [],
      checks: [],
      form: {
        picUrl: '',
        publisher: '',
        picTitle: '',
        picContent: ''
      },
      user: localStorage.getItem('userInfo') ? JSON.parse(localStorage.getItem('userInfo')) : {},
      // 表单多选
      multipleSelection: [],
      roleId: 0,
      roleFlag: '',
      showPicUrl: true
    };
  }, inject: ['reload'],
  created() {
    this.load();
    // console.log(this.user.username);
    // console.log( this.form.publisher);
  },
  methods: {
    // 分页接口调用,请求查询分页数据
    load() {
      this.request
        .get("/notice/page", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            picTitle: this.picTitle
          },
        })
        .then((res) => {
          // console.log(res.data.records[0].picContent);
          this.tableData = res.data.records;
          this.total = res.data.total;
        });
    },
    // 重置
    reset() {
      this.picTitle = "";
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
      this.form.publisher = this.user.username
    },
    // 弹窗取消
    closePopup() {
      this.dialogVisible = false;
      this.dialogContentVisible = false
      this.load();
    },
    dialogContentVisibled(picContent) {
      this.dialogContentVisible = true
      this.picContent = picContent
      // console.log(this.picContent);
      // console.log(picContent);
    },
    // 弹窗关闭
    handleClose() {
      this.closePopup();
    },
    // 新增弹窗保存
    save() {
      this.request.post("/notice/save", this.form).then((res) => {
        // console.log(res.data);
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
      this.request.delete("/notice/del/" + id).then((res) => {
        if (res.data) {
          // console.log(res);
          this.$message.success("删除成功");
          this.load();
        } else {
          this.$message.error("删除失败");
        }
      });
      this.reload()
      this.load();
    },
    // 表单批量选中
    handleSelectionChange(val) {
      // console.log(val);
      this.multipleSelection = val;
    },
    // 批量删除
    delbatch() {
      let ids = this.multipleSelection.map((v) => v.id);
      this.request.delete("/notice/del/batch", { data: ids }).then((res) => {
        if (res.data) {
          // console.log(res);
          this.$message.success("批量删除成功");
          this.load();
        } else {
          this.$message.error("批量删除失败");
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
        this.form.picUrl = response.data
      })
      this.showPicUrl = false
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
    // 上传头像
    handleAvatarSuccess(res) {
      // console.log(res.data);
      // console.log(res.msg);
      if (res.code == '200') {
        this.$message.success('替换成功')
        // console.log(res.data);
        this.form.picUrl = res.data
      } else {
        this.$message.error('替换失败，文件格式不正确')
      }
    },
  },
};
</script>

<style lang="less" scoped>
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

// .el-form-item {
//   em {
//     display: block;
//     width: 100px;
//     height: 100px;
//     background: attr(data-background) center/cover no-repeat;
//     background-size: 100% 100%;
//   }
// }

.el-upload {
  width: 138px;
  height: 138px;
  display: block;

  img {
    display: block;
    width: 100%;
    height: 100%;
    background-size: cover !important;
  }
}

.icon {
  width: 16px;
  height: 16px;
  vertical-align: -0.15em;
  fill: currentColor;
  overflow: hidden;
}
</style>
