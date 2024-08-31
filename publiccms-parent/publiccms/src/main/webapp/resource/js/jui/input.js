/**
 * @author Roger Wu
 */

( function($) {
    var allSelectBox = [ ];
    var killAllBox = function(bid) {
        $.each(allSelectBox, function(i) {
            if (allSelectBox[i] != bid ) {
                if (!$("#" + allSelectBox[i])[0] ) {
                    $("#op_" + allSelectBox[i]).remove();
                } else {
                    $("#op_" + allSelectBox[i]).css({
                        height: "", width: ""
                    }).hide();
                }
                $("#"+ allSelectBox[i]).children("a").removeClass("expand");
                $(document).off("click", null, killAllBox);
            }
        });
    };
    var _onchange = function(event) {
        var $ref = $("select[name=" + $.escapeSelector(event.data.$this.attr("ref"))+"]");
        if ($ref.length == 0 ) {
            return false;
        }
        if(event.data.$this.attr("index")){
            $ref=$ref.eq(event.data.$this.attr("index"));
        }
        $.ajax({
            type: "POST", dataType: "json", url: event.data.$this.attr("refUrl").replace("{value}", encodeURIComponent(event.data.$this.val())), cache: false, data: {} ,
            success: function(json) {
                if (!json ) {
                    return;
                }
                $ref.empty();
                $.each(json, function(i) {
                    if (json[i] && json[i].length > 1 ) {
                        $("<option></option>").attr("value",json[i][0]).text(json[i][1]).appendTo($ref);
                    }
                });
                var $refCombox = $ref.parents("div.combox:first");
                $ref.insertAfter($refCombox);
                $refCombox.remove();
                $ref.trigger("change").combox();
            }, error: JUI.ajaxError
        });
    };
    $.extend($.fn, {
        comboxSelect: function(options) {
            var op = $.extend({
                selector: ">a"
            }, options);
            return this.each(function() {
                var box = $(this);
                var selector = $(op.selector, box);
                allSelectBox.push(box.attr("id"));
                $(op.selector, box).click(function() {
                    var options = $("#op_" + box.attr("id"));
                    var optionlist=$("ul",options);
                    if (options.is(":hidden") ) {
                        box.children("a").addClass("expand");
                        if (options.height() > 600 ) {
                            options.addClass("lot");
                        }
                        if (options.height() > 300 ) {
                            optionlist.css({
                                height: "300px"
                            });
                        }
                        var top = box.offset().top + box[0].offsetHeight + 1;
                        if (top + options.outerHeight(true) > $(window).height() - 20 ) {
                            top = top - box.outerHeight(true) - options.outerHeight(true) - 1;
                        }
                        var left = box.offset().left;
                        if (left + options.outerWidth(true) > $(window).width()) {
                            left = $(window).width() - options.outerWidth(true) - 1;
                        }
                        options.css({
                            top: top, left: left
                        }).show();
                        if($("a.selected",options).length && 0 == optionlist.scrollTop()){
                            optionlist.stop().animate({scrollTop:$("a.selected",optionlist).offset().top + 60 - options.height() -optionlist.offset().top},500);
                        }
                        $(".search input",options).focus().keyup(function(){
                            var val = $(this).val();
                            if(val){
                                $("li",optionlist).hide();
                                $("li a:contains("+$.escapeSelector(val)+")",optionlist).parent().show();
                                $("li a",optionlist).each(function(){
                                    if(-1 < $(this).attr("value").indexOf(val)){
                                        $(this).parent().show();
                                    }
                                });
                            }else{
                                $("li",optionlist).show();
                            }
                            $("li.disabled",optionlist).hide();
                        }).click(function(){
                            return false;
                        }).val("");
                        $("li",optionlist).show();
                        $("li.disabled",optionlist).hide();
                        killAllBox(box.attr("id"));
                        $(document).click(killAllBox);
                    } else {
                        $(document).off("click", null, killAllBox);
                        killAllBox();
                    }
                    return false;
                });
                $("#op_" + box.attr("id")).find(" li").comboxOption(selector, box);
            });
        } ,
        comboxOption: function(selector, box) {
            return this.each(function() {
                $(">a", this).click(function() {
                    var $this = $(this);
                    $this.parent().parent().find(".selected").removeClass("selected");
                    $this.addClass("selected");
                    selector.text($this.text());
                    var $input = $("select", box);
                    if ($input.val() != unEscapeHtml($this.attr("value")) ) {
                        $input.val(unEscapeHtml($this.attr("value"))).trigger("change");
                    }
                });
            });
        } ,
        combox: function() {
            /* 清理下拉层 */
            var _selectBox = [ ];
            $.each(allSelectBox, function(i) {
                if ($("#" + allSelectBox[i])[0] ) {
                    _selectBox.push(allSelectBox[i]);
                } else {
                    $("#op_" + allSelectBox[i]).remove();
                }
            });
            allSelectBox = _selectBox;
            return this.each(function(i) {
                var $this = $(this).removeClass("combox");
                var $thisCombox = $this.parents("div.combox:first");
                if($thisCombox.length){
                    $this.insertAfter($thisCombox);
                    $thisCombox.remove();
                }
                if($this.hasClass("requiredIfNotEmpty") ){
                    if(0 == $("option", $this).length || 1 == $("option", $this).length && !$("option", $this).val()){
                        $this.removeClass("required");
                    }else{
                        $this.addClass("required")
                    }
                }
                var name = $this.attr("name");
                var value = $this.val();
                var label = "";
                if( "undefined" !== typeof value  && value){
                    label = $("option[value=" + $.escapeSelector(value) + "]", $this).text();
                }else if("" == value){
                    label = $("option[value=\"\"]", $this).text();
                }
                var ref = $this.attr("ref");
                var refUrl = $this.attr("refUrl") || "";
                var cid = $this.attr("id") || Math.round(Math.random() * 10000000);
                var select = "<div class=\"combox\"><div id=\"combox_" + cid + "\" class=\"select\"" + ( ref ? " ref=\"" + escapeHtml(ref) + "\"": "" ) + ">";
                select += "<a href=\"javascript:\" class=\"" + $this.attr("class") + "\" name=\"" + name + "\" value=\"" + escapeHtml(value) + "\">" + label + "</a></div></div>";
                var options = "<div class=\"comboxop\" id=\"op_combox_" + cid + "\"><div class=\"search\"><input type=\"text\" class=\"textInput\"/></div><ul>";
                $("option", $this).each(function() {
                    var option = $(this);
                    options += "<li><a class=\"" + ( value == option[0].value ? "selected": "" ) + "\" href=\"#\" title=\"" + escapeHtml(option[0].text) + "\" value=\"" + escapeHtml(option[0].value) + "\">" + escapeHtml(option[0].text)
                            + "</a></li>";
                });
                options += "</ul></div>";
                $("body").append(options);
                $this.after(select);
                $("div.select", $this.next()).comboxSelect().append($this);
                if (ref && refUrl ) {
                    $this.off("change", null, _onchange).on("change", null, {
                        $this: $this
                    }, _onchange);
                }
            });
        },
        comboxDisable: function(){
          return this.each(function(){
            $(this).parents(".combox .select:first").addClass("disabled");
          });
        },
        comboxEnable: function(){
          return this.each(function(){
            $(this).parents(".combox .select:first").removeClass("disabled");
          });
        },
        comboxVal: function(val){
          return this.each(function(){
            var $box = $(this).parents(".combox .select:first");
            $("#op_"+$box.attr("id")).find("li a[value=\""+$.escapeSelector(val)+"\"]").trigger("click");
          });
        }
    });
} )(jQuery);
/**
 * @author ZhangHuihua@msn.com
 */
( function($) {
    $.fn.extend({
        checkboxCtrl: function(parent) {
            return this.each(function() {
                var $trigger = $(this);
                $trigger.click(function() {
                    var group = $trigger.attr("group");
                    if ($trigger.is(":checkbox") ) {
                        var type = $trigger.is(":checked") ? "all": "none";
                        if (group ) {
                            $.checkbox.select(group, type, parent);
                        }
                    } else {
                        if (group ) {
                            $.checkbox.select(group, $trigger.attr("selectType") || "all", parent);
                        }
                    }
                });
            });
        }
    });
    $.checkbox = {
        selectAll: function(_name, _parent) {
            this.select(_name, "all", _parent);
        },
        unSelectAll: function(_name, _parent) {
            this.select(_name, "none", _parent);
        },
        selectInvert: function(_name, _parent) {
            this.select(_name, "invert", _parent);
        },
        select: function(_name, _type, _parent) {
            $parent = $(_parent || document);
            $checkboxLi = $parent.find(":checkbox[name=\"" + _name + "\"]");
            switch (_type) {
                case "invert":
                    $checkboxLi.each(function() {
                        $checkbox = $(this);
                        $checkbox.prop("checked", !$checkbox.is(":checked"));
                    });
                    break;
                case "none":
                    $checkboxLi.prop("checked", false);
                    break;
                default :
                    $checkboxLi.prop("checked", true);
                    break;
            }
        }
    };
} )(jQuery);

/**
 * @requires jquery.validate.js
 * @author ZhangHuihua@msn.com
 */
( function($) {
    if ($.validator ) {
        $.validator.addMethod("alphanumeric", function(value, element) {
            return this.optional(element) || /^\w+$/i.test(value);
        }, "Letters, numbers or underscores only please");
        $.validator.addMethod("nocommas", function(value, element) {
            return this.optional(element) || /^[^,]+$/i.test(value);
        }, "Cannot contain commas");
        $.validator.addMethod("lettersonly", function(value, element) {
            return this.optional(element) || /^[a-z]+$/i.test(value);
        }, "Letters only please");
        $.validator.addMethod("letterstart", function(value, element) {
            return this.optional(element) || /^[a-z]+\w+$/i.test(value);
        }, "Letters, numbers or underscores only  please,The first character must be letter");
        $.validator.addMethod("phone", function(value, element) {
            return this.optional(element) || /^[0-9 \(\)]{7,30}$/.test(value);
        }, "Please specify a valid phone number");
        $.validator.addMethod("postcode", function(value, element) {
            return this.optional(element) || /^[0-9 A-Za-z]{5,20}$/.test(value);
        }, "Please specify a valid postcode");
        $.validator.addMethod("domain", function(value, element) {
            return this.optional(element) || /^((((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|(\[?((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:)))(%.+)?\]?)|(localhost)|(loopback)|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)*)?$/i.test(value);
        }, "Please specify a valid domain");
        $.validator.addMethod("date", function(value, element) {
            value = value.replace(/\s+/g, "");
            if (String.prototype.parseDate ) {
                var $input = $(element);
                var pattern = $input.attr("dateFmt") || "yyyy-MM-dd";
                return !$input.val() || $input.val().parseDate(pattern);
            } else {
                return this.optional(element) || value.match(/^\d{4}[\/-]\d{1,2}[\/-]\d{1,2}$/);
            }
        }, "Please enter a valid date.");

        /*
         * 自定义js函数验证 <input type="text" name="xxx" customvalid="xxxFn(element)"
         * title="xxx" />
         */
        $.validator.addMethod("customvalid", function(value, element, params) {
            try {
                return eval("(" + params + ")");
            } catch (e) {
                return false;
            }
        }, "Please fix this field.");
        $.validator.addClassRules({
            date: {
                date: true
            }, alphanumeric: {
                alphanumeric: true
            }, lettersonly: {
                lettersonly: true
            }, phone: {
                phone: true
            }, postcode: {
                postcode: true
            }
        });
        $.validator.setDefaults({
            errorElement: "span"
        });
        $.validator.autoCreateRanges = true;
    }

} )(jQuery);