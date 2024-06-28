<template>
  <div class="wrapper">
       <!-- 头部分 -->
       <div class="header">
            <a>
            <i class="fa fa-chevron-left" aria-hidden="true" @click="goBack"/>
            </a>
            <header>
                <span class="active">红包</span>
                <span>券</span>
            </header>
        </div>
        <div class="all">
            <span>全部红包</span>
            <span>历史红包</span>
        </div>
        <!--红包-->
        <div class="red-bag">
            <section>
                <div class="section-left">
                    <img src="../../assets/image/红包券.png">
                    <div class="text">
                        <p>超级吃货红包</p>
                        <span>2024/04/06 23:59到期</span>
                    </div>   
                </div>
                <div class="section-right">
                    <p>&#165;6</p>
                    <span>无门槛</span>
                </div>
            </section>
            <div class="button">
                <span>限外卖到家业务使用</span>
                <button>去使用</button>
            </div>
        </div>
        <div class="red-bag">
            <section>
                <div class="section-left">
                    <img src="../../assets/image/红包券.png">
                    <div class="text">
                        <p>超级吃货红包</p>
                        <span>2024/04/06 23:59到期</span>
                    </div>   
                </div>
                <div class="section-right">
                    <div><p>&#165;5</p><p class="dot">.5</p></div>
                    <span>无门槛</span>
                </div>
            </section>
            <div class="button">
                <span>限外卖到家业务使用</span>
                <button>去使用</button>
            </div>
        </div>
        <div class="red-bag">
            <section>
                <div class="section-left">
                    <img src="../../assets/image/红包券.png">
                    <div class="text">
                        <p>吃货红包</p>
                        <span>2024/04/06 23:59到期</span>
                    </div>   
                </div>
                <div class="section-right">
                    <div><p>&#165;5</p><p class="dot">.5</p></div>
                    <span>满20可用</span>
                </div>
            </section>
            <div class="button">
                <span>限外卖到家业务使用</span>
                <button>去使用</button>
            </div>
        </div>
        <div class="red-bag red-bag2">
            <section>
                <div class="section-left">
                    <img src="../../assets/image/红包券管理1.png">
                    <div class="text">
                        <p>超级吃货加量包</p>
                        <span>今日 23:59到期</span>
                    </div>   
                </div>
                <div class="section-right">
                    <div><p>&#165;18</p><p class="dot" style="color: #ff5b1a;">.5</p></div>
                    <span>满36可用</span>
                </div>
            </section>
            <div class="button">
                <span>限购超级吃货红包使用</span>
                <button>去使用</button>
            </div>
        </div>
        <div class="red-bag red-bag2">
            <section>
                <div class="section-left">
                    <img src="../../assets/image/红包券管理1.png">
                    <div class="text">
                        <p>超级吃货加量包</p>
                        <span>今日 23:59到期</span>
                    </div>   
                </div>
                <div class="section-right">
                    <p>&#165;10</p>
                    <span>满16可用</span>
                </div>
            </section>
            <div class="button">
                <span>限购超级吃货红包使用</span>
                <button>去使用</button>
            </div>
        </div>
        <div class="red-bag red-bag2">
            <section>
                <div class="section-left">
                    <img src="../../assets/image/红包券管理1.png">
                    <div class="text">
                        <p>超级吃货加量包</p>
                        <span>今日 23:59到期</span>
                    </div>   
                </div>
                <div class="section-right">
                    <p>&#165;15</p>
                    <span>满24可用</span>
                </div>
            </section>
            <div class="button">
                <span>限购超级吃货红包使用</span>
                <button>去使用</button>
            </div>
        </div>


  </div>
</template>

<script>

export default {
  data () {
    return {

    }
  },
  components: {
    
  },
  methods: {
    goBack() {
            this.$router.go(-1);
        },
  },
}

</script>
<style scoped>
.wrapper{
            display: flex;flex-direction: column;
        }
         .wrapper .header{ width:100%;height:12vw;background: linear-gradient(to right,#403A3E,#BE5869);color:#ffffff;font-size: 5vw;
            left:0;top:0;display:flex;align-items: center;}
        .wrapper .header i{display: flex;justify-content: left;padding-left: 3vw;cursor: pointer;}
        .wrapper .header span{ padding-left: 25vw;}
        .wrapper .header header{
            justify-content: space-around;display: flex;align-items: center;
        }
        .wrapper .header header .active{
            color: #fff200;
        }
        /*红包*/
        .wrapper .all{
            width: 96%;display: flex;justify-content: space-between;
            background: linear-gradient(to right,#403A3E,#BE5869);
            color:#ffffff;padding: 1vw 2vw;
            font-size: 4.5vw;
        }
        .wrapper .red-bag{
            background:linear-gradient(to right,#fdff79, #ff4800d6);width: 96%;display: flex;
            margin-left: 2vw;border-radius: 2vw;margin-top: 1vw;
            flex-direction: column;
        }
        .wrapper .red-bag2{
            background: rgb(248, 248, 248);
        }
        .wrapper .red-bag section{
            display: flex;justify-content: space-between;
            border-bottom: 1px dotted #b9b9b9ab;height: 18vw;
        }
        .wrapper .red-bag section img{
            height: 13vw;width: 13vw;margin-top: 2vw;margin-left: 2vw;
        }
        .wrapper .red-bag section .section-left{
            flex-direction: row;flex-wrap: wrap;display: flex;
        }
        .wrapper .red-bag section .section-left .text{
            margin-top: 3vw;margin-left: 2vw;
        }
        .wrapper .red-bag section .section-left .text p{
            font-size: 5vw;color: #3e3e3e;
        }
        .wrapper .red-bag section .section-left .text span{
            font-size: 3vw;color: #ff0000;
        }
        .wrapper .red-bag section .section-right{
            margin-right: 2vw;margin-top: 3vw;display: flex;
            flex-direction: column;
        }
        .wrapper .red-bag section .section-right p{
            font-size: 7vw;color: #fffb10;padding-right: 3vw;
            display: flex;justify-content: right;
        }
        .wrapper .red-bag section .section-right div{
            display: flex;position: relative;right: -2vw;
           
        }
        .wrapper .red-bag section .section-right div .dot{
            font-size: 4vw;color: #fffb10;position: relative;top: 2.3vw;right: 2.6vw;
        }
        .wrapper .red-bag2 section .section-right p{
            color: #ff5b1a;
        }
        .wrapper .red-bag section .section-right span{
            font-size: 3.5vw;color: #767779;padding-right: 2vw;
            display: flex;justify-content: right;
        }
        .wrapper .red-bag .button{
            display: flex;justify-content: space-between;height: 8svw;
            align-items: center;
        }
        .wrapper .red-bag .button span{
            font-size: 3.5vw;color: #79859e; margin-left: 2vw;
        }
        .wrapper .red-bag .button button{
            background-color: #fffb78;border-radius:3vw;
            margin-right: 2vw;color: rgb(59, 49, 14);border: none;
            font-size: 3.6vw;
        }
        .wrapper .red-bag2 .button button{
            background-color: #ff5b1a;color: #fff;
        }
</style>