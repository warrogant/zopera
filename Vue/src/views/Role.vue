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
      <el-table-column prop="name" label="名称" width="200" class="itme">
      </el-table-column>
      <el-table-column prop="description" label="描述" width="200" class="itme">
      </el-table-column>
      <el-table-column prop="flag" label="唯一标识" width="250" class="itme">
      </el-table-column>
      <el-table-column label="操作">
        <template v-slot="scope">
          <el-button size="mini" type="warning" @click="selectMenu(scope.row.id)"><i class="el-icon-folder-checked"></i>
            权限</el-button>
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
    <!-- 弹窗 -->
    <el-dialog title="新增角色信息" :visible.sync="dialogVisible" :before-close="handleClose" width="30%">
      <el-form :label-position="labelPosition" label-width="80px" :model="form" >
        <el-form-item label="昵称：" prop="name">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="描述：" prop="description">
          <el-input v-model="form.description"></el-input>
        </el-form-item>
        <el-form-item label="唯一标识：" prop="description">
          <el-input v-model="form.flag"></el-input>
        </el-form-item>
      </el-form>
      <el-button @click="closePopup">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </el-dialog>
    <!-- 分配菜单 -->
    <!--
      :check-strictly="true"  : 一级
      -->

    <el-dialog title="分配权限" :visible.sync="menuDialogVis" :before-close="handleClose" width="30%">
      <el-tree :props="props" ref="tree" :data="menuData" show-checkbox node-key="id" :default-expanded-keys="expends"
        :default-checked-keys="checks">
        <span class="custom-tree-node" slot-scope="{data}">
          <span>
            <svg class="icon" aria-hidden="true">
              <use v-bind:xlink:href="data.icon"></use>
            </svg>&nbsp;&nbsp;&nbsp;{{ data.name }}
          </span>
        </span>
      </el-tree>
      <div style="margin-top: 5px;">
        <el-button @click="menuDialogVis = false">取 消</el-button>
        <el-button type="primary" @click="saveRoleMenu">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "roleBackstage",
  props: [""],
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 5,
      // 搜索框内容
      name: "",
      // 新增弹窗
      dialogVisible: false,
      menuDialogVis:false,
      labelPosition: "right",
      headerBg: "headerBg",
      // 菜单默认展开设置
      expends: [],
      checks: [],
      form: {
        name: '',
        description: ""
      },
      // 表单多选
      multipleSelection: [],
      // 权限树结构
      menuData: [],
      props: {
        children: 'children',
        label: 'name'
      },
      roleId: 0,
      roleFlag: '',
      ids:[],

    };
  },
  created() {
    this.load();
  },
  methods: {
    // 分页接口调用,请求查询分页数据
    load() {
      this.request
        .get("/role/page", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            name: this.name
          },
        })
        .then((res) => {
          // console.log(res.data.total);
          this.tableData = res.data.records;
          this.total = res.data.total;
        });
    },
    // 重置
    reset() {
      this.name = "";
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
      this.menuDialogVis = false
      this.load();
    },
    // 弹窗关闭
    handleClose() {
      this.closePopup();
    },
    // 新增弹窗保存
    save() {
      this.request.post("/role/save", this.form).then((res) => {
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
    // 保存权限选项
    saveRoleMenu() {
      this.request.post('/role/roleMenu/' + this.roleId, this.$refs.tree.getCheckedKeys()).then(res => {
        // console.log(res);
        if (res.code == '200') {
          this.$message.success('绑定成功')
          this.menuDialogVis = false
          
          // if (this.roleFlag == 'ROLE_ADMIN') {
          //  console.log(111);
          //   this.$store.commit('logout')
          // }
        } else {
          this.$message.error('绑定失败')
        }
      })
    },
    // 表单编辑
    handleEdit(row) {
      this.form = row;
      this.dialogVisible = true;
    },
    // 表单(id)删除
    handleDelete(id) {
      this.request.delete("/role/del/" + id).then((res) => {
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
      this.request.delete("/role/del/batch", { data: ids }).then((res) => {
        if (res.data) {
          // console.log(res);
          this.$message.success("批量删除成功");
          this.load();
        } else {
          this.$message.error("批量删除失败");
        }
      });
    },
    // 分配菜单  roleId
    selectMenu(roleId) {
      // this.menuDialogVis = true;
      this.roleId = roleId
      // 请求菜单数据
      this.request.get('/menu').then(res => {
        this.menuData = res.data
        // 把后台返回的菜单数据处理成  id数据
        this.expends = this.menuData.map(v => v.id)
      })
      // 菜单设置
      this.request.get('/role/getRoleMenu/' + roleId).then(res => {
        this.menuDialogVis = true
        // 把后台返回的菜单数据处理成  id数据
        this.checks = res.data

        this.request.get('/menu/ids').then(res => {
          const ids = res.data
          ids.forEach(id => {
            if (!this.checks.includes(id)) {
              this.$refs.tree.setChecked(id,false)
              // this.$refs.tree.getCheckedKeys().concat(this.$refs.tree.getHalfCheckedKeys())
            }
          });
        })
      })
    }
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

.icon {
  width: 16px;
  height: 16px;
  vertical-align: -0.15em;
  fill: currentColor;
  overflow: hidden;
}
</style>
