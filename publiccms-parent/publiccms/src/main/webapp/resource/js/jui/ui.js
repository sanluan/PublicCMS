/**
 * 初始化UI
 */
function initEnv() {
    $("body").append(JUI.frag["dwzFrag"]);
    if (!$.support.leadingWhitespace ) {
        try {
            document.execCommand("BackgroundImageCache", false, true);
        } catch (e) {}
    }
    $(window).resize(function() {
        initLayout();
        $(this).trigger(JUI.eventType.resizeGrid);
        $(this).trigger(JUI.eventType.resizeChart);
    });
    var ajaxbg = $("#background,#progressBar");
    ajaxbg.hide();
    $(document).ajaxStart(function() {
        ajaxbg.show();
    }).ajaxStop(function() {
        ajaxbg.hide();
    });
    $("#progressBar").click(function(){
        ajaxbg.hide();
    });
    $("#leftside").jBar({
        minW: 150, maxW: 700
    });
    if ($.taskBar ){
        $.taskBar.init();
    }
    setTimeout(function() {
        initLayout();
        if (window.navTab) {
            navTab.init();
        }
        initUI();
        if ($.fn.navMenu ){
            var hash = location.hash.skipChar('#').replace(/\?.*$/, '');
            var callback;
            var parentId;
            if(hash ) {
                parentId = hash.substring(0, hash.indexOf('_'));
                var tabid = hash.substring(hash.indexOf('_') + 1);
                if(tabid ) {
                    callback = function(){
                        $('#menu a[rel='+escapeJquery(tabid)+']').click();
                    }
                }
            }
            $("#navMenu").navMenu(callback);
            if(parentId ) {
                $('#navMenu a[parentid='+parentId+']').click();
            }
        }
        $(document).trigger(JUI.eventType.initEnvAfter);
    }, 10);
}
/**
 * 初始化布局
 */
function initLayout() {
    var iContentW = $(window).width() - (JUI.ui.sbar ? $("#sidebar").width() : 0);
    var iContentH = $(window).height() - $('header').outerHeight(true) - $('footer').outerHeight(true);
    $("#navTab").css({"width":iContentW+'px'});
    $("main .tabsPageContent").height(iContentH - $('.tabsPageHeader').outerHeight(true)).find("[layoutH]").layoutH();
    $("#splitBar, #splitBarProxy").height(iContentH - 2);
    $("#taskbar").css({
        top: (iContentH + $("header").height())+'px', width: $(window).width()+'px'
    });
    $("#menu").css({'max-height':(iContentH-$("#sidebar .collapse").height())+'px'});
}
/**
 * 为容器初始化UI
 * @param _box 容器
 */
function initUI(_box) {
    var $p = $(_box || document);
    // css tables
    $('table.list', $p).cssTable();
    // jTables
    $('table.table', $p).jTable();

    // auto bind tabs
    $("div.tabs", $p).each(function() {
        var $this = $(this);
        var options = {};
        options.currentIndex = $this.attr("currentIndex") || 0;
        options.eventType = $this.attr("eventType") || "click";
        $this.tabs(options);
    });
    $("ul.tree", $p).jTree();
    $('div.accordion', $p).each(function() {
        var $this = $(this);
        $this.accordion({
            alwaysOpen: false, active: 0, autoheight:false
        });
    });
    $(":button.checkboxCtrl, :checkbox.checkboxCtrl", $p).checkboxCtrl($p);
    if ($.fn.combox ){
        $("select.combox", $p).combox();
    }
    if ($.fn.uploadify ) {
        $(":file[uploaderOption]", $p).each(function() {
            var $this = $(this);
            var options = {
                fileObjName: $this.attr("name") || "file", auto: true, multi: true, onUploadError: uploadifyError
            };
            var uploaderOption = JUI.jsonEval($this.attr("uploaderOption"));
            $.extend(options, uploaderOption);
            JUI.debug("uploaderOption: " + JUI.obj2str(uploaderOption));
            $this.uploadify(options);
        });
    }

    // validate form
    $("form.required-validate", $p).each(function() {
        var $form = $(this);
        $form.validate({
            onsubmit: false, focusInvalid: false, focusCleanup: true, errorElement: "span", ignore: ".ignore", invalidHandler: function(form, validator) {
                var errors = validator.numberOfInvalids();
                if (errors ) {
                    var message = JUI.msg("validateFormError", [ errors ]);
                    alertMsg.error(message);
                }
            }
        });
        $form.find("input[customvalid]").each(function() {
            var $input = $(this);
            $input.rules("add", {
                customvalid: $input.attr("customvalid")
            })
        });
    });
    if ($.fn.datepicker ) {
        $('input.date', $p).each(function() {
            var $this = $(this);
            var opts = {};
            if ($this.attr("dateFmt") ) {
                opts.pattern = $this.attr("dateFmt");
            }
            if ($this.attr("minDate") ) {
                opts.minDate = $this.attr("minDate");
            }
            if ($this.attr("maxDate") ) {
                opts.maxDate = $this.attr("maxDate");
            }
            if ($this.attr("mmStep") ) {
                opts.mmStep = $this.attr("mmStep");
            }
            if ($this.attr("ssStep") ) {
                opts.ssStep = $this.attr("ssStep");
            }
            $this.datepicker(opts);
        });
    }

    initLink($p);

    if ($.fn.sortDrag) {
        $("div.sortDrag", $p).sortDrag();
    }
    if ($.fn.miscSortDrag) {
        $(".miscSortDrag", $p).miscSortDrag();
    }

    // ajax.js
    if ($.fn.multLookup ) {
        $("[multLookup]:button", $p).multLookup();
    }
    if ($.fn.suggest ) {
        $("input[suggestFields]", $p).suggest();
    }
    if ($.fn.itemDetail ) {
        $("table.itemDetail", $p).itemDetail();
    }
    // 执行第三方jQuery插件【 第三方jQuery插件注册：JUI.regPlugins.push(function($p){}); 】
    $.each(JUI.regPlugins, function(index, fn) {
        fn($p);
    });
    // init styles
    $("input[type=text], input[type=number], input[type=password], textarea", $p).not("textarea.editor", $p).addClass("textInput");
    $("input[readonly], textarea[readonly]", $p).addClass("readonly");
    $("input[disabled=true], textarea[disabled=true]", $p).addClass("disabled");
    $("input[type=text]", $p).not("div.tabs input[type=text]", $p).filter("[alt]").inputAlert();
}
/**
 * 为容器初始化链接
 * @param $p 容器
 */
function initLink($p) {
    // navTab
    $("a[target=navTab]", $p).each(function() {
        $(this).click(function(event) {
            var $this = $(this);
            var title = $this.attr("title") || $this.text();
            if(title){
                title = title.replace(/<[^>]*>/gi,"");
            }
            var icon = $this.attr("icon") || $this.find("i").prop("outerHTML");
            var tabid = $this.attr("rel") || "_blank";
            var fresh = eval($this.attr("fresh") || "true");
            var external = eval($this.attr("external") || "false");
            var url = $this.attr("href").replaceTmById($(event.target).parents(".unitBox:first"));
            var newWindow = (navigator.platform.match("Mac") ? event.metaKey : event.ctrlKey);
            JUI.debug(url);
            if (!url.isFinishedTm() ) {
                alertMsg.error($this.attr("warn") || JUI.msg("alertSelectMsg"));
                return false;
            }
            navTab.openTab(tabid, url, {
                title: title, icon: icon, fresh: fresh, external: external, focusNewWindow:newWindow
            });
            return false;
        });
    });

    // dialogs
    $("a[target=dialog]", $p).each(function() {
        $(this).click(function(event) {
            var $this = $(this);
            var title = $this.attr("title") || $this.text();
            var rel = $this.attr("rel") || "_blank";
            var options = {};
            var w = $this.attr("width");
            var h = $this.attr("height");
            if (w ) {
                options.width = w;
            }
            if (h ) {
                options.height = h;
            }
            options.max = eval($this.attr("max") || "false");
            options.mask = eval($this.attr("mask") || "false");
            options.maxable = eval($this.attr("maxable") || "true");
            options.minable = eval($this.attr("minable") || "true");
            options.fresh = eval($this.attr("fresh") || "true");
            options.resizable = eval($this.attr("resizable") || "true");
            options.drawable = eval($this.attr("drawable") || "true");
            options.close = eval($this.attr("close") || "");
            options.param = $this.attr("param") || "";
            options.focusNewWindow = (navigator.platform.match("Mac") ? event.metaKey : event.ctrlKey);
            var url = $this.attr("href").replaceTmById($(event.target).parents(".unitBox:first"));
            JUI.debug(url);
            if (!url.isFinishedTm() ) {
                alertMsg.error($this.attr("warn") || JUI.msg("alertSelectMsg"));
                return false;
            }
            $.pdialog.open(url, rel, title, options);
            return false;
        });
    });
    $("a[target=ajax]", $p).each(function() {
        $(this).click(function() {
            var $this = $(this);
            var rel = $this.attr("rel");
            if (rel ) {
                var $rel = $("#" + rel);
                $rel.loadUrl($this.attr("href"), {}, function() {
                    $rel.find("[layoutH]").layoutH();
                });
            }
            return false;
        });
    });
    if ($.fn.ajaxTodo ) {
        $("a[target=ajaxTodo]", $p).ajaxTodo();
    }
    if ($.fn.dwzExport ) {
        $("a[target=dwzExport]", $p).dwzExport();
    }
    if ($.fn.lookup ) {
        $("a[lookupGroup]", $p).lookup();
    }
    if ($.fn.selectedTodo ) {
        $("a[target=selectedTodo]", $p).selectedTodo();
    }
}
/**
 * Theme Plugins
 *
 * @author ZhangHuihua@msn.com
 */
( function($) {
    $.fn.extend({
        theme: function(options) {
            var op = $.extend({
                themeBase: "themes", defaultTheme: "default"
            }, options);
            var _themeHref = op.themeBase + "#theme#.css";
            var $themeItem = $("<link href=\"" + _themeHref.replace("#theme#", op.defaultTheme) + "\" rel=\"stylesheet\" media=\"screen\"/>");
            var setTheme = function(themeName) {
                $themeItem.attr("href", _themeHref.replace("#theme#", themeName));
                jThemeLi.find(">div").removeClass("selected");
                jThemeLi.filter("[theme=" + themeName + "]").find(">div").addClass("selected");
                if ("function" === typeof $.cookie ) {
                    $.cookie("dwz_theme", themeName);
                }
            }
            var jThemeLi = $(this).find(">li[theme]");
            jThemeLi.each(function(index) {
                var $this = $(this);
                var themeName = $this.attr("theme");
                if(themeName == op.defaultTheme){
                    $this.find(">div").addClass("selected");
                }
                $this.addClass(themeName).click(function() {
                    setTheme(themeName);
                });
            });
            if ("function" === typeof $.cookie ) {
                if ($.cookie("dwz_theme") ) {
                    setTheme($.cookie("dwz_theme"));
                }
            }
            $themeItem.appendTo($("head"));
            return this;
        }
    });
} )(jQuery);