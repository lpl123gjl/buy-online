<template>
<div>
    <button @click="go">请求</button>
    <button @click="axios_get">axios_get请求</button>
    <button @click="axios_post">axios_post请求</button>
    <ul>
        <li v-for="(item,index) in arr" :key="index">
        {{ item.title }}
        <p style="color: red;font-size: 22px;">{{ item.ctime }}</p>
        <p>{{ item.description }}</p>
        </li>
    </ul>
</div>
</template>

<script>
export default {
    data() {
        return {
            arr:[]
        }
    },
    components: {

    },
    methods: {
        go() {
            //创建XMLHttpRequest核心对象
            let xhr = new XMLHttpRequest();
            //open()方法来完成Http请求（get方式）
            xhr.open("get", "https://apis.tianapi.com/topnews/index?key=1225f2f918604ff8711dc2e2b2502d57");
            //向服务器发送请求
            xhr.send(null);
            //使用onreadystatechange事件监听XMLHttpRequest对象状态。
            xhr.onreadystatechange = function () {
                //readyState属性代表了 XMLHttpRequest对象的五种状态。4状态为响应内容接收并解析完成
                if (xhr.readyState == 4) {
                    //http请求会返回一个状态码。200为请求成功。
                    if (xhr.status == 200) {
                        //xhr.responseText就是服务器端返回的数据
                        console.log(xhr.responseText);
                    }
                }
            };
        },
        axios_get(){
            let url = "https://apis.tianapi.com/topnews/index?key=1225f2f918604ff8711dc2e2b2502d57"
            this.$axios.get(url).then((response)=>{
                console.log(response);
            }).catch((error)=>{
                console.log(error);
            })
        },
        axios_post(){
            let url = "https://apis.tianapi.com/topnews/index?key=1225f2f918604ff8711dc2e2b2502d57";
            let mykey = {key:'1225f2f918604ff8711dc2e2b2502d57'}
            this.$axios.post(url,this.$qs.stringify(mykey)).then((response)=>{
                this.arr = response.data.result.list;
                console.log(response.data.result.list)
            }).catch((error)=>{
                console.log(error)
            })
        }
    },
}
</script>

<style scoped>
</style>
