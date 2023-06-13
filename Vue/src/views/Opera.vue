<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>
    <div style="margin: 10px 0">
      <!-- <el-upload action="http://localhost:9090/file/upload" :show-file-list="false" :on-success="handleFileUploadSuccess"
        style="display: inline-block">
        <el-button type="primary" class="ml-5">上传文件 <i class="el-icon-top"></i></el-button>
      </el-upload> -->
      <el-button type="primary" class="ml-5" @click="increase">上传戏曲<i class="el-icon-top"></i></el-button>
      <el-popconfirm class="ml-5" confirm-button-text='确定' cancel-button-text='我再想想' icon="el-icon-info" icon-color="red"
        title="您确定批量删除这些数据吗？" @confirm="delBatch">
        <el-button type="danger" slot="reference">批量删除 <i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>

    </div>
    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"
      @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
      <el-table-column prop="uploader" label="上传者"></el-table-column>
      <el-table-column prop="name" label="戏曲名称"></el-table-column>
      <el-table-column prop="url" label="上传路径"></el-table-column>
      <el-table-column prop="pic" label="戏曲封面">
        <template slot-scope="scope">
          <el-button type="text" @click="dialogPicVisible(scope.row.pic)">预览封面</el-button>
        </template>
      </el-table-column>
      <el-table-column prop="operaType" label="戏曲类别"></el-table-column>
      <el-table-column prop="introduce" label="简介">
        <template slot-scope="scope">
          <el-button type="text" @click="dialogContentVisibled(scope.row.introduce)">查看内容</el-button>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
          <el-button type="primary" @click="download(scope.row.url)">下载</el-button>
          <el-popconfirm class="ml-5" confirm-button-text='确定' cancel-button-text='我再想想' icon="el-icon-info"
            icon-color="red" title="您确定删除吗？" @confirm="del(scope.row.id)">
            <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
      <el-table-column prop="state" label="活动状态">
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
    <div style="padding: 10px 0">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="pageNum"
        :page-sizes="[5, 10, 20]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>
    <!-- 上传 弹窗 -->
    <el-dialog title="戏曲信息" :visible.sync="dialogVisible" :before-close="handleClose" width="35%">
      <el-form :label-position="labelPosition" label-width="80px" :model="form">
        <el-form-item label="戏曲封面">
          <el-upload ref="upload" action="" list-type="picture-card" :on-preview="handlePictureCardPreview"
            :on-remove="handleRemove" :on-change="UploadImage" :limit="1" :file-list="imageFileList" :auto-upload="false"
            :on-success="handleAvatarSuccess" :on-exceed="handleExceed">
            <!-- <img v-if="showPicUrl" :src="form.picUrl"> -->
            <i class="el-icon-plus"></i>
            <template #tip>
              <div style="font-size: 12px;color: #919191;">
                单次限制上传一张图片,仅上传jpg/png/jpeg文件
              </div>
            </template>
          </el-upload>
        </el-form-item>
        <el-form-item label="上传者：" prop="uploader">
          <el-input v-model="form.uploader" disabled></el-input>
        </el-form-item>
        <el-form-item>
          <el-upload action="http://localhost:9090/file/upload" :show-file-list="true" :limit="1"
            :file-list="videoFileList" :on-success="handleFileUploadSuccess" style="display: inline-block">
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
    <!-- 封面  弹窗  -->
    <el-dialog title="封面预览" :visible.sync="dialogPicVisibled" width="25%" :before-close="handleClose">
      <img :src="form.pic" alt="" style="width: 100%;height: 100%;  margin: 0 auto;">
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogPicVisibled = false">取 消</el-button>
        <el-button type="primary" @click="dialogPicVisibled = false">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 内容  弹窗  -->
    <el-dialog title="简介详情" :visible.sync="dialogContentVisible" width="25%" :before-close="handleClose">
      <span>{{ form.introduce }}</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogContentVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogContentVisible = false">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
  
<script>
export default {
  name: "OperaZ",
  inject: ['reload'],
  data() {
    return {
      tableData: [],
      name: '',
      multipleSelection: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      state: 3,
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
      dialogContentVisible: false,
      dialogPicVisibled: false,
      labelPosition: 'right',
      showPicUrl: true,
      user: localStorage.getItem('userInfo') ? JSON.parse(localStorage.getItem('userInfo')) : {},
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.request.get("/file/findOperaReviewPage", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          state: this.state,
          name: this.name,
        }
      }).then(res => {
        console.log(res.data.records);
        this.tableData = res.data.records
        this.total = res.data.total
      })


    },
    changeEnable(row) {
      this.request.post("/file/update", row).then(res => {
        if (res.code === '200') {
          this.$message.success("操作成功")
        }
      })
    },
    del(id) {
      this.request.delete("/file/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
    },
    handleSelectionChange(val) {
      // console.log(val)
      this.multipleSelection = val
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      this.request.post("/file/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    reset() {
      this.name = ""
      this.load()
    },
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
        this.form.url = res.data
        this.$message.success("上传成功")
      } else {
        this.$message.error("上传失败，" + res.msg)
      }
      this.load()
    },
    // 下载
    download(url) {
      // console.log(url);
      window.open(url)
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
    // 图片 上传
    UploadImage(file) {
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
    // 新增弹窗保存
    save() {
      this.request.post("/file/operaSave", this.form).then((res) => {
        // console.log(res.data);
        if (res.data) {
          console.log('弹窗数据' + res.data);
          this.$message.success("保存成功");
          this.dialogVisible = false;
          this.load();
        } else {
          this.$message.error("保存失败");
        }
      });
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
  
<style scoped></style>
  