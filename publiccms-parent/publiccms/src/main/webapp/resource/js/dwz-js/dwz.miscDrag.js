
/**
 * @author 张慧华 z@j-ui.com
 */
(function($) {
    DWZ.miscDrag = {
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
            var $helper = $(arguments[0]), $sortBox = $helper.data("$sortBox"), $overBox = DWZ.miscDrag._getOverSortBox($helper);
            if ($overBox.length > 0) {
                //移动到指定容器
                var $dragBox = $helper.appendTo($overBox).mousedown(function(event) {
                    $(this).jDrag({
                        event: event
                    });
                });
                var txt = $dragBox.html(), icon = $dragBox.data("icon"), id = $dragBox.data("id"), sequence = $overBox.find("> div").length;
                var overBoxPos = $overBox.position(), dragBoxPos = $dragBox.position();
                var content = icon ? '<img src="' + icon + '" />' : txt;
                $dragBox.css({
                    height: "auto",
                    top: dragBoxPos.top - overBoxPos.top + "px",
                    left: dragBoxPos.left - overBoxPos.left + "px"
                });
                var rel = $sortBox.attr("rel");
                if (rel) {
                    $('<div class="sortDrag" data-id="' + id + '"><h2>' + sequence + "</h2></div>").appendTo(rel);
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
                return DWZ.isOver(y, x, sortBoxPos.top, sortBoxPos.left, sortBoxH, sortBoxW);
            });
        },
        _createPlaceholder: function($item) {
            return $("<" + $item[0].nodeName + ' class="sortDragPlaceholder"/>').css({
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
            var $overBox = DWZ.miscDrag._getOverSortBox($helper);
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
                var $result = $('<div class="dragItem">' + html + "</div>");
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
                    $item.mousedown(function(event) {
                        DWZ.miscDrag.start($box, $item, event, op);
                        event.preventDefault();
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
                        $dragItem.find(".ctl-label :input").each(function() {
                            var $lable = $(this), lableName = $lable.data("name");
                            if (lableName) {
                                if ("checkbox" == $lable.attr("type")) {
                                    itemData[lableName] = $lable.is(":checked");
                                } else {
                                    itemData[lableName] = $lable.val();
                                }
                            }
                        });
                        $dragItemSortDragList = $dragItem.find(">.dragContent .sortDrag");
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
                    $item.mousedown(function(event) {
                        DWZ.miscDrag.startSortDrag($sortBox, $item, event, op);
                        event.preventDefault();
                    });
                });
            });
        }
    });
})(jQuery);