<template>
  <div class="page-container">
    <div class="page-content">
      <div>
        <el-upload
          class="upload-image"
          drag
          :action="uploadUrl"
          with-credentials
          :on-error = "handleError"
          :on-success = "handleSuccess"
          :show-file-list = false
          multiple>
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        </el-upload>
      </div>
      <div>
        <Waterfall
          ref="waterfall"
          :list="imageList"
          :gutter="10"
          :width="240"
          :breakpoints="{
            2400: {
              rowPerView: 6,
            },
            1200: { //当屏幕宽度小于等于1200
              rowPerView: 4,
            },
            800: { //当屏幕宽度小于等于800
              rowPerView: 3,
            },
            500: { //当屏幕宽度小于等于500
              rowPerView: 2,
            }
          }"
          animationEffect="fadeIn"
          animationDuration="1s"
          animationDelay="0.3s"
          backgroundColor="rgb(255, 255, 255)">
          <template
            slot="item"
            slot-scope="props"
          >
            <el-card class="card">
              <div
                class="cover"
                :style="initCardStyle(props)"
                @click="handlePictureCardPreview(props.data.id)"
              >
                <img
                  :src="props.data.url"
                  alt
                  @load="$refs.waterfall.refresh"
                >
              </div>

              <div class="name">
                <p>上传时间：{{props.data.createTime}}</p>
              </div>
              <div class="menus">
                <p
                  data-title="复制链接"
                  @click="handleCopy(props.data)"
                />
                <p
                  data-title="编辑"
                  @click="handleEdit(props.data)"
                />
                <p
                  data-title="删除"
                  @click="handleDelete(props.data)"
                />
              </div>
            </el-card>
          </template>
        </Waterfall>
      </div>
      <el-divider>
        <p v-if="loading" class="foot-tip">加载中...</p>
        <p v-if="noMore" class="foot-tip">没有更多了</p>
      </el-divider>
    </div>
    <el-dialog :visible.sync="dialogVisible">
      <el-image :src="dialogImageUrl"></el-image>
    </el-dialog>
    <el-backtop target=".main-container"></el-backtop>
  </div>
</template>

<script>
  import {delFile, getFile, getImageList} from "../../api/file";
  import {baseURL} from "../../utils/request";
  import Waterfall from "vue-waterfall-plugin";

  export default {
    name: "imageList",
    components: {Waterfall},
    data() {
      return {
        query: {
          currentPage: 1,
          limit: 10,
          style: 'image/resize,l_200,m_mfit'
        },
        uploadUrl: baseURL + '/upload/image',
        imageList: [],
        dialogImageUrl: null,
        dialogVisible: false,
        displaySetting: {
          1200: { //当屏幕宽度小于等于1200
            rowPerView: 4,
          },
          800: { //当屏幕宽度小于等于800
            rowPerView: 3,
          },
          500: { //当屏幕宽度小于等于500
            rowPerView: 2,
          }
        },


        colors: ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399'],
        list: [],
        loading: false,
        effect: 'fadeIn',
        duration: 1,
        delay: 0.3,
        isSetInitStyle: true,
        effectOptions: [
          {
            label: 'fadeIn',
            value: 'fadeIn'
          },
          {
            label: 'fadeInUp',
            value: 'fadeInUp'
          },
          {
            label: 'fadeInDown',
            value: 'fadeInDown'
          },
          {
            label: 'zoomIn',
            value: 'zoomIn'
          }
        ],
        isOpen: false,
        slideWidth: 200,
        boxWidth: 'auto',
        noMore: false
      }
    },
    computed: {
      disabled() {
        return this.loading || this.noMore;
      }
    },
    mounted() {
      this.isSetInitStyle = window.localStorage.getItem('isSetInitStyle')
        ? JSON.parse(window.localStorage.getItem('isSetInitStyle'))
        : false;

      let _self = this;

      window.addEventListener("scroll", function (e) {
        if (window.scrollY >= document.body.scrollHeight - document.body.clientHeight - 20) {
          if (!_self.loading && !_self.noMore) {
            _self.loading = true
            _self.load();
          }
        }
      });
    },
    methods: {
      search() {
        getImageList(this.query).then(res => {
          console.log(res.data.list)
          this.imageList = res.data.list
        })
      },
      handleError(err, file, fileList) {
        this.$message.error(JSON.parse(err.message).message)
      },
      handleSuccess(response, file, fileList) {
        this.$message.success('上传成功')
        console.log('success ', response)
        this.search()
      },
      handleRemove(file) {
        delFile(file.id).then(res => {
          this.$message.success('删除成功')
          this.imageList = this.imageList.filter(item => {return item.id !== file.id})
        })
      },
      handlePictureCardPreview(id) {
        getFile(id).then(res => {
          this.dialogImageUrl = res.data.url
          this.dialogVisible = true
        })
      },

      load() {
        setTimeout(() => {
          this.query.currentPage = this.query.currentPage + 1;
          getImageList(this.query).then(res => {
            console.log('加载第' + this.query.currentPage + '页')
            if (res.data.list.length === 0) {
              this.noMore = true
            }
            let list = res.data.list
            this.imageList = [...this.imageList,...list]
            this.loading = false;
          })
        }, 1500);
      },

      // 初始化卡片样式
      initCardStyle(props) {
        if (this.isSetInitStyle) {
          return {
            width: `${props.data.itemWidth - 20}px`,
            height: `${((props.data.itemWidth - 20) / props.data.width) *
            props.data.height}px`,
            backgroundColor: props.data.blankColor
          };
        }
      },
      /**
       * 编辑
       */
      handleEdit() {
        this.$message.success('编辑');
      },
      /**
       * 删除
       */
      handleDelete(item) {
        this.$alert('该操作不可恢复，是否确定？', '标题名称', {
          confirmButtonText: '确定',
          callback: action => {
            this.handleRemove(item)
          }
        });
      },
      handleCopy(item) {
        this.$copyText(item.prefix + item.name)
        this.$message.success('复制链接成功！')
      }
    },
    created() {
      this.search()
    }
  }
</script>

<style lang="scss">

  .card {
    background: #fff;
    border-radius: 5px;
    overflow: hidden;
    cursor: pointer;
    position: relative;
    transition: 0.2s;
    top: 0;
    &:hover {
      top: -3px;
    }
    .cover {
      margin: 10px 10px 0 10px;
      img {
        display: block;
        width: 100%;
      }
    }
    .name {
      background: #fff;
      color: #666;
      font-weight: 600;
      padding: 10px 20px;
      font-size: 14px;
    }
    .menus {
      padding: 10px;
      border-top: 1px solid #e7e7e7;
      text-align: right;
      p {
        position: relative;
        display: inline-block;
        padding: 4px 10px;
        text-decoration: none;
        text-align: center;
        cursor: pointer;
        user-select: none;
        color: white;
        font-size: 12px;
        margin-left: 10px;

        &::before {
          content: "";
          position: absolute;
          top: 0;
          left: 0;
          bottom: 0;
          right: 0;
          background: linear-gradient(135deg, #6e8efb, #a777e3);
          border-radius: 4px;
          transition: box-shadow 0.5s ease, transform 0.2s ease;
          will-change: transform;
          box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
          transform: translateY(var(--ty, 0)) rotateX(var(--rx, 0))
          rotateY(var(--ry, 0)) translateZ(var(--tz, -12px));
        }

        &:hover::before {
          box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
        }

        &::after {
          position: relative;
          display: inline-block;
          content: attr(data-title);
          transition: transform 0.2s ease;
          font-weight: bold;
          letter-spacing: 0.01em;
          will-change: transform;
          transform: translateY(var(--ty, 0)) rotateX(var(--rx, 0))
          rotateY(var(--ry, 0));
        }
      }
    }
  }

  .foot-tip {
    color: #7c7c7c;
  }

  .upload-image {
    text-align: center;
  }

</style>
