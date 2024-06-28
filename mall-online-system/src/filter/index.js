
//定义全局过滤器js文件。
export default{
    //定义过滤器，可以定义多个，用逗号分隔。
    fmtSex:(value)=>{
        if(value == 1){
            return '先生';
        }else if(value == 0){
            return '女士'
        }else{
            return 'Error';
        }
    },
    fmtDate:value=>{
        //将value日期字符串转换为 日期对象
        let date = new Date(value);
        let y = date.getFullYear();
        let M = (date.getMonth() + 1).toString().padStart(2,'0');
        let d = date.getDate().toString().padStart(2,'0');

        let h = date.getHours().toString().padStart(2,'0');
        let m = date.getMinutes().toString().padStart(2,'0');
        let s = date.getSeconds().toString().padStart(2,'0');

        return `${y}年${M}月${d}日 ${h}:${m}:${s}`;
    },
    fmtTitle:value=>{
       return value.length > 18? value.substring(0,18)+"...":value;
    }
    
}