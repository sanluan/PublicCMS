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
                $('#'+ allSelectBox[i]).children('a').removeClass('expand');
                $(document).off("click", null, killAllBox);
            }
        });
    };
    var _onchange = function(event) {
        var $ref = $("select[name=" + escapeJquery(event.data.$this.attr('ref'))+"]");
        if ($ref.length == 0 ) {
            return false;
        }
        if(event.data.$this.attr('index')){
            $ref=$ref.eq(event.data.$this.attr('index'));
        }
        $.ajax({
            type: 'POST', dataType: "json", url: event.data.$this.attr('refUrl').replace("{value}", encodeURIComponent(event.data.$this.val())), cache: false, data: {} ,
            success: function(json) {
                if (!json ) {
                    return;
                }
                $ref.empty();
                $.each(json, function(i) {
                    if (json[i] && json[i].length > 1 ) {
                        $('<option></option>').attr('value',json[i][0]).text(json[i][1]).appendTo($ref);
                    }
                });
                var $refCombox = $ref.parents("div.combox:first");
                $ref.insertAfter($refCombox);
                $refCombox.remove();
                $ref.trigger("change").combox();
            }, error: DWZ.ajaxError
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
                    var optionlist=$('ul',options);
                    if (options.is(":hidden") ) {
                        box.children('a').addClass('expand');
                        if (options.height() > 600 ) {
                            options.addClass('lot');
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
                        options.css({
                            top: top, left: box.offset().left
                        }).show();
                        if($('a.selected',options).length && 0 == optionlist.scrollTop()){
                            optionlist.stop().animate({scrollTop:$('a.selected',optionlist).offset().top + 60 - options.height() -optionlist.offset().top},500);
                        }
                        $('.search input',options).focus().keyup(function(){
                            var val = $(this).val();
                            if(val){
                                $('li',optionlist).hide();
                                $('li a:contains('+escapeJquery(val)+')',optionlist).parent().show();
                                $('li a',optionlist).each(function(){
                                    if(-1 < $(this).attr('value').indexOf(val)){
                                        $(this).parent().show();
                                    }
                                });
                            }else{
                                $('li',optionlist).show();
                            }
                            $('li.disabled',optionlist).hide();
                        }).click(function(){
                            return false;
                        }).val('');
                        $('li',optionlist).show();
                        $('li.disabled',optionlist).hide();
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
                    if ($input.val() != $this.attr("value") ) {
                        $("select", box).val($this.attr("value")).trigger("change");
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
                var name = $this.attr("name");
                var value = $this.val();
                var label = '';
                if( "undefined" !== typeof value  && value){
                    label = $("option[value=" + escapeJquery(value) + "]", $this).text();
                }else if("" == value){
                    label = $("option[value='']", $this).text();
                }
                var ref = $this.attr("ref");
                var refUrl = $this.attr("refUrl") || "";
                var cid = $this.attr("id") || Math.round(Math.random() * 10000000);
                var select = '<div class="combox"><div id="combox_' + cid + '" class="select"' + ( ref ? ' ref="' + ref + '"': '' ) + '>';
                select += '<a href="javascript:" class="' + $this.attr("class") + '" name="' + name + '" value="' + escapeHtml(value) + '">' + label + '</a></div></div>';
                var options = '<div class="comboxop" id="op_combox_' + cid + '"><div class="search"><input type="text" class="textInput"/></div><ul>';
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
        }
    });
} )(jQuery);