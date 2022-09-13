/**
 * @author Roger Wu
 * @version 1.0
 */
( function($) {
    $.fn.cssv = function(pre) {
        var cssPre = $(this).css(pre);
        return cssPre.substring(0, cssPre.indexOf("px")) * 1;
    };
    $.fn.jBar = function(options) {
        var op = $.extend({
            container: "#navTab",containerHeader: "#navTab .tabsPageHeader", toggleBut: ".toggleCollapse", sideBar: "#sidebar", sideBar2: "#sidebar_s", splitBar: "#splitBar",
            splitBar2: "#splitBarProxy", iconClass: "icon-chevron-sign-right"
        }, options);
        return this.each(function() {
            var jbar = this;
            var sbar = $(op.sideBar2, jbar);
            var bar = $(op.sideBar, jbar);
            $(op.toggleBut).click(function() {
                if($(op.splitBar).is(':visible')){
                    DWZ.ui.sbar = false;
                    $('.icon',op.toggleBut).addClass(op.iconClass);
                    $(op.splitBar).hide();
                    var barleft = sbar.outerWidth() - bar.outerWidth();
                    var cleft = $(op.container).cssv("margin-left") - bar.outerWidth();
                    var cwidth = bar.outerWidth() + $(op.container).outerWidth();
                    $(op.containerHeader).animate({
                        'margin-left': sbar.outerWidth()
                    },50);
                    sbar.show().css("left", 0);
                    $(op.container).animate({
                        'margin-left': cleft, width: cwidth
                    }, 50, function() {
                        bar.animate({
                            left: barleft,
                            top: sbar.outerHeight()
                        }, 50, function() {
                            bar.hide();
                            bar.css("bottom","auto");
                            $(window).trigger(DWZ.eventType.resizeGrid);
                        });
                    });
                    $(sbar).click(function() {
                        if (bar.is(":hidden") ) {
                            bar.show().animate({
                                left: 0
                            }, 50);
                            $(op.container).click(_hideBar);
                        } else {
                            bar.animate({
                                left: barleft
                            }, 50, function() {
                                bar.hide();
                            });
                        }
                        function _hideBar() {
                            $(op.container).off("click", null, _hideBar);
                            if (!DWZ.ui.sbar ) {
                                bar.animate({
                                    left: barleft
                                }, 50, function() {
                                    bar.hide();
                                });
                            }
                        }
                        return false;
                    });
                }else{
                    DWZ.ui.sbar = true;
                    $('.icon',op.toggleBut).removeClass(op.iconClass);
                    sbar.css('left', -50);
                    $(op.containerHeader).animate({
                        'margin-left': 0
                    }, 80);
                    bar.show().css('bottom',0).css('top',0).animate({
                        left: 0
                    }, 80, function() {
                        $(op.splitBar).show();
                        var cleft = bar.outerWidth();
                        var cwidth = $(op.container).outerWidth() - ( cleft - $(op.container).cssv("margin-left") );
                        $(op.container).css({
                            'margin-left': cleft, width: cwidth
                        });
                        $(sbar).off('click');
                        $(window).trigger(DWZ.eventType.resizeGrid);
                    });
                }
                return false;
            });
            $(op.splitBar).mousedown(function(event) {
                $(op.splitBar2).each(function() {
                    var spbar2 = $(this);
                    setTimeout(function() {
                        spbar2.show();
                    }, 10);
                    spbar2.css({
                        visibility: "visible", left: $(op.splitBar).css("left")
                    });
                    spbar2.jDrag($.extend(options, {
                        obj: $("#sidebar"), move: "horizontal", event: event, stop: function() {
                            $(this).css("visibility", "hidden");
                            var move = $(this).cssv("left") - $(op.splitBar).cssv("left");
                            var sbarwidth = bar.outerWidth() + move;
                            var cleft = $(op.container).cssv("margin-left") + move;
                            var cwidth = $(op.container).outerWidth() - move - 1;
                            bar.css("width", sbarwidth);
                            $(op.splitBar).css("left", $(this).css("left"));
                            $(op.container).css({
                                'margin-left': cleft, width: cwidth
                            });
                        }
                    }));
                    return false;
                });
            });
        });
    }
} )(jQuery);