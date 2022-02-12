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
function loadScripts(urls, callback) {
    function loadScript(url, callback){
        var script = document.createElement("script")
        script.type = "text/javascript";
        if (script.readyState){
            script.onreadystatechange = function(){
                if (script.readyState == "loaded" || script.readyState == "complete"){
                    script.onreadystatechange = null;
                    callback();
                }
            };
        } else {
            script.onload = function(){
                callback();
            };
        }
        script.src = url;
        document.getElementsByTagName("head")[0].appendChild(script);
    }
    var i = 0,count = urls.length-1;
    var loadOrCallback=function(){
        if (i == count) {
            callback && callback();
        }else{
            loadScript(urls[++i],loadOrCallback);
        }
    };
    loadScript(urls[i],loadOrCallback);
}
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