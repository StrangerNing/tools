export default {
  data() {
    return {
      isFullScreen: false
    }
  },
  methods: {
    onCheckFullScreen() {
      let offsetWid = document.documentElement.clientWidth;
      this.isFullScreen = offsetWid < 768
    },
    checkFullScreen() {
      let that = this
      this.onCheckFullScreen()
      window.onresize=function () {
        that.onCheckFullScreen()
      }
    }
  }
}
