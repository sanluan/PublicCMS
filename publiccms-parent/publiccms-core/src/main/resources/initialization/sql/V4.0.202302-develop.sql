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
ALTER TABLE `cms_content_attribute` DROP COLUMN `extends_text`;
-- 2023-04-16 --
ALTER TABLE `cms_survey` ADD COLUMN  `allow_anonymous` tinyint(1) NOT NULL COMMENT '允许匿名' AFTER `end_date`;
ALTER TABLE `cms_vote` ADD COLUMN  `allow_anonymous` tinyint(1) NOT NULL COMMENT '允许匿名' AFTER `description`;
ALTER TABLE `cms_user_survey` ADD COLUMN  `anonymous` tinyint(1) NOT NULL COMMENT '匿名' AFTER `site_id`;
ALTER TABLE `cms_user_vote` ADD COLUMN  `anonymous` tinyint(1) NOT NULL COMMENT '匿名' AFTER `user_id`;
-- 05-09 --
CREATE TABLE `cms_user_collection`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户',
  `content_id` bigint(20) NOT NULL COMMENT '内容',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY (`user_id`, `content_id`),
  KEY `cms_user_collection_user_id`(`user_id`, `create_date`)
) COMMENT = '用户收藏表';
ALTER TABLE `cms_content` ADD COLUMN `collections` int(11) NOT NULL COMMENT '收藏数' AFTER `clicks`;
-- 07-02 --
ALTER TABLE `cms_place`
    ADD COLUMN `max_clicks` int(11) NOT NULL COMMENT '最大点击数' AFTER `clicks`,
    MODIFY COLUMN `status` int(11) NOT NULL COMMENT '状态：0、草稿 1、已发布 2、待审核 3、已下架' after `expiry_date`;