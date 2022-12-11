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
                        $(":button.close", dialog).click(function() {
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
            var op = $.extend({}, $.pdialog._op, options);
            var dialog = $("body").data(dlgid);
            // 重复打开一个层
            if (dialog && !op.focusNewWindow) {
                if (dialog.is(":hidden") ) {
                    dialog.show();
                }
                if (op.max ) {
                    $.pdialog.maxsize(dialog);
                    dialog.jresize("destroy").dialogDrag("destroy");
                }
                if (op.fresh || url != dialog.data("url") ) {
                    dialog.data("url", url);
                    dialog.find(".dialogHeader").find("h1").text(title);
                    this.switchDialog(dialog);
                    var jDContent = dialog.find(".dialogContent");
                    jDContent.loadUrl(url, {}, function() {
                        jDContent.find("[layoutH]").layoutH(jDContent);
                        $("button.close").click(function() {
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
                dialog = $(dialog);
                dialog.data("id", dlgid);
                dialog.data("url", url);
                if (options.close ) {
                    dialog.data("close", options.close);
                }
                if (options.param ) {
                    dialog.data("param", options.param);
                }
                dialog.find(".dialogHeader").find("h1").text(title);
                dialog.css("zIndex", ( $.pdialog._zIndex += 2 ));
                $.pdialog._init(dialog, options);
                dialog.click(function() {
                    $.pdialog.switchDialog(dialog);
                });
                if (op.resizable ) {
                    dialog.jresize();
                }
                if (op.drawable ) {
                    dialog.dialogDrag();
                }
                $("a.close", dialog).click(function(event) {
                    $.pdialog.close(dialog);
                    return false;
                });
                if (op.maxable ) {
                    $("a.maximize", dialog).show().click(function(event) {
                        $.pdialog.switchDialog(dialog);
                        $.pdialog.maxsize(dialog);
                        dialog.jresize("destroy").dialogDrag("destroy");
                        return false;
                    });
                } else {
                    $("a.maximize", dialog).hide();
                }
                $("a.restore", dialog).click(function(event) {
                    $.pdialog.restore(dialog);
                    dialog.jresize().dialogDrag();
                    return false;
                });
                if (op.minable ) {
                    $("a.minimize", dialog).show().click(function(event) {
                        $.pdialog.minimize(dialog);
                        return false;
                    });
                } else {
                    $("a.minimize", dialog).hide();
                }
                $("div.dialogHeader a", dialog).mousedown(function() {
                    return false;
                });
                $("div.dialogHeader", dialog).dblclick(function() {
                    if ($("a.restore", dialog).is(":hidden") ) {
                        $("a.maximize", dialog).trigger("click");
                    } else {
                        $("a.restore", dialog).trigger("click");
                    }
                });
                if (op.max ) {
                    $.pdialog.maxsize(dialog);
                    dialog.jresize("destroy").dialogDrag("destroy");
                }
                $("body").data(dlgid, dialog);
                $.pdialog._current = dialog;
                // load data
                var jDContent = $(".dialogContent", dialog);
                jDContent.loadUrl(url, {}, function() {
                    jDContent.find("[layoutH]").layoutH(jDContent);
                    $("button.close").click(function() {
                        $.pdialog.close(dialog);
                        return false;
                    });
                });
            }
            if (op.mask ) {
                dialog.css("zIndex", 1000);
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
            if ($.pdialog._current ) {
                var cindex = $($.pdialog._current).css("zIndex");
                $.pdialog._current.css("zIndex", index);
                dialog.css("zIndex", cindex);
                $.pdialog._current = dialog;
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
         * 改变左右拖动层的高度
         *
         * @param {Object}
         *            target
         * @param {Object}
         *            tmove
         * @param {Object}
         *            dialog
         */
        resizeTool: function(target, tmove, dialog) {
            $("div[class^='resizable']", dialog).filter(function() {
                return $(this).attr("tar") == 'w' || $(this).attr("tar") == 'e';
            }).each(function() {
                $(this).css("height", $(this).outerHeight() + tmove);
            });
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
                $.pdialog.resizeTool(target, tmove, dialog);
            }
            $(window).trigger(JUI.eventType.resizeGrid);
        },
        close: function(dialog) {
            if (typeof dialog == 'string' ) {
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
        },
        closeCurrent: function() {
            this.close($.pdialog._current);
        },
        checkTimeout: function() {
            var $conetnt = $(".dialogContent", $.pdialog._current);
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
            var iContentH = $(window).height() - $('footer').height();
            dialog.css({
                top: "0px", left: "0px", width: iContentW + "px", height: iContentH + "px"
            });
            $.pdialog._resizeContent(dialog,iContentW,iContentH);
        },
        restore: function(dialog) {
            var original = dialog.data("original");
            var dwidth = parseInt(original.width);
            var dheight = parseInt(original.height);
            dialog.css({
                top: original.top, left: original.left, width: dwidth, height: dheight
            });
            $.pdialog._resizeContent(dialog,dwidth,dheight);
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
        }
    };
} )(jQuery);
/**
 * @author Roger Wu
 */
( function($) {
    $.fn.dialogDrag = function(options) {
        if (typeof options == 'string' ) {
            if (options == 'destroy' ) {
                return this.each(function() {
                    var dialog = this;
                    $("div.dialogHeader", dialog).off("mousedown");
                });
            }
        }
        return this.each(function() {
            var dialog = $(this);
            $("div.dialogHeader", dialog).mousedown(function(e) {
                $.pdialog.switchDialog(dialog);
                dialog.data("task", true);
                setTimeout(function() {
                    if (dialog.data("task") ) {
                        $.dialogDrag.start(dialog, e);
                    }
                }, 100);
                return false;
            }).mouseup(function(e) {
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