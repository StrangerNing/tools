<template>
  <div class="page-container">
    <div class="page-text">
      <span>短链接查询</span>
    </div>
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
        width="180">
      </el-table-column>
      <el-table-column
        prop="originUrl"
        label="原链接">
      </el-table-column>
      <el-table-column
        prop="createTime"
        label="创建时间">
      </el-table-column>
      <el-table-column
        prop="createName"
        label="创建人"
        width="120">
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="params.currentPage"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="params.limit"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total" style="margin-top: 20px">
    </el-pagination>
  </div>
</template>

<script>
  import {queryUrl} from "../../api/shortUrl";

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
      }
    },
    created() {
      this.queryUrl()
    }
  }
</script>

<style scoped>

</style>
