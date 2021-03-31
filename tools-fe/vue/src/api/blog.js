import request from '@/utils/request'

export function add (data) {
  return request({
    url: '/wapi/blog/article/add',
    method: 'post',
    data: data
  })
}

export function edit(data) {
  return request({
    url: '/wapi/blog/article/edit',
    method: 'post',
    data: data
  })
}

export function deleteArticle(params) {
  return request({
    url: '/wapi/blog/article/del/' + params,
    method: 'get'
  })
}

export function searchTag(param) {
  return request({
    url: '/wapi/blog/tag/search',
    method: 'get',
    params: param
  })
}

export function searchCategory(param) {
  return request({
    url: '/wapi/blog/category/search',
    method: 'get',
    params: param
  })
}

export function list(params) {
  return request({
    url: '/wapi/blog/article/list',
    method: 'get',
    params: params
  })
}

export function getArticle(params) {
  return request({
    url: '/wapi/blog/article/detail/' + params,
    method: 'get'
  })
}

export function uploadFile(data) {
  return request({
    url: '/upload/blog/base64',
    method: 'post',
    data: data
  })
}

export function refreshIndex() {
  return request({
    url: '/wapi/blog/index/refresh',
    method: 'get'
  })
}
