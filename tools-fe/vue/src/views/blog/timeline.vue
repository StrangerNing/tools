<template>
  <div class="page-container">
    <div class="page-text">
      <span>博客大事件</span>
    </div>
    <div class="page-content">
      <el-form label-width="90px">
        <el-row>
          <el-col :span="6">
            <el-form-item label="标题：">
              <el-input size="small" v-model="params.title" clearable></el-input>
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
        <el-button size="small" type="primary" @click="create" icon="el-icon-plus">新建</el-button>
        <el-button size="small" type="warning" @click="modify" icon="el-icon-edit" :disabled="!currentRowId">修改</el-button>
        <el-button size="small" type="danger" @click="deleteItem" icon="el-icon-close" :disabled="!currentRowId">删除</el-button>
        <el-button size="small" type="success" @click="recoverItem" icon="el-icon-check" :disabled="!currentRowId">恢复</el-button>
      </el-row>
    </div>
    <div class="page-content">
      <el-table :data="data" border width="100%" @current-change="choose" class="page-table">
        <el-table-column label="选择" width="70px">
          <template slot-scope="scope">
            <el-radio v-model="currentRowId" :label="scope.row.id"><i></i></el-radio>
          </template>
        </el-table-column>
        <el-table-column label="标题" prop="title" width="150px" show-overflow-tooltip></el-table-column>
        <el-table-column label="描述" prop="description" width="250px" show-overflow-tooltip></el-table-column>
        <el-table-column label="类型"width="150px" show-overflow-tooltip>
          <template slot-scope="scope">
            {{blogEnums.timelineTypeEnum.getLabelByValue(scope.row.type)}}
          </template>
        </el-table-column>
        <el-table-column label="状态" width="150px" show-overflow-tooltip>
          <template slot-scope="scope">
            {{blogEnums.timelineStatusEnum.getLabelByValue(scope.row.status)}}
          </template>
        </el-table-column>
        <el-table-column label="发布时间" width="150px" show-overflow-tooltip>
          <template slot-scope="scope">
            {{formatDate(scope.row.publishTime)}}
          </template>
        </el-table-column>
        <el-table-column label="新建时间" prop="createTime" width="200px">
          <template slot-scope="scope">
            {{formatDate(scope.row.createTime, 'yyyy-MM-dd HH:mm:ss')}}
          </template>
        </el-table-column>
        <el-table-column label="修改时间" prop="modifyTime" width="200px">
          <template slot-scope="scope">
            {{formatDate(scope.row.modifyTime, 'yyyy-MM-dd HH:mm:ss')}}
          </template>
        </el-table-column>
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
    <el-dialog :title="dialogTitle" :visible.sync="createVisible">
      <el-form label-width="15%">
        <el-row>
          <el-col :span="24">
            <el-form-item label="标题：">
              <el-input size="small" v-model="createForm.title" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="描述：">
              <el-input size="small" v-model="createForm.description" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="类型：">
              <el-select v-model="createForm.type" placeholder="请选择" size="small" clearable>
                <el-option
                  v-for="item in blogEnums.timelineTypeEnum"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="状态：">
              <el-select v-model="createForm.status" placeholder="请选择" size="small" clearable>
                <el-option
                  v-for="item in blogEnums.timelineStatusEnum"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="发布时间：">
              <el-date-picker
                v-model="createForm.publishTime"
                size="small"
                type="date"
                placeholder="选择日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div style="text-align: center">
        <el-button type="primary" @click="commitCreate">提交</el-button>
        <el-button type="primary" @click="closeCreateDialog">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {addTimelineEvent, getTimelineList, updateTimelineEvent} from "../../api/blog";
  import blogEnums from "./blogEnums";
  import {formatDate} from "element-ui/lib/utils/date-util";

  export default {
    name: "timeline",
    data() {
      return {
        params: {
          title: null,
          limit: 10,
          currentPage: 1
        },
        data: [],
        currentRow: null,
        currentRowId: null,
        total: null,
        createVisible: false,
        dialogTitle: null,
        createForm: {},
        blogEnums: blogEnums,
        formatDate: formatDate
      }
    },
    methods: {
      choose(item) {
        if (item === null) {
          this.currentRow = null
          this.currentRowId = null
          return
        }
        this.currentRow = item
        this.currentRowId = item.id
      },
      search() {
        getTimelineList(this.params).then(res => {
          this.data = res.data.list
          this.total = res.data.totalCount
          this.params.currentPage = res.data.currentPage
        })
      },
      research() {
        this.params.currentPage = 1
        this.search()
      },
      create() {
        this.createVisible = true
        this.createForm = {}
        this.dialogTitle = '新建事件'
      },
      modify() {
        this.createVisible = true
        this.createForm = JSON.parse(JSON.stringify(this.currentRow))
        this.dialogTitle = '修改事件'
      },
      deleteItem() {
        this.createForm = JSON.parse(JSON.stringify(this.currentRow))
        this.createForm.status = 0
        updateTimelineEvent(this.createForm).then(res => {
          this.$message.success('新建成功')
          this.research()
        })
      },
      recoverItem() {
        this.createForm = JSON.parse(JSON.stringify(this.currentRow))
        this.createForm.status = 1
        updateTimelineEvent(this.createForm).then(res => {
          this.$message.success('新建成功')
          this.research()
        })
      },
      commitCreate() {
        if (this.createForm.id) {
          updateTimelineEvent(this.createForm).then(res => {
            this.closeCreateDialog()
            this.$message.success('操作成功')
            this.research()
          })
        } else {
          addTimelineEvent(this.createForm).then(res => {
            this.closeCreateDialog()
            this.$message.success('新建成功')
            this.research()
          })
        }
      },
      closeCreateDialog() {
        this.createVisible = false
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

</style>
