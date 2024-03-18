/**
 * @license Copyright (c) 2003-2023, CKSource Holding sp. z o.o. All rights reserved.
 * For licensing, see https://ckeditor.com/legal/ckeditor-oss-license
 */

CKEDITOR.editorConfig = function( config ) {
	// Define changes to default configuration here. For example:
    config.language = window.CKEDITOR_LANGUAGE;
  // config.language = 'fr';
	// config.uiColor = '#AADC6E';
	// http://docs.ckeditor.com/#!/api/CKEDITOR.config
   // config.extraPlugins = 'audio';
  //  odeSnippet_theme: 'zenburn';
    config.filebrowserUploadUrl = base + '/ckeditor/upload?1=1';//定义图片上传的地址
    config.filebrowserBrowseUrl = base + '/cmsWebFile/browse.html?type=image';  //定义图片的浏览服务器窗口.
};

CKEDITOR.on('instanceReady', function(event){
    var editor=event.editor;
    editor.addCommand("save", { readOnly: 1, modes: { wysiwyg: 1,source: 1 }, exec: function (editor) {
        if ( editor.fire( 'save' ) ) {
            var $form = editor.element.$.form;
            if ( $form && $form.onsubmit() ) {
                try {
                    $form.submit();
                } catch ( e ) {
                    if ( $form.submit.click ){
                        $form.submit.click();
                    }
                }
            }
        }
    }});
});