import request from '@/utils/request'

export function submitUrl(param) {
  return request({
    url: '/wapi/url/save',
    method: 'post',
    data: param
  })
}

export function queryUrl(params) {
  return request({
    url: '/wapi/url/query',
    method: 'get',
    params: params
  })
}

export function updateUrl(data) {
  return request({
    url: '/wapi/url/update',
    method: 'post',
    data: data
  })
}

export function getUrlStatistics(params) {
  return request({
    url: '/wapi/url/statistics',
    method: 'get',
    params: params
  })
}
