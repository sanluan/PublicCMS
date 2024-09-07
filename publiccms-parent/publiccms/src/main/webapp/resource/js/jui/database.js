/**
 * @author ZhangHuihua@msn.com
 */
( function($) {
    var _lookup = {
        currentGroup: "", suffix: "", $target: null, pk: "id", nextButton: null
    };
    var _util = {
        _lookupPrefix: function(key) {
            var strDot = _lookup.currentGroup ? ".": "";
            return _lookup.currentGroup + strDot + key;
        }, lookupPk: function(key) {
            return this._lookupPrefix(key);
        }, lookupSuffix: function(){
            return _lookup.suffix;
        }
    };
    function suggest(args,key,$input){
        if ("" === _util.lookupSuffix() || $input.attr("suffix") === _util.lookupSuffix()) {
            if($input.hasClass("editor")){
                if("ckeditor"==$input.attr("editorType")) {
                    CKEDITOR.instances[$input.data("id")].setData(args[key]);
                } else if ("tinymce"==$input.attr("editorType")){
                    tinymce.get($input.data("id")).setContent(args[key]);
                } else {
                    UE.instants[$input.data("id")].setContent(args[key]);
                }
            } else if($input.hasClass("code")) {
                JUI.instances[$input.data("id")].setValue(args[key]);
            } else {
                $input.val(args[key]).trigger("change");
            }
        }
    }
    $.extend({
        bringBackSuggest: function(args,keys) {
            var $box = _lookup["$target"].parents(".unitBox").first();
            if(keys){
                $.each(keys,function(n,key){
                    $box.find(":input[name="+$.escapeSelector(_util.lookupPk(key))+"]").each(function() {
                        for ( var k in args) {
                            _lookup.suffix=k;
                            suggest(args,k,$(this));
                        }
                    });
                });
            } else {
                for ( var key in args) {
                    $box.find(":input[name="+$.escapeSelector(_util.lookupPk(key))+"]").each(function() {
                        suggest(args,key,$(this));
                    });
                }
            }
        },
        bringBack: function(json) {
            if (json[JUI.keys.statusCode] == JUI.statusCode.error ) {
                if (json[JUI.keys.message] ) {
                    alertMsg.error(json[JUI.keys.message]);
                }
            } else if (json[JUI.keys.statusCode] == JUI.statusCode.timeout ) {
                alertMsg.error(json[JUI.keys.message] || JUI.msg("sessionTimout"), {
                    okCall: function() {
                        JUI.loadLogin();
                    }
                });
            } else {
                $.bringBackSuggest(json);
                $.pdialog.closeCurrent();
            }
        },
        batchBringBack: function(args,keys) {
            if(_lookup.nextButton ) {
                if (args[JUI.keys.statusCode] == JUI.statusCode.timeout ) {
                    alertMsg.error(args[JUI.keys.message] || JUI.msg("sessionTimout"), {
                        okCall: function() {
                            JUI.loadLogin();
                        }
                    });
                } else {
                    for ( var row in args) {
                        _lookup.nextButton.trigger("click");
                        if (args[row][JUI.keys.statusCode] == JUI.statusCode.error ) {
                            if (args[row][JUI.keys.message] ) {
                                alertMsg.error(args[row][JUI.keys.message]);
                            }
                            break;
                        } else {
                            $.bringBackSuggest(args[row],keys);
                        }
                    }
                }
                _lookup.nextButton = null;
            }
            $.pdialog.closeCurrent();
        }
    });
    $.fn.extend({
        lookup: function() {
            return this.each(function() {
                var $this = $(this), options = {
                    mask: true, width: $this.attr("width") || 820, height: $this.attr("height") || 500, maxable: eval($this.attr("maxable") || "true") ,
                    resizable: eval($this.attr("resizable") || "true")
                };
                $this.on("click", function(event) {
                    _lookup = $.extend(_lookup, {
                        currentGroup: $this.attr("lookupGroup") || "", suffix: $this.attr("suffix") || "", $target: $this, pk: $this.attr("lookupPk") || ""
                    });
                    var url = $this.attr("href").replaceTmById($(event.target).parents(".unitBox").first());
                    if (!url.isFinishedTm() ) {
                        alertMsg.error($this.attr("warn") || JUI.msg("alertSelectMsg"));
                        return false;
                    }
                    $.pdialog.open(url, "_blank", $this.attr("title") || $this.text(), options);
                    return false;
                });
            });
        } ,
        multLookup: function() {
            return this.each(function() {
                var $this = $(this), args = {};
                $this.on("click", function(event) {
                    var $unitBox = $this.parents(".unitBox").first();
                    $unitBox.find("[name=\"" + $this.attr("multLookup") + "\"]").filter(":checked").each(function() {
                        var _args = JUI.jsonEval($(this).val());
                        for ( var key in _args) {
                            var value = args[key] ? args[key] + ",": "";
                            args[key] = value + _args[key];
                        }
                    });
                    if ($.isEmptyObject(args) ) {
                        alertMsg.error($this.attr("warn") || JUI.msg("alertSelectMsg"));
                        return false;
                    }
                    $.bringBack(args);
                    return false;
                });
            });
        } ,
        suggest: function() {
            var op = {
                suggest$: "#suggest", suggestShadow$: "#suggestShadow"
            };
            var selectedIndex = -1;
            return this.each(function() {
                var $input = $(this).attr("autocomplete", "off").keydown(function(event) {
                    if (event.keyCode == JUI.keyCode.ENTER && $(op.suggest$).is(":visible") ) {
                        return false; // 屏蔽回车提交
                    }
                });
                var suggestFields = $input.attr("suggestFields").split(",");
                var callbackFields;
                if($input.attr("callbackFields")){
                    callbackFields = $input.attr("callbackFields").split(",");
                }
                function _show(event) {
                    var offset = $input.offset();
                    var iTop = offset.top + this.offsetHeight;
                    var $suggest = $(op.suggest$);
                    if ($suggest.length == 0 ) {
                        $suggest = $("<div id=\"suggest\"></div>").appendTo($("body"));
                    }
                    $suggest.css({
                        left: offset.left + "px", top: iTop + "px"
                    }).show();
                    _lookup = $.extend(_lookup, {
                        currentGroup: $input.attr("lookupGroup") || "", suffix: $input.attr("suffix") || "", $target: $input, pk: $input.attr("lookupPk") || ""
                    });
                    var url = $input.attr("suggestUrl").replaceTmById($(event.target).parents(".unitBox").first());
                    if (!url.isFinishedTm() ) {
                        alertMsg.error($input.attr("warn") || JUI.msg("alertSelectMsg"));
                        return false;
                    }
                    var postData = {};
                    postData[$input.attr("postField") || "inputValue"] = $input.val();
                    $.ajax({
                        global: false, type: "POST", dataType: "json", url: url, cache: false, data: postData, success: function(response) {
                            if (!response ) {
                                return;
                            }
                            var html = "";
                            $.each(response, function(i) {
                                var liAttr = "", liLabel = "";
                                for (var i = 0; i < suggestFields.length; i++) {
                                    var str = this[suggestFields[i]];
                                    if (str ) {
                                        if (liLabel ) {
                                            liLabel += "-";
                                        }
                                        liLabel += str;
                                    }
                                }
                                for ( var key in this) {
                                    if (liAttr ) {
                                        liAttr += ",";
                                    }
                                    liAttr += "'" + key + "':'" + this[key] + "'";
                                }
                                html += "<li lookupAttrs=\"" + liAttr + "\">" + liLabel + "</li>";
                            });
                            var $lis = $suggest.html("<ul>" + html + "</ul>").find("li");
                            $lis.on("click", function() {
                                _select($(this),callbackFields);
                                if($input.next().hasClass("suggestButton")){
                                    $input.next().trigger("click");
                                }
                            });
                            if ($lis.length == 0 ) {
                                $suggest.hide();
                                var jsonStr = "";
                                for (var i = 0; i < suggestFields.length; i++) {
                                    if (_util.lookupPk(suggestFields[i]) == event.target.name ) {
                                        break;
                                    }
                                    if (jsonStr ) {
                                        jsonStr += ",";
                                    }
                                    jsonStr += "'" + suggestFields[i] + "':''";
                                }
                                jsonStr = "{'" + _lookup.pk + "':''," + jsonStr + "}";
                                $.bringBackSuggest(JUI.jsonEval(jsonStr),callbackFields);
                            }
                        }, error: function() {
                            $suggest.html("");
                        }
                    });
                    $(document).on("click", null, null , _close);
                    return false;
                }
                function _select($item,callbackFields) {
                    var jsonStr = "{" + $item.attr("lookupAttrs") + "}";
                    $.bringBackSuggest(JUI.jsonEval(jsonStr),callbackFields);
                }
                function _close() {
                    $(op.suggest$).html("").hide();
                    selectedIndex = -1;
                    $(document).off("click", null, _close);
                }
                $input.focus(_show).on("click", false).keyup(function(event) {
                    var $items = $(op.suggest$).find("li");
                    switch (event.keyCode) {
                        case JUI.keyCode.ESC:
                        case JUI.keyCode.TAB:
                        case JUI.keyCode.SHIFT:
                        case JUI.keyCode.HOME:
                        case JUI.keyCode.END:
                        case JUI.keyCode.LEFT:
                        case JUI.keyCode.RIGHT:
                            break;
                        case JUI.keyCode.ENTER:
                            _close();
                            break;
                        case JUI.keyCode.DOWN:
                            if (selectedIndex >= $items.length - 1 ) {
                                selectedIndex = -1;
                            } else {
                                selectedIndex++;
                            }
                            break;
                        case JUI.keyCode.UP:
                            if (selectedIndex < 0 ) {
                                selectedIndex = $items.length - 1;
                            } else {
                                selectedIndex--;
                            }
                            break;
                        default :
                            _show(event);
                    }
                    $items.removeClass("selected");
                    if (selectedIndex >= 0 ) {
                        var $item = $items.eq(selectedIndex).addClass("selected");
                        _select($item,callbackFields);
                    }
                });
            });
        } ,
        itemDetail: function() {
            return this.each(function() {
                var $table = $(this).css("clear", "both"), $tbody = $table.find("tbody");
                var fields = [ ];
                $table.find("thead th[type]").each(function(i) {
                    var $th = $(this);
                    var field = {
                        type: $th.attr("type") || "text", patternDate: $th.attr("dateFmt") || "yyyy-MM-dd", name: $th.attr("name") || "" ,suffix: $th.attr("suffix")||"",
                        defaultVal: escapeHtml($th.attr("defaultVal") || ""), size: $th.attr("size") || "12", enumUrl: $th.attr("enumUrl") || "" ,
                        lookupGroup: $th.attr("lookupGroup") || "", lookupUrl: $th.attr("lookupUrl") || "", lookupPk: $th.attr("lookupPk") || "id" ,
                        suggestUrl: $th.attr("suggestUrl"), suggestFields: $th.attr("suggestFields"), postField: $th.attr("postField") || "" ,
                        fieldClass: $th.attr("fieldClass") || "", fieldAttrs: $th.attr("fieldAttrs") || "", title:$th.text(), pkValue: $th.attr("pkValue")
                    };
                    fields.push(field);
                });
                $tbody.find("a.btnDel").on("click", function() {
                    var $btnDel = $(this);
                    if ($btnDel.is("[href^=javascript]") ) {
                        $btnDel.parents("tr").first().remove();
                        initSuffix($tbody);
                        return false;
                    }
                    function delDbData() {
                        $.ajax({
                            type: "POST", dataType: "json", url: $btnDel.attr("href"), cache: false, success: function() {
                                $btnDel.parents("tr").first().remove();
                                initSuffix($tbody);
                            }, error: JUI.ajaxError
                        });
                    }
                    if ($btnDel.attr("title") ) {
                        alertMsg.confirm($btnDel.attr("title"), {
                            okCall: delDbData
                        });
                    } else {
                        delDbData();
                    }
                    return false;
                });
                var addButTxt = $table.attr("addButton") || "Add New";
                if (addButTxt ) {
                    var $caption = $("<caption></caption>").appendTo($table);
                    var $addBut = $("<label style=\"width:auto;\"><button type=\"button\" class=\"button\">" + addButTxt + "</button></label>").appendTo($caption).find("button");
                    var batchButtonTxt = $table.attr("batchButton") || "Batch upload";
                    var batchUrl = $table.attr("batchUrl");
                    if(batchUrl){
                        var $batchButton = $("<label><a class=\"button\" lookupGroup=\"\" href=\""+batchUrl+"\" width=\"1000\" height=\"600\" >"+batchButtonTxt+"</a></label>").initUI().appendTo($caption).find("a");
                        $batchButton.on("click", function(){
                            _lookup = $.extend(_lookup, {
                                nextButton : $addBut
                            });
                        });
                    } else {
                        var $rowNum = $("<label><input type=\"text\" name=\"dwz_rowNum\" class=\"textInput\" value=\"1\" size=\"2\"/></label>").appendTo($caption).find("input");
                    }

                    var trTm = "";
                    $addBut.on("click", function() {
                        if (!trTm ) {
                            trTm = trHtml(fields);
                        }
                        var rowNum = 1;
                        if(!batchUrl ){
                            try {
                                rowNum = parseInt($rowNum.val());
                            } catch (e) {}
                        }
                        for (var i = 0; i < rowNum; i++) {
                            var $tr = $($.parseHTML(trTm, document, true));
                            $tr.appendTo($tbody).initUI().find("a.btnDel").on("click", function() {
                                $(this).parents("tr").first().remove();
                                initSuffix($tbody);
                                return false;
                            });
                            $tr.on("click", function(){
                                $tbody.find(">tr.selected").removeClass("selected");
                                $tr.addClass("selected");
                            });
                        }
                        initSuffix($tbody);
                        var $attach = $tr.find(".btnAttach");
                        if( $attach.length ){
                            _lookup = $.extend(_lookup, {
                                currentGroup: $attach.attr("lookupGroup") || "", suffix: $attach.attr("suffix") || "", $target: $attach, pk: $attach.attr("lookupPk") || ""
                            });
                        } else if(_lookup.nextButton){
                            var $batch = $tr.find("[batchGroup]");
                            if($batch.length){
                                 _lookup = $.extend(_lookup, {
                                    currentGroup: $batch.attr("batchGroup") || "", suffix: $batch.attr("suffix") || "", $target: $batch, pk: $batch.attr("lookupPk") || ""
                                });
                            }
                        }
                    });
                }
            });

            /**
             * 删除时重新初始化下标
             */
            function initSuffix($tbody) {
                $tbody.find(">tr").each(function(i) {
                    $(":input, a.btnLook, a.btnAttach", this).each(function() {
                        var $this = $(this), name = $this.attr("name"), val = $this.val();
                        if (name ) {
                            $this.attr("name", name.replaceSuffix(i));
                        }
                        var lookupGroup = $this.attr("lookupGroup");
                        if (lookupGroup ) {
                            $this.attr("lookupGroup", lookupGroup.replaceSuffix(i));
                        }
                        var batchGroup = $this.attr("batchGroup");
                        if (batchGroup ) {
                            $this.attr("batchGroup", batchGroup.replaceSuffix(i));
                        }
                        var suffix = $this.attr("suffix");
                        if (suffix ) {
                            $this.attr("suffix", suffix.replaceSuffix(i));
                        }
                        if (val && val.indexOf("#index#") >= 0 ) {
                            $this.val(val.replace("#index#", i + 1));
                        }
                    });
                });
            }
            function tdHtml(field) {
                var html = "";
                var suffixFrag = field.suffix ? " suffix=\"" + field.suffix + "\"": "";
                var attrFrag = "";
                if (field.fieldAttrs ) {
                    var attrs = JUI.jsonEval(field.fieldAttrs);
                    for ( var key in attrs) {
                        attrFrag += key + "=\"" + attrs[key] + "\" ";
                    }
                }
                switch (field.type) {
                    case "del":
                        html = "<a href=\"javascript:void(0)\" class=\"btnDel " + field.fieldClass + "\"></a>";
                        break;
                    case "lookup":
                        var suggestFrag = "";
                        if (field.suggestFields ) {
                            suggestFrag = "autocomplete=\"off\" lookupGroup=\"" + field.lookupGroup + "\"" + suffixFrag + " lookupPk=\"" + field.lookupPk + "\" suggestUrl=\"" + field.suggestUrl + "\" suggestFields=\""
                                    + field.suggestFields + "\"" + " postField=\"" + field.postField + "\"";
                        }
                        if (field.lookupPk) {
                            var strDot = field.lookupGroup ? ".": "";
                            html +=  "<input type=\"hidden\" name=\""+field.lookupGroup + strDot + field.lookupPk+"\"" + suffixFrag + " value=\"" + field.defaultVal + "\"/>";
                        }
                        html +=  "<input type=\"text\" name=\"" + field.name + "\" " + suggestFrag + suffixFrag + " size=\"" + field.size + "\" class=\"" + field.fieldClass + "\" " + attrFrag + "/>"
                            + "<a class=\"btnLook\" href=\"" + field.lookupUrl + "\" lookupGroup=\"" + field.lookupGroup + "\"" + suffixFrag + " lookupPk=\"" + field.lookupPk + "\" " + attrFrag + ">"+field.title+"</a>";
                        break;
                    case "attach":
                        html =  "<input type=\"text\" name=\"" + field.name + "\" size=\"" + field.size + "\" class=\"" + field.fieldClass + "\" " + attrFrag + "/>"
                                 + "<a class=\"btnAttach\" href=\"" + field.lookupUrl + "\" lookupGroup=\"" + field.lookupGroup + "\"" + suffixFrag + " lookupPk=\"" + field.lookupPk + "\" width=\"1000\" height=\"600\" " + attrFrag + ">"+field.title+"</a>";
                        break;
                    case "enum":
                        $.ajax({
                            type: "POST", dataType: "html", async: false, url: field.enumUrl, data: {
                                inputName: field.name,
                                lookupPk: field.lookupPk,
                                pkValue: field.pkValue
                            }, success: function(response) {
                                html = response;
                            }
                        });
                        break;
                    case "date":
                        html = "<input type=\"text\" name=\"" + field.name + "\" value=\"" + field.defaultVal + "\" class=\"date " + field.fieldClass + "\" dateFmt=\"" + field.patternDate
                                + "\" size=\"" + field.size + "\" " + attrFrag + "/>";
                        break;
                    case "checkbox":
                        html = "<input type=\"checkbox\" name=\"" + field.name + "\" class=\"" + field.fieldClass + "\" " + attrFrag + "/>";
                        break;
                    case "textarea":
                        html = "<textarea name=\"" + field.name + "\" class=\"" + field.fieldClass + "\" " + attrFrag + ">" + field.defaultVal + "</textarea>";
                        break;
                    case "number":
                        html = "<input type=\"number\" name=\"" + field.name + "\" value=\"" + field.defaultVal + "\"" + suffixFrag + " size=\"" + field.size + "\" class=\"" + field.fieldClass + "\" "
                        + attrFrag + "/>";
                        break;
                    default :
                        html = "<input type=\"text\" name=\"" + field.name + "\" value=\"" + field.defaultVal + "\"" + suffixFrag + " size=\"" + field.size + "\" class=\"" + field.fieldClass + "\" "
                                + attrFrag + "/>";
                        break;
                }
                return "<td>" + html + "</td>";
            }
            function trHtml(fields) {
                var html = "";
                $(fields).each(function() {
                    html += tdHtml(this);
                });
                return "<tr class=\"unitBox\">" + html + "</tr>";
            }
        },
        selectedTodo: function() {
            function _getIds(selectedIds, targetType) {
                var ids = "";
                var $box = targetType == "dialog" ? $.pdialog.getCurrent(): navTab.getCurrentPanel();
                $box.find("input:checked").filter("[name=\"" + selectedIds + "\"]").each(function(i) {
                    var val = $(this).val();
                    ids += i == 0 ? val: "," + val;
                });
                return ids;
            }
            return this.each(function() {
                var $this = $(this);
                var selectedIds = $this.attr("rel") || "ids";
                var postType = $this.attr("postType") || "map";
                $this.on("click", function() {
                    var targetType = $this.attr("targetType");
                    var ids = _getIds(selectedIds, targetType);
                    if (!ids ) {
                        alertMsg.error($this.attr("warn") || JUI.msg("alertSelectMsg"));
                        return false;
                    }
                    var _callback = $this.attr("callback") || ( targetType == "dialog" ? dialogAjaxDone: navTabAjaxDone );
                    if ("function" !== typeof _callback ) {
                        _callback = eval("(" + _callback + ")");
                    }
                    function _doPost() {
                        $.ajax({
                            type: "POST", url: $this.attr("href"), dataType: "json", cache: false, data: function() {
                                if (postType == "map" ) {
                                    return $.map(ids.split(","), function(val, i) {
                                        return {
                                            name: selectedIds, value: val
                                        };
                                    })
                                } else {
                                    var _data = {};
                                    _data[selectedIds] = ids;
                                    return _data;
                                }
                            }(), success: _callback, error: JUI.ajaxError
                        });
                    }
                    var title = $this.attr("title");
                    if (title ) {
                        alertMsg.confirm(title, {
                            okCall: _doPost
                        });
                    } else {
                        _doPost();
                    }
                    return false;
                });
            });
        }
    });
} )(jQuery);