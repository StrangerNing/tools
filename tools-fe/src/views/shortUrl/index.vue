<template>
  <div class="page-container">
      <el-form label-width="100px">
        <div  class="page-text">
          <span>短链接生成</span>
        </div>
        <el-row :gutter="12" style="margin-top: 20px">
          <el-col :xs="22" :sm="11" :lg="8">
            <el-form-item label="输入网址：">
              <el-input v-model="url" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="2" :sm="1" :lg="1">
            <el-button type="primary" @click="submit">提交</el-button>
          </el-col>
        </el-row>
        <el-row v-if="showTip">
          <el-col :xs="24" :sm="12" :lg="9">
            <el-card>
              <el-row>
                <span>生成成功！短网址为：</span>
                <a style="color: #1482f0" target="_blank" :href="'http://'+shortUrl">{{shortUrl}}</a>
              </el-row>
              <el-row style="margin-top: 20px;text-align: center">
                <el-button type="primary" size="small" v-clipboard:copy="shortUrl" v-clipboard:success="onCopy" v-clipboard:error="onError">复制</el-button>
                <el-button type="success" size="small" @click="hrefUrl">跳转</el-button>
              </el-row>
            </el-card>
          </el-col>
        </el-row>
      </el-form>
  </div>
</template>

<script>
  import {submitUrl} from "../../api/shortUrl";

  export default {
    name: "index",
    data() {
      return {
        url: '',
        shortUrl: '',
        showTip: false
      }
    },
    methods: {
      submit() {
        let param = {
          originUrl: this.url
        }
        submitUrl(param).then(res => {
          if (res.success) {
            this.$message.success("提交成功")
            this.shortUrl = 'localhost:8080/' + res.data
            this.showTip = true
          }
        })
      },
      onCopy() {
        this.$message.success("短网址已复制到剪切板")
      },
      onError() {
        this.$message.error("短网址复制失败，请重试")
      },
      hrefUrl() {
        window.open('http://'+this.shortUrl)
      }
    }
  }
</script>

<style lang="scss" scoped>

</style>
