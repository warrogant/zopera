<template>
  <div>
    <!-- 搜索框 -->
    <div style="margin: 10px 0">
      <el-input placeholder="请输入昵称" suffix-icon="el-icon-s-custom" v-model="name"
        style="width: 200px; margin-right: 8px"></el-input>
      <el-button type="primary" icon="el-icon-search" @click="load">搜索</el-button>
      <el-button type="warning" icon="el-icon-refresh" @click="reset">重置</el-button>
    </div>
    <div style="margin: 10px 0; display: flex">
      <!-- 新增 -->
      <el-button type="primary" icon="el-icon-circle-plus-outline" @click="increase()">新增</el-button>
      <!-- 删除确认弹窗 -->
      <el-popconfirm confirm-button-text="好的" cancel-button-text="返回" icon="el-icon-info" icon-color="red"
        style="margin: 0 10px" title="这是一段内容确定要'批量删除'吗？" @confirm="delbatch">
        <el-button type="danger" slot="reference" icon="el-icon-delete">批量删除</el-button>
      </el-popconfirm>
    </div>
    <!-- 内容 -->
    <el-table :data="tableData" :total="total" :header-cell-class-name="headerBg" row-key="id" border default-expand-all
      @selection-change="handleSelectionChange" :default-sort="{ prop: 'date', order: 'descending' }">
      <!-- 多选 -->
      <el-table-column type="selection" width="40"> </el-table-column>
      <!-- <el-table-column prop="id" label="ID" width="100" class="itme" sortable>
      </el-table-column> -->
      <el-table-column prop="name" label="名称" width="150" class="itme">
      </el-table-column>
      <el-table-column prop="path" label="路径" width="130" class="itme">
      </el-table-column>
      <el-table-column prop="pagePath" label="页面路径" width="130" class="itme">
      </el-table-column>
      <el-table-column prop="icon" label="图标" width="100" class="itme">
        <template slot-scope="scope">
          <svg class="icon" aria-hidden="true">
                <use v-bind:xlink:href="scope.row.icon"></use>
              </svg>
        </template>
      </el-table-column>
      <el-table-column prop="description" label="描述" width="150" class="itme">
      </el-table-column>
      <el-table-column label="操作">
        <template v-slot="scope">
          <el-button size="mini" type="primary" @click="handaddChildren(scope.row.id)"
            v-if="!scope.row.pid && !scope.row.path"><i class="el-icon-circle-plus-outline"></i>
            新增子菜单</el-button>
          <el-button size="mini" type="info" @click="handleEdit(scope.row)"><i class="el-icon-edit-outline"></i>
            编辑</el-button>
          <el-popconfirm confirm-button-text="好的" cancel-button-text="返回" icon="el-icon-info" icon-color="red"
            style="margin-left: 3px" title="这是一段内容确定删除吗？" @confirm="handleDelete(scope.row.id)">
            <el-button size="mini" type="danger" slot="reference"><i class="el-icon-delete"></i> 删除</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <!-- 弹窗 -->
    <el-dialog title="新增菜单" :visible.sync="dialogVisible" :before-close="handleClose" width="30vw">
      <el-form :label-position="labelPosition" label-width="80px" :model="form">
        <el-form-item label="名称：" prop="name">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="路径：" prop="description">
          <el-input v-model="form.path"></el-input>
        </el-form-item>
        <el-form-item label="页面路径：" prop="pagePath" > 
          <el-input v-model="form.pagePath"></el-input>
        </el-form-item>
        <el-form-item label="图标：" prop="icon" style="100vw">
          <el-select v-model="form.icon" clearable placeholder="请选择">
            <el-option v-for="item in symbolOptions" :key="item.name" :label="item.name" :value="item.value" width="100">
              <svg class="icon" aria-hidden="true">
                <use v-bind:xlink:href="item.value"></use>
              </svg>&nbsp;&nbsp;&nbsp;<span>{{ item.name }}</span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="描述：" prop="description">
          <el-input v-model="form.description"></el-input>
        </el-form-item>
      </el-form>
      <el-button @click="closePopup">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "menuBackstage",
  props: [""],
  data() {
    return {
      tableData: [],
      total: 0,
      // 搜索框内容
      name: "",
      // 新增弹窗
      dialogVisible: false,
      labelPosition: "right",
      headerBg: "headerBg",
      form: {
        name: '',
        path: '',
        icon: '',
        description: ""
      },
      // 表单多选
      multipleSelection: [],
      // 图标
      symbolOptions: []
    };
  },
  created() {
    this.load();
  },
  methods: {
    // 分页接口调用,请求查询分页数据
    load() {
      this.request
        .get("/menu", {
          params: {
            name: this.name
          },
        })
        .then((res) => {
          // console.log(res.data.total);
          // console.log(res);
          this.tableData = res.data;
          this.total = res.data.total;
        });
    },
    // 重置
    reset() {
      this.name = "";
      this.load();
    },
    // 新增子菜单
    handaddChildren(pid) {
      this.dialogVisible = true;
      this.form = {}
      // if(pid){
      this.form.pid = pid
      // }
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
      this.request.post("/menu/save", this.form).then((res) => {
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
      this.form = JSON.parse(JSON.stringify(row));
      this.dialogVisible = true;
      // 请求图标数据
      this.request
        .get("/menu/symbol").then((res) => {
          console.log(res.data);
          this.symbolOptions = res.data;
        });
    },
    // 表单(id)删除
    handleDelete(id) {
      this.request.delete("/menu/del/" + id).then((res) => {
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
      this.request.delete("/menu/del/batch", { data: ids }).then((res) => {
        if (res.data) {
          // console.log(res);
          this.$message.success("批量删除成功");
          this.load();
        } else {
          this.$message.error("批量删除失败");
        }
      });
    }
  },
};
</script>

<style>
.el-table {
  margin-top: 10px;
  color: black !important;
  text-align: center !important;
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

.icon {
  width: 16px;
  height: 16px;
  vertical-align: -0.15em;
  fill: currentColor;
  overflow: hidden;
}

.el-option svg,span{
  vertical-align: top;
}
</style>
