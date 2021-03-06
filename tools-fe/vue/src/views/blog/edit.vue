<template>
  <div class="page-container">
    <el-form label-width="90px" label-position="right">
      <div>
        <el-row>
          <el-col :xs="24" :sm="12" :md="8" :lg="6">
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
          </el-col>
          <el-col :xs="24" :sm="12" :md="8" :lg="6">
            <el-form-item label="文章样式：">
              <el-select v-model="article.type" size="small">
                <el-option
                  v-for="item in blogEnums.articleTypeEnum"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-form-item label="标题：">
            <el-input v-model="article.title"></el-input>
          </el-form-item>
        </el-row>
        <el-row>
          <el-form-item label="别名：">
            <el-input v-model="article.alias"></el-input>
          </el-form-item>
        </el-row>
        <el-row>
          <el-form-item label="分类：">
            <el-tag
              :key="category.name"
              v-for="category in article.categories"
              closable
              :disable-transitions="false"
              @close="handleCategoryClose(category)" :class="category.color"
              effect="plain">
              {{category.name}}
            </el-tag>
            <el-select
              class="input-new-tag"
              v-if="inputCategoryVisible"
              v-model="inputCategoryValue"
              size="small"
              ref="saveCategoryInput"
              filterable
              default-first-option
              remote
              :remote-method="queryCategoryAsync"
              @change="changeCategory"
              @focus="focusCategoryInput"
              @visible-change="cancelCategoryInputTag">
              <el-option
                v-for="item in categoryOptions"
                :key="item.label"
                :label="item.label"
                :value="item">
              </el-option>
            </el-select>
            <el-button v-else class="button-new-tag" size="small" @click="showCategoryInput">+ 添加分类</el-button>
          </el-form-item>
        </el-row>
        <el-row>
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
            <el-button v-else class="button-new-tag" size="small" @click="showTagInput">+ 添加标签</el-button>
          </el-form-item>
        </el-row>
        <el-row>
          <el-col :span="6">
            <el-form-item label="封面：">
              <el-button-group v-if="article.thumb">
                <el-button type="primary" size="small" @click="previewThumb">预览</el-button>
                <el-button type="danger" size="small" @click="deleteThumb">删除</el-button>
              </el-button-group>
              <el-upload v-else
                         :action="uploadUrl"
                         :show-file-list="false"
                         :on-success = "handleThumbSuccess"
                         :on-error="handleThumbError"
                         with-credentials>
                <el-button size="small" type="primary">点击上传</el-button>
              </el-upload>
            </el-form-item>
          </el-col>
          <el-col :span="6" v-if="article.editType === 1">
            <el-form-item label="上传md：">
              <el-upload action="/"
                         :show-file-list="false"
                         with-credentials
                         :http-request="uploadMd">
                <el-button size="small" type="primary">点击上传</el-button>
              </el-upload>
            </el-form-item>
          </el-col>
        </el-row>
        <div v-if="article.editType === 1">
          <mavon-editor ref="md" v-model="article.markdown" @change="mavonEditorChange" @imgAdd="imgAdd" @imgDel="imgDel"/>
        </div>
        <div v-else>
          <span id="article"></span>
        </div>
        <div class="page-content">
          <el-button type="primary" @click="openBeforePublish" :disabled="loading">发布</el-button>
          <span v-if="autoSaveTime" style="float: right">最近自动保存于{{parseTime(autoSaveTime)}}</span>
        </div>
      </div>
    </el-form>
    <el-dialog title="设置发布属性" :visible.sync="beforePublishVisible">
      <el-form label-width="90px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="权限：">
              <el-select v-model="article.permission" placeholder="请选择" size="small" clearable>
                <el-option
                  v-for="item in blogEnums.articlePermissionEnum"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="优先级：">
              <el-input size="small" v-model="article.priority" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="允许评论：">
              <el-select v-model="article.comment" placeholder="请选择" size="small" clearable>
                <el-option
                  v-for="item in blogEnums.commentLimitEnum"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="状态：">
              <el-select v-model="article.status" size="small" clearable>
                <el-option
                  v-for="item in blogEnums.articleStatusEnum"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="orderPublishVisible">
            <el-form-item label="定时发布：">
              <el-date-picker
                size="small"
                v-model="article.orderPublishTime"
                type="datetime"
                placeholder="选择日期时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <div style="text-align: center">
            <el-button type="warning" icon="el-icon-check" size="small" @click="submit(false)">提交</el-button>
          </div>
        </el-row>
      </el-form>
    </el-dialog>
    <el-dialog title="有以下草稿，是否继续编辑" :visible.sync="draftVisible">
      <el-table :data="drafts" border width="100%" @current-change="choose" class="page-table">
        <el-table-column label="选择" width="70px">
          <template slot-scope="scope">
            <el-radio v-model="currentRowId" :label="scope.row.id"><i></i></el-radio>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" min-width="200px" show-overflow-tooltip/>
        <el-table-column label="分类" min-width="200px">
          <template slot-scope="scope">
            <span v-for="category in scope.row.categories">
              <span :class="category.color">{{category.name + ' '}}</span>
            </span>
          </template>
        </el-table-column>
        <el-table-column label="标签" min-width="300px" show-overflow-tooltip>
          <template slot-scope="scope">
            <span v-for="tag in scope.row.tags" style="white-space: nowrap;">{{tag.tag}}&nbsp</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" min-width="80px">
          <template slot-scope="scope">
            <el-tag effect="dark" v-if="scope.row.status === 0" type="danger">{{blogEnums.articleStatusEnum.getLabelByValue(scope.row.status)}}</el-tag>
            <el-tag effect="dark" v-if="scope.row.status === 1" type="success">{{blogEnums.articleStatusEnum.getLabelByValue(scope.row.status)}}</el-tag>
            <el-tag effect="dark" v-if="scope.row.status === 2" type="primary">{{blogEnums.articleStatusEnum.getLabelByValue(scope.row.status)}}</el-tag>
            <el-tag effect="dark" v-if="scope.row.status === 3" type="warning">{{blogEnums.articleStatusEnum.getLabelByValue(scope.row.status)}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="权限" min-width="80px">
          <template slot-scope="scope">
            {{blogEnums.articlePermissionEnum.getLabelByValue(scope.row.permission)}}
          </template>
        </el-table-column>
        <el-table-column label="优先级" prop="priority" min-width="80px">
        </el-table-column>
        <el-table-column label="允许评论" min-width="80px">
          <template slot-scope="scope">
            {{blogEnums.commentLimitEnum.getLabelByValue(scope.row.comment)}}
          </template>
        </el-table-column>
        <el-table-column label="编写类型" min-width="90px">
          <template slot-scope="scope">
            {{blogEnums.editTypeEnum.getLabelByValue(scope.row.editType)}}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="新建时间" min-width="160px"/>
        <el-table-column prop="modifyTime" label="修改时间" min-width="160px"/>
      </el-table>
      <el-row style="margin-top: 20px;text-align: right">
        <el-button size="small" type="primary" @click="closeDraft">关闭</el-button>
        <el-button size="small" type="primary" @click="editDraft">确认</el-button>
      </el-row>
    </el-dialog>
    <el-image-viewer v-if="showViewer"
                     :on-close="closeViewer"
                     :initialIndex="viewerIndex"
                     :url-list="previewList"></el-image-viewer>
  </div>
</template>

<script>
  import remoteLoad from "../../utils/remoteLoad";
  import {add, edit, getArticle, list, searchCategory, searchTag, uploadFile} from "../../api/blog";
  import {baseURL} from "../../utils/request";
  import ElImageViewer from 'element-ui/packages/image/src/image-viewer';
  import {delFileByName} from "../../api/file";
  import blogEnums from "../blog/blogEnums";
  import {parseTime} from '../../utils/index'

  export default {
    name: "edit",
    components: {ElImageViewer},
    data() {
      return {
        parseTime: parseTime,
        article: {
          type:1,
          editType: 2,
          title: '',
          alias: '',
          tags: [],
          content: '',
          markdown: null,
          thumb: '',
          thumbPreview: null,
          categories: [],
          status: blogEnums.articleStatusEnum.getValueByFiledName('normal'),
          permission: blogEnums.articlePermissionEnum.getValueByFiledName('all'),
          priority: 10,
          comment: blogEnums.commentLimitEnum.getValueByFiledName('enable'),
          orderPublishTime: null
        },
        orderPublishVisible: false,
        blogEnums: blogEnums,
        loading: false,
        options: [],
        categoryOptions: [],
        uploadUrl: baseURL + '/upload/blog',
        inputTagVisible: false,
        inputCategoryVisible: false,
        inputTagValue: null,
        inputCategoryValue: null,
        wangEditor: null,
        showViewer: false,
        type: 1,
        imgList: {},
        beforePublishVisible: false,
        draftVisible: false,
        previewList: [],
        viewerIndex: 0,
        images:[],
        drafts: [],
        currentRow: {},
        currentRowId: null,
        autoSaveTime: null
      }
    },
    watch: {
      'article.editType': function (value) {
        if (value === 2) {
          this.openWangEditor()
        }
        if (value === 1 && this.wangEditor !== null) {
          // this.article.markdown = this.wangEditor.txt.html()
          this.article.content = this.wangEditor.txt.html()
        }
      },
      'article.status': function (value) {
        this.orderPublishVisible = value === blogEnums.articleStatusEnum.getValueByFiledName('delay');
      },
      'article.type': function (value) {
        this.article.editType = value === blogEnums.articleTypeEnum.getValueByFiledName('images') ? null : 1
      }
    },
    methods: {
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

            // insertImgFn 可把图片插入到编辑器，传入图片 src ，执行函数即可
            insertImgFn(result.data.prefix + result.data.name)
          }
        }

        this.wangEditor.config.uploadVideoServer = this.uploadUrl
        this.wangEditor.config.uploadVideoName = 'file'
        this.wangEditor.config.withVideoCredentials = true
        this.wangEditor.config.uploadVideoHooks = {
          customInsert: function(insertImgFn, result) {
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
      handleCategoryClose(category) {
        this.article.categories.splice(this.article.categories.indexOf(category), 1);
      },
      cancelInputTag(open) {
        if (!open) {
          if (!this.inputTagValue) {
            this.inputTagVisible = false
          }
        }
      },
      cancelCategoryInputTag(open) {
        if (!open) {
          if (!this.inputCategoryValue) {
            this.inputCategoryVisible = false
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
      changeCategory(item) {
        if (item instanceof Object) {
          if (this.article.categories.filter(category => {return category.name === item.label}).length > 0) {
            this.$message.error('该标签已存在')
            this.inputCategoryVisible = false
            return
          }
          this.article.categories.push({id: item.value, name: item.label, color: item.color})
        }
        this.inputCategoryVisible = false
      },
      focusInput() {
        this.inputTagValue = ''
        this.options = []
      },
      focusCategoryInput() {
        this.inputCategoryValue = ''
        this.categoryOptions = []
      },
      showTagInput() {
        this.inputTagVisible = true;
        this.$nextTick(_ => {
          this.$refs.saveTagInput.focus();
        });
      },
      showCategoryInput() {
        this.inputCategoryVisible = true
        this.$nextTick(_ => {
          this.$refs.saveCategoryInput.focus();
        });
      },
      mavonEditorChange(value, render) {
        this.article.content = render
      },
      imgAdd(pos, $file) {
        // 第一步.将图片上传到服务器.
        this.imgList[pos] = $file;

        let base64Data = $file.miniurl.replace(/^data:image\/\w+;base64,/, "")
        let filename = $file.name
        let params = {base64: base64Data, filename: filename}

        uploadFile(params).then((res) => {
          let _res = res.data.prefix + res.data.name;
          // 第二步.将返回的url替换到文本原位置![...](0) -> ![...](url)
          this.$refs.md.$img2Url(pos, _res);
        })
      },
      imgDel() {
        delete this.imgList[pos];
      },
      openBeforePublish() {
        this.beforePublishVisible = true
        this.article.status = blogEnums.articleStatusEnum.getValueByFiledName('normal')
      },
      submit(autoSave) {
        this.loading = true
        if (this.article.editType !== null && this.article.editType === 2) {
          this.article.content = this.wangEditor.txt.html()
          this.article.markdown = null
        }
        if (!this.article.content) {
          if (this.article.status !== blogEnums.articleStatusEnum.getValueByFiledName('draft')) {
            this.$message.error('文章内容为空')
          }
          return
        }
        if (this.type === 1) {
          add(this.article).then(res => {
            if (autoSave) {
              this.type = 2
              this.article.id = res.data
              this.article.version = 0
              this.autoSaveTime = Date.now()
              this.loading = false
            } else {
              if (res.status === 1) {
                this.$message.success('发布成功！')
                this.$router.push({name: 'articleList'})
              } else {
                this.$message.error('发布失败！')
                this.loading = false
              }
            }
          }).catch(e => {
            this.loading = false
          })
        } else {
          edit(this.article).then(res => {
            if (autoSave) {
              this.article.version = this.article.version + 1
              this.autoSaveTime = Date.now()
              this.loading = false
            } else {
              if (res.status === 1) {
                this.$message.success('修改成功！')
                this.$router.push({name: 'articleList'})
              } else {
                this.$message.error('修改失败！')
                this.loading = false
              }
            }
          }).catch(e => {
            this.loading = false
          })
        }

      },
      handleThumbSuccess(response, file, fileList) {
        this.$message.success('上传成功')
        this.article.thumb = response.data.name
        this.article.thumbPreview = response.data.url
      },
      handleThumbError(err, file, fileList) {
        this.$message.error(JSON.parse(err.message).msg)
      },
      closeViewer() {
        document.body.style.overflow = null
        this.showViewer = false
        this.viewerIndex = 0
      },
      previewThumb() {
        document.body.style.overflow = 'hidden'
        this.showViewer = true
        this.previewList = [this.article.thumbPreview]
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
      queryCategoryAsync(queryString) {
        searchCategory({name: queryString}).then(res => {
          if (res.data) {
            let result = res.data.map(item => {return {label: item.name, value: item.id, color: item.color}})
            this.categoryOptions = result
          }
        })
      },
      uploadMd(params) {
        let that = this
        let reader = new FileReader()
        reader.readAsText(params.file)
        reader.onload = function () {
          that.article.markdown = reader.result
        }
      },
      choose (item) {
        if (item === null) {
          this.currentRow = null
          this.currentRowId = null
          return
        }
        this.currentRow = item
        this.currentRowId = item.id
      },
      closeDraft() {
        this.draftVisible = false
      },
      editDraft() {
        let id = this.currentRowId
        getArticle(id).then(res => {
          this.article = res.data
          this.type = 2
          if (this.article.editType === 2) {
            this.openWangEditor()
          }
          this.draftVisible = false
        })
      }
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
      } else {
        this.openWangEditor()
      }

      let queryDrafts = {status: blogEnums.articleStatusEnum.getValueByFiledName('draft')}
      list(queryDrafts).then(res => {
        this.drafts = res.data.list
        if (this.drafts.length > 0) {
          this.draftVisible = true
        }
      })
      let that = this
      var interval = setInterval(function () {
        if (!that.beforePublishVisible) {
          that.article.status = blogEnums.articleStatusEnum.getValueByFiledName('draft')
          that.submit(true)
        }
      }, 10000)
      this.$once('hook:beforeDestroy', () => {
        clearInterval(interval);
      })
    },
  }
</script>

<style scoped>
  .el-select {
    width: 100%;
  }

  .el-date-editor.el-input, .el-date-editor.el-input__inner {
    width: 100%;
  }

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

  /*Bootstrap color customize*/
  .text-primary{color:#5869DA!important;}
  .text-secondary{color:#2d3d8b!important;}
  .text-success{color:#09815C!important;}
  .text-danger{color:#e3363e!important;}
  .text-warning{color:#e38836!important;}
  .text-info{color:#4da7d4!important;}
  .text-light{color:#f8f9f9!important;}
  .text-dark{color:#000c2d!important;}
  .text-muted,
  .text-muted a{color:#687385!important;}
  .text-white{color:#FFFFFF!important;}

</style>
