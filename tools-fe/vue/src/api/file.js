import request from '@/utils/request'

export function getImageList(params) {
  return request({
    url: '/file/image/list',
    method: 'get',
    params: params
  })
}

export function delFile(params) {
  return request({
    url: '/file/del/' + params,
    method: 'get'
  })
}

export function getFile(params) {
  return request({
    url: '/file/get/' + params,
    method: 'get'
  })
}
