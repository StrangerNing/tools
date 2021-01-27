<template>
  <div class="page-container">
    <div class="page-text">
      图床管理
    </div>
    <div class="page-content">
      <waterfall-upload
        :action="uploadUrl"
        list-type="picture-card"
        :with-credentials='true'
        :file-list="imageList"
        :on-success="handleSuccess"
        :on-preview="handlePictureCardPreview"
        :on-remove="handleRemove">
        <i class="el-icon-plus"></i>
      </waterfall-upload>
      <el-dialog :visible.sync="dialogVisible" :before-close="beforeClose">
        <img width="100%" :src="dialogImageUrl" alt="">
      </el-dialog>
    </div>
  </div>
</template>

<script>
  import {delFile, getFile, getImageList} from "../../api/file";
  import {baseURL} from "../../utils/request";
  import WaterfallUpload from "../../components/WaterfallUpload/index";

  export default {
    name: "imageList",
    components: {WaterfallUpload},
    data() {
      return {
        query: {
          currentPage: 1,
          limit: 10,
          style: 'image/resize,l_100,m_mfit'
        },
        uploadUrl: baseURL + '/upload/image',
        imageList: [],
        dialogImageUrl: null,
        dialogVisible: false
      }
    },
    methods: {
      search() {
        getImageList(this.query).then(res => {
          console.log(res.data.list)
          this.imageList = res.data.list
        })
      },
      handleRemove(file, fileList) {
        delFile(file.id).then(res => {
          this.$message.success('删除成功')
        })
        console.log(file, fileList);
      },
      handlePictureCardPreview(file) {
        console.log(file)
        getFile(file.id).then(res => {
          this.dialogImageUrl = res.data.url;
          this.dialogVisible = true;
        })
      },
      handleSuccess(response, file, fileList) {
        fileList.reverse()
        console.log(response, file, fileList)

      },
      beforeClose(done) {
        this.dialogImageUrl = null
        done()
      }
    },
    created() {
      this.search()
    }
  }
</script>

<style scoped>

</style>
