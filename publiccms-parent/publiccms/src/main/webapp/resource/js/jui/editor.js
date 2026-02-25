(function($){
    JUI.regPlugins.push(function($p){
        var ajaxbg = $("#background,#progressBar");
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
                        ajaxbg.show();
                        loadScripts(window.editor.ckeditorResources,function(){
                            window.editor.ckeditorIniting=false;
                            window.editor.ckeditorInitd=true;
                            CKEDITOR.replace(dataId);
                            ajaxbg.hide();
                            if(0 < window.editor.ckeditorArray.length){
                                for(var i=0;i<window.editor.ckeditorArray.length;i++){
                                    CKEDITOR.replace(window.editor.ckeditorArray.shift());
                                }
                            }
                        },window.editor.base);
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
                        ajaxbg.show();
                        loadScripts(window.editor.tinymceResources,function(){
                            window.editor.tinymceIniting=false;
                            window.editor.tinymceInitd=true;
                            tinymce.init($.extend(true, {selector:"#"+dataId}, window.TINYMCE_OPTIONS));
                            ajaxbg.hide();
                            if(0 < window.editor.tinymceArray.length){
                                for(var i=0;i<window.editor.tinymceArray.length;i++){
                                    tinymce.init($.extend(true, {selector:"#"+window.editor.tinymceArray.shift()}, window.TINYMCE_OPTIONS));
                                }
                            }
                        },window.editor.base);
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
                        ajaxbg.show();
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
                            editor.addListener( 'beforesubmit', function(  ) {
                                if ( editor.form && editor.form.onsubmit() ) {
                                    try {
                                        editor.form.submit();
                                    } catch ( e ) {
                                        if ( editor.form.submit.click ){
                                            editor.form.submit.click();
                                        }
                                    }
                                }
                                return false;
                            });
                            $this.attr("data-id","ueditorInstant"+editor.uid);
                            ajaxbg.hide();
                            if(0 < window.editor.ueditorArray.length){
                                for(var i=0;i<window.editor.ueditorArray.length;i++){
                                    var editor = new baidu.editor.ui.Editor();
                                    var $textarea=$("#"+window.editor.ueditorArray.shift());
                                    $textarea.attr("data-id","ueditorInstant"+editor.uid);
                                    if ($textarea.attr("maxlength") ){
                                        editor.setOpt({
                                            maximumWords: $textarea.attr("maxlength")
                                        });
                                    }
                                    editor.render($textarea[0]);
                                    editor.addListener( 'beforesubmit', function( ) {
                                        if ( editor.form && editor.form.onsubmit() ) {
                                            try {
                                                editor.form.submit();
                                            } catch ( e ) {
                                                if ( editor.form.submit.click ){
                                                    editor.form.submit.click();
                                                }
                                            }
                                        }
                                        return false;
                                    });
                                }
                            }
                        },window.editor.base);
                    }
                }
            }
        });
        $("textarea.code", $p).each(function() {
            var $this = $(this);
            var index= window.codemirror.index++;
            var dataId="codeEditor_"+index;
            var mode = "htmlmixed"
            if($(this).attr("mode")){
                mode = $(this).attr("mode");
            }
            function initCodeMirror($this,mode,dataId){
                JUI.instances[dataId]=CodeMirror.fromTextArea($this[0], {
                    mode: mode,
                    tabSize        : 4,
                    indentUnit     : 4,
                    lineNumbers    : true,
                    lineWrapping   : true,
                    indentWithTabs : true,
                    foldGutter     : true,
                    gutters        : ["CodeMirror-linenumbers", "CodeMirror-foldgutter"],
                    extraKeys      : { "Ctrl": "autocomplete" }
                });
                if($this.prop("readonly")){
                    JUI.instances[dataId].setOption("readOnly",true);
                }
                if ("function" === typeof $.cookie && $("select.codeTheme",$p).length){
                    $("select.codeTheme",$p).change(function(){
                        $.cookie("code_theme",$(this).val(), { expires: 30 });
                        JUI.instances[dataId].setOption("theme", $(this).val());
                    });
                    if ($.cookie("code_theme")) {
                        $("select.codeTheme",$p).comboxVal($.cookie("code_theme"));
                        JUI.instances[dataId].setOption("theme", $.cookie("code_theme"));
                    }
                }
                $this.attr("data-id",dataId);
            }
            if(!window.codemirror.initd){
                ajaxbg.show();
                loadScripts(window.codemirror.resources,function(){
                    window.codemirror.initd=true;
                    initCodeMirror($this,mode,dataId);
                    ajaxbg.hide();
                },window.codemirror.base);
            } else {
                if(window.codemirror.initing){
                    window.codemirror.objArray.push({obj:$this, mode:mode, dataId:dataId});
                } else {
                    initCodeMirror($this,mode,dataId);
                    if(0 < window.codemirror.objArray.length){
                        for(var i=0;i<window.codemirror.objArray.length;i++){
                            var codeConfig=window.codemirror.objArray.shift();
                            initCodeMirror(codeConfig.obj, codeConfig.mode, codeConfig.dataId);
                        }
                    }
                }
            }

        });
    });
})(jQuery);