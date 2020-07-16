<template>
  <div>
    <h2>键盘钢琴</h2>
    <audio id="piano">
      <source src="static/piano/c-1.mp3"/>
    </audio>
    <el-button type="primary" @click="play">播放</el-button>
  </div>
</template>

<script>
  export default {
    name: "keyboard",
    data() {
      return {
        keyMap: {
          a: "@/assets/piano/c-1.mp3"
        }
      }
    },
    methods: {
      play() {
        // document.getElementById('piano').currentTime = 0
        document.getElementById('piano').play()
        // let audio = document.createElement("AUDIO")
        // audio.setAttribute("src","/static/piano/c-1.mp3")
        // audio.setAttribute("controls","controls")
        // document.body.appendChild(audio)
        // audio.play()
      }
    }
  }
</script>

<style scoped>

</style>
