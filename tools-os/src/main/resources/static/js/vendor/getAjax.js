//定义一个使用promise的ajax请求
const getAjax = (data, url, method) => {
  //resolve: success
  //reject: error
  return new Promise((resolve, reject) => {
    $.ajax({
      url: url,
      data: data,
      method: method,
      async: true,
      cache: false,
      dataType: 'json',
      contentType: "application/json;charset=UTF-8",
      success: res => {
        resolve(res)
      },
      error: error => {
        reject(error.responseJSON.message)
      }
    })
  })
}

const postAjax = (data, url, method) => {
  //resolve: success
  //reject: error
  return new Promise((resolve, reject) => {
    $.ajax({
      url: url,
      data: data,
      method: method,
      async: true,
      cache: false,
      dataType: 'json',
      // contentType: "application/json;charset=UTF-8",
      success: res => {
        resolve(res)
      },
      error: error => {
        reject(error.status)
      }
    })
  })
}

const getFileAjax = (data, url, method) => {
  //resolve: success
  //reject: error
  return new Promise((resolve, reject) => {
    $.ajax({
      url: url,
      data: data,
      method: method,
      async: true,
      cache: false,
      processData: false,
      contentType : false,
      dataType: 'json',
      success: res => {
        resolve(res)
      },
      error: error => {
        reject(error.status)
      }
    })
  })
}
