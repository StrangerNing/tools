export function recaptcha () {
  return new Promise(function (resolve, reject) {
    const tag = document.getElementsByTagName('script')
    for (let i of tag) {
      if (i.src === 'https://www.recaptcha.net/recaptcha/api.js?render=explicit') {
        resolve(window.recaptcha)
        return
      }
    }
    const script = document.createElement('script')
    script.type = 'text/javascript'
    script.src = 'https://www.recaptcha.net/recaptcha/api.js?render=explicit'
    script.onerror = reject
    document.body.appendChild(script)
    script.onload = () => {
      resolve(window.recaptcha)
    }
  })
}
