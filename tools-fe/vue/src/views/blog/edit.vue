<template>
  <div class="page-container">
    <el-form label-width="90px" label-position="left">
      <div class="page-text">
        <span v-if="type === 1">新建文章</span>
        <span v-else>编辑文章</span>
      </div>
      <div>
        <el-form-item label="编辑器：">
          <el-switch
            v-model="article.editType"
            active-color="#13ce66"
            inactive-color="#409eff"
            active-text="markdown"
            inactive-text="html"
            :active-value="1"
            :inactive-value="2">
          </el-switch>
        </el-form-item>
        <el-form-item label="标题：">
          <el-input v-model="article.title"></el-input>
        </el-form-item>
        <el-form-item label="标签：">
          <el-tag
            :key="tag.tag"
            v-for="tag in article.tags"
            closable
            :disable-transitions="false"
            @close="handleTagClose(tag)">
            {{tag.tag}}
          </el-tag>
          <el-select
            class="input-new-tag"
            v-if="inputTagVisible"
            v-model="inputTagValue"
            ref="saveTagInput"
            size="small"
            filterable
            allow-create
            default-first-option
            remote
            :remote-method="querySearchAsync"
            @change="change"
            @focus="focusInput"
            @visible-change="cancelInputTag">
            <el-option
              v-for="item in options"
              :key="item.label"
              :label="item.label"
              :value="item">
            </el-option>
          </el-select>
          <el-button v-else class="button-new-tag" size="small" @click="showTagInput">+ New Tag</el-button>
        </el-form-item>
        <el-form-item label="封面：">
          <el-button-group v-if="article.thumb">
            <el-button type="primary" size="small" @click="previewThumb">预览</el-button>
            <el-button type="danger" size="small" @click="deleteThumb">删除</el-button>
          </el-button-group>
          <el-upload v-else
            :action="uploadUrl"
            :show-file-list="false"
            :on-success = "handleThumbSuccess"
            with-credentials>
            <el-button size="small" type="primary">点击上传</el-button>
          </el-upload>
        </el-form-item>
        <div v-if="article.editType === 1">
          <mavon-editor ref="md" v-model="article.markdown" @change="mavonEditorChange" @imgAdd="imgAdd" @imgDel="imgDel"/>
        </div>
        <div v-else>
          <span id="article"></span>
        </div>
        <div class="page-content">
          <el-button type="primary" @click="submit" :disabled="loading">发布</el-button>
        </div>
      </div>
    </el-form>
    <el-image-viewer v-if="showViewer"
                     :on-close="closeViewer"
                     :url-list="[article.thumbPreview]"></el-image-viewer>
  </div>
</template>

<script>
  import remoteLoad from "../../utils/remoteLoad";
  import {add, edit, getArticle, searchTag, uploadFile} from "../../api/blog";
  import {baseURL} from "../../utils/request";
  import ElImageViewer from 'element-ui/packages/image/src/image-viewer';
  import {delFileByName} from "../../api/file";

  export default {
    name: "edit",
    components: {ElImageViewer},
    data() {
      return {
        article: {
          editType: 2,
          title: '',
          tags: [],
          content: '',
          markdown: null,
          thumb: '',
          thumbPreview: null
        },
        loading: false,
        options: [],
        uploadUrl: baseURL + '/upload/blog',
        inputTagVisible: false,
        inputTagValue: null,
        wangEditor: null,
        showViewer: false,
        type: 1,
        imgList: {}
      }
    },
    watch: {
      'article.editType': function (value) {
        if (value === 2) {
          this.openWangEditor()
        }
        if (value === 1 && this.wangEditor !== null) {
          console.log(this.wangEditor)
          // this.article.markdown = this.wangEditor.txt.html()
          this.article.content = this.wangEditor.txt.html()
        }
      }
    },
    methods: {
      save() {
        console.log(this.value)
      },
      async openWangEditor() {
        await remoteLoad('https://cdn.jsdelivr.net/npm/wangeditor@latest/dist/wangEditor.min.js')
        const E = window.wangEditor
        // const editor = new E("#div1")
        this.wangEditor = new E(document.getElementById('article'))
        this.wangEditor.config.zIndex = 0
        this.wangEditor.config.uploadImgServer = this.uploadUrl
        this.wangEditor.config.uploadFileName = 'file'
        this.wangEditor.config.withCredentials = true
        this.wangEditor.config.uploadImgHooks = {
          customInsert: function(insertImgFn, result) {
            // result 即服务端返回的接口
            console.log('customInsert', result)

            // insertImgFn 可把图片插入到编辑器，传入图片 src ，执行函数即可
            insertImgFn(result.data.prefix + result.data.name)
          }
        }

        this.wangEditor.config.uploadVideoServer = this.uploadUrl
        this.wangEditor.config.uploadVideoName = 'file'
        this.wangEditor.config.withVideoCredentials = true
        this.wangEditor.config.uploadVideoHooks = {
          customInsert: function(insertImgFn, result) {
            // result 即服务端返回的接口
            console.log('customInsert', result)

            // insertImgFn 可把图片插入到编辑器，传入图片 src ，执行函数即可
            insertImgFn(result.data.prefix + result.data.name)
          }
        }

        this.wangEditor.create()
        this.wangEditor.txt.html(this.article.content)
      },
      handleTagClose(tag) {
        this.article.tags.splice(this.article.tags.indexOf(tag), 1);
      },
      cancelInputTag(open) {
        if (!open) {
          if (!this.inputTagValue) {
            console.log('失去焦点', this.inputTagValue)
            this.inputTagVisible = false
          }
        }
      },
      change(item) {
        if (item instanceof Object) {
          if (this.article.tags.filter(tag => {return tag.tag === item.label}).length > 0) {
            this.$message.error('该标签已存在')
            this.inputTagVisible = false
            return
          }
          this.article.tags.push({id: item.value, tag: item.label})
        } else {
          if (this.article.tags.filter(tag => {return tag.tag === item}).length > 0) {
            this.$message.error('该标签已存在')
            this.inputTagVisible = false
            return
          }
          this.article.tags.push({id: null, tag: item})
        }
        this.inputTagVisible = false
      },
      focusInput() {
        this.inputTagValue = ''
        this.options = []
      },
      handleSelect(item) {
        console.log(item)
      },
      showTagInput() {
        this.inputTagVisible = true;
        this.$nextTick(_ => {
          console.log(this.$refs.saveTagInput)
          this.$refs.saveTagInput.focus();
        });
      },
      mavonEditorChange(value, render) {
        this.article.content = render
      },
      imgAdd(pos, $file) {
        console.log(pos, $file)
        // 第一步.将图片上传到服务器.
        this.imgList[pos] = $file;

        let base64Data = $file.miniurl.replace(/^data:image\/\w+;base64,/, "")
        let filename = $file.name
        let params = {base64: base64Data, filename: filename}

        uploadFile(params).then((res) => {
          console.log('upload...')
          let _res = res.data.prefix + res.data.name;
          // 第二步.将返回的url替换到文本原位置![...](0) -> ![...](url)
          this.$refs.md.$img2Url(pos, _res);
        })
      },
      imgDel() {
        delete this.imgList[pos];
      },
      submit() {
        this.loading = true
        if (this.article.editType === 2) {
          this.article.content = this.wangEditor.txt.html()
          this.article.markdown = null
        }
        if (this.type === 1) {
          add(this.article).then(res => {
            if (res.status === 1) {
              this.$message.success('发布成功！')
              this.$router.push({name: 'articleList'})
            } else {
              this.$message.error('发布失败！')
              this.loading = false
            }
          }).catch(e => {
            this.loading = false
          })
        } else {
          edit(this.article).then(res => {
            if (res.status === 1) {
              this.$message.success('修改成功！')
              this.$router.push({name: 'articleList'})
            } else {
              this.$message.error('修改失败！')
              this.loading = false
            }
          }).catch(e => {
            this.loading = false
          })
        }

      },
      handleThumbSuccess(response, file, fileList) {
        this.$message.success('上传成功')
        console.log('success ', response)
        this.article.thumb = response.data.name
        this.article.thumbPreview = response.data.url
      },
      closeViewer() {
        document.body.style.overflow = null
        this.showViewer = false
      },
      previewThumb() {
        document.body.style.overflow = 'hidden'
        this.showViewer = true
      },
      deleteThumb() {
        delFileByName({name: this.article.thumb}).then(res => {
          this.$message.success('删除成功')
          this.article.thumbPreview = null
          this.article.thumb = null
        })
      },
      querySearchAsync(queryString) {
        searchTag({tag: queryString}).then(res => {
          if (res.data) {
            let result = res.data.map(item => {return {label: item.tag, value: item.id}})
            this.options = result
            return
          }
          this.options = []
        })
      },
    },
    mounted() {

    },
    created() {
      let id = this.$route.query.id

      if (id) {
        getArticle(id).then(res => {
          this.article = res.data
          this.type = 2
          if (this.article.editType === 2) {
            this.openWangEditor()
          }
        })
      }
    },
  }
</script>

<style scoped>
  .el-tag + .el-tag {
    margin-left: 10px;
  }
  .button-new-tag {
    margin-right: 10px;
    height: 32px;
    line-height: 30px;
    padding-top: 0;
    padding-bottom: 0;
  }
  .input-new-tag {
    width: 90px;
    margin-right: 10px;
    vertical-align: bottom;
  }
</style>
