Fancybox.bind("#content img", {
  groupAll : true, // Group all items
  caption: function (fancybox, carousel, slide) {
    return (
      slide.caption
    );
  },
  on : {
    ready : (fancybox) => {
      console.log(`fancybox #${fancybox.id} is ready!`);
    }
  }
});
