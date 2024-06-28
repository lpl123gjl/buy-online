<template>
  <div class="page" style="margin: 0 15px;">
    <el-container>
      <el-aside width="450px" style="overflow: hidden;margin-right: 20px;">
        <el-row :gutter="10">
          <el-col :span="24">
            <div class="grid-content bg-purple top">
              <div style="display: flex;align-items: center;height: 100%;">
                <img :src="user.avatar">
                <div class="userInfo" style="display: flex;flex-direction: column;">
                  <div><span style="margin-right: 70px;">用户名：</span><span>{{ user.username }}</span></div>
                  <div><span style="margin-right: 70px;">邮箱：</span><span>{{ user.email }}</span></div>
                  <div><span style="margin-right: 70px;">联系电话：</span><span>{{ user.tel }}</span></div>
                  <div><span style="margin-right: 70px;">绑定商家：</span><span>商家</span></div>
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </el-aside>
      <el-main>
        <el-row :gutter="10">
          <el-col :span="12">
            <div class="grid-content bg-purple content right">
              <div>
                <span class="num"><strong>{{ businessCount }}</strong></span>
                <span class="text">已入驻商家数</span>
              </div>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="grid-content bg-purple content right">
              <div>
                <span class="num"><strong>{{ accountCount }}</strong></span>
                <span class="text">平台用户数</span>
              </div>
            </div>
          </el-col>

        </el-row>
        <el-row :gutter="10">
          <el-col :span="12">
            <div class="grid-content bg-purple content right">
              <div>
                <span class="num"><strong>{{ userCount }}</strong></span>
                <span class="text">管理员数</span>
              </div>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="grid-content bg-purple content right">
              <div>
                <span class="num"><strong>{{ allOrdersCount }}</strong></span>
                <span class="text">平台成交订单数</span>
              </div>
            </div>
          </el-col>

        </el-row>

      </el-main>
    </el-container>




    <el-row :gutter="20">
      <el-col :span="24">
        <div class="grid-content bg-purple bottom" v-if="hasAuth('sys:orders:all')">
          <div class="select">
            <span style="font-size: 20px;margin-right: 200px;"><strong>商家热度排行榜</strong></span>
            <el-radio-group v-model="timeRange" style="margin-bottom: 30px;">
            <el-radio-button label="ONE_YEAR">一年内</el-radio-button>
            <el-radio-button label="ONE_MONTH">本月</el-radio-button>
          </el-radio-group>
          <el-select v-model="value" placeholder="请选择" @change="getRankList(value)">
            <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
          </div>
          <div style="padding:20px 30px;box-sizing:border-box">
            <el-table :data="tableData" style="width: 100%;" ref="table">
              <el-table-column prop="rank" label="排行" width="100" align="center">
              </el-table-column>
              <el-table-column prop="created" label="入驻日期" width="250" align="center" :formatter="formatDate">
              </el-table-column>
              <el-table-column prop="businessName" label="商家名称" width="250" align="center">
              </el-table-column>
              <el-table-column prop="businessExplain" label="商家描述" width="200" align="center">
              </el-table-column>
              <el-table-column prop="ordersCount" label="成交订单数(单)" align="center" v-if="isSelect">
              </el-table-column>
              <el-table-column prop="sales" label="成交额(元)" align="center" v-if="!isSelect">
              </el-table-column>
            </el-table>
          </div>

        </div>
        <div class="grid-content bg-purple bottom" v-if="hasAuth('sys:orders:one')">
          <div class="select">
            <span style="font-size: 20px;margin-right: 380px;">
              <strong>商品热度排行榜</strong></span>
              <span style="margin-right:10px;margin-top:5px">商家编号</span>
          <el-select style="margin-top:5px;margin-right:5px" v-model="value2" placeholder="请选择" @change="getRankGoods(value2)">
            <el-option v-for="item in options" :key="item.value2" :label="item.label" :value="item.value2">
            </el-option>
          </el-select>
          </div>
          <div style="padding:20px 30px;box-sizing:border-box">
            <el-table :data="tableData" style="width: 100%;" ref="table">
              <el-table-column prop="rank" label="排行" width="100" align="center">
              </el-table-column>
              <el-table-column prop="goodsId" label="商品编号" width="250" align="center">
              </el-table-column>
              <el-table-column prop="goodsName" label="商品名称" width="250" align="center">
              </el-table-column>
              <el-table-column prop="quantity" label="商品销量(份)" width="200" align="center" sortable>
              </el-table-column>
              <el-table-column prop="sales" label="销售额(元)" align="center" sortable>
              </el-table-column>
            </el-table>
          </div>

        </div>
      </el-col>

    </el-row>
  </div>
</template>

<script>
export default {
  data() {
    return {
      user: {
        username: '',
        avatar: '',
        email: '',
        tel: ''
      },
      businessIds:[],
      businessCount: '',
      userCount: '',
      accountCount: '',
      allOrdersCount: '',
      tableData: [],
      value: 'count',
      value2:'',
      isSelect: true,
      timeRange: 'ONE_MONTH',
      options: [
        {
          value:'count',
          label:'销售量(单)'
        },
        {
          value:'sales',
          label:'销售额(元)'
        }
      ],
      isRank:false
    }
  },
  watch: {
    timeRange(newValue) {
      this.getRankList(this.value, newValue)
      setTimeout(() => {
        this.$refs.table.doLayout(); // 手动重新渲染table
      }, 0);
    },
  },
  created() {
    this.getUserInfo();
    if(this.businessIds.length==0){
      this.getRankList(this.value, this.timeRange); 
    }
    this.getBusinessCount();
    this.getAccountCount();
    this.getUserCount();
    this.getAllOrdersCount();
    
    
  },
  methods: {
    getUserInfo() {
      //请求服务器获得
      this.$axios.get('/api/userinfo').then(response => {
        this.user = response.data.resultdata
        if(response.data.resultdata.business.length>0){
          this.businessIds = response.data.resultdata.business
        this.value2 = this.businessIds[0].businessId;
        this.getRankGoods(this.value2);
        this.businessIds.forEach((b,index)=>{
        this.options[index] = {
          value2:b.businessId,
          label:b.businessId
        }
      })
        }
          
      }).catch(e => {
        console.log(e);
      })
    },
    getBusinessCount() {
      this.$axios.get('/business/count').then(response => {
        this.businessCount = response.data.resultdata
      }).catch(e => {
        console.log(e);
      })
    },
    getUserCount() {
      this.$axios.get('/user/count').then(response => {
        this.userCount = response.data.resultdata
      }).catch(e => {
        console.log(e);
      })
    },
    getAccountCount() {
      this.$axios.get('/account/count').then(response => {
        this.accountCount = response.data.resultdata
      }).catch(e => {
        console.log(e);
      })
    },
    getAllOrdersCount() {
      this.$axios.get('/orders/allcount').then(response => {
        this.allOrdersCount = response.data.resultdata
      }).catch(e => {
        console.log(e);
      })
    },
    getRankGoods(id){
 
      this.$axios.get('/orders/rank/' + id).then(response => {
        this.tableData = response.data.resultdata.map((item, index) => {
          return {
            goodsId: item.goodsId,
            goodsName: item.goodsName,
            quantity: item.quantity,
            sales: item.sales,
            rank: index + 1
          }
        })
        this.$forceUpdate();
      }).catch(e => {
        console.log(e);
      })
    },
    getRankList(val, timeRange) {
      if (val == "count") {
        this.isSelect = true
      }
      if (val == "sales") {
        this.isSelect = false
      }
      this.$axios.get('/orders/' + val + '/' + timeRange).then(response => {
        this.tableData = response.data.resultdata.map((item, index) => {
          return {
            created: item.created,
            businessName: item.businessName,
            businessExplain: item.businessExplain,
            ordersCount: item.ordersCount,
            sales: item.sales,
            rank: index + 1
          }
        })
        this.$forceUpdate();
      }).catch(e => {
        console.log(e);
      })
    },
    formatDate(row, column) {
      return new Date(row.created).toLocaleDateString();
    }
  },
}

</script>
<style scoped>
.page {
  height: 100%
}

.el-row {
  &:last-child {
    margin-bottom: 0;
  }
}

.el-col {
  border-radius: 4px;
}

.bg-purple-dark {
  background: #ffffff;
}

.bg-purple {
  background: #ffffff;
  margin: 5px 4px;
}

.bg-purple-light {
  background: #ffffff;
}

.grid-content {
  border-radius: 4px;
  min-height: 36px;
  box-shadow: 0 0 6px 0 #e0e0e0;
}

.row-bg {
  padding: 10px 0;
  background-color: #f9fafc;
}

.top {
  height: 160px;
  background-color: #ffffff;
}

.content {
  height: 70px;
  margin-bottom: 15px;
}

.el-main {
  padding: 0;
  overflow: hidden;
}

.top img {
  width: 80px;
  height: 80px;
  border-radius: 10px;
  margin: 20px;
}

.userInfo {
  font-size: 16px;
  margin-left: 15px;
}

.userInfo div {
  display: flex;
  justify-content: space-between;
  margin-bottom: 5px;
}

.bottom {
  height: 600px
}

.select{
  float: right;
  display: flex;align-items: center;
}
.el-select {
  margin-right: 10px;
}

.right {
  background-color: #6cb5ff
}

.right div {
  display: flex;
  flex-direction: column;
  padding: 10px;
  box-sizing: border-box;
  margin-left: 5px;
}

.right div .num {
  font-size: 20px;
  margin-bottom: 2px;
  color: #ffffff;
}

.right div .text {
  color: #f9f9f9;
}
.el-radio-group{
  margin-top: 30px;
  margin-right: 10px;
}
</style>