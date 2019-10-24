(function ($) {
    "use strict";

    function showPage(bt, pagename) {
        var btn = $(bt),
            pageContListID = $('.page-container li#' + pagename),
            pageContListSelected = $('.page-container li.selected'),
            pageTitleCont = pageContListID.find(".title-container"),
            pageDescription = pageContListID.find(".description");

        if (!pageContListID.hasClass('selected')) {
            pageContListSelected.find(".title-container h2").addClass('rotated');
            $(".nav-menu li.selected").removeClass('selected');
            btn.addClass('selected');
            if (window.innerWidth > 767) {
                    pageContListSelected.addClass('hidden');
                    pageContListSelected.find(".description").css('height', '475px');
                    pageContListSelected.removeClass('selected');
                    pageContListID.removeClass('hidden');
                    pageContListID.addClass('selected');
                   pageTitleCont.find("h2").removeClass('rotated');

            } else {
                    pageContListSelected.addClass('hidden');
                    pageContListSelected.removeClass('selected');
                    pageContListID.removeClass('hidden');
                    pageContListID.addClass('selected');
                     pageTitleCont.css('-webkit-transform', 'translate3d(0px, 0px, 0px)');
                    pageTitleCont.find("h2").removeClass('rotated');
                    pageDescription.css({
                        'height': 'auto',
                        '-webkit-transform': 'translate3d(0px, 0px, 0px)'
                    });

            }
        }
    }

    /*---------------Scripts for Page Trasition Animation Ends-------------------------*/
    /* function for hide Front Screen*/
    function hidefront() {
        var frontPage = $("#frontpage"),
            frontPageContent = $("#content"),
            pageHome = $("#home"),
            pageHomeDesc = pageHome.find(".description"),
            homeTitleContainer = pageHome.find(".title-container");

        frontPage.find(".front-img").css("opacity", 0);
        frontPage.addClass("hidden");
        frontPageContent.removeClass("hidden");
        centerContent();
        homeTitleContainer.removeClass("hidden");
        $("#profile-img").removeClass('transparent');
            pageHomeDesc.removeClass("hidden");
            pageHome.find('.fade-text').removeClass('transparent');
            pageHome.find('ul.personal-info li.rotate-out').removeClass('rotated');
    }
    /*Code for Centering the vCard*/
    function centerContent() {
        var content = $('#content'),
            frontMain = $('#frontpage');

        if (window.innerWidth > 767) {
            content.css("top", (window.innerHeight - content.height()) / 2);
        } else {
            content.css("top", 15);
        }
        if (window.innerHeight > frontMain.height()) {
            frontMain.css("top", (window.innerHeight - frontMain.height()) / 2);
        } else {
            frontMain.css("top", 15);
        }
    }

    $(window).on('load', function() {
        $(".loading-screen").fadeOut("slow");
        centerContent();
    });
    $(window).on('resize', function() {
        if (window.innerWidth > 767) {
            $('.description').css('-webkit-transform', 'none');
            $('.description').css('height', '475px');
        } else {
            $('.description').css('height', 'auto');
        }
        centerContent();
    });
/*Code for Centering the vCard*/
function centerContent() {
    var content = $('#content'),
        frontMain = $('#frontpage');

    if (window.innerWidth > 767) {
        content.css("top", (window.innerHeight - content.height()) / 2);
    } else {
        content.css("top", 15);
    }
    if (window.innerHeight > frontMain.height()) {
        frontMain.css("top", (window.innerHeight - frontMain.height()) / 2);
    } else {
        frontMain.css("top", 15);
    }
}

$(window).on('load', function() {
    $(".loading-screen").fadeOut("slow");
    centerContent();
});
$(window).on('resize', function() {
    if (window.innerWidth > 767) {
        $('.description').css('-webkit-transform', 'none');
        $('.description').css('height', '475px');
    } else {
        $('.description').css('height', 'auto');
    }
    centerContent();
});


/*-------------- Document Ready --------------*/
$(document).on('ready', function() {
    var eduTap = $('.education-class li'),
        frontPage = $("#frontpage");

    setTimeout(function(){
        $(".frontclick").addClass('active');
    },1000);

    $('.tabs').tabslet({
      mouseevent: 'click',
      attribute: 'href',
      animation: true
    });

    frontPage.on('click', function() {
        $(".frontclick").removeClass('active');
        hidefront();
    });

    $('#menu-container .nav-btn').on('click', function() {
        var $this = $(this);
        showPage($this, $this.data('page'))
    });


    eduTap.on('click', function() {
        var $this = $(this);
        if ($this.hasClass('current')) {
            $this.removeClass('current');
        } else {
            eduTap.removeClass('current');
            $this.addClass('current');
        }
    });

    // init Isotope
    var $grid = $('.folio-item').isotope({
        itemSelector: '.view',
        layoutMode: 'fitRows'
    });

    // bind filter button click
    $('.filters-button-group').on('click', 'li', function() {
        var filterValue = $(this).attr('data-filter');
        $grid.isotope({
            filter: filterValue
        });
    });

    $('.folio-btn').on('click', function() {
        setTimeout(function() {
            var filterValue = $(this).attr('data-filter');
            // use filterFn if matches value
            $grid.isotope({
                filter: filterValue
            });
            $('.button-group .is-checked').removeClass('is-checked');
            $('.button-group li:first-child').addClass('is-checked');
        }, 1000);
    });
    // change is-checked class on buttons
    $('.button-group').each(function(i, buttonGroup) {
        var $buttonGroup = $(buttonGroup);
        $buttonGroup.on('click', 'li', function() {
            $buttonGroup.find('.is-checked').removeClass('is-checked');
            $(this).addClass('is-checked');
        });
    });

    var firstTime = false;
    $("#resume-btn").on('click', function() {
        if (!firstTime) {
            var startColor = '#f4d03f';
            var endColor = '#f4d03f';
            var element1 = document.getElementById('line-container1');
            var element2 = document.getElementById('line-container2');
            var element3 = document.getElementById('line-container3');
            var circle1 = new ProgressBar.Circle(element1, {
                color: startColor,
                trailColor: '#fff6da',
                trailWidth: 7,
                duration: 2000,
                easing: 'easeInOut',
                strokeWidth: 8,
                // Set default step function for all animate calls
                step: function(state, circle1) {
                    circle1.path.setAttribute('stroke', state.color);
                    $("#progress-text1").text((circle1.value() * 100).toFixed(0) + "%");
                }
            });
            var circle2 = new ProgressBar.Circle(element2, {
                color: startColor,
                trailColor: '#fff6da',
                trailWidth: 7,
                duration: 2000,
                easing: 'easeInOut',
                strokeWidth: 8,
                // Set default step function for all animate calls
                step: function(state, circle2) {
                    circle2.path.setAttribute('stroke', state.color);
                    $("#progress-text2").text((circle2.value() * 100).toFixed(0) + "%");
                }
            });
            var circle3 = new ProgressBar.Circle(element3, {
                color: startColor,
                trailColor: '#fff6da',
                trailWidth: 7,
                duration: 2000,
                easing: 'easeInOut',
                strokeWidth: 8,
                // Set default step function for all animate calls
                step: function(state, circle3) {
                    circle3.path.setAttribute('stroke', state.color);
                    $("#progress-text3").text((circle3.value() * 100).toFixed(0) + "%");
                }
            });
            var line = new ProgressBar.Line('#bar1', {
                strokeWidth: 20,
                easing: 'easeInOut',
                duration: 1400,
                color: '#f4d03f',
                trailColor: '#fff6da',
                trailWidth: 21,
            });
            var line2 = new ProgressBar.Line('#bar2', {
                strokeWidth: 20,
                easing: 'easeInOut',
                duration: 1400,
                color: '#f4d03f',
                trailColor: '#fff6da',
                trailWidth: 21,
            });
            var line3 = new ProgressBar.Line('#bar3', {
                strokeWidth: 20,
                easing: 'easeInOut',
                duration: 1400,
                color: '#f4d03f',
                trailColor: '#fff6da',
                trailWidth: 21,
            });
            setTimeout(function() {
                circle1.animate(0.85, {
                    from: {
                        color: startColor
                    },
                    to: {
                        color: endColor
                    }
                });
                circle3.animate(0.95, {
                    from: {
                        color: startColor
                    },
                    to: {
                        color: endColor
                    }
                });
            }, 1200);
            setTimeout(function() {
                circle2.animate(0.70, {
                    from: {
                        color: startColor
                    },
                    to: {
                        color: endColor
                    }
                });
            }, 1500);
            setTimeout(function() {
                line.animate(1.0);
                line2.animate(1.0);
                line3.animate(1.0);
            }, 1800);
            firstTime = true;
        }
    });

    // Function for email address validation
    function validateEmail(email) {
        var re = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i;
        return re.test(email);
    }
    /******************** CONTACT FORM ********************/
    $('#contact-form').on('submit', function(e) {
       e.preventDefault();
       var  error_msg_wrp = $(this).parent(".mail-container").find('.cnmail'),
            error_msg = error_msg_wrp.find('.error-msg'),
            data = $(this).serialize();
       if (validateEmail( $(this).find('input[name="email"]').val() )) {
          $.ajax({
             type: "POST",
             url: $(this).attr('action'),
             data: data,
             success: function(e) {
                error_msg.fadeOut(200);
                $('#subscribe_modal_1').modal();// console.log(error_msg)
             }
          });
       } else {
          error_msg.fadeIn(200);
       }

        return false;
    });

});
}(jQuery));



