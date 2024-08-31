/**
 * @author ZhangHuihua@msn.com
 */
var JUI = {
    version: "1.7.0" ,
    regPlugins: [ ], // [function($parent){} ...]
    // sbar: show sidebar
    keyCode: {
        ENTER: 13, ESC: 27, END: 35, HOME: 36, SHIFT: 16, TAB: 9, LEFT: 37, RIGHT: 39, UP: 38, DOWN: 40, DELETE: 46, BACKSPACE: 8, CHAR_S: 83
    } ,
    eventType: {
        pageClear: "pageClear", // 用于重新ajaxLoad、关闭nabTab, 关闭dialog时，去除xheditor等需要特殊处理的资源
        editorSync: "editorSync",
        resizeGrid: "resizeGrid", // 用于窗口或dialog大小调整
        resizeChart: "resizeChart", // 用于报表大小调整
        initEnvAfter: "initEnvAfter" // initEnv完成触发
    } ,
    isOverAxis: function(x, reference, size) {
        // Determines when x coordinate is over "b" element axis
        return (x > reference ) && (x < (reference + size ) );
    } ,
    isOver: function(y, x, top, left, height, width) {
        // Determines when x, y coordinates is over "b" element
        return this.isOverAxis(y, top, height) && this.isOverAxis(x, left, width);
    } ,
    pageInfo: {
        pageNum: "pageNum", numPerPage: "numPerPage", orderField: "orderField", orderDirection: "orderDirection"
    } ,
    statusCode: {
        ok: 200, okAndRefresh: 201, error: 300, timeout: 301
    } ,
    keys: {
        statusCode: "statusCode", message: "message"
    } ,
    frag: {}, // page fragment
    _msg: {}, // alert message
    _set: {
        loginUrl: "", // session timeout
        loginTitle: "", // if loginTitle open a login dialog
        debug: false
    } ,
    msg: function(key, args) {
        var _format = function(str, args) {
            args = args || [ ];
            var result = str || "";
            for (var i = 0; i < args.length; i++) {
                result = result.replace(new RegExp("\\{" + i + "\\}", "g"), args[i]);
            }
            return result;
        }
        return _format(this._msg[key], args);
    } ,
    debug: function(msg) {
        if (this._set.debug ) {
            if (typeof (console ) != "undefined" ) {
                console.log(msg);
            } else {
                alert(msg);
            }
        }
    } ,
    loadLogin: function() {
        if ($.pdialog && JUI._set.loginTitle ) {
            $.pdialog.open(JUI._set.loginUrl, "login", JUI._set.loginTitle, {
                mask: true, width: 520, height: 260
            });
        } else {
            window.location = JUI._set.loginUrl;
        }
    } ,
    instances : {},
    /*
     * json to string
     */
    obj2str: function(o) {
        var r = [ ];
        if (typeof o == "string" ) {
            return "\"" + o.replace(/([\'\"\\])/g, "\\$1").replace(/(\n)/g, "\\n").replace(/(\r)/g, "\\r").replace(/(\t)/g, "\\t") + "\"";
        }
        if (typeof o == "object" ) {
            if (!o.sort ) {
                for (var i in o)
                    r.push("\""+i + "\":" + JUI.obj2str(o[i]));
                if (!!document.all && !/^\n?function\s*toString\(\)\s*\{\n?\s*\[native code\]\n?\s*\}\n?\s*$/.test(o.toString) ) {
                    r.push("toString:" + o.toString.toString());
                }
                r = "{" + r.join() + "}"
            } else {
                for (var i = 0; i < o.length; i++) {
                    r.push(JUI.obj2str(o[i]));
                }
                r = "[" + r.join() + "]"
            }
            return r;
        }
        return o.toString();
    } ,
    jsonEval: function(data) {
        try {
            if (typeof data == "string" ) {
                return eval("(" + data + ")");
            } else {
                return data;
            }
        } catch (e) {
            return {};
        }
    } ,
    ajaxError: function(xhr, ajaxOptions, thrownError) {
        if("undefined" == typeof thrownError||"" ==thrownError){
            var exception = $($.parseHTML(xhr.responseText, document, true)).find("#divexception textarea");
            if(exception.length){
                thrownError=exception.val();
            }else if(0===xhr.status){
                thrownError = JUI.msg("networkError");
            }
        }
        if (alertMsg ) {
            alertMsg.error("<div>Http status: " + xhr.status + " " + xhr.statusText + "</div>" + "<div>ajaxOptions: " + ajaxOptions + "</div>" + "<div>thrownError: " + thrownError + "</div>");
        } else {
            alert("Http status: " + xhr.status + " " + xhr.statusText + "\najaxOptions: " + ajaxOptions + "\nthrownError:" + thrownError);
        }
    },
    ajaxDone: function(json) {
        if (json[JUI.keys.statusCode] == JUI.statusCode.error ) {
            if (json[JUI.keys.message] && alertMsg ) {
                alertMsg.error(json[JUI.keys.message]);
            }
            if (json["fields"]){
                $.each(json["fields"].split(","),function(index,field){
                    $("[name="+$.escapeSelector(field)+"]", ( !$.pdialog.getCurrent() ) ? navTab.getCurrentPanel(): $.pdialog.getCurrent()).addClass("error");
                })
            }
        } else if (json[JUI.keys.statusCode] == JUI.statusCode.timeout ) {
            if (alertMsg ){
                alertMsg.error(json[JUI.keys.message] || JUI.msg("sessionTimout"), {
                    okCall: JUI.loadLogin
                });
            } else {
                JUI.loadLogin();
            }
        } else if (json[JUI.keys.statusCode] == JUI.statusCode.okAndRefresh ){
            if (json[JUI.keys.message] && alertMsg ){
                alertMsg.correct(json[JUI.keys.message],{
                    callback:function(){
                        window.location.reload();
                    }
                });
            }
        } else {
            if (json[JUI.keys.message] && alertMsg ){
                alertMsg.correct(json[JUI.keys.message]);
            }
        }
    },
    init: function(pageFrag, options) {
        var op = $.extend({
            loginUrl: "login.html", loginTitle: null, callback: null, debug: false, statusCode: {}
        }, options);
        this._set.loginUrl = op.loginUrl;
        this._set.loginTitle = op.loginTitle;
        this._set.debug = op.debug;
        $.extend(JUI.statusCode, op.statusCode);
        $.extend(JUI.keys, op.keys);
        $.extend(JUI.pageInfo, op.pageInfo);
        $.ajax({
            type: "GET", url: pageFrag, dataType: "html", cache: false, error: function(xhr) {
                alert(xhr.statusText);
            }, success: function(html) {
                $($.parseHTML(html, document, true)).each(function() {
                    var fragId = $(this).attr("id");
                    if (fragId ) {
                        JUI.frag[fragId] = $(this).text();
                    }
                });
                if ("function" === typeof op.callback ) {
                    op.callback();
                }
            }
        });
        if (!$(window).isBind(JUI.eventType.resizeChart) ) {
            $(window).on(JUI.eventType.resizeChart, null, null, function() {
                $(".chart").each(function() {
                    $chart=$(this);
                    if($chart.data("id") && window[$chart.data("id")] && "function" == typeof window[$chart.data("id")].resize){
                        window[$chart.data("id")].resize();
                    }
                });
            });
        }
        var _doc = $(document);
        if (!_doc.isBind(JUI.eventType.editorSync) ) {
            _doc.on(JUI.eventType.editorSync, null, null, function(event) {
                var box = event.target;
                var $box = $(box);
                $("textarea.editor", $box).each(function() {
                    if("ckeditor"==$(this).attr("editorType")) {
                        if(CKEDITOR.instances[$(this).data("id")]) {
                            CKEDITOR.instances[$(this).data("id")].updateElement();
                        }
                    } else if ("tinymce"==$(this).attr("editorType")){
                        if(tinymce.get($(this).data("id"))) {
                            tinymce.get($(this).data("id")).save();
                        }
                    } else {
                        if(UE.instants[$(this).data("id")]) {
                            UE.instants[$(this).data("id")].sync();
                        }
                    }
                });
                $("textarea.code", $box).each(function() {
                    if(JUI.instances[$(this).data("id")]) {
                        JUI.instances[$(this).data("id")].save();
                    }
                });

                $(".miscSortDrag", $box).each(function() {
                    var $sortBox=$(this);
                    if($sortBox.data("result")){
                        $sortBox.find($sortBox.data("result")).val(JUI.obj2str($sortBox.miscSortDragData($sortBox)));
                    }
                });
            });
        }
        if (!_doc.isBind(JUI.eventType.pageClear) ) {
            _doc.on(JUI.eventType.pageClear, null, null, function(event) {
                var box = event.target;
                var $box = $(box);
                $("textarea.editor", $box).each(function() {
                    if("ckeditor"==$(this).attr("editorType")) {
                        if(CKEDITOR.instances[$(this).data("id")]) {
                            CKEDITOR.instances[$(this).data("id")].destroy();
                        }
                    } else if("tinymce"==$(this).attr("editorType")) {
                        tinymce.remove("#"+$(this).data("id"));
                    } else {
                        if(UE.instants[$(this).data("id")]) {
                            UE.instants[$(this).data("id")].destroy();
                        }
                    }
                });
                $("textarea.code", $box).each(function() {
                    if(JUI.instances[$(this).data("id")]) {
                        JUI.instances[$(this).data("id")].toTextArea();
                        delete JUI.instances[$(this).data("id")];
                    }
                });
                $(".image-editor", $box).each(function() {
                    if(JUI.instances[$(this).data("id")]) {
                        delete JUI.instances[$(this).data("id")];
                    }
                });
                $("[close-url]",$box).each(function (){
                    $.getJSON($(this).attr("close-url"), function(data) {});
                });
            });
        }
    }
};

( function($) {
    // JUI set regional
    $.setRegional = function(key, value) {
        if (!$.regional ) {
            $.regional = {};
        }
        $.regional[key] = value;
    };

    // JUI set msg
    $.setMessage = function(key, value) {
        JUI._msg[key] = value;
    };

    $.fn.extend({
        /**
         * @param {Object}
         *            op: {type:GET/POST, url:ajax请求地址, data:ajax请求参数列表,
         *            callback:回调函数 }
         */
        ajaxUrl: function(op) {
            var $this = $(this);
            $this.trigger(JUI.eventType.pageClear);
            $.ajax({
                type: op.type || "GET", url: op.url, data: op.data, cache: false, success: function(response) {
                    var json = JUI.jsonEval(response);
                    if (json[JUI.keys.statusCode] == JUI.statusCode.error ) {
                        if (json[JUI.keys.message] ) {
                            alertMsg.error(json[JUI.keys.message]);
                        }
                    } else if (json[JUI.keys.statusCode] == JUI.statusCode.timeout ) {
                        $this.html(response);
                        if ($.pdialog ) {
                            $.pdialog.checkTimeout();
                        }
                        if (navTab ) {
                            navTab.checkTimeout();
                        }
                        alertMsg.error(json[JUI.keys.message] || JUI.msg("sessionTimout"), {
                            okCall: function() {
                                JUI.loadLogin();
                            }
                        });
                    } else {
                        $this.html(response).initUI();
                        if ("function" === typeof op.callback ) {
                            op.callback(response);
                        }
                    }
                },
                error: JUI.ajaxError, statusCode: {
                    503: function(xhr, ajaxOptions, thrownError) {
                        alert(JUI.msg("statusCode_503") || thrownError);
                    }
                }
            });
        },
        loadUrl: function(url, data, callback) {
            $(this).ajaxUrl({
                url: url, data: data, callback: callback
            });
        },
        initUI: function() {
            return this.each(function() {
                if ("function" === typeof initUI ) {
                    initUI(this);
                }
            });
        },
        /**
         * adjust component inner reference box height
         *
         * @param {Object}
         *            refBox: reference box jQuery Obj
         */
        layoutH: function($refBox) {
            return this.each(function() {
                var $this = $(this);
                if (!$refBox ) {
                    $refBox = $this.parents("div.layoutBox").first();
                }
                var iRefH = $refBox.height();

                var iLayoutH = 0;
                if ($this.parents(".rightPageContent").length != 0){
                    iLayoutH = $this.getSiblingsElemsH($this.parents(".rightPageContent"));
                }else if ($this.parents(".leftPageContent").length != 0){
                    iLayoutH = $this.getSiblingsElemsH($this.parents(".leftPageContent"));
                }else if ($this.parents(".pageFormContent").length != 0){
                    iLayoutH = $this.getSiblingsElemsH($this.parents(".pageFormContent")) + 30;
                }else if ($this.parents(".page").length != 0 ) {
                    iLayoutH = $this.getSiblingsElemsH($this.parents(".page"));
                }else if ($this.parents(".dialogContent").length != 0){
                    iLayoutH = $this.getSiblingsElemsH($this.parents(".dialogContent"));
                }

                var iH = iRefH - iLayoutH > 50 ? iRefH - iLayoutH: 50;
                if ($this.isTag("table") ) {
                    $this.removeAttr("layoutH").wrap("<div layoutH=\"" + iLayoutH + "\" style=\"overflow:auto;height:" + iH + "px\"></div>");
                } else {
                    $this.outerHeight(iH).css("overflow", "auto");
                }
            });
        },
        /**
         * 获取page中的其他元素的总高度
         */
        getSiblingsElemsH: function($container) {
            var $page = $container;
            var headerH = this.getElemsH($page, ".pageHeader");
            var formBarH = this.getElemsH($page, ".formBar");
            var contentTitleH = this.getElemsH($page, ".contentTitle");
            var gridHeaderH = this.getElemsH($page, ".gridHeader");
            var tabsHeaderH = this.getElemsH($page, ".tabsHeader");
            var pageBarH = this.getElemsH($page, ".pageBar.panelBar");
            var panelBarH = this.getElemsH($page, ".panelBar:not(.pageBar)");
            return headerH + pageBarH + gridHeaderH + panelBarH + formBarH + tabsHeaderH + contentTitleH;
        },
        /**
         * 获取元素高度
         * @param $container 总容器
         * @param elem    当前元素的jquery选择的字符串
         */
        getElemsH: function($container, elem){
            var h = 0;
            var $elem = $container.find(elem);
            var isSilbinsElem = true;
            if($elem.length != 0){
                if(this.find(elem).length == 0){
                    var $silbingsElems = this.siblings();
                    for(var i=0;i<$silbingsElems.length;i++){
                        if($silbingsElems.eq(i).find(elem).length != 0){
                            isSilbinsElem = false;
                            break;
                        }
                    }
                    if(isSilbinsElem){
                        h = $elem.outerHeight(true);
                    }
                }
            }
            return h;
        },
        isTag: function(tn) {
            if (!tn || undefined == $(this)[0] ) {
                return false;
            }
            return $(this)[0].tagName.toLowerCase() == tn ? true: false;
        },
        /**
         * 判断当前元素是否已经绑定某个事件
         *
         * @param {Object}
         *            type
         */
        isBind: function(type) {
            var _events = $(this).data("events");
            return _events && type && _events[type];
        }
    });

    /**
     * 扩展String方法
     */
    $.extend(String.prototype, {
        isPositiveInteger: function() {
            return ( new RegExp(/^[1-9]\d*$/).test(this) );
        },
        isInteger: function() {
            return (new RegExp(/^\d+$/).test(this));
        },
        isNumber: function(value, element) {
            return (new RegExp(/^-?(?:\d+|\d{1,3}(?:,\d{3})+)(?:\.\d+)?$/).test(this));
        },
        trimstr: function() {
            return this.replace(/(^\s*)|(\s*$)|\r|\n/g, "");
        },
        startsWith: function(pattern) {
            return this.indexOf(pattern) === 0;
        },
        endsWith: function(pattern) {
            var d = this.length - pattern.length;
            return d >= 0 && this.lastIndexOf(pattern) === d;
        },
        replaceSuffix: function(index) {
            var i = this.lastIndexOf("[");
            return this.substring(0,i)+this.substring(i).replace(/\[[0-9]+\]/, "[" + index + "]").replace("#index#", index);
        },
        decodeTXT: function() {
            return (this).replaceAll("\u00A0", " ");
        },
        encodeTXT: function() {
            return (this).replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll(" ", "&nbsp;");
        },
        replaceAll: function(os, ns) {
            return this.replace(new RegExp(os, "gm"), ns);
        },
        replaceTm: function(data) {
            if (!data) {
                return this;
            }
            return this.replace(RegExp("({[A-Za-z_]+[A-Za-z0-9_]*})", "g"), function($1) {
                return data[$1.replace(/[{}]+/g, "")];
            })
        },
        replaceTmById: function(_box) {
            var $parent = _box || $(document);
            return this.replace(RegExp("({[A-Za-z_]+[A-Za-z0-9_]*})", "g"), function($1) {
                var $input = $parent.find("#" + $1.replace(/[{}]+/g, ""));
                return $input.val() ? $input.val(): $1;
            });
        },
        isFinishedTm: function() {
            return ! ( new RegExp("{[A-Za-z_]+[A-Za-z0-9_]*}").test(this) );
        },
        skipChar: function(ch) {
            if (!this || this.length === 0) {
                return "";
            }
            if (this.charAt(0) === ch) {
                return this.substring(1).skipChar(ch);
            }
            return this;
        },
        isValidPwd: function() {
            return (new RegExp(/^([_]|[a-zA-Z0-9]){6,32}$/).test(this));
        },
        isValidMail: function() {
            return (new RegExp(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/).test(this.trimstr()));
        },
        isUrl: function() {
            return ( new RegExp(/^([a-zA-z]+:)?\/\/([a-zA-Z0-9\-\.]+)([-\w .\/?%&=:]*)$/).test(this) );
        },
        isExternalUrl: function() {
            var domain = document.domain;
            if("" == domain){
                domain = "localhost";
            }
            return this.isUrl() && this.indexOf("//" + domain) == -1;
        }
    });
} )(jQuery);
/**
 * html转义
 * @param sHtml
 * @returns
 */
function html2Escape(sHtml) {
    return Base64.encode(sHtml);
}

function unEscapeHtml(str) {
    if(str) {
        return str.decodeTXT();
    } else {
        return str;
    }
}
function escapeHtml(str) {
    if(str) {
        return str.encodeTXT();
    } else {
        return str;
    }
}