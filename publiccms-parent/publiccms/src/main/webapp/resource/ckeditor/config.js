/**
 * @license Copyright (c) 2003-2017, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function( config ) {
	// Define changes to default configuration here. For example:
    config.language = window.CKEDITOR_LANGUAGE;
	// config.uiColor = '#AADC6E';
	// http://docs.ckeditor.com/#!/api/CKEDITOR.config
    config.extraPlugins = 'codesnippet';
    odeSnippet_theme: 'zenburn';

    config.filebrowserImageUploadUrl = base + '/ckeditor/upload.json?type=image';//定义图片上传的地址
    config.filebrowserImageBrowseUrl = base + '/cmsWebFile/browse.html?type=image';  //定义图片的浏览服务器窗口.

    config.filebrowserFlashUploadUrl = base + '/ckeditor/upload.json?type=image';//定义flash上传的地址
    config.filebrowserFlashBrowseUrl = base + '/cmsWebFile/browse.html?type=flash';//定义flash的浏览服务器窗口
};
