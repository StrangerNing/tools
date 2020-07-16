<template>
  <div class="page-container">
    <div class="page-text">
      <span>API管理</span>
    </div>
    <div class="page-content">
      <el-row>
        <el-col :xs="24" :sm="12" :lg="6">
          <el-input placeholder="请输入AK，只支持精确搜索" v-model="ak">
            <el-button slot="append" icon="el-icon-search" @click="searchApiKey"></el-button>
          </el-input>
        </el-col>
      </el-row>
      <el-row>
        <el-button type="primary" size="medium" style="margin-top: 20px;margin-bottom: 20px" @click="openCreateDialog('创建应用')">创建应用</el-button>
      </el-row>
      <el-table :data="akList" border>
        <el-table-column
          prop="id"
          label="应用编号">
        </el-table-column>
        <el-table-column
          prop="name"
          label="应用名称">
        </el-table-column>
        <el-table-column
          prop="ak"
          label="访问应用(AK)">
        </el-table-column>
        <el-table-column
          prop="remark"
          label="备注">
        </el-table-column>
        <el-table-column
          label="应用配置">
          <template slot-scope="scope">
            <el-button type="primary" size="small" @click="updateAk(scope.row)">修改</el-button>
            <el-button type="danger" size="small" @click="delAk(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog
      :title=createDialogTitle
      :visible.sync="createVisible" :before-close="closeCreateDialog">
      <el-form :model="createForm" label-width="20%">
        <el-form-item label="应用名称">
          <el-input v-model="createForm.name"></el-input>
        </el-form-item>
        <el-form-item label="启用服务">
          <el-input v-model="createForm.module"></el-input>
        </el-form-item>
        <el-form-item label="请求校验方式">
          <el-input v-model="createForm.limitType"></el-input>
        </el-form-item>
        <el-form-item label="IP名单">
          <el-input type="textarea" v-model="createForm.limitIp"></el-input>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="createForm.remark"></el-input>
        </el-form-item>
      </el-form>
      <div style="text-align: center">
        <el-button type="primary" @click="commitCreate">提交</el-button>
        <el-button type="primary" @click="closeCreateDialog">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {createApiKey, delApiKey, getApiKeyList, searchByKey, updateApiKey} from "../../api/user";

  export default {
    name: "index",
    data() {
      return {
        ak: '',
        akList: [],
        createForm: {
          name: '',
          module: '',
          limitType: '',
          limitIp: ''
        },
        createVisible: false,
        createDialogTitle: '创建应用'
      }
    },
    methods: {
      getAkList() {
        getApiKeyList().then(res => {
          this.akList = res.data
        })
      },
      openCreateDialog(title) {
        this.createDialogTitle = title
        this.createVisible = true
      },
      closeCreateDialog() {
        this.createVisible = false
        this.createForm = {}
      },
      commitCreate() {
        if (null == this.createForm.id) {
          createApiKey(this.createForm).then(res => {
            if (res.success) {
              this.closeCreateDialog()
              this.$alert('生成成功，该应用的AK为：\n' + res.data.ak + '\n请注意不要泄露AK以免被盗用造成账号封禁！', 'AK', {
                confirmButtonText: '确定',
                callback: action => {
                  this.getAkList()
                }
              })
            }
          })
        } else {
          updateApiKey(this.createForm).then(res => {
            if (res.data) {
              this.closeCreateDialog()
              this.getAkList()
              this.$message.success('修改成功！')
            } else {
              this.$message.error('修改失败！')
            }
          })
        }
      },
      delAk(id) {
        this.$confirm('删除操作不可恢复，是否继续？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          delApiKey(id).then(res => {
            if (res.data) {
              this.$message.success('删除成功！')
              this.getAkList()
            } else {
              this.$message.error(res.data)
            }
          })
        })
      },
      updateAk(row) {
        this.createDialogTitle = '修改应用配置'
        this.createForm = JSON.parse(JSON.stringify(row))
        this.createVisible = true
      },
      searchApiKey() {
        searchByKey({ak: this.ak}).then(res => {
          if (res.success) {
            this.akList = res.data
          } else {
            this.$message.error(res.data)
          }
        })
      }
    },
    created() {
      this.getAkList()
    }
  }
</script>

<style scoped>

</style>
