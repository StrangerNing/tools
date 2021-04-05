$(document).ready(function () {
  let history = localStorage.getItem('history')
  let id = $('#title').data('id')
  let title = $('#title').text()
  let thumb = $('#thumb').attr('src')
  let createTime = $('#createTime').attr('datetime')
  let minutes = $('#minutes').text()
  let href = window.location.href
  let his = {
    id: id,
    title: title,
    thumb: thumb,
    createTime: createTime,
    minutes: minutes,
    href: href
  }
  let json
  if (history) {
    json = JSON.parse(history)
    json = json.filter(item => {return item.id !== id})
    json.unshift(his)
  } else {
    json = [his]
  }
  localStorage.setItem('history', JSON.stringify(json))
})
