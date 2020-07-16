import request from '@/utils/request'

export function login (data) {
  return request({
    url: '/user/login',
    method: 'get',
    params: data
  })
}

export function getInfo () {
  return request({
    url: '/user/info',
    method: 'get'
  })
}

export function logout () {
  return request({
    url: '/user/logout',
    method: 'post'
  })
}

export function getApiKeyList() {
  return request({
    url: '/user/api/list',
    method: 'get'
  })
}

export function createApiKey(data) {
  return request({
    url: '/user/api/create',
    method: 'post',
    data: data
  })
}

export function delApiKey(params) {
  return request({
    url: '/user/api/del/' + params,
    method: 'get'
  })
}

export function updateApiKey(data) {
  return request({
    url: '/user/api/update',
    method: 'post',
    data: data
  })
}

export function searchByKey(param) {
  return request({
    url: '/user/api/search',
    method: 'get',
    params: param
  })
}
