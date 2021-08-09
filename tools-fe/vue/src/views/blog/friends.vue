<template>
  <div class="page-container">
    <div class="page-text">
      <span>友链管理</span>
    </div>
    <div class="page-content">
      <el-form label-width="90px">
        <el-row>
          <el-col :span="6">
            <el-form-item label="名称：">
              <el-input size="small" v-model="params.name" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="网址：">
              <el-input size="small" v-model="params.website" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="邮箱：">
              <el-input size="small" v-model="params.email" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="昵称：">
              <el-input size="small" v-model="params.nickname" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="状态：">
              <el-select v-model="params.status" placeholder="请选择" size="small" clearable>
                <el-option
                  v-for="item in blogEnums.friendsLinkEnum"
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
        <el-button size="small" type="primary" @click="create" icon="el-icon-plus">新建</el-button>
        <el-button size="small" type="success" @click="approve" icon="el-icon-check" :disabled="!currentRowId || currentRow.status === 2">通过</el-button>
        <el-button size="small" type="danger" @click="deny" icon="el-icon-close" :disabled="!currentRowId">拒绝</el-button>
      </el-row>
    </div>
    <div class="page-content">
      <el-table :data="data" border width="100%" @current-change="choose" class="page-table">
        <el-table-column label="选择" width="70px">
          <template slot-scope="scope">
            <el-radio v-model="currentRowId" :label="scope.row.id"><i></i></el-radio>
          </template>
        </el-table-column>
        <el-table-column label="网站名称" prop="name" width="150px" show-overflow-tooltip></el-table-column>
        <el-table-column label="网站地址" width="200px" show-overflow-tooltip>
          <template slot-scope="scope">
            <a :href="scope.row.website" style="color: #3a8ee6">{{scope.row.website}}</a>
          </template>
        </el-table-column>
        <el-table-column label="网站图标" prop="icon" width="80px">
          <template slot-scope="scope">
            <img :src="scope.row.icon" alt="" style="width: 20px;height: 20px">
          </template>
        </el-table-column>
        <el-table-column label="网站介绍" prop="introduction" width="200px" show-overflow-tooltip></el-table-column>
        <el-table-column label="友链状态" width="100px">
          <template slot-scope="scope">
            {{blogEnums.friendsLinkEnum.getLabelByValue(scope.row.status)}}
          </template>
        </el-table-column>
        <el-table-column label="申请邮箱" prop="email" width="150px" show-overflow-tooltip></el-table-column>
        <el-table-column label="申请人昵称" prop="nickname" width="100px" show-overflow-tooltip></el-table-column>
        <el-table-column label="不通过原因" prop="message" width="150px" show-overflow-tooltip></el-table-column>
        <el-table-column label="新建时间" prop="createTime" width="200px"></el-table-column>
        <el-table-column label="修改时间" prop="modifyTime" width="200px"></el-table-column>
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
      <el-form label-width="10%">
        <el-row>
          <el-col :span="24">
            <el-form-item label="名称：">
              <el-input size="small" v-model="createForm.name" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="网址：">
              <el-input size="small" v-model="createForm.website" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="邮箱：">
              <el-input size="small" v-model="createForm.email" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="昵称：">
              <el-input size="small" v-model="createForm.nickname" clearable></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="状态：">
              <el-select v-model="createForm.status" placeholder="请选择" size="small" clearable>
                <el-option
                  v-for="item in blogEnums.friendsLinkEnum"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="createForm.status === blogEnums.friendsLinkEnum.getValueByFiledName('deny')">
            <el-form-item label="原因：">
              <el-input size="small" v-model="createForm.message" clearable></el-input>
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
  import {addFriendsLink, getFriendsLink, updateFriendsLink} from "../../api/blog";
  import blogEnums from "./blogEnums";

  export default {
    name: "friends",
    data() {
      return {
        params: {
          name: null,
          website: null,
          email: null,
          nickname: null,
          status: null,
          limit: 10,
          currentPage: 1
        },
        data: [],
        currentRow: null,
        currentRowId: null,
        total: null,
        createVisible: false,
        createForm: {message: null},
        dialogTitle: '',
        blogEnums: blogEnums
      }
    },
    watch: {
      'createVisible': function (value) {
        if (!value) {
          this.createForm = {}
        }
      }
    },
    methods: {
      search() {
        getFriendsLink(this.params).then(res => {
          this.data = res.data.list
          this.total = res.data.totalCount
          this.params.currentPage = res.data.currentPage
        })
      },
      research() {
        this.params.currentPage = 1
        this.search()
      },
      choose(item) {
        if (item === null) {
          this.currentRow = null
          this.currentRowId = null
          return
        }
        this.currentRow = item
        this.currentRowId = item.id
      },
      create() {
        this.createVisible = true
        this.createForm = {}
        this.dialogTitle = '新建友链'
      },
      approve() {
        this.createVisible = true
        this.createForm = JSON.parse(JSON.stringify(this.currentRow))
        this.createForm.status = blogEnums.friendsLinkEnum.getValueByFiledName('approve')
        this.dialogTitle = '审核友链'
      },
      deny() {
        this.createVisible = true
        this.createForm = JSON.parse(JSON.stringify(this.currentRow))
        this.createForm.status = blogEnums.friendsLinkEnum.getValueByFiledName('deny')
        this.dialogTitle = '拒绝友链'
      },
      commitCreate() {
        if (this.createForm.status !== blogEnums.friendsLinkEnum.getValueByFiledName('deny')) {
          this.createForm.message = null
        }
        if (this.createForm === null || this.createForm.id === null) {
          addFriendsLink(this.createForm).then(res => {
            this.closeCreateDialog()
            this.$message.success('新建成功')
            this.research()
          })
        } else {
          updateFriendsLink(this.createForm).then(res => {
            this.closeCreateDialog()
            this.$message.success('操作成功')
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
