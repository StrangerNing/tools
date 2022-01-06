import getAjax from "./vendor/getAjax.js";

var register = document.getElementById('register')
register.onclick = function () {
  // let captcha = document.getElementById('captcha')
  // if (!captcha) {
  //   recaptcha.show()
  //   grecaptcha.render('captcha', {
  //     'sitekey' : '6LcPWzEaAAAAAN37oKCUBAQNJHfA-kdE0FYguIhh'
  //   });
  // } else {
  //   recaptcha.hide()
  // }
  let form = $('#registerForm').serializeArray()
  let data = {}
  $.each(form, function(index, field) {
    data[field.name] = field.value; //通过变量，将属性值，属性一起放到对象中
  })
  let captchaCode = grecaptcha.getResponse()
  if (data.username === null || data.username === '') {
    toastr.error('请输入用户名')
  } else if (data.password === null || data.password === '') {
    toastr.error('请输入密码')
  } else if (data.confirmPassword === null || data.confirmPassword !== data.password) {
    toastr.error('两次输入的密码不一致')
  } else if (captchaCode === '') {
    toastr.error('请完成人机校验')
  } else {
    data.captchaCode = captchaCode
    let url = "https://edchu.cn/wapi/user/register"
    submit(JSON.stringify(data), url, 'post').then(res => {
      toastr.success(res.msg)
      setInterval(function () {
        $(location).attr('href', "https://sso.edchu.cn")
      },2000)
    }).catch(e => {
      toastr.error(e)
      grecaptcha.reset()
    })
  }
}

const submit = async (data, url, method)=> await getAjax(data, url, method)
