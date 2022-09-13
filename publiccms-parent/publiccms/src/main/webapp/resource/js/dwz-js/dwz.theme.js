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