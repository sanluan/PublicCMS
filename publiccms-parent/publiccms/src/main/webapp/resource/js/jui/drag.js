/**
 * @author Roger Wu
 */
( function($) {
    $.scrollPosParents = function(el){
        var $el = $(el);
        var scrollPos = {top:0, left:0};
        $el.parents().each(function(){
            var $p = $(this);
            scrollPos.top += $p.scrollTop();
            scrollPos.left += $p.scrollLeft();
        });
        return scrollPos;
    };

    $.fn.jDrag = function(options) {
        if (typeof options == "string" ) {
            if (options == "destroy" ) {
                return this.each(function() {
                    $(this).off("mousedown", $.rwdrag.start);
                    $.data(this, "pp-rwdrag", null);
                });
            }
        }
        return this.each(function() {
            var el = $(this);
            $.data($.rwdrag, "pp-rwdrag", {
                options: $.extend({
                    el: el, obj: el
                }, options)
            });
            if (options.event ) {
                $.rwdrag.start(options.event);
            } else {
                var select = options.selector;
                $(select, obj).on("mousedown", $.rwdrag.start);
            }
        });
    };
    $.rwdrag = {
        start: function(e) {
            document.onselectstart = function(e) {
                return false
            };// 禁止选择

            var data = $.data(this, "pp-rwdrag");
            var el = data.options.el[0];
            $.data(el, "pp-rwdrag", {
                options: data.options
            });
            if (!$.rwdrag.current ) {
                var me=e;
                if (me.touches){
                    me=me.touches[0];
                }
                $.rwdrag.current = {
                    el: el, oleft: parseInt(el.style.left) || 0, otop: parseInt(el.style.top) || 0, ox: me.pageX || me.screenX, oy: me.pageY || me.screenY
                };
                if (e.touches){
                    $(document).on("touchend", $.rwdrag.stop).on("touchmove", $.rwdrag.drag);
                } else {
                    $(document).on("mouseup", $.rwdrag.stop).on("mousemove", $.rwdrag.drag);
                }
            }
        },
        drag: function(e) {
            if (!e ) {
                var e = window.event;
            }
            var current = $.rwdrag.current;
            var data = $.data(current.el, "pp-rwdrag");
            me=e;
            if (me.touches){
                me=me.touches[0];
            }
            var left = ( current.oleft + ( me.pageX || me.clientX ) - current.ox );
            var top = ( current.otop + ( me.pageY || me.clientY ) - current.oy );
            if (data.options.move == "horizontal" ) {
                if ( ( data.options.minW && left >= $(data.options.obj).cssv("left") + data.options.minW )
                        && ( data.options.maxW && left <= $(data.options.obj).cssv("left") + data.options.maxW ) ) {
                    current.el.style.left = left + "px";
                } else if (data.options.scop ) {
                    if (data.options.relObj ) {
                        if ( ( left - parseInt(data.options.relObj.style.left) ) > data.options.cellMinW ) {
                            current.el.style.left = left + "px";
                        }
                    } else {
                        current.el.style.left = left + "px";
                    }
                }
            } else if (data.options.move == "vertical" ) {
                current.el.style.top = top + "px";
            } else {
                var selector = data.options.selector ? $(data.options.selector, data.options.obj): $(data.options.obj);
                if (left >= -selector.outerWidth() * 2 / 3 && ( left + selector.outerWidth() / 3 < $(window).width() )
                        && ( top + selector.outerHeight() < $(window).height() ) ) {
                    current.el.style.left = left + "px";
                    current.el.style.top = top + "px";
                }
            }
            if (data.options.drag ) {
                data.options.drag.apply(current.el, [ current.el, e ]);
            }
            return $.rwdrag.preventEvent(e);
        }, stop: function(e) {
            var current = $.rwdrag.current;
            var data = $.data(current.el, "pp-rwdrag");
            if (e.touches){
                $(document).off("touchmove", $.rwdrag.drag).off("touchend", $.rwdrag.stop);
            }else{
                $(document).off("mousemove", $.rwdrag.drag).off("mouseup", $.rwdrag.stop);
            }
            if (data.options.stop ) {
                data.options.stop.apply(current.el, [ current.el, e ]);
            }
            $.rwdrag.current = null;
            document.onselectstart = function(e) {
                return true
            };// 启用选择
            return $.rwdrag.preventEvent(e);
        }, preventEvent: function(e) {
            if (e.stopPropagation ) {
                e.stopPropagation();
            }
            if (!e.touches ){
                if (e.preventDefault ) {
                    e.preventDefault();
                }
                return false;
            }else{
                return true;
            }
            
        }
    };
} )(jQuery);
/**
 * @author 张慧华 z@j-ui.com
 */
(function ($) {
    var _op = {
        cursor: "move", // selector 的鼠标手势
        sortBoxs: "div.sortDrag", //拖动排序项父容器
        items: ">.dragItem", //拖动排序项选择器
        selector: "", //拖动排序项用于拖动的子元素的选择器，为空时等于item
        zIndex: 1000
    };
    JUI.sortDrag = {
        _onDrag: false, //用于判断重复绑定拖动事件
        start: function ($sortBox, $item, event, op) {
            var me = this;
            if (me._onDrag) {
                setTimeout(function () {
                    me._onDrag = false;
                }, 500);
                return false;
            }
            me._onDrag = true;

            var $placeholder = this._createPlaceholder($item);
            var $helper = $item.clone();
            var position = $item.position();
            var scrollPosParents = $.scrollPosParents($sortBox);
            $helper.data("$sortBox", $sortBox).data("op", op).data("$item", $item).data("$placeholder", $placeholder);
            $helper.addClass("sortDragHelper").css({
                position: "absolute", top: position.top , left: position.left, zIndex: op.zIndex, width: $item.width() + "px",
                height: $item.height() + "px"
            }).jDrag({
                selector: op.selector, drag: this.drag, stop: this.stop, event: event
            });
            $item.before($placeholder).before($helper).hide();
            return false;
        } ,
        drag: function (el, event) {
            var $helper = $(arguments[0]), $sortBox = $helper.data("$sortBox"), $placeholder = $helper.data("$placeholder");
            var $items = $sortBox.find($helper.data("op")["items"]).filter(":visible").filter(":not(.sortDragPlaceholder, .sortDragHelper)");
            var helperPos = $helper.position();
            var $overBox = JUI.sortDrag._getOverSortBox($helper);
            if ($sortBox.data("over-sort") == true && $overBox.length > 0 && $overBox[0] != $sortBox[0] ) { //移动到其他容器
                $placeholder.appendTo($overBox);
                $helper.data("$sortBox", $overBox);
            } else if($items.length){
                for (var i = 0; i < $items.length; i++) {
                    var $this = $items.eq(i), position = $this.position();
                    if (helperPos.top > position.top + 10) {
                        $this.after($placeholder);
                    } else if (helperPos.top <= position.top || helperPos.left <= position.left) {
                        $this.before($placeholder);
                        break;
                    }
                }
            }
        },
        stop: function () {
            var $helper = $(arguments[0]), $item = $helper.data("$item"), $placeholder = $helper.data("$placeholder");
            $item.insertAfter($placeholder).show();
            $placeholder.remove();
            $helper.remove();
            JUI.sortDrag._onDrag = false;
        },
        _createPlaceholder: function ($item) {
            return $("<" + $item[0].nodeName + " class=\"sortDragPlaceholder\"/>").css({
                width: $item.outerWidth() + "px", height: $item.outerHeight() + "px", marginTop: $item.css("marginTop"), marginRight: $item.css("marginRight"),
                marginBottom: $item.css("marginBottom"), marginLeft: $item.css("marginLeft")
            });
        },
        _getOverSortBox: function ($item) {
            var itemPos = $item.offset(),y = itemPos.top, x = itemPos.left + ($item.width() / 2);
            var op = $.extend({}, _op, $item.data("op"));
            return $(op.sortBoxs).filter(":visible").filter(function(){
                var $sortBox = $(this);
                return !$sortBox.data("accept") || -1 < $sortBox.data("accept").split(",").indexOf($item.data("type"));
            }).filter(function () {
                var $sortBox = $(this), sortBoxPos = $sortBox.offset(), sortBoxH = $sortBox.height(), sortBoxW = $sortBox.width();
                return JUI.isOver(y, x, sortBoxPos.top, sortBoxPos.left, sortBoxH, sortBoxW);
            });
        }
    };

    $.fn.sortDrag = function (options) {
        return this.each(function () {
            var op = $.extend({}, _op, options);
            var $sortBox = $(this);
            if ($sortBox.attr("selector") ) {
                op.selector = $sortBox.attr("selector");
            }
            $sortBox.find(op.items).each(function (i) {
                var $item = $(this), $selector = $item;
                if (op.selector) {
                    $selector = $item.find(op.selector).css({cursor: op.cursor});
                }
                if (op.refresh) {
                    $selector.off("mousedown touchstart");
                }
                $selector.on("mousedown touchstart",function (event) {
                    if (!$sortBox.hasClass("disabled") && !$(event.target).is("input")&& !$(event.target).is("a")) {
                        JUI.sortDrag.start($sortBox, $item, event, op);
                        if(!event.touchs) {
                            event.preventDefault();
                        }
                    }
                });
            });

            $sortBox.find(".close").one("mousedown touchstart",function (event) {
                $(this).parent().remove();
                return false;
            });
        });
    }

})(jQuery);

/**
 * @author 张慧华 z@j-ui.com
 */
(function($) {
    JUI.miscDrag = {
        start: function($sortBox, $item, event, op) {
            var $helper = $item.clone();
            var position = $item.position();
            $helper.addClass("sortDragHelper").css({
                position: "absolute",
                top: position.top + $sortBox.scrollTop(),
                left: position.left,
                zIndex: op.zIndex,
                minWidth: $item.width() + "px",
                height: $item.height() + "px"
            }).jDrag({
                drag: this.drag,
                stop: this.stop,
                event: event
            });
            $helper.data("$sortBox", $sortBox);
            $item.before($helper);
            return false;
        },
        drag: function(el, event) {},
        stop: function(el, event) {
            var $helper = $(arguments[0]), $sortBox = $helper.data("$sortBox"), $overBox = JUI.miscDrag._getOverSortBox($helper);
            if ($overBox.length > 0) {
                //移动到指定容器
                var $dragBox = $helper.appendTo($overBox).on("mousedown touchstart",function(event) {
                    $(this).jDrag({
                        event: event
                    });
                });
                var txt = $dragBox.html(), icon = $dragBox.data("icon"), id = $dragBox.data("id"), sequence = $overBox.find("> div").length;
                var overBoxPos = $overBox.position(), dragBoxPos = $dragBox.position();
                var content = icon ? "<img src=\"" + icon + "\" />" : txt;
                $dragBox.css({
                    height: "auto",
                    top: dragBoxPos.top - overBoxPos.top + "px",
                    left: dragBoxPos.left - overBoxPos.left + "px"
                });
                var rel = $sortBox.attr("rel");
                if (rel) {
                    $("<div class=\"sortDrag\" data-id=\"" + id + "\"><h2>" + sequence + "</h2></div>").appendTo(rel);
                }
            } else {
                $helper.remove();
            }
        },
        _getOverSortBox: function($item) {
            var itemPos = $item.offset(), y = itemPos.top + $item.height() / 2, x = itemPos.left + $item.width() / 2;
            var op = $item.data("op");
            return $(op.sortBoxs).filter(":visible").filter(function() {
                var $sortBox = $(this);
                return !$sortBox.data("accept") || -1 < $sortBox.data("accept").split(",").indexOf($item.data("type"));
            }).filter(function() {
                var $sortBox = $(this), sortBoxPos = $sortBox.offset(), sortBoxH = $sortBox.height(), sortBoxW = $sortBox.width();
                return JUI.isOver(y, x, sortBoxPos.top, sortBoxPos.left, sortBoxH, sortBoxW);
            });
        },
        _createPlaceholder: function($item) {
            return $("<" + $item[0].nodeName + " class=\"sortDragPlaceholder\"/>").css({
                height: $item.outerHeight() + "px",
                marginTop: $item.css("marginTop"),
                marginRight: $item.css("marginRight"),
                marginBottom: $item.css("marginBottom"),
                marginLeft: $item.css("marginLeft")
            });
        },
        startSortDrag: function($sortBox, $item, event, op) {
            var $placeholder = this._createPlaceholder($item);
            var $helper = $item.clone();
            var position = $item.position();
            $helper.data("$sortBox", $sortBox).data("op", op).data("$item", $item).data("$placeholder", $placeholder);
            $helper.addClass("sortDragHelper").css({
                position: "absolute",
                top: position.top + $sortBox.scrollTop(),
                left: position.left,
                zIndex: op.zIndex,
                width: $item.width() + "px",
                height: $item.height() + "px"
            }).jDrag({
                drag: this.dragSortDrag,
                stop: this.stopSortDrag,
                event: event
            });
            $item.before($helper).before($placeholder);
            return false;
        },
        dragSortDrag: function(el, event) {
            var $helper = $(arguments[0]), $sortBox = $helper.data("$sortBox"), $placeholder = $helper.data("$placeholder");
            // 修复出现滚动条拖拽位置
            var $unitBox = $helper.parents(".unitBox:first"), position = $helper.position();
            $helper.css({
                top: position.top + $unitBox.scrollTop()
            });
            var $overBox = JUI.miscDrag._getOverSortBox($helper);
            if ($overBox.length > 0 && $overBox[0] != $sortBox[0]) {
                //移动到其他容器
                var $items = $overBox.find(">.dragItem").filter(":visible").filter(":not(.sortDragPlaceholder, .sortDragHelper)");
                if ($items.length) {
                    helperPos = $helper.offset();
                    for (var i = 0; i < $items.length; i++) {
                        var $this = $items.eq(i), position = $this.offset();
                        if (helperPos.top > position.top + 10) {
                            $this.after($placeholder);
                        } else if (helperPos.top <= position.top || helperPos.left <= position.left) {
                            $this.before($placeholder);
                            break;
                        }
                    }
                } else {
                    $placeholder.appendTo($overBox);
                }
            }
        },
        stopSortDrag: function() {
            var $helper = $(arguments[0]), $sortBox = $helper.data("$sortBox"), $placeholder = $helper.data("$placeholder"), $item = $helper.data("$item");
            if ($placeholder && $placeholder.is(":visible")) {
                //复制到目标容器
                var $destBox = $placeholder.parents(".sortDrag:first");
                var html = $helper.html();
                var $result = $("<div class=\"dragItem\">" + html + "</div>");
                $result.attr("data-id", $helper.data("id"));
                $result.attr("data-type", $helper.data("type"));
                $result.insertAfter($placeholder).show();
                $placeholder.remove();
                $helper.remove();
                if ($sortBox.data("duplicate") != 1) {
                    $item.remove();
                }
                //从新绑定sortDrag
                if ($.fn.sortDrag) {
                    $destBox.sortDrag({
                        refresh: true
                    });
                }
            } else {
                $placeholder.remove();
                $helper.remove();
            }
        }
    };
    $.fn.extend({
        miscDrag: function(options) {
            var op = $.extend({
                cursor: "move",
                // selector 的鼠标手势
                sortBoxs: "div.miscDrag",
                //拖动排序项父容器
                items: "> dt .dragBox",
                //拖动排序项选择器
                zIndex: 1e3
            }, options);
            return this.each(function() {
                var $box = $(this);
                $box.find(op.items).each(function(i) {
                    var $item = $(this);
                    $item.on("mousedown touchstart",function(event) {
                        JUI.miscDrag.start($box, $item, event, op);
                        if(!event.touchs) {
                            event.preventDefault();
                        }
                    });
                });
            });
        },
        miscDragData: function() {
            var $miscDrag = $(this), $miscSortDrag = $($miscDrag.attr("rel")), $dragBoxList = $miscDrag.find("dd .dragBox"), $sortDragList = $miscSortDrag.find(".sortDrag");
            var data = [];
            for (var i = 0; i < $dragBoxList.length; i++) {
                var $dragBox = $dragBoxList.eq(i), $sortDrag = $sortDragList.eq(i), $dragBoxPos = $dragBox.position();
                var dataItem = {
                    id: $dragBox.data("id"),
                    top: parseInt($dragBoxPos.top),
                    left: parseInt($dragBoxPos.left),
                    items: []
                };
                $sortDrag.find(".dragItem").each(function(index) {
                    var $dragItem = $(this), $dragItemPos = $dragItem.position();
                    dataItem.items.push({
                        id: $dragItem.data("id")
                    });
                });
                data.push(dataItem);
            }
            return data;
        },
        miscSortDragData: function() {
            var $miscSortDrag = $(this), $sortDragList = $miscSortDrag.find(".sortDrag[data-parent=" + $miscSortDrag.data("id") + "]");
            function fillData($sortDragList) {
                var data = [];
                for (var i = 0; i < $sortDragList.length; i++) {
                    var $sortDrag = $sortDragList.eq(i);
                    var dataItem = {
                        items: []
                    };
                    if ($sortDrag.data("id")) {
                        dataItem.id = $sortDrag.data("id");
                    }
                    $sortDrag.find(">.dragItem").each(function() {
                        var $dragItem = $(this);
                        var itemData = {
                            id: $dragItem.data("id")
                        };
                        $dragItem.find(">.ctl-label>:input").each(function() {
                            var $lable = $(this), lableName = $lable.data("name");
                            if (lableName) {
                                if ("checkbox" == $lable.attr("type")) {
                                    itemData[lableName] = $lable.is(":checked");
                                } else {
                                    itemData[lableName] = $lable.val();
                                }
                            }
                        });
                        $dragItemSortDragList = $dragItem.find(">.ctl-label .sortDrag");
                        if ($dragItemSortDragList.length) {
                            itemData.items = fillData($dragItemSortDragList);
                        }
                        dataItem.items.push(itemData);
                    });
                    data.push(dataItem);
                }
                return data;
            }
            return fillData($sortDragList);
        },
        miscSortDrag: function(options) {
            var op = $.extend({
                cursor: "move",
                // selector 的鼠标手势
                sortBoxs: "dl.miscSortDrag .sortDrag",
                //拖动排序项父容器
                items: "> dt .dragItem",
                //拖动排序项选择器
                zIndex: 1e3
            }, options);
            return this.each(function() {
                var $sortBox = $(this);
                $sortBox.find(op.items).each(function(i) {
                    var $item = $(this);
                    $item.on("mousedown touchstart",function(event) {
                        JUI.miscDrag.startSortDrag($sortBox, $item, event, op);
                        if(!event.touchs) {
                            event.preventDefault();
                        }
                    });
                });
            });
        }
    });
})(jQuery);