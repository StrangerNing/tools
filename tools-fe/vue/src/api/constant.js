import request from '@/utils/request'

export function getConstantList(params) {
  return request({
    url: '/wapi/constant/query',
    method: 'get',
    params: params
  })
}

export function updateAllConstants() {
  return request({
    url: '/wapi/constant/update',
    method: 'get'
  })
}

export function updateConstants(data) {
  return request({
    url: '/wapi/constant/modify',
    method: 'post',
    data: data
  })
}

export function addConstant(data) {
  return request({
    url: '/wapi/constant/add',
    method: 'post',
    data: data
  })
}
