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
        var content = $(DWZ.frag[cur.id]);
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