import getAjax from '/static/js/vendor/getAjax.js'

const getTimelineByYear = async (data, url, method)=> await getAjax(data, url, method)

var currentPage = 1
var limit = 20
var hasMore = true
var year = 0

function getTimeline() {
  let params = {
    currentPage: currentPage,
    limit: limit,
    status: 1
  }
  let url = '/wapi/blog/timeline/list'
  let template = ''
  $('#moreBtn').text('正在加载')
  getTimelineByYear(params, url, 'get').then(res => {
    let timelineList = res.data.list
    for (let i = 0; i < timelineList.length; i++) {
      let timeline = timelineList[i]
      let publishTime = new Date(timeline.publishTime)
      if (publishTime.getFullYear() !== year) {
        year = publishTime.getFullYear()
        template += '<li class="title">' + year + '</li>'
      }
      let formatPublishTime = publishTime.toLocaleDateString()
      template += '<li>' +
        ' <span class="timer">'+ formatPublishTime + '</span>' +
        ' <span class="dot ' + timeline['typeBg'] + '"></span>' +
        '   <div class="content">' +
        '     <h3 class="subtitle">' + timeline.title + '</h3>' +
        '     <p>' + timeline.description + '</p>' +
        '   </div>' +
        '</li>'
    }
    $('#more').before(template)
    if (limit * currentPage >= res.data.totalCount) {
      $('#moreBtn').text('没有了～')
      hasMore = false
    } else {
      $('#moreBtn').text('查看更多')
      currentPage++
    }
  })
}

$('#moreBtn').click(function () {
  if (hasMore) {
    getTimeline()
  }
})

getTimeline()

