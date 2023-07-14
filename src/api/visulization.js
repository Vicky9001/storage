import request from '@/utils/request'

export function visulization(data) {
  return request({
    url: '/warehouse/warning/visualization',
    method: 'get',
    params: data
  })
}
