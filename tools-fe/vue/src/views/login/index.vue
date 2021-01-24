<template>
  <div class="login-container">
    <el-form v-if="isLoginPage" ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" auto-complete="on" label-position="left">

      <div class="title-container">
        <h3 class="title">登录</h3>
      </div>

      <el-form-item prop="username">
        <span class="svg-container">
          <svg-icon icon-class="user" />
        </span>
        <el-input
          ref="username"
          v-model="loginForm.username"
          placeholder="请输入用户名"
          name="username"
          type="text"
          tabindex="1"
          auto-complete="on"
        />
      </el-form-item>

      <el-form-item prop="password">
        <span class="svg-container">
          <svg-icon icon-class="password" />
        </span>
        <el-input
          :key="passwordType"
          ref="password"
          v-model="loginForm.password"
          :type="passwordType"
          placeholder="请输入密码"
          name="password"
          tabindex="2"
          auto-complete="on"
          @keyup.enter.native="handleLogin"
        />
        <span class="show-pwd" @click="showPwd">
          <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
        </span>
      </el-form-item>

      <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:30px;" @click.native.prevent="handleLogin">登录</el-button>

      <div class="tips">
        <el-link type="primary" @click ="enterRegister">没有账号？点击注册</el-link>
      </div>

    </el-form>

    <el-form v-else class="login-form" auto-complete="on" label-position="left">
      <div class="title-container">
        <h3 class="title">注册</h3>
      </div>

      <el-form-item prop="username">
        <span class="svg-container">
          <svg-icon icon-class="user" />
        </span>
        <el-input
          ref="username"
          v-model="registerForm.username"
          placeholder="请输入用户名"
          name="username"
          type="text"
          tabindex="1"
          auto-complete="on"
        />
      </el-form-item>

      <el-form-item prop="password">
        <span class="svg-container">
          <svg-icon icon-class="password" />
        </span>
        <el-input
          :key="passwordType"
          ref="password"
          v-model="registerForm.password"
          :type="passwordType"
          placeholder="请输入密码"
          name="password"
          tabindex="2"
          auto-complete="on"
          @keyup.enter.native="handleRegister"
        />
        <span class="show-pwd" @click="showPwd">
          <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
        </span>
      </el-form-item>

      <el-form-item prop="password">
        <span class="svg-container">
          <svg-icon icon-class="password" />
        </span>
        <el-input
          :key="passwordType"
          ref="password"
          v-model="registerForm.confirmPassword"
          :type="passwordType"
          placeholder="请再次输入密码"
          name="confirmPassword"
          tabindex="3"
          auto-complete="on"
          @keyup.enter.native="handleRegister"
        />
        <span class="show-pwd" @click="showPwd">
          <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
        </span>
      </el-form-item>

      <el-popover placement="top-end" trigger="click" v-model="captchaVisible">
        <recaptcha ref="recaptcha" v-model="registerValid" :handle-success="validRecaptcha"></recaptcha>
        <el-button slot="reference" :loading="loading" type="primary" style="width:100%;margin-bottom:30px;" @click.native.prevent="clickRegister">确认注册</el-button>
      </el-popover>

      <div class="tips">
        <el-link type="primary" @click ="enterLogin">返回登录</el-link>
      </div>

    </el-form>
  </div>
</template>

<script>
import { validUsername } from '@/utils/validate'
import { register } from '@/api/user'
import Recaptcha from "../../components/Recaptcha/index";

export default {
  name: 'Login',
  components: {Recaptcha},
  inject: ['reload'],
  data() {
    const validateUsername = (rule, value, callback) => {
      // if (!validUsername(value)) {
      //   callback(new Error('Please enter the correct user name'))
      // } else {
        callback()
      // }
    }
    const validatePassword = (rule, value, callback) => {
      // if (value.length < 6) {
      //   callback(new Error('The password can not be less than 6 digits'))
      // } else {
        callback()
      // }
    }
    return {
      loginForm: {
        username: null,
        password: null
      },
      registerForm: {
        username: null,
        password: null,
        confirmPassword: null,
        captchaCode: null
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur', validator: validateUsername }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }]
      },
      loading: false,
      passwordType: 'password',
      redirect: undefined,
      isLoginPage: true,
      registerValid: {
        codeFlag: false,
        captchaCode: null
      },
      captchaVisible: false
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  methods: {
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.login()
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    login() {
      this.loading = true
      this.$store.dispatch('user/login', this.loginForm).then(() => {
        console.log('1111')
        this.$router.push({ path: this.redirect || '/' })
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    enterRegister() {
      this.isLoginPage = false
      this.loginForm = {}
      this.passwordType = 'password'
    },
    enterLogin() {
      this.isLoginPage = true
      this.registerForm = {}
      this.passwordType = 'password'
    },
    handleRegister() {
      if (this.registerValid.codeFlag) {
        console.log('发送注册请求')
        this.registerForm.captchaCode = this.registerValid.captchaCode
        register(this.registerForm).then(res => {
          if (res.data) {
            this.loginForm.username = this.registerForm.username
            this.loginForm.password = this.registerForm.password
            this.$confirm('注册成功，是否直接登录？', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'success'
            }).then(() => {
              this.login()
            }).catch(() => {
              this.reload()
              this.loading = false
            });
          } else {
            this.$message.error("注册失败，请重试")
          }
        }).catch(() => {
          this.$refs.recaptcha.handleCaptchaError()
        })
        this.captchaVisible = false
      } else {
        this.$message.error('请完善人机认证')
      }
    },
    validRecaptcha(val) {
      console.log(" =======", val, this.registerForm)
      if (val != null) {
        this.registerValid.captchaCode = val;
        this.registerValid.codeFlag = true;
        console.log('父组件方法，验证成功')
      }
      this.handleRegister()
    },
    clickRegister() {
      if (this.registerValid.codeFlag) {
        this.$refs.recaptcha.handleCaptchaError()
      } else {
        this.$refs.recaptcha.handleLoadGoogleCaptcha()
      }
      console.log(this.registerValid)
    }
  }
}
</script>

<style lang="scss">
/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

$bg:#283443;
$light_gray:#fff;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
    color: $cursor;
  }
}

/* reset element-ui css */
.login-container {
  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;

    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }

  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
}
</style>

<style lang="scss" scoped>
$bg:#2d3a4b;
$dark_gray:#889aa4;
$light_gray:#eee;

.login-container {
  min-height: 100%;
  width: 100%;
  background-color: $bg;
  overflow: hidden;

  .login-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 160px 35px 0;
    margin: 0 auto;
    overflow: hidden;
  }

  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 26px;
      color: $light_gray;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }
}
</style>
