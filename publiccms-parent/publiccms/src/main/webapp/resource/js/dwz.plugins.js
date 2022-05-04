DWZ.regPlugins.push(function($p){
    $(".searchBar .searchContent", $p).click(function(event){
        if($(event.target).is('ul')||$(event.target).is('li')){
            $content = $(this).closest(".pageHeader").next().find('[layouth]');
            var height = $(this).height();
            $(this).toggleClass('searchContentHover');
            $content.height($content.height()-$(this).height()+height);
        }
    });
});
DWZ.regPlugins.push(function($p){
    $('.tagsBox',$p).each(function (){
        var $tagBox = $(this);
        var $callback = $tagBox.attr("callback");
        if ($callback && !$.isFunction($callback) ) {
            $callback = eval('(' + $callback + ')');
        }
        $tagBox.on('click','span>a',function(){
            $(this).parent().remove();
            if ($callback) {
                $callback();
            }
            return false;
        });
    });
});
DWZ.regPlugins.push(function($p){
    if($('.cmsVersion',$p).length ) {
        var fullVersion=$('.cmsVersion a',$p).eq(0).text();
        $.getJSON(Base64.decode('Ly9jbXMucHVibGljY21zLmNvbS9hcGkvZGlyZWN0aXZlL3ZlcnNpb24=')+"?version="+fullVersion, function(data) {
            var version=fullVersion.substring(0,fullVersion.lastIndexOf('.'));
            var revision=fullVersion.substring(fullVersion.lastIndexOf('.')+1);
            if(version!==data.cms ) {
                $('.cmsVersion .old',$p).show();
            } else {
                if(revision == data.revision){
                    $('.cmsVersion .new',$p).show();
                } else {
                    $('.cmsVersion .old',$p).css('color','gray').show();
                }
            }
        });
    }
});
DWZ.regPlugins.push(function($p){
    $("textarea.editor", $p).each(function(i) {
        var $this = $(this);
        var index= window.editor.index++;
        var dataId="editor_"+index;
        if("ckeditor"==$this.attr("editorType")) {
            if(!window.editor.ckeditorInitd){
                loadScripts(window.editor.ckeditorResources,function(){
                    window.editor.ckeditorInitd=true;
                    $this.attr("id",dataId);
                    CKEDITOR.replace(dataId);
                    $this.attr("data-id",dataId);
                });
            } else {
                $this.attr("id",dataId);
                CKEDITOR.replace(dataId);
                $this.attr("data-id",dataId);
            }
        } else if("kindeditor"==$this.attr("editorType")) {
            if(!window.editor.kindeditorInitd){
                loadScripts(window.editor.kindeditorResources,function(){
                    window.editor.kindeditorInitd=true;
                    $this.attr("id",dataId);
                    KindEditor.create('#'+dataId,window.KINDEDITOR_OPTIONS);
                    $this.attr("data-id",dataId);
                });
            } else {
                $this.attr("id",dataId);
                KindEditor.create('#'+dataId,window.KINDEDITOR_OPTIONS);
                $this.attr("data-id",dataId);
            }
        } else {
            if(!window.editor.ueditorInitd){
                loadScripts(window.editor.ueditorResources,function(){
                    window.editor.ueditorInitd=true;
                    var editor = new baidu.editor.ui.Editor();
                    if ($this.attr("maxlength") ){
                        editor.setOpt({
                            maximumWords: $this.attr("maxlength")
                        });
                    }
                    editor.render($this[0]);
                    $this.attr("data-id","ueditorInstant"+editor.uid);
                });
            } else {
                var editor = new baidu.editor.ui.Editor();
                if ($this.attr("maxlength") ){
                    editor.setOpt({
                        maximumWords: $this.attr("maxlength")
                    });
                }
                editor.render($this[0]);
                $this.attr("data-id","ueditorInstant"+editor.uid);
            }
        }
    });
    $("textarea.code", $p).each(function() {
        var $this = $(this);
        var index= window.codemirror.index++;
        var dataId="editor_"+index;
        var mode = 'htmlmixed'
        if($(this).attr('mode')){
            mode = $(this).attr('mode');
        }
        if(!window.codemirror.initd){
            loadScripts(window.codemirror.resources,function(){
                window.codemirror.initd=true;
                DWZ.instances[dataId]=CodeMirror.fromTextArea($this[0], {
                    mode: mode,
                    lineNumbers: true,
                    tabSize        : 4,
                    indentUnit     : 4,
                    lineWrapping   : true,
                    indentWithTabs : true,
                    extraKeys: { "Ctrl": "autocomplete" }
                });
                $this.attr("data-id",dataId);
            });
        } else {
            DWZ.instances[dataId]=CodeMirror.fromTextArea($this[0], {
                mode: mode,
                lineNumbers: true,
                tabSize        : 4,
                indentUnit     : 4,
                lineWrapping   : true,
                indentWithTabs : true,
                extraKeys: { "Ctrl": "autocomplete" }
            });
            $this.attr("data-id",dataId);
        }
       
    });
});
/**
 * Created by huihuazhang on 2016/4/27.
 * 基于HTML5 文件上传的核心脚本
 * http://www.w3.org/TR/html-markup/input.file.html
 */
(function($){
    function readAsDataURL(img, file, maxW, maxH){
        // Using FileReader to display the image content
        var reader = new FileReader();
        reader.onload = (function(aImg) {
            return function(e) {
                aImg.src = e.target.result;

                var width = aImg.naturalWidth,
                    height = aImg.naturalHeight;
                aImg.setAttribute('data-width', width);
                aImg.setAttribute('data-height', height);

                if (maxW && maxH) {

                    if (width/maxW > height/maxH) {
                        aImg.setAttribute('height', maxH);
                    } else {
                        aImg.setAttribute('width', maxW);
                    }
                }

            };
        })(img);

        reader.readAsDataURL(file);
    }

    function previewUploadImg($uploadWrap, files, maxW, maxH) {

        var $previewElem = $('<div class="thumbnail"></div>').appendTo($uploadWrap);

        var file = files[0];

        if (!file) {return false;}

        if (!file.type.match(/image.*/)) {
            throw "File Type must be an image";
        }

        var img = document.createElement("img");
        img.file = file;
        $previewElem.empty().append(img);

        if ($previewElem.find('.del-icon').size() == 0) {
            $('<a class="del-icon"></a>').appendTo($previewElem).click(function(event){
                $previewElem.remove();
                $uploadWrap.find('input[type=file]').val('');
            });
        }
        if ($previewElem.find('.edit-icon').size() == 0) {
            $('<a class="edit-icon"></a>').appendTo($previewElem).click(function(event){
                editImg($uploadWrap,file,file.name,function(dataURL){
                    if(dataURL){
                        img.src=dataURL;
                        $uploadWrap.find('input[name=base64File]').val(dataURL.substring(dataURL.indexOf('base64,')+7));
                        $uploadWrap.find('input[name=originalFilename]').val(file.name);
                        $uploadWrap.find('input[type=file]').val('');
                    }
                });
            });
        }

        readAsDataURL(img, file, maxW, maxH);

    }
    
    function editImg($uploadWrap,img,fileName,callback){
        if(0 == $uploadWrap.parent().find('.image-editor').length ) {
            $uploadWrap.after('<div class="image-editor" ><div class="unit"><p class="image-box"></p><label><a href="javascript:;" class="button"><i class="icon-ok"></i></a></label></div></div>');
        }
        var $this = $uploadWrap.parent().find('.image-editor');
        if($this.attr("data-id")){
            DWZ.instances[$this.attr("data-id")].destroy();
        }
        var index = window.photoclip.index++;
        var dataId = "photoclip_"+index;
        var widthInput=$uploadWrap.parents("form:first").find("input[name=width]");
        var heightInput=$uploadWrap.parents("form:first").find("input[name=height]");
        widthInput.change(function(){
            DWZ.instances[$uploadWrap.parent().find('.image-editor').data("id")].size(parseInt(widthInput.val()),parseInt(heightInput.val()));
        });
        heightInput.change(function(){
            DWZ.instances[$uploadWrap.parent().find('.image-editor').data("id")].size(parseInt(widthInput.val()),parseInt(heightInput.val()));
        });
        var options= {
            size: [parseInt(widthInput.val()), parseInt(heightInput.val())],
            ok: $this.find('.button'),
            done: function(dataURL){
                if ($.isFunction(callback) ) {
                    callback(dataURL);
                }
                DWZ.instances[dataId].destroy();
                delete DWZ.instances[dataId];
                $this.remove();
            },
            fail: function(msg) {
                alertMsg.error(msg);
            }
        };
        var filenames=fileName.split('.');
        if('png'==filenames[filenames.length-1]){
            options.outputType='png';
        }
        function init(dataId,img){
            var photoClip = new PhotoClip($this.find('.image-box')[0], options);
            photoClip.load(img);
            DWZ.instances[dataId] = photoClip;
        }
        if(!window.photoclip.initd){
            loadScripts(window.photoclip.resources,function(){
                window.photoclip.initd=true;
                init(dataId,img);
            });
        } else {
            init(dataId,img);
        }
        $this.attr("data-id",dataId);
    }

    // multiple
    function previewUploadImg2($uploadWrap, files, maxW, maxH) {

        var rel = $uploadWrap.attr('rel');
        var $previewElem = $(rel);

        $previewElem.empty();
        for (var index=0; index<files.length; index++) {
            var file = files[index];

            var $thumb = $('<li class="thumbnail"></li>');

            var img = document.createElement("img");
            img.file = file;
            $thumb.append(img);
            $previewElem.append($thumb);

            readAsDataURL(img, file, maxW, maxH);
        }
    }
    
    $.fn.extend({
        /**
         * 图片编辑
         * @param options
         */
        editImg: function(options){
            $uploadWrap = $(this);
            var $previewElem = $uploadWrap.find('.thumbnail');
            if(0 == $previewElem.length){
                $previewElem = $('<div class="thumbnail"></div>').appendTo($uploadWrap);
            }
            var img = document.createElement("img");
            img.src = options.imgUrl;
            $previewElem.empty().append(img);

            editImg($(this),options.imgUrl,options.imgName,function(dataURL){
                if(dataURL){
                    img.src=dataURL;
                    $uploadWrap.find('input[name=base64File]').val(dataURL.substring(dataURL.indexOf('base64,')+7));
                    $uploadWrap.find('input[name=originalFilename]').val(options.imgName);
                    $uploadWrap.find('input[type=file]').val('');
                    if ($previewElem.find('.del-icon').size() == 0) {
                        $('<a class="del-icon"></a>').appendTo($previewElem).click(function(event){
                            $previewElem.remove();
                            $uploadWrap.find('input[name=base64File]').val('');
                            $uploadWrap.find('input[name=originalFilename]').val('');
                        });
                    }
                }
            });
        }
    });

    $.fn.extend({
        /**
         * 选择上传图片缩略图列表预览
         * @param options
         */
        previewUploadImg: function(options){
            var op = $.extend({maxW:80, maxH:80}, options);
            return this.each(function(){
                var $uploadWrap = $(this);

                var $inputFiles = $uploadWrap.find('input[type=file]');
                $inputFiles.each(function(index){
                    var $inputFile = $(this).css({left:(op.maxW*index)+'px'});
                    $inputFile.on('change', function () {
                        var files = this.files;

                        if (this.multiple) {
                            previewUploadImg2($uploadWrap, files, op.maxW, op.maxH);
                        } else {
                            previewUploadImg($uploadWrap, files, op.maxW, op.maxH);
                        }
                    });
                });

                var $delIcon = $uploadWrap.find('.del-icon');
                if ($delIcon) { // 删除服务器上的图片
                    $delIcon.click(function(event){
                        $.ajax({
                            type: 'GET',
                            url:$delIcon.attr('href'),
                            dataType:"json",
                            cache: false,
                            success: function(json){
                                DWZ.ajaxDone(json);

                                if (json[DWZ.keys.statusCode] == DWZ.statusCode.ok){
                                    $uploadWrap.find('div.thumbnail').remove();
                                    $uploadWrap.find('input[type=file]').val('');
                                }
                            },
                            error: DWZ.ajaxError
                        });

                        return false;
                    });
                }

            });
        }
    });


    DWZ.regPlugins.push(function($p){
        $("div.upload-wrap", $p).previewUploadImg({maxW:300,maxH:200});
    });

})(jQuery);

DWZ.regPlugins.push(function($p){
    $("a.view[target=_blank]", $p).each(function(){
        $btn=$(this);
        $input=$('input[name='+escapeJquery($(this).attr('ref'))+']',$(this).parents(".unitBox:first"));
        function control(){
            if($(this).val()){
                $('a.view[target=_blank][ref='+escapeJquery($(this).attr('name'))+']').show();
            }else{
                $('a.view[target=_blank][ref='+escapeJquery($(this).attr('name'))+']').hide();
            }
        }
        $input.change(control);
        if($input.val()){
            $btn.show();
        }else{
            $btn.hide();
        }
        $btn.click(function(){
            var value=$('input[name='+escapeJquery($(this).attr('ref'))+']',$(this).parents(".unitBox:first")).val();
            if(value){
                $(this).attr('href',value.isUrl() ? value : $(this).data('prefix')+value);
            }else{
                return false;
            }
        });
    });
});

DWZ.regPlugins.push(function($p){
    function initDictionary($dictionary,$exclude,url){
        var valuearray=[];
        $dictionary.find('select,input[checked]').each(function(){
            if($(this).val()){
                valuearray.push($(this).val());
            }
        });
        $.ajax({
            type: 'POST', dataType: "json", url: url+'?id='+$dictionary.data('id')+'&excludeDictionaryId='+$exclude.data('id')+'&values='+valuearray.join(), cache: false, data: {} ,
            success: function(json) {
                if (!json ) {
                    return;
                }
                combox = $exclude.find('.combox');
                tree = $exclude.find('.tree');
                checkbox = $exclude.find('[type=checkbox]');
                if(combox.length) {
                    combox.each(function(){
                        var $combox = $("#op_" + $(this).find('.select').attr('id'));
                        $combox.find("li.disabled").removeClass('disabled').show();
                        $.each(json, function(i) {
                            $combox.find("li a[value="+json[i]+"]").parent().addClass('disabled').hide();
                            if($combox.find("li a[value="+json[i]+"]").hasClass('selected')){
                                $combox.find("li:visible:eq(0) a").click();
                            }
                        });
                    });
                } else if (tree.length) {
                    tree.each(function(){
                        $tree=$(this);
                        $tree.find('li').show();
                        $.each(json, function(i) {
                            $tree.find("li a[tvalue="+json[i]+"]").parent().parent().addClass('disabled').hide();
                            if($tree.find("li a[tvalue="+json[i]+"]").parent().find('[type=checkbox]').is(':checked')){
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
                                if($checkbox.is(':checked')){
                                    $checkbox.click();
                                }
                            }
                        });
                    });
                }
            }, error: DWZ.ajaxError
        });
    }
    $(".dictionary", $p).each(function(){
        if($(this).data('ref')){
            var refArray = $(this).data('ref').split(',');
            var url=$(this).data('url');
            var $dictionary=$(this);
            for (var index=0; index<refArray.length; index++) {
                (function($dictionary,index,$box,url){
                    var $exclude=$('.dictionary[data-id='+refArray[index]+']',$box);
                    if($exclude.length){
                        $dictionary.find('select,input').change(function(){
                            initDictionary($dictionary,$exclude,url);
                        });
                    }
                    initDictionary($dictionary,$exclude,url);
                })($dictionary,index,( !$.pdialog.getCurrent() ) ? navTab.getCurrentPanel(): $.pdialog.getCurrent(),url);
            }
        }
    });
});
DWZ.regPlugins.push(function($p){
    $("[lock-url]", $p).each(function(){
        var $this=$(this);
        $.getJSON($this.attr("lock-url"), function(data) {
            if(data && $this.hasClass("buttonActive")){
                $this.removeClass("buttonActive").addClass("buttonDisabled").prop("disabled",true).attr("title",data.nickname + "-" + new Date(data.createDate).toLocaleString());
                $('<i class="icon-lock icon-large"></i>').prependTo($this);
            }
        });
    });
});
DWZ.regPlugins.push(function($p){
    $('input.color', $p).each(function() {
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
$(document).keydown(function(e){
    if(e.keyCode == DWZ.keyCode.ESC && $.pdialog.getCurrent()){
        $.pdialog.closeCurrent();
    } else if(e.keyCode == DWZ.keyCode.CHAR_S && (navigator.platform.match("Mac") ? e.metaKey : e.ctrlKey)) {
        var formSubmit=$('form',!$.pdialog.getCurrent() ? navTab.getCurrentPanel(): $.pdialog.getCurrent()).not('.pagerForm').find('[type=submit]:eq(0)');
        if(formSubmit.length){
          formSubmit.click();
          e.preventDefault();
        }
    }
});