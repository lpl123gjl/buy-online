<template>
  <div>
    <!-- 评论管理 工具栏 -->
    <el-form ref="searchForm" :model="searchForm" :inline="true">
      <el-form-item>
        <!-- 输入框，用于输入要搜索的评论详情 -->
        <el-input placeholder="请输入要搜索的评论详情" v-model="searchForm.details" clearable />
      </el-form-item>
      <el-form-item>
        <!-- 搜索按钮，点击后调用 loadCommentList 方法 -->
        <el-button @click="loadCommentList()">搜索</el-button>
      </el-form-item>
    </el-form>

    <!-- 评论数据表格显示 -->
    <el-table :data="tableData" border style="width:100%; margin-bottom:20px;" :cell-style="{ padding: '4px' }">
      <el-table-column prop="accountName" label="用户名" width="100" />
      <!-- 评论详情列 -->
      <el-table-column prop="details" label="评论详情" width="250"/>
      <!-- 商家名称列 -->
      <el-table-column prop="businessName" label="商家名称" width="180" />
      <!-- 商品名称列 -->
      <el-table-column prop="goodsName" label="商品名称" width="180" />
      <el-table-column label="评分" width="150">
        <template slot-scope="scope">
          <el-rate v-model="scope.row.score" disabled></el-rate>
        </template>
      </el-table-column>
      <!-- fmtDate 格式化日期 -->
      <el-table-column label="创建时间">
        <template slot-scope="scope">
          {{ scope.row.created | fmtDate }}
        </template>
      </el-table-column>
      
      <el-table-column fixed="right" label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="viewComment(scope.row.commId)">查看详情</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="current"
      :page-sizes="[5, 10, 15, 20]" :page-size="size" layout="total, sizes, prev, pager, next, jumper" :total="total">
    </el-pagination>

    <!-- 查看评论的抽屉 -->
    <el-drawer title="查看评论" :visible.sync="drawerVisible" size="50%" :with-header="true" @closed="resetCommentForm('commentForm')">
      <el-form ref="commentForm" :model="commentForm" label-width="100px">
        
        <el-form-item label="用户名" label-width="100px">
          <el-input v-model="commentForm.accountName" disabled />
        </el-form-item>
        
        <el-form-item label="评分" label-width="100px">
          <el-rate v-model="commentForm.score" style="padding-top: 10px;" disabled ></el-rate>
        </el-form-item>
        
        <el-form-item label="评论详情" label-width="100px">
          <el-input v-model="commentForm.details" type="textarea" disabled />
        </el-form-item>
        
        <el-form-item label="商家名称" label-width="100px">
          <el-input v-model="commentForm.businessName" disabled />
        </el-form-item>
        
        <el-form-item label="商品名称" label-width="100px">
          <el-input v-model="commentForm.goodsName" disabled />
        </el-form-item>
        
        <el-form-item label="评论图片" label-width="100px">
          <el-image :src="commentForm.commImage" style="width: 100px; height: 100px" />
        </el-form-item>
        
        <el-form-item style="text-align: center;">
          <el-button type="primary" @click="drawerVisible = false">关闭</el-button>
        </el-form-item>
      </el-form>
    </el-drawer>
  </div>
</template>

<script>
export default {
  data() {
    return {
      searchForm: {
        details: '', // 搜索表单中的评论详情
      },
      tableData: [], // 表格数据
      current: 1, // 当前页码
      size: 5, // 每页显示的条数
      total: 0, // 总条数
      drawerVisible: false, // 抽屉的可见性
      commentForm: {
        accountId: '', 
        accountName: '', 
        score: null, 
        details: '', 
        businessId: null, 
        businessName: '', 
        goodsId: null, 
        goodsName: '', 
        commImage: '', 
      },
    };
  },
  created() {
    this.loadCommentList();
  },
  methods: {
    // 加载评论列表
    loadCommentList() {
      this.$axios.get('/comment/list', {
        params: {
          details: this.searchForm.details, // 搜索表单中的评论详情
          current: this.current, // 当前页码
          size: this.size, // 每页显示的条数
        },
      }).then(response => {
        const data = response.data.resultdata;
        this.tableData = data.records; 
        this.current = data.current; 
        this.size = data.size; 
        this.total = data.total; 
      });
    },
    // 查看评论详情
    viewComment(id) {
      this.$axios.get(`/comment/info/${id}`).then(response => {
        this.commentForm = response.data.resultdata; 
        this.drawerVisible = true; // 显示抽屉
      });
    },
    // 处理每页显示条数变化
    handleSizeChange(size) {
      this.size = size; 
      // 重新加载评论列表
      this.loadCommentList(); 
    },
    // 处理当前页码变化
    handleCurrentChange(current) {
      this.current = current; 
      this.loadCommentList(); 
    },
    // 重置评论表单
    resetCommentForm() {
      this.commentForm = {
        accountId: '',
        accountName: '',
        score: null,
        details: '',
        businessId: null,
        businessName: '',
        goodsId: null,
        goodsName: '',
        commImage: '',
      };
    },
  },
};
</script>

<style scoped>
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
