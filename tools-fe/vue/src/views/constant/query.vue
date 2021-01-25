<template>
  <div class="page-container">
    <div class="page-text">
      全局变量管理
    </div>
    <div>
      <span style="color: #5a5e66">请注意，变量名应与项目内全局变量名一致！修改变量值提交后请点击应用按钮生效！</span>
    </div>
    <div class="page-content">
      <el-row>
        <el-button type="primary" size="small" icon="el-icon-plus" @click="showUpdateDialog(null)">新建</el-button>
        <el-button type="danger" size="small" icon="el-icon-refresh" @click="updateAllConstants">应用</el-button>
      </el-row>
      <el-table
        border
        :data="constantList"
        style="width: 100%; margin-top: 20px">
        <el-table-column
          type="index"
          width="50">
        </el-table-column>
        <el-table-column
          prop="name"
          label="变量名"
          width="200">
          <template slot-scope="scope">
            <span v-if="scope.row.status === 0" style="color: red">
              {{scope.row.name}}
            </span>
            <span v-else>
              {{scope.row.name}}
            </span>
          </template>
        </el-table-column>
        <el-table-column
          prop="value"
          label="变量值">
        </el-table-column>
        <el-table-column
          prop="remark"
          label="备注">
        </el-table-column>
        <el-table-column
          prop="modifyTime"
          label="最后修改时间"
          width="160">
        </el-table-column>
        <el-table-column
          prop="modifyName"
          label="最后修改人"
          width="120">
        </el-table-column>
        <el-table-column label="操作" width="200px">
          <template slot-scope="scope">
            <div>
              <el-button size="small" type="primary" @click="showUpdateDialog(scope.row)">修改</el-button>
              <el-button v-if="scope.row.status === 1" size="small" type="warning" @click="enableOrDisable(scope.row, 0)">禁用</el-button>
              <el-button v-else size="small" type="success" @click="enableOrDisable(scope.row, 1)">启用</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
      <el-row class="hidden-xs-only">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="params.currentPage"
          :page-sizes="[10, 20, 50, 100]"
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
    <el-dialog :title="title" :visible.sync="updateDialogVisible" :fullscreen="isFullScreen">
      <el-form label-width="100px">
        <el-form-item label="变量名">
          <el-input v-model="temp.name"></el-input>
        </el-form-item>
        <el-form-item label="变量值">
          <el-input v-model="temp.value"></el-input>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="temp.remark"></el-input>
        </el-form-item>
        <div style="text-align: center">
          <el-button type="primary" icon="el-icon-close" @click="updateDialogVisible=false">取消</el-button>
          <el-button type="primary" icon="el-icon-upload" @click="updateConstants">提交</el-button>
        </div>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
  import {getConstantList, updateConstants, updateAllConstants, addConstant} from "../../api/constant";
  import fullScreen from "../../utils/fullscreen";

  export default {
    name: "query",
    mixins: [fullScreen],
    data() {
      return {
        constantList: [],
        params: {
          currentPage: 1,
          limit: 10
        },
        total: 0,
        title: '新建变量',
        updateDialogVisible: false,
        temp: {},
        isFullScreen: true
      }
    },
    methods: {
      getConstantList() {
        getConstantList(this.params).then(res => {
          this.constantList = res.data.list
            this.total = res.data.totalCount
        })
      },
      updateAllConstants() {
        updateAllConstants().then(res => {
          if (res.status === 1) {
            this.$message.success("应用成功！")
          }
        })
      },
      updateConstants() {
        if (this.temp.id === null || this.temp.id === undefined) {
          addConstant(this.temp).then(res => {
            this.alert(res)
          })
        } else {
          updateConstants(this.temp).then(res => {
            this.alert(res)
          })
        }
      },
      alert(res) {
        if (res.data) {
          this.$message.success("修改成功！")
          this.getConstantList()
          this.updateDialogVisible = false
        } else {
          this.$message.error("修改失败，请刷新重试")
        }
      },
      enableOrDisable(row, val) {
        this.temp = JSON.parse(JSON.stringify(row))
        this.temp.status = val
        this.updateConstants()
      },
      showUpdateDialog(temp) {
        this.title = temp === null ? '新建全局常量' : '修改全局变量'
        this.temp = temp === null ? {name: '', value: '', remark: ''} : JSON.parse(JSON.stringify(temp))
        this.updateDialogVisible = true
      },
      handleSizeChange(val) {
        this.params.limit = val
        this.params.currentPage = 1
        this.getConstantList()
      },
      handleCurrentChange(val) {
        this.params.currentPage = val
        this.getConstantList()
      }
    },
    created() {
      this.getConstantList()
      this.checkFullScreen()
    }
  }
</script>

<style scoped>

</style>
