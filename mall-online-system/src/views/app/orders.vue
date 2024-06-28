<template>
    <div>
        <!-- 用户管理 工具栏 -->
        <el-form ref="searchForm" :model="searchForm" :inline="true">
            <el-form-item>
                <el-input placeholder="请输入要搜索的订单编号" v-model="searchForm.orderId" clearable />
            </el-form-item>
            <el-form-item>
                <el-button @click="loadOrdersList">搜索</el-button>
                <el-popconfirm title="确认要删除这些订单吗（未支付的订单将被自动取消）?" @confirm="deleteOrders()">
                    <el-button type="danger" slot="reference" :disabled="deleteAllBtnStu">批量删除</el-button>
                </el-popconfirm>
            </el-form-item>
            <!-- <el-form-item>
                <el-select style="margin-top:5px;margin-right:5px" v-model="value" placeholder="请选择"
                    @change="selsectBusiness(value)">
                    <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item> -->
        </el-form>

        <div v-if="hasAuth('sys:orders:all')">
            <!-- 用户数据表格显示 -->
            <el-table :data="tableData" border style="width:100%; margin-bottom:20px;" :cell-style="{ padding: '4px' }"
                @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="50"></el-table-column>
                <el-table-column prop="orderId" label="订单编号" width="158" />
                <el-table-column prop="deliveryaddress.contactName" label="用户名" width="120" />
                <el-table-column prop="deliveryaddress.contactTel" label="联系电话" width="200" />
                <el-table-column prop="business.businessName" label="店铺名称" width="280" />
                <el-table-column prop="orderTotal" label="订单金额" width="200" />
                <el-table-column prop="state" label="订单状态" width="200">
                    <template slot-scope="scope">
                        <el-tag v-if="scope.row.state === 1" type="success" size="mini">已支付</el-tag>
                        <el-tag v-else-if="scope.row.state === 0" type="danger" size="mini">未支付</el-tag>
                    </template>
                </el-table-column>


                <el-table-column fixed="right" label="操作">
                    <template slot-scope="scope">
                        <el-button type="text" size="small"
                            @click="getInfo(scope.row.created, scope.row.orderTotal, scope.row.business, scope.row.deliveryaddress, scope.row.ordersDetailets)">详细</el-button>
                        <el-divider direction="vertical" />
                        <el-popconfirm v-if="scope.row.state === 1" title="确定要删除该订单吗？"
                            @confirm="deleteOrder(scope.row.orderId)">
                            <el-button slot="reference" type="text" size="small">删除订单</el-button>
                        </el-popconfirm>
                        <el-popconfirm v-else-if="scope.row.state === 0" title="确定要取消该订单吗？"
                            @confirm="deleteOrder(scope.row.orderId)">
                            <el-button slot="reference" type="text" size="small">取消订单</el-button>
                        </el-popconfirm>

                    </template>
                </el-table-column>
            </el-table>
        </div>


        <div v-if="hasAuth('sys:orders:one')">
            <!-- 用户数据表格显示 -->
            <el-table :data="tableData" border style="width:100%; margin-bottom:20px;" :cell-style="{ padding: '4px' }"
                @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="50"></el-table-column>
                <el-table-column prop="orderId" label="订单编号" width="158" />
                <el-table-column prop="deliveryaddress.contactName" label="用户名" width="120" />
                <el-table-column prop="deliveryaddress.contactTel" label="联系电话" width="200" />
                <el-table-column prop="business.businessName" label="店铺名称" width="280" />
                <el-table-column prop="orderTotal" label="订单金额" width="200" />
                <el-table-column prop="state" label="订单状态" width="200">
                    <template slot-scope="scope">
                        <el-tag v-if="scope.row.state === 1" type="success" size="mini">已支付</el-tag>
                        <el-tag v-else-if="scope.row.state === 0" type="danger" size="mini">未支付</el-tag>
                    </template>
                </el-table-column>


                <el-table-column fixed="right" label="操作">
                    <template slot-scope="scope">
                        <el-button type="text" size="small"
                            @click="getInfo(scope.row.created, scope.row.orderTotal, scope.row.business, scope.row.deliveryaddress, scope.row.ordersDetailets)">详细</el-button>
                        <el-divider direction="vertical" />
                        <el-popconfirm v-if="scope.row.state === 1" title="确定要删除该订单吗？"
                            @confirm="deleteOrder(scope.row.orderId)">
                            <el-button slot="reference" type="text" size="small">删除订单</el-button>
                        </el-popconfirm>
                        <el-popconfirm v-else-if="scope.row.state === 0" title="确定要取消该订单吗？"
                            @confirm="deleteOrder(scope.row.orderId)">
                            <el-button slot="reference" type="text" size="small">取消订单</el-button>
                        </el-popconfirm>

                    </template>
                </el-table-column>
            </el-table>
        </div>

        <el-dialog :visible.sync="dialogOrdersInfoVisible" width="80%">
            <span style="font-size: 14px;position: relative;bottom: 15px;">下单时间：{{ info.created | fmtDate }}</span>
            <el-descriptions direction="vertical" :column="3" border>
                <el-descriptions-item label="用户名">{{ info.deliveryaddress.contactName }}</el-descriptions-item>
                <el-descriptions-item label="手机号">{{ info.deliveryaddress.contactTel }}</el-descriptions-item>
                <el-descriptions-item label="配送地址">{{ info.deliveryaddress.address }}</el-descriptions-item>
                <el-descriptions-item label="商品详细" :span="2">
                    <template v-for="item in info.infoList">
                        <div class="info">
                            <div class="left">
                                <img :src="item.goods.goodsImg">
                                <div>
                                    <p>{{ item.goods.goodsName }}</p>
                                    <p>x {{ item.quantity }}</p>
                                </div>
                            </div>
                            <p class="price"><span style="margin-right: 4px;">&#165 </span> {{ item.goods.goodsPrice }}
                            </p>
                        </div>
                    </template>
                    <template>
                        <div class="right">
                            <p>配送费：<span>&#165 </span> {{ info.business.deliveryPrice }}</p>
                            <p>总计：<span>&#165 </span> {{ info.orderTotal }}</p>
                        </div>
                    </template>
                </el-descriptions-item>
                <el-descriptions-item label="备注">备注</el-descriptions-item>
            </el-descriptions>
        </el-dialog>

        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="current"
            :page-sizes="[5, 10, 15, 20]" :page-size="size" layout="total, sizes, prev, pager, next, jumper"
            :total="total" style="display:flex;justify-content:center">
        </el-pagination>

    </div>
</template>

<script>
export default {
    data() {
        return {
            tableData: [],   //用户表格显示数组
            deleteAllBtnStu: true, //是否禁用用户的批量删除
            dialogFormVisible: false,  //是否显示新建用户对话框
            dialogRoleFormVisible: false,  //是否显示分配角色对话框
            dialogOrdersInfoVisible: false,
            user: '',
            //搜索工具栏对象
            searchForm: {
                orderId: ''
            },
            //分页相关的参数
            current: 1, //当前页码，默认是显示第一页
            size: 5, //一页显示记录数
            total: 0, //总共的记录数

            OrderForm: {
                orderId: '',
                username: '',
                tel: '',
                businessName: '',
                total: '',
                remark: '',
            },

            info: {
                created: '',
                orderTotal: 0,
                business: {},
                deliveryaddress: {},
                infoList: [],  //储存订单详细信息
            },

            selection: [],  //存储表格中所有复选框 选中用户对象
            options: []
        }
    },
    components: {

    },
    created() {
        this.user = this.$getSessionStorage('user')
        if (this.user.length > 0) {
            this.loadOrdersOne(this.user)
            console.log(1)
        } else {
            this.loadOrdersList()
            console.log(2)
        }

    },

    methods: {
        //加载 分页用户表格数据
        loadOrdersList() {
            this.$axios.get('/orders/list', {
                params: {
                    orderId: this.searchForm.orderId,  //参数，就是模糊查询条件
                    current: this.current,
                    size: this.size,
                }
            }).then(response => {
                this.tableData = response.data.resultdata.records  //分页的表格数据
                this.current = response.data.resultdata.current
                this.size = response.data.resultdata.size
                this.total = response.data.resultdata.total
                if (this.tableData.length === 0 && this.current > 1) {
                    this.current--; // 将当前页码减一
                    // 然后重新获取当前页的数据
                    this.loadOrdersList();
                } else {
                    // 如果当前页还有数据，可以不做任何操作
                }
            }).catch(e => { console.log(e); });
        },
        loadOrdersOne(business) {
            // 提取businessId为一个新的数组
            let businessIds = business.map(item => item.businessId);
            this.options[index] = {
                    value: b.businessId,
                    label: b.businessId
                }
            
            // 将数组转换为查询参数字符串
            let businessIdsStr = businessIds.join(',');
            this.$axios.get('/orders/one', {
                params: {
                    businessIds: businessIdsStr,
                    orderId: this.searchForm.orderId,  //参数，就是模糊查询条件
                    current: this.current,
                    size: this.size,
                }
            }).then(response => {
                this.tableData = response.data.resultdata.records  //分页的表格数据
                this.current = response.data.resultdata.current
                this.size = response.data.resultdata.size
                this.total = response.data.resultdata.total

                if (this.tableData.length === 0 && this.current > 1) {
                    this.current--; // 将当前页码减一
                    // 然后重新获取当前页的数据
                    this.loadOrdersList()
                } else {
                    // 如果当前页还有数据，可以不做任何操作
                }
            }).catch(e => { console.log(e); });
        },
        //用户表格数据的复选框事件方法
        handleSelectionChange(userArr) {
            this.selection = userArr;
            if (this.selection) {
                this.deleteAllBtnStu = (this.selection.length === 0);
            }
        },
        handleSizeChange(val) {
            console.log(`每页 ${val} 条`);
            this.size = val;
            if (this.user.length > 0) {
                this.loadOrdersOne(this.user)
            } else {
                this.loadOrdersList(); //一页显示的记录数改变，重新加载分页和表格数据。
            }

        },
        handleCurrentChange(val) {
            console.log(`当前页: ${val}`);
            this.current = val;
            if (this.user.length > 0) {
                this.loadOrdersOne(this.user)
            } else {
                this.loadOrdersList(); //一页显示的记录数改变，重新加载分页和表格数据。
            }
        },
        getInfo(created, orderTotal, business, deliveryaddress, list) {
            this.info.created = created;
            this.info.infoList = list;
            this.info.deliveryaddress = deliveryaddress;
            this.info.orderTotal = orderTotal;
            this.info.business = business;
            this.dialogOrdersInfoVisible = true;
        },
        deleteOrder(id) {
            var orderIds = [];

            //单个删除 参数id是有值的。而批量删除是从selection数组取删除的用户id
            id ? orderIds.push(id) : this.selection.forEach(u => { orderIds.push(u.id) });

            this.$axios.post('/orders/delete', orderIds).then(response => {
                this.$message({
                    showClose: true,
                    message: response.data.resultdata,
                    type: 'success',
                    duration: 1000,
                });
                console.log(this.tableData.length)
                //删除成功，重新加载角色的分页表格数据
                this.loadOrdersList()
            })
        },
        editOrderMethod(orderId) {

        },
        close() {
            this.dialogRoleFormVisible = false;
        }
    },
}

</script>
<style scoped>
.info {
    display: flex;
    justify-content: space-between;
}

.info img {
    width: 80px;
    height: 80px;
}

.info .left {
    display: flex;
    font-size: 14px;
    margin: 5px;
}

.info .left div {
    margin-left: 30px;
}

.info .price {
    margin-right: 20px;
    font-size: 14.5px;
    display: flex;
    justify-content: center;
    align-items: center;
}

.right {
    margin-left: 8px;
    margin-right: 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.info .price,
.right {
    font-size: 14.5px;
}

.right span,
.price span {
    font-size: 18px;
}

.right span {
    text-align: center;
    position: relative;
    top: 1px;
}
</style>