<template>
    <div>
        <!-- 用户管理 工具栏 -->
        <el-form ref="searchForm1" :model="searchForm1" :inline="true">
            <el-form-item>
                <el-input placeholder="请输入要搜索的账户名称" v-model="searchForm1.accountName" clearable /> 
            </el-form-item>    
            <el-form-item>
                <el-button @click="loadAccountList()">搜索</el-button>
                <el-button type="primary" @click="dialogFormVisible1=true">新建</el-button> 
                <el-popconfirm title="确认要删除这些用户吗?" @confirm="deleteAccount()">
                    <el-button type="danger" slot="reference" :disabled="deleteAllBtnStu1">批量删除</el-button>
                </el-popconfirm>
            </el-form-item>
        </el-form>
    
        <!-- 用户数据表格显示 -->
        <el-table :data="tableData1" border style="width:100%; margin-bottom:20px; text-align: center;" 
            :cell-style="{padding:'4px'}" @selection-change="handleSelection1Change">
            <el-table-column type="selection" width="50"></el-table-column>
            <el-table-column label="头像" width="70">
                <template slot-scope="scope">
                    <!-- <img style="width:40px; height:40px; background-size: cover;" :src="scope.row.avatar"/> -->
                    <el-avatar  :src="scope.row.accountImg"></el-avatar>
                </template>
            </el-table-column>
            <el-table-column prop="accountName" label="用户名" width="120" />
            <el-table-column label="性别" width="100">
                <template slot-scope="scope" >
                        <el-tag v-if="scope.row.accountSex===0" type="success" size="mini">女生</el-tag>
                        <el-tag v-else-if="scope.row.accountSex===1" type="warning" size="mini">男生</el-tag>
                </template>
             </el-table-column>
            <el-table-column prop="accountId" label="电话" width="140" />
            <el-table-column label="创建时间">
                <template slot-scope="scope">
                    {{ scope.row.created | fmtDate }}
                </template>
            </el-table-column>
            <el-table-column label="更新时间">
            <template slot-scope="scope" v-if="scope.row.updated!=null">
                {{ scope.row.updated | fmtDate }}
            </template>
            <template v-else>
                
            </template>
            </el-table-column>
            <el-table-column label="用户地址详情" width="120">
                <template slot-scope="scope">
                <el-button @click="openDrawer(scope.row.accountId)" type="primary" style="margin-left: 6px; padding: 8px;" icon="el-icon-position" size="mini" >查询地址</el-button>
                <el-drawer
                    title="用户详细地址"
                    :visible.sync="drawer"
                    size="40%"
                    :direction="direction"
                    ref="drawer"
                    :before-close="handleClose"
                    :modal-append-to-body='false'>
                    <div>
         <!-- 角色管理 工具栏 -->
         <!-- <el-form ref="searchForm" :model="searchForm" :inline="true">
             <el-form-item>
                 <el-input placeholder="请输入要搜索的联系人名称" v-model="searchForm.contactName" clearable /> 
             </el-form-item>    
             <el-form-item>
                 <el-button @click="loadComuList()">搜索</el-button>
             </el-form-item>
         </el-form> -->
 
         <!-- 角色表格 -->
         <el-table :data="tableData" border style="width:100%; margin-bottom:20px;" 
         :cell-style="{padding:'4px'}" @selection-change="handleSelectionChange">
             <el-table-column type="selection" width="50"></el-table-column>
             <el-table-column prop="contactName" label="联系人姓名" width="150"></el-table-column>
             <el-table-column label="性别" width="100">
                <template slot-scope="scope" >
                        <el-tag v-if="scope.row.statu===0" type="success" size="mini">女生</el-tag>
                        <el-tag v-else-if="scope.row.statu===1" type="warning" size="mini">男生</el-tag>
                </template>
             </el-table-column>
             <el-table-column prop="contactTel" label="电话" width="150"></el-table-column>
             <el-table-column prop="address" label="地址" width="380"></el-table-column>
             <el-table-column prop="accountName" label="所属用户" width="120">
                <template slot-scope="scope" >
                <el-tag type="warning" size="small" style="margin-left:2px;">
                {{ scope.row.accountName }}
                </el-tag>
                </template>
                </el-table-column>
             <el-table-column label="创建时间">
            <template slot-scope="scope">
                {{ scope.row.created | fmtDate }}
            </template>
        </el-table-column>
        <el-table-column label="更新时间">
            <template slot-scope="scope" v-if="scope.row.updated!=null">
                {{ scope.row.updated | fmtDate }}
            </template>
            <template v-else>
                
            </template>
        </el-table-column>
             <el-table-column prop="statu" label="角色状态" width="120">
                 <template slot-scope="scope">
                     <el-tag v-if="scope.row.statu===1" type="success" size="mini">正常</el-tag>
                     <el-tag v-else-if="scope.row.statu===0" type="danger" size="mini">禁用</el-tag>
                 </template>    
             </el-table-column>
         </el-table>
         <!-- 分页组件 -->
         <el-pagination
         @size-change="handleSizeChange"
         @current-change="handleCurrentChange"
         :current-page="current"
         :page-sizes="[5, 10, 15, 20]"
         :page-size="size"
         layout="total, sizes, prev, pager, next, jumper"
         :total="total">
         </el-pagination>
                     </div>               
                </el-drawer>
            </template>
            </el-table-column>
            <el-table-column prop="statu" label="用户状态" width="90">
                <template slot-scope="scope">
                    <el-tag v-if="scope.row.statu===1" type="success" size="mini">正常</el-tag>
                    <el-tag v-else-if="scope.row.statu===0" type="danger" size="mini">禁用</el-tag>
                </template>    
            </el-table-column>
            <el-table-column fixed="right" label="操作">
                    <template slot-scope="scope">
                        <el-button type="text" size="small" @click="editAccount(scope.row.accountId)">编辑</el-button>
                        <el-divider direction="vertical" />
                    <el-button type="text" size="small"  @click="resetPassword(scope.row.accountId,scope.row.accountName)">重置密码</el-button>
                    <el-divider direction="vertical" />    
                    <el-popconfirm title="确定要删除该用户数据吗？" @confirm="deleteAccount(scope.row.accountId)">
                            <el-button slot="reference" type="text" size="small">删除</el-button>
                        </el-popconfirm>
                    </template>
                </el-table-column>
        </el-table>
        <el-pagination
            @size-change="handleSize1Change"
            @current-change="handleCurrent1Change"
            style="margin-top: 10px;"
            :current-page="current1"
            :page-sizes="[5, 10, 15, 20]"
            :page-size="size1"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total1">
        </el-pagination>
    
        <!-- 新建和编辑用户的对话框 -->
        <el-dialog title="编辑用户" :visible.sync="dialogFormVisible1" width="550px" @closed="resetAccountForm('accountForm')">
            <el-form ref="accountForm" :model="accountForm" label-width="100px" :rules="accountRules">
                <el-form-item lang="用户头像" label-width="100px">
                    <!-- 因为上传图片不是使用 axios，所以不会自动添加基础路径 /sys，需要自己添加 -->
                    <!-- 因为上传组件不是使用axios,所以需要手动设置请求头携带token :headers="headers" -->
                    <el-upload class="avatar-uploader" action="/sys/api/upload" 
                    :show-file-list="false" :on-success="handleAvatarSuccess" :headers="headers">
                    <img v-if="accountForm.accountImg" :src="accountForm.accountImg" class="avatar">
                    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                </el-form-item>
                <el-form-item label="用户名" label-width="100px" prop="accountName">
                    <el-input v-model="accountForm.accountName" />
                </el-form-item>
                <el-form-item label="密码" label-width="100px">
                    <el-input v-model="accountForm.password" />
                </el-form-item>
                <el-form-item label="性别" label-width="100px" prop="accountSex">
                    <el-radio-group v-model="accountForm.accountSex">
                        <el-radio :label="0">女</el-radio>
                        <el-radio :label="1">男</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="电话" label-width="100px" prop="accountId">
                    <el-input v-model="accountForm.accountId" />
                </el-form-item>
                <el-form-item label="用户状态" label-width="100px" prop="statu">
                    <el-radio-group v-model="accountForm.statu">
                        <el-radio :label="0">禁用</el-radio>
                        <el-radio :label="1">正常</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="操作" label-width="100px" prop="delTag">
                    <el-radio-group v-model="accountForm.delTag">
                        <el-radio :label="0">更新</el-radio>
                        <el-radio :label="1">创建</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="submitAccountForm('accountForm')">保存用户</el-button>
                    <el-button @click="resetAccountForm('accountForm')">重置</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
    </template>
     <script>
     export default{
         data(){
             return{
                drawer: false,
                direction: 'ttb',
                //上传组件请求头信息：
                headers:{
                    token:JSON.parse(sessionStorage.getItem('TOKEN')) 
                },
                //新建用户表单验证规则
                accountRules:{
                    accountName:[ { required: true, message: '请输入用户名', trigger: 'blur' } ],
                    accountId:[ { required: true, message: '请输入电话号码', trigger: 'blur' } ],
                    accountSex:[ { required: true, message: '请输入性别', trigger: 'blur' } ],
                    statu:[ { required: true, message: '请输入状态', trigger: 'blur' } ],
                    delTag:[ { required: true, message: '请输入操作', trigger: 'blur' } ],
                },
    
                //用户对话框表单
                accountForm:{
                    accountId:'',
                    password:'',   //头像
                    accountSex:'', //用户名
                    accountName:'',  //密码
                    accountImg:'',
                    statu:1,  //状态
                    delTag:1,
                },
    
                tableData1:[],   //用户表格显示数组
                deleteAllBtnStu1:true, //是否禁用用户的批量删除
                dialogFormVisible1:false,  //是否显示新建用户对话框
                dialogRoleFormVisible1:false,  //是否显示分配角色对话框
                //搜索工具栏对象
                searchForm1:{
                    accountName:''
                },
                //分页相关的参数
                current1:1, //当前页码，默认是显示第一页
                size1:5, //一页显示记录数
                total1:0, //总共的记录数
    
                selection:[],  //存储表格中所有复选框 选中用户对象



                //新建角色的表单对象
             deliveryForm:{
                 daId:0,     //角色id，更新id是有值。保存id===0
                 contactName:'',   //角色名称
                 contactSex:null,
                 accountId:'',
                 accountName:'',
                 contactTel:'',   //角色的编码
                 address:'',  //角色的描述
                 statu:null,    //角色的状态 1正常 0禁用
             },
             //角色对话框表单验证规则
             deliveryRules:{
                contactName:[ { required: true, message: '请输入联系人名称', trigger: 'blur' } ],
                contactTel:[ { required: true, message: '请输入联系人电话', trigger: 'blur' } ],
                address:[ { required: true, message: '请输入联系人地址', trigger: 'blur' } ],
                 statu:[ { required: true, message: '请输入联系人状态', trigger: 'blur' } ],
                 contactSex:[ { required: true, message: '请输入联系人性别', trigger: 'blur' } ],
             },
             tableData:[],  //角色表格的数据
             dialogFormVisible:false, //新建角色对话框是否可见。
             deleteAllBtnStu:true,  //批量删除按钮是否禁用
             //工具栏搜索的表单对象
             searchForm:{
                contactName:'',  //搜索框输入的文本内容 
             },
             //分页相关的参数
             current:1, //当前页码，默认是显示第一页
             size:5, //一页显示记录数
             total:0, //总共的记录数
             
             accountId:'',
             selection:[],  //记录使用表格复选框选中角色数据
             }
         },
         created(){
            this.loadAccountList();
            
         },
         methods:{
            openDrawer(id){
                this.loadComuList(id);
                this.drawer=true
                console.log(id)
                this.accountId=id;
            },
            resetAccountForm(formName){
            this.accountForm={
                    id:'',
                    accountId:'',
                    password:'',   //头像
                    accountSex:'', //用户名
                    accountName:'',  //密码
                    accountImg:'',
                    statu:1,  //状态
                    delTag:1,  
            };
            this.$refs[formName].resetFields();
        },
        resetPassword(accountId,accountName){
            this.$confirm('重置用户【'+accountName+'】的密码，是否继续执行该操作','重置账户密码提示',{
                confirmButtonText:'执行',
                cancelButtonText:'取消',
                type:'warning'
            }).then(()=>{
                //请求服务器端，重置密码
                this.$axios.post('/account/repass/'+accountId).then(response=>{
                    this.$message({
                            showClose: true,
                            message: response.data.resultdata,
                            type: 'success',
                            duration:1000,
                    });
                    this.loadAccountList()
                }).catch(e=>{ console.log(e); })

            }).catch(()=>{});
        },
        //提交新建用户或 编辑用户的表单
        submitAccountForm(formName){
            this.$refs[formName].validate((valid)=>{
                if(valid){
                    console.log(this.accountForm.accountId)
                    this.$axios.post('/account/'+(this.accountForm.delTag!=1?'update':'save'),this.accountForm).then(response=>{
                        this.$message({
                            showClose: true,
                            message: response.data.resultdata,
                            type: 'success',
                            duration:1000,
                        });
                        //添加用户成功，重新加载分页表格数据
                        this.loadAccountList();
                        //重置角色对话框中的表单
                        this.resetAccountForm(formName);
                        this.dialogFormVisible1=false;
                    }).catch(e=>{ console.log(e)});
                }else{
                    return false;
                }
            })
        },
            handleSelection1Change(roleArr){
             this.selection = roleArr
             if(this.selection){
                 //this.selection数组为true，表示选中角色数据行
                 //this.selection.length === 0 没有选中，设置deleteAllBtnStu 设置true禁用
                 //this.selection.length !==0 选中  设置deleteAllBtnStu 设置false不禁用
                 this.deleteAllBtnStu1 = (this.selection.length === 0);
             }
         },
            handleClose(done) {
            this.$confirm('确认关闭？').then(_ => {
                done();
            }).catch(_ => {});
            },
            editAccount(id){
                this.$axios.get('/account/info/'+id).then(response=>{
                let account = response.data.resultdata;
                this.accountForm = account;
                this.dialogFormVisible1=true;
            })
            },
            deleteAccount(id){
                var accountIds =[];  //存储所有要删除的角色id 
                console.log(id+111)
                //单个删除 参数id是有值的。而批量删除是从selection数组取删除的角色id
                id?accountIds.push(id):this.selection.forEach( r=>{ accountIds.push(r.accountId) } );
                this.$axios.post('/account/delete',accountIds).then(response=>{
                this.$message({
                    showClose: true,
                    message: response.data.resultdata,
                    type: 'success',
                    duration:1000,
                });
                //删除成功，重新加载角色的分页表格数据
                this.loadAccountList();
                }).catch(e=>{ console.log(e); })
            },
            handleAvatarSuccess(response,file){
            console.log('头像',response.resultdata);
            //上传成功，从回调的response参数中 获得上传图片在OSS服务器上地址。
            //这个response和axios中使用response对象是不一样的。这个response不需要.data。直接返回的就是 result数据。
            this.accountForm.accountImg = response.resultdata
            this.$forceUpdate()  //强制刷新
        },
            handleSize1Change(val){
                console.log(`每页 ${val} 条`);
                this.size1 = val;
                this.loadAccountList(); //一页显示的记录数改变，重新加载分页和表格数据。
            },
            handleCurrent1Change(val){
                console.log(`当前页: ${val}`);
                this.current1 = val;
                this.loadAccountList();  //当前页面改变，重新加载页面的数据。
            },
            //加载 分页用户表格数据
            loadAccountList(){
                this.$axios.get('/account/list',{
                    params:{
                        accountName:this.searchForm1.accountName,  //参数，就是模糊查询条件
                        current:this.current1,
                        size:this.size1,
                    }
                }).then(response=>{
                    this.tableData1 = response.data.resultdata.records  //分页的表格数据
                    this.current1 = response.data.resultdata.current
                    this.size1 = response.data.resultdata.size
                    this.total1 = response.data.resultdata.total
                }).catch(e=>{ console.log(e); });
            },
            //改变表格 复选框的事件方法




         //参数roleArr 就是复选框选中对应这一行角色对象的数组(可以选多行)。
         handleSelectionChange(roleArr){
             this.selection = roleArr
             if(this.selection){
                 //this.selection数组为true，表示选中角色数据行
                 //this.selection.length === 0 没有选中，设置deleteAllBtnStu 设置true禁用
                 //this.selection.length !==0 选中  设置deleteAllBtnStu 设置false不禁用
                 this.deleteAllBtnStu = (this.selection.length === 0);
             }
         },
 
         //点击页面上 页码触发的事件方法，该方法中编写，根据点击的页码加载该页的数据。
         //val 参数就是点击 页码
         handleCurrentChange(val){
             console.log(`当前页: ${val}`);
             this.current = val;
             this.loadComuList(this.accountId);  //当前页面改变，重新加载页面的数据。
         },
 
         //选择一页显示记录数触发的事件方法，该方法中编写 重新加载分页和表格数据
         //val就是选择 一页要显示的记录数。
         handleSizeChange(val){
             console.log(`每页 ${val} 条`);
             this.size = val;
             this.loadComuList(this.accountId); //一页显示的记录数改变，重新加载分页和表格数据。
         },
         //加载表格分页数据
         loadComuList(id){
             this.$axios.get('/delivery/list/'+id,{
                 params:{
                    accountId:this.accountForm.accountId,
                     contactName:this.searchForm.contactName,  //参数，就是模糊查询条件
                     current:this.current,
                     size:this.size,
                 }
             }).then(response=>{
                console.log(this.accountId)
                console.log(response.data.resultdata)
                 this.tableData = response.data.resultdata.records  //分页的表格数据
                 this.current = response.data.resultdata.current 
                 this.size = response.data.resultdata.size
                 this.total = response.data.resultdata.total
             }).catch(e=>{ console.log(e); });
         },
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
        .el-table-column{
            text-align: center
        }
     </style>
       