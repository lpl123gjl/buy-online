import VueRouter from 'vue-router'
/*导入的页面*/
import Login from '@/views/Login.vue'
import Home from '@/views/Home.vue'
import BusinessList from '@/views/system/BusinessList.vue'
import BusinessDetail from '@/views/system/businessDetail.vue'
import RedBag from '@/views/system/RedBag' 
import My from '@/views/my.vue'
import ToMoney from '@/views/system/toMoney.vue'
import UpMoney from '@/views/system/upMoney.vue'
import BuyCar from '@/views/buycar.vue'
import ChangeName from '@/views/system/change-name'
import ConfirmOrder from '@/views/system/confirm-order'
import OnlinePay from '@/views/system/online-pay.vue'
import ProductDetails from '@/views/system/product-details.vue'
import ChangePassword from '@/views/system/change-password'
import MyselfInformation from '@/views/system/myself-information'
import ChangePhone from '@/views/system/change-phone'
import ChangeSex from '@/views/system/change-sex'
import AddNewAddress from '@/views/system/AddNewAddress.vue'
import OrderList from '@/views/OrderList.vue'
import OrderListPay from '@/views/system/OrderListPay.vue'
import OrderListPayed from '@/views/system/OrderListPayed.vue'
import AddressMange from '@/views/system/address-mange'
import Register from '@/views/system/Register'
import dome from '@/views/test/dome'
import ajax from '@/views/test/ajax'
import father from '@/views/test/father.vue'
import EditAddress from '@/views/system/EditAddress.vue'
import Baidu from '@/views/system/Baidu.vue'

//创建
const router =new VueRouter({
    routes:[
        {
            name:'Login',
            path:'/login',
            component:Login,
            
        },
        {
            name:'Home',
            path:'/',
            component:Home,
        },
        {
            name:'BusinessList',
            path:'/businessList',
            component:BusinessList,
        },
        {
            name:'BusinessDetail',
            path:'/businessDetail',
            component:BusinessDetail,
        },
        {
            name:'RedBag',
            path:'/redBag',
            component:RedBag,
        },
        {
            name:'toMoney',
            path:'/toMoney',
            component:ToMoney ,
        },
        {
            name:'upMoney',
            path:'/upMoney',
            component: UpMoney,
        },
        {
            name:'buycar',
            path:'/buycar',
            component: BuyCar,
        },
        {
            name:'my',
            path:'/my',
            component: My,
        },
        {
            name:'change-name',
            path:'/change-name',
            component:ChangeName,
        },
        {
            name:'confirm-order',
            path:'/confirm-order',
            component:ConfirmOrder,
        },
        {
            name:'online-pay',
            path:'/online-pay',
            component:OnlinePay,
        },
        {
            name:'product-details',
            path:'/product-details',
            component:ProductDetails,
        },
        {
            name:'change-password',
            path:'/change-password',
            component:ChangePassword,
        },
        {
            name:'myself-information',
            path:'/myself-information',
            component:MyselfInformation,
        },
        {
            name:'change-sex',
            path:'/change-sex',
            component:ChangeSex,
        },
        {
            name:'change-phone',
            path:'/change-phone',
            component:ChangePhone,
        },
        {
            name:'OrderList',
            path:'/orderList',
            component:OrderList,
        },
        {
            name:'OrderListPay',
            path:'/orderlistpay',
            component:OrderListPay,
        },
        {
            name:'OrderListPayed',
            path:'/orderlistpayed',
            component:OrderListPayed,
        },
        {
            name:'AddNewAddress',
            path:'/addnewaddress',
            component:AddNewAddress,
        },
        {
            name:'address-mange',
            path:'/addressmange',
            component:AddressMange,
        },
        {
            name:'Regieter',
            path:'/register',
            component:Register,
        },
        {
            name:'dome',
            path:'/dome',
            component:dome,
        },
        {   
            name:'ajax',
            path:'/ajax',
            component:ajax,
        },
        {
            path:'/father',
            component:father
        },
        {   
            name:'editaddress',
            path:'/editaddress',
            component:EditAddress,
        },
        {
             name:'Baidu',
             path:'/baidu',
             component:Baidu,
             },
        
    ]
})

//导出
export default router