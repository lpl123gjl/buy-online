<template>
  <div>
    <!--类别管理  -->
    <el-form ref="searchForm" :model="searchForm" :inline="true">
      <el-form-item>
        <el-input placeholder="请输入要搜索的分类名称" v-model="searchForm.name" clearable />
      </el-form-item>
      <el-form-item>
        <el-button @click="loadCategoryList()">搜索</el-button>
        <el-button type="primary" @click="dialogFormVisible=true">新建</el-button>
        <el-popconfirm title="确认要删除这些分类吗" @confirm="deleteCategory()">
          <el-button type="danger" slot="reference" :disabled="deleteAllBtnStu">批量删除</el-button>
        </el-popconfirm>
      </el-form-item>
    </el-form>
    <!--类别表格-->
    <el-table :data="tableData" border style="width:100%; margin-bottom:20px;" :cell-style="{padding:'4px'}"
      @selection-change="handleSelectionChange" stripe>
      <el-table-column type="selection" width="50"></el-table-column>
      <el-table-column label="头像" width="60">
        <template slot-scope="scope">
          <el-avatar :src="scope.row.categoryCover"></el-avatar>
        </template>
      </el-table-column>
      <el-table-column prop="categoryName" label="分类名称" width="200"></el-table-column>
      <el-table-column prop="statu" label="分类状态" width="120">
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.statu === 1" size="mini">正常</el-tag>
          <el-tag type="danger" v-if="scope.row.statu === 0" size="mini">禁用</el-tag>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作">
                <template slot-scope="scope">
                    <el-button @click="editCategory(scope.row.categoryId)" type="text" size="small">编辑</el-button>
                    <el-divider direction="vertical" />
                    <el-popconfirm title="确定要删除角色数据吗？" @confirm="deleteCategory(scope.row.categoryId)">
                        <el-button slot="reference" type="text" size="small">删除</el-button>
                    </el-popconfirm>
                </template>
            </el-table-column>
    </el-table>

    <!--分页组件-->
    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="current"
      :page-sizes="[5,10,15,20]" :page-size="size" layout="total, sizes, prev, pager, next, jumper" :total="total">
    </el-pagination>

    <!--新建分类对话框-->
    <el-dialog title="新建分类" :visible.sync="dialogFormVisible" width="550px">
      <el-form :model="categoryForm" ref="categoryForm" label-width="150px" :rules="categoryRoles">
        <el-upload class="avatar-uploader" action="/sys/api/upload" :show-file-list="false"
          :on-success="handleAvatarSuccess" :headers="headers">
          <img v-if="categoryForm.categoryCover" :src="categoryForm.categoryCover" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
        <el-form-item label="分类名称" label-width="150px" prop="categoryName">
          <el-input v-model="categoryForm.categoryName" />
        </el-form-item>
        <el-form-item label="分类状态" label-width="100px">
                    <el-radio-group v-model="categoryForm.statu">
                        <el-radio :label="0">禁用</el-radio>
                        <el-radio :label="1">正常</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="submitCategoryForm('categoryForm')">保存商家</el-button>
                    <el-button @click="resetCategoryForm('categoryForm')">重置</el-button>
                </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data () {
    return {
       //分页相关参数
       current: 1,//当前页码，默认是 显示第一页
             size: 5,
             total: 0,
      selection:[],//记录使用表格复选框中对应这一行角色数据
            tableData:[],//商家表格数据
            deleteAllBtnStu:true,//批量删除的按钮是否禁用
            //设置新建窗口可视
            dialogFormVisible:false,
      categoryForm:{
        categoryId:0,
        categoryName:'',
        categoryCover:'',
        statu:1,
      },
      categoryRoles:{
        category:[ { required: true, message: '请输入分类姓名', trigger: 'blur' }],
      },
      //上传组件请求头信息：
      headers: {
                 token: JSON.parse(sessionStorage.getItem('TOKEN'))
             },
       //根据姓名搜索
       searchForm:{
                name:'',
            },

    }
  },
  components: {

  },
  created(){
        this.loadCategoryList();
        
     },
  methods: {
    //加载搜索商家的方法
    loadCategoryList(){
            this.$axios.get('/category/list',{
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
     //编辑商家信息
     editCategory(categoryId){
            this.$axios.get('/category/info/' + categoryId).then(response => {
                 //接受返回
                 this.categoryForm = response.data.resultdata;
                 this.dialogFormVisible = true;
             }).catch(e => { console.log(e); })
             this.resetCategoryForm();
            
        },
     //删除商家信息
     deleteCategory(id){
            var categoryIds = [];
            id ? categoryIds.push(id) : this.selection.forEach(r=>{categoryIds.push(c.categoryId)});
            this.$axios.post('/category/delete', categoryIds).then(response => {
                 this.$message({
                     showClose: true,
                     message: response.data.resultdata,
                     type: 'success',
                     duration: 1000,
                 });
                 //删除成功，重新加载角色的分页表格数据
                 this.loadCategoryList();
             }).catch(e => { console.log(e); })
        },
     //点击页面改变页码
     handleCurrentChange(val) {
             console.log(`当前页: ${val}`);
             this.current = val;
             this.loadCategoryList();
         },
    //选择一页显示记录数触发的事件方法
    handleSizeChange(val) {
             console.log(`每页 ${val} 条`);
             this.size = val;
             this.loadCategoryList();
         },
      //上传用户头像成功后的回调函数
      handleAvatarSuccess(response, file) {
             console.log('头像', response.resultdata);
             //上传成功，从回调的response参数中 获得上传图片在OSS服务器上地址。
             //这个response和axios中使用response对象是不一样的。这个response不需要.data。直接返回的就是 result数据。
             this.categoryForm.categoryCover = response.resultdata
         },
    //提交用户数据
    submitCategoryForm(formName){
            this.$refs[formName].validate((valid) => {
                 if (valid) {
                     //提交服务器端，保存角色的信息
                     this.$axios.post('/category/'+(this.categoryForm.categoryId!=0?'update':'save'), this.categoryForm).then(response => {
                         this.$message({
                             showClose: true,
                             message: response.data.resultdata,
                             type: 'success',
                             duration: 1000,
                         });
                         //添加角色成功，重新加载分页表格数据
                         this.loadCategoryList();
                         //重置角色对话框中的表单
                         this.resetCategoryForm(formName);
                         this.dialogFormVisible = false;
                     }).catch(e => { console.log(e) });
                 } else {
                     return false;
                 }
             })
        },
    //重置新建商家对话框
    resetCategoryForm(formName){
      this.categoryForm={
        categoryId:0,
        categoryName:'',
        categoryCover:'',
        statu:1,
      };
        this.$refs[formName].resetFields();
        },
  },
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