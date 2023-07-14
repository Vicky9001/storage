import request from '@/utils/request'

export function getGoods(data) {
  return request({
    url: '/external/goods/goodsList',
    method: 'get',
    params: data
  })
}
export function addGoods(data) {
  return request({
    url: '/external/goods/addGoods',
    method: 'post',
    data
  })
}
export function modGoods(data) {
  return request({
    url: '/external/goods/modGoods',
    method: 'put',
    data
  })
}

export function delGoods(data) {
  return request({
    url: `/external/goods/delGoods`,
    method: 'delete',
    data
  })
}

