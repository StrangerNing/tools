export function callNewPage (url, options = {}) {
  if (!options.autoClose) options.autoClose = true
  let path = /\/$/.test(url) ? url : `${url}/`
  const target = /^(http|https):\/\//.test(path) ? url
    : `/#${url}`
  let queryStr = `?timestamp=${new Date().getTime()}&iframeType=iframeType`
  for (let key in options) {
    queryStr += `&${key}=${options[key]}`
  }
  window.open(`${target}${queryStr}`, options.pageName)
}
