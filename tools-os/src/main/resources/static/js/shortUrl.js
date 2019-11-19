
function commit() {
  const params = {
    url: this.url
  }
  saveUrl(params).then(res => {
    console.log(res)
  })
}
