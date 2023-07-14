import request from '@/utils/request'

export function getGoods(data) {
  return request({
    url: '/warehouse/warning/getGoods',
    method: 'get',
    params: data
  })
}

export function warning(data) {
  return request({
    url: '/warehouse/warning/warning',
    method: 'get',
    params: data
  })
}

export function del(data) {
  return request({
    url: '/warehouse/warning/delete',
    method: 'delete',
    data
  })
}

export function create(data) {
  return request({
    url: '/warehouse/warning/create',
    method: 'post',
    data
  })
}

export function warningRecord(data) {
  return request({
    url: '/warehouse/warning/warningRecord',
    method: 'get',
    params: data
  })
}
