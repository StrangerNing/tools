import request from '@/utils/request'

export function submitUrl(param) {
  return request({
    url: '/url/save',
    method: 'post',
    data: param
  })
}

export function queryUrl(params) {
  return request({
    url: '/url/query',
    method: 'get',
    params: params
  })
}
