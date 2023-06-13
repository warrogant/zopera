<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入活动名称" suffix-icon="el-icon-search" v-model="name"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>
    <div style="margin: 10px 0">
      <el-button type="primary" class="ml-5" @click="increase">发布活动<i class="el-icon-top"></i></el-button>
    </div>
    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"
      @selection-change="handleSelectionChange">
      <!-- <el-table-column type="selection" width="55"></el-table-column> -->
      <!-- <el-table-column prop="id" label="ID" width="80" sortable></el-table-column> -->
      <el-table-column prop="sponsor" label="活动发起人"></el-table-column>
      <el-table-column prop="coverUrl" label="活动封面图片">
        <template slot-scope="scope">
          <el-button type="text" @click="dialogPicVisible(scope.row.coverUrl)">预览封面</el-button>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="活动名称"></el-table-column>
      <el-table-column prop="content" label="活动内容">
        <template slot-scope="scope">
          <el-button type="text" @click="dialogContentVisibled(scope.row.content)">查看内容</el-button>
        </template>
      </el-table-column>
      <el-table-column prop="location" label="活动地点">
        <!-- <template slot-scope="scope">
          <el-button type="text" @click="dialogLocationVisible(scope.row.location)">查看地址</el-button>
        </template> -->
      </el-table-column>
      <el-table-column prop="startTime" label="活动开始时间"></el-table-column>
      <el-table-column prop="endTime" label="活动结束时间"></el-table-column>
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
      <el-table-column label="审核" width="200" align="center">
        <template slot-scope="scope">
          <el-button type="primary" @click="Approved(scope.row.id)">通过</el-button>
          <el-button type="danger" slot="reference" @click="reviewFailed(scope.row.id)"><i
              class="el-icon-remove-outline"></i>不通过
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="padding: 10px 0">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="pageNum"
        :page-sizes="[5, 10, 20]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>
    <!-- 发布活动 弹窗 -->
    <el-dialog title="活动信息" :visible.sync="dialogVisible" :before-close="handleClose" width="35%">
      <el-form :label-position="labelPosition" label-width="80px" :model="form" :rules="rules">
        <el-form-item label="活动封面">
          <el-upload ref="upload" action="" list-type="picture-card" :on-preview="handlePictureCardPreview"
            :on-remove="handleRemove" :on-change="UploadImage" :limit="1" :file-list="fileList" :auto-upload="false">
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
          <el-input type="textarea" :rows="2" placeholder="请输入内容" v-model="form.content"></el-input>
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-col :span="5">
            <el-date-picker v-model="form.startTime" type="date" placeholder="选择日期时间" value-format="yyyy-MM-dd">
            </el-date-picker>
          </el-col>
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-col :span="6">
            <el-date-picker v-model="form.endTime" type="date" placeholder="选择日期时间" value-format="yyyy-MM-dd">
            </el-date-picker>
          </el-col>
        </el-form-item>
        <el-form-item label="地点" prop="location">
          <el-cascader size="large" :options="options" v-model="form.selectedOptions" style="width: 100%;"
            @change="addressChange(form.selectedOptions)">
          </el-cascader>
        </el-form-item>
        <el-form-item prop="detail">
          <el-input type="textarea" placeholder="详细地址" v-model="form.detail" clearable maxlength="10" show-word-limit></el-input>
        </el-form-item>
      </el-form>
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </el-dialog>
    <!-- 封面  弹窗  -->
    <el-dialog title="封面预览" :visible.sync="dialogPicVisibled" width="25%" :before-close="handleClose">
      <img :src="form.coverUrl" alt="" style="width: 100%;height: 100%;  margin: 0 auto;">
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogPicVisibled = false">取 消</el-button>
        <el-button type="primary" @click="dialogPicVisibled = false">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 内容  弹窗  -->
    <el-dialog title="简介详情" :visible.sync="dialogContentVisible" width="25%" :before-close="handleClose">
      <span>{{ form.content }}</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogContentVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogContentVisible = false">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 地址  弹窗  -->
    <el-dialog title="地址详情" :visible.sync="dialogLocationVisibled" width="25%" :before-close="handleClose">
      <span>{{ form.location }}</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogLocationVisibled = false">取 消</el-button>
        <el-button type="primary" @click="dialogLocationVisibled = false">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 审核不通过，审核信息弹窗 -->
    <el-dialog title="审核不通过确认" :visible.sync="dialogReviewVisible" width="30%">
      <el-form>
        <el-form-item label="不同意理由">
          <el-input v-model="stateInfo" autocomplete="off" type="textarea" maxlength="100" show-word-limit></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogReviewVisible = false">取 消</el-button>
        <el-button type="primary" @click="reviewFailedSave(stateInfo)">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
  
<script>
import { regionData, CodeToText } from 'element-china-area-data'
export default {
  name: "ActivityReview",
  inject: ['reload'],
  data() {
    return {
      options: regionData,
      tableData: [],
      name: '',
      multipleSelection: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      sponsor: '',
      id: '',
      state: 0,
      stateInfo: '',
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
      value: '',
      fileList: [],
      imageFileList: [],
      videoFileList: [],
      dialogVisible: false,
      dialogContentVisible: false,
      dialogPicVisibled: false,
      dialogReviewVisible: false,
      dialogLocationVisibled: false,
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
      this.state = 0
      this.request.get("/reception/findActivityPage", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          sponsor: this.sponsor,
          state: this.state,
          name: this.name
        }
      }).then(res => {
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
    // 同意审核
    Approved(id) {
      this.state = 1
      this.request.post(`/reception/updateActivityByIdState?id=${id}&state=${this.state}&stateInfo=${this.stateInfo}`).then(res => {
        if (res.data == 1) {
          this.$message.success('操作成功')
          this.load()
        }
      })
    },
    // 审核不通过
    reviewFailed(id) {
      this.id = id
      this.dialogReviewVisible = true
    },
    reviewFailedSave(stateInfo) {
      this.state = 2
      if (stateInfo !== '') {
        this.request.post(`/reception/updateActivityByIdState?id=${this.id}&state=${this.state}&stateInfo=${stateInfo}`).then(res => {
          if (res.data == 1) {
            this.$message.success('操作成功')
            this.stateInfo = ''
            this.load()
          }
        })
        this.load()
        this.dialogReviewVisible = false
      } else {
        this.$message.error('请输入审核不通过理由')
      }
    },
    handleSelectionChange(val) {
      // console.log(val)
      this.multipleSelection = val
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
    // 新增弹窗
    increase() {
      this.dialogVisible = true;
      this.form = {};
      this.form.sponsor = this.user.username
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
      this.dialogPicVisibled = false
      this.dialogLocationVisibled = false
      this.dialogReviewVisible = false
      this.load();
    },
    dialogContentVisibled(content) {
      this.dialogContentVisible = true
      this.form.content = content
      // console.log(this.picContent);
      // console.log(picContent);
    },
    dialogLocationVisible(location) {
      this.dialogLocationVisibled = true
      this.form.location = location
    },
    dialogPicVisible(coverUrl) {
      this.dialogPicVisibled = true
      this.form.coverUrl = coverUrl
      // console.log(this.picContent);
      // console.log(picContent);
    },
    // 新增弹窗保存
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
        this.form.coverUrl = response.data
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
    addressChange(arr) {
      // console.log(arr);
      this.form.location = `${CodeToText[arr[0]]}/${CodeToText[arr[1]]}/${CodeToText[arr[2]]}`
    },
  }
}
</script>
  
<style scoped></style>
  