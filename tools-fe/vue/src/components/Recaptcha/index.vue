<template>
  <div id="captcha"></div>
</template>

<script>

  import remoteLoad from "../../utils/remoteLoad";

  export default {
    name: "Recaptcha",
    props: {
      value: {
        type: Object,
        default: {
          codeFlag: false,
          captchaCode: null
        }
      },
      handleSuccess: {
        type: Function
      }
    },
    data() {
      return {
        captchaId: null,
        register: this.value
      }
    },
    async created() {
      await remoteLoad('https://www.recaptcha.net/recaptcha/api.js?render=explicit')
    },
    methods: {
      handleLoadGoogleCaptcha() {
        let env = this;
        console.log(env.captchaId)
        if (env.captchaId === undefined || env.captchaId === null) {
          env.captchaId = grecaptcha.render("captcha", {
            //在Google reCaptcha网站获取的Site Key
            "sitekey": '6LcPWzEaAAAAAN37oKCUBAQNJHfA-kdE0FYguIhh',
            //主题
            "theme": "light",
            //验证成功后的回调函数
            "callback": this.handleSuccess ? this.handleSuccess : env.handleCaptchaCallback,
            //验证码超时后的回调函数
            "expired-callback": env.handleCaptchaExpired,
            //验证失败时的回调函数
            "error-callback": env.handleCaptchaError,
            //默认语言 默认是英语，简体中文写zh-CN 如果有其他需要请自行搜索
            "hl": 'zh-CN'
          });
        }
      },
      handleCaptchaCallback(val) {
        console.log(val, this.register)
        if (val != null) {
          this.register.captchaCode = val;
          this.register.codeFlag = true;
          console.log('验证成功', this.register)
          this.$emit('input', this.register)
        }
      },
      handleCaptchaExpired() {
        this.register.captchaCode = "";
        this.register.codeFlag = false;
        console.log('验证过期')
        this.$emit('input', this.register)
      },
      handleCaptchaError() {
        this.register.captchaCode = "";
        //重置验证码
        console.log('重置验证码')
        grecaptcha.reset(this.captchaId);
        this.register.codeFlag = false;
        this.$emit('input', this.register)
      }
    }
  }
</script>

<style scoped>

</style>
