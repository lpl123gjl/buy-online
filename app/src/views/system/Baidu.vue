<template>
  <div class="map-container">
    <baidu-map class="map" :zoom="17" ref="baiduMap">
      <bm-geolocation anchor="BMAP_ANCHOR_BOTTOM_RIGHT" :showAddressBar="true" :autoLocation="true" @locationSuccess="handleLocationSuccess" @locationError="handleLocationError"></bm-geolocation>
    </baidu-map>
  </div>
</template>

<script>
import BaiduMap from 'vue-baidu-map/components/map/Map.vue'
import BmGeolocation from 'vue-baidu-map/components/controls/Geolocation.vue'

export default {
  data(){
      return{
          addressComponent: null // 定义addressComponent变量，初始化为null
      }
  },
  components: {
    BaiduMap,
    BmGeolocation
  },
  methods: {
      handleLocationSuccess(point, addressComponent, marker) {
    // 这里可以处理定位成功后的数据
    console.log('定位成功', point, addressComponent);

    // 重新设置地图中心点
    this.$refs.baiduMap.map.centerAndZoom(point, 17);

    // 将addressComponent对象转换为JSON字符串
    const addressComponentStr = JSON.stringify(point.addressComponent);
    console.log(addressComponentStr);

    // 保存addressComponentStr到sessionStorage
    sessionStorage.setItem('addressComponent', addressComponentStr);

    setTimeout(() => {
  // 导航到下一页面或其他操作
  this.$router.push({ path: '/' });
}, 3000); 
  },
    handleLocationError(StatusCode) {
      // 这里可以处理定位失败的情况
      console.error('定位失败', StatusCode);
    }
  }
}
</script>

<style>
.map-container {
  height: 100vh;
  width: 100%;
}
.map {
  height: 100%;
  width: 100%;
}
</style>
