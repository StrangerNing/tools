$('#submit').click(function () {
  let url = '/wapi/blog/article/comment/add'
  let form = $('#commentForm').serializeArray()
  let data = {}
  $.each(form, function(index, field) {
    data[field.name] = field.value; //通过变量，将属性值，属性一起放到对象中
  })
  submit(JSON.stringify(data), url, 'post').then(res => {
    toastr.success(res.msg)
    setInterval(function () {
      $(location).attr('href', '#comments-area')
      location.reload()
    },2000)
  }).catch(e => {
    toastr.error(e)
  })
  console.log(data)
})

$('.btn-reply').click(function () {
  let replyId = $(this).data('reply')
  let replyName = $(this).data('name')
  let parentId = $(this).data('parent')
  console.log('replyId:',replyId, 'parentId:',parentId)
  let content = $('#content')
  content.val('')
  if (parentId) {
    $('#parentId').val(parentId)
    if (parentId !== replyId) {
      console.log('add content')
      content.val('@' + replyName + ' ')
    }
  }
  $('#replyId').val(replyId)
  $('#replyName').empty().append('回复：' + escape(replyName))
  $('#replyTo').css('display', 'inline')
})

$('#clearReply').click(function () {
  $('#replyId').val(null)
  $('#parentId').val(null)
  $('#replyName').empty()
  $('#replyTo').css('display', 'none')
  $('#content').val('')
})

$('.thumb > .avatar').attr('src', function () {
  let name = $(this).data('name')
  return textToImage(name)
})

function textToImage(name){
  let colors = [
    "232,221,203", "205,179,128", "3,101,100", "3,54,73", "3,22,52",
    "237,222,139", "251,178,23", "96,143,159", "1,77,103", "254,67,101", "252,157,154",
    "249,205,173", "200,200,169", "131,175,155", "229,187,129", "161,23,21", "34,8,7",
    "118,77,57", "17,63,61", "60,79,57", "95,92,51", "179,214,110", "248,147,29",
    "227,160,93", "178,190,126", "114,111,238", "56,13,49", "89,61,67", "250,218,141",
    "3,38,58", "179,168,150", "222,125,44", "20,68,106", "130,57,53", "137,190,178",
    "201,186,131", "222,211,140", "222,156,83", "23,44,60", "39,72,98", "153,80,84",
    "217,104,49", "230,179,61", "174,221,129", "107,194,53", "6,128,67", "38,157,128",
    "178,200,187", "69,137,148", "117,121,71", "114,83,52", "87,105,60", "82,75,46",
    "171,92,37", "100,107,48", "98,65,24", "54,37,17", "137,157,192", "250,227,113",
    "29,131,8", "220,87,18", "29,191,151", "35,235,185", "213,26,33", "160,191,124",
    "101,147,74", "64,116,52", "255,150,128", "255,94,72", "38,188,213", "167,220,224",
    "1,165,175", "179,214,110", "248,147,29", "230,155,3", "209,73,78", "62,188,202",
    "224,160,158", "161,47,47", "0,90,171", "107,194,53", "174,221,129", "6,128,67",
    "38,157,128", "201,138,131", "220,162,151", "137,157,192", "175,215,237", "92,167,186",
    "255,66,93", "147,224,255", "247,68,97", "185,227,217"
  ];
  //设置初始值,防止name为空时程序无法执行
  var nick = "未知";
  //判断name是否为空
  if(name){
    nick = name.toString().charAt(0);
  }
  var fontSize = 64;
  var fontWeight = 'normal';

  var canvas = document.getElementById('canvas');
  if(canvas){
    canvas.remove();
  }else{
    var html = "<canvas id='canvas' style='display:none'></canvas>";
    $("body").append(html);
    canvas = document.getElementById('canvas');
  }
  canvas.width = 128;
  canvas.height = 128;
  var context = canvas.getContext('2d');
  //头像背景颜色设置
  context.fillStyle = 'rgb(' + colors[Math.floor(Math.random()*(colors.length))] + ')';
  context.fillRect(0, 0, canvas.width, canvas.height);
  //头像字体颜色设置
  context.fillStyle = '#FFFFFF';
  context.font = fontWeight + ' ' + fontSize + 'px sans-serif';
  context.textAlign = 'center';
  context.textBaseline="middle";
  context.fillText(nick, fontSize, fontSize);
  return canvas.toDataURL("image/png");
}

const submit = async (data, url, method)=> await getAjax(data, url, method)
