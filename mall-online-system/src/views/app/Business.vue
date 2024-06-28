<template>
    <div>
        <!--角色管理  -->
        <el-form ref="searchForm" :model="searchForm" :inline="true">
            <el-form-item>
                <el-input placeholder="请输入要搜索的商家名称" v-model="searchForm.name" clearable />
            </el-form-item>
            <el-form-item>
                <el-button @click="loadBusinessList()">搜索</el-button>
                <el-button type="primary" @click="dialogFormVisible=true">新建</el-button>
                <el-popconfirm title="确认要删除这些角色吗" @confirm="deleteBusiness()">
                    <el-button type="danger" slot="reference" :disabled="deleteAllBtnStu">批量删除</el-button>
                </el-popconfirm>
            </el-form-item>
        </el-form>

        <!--商家表格-->
        <el-table :data="tableData" border style="width:100%; margin-bottom:20px;" :cell-style="{padding:'4px'}"
            @selection-change="handleSelectionChange" stripe>
            <el-table-column type="selection" width="50"></el-table-column>
            <el-table-column label="头像" width="60">
                <template slot-scope="scope">
                    <el-avatar :src="scope.row.businessImg"></el-avatar>
                </template>
            </el-table-column>
            <el-table-column prop="businessName" label="商家名称" width="200"></el-table-column>
            <el-table-column prop="businessAddress" label="商家地址" width="250"></el-table-column>
            <el-table-column prop="businessExplain" label="商家描述" width="150"></el-table-column>
            <el-table-column prop="categoryId" label="商家分类id" width="100"></el-table-column>
            <el-table-column prop="starPrice" label="起送金额" width="100"></el-table-column>
            <el-table-column prop="deliveryPrice" label="配送费" width="100"></el-table-column>
            <el-table-column prop="statu" label="商家状态" width="120">
                <template slot-scope="scope">
                    <el-tag type="success" v-if="scope.row.statu === 1" size="mini">正常</el-tag>
                    <el-tag type="danger" v-if="scope.row.statu === 0" size="mini">禁用</el-tag>
                </template>
            </el-table-column>
            <el-table-column fixed="right" label="操作">
                <template slot-scope="scope">
                    <el-button @click="editBusiness(scope.row.businessId)" type="text" size="small">编辑</el-button>
                    <el-divider direction="vertical" />
                    <el-popconfirm title="确定要删除角色数据吗？" @confirm="deleteBusiness(scope.row.businessId)">
                        <el-button slot="reference" type="text" size="small">删除</el-button>
                    </el-popconfirm>
                </template>
            </el-table-column>
        </el-table>

            <!--分页组件-->
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="current"
            :page-sizes="[5,10,15,20]" :page-size="size" layout="total, sizes, prev, pager, next, jumper"
            :total="total">
        </el-pagination>

        <!--新建商家对话框-->
        <el-dialog title="新建商家" :visible.sync="dialogFormVisible" width="550px">
            <el-form :model="businessForm" ref="businessForm" label-width="150px" :rules="businessRoles">
                <el-form-item label="商家头像" label-width="150px">
                     <!-- 因为上传图片不是使用 axios，所以不会自动添加基础路径 /sys，需要自己添加 -->
                    <!-- 因为上传组件不是使用axios,所以需要手动设置请求头携带token :headers="headers" -->
                    <el-upload class="avatar-uploader" action="/sys/api/upload" 
                     :show-file-list="false" :on-success="handleAvatarSuccess" :headers="headers">
                        <img v-if="businessForm.businessImg" :src="businessForm.businessImg" class="avatar">
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                </el-form-item>
                <el-form-item label="商家姓名" label-width="150px" prop="businessName">
                    <el-input v-model="businessForm.businessName" />
                </el-form-item>
                <el-form-item label="商家地址" label-width="150px" prop="businessAddress">
                    <el-input v-model="businessForm.businessAddress" />
                </el-form-item>
                <el-form-item label="商家描述" label-width="150px" prop="businessExplain">
                    <el-input v-model="businessForm.businessExplain" />
                </el-form-item>
                <el-form-item label="商品分类id" label-width="150px">
                    <el-radio-group v-model="businessForm.categoryId">
                        <el-radio :label="1">美食</el-radio>
                        <el-radio :label="2">早餐</el-radio>
                        <el-radio :label="3">跑腿代购</el-radio>
                        <el-radio :label="4">汉堡披萨</el-radio>
                        <el-radio :label="5">速食简餐</el-radio>
                        <el-radio :label="6">地方小吃</el-radio>
                        <el-radio :label="7">米粉面馆</el-radio>
                        <el-radio :label="8">包子粥铺</el-radio>
                        <el-radio :label="9">炸鸡炸串</el-radio>
                        <el-radio :label="10">甜品饮品</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="起送金额" label-width="150px" prop="starPrice">
                    <el-input v-model="businessForm.starPrice" />
                </el-form-item>
                <el-form-item label="配送费" label-width="150px" prop="deliveryPrice">
                    <el-input v-model="businessForm.deliveryPrice" />
                </el-form-item>
                <el-form-item label="商家状态" label-width="100px">
                    <el-radio-group v-model="businessForm.statu">
                        <el-radio :label="0">禁用</el-radio>
                        <el-radio :label="1">正常</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="submitBusinessForm('businessForm')">保存商家</el-button>
                    <el-button @click="resetBusinessForm('businessForm')">重置</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
</template>
 <script>
 export default{
     data(){
         return{
             //上传组件请求头信息：
             headers: {
                 token: JSON.parse(sessionStorage.getItem('TOKEN'))
             },
            //商家表单规则
            businessRoles:{
                businessName: [ { required: true, message: '请输入商家姓名', trigger: 'blur' }],
                businessAddress: [ { required: true, message: '请输入商家地址', trigger: 'blur' }],
                businessExplain: [ { required: true, message: '请输入商家描述', trigger: 'blur' }],
            },
            //商家表单
            businessForm:{
                businessId:0,
                businessName:'',
                businessAddress:'',
                businessExplain:'',
                businessImg:'',
                categoryId:'',
                starPrice:'',
                deliveryPrice:'',
                statu:1,
            },
            selection:[],//记录使用表格复选框中对应这一行角色数据
            tableData:[],//商家表格数据
            deleteAllBtnStu:true,//批量删除的按钮是否禁用
            //设置新建窗口可视
            dialogFormVisible:false,
            //根据姓名搜索
            searchForm:{
                name:'',
            },
             //分页相关参数
             current: 1,//当前页码，默认是 显示第一页
             size: 5,
             total: 0,
            
         }
     },
     created(){
        this.loadBusinessList();
        
     },
     methods:{
   
        //重置新建商家对话框
        resetBusinessForm(formName){
            this.businessForm={
                businessId:0,
                businessName:'',
                businessAddress:'',
                businessExplain:'',
                businessImg:'',
                categoryId:'',
                starPrice:'',
                deliveryPrice:'',
                statu:1,
            };
        this.$refs[formName].resetFields();
        },

        //提交用户数据
        submitBusinessForm(formName){
            this.$refs[formName].validate((valid) => {
                 if (valid) {
                     //提交服务器端，保存角色的信息
                     this.$axios.post('/business/'+(this.businessForm.businessId!=0?'update':'save'), this.businessForm).then(response => {
                         this.$message({
                             showClose: true,
                             message: response.data.resultdata,
                             type: 'success',
                             duration: 1000,
                         });
                         //添加角色成功，重新加载分页表格数据
                         this.loadBusinessList();
                         //重置角色对话框中的表单
                         this.resetBusinessForm(formName);
                         this.dialogFormVisible = false;
                     }).catch(e => { console.log(e) });
                 } else {
                     return false;
                 }
             })
        },
        //上传用户头像成功后的回调函数
        handleAvatarSuccess(response, file) {
             console.log('头像', response.resultdata);
             //上传成功，从回调的response参数中 获得上传图片在OSS服务器上地址。
             //这个response和axios中使用response对象是不一样的。这个response不需要.data。直接返回的就是 result数据。
             this.businessForm.businessImg = response.resultdata
         },
         //选择一页显示记录数触发的事件方法
         handleSizeChange(val) {
             console.log(`每页 ${val} 条`);
             this.size = val;
             this.loadBusinessList();
         },
         //点击页面改变页码
         handleCurrentChange(val) {
             console.log(`当前页: ${val}`);
             this.current = val;
             this.loadBusinessList();
         },
        //删除商家信息
        deleteBusiness(id){
            var businessIds = [];
            id ? businessIds.push(id) : this.selection.forEach(r=>{businessIds.push(b.businessId)});
            this.$axios.post('/business/delete', businessIds).then(response => {
                 this.$message({
                     showClose: true,
                     message: response.data.resultdata,
                     type: 'success',
                     duration: 1000,
                 });
                 //删除成功，重新加载角色的分页表格数据
                 this.loadBusinessList();
             }).catch(e => { console.log(e); })
        },
        //编辑商家信息
        editBusiness(businessId){
            this.$axios.get('/business/info/' + businessId).then(response => {
                 //接受返回
                 this.businessForm = response.data.resultdata;
                 this.dialogFormVisible = true;
             }).catch(e => { console.log(e); })
             this.resetBusinessForm();
            
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
        //加载搜索商家的方法
        loadBusinessList(){
            this.$axios.get('/business/list',{
                params:{
                    name:this.searchForm.name,//参数，就是模糊查询条件
                    current:this.current,
                    size:this.size
                }
            }).then(response=>{
                this.tableData=response.data.resultdata.records
                this.current=response.data.resultdata.current,
                this.size=response.data.resultdata.size,
                this.total=response.data.resultdata.total
            }).catch(e=>{console.log(e);})
        },
     
        
     }
 }
 </script>
 <style scoped>
     /* <!--上传图片的样式--> */
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
   