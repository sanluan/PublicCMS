/**
 * @author Roger Wu
 */

( function($) {
    var jmenus = new Map();
    // If the JUI scope is not available, add it
    $.jui = $.jui || {};
    $.fn.extend({
        accordion: function(options, data) {
            var args = Array.prototype.slice.call(arguments, 1);
            return this.each(function() {
                if (options.fillSpace) jmenus.put(options.fillSpace, this);
                if (typeof options == "string" ) {
                    var accordion = $.data(this, "jui-accordion");
                    accordion[options].apply(accordion, args);
                    // INIT with optional options
                } else if (!$(this).is(".jui-accordion") ) {
                    $.data(this, "jui-accordion", new $.jui.accordion(this, options));
                }
            });
        },
        /**
         * deprecated, use accordion("activate", index) instead
         *
         * @param {Object}
         *            index
         */
        activate: function(index) {
            return this.accordion("activate", index);
        }
    });
    $.jui.accordion = function(container, options) {

        // setup configuration
        this.options = options = $.extend({}, $.jui.accordion.defaults, options);
        this.element = container;
        $(container).addClass("jui-accordion");
        // calculate active if not specified, using the first header
        options.headers = $(container).find(options.header);
        options.active = findActive(options.headers, options.active);
        if ( options.fillSpace ) {
            fillSpace(options.fillSpace);
        } else if ( options.autoheight ) {
            var maxHeight = 0;
            options.headers.next().each(function() {
                maxHeight = Math.max(maxHeight, $(this).outerHeight());
            }).height(maxHeight);
        }
        options.headers.not(options.active || "").next().hide();
        options.active.find("h2").addClass(options.selectedClass);
        options.active.find("h2 .icon").addClass(options.selectedIconClass);
        if (options.event ) {
            $(container).on( ( options.event ) + ".jui-accordion", null, null, clickHandler);
        }
    };
    $.jui.accordion.prototype = {
        activate: function(index) {
            // call clickHandler with custom event
            clickHandler.call(this.element, {
                target: findActive(this.options.headers, index)[0]
            });
        },

        enable: function() {
            this.options.disabled = false;
        }, disable: function() {
            this.options.disabled = true;
        }, destroy: function() {
            this.options.headers.next().css("display", "");
            if ( this.options.fillSpace || this.options.autoheight ) {
                this.options.headers.next().css("height", "");
            }
            $.removeData(this.element, "jui-accordion");
            $(this.element).removeClass("jui-accordion").off(".jui-accordion");
        }
    }
    function scopeCallback(callback, scope) {
        return function() {
            return callback.apply(scope, arguments);
        };
    }
    function completed(cancel) {
        // if removed while animated data can be empty
        if (!$.data(this, "jui-accordion") ) {
            return;
        }
        var instance = $.data(this, "jui-accordion");
        var options = instance.options;
        options.running = cancel ? 0: --options.running;
        if (options.running ) {
            return;
        }
        if (options.clearStyle ) {
            options.toShow.add(options.toHide).css({
                height: "", overflow: ""
            });
        }
        $(this).triggerHandler("change.jui-accordion", [ options.data ], options.change);
    }
    function fillSpace(key){
        var obj = jmenus.get(key);
        if (!obj) return;

        var parent = $(obj).parent();
        var height = parent.height() - (($(".accordionHeader", obj).length) * ($(".accordionHeader:first-child", obj).outerHeight())) -2;

        var os = parent.children().not(obj);
        $.each(os, function(i){
            height -= $(os[i]).outerHeight();
        });
        $(".accordionContent",obj).height(height);
    }
    function toggle(toShow, toHide, data, clickedActive, down) {
        var options = $.data(this, "jui-accordion").options;
        options.toShow = toShow;
        options.toHide = toHide;
        options.data = data;
        var complete = scopeCallback(completed, this);

        // count elements to animate
        options.running = toHide.length == 0 ? toShow.length: toHide.length;
        if (options.animated ) {
            if (!options.alwaysOpen && clickedActive ) {
                $.jui.accordion.animations[options.animated]({
                    toShow: jQuery([ ]), toHide: toHide, complete: complete, down: down, autoheight: options.autoheight
                });
            } else {
                $.jui.accordion.animations[options.animated]({
                    toShow: toShow, toHide: toHide, complete: complete, down: down, autoheight: options.autoheight
                });
            }
        } else {
            if (!options.alwaysOpen && clickedActive ) {
                toShow.toggle();
            } else {
                toHide.hide();
                toShow.show();
            }
            complete(true);
        }
    }
    function clickHandler(event) {
        var options = $.data(this, "jui-accordion").options;
        if (options.disabled ) {
            return false;
        }

        // called only when using activate(false) to close all parts
        // programmatically
        if (!event.target && !options.alwaysOpen ) {
            options.active.find("h2").toggleClass(options.selectedClass);
            options.active.find("h2 .icon").toggleClass(options.selectedIconClass);
            var toHide = options.active.next(), data = {
                instance: this, options: options, newHeader: jQuery([ ]), oldHeader: options.active, newContent: jQuery([ ]), oldContent: toHide
            }, toShow = options.active = $([ ]);
            toggle.call(this, toShow, toHide, data);
            return false;
        }
        // get the click target
        var clicked = $(event.target);

        // due to the event delegation model, we have to check if one
        // of the parent elements is our actual header, and find that
        if (clicked.parents(options.header).length ) {
            while (!clicked.is(options.header)) {
                clicked = clicked.parent();
            }
        }
        var clickedActive = clicked[0] == options.active[0];

        // if animations are still active, or the active header is the target,
        // ignore click
        if (options.running || ( options.alwaysOpen && clickedActive ) ) {
            return false;
        }
        if (!clicked.is(options.header) ) {
            return;
        }

        // switch classes
        options.active.find("h2").toggleClass(options.selectedClass);
        options.active.find("h2 .icon").toggleClass(options.selectedIconClass);
        if (!clickedActive ) {
            clicked.find("h2").addClass(options.selectedClass);
            clicked.find("h2 .icon").toggleClass(options.selectedIconClass);
        }

        // find elements to show and hide
        var toShow = clicked.next(), toHide = options.active.next(),
        // data = [clicked, options.active, toShow, toHide],
        data = {
            instance: this, options: options, newHeader: clicked, oldHeader: options.active, newContent: toShow, oldContent: toHide
        }, down = options.headers.index(options.active[0]) > options.headers.index(clicked[0]);
        options.active = clickedActive ? $([ ]): clicked;
        toggle.call(this, toShow, toHide, data, clickedActive, down);
        return false;
    }

    function findActive(headers, selector) {
        return selector != undefined ? typeof selector == "number" ? headers.filter(":eq(" + selector + ")"): headers.not(headers.not(selector)): selector === false ? $([ ])
                : headers.filter(":eq(0)");
    }
    $.extend($.jui.accordion, {
        defaults: {
            selectedClass: "collapsable",selectedIconClass: "icon-chevron-down", alwaysOpen: true, animated: 'slide', event: "click", header: ".accordionHeader", autoheight: true, running: 0, clearStyle: true
        }, animations: {
            slide: function(options, additions) {
                options = $.extend({
                    easing: "swing", duration: 100
                }, options, additions);
                if (!options.toHide.length ) {
                    options.toShow.animate({
                        height: "show"
                    }, options);
                    return;
                }
                var hideHeight = options.toHide.height(), showHeight = options.toShow.height(), difference = showHeight / hideHeight;
                options.toShow.css({
                    height: '0px'
                }).show();
                options.toHide.filter(":hidden").each(options.complete).end().filter(":visible").animate({
                    height: "hide"
                }, {
                    step: function(now) {
                        var current = ( hideHeight - now ) * difference;
                        if (!$.support.leadingWhitespace ) {
                            current = Math.ceil(current);
                        }
                        options.toShow.height(current);
                    }, duration: options.duration, easing: options.easing, complete: function() {
                        options.toShow.css({
                            height: showHeight+'px'
                        });
                        options.toShow.css({
                            overflow: "auto"
                        });
                        options.complete();
                    }
                });
            }, bounceslide: function(options) {
                this.slide(options, {
                    easing: options.down ? "bounceout": "swing", duration: options.down ? 200: 100
                });
            }, easeslide: function(options) {
                this.slide(options, {
                    easing: "easeinout", duration: 100
                })
            }
        }
    });
} )(jQuery);
/**
 * @author zhanghuihua@msn.com
 */
( function($) {
    $.fn.navMenu = function(callback) {
        return this.each(function() {
            var $box = $(this);
            var $callback = callback;
            $box.find("li>a").click(function() {
                var $a = $(this);
                if(!$a.is("[href^=javascript]")){
                    $("#sidebar #menu").ajaxUrl({
                        type: "get", url: $a.attr("href"), callback: function(response) {
                            $box.find("li").removeClass("selected");
                            $a.parent().addClass("selected");
                            if($callback ) {
                                $callback();
                                $callback = null;
                            }
                        }
                    });
                }
                return false;
            });
        });
    }
} )(jQuery);
/**
 * @author zhanghuihua@msn.com
 */

( function($) {
    var menu,  hash;
    $.fn.extend({
        contextMenu: function(id, options) {
            var op = $.extend({
                bindings: {}, ctrSub: null
            }, options);
            if (!menu ) {
                menu = $('<div id="contextmenu"></div>').appendTo('body').hide();
            }
            hash = hash || [ ];
            hash.push({
                id: id, bindings: op.bindings || {}, ctrSub: op.ctrSub
            });
            var index = hash.length - 1;
            $(this).on('contextmenu', null, null, function(e) {
                display(index, this, e, op);
                return false;
            });
            return this;
        }
    });
    function display(index, trigger, e, options) {
        var cur = hash[index];
        var content = $(JUI.frag[cur.id]);
        content.find('li');

        // Send the content to the menu
        menu.html(content);
        $.each(cur.bindings, function(id, func) {
            $("[rel='" + id + "']", menu).on('click', null, null, function(e) {
                hide();
                func($(trigger), $("#" + cur.id));
            });
        });
        var posX = e.pageX;
        var posY = e.pageY;
        if ($(window).width() < posX + menu.width() ) {
            posX -= menu.width();
        }
        if ($(window).height() < posY + menu.height() ) {
            posY -= menu.height();
        }
        menu.css({
            'left': posX+'px', 'top': posY+'px'
        }).show();
        $(document).one('click', hide);
        if ("function" === typeof cur.ctrSub ) {
            cur.ctrSub($(trigger), $("#" + cur.id));
        }
    }
    function hide() {
        menu.hide();
    }
} )(jQuery);
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
                    JUI.ui.sbar = false;
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
                            $(window).trigger(JUI.eventType.resizeGrid);
                            $(window).trigger(JUI.eventType.resizeChart);
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
                            if (!JUI.ui.sbar ) {
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
                    JUI.ui.sbar = true;
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
                        $(window).trigger(JUI.eventType.resizeGrid);
                        $(window).trigger(JUI.eventType.resizeChart);
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