//配置全局axios的代码文件
import axios from 'axios'
import Element from 'element-ui'
import router from '@/router'
//进行全局axios设置。需要定一个新的axios
const myaxios = axios.create({
    timeout: 5000, //设置请求超时时间
});
//请求基础路径：
myaxios.defaults.baseURL = '/sys';
//设置axios请求拦截器，就在axios发起请求之前执行
myaxios.interceptors.request.use(config => {
    //登录之后，每次请求都必须携带 token请求头信息。
    config.headers['token'] = JSON.parse(sessionStorage.getItem('TOKEN'));
    return config;
})
// 对于axios所有的响应做处理。设置响应的拦截器。
//响应的拦截器，就是每次请求回调 .then()方法之前执行。
myaxios.interceptors.response.use(response => {
    let Result = response.data;
    if (Result.code == 20000) {
        //如果状态码是20000，表示请求成功，就正常执行请求回调.then()
        return response;
    } else {
        //返回code不是20000，表示各种请求的失败码，直接进行错误提示：
        Element.Message.error(Result.message ? Result.message : "系统异常", {
            duration: 1000,
        });
        return Promise.reject(response.data.message);
    }
   
}, error => { //http: 200、404、500
    //error是对http请求不同错误状态码做判断
    if (error) {
        error.message = error.response.data.message;
    }
    if (error.response.status == 401) {
        //401：表示Java服务器上的API接口是有权限，当前登录用户不具备该权限，会报401。
        router.push('/login');
        return Promise.reject(error);
    }
    if (error.response.status == 404) {
        //如果是404,就跳转至 访问不到资源的 错误显示页面
        router.push('/notfound');
        return Promise.reject(error);
    }
    if (error.response.status == 400) {
        //状态码是400，就是请求Java服务器方法的方式错误。
        error.message = '请求服务器端方法错误';
    }
    if (error.response.status == 500) {
        error.message = '服务器端代码发生错误';
    }
    Element.Message.error(error.message, {
        duration: 1000,
    });
    return Promise.reject(error);
})
//将自定义 myaxios
export default myaxios
