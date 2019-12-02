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

export function updateUrl(data) {
  return request({
    url: '/url/update',
    method: 'post',
    data: data
  })
}

export function getUrlStatistics(params) {
  return request({
    url: 'url/statistics',
    method: 'get',
    params: params
  })
}
