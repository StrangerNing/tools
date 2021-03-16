import request from '@/utils/request'

export function getImageList(params) {
  return request({
    url: '/wapi/file/image/list',
    method: 'get',
    params: params
  })
}

export function delFile(params) {
  return request({
    url: '/wapi/file/del/' + params,
    method: 'get'
  })
}

export function delFileByName(params) {
  return request({
    url: '/wapi/file/del',
    method: 'get',
    params: params
  })
}

export function getFile(params) {
  return request({
    url: '/wapi/file/get/' + params,
    method: 'get'
  })
}
