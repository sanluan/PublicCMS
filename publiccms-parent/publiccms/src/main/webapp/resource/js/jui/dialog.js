/**
 * @author Roger Wu reference:drag.js, dialogDrag.js, resize.js,
 *         taskBar.js
 */
( function($) {
    $.pdialog = {
        _op: {
            height: 500, width: 850, minH: 40, minW: 50, total: 20, max: false, mask: false, resizable: true, drawable: true, maxable: true, minable: true ,focusNewWindow: false,
            fresh: true
        } ,
        _current: null ,
        _zIndex: 42 ,
        getCurrent: function() {
            return this._current;
        } ,
        reload: function(url, options) {
            var op = $.extend({
                data: {}, dialogId: "", callback: null
            }, options);
            var dialog = ( op.dialogId && $("body").data(op.dialogId) ) || this._current;
            if (dialog ) {
                var jDContent = dialog.find(".dialogContent");
                jDContent.ajaxUrl({
                    type: "POST", url: url, data: op.data, callback: function(response) {
                        jDContent.find("[layoutH]").layoutH(jDContent);
                        $(":button.close", dialog).on("click", function() {
                            $.pdialog.close(dialog);
                            return false;
                        });
                        if ("function" === typeof op.callback ) {
                            op.callback(response);
                        }
                    }
                });
            }
        } ,
        // 打开一个层
        open: function(url, dlgid, title, options) {
            var op = $.extend({}, this._op, options);
            var dialog = $("body").data(dlgid);
            // 重复打开一个层
            if (dialog && !op.focusNewWindow) {
                dialog.find(".dialogHeader").find("h1").text(title);
                if (dialog.is(":hidden") ) {
                    dialog.show();
                }
                if (op.max ) {
                    this.maxsize(dialog);
                    dialog.jresize("destroy").dialogDrag("destroy");
                }
                if (op.fresh || url != dialog.data("url") ) {
                    dialog.data("url", url);
                    this.switchDialog(dialog);
                    var jDContent = dialog.find(".dialogContent");
                    jDContent.loadUrl(url, {}, function() {
                        jDContent.find("[layoutH]").layoutH(jDContent);
                        $("button.close").on("click", function() {
                            $.pdialog.close(dialog);
                            return false;
                        });
                    });
                }
            } else { // 打开一个全新的层
                if(op.focusNewWindow){
                  dlgid += Math.round(Math.random() * 10000000);
                }
                dialog = $($.parseHTML(JUI.frag["dialogFrag"], document, true)).appendTo($("body"));
                dialog.data("id", dlgid);
                dialog.data("url", url);
                if (options.close ) {
                    dialog.data("close", options.close);
                }
                if (options.param ) {
                    dialog.data("param", options.param);
                }
                dialog.find(".dialogHeader").find("h1").text(title);
                dialog.css("zIndex", ( this._zIndex += 2 ));
                this._init(dialog, options);
                dialog.on("click", function() {
                    $.pdialog.switchDialog(dialog);
                });
                if (op.resizable ) {
                    dialog.jresize();
                }
                if (op.drawable ) {
                    dialog.dialogDrag();
                }
                $("a.close", dialog).on("click", function() {
                    $.pdialog.close(dialog);
                    return false;
                });
                if (op.maxable ) {
                    $("a.maximize", dialog).show().on("click", function() {
                        $.pdialog.switchDialog(dialog);
                        $.pdialog.maxsize(dialog);
                        dialog.jresize("destroy").dialogDrag("destroy");
                        return false;
                    });
                } else {
                    $("a.maximize", dialog).hide();
                }
                $("a.restore", dialog).on("click", function() {
                    $.pdialog.restore(dialog);
                    dialog.jresize().dialogDrag();
                    return false;
                });
                if (op.minable ) {
                    $("a.minimize", dialog).show().on("click", function() {
                        $.pdialog.minimize(dialog);
                        return false;
                    });
                } else {
                    $("a.minimize", dialog).hide();
                }
                $("div.dialogHeader a", dialog).on("mousedown", function() {
                    return false;
                });
                $("div.dialogHeader", dialog).on( "dblclick", function() {
                    if ($("a.restore", dialog).is(":hidden") ) {
                        $("a.maximize", dialog).trigger("click");
                    } else {
                        $("a.restore", dialog).trigger("click");
                    }
                });
                if (op.max ) {
                    this.maxsize(dialog);
                    dialog.jresize("destroy").dialogDrag("destroy");
                }
                $("body").data(dlgid, dialog);
                this._current = dialog;
                // load data
                var jDContent = $(".dialogContent", dialog);
                jDContent.loadUrl(url, {}, function() {
                    jDContent.find("[layoutH]").layoutH(jDContent);
                    $("button.close").on("click", function() {
                        $.pdialog.close(dialog);
                        return false;
                    });
                });
            }
            if (op.mask ) {
                $("a.minimize", dialog).hide();
                dialog.data("mask", true);
                $("#dialogBackground").show();
            } else {
                // add a task to task bar
                if (op.minable ){
                    $.taskBar.addDialog(dlgid, title);
                }
            }
        } ,
        /**
         * 切换当前层
         *
         * @param {Object}
         *            dialog
         */
        switchDialog: function(dialog) {
            var index = dialog.css("zIndex");
            if (this._current ) {
                var cindex = this._current.css("zIndex");
                this._current.css("zIndex", index);
                dialog.css("zIndex", cindex);
                this._current = dialog;
            }
            $.taskBar.switchTask(dialog.data("id"));
        } ,
        _init: function(dialog, options) {
            var op = $.extend({}, this._op, options);
            var height = op.height > op.minH ? op.height < $(document).height() ? op.height: $(document).height() : op.minH;
            var width = op.width > op.minW ? op.width < $(document).width() ? op.width: $(document).width() : op.minW;
            if (isNaN(dialog.height()) || dialog.height() < height ) {
                dialog.height(height);
                $(".dialogContent", dialog).height(height - $(".dialogHeader", dialog).outerHeight());
            }
            if (isNaN(dialog.css("width")) || dialog.width() < width ) {
                dialog.width(width);
            }
            var iTop = ( $(window).height() - dialog.height() ) / 2;
            dialog.css({
                left: ( $(window).width() - dialog.width() ) / 2, top: iTop > 0 ? iTop: 0
            });
        } ,
        /**
         * 初始化半透明层
         *
         * @param {Object}
         *            resizable
         * @param {Object}
         *            dialog
         * @param {Object}
         *            target
         */
        initResize: function(resizable, dialog, target) {
            $("body").css("cursor", target + "-resize");
            resizable.css({
                top: dialog.css("top"), left: dialog.css("left"), height: dialog.css("height"), width: dialog.css("width")
            });
            resizable.show();
        } ,
        /**
         * 改变原始层的大小
         *
         * @param {Object}
         *            obj
         * @param {Object}
         *            dialog
         * @param {Object}
         *            target
         */
        resizeDialog: function(obj, dialog, target) {
            var oleft = parseInt(obj.style.left);
            var otop = parseInt(obj.style.top);
            var height = parseInt(obj.style.height);
            var width = parseInt(obj.style.width);
            if (target == "n" || target == "nw" ) {
                tmove = parseInt(dialog.css("top")) - otop;
            } else {
                tmove = height - parseInt(dialog.css("height"));
            }
            dialog.css({
                left: oleft, width: width, top: otop, height: height
            });
            $(".dialogContent", dialog).css("width", (width -10) + "px");
            if (target != "w" && target != "e" ) {
                var content = $(".dialogContent", dialog);
                content.css({
                    height: height - $(".dialogHeader", dialog).outerHeight()
                });
                content.find("[layoutH]").layoutH(content);
            }
            $(window).trigger(JUI.eventType.resizeGrid);
            $(window).trigger(JUI.eventType.resizeChart);
        },
        close: function(dialog) {
            if (typeof dialog == "string" ) {
                dialog = $("body").data(dialog);
            }
            var close = dialog.data("close");
            var go = true;
            if (close && "function" === typeof close ) {
                var param = dialog.data("param");
                if (param && param != "" ) {
                    param = JUI.jsonEval(param);
                    go = close(param);
                } else {
                    go = close();
                }
                if (!go ) {
                    return;
                }
            }
            dialog.hide();
            if (dialog.data("mask") ) {
                $("#dialogBackground").hide();
            } else {
                if (dialog.data("id") ) {
                    $.taskBar.closeDialog(dialog.data("id"));
                }
            }
            $("body").removeData(dialog.data("id"));
            dialog.trigger(JUI.eventType.pageClear).remove();
            this._current = null;
            var maxIndex=0;
            $("div[class=dialog]").each(function(){
                var $this=$(this);
                if(parseInt($this.css("zIndex")) > maxIndex){
                    maxIndex=parseInt($this.css("zIndex"));
                    $.pdialog._current=$this;
                }
            });
        },
        closeCurrent: function() {
            this.close(this._current);
        },
        checkTimeout: function() {
            var $conetnt = $(".dialogContent", this._current);
            var json = JUI.jsonEval($conetnt.html());
            if (json && json[JUI.keys.statusCode] == JUI.statusCode.timeout ) {
                this.closeCurrent();
            }
        },
        maxsize: function(dialog) {
            dialog.data("original", {
                top: dialog.css("top"), left: dialog.css("left"), width: dialog.css("width"), height: dialog.css("height")
            });
            $("a.maximize", dialog).hide();
            $("a.restore", dialog).show();
            var iContentW = $(window).width();
            var iContentH = $(window).height() - $("footer").height();
            dialog.css({
                top: "0px", left: "0px", width: iContentW + "px", height: iContentH + "px"
            });
            this._resizeContent(dialog,iContentW,iContentH);
        },
        restore: function(dialog) {
            var original = dialog.data("original");
            var dwidth = parseInt(original.width);
            var dheight = parseInt(original.height);
            dialog.css({
                top: original.top, left: original.left, width: dwidth, height: dheight
            });
            this._resizeContent(dialog,dwidth,dheight);
            $("a.maximize", dialog).show();
            $("a.restore", dialog).hide();
        },
        minimize: function(dialog) {
            dialog.hide();
            var task = $.taskBar.getTask(dialog.data("id"));
            $(".resizable").css({
                top: dialog.css("top"), left: dialog.css("left"), height: dialog.css("height"), width: dialog.css("width")
            }).show().animate({
                top: $(window).height() - 60, left: task.position().left, width: task.outerWidth(), height: task.outerHeight()
            }, 250, function() {
                $(this).hide();
                $.taskBar.inactive(dialog.data("id"));
            });
        },
        _resizeContent: function(dialog,width,height) {
            var content = $(".dialogContent", dialog);
            content.css({
                height: height - $(".dialogHeader", dialog).outerHeight()
            });
            content.css("width", (width -10) + "px");
            content.find("[layoutH]").layoutH(content);
            $(window).trigger(JUI.eventType.resizeGrid);
            $(window).trigger(JUI.eventType.resizeChart);
        }
    };
} )(jQuery);
/**
 * @author Roger Wu
 */
( function($) {
    $.fn.dialogDrag = function(options) {
        if (typeof options == "string" ) {
            if (options == "destroy" ) {
                return this.each(function() {
                    var dialog = this;
                    $("div.dialogHeader", dialog).off("mousedown");
                });
            }
        }
        return this.each(function() {
            var dialog = $(this);
            $("div.dialogHeader", dialog).on("mousedown", function(e) {
                $.pdialog.switchDialog(dialog);
                dialog.data("task", true);
                setTimeout(function() {
                    if (dialog.data("task") ) {
                        $.dialogDrag.start(dialog, e);
                    }
                }, 100);
                return false;
            }).on("mouseup", function(e) {
                dialog.data("task", false);
                return false;
            });
        });
    };
    $.dialogDrag = {
        currId: null, _init: function(dialog) {
            this.currId = new Date().getTime();
            var shadow = $("#dialogProxy");
            if (!shadow.length ) {
                shadow = $(JUI.frag["dialogProxy"]);
                $("body").append(shadow);
            }
            $("h1", shadow).text($(".dialogHeader h1", dialog).text());
        },
        start: function(dialog, event) {
            this._init(dialog);
            var sh = $("#dialogProxy");
            sh.css({
                left: dialog.css("left"), top: dialog.css("top"), height: dialog.css("height"), width: dialog.css("width"), zIndex: parseInt(dialog.css("zIndex")) + 1
            }).show();
            $("div.dialogContent", sh).css("height", $("div.dialogContent", dialog).css("height"));
            sh.data("dialog", dialog);
            dialog.css({
                left: "-10000px", top: "-10000px"
            });
            $(sh).jDrag({
                selector: ".dialogHeader", stop: this.stop, event: event
            });
            return false;
        },
        stop: function() {
            var sh = $(arguments[0]);
            var dialog = sh.data("dialog");
            dialog.css({
                left: $(sh).css("left"), top: $(sh).css("top")
            });
            $(sh).hide();
        }
    }
} )(jQuery);

/**
 * @author Roger Wu
 * @version 1.0
 */
( function($) {
    $.fn.extend({
        jresize: function(options) {
            if (typeof options == "string" ) {
                if (options == "destroy" ) {
                    return this.each(function() {
                        var dialog = this;
                        $("div[class^=\"resizable\"]", dialog).each(function() {
                            $(this).hide();
                        });
                    });
                }
            }
            return this.each(function() {
                var dialog = $(this);
                var resizable = $(".resizable");
                $("div[class^=\"resizable\"]", dialog).each(function() {
                    var bar = this;
                    $(bar).on("mousedown", function(event) {
                        $.pdialog.switchDialog(dialog);
                        $.resizeTool.start(resizable, dialog, event, $(bar).attr("tar"));
                        return false;
                    }).show();
                });
            });
        }
    });
    $.resizeTool = {
        start: function(resizable, dialog, e, target) {
            $.pdialog.initResize(resizable, dialog, target);
            $.data(resizable[0], "layer-drag", {
                options: $.extend($.pdialog._op, {
                    target: target, dialog: dialog, stop: $.resizeTool.stop
                })
            });
            $.layerdrag.start(resizable[0], e, $.pdialog._op);
        }, stop: function() {
            var data = $.data(arguments[0], "layer-drag");
            $.pdialog.resizeDialog(arguments[0], data.options.dialog, data.options.target);
            $("body").css("cursor", "");
            $(arguments[0]).hide();
        }
    };
    $.layerdrag = {
        start: function(obj, e, options) {
            if (!$.layerdrag.current ) {
                $.layerdrag.current = {
                    el: obj, oleft: parseInt(obj.style.left) || 0, owidth: parseInt(obj.style.width) || 0, otop: parseInt(obj.style.top) || 0 ,
                    oheight: parseInt(obj.style.height) || 0, ox: e.pageX || e.screenX, oy: e.pageY || e.clientY
                };
                $(document).on("mouseup", null, null, $.layerdrag.stop);
                $(document).on("mousemove", null, null, $.layerdrag.drag);
            }
            return $.layerdrag.preventEvent(e);
        }, drag: function(e) {
            if (!e ) {
                var e = window.event;
            }
            var current = $.layerdrag.current;
            var data = $.data(current.el, "layer-drag");
            var lmove = ( e.pageX || e.screenX ) - current.ox;
            var tmove = ( e.pageY || e.clientY ) - current.oy;
            if ( ( e.pageY || e.clientY ) <= 0 || ( e.pageY || e.clientY ) >= ( $(window).height() - $(".dialogHeader", $(data.options.dialog)).outerHeight() ) ) {
                return false;
            }
            var target = data.options.target;
            var width = current.owidth;
            var height = current.oheight;
            if (target != "n" && target != "s" ) {
                width += ( target.indexOf("w") >= 0 ) ? -lmove: lmove;
            }
            if (width >= $.pdialog._op.minW ) {
                if (target.indexOf("w") >= 0 ) {
                    current.el.style.left = ( current.oleft + lmove ) + "px";
                }
                if (target != "n" && target != "s" ) {
                    current.el.style.width = width + "px";
                }
            }
            if (target != "w" && target != "e" ) {
                height += ( target.indexOf("n") >= 0 ) ? -tmove: tmove;
            }
            if (height >= $.pdialog._op.minH ) {
                if (target.indexOf("n") >= 0 ) {
                    current.el.style.top = ( current.otop + tmove ) + "px";
                }
                if (target != "w" && target != "e" ) {
                    current.el.style.height = height + "px";
                }
            }
            return $.layerdrag.preventEvent(e);
        }, stop: function(e) {
            var current = $.layerdrag.current;
            var data = $.data(current.el, "layer-drag");
            $(document).off("mousemove", null, $.layerdrag.drag);
            $(document).off("mouseup", null, $.layerdrag.stop);
            if (data.options.stop ) {
                data.options.stop.apply(current.el, [ current.el ]);
            }
            $.layerdrag.current = null;
            return $.layerdrag.preventEvent(e);
        }, preventEvent: function(e) {
            if (e.stopPropagation ) {
                e.stopPropagation();
            }
            if (e.preventDefault ) {
                e.preventDefault();
            }
            return false;
        }
    };
} )(jQuery);