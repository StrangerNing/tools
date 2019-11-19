import service from "./request.js";

export function saveUrl(data) {
  return service({
    url: '/save',
    method: 'post',
    data: JSON.stringify(data)
  })
}
