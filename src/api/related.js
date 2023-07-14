import request from '@/utils/request'

export function getDepartment(data) {
  return request({
    url: '/external/department/departmentList',
    method: 'get',
    params: data
  })
}
export function addDepartment(data) {
  return request({
    url: '/external/department/addDepartment',
    method: 'post',
    data
  })
}
export function modDepartment(data) {
  return request({
    url: '/external/department/modDepartment',
    method: 'put',
    data
  })
}

export function deleteDepartment(data) {
  return request({
    url: `/external/department/delDepartment`,
    method: 'delete',
    data
  })
}
export function getSupplier(data) {
  return request({
    url: '/external/supplier/supplierList',
    method: 'get',
    params: data
  })
}

export function addSupplier(data) {
  return request({
    url: '/external/supplier/addSupplier',
    method: 'post',
    data
  })
}
export function modSupplier(data) {
  return request({
    url: '/external/supplier/modSupplier',
    method: 'put',
    data
  })
}

export function delSupplier(data) {
  return request({
    url: `/external/supplier/delSupplier`,
    method: 'delete',
    data
  })
}
