/**
 * @author 张慧华 z@j-ui.com
 */
(function ($) {
    var _op = {
        cursor: 'move', // selector 的鼠标手势
        sortBoxs: 'div.sortDrag', //拖动排序项父容器
        items: '>.dragItem', //拖动排序项选择器
        selector: '', //拖动排序项用于拖动的子元素的选择器，为空时等于item
        zIndex: 1000
    };
    DWZ.sortDrag = {
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
            $helper.data('$sortBox', $sortBox).data('op', op).data('$item', $item).data('$placeholder', $placeholder);
            $helper.addClass('sortDragHelper').css({
                position: 'absolute', top: position.top , left: position.left, zIndex: op.zIndex, width: $item.width() + 'px',
                height: $item.height() + 'px'
            }).jDrag({
                selector: op.selector, drag: this.drag, stop: this.stop, event: event
            });
            $item.before($placeholder).before($helper).hide();
            return false;
        } ,
        drag: function (el, event) {
            var $helper = $(arguments[0]), $sortBox = $helper.data('$sortBox'), $placeholder = $helper.data('$placeholder');
            var $items = $sortBox.find($helper.data('op')['items']).filter(':visible').filter(':not(.sortDragPlaceholder, .sortDragHelper)');
            var helperPos = $helper.position();
            var $overBox = DWZ.sortDrag._getOverSortBox($helper);
            if ($sortBox.data('over-sort') == true && $overBox.length > 0 && $overBox[0] != $sortBox[0] ) { //移动到其他容器
                $placeholder.appendTo($overBox);
                $helper.data('$sortBox', $overBox);
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
            var $helper = $(arguments[0]), $item = $helper.data('$item'), $placeholder = $helper.data('$placeholder');
            $item.insertAfter($placeholder).show();
            $placeholder.remove();
            $helper.remove();
            DWZ.sortDrag._onDrag = false;
        },
        _createPlaceholder: function ($item) {
            return $('<' + $item[0].nodeName + ' class="sortDragPlaceholder"/>').css({
                width: $item.outerWidth() + 'px', height: $item.outerHeight() + 'px', marginTop: $item.css('marginTop'), marginRight: $item.css('marginRight'),
                marginBottom: $item.css('marginBottom'), marginLeft: $item.css('marginLeft')
            });
        },
        _getOverSortBox: function ($item) {
            var itemPos = $item.offset(),y = itemPos.top, x = itemPos.left + ($item.width() / 2);
            var op = $.extend({}, _op, $item.data('op'));
            return $(op.sortBoxs).filter(':visible').filter(function(){
                var $sortBox = $(this);
                return !$sortBox.data("accept") || -1 < $sortBox.data("accept").split(",").indexOf($item.data("type"));
            }).filter(function () {
                var $sortBox = $(this), sortBoxPos = $sortBox.offset(), sortBoxH = $sortBox.height(), sortBoxW = $sortBox.width();
                return DWZ.isOver(y, x, sortBoxPos.top, sortBoxPos.left, sortBoxH, sortBoxW);
            });
        }
    };

    $.fn.sortDrag = function (options) {
        return this.each(function () {
            var op = $.extend({}, _op, options);
            var $sortBox = $(this);
            if ($sortBox.attr('selector') ) {
                op.selector = $sortBox.attr('selector');
            }
            $sortBox.find(op.items).each(function (i) {
                var $item = $(this), $selector = $item;
                if (op.selector) {
                    $selector = $item.find(op.selector).css({cursor: op.cursor});
                }
                if (op.refresh) {
                    $selector.off('mousedown');
                }
                $selector.mousedown(function (event) {
                    if (!$sortBox.hasClass('disabled') && !$(event.target).is('input')&& !$(event.target).is('a')) {
                        DWZ.sortDrag.start($sortBox, $item, event, op);
                        event.preventDefault();
                    }
                });
            });

            $sortBox.find('.close').mousedown(function (event) {
                $(this).parent().remove();
                return false;
            });
        });
    }

})(jQuery);