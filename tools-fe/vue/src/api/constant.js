import request from '@/utils/request'

export function getConstantList(params) {
  return request({
    url: '/constant/query',
    method: 'get',
    params: params
  })
}

export function updateAllConstants() {
  return request({
    url: '/constant/update',
    method: 'get'
  })
}

export function updateConstants(data) {
  return request({
    url: '/constant/modify',
    method: 'post',
    data: data
  })
}

export function addConstant(data) {
  return request({
    url: '/constant/add',
    method: 'post',
    data: data
  })
}
