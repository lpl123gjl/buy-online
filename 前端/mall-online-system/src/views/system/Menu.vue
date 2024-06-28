<template>
  <div>
      <!-- 菜单管理工具栏 -->
      <el-form :inline="true">
          <el-form-item>
              <el-button type="primary" @click="dialogFormVisible=true">新增菜单</el-button>
          </el-form-item>
      </el-form>
  
      <!-- 新增菜单对话框 -->
      <el-dialog title="编辑菜单" :visible.sync="dialogFormVisible" width="550px" @closed="closed('menuForm')">
          <el-form ref="menuForm":model="menuForm" label-width="100px" :rules="menuRules">
              <el-form-item label="上级菜单">
                  <el-select v-model="menuForm.parentId" placeholder="请选择上级菜单">
                      <template v-for="item in tableData">
                          <el-option :label="item.name" :value="item.id"></el-option>
                          <template v-for="child in item.children">
                              <el-option :label="child.name" :value="child.id">
                                  <span>&nbsp;&nbsp;&nbsp;{{ ' - ' + child.name }}</span>
                              </el-option>
                          </template>
                      </template>
                  </el-select>
              </el-form-item>
              <el-form-item label="菜单名称" label-width="100px" prop="name">
                  <el-input v-model="menuForm.name" />
              </el-form-item>
              <el-form-item label="权限编码" label-width="100px" prop="perms">
                  <el-input v-model="menuForm.perms" />
              </el-form-item>
              <el-form-item label="图标" label-width="100px">
                  <el-input v-model="menuForm.icon" />
              </el-form-item>
              <el-form-item label="菜单URL" label-width="100px">
                  <el-input v-model="menuForm.path" />
              </el-form-item>
              <el-form-item label="菜单组件" label-width="100px">
                  <el-input v-model="menuForm.component" />
              </el-form-item>
              <el-form-item label="菜单类型" label-width="100px">
                  <el-radio-group v-model="menuForm.type">
                      <el-radio :label="0">目录</el-radio>
                      <el-radio :label="1">菜单</el-radio>
                      <el-radio :label="2">按钮</el-radio>
                  </el-radio-group>
              </el-form-item>
              <el-form-item label="菜单状态" label-width="100px">
                  <el-radio-group v-model="menuForm.statu">
                      <el-radio :label="0">禁用</el-radio>
                      <el-radio :label="1">正常</el-radio>
                  </el-radio-group>
              </el-form-item>
              <el-form-item>
                  <el-button type="primary" @click="submitMenuForm('menuForm')">保存菜单</el-button>
                  <el-button @click="resetMenuForm('menuForm')">重置</el-button>
              </el-form-item>
          </el-form>
      </el-dialog>
  
      <!-- 菜单的表格数据显示 -->
      <el-table :data="tableData" border style="width:100%; margin-bottom:20px;" row-key="id" 
      :tree-props="{hasChildren:'hasChildren', children:'children' }" :cell-style="{padding:'4px'}">
          <el-table-column prop="name" label="名称" width="180"></el-table-column>
          <el-table-column prop="perms" label="权限编码" width="180"></el-table-column>
          <el-table-column prop="icon" label="菜单图标" width="180"></el-table-column>
          <el-table-column prop="path" label="菜单URL" width="180"></el-table-column>
          <el-table-column prop="component" label="菜单组件" width="180"></el-table-column>
          <el-table-column prop="type" label="菜单类别" width="120">
              <!-- scope就是表格的数据 取当前这一行数据 scope.row.type -->
              <template slot-scope="scope">
                  <el-tag v-if="scope.row.type===0" type="info" size="mini">目录</el-tag>
                  <el-tag v-else-if="scope.row.type===1" size="mini">菜单</el-tag>
                  <el-tag v-else-if="scope.row.type===2" type="warning" size="mini">按钮</el-tag>
              </template>
          </el-table-column>
          <el-table-column prop="statu" label="菜单状态" width="120">
              <template slot-scope="scope">
                  <el-tag v-if="scope.row.statu===1" type="success" size="mini">正常</el-tag>
                  <el-tag v-else-if="scope.row.statu===0" type="danger" size="mini">禁用</el-tag>
              </template>
          </el-table-column>
          <el-table-column fixed="right" label="操作">
              <template slot-scope="scope">
                  <el-button @click="editMenu(scope.row.id)" type="text" size="small">编辑</el-button>
                  <el-divider direction="vertical" />
                  <el-popconfirm  confirm-button-text='确定删除' cancel-button-text='不用了'
    icon="el-icon-info" icon-color="red" title="确定要删除该菜单数据吗？" @confirm="removeMenu(scope.row.id)">
                       <el-button slot="reference" type="text" size="small">删除</el-button>
                  </el-popconfirm>
              </template>
          </el-table-column>
      </el-table>
      
  
  </div>
  </template>
   <script>
   export default{
       data(){
           return{
             //新增菜单消息框 是否显示
             dialogFormVisible:false, 
             //新增菜单对象
             menuForm:{
                  id:0,
                  parentId:0,  //上级菜单id
                  name:'',    //菜单名称
                  perms:'',   //菜单权限字符串  sys:user:save
                  icon:'',    //菜单图标
                  path:'',    //菜单路由到组件路径
                  component:'',   //菜单路由组件的位置
                  type:0,         //菜单 0 目录    1 菜单      2按钮
                  statu:1,        //菜单状态   1正常   0禁用
             },
             //菜单界面 表格数据
             tableData:[],
  
             //新建菜单 对话框验证规则
             menuRules:{
              name:[ { required: true, message: '请输入菜单名称', trigger: 'blur' } ],
              perms:[ { required: true, message: '请输入菜单权限编码', trigger: 'blur' } ],
             }
           }
       },
       created(){
          this.loadMenuList();
       },
       methods:{
          
          //关闭对话框，执行数据重置
          closed(formName){
              this.resetMenuForm(formName);
          },
          //删除菜单
          removeMenu(id){
              this.$axios.post('/menu/delete/'+id).then(response=>{
                  this.$message({
                      showClose: true,
                      message: response.data.resultdata,
                      type: 'success',
                      duration:1000,
                  });
                  //重新加载表格中菜单数据:
                  this.loadMenuList();
              }).catch(e=>{console.log(e);})
          },
          //编辑菜单  参数，编辑菜单id
          editMenu(id){
              this.$axios.get('/menu/info/'+id).then(response=>{
                  //接受返回
                 this.menuForm = response.data.resultdata;
                 this.dialogFormVisible=true;
              }).catch(e=>{console.log(e);})
          },
          //加载页面菜单表格数据
          loadMenuList(){
              this.$axios.get('/menu/list').then(response=>{
                  this.tableData = response.data.resultdata;
              }).catch(e=>{console.log(e);})
          },
  
          //提交保存菜单对话框
          submitMenuForm(formName){
              this.$refs[formName].validate((valid)=>{
                  if(valid){
                      //新建：/menu/save 
                      //编辑：/menu/update
                      this.$axios.post('/menu/'+(this.menuForm.id!=0?'update':'save'),this.menuForm).then(response=>{
                          //显示添加菜单成功的消息框
                          this.$message({
                              showClose: true,
                              message: response.data.resultdata,
                              type: 'success',
                              onClose:()=>{
                                  //添加成功消息框的回调：
                                  this.loadMenuList();
                                  //新建菜单对话框。设置隐藏：
                                  this.dialogFormVisible=false;
                                  //重置 新建菜单表单数据
                                  this.resetMenuForm('menuForm');
                              }
                          });
                      });
                  }else{
                      return false;
                  }
              })
          },
          //重置新建菜单对话框表单
          resetMenuForm(formName){   
              this.$refs[formName].resetFields();
              this.menuForm.parentId=0;
              this.menuForm.icon='';
              this.menuForm.path='';
              this.menuForm.component='';
              this.menuForm.type=0;
              this.menuForm.statu=1;
              this.menuForm.name="";
              this.menuForm.perms="";
          }
       }
   }
   </script>
   <style scoped>
       
   </style>
     