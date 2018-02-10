-- 20170715 --
INSERT INTO `sys_moudle` VALUES ('125', '撤销审核', null, 'cmsContent/uncheck', null, '12', '0', '0');
DROP TABLE IF EXISTS `home_dialog`;
DROP TABLE IF EXISTS `home_message`;
DROP TABLE IF EXISTS `home_group_active`;
-- 20170804 --
UPDATE sys_moudle SET `parent_id` = 117 WHERE  `sys_moudle`.`id` in(118,119);
-- 20170812 --
INSERT INTO `sys_moudle` VALUES ('126', '文件', null, null, '<i class=\"icon-folder-close-alt icon-large\"></i>', null, '1', '1');
UPDATE sys_moudle SET `parent_id` = 126 WHERE  `sys_moudle`.`id` = 38;
UPDATE `sys_moudle` SET `name` = '站点配置' WHERE  `sys_moudle`.`id` = 140;
UPDATE `sys_moudle` SET `name` = '数据字典' WHERE  `sys_moudle`.`id` = 122;
ALTER TABLE `cms_content` 
	ADD COLUMN `check_date` datetime default NULL COMMENT '审核日期' AFTER `publish_date`,
	ADD COLUMN `update_date` datetime default NULL COMMENT '更新日期' AFTER `check_date`,
	DROP INDEX `publish_date`,
	DROP INDEX `user_id`,
	DROP INDEX `category_id`,
	DROP INDEX `model_id`,
	DROP INDEX `parent_id`,
	DROP INDEX `status`,
	DROP INDEX `childs`,
	DROP INDEX `scores`,
	DROP INDEX `comments`,
	DROP INDEX `clicks`,
	DROP INDEX `title`,
	DROP INDEX `check_user_id`,
	DROP INDEX `site_id`,
	DROP INDEX `has_files`,
	DROP INDEX `has_images`,
	DROP INDEX `only_url`,
	DROP INDEX `sort`,
	ADD INDEX `check_date` (`check_date`,`update_date`),
	ADD INDEX `scores` (`scores`,`comments`,`clicks`),
	ADD INDEX `status` (`site_id`,`status`,`category_id`,`disabled`,`model_id`,`parent_id`,`sort`,`publish_date`),
	ADD INDEX `only_url` (`only_url`,`has_images`,`has_files`,`user_id`);
UPDATE `cms_content` SET `check_date` = `publish_date`;
-- 20170905 --
UPDATE `sys_moudle` SET `url` = 'cmsPlace/publish_place' WHERE  `sys_moudle`.`id` = 53;
-- 20170922 --
UPDATE `sys_moudle` SET `parent_id` = 5 WHERE  `sys_moudle`.`id` = 63;
UPDATE `sys_moudle` SET `authorized_url` = 'cmsContent/addMore,file/doUpload,cmsContent/lookup,cmsContent/lookup_list,cmsContent/save,ueditor,ckeditor/upload' WHERE  `sys_moudle`.`id` = 102;
-- 20170922 --
INSERT INTO `sys_moudle` VALUES ('127', '推荐位数据', 'cmsPlace/dataList', null, null , '107', '1', '1');
INSERT INTO `sys_moudle` VALUES ('128', '用户数据监控', 'report/user', NULL, '<i class=\"icon-male icon-large\"></i>', '46', '1', '0');
ALTER TABLE `sys_moudle` ORDER BY  `id`;
DELETE FROM `sys_moudle` WHERE id = 130;
-- 20171103 --
UPDATE `sys_user` SET roles = '2' where id = 2 and site_id = 2;
-- 20171216 --
ALTER TABLE `cms_place` MODIFY COLUMN `item_id` bigint(20) default NULL COMMENT '推荐项目ID' AFTER `item_type`;
ALTER TABLE `cms_category` MODIFY COLUMN `site_id` smallint(6) NOT NULL COMMENT '站点ID' AFTER `id`;
ALTER TABLE `cms_category_type` CHANGE COLUMN `siteId` `site_id` smallint(6) NOT NULL COMMENT '站点ID' AFTER `id`, DROP INDEX `siteId`,ADD INDEX `site_id`(`site_id`) USING BTREE;
ALTER TABLE `cms_content` MODIFY COLUMN `site_id` smallint(6) NOT NULL COMMENT '站点ID' AFTER `id`;
ALTER TABLE `cms_lottery` MODIFY COLUMN `site_id` smallint(6) NOT NULL COMMENT '站点ID' AFTER `id`;
ALTER TABLE `cms_tag` MODIFY COLUMN `site_id` smallint(6) NOT NULL COMMENT '站点ID' AFTER `id`;
ALTER TABLE `cms_tag_type` MODIFY COLUMN `site_id` smallint(6) NOT NULL COMMENT '站点ID' AFTER `id`;
ALTER TABLE `cms_vote` MODIFY COLUMN `site_id` smallint(6) NOT NULL COMMENT '站点ID' AFTER `id`;
ALTER TABLE `cms_word` MODIFY COLUMN `site_id` smallint(6) NOT NULL COMMENT '站点ID' AFTER `id`;
ALTER TABLE `home_article` MODIFY COLUMN `site_id` smallint(6) NOT NULL COMMENT '站点ID' AFTER `id`;
ALTER TABLE `home_broadcast` MODIFY COLUMN `site_id` smallint(6) NOT NULL COMMENT '站点ID' AFTER `id`;
ALTER TABLE `home_comment` MODIFY COLUMN `site_id` smallint(6) NOT NULL COMMENT '站点ID' AFTER `id`;
ALTER TABLE `home_directory` MODIFY COLUMN `site_id` smallint(6) NOT NULL COMMENT '站点ID' AFTER `id`;
ALTER TABLE `home_file` MODIFY COLUMN `site_id` smallint(6) NOT NULL COMMENT '站点ID' AFTER `id`;
ALTER TABLE `home_group` MODIFY COLUMN `site_id` smallint(6) NOT NULL COMMENT '站点ID' AFTER `id`;
ALTER TABLE `home_group_post` MODIFY COLUMN `site_id` smallint(6) NOT NULL COMMENT '站点ID' AFTER `id`;
ALTER TABLE `home_score` MODIFY COLUMN `site_id` smallint(6) NOT NULL COMMENT '站点ID' AFTER `id`;
ALTER TABLE `home_user` MODIFY COLUMN `site_id` smallint(6) NOT NULL COMMENT '站点ID' AFTER `user_id`;
ALTER TABLE `log_login` MODIFY COLUMN `site_id` smallint(6) NOT NULL COMMENT '站点ID' AFTER `id`;
ALTER TABLE `log_operate` MODIFY COLUMN `site_id` smallint(6) NOT NULL COMMENT '站点ID' AFTER `id`;
ALTER TABLE `log_task` MODIFY COLUMN `site_id` smallint(6) NOT NULL COMMENT '站点ID' AFTER `id`;
ALTER TABLE `log_upload` MODIFY COLUMN `site_id` smallint(6) NOT NULL COMMENT '站点ID' AFTER `id`;
ALTER TABLE `sys_app` MODIFY COLUMN `site_id` smallint(6) NOT NULL COMMENT '站点ID' AFTER `id`;
ALTER TABLE `sys_app_client` MODIFY COLUMN `site_id` smallint(6) NOT NULL COMMENT '站点ID' FIRST;
ALTER TABLE `sys_config_data` MODIFY COLUMN `site_id` smallint(6) NOT NULL COMMENT '站点ID' FIRST;
ALTER TABLE `sys_dept` MODIFY COLUMN `site_id` smallint(6) NOT NULL COMMENT '站点ID' AFTER `id`;
ALTER TABLE `sys_domain` MODIFY COLUMN `site_id` smallint(6) NOT NULL COMMENT '站点ID' AFTER `name`;
ALTER TABLE `sys_role` MODIFY COLUMN `site_id` smallint(6) NOT NULL COMMENT '站点ID' AFTER `id`;
ALTER TABLE `sys_task` MODIFY COLUMN `site_id` smallint(6) NOT NULL COMMENT '站点ID' AFTER `id`;
ALTER TABLE `sys_user` MODIFY COLUMN `site_id` smallint(6) NOT NULL COMMENT '站点ID' AFTER `id`;
ALTER TABLE `sys_user_token` MODIFY COLUMN `site_id` smallint(6) NOT NULL COMMENT '站点ID' AFTER `auth_token`;
ALTER TABLE `cms_category_type` COMMENT = '分类类型';
ALTER TABLE `cms_lottery` COMMENT = '抽奖';
ALTER TABLE `cms_lottery_user` COMMENT = '抽奖用户';
ALTER TABLE `cms_lottery_user_attribute` COMMENT = '抽奖用户扩展数据';
ALTER TABLE `cms_vote` COMMENT = '投票';
ALTER TABLE `cms_vote_item` COMMENT = '投票选项';
ALTER TABLE `cms_vote_user` COMMENT = '投票用户';
ALTER TABLE `cms_word` COMMENT = '搜索词';
ALTER TABLE `sys_app` COMMENT = '应用';
ALTER TABLE `sys_app_client` COMMENT = '应用客户端';
ALTER TABLE `sys_app_token` COMMENT = '应用授权';
ALTER TABLE `sys_extend` COMMENT = '扩展';
ALTER TABLE `sys_extend_field` COMMENT = '扩展字段';
-- 2017-12-24 --
ALTER TABLE `cms_lottery` DROP COLUMN `interval_hour`,DROP COLUMN `gift`,DROP COLUMN `extend_id`;
ALTER TABLE `cms_vote` DROP COLUMN `anonymous`,DROP COLUMN `interval_hour`,DROP COLUMN `item_extend_id` , DROP INDEX `disabled`, ADD INDEX  `disabled` (`site_id`,`end_date`,`disabled`);
DROP TABLE IF EXISTS `cms_vote_item_attribute`;
DROP TABLE IF EXISTS `cms_lottery_user_attribute`;
-- 2018-01-18 --
ALTER TABLE `cms_category` DROP COLUMN `contents`;
-- 2018-02-08 --
ALTER TABLE `sys_moudle` RENAME `sys_module`;
ALTER TABLE `sys_role_moudle` RENAME `sys_role_module`;
ALTER TABLE `sys_role_module` CHANGE COLUMN `moudle_id` `module_id`  int(11) NOT NULL COMMENT '模块ID' AFTER `role_id`;
ALTER TABLE `sys_role` CHANGE COLUMN `show_all_moudle` `show_all_module`  int(11) NOT NULL COMMENT '显示全部模块' AFTER `owns_all_right`;
