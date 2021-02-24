import request from '@/utils/request'

export function add (data) {
  return request({
    url: '/blog/article/add',
    method: 'post',
    data: data
  })
}

export function edit(data) {
  return request({
    url: '/blog/article/edit',
    method: 'post',
    data: data
  })
}

export function deleteArticle(params) {
  return request({
    url: '/blog/article/del/' + params,
    method: 'get'
  })
}

export function searchTag(param) {
  return request({
    url: '/blog/tag/search',
    method: 'get',
    params: param
  })
}

export function list(params) {
  return request({
    url: '/blog/article/list',
    method: 'get',
    params: params
  })
}

export function getArticle(params) {
  return request({
    url: '/blog/article/detail/' + params,
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
