import request from '@/utils/request'

export function getHouseList(data) {
  return request({
    url: '/warehouse/position/houseList',
    method: 'get',
    params: data

  })
}
export function getArea(data) {
  return request({
    url: '/warehouse/position/areaList',
    method: 'get',
    params: data
  })
}
export function addArea(data) {
  return request({
    url: '/warehouse/position/addArea',
    method: 'post',
    data
  })
}

export function deleteArea(data) {
  return request({
    url: `/warehouse/position/delArea`,
    method: 'delete',
    data
  })
}
export function getPosition(data) {
  return request({
    url: '/warehouse/position/positionList',
    method: 'get',
    params: data
  })
}

export function addPosition(data) {
  return request({
    url: '/warehouse/position/addPosition',
    method: 'post',
    data
  })
}
export function modPosition(data) {
  return request({
    url: '/warehouse/position/modPosition',
    method: 'put',
    data
  })
}

export function delPosition(data) {
  return request({
    url: `/warehouse/position/delPosition`,
    method: 'delete',
    data
  })
}
