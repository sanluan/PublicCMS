/**
 * @author ZhangHuihua@msn.com
 */
var DWZ = {
    version: "1.6.2" ,
    regPlugins: [ ], // [function($parent){} ...]
    // sbar: show sidebar
    keyCode: {
        ENTER: 13, ESC: 27, END: 35, HOME: 36, SHIFT: 16, TAB: 9, LEFT: 37, RIGHT: 39, UP: 38, DOWN: 40, DELETE: 46, BACKSPACE: 8, CHAR_S: 83
    } ,
    eventType: {
        pageClear: "pageClear", // 用于重新ajaxLoad、关闭nabTab, 关闭dialog时，去除xheditor等需要特殊处理的资源
        editorSync: "editorSync",
        resizeGrid: "resizeGrid", // 用于窗口或dialog大小调整
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
    ui: {
        sbar: true
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
        if ($.pdialog && DWZ._set.loginTitle ) {
            $.pdialog.open(DWZ._set.loginUrl, "login", DWZ._set.loginTitle, {
                mask: true, width: 520, height: 260
            });
        } else {
            window.location = DWZ._set.loginUrl;
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
                    r.push("\""+i + "\":" + DWZ.obj2str(o[i]));
                if (!!document.all && !/^\n?function\s*toString\(\)\s*\{\n?\s*\[native code\]\n?\s*\}\n?\s*$/.test(o.toString) ) {
                    r.push("toString:" + o.toString.toString());
                }
                r = "{" + r.join() + "}"
            } else {
                for (var i = 0; i < o.length; i++) {
                    r.push(DWZ.obj2str(o[i]));
                }
                r = "[" + r.join() + "]"
            }
            return r;
        }
        return o.toString();
    } ,
    jsonEval: function(data) {
        try {
            if (typeof data == 'string' ) {
                return eval('(' + data + ')');
            } else {
                return data;
            }
        } catch (e) {
            return {};
        }
    } ,
    ajaxError: function(xhr, ajaxOptions, thrownError) {
        if (alertMsg ) {
            if('undefined' == typeof thrownError||"" ==thrownError){
                var exception = $($.parseHTML(xhr.responseText, document, true)).find('#divexception textarea');
                if(exception.length){
                    thrownError=exception.val();
                }
            }
            alertMsg.error("<div>Http status: " + xhr.status + " " + xhr.statusText + "</div>" + "<div>ajaxOptions: " + ajaxOptions + "</div>" + "<div>thrownError: " + thrownError
                    + "</div>");
        } else {
            alert("Http status: " + xhr.status + " " + xhr.statusText + "\najaxOptions: " + ajaxOptions + "\nthrownError:" + thrownError + "\n" + xhr.responseText);
        }
    },
    ajaxDone: function(json) {
        if (json[DWZ.keys.statusCode] == DWZ.statusCode.error ) {
            if (json[DWZ.keys.message] && alertMsg ) {
                alertMsg.error(json[DWZ.keys.message]);
            }
        } else if (json[DWZ.keys.statusCode] == DWZ.statusCode.timeout ) {
            if (alertMsg ){
                alertMsg.error(json[DWZ.keys.message] || DWZ.msg("sessionTimout"), {
                    okCall: DWZ.loadLogin
                });
            } else {
                DWZ.loadLogin();
            }
        } else if (json[DWZ.keys.statusCode] == DWZ.statusCode.okAndRefresh ){
            if (json[DWZ.keys.message] && alertMsg ){
                alertMsg.correct(json[DWZ.keys.message],{
                    callback:function(){
                        window.location.reload();
                    }
                });
            }
        } else {
            if (json[DWZ.keys.message] && alertMsg ){
                alertMsg.correct(json[DWZ.keys.message]);
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
        $.extend(DWZ.statusCode, op.statusCode);
        $.extend(DWZ.keys, op.keys);
        $.extend(DWZ.pageInfo, op.pageInfo);
        $.ajax({
            type: 'GET', url: pageFrag, dataType: 'html', cache: false, error: function(xhr) {
                alert(xhr.statusText);
            }, success: function(html) {
                $($.parseHTML(html, document, true)).each(function() {
                    var pageId = $(this).attr("id");
                    if (pageId ) {
                        DWZ.frag[pageId] = $(this).text();
                    }
                });
                if ("function" === typeof op.callback ) {
                    op.callback();
                }
            }
        });
        var _doc = $(document);
        if (!_doc.isBind(DWZ.eventType.editorSync) ) {
            _doc.on(DWZ.eventType.editorSync, null, null, function(event) {
                var box = event.target;
                var $box = $(box);
                $("textarea.editor", $box).each(function() {
                    if('ckeditor'==$(this).attr('editorType')) {
                        if(CKEDITOR.instances[$(this).data("id")]) {
                            CKEDITOR.instances[$(this).data("id")].updateElement();
                        }
                    } else if ("tinymce"==$(this).attr("editorType")){
                        if(tinymce.get($(this).data("id"))) {
                            tinymce.get($(this).data("id")).save();
                        }
                    } else if ("kindeditor"==$(this).attr("editorType")){
                        KindEditor.sync('#'+$(this).data("id"));
                    } else {
                        if(UE.instants[$(this).data("id")]) {
                            UE.instants[$(this).data("id")].sync();
                        }
                    }
                });
                $("textarea.code", $box).each(function() {
                    if(DWZ.instances[$(this).data("id")]) {
                        DWZ.instances[$(this).data("id")].save();
                    }
                });
        
                $(".miscSortDrag", $box).each(function() {
                    var $sortBox=$(this);
                    if($sortBox.data("result")){
                        $sortBox.find($sortBox.data("result")).val(DWZ.obj2str($sortBox.miscSortDragData($sortBox)));
                    }
                });
            });
        }
        if (!_doc.isBind(DWZ.eventType.pageClear) ) {
            _doc.on(DWZ.eventType.pageClear, null, null, function(event) {
                var box = event.target;
                var $box = $(box);
                $("textarea.editor", $box).each(function() {
                    if('ckeditor'==$(this).attr('editorType')) {
                        if(CKEDITOR.instances[$(this).data("id")]) {
                            CKEDITOR.instances[$(this).data("id")].destroy();
                        }
                    } else if("tinymce"==$(this).attr("editorType")) {
                        tinymce.remove('#'+$(this).data("id"));
                    } else if("kindeditor"==$(this).attr("editorType")) {
                        KindEditor.remove('#'+$(this).data("id"));
                    } else {
                        if(UE.instants[$(this).data("id")]) {
                            UE.instants[$(this).data("id")].destroy();
                        }
                    }
                });
                $("textarea.code", $box).each(function() {
                    if(DWZ.instances[$(this).data("id")]) {
                        DWZ.instances[$(this).data("id")].toTextArea();
                        delete DWZ.instances[$(this).data("id")];
                    }
                });
                $(".image-editor", $box).each(function() {
                    if(DWZ.instances[$(this).data("id")]) {
                        DWZ.instances[$(this).data("id")].destroy();
                        delete DWZ.instances[$(this).data("id")];
                    }
                });
                $('[close-url]',$box).each(function (){
                    $.getJSON($(this).attr("close-url"), function(data) {});
                });
            });
        }
    }
};

( function($) {
    // DWZ set regional
    $.setRegional = function(key, value) {
        if (!$.regional ) {
            $.regional = {};
        }
        $.regional[key] = value;
    };

    // DWZ set msg
    $.setMessage = function(key, value) {
        DWZ._msg[key] = value;
    };

    $.fn.extend({
        /**
         * @param {Object}
         *            op: {type:GET/POST, url:ajax请求地址, data:ajax请求参数列表,
         *            callback:回调函数 }
         */
        ajaxUrl: function(op) {
            var $this = $(this);
            $this.trigger(DWZ.eventType.pageClear);
            $.ajax({
                type: op.type || 'GET', url: op.url, data: op.data, cache: false, success: function(response) {
                    var json = DWZ.jsonEval(response);
                    if (json[DWZ.keys.statusCode] == DWZ.statusCode.error ) {
                        if (json[DWZ.keys.message] ) {
                            alertMsg.error(json[DWZ.keys.message]);
                        }
                    } else if (json[DWZ.keys.statusCode] == DWZ.statusCode.timeout ) {
                        $this.html(response);
                        if ($.pdialog ) {
                            $.pdialog.checkTimeout();
                        }
                        if (navTab ) {
                            navTab.checkTimeout();
                        }
                        alertMsg.error(json[DWZ.keys.message] || DWZ.msg("sessionTimout"), {
                            okCall: function() {
                                DWZ.loadLogin();
                            }
                        });
                    } else {
                        $this.html(response).initUI();
                        if ("function" === typeof op.callback ) {
                            op.callback(response);
                        }
                    }
                },
                error: DWZ.ajaxError, statusCode: {
                    503: function(xhr, ajaxOptions, thrownError) {
                        alert(DWZ.msg("statusCode_503") || thrownError);
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
                    $refBox = $this.parents("div.layoutBox:first");
                }
                var iRefH = $refBox.height();

                var iLayoutH = 0;
                if ($this.parents('.rightPageContent').length != 0){
                    iLayoutH = $this.getSiblingsElemsH($this.parents('.rightPageContent'));
                }else if ($this.parents('.leftPageContent').length != 0){
                    iLayoutH = $this.getSiblingsElemsH($this.parents('.leftPageContent'));
                }else if ($this.parents('.pageFormContent').length != 0){
                    iLayoutH = $this.getSiblingsElemsH($this.parents('.pageFormContent')) + 30;
                }else if ($this.parents('.page').length != 0 ) {
                    iLayoutH = $this.getSiblingsElemsH($this.parents('.page'));
                }else if ($this.parents('.dialogContent').length != 0){
                    iLayoutH = $this.getSiblingsElemsH($this.parents('.dialogContent'));
                }

                var iH = iRefH - iLayoutH > 50 ? iRefH - iLayoutH: 50;
                if ($this.isTag("table") ) {
                    $this.removeAttr("layoutH").wrap('<div layoutH="' + iLayoutH + '" style="overflow:auto;height:' + iH + 'px"></div>');
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
            var headerH = this.getElemsH($page, '.pageHeader');
            var formBarH = this.getElemsH($page, '.formBar');
            var contentTitleH = this.getElemsH($page, '.contentTitle');
            var gridHeaderH = this.getElemsH($page, '.gridHeader');
            var tabsHeaderH = this.getElemsH($page, '.tabsHeader');
            var pageBarH = this.getElemsH($page, '.pageBar.panelBar');
            var panelBarH = this.getElemsH($page, '.panelBar:not(.pageBar)');
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
        inputAlert: function() {
            return this.each(function() {
                var $this = $(this);
                function getAltBox() {
                    return $this.parent().find("label.alt");
                }
                function altBoxCss(opacity) {
                    var position = $this.position();
                    return {
                        width: $this.width(), top: position.top + 'px', left: position.left + 'px', opacity: opacity || 1
                    };
                }
                if (getAltBox().length < 1 ) {
                    if (!$this.attr("id") ) {
                        $this.attr("id", $this.attr("name") + "_" + Math.round(Math.random() * 10000));
                    }
                    var $label = $('<label class="alt" for="' + $this.attr("id") + '">' + $this.attr("alt") + '</label>').appendTo($this.parent());
                    $label.css(altBoxCss(0.6));
                    if ($this.val() ) {
                        $label.hide();
                    }
                }
                $this.focus(function() {
                    getAltBox().css(altBoxCss(0.3));
                }).blur(function() {
                    if (!$(this).val() ) {
                        getAltBox().show().css("opacity", 1);
                    }
                }).keydown(function() {
                    getAltBox().hide();
                });
            });
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
        trim: function() {
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
            var i = this.lastIndexOf('[');
            return this.substring(0,i)+this.substring(i).replace(/\[[0-9]+\]/, "[" + index + "]").replace("#index#", index);
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
            return (new RegExp(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/).test(this.trim()));
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
/**
 * You can use this map like this: var myMap = new Map();
 * myMap.put("key","value"); var key = myMap.get("key"); myMap.remove("key");
 */
function Map() {
    this.elements = new Array();
    this.size = function() {
        return this.elements.length;
    }
    this.isEmpty = function() {
        return ( this.elements.length < 1 );
    }
    this.clear = function() {
        this.elements = new Array();
    }
    this.put = function(_key, _value) {
        this.remove(_key);
        this.elements.push({
            key: _key, value: _value
        });
    }
    this.remove = function(_key) {
        try {
            for (i = 0; i < this.elements.length; i++) {
                if (this.elements[i].key == _key ) {
                    this.elements.splice(i, 1);
                    return true;
                }
            }
        } catch (e) {
            return false;
        }
        return false;
    }
    this.get = function(_key) {
        try {
            for (i = 0; i < this.elements.length; i++) {
                if (this.elements[i].key == _key ) {
                    return this.elements[i].value;
                }
            }
        } catch (e) {
            return null;
        }
    }
    this.element = function(_index) {
        if (_index < 0 || _index >= this.elements.length ) {
            return null;
        }
        return this.elements[_index];
    }
    this.containsKey = function(_key) {
        try {
            for (i = 0; i < this.elements.length; i++) {
                if (this.elements[i].key == _key ) {
                    return true;
                }
            }
        } catch (e) {
            return false;
        }
        return false;
    }
    this.values = function() {
        var arr = new Array();
        for (i = 0; i < this.elements.length; i++) {
            arr.push(this.elements[i].value);
        }
        return arr;
    }
    this.keys = function() {
        var arr = new Array();
        for (i = 0; i < this.elements.length; i++) {
            arr.push(this.elements[i].key);
        }
        return arr;
    }
}