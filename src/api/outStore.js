import request from '@/utils/request'

export function outOrder(data) {
  return request({
    url: '/warehouse/stokeOut/outOrder',
    method: 'get',
    params: data

  })
}
export function getOutNum(data) {
  return request({
    url: '/warehouse/stokeOut/outNum',
    method: 'get',
    params: data

  })
}
export function getGoods(data) {
  return request({
    url: '/external/goods/goodsList',
    method: 'get',
    params: data
  })
}
export function getDepartment(data) {
  return request({
    url: '/warehouse/stokeOut/departmentList',
    method: 'get',
    params: data
  })
}
export function createOrder(data) {
  return request({
    url: '/external/stokeOut/create',
    method: 'post',
    data
  })
}

export function changeState(data) {
  return request({
    url: `/dev6-api/changeState`,
    method: 'post',
    data
  })
}
