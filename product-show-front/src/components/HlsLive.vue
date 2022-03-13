<template>
  <video-player class="vjs-custom-skin"
                ref="videoPlayer"
                :options="playerOptions"
                @ready="playerReadied">
  </video-player>
</template>

<script>
import 'videojs-contrib-hls'

export default {
  name: 'hls-live',
  props: {
    src: {
      type: String,
      default: 'http://localhost:8022/ps/hls/6033c3afdf82143c4c87fc958f9bfdb6.m3u8'
    }
  },
  data() {
    return {
      playerOptions: {
        height: '320',
        width: '100%',
        sources: [{
          withCredentials: false,
          type: "application/x-mpegURL",
          src: this.src
        }],
        controlBar: {
          timeDivider: false,
          durationDisplay: false
        },
        flash: {hls: {withCredentials: false}},
        html5: {hls: {withCredentials: false}},
        autoplay: true, // 自动播放
        controls: true, // 控制条
        fluid: true, // 按比例缩放适应容器
        preload: 'auto', // 预加载
        // muted: true, // 消除所有音频
        // loop: false, // 循环播放
        aspectRatio: "16:9",
        // poster: 'https://surmon-china.github.io/vue-quill-editor/static/images/surmon-9.jpg' //首屏图片
      }
    }
  },
  methods: {
    playerReadied(player) {
      let hls = player.tech({IWillNotUseThisInPlugins: true}).hls
      console.log(hls)
      player.tech_.hls.xhr.beforeRequest = function (options) {
        // console.log(options)
        return options
      }
    }
  }
}
</script>