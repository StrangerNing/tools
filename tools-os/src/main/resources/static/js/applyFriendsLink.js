$('#confirm').click(function () {
  let url = '/wapi/blog/friends/apply'
  let form = $('#applyFriendsForm').serializeArray()
  let data = {}
  $.each(form, function(index, field) {
    data[field.name] = field.value; //通过变量，将属性值，属性一起放到对象中
  })
  submit(JSON.stringify(data), url, 'post').then(res => {
    toastr.success(res.msg)
    setInterval(function () {
      location.reload()
    },2000)
  }).catch(e => {
    toastr.error(e)
    $('#submit-modal').modal('hide')
  })
  console.log(data)
})

const submit = async (data, url, method)=> await getAjax(data, url, method)
