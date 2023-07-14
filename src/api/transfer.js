import request from '@/utils/request'

export function getOrder(data) {
  return request({
    url: '/warehouse/transfer/transferOrder',
    method: 'get',
    params: data

  })
}
export function getGoods(data) {
  return request({
    url: '/warehouse/transfer/getGoods',
    method: 'get',
    params: data
  })
}
export function getTransferNum(data) {
  return request({
    url: '/warehouse/transfer/transferNum',
    method: 'get',
    params: data

  })
}
export function createOrder(data) {
  return request({
    url: '/warehouse/transfer/create',
    method: 'post',
    data
  })
}

export function changeState(data) {
  return request({
    url: `/warehouse/transfer/changeState`,
    method: 'post',
    data
  })
}
