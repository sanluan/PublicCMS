-- 2022-04-25 --
UPDATE `sys_module` SET `url` = 'cmsPlace/metadata' WHERE `id` ='place_publish';
-- 2022-05-02 --
UPDATE `sys_module` SET `sort` = 4 WHERE `id` ='task_template_list';
UPDATE `sys_module` SET `sort` = 5 WHERE `id` ='webfile_list';
UPDATE `sys_module` SET `sort` = 2 WHERE `id` ='place_list';
INSERT INTO `sys_module` VALUES ('diy_list', 'cmsDiy/list', 'cmsDiy/region,cmsDiy/layout,cmsDiy/module,placeTemplate/lookupPlace,cmsCategoryType/lookup', 'icon-dashboard', 'file_menu', 1, 5);
INSERT INTO `sys_module_lang` VALUES ('diy_list', 'en', 'Page visualization management');
INSERT INTO `sys_module_lang` VALUES ('diy_list', 'ja', 'ページ視覚化管理');
INSERT INTO `sys_module_lang` VALUES ('diy_list', 'zh', '页面可视化管理');
ALTER TABLE `sys_user`
    CHANGE COLUMN `nick_name` `nickname` varchar(45) NOT NULL COMMENT '昵称' AFTER `weak_password`;
-- 2022-05-05 --
INSERT INTO `sys_module` VALUES ('page_diy', 'cmsPage/diy', 'cmsPage/style,cmsDiy/save', 'bi bi-palette', 'page_menu', 1, 3);
INSERT INTO `sys_module_lang` VALUES ('page_diy', 'en', 'Visualized page');
INSERT INTO `sys_module_lang` VALUES ('page_diy', 'ja', '視覚化されたページ');
INSERT INTO `sys_module_lang` VALUES ('page_diy', 'zh', '页面可视化');

-- 2022-05-10 --
ALTER TABLE `cms_content` 
    ADD COLUMN `update_user_id` bigint(20) DEFAULT NULL COMMENT '更新用户' AFTER `check_date`;
CREATE TABLE `cms_content_text_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content_id` bigint(20) NOT NULL COMMENT '内容',
  `field_name` varchar(100) NOT NULL COMMENT '字段名',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `user_id` bigint(20) NOT NULL COMMENT '修改用户',
  `text` longtext NOT NULL COMMENT '文本',
  PRIMARY KEY (`id`),
  KEY `cms_content_history_content_id` (`content_id`,`field_name`,`create_date`,`user_id`)
) COMMENT='内容扩展';
INSERT INTO `sys_module` VALUES ('content_text_history', 'cmsContentTextHistory/lookup', 'cmsContentTextHistory/use', NULL, 'content_add', 0, 0);
INSERT INTO `sys_module_lang` VALUES ('content_text_history', 'en', 'Modify records');
INSERT INTO `sys_module_lang` VALUES ('content_text_history', 'ja', 'レコードを変更する');
INSERT INTO `sys_module_lang` VALUES ('content_text_history', 'zh', '修改记录');
-- 2022-05-18 --
ALTER TABLE `visit_history`
    DROP INDEX `visit_visit_date`,
    DROP INDEX `visit_session_id`,
    ADD INDEX `visit_history_visit_date` (`site_id`,`visit_date`,`visit_hour`),
    ADD INDEX `visit_history_session_id` (`site_id`,`session_id`,`visit_date`,`create_date`);
ALTER TABLE `visit_item`
    DROP INDEX `visit_session_id`,
    ADD INDEX `visit_item_session_id` (`site_id`,`visit_date`,`item_type`, `item_id`, `pv`);
ALTER TABLE `visit_session`
    DROP INDEX `visit_visit_date`,
    ADD INDEX `visit_session_visit_date` (`site_id`,`visit_date`,`session_id`,`last_visit_date`);
ALTER TABLE `visit_url`
    DROP INDEX `visit_session_id`,
    ADD INDEX `visit_url_pv`(`site_id`, `visit_date`, `pv`);
-- 2022-05-19 --
INSERT INTO `sys_module` VALUES ('page_diy_buttons', 'cmsDiy/buttons', NULL, NULL, 'page_diy', 0, 3);
INSERT INTO `sys_module` VALUES ('page_diy_preview', 'cmsDiy/preview', NULL, NULL, 'page_diy', 0, 2);
INSERT INTO `sys_module` VALUES ('page_diy_region', 'cmsPage/region', NULL, NULL, 'page_diy', 0, 1);
INSERT INTO `sys_module_lang` VALUES ('page_diy_buttons', 'en', 'Button');
INSERT INTO `sys_module_lang` VALUES ('page_diy_buttons', 'ja', 'ボタン');
INSERT INTO `sys_module_lang` VALUES ('page_diy_buttons', 'zh', '按钮');
INSERT INTO `sys_module_lang` VALUES ('page_diy_preview', 'en', 'Quick Maintenance');
INSERT INTO `sys_module_lang` VALUES ('page_diy_preview', 'ja', 'クイックメンテナンス');
INSERT INTO `sys_module_lang` VALUES ('page_diy_preview', 'zh', '快捷维护');
INSERT INTO `sys_module_lang` VALUES ('page_diy_region', 'en', 'Region');
INSERT INTO `sys_module_lang` VALUES ('page_diy_region', 'ja', '領域');
INSERT INTO `sys_module_lang` VALUES ('page_diy_region', 'zh', '区域');
-- 2022-05-22 --
ALTER TABLE `cms_place` COMMENT = '推荐位数据';
-- 2022-05-31 --
INSERT INTO `sys_module` VALUES ('template_search', 'cmsTemplate/search', 'cmsTemplate/replace', NULL, 'template_list', 0, 0);
INSERT INTO `sys_module_lang` VALUES ('template_search', 'en', 'Search');
INSERT INTO `sys_module_lang` VALUES ('template_search', 'ja', '検索');
INSERT INTO `sys_module_lang` VALUES ('template_search', 'zh', '搜索');
-- 2022-07-04 --
ALTER TABLE `visit_history` ADD INDEX  `visit_history_create_date` (`create_date`, `site_id`, `session_id`, `visit_date`, `ip`);
ALTER TABLE `cms_content` 
    DROP INDEX `cms_content_check_date`,
    DROP INDEX `cms_content_score`,
    DROP INDEX `cms_content_only_url`,
    ADD INDEX `cms_content_disabled` (`site_id`, `parent_id`, `disabled`, `sort`, `publish_date`);
-- 2022-07-05 --
DROP TABLE IF EXISTS `sys_site_datasource`;
DROP TABLE IF EXISTS `sys_datasource`;
-- 2022-07-06 --
ALTER TABLE `cms_content` 
    DROP INDEX `cms_content_status`,
    DROP INDEX `cms_content_disabled`,
    ADD INDEX `cms_content_parent_id` (`site_id`, `parent_id`, `disabled`, `sort`, `publish_date`),
    ADD INDEX `cms_content_disabled` (`site_id`, `disabled`, `sort`, `publish_date`),
    ADD INDEX `cms_content_status` (`site_id`, `status`, `parent_id`, `category_id`, `disabled`, `model_id`, `publish_date`, `expiry_date`, `sort`);
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
    ADD INDEX `trade_payment_create_date` (`site_id`, `create_date`);
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