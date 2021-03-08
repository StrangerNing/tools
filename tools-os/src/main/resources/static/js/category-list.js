import "https://cdn.bootcss.com/timeago.js/3.0.2/timeago.js"

$('.page-item').click(function () {
  console.log($(this).data('current'))
  let current = $(this).data('current')
  let to = $(this).data('to')
  let limit = $(this).data('limit')
  if (current === to) {
    return
  }

  let template = ''
  for (let i = 0; i < limit; i++) {
    template += `<div class="post-module-3">
                            <div class="loop-list loop-list-style-1">
                                <article class="hover-up-2 transition-normal wow fadeInUp animated">
                                    <div class="row mb-40 list-style-2">
                                        <div class="col-md-4">
                                            <div class="post-thumb position-relative border-radius-5">
                                                <div class="img-hover-slide border-radius-5 position-relative" style="background-color:rgb(22 29 39 / 12%)">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-8 align-self-center">
                                            <div class="post-content">
                                                <div class="entry-meta meta-0 font-small mb-10">
                                                    <span class="post-on time loading-shadow loading-inline short" ></span>
                                                </div>
                                                <h5 class="post-title font-weight-900 mb-20">
                                                    <div class="list-title nice-skeleton-block loading-shadow title"></div>
<!--                                                    <span class="post-format-icon"><i class="elegant-icon icon_star_alt"></i></span>-->
                                                </h5>
                                                <div class="entry-meta meta-1 float-left font-x-small text-uppercase">
                                                    <span class="post-on time loading-shadow loading-inline long" ></span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </article>
                            </div>
                        </div>`
  }
  $('.single-article > article').remove()
  $('.single-article').append(template)

  let pagination = `<li class="page-item"><a class="page-link" href="#"></a></li>
                   <li><a class="page-link" href="#"></a></li>
                   <li><a class="page-link" href="#"></a></li>
                   <li><a class="page-link" href="#"></a></li>
                   <li><a class="page-link" href="#"></a></li>
                   <li><a class="page-link" href="#"></a></li>
                   <li class="page-item"><a class="page-link" href="#"></a></li>`
  $('.pagination > li').remove()
  $('.pagination').append(pagination)

  let params = {
    currentPage: to,
    limit: limit
  }
  let url = '/wapi/list'
  getNextPage(params, url, 'get').then(res => {
    if (res.data.list) {
      let articleList = res.data.list
      let more = ''
      var timeagoInstance = timeago();// ÊµÀý

      let template = ''
      for (let temp = 0; temp < articleList.length; temp++) {

      }
    }
  })
})

const getNextPage = async (data, url, method)=> await getAjax(data, url, method)
