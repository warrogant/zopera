<template>
    <div>
      <div v-for="opera in operas" :key="opera.id">
        <span>{{ opera.name }}</span>
        <button @click="toggleFavorite(opera.id)">收藏</button>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        operas: [], // 从后端获取的戏曲列表数据
      };
    },
    methods: {
      toggleFavorite(operaId) {
        // 发送POST请求到后端，切换收藏状态
        this.$http.post('/favorite/toggle', { operaId })
          .then(response => {
            // 更新前端页面的收藏状态
            this.updateFavoriteStatus(operaId, response.data.favorite);
          })
          .catch(error => {
            console.error(error);
          });
      },
      updateFavoriteStatus(operaId, favorite) {
        // 更新前端页面的收藏状态
        this.operas.forEach(opera => {
          if (opera.id === operaId) {
            opera.favorite = favorite;
          }
        });
      },
    },
    mounted() {
      // 从后端获取戏曲列表数据
      this.$http.get('/opera/list')
        .then(response => {
          this.operas = response.data;
        })
        .catch(error => {
          console.error(error);
        });
    },
  };
  </script>
  