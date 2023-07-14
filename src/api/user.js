import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/auth/oauth/token',
    method: 'post',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
    },
    transformRequest: [
      function(data) {
        var ret = ''
        for (var it in data) {
          ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
        }
        ret = ret.substring(0, ret.lastIndexOf('&'))
        return ret
      }
    ],
    data
  })
}

export function getInfo(token) {
  return request({
    url: '/external/user/info',
    method: 'get',
    params: { token }
  })
}

export function alterPassword(data) {
  return request({
    url: '/external/user/update',
    method: 'put',
    data
  })
}

export function logout() {
  return request({
    url: '/external/user/logout',
    method: 'post'
  })
}

export function register(data) {
  return request({
    url: '/external/user/register',
    method: 'post',
    data
  })
}

export function getUserList(data) {
  return request({
    url: '/external/user/userlist',
    method: 'get',
    params: data
  })
}

export function updateUserInfo(data) {
  return request({
    url: '/external/user/update',
    method: 'put',
    data
  })
}

export function deleteUser(data) {
  return request({
    url: '/external/user/delete',
    method: 'delete',
    data
  })
}

export function addUser(data) {
  return request({
    url: '/external/user/addUser',
    method: 'post',
    data
  })
}

export function updateRoles(data) {
  return request({
    url: '/external/user/updateRoles',
    method: 'post',
    data
  })
}

export function getRoles() {
  return request({
    url: '/external/role/roleList',
    method: 'get'
  })
}

export function searchRole(name) {
  return request({
    url: '/external/role/searchRoleList',
    method: 'get',
    params: { name }
  })
}

