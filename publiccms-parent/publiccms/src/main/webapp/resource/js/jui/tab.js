/**
 * @author ZhangHuihua@msn.com
 */
( function($) {
    $.fn.extend({

        /**
         * options: reverse[true, false], eventType[click, hover],
         * currentIndex[default index 0] stTab[tabs selector], stTabPanel[tab
         * panel selector] ajaxClass[ajax load], closeClass[close tab]
         */
        tabs: function(options) {
            var op = $.extend({
                reverse: false, eventType: "click", currentIndex: 0, stTabHeader: "> .tabsHeader", stTab: ">.tabsHeaderContent>ul", stTabPanel: "> .tabsContent" ,
                ajaxClass: "j-ajax", closeClass: "close", prevClass: "tabsLeft", nextClass: "tabsRight"
            }, options);
            return this.each(function() {
                initTab($(this));
            });
            function initTab(jT) {
                var jSelector = jT.add($("> *", jT));
                var jTabHeader = $(op.stTabHeader, jSelector);
                var jTabs = $(op.stTab + " li", jTabHeader);
                var jGroups = $(op.stTabPanel + " > *", jSelector);
                jTabs.off().find("a").off();
                jTabHeader.find("." + op.prevClass).off();
                jTabHeader.find("." + op.nextClass).off();
                jTabs.each(function(iTabIndex) {
                    if (op.currentIndex == iTabIndex ) {
                        $(this).addClass("selected");
                    } else {
                        $(this).removeClass("selected");
                    }
                    if (op.eventType == "hover" ) {
                        $(this).on( "mouseenter",function(event) {
                            switchTab(jT, iTabIndex)
                        });
                    } else {
                        $(this).on("click", function(event) {
                            switchTab(jT, iTabIndex)
                        });
                    }
                    $("a", this).each(function() {
                        if ($(this).hasClass(op.ajaxClass) ) {
                            $(this).on("click", function(event) {
                                var jGroup = jGroups.eq(iTabIndex);
                                if (this.href && !jGroup.attr("loaded") ) {
                                    jGroup.loadUrl(this.href, {}, function() {
                                        jGroup.find("[layoutH]").layoutH();
                                        jGroup.attr("loaded", true);
                                    });
                                }
                                event.preventDefault();
                            });
                        } else if ($(this).hasClass(op.closeClass) ) {
                            $(this).on("click", function(event) {
                                jTabs.eq(iTabIndex).remove();
                                jGroups.eq(iTabIndex).remove();
                                if (iTabIndex == op.currentIndex ) {
                                    op.currentIndex = ( iTabIndex + 1 < jTabs.length ) ? iTabIndex: iTabIndex - 1;
                                } else if (iTabIndex < op.currentIndex ) {
                                    op.currentIndex = iTabIndex;
                                }
                                initTab(jT);
                                return false;
                            });
                        }
                    });
                });
                switchTab(jT, op.currentIndex);
            }
            function switchTab(jT, iTabIndex) {
                var jSelector = jT.add($("> *", jT));
                var jTabHeader = $(op.stTabHeader, jSelector);
                var jTabs = $(op.stTab + " li", jTabHeader);
                var jGroups = $(op.stTabPanel + " > *", jSelector);
                var jTab = jTabs.eq(iTabIndex);
                var jGroup = jGroups.eq(iTabIndex);
                if (op.reverse && ( jTab.hasClass("selected") ) ) {
                    jTabs.removeClass("selected");
                    jGroups.hide();
                } else {
                    op.currentIndex = iTabIndex;
                    jTabs.removeClass("selected");
                    jTab.addClass("selected");
                    jGroups.hide().eq(op.currentIndex).show();
                }
            }
        }
    });
} )(jQuery);

/**
 * @author ZhangHuihua@msn.com
 */
$.setRegional("alertMsg", {
    title: {
        error: "Error", info: "Information", warn: "Warning", correct: "Successful", confirm: "Confirmation"
    }, butMsg: {
        ok: "OK", yes: "Yes", no: "No", cancel: "Cancel"
    }
});
var alertMsg = {
    _boxId: "#alertMsgBox", _bgId: "#alertBackground", _closeTimer: null, _types: {
        error: "error", info: "info", warn: "warn", correct: "correct", confirm: "confirm"
    }, _getTitle: function(key) {
        return $.regional.alertMsg.title[key];
    }, _keydownOk: function(event) {
        if (event.keyCode == JUI.keyCode.ENTER || event.keyCode == JUI.keyCode.BACKSPACE ){
            event.data.target.trigger("click");
            return false;
        }
    }, _keydownEsc: function(event) {
        if (event.keyCode == JUI.keyCode.ESC ){
            event.data.target.trigger("click");
        }
    } ,
    /**
     * @param {Object}
     *            type
     * @param {Object}
     *            msg
     * @param {Object}
     *            buttons [button1, button2]
     */
    _open: function(type, msg, buttons) {
        $(this._boxId).remove();
        var butsHtml = "";
        if (buttons ) {
            for (var i = 0; i < buttons.length; i++) {
                var sRel = buttons[i].call ? "callback": "";
                butsHtml += JUI.frag["alertButFrag"].replace("#butMsg#", buttons[i].name).replace("#callback#", sRel);
            }
        }
        var boxHtml = JUI.frag["alertBoxFrag"].replace("#type#", type).replace("#title#", this._getTitle(type)).replace("#message#", msg).replace("#butFragment#", butsHtml);
        $($.parseHTML(boxHtml, document, true)).appendTo("body").css({
            top: -$(this._boxId).height() + "px"
        }).animate({
            top: "0px"
        }, 500);
        if (this._closeTimer ) {
            clearTimeout(this._closeTimer);
            this._closeTimer = null;
        }
        if (this._types.info == type || this._types.correct == type ) {
            this._closeTimer = setTimeout(function() {
                alertMsg.close()
            }, 3500);
        } else {
            $(this._bgId).show();
        }
        var jButs = $(this._boxId).find("a.button");
        var jCallButs = jButs.filter("[rel=callback]");
        var jDoc = $(document);
        for (var i = 0; i < buttons.length; i++) {
            if (buttons[i].call ){
                jCallButs.eq(i).on("click", buttons[i].call);
            }
            if (buttons[i].keyCode == JUI.keyCode.ENTER ) {
                jDoc.on("keydown", null, {
                    target: jButs.eq(i)
                }, this._keydownOk);
            }
            if (buttons[i].keyCode == JUI.keyCode.ESC ) {
                jDoc.on("keydown", null, {
                    target: jButs.eq(i)
                }, this._keydownEsc);
            }
        }
    }, close: function() {
        $(document).off("keydown", null, this._keydownOk).off("keydown", null, this._keydownEsc);
        $(this._boxId).animate({
            top: -$(this._boxId).height()
        }, 100, function() {
            $(this).remove();
        });
        $(this._bgId).hide();
        if(this._callback){
            this._callback();
            this._callback = null;
        }
    }, error: function(msg, options) {
        this._alert(this._types.error, msg, options);
    }, info: function(msg, options) {
        this._alert(this._types.info, msg, options);
    }, warn: function(msg, options) {
        this._alert(this._types.warn, msg, options);
    }, correct: function(msg, options) {
        this._alert(this._types.correct, msg, options);
    }, _alert: function(type, msg, options) {
        var op = {
            okName: $.regional.alertMsg.butMsg.ok, okCall: null
        };
        $.extend(op, options);
        if(options && options.callback && "function" === typeof options.callback){
            this._callback = options.callback;
        }
        var buttons = [ {
            name: op.okName, call: op.okCall, keyCode: JUI.keyCode.ENTER
        } ];
        this._open(type, msg, buttons);
    } ,
    /**
     * @param {Object}
     *            msg
     * @param {Object}
     *            options {okName, okCal, cancelName, cancelCall}
     */
    confirm: function(msg, options) {
        var op = {
            okName: $.regional.alertMsg.butMsg.ok, okCall: null, cancelName: $.regional.alertMsg.butMsg.cancel, cancelCall: null
        };
        $.extend(op, options);
        var buttons = [ {
            name: op.okName, call: op.okCall, keyCode: JUI.keyCode.ENTER
        }, {
            name: op.cancelName, call: op.cancelCall, keyCode: JUI.keyCode.ESC
        } ];
        this._open(this._types.confirm, msg, buttons);
    }
};