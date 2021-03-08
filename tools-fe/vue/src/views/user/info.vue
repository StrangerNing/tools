<template>
  <div class="page-container">
    <el-form label-width="100px">
      <el-row>
        <el-col :xs="24" :sm="16" :md="12" :lg="8">
          <el-form-item label="头像">
            <el-upload
              class="avatar-uploader"
              :action= uploadUrl
              :with-credentials='true'
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload">
              <img v-if="userInfo.avatarUrl" :src="userInfo.avatarUrl" class="avatar">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :xs="24" :sm="16" :md="12" :lg="8">
          <el-form-item label="用户名">
            <el-input v-model="userInfo.username"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :xs="24" :sm="16" :md="12" :lg="8">
          <el-form-item label="昵称">
            <el-input v-model="userInfo.nickname"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :xs="24" :sm="16" :md="12" :lg="8">
          <el-form-item label="性别">
            <el-select style="width: 99%" v-model="userInfo.sex" placeholder="请选择" filterable>
              <el-option label="男" :value= 0>男</el-option>
              <el-option label="女" :value= 1>女</el-option>
              <el-option label="未知" :value= 2>未知</el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :xs="24" :sm="16" :md="12" :lg="8">
          <el-form-item label="手机号">
            <el-input v-model="userInfo.mobile"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :xs="24" :sm="16" :md="12" :lg="8">
          <el-form-item label="邮箱">
            <el-input v-model="userInfo.email"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :xs="24" :sm="16" :md="12" :lg="8" style="text-align: right;">
          <el-button type="primary" size="small" @click="submit">提交</el-button>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script>
  import {baseURL} from "../../utils/request";
  import {getTokenByKey} from "../../utils/auth";
  import {getInfo, updateInfo} from "../../api/user";

  export default {
    name: "info",
    data () {
      return {
        userInfo: {
          username: null,
          nickname: null,
          sex: null,
          email: null,
          mobile: null,
          avatar: null,
          avatarUrl: null
        },
        uploadUrl: baseURL + '/upload/avatar',
        token: getTokenByKey('JSESSIONID')
      }
    },
    methods: {
      handleAvatarSuccess(res, file) {
        console.log(res.data.name)
        this.userInfo.avatar = res.data.name;
        this.userInfo.avatarUrl = res.data.url
      },
      beforeAvatarUpload(file) {
        // const isJPG = file.type === 'image/jpeg';
        const isLt2M = file.size / 1024 / 1024 < 2;

        // if (!isJPG) {
        //   this.$message.error('上传头像图片只能是 JPG 格式!');
        // }
        if (!isLt2M) {
          this.$message.error('上传头像图片大小不能超过 2MB!');
        }
        return isLt2M;
      },
      submit() {
        updateInfo(this.userInfo).then( res => {
          if (res.data) {
            this.$message.success('修改成功')
          } else {
            this.$message.error(res.msg)
          }
          this.getUserInfo()
        })
      },
      getUserInfo() {
        getInfo().then(res => {
          this.userInfo = res.data
        })
      }
    },
    created() {
      this.getUserInfo()
    }
  }
</script>

<style scoped>
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
</style>
