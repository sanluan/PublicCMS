JUI.regPlugins.push(function($p){
    $(".searchBar .searchContent", $p).click(function(event){
        if($(event.target).is("ul")||$(event.target).is("li")){
            $content = $(this).closest(".pageHeader").next().find("[layouth]");
            var height = $(this).height();
            $(this).toggleClass("searchContentHover");
            $content.height($content.height()-$(this).height()+height);
        }
    });
});
JUI.regPlugins.push(function($p){
    $(".tagsBox",$p).each(function (){
        var $tagBox = $(this);
        var $callback = $tagBox.attr("callback");
        if ($callback && !$.isFunction($callback) ) {
            $callback = eval("(" + $callback + ")");
        }
        $tagBox.on("click","span>a",function(){
            $(this).parent().remove();
            if ($callback) {
                $callback();
            }
            return false;
        });
    });
});
JUI.regPlugins.push(function($p){
    if($(".cmsVersion",$p).length ) {
        var fullVersion=$(".cmsVersion a",$p).eq(0).text();
        $.ajax({
            url: Base64.decode("Ly9jbXMucHVibGljY21zLmNvbS9hcGkvZGlyZWN0aXZlL3ZlcnNpb24=")+"?version="+fullVersion,
            global: false,
            dataType: "json",
            success: function(data) {
                var version=fullVersion.substring(0,fullVersion.lastIndexOf("."));
                var databaseVersion=version.substring(version.lastIndexOf(".")+1);
                var revision=fullVersion.substring(fullVersion.lastIndexOf(".")+1);
                var remoteDatabaseVersion = data.cms.substring(data.cms.lastIndexOf(".")+1);
                if(databaseVersion !== remoteDatabaseVersion ) {
                    $(".cmsVersion .old",$p).show();
                } else {
                    if(revision == data.revision){
                        $(".cmsVersion .new",$p).show();
                    } else {
                        $(".cmsVersion .old",$p).css("color","gray").show();
                    }
                }
            }
        });
    }
});
JUI.regPlugins.push(function($p){
    $("textarea.editor", $p).each(function(i) {
        var $this = $(this);
        var index= window.editor.index++;
        var dataId="editor_"+index;
        $this.attr("id",dataId);
        $this.attr("data-id",dataId);
        if("ckeditor"==$this.attr("editorType")) {
            if(window.editor.ckeditorInitd){
                CKEDITOR.replace(dataId);
            } else {
                if(window.editor.ckeditorIniting){
                    window.editor.ckeditorArray.push(dataId);
                } else {
                    window.editor.ckeditorIniting=true;
                    loadScripts(window.editor.ckeditorResources,function(){
                        window.editor.ckeditorIniting=false;
                        window.editor.ckeditorInitd=true;
                        CKEDITOR.replace(dataId);
                        if(0 < window.editor.ckeditorArray.length){
                            for(var i=0;i<window.editor.ckeditorArray.length;i++){
                                CKEDITOR.replace(window.editor.ckeditorArray[i]);
                            }
                        }
                    });
                }
            }
        } else if("tinymce"==$this.attr("editorType")) {
            if(window.editor.tinymceInitd){
                tinymce.init($.extend(true, {selector:"#"+dataId}, window.TINYMCE_OPTIONS));
            } else {
                if(window.editor.tinymceIniting){
                    window.editor.tinymceArray.push(dataId);
                } else {
                    window.editor.tinymceIniting=true;
                    loadScripts(window.editor.tinymceResources,function(){
                        window.editor.tinymceIniting=false;
                        window.editor.tinymceInitd=true;
                        tinymce.init($.extend(true, {selector:"#"+dataId}, window.TINYMCE_OPTIONS));
                        if(0 < window.editor.tinymceArray.length){
                            for(var i=0;i<window.editor.tinymceArray.length;i++){
                                tinymce.init($.extend(true, {selector:"#"+window.editor.tinymceArray[i]}, window.TINYMCE_OPTIONS));
                            }
                        }
                    });
                }
            }
        } else if("kindeditor"==$this.attr("editorType")) {
            if(window.editor.kindeditorInitd){
                KindEditor.create("#"+dataId,window.KINDEDITOR_OPTIONS);
            } else {
                if(window.editor.kindeditorIniting){
                    window.editor.kindeditorArray.push(dataId);
                } else {
                    window.editor.kindeditorIniting=true;
                    loadScripts(window.editor.kindeditorResources,function(){
                        window.editor.kindeditorIniting=false;
                        window.editor.kindeditorInitd=true;
                        KindEditor.create("#"+dataId,window.KINDEDITOR_OPTIONS);
                        if(0 < window.editor.kindeditorArray.length){
                            for(var i=0;i<window.editor.kindeditorArray.length;i++){
                                KindEditor.create("#"+window.editor.kindeditorArray[i],window.KINDEDITOR_OPTIONS);
                            }
                        }
                    });
                }
            }
        } else {
            if(window.editor.ueditorInitd){
                var editor = new baidu.editor.ui.Editor();
                if ($this.attr("maxlength") ){
                    editor.setOpt({
                        maximumWords: $this.attr("maxlength")
                    });
                }
                editor.render($this[0]);
                $this.attr("data-id","ueditorInstant"+editor.uid);
            } else {
                if(window.editor.ueditorIniting){
                    window.editor.ueditorArray.push(dataId);
                } else {
                    window.editor.ueditorIniting=true;
                    loadScripts(window.editor.ueditorResources,function(){
                        window.editor.ueditorIniting=false;
                        window.editor.ueditorInitd=true;
                        var editor = new baidu.editor.ui.Editor();
                        if ($this.attr("maxlength") ){
                            editor.setOpt({
                                maximumWords: $this.attr("maxlength")
                            });
                        }
                        editor.render($this[0]);
                        $this.attr("data-id","ueditorInstant"+editor.uid);
                        if(0 < window.editor.ueditorArray.length){
                            for(var i=0;i<window.editor.ueditorArray.length;i++){
                                var editor = new baidu.editor.ui.Editor();
                                editor.render($("#"+window.editor.ueditorArray[i])[0]);
                                $("#"+window.editor.ueditorArray[i]).attr("data-id","ueditorInstant"+editor.uid);
                            }
                        }
                    });
                }
            }
        }
    });
    $("textarea.code", $p).each(function() {
        var $this = $(this);
        var index= window.codemirror.index++;
        var dataId="editor_"+index;
        var mode = "htmlmixed"
        if($(this).attr("mode")){
            mode = $(this).attr("mode");
        }
        if(!window.codemirror.initd){
            loadScripts(window.codemirror.resources,function(){
                window.codemirror.initd=true;
                JUI.instances[dataId]=CodeMirror.fromTextArea($this[0], {
                    mode: mode,
                    lineNumbers: true,
                    tabSize        : 4,
                    indentUnit     : 4,
                    lineWrapping   : true,
                    indentWithTabs : true,
                    extraKeys: { "Ctrl": "autocomplete" }
                });
                if($this.prop("readonly")){
                    JUI.instances[dataId].setOption("readOnly",true);
                }
                $this.attr("data-id",dataId);
            });
        } else {
            JUI.instances[dataId]=CodeMirror.fromTextArea($this[0], {
                mode: mode,
                lineNumbers: true,
                tabSize        : 4,
                indentUnit     : 4,
                lineWrapping   : true,
                indentWithTabs : true,
                extraKeys: { "Ctrl": "autocomplete" }
            });
            if($this.prop("readonly")){
                JUI.instances[dataId].setOption("readOnly",true);
            }
            $this.attr("data-id",dataId);
        }

    });
});

JUI.regPlugins.push(function($p){
    $("a.view[target=_blank]", $p).each(function(){
        $btn=$(this);
        $input=$("input[name="+escapeJquery($(this).attr("ref"))+"]",$(this).parents(".unitBox:first"));
        function control(){
            if($(this).val()){
                $("a.view[target=_blank][ref="+escapeJquery($(this).attr("name"))+"]").show();
            }else{
                $("a.view[target=_blank][ref="+escapeJquery($(this).attr("name"))+"]").hide();
            }
        }
        $input.change(control);
        if($input.val()){
            $btn.show();
        }else{
            $btn.hide();
        }
        $btn.click(function(){
            var value=$("input[name="+escapeJquery($(this).attr("ref"))+"]",$(this).parents(".unitBox:first")).val();
            if(value){
                $(this).attr("href",value.isUrl() ? value : $(this).data("prefix")+value);
            }else{
                return false;
            }
        });
    });
});

JUI.regPlugins.push(function($p){
    function initDictionary($dictionary,$exclude,url){
        var valuearray=[];
        $dictionary.find("select,input[checked]").each(function(){
            if($(this).val()){
                valuearray.push($(this).val());
            }
        });
        $.ajax({
            type: "POST", dataType: "json", url: url+"?id="+$dictionary.data("id")+"&excludeDictionaryId="+$exclude.data("id")+"&values="+valuearray.join(), cache: false, data: {} ,
            success: function(json) {
                if (!json ) {
                    return;
                }
                combox = $exclude.find(".combox");
                tree = $exclude.find(".tree");
                checkbox = $exclude.find("[type=checkbox]");
                if(combox.length) {
                    combox.each(function(){
                        var $combox = $("#op_" + $(this).find(".select").attr("id"));
                        $combox.find("li.disabled").removeClass("disabled").show();
                        $.each(json, function(i) {
                            $combox.find("li a[value="+json[i]+"]").parent().addClass("disabled").hide();
                            if($combox.find("li a[value="+json[i]+"]").hasClass("selected")){
                                $combox.find("li:visible:eq(0) a").click();
                            }
                        });
                    });
                } else if (tree.length) {
                    tree.each(function(){
                        $tree=$(this);
                        $tree.find("li").show();
                        $.each(json, function(i) {
                            $tree.find("li a[tvalue="+json[i]+"]").parent().parent().addClass("disabled").hide();
                            if($tree.find("li a[tvalue="+json[i]+"]").parent().find("[type=checkbox]").is(":checked")){
                                $tree.find("li a[tvalue="+json[i]+"]").click();
                            }
                        });
                    });
                } else if (checkbox.length) {
                    checkbox.each(function(){
                        $checkbox=$(this);
                        $checkbox.parent().show();
                        $.each(json, function(i) {
                            if($checkbox.val()==json[i]){
                                $checkbox.parent().hide();
                                if($checkbox.is(":checked")){
                                    $checkbox.click();
                                }
                            }
                        });
                    });
                }
            }, error: JUI.ajaxError
        });
    }
    $(".dictionary", $p).each(function(){
        if($(this).data("ref")){
            var refArray = $(this).data("ref").split(",");
            var url=$(this).data("url");
            var $dictionary=$(this);
            for (var index=0; index<refArray.length; index++) {
                (function($dictionary,index,$box,url){
                    var $exclude=$(".dictionary[data-id="+refArray[index]+"]",$box);
                    if($exclude.length){
                        $dictionary.find("select,input").change(function(){
                            initDictionary($dictionary,$exclude,url);
                        });
                    }
                    initDictionary($dictionary,$exclude,url);
                })($dictionary,index,( !$.pdialog.getCurrent() ) ? navTab.getCurrentPanel(): $.pdialog.getCurrent(),url);
            }
        }
    });
});
JUI.regPlugins.push(function($p){
    $("[lock-url]", $p).each(function(){
        var $this=$(this);
        $.getJSON($this.attr("lock-url"), function(data) {
            if(data && $this.hasClass("buttonActive")){
                $this.removeClass("buttonActive").addClass("buttonDisabled").prop("disabled",true).attr("title",data.nickname + "-" + new Date(data.createDate).toLocaleString());
                $("<i class=\"icon-lock icon-large\"></i>").prependTo($this);
            }
        });
    });
});
JUI.regPlugins.push(function($p){
    $("input.color", $p).each(function() {
        var $this = $(this);
        var opts = {
            color: $this.val(),
            showInput: true,
            showInitial: true,
            allowEmpty: true,
            showPalette: true,
            showSelectionPalette: true,
            preferredFormat: "hex",
            localStorageKey: "spectrum",
            showButtons: true,
            palette: [
                ["#000000", "#434343", "#666666", "#999999", "#b7b7b7", "#cccccc", "#d9d9d9", "#efefef", "#f3f3f3", "#ffffff"],
                ["#980000", "#ff0000", "#ff9900", "#ffff00", "#00ff00", "#00ffff", "#4a86e8", "#0000ff", "#9900ff", "#ff00ff"],
                ["#e6b8af", "#f4cccc", "#fce5cd", "#fff2cc", "#d9ead3", "#d0e0e3", "#c9daf8", "#cfe2f3", "#d9d2e9", "#ead1dc"],
                ["#dd7e6b", "#ea9999", "#f9cb9c", "#ffe599", "#b6d7a8", "#a2c4c9", "#a4c2f4", "#9fc5e8", "#b4a7d6", "#d5a6bd"],
                ["#cc4125", "#e06666", "#f6b26b", "#ffd966", "#93c47d", "#76a5af", "#6d9eeb", "#6fa8dc", "#8e7cc3", "#c27ba0"],
                ["#a61c00", "#cc0000", "#e69138", "#f1c232", "#6aa84f", "#45818e", "#3c78d8", "#3d85c6", "#674ea7", "#a64d79"],
                ["#85200c", "#990000", "#b45f06", "#bf9000", "#38761d", "#134f5c", "#1155cc", "#0b5394", "#351c75", "#741b47"],
                ["#5b0f00", "#660000", "#783f04", "#7f6000", "#274e13", "#0c343d", "#1c4587", "#073763", "#20124d", "#4c1130"]
            ]
        };
        if ($this.attr("showButtons") ) {
            opts.showButtons = $this.attr("showButtons");
        }
        if ($this.attr("showAlpha") ) {
            opts.showAlpha = $this.attr("showAlpha");
            opts.localStorageKey = "spectrum.alpha";
            opts.preferredFormat="hex8";
        }
        if ($this.attr("preferredFormat") ) {
            opts.preferredFormat = $this.attr("preferredFormat");
        }
        if ($this.attr("showInitial") ) {
            opts.showInitial = $this.attr("showInitial");
        }
        if ($this.attr("showPalette") ) {
            opts.showPalette = $this.attr("showPalette");
        }
        if ($this.attr("showSelectionPalette") ) {
            opts.showSelectionPalette = $this.attr("showSelectionPalette");
        }
        if ($this.attr("showPaletteOnly") ) {
            opts.showPaletteOnly = $this.attr("showPaletteOnly");
        }
        $this.spectrum(opts);
    });
});
JUI.regPlugins.push(function($p){
    $(".compare", $p).each(function() {
        var $this=$(this);
        var $resultBox = $this.find(".result-box");
        var $current = $this.find("[name=new]");
        var $compareMode = $this.find("[name=comparetype]");
        var $history = $this.find("[name=old]");
        var comparetype="words";
        function compare(resultBox,comparetype,oldstring,newstring){
            var diff
            if("chars"===comparetype) {
                diff = Diff.diffChars(oldstring, newstring);
            } else if("lines"===comparetype) {
                diff = Diff.diffLines(oldstring, newstring);
            } else if("sentence"===comparetype) {
                diff = Diff.diffSentences(oldstring, newstring);
            } else if("css"===comparetype) {
                diff = Diff.diffCss(oldstring, newstring);
            } else {
                diff = Diff.diffWords(oldstring, newstring);
            }
            
            resultBox.empty();
            for (var i=0; i < diff.length; i++) {
                if (diff[i].removed) {
                    $("<del></del>").appendTo(resultBox).text(diff[i].value);
                } else if (diff[i].added) {
                    $("<ins></ins>").appendTo(resultBox).text(diff[i].value);
                } else {
                  resultBox.append($("<div></div>").text(diff[i].value).html());
                }
            }
        }
        if($history.length && $current.length && $resultBox.length){
            if($compareMode.length){
                comparetype=$compareMode.val();
                $compareMode.change(function(){
                    compare($resultBox, comparetype, $history.val(), $current.val());
                });
            }
            if($current.is(".code") && JUI.instances[$current.data("id")]){
                JUI.instances[$current.data("id")].on('change',function(editor){
                    compare($resultBox, comparetype, $history.val(), editor.getValue());
                });
            } else {
                $current.change(function(){
                    compare($resultBox, comparetype, $history.val(), $current.val());
                });
            }
            if(window.jsdiff.initd){
                compare($resultBox, comparetype, $history.val(), $current.val());
            } else {
                loadScripts(window.jsdiff.resources,function(){
                    window.jsdiff.initd=true;
                    compare($resultBox, comparetype, $history.val(), $current.val());
                });
            }
        }
    });
});
$(document).keydown(function(e){
    if(e.keyCode == JUI.keyCode.ESC && $.pdialog.getCurrent()){
        $.pdialog.closeCurrent();
    } else if(e.keyCode == JUI.keyCode.CHAR_S && (navigator.platform.match("Mac") ? e.metaKey : e.ctrlKey)) {
        var formSubmit=$("form",!$.pdialog.getCurrent() ? navTab.getCurrentPanel(): $.pdialog.getCurrent()).not(".pagerForm").find("[type=submit]:eq(0)");
        if(formSubmit.length){
          formSubmit.click();
          e.preventDefault();
        }
    }
});