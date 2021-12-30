import "/static/js/vendor/timeago.js"
$('#featureMoreBtn').click(function () {
  let currentPage = $(this).data('id')
  let limit = $(this).data('limit')

  let template = ''
  for (let temp = 0; temp < limit; temp ++) {
    template += `<article class="col-lg-4 col-md-6 mb-30 wow template" data-wow-delay="0.2s">
                        <div class="post-card-1 border-radius-10 hover-up">
                            <div class="post-thumb img-hover-slide position-relative" style="background-color:rgb(22 29 39 / 12%)">
                                <a class="img-link"></a>
                            </div>
                            <div class="post-content p-30">
                                <div class="entry-meta meta-0 font-small mb-10">
                                </div>
                                <div class="d-flex post-card-content">
                                    <div style="height: 22px;width: 35%; background-color: rgb(22 29 39 / 12%);"></div>
                                    <div class="list-title nice-skeleton-block" style="height: 22px; background-color: rgb(22 29 39 / 12%);"></div>
                                    <div style="height: 22px;width: 45%; background-color: rgb(22 29 39 / 12%);"></div>
                                    <div class="entry-meta meta-1 float-left font-x-small">
                                        <span class="post-on time"></span>
<!--                                        <span class="time-reading has-dot">12 mins read</span>-->
                                        <span class="post-by has-dot"></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </article>`
  }
  $('#articleList').append(template)

  let params = {
    isSticky: false,
    currentPage: currentPage,
    limit: limit
  }
  let url = '/wapi/blog/article/more'
  getNextPage(params, url, 'get').then(res => {
    if (res.data.list) {
      let articleList = res.data.list
      let more = ''
      var timeagoInstance = timeago();// 实例

      for (let index = 0; index < articleList.length; index++) {
        more += `<article class="col-lg-4 col-md-6 mb-30 wow animated">
                        <div class="post-card-1 border-radius-10 hover-up">
                            <div class="post-thumb thumb-overlay img-hover-slide position-relative" style="background-image: url(`+ articleList[index].thumbPreview +`)">
                                <a class="img-link" href="` + articleList[index].alias + `"></a>
                                <ul class="social-share">
                                    <li><a href="#"><i class="elegant-icon social_share"></i></a></li>
                                    <li><a class="fb" href="#" title="Share on Facebook" target="_blank"><i class="elegant-icon social_facebook"></i></a></li>
                                    <li><a class="tw" href="#" target="_blank" title="Tweet now"><i class="elegant-icon social_twitter"></i></a></li>
                                    <li><a class="pt" href="#" target="_blank" title="Pin it"><i class="elegant-icon social_pinterest"></i></a></li>
                                </ul>
                            </div>
                            <div class="post-content p-30">
                                <div class="entry-meta meta-0 font-small mb-10">`
        if (articleList[index].categories) {
          for (let i = 0; i < articleList[index].categories.length; i++) {
            let category = articleList[index].categories[i]
            more += `<a href="` + category.href + `"><span class="post-cat text-uppercase ` + category.color + `">` + category.name + `</span></a>`
          }
        }
        more += `</div>
                    <div class="d-flex post-card-content">
                       <h5 class="post-title mb-20 font-weight-400">
                          <a href="` + articleList[index].alias + `">` + articleList[index].title +`</a>
                       </h5>
                       <div class="entry-meta meta-1 float-left font-x-small">
                          <span class="post-on time">`
        more += timeagoInstance.format(articleList[index].createTime, 'zh_CN') +`</span>
                          <span class="time-reading has-dot">` + articleList[index].minutes + ` mins read</span>
                          <span class="post-by has-dot">`+ articleList[index].views +` views</span>
                       </div>
                    </div>
                 </div>
               </div>
             </article>`
      }
      window.setTimeout(function() {
        $('.template').remove()
        $('#articleList').append(more)
      }, 2000);

      if (limit * currentPage > res.data.totalCount) {
        console.log('一滴也没有了')
        $('#featureMoreBtn').css('display', 'none')
      } else {
        $('#featureMoreBtn').data('id', currentPage + 1)
      }
    }
    console.log(res)
  })
})

const getNextPage = async (data, url, method)=> await getAjax(data, url, method)
