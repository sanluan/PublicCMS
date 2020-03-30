/**
 * @license Copyright (c) 2003-2019, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see https://ckeditor.com/legal/ckeditor-oss-license
 */

CKEDITOR.editorConfig = function( config ) {
	// Define changes to default configuration here. For example:
    config.language = window.CKEDITOR_LANGUAGE;
  // config.language = 'fr';
	// config.uiColor = '#AADC6E';
	// http://docs.ckeditor.com/#!/api/CKEDITOR.config
    config.extraPlugins = 'codesnippet';
    odeSnippet_theme: 'zenburn';

    config.filebrowserUploadUrl = base + '/ckeditor/upload.json';//定义图片上传的地址
    config.filebrowserBrowseUrl = base + '/cmsWebFile/browse.html?type=image';  //定义图片的浏览服务器窗口.
};
