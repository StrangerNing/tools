<template>
  <div class="dashboard-container">
    <div class="dashboard-text">name: {{ name }}</div>
    <el-button type="primary" @click="getUserInfo">跳转</el-button>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import {getInfo} from "../../api/user";

export default {
  name: 'Dashboard',
  data() {
    return {

    }
  },
  computed: {
    ...mapGetters([
      'name'
    ])
  },
  methods: {
    getUserInfo() {
      console.log(this.$store.getters.avatar)
      console.log(this.$store.getters.nickname)
      getInfo().then(res => {
        this.$alert(res.data, '用户信息', {
          confirmButtonText: '确定'
        })
      })
    }
  },
  created() {
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
</style>
