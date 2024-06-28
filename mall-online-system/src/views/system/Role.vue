<template>
  <div>
    <el-form ref="searchForm" :model="searchForm" :inline="true">
      <el-form-item>
        <el-input placeholder="请输入要搜索的角色名称" v-model="searchForm.name" clearable />
      </el-form-item>
      <el-form-item>
        <el-button @click="loadRoleList()">搜索</el-button>
        <el-button type="primary" @click="dialogFormVisible = true">新建</el-button>
        <el-popconfirm title="确认要删除这些角色吗？" @confirm="deleteRole()">
          <el-button type="danger" slot="reference" :disabled="deleteAllBtnStu">批量删除</el-button>
        </el-popconfirm>
      </el-form-item>
    </el-form>
    <!-- 新建角色对话框 -->
    <el-dialog title="编辑角色" :visible.sync="dialogFormVisible" width="550px" @closed="closed('roleForm')">
      <el-form ref="roleForm" :model="roleForm" label-width="100px" :rules="RoleRules">
        <el-form-item label="角色名称" label-width="100px" prop="name">
          <el-input v-model="roleForm.name" />
        </el-form-item>
        <el-form-item label="角色编码" label-width="100px" prop="code">
          <el-input v-model="roleForm.code" />
        </el-form-item>
        <el-form-item label="描述" label-width="100px" prop="remark">
          <el-input v-model="roleForm.remark" />
        </el-form-item>
        <el-form-item label="菜单状态" label-width="100px" prop="statu">
          <el-radio-group v-model="roleForm.statu">
            <el-radio :label="0">禁用</el-radio>
            <el-radio :label="1">正常</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitRoleForm('roleForm')">保存角色</el-button>
          <el-button @click="resetRoleForm('roleForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
    <!-- 分配权限的对话框 -->
    <el-dialog title="分配权限" :visible.sync="dialogPermsFormVisible" width="450px" @closed="closedPermsForm('permsForm')">
      <el-form :model="permsForm" ref="permsForm" label-width="100px">
        <el-tree show-checkbox :data="MenuTreeData" ref="permsTree" :default-expand-all="true" :props="defaultProps"
          node-key="id" :check-strictly="true">
        </el-tree>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button>重置</el-button>
        <el-button type="primary" @click="submitPermsForm">保存权限</el-button>
      </div>
    </el-dialog>

    <el-table :data="tableData" border style="width:100%; margin-bottom:20px;" :cell-style="{ padding: '4px' }"
      @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50"></el-table-column>
      <el-table-column prop="name" label="角色名称" width="180"></el-table-column>
      <el-table-column prop="code" label="角色编码" width="180"></el-table-column>
      <el-table-column prop="remark" label="菜单描述" width="480"></el-table-column>
      <el-table-column prop="statu" label="角色状态" width="120">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.statu === 1" type="success" size="mini">正常</el-tag>
          <el-tag v-else-if="scope.row.statu === 0" type="danger" size="mini">禁用</el-tag>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="editRole(scope.row.id)">编辑</el-button>
          <el-divider direction="vertical"></el-divider>
          <el-button type="text" size="small" @click="permHandler(scope.row.id)">分配权限</el-button>
          <el-divider direction="vertical"></el-divider>
          <el-popconfirm title="确定要删除该角色数据吗？" @confirm="deleteRole(scope.row.id)">
            <el-button slot="reference" type="text" size="small">删除</el-button>
          </el-popconfirm>

        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="current"
      :page-sizes="[5, 10, 15, 20]" :page-size="size" layout="total, sizes, prev, pager, next, jumper" :total="total"
      style="display:flex;justify-content:center">
    </el-pagination>

  </div>
</template>

<script>
export default {
  data() {
    return {
      roleForm: {
        id: 0,
        name: '',
        code: '',
        remark: '',
        statu: 1,
      },
      tableData: [],
      dialogFormVisible: false,
      deleteAllBtnStu: true,
      searchForm: {
        name: '',
      },
      current: 1,
      size: 5,
      total: 0,
      //分配权限的对话框是否显示变量
      dialogPermsFormVisible: false,
      //权限对象
      permsForm: {},
      MenuTreeData: [], //分配权限Tree树形控件的数据
      //设置树形控件配置，label树形控件每个节点文本取那个属性，children子选项取那个属性
      defaultProps: {
        children: 'children',
        label: 'name'
      },

      RoleRules: {
        name: [
          { required: true, message: '请输入角色名称', trigger: 'blur' },
        ],
        code: [
          { required: true, message: '请输入角色编码', trigger: 'blur' },
        ]
      }
    }
  },
  components: {

  },
  created() {
    this.loadRoleList();
  },
  methods: {
    //删除角色的方法
    deleteRole(id) {
      var roleIds = []; //存储所有要删除的角色id
      //单个删除 参数id是有值的。而批量删除是从selection数组取删除的角色id
      id ? roleIds.push(id) : this.selection.forEach(r => { roleIds.push(r.id) });
      this.$axios.post('/role/delete', roleIds).then(response => {
        this.$message({
          showClose: true,
          message: response.data.resultdata,
          type: 'success',
          duration: 1000,
        });
        //删除成功，重新加载角色的分页表格数据
        this.loadRoleList();
      }).catch(e => { console.log(e); })
    },
    //加载表格分页数据
    loadRoleList() {
      this.$axios.get('/role/list', {
        params: {
          name: this.searchForm.name,
          current: this.current,
          size: this.size,
        }
      }).then(response => {
        this.tableData = response.data.resultdata.records //分页的表格数据
        this.current = response.data.resultdata.current
        this.size = response.data.resultdata.size
        this.total = response.data.resultdata.total
      }).catch(e => { console.log(e); })
    },
    //点击页面上 页码触发的事件方法，该方法中编写，根据点击的页码加载该页的数据。
    //val 参数就是点击 页码
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.current = val;
      this.loadRoleList(); //当前页面改变，重新加载页面的数据。
    },
    //选择一页显示记录数触发的事件方法，该方法中编写 重新加载分页和表格数据
    //val就是选择 一页要显示的记录数。
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.size = val;
      this.loadRoleList(); //一页显示的记录数改变，重新加载分页和表格数据。
    },
    //改变表格 复选框的事件方法
    //参数roleArr 就是复选框选中对应这一行角色对象的数组(可以选多行)。
    handleSelectionChange(roleArr) {
      this.selection = roleArr
      if (this.selection) {
        //this.selection数组为true，表示选中角色数据行
        //this.selection.length === 0 没有选中，设置deleteAllBtnStu 设置true禁用
        //this.selection.length !==0 选中 设置deleteAllBtnStu 设置false不禁用
        this.deleteAllBtnStu = (this.selection.length === 0);
      }
    },
    //提交保存角色的方法
    submitRoleForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          //提交服务器端，保存角色的信息
          this.$axios.post('/role/' + (this.roleForm.id != 0 ? 'update' : 'save'), this.roleForm).then(response => {
            this.$message({
              showClose: true,
              message: response.data.resultdata,
              type: 'success',
              duration: 1000,
            });
            //添加角色成功，重新加载分页表格数据
            this.loadRoleList();
            //重置角色对话框中的表单
            this.resetRoleForm(formName);
            this.dialogFormVisible = false;
          }).catch(e => { console.log(e) });
        } else {
          return false;
        }
      })
    },

    //重置新建角色对话框表单
    resetRoleForm(formName) {
      this.$refs[formName].resetFields();
      this.roleForm.id = 0;
      this.roleForm.name = "";
      this.roleForm.code = "";
      this.roleForm.remark = "";
      this.roleForm.statu = 1;
    },
    //编辑角色,参数就是编辑角色id
    editRole(id) {
      this.$axios.get('/role/info/' + id).then(response => {
        //接受返回
        this.roleForm = response.data.resultdata;
        this.dialogFormVisible = true;
      }).catch(e => { console.log(e); })
    },
    closed(formName) {
      this.resetRoleForm(formName);
    },
    //加载表格分页数据
    loadRoleList() {
      this.$axios.get('/role/list', {
        params: {
          name: this.searchForm.name, //参数，就是模糊查询条件
          current: this.current,
          size: this.size,
        }
      }).then(response => {
        this.tableData = response.data.resultdata.records //分页的表格数据
        this.current = response.data.resultdata.current
        this.size = response.data.resultdata.size
        this.total = response.data.resultdata.total
      }).catch(e => { console.log(e); });
      //查询Tree控件 显示所有的权限信息
      this.$axios.get('/menu/list').then(response => {
        this.MenuTreeData = response.data.resultdata;
      }).catch(e => { console.log(e); });
    },
    permHandler(id) {
      this.$axios.get('/role/info/' + id).then(response => {
        this.$refs.permsTree.setCheckedKeys(response.data.resultdata.menuIds);
        this.permsForm = response.data.resultdata;
      }).catch(e => {
        console.log(e);
      })
      this.dialogPermsFormVisible = true
    },
    //提交保存角色权限的方法
    submitPermsForm() {
      //获得角色现在权限编码menuIds
      let menuIds = this.$refs.permsTree.getCheckedKeys();
      console.log(this.permsForm.id)
      //提交服务器保存 权限信息到 关联表 sys_role_menu
      this.$axios.post('/role/perms/' + this.permsForm.id, menuIds).then(response => {
        this.$message({
          showClose: true,
          message: response.data.resultdata,
          type: 'success',
          duration: 1000,
        });
        this.loadRoleList(); //重新加载角色信息
        this.dialogPermsFormVisible = false;
      }).catch(e => { console.log(e); })
    },
    closedPermsForm(formName) {

    }

  },

}

</script>
<style scoped></style>