<template>
  <div class="example-title"> vue-video-player 播放 HLS 流 </div>
  <HlsLive></HlsLive>
</template>

<script>
import HlsLive from './components/HlsLive.vue'

export default {
  name: 'App',
  components: {
    HlsLive
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
