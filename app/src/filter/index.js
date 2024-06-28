export default{
    get_sex:(value)=>{
        if(value == 1){
            return '男士'
        }else if(value == 0){
            return '女士'
        }else{
            return 'Error'
        }
    },
    fmtDate:value=>{
        let date = new Date(value);
        let y = date.getFullYear();
        let M = (date.getMonth()+1).toString().padStart(2,'0');
        let d = date.getDate().toString().padStart(2,'0');

        let h = date.getHours().toString().padStart(2,'0');
        let m = date.getMinutes().toString().padStart(2,'0');
        let s = date.getSeconds().toString().padStart(2,'0');

        return `${y}年${M}月${d}日${h}:${m}:${s}`
    },
    fmtTitle:value=>{
        return value.length>15?value.substring(0,15)+'...':value;
    },
    fmtTitle2:value=>{
        return value.length>7?value.substring(0,7)+'...':value;
    }
}