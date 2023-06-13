<template>
  <div class="home">
    <!-- 轮播图 -->
    <CarouselView></CarouselView>
    <!-- 推荐 -->
    <RecommendView></RecommendView>
    <!-- 速递 -->
    <ExpressView></ExpressView>
    <!-- 其它戏曲 -->
    <OtherOpera></OtherOpera>
    <Footer></Footer>
  </div>
</template>
<script>
import Footer from './Footer.vue';
import CarouselView from '@/views/reception/CarouselPage.vue'
import RecommendView from '@/views/reception/RecommendView.vue'
import ExpressView from '@/views/reception/ExpressView.vue'
import OtherOpera from '@/views/reception/OtherOpera.vue'
export default {
  name: 'ReceptionHome',
  components: { Footer, CarouselView, RecommendView, ExpressView, OtherOpera }
}
</script>
