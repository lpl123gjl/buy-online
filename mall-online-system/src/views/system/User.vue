<template>
  <div>
      <!-- 用户管理 工具栏 -->
      <el-form ref="searchForm" :model="searchForm" :inline="true">
          <el-form-item>
              <el-input placeholder="请输入要搜索的用户名称" v-model="searchForm.name" clearable /> 
          </el-form-item>    
          <el-form-item>
              <el-button @click="loadUserList()">搜索</el-button>
              <el-button type="primary" @click="dialogFormVisible=true">新建</el-button> 
              <el-popconfirm title="确认要删除这些用户吗?" @confirm="deleteUser()">
                  <el-button v-if="hasAuth('sys:user:delete')" type="danger" slot="reference" :disabled="deleteAllBtnStu">批量删除</el-button>
              </el-popconfirm>
          </el-form-item>
      </el-form>
  
      <!-- 用户数据表格显示 -->
      <el-table :data="tableData" border style="width:100%; margin-bottom:20px;" 
          :cell-style="{padding:'4px'}" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="50"></el-table-column>
          <el-table-column label="头像" width="58">
              <template slot-scope="scope">
                  <!-- <img style="width:40px; height:40px; background-size: cover;" :src="scope.row.avatar"/> -->
                  <el-avatar  :src="scope.row.avatar"></el-avatar>
              </template>
          </el-table-column>
          <el-table-column prop="username" label="用户名" width="120" />
          <el-table-column  label="角色" width="180" >
              <template slot-scope="scope">
                  <el-tag type="warning" size="small" v-for="(item,index) in scope.row.roles" :key="index" style="margin-left:2px;">
                  {{ item.name }}
                  </el-tag>
              </template>
          </el-table-column>
          <el-table-column prop="email" label="邮箱" width="180" />
          <el-table-column prop="tel" label="电话" width="140" />
          <el-table-column prop="statu" label="用户状态" width="120">
              <template slot-scope="scope">
                  <el-tag v-if="scope.row.statu===1" type="success" size="mini">正常</el-tag>
                  <el-tag v-else-if="scope.row.statu===0" type="danger" size="mini">禁用</el-tag>
              </template>    
          </el-table-column>
          <el-table-column label="创建时间">
              <template slot-scope="scope">
                  {{ scope.row.created | fmtDate }}
              </template>
          </el-table-column>
          <el-table-column fixed="right" label="操作">
                  <template slot-scope="scope">
                      <el-button type="text" size="small" @click="editUser(scope.row.id)">编辑</el-button>
                      <el-divider direction="vertical" />
                      <el-button type="text" size="small" @click="editRoleMethod(scope.row.id)">分配角色</el-button>
                      <el-divider direction="vertical" />
                      <el-button type="text" size="small" @click="editBusinessMethod(scope.row.id)">分配商家</el-button>
                      <el-divider direction="vertical" />
                      <el-button type="text" size="small" v-if="hasAuth('sys:user:repass')" @click="resetPassword(scope.row.id,scope.row.username)">重置密码</el-button>
                      <el-divider direction="vertical" />
                      <el-popconfirm title="确定要删除该用户数据吗？" @confirm="deleteUser(scope.row.id)">
                          <el-button slot="reference" type="text" size="small" v-if="hasAuth('sys:user:delete')">删除</el-button>
                      </el-popconfirm>
                  </template>
              </el-table-column>
      </el-table>
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="current"
          :page-sizes="[5, 10, 15, 20]"
          :page-size="size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          style="display:flex;justify-content:center">
      </el-pagination>
  
      <!-- 新建和编辑用户的对话框 -->
      <el-dialog title="编辑用户" :visible.sync="dialogFormVisible" width="550px" @closed="resetUserForm('userForm')">
          <el-form ref="userForm" :model="userForm" label-width="100px" :rules="userRules">
              <el-form-item lang="用户头像" label-width="100px">
                  <!-- 因为上传图片不是使用 axios，所以不会自动添加基础路径 /sys，需要自己添加 -->
                  <!-- 因为上传组件不是使用axios,所以需要手动设置请求头携带token :headers="headers" -->
                  <el-upload class="avatar-uploader" action="/sys/api/upload" 
                  :show-file-list="false" :on-success="handleAvatarSuccess" :headers="headers">
                  <img v-if="userForm.avatar" :src="userForm.avatar" class="avatar">
                  <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                  </el-upload>
              </el-form-item>
              <el-form-item label="用户名" label-width="100px" prop="username">
                  <el-input v-model="userForm.username" />
              </el-form-item>
              <el-form-item label="密码" label-width="100px">
                  <el-input v-model="userForm.password" />
              </el-form-item>
              <el-form-item label="邮件" label-width="100px" prop="email">
                  <el-input v-model="userForm.email" />
              </el-form-item>
              <el-form-item label="手机号" label-width="100px" prop="tel">
                  <el-input v-model="userForm.tel" />
              </el-form-item>
              <el-form-item label="用户状态" label-width="100px" prop="statu">
                  <el-radio-group v-model="userForm.statu">
                      <el-radio :label="0">禁用</el-radio>
                      <el-radio :label="1">正常</el-radio>
                  </el-radio-group>
              </el-form-item>
              <el-form-item>
                  <el-button type="primary" @click="submitUserForm('userForm')">保存用户</el-button>
                  <el-button @click="resetUserForm('userForm')">重置</el-button>
              </el-form-item>
          </el-form>
      </el-dialog>
  
      <!-- 分配角色对话框  -->
      <el-dialog title="分配角色" :visible.sync="dialogRoleFormVisible" width="550px" >
          <el-form :model="userRoleForm" ref="userRoleForm">
              <el-tree show-checkbox :data="roleTreeData" ref="roleTree" :default-expand-all="true" node-key="id"
              :props="defaultProps" >
              </el-tree>
          </el-form>
          <div slot="footer" class="dialog-footer">
                  <el-button @click="close">关闭</el-button>
                  <el-button type="primary" @click="submitRoleForm('userRoleForm')">保存分配角色</el-button>
          </div>
      </el-dialog>
      <!-- 分配商家对话框  -->
      <el-dialog title="分配商家" :visible.sync="dialogBusinessFormVisible" width="350px" >
          <el-form :model="userBusinessForm" ref="userBusinessForm">
              <el-tree show-checkbox :data="userBusinessTreeData" ref="userBusinessTree" :default-expand-all="true" node-key="id"
              :props="defaultProps" >
              </el-tree>
          </el-form>
          <div slot="footer" class="dialog-footer">
                  <el-button @click="close2">关闭</el-button>
                  <el-button type="primary" @click="submitBusinessForm('userBusinessForm')">保存分配商家</el-button>
          </div>
      </el-dialog>
  </div>
  </template>
   <script>
   export default{
       data(){
           return{
              //上传组件请求头信息：
              headers:{
                  token:JSON.parse(sessionStorage.getItem('TOKEN')) 
              },
  
              //分配角色表单对象
              userRoleForm:{},
              //分配角色树形控件数据
              roleTreeData:[],

              //分配商家表单对象
              userBusinessForm:{},
              //分配商家树形控件数据
              userBusinessTreeData:[],

              //新建用户表单验证规则
              userRules:{
                  username:[ { required: true, message: '请输入用户名', trigger: 'blur' } ],
                  email:[ { required: true, message: '请输入电子邮箱', trigger: 'blur' } ],
                  tel:[ { required: true, message: '请输入手机号', trigger: 'blur' } ],
              },
  
              //用户对话框表单
              userForm:{
                  id:0,  
                  avatar:'',   //头像
                  username:'', //用户名
                  password:'',  //密码
                  email:'',   //邮件
                  tel:'', //电话
                  statu:1,  //状态
              },
  
              tableData:[],   //用户表格显示数组
              deleteAllBtnStu:true, //是否禁用用户的批量删除
              dialogFormVisible:false,  //是否显示新建用户对话框
              dialogRoleFormVisible:false,  //是否显示分配角色对话框
              dialogBusinessFormVisible:false,  //是否显示分配商家对话框
              //搜索工具栏对象
              searchForm:{
                  name:''
              },
              //分页相关的参数
              current:1, //当前页码，默认是显示第一页
              size:5, //一页显示记录数
              total:0, //总共的记录数
  
              selection:[],  //存储表格中所有复选框 选中用户对象
              defaultProps:{
                  children:'children',
                  label:'name',
              },
           }
       },
       created(){
          this.loadUserList();
          this.$axios.get('/role/tree').then(response=>{
              this.roleTreeData = response.data.resultdata
          }).catch(e=>{ console.log(e); })

          //加载分配商家tree
          this.loadBusinessTree()
          
       },
       
       methods:{
          //分配当前用户的角色信息
          editRoleMethod(id){
              //使用用户id 查询该用户的信息以及该用户所具有角色信息
              this.$axios.get('/user/info/'+id).then(response=>{
                  let user = response.data.resultdata;
                  this.userRoleForm = user;
                  //获得当前用户 具备所有的角色id
                  let roleIds = [];
                  user.roles.forEach( r =>{
                      roleIds.push(r.id);
                  });
  
  
                  //在Tree控件中设置当前用户具备的角色
                  this.$refs.roleTree.setCheckedKeys(roleIds);
  
              }).catch(e=>{ console.log(e); });
              this.dialogRoleFormVisible=true;  //显示分配角色对话框
          },
          //保存分配商家
          submitBusinessForm(formName){
              //获得tree控件中分配的角色id
              let businessIds = this.$refs.userBusinessTree.getCheckedKeys();
  
              //提交服务器端保存：
              this.$axios.post('/user/business/'+this.userBusinessForm.id,businessIds).then(response=>{
                  this.$message({
                              showClose: true,
                              message: response.data.resultdata,
                              type: 'success',
                              duration:1000,
                  });
                  this.loadUserList();
                  this.loadBusinessTree()
                  this.dialogBusinessFormVisible = false;
              }).catch(e=>{ console.log(e); });
          },
          loadBusinessTree(){
            this.$axios.get('/business/tree').then(response=>{
              this.userBusinessTreeData = this.formatTreeData(response.data.resultdata);
          }).catch(e=>{ console.log(e); })
          },
          //分配当前用户的商家信息
          editBusinessMethod(id){
            this.$axios.get('/user/info/'+id).then(response=>{
                  let user = response.data.resultdata;
                  this.userBusinessForm = user;
              }).catch(e=>{ console.log(e); });
              this.dialogBusinessFormVisible=true;  //显示分配角色对话框
          },
          formatTreeData(businessIdList) {
            return businessIdList.map(id => {
                return {
                id: id,
                name: id
                };
            });
            },
          //保存分配角色
          submitRoleForm(formName){
              //获得tree控件中分配的角色id
              let roleIds = this.$refs.roleTree.getCheckedKeys();
  
              //提交服务器端保存：
              this.$axios.post('/user/role/'+this.userRoleForm.id,roleIds).then(response=>{
                  this.$message({
                              showClose: true,
                              message: response.data.resultdata,
                              type: 'success',
                              duration:1000,
                  });
                  this.loadUserList();
                  this.dialogRoleFormVisible = false;
              }).catch(e=>{ console.log(e); });
          },

          //重置密码：
          resetPassword(id,username){
              this.$confirm('重置用户【'+username+'】的密码，是否继续执行该操作','重置账户密码提示',{
                  confirmButtonText:'执行',
                  cancelButtonText:'取消',
                  type:'warning'
              }).then(()=>{
                  //请求服务器端，重置密码
                  this.$axios.post('/user/repass/'+id).then(response=>{
                      this.$message({
                              showClose: true,
                              message: response.data.resultdata,
                              type: 'success',
                              duration:1000,
                      });
                  }).catch(e=>{ console.log(e); })
  
              }).catch(()=>{});
          },
          //编辑用户的方法
          editUser(id){
              this.$axios.get('/user/info/'+id).then(response=>{
                  let user = response.data.resultdata;
                  this.userForm = user;
                  this.dialogFormVisible=true;
                  //TODO: 拿到用户权限，需要设置分配角色 树形控件
              })
          },
          //重置对话框
          resetUserForm(formName){
              this.userForm={
                  id:0,  
                  avatar:'',   
                  username:'', 
                  password:'',  
                  email:'',   
                  tel:'', 
                  statu:1,  
              };
              this.$refs[formName].resetFields();
          },
          //提交新建用户或 编辑用户的表单
          submitUserForm(formName){
              this.$refs[formName].validate((valid)=>{
                  if(valid){
                      this.$axios.post('/user/'+(this.userForm.id!=0?'update':'save'),this.userForm).then(response=>{
                          this.$message({
                              showClose: true,
                              message: response.data.resultdata,
                              type: 'success',
                              duration:1000,
                          });
                          //添加用户成功，重新加载分页表格数据
                          this.loadUserList();
                          //重置角色对话框中的表单
                          this.resetUserForm(formName);
                          this.dialogFormVisible=false;
                      }).catch(e=>{ console.log(e)});
                  }else{
                      return false;
                  }
              })
          },
          //上传用户头像成功以后回调函数
          handleAvatarSuccess(response,file){
              console.log('头像',response.resultdata);
              //上传成功，从回调的response参数中 获得上传图片在OSS服务器上地址。
              //这个response和axios中使用response对象是不一样的。这个response不需要.data。直接返回的就是 result数据。
              this.userForm.avatar = response.resultdata
              this.$forceUpdate()  //强制刷新
          },
          handleSizeChange(val){
              console.log(`每页 ${val} 条`);
              this.size = val;
              this.loadUserList(); //一页显示的记录数改变，重新加载分页和表格数据。
          },
          handleCurrentChange(val){
              console.log(`当前页: ${val}`);
              this.current = val;
              this.loadUserList();  //当前页面改变，重新加载页面的数据。
          },
          //加载 分页用户表格数据
          loadUserList(){
              this.$axios.get('/user/list',{
                  params:{
                      name:this.searchForm.name,  //参数，就是模糊查询条件
                      current:this.current,
                      size:this.size,
                  }
              }).then(response=>{
                  this.tableData = response.data.resultdata.records  //分页的表格数据
                  this.current = response.data.resultdata.current 
                  this.size = response.data.resultdata.size
                  this.total = response.data.resultdata.total
              }).catch(e=>{ console.log(e); });
          },
          //删除用户的方法
          deleteUser(id){
              var userIds =[];  //存储所有要删除的用户id 
  
              //单个删除 参数id是有值的。而批量删除是从selection数组取删除的用户id
              id?userIds.push(id):this.selection.forEach( u=>{ userIds.push(u.id) } );
  
              this.$axios.post('/user/delete',userIds).then(response=>{
                  this.$message({
                      showClose: true,
                      message: response.data.resultdata,
                      type: 'success',
                      duration:1000,
                  });
                  //删除成功，重新加载角色的分页表格数据
                  this.loadUserList();
              })  
          },
          //用户表格数据的复选框事件方法
          handleSelectionChange(userArr){
              this.selection = userArr;
              if(this.selection){
                  this.deleteAllBtnStu = (this.selection.length === 0);
              }
          },
          close(){
            this.dialogRoleFormVisible = false;
          },
          close2(){
            this.dialogBusinessFormVisible = false;
          }
       }
   }
   </script>
   <style scoped>
       .avatar-uploader .el-upload {
          border: 1px dashed #d9d9d9;
          border-radius: 6px;
          cursor: pointer;
          position: relative;
          overflow: hidden;
      }
      .avatar-uploader .el-upload:hover {
          border-color: #409EFF;
      }
      .avatar-uploader-icon {
          font-size: 28px;
          color: #8c939d;
          width: 178px;
          height: 178px;
          line-height: 178px;
          text-align: center;
      }
      .avatar {
          width: 178px;
          height: 178px;
          display: block;
          
      }
   </style>
     