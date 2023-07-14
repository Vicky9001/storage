import request from '@/utils/request'

export function searchPositions(data) {
  return request({
    url: '/warehouse/position/searchPositions',
    method: 'get',
    params: data
  })
}

export function checkOrder(data) {
  return request({
    url: '/warehouse/check/checkOrder',
    method: 'get',
    params: data
  })
}

export function changeState(data) {
  return request({
    url: '/warehouse/check/changeState',
    method: 'post',
    data
  })
}

export function createOrder(data) {
  return request({
    url: '/warehouse/check/create',
    method: 'post',
    data
  })
}

export function updateOrder(data) {
  return request({
    url: '/warehouse/check/update',
    method: 'post',
    data
  })
}

export function statistics(data) {
  return request({
    url: '/warehouse/stokeIn/statistics',
    method: 'get',
    params: data
  })
}

export function getOut(data) {
  return request({
    url: '/warehouse/stokeOut/statistics',
    method: 'get',
    params: data
  })
}
