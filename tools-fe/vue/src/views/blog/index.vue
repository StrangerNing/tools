<template>
  <div class="page-container">
    <div class="page-text">
      <span>文章列表</span>
    </div>
    <div class="page-content">
      <el-form label-width="90px">
        <el-row>
          <el-col :span="6">
            <el-form-item label="名称：">
              <el-input size="small" v-model="params.title" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="标签：">
              <el-select
                class="input-new-tag"
                v-model="params.tag"
                ref="saveTagInput"
                size="small"
                filterable
                default-first-option
                remote
                clearable
                :remote-method="querySearchAsync">
                <el-option
                  v-for="item in options"
                  :key="item.label"
                  :label="item.label"
                  :value="item.label">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="分类：">
              <el-select
                class="input-new-tag"
                v-model="params.category"
                size="small"
                filterable
                default-first-option
                remote
                clearable
                :remote-method="queryCategorySearchAsync">
                <el-option
                  v-for="item in categoryOptions"
                  :key="item.label"
                  :label="item.label"
                  :value="item.label">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="状态：">
              <el-select v-model="params.status" size="small" clearable>
                <el-option
                  v-for="item in blogEnums.articleStatusEnum"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <el-form-item label="权限：">
              <el-select v-model="params.permission" placeholder="请选择" size="small" clearable>
                <el-option
                  v-for="item in blogEnums.articlePermissionEnum"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="优先级：">
              <el-input size="small" v-model="params.priority" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="允许评论：">
              <el-select v-model="params.comment" placeholder="请选择" size="small" clearable>
                <el-option
                  v-for="item in blogEnums.commentLimitEnum"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="编写类型：">
              <el-select v-model="params.editType" placeholder="请选择" size="small" clearable>
                <el-option
                  v-for="item in blogEnums.editTypeEnum"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <div style="text-align: center">
            <el-button type="warning" icon="el-icon-search" size="small" @click="research">查询</el-button>
          </div>
        </el-row>
      </el-form>
    </div>
    <div class="page-content">
      <el-row>
        <el-button size="small" type="primary" @click="edit" icon="el-icon-edit" :disabled="!currentRowId">编辑</el-button>
        <el-button size="small" type="primary" @click="preview" icon="el-icon-s-promotion" :disabled="!currentRowId">预览</el-button>
        <el-button size="small" type="danger" @click="deleteArticle" icon="el-icon-delete" :disabled="deleteButtonDisable">删除</el-button>
      </el-row>
    </div>
    <div class="page-content">
      <el-table :data="data" border width="100%" @current-change="choose" class="page-table">
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
        <el-table-column label="标签" min-width="300px">
          <template slot-scope="scope">
            <el-tag
              :key="tag.tag"
              v-for="tag in scope.row.tags" style="margin-right: 5px;margin-top: 5px">
              {{tag.tag}}
            </el-tag>
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
      <el-row class="hidden-xs-only">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="params.currentPage"
          :page-sizes="[5, 10, 20, 50]"
          :page-size="params.limit"
          layout="total,sizes, prev, pager, next, jumper"
          :total="total" style="margin-top: 20px">
        </el-pagination>
      </el-row>
      <el-row class="hidden-sm-and-up">
        <el-pagination
          small
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="params.currentPage"
          :page-size="params.limit"
          layout="total, prev, pager, next"
          :total="total" style="margin-top: 20px">
        </el-pagination>
      </el-row>
    </div>
    <el-dialog title="预览" fullscreen :visible.sync="previewVisible" @opened="insertHtml">
      <div ref="preview"></div>
    </el-dialog>
  </div>
</template>

<script>
  import {deleteArticle, list, searchCategory, searchTag} from "../../api/blog";
  import blogEnums from "../constant/blogEnums";
  import "mavon-editor/dist/css/index.css"

  export default {
    name: "index",
    data() {
      return {
        params: {
          currentPage: 1,
          limit: 5
        },
        currentRowId: null,
        currentRow: {},
        deleteButtonDisable: true,
        data: [],
        options:[],
        categoryOptions:[],
        total: 0,
        blogEnums: blogEnums,
        previewVisible: false
      }
    },
    methods: {
      search() {
        list(this.params).then(res => {
          this.data = res.data.list
          this.total = res.data.totalCount
          this.params.currentPage = res.data.currentPage
        })
      },
      research() {
        this.params.currentPage = 1
        this.search()
      },
      edit() {
        this.$router.push({name: 'editArticle', query: {id: this.currentRowId}})
      },
      preview() {
        this.previewVisible = true
        console.log(this.$refs.preview)

      },
      insertHtml() {
        console.log(this.$refs.preview)
        this.$refs['preview'].innerHTML = this.currentRow.content
      },
      deleteArticle() {
        deleteArticle(this.currentRowId).then(res => {
          if (res.status === 1) {
            this.$message.success('删除成功')
            this.search()
          }
        })
      },
      choose (item) {
        if (item === null) {
          this.currentRow = null
          this.currentRowId = null
          this.deleteButtonDisable = true
          return
        }
        this.currentRow = item
        this.currentRowId = item.id
        this.deleteButtonDisable = item.status === 0
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
      queryCategorySearchAsync(queryString) {
        searchCategory({name: queryString}).then(res => {
          if (res.data) {
            let result = res.data.map(item => {return {label: item.name, value: item.id}})
            this.categoryOptions = result
            return
          }
          this.categoryOptions = []
        })
      },
      handleSizeChange(val) {
        this.params.limit = val
        this.params.currentPage = 1
        this.search()
      },
      handleCurrentChange(val) {
        this.params.currentPage = val
        this.search()
      },
    },
    mounted() {
      this.search()
    }
  }
</script>

<style scoped>
.el-select {
  width: 100%;
}
.page-table {
  overflow: scroll;
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
