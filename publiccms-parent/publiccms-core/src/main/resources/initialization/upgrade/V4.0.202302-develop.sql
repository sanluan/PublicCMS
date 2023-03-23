-- 2023-02-27 --
ALTER TABLE `log_operate` MODIFY COLUMN `content` longtext default NULL COMMENT '内容' AFTER `create_date`;
ALTER TABLE `log_upload`
    ADD COLUMN `privatefile` tinyint(1) NOT NULL DEFAULT 0 COMMENT '私有文件' AFTER `original_name`,
    DROP INDEX `log_upload_file_type`,
    ADD INDEX `log_upload_file_type` (`site_id`, `privatefile`, `file_type`, `file_size`);
-- 2023-03-09 --
UPDATE `sys_module` SET `authorized_url`= 'ueditor,tinymce/upload,tinymce/imageList,ckeditor/upload,cmsWebFile/browse,file/doImport' WHERE `id` ='editor';
-- 2023-03-11 --
UPDATE `sys_module` SET `authorized_url`= 'cmsTemplate/doUpload,cmsTemplate/import,cmsTemplate/doImport,cmsTemplate/sitefileList,cmsTemplate/viewSitefile,cmsTemplate/visitSitefileImage' WHERE `id` ='template_import';
-- 2023-03-20 --
UPDATE `sys_module` SET `authorized_url`= 'visit/exportDay' WHERE `id` ='visit_day';
UPDATE `sys_module` SET `authorized_url`= 'visit/view,visit/exportHistory' WHERE `id` ='visit_history';
UPDATE `sys_module` SET `authorized_url`= 'visit/exportItem' WHERE `id` ='visit_item';
UPDATE `sys_module` SET `authorized_url`= 'visit/exportSession' WHERE `id` ='visit_session';
UPDATE `sys_module` SET `authorized_url`= 'visit/exportUrl' WHERE `id` ='visit_url';
ALTER TABLE `visit_session`
    DROP INDEX `visit_session_visit_date`,
    ADD INDEX `visit_session_visit_date` (`site_id`, `visit_date`, `session_id`, `ip`, `last_visit_date`);
ALTER TABLE `visit_url`
    DROP INDEX `visit_url_pv`,
    ADD INDEX `visit_url_pv` (`site_id`, `visit_date`, `url`, `pv`);
-- 2023-03-24 --
UPDATE `sys_module` SET `authorized_url`= 'cmsCategory/addMore,cmsCategory/virify,cmsCategory/rebuildChildIds,cmsCategory/batchPublish,cmsCategory/batchCopy,cmsCategory/batchCreate,cmsCategory/batchSave,cmsCategory/batchSeo,cmsCategory/batchSeo,cmsCategory/saveSeo,cmsCategory/categoryPath,cmsCategory/contentPath,cmsCategory/save' WHERE `id` ='category_add';
UPDATE `sys_module` SET `authorized_url`= 'cmsDictionary/addChild,cmsDictionary/batchCreate,cmsDictionary/exclude,cmsDictionary/excludeTree,cmsDictionary/excludeValue,cmsDictionaryExclude/save,cmsDictionaryExcludeValue/save,cmsDictionary/save,cmsDictionary/virify' WHERE `id` ='dictionary_add';
