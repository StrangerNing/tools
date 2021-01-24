import Cookies from 'js-cookie'

const TokenKey = 'user'

export function getToken () {
  return Cookies.get(TokenKey)
}

export function getTokenByKey(key) {
  return Cookies.get(key)
}

export function setToken (token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken () {
  return Cookies.remove(TokenKey)
}
