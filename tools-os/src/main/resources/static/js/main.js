(function($) {
    'use strict';

    // Page loading
    $(window).on('load', function() {
        $('.preloader').delay(450).fadeOut('slow');
    });

    // Scroll progress
    var scrollProgress = function() {
        var docHeight = $(document).height(),
          windowHeight = $(window).height(),
          scrollPercent;
        $(window).on('scroll', function() {
            docHeight = $(document).height()
            windowHeight = $(window).height()
            scrollPercent = $(window).scrollTop() / (docHeight - windowHeight) * 100;
            $('.scroll-progress').width(scrollPercent + '%');
        });
    };

    // Off canvas sidebar
    var OffCanvas = function() {
        $('#off-canvas-toggle').on('click', function(e) {
            $('body').toggleClass("canvas-opened");
        });

        $('.dark-mark').on('click', function() {
            $('body').removeClass("canvas-opened");
        });
        $('.off-canvas-close').on('click', function() {
            $('body').removeClass("canvas-opened");
        });
    };

    // Search form
    var openSearchForm = function() {
        $('button.search-icon').on('click', function() {
            $('body').toggleClass("open-search-form");
            $('.mega-menu-item').removeClass("open");
            $("html, body").animate({ scrollTop: 0 }, "slow");
            $('#searchBtn').data('display', true)
            $('#search').focus()

        });
        $('.search-close').on('click', function() {
            $('body').removeClass("open-search-form");
            $('#searchBtn').data('display', false)
        });
    };

    // Mobile menu
    var mobileMenu = function() {
        var menu = $('ul#mobile-menu');
        if (menu.length) {
            menu.slicknav({
                prependTo: ".mobile_menu",
                closedSymbol: '+',
                openedSymbol: '-'
            });
        };
    };

    var SubMenu = function() {
        // $(".sub-menu").hide();
        $(".menu li.menu-item-has-children").on({
            mouseenter: function() {
                $('.sub-menu:first, .children:first', this).stop(true, true).slideDown('fast');
            },
            mouseleave: function() {
                $('.sub-menu:first, .children:first', this).stop(true, true).slideUp('fast');
            }
        });
    };

    var WidgetSubMenu = function() {
        //$(".sub-menu").hide();
        $('.menu li.menu-item-has-children').on('click', function() {
            var element = $(this);
            if (element.hasClass('open')) {
                element.removeClass('open');
                element.find('li').removeClass('open');
                element.find('ul').slideUp(200);
            } else {
                element.addClass('open');
                element.children('ul').slideDown(200);
                element.siblings('li').children('ul').slideUp(200);
                element.siblings('li').removeClass('open');
                element.siblings('li').find('li').removeClass('open');
                element.siblings('li').find('ul').slideUp(200);
            }
        });
    };

    // Slick slider
    var customSlickSlider = function() {

        // Slideshow Fade
        $('.slide-fade').slick({
            infinite: true,
            dots: false,
            arrows: true,
            autoplay: false,
            autoplaySpeed: 3000,
            fade: true,
            fadeSpeed: 1500,
            prevArrow: '<button type="button" class="slick-prev"><i class="elegant-icon arrow_left"></i></button>',
            nextArrow: '<button type="button" class="slick-next"><i class="elegant-icon arrow_right"></i></button>',
            appendArrows: '.arrow-cover',
        });

        // carausel 3 columns
        $('.carausel-3-columns').slick({
            dots: false,
            infinite: true,
            speed: 1000,
            arrows: false,
            autoplay: true,
            slidesToShow: 3,
            slidesToScroll: 1,
            loop: true,
            adaptiveHeight: true,
            responsive: [{
                    breakpoint: 1024,
                    settings: {
                        slidesToShow: 3,
                        slidesToScroll: 3,
                    }
                },
                {
                  breakpoint: 992,
                  settings: {
                      slidesToShow: 2,
                      slidesToScroll: 2
                  }
                },
                {
                    breakpoint: 768,
                    settings: {
                        slidesToShow: 1,
                        slidesToScroll: 1
                    }
                }
            ]
        });

        // featured slider 2
        $('.featured-slider-2-items').slick({
            slidesToShow: 1,
            slidesToScroll: 1,
            arrows: false,
            dots: false,
            fade: true,
            asNavFor: '.featured-slider-2-nav',
        });
        $('.featured-slider-2-nav').slick({
            slidesToShow: 3,
            slidesToScroll: 1,
            vertical: true,
            asNavFor: '.featured-slider-2-items',
            dots: false,
            arrows: false,
            focusOnSelect: true,
            verticalSwiping: true
        });
        // featured slider 3
        $('.featured-slider-3-items').slick({
            slidesToShow: 1,
            slidesToScroll: 1,
            arrows: true,
            dots: false,
            fade: true,
            prevArrow: '<button type="button" class="slick-prev"><i class="elegant-icon arrow_left"></i></button>',
            nextArrow: '<button type="button" class="slick-next"><i class="elegant-icon arrow_right"></i></button>',
            appendArrows: '.slider-3-arrow-cover',
        });
    };

    var typeWriter = function() {
        var TxtType = function(el, toRotate, period) {
            this.toRotate = toRotate;
            this.el = el;
            this.loopNum = 0;
            this.period = parseInt(period, 10) || 2000;
            this.txt = '';
            this.tick();
            this.isDeleting = !1
        };
        TxtType.prototype.tick = function() {
            var i = this.loopNum % this.toRotate.length;
            var fullTxt = this.toRotate[i];
            if (this.isDeleting) {
                this.txt = fullTxt.substring(0, this.txt.length - 1)
            } else {
                this.txt = fullTxt.substring(0, this.txt.length + 1)
            }
            this.el.innerHTML = '<span class="wrap">' + this.txt + '</span>';
            var that = this;
            var delta = 200 - Math.random() * 100;
            if (this.isDeleting) {
                delta /= 2
            }
            if (!this.isDeleting && this.txt === fullTxt) {
                delta = this.period;
                this.isDeleting = !0
            } else if (this.isDeleting && this.txt === '') {
                this.isDeleting = !1;
                this.loopNum++;
                delta = 500
            }
            setTimeout(function() {
                that.tick()
            }, delta)
        };
        window.onload = function() {
            var elements = document.getElementsByClassName('typewrite');
            for (var i = 0; i < elements.length; i++) {
                var toRotate = elements[i].getAttribute('data-type');
                var period = elements[i].getAttribute('data-period');
                if (toRotate) {
                    new TxtType(elements[i], JSON.parse(toRotate), period)
                }
            }
            var css = document.createElement("style");
            css.type = "text/css";
            css.innerHTML = ".typewrite > .wrap { border-right: 0.05em solid #5869DA}";
            document.body.appendChild(css)
        }
    }

    // Nice Select
    var niceSelectBox = function() {
        var nice_Select = $('select');
        if (nice_Select.length) {
            nice_Select.niceSelect();
        }
    };

    //Header sticky
    var headerSticky = function() {
        $(window).on('scroll', function() {
            var scroll = $(window).scrollTop();
            if (scroll < 78) {
                $(".slicknav_menu").css("margin", -1 * scroll + "px 0 0 0")
                $(".header-sticky").removeClass("sticky-bar");
            } else {
                $(".slicknav_menu").css("margin", "0")
                $(".header-sticky").addClass("sticky-bar");
            }
        });
    };

    // Scroll up to top
    var scrollToTop = function() {
        $.scrollUp({
            scrollName: 'scrollUp', // Element ID
            topDistance: '300', // Distance from top before showing element (px)
            topSpeed: 300, // Speed back to top (ms)
            animation: 'fade', // Fade, slide, none
            animationInSpeed: 200, // Animation in speed (ms)
            animationOutSpeed: 200, // Animation out speed (ms)
            scrollText: '<i class="elegant-icon arrow_up"></i>', // Text for element
            activeOverlay: false, // Set CSS color to display scrollUp active point, e.g '#00FFFF'
        });
    };

    //VSticker
    var VSticker = function() {
        $('#news-flash').vTicker({
            speed: 800,
            pause: 3000,
            animation: 'fade',
            mousePause: false,
            showItems: 1
        });
        $('#date-time').vTicker({
            speed: 800,
            pause: 3000,
            animation: 'fade',
            mousePause: false,
            showItems: 1
        });
    };

    //sidebar sticky
    var stickySidebar = function() {
        $('.sticky-sidebar').theiaStickySidebar();
    };

    //Custom scrollbar
    var customScrollbar = function() {
        var $ = document.querySelector.bind(document);
        let option={
            wheelPropagation: false
        }
        var ps = new PerfectScrollbar('.custom-scrollbar', option);
    };

    //Mega menu
    var megaMenu = function() {
        $('.sub-mega-menu .nav-pills > a').on('mouseover', function(event) {
            $(this).tab('show');
        });
    };

    //magnific Popup
    var magPopup = function() {
        if ($('.play-video').length) {
            $('.play-video').magnificPopup({
                disableOn: 700,
                type: 'iframe',
                mainClass: 'mfp-fade',
                removalDelay: 160,
                preloader: false,
                fixedContentPos: false
            });
        }
    };

    var masonryGrid = function() {
        if ($(".grid").length) {
            // init Masonry
            var $grid = $('.grid').masonry({
                itemSelector: '.grid-item',
                percentPosition: true,
                columnWidth: '.grid-sizer',
                gutter: 0
            });

            // layout Masonry after each image loads
            $grid.imagesLoaded().progress(function() {
                $grid.masonry();
            });
        }
    };

    /* More articles*/
    var moreArticles = function() {
        $.fn.vwScroller = function(options) {
            var default_options = {
                delay: 500,
                /* Milliseconds */
                position: 0.7,
                /* Multiplier for document height */
                visibleClass: '',
                invisibleClass: '',
            }

            var isVisible = false;
            var $document = $(document);
            var $window = $(window);

            options = $.extend(default_options, options);

            var observer = $.proxy(function() {
                var isInViewPort = $document.scrollTop() > (($document.height() - $window.height()) * options.position);

                if (!isVisible && isInViewPort) {
                    onVisible();
                } else if (isVisible && !isInViewPort) {
                    onInvisible();
                }
            }, this);

            var onVisible = $.proxy(function() {
                isVisible = true;

                /* Add visible class */
                if (options.visibleClass) {
                    this.addClass(options.visibleClass);
                }

                /* Remove invisible class */
                if (options.invisibleClass) {
                    this.removeClass(options.invisibleClass);
                }

            }, this);

            var onInvisible = $.proxy(function() {
                isVisible = false;

                /* Remove visible class */
                if (options.visibleClass) {
                    this.removeClass(options.visibleClass);
                }

                /* Add invisible class */
                if (options.invisibleClass) {
                    this.addClass(options.invisibleClass);
                }
            }, this);

            /* Start observe*/
            setInterval(observer, options.delay);

            return this;
        }

        if ($.fn.vwScroller) {
            var $more_articles = $('.single-more-articles');
            $more_articles.vwScroller({ visibleClass: 'single-more-articles--visible', position: 0.55 })
            $more_articles.find('.single-more-articles-close-button').on('click', function() {
                $more_articles.hide();
            });
        }

        $('button.single-more-articles-close').on('click', function() {
            $('.single-more-articles').removeClass('single-more-articles--visible');
        });
    }

    var renderHistory = function () {
        let history = localStorage.getItem('history')
        let area = $('#historyList')
        let template = ''
        let timeagoInstance = timeago();
        if (history) {
            let list = JSON.parse(history)
            for (let i = 0; i < list.length; i ++) {
                template += `<li class="mb-30">
                                <div class="d-flex hover-up-2 transition-normal">
                                    <div class="post-thumb post-thumb-80 d-flex mr-15 border-radius-5 img-hover-scale overflow-hidden">
                                        <a class="color-white" href="` + list[i].href + `">
                                            <img src="` + list[i].thumb + `" alt="">
                                        </a>
                                    </div>
                                    <div class="post-content media-body">
                                        <h6 class="post-title mb-15 text-limit-2-row font-medium"><a href="` + list[i].href + `">` + list[i].title + `</a></h6>
                                        <div class="entry-meta meta-1 float-left font-x-small text-uppercase">
                                            <span class="post-on time" datetime="` + list[i].createTime + `">` + timeagoInstance.format(list[i].createTime, 'zh_CN') + `</span>
                                            <span class="time-reading has-dot">` + list[i].minutes + `</span>
                                        </div>
                                    </div>
                                </div>
                            </li>`
            }
            area.append(template)
        } else {
            area.append('<p>暂无阅读记录</p>')
        }
    }

    var subscribe = function () {
        $('#subscribe-btn').click(function () {
            var myModal = new bootstrap.Modal(document.getElementById('subscribe-modal'), {
                keyboard: false
            })
            let mail = $('#subscribe-input').val()
            let subscribeMsg = $('#subscribe-msg')
            if (mail === undefined || mail === null || mail === '') {
                subscribeMsg.html('<p>请输入你的邮箱地址</p>')
            } else {
                var regexEmail = /\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*/;
                if (regexEmail.test(mail)) {
                    subscribeMsg.html('<p>请稍后...</p>')
                    let url = '/wapi/blog/subscribe/newsletter/check'
                    let data = {mail: mail}
                    submitSubscribe(JSON.stringify(data), url, 'post').then(res => {
                        subscribeMsg.html('<p>确认订阅的邮件已经发送到您的邮箱，请稍后前往您的邮箱进行确认～</p>')
                    }).catch(e => {
                        subscribeMsg.html('<p>' + e + '，请重试X﹏X</p>')
                    })
                } else {
                    subscribeMsg.html('<p>请输入一个正确格式的邮箱地址</p>')
                }

            }

            myModal.toggle()
        })
    }

    const submitSubscribe = async (data, url, method)=> await getAjax(data, url, method)

    /* WOW active */
    new WOW().init();

    //Load functions
    $(document).ready(function() {
        openSearchForm();
        OffCanvas();
        customScrollbar();
        magPopup();
        scrollToTop();
        headerSticky();
        stickySidebar();
        customSlickSlider();
        megaMenu();
        mobileMenu();
        typeWriter();
        WidgetSubMenu();
        scrollProgress();
        masonryGrid();
        niceSelectBox();
        moreArticles();
        VSticker();
        renderHistory();
        subscribe();
    });

})(jQuery);
