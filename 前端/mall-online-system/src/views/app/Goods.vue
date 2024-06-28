<template>
    <div>
        <!-- 商品管理 工具栏 -->
        <el-form ref="searchForm" :model="searchForm" :inline="true">
            <el-form-item>
                <!-- 输入框，用于输入要搜索的商品名称 -->
                <el-input placeholder="请输入要搜索的商品名称" v-model="searchForm.name" clearable />
            </el-form-item>
            <el-form-item>
                <!-- 搜索按钮，点击后调用 loadGoodsList 方法 -->
                <el-button @click="loadGoodsList()">搜索</el-button>
                <!-- 新建按钮，点击后显示对话框 -->
                <el-button type="primary" @click="dialogFormVisible = true">新建</el-button>
                <!-- 批量删除按钮，点击后调用 deleteGoods 方法 -->
                <el-popconfirm title="确认要删除这些商品吗?" @confirm="deleteGoods()">
                    <el-button type="danger" slot="reference" :disabled="deleteAllBtnStu">批量删除</el-button>
                </el-popconfirm>
            </el-form-item>
        </el-form>

        <!-- 商品数据表格显示 -->
        <el-table :data="tableData" border style="width:100%; margin-bottom:20px;" :cell-style="{ padding: '4px' }"
            @selection-change="handleSelectionChange">
            <!-- 选择框列 -->
            <el-table-column type="selection" width="50"></el-table-column>
            <!-- 商品图片列 -->
            <el-table-column prop="goodsImg" label="图片" width="58">
                <template slot-scope="scope">
                    <el-image :src="scope.row.goodsImg" :preview-src-list="[scope.row.goodsImg]"
                        :style="{ width: '40px', height: '40px' }"></el-image>
                </template>
            </el-table-column>
            <!-- 商品名称列 -->
            <el-table-column prop="goodsName" label="商品名称" width="120" />
            <!-- 商品价格列 -->
            <el-table-column prop="goodsPrice" label="价格" width="80" />
            <!-- 所属商家列 -->
            <el-table-column label="所属商家" width="150">
                <template slot-scope="scope">
                    <!-- 根据商家编号获取商家名称 -->
                    {{ getBusinessName(scope.row.businessId) }}
                </template>
            </el-table-column>
            <!-- 商品介绍列 -->
            <el-table-column prop="goodsExplain" label="商品介绍" width="250" />
            <!-- 备注列 -->
            <el-table-column prop="remarks" label="备注" width="140" />
            <!-- 创建时间列，使用自定义过滤器 fmtDate 格式化日期 -->
            <el-table-column label="创建时间">
                <template slot-scope="scope">
                    {{ scope.row.created | fmtDate }}
                </template>
            </el-table-column>
            <!-- 操作列，包含编辑和删除按钮 -->
            <el-table-column fixed="right" label="操作" width="180">
                <template slot-scope="scope">
                    <el-button type="text" size="small" @click="editGoods(scope.row.goodsId)">编辑</el-button>
                    <el-divider direction="vertical" />
                    <el-popconfirm title="确定要删除该商品数据吗？" @confirm="deleteGoods(scope.row.goodsId)">
                        <el-button slot="reference" type="text" size="small">删除</el-button>
                    </el-popconfirm>
                </template>
            </el-table-column>
        </el-table>
        <!-- 分页组件 -->
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="current"
            :page-sizes="[5, 10, 15, 20]" :page-size="size" layout="total, sizes, prev, pager, next, jumper"
            :total="total">
        </el-pagination>

        <!-- 新建和编辑商品的对话框 -->
        <el-dialog title="编辑商品" :visible.sync="dialogFormVisible" width="550px" @closed="resetGoodsForm('goodsForm')">
            <el-form ref="goodsForm" :model="goodsForm" label-width="100px" :rules="goodsRules">
                <!-- 商品图片上传 -->
                <el-form-item label="商品图片" label-width="100px">
                    <!-- 因为上传图片不是使用 axios，所以不会自动添加基础路径 /sys，需要自己添加 -->
                    <!-- 因为上传组件不是使用axios,所以需要手动设置请求头携带token :headers="headers" -->
                    <el-upload class="avatar-uploader" action="/sys/api/upload" :show-file-list="false"
                        :on-success="handleAvatarSuccess" :headers="headers">
                        <img v-if="goodsForm.goodsImg" :src="goodsForm.goodsImg" class="avatar">
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                </el-form-item>
                <!-- 商品名称输入框 -->
                <el-form-item label="商品名称" label-width="100px" prop="goodsName">
                    <el-input v-model="goodsForm.goodsName" />
                </el-form-item>
                <!-- 商品介绍输入框 -->
                <el-form-item label="商品介绍" label-width="100px" prop="goodsExplain">
                    <el-input v-model="goodsForm.goodsExplain" />
                </el-form-item>
                <!-- 商品价格输入框 -->
                <el-form-item label="价格" label-width="100px" prop="goodsPrice">
                    <el-input v-model="goodsForm.goodsPrice" />
                </el-form-item>
                <!-- 所属商家选择框 -->
                <el-form-item label="所属商家" label-width="100px" prop="businessId">
                    <el-select v-model="goodsForm.businessId" placeholder="请选择商家">
                        <el-option v-for="item in businessList" :key="item.businessId" :label="item.businessName"
                            :value="item.businessId">
                        </el-option>
                    </el-select>
                </el-form-item>
                <!-- 备注输入框 -->
                <el-form-item label="备注" label-width="100px" prop="remarks">
                    <el-input v-model="goodsForm.remarks" />
                </el-form-item>
                <!-- 保存和重置按钮 -->
                <el-form-item>
                    <el-button type="primary" @click="submitGoodsForm('goodsForm')">保存商品</el-button>
                    <el-button @click="resetGoodsForm('goodsForm')">重置</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
</template>

<script>
export default {
    data() {
        return {
            headers: {
                token: JSON.parse(sessionStorage.getItem('TOKEN')) // 从 sessionStorage 中获取 token
            },
            goodsRules: {
                goodsName: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
                goodsPrice: [{ required: true, message: '请输入商品价格', trigger: 'blur' }],
                businessId: [{ required: true, message: '请选择商家', trigger: 'change' }],
            },
            goodsForm: {
                goodsId: 0,
                goodsImg: '',
                goodsName: '',
                goodsExplain: '',
                goodsPrice: '',
                businessId: null,
                remarks: '',
            },
            tableData: [], 
            // 批量删除按钮状态
            deleteAllBtnStu: true, 
            // 对话框可见性
            dialogFormVisible: false, 
            searchForm: {
                name: '' // 搜索中的商品名称
            },
            current: 1, // 当前页码
            size: 5, // 每页显示的条数
            total: 0, // 总条数
            selection: [], // 选中的商品
            businessList: [],  // 商家列表，
        };
    },

    created() {
        // 组件创建时加载商品列表和商家列表
        this.loadGoodsList();
        this.loadBusinessList();
    },
    methods: {
        // 加载商家列表
        loadBusinessList() {
            this.$axios.get('/business/getlist')
                .then(response => {
                    console.log('商家列表响应:', response.data.resultdata); 
                    this.businessList = response.data.resultdata;
                })
                .catch(e => {
                    console.error("加载商家列表失败:", e);
                });
        },
        // 根据商家编号获取商家名称
        getBusinessName(businessId) {
            console.log('查询的商家编号:', businessId);
            console.log('当前商家列表:', this.businessList);
            const business = this.businessList.find(b => b.businessId === businessId);
            // 打印找到的商家信息以调试
            console.log('找到的商家:', business);
            return business ? business.businessName : '未找到';
        },
        // 加载商品列表
        loadGoodsList() {
            this.$axios.get('/goods/list', {
                params: {
                    name: this.searchForm.name, 
                    current: this.current, 
                    size: this.size, 
                }
            }).then(response => {
                console.log('商品列表响应:', response.data.resultdata);
                this.tableData = response.data.resultdata.records; 
                this.current = response.data.resultdata.current; 
                this.size = response.data.resultdata.size; 
                this.total = response.data.resultdata.total; 
            }).catch(e => {
                console.log(e);
            });
        },
        // 处理每页显示条数变化
        handleSizeChange(val) {
            // 更新每页显示的条数
            this.size = val; 
            // 重新加载商品列表
            this.loadGoodsList(); 
        },
        // 处理当前页码变化
        handleCurrentChange(val) {
            this.current = val; 
            this.loadGoodsList(); 
        },
        // 处理选中商品变化
        handleSelectionChange(goodsArr) {
            // 更新选中的商品
            this.selection = goodsArr; 
            // 更新批量删除按钮状态
            this.deleteAllBtnStu = (this.selection.length === 0); 
        },
        // 重置商品表单
        resetGoodsForm(formName) {
            this.$refs[formName].resetFields(); // 重置表单字段
            this.goodsForm = {
                goodsId: 0,
                goodsImg: '',
                goodsName: '',
                goodsExplain: '',
                goodsPrice: '',
                businessId: null,
                remarks: '',
            };
        },
        // 提交商品表单
        submitGoodsForm(formName) {
            this.$refs[formName].validate(valid => {
                if (valid) {
                    this.$axios.post('/goods/' + (this.goodsForm.goodsId ? 'update' : 'save'), this.goodsForm).then(response => {
                        this.$message({
                            showClose: true,
                            message: response.data.resultdata,
                            type: 'success',
                            duration: 1000,
                        });
                        this.loadGoodsList(); // 重新加载商品列表
                        this.resetGoodsForm(formName); // 重置商品表单
                        this.dialogFormVisible = false; // 关闭对话框
                    }).catch(e => {
                        console.log(e);
                    });
                } else {
                    return false;
                }
            });
        },
        // 处理图片上传成功
        handleAvatarSuccess(response, file) {
            console.log('头像', response.resultdata);
            // 上传成功，从回调的response参数中 获得上传图片在OSS服务器上地址。
            // 这个response和axios中使用response对象是不一样的。这个response不需要.data。直接返回的就是 result数据。
            this.goodsForm.goodsImg = response.resultdata;
            this.$forceUpdate();
        },
        // 编辑商品
        editGoods(id) {
            this.$axios.get('/goods/info/' + id).then(response => {
                this.goodsForm = response.data.resultdata; 
                this.dialogFormVisible = true; 
            }).catch(e => {
                console.log(e);
            });
        },
        // 删除商品
        deleteGoods(id) {
            // 获取要删除的商品ID
            const goodsIds = id ? [id] : this.selection.map(g => g.goodsId); 
            this.$axios.post('/goods/delete', goodsIds).then(response => {
                this.$message({
                    showClose: true,
                    message: response.data.resultdata,
                    type: 'success',
                    duration: 1000,
                });
                // 重新加载商品列表
                this.loadGoodsList(); 
            }).catch(e => {
                console.log(e);
            });
        },
    }
};
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
