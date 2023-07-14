import Cookies from 'js-cookie'

const TokenKey = 'Admin-Token'
const Token2Key = 'Admin-Token2'

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}

export function getToken2() {
  return Cookies.get(Token2Key)
}

export function setToken2(token2) {
  return Cookies.set(Token2Key, token2)
}
export function removeToken2() {
  return Cookies.remove(Token2Key)
}

