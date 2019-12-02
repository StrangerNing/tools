<template>
  <div class="page-container">
    <div class="page-text">
      <span>链接统计</span>
    </div>
    <div id="map" style="width: 600px;height: 400px"></div>
  </div>
</template>

<script>
  import {getUrlStatistics} from "../../api/shortUrl";

  export default {
    name: "statistics",
    data() {
      return {
        data: [],
        params: {
          urlId: this.$route.params.id
        }
      }
    },
    methods: {
      getStatistics() {
        getUrlStatistics(this.params).then(res => {
          if (res.data) {
            let that = this
            let fields = ['lng', 'lat', 'count']
            res.data.forEach(function (item) {
              let row = []
              fields.forEach(function (field) {
                row.push(item[field])
              })
              that.data.push(row)
            })
            console.log(this.data)
            this.drawMap()
          }
        })
      },
      drawMap() {
        let mapChart = this.$echarts.init(document.getElementById("map"));
        let that = this
        let option = {
          backgroundColor:'#fff',
          tooltip: {
            trigger: "item",
            formatter: function (params, ticket, callback) {
              console.log(params)
              return '点击数：' + params.data[2]
            }
          },
          geo: {
            map: "china",
            right: "2%",
            left: "30%",
            label: {
              normal: {
                show: true,
                color: "#fff"
              },
              emphasis: {
                show: false
              }
            },
            roam: true,
            itemStyle: {
              normal: {
                borderWidth: 1,
                areaColor: "rgba(128, 128, 128, 0)",
                borderColor: "#3CC3FF"
              },
              emphasis: {
                areaColor: "#3EF3F4"
              }
            }
          },
          series: [
            {
              name: "连接数",
              type: "effectScatter",
              coordinateSystem: "geo",
              data: that.data,
              //标记大小，地图上的圆点
              symbolSize: 12,
              showEffectOn: "render",
              rippleEffect: {
                brushType: "stroke"
              },
              hoverAnimation: true,
              label: {
                //地图黄点显示内容
                normal: {
                  formatter: "{b}",
                  position: "right",
                  show: true
                }
              },
              itemStyle: {
                normal: {
                  color: "#f4e925",
                  shadowBlur: 10,
                  shadowColor: "#333"
                }
              },
              zlevel: 1
            }
          ]
        };
        mapChart.setOption(option);
        mapChart.resize();
      }
    },
    mounted() {
      this.getStatistics()
    }
  }
</script>

<style scoped>

</style>
