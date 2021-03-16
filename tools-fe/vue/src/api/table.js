import request from '@/utils/request'

export function getList (params) {
  return request({
    url: '/wapi/table/list',
    method: 'get',
    params
  })
}
