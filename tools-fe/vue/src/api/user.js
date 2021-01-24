import request from '@/utils/request'

export function login (data) {
  return request({
    url: '/user/login',
    method: 'get',
    params: data
  })
}

export function register(data) {
  return request({
    url: '/user/register',
    method: 'post',
    data: data
  })
}

export function getInfo () {
  return request({
    url: '/user/info',
    method: 'get'
  })
}

export function updateInfo(data) {
  return request({
    url: '/user/info/update',
    method: 'post',
    data: data
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
