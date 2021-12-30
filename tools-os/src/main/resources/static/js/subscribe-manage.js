$('#confirmManage').click(function () {
  let subscribeTypes = []
  $("input[name='subscribe-type']").each(function (i, value) {
    if ($(this).prop("checked") === true) {
      subscribeTypes.push($(this).val());
    }
  });
  let url = '/wapi/blog/subscribe/manage'
  let eid = getQueryVariable('eid')
  let params = {
    subscribeTypes: subscribeTypes,
    eid: eid
  }
  subscribeManage(JSON.stringify(params), url, 'post').then(res => {
    toastr.success(res.msg)
  })
  console.log(subscribeTypes)
})

function getQueryVariable(variable)
{
  var query = window.location.search.substring(1);
  var vars = query.split("&");
  for (var i=0;i<vars.length;i++) {
    var pair = vars[i].split("=");
    if(pair[0] === variable){return pair[1];}
  }
  return(false);
}

const subscribeManage = async (data, url, method)=> await getAjax(data, url, method)
