<template>
    <div>
        <h1>猪猪侠 :{{ sex | tsex }}</h1>
        <h2>{{ msg | ucase }}</h2>
        <h1>熊二 :{{ sex2 | tsex }}</h1>
        <h3><s>{{ price }}</s> {{ price | p | p2 }}</h3>

        <h1>{{ date | dft }}</h1>
    </div>
</template>

<script>
import Vue from 'vue';
Vue.filter('ucase', function (value) {
    return value.toUpperCase();
})
Vue.filter('tsex', function (value) {
    if (value == 0) {
        return '男';
    } else if (value == 1) {
        return '女';
    } else {
        return 'error'
    }
})
Vue.filter('p', function (value) {
    return value * 0.66;
})
Vue.filter('p2', function (value) {
    return parseFloat(value).toFixed(2);
})
export default {
    data() {
        return {
            sex: 0,
            sex2: 3,
            price: 999,
            msg: 'zhuzhuxia',
            date: new Date(),
        }
    },
    components: {

    },
    methods: {

    },
    filters: {
        dft: function (value) {
            let y = value.getFullYear();
            let m = (value.getMonth() + 1).toString().padStart(2, '0');
            let d = value.getDate().toString().padStart(2, '0');
            let h = value.getHours().toString().padStart(2, '0');
            let M = value.getMinutes().toString().padStart(2, '0');
            let s = value.getSeconds().toString().padStart(2, '0');

            return `${y}年${m}月${d}日 ${h}:${M}:${s}`;

        }
    }
}

</script>
<style scoped></style>