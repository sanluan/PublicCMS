/**
 * Created by huihuazhang on 2016/4/27.
 * 基于HTML5 文件上传的核心脚本
 * http://www.w3.org/TR/html-markup/input.file.html
 */
(function($){
    function readAsDataURL(img, file){
        // Using FileReader to display the image content
        var reader = new FileReader();
        reader.onload = (function(aImg) {
            return function(e) {
                aImg.src = e.target.result;
            };
        })(img);

        reader.readAsDataURL(file);
    }

    function previewUploadImg($uploadWrap, files) {
        var $previewElem = $("<div class=\"thumbnail\"></div>").appendTo($uploadWrap);
        var file = files[0];
        if (!file) {return false;}
        if (!file.type.match(/image.*/)) {
            throw "File Type must be an image";
        }
        var img = document.createElement("img");
        img.file = file;
        $previewElem.empty().append(img);

        if ($previewElem.find(".del-icon").length == 0) {
            $("<a class=\"del-icon\"></a>").appendTo($previewElem).click(function(){
                $previewElem.remove();
                $uploadWrap.find("input[type=file]").val("");
            });
        }
        if ($previewElem.find(".edit-icon").length == 0 && $uploadWrap.find("input[name=base64File]").length) {
            $("<a class=\"edit-icon\"></a>").appendTo($previewElem).click(function(){
                editImg($uploadWrap,img,file.name,function(dataURL,fileName){
                    if(dataURL){
                        img.src=dataURL;
                        $uploadWrap.find("input[name=base64File]").val(dataURL.substring(dataURL.indexOf("base64,")+7));
                        $uploadWrap.find("input[name=originalFilename]").val(fileName);
                        $uploadWrap.find("input[type=file]").val("");
                    }
                });
            });
        }
        readAsDataURL(img, file);
    }

    function editImg($uploadWrap,img,fileName,callback){
        var $this = $uploadWrap.parent().find(".image-editor");
        if($this.length) {
            if($this.data("id")){
                delete JUI.instances[$this.data("id")];
            }
        } else {
            $uploadWrap.after("<div class=\"image-editor\"></div>");
            $this = $uploadWrap.parent().find(".image-editor");
        }
        $this.on("click","button",function(){
            return false;
        });
        var index = window.imageEditor.index++;
        var dataId = "imageEditor_"+index;
        var widthInput=$uploadWrap.find("input[name=width]");
        var heightInput=$uploadWrap.find("input[name=height]");
        function initSize(imageEditor,width,height){
            if(width && height && imageEditor){
                imageEditor.render({
                    Crop:{
                        autoResize: true,
                        presetsItems:[
                            {
                                titleKey: 'custom',
                                descriptionKey: width+'*'+height,
                                width: width,
                                height: height,
                                disableManualResize:true
                            }
                        ]
                    }
                });
            }
        }
        widthInput.change(function(){
            if(JUI.instances[$this.data("id")]){
                initSize(JUI.instances[$this.data("id")],parseInt(widthInput.val()),parseInt(heightInput.val()));
            }
        });
        heightInput.change(function(){
            if(JUI.instances[$this.data("id")]){
                initSize(JUI.instances[$this.data("id")],parseInt(widthInput.val()),parseInt(heightInput.val()));
            }
        });
        function init(editor,dataId,img,fileName){
            filerobotImageEditorConfig.source=img;
            filerobotImageEditorConfig.defaultSavedImageName=fileName;
            JUI.instances[dataId] = new FilerobotImageEditor(editor[0], filerobotImageEditorConfig);
            JUI.instances[dataId].render({
                onSave:function(imageData, imageDesignState){
                    if ($.isFunction(callback) ) {
                        callback(imageData.imageBase64,imageData.fullName);
                    }
                },
                onClose: function(closingReason, haveNotSavedChanges){
                  delete JUI.instances[dataId];
                }
            });
            initSize(JUI.instances[dataId],parseInt(widthInput.val()),parseInt(heightInput.val()));
        }
        if(window.imageEditor.initd){
            init($this,dataId,img,fileName);
        } else {
            loadScripts(window.imageEditor.resources,function(){
                window.imageEditor.initd=true;
                init($this,dataId,img,fileName);
            });
        }
        $this.attr("data-id",dataId);
    }

    // multiple
    function previewUploadImg2($uploadWrap, files) {
        var rel = $uploadWrap.attr("rel");
        var $previewElem = $(rel);
        $previewElem.empty();
        for (var index=0; index<files.length; index++) {
            var file = files[index];
            var $thumb = $("<li class=\"thumbnail\"></li>");
            var img = document.createElement("img");
            img.file = file;
            $thumb.append(img);
            $previewElem.append($thumb);
            readAsDataURL(img, file);
        }
    }

    $.fn.extend({
        /**
         * 图片编辑
         * @param options
         */
        editImg: function(options){
            $uploadWrap = $(this);
            var $previewElem = $uploadWrap.find(".thumbnail");
            if(0 == $previewElem.length){
                $previewElem = $("<div class=\"thumbnail\"></div>").appendTo($uploadWrap);
            }
            var img = document.createElement("img");
            img.src = options.imgUrl;
            $previewElem.empty().append(img);

            if ($previewElem.find(".del-icon").length == 0) {
                $("<a class=\"del-icon\"></a>").appendTo($previewElem).click(function(){
                    $previewElem.remove();
                    $uploadWrap.find("input[type=file]").val("");
                });
            }
            editImg($(this),options.imgUrl,options.imgName,function(dataURL,fileName){
                if(dataURL){
                    img.src=dataURL;
                    $uploadWrap.find("input[name=base64File]").val(dataURL.substring(dataURL.indexOf("base64,")+7));
                    $uploadWrap.find("input[name=originalFilename]").val(fileName);
                    $uploadWrap.find("input[type=file]").val("");
                    if ($previewElem.find(".del-icon").length != 0) {
                        $previewElem.find(".del-icon").click(function(){
                            $previewElem.remove();
                            $uploadWrap.find("input[name=base64File]").val("");
                            $uploadWrap.find("input[name=originalFilename]").val("");
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
            var op = $.extend({maxW:80}, options);
            return this.each(function(){
                var $uploadWrap = $(this);

                var $inputFiles = $uploadWrap.find("input[type=file]");
                $inputFiles.each(function(index){
                    var $inputFile = $(this).css({left:(op.maxW*index)+"px"});
                    $inputFile.on("change", function () {
                        var files = this.files;
                        if (this.multiple) {
                            previewUploadImg2($uploadWrap, files);
                        } else {
                            previewUploadImg($uploadWrap, files);
                        }
                    });
                });

                var $delIcon = $uploadWrap.find(".del-icon");
                if ($delIcon) { // 删除服务器上的图片
                    $delIcon.click(function(){
                        $.ajax({
                            type: "GET",
                            url:$delIcon.attr("href"),
                            dataType:"json",
                            cache: false,
                            success: function(json){
                                JUI.ajaxDone(json);
                                if (json[JUI.keys.statusCode] == JUI.statusCode.ok){
                                    $uploadWrap.find("div.thumbnail").remove();
                                    $uploadWrap.find("input[type=file]").val("");
                                }
                            },
                            error: JUI.ajaxError
                        });

                        return false;
                    });
                }

            });
        }
    });


    JUI.regPlugins.push(function($p){
        $("div.upload-wrap", $p).previewUploadImg({maxW:300});
    });

})(jQuery);