<template>
  <div class="wrapper">

      <!-- 头部分 -->
      <div class="header">
            <a ><i class="fa fa-chevron-left" aria-hidden="true" @click="goBack"/></a>
            <p>修改地址</p>
        </div>
       
        <!--地址管理-->
        <template v-if="deliveryAddress.length > 0">
            <ul class="address" v-for="item in deliveryAddress" :key="item.daId" >
            <li class="show">
                <div class="left" @click="setDefaultAddress(item)">
                    <div>  
                        <p>{{item.address}}</p>
                    </div>
                    <div> 
                        <span>{{item.contactName}}</span>
                        <span>{{ item.contactSex | get_sex}}</span>
                        <span>{{item.accountId}}</span>
                    </div>
                </div>
                <div class="right">
                    <img src="@/assets/image/编辑1.png" @click="editAddress(item.daId)">
                    <img src="@/assets/image/删除1.png" @click="removeAddress(item.daId)">  
                </div>
            </li>
        </ul>
        </template>
        <template v-if="deliveryAddress.length = 0">
            <h3>还没有设置配送地址</h3>
        </template>

        <div class="add" @click="toadd(businessId)">
            <img src="@/assets/image/增加1.png"  >
            <span>新增用户地址</span>
        </div>
        
    <Footer></Footer>
  </div>
</template>

<script>
import Footer from '@/components/Footer.vue';
export default {
  data () {
    return {
        businessId:0,
        account:{},
        deliveryAddress:[]
    }
  },
  components: {
    Footer,
  },
  created(){
    this.account = this.$getSessionStorage('account');
    this.businessId = this.$route.query.businessId;
    this.loadAddressByAccountId()
  },
  methods: {
    goBack(){
        this.$router.go(-1);
    },
    toadd(id){
        this.$router.push({path:'/addnewaddress',query:{businessId : id}})
    },
    loadAddressByAccountId(){
        let url = "/deliveryaddress/listByAccountId/"+this.account.accountId;
        this.$axios.get(url).then((response)=>{
            this.deliveryAddress = response.data.resultdata;
        }).catch((error)=>{
            console.log(error)
        })
    },
    setDefaultAddress(da){
        this.$setLocalStorage(this.account.accountId,da);
        this.$router.push({path:'/confirm-order',query:{businessId : this.businessId}})
    },
    removeAddress(id){
        this.$confirm('确认删除地址吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
            let url = "/deliveryaddress/delete/"+id;
            this.$axios.post(url).then((response)=>{
                let defaultAddress = this.$getLocalStorage(this.account.accountId);
                if(defaultAddress != null && defaultAddress.daId == id){
                    this.$removeLocalStorage(this.account.accountId)
                }
                this.loadAddressByAccountId();
                this.$message({
                    type: 'success',
                    message: '删除成功!'
                        });
                    }).catch((error)=>{
                        console.log(error)
                    })
             }).catch(() => {    
            });
    },
    editAddress(id){
        this.$router.push({path:'/EditAddress',query:{
            businessId : this.businessId,
            daId : id
        }})
    }

  },
}

</script>
<style scoped>
.wrapper{
            width: 100%;position: relative;
          }
          .wrapper .header{ width:100%;height:12vw;background: linear-gradient(to right,#403A3E,#BE5869);
            color:#79859e;font-size: 5vw;
            left:0;top:0;display:flex;align-items: center;}
        .wrapper .header i{display: flex;justify-content: left;padding-left: 3vw;cursor: pointer;}
        .wrapper .header p{ font-weight: 500;padding-left: 33vw;}
        

        /*地址管理*/
        .wrapper .address{
            
            border-bottom:1px solid #403A3E;padding: 2vw;
            position: relative;
        }
        
        .wrapper .address .left span{
            color: #79859e;flex-direction: row;font-size: 3vw;margin-right: 2vw;
        }
        .wrapper .address .show{
            display: flex;justify-content: space-between;align-items: center;
            margin: 0 2.8vw;
        }
        .wrapper .address .show .left{
            flex-wrap: wrap;flex-direction: column;
        }
        .wrapper .address .right{
            display: flex;align-items: center;width: 15vw;
        }
        .wrapper .address .right img{
            height: 6vw;width: 6vw;margin-right: 5vw;
        }
        
        .wrapper .add{
            width: 100%;justify-content: center;height: 15vw;border: 1px solid #403A3E;display: flex;
            align-items: center;margin-top: 2vw;background: linear-gradient(to right bottom,#fd746c,#ff9068);
            border-radius: 2vw;
        }
        .wrapper .add img{
            height: 6vw;width: 6vw;margin-right: 2vw;
        }

</style>