import request from '@/utils/request'

export function getArea(data) {
  return request({
    url: '/warehouse/stokeIn/areaList',
    method: 'get',
    params: data
  })
}

export function createOrder(data) {
  return request({
    url: '/warehouse/stokeIn/createWarehousing',
    method: 'post',
    data
  })
}
export function getInNum(data) {
  return request({
    url: '/warehouse/stokeIn/inNum',
    method: 'get',
    params: data

  })
}
export function getOrderPrice(data) {
  return request({
    url: '/external/purchase/totalPrice',
    method: 'get',
    params: data

  })
}
export function getWaitingList(data) {
  return request({
    url: '/warehouse/stokeIn/waitingList',
    method: 'get',
    params: data
  })
}
export function getWarehousingOrder(data) {
  return request({
    url: '/warehouse/stokeIn/warehousingOrder',
    method: 'get',
    params: data
  })
}
export function changeState(data) {
  return request({
    url: '/warehouse/stokeIn/changeState',
    method: 'post',
    data
  })
}
export function createWarehousing(data) {
  return request({
    url: '/warehouse/stokeIn/createWarehousing',
    method: 'post',
    data
  })
}
