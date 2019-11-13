// create an axios instance
const service = axios.create({
  baseURL: 'http://10.112.161.203:8080', // api的base_url
  withCredentials: true, // 跨域请求时发送 cookies
  timeout: 120000 // 请求超时时间
})

// request拦截器
service.interceptors.request.use(config => {
  //config.headers['token'] = 66;
  config.headers['Content-Type'] = "application/json";
  // config.headers['id'] = window.sessionStorage.getItem("id");
  // console.log(config.headers)
  return config
}, error => {
  // Do something with request error
  // console.log(error) // for debug
  Promise.reject(error)
})

// respone拦截器
service.interceptors.response.use(
  response => {
    const res = response.data

    return res
  },
  error => {
    console.log(error) // for debug
    Toast('服务器异常, 请稍后重试')

    return Promise.reject(error)
  }
)

export default service
