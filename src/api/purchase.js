import request from '@/utils/request'

export function getOrder(data) {
  return request({
    url: '/external/purchase/purchaseOrder',
    method: 'get',
    params: data

  })
}
export function getPurchaseNum(data) {
  return request({
    url: '/external/purchase/purchaseNum',
    method: 'get',
    params: data

  })
}
export function getGoods(data) {
  return request({
    url: '/external/goods/getGoods',
    method: 'get',
    params: data
  })
}
export function createOrder(data) {
  return request({
    url: '/external/purchase/createPurchase',
    method: 'post',
    data
  })
}

export function approveOrder(data) {
  return request({
    url: `/external/purchase/approve`,
    method: 'post',
    data
  })
}
export function rejectOrder(data) {
  return request({
    url: `/external/purchase/reject`,
    method: 'post',
    data
  })
}

export function updateOrder(data) {
  return request({
    url: '/external/purchase/update',
    method: 'put',
    data
  })
}

export function payOrder(data) {
  return request({
    url: '/external/purchase/pay',
    method: 'post',
    data
  })
}

export function reserve(data) {
  return request({
    url: `/external/purchase/reserve`,
    method: 'post',
    data
  })
}
