import request from '@/utils/request'

export function login (data) {
  return request({
    url: '/wapi/user/login',
    method: 'get',
    params: data
  })
}

export function register(data) {
  return request({
    url: '/wapi/user/register',
    method: 'post',
    data: data
  })
}

export function getInfo () {
  return request({
    url: '/wapi/user/info',
    method: 'get'
  })
}

export function updateInfo(data) {
  return request({
    url: '/wapi/user/info/update',
    method: 'post',
    data: data
  })
}

export function logout () {
  return request({
    url: '/wapi/user/logout',
    method: 'get'
  })
}

export function getApiKeyList() {
  return request({
    url: '/wapi/user/api/list',
    method: 'get'
  })
}

export function createApiKey(data) {
  return request({
    url: '/wapi/user/api/create',
    method: 'post',
    data: data
  })
}

export function delApiKey(params) {
  return request({
    url: '/wapi/user/api/del/' + params,
    method: 'get'
  })
}

export function updateApiKey(data) {
  return request({
    url: '/wapi/user/api/update',
    method: 'post',
    data: data
  })
}

export function searchByKey(param) {
  return request({
    url: '/wapi/user/api/search',
    method: 'get',
    params: param
  })
}
