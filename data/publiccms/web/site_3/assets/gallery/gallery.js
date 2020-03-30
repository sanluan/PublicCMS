/**
 * 类腾讯高清组图展示组件
 *
 * @copyright (c) CmsTop {@link http://www.cmstop.com}
 * @author    micate {@link http://micate.me}
 * @homepage  @github {@link http://github.com/micate/gallery}
 * @depends   jQuery 1.3.2+
 * @version   $Id$
 */
(function($, root) {

    Array.prototype.indexOf || (Array.prototype.indexOf = function(searchElement, fromIndex) {
        "use strict";
        if (this == null) {
            throw new TypeError();
        }
        var self = Object(this),
            len = self.length >>> 0;
        if (len === 0) {
            return -1;
        }
        var n = 0;
        if (arguments.length > 0) {
            n = Number(arguments[1]);
            if (n != n) {
                n = 0;
            } else if (n != 0 && n != Infinity && n != -Infinity) {
                n = (n > 0 || -1) * Math.floor(Math.abs(n));
            }
        }
        if (n >= len) {
            return -1;
        }
        var k = n >= 0 ? n : Math.max(len - Math.abs(n), 0);
        for (; k < len; k++) {
            if (k in self && self[k] === searchElement) {
                return k;
            }
        }
        return -1;
    });

    var IE6 = !$.support.leadingWhitespace,
        toString = Object.prototype.toString,
        isString = function(val) {
            return toString.call(val) === '[object String]';
        },
        isPlainObject = function(val) {
            return val && toString.call(val) === '[object Object]' && 'isPrototypeOf' in val;
        },
        isFunction = function(val) {
            return toString.call(val) === '[object Function]';
        },
        isArray = Array.isArray || function(val) {
            return toString.call(val) === '[object Array]';
        },
        each = function() {
            var R_SPLIT = /[\s|,]+/;
            return function(val, func) {
                if (! val || ! isFunction(func)) {
                    return;
                }
                val = val.split(R_SPLIT);
                for (var index = 0, length = val.length, item = val[index];
                     index < length && func.call(item, item, index) !== false;
                     item = val[++index]) {
                }
            };
        }(),
        clone = function(object) {
            if (object == null || typeof object != 'object') {
                return object;
            }
            var temp = new object.constructor(), key;
            for (key in object) {
                if (object[key] === object) {
                    continue;
                }
                temp[key] = clone(object[key]);
            }
            return temp;
        },
        ucfirst = function(val) {
            if (! isString(val)) {
                return val;
            }
            return val.charAt(0).toUpperCase() + val.slice(1);
        },
        template = function(template, values) {
            var key;
            values = isPlainObject(values) ? values : {};
            for (key in values) {
                if (values.hasOwnProperty(key)) {
                    template = template.replace(new RegExp('{' + key + '}', 'gm'), values[key] || '');
                }
            }
            return template;
        };

    /**
     * 轻量级的 Class.extend() 实现，来自 John Resig
     *
     * 修改了默认的 _super() 方法为 parent()
     *
     * @link http://ejohn.org/blog/simple-javascript-inheritance/
     */
    var Class = function() {
        var initializing = false,
            fnTest = /xyz/.test(function() {xyz;}) ? /\bparent\b/ : /.*/,
            Class = function() {};
        Class.extend = function(proporties) {
            var parent = this.prototype;

            initializing = true;
            var prototype = new this();
            initializing = false;

            for (var name in proporties) {
                if (typeof proporties[name] == 'function'
                    && typeof parent[name] == 'function'
                    && fnTest.test(proporties[name])) {
                    prototype[name] = (function(name, func) {
                        return function() {
                            var temp = this.parent;
                            this.parent = parent[name];
                            var ret = func.apply(this, arguments);
                            this.parent = temp;
                            return ret;
                        };
                    })(name, proporties[name]);
                } else if (isArray(proporties[name])) {
                    prototype[name] = proporties[name].slice();
                } else if (isPlainObject(proporties[name]) || isFunction(proporties[name])) {
                    prototype[name] = clone(proporties[name]);
                } else {
                    prototype[name] = proporties[name];
                }
            }

            function Class() {
                if (! initializing && this.init) {
                    this.init.apply(this, arguments);
                }
            }

            Class.prototype = prototype;
            Class.prototype.constructor = Class;
            Class.extend = arguments.callee;

            return Class;
        };
        return Class;
    }();

    var Base = Class.extend({
        name: '',
        element: null,
        guid: null,
        _eventNames: '',
        init: function(options) {
            var self = this;
            if (! isPlainObject(options) || ! options.element) {
                throw new TypeError('参数不正确');
            }
            options = this.options = $.extend({}, this.OPTIONS, options);
            this.guid = this.name.toUpperCase() + ((new Date()).getTime().toString(16));
            this.element = this.options.element.jquery ? this.options.element : $(this.options.element);
            each(this._eventNames, function(event) {
                event = ucfirst(event);
                var before = 'before' + event,
                    after = 'after' + event;
                options[before] && (self.bind(before, options[before]));
                options[after] && (self.bind(after, options[after]));
            });
            return this;
        },
        find: function(name, context) {
            return (context && context.jquery ? context : this.element).find('[data-' + this.name + '=' + name + ']');
        },
        bind: function(event, func) {
            (event in this.events) || (this.events[event] = []);
            this.events[event].push(func);
            return this;
        },
        trigger: function(event, args) {
            if (event in this.events) {
                for (var index = 0, func;
                     (func = this.events[event][index++]) && (func.apply(this, args || []) !== false); ) {
                }
            }
            return this;
        }
    });

    var Slider = Base.extend({
        OPTIONS: {
            element: null,
            photos: [],
            photoWidth: 100,
            photoHeight: 75,
            currentClass: 'current',
            current: 0,
            btnMinWidth: 65
        },
        name: 'slider',
        options: {},
        events: {},

        _eventNames: 'show updateScrollbarPosition updateThumbListPosition',

        _width: 0,
        _itemWidth: 0,
        _totalWidth: 0,
        _visibleWidth: 0,
        _visibleSize: 0,
        _itemsScrollWidth: 0,

        _total: 0,
        _current: -1,
        _draging: false,

        _control: null,
        _bar: null,
        _items: null,
        _btn: null,
        _itemsLeft: null,
        _itemsRight: null,

        init: function(options) {
            this.parent(options);

            if (! isArray(this.options.photos)) {
                this.options.photos = [];
            }

            this._width = this.element.width();
            this._control = this.find('control');
            this._bar = this.find('bar');
            this._items = this.find('items');
            this._itemsLeft = this.find('items-left');
            this._itemsRight = this.find('items-right');
            this._total = this.options.photos.length;
            this._btn = this.find('btn');

            this.render();
            return this;
        },
        render: function() {
            var self = this,
                o = this.options,
                items = this._items.empty().show(),
                index = 0, total = o.photos.length;

            // 构建列表
            $.each(o.photos, function(index, photo) {
                var item = $('<li><a hideFocus="true" href="javascript:void(0);"><img /></a></li>')
                    .attr('data-slider-index', index);
                item.find('a,img').css({
                    width: o.photoWidth,
                    height: o.photoHeight
                });
                item.find('img').attr('src', photo.thumb || photo).attr('alt', photo.note || '');
                item.click(function() {
                    self.show(index);
                }).appendTo(items);
                if (! self._itemWidth) {
                    self._itemWidth = item.outerWidth(true);
                }
            });

            // 修正缩略图列表的宽度
            this._totalWidth = this._itemWidth * total;
            items.css('width', this._totalWidth).show();

            // 计算一些数值
            this._visibleWidth = items.parent().innerWidth();
            this._visibleSize = Math.floor(this._visibleWidth / this._itemWidth);
            this._itemsScrollWidth = Math.max(this._total - this._visibleSize, 0) * this._itemWidth;

            // 按钮点击向左向右滚动
            if (this._total > this._visibleSize) {
                var interval,
                    fps = 13,
                    speed = 200,
                    moveStop = function() {
                        interval && clearInterval(interval);
                    },
                    moveLeft = function() {
                        var left = Math.abs(parseInt(items.css('left')));
                        left = Math.max(0, Math.ceil(left - Math.ceil(self._itemsScrollWidth / speed)));
                        self.updateThumbListPosition(left * -1);
                        if (left === 0) {
                            moveStop();
                        }
                    },
                    moveRight = function() {
                        var left = Math.abs(parseInt(items.css('left')));
                        left = Math.min(self._itemsScrollWidth, Math.ceil(left + Math.ceil(self._itemsScrollWidth / speed)));
                        self.updateThumbListPosition(left * -1);
                        if (left === self._itemsScrollWidth) {
                            moveStop();
                        }
                    };
                this._itemsLeft.bind('mousedown', function() {
                    moveStop();
                    interval = setInterval(moveLeft, fps);
                });
                this._itemsLeft.bind('mouseup', function() {
                    moveStop();
                });
                this._itemsRight.bind('mousedown', function() {
                    moveStop();
                    interval = setInterval(moveRight, fps);
                });
                this._itemsRight.bind('mouseup', function() {
                    moveStop();
                });
            }

            // 显示预设缩略图
            this.show(o.current || 0);

            // 初始化滚动条
            if (this._bar && this._bar.length) {
                this.renderScrollbar();
            }

            return this;
        },
        show: function(index) {
            var o = this.options,
                items = this._items,
                item = items.children().eq(index);

            if (! item.length || index === this._current) {
                return false;
            }

            this.trigger('beforeShow', [index]);

            items.children().eq(this._current).removeClass(o.currentClass);
            item.addClass(o.currentClass);

            var total = this._total,
                visibleSize = this._visibleSize,              // 可见个数
                centerSize = Math.ceil(visibleSize / 2),      // 水平居中的个数
                pos = 0;                                      // 最终用来计算的位置值

            // 总数大于可见数时才滚动
            if (total > visibleSize) {
                if (index < centerSize) { // 靠近开始
                    pos = 0;
                } else if ((total - (index + 1)) < centerSize) { // 靠近结束
                    pos = total - visibleSize;
                } else { // 中间部分
                    pos = (index + 1) - centerSize;
                }
                this.updateThumbListPosition(this._itemWidth * pos * -1, true);
            }

            this._current = index;

            this.trigger('afterShow', [index]);
            return this;
        },
        renderScrollbar: function() {
            var self = this,
                startLeft, startX,
                btn = this._btn,
                btnParent = btn.parent(),
                btnParentWidth = btnParent.innerWidth(),
                btnWidth = Math.max(this.options.btnMinWidth, Math.floor(btnParentWidth * (
                    this._total > this._visibleSize
                        ? (this._visibleSize || 1) / (this._total || 1)
                        : 1
                ))),
                btnMaxLeftValue = (btnParentWidth - btnWidth) || 1,
                itemsScrollWidth = this._itemsScrollWidth;

            // 修正滚动条宽度
            btn.width(btnWidth);

            // 总数小于等于可见数时不初始化滚动条
            if (this._total <= this._visibleSize) {
                return this;
            }

            // 缩略图位置变化后改变滚动条位置
            this.bind('afterUpdateThumbListPosition', function(left, animate) {
                self.updateScrollbarPosition(Math.max(0, Math.min(Math.floor(Math.abs(left) / itemsScrollWidth * btnMaxLeftValue), btnMaxLeftValue)), animate);
            });

            // 拖动滚动条查看
            btn.bind('mousedown.slider', function(ev) {
                var position = btn.position(),
                    doc = $(document);
                self._draging && doc.trigger('mouseup.slider');
                self._draging = true;

                startLeft = position.left;
                startX = ev.pageX;

                doc.bind('mousemove.slider', function(ed) {
                    if (! self._draging) {
                        return;
                    }
                    var left = startLeft + ed.clientX - startX;
                    left = Math.max(0, Math.min(left, btnMaxLeftValue));
                    // 直接更新缩略图位置而非采用绑定触发方式，避免循环绑定；滚动条的更新采用触发方式
                    self.updateThumbListPosition(Math.floor(left / btnMaxLeftValue * itemsScrollWidth) * -1);
                    ed.preventDefault();
                });
                doc.bind('selectstart.slider', function() {
                    return false;
                });
                doc.bind('mouseup.slider', function() {
                    doc.unbind('.slider');
                    self._draging = false;
                });
                // 阻止默认事件，不触发浏览器的拖拽事件
                ev.preventDefault();
            });

            // 滚动条父元素点击后触发滚动条滑动
            btnParent.bind('mousedown', function(ev) {
                var left = ev.pageX - btnParent.position().left,
                    oldLeft = btn.position().left,
                    scrollbarLeft = oldLeft,
                    thumblistLeft;
                if (left < oldLeft) { // 左移一个长度
                    scrollbarLeft = oldLeft - btnWidth;
                } else if (left > (oldLeft + btnWidth)) { // 右移一个长度
                    scrollbarLeft = oldLeft + btnWidth;
                }
                scrollbarLeft = Math.max(0, Math.min(scrollbarLeft, btnMaxLeftValue)),
                thumblistLeft = Math.floor(scrollbarLeft / btnMaxLeftValue * itemsScrollWidth) * -1;
                self.updateThumbListPosition(thumblistLeft, true);
            });

            return this;
        },
        updateScrollbarPosition: function(left, animate) {
            this.trigger('beforeUpdateScrollbarPosition', [left, animate]);
            this._btn.stop(false, true);
            if (animate) {
                this._btn.animate({
                    left: left
                });
            } else {
                this._btn.css('left', left);
            }
            this.trigger('afterUpdateScrollbarPosition', [left, animate]);
            return this;
        },
        updateThumbListPosition: function(left, animate) {
            this.trigger('beforeUpdateThumbListPosition', [left, animate]);
            this._items.stop(false, true);
            if (animate) {
                this._items.animate({
                    left: left
                });
            } else {
                this._items.css('left', left);
            }
            this.trigger('afterUpdateThumbListPosition', [left, animate]);
            return this;
        }
    });

    var Pager = Base.extend({
        OPTIONS: {
            element: null,
            total: 0,
            visible: 9,
            current: 0,
            currentClass: 'current',
            center: false
        },
        name: 'pager',
        options: {},
        events: {},

        _current: 0,
        _total: 1,

        _prev: null,
        _next: null,
        _items: null,

        _eventNames: 'prev next jumpTo show',

        init: function(options) {
            this.parent(options);

            options = this.options;
            this._prev = this.find('prev');
            this._next = this.find('next');
            this._items = this.find('items').empty();
            this._total = options.total || 1;

            this.render(options.current || 1);
            return this;
        },
        render: function(current) {
            var self = this;

            this._prev.click(function() {
                self.prev();
            });
            this._next.click(function() {
                self.next();
            });

            // 显示当前页码
            this.jumpTo(current);

            return this;
        },
        renderItems: function(current) {
            var self = this,
                o = this.options,
                half = Math.floor((o.visible - 1) / 2),
                total = this._total,
                start = Math.max(1, Math.min(current - half, total - o.visible + 1)),
                end = Math.min(total, Math.max(current + half, start + o.visible - 1));

            // 清空页码区域
            this._items.empty();

            // 回到第一页
            if (start !== 1) {
                $('<li data-pager-index="1"><a href="javascript:void(0);" hideFocus="true">1 …</a></li>').appendTo(this._items);
            }

            // 生成页码
            for (var i = start; i <= end; i++) {
                $('<li data-pager-index="' + i + '"><a href="javascript:void(0);" hideFocus="true">' + i + '</a></li>').appendTo(this._items);
            }

            // 跳到最后一页
            if (end !== total) {
                $('<li data-pager-index="' + total + '"><a href="javascript:void(0);" hideFocus="true">… ' + total + '</a></li>').appendTo(this._items);
            }

            this._items.children().click(function(ev) {
                var page = parseInt($(this).attr('data-pager-index'));
                if (! page || page === self._current) {
                    return false;
                }
                self.jumpTo(page);
                ev.preventDefault();
            });

            this._items.find('[data-pager-index=' + current + ']').addClass(o.currentClass);

            // 居中显示
            if (o.center) {
                this.element.css({
                    left: Math.floor((this.element.parent().innerWidth() - this.element.outerWidth()) / 2)
                });
            }

            return this;
        },
        prev: function() {
            this.show(this._current - 1, 'prev');
            return this;
        },
        next: function() {
            this.show(this._current + 1, 'next');
            return this;
        },
        jumpTo: function(index) {
            this.show(index, 'jumpTo');
            return this;
        },
        show: function(index, event) {
            if (index === this._current) {
                return this;
            }

            event = ucfirst(event);
            this.trigger('beforeShow', [index, 'before' + event]);

            // 序号合法判断，放在这里是为了能够监听到 beforeShow 事件的不规范序号，
            // 如监听第一个之前或最后一个之后的事件
            if (index <= 0 || index > this._total) {
                return false;
            }

            this.trigger('before' + event, [index]);

            this.renderItems(index);
            this._current = index;

            this.trigger('after' + event, [index]);
            this.trigger('afterShow', [index, 'after' + event]);
            return this;
        }
    });

    /**
     * 以分页列表方式展示小图
     *
     * @type {PhotoList}
     */
    var PhotoList = Base.extend({
        OPTIONS: {
            element: null,
            photos: [],
            photoWidth: 100,
            photoHeight: 75,
            currentClass: 'current',
            current: 0,
            pagerHeight: 50
        },
        name: 'photolist',
        options: {},
        events: {},
        pager: null,

        _items: null,

        _width: 0,
        _height: 0,
        _itemWidth: 0,
        _itemHeight: 0,
        _rendered: false,

        _pagesize: 1,
        _totalPage: 1,
        _current: 0,
        _currentPage: 1,

        _eventNames: 'show hide select',

        init: function(options) {
            this.parent(options);

            if (! isArray(this.options.photos)) {
                this.options.photos = [];
            }

            options = this.options;
            this._items = this.find('items');
            this._current = options.current || 0;

            // 当元素为可见时渲染，否则宽度和高度获取不正确
            if (this.element.is(':visible')) {
                this.render();
            }

            return this;
        },
        render: function() {
            var self = this,
                o = this.options,
                pager = this.pager,
                item;

            this._width = this.element.innerWidth();
            this._height = this.element.innerHeight();

            // 探测单个图片的大小
            this._items.empty();
            item = this.renderItem(o.photos[0], 0);
            item.appendTo(this._items);
            this._itemWidth = item.outerWidth(true);
            this._itemHeight = item.outerHeight(true);
            item.remove();

            this._pagesize = Math.floor(this._width / this._itemWidth) * Math.floor((this._height - o.pagerHeight) / this._itemHeight);
            this._totalPage = Math.ceil(o.photos.length / this._pagesize);
            this._currentPage = Math.ceil(o.current / this._pagesize);

            this.pager = new Pager({
                element: this.find('pager'),
                total: this._totalPage,
                current: this._currentPage,
                center: true,
                afterShow: function(page) {
                    self.renderPage(page);
                }
            });

            this._rendered = true;
            return this;
        },
        renderItem: function(image, index) {
            var self = this,
                item = $('<li><a href="javascript:void(0);" hideFocus="true"></a></li>');
            item.attr('data-photolist-index', index);
            item.find('a').css('background', '#000 url(' + (image.small || image.thumb) + ') no-repeat center center').click(function() {
                self.select(index);
                return false;
            });
            return item;
        },
        renderPage: function(page) {
            var self = this,
                o = this.options,
                start = Math.max(0, (page - 1)) * this._pagesize,
                photos = o.photos.slice(start, start + this._pagesize),
                item;

            this._items.empty();
            $.each(photos, function(index, photo) {
                item = self.renderItem(photo, start + index).appendTo(self._items);
                index === self._current && self.select(index, true);
            });

            return this;
        },
        show: function(index) {
            var page;

            this.trigger('beforeShow', [index]);

            this.element.show();

            if (! this._rendered) {
                this.render();
            }

            if (index !== undefined) {
                index = parseInt(index);
                if (index !== this._current) {
                    this._current = index;
                    this._currentPage = Math.ceil((index + 1) / this._pagesize);
                    this.pager.jumpTo(this._currentPage);
                    this.select(index, true);
                }
            }

            this.trigger('afterShow', [index]);
            return this;
        },
        hide: function() {
            this.trigger('beforeHide');

            this.element.hide();

            this.trigger('afterHide');
            return this;
        },
        select: function(index, slient) {
            var item = this._items.find('[data-photolist-index=' + index + ']');
            if (! item.length) {
                return false;
            }

            ! slient && this.trigger('beforeSelect', [index, item]);

            item.siblings().removeClass(this.options.currentClass);
            item.addClass(this.options.currentClass);

            ! slient && this.trigger('afterSelect', [index, item]);
            return this;
        }
    });

    var Gallery = Base.extend({
        OPTIONS: {
            element: null,
            photos: [],
            preload: 1,
            autoslide: false,
            interval: 5000,
            keyboard: true,
            scrollIntoView: true,
            minHeight: 500,
            maxWidth: 0,
            hashPageParam: 'p',

            thumbWidth: 100,
            thumbHeight: 75,
            thumbCurrentClass: 'current',

            smallWidth: 100,
            smallHeight: 75,
            smallCurrentClass: 'current'
        },
        name: 'gallery',
        options: {},
        events: {},

        slider: null,
        photolist: null,

        _eventNames: 'prev next jumpTo show slide stop exit',
        _width: 0,
        _height: 0,

        _current: -1,
        _total: 0,

        _isPlaying: false,

        _hasPrev: false,
        _hasNext: false,

        _photo: null,
        _photoDescription: null,
        _photoPrev: null,
        _photoNext: null,
        _loading: null,
        _counterNow: null,
        _counterTotal: null,
        _content: null,
        _end: null,
        _autoplay: null,
        _autostop: null,
        _viewList: null,
        _viewOrigin: null,

        _hashParamRegexp: null,
        _playInterval: null,

        init: function(options) {
            var self = this;
            this.parent(options);

            if (! isArray(this.options.photos)) {
                this.options.photos = [];
            }

            options = this.options;
            this._width = this.element.width();
            this._total = options.photos.length;
            this._loading = this.find('loading').hide();
            this._counterNow = this.find('counter-now');
            this._counterTotal = this.find('counter-total');
            this._photo = this.find('photo-items').empty();
            this._photoDescription = this.find('photo-description');
            this._photoPrev = this.find('photo-prev');
            this._photoNext = this.find('photo-next');
            this._content = this.find('content');
            this._end = this.find('end').hide();
            this.find('slider').length && (this.slider = new Slider({
                element: this.find('slider'),
                photos: options.photos,
                photoWidth: options.thumbWidth,
                photoHeight: options.thumbHeight,
                currentClass: options.thumbCurrentClass
            }));
            this._total && this.find('photolist').length && (this.photolist = new PhotoList({
                element: this.find('photolist'),
                photos: options.photos,
                photoWidth: options.smallWidth || options.thumbWidth,
                photoHeight: options.smallHeight || options.thumbHeight,
                currentClass: options.smallCurrentClass || options.thumbCurrentClass,
                afterSelect: function(index) {
                    self.jumpTo(index);
                    self.photolist.hide();
                }
            }));
            this._autoplay = this.find('auto-play');
            this._autostop = this.find('auto-stop').hide();
            this._viewList = this.find('view-list');
            this._viewOrigin = this.find('view-origin');

            this.render();
            return this;
        },
        render: function() {
            var self = this,
                o = this.options,
                width = this._width,
                half = Math.floor(width / 2),
                photo = this._photo,
                prev = this._photoPrev.width(half),
                prevTrigger = prev.find('a').hide(),
                next = this._photoNext.width(half),
                nextTrigger = next.find('a').hide(),
                loading = this._loading,
                slider = this.slider;

            // 显示总数
            this._counterTotal.html(this._total);

            // 最小高度
            o.minHeight && (photo.css({
                'min-height': o.minHeight
            }), IE6 && photo.css({
                height: o.minHeight
            }));

            // 初始化 slider，双向绑定事件
            if (slider) {
                this.bind('afterShow', function(index) {
                    slider.show(index);
                });
                slider.bind('afterShow', function(index) {
                    self.jumpTo(index);
                });
                slider.element.bind('mousedown', function() {
                    if (self._isPlaying) {
                        self.stop();
                    }
                });
            }

            // 滚动视图到图片区域
            o.scrollIntoView && $('html, body').animate({
                'scrollTop': this.element.offset().top
            }, 500);

            // 加载图片时显示 loading 样式
            this.bind('beforeLoadImage', function() {
                loading.show();
            });
            this.bind('afterLoadImage', function() {
                loading.hide();
            });

            // 处理向前向后样式和逻辑
            this.bind('afterShow', function(index) {
                self._hasPrev = self._hasNext = true;
                if (index === 0) {
                    self._hasPrev = false;
                    prevTrigger.stop(false, true).fadeOut();
                }
                if (index === (this._total - 1)) {
                    self._hasNext = false;
                    nextTrigger.stop(false, true).fadeOut();
                }
            });
            prev.click(function() {
                if (self._isPlaying) {
                    self.stop();
                }
                self.prev();
                return false;
            });
            next.click(function() {
                if (self._isPlaying) {
                    self.stop();
                }
                self.next();
                return false;
            });
            prev.hover(function() {
                prevTrigger.stop(false, true);
                self._hasPrev && prevTrigger.is(':hidden') && prevTrigger.fadeIn();
            }, function() {
                prevTrigger.stop(false, true);
                prevTrigger.is(':visible') && prevTrigger.fadeOut();
            });
            next.hover(function() {
                nextTrigger.stop(false, true);
                self._hasNext && nextTrigger.is(':hidden') && nextTrigger.fadeIn();
            }, function() {
                nextTrigger.stop(false, true);
                nextTrigger.is(':visible') && nextTrigger.fadeOut();
            });

            // 同步更新附加信息
            this.bind('afterShow', function(index) {
                // 计数器
                self._counterNow.html(index + 1);
                // 查看原图
                self._viewOrigin.attr('href', self.options.photos[index].big);
            });

            // 处理结束后显示的推荐信息
            if (this._end.length) {
                this.bind('beforeShow', function(index) {
                    if (index === self._total) {
                        self._end.show();
                    } else {
                        self._end.hide();
                    }
                });
            }

            // 如果要显示的序号大于最后一张，并且处于自动播放状态，就停止播放
            this.bind('beforeShow', function(index) {
                if (index === self._total) {
                    self.stop();
                }
            });

            // 预加载图片
            o.preload && this.bind('afterShow', function(index) {
                // 向后预加载
                self.preload(o.photos.slice(index + 1, index + 1 + o.preload));
                // 向前预加载
                self.preload(o.photos.slice(Math.max(0, index - o.preload), index));
            });

            // 加载 location.hash 指定的图片，或第一张图片
            if (o.hashPageParam) {
                this._hashParamRegexp = new RegExp('\\b' + o.hashPageParam + '=(\\d+)\\b', 'i');
                // 更新 hash
                this.bind('afterShow', function(index) {
                    var hash = location.hash;
                    if (hash.match(self._hashParamRegexp)) {
                        location.hash = hash.replace(self._hashParamRegexp, function() {
                            return self.options.hashPageParam + '=' + (index + 1);
                        });
                    } else {
                        location.hash = (hash ? (hash + '&') : '') + self.options.hashPageParam + '=' + (index + 1);
                    }
                });
                this.jumpTo(Math.max(0, parseInt(((location.hash.match(this._hashParamRegexp) || [])[1] || 1)) - 1));
            } else {
                this.jumpTo(0);
            }

            // 绑定键盘事件
            o.keyboard && $(document).bind('keydown', function(ev) {
                // 组合键、输入域的事件不触发
                if (ev.ctrlKey || ev.shiftKey || ev.altKey || $(ev.originalTarget).is(':input')) {
                    return;
                }
                // 阻止上下键盘事件易导致可访问性问题，暂不启用
                switch (ev.keyCode) {
                    //case 38: // top
                    case 37: // left
                    //case 75: // k
                    case 72: // h
                        self.prev();
                        ev.preventDefault();
                        break;
                    case 39: // right
                    //case 40: // down
                    case 74: // j
                        //case 76: // l
                        self.next();
                        ev.preventDefault();
                        break;
                    default:
                        break;
                }
            });

            // 自动播放
            this._autoplay.click(function() {
                self.slide();
                return false;
            });
            this._autostop.click(function() {
                self.stop();
                return false;
            });
            this.bind('beforeSlide', function() {
                self._autoplay.hide();
                self._autostop.css('display', '');
            });
            this.bind('afterStop', function() {
                self._autoplay.css('display', '');
                self._autostop.hide();
            });

            // 查看全部图片
            this._viewList.click(function() {
                self.photolist && self.photolist.show(self._current);
            });

            return this;
        },
        renderPhoto: function(photo, index, width, height) {
            var o = this.options,
                img = $('<img />').attr('src', photo.big || photo).attr('data-gallery', index);

            // 防止图片过宽破坏页面布局
            if ((o.maxWidth && width > o.maxWidth) || width > this._width) {
                img.css('width', o.maxWidth ? Math.min(this._width, o.maxWidth) : this._width);
            }
            if ((o.minHeight && height < o.minHeight)) {
                img.css('margin-top', Math.floor((o.minHeight - height) / 2));
            }

            return img;
        },
        resizeTrigger: function(width, height) {
            var o = this.options,
                newWidth = width,
                newHeight = height;
            if ((o.maxWidth && width > o.maxWidth) || width > this._width) {
                newWidth = o.maxWidth ? Math.min(this._width, o.maxWidth) : this._width;
                newHeight = Math.ceil(height * (newWidth / width));
            }
            if (o.minHeight && newHeight < o.minHeight) {
                newHeight = o.minHeight;
            }
            this._photoPrev.height(newHeight);
            this._photoNext.height(newHeight);
        },
        preload: function() {
            var queue = [],
                loaded = [];
            return function(photos) {
                var self = this, photo;
                if (photos && isArray(photos)) {
                    while (photo = photos.shift()) {
                        loaded.indexOf(photo) === -1 && queue.push(photo);
                    }
                }
                queue.length && self.loadImage(queue.shift(), {
                    slient: true,
                    success: function(image) {
                        loaded.indexOf(image) === -1 && loaded.push(image);
                        self.preload();
                    },
                    error: function(image) {
                        loaded.indexOf(image) === -1 && loaded.push(image);
                        self.preload();
                    }
                });
            };
        }(),
        loadImage: function() {
            var queue = {};
            return function(photo, config) {
                if (! photo) {
                    return false;
                }

                var self = this,
                    src = photo.big || photo, img, complete;

                config = isPlainObject(config) ? config : {};

                if (queue[src]) {
                    if (queue[src].state === 'loading') {
                        config.success && queue[src].success.push(config.success);
                        config.error && queue[src].error.push(config.error);
                    } else if (queue[src].state === 'loaded') {
                        config.success && config.success.call(photo, photo, queue[src].width, queue[src].height);
                    } else {
                        config.error && config.error.call(photo, photo);
                    }
                    return;
                }

                queue[src] = {
                    state: 'loading',
                    success: [],
                    error: []
                };

                config.success && queue[src].success.push(config.success);
                config.error && queue[src].error.push(config.error);

                ! config.slient && this.trigger('beforeLoadImage', [photo]);

                complete = function() {
                    var func;
                    queue[src].state = 'loaded';
                    queue[src].error = [];
                    queue[src].width = this.width;
                    queue[src].height = this.height;
                    ! config.slient && self.trigger('afterLoadImage', [photo, this.width, this.height]);
                    while (func = queue[src].success.shift()) {
                        if (func.call(this, photo, queue[src].width, queue[src].height) === false) {
                            break;
                        }
                    }
                    img = img.onload = img.onerror = null;
                };

                img = new Image();
                img.src = src;
                if (img.complete) {
                    complete.call(img);
                    return;
                }
                img.onerror = function() {
                    var func;
                    queue[src].state = 'error';
                    queue[src].success = [];
                    ! config.slient && self.trigger('afterLoadImage', [photo]);
                    while (func = queue[src].error.shift()) {
                        if (func.call(this, photo) === false) {
                            break;
                        }
                    }
                    img = img.onload = img.onerror = null;
                }
                img.onload = complete;
            };
        }(),
        prev: function() {
            this.show(this._current - 1, 'prev');
            return this;
        },
        next: function() {
            this.show(this._current + 1, 'next');
            return this;
        },
        jumpTo: function(index) {
            this.show(index, 'jumpTo');
        },
        show: function(index, event) {
            var self = this,
                current = this._current,
                photo = this._photo,
                photoToShow;

            if (index === current) {
                return false;
            }

            event = ucfirst(event);
            this.trigger('beforeShow', [index, 'before' + event]);

            // 序号合法判断，放在这里是为了能够监听到 beforeShow 事件的不规范序号，
            // 如监听第一张之前或最后一张之后的事件
            if (index < 0 || index > (this._total - 1)) {
                return false;
            }

            this.trigger('before' + event, [index]);

            photoToShow = this.find(index, photo);
            function callback(width, height) {
                self.find(current, photo).hide();
                photoToShow.css('display', '');
                self._photoDescription.html(self.options.photos[index].note || '');
                self.resizeTrigger(width, height);
                self._current = index;
                self.trigger('after' + event, [index]);
                self.trigger('afterShow', [index, 'after' + event]);
            }
            // 已加载过
            if (photoToShow.length) {
                callback(photoToShow.width(), photoToShow.height());
            } else {
                this.loadImage(this.options.photos[index], {
                    success: function(image, width, height) {
                        // 如果队列中的图片被绑定了多个回调，有可能该图片已经被插入到 DOM 中
                        if (! self.find(index, photo).length) {
                            photoToShow = self.renderPhoto(image, index, width, height).hide().appendTo(photo);
                        }
                        callback(width, height);
                    }
                });
            }
            return this;
        },

        showList: function() {

        },
        hideList: function() {

        },

        slide: function() {
            if (this._isPlaying || ! this._total) {
                return false;
            }

            var self = this;
            this._isPlaying = true;
            this.trigger('beforeSlide', [this._current]);

            this._playInterval = setInterval(function() {
                self.next();
            }, 3000);

            this.trigger('afterSlide');
            return this;
        },
        stop: function() {
            if (! this._isPlaying) {
                return false;
            }

            this.trigger('beforeStop', [this._current, this._playInterval]);

            this._playInterval && clearTimeout(this._playInterval);
            this._isPlaying = false;

            this.trigger('afterStop', [this._current]);
            return this;
        }
    });

    root.Slider = Slider;
    root.Pager = Pager;
    root.PhotoList = PhotoList;
    root.Gallery = Gallery;

    $.fn.gallery = function(elem, options) {
        return this.each(function() {
            options = isPlainObject(options) || {};
            options.element = elem;
            $(this).data('gallery', new Gallery(options));
        });
    };

})(jQuery, window['GALLERY_NAMESPACE'] || window);
