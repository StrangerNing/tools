$('#content img').click(function () {
  let bigImage = $('.dark-mark')
  let imageSrc = $(this).attr('src')
  bigImage.css('display', 'flex')
  bigImage.toggleClass('dark-mark-display')
  bigImage.html(`<img style="margin: auto" src="` + imageSrc + `"/>`)
})
$('.dark-mark').click(function () {
  let bigImage = $('.dark-mark')
  bigImage.html('')
  bigImage.removeClass('dark-mark-display')
})
