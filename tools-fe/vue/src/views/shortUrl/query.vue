<template>
  <div class="page-container">
    <div class="page-text">
      <span>短链接查询</span>
    </div>
    <div class="page-content">
      <el-table
        :data="urlList" border
        style="width: 100%">
        <el-table-column
          type="index"
          :index="indexMethod"
          width="50">
        </el-table-column>
        <el-table-column
          prop="shortUrl"
          label="短链接"
          width="250px">
          <template slot-scope="scope">
            <a v-if="scope.row.status === 1" style="color: #1482f0" target="_blank" :href="scope.row.shortUrl">{{scope.row.shortUrl}}</a>
            <a v-else style="color: red" target="_blank" :href="scope.row.shortUrl">{{scope.row.shortUrl}}</a>
          </template>
        </el-table-column>
        <el-table-column
          prop="originUrl"
          label="原链接"
          min-width="200">
        </el-table-column>
        <el-table-column
          prop="createTime"
          label="创建时间"
          width="160">
        </el-table-column>
        <el-table-column
          prop="createName"
          label="创建人"
          width="120">
        </el-table-column>
        <el-table-column
          label="操作"
          width="200px">
          <template slot-scope="scope">
            <el-button type="primary" size="small" @click="lookup(scope.row)">查看</el-button>
            <el-button v-if="scope.row.status === 1" type="warning" size="small" @click="updateShortUrl(scope.row, 0)">禁用</el-button>
            <el-button v-else type="success" size="small" @click="updateShortUrl(scope.row, 1)">启用</el-button>
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
  </div>
</template>

<script>
  import {queryUrl, updateUrl} from "../../api/shortUrl";

  export default {
    name: "query",
    data() {
      return {
        params: {
          currentPage: 1,
          limit: 10
        },
        total: 0,
        urlList: []
      }
    },
    methods: {
      queryUrl() {
        queryUrl(this.params).then(res => {
          this.urlList = res.data.list
          this.total = res.data.totalCount
        })
      },
      handleSizeChange(val) {
        this.params.limit = val
        this.params.currentPage = 1
        this.queryUrl()
      },
      handleCurrentChange(val) {
        this.params.currentPage = val
        this.queryUrl()
      },
      indexMethod(index) {
        return (this.params.currentPage - 1) * this.params.limit + (index + 1)
      },
      updateShortUrl(row, value) {
        let params = JSON.parse(JSON.stringify(row))
        params.status = value
        updateUrl(params).then(res => {
          if (res.data) {
            this.$message.success('操作成功！')
          }else {
            this.$message.error('操作失败！')
          }
          this.queryUrl()
        })
      },
      lookup(row) {
        console.log(row)
        this.$router.push({
          name: "statistics",
          params: {
            id: row.id
          }
        })
      }
    },
    created() {
      this.queryUrl()
    }
  }
</script>

<style scoped>

</style>
