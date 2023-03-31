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
    ADD INDEX `visit_url_pv` (`site_id`, `visit_date`, `pv`);
-- 2023-03-24 --
UPDATE `sys_module` SET `authorized_url`= 'cmsCategory/addMore,cmsCategory/virify,cmsCategory/rebuildChildIds,cmsCategory/batchPublish,cmsCategory/batchCopy,cmsCategory/batchCreate,cmsCategory/batchSave,cmsCategory/seo,cmsCategory/saveSeo,cmsCategory/categoryPath,cmsCategory/contentPath,cmsCategory/save' WHERE `id` ='category_add';
ALTER TABLE `visit_history`
    ADD COLUMN `user_id` bigint(20) DEFAULT NULL COMMENT '用户' AFTER `visit_hour`,
    DROP INDEX `visit_history_create_date`,
    DROP INDEX `visit_history_session_id`,
    ADD INDEX `visit_history_create_date` (`site_id`, `create_date`, `session_id`, `ip`),
    ADD INDEX `visit_history_user_id` (`site_id`, `create_date`, `user_id`),
    ADD INDEX `visit_history_item_type` (`site_id`, `visit_date`, `item_type`);
-- 2023-03-26 --
UPDATE `sys_module` SET `attached` =  'bi bi-code-square' WHERE `id` ='template_list';
UPDATE `sys_module` SET `authorized_url`= 'cmsModel/categoryList,cmsModel/template' WHERE `id` ='model_list';
-- 2023-03-27 --
ALTER TABLE `cms_content` DROP COLUMN `contribute`;
ALTER TABLE `sys_task` ADD COLUMN `multi_node` tinyint(1) NOT NULL COMMENT '多节点执行' AFTER `status`;
-- 2023-03-31 --
UPDATE `sys_module_lang` SET `value` =  'Export' WHERE `lang` ='en' and module_id = 'dictionary_export';
UPDATE `sys_module_lang` SET `value` =  '輸出' WHERE `lang` ='ja' and module_id = 'dictionary_export';
UPDATE `sys_module_lang` SET `value` =  '导出' WHERE `lang` ='zh' and module_id = 'dictionary_export';
DELETE FROM `sys_module_lang` WHERE `module_id` ='refund_view';
INSERT INTO `sys_module_lang` VALUES ('record_view', 'en', 'View');
INSERT INTO `sys_module_lang` VALUES ('record_view', 'ja', '見る');
INSERT INTO `sys_module_lang` VALUES ('record_view', 'zh', '查看');