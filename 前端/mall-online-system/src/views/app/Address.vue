<template>
    <div>
         <!-- 角色管理 工具栏 -->
         <el-form ref="searchForm" :model="searchForm" :inline="true">
             <el-form-item>
                 <el-input placeholder="请输入要搜索的联系人名称" v-model="searchForm.contactName" clearable /> 
             </el-form-item>    
             <el-form-item>
                 <el-button @click="loadComuList()">搜索</el-button>
                 <el-button type="primary" @click="dialogFormVisible=true">新建</el-button> 
                 <el-popconfirm title="确认要删除这些角色吗?" @confirm="deleteDelivery()">
                     <el-button type="danger" slot="reference" :disabled="deleteAllBtnStu">批量删除</el-button>
                 </el-popconfirm>
             </el-form-item>
         </el-form>
 
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
            <template slot-scope="scope">
                {{ scope.row.updated | fmtDate }}
            </template>
        </el-table-column>
             <el-table-column prop="statu" label="角色状态" width="120">
                 <template slot-scope="scope">
                     <el-tag v-if="scope.row.statu===1" type="success" size="mini">正常</el-tag>
                     <el-tag v-else-if="scope.row.statu===0" type="danger" size="mini">禁用</el-tag>
                 </template>    
             </el-table-column>
             <el-table-column fixed="right" label="操作">
                 <template slot-scope="scope">
                     <el-button @click="editDelivery(scope.row.daId)" type="text" size="small">编辑</el-button>
                     <el-divider direction="vertical" />
                     <el-popconfirm title="确定要删除该角色数据吗？" @confirm="deleteDelivery(scope.row.daId)">
                         <el-button slot="reference" type="text" size="small">删除</el-button>
                     </el-popconfirm>
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
 
         <!-- 新建和编辑用户的对话框 -->
    <el-dialog title="编辑用户" :visible.sync="dialogFormVisible" width="550px" @closed="resetDeliveryForm('deliveryForm')">
        <el-form ref="deliveryForm" :model="deliveryForm" label-width="100px" :rules="deliveryRules">
            <el-form-item label="联系人姓名" label-width="100px" prop="contactName">
                <el-input v-model="deliveryForm.contactName" />
            </el-form-item>
            <el-form-item label="性别" label-width="100px" prop="contactSex">
                <el-radio-group v-model="deliveryForm.contactSex">
                    <el-radio :label="0">女</el-radio>
                    <el-radio :label="1">男</el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item label="电话" label-width="100px" prop="contactTel">
                <el-input v-model="deliveryForm.contactTel" />
            </el-form-item>
            <el-form-item label="地址" label-width="100px" prop="address">
                <el-input v-model="deliveryForm.address" />
            </el-form-item>
            <el-form-item label="用户状态" label-width="100px" prop="statu">
                <el-radio-group v-model="deliveryForm.statu">
                    <el-radio :label="0">禁用</el-radio>
                    <el-radio :label="1">正常</el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="submitDeliveryForm('deliveryForm')">保存用户</el-button>
                <el-button @click="resetDeliveryForm('deliveryForm')">重置</el-button>
            </el-form-item>
        </el-form>
    </el-dialog>
 
         
    </div>
 </template>
  <script>
  export default{
      data(){
          return{
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
 
             selection:[],  //记录使用表格复选框选中角色数据
          }
      },
      created(){
         this.loadComuList();
      },
      methods:{
 
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
             this.loadComuList();  //当前页面改变，重新加载页面的数据。
         },
 
         //选择一页显示记录数触发的事件方法，该方法中编写 重新加载分页和表格数据
         //val就是选择 一页要显示的记录数。
         handleSizeChange(val){
             console.log(`每页 ${val} 条`);
             this.size = val;
             this.loadComuList(); //一页显示的记录数改变，重新加载分页和表格数据。
         },
         //加载表格分页数据
         loadComuList(){
             this.$axios.get('/delivery/list',{
                 params:{
                     contactName:this.searchForm.contactName,  //参数，就是模糊查询条件
                     current:this.current,
                     size:this.size,
                 }
             }).then(response=>{
                console.log(this.searchForm.contactName)
                console.log(response.data.resultdata)
                 this.tableData = response.data.resultdata.records  //分页的表格数据
                 this.current = response.data.resultdata.current 
                 this.size = response.data.resultdata.size
                 this.total = response.data.resultdata.total
             }).catch(e=>{ console.log(e); });
         },
         resetDeliveryForm(formName){
            this.deliveryForm={
                daId:0,     
                contactName:'', 
                contactTel:'',
                accountId:'',  
                address:'',    
                contactSex:null,
                statu:null,  
            };
            this.$refs[formName].resetFields();
        },
        //提交新建用户或 编辑用户的表单
        submitDeliveryForm(formName){
            this.$refs[formName].validate((valid)=>{
                if(valid){
                    this.$axios.post('/delivery/'+(this.deliveryForm.daId!=0?'update':'save'),this.deliveryForm).then(response=>{
                        this.$message({
                            showClose: true,
                            message: response.data.resultdata,
                            type: 'success',
                            duration:1000,
                        });
                        //添加用户成功，重新加载分页表格数据
                        this.loadComuList();
                        //重置角色对话框中的表单
                        this.resetDeliveryForm(formName);
                        this.dialogFormVisible=false;
                    }).catch(e=>{ console.log(e)});
                }else{
                    return false;
                }
            })
        },
        editDelivery(id){
            console.log(id)
            this.$axios.get('/delivery/info/'+id).then(response=>{
                let delivery = response.data.resultdata;
                this.deliveryForm = delivery;
                this.dialogFormVisible=true;
            })
        },
        deleteDelivery(id){
            var deliveryIds =[];  //存储所有要删除的角色id 
            console.log(id+111)
            //单个删除 参数id是有值的。而批量删除是从selection数组取删除的角色id
            id?deliveryIds.push(id):this.selection.forEach( r=>{ deliveryIds.push(r.daId) } );

            this.$axios.post('/delivery/delete',deliveryIds).then(response=>{
                this.$message({
                    showClose: true,
                    message: response.data.resultdata,
                    type: 'success',
                    duration:1000,
                });
                //删除成功，重新加载角色的分页表格数据
                this.loadComuList();
            }).catch(e=>{ console.log(e); })
        },
      }
  }
  </script>
  <style scoped>
      
  </style>
    