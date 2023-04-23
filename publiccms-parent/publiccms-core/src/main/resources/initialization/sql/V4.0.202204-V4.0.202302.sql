-- 2022-04-25 --
UPDATE `sys_module` SET `url` = 'cmsPlace/metadata' WHERE `id` ='place_publish';
-- 2022-05-02 --
UPDATE `sys_module` SET `sort` = 4 WHERE `id` ='task_template_list';
UPDATE `sys_module` SET `sort` = 5 WHERE `id` ='webfile_list';
UPDATE `sys_module` SET `sort` = 2 WHERE `id` ='place_list';
INSERT INTO `sys_module` VALUES('diy_list', 'cmsDiy/list', 'cmsDiy/region,cmsDiy/layout,cmsDiy/module,placeTemplate/lookupPlace,cmsCategoryType/lookup', 'icon-dashboard', 'file_menu', 1, 5);
INSERT INTO `sys_module_lang` VALUES('diy_list', 'en', 'Page visualization management');
INSERT INTO `sys_module_lang` VALUES('diy_list', 'ja', 'ページ視覚化管理');
INSERT INTO `sys_module_lang` VALUES('diy_list', 'zh', '页面可视化管理');
ALTER TABLE `sys_user`
    CHANGE COLUMN `nick_name` `nickname` varchar(45) NOT NULL COMMENT '昵称' AFTER `weak_password`;
-- 2022-05-05 --
INSERT INTO `sys_module` VALUES('page_diy', 'cmsPage/diy', 'cmsPage/region,cmsDiy/save', 'bi bi-palette', 'page_menu', 1, 3);
INSERT INTO `sys_module_lang` VALUES('page_diy', 'en', 'Visualized page');
INSERT INTO `sys_module_lang` VALUES('page_diy', 'ja', '視覚化されたページ');
INSERT INTO `sys_module_lang` VALUES('page_diy', 'zh', '页面可视化');

-- 2022-05-10 --
ALTER TABLE `cms_content`
    ADD COLUMN `update_user_id` bigint(20) DEFAULT NULL COMMENT '更新用户' AFTER `check_date`;
CREATE TABLE `cms_content_text_history`(
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content_id` bigint(20) NOT NULL COMMENT '内容',
  `field_name` varchar(50) NOT NULL COMMENT '字段名',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `user_id` bigint(20) NOT NULL COMMENT '修改用户',
  `text` longtext NOT NULL COMMENT '文本',
  PRIMARY KEY(`id`),
  KEY `cms_content_history_content_id`(`content_id`,`field_name`,`create_date`,`user_id`)
) COMMENT='内容扩展';
-- 2022-05-18 --
ALTER TABLE `visit_history`
    DROP INDEX `visit_visit_date`,
    DROP INDEX `visit_session_id`,
    ADD INDEX `visit_history_visit_date`(`site_id`,`visit_date`,`visit_hour`),
    ADD INDEX `visit_history_session_id`(`site_id`,`session_id`,`visit_date`,`create_date`);
ALTER TABLE `visit_item`
    DROP INDEX `visit_session_id`,
    ADD INDEX `visit_item_session_id`(`site_id`,`visit_date`,`item_type`, `item_id`, `pv`);
ALTER TABLE `visit_session`
    DROP INDEX `visit_visit_date`,
    ADD INDEX `visit_session_visit_date`(`site_id`,`visit_date`,`session_id`,`last_visit_date`);
ALTER TABLE `visit_url`
    DROP INDEX `visit_session_id`,
    ADD INDEX `visit_url_pv`(`site_id`, `visit_date`, `pv`);
-- 2022-05-19 --
INSERT INTO `sys_module` VALUES('page_diy_buttons', 'cmsDiy/buttons', NULL, NULL, 'page_diy', 0, 3);
INSERT INTO `sys_module` VALUES('page_diy_preview', 'cmsDiy/preview', NULL, NULL, 'page_diy', 0, 2);
INSERT INTO `sys_module` VALUES('page_diy_region', 'cmsPage/region', NULL, NULL, 'page_diy', 0, 1);
INSERT INTO `sys_module_lang` VALUES('page_diy_buttons', 'en', 'Button');
INSERT INTO `sys_module_lang` VALUES('page_diy_buttons', 'ja', 'ボタン');
INSERT INTO `sys_module_lang` VALUES('page_diy_buttons', 'zh', '按钮');
INSERT INTO `sys_module_lang` VALUES('page_diy_preview', 'en', 'Quick Maintenance');
INSERT INTO `sys_module_lang` VALUES('page_diy_preview', 'ja', 'クイックメンテナンス');
INSERT INTO `sys_module_lang` VALUES('page_diy_preview', 'zh', '快捷维护');
INSERT INTO `sys_module_lang` VALUES('page_diy_region', 'en', 'Region');
INSERT INTO `sys_module_lang` VALUES('page_diy_region', 'ja', '領域');
INSERT INTO `sys_module_lang` VALUES('page_diy_region', 'zh', '区域');
-- 2022-05-22 --
ALTER TABLE `cms_place` COMMENT = '推荐位数据';
-- 2022-05-31 --
INSERT INTO `sys_module` VALUES('template_search', 'cmsTemplate/search', 'cmsTemplate/replace', NULL, 'template_list', 0, 0);
INSERT INTO `sys_module_lang` VALUES('template_search', 'en', 'Search');
INSERT INTO `sys_module_lang` VALUES('template_search', 'ja', '検索');
INSERT INTO `sys_module_lang` VALUES('template_search', 'zh', '搜索');
-- 2022-07-04 --
ALTER TABLE `visit_history` ADD INDEX  `visit_history_create_date`(`create_date`, `site_id`, `session_id`, `visit_date`, `ip`);
ALTER TABLE `cms_content`
    DROP INDEX `cms_content_check_date`,
    DROP INDEX `cms_content_score`,
    DROP INDEX `cms_content_only_url`,
    ADD INDEX `cms_content_disabled`(`site_id`, `parent_id`, `disabled`, `sort`, `publish_date`);
-- 2022-07-05 --
DROP TABLE IF EXISTS `sys_site_datasource`;
DROP TABLE IF EXISTS `sys_datasource`;
-- 2022-07-06 --
ALTER TABLE `cms_content`
    DROP INDEX `cms_content_status`,
    DROP INDEX `cms_content_disabled`,
    ADD INDEX `cms_content_parent_id`(`site_id`, `parent_id`, `disabled`, `sort`, `publish_date`),
    ADD INDEX `cms_content_disabled`(`site_id`, `disabled`, `sort`, `publish_date`),
    ADD INDEX `cms_content_status`(`site_id`, `status`, `parent_id`, `category_id`, `disabled`, `model_id`, `publish_date`, `expiry_date`, `sort`);
ALTER TABLE `cms_comment`
    DROP INDEX `cms_comment_update_date`,
    ADD INDEX `cms_comment_user_id`(`site_id`, `user_id`, `status`, `disabled`);
ALTER TABLE `sys_app_token`
    DROP INDEX `sys_app_token_app_id`,
    DROP INDEX `sys_app_token_create_date`,
    ADD INDEX `sys_app_token_app_id`(`app_id`, `create_date`),
    ADD INDEX `sys_app_token_expiry_date`(`expiry_date`);
ALTER TABLE `sys_email_token`
    DROP INDEX `sys_email_token_user_id`,
    DROP INDEX `sys_email_token_create_date`,
    ADD INDEX `sys_email_token_expiry_date`(`expiry_date`),
    ADD INDEX `sys_email_token_user_id`(`user_id`, `create_date`);
ALTER TABLE `sys_user_token`
    DROP INDEX `sys_user_token_site_id`,
    DROP INDEX `sys_user_token_user_id`,
    DROP INDEX `sys_user_token_create_date`,
    DROP INDEX `sys_user_token_channel`,
    ADD INDEX `sys_user_token_site_id`(`site_id`, `user_id`, `create_date`),
    ADD INDEX `sys_user_token_expiry_date`(`expiry_date`),
    ADD INDEX `sys_user_token_user_id`(`user_id`);
-- 2022-07-07 --
ALTER TABLE `sys_user`
    DROP INDEX `sys_user_name`,
    DROP INDEX `sys_user_email`,
    DROP INDEX `sys_user_disabled`,
    DROP INDEX `sys_user_dept_id`,
    DROP INDEX `sys_user_lastLoginDate`,
    DROP INDEX `sys_user_email_checked`,
    DROP INDEX `sys_user_site_id`,
    ADD UNIQUE INDEX `sys_user_name`(`site_id`, `name`),
    ADD INDEX `sys_user_email`(`site_id`, `email`, `email_checked`),
    ADD INDEX `sys_user_disabled`(`site_id`, `disabled`),
    ADD INDEX `sys_user_dept_id`(`site_id`, `registered_date`, `disabled`);
ALTER TABLE `sys_app_client`
    ADD INDEX `sys_app_client_disabled`(`site_id`, `disabled`, `create_date`);
ALTER TABLE `trade_order`
    DROP INDEX `trade_order_create_date`,
    ADD INDEX `trade_order_create_date`(`site_id`, `create_date`);
ALTER TABLE `trade_payment`
    DROP INDEX `trade_payment_account_type`,
    DROP INDEX `trade_payment_trade_type`,
    DROP INDEX `trade_payment_create_date`,
    ADD INDEX `trade_payment_account_type`(`site_id`, `account_type`, `account_serial_number`),
    ADD INDEX `trade_payment_trade_type`(`site_id`, `trade_type`, `serial_number`),
    ADD INDEX `trade_payment_create_date`(`site_id`, `create_date`);
ALTER TABLE `trade_payment_history`
    DROP INDEX `trade_payment_history_create_date`,
    ADD INDEX `trade_payment_history_create_date`(`site_id`, `create_date`);
ALTER TABLE `trade_order_history`
    DROP INDEX `trade_order_history_create_date`,
    ADD INDEX `trade_order_history_create_date`(`site_id`, `create_date`);
ALTER TABLE `trade_account_history`
    DROP INDEX `trade_account_history_create_date`,
    ADD INDEX `trade_account_history_create_date`(`site_id`, `create_date`);
ALTER TABLE `trade_refund`
    DROP INDEX `trade_refund_create_date`,
    DROP INDEX `trade_refund_user_id`,
    ADD INDEX `trade_refund_create_date`(`site_id`, `create_date`),
    ADD INDEX `trade_refund_user_id`(`site_id`,`user_id`,`status`);
ALTER TABLE `cms_vote_item`
    DROP INDEX `cms_vote_item_vote_id`,
    ADD INDEX `cms_vote_item_vote_id`(`vote_id`, `sort`);
ALTER TABLE `cms_user_score`
    CHANGE COLUMN `scores` `score` int(11) NOT NULL COMMENT '分数' AFTER `item_id`;
ALTER TABLE `cms_vote`
    CHANGE COLUMN `scores` `votes` int(11) NOT NULL COMMENT '总票数' AFTER `end_date`;
ALTER TABLE `cms_vote_item`
    CHANGE COLUMN `scores` `votes` int(11) NOT NULL COMMENT '票数' AFTER `title`;
ALTER TABLE `cms_vote_item`
    DROP INDEX `cms_vote_item_vote_id`,
    ADD INDEX `cms_vote_item_vote_id`(`vote_id`, `sort`);
ALTER TABLE `cms_user_survey_question`
    DROP INDEX `cms_user_survey_site_id`,
    ADD INDEX `cms_user_survey_question_site_id`(`site_id`, `survey_id`, `create_date`);
ALTER TABLE `cms_user_score`
    DROP INDEX `cms_user_score_user_id`,
    ADD INDEX `cms_user_score_user_id`(`user_id`, `item_type`, `create_date`);
-- 2022-07-10 --
ALTER TABLE `cms_word`
    DROP INDEX `cms_word_name`,
    DROP INDEX `cms_word_hidden`,
    DROP INDEX `cms_word_create_date`,
    DROP INDEX `cms_word_search_count`,
    DROP INDEX `cms_word_site_id`,
    ADD UNIQUE INDEX `cms_word_name`(`site_id`, `name`),
    ADD INDEX `cms_word_hidden`(`site_id`, `hidden`);
-- 2022-07-15 --
ALTER TABLE `cms_content_attribute`
    ADD COLUMN `dictionary_values` text NULL COMMENT '数据字典值' AFTER `search_text`,
    ADD COLUMN `files_text` text NULL COMMENT '附件文本' AFTER `dictionary_values`,
    ADD COLUMN `min_price` decimal(10, 2) NULL COMMENT '最低价格' AFTER `files_text`,
    ADD COLUMN `max_price` decimal(10, 2) NULL COMMENT '最高价格' AFTER `min_price`;
update cms_content_attribute a set a.dictionary_values =(select dictionary_values from cms_content b where a.content_id = b.id);
ALTER TABLE `cms_content`
    DROP COLUMN `dictionary_values`;
ALTER TABLE `cms_content`
    MODIFY COLUMN `quote_content_id` bigint(20) NULL DEFAULT NULL COMMENT '引用内容(当父内容不为空时为顶级内容)' AFTER `parent_id`;
-- 2022-07-16 --
ALTER TABLE `cms_content_attribute`
    ADD COLUMN `extends_text` text NULL COMMENT '扩展文本' AFTER `dictionary_values`,
    ADD COLUMN `extends_fields` text NULL COMMENT '扩展文本字段' AFTER `extends_text`;
-- 2022-07-23 --
ALTER TABLE `cms_content_related`
    ADD COLUMN `relation_type` varchar(20) NULL COMMENT '关系类型' AFTER `content_id`,
    ADD COLUMN `relation` varchar(50) NULL COMMENT '关系' AFTER `relation_type`,
    DROP INDEX `cms_content_related_content_id`,
    DROP INDEX `cms_content_related_related_content_id`,
    ADD INDEX `cms_content_related_content_id`(`content_id`, `relation_type`, `relation`, `sort`),
    ADD INDEX `cms_content_related_related_content_id`(`related_content_id`, `relation_type`, `relation`);
UPDATE `sys_module` SET `authorized_url` = 'cmsPlace/push,cmsPlace/add,cmsPlace/save,cmsContent/unrelated,cmsPlace/delete' WHERE `id` ='content_push';
-- 2022-08-03 --
ALTER TABLE `cms_content`
    ADD INDEX `cms_content_category_id`(`site_id`, `category_id`, `parent_id`, `disabled`);
-- 2022-08-05 --
UPDATE `sys_module` SET `sort` = 2,`parent_id`='content' WHERE `id` ='trade_menu';
UPDATE `sys_module` SET `sort` = 2,`parent_id`='page' WHERE `id` ='visit_menu';
-- 2022-08-14 --
UPDATE `sys_module` SET `sort` = 0,`parent_id`='visit_menu' WHERE `id` ='report_visit';
UPDATE `log_upload` SET file_type = 'document' WHERE file_path like '%.doc' or file_path like '%.docx' or file_path like '%.xls' or file_path like '%.xlsx' or file_path like '%.ppt' or file_path like '%.pptx' or file_path like '%.pdf' or file_path like '%.txt' or file_path like '%.md' or file_path like '%.xml' or file_path like '%.ofd';
-- 2022-08-17 --
UPDATE `sys_module` SET `authorized_url` = 'cmsCategory/addMore,cmsCategory/virify,cmsCategory/rebuildChildIds,cmsCategory/batchPublish,cmsTemplate/lookup,cmsCategory/categoryPath,cmsCategory/contentPath,file/doUpload,cmsDictionary/lookup,cmsCategory/save' WHERE `id` ='category_add';
UPDATE `sys_module` SET `authorized_url` = 'cmsModel/save,cmsTemplate/lookup,cmsModel/rebuildSearchText,cmsModel/batchPublish,cmsDictionary/lookup' WHERE `id` ='model_add';
ALTER TABLE `cms_content`
    DROP INDEX `cms_content_disabled`,
    ADD INDEX `cms_content_disabled`(`site_id`, `disabled`, `category_id`, `model_id`);
-- 2022-09-26 --
INSERT INTO `sys_module` VALUES('file_history', 'cmsFileHistory/list', 'cmsFileHistory/use,cmsFileHistory/compare', NULL, 'template_list', 0, 0);
INSERT INTO `sys_module` VALUES('file_recycle', 'cmsFileBackup/list', 'cmsFileBackup/content,cmsFileBackup/recycle', NULL, 'template_list', 0, 0);
INSERT INTO `sys_module_lang` VALUES('file_history', 'en', 'File modification history');
INSERT INTO `sys_module_lang` VALUES('file_history', 'ja', 'ファイル変更履歴');
INSERT INTO `sys_module_lang` VALUES('file_history', 'zh', '文件修改历史');
INSERT INTO `sys_module_lang` VALUES('file_recycle', 'en', 'File recycle bin');
INSERT INTO `sys_module_lang` VALUES('file_recycle', 'ja', 'ファイルのごみ箱');
INSERT INTO `sys_module_lang` VALUES('file_recycle', 'zh', '文件回收站');
-- 2022-10-03 --
RENAME TABLE `sys_dept_page` TO `sys_dept_item`;
ALTER TABLE `sys_dept_item`
    CHANGE COLUMN `page` `item_id` varchar(100) NOT NULL COMMENT '项目' AFTER `dept_id`,
    ADD COLUMN `item_type` varchar(50) NOT NULL DEFAULT 'page' COMMENT '项目类型' AFTER `dept_id`,
    DROP PRIMARY KEY,
    ADD PRIMARY KEY(`dept_id`, `item_type`, `item_id`),
    DROP INDEX `sys_dept_page_page`,
    ADD INDEX `sys_dept_item_item_id`(`item_type`, `item_id`),
    COMMENT = '部门数据项';
INSERT INTO sys_dept_item SELECT dept_id,'config',config FROM sys_dept_config;
INSERT INTO sys_dept_item SELECT dept_id,'category',category_id FROM sys_dept_category;
DROP TABLE `sys_dept_category`;
DROP TABLE `sys_dept_config`;
-- 2022-10-14 --
ALTER TABLE `sys_user` CHANGE COLUMN `password` `password` varchar(150) NOT NULL COMMENT '混淆码.密码' AFTER `name`;
UPDATE `sys_user` SET `password` = CONCAT(`salt`,'.',`password`) WHERE `salt` IS NOT NULL AND `salt` != '';
ALTER TABLE `sys_user` DROP COLUMN `salt`;
-- 2022-11-08 --
UPDATE `sys_module` SET `authorized_url`= 'cmsWebFile/doUpload,cmsWebFile/uploadIco,cmsWebFile/doUpload,cmsWebFile/doUploadIco,cmsWebFile/check' WHERE `id` ='webfile_upload';
-- 2022-11-18 --
ALTER TABLE `log_operate`
    CHANGE COLUMN `content` `content` text default NULL COMMENT '内容',
    DROP INDEX `log_operate_site_id`,
    DROP INDEX `log_operate_create_date`,
    DROP INDEX `log_operate_user_id`,
    DROP INDEX `log_operate_operate`,
    DROP INDEX `log_operate_ip`,
    DROP INDEX `log_operate_channel`,
    ADD INDEX `log_operate_user_id`(`site_id`, `user_id`, `dept_id`),
    ADD INDEX `log_operate_operate`(`site_id`, `operate`, `create_date`),
    ADD INDEX `log_operate_ip`(`site_id`, `ip`, `create_date`),
    ADD INDEX `log_operate_channel`(`site_id`, `channel`, `create_date`);
ALTER TABLE `log_task`
    DROP INDEX `log_task_site_id`,
    DROP INDEX `log_task_task_id`,
    DROP INDEX `log_task_success`,
    DROP INDEX `log_task_begintime`,
    ADD INDEX `log_task_task_id`(`site_id`, `task_id`, `begintime`),
    ADD INDEX `log_task_success`(`site_id`, `success`, `begintime`);
ALTER TABLE `log_upload`
    DROP INDEX `log_upload_user_id`,
    DROP INDEX `log_upload_create_date`,
    DROP INDEX `log_upload_ip`,
    DROP INDEX `log_upload_site_id`,
    DROP INDEX `log_upload_channel`,
    DROP INDEX `log_upload_file_type`,
    DROP INDEX `log_upload_file_size`,
    ADD INDEX `log_upload_user_id`(`site_id`, `user_id`, `create_date`),
    ADD INDEX `log_upload_ip`(`site_id`, `ip`, `create_date`),
    ADD INDEX `log_upload_channel`(`site_id`, `channel`, `create_date`),
    ADD INDEX `log_upload_file_type`(`site_id`, `file_type`, `file_size`);
ALTER TABLE `log_login`
    DROP INDEX `log_login_result`,
    DROP INDEX `log_login_user_id`,
    DROP INDEX `log_login_create_date`,
    DROP INDEX `log_login_ip`,
    DROP INDEX `log_login_site_id`,
    DROP INDEX `log_login_channel`,
    ADD INDEX `log_login_result`(`site_id`, `result`, `create_date`),
    ADD INDEX `log_login_user_id`(`site_id`, `user_id`, `create_date`),
    ADD INDEX `log_login_ip`(`site_id`, `ip`, `create_date`),
    ADD INDEX `log_login_channel`(`site_id`, `channel`, `create_date`);
-- 2022-11-22 --
UPDATE `sys_module` SET `url`='cmsContent/uncheck_list',`parent_id`='content_menu',`attached`='icon-check-sign',`menu`=1,`sort`=1 WHERE `id` = 'content_check';
INSERT INTO `sys_module` VALUES ('content_search', 'cmsContent/search', 'cmsContent/view', 'icon-search', 'content_menu', 1, 2);
UPDATE `sys_module` SET `sort`=`sort` + 2 WHERE `id` in('comment_list','category_list','tag_list','product_list','content_vote','survey_list','word_list','content_recycle_list');
INSERT INTO `sys_module_lang` VALUES ('content_search', 'en', 'Content search');
INSERT INTO `sys_module_lang` VALUES ('content_search', 'ja', 'コンテンツ検索');
INSERT INTO `sys_module_lang` VALUES ('content_search', 'zh', '内容搜索');
UPDATE `sys_module_lang` SET `value` = 'Content check' WHERE `module_id` = 'content_check' AND `lang` = 'en';
UPDATE `sys_module_lang` SET `value` = '内容の審査' WHERE `module_id` = 'content_check' AND `lang` = 'ja';
UPDATE `sys_module_lang` SET `value` = '内容审核' WHERE `module_id` = 'content_check' AND `lang` = 'zh';
UPDATE `sys_module` SET `id` = 'vote_list' WHERE `id` = 'content_vote';
UPDATE `sys_module` SET `id` = 'vote_add' WHERE `id` = 'content_vote_add';
UPDATE `sys_module` SET `id` = 'vote_delete' WHERE `id` = 'content_vote_delete';
UPDATE `sys_module` SET `id` = 'vote_view' WHERE `id` = 'content_vote_view';
UPDATE `sys_module` SET `attached` = 'bi bi-search-heart' WHERE `id` = 'word_list';
UPDATE `sys_module_lang` SET `module_id` = 'vote_list' WHERE `module_id` = 'content_vote';
UPDATE `sys_module_lang` SET `module_id` = 'vote_add' WHERE `module_id` = 'content_vote_add';
UPDATE `sys_module_lang` SET `module_id` = 'vote_delete' WHERE `module_id` = 'content_vote_delete';
UPDATE `sys_module_lang` SET `module_id` = 'vote_view' WHERE `module_id` = 'content_vote_view';
-- 2022-12-02 --
INSERT INTO `sys_module` VALUES ('content_select_survey', 'cmsSurvey/lookup', NULL, NULL, 'content_add', 0, 0);
INSERT INTO `sys_module_lang` VALUES ('content_select_survey', 'en', 'Select 问卷');
INSERT INTO `sys_module_lang` VALUES ('content_select_survey', 'ja', 'アンケート選択');
INSERT INTO `sys_module_lang` VALUES ('content_select_survey', 'zh', '选择问卷');
-- 2022-12-09 --
ALTER TABLE `cms_place` ADD COLUMN `description` varchar(300) default NULL COMMENT '简介' AFTER `url`;
-- 2022-12-11 --
RENAME TABLE `cms_content_text_history` TO `cms_editor_history`;
ALTER TABLE `cms_editor_history`
    ADD COLUMN `item_type` varchar(50) DEFAULT NULL COMMENT '数据类型' AFTER `id`,
    CHANGE COLUMN `content_id` `item_id` varchar(50) NOT NULL COMMENT '数据id',
    DROP INDEX `cms_content_history_content_id`,
    ADD INDEX `cms_editor_history_item_id` (`item_type`,`item_id`, `field_name`, `create_date`);
UPDATE `cms_editor_history` set item_type = 'content';
ALTER TABLE `cms_editor_history` CHANGE COLUMN `item_type` `item_type` varchar(50) NOT NULL COMMENT '数据类型';
DELETE FROM `sys_module` WHERE `id` = 'content_text_history';
DELETE FROM `sys_module_lang` WHERE `module_id` = 'content_text_history';
INSERT INTO `sys_module` VALUES('editor_history', 'cmsEditorHistory/lookup', 'cmsEditorHistory/use,cmsEditorHistory/compare', NULL, 'content_add', 0, 0);
INSERT INTO `sys_module_lang` VALUES ('editor_history', 'en', 'Modify records');
INSERT INTO `sys_module_lang` VALUES ('editor_history', 'ja', 'レコードを変更する');
INSERT INTO `sys_module_lang` VALUES ('editor_history', 'zh', '修改记录');
UPDATE `sys_module` SET `authorized_url`= 'sysConfigData/save,sysConfigData/edit,cmsEditorHistory/lookup,cmsEditorHistory/use,cmsEditorHistory/compare' WHERE `id` ='config_data_edit';
UPDATE `sys_module` SET `authorized_url`= 'cmsPage/save,file/doUpload,cmsPage/clearCache,cmsEditorHistory/lookup,cmsEditorHistory/use,cmsEditorHistory/compare' WHERE `id` ='page_save';
UPDATE `sys_module` SET `authorized_url`= 'cmsContent/lookup,cmsPlace/lookup,cmsPlace/lookup_content_list,file/doUpload,cmsPlace/save,cmsEditorHistory/lookup,cmsEditorHistory/use,cmsEditorHistory/compare' WHERE `id` ='place_add';
UPDATE `sys_module` SET `authorized_url`= 'cmsCategory/addMore,cmsCategory/virify,cmsCategory/rebuildChildIds,cmsCategory/batchPublish,cmsTemplate/lookup,cmsCategory/categoryPath,cmsCategory/contentPath,file/doUpload,cmsDictionary/lookup,cmsCategory/save,cmsEditorHistory/lookup,cmsEditorHistory/use,cmsEditorHistory/compare' WHERE `id` ='category_add';
-- 2022-12-12 --
ALTER TABLE `cms_category_model`
    ADD COLUMN `site_id` smallint(6) NOT NULL COMMENT '站点' AFTER `model_id`,
    ADD INDEX `cms_category_model_site_id`(`site_id`, `model_id`);
UPDATE `cms_category_model` cm SET cm.`site_id` = (SELECT c.`site_id` FROM `cms_category` c WHERE c.`id`=cm.`category_id`);
ALTER TABLE `cms_editor_history`
    ADD COLUMN `site_id` smallint(6) NOT NULL COMMENT '站点' AFTER `id`,
    DROP INDEX `cms_editor_history_item_id`,
    ADD INDEX `cms_editor_history_item_id`(`site_id`, `item_type`, `item_id`, `field_name`, `create_date`);
DELETE FROM `cms_editor_history` WHERE `item_id` NOT IN(select `id` from `cms_content`);
UPDATE `cms_editor_history` eh SET eh.`site_id` = (SELECT c.`site_id` FROM `cms_content` c WHERE c.`id`= eh.`item_id` and eh.`item_type`='content');
ALTER TABLE `cms_user_survey`
    ADD COLUMN `ip` varchar(130) NOT NULL COMMENT 'IP' AFTER `score`;
ALTER TABLE `cms_user_score`
    ADD COLUMN `ip` varchar(130) NOT NULL COMMENT 'IP' AFTER `score`;
ALTER TABLE `cms_comment`
    ADD COLUMN `ip` varchar(130) NOT NULL COMMENT 'IP' AFTER `content_id`;
ALTER TABLE `cms_word`
    ADD COLUMN `ip` varchar(130) NOT NULL COMMENT 'IP' AFTER `hidden`;
-- 2022-12-18 --
INSERT INTO `sys_module` VALUES ('category_export', NULL, 'cmsCategory/export', NULL, 'category_list', 0, 0);
INSERT INTO `sys_module` VALUES ('category_import', 'cmsCategory/import','cmsCategory/doImport' ,NULL, 'category_list', 0, 0);
UPDATE `sys_module` SET `url` = 'cmsContent/export', `authorized_url`= 'cmsContent/exportExcel,cmsContent/exportData' WHERE `id` ='content_export';
INSERT INTO `sys_module` VALUES ('content_import', 'cmsContent/import', 'cmsContent/doImport', NULL, 'content_list', 0, 0);
INSERT INTO `sys_module` VALUES ('dictionary_export', NULL, 'cmsDictionary/export', NULL, 'dictionary_list', 0, 0);
INSERT INTO `sys_module` VALUES ('dictionary_import', 'cmsDictionary/import', 'cmsDictionary/doImport', NULL, 'dictionary_list', 0, 0);

INSERT INTO `sys_module_lang` VALUES ('category_export', 'en', 'Export');
INSERT INTO `sys_module_lang` VALUES ('category_export', 'ja', '輸出');
INSERT INTO `sys_module_lang` VALUES ('category_export', 'zh', '导出');
INSERT INTO `sys_module_lang` VALUES ('category_import', 'en', 'Import');
INSERT INTO `sys_module_lang` VALUES ('category_import', 'ja', '導入');
INSERT INTO `sys_module_lang` VALUES ('category_import', 'zh', '导入');
INSERT INTO `sys_module_lang` VALUES ('content_import', 'en', 'Import');
INSERT INTO `sys_module_lang` VALUES ('content_import', 'ja', '導入');
INSERT INTO `sys_module_lang` VALUES ('content_import', 'zh', '导入');
INSERT INTO `sys_module_lang` VALUES ('dictionary_export', 'en', 'Import');
INSERT INTO `sys_module_lang` VALUES ('dictionary_export', 'ja', '導入');
INSERT INTO `sys_module_lang` VALUES ('dictionary_export', 'zh', '导入');
INSERT INTO `sys_module_lang` VALUES ('dictionary_import', 'en', 'Import');
INSERT INTO `sys_module_lang` VALUES ('dictionary_import', 'ja', '導入');
INSERT INTO `sys_module_lang` VALUES ('dictionary_import', 'zh', '导入');
-- 2022-12-21 --
UPDATE `sys_module` SET `authorized_url`= 'cmsTemplate/saveMetaData,cmsTemplate/createDirectory' WHERE `id` ='template_metadata';
UPDATE `sys_module` SET `authorized_url`= 'placeTemplate/directory' WHERE `id` ='place_template_list';
-- 2022-12-22 --
UPDATE `sys_module` SET `authorized_url`= 'tradeAccount/save' WHERE `id` ='account_add';
UPDATE `sys_module` SET `authorized_url`= NULL  WHERE `id` in ('account_history_list','comment_list','content_recycle_list','dept_list','log_login','log_operate','log_upload','order_list','place_list','refund_list','tag_list');
UPDATE `sys_module` SET `authorized_url`= 'sysAppToken/issue,sysAppToken/delete,sysAppToken/list' WHERE `id` ='app_issue';
INSERT INTO `sys_module` VALUES ('app_view', 'sysApp/view', NULL, NULL, 'app_list', 0, 0);
INSERT INTO `sys_module_lang` VALUES ('app_view', 'en', 'View');
INSERT INTO `sys_module_lang` VALUES ('app_view', 'ja', '見る');
INSERT INTO `sys_module_lang` VALUES ('app_view', 'zh', '查看');
UPDATE `sys_module` SET `authorized_url`= 'cmsCategory/addMore,cmsCategory/virify,cmsCategory/rebuildChildIds,cmsCategory/batchPublish,cmsCategory/categoryPath,cmsCategory/contentPath,cmsCategory/save' WHERE `id` ='category_add';
UPDATE `sys_module` SET `authorized_url`= 'cmsCategory/move' WHERE `id` ='category_move';
UPDATE `sys_module` SET `authorized_url`= 'cmsCategoryType/save,cmsCategory/categoryPath,cmsCategory/contentPath' WHERE `id` ='category_type_add';
UPDATE `sys_module` SET `parent_id`= 'common',`sort` = 0 WHERE `id` ='clearcache';
INSERT INTO `sys_module` VALUES ('common', NULL, NULL, NULL, NULL, 0, 0);
INSERT INTO `sys_module_lang` VALUES ('common', 'en', 'Common');
INSERT INTO `sys_module_lang` VALUES ('common', 'ja', '共通');
INSERT INTO `sys_module_lang` VALUES ('common', 'zh', '通用');
UPDATE `sys_module` SET `authorized_url`= 'sysConfigData/save,sysConfigData/edit' WHERE `id` ='config_data_edit';
DELETE FROM `sys_module` WHERE `id` in ('config_list_data_dictionary','page_select_category','page_select_category_type','page_select_content','page_select_tag_type','page_select_template','page_select_user','place_template_place','place_template_webfile','template_data_dictionary','template_place');
DELETE FROM `sys_module_lang` WHERE `module_id` in ('config_list_data_dictionary','page_select_category','page_select_category_type','page_select_content','page_select_tag_type','page_select_template','page_select_user','place_template_place','place_template_webfile','template_data_dictionary','template_place');
UPDATE `sys_module` SET `authorized_url`= 'cmsContent/addMore,cmsContent/save' WHERE `id` ='content_add';
INSERT INTO `sys_module` VALUES ('content_change_model', 'cmsContent/changeModelParameters', 'cmsContent/changeModel', NULL, 'content_list', 0, 0);
INSERT INTO `sys_module_lang` VALUES ('content_change_model', 'en', 'Modify content model');
INSERT INTO `sys_module_lang` VALUES ('content_change_model', 'ja', 'コンテンツモデルを修正します');
INSERT INTO `sys_module_lang` VALUES ('content_change_model', 'zh', '修改内容模型');
UPDATE `sys_module` SET `authorized_url`= 'sysDept/save,sysDept/virify' WHERE `id` ='dept_add';
UPDATE `sys_module` SET `authorized_url`= 'cmsDiy/region,cmsDiy/layout,cmsDiy/module,cmsDiy/saveRegion,cmsDiy/saveLayout,cmsDiy/saveModule,cmsDiy/delete' WHERE `id` ='diy_list';
UPDATE `sys_module` SET `authorized_url`= 'sysDomain/saveConfig,cmsTemplate/directoryLookup' WHERE `id` ='domain_config';
INSERT INTO `sys_module` VALUES ('editor', NULL, 'ueditor,tinymce/upload,tinymce/imageList,ckeditor/upload,cmsWebFile/browse,kindeditor/upload,cmsWebFile/fileList,file/doImport', NULL, 'common', 0, 0);
INSERT INTO `sys_module_lang` VALUES ('editor', 'en', 'editor');
INSERT INTO `sys_module_lang` VALUES ('editor', 'ja', 'エディタ');
INSERT INTO `sys_module_lang` VALUES ('editor', 'zh', '编辑器');
UPDATE `sys_module` SET `parent_id`= 'common'  WHERE `id` ='editor_history';
INSERT INTO `sys_module` VALUES ('file_upload', NULL, 'file/doUpload,file/doBatchUpload', NULL, 'common', 0, 0);
INSERT INTO `sys_module_lang` VALUES ('file_upload', 'en', 'File upload');
INSERT INTO `sys_module_lang` VALUES ('file_upload', 'ja', 'ファイルのアップロードです');
INSERT INTO `sys_module_lang` VALUES ('file_upload', 'zh', '文件上传');
UPDATE `sys_module` SET `authorized_url`= 'cmsModel/save,cmsModel/rebuildSearchText,cmsModel/batchPublish' WHERE `id` ='model_add';
UPDATE `sys_module` SET `authorized_url`= 'cmsContent/save' WHERE `id` ='myself_content_add';
UPDATE `sys_module` SET `authorized_url`= 'cmsPage/metadata' WHERE `id` ='page_list';
UPDATE `sys_module` SET `authorized_url`= 'cmsPage/save,cmsPage/clearCache' WHERE `id` ='page_save';
UPDATE `sys_module` SET `authorized_url`= 'cmsPlace/lookup,cmsPlace/lookup_content_list,cmsPlace/save' WHERE `id` ='place_add';
UPDATE `sys_module` SET `id`= 'place_form',`parent_id`='common' WHERE `id` ='template_place_form';
UPDATE `sys_module_lang` SET `module_id`= 'place_form' WHERE `module_id` ='template_place_form';
UPDATE `sys_module` SET `authorized_url`= 'cmsTemplate/help,cmsTemplate/savePlace,cmsTemplate/chipLookup,cmsWebFile/lookup' WHERE `id` ='place_template_content';
UPDATE `sys_module` SET `id`= 'select_category',`parent_id`='common',`url`='cmsCategory/lookup',`authorized_url`='cmsCategory/lookupByModelId' WHERE `id` ='content_select_category';
UPDATE `sys_module_lang` SET `module_id`= 'select_category' WHERE `module_id` ='content_select_category';
UPDATE `sys_module` SET `id`= 'select_category_type',`parent_id`='common' WHERE `id` ='content_select_category_type';
UPDATE `sys_module_lang` SET `module_id`= 'select_category_type' WHERE `module_id` ='content_select_category_type';
UPDATE `sys_module` SET `id`= 'select_content',`parent_id`='common' WHERE `id` ='content_select_content';
UPDATE `sys_module_lang` SET `module_id`= 'select_content' WHERE `module_id` ='content_select_content';
INSERT INTO `sys_module` VALUES ('select_dept', 'sysDept/lookup', NULL, NULL, 'common', 0, 0);
INSERT INTO `sys_module_lang` VALUES ('select_dept', 'en', 'Select department');
INSERT INTO `sys_module_lang` VALUES ('select_dept', 'ja', '部門を選びます');
INSERT INTO `sys_module_lang` VALUES ('select_dept', 'zh', '选择部门');
UPDATE `sys_module` SET `id`= 'select_dictionary',`parent_id`='common' WHERE `id` ='place_template_data_dictionary';
UPDATE `sys_module_lang` SET `module_id`= 'select_dictionary' WHERE `module_id` ='place_template_data_dictionary';
UPDATE `sys_module` SET `id`= 'select_fragment',`parent_id`='common' WHERE `id` ='place_template_fragment';
UPDATE `sys_module_lang` SET `module_id`= 'select_fragment' WHERE `module_id` ='place_template_fragment';
INSERT INTO `sys_module` VALUES ('select_place', 'placeTemplate/lookup', 'placeTemplate/lookupPlace', NULL, 'common', 0, 0);
INSERT INTO `sys_module_lang` VALUES ('select_place', 'en', 'Select page fragment');
INSERT INTO `sys_module_lang` VALUES ('select_place', 'ja', 'ページのセグメントを選択します');
INSERT INTO `sys_module_lang` VALUES ('select_place', 'zh', '选择页面片段');
UPDATE `sys_module` SET `id`= 'select_survey',`parent_id`='common' WHERE `id` ='content_select_survey';
UPDATE `sys_module_lang` SET `module_id`= 'select_survey' WHERE `module_id` ='content_select_survey';
UPDATE `sys_module_lang` SET `value`= 'Select survey' WHERE `module_id` ='select_survey' AND `lang` = 'en';
UPDATE `sys_module` SET `id`= 'select_tag',`parent_id`='common' WHERE `id` ='content_select_tag';
UPDATE `sys_module_lang` SET `module_id`= 'select_tag' WHERE `module_id` ='content_select_tag';
UPDATE `sys_module` SET `id`= 'select_tag_type',`parent_id`='common' WHERE `id` ='content_select_tag_type';
UPDATE `sys_module_lang` SET `module_id`= 'select_tag_type' WHERE `module_id` ='content_select_tag_type';
UPDATE `sys_module` SET `id`= 'select_template',`parent_id`='common' WHERE `id` ='content_select_template';
UPDATE `sys_module_lang` SET `module_id`= 'select_template' WHERE `module_id` ='content_select_template';
UPDATE `sys_module` SET `id`= 'select_user',`parent_id`='common' WHERE `id` ='content_select_user';
UPDATE `sys_module_lang` SET `module_id`= 'select_user' WHERE `module_id` ='content_select_user';
UPDATE `sys_module` SET `id`= 'select_vote',`parent_id`='common' WHERE `id` ='content_select_vote';
UPDATE `sys_module_lang` SET `module_id`= 'select_vote' WHERE `module_id` ='content_select_vote';
UPDATE `sys_module` SET `authorized_url`= 'cmsTag/save' WHERE `id` ='tag_add';
UPDATE `sys_module` SET `authorized_url`= 'cmsTemplate/save,cmsTemplate/saveMetaData,cmsTemplate/chipLookup,cmsWebFile/lookup,cmsTemplate/help,cmsTemplate/upload,cmsTemplate/doUpload,cmsTemplate/export' WHERE `id` ='template_content';
UPDATE `sys_module` SET `id`= 'content_form',`parent_id`='common',`authorized_url`= 'cmsCategory/contributeForm' WHERE `id` ='template_content_form';
UPDATE `sys_module_lang` SET `module_id`= 'content_form' WHERE `module_id` ='template_content_form';
UPDATE `sys_module` SET `authorized_url`= 'cmsWord/hidden,cmsWord/delete,cmsWord/show,cmsWord/add,cmsWord/save' WHERE `id` ='word_list';
UPDATE `sys_module` SET `authorized_url`= 'tradeOrder/process,tradeOrder/invalid,tradeOrder/close,tradeOrder/export' WHERE `id` ='order_process';
UPDATE `sys_module` SET `authorized_url`= 'logUpload/delete' WHERE `id` ='log_upload';
UPDATE `sys_module` SET `authorized_url`= 'cmsTemplate/savePlaceMetaData,cmsTemplate/deletePlace,cmsTemplate/createDirectory' WHERE `id` ='place_template_metadata';
UPDATE `sys_module` SET `authorized_url`= 'tradePayment/refund,tradePayment/refuse' WHERE `id` ='payment_list';
UPDATE `sys_module` SET `authorized_url`= 'sysTask/pause,sysTask/interrupt' WHERE `id` ='task_pause';
UPDATE `sys_module` SET `authorized_url`= 'cmsWebFile/save,cmsWebFile/delete' WHERE `id` ='webfile_content';
UPDATE `sys_module` SET `authorized_url`= 'visit/view',`url` = 'visit/history' WHERE `id` ='visit_history';
-- 2023-01-05 --
ALTER TABLE `cms_dictionary_data`
    ADD COLUMN `sort` int(11) NOT NULL default '0' COMMENT '顺序' AFTER `text`;
-- 2023-01-08 --
ALTER TABLE `cms_category`
    ADD COLUMN `custom_path` tinyint(1) NOT NULL default 1 COMMENT '自定义访问路径' AFTER `code`,
    ADD COLUMN `custom_content_path` tinyint(1) NOT NULL default 1 COMMENT '自定义内容访问路径' AFTER `url`;
ALTER TABLE `cms_category_model`
    ADD COLUMN `custom_content_path` tinyint(1) NOT NULL default 0 COMMENT '自定义内容访问路径' AFTER `site_id`,
    ADD COLUMN `content_path` varchar(1000) default NULL COMMENT '内容路径'  AFTER `template_path`;
DROP TABLE IF EXISTS `sys_record`;
CREATE TABLE `sys_record` (
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `code` varchar(50) NOT NULL COMMENT '记录编码',
  `data` longtext NOT NULL COMMENT '数据',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `update_date` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY  (`site_id`, `code`),
  KEY `sys_record_site_id` (`site_id`,`create_date`)
) COMMENT='自定义记录';
INSERT INTO `sys_module` VALUES ('record_add', 'sysRecord/add', 'sysRecord/save', NULL, 'record_list', 0, 0);
INSERT INTO `sys_module` VALUES ('record_delete', NULL, 'sysRecord/delete', NULL, 'record_list', 0, 0);
INSERT INTO `sys_module` VALUES ('record_list', 'sysRecord/list', NULL, 'bi bi-receipt', 'system_menu', 1, 6);
INSERT INTO `sys_module_lang` VALUES ('record_add', 'en', 'Edit');
INSERT INTO `sys_module_lang` VALUES ('record_add', 'ja', '変更');
INSERT INTO `sys_module_lang` VALUES ('record_add', 'zh', '修改');
INSERT INTO `sys_module_lang` VALUES ('record_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('record_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('record_delete', 'zh', '删除');
INSERT INTO `sys_module_lang` VALUES ('record_list', 'en', 'Custom record management');
INSERT INTO `sys_module_lang` VALUES ('record_list', 'ja', 'カスタムレコード管理');
INSERT INTO `sys_module_lang` VALUES ('record_list', 'zh', '自定义记录管理');
-- 2023-01-12 --
UPDATE `sys_module` SET `authorized_url`= 'cmsModel/categoryList' WHERE `id` ='model_list';
UPDATE `sys_module` SET `authorized_url`= 'cmsTemplate/save,cmsTemplate/saveMetaData,cmsTemplate/chipLookup,cmsWebFile/lookup,cmsTemplate/help' WHERE `id` ='template_content';
INSERT INTO `sys_module` VALUES ('config_data_import', 'sysConfigData/import', 'sysConfigData/doImport', NULL, 'config_data_list', 0, 0);
INSERT INTO `sys_module` VALUES ('config_data_export', 'sysConfigData/export', 'cmsCategory/doImport', NULL, 'config_data_list', 0, 0);
INSERT INTO `sys_module` VALUES ('template_export', 'cmsTemplate/export', 'cmsTemplate/export', NULL, 'template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('template_import', 'cmsTemplate/upload', 'cmsTemplate/doUpload,cmsTemplate/import,cmsTemplate/doImport', NULL, 'template_list', 0, 0);
INSERT INTO `sys_module_lang` VALUES ('config_data_export', 'en', 'Export');
INSERT INTO `sys_module_lang` VALUES ('config_data_export', 'ja', '輸出');
INSERT INTO `sys_module_lang` VALUES ('config_data_export', 'zh', '导出');
INSERT INTO `sys_module_lang` VALUES ('config_data_import', 'en', 'Import');
INSERT INTO `sys_module_lang` VALUES ('config_data_import', 'ja', '導入');
INSERT INTO `sys_module_lang` VALUES ('config_data_import', 'zh', '导入');
INSERT INTO `sys_module_lang` VALUES ('template_export', 'en', 'Export');
INSERT INTO `sys_module_lang` VALUES ('template_export', 'ja', '輸出');
INSERT INTO `sys_module_lang` VALUES ('template_export', 'zh', '导出');
INSERT INTO `sys_module_lang` VALUES ('template_import', 'en', 'Import');
INSERT INTO `sys_module_lang` VALUES ('template_import', 'ja', '導入');
INSERT INTO `sys_module_lang` VALUES ('template_import', 'zh', '导入');
-- 2023-01-14 --
UPDATE `sys_module` SET `url`='cmsContent/workload',`attached`='bi bi-calendar-heart' WHERE `id` ='log_workload';
UPDATE `sys_module` SET `authorized_url`= 'cmsTemplate/doUpload,cmsTemplate/import,cmsTemplate/doImport,cmsTemplate/lookupSiteFile,cmsTemplate/viewsitefile,cmsTemplate/visitSitefileImage' WHERE `id` ='template_import';
-- 2023-01-15 --
INSERT INTO `sys_module` VALUES ('record_view', 'sysRecord/view', NULL, NULL, 'record_list', 0, 0);
INSERT INTO `sys_module_lang` VALUES ('record_view', 'en', 'View');
INSERT INTO `sys_module_lang` VALUES ('record_view', 'ja', '見る');
INSERT INTO `sys_module_lang` VALUES ('record_view', 'zh', '查看');
-- 2023-01-18 --
UPDATE `sys_module` SET `authorized_url`= 'cmsCategoryType/categoryList' WHERE `id` ='category_type_list';
-- 2023-02-17 --
UPDATE `sys_module` SET `attached`= NULL WHERE `attached` ='';
UPDATE `sys_module` set `authorized_url`= 'cmsContent/lookup_list,cmsContent/contentImage' WHERE `id` ='select_content';
-- 2023-02-27 --
ALTER TABLE `log_operate` MODIFY COLUMN `content` longtext default NULL COMMENT '内容' AFTER `create_date`;