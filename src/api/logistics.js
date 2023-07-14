import request from '@/utils/request'

export function getLogisticsExpense() {
  return request({
    url: '/external/logistics/logisticsList',
    method: 'get'
  })
}

export function getDistance(data) {
  return request({
    url: '/external/logistics/distanceList',
    method: 'get',
    params: data
  })
}
