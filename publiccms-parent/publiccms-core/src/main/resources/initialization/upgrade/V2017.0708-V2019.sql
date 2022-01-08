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
UPDATE `sys_module` SET `authorized_url` = 'sysUser/lookup,cmsContent/recycle,cmsContent/realDelete' WHERE  `id` = 117;
ALTER TABLE `sys_user` MODIFY COLUMN `last_login_ip` varchar(64) NULL DEFAULT NULL COMMENT '最后登录ip' AFTER `last_login_date`;
ALTER TABLE `sys_user_token` MODIFY COLUMN `login_ip` varchar(64) NOT NULL COMMENT '登录IP' AFTER `create_date`;
-- 20180414 --
DROP TABLE IF EXISTS `home_active`;
DROP TABLE IF EXISTS `home_article`;
DROP TABLE IF EXISTS `home_article_content`;
DROP TABLE IF EXISTS `home_attention`;
DROP TABLE IF EXISTS `home_broadcast`;
DROP TABLE IF EXISTS `home_comment`;
DROP TABLE IF EXISTS `home_comment_content`;
DROP TABLE IF EXISTS `home_directory`;
DROP TABLE IF EXISTS `home_file`;
DROP TABLE IF EXISTS `home_friend`;
DROP TABLE IF EXISTS `home_friend_apply`;
DROP TABLE IF EXISTS `home_group`;
DROP TABLE IF EXISTS `home_group_apply`;
DROP TABLE IF EXISTS `home_group_post`;
DROP TABLE IF EXISTS `home_group_post_content`;
DROP TABLE IF EXISTS `home_group_user`;
DROP TABLE IF EXISTS `home_score`;
DROP TABLE IF EXISTS `home_user`;
UPDATE `sys_module` SET `parent_id` = 149 WHERE  `id` in (150,151,152,153,154);
UPDATE `sys_module` SET `parent_id` = 99 WHERE  `id` in (145,146);
-- 20180504 --
UPDATE `sys_module` SET `authorized_url` = 'cmsContent/push_content,cmsContent/push_content_list,cmsContent/push_to_content,cmsContent/push_page,cmsContent/push_page_list,cmsPlace/add,cmsPlace/save,cmsContent/related,cmsContent/unrelated,cmsPlace/delete' WHERE  `id` = 23;
INSERT INTO `sys_module` VALUES ('141', '修改内容模型', 'cmsContent/changeModelParameters', 'cmsContent/changeModel', null, '12', '0', '0');
-- 20180605 --
DELETE FROM `sys_module` WHERE id < 1000;
ALTER TABLE `sys_module`
    MODIFY COLUMN `id` varchar(30) NOT NULL FIRST,
    MODIFY COLUMN `parent_id` varchar(30) NULL DEFAULT NULL COMMENT '父模块' AFTER `attached`;
ALTER TABLE `sys_role` MODIFY COLUMN `show_all_module` tinyint(1) NOT NULL COMMENT '显示全部模块' AFTER `owns_all_right`;
ALTER TABLE `sys_site` MODIFY COLUMN `id` smallint(6) NOT NULL AUTO_INCREMENT FIRST;
ALTER TABLE `cms_place` MODIFY COLUMN `site_id` smallint(6) NOT NULL COMMENT '站点ID' AFTER `id`;
ALTER TABLE `sys_role_module` MODIFY COLUMN `module_id` varchar(30) NOT NULL COMMENT '模块ID' AFTER `role_id`;

CREATE TABLE `sys_module_lang` (
  `module_id` varchar(30) NOT NULL COMMENT '模块ID',
  `lang` varchar(20) NOT NULL COMMENT '语言',
  `value` varchar(100) DEFAULT NULL COMMENT '值',
  PRIMARY KEY (`module_id`,`lang`) USING BTREE
) COMMENT='模块语言';

INSERT INTO `sys_module_lang` SELECT `id`,'',`name` FROM `sys_module` where id >= 1000;
INSERT INTO `sys_module_lang` SELECT `id`,'en',`name` FROM `sys_module` where id >= 1000;

INSERT INTO `sys_module_lang` VALUES ('app_add', '', '增加/修改');
INSERT INTO `sys_module_lang` VALUES ('app_add', 'en', 'Add/edit');
INSERT INTO `sys_module_lang` VALUES ('app_client_disable', '', '禁用');
INSERT INTO `sys_module_lang` VALUES ('app_client_disable', 'en', 'Disable');
INSERT INTO `sys_module_lang` VALUES ('app_client_enable', '', '启用');
INSERT INTO `sys_module_lang` VALUES ('app_client_enable', 'en', 'Enable');
INSERT INTO `sys_module_lang` VALUES ('app_client_list', '', '客户端管理');
INSERT INTO `sys_module_lang` VALUES ('app_client_list', 'en', 'Application client management');
INSERT INTO `sys_module_lang` VALUES ('app_delete', '', '删除');
INSERT INTO `sys_module_lang` VALUES ('app_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('app_list', '', '应用授权');
INSERT INTO `sys_module_lang` VALUES ('app_list', 'en', 'Application Authorization');
INSERT INTO `sys_module_lang` VALUES ('category', '', '分类');
INSERT INTO `sys_module_lang` VALUES ('category', 'en', 'Category');
INSERT INTO `sys_module_lang` VALUES ('category_add', '', '增加/修改');
INSERT INTO `sys_module_lang` VALUES ('category_add', 'en', 'Add/edit');
INSERT INTO `sys_module_lang` VALUES ('category_delete', '', '删除');
INSERT INTO `sys_module_lang` VALUES ('category_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('category_extend', '', '分类扩展');
INSERT INTO `sys_module_lang` VALUES ('category_extend', 'en', 'Category extension');
INSERT INTO `sys_module_lang` VALUES ('category_menu', '', '分类管理');
INSERT INTO `sys_module_lang` VALUES ('category_menu', 'en', 'Category management');
INSERT INTO `sys_module_lang` VALUES ('category_move', '', '移动');
INSERT INTO `sys_module_lang` VALUES ('category_move', 'en', 'Move');
INSERT INTO `sys_module_lang` VALUES ('category_publish', '', '生成');
INSERT INTO `sys_module_lang` VALUES ('category_publish', 'en', 'Publish');
INSERT INTO `sys_module_lang` VALUES ('category_push', '', '推荐');
INSERT INTO `sys_module_lang` VALUES ('category_push', 'en', 'Push');
INSERT INTO `sys_module_lang` VALUES ('category_type_add', '', '增加/修改');
INSERT INTO `sys_module_lang` VALUES ('category_type_add', 'en', 'test');
INSERT INTO `sys_module_lang` VALUES ('category_type_change', '', '修改类型');
INSERT INTO `sys_module_lang` VALUES ('category_type_change', 'en', 'Change category type');
INSERT INTO `sys_module_lang` VALUES ('category_type_delete', '', '删除');
INSERT INTO `sys_module_lang` VALUES ('category_type_delete', 'en', 'test');
INSERT INTO `sys_module_lang` VALUES ('category_type_list', '', '分类类型');
INSERT INTO `sys_module_lang` VALUES ('category_type_list', 'en', 'Category type');
INSERT INTO `sys_module_lang` VALUES ('clearcache', '', '刷新缓存');
INSERT INTO `sys_module_lang` VALUES ('clearcache', 'en', 'Clear cache');
INSERT INTO `sys_module_lang` VALUES ('config_add', '', '添加/修改');
INSERT INTO `sys_module_lang` VALUES ('config_add', 'en', 'Add/edit');
INSERT INTO `sys_module_lang` VALUES ('config_data_delete', '', '清空配置');
INSERT INTO `sys_module_lang` VALUES ('config_data_delete', 'en', 'Clear config data');
INSERT INTO `sys_module_lang` VALUES ('config_data_edit', '', '修改');
INSERT INTO `sys_module_lang` VALUES ('config_data_edit', 'en', 'Edit');
INSERT INTO `sys_module_lang` VALUES ('config_data_list', '', '站点配置');
INSERT INTO `sys_module_lang` VALUES ('config_data_list', 'en', 'Site configuration');
INSERT INTO `sys_module_lang` VALUES ('config_delete', '', '删除配置');
INSERT INTO `sys_module_lang` VALUES ('config_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('config_list', '', '站点配置管理');
INSERT INTO `sys_module_lang` VALUES ('config_list', 'en', 'Site config management');
INSERT INTO `sys_module_lang` VALUES ('config_list_data_dictionary', '', '选择数据字典');
INSERT INTO `sys_module_lang` VALUES ('config_list_data_dictionary', 'en', 'Select data dictionary');
INSERT INTO `sys_module_lang` VALUES ('config_menu', '', '配置管理');
INSERT INTO `sys_module_lang` VALUES ('config_menu', 'en', 'Configuration management');
INSERT INTO `sys_module_lang` VALUES ('content', '', '内容');
INSERT INTO `sys_module_lang` VALUES ('content', 'en', 'Content');
INSERT INTO `sys_module_lang` VALUES ('content_add', '', '增加/修改');
INSERT INTO `sys_module_lang` VALUES ('content_add', 'en', 'Add/edit');
INSERT INTO `sys_module_lang` VALUES ('content_check', '', '审核');
INSERT INTO `sys_module_lang` VALUES ('content_check', 'en', 'Check');
INSERT INTO `sys_module_lang` VALUES ('content_delete', '', '删除');
INSERT INTO `sys_module_lang` VALUES ('content_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('content_extend', '', '内容扩展');
INSERT INTO `sys_module_lang` VALUES ('content_extend', 'en', 'Content extension');
INSERT INTO `sys_module_lang` VALUES ('content_menu', '', '内容管理');
INSERT INTO `sys_module_lang` VALUES ('content_menu', 'en', 'Content management');
INSERT INTO `sys_module_lang` VALUES ('content_move', '', '移动');
INSERT INTO `sys_module_lang` VALUES ('content_move', 'en', 'Move');
INSERT INTO `sys_module_lang` VALUES ('content_publish', '', '生成');
INSERT INTO `sys_module_lang` VALUES ('content_publish', 'en', 'Publish');
INSERT INTO `sys_module_lang` VALUES ('content_push', '', '推荐');
INSERT INTO `sys_module_lang` VALUES ('content_push', 'en', 'Push');
INSERT INTO `sys_module_lang` VALUES ('content_recycle_delete', '', '删除');
INSERT INTO `sys_module_lang` VALUES ('content_recycle_delete', 'en', 'test');
INSERT INTO `sys_module_lang` VALUES ('content_recycle_list', '', '内容回收站');
INSERT INTO `sys_module_lang` VALUES ('content_recycle_list', 'en', 'Content recycle');
INSERT INTO `sys_module_lang` VALUES ('content_recycle_recycle', '', '还原');
INSERT INTO `sys_module_lang` VALUES ('content_recycle_recycle', 'en', 'test');
INSERT INTO `sys_module_lang` VALUES ('content_refresh', '', '刷新');
INSERT INTO `sys_module_lang` VALUES ('content_refresh', 'en', 'Refresh');
INSERT INTO `sys_module_lang` VALUES ('content_select_category', '', '选择分类');
INSERT INTO `sys_module_lang` VALUES ('content_select_category', 'en', 'Select category');
INSERT INTO `sys_module_lang` VALUES ('content_select_category_type', '', '选择分类类型');
INSERT INTO `sys_module_lang` VALUES ('content_select_category_type', 'en', 'Select category type');
INSERT INTO `sys_module_lang` VALUES ('content_select_content', '', '选择内容');
INSERT INTO `sys_module_lang` VALUES ('content_select_content', 'en', 'Select content');
INSERT INTO `sys_module_lang` VALUES ('content_select_tag_type', '', '选择标签类型');
INSERT INTO `sys_module_lang` VALUES ('content_select_tag_type', 'en', 'Select tag type');
INSERT INTO `sys_module_lang` VALUES ('content_select_template', '', '选择模板');
INSERT INTO `sys_module_lang` VALUES ('content_select_template', 'en', 'Select template');
INSERT INTO `sys_module_lang` VALUES ('content_select_user', '', '选择用户');
INSERT INTO `sys_module_lang` VALUES ('content_select_user', 'en', 'Select user');
INSERT INTO `sys_module_lang` VALUES ('content_sort', '', '置顶');
INSERT INTO `sys_module_lang` VALUES ('content_sort', 'en', 'Sort');
INSERT INTO `sys_module_lang` VALUES ('content_uncheck', '', '撤销审核');
INSERT INTO `sys_module_lang` VALUES ('content_uncheck', 'en', 'Uncheck');
INSERT INTO `sys_module_lang` VALUES ('content_view', '', '查看');
INSERT INTO `sys_module_lang` VALUES ('content_view', 'en', 'View');
INSERT INTO `sys_module_lang` VALUES ('dept_add', '', '增加/修改');
INSERT INTO `sys_module_lang` VALUES ('dept_add', 'en', 'Add/edit');
INSERT INTO `sys_module_lang` VALUES ('dept_delete', '', '删除');
INSERT INTO `sys_module_lang` VALUES ('dept_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('dept_list', '', '部门管理');
INSERT INTO `sys_module_lang` VALUES ('dept_list', 'en', 'Department management');
INSERT INTO `sys_module_lang` VALUES ('dept_user_list', '', '人员管理');
INSERT INTO `sys_module_lang` VALUES ('dept_user_list', 'en', 'Department user management');
INSERT INTO `sys_module_lang` VALUES ('develop', '', '开发');
INSERT INTO `sys_module_lang` VALUES ('develop', 'en', 'Development');
INSERT INTO `sys_module_lang` VALUES ('dictionary_add', '', '添加/修改');
INSERT INTO `sys_module_lang` VALUES ('dictionary_add', 'en', 'Add/edit');
INSERT INTO `sys_module_lang` VALUES ('dictionary_delete', '', '删除');
INSERT INTO `sys_module_lang` VALUES ('dictionary_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('dictionary_list', '', '数据字典');
INSERT INTO `sys_module_lang` VALUES ('dictionary_list', 'en', 'Dictionary management');
INSERT INTO `sys_module_lang` VALUES ('domain_config', '', '修改');
INSERT INTO `sys_module_lang` VALUES ('domain_config', 'en', 'Edit');
INSERT INTO `sys_module_lang` VALUES ('domain_list', '', '绑定域名');
INSERT INTO `sys_module_lang` VALUES ('domain_list', 'en', 'Domain management');
INSERT INTO `sys_module_lang` VALUES ('file_menu', '', '文件管理');
INSERT INTO `sys_module_lang` VALUES ('file_menu', 'en', 'File maintenance');
INSERT INTO `sys_module_lang` VALUES ('log_login', '', '登录日志');
INSERT INTO `sys_module_lang` VALUES ('log_login', 'en', 'Login log');
INSERT INTO `sys_module_lang` VALUES ('log_login_delete', '', '删除');
INSERT INTO `sys_module_lang` VALUES ('log_login_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('log_menu', '', '日志管理');
INSERT INTO `sys_module_lang` VALUES ('log_menu', 'en', 'Log management');
INSERT INTO `sys_module_lang` VALUES ('log_operate', '', '操作日志');
INSERT INTO `sys_module_lang` VALUES ('log_operate', 'en', 'Operate log');
INSERT INTO `sys_module_lang` VALUES ('log_operate_delete', '', '删除');
INSERT INTO `sys_module_lang` VALUES ('log_operate_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('log_operate_view', '', '查看');
INSERT INTO `sys_module_lang` VALUES ('log_operate_view', 'en', 'View');
INSERT INTO `sys_module_lang` VALUES ('log_task', '', '任务计划日志');
INSERT INTO `sys_module_lang` VALUES ('log_task', 'en', 'Task log');
INSERT INTO `sys_module_lang` VALUES ('log_task_delete', '', '删除');
INSERT INTO `sys_module_lang` VALUES ('log_task_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('log_task_view', '', '查看');
INSERT INTO `sys_module_lang` VALUES ('log_task_view', 'en', 'View');
INSERT INTO `sys_module_lang` VALUES ('log_upload', '', '文件上传日志');
INSERT INTO `sys_module_lang` VALUES ('log_upload', 'en', 'Upload log');
INSERT INTO `sys_module_lang` VALUES ('maintenance', '', '维护');
INSERT INTO `sys_module_lang` VALUES ('maintenance', 'en', 'Maintenance');
INSERT INTO `sys_module_lang` VALUES ('model_add', '', '增加/修改');
INSERT INTO `sys_module_lang` VALUES ('model_add', 'en', 'Add/edit');
INSERT INTO `sys_module_lang` VALUES ('model_delete', '', '删除');
INSERT INTO `sys_module_lang` VALUES ('model_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('model_list', '', '内容模型管理');
INSERT INTO `sys_module_lang` VALUES ('model_list', 'en', 'Model management');
INSERT INTO `sys_module_lang` VALUES ('myself', '', '个人');
INSERT INTO `sys_module_lang` VALUES ('myself', 'en', 'Myself');
INSERT INTO `sys_module_lang` VALUES ('myself_content', '', '我的内容');
INSERT INTO `sys_module_lang` VALUES ('myself_content', 'en', 'My content');
INSERT INTO `sys_module_lang` VALUES ('myself_content_add', '', '修改');
INSERT INTO `sys_module_lang` VALUES ('myself_content_add', 'en', 'Add');
INSERT INTO `sys_module_lang` VALUES ('myself_content_delete', '', '删除');
INSERT INTO `sys_module_lang` VALUES ('myself_content_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('myself_content_publish', '', '生成');
INSERT INTO `sys_module_lang` VALUES ('myself_content_publish', 'en', 'Publish');
INSERT INTO `sys_module_lang` VALUES ('myself_content_push', '', '推荐');
INSERT INTO `sys_module_lang` VALUES ('myself_content_push', 'en', 'Push');
INSERT INTO `sys_module_lang` VALUES ('myself_content_refresh', '', '刷新');
INSERT INTO `sys_module_lang` VALUES ('myself_content_refresh', 'en', 'Refresh');
INSERT INTO `sys_module_lang` VALUES ('myself_log_login', '', '我的登录日志');
INSERT INTO `sys_module_lang` VALUES ('myself_log_login', 'en', 'My login log');
INSERT INTO `sys_module_lang` VALUES ('myself_log_operate', '', '我的操作日志');
INSERT INTO `sys_module_lang` VALUES ('myself_log_operate', 'en', 'My operate log');
INSERT INTO `sys_module_lang` VALUES ('myself_menu', '', '与我相关');
INSERT INTO `sys_module_lang` VALUES ('myself_menu', 'en', 'My account');
INSERT INTO `sys_module_lang` VALUES ('myself_password', '', '修改密码');
INSERT INTO `sys_module_lang` VALUES ('myself_password', 'en', 'Change password');
INSERT INTO `sys_module_lang` VALUES ('myself_token', '', '我的登录授权');
INSERT INTO `sys_module_lang` VALUES ('myself_token', 'en', 'My login token');
INSERT INTO `sys_module_lang` VALUES ('page', '', '页面');
INSERT INTO `sys_module_lang` VALUES ('page', 'en', 'Page');
INSERT INTO `sys_module_lang` VALUES ('page_list', '', '页面管理');
INSERT INTO `sys_module_lang` VALUES ('page_list', 'en', 'Page management');
INSERT INTO `sys_module_lang` VALUES ('page_menu', '', '页面维护');
INSERT INTO `sys_module_lang` VALUES ('page_menu', 'en', 'Page maintenance');
INSERT INTO `sys_module_lang` VALUES ('page_metadata', '', '元数据管理');
INSERT INTO `sys_module_lang` VALUES ('page_metadata', 'en', 'Metadata management');
INSERT INTO `sys_module_lang` VALUES ('page_publish', '', '生成页面');
INSERT INTO `sys_module_lang` VALUES ('page_publish', 'en', 'Publish');
INSERT INTO `sys_module_lang` VALUES ('page_save', '', '保存页面配置');
INSERT INTO `sys_module_lang` VALUES ('page_save', 'en', 'Save configuration');
INSERT INTO `sys_module_lang` VALUES ('page_select_category', '', '选择分类');
INSERT INTO `sys_module_lang` VALUES ('page_select_category', 'en', 'Select category');
INSERT INTO `sys_module_lang` VALUES ('page_select_category_type', '', '选择分类类型');
INSERT INTO `sys_module_lang` VALUES ('page_select_category_type', 'en', 'Select category type');
INSERT INTO `sys_module_lang` VALUES ('page_select_content', '', '选择内容');
INSERT INTO `sys_module_lang` VALUES ('page_select_content', 'en', 'Select content');
INSERT INTO `sys_module_lang` VALUES ('page_select_tag_type', '', '选择标签类型');
INSERT INTO `sys_module_lang` VALUES ('page_select_tag_type', 'en', 'Select tag type');
INSERT INTO `sys_module_lang` VALUES ('page_select_template', '', '选择模板');
INSERT INTO `sys_module_lang` VALUES ('page_select_template', 'en', 'Select template');
INSERT INTO `sys_module_lang` VALUES ('page_select_user', '', '选择用户');
INSERT INTO `sys_module_lang` VALUES ('page_select_user', 'en', 'Select user');
INSERT INTO `sys_module_lang` VALUES ('place_add', '', '增加/修改推荐位数据');
INSERT INTO `sys_module_lang` VALUES ('place_add', 'en', 'Add/edit');
INSERT INTO `sys_module_lang` VALUES ('place_check', '', '审核推荐位数据');
INSERT INTO `sys_module_lang` VALUES ('place_check', 'en', 'Check');
INSERT INTO `sys_module_lang` VALUES ('place_clear', '', '清空推荐位数据');
INSERT INTO `sys_module_lang` VALUES ('place_clear', 'en', 'Clear');
INSERT INTO `sys_module_lang` VALUES ('place_data_list', '', '推荐位数据');
INSERT INTO `sys_module_lang` VALUES ('place_data_list', 'en', 'Page fragment data');
INSERT INTO `sys_module_lang` VALUES ('place_delete', '', '删除推荐位数据');
INSERT INTO `sys_module_lang` VALUES ('place_delete', 'en', 'data');
INSERT INTO `sys_module_lang` VALUES ('place_list', '', '页面片段管理');
INSERT INTO `sys_module_lang` VALUES ('place_list', 'en', 'Page fragment management');
INSERT INTO `sys_module_lang` VALUES ('place_publish', '', '发布');
INSERT INTO `sys_module_lang` VALUES ('place_publish', 'en', 'Publish');
INSERT INTO `sys_module_lang` VALUES ('place_refresh', '', '刷新推荐位数据');
INSERT INTO `sys_module_lang` VALUES ('place_refresh', 'en', 'Refresh');
INSERT INTO `sys_module_lang` VALUES ('place_template_content', '', '修改');
INSERT INTO `sys_module_lang` VALUES ('place_template_content', 'en', 'Edit template');
INSERT INTO `sys_module_lang` VALUES ('place_template_data_dictionary', '', '选择数据字典');
INSERT INTO `sys_module_lang` VALUES ('place_template_data_dictionary', 'en', 'Select data dictionary');
INSERT INTO `sys_module_lang` VALUES ('place_template_fragment', '', '模板片段');
INSERT INTO `sys_module_lang` VALUES ('place_template_fragment', 'en', 'Template fragment');
INSERT INTO `sys_module_lang` VALUES ('place_template_help', '', '模板帮助');
INSERT INTO `sys_module_lang` VALUES ('place_template_help', 'en', 'Template help');
INSERT INTO `sys_module_lang` VALUES ('place_template_list', '', '页面片段模板');
INSERT INTO `sys_module_lang` VALUES ('place_template_list', 'en', 'Page fragment template');
INSERT INTO `sys_module_lang` VALUES ('place_template_metadata', '', '修改元数据');
INSERT INTO `sys_module_lang` VALUES ('place_template_metadata', 'en', 'Edit metadata');
INSERT INTO `sys_module_lang` VALUES ('place_template_place', '', '页面片段');
INSERT INTO `sys_module_lang` VALUES ('place_template_place', 'en', 'Page fragment');
INSERT INTO `sys_module_lang` VALUES ('place_template_webfile', '', '网站文件');
INSERT INTO `sys_module_lang` VALUES ('place_template_webfile', 'en', 'Website file');
INSERT INTO `sys_module_lang` VALUES ('place_view', '', '查看推荐位数据');
INSERT INTO `sys_module_lang` VALUES ('place_view', 'en', 'View');
INSERT INTO `sys_module_lang` VALUES ('report_user', '', '用户数据监控');
INSERT INTO `sys_module_lang` VALUES ('report_user', 'en', 'User report');
INSERT INTO `sys_module_lang` VALUES ('role_add', '', '增加/修改');
INSERT INTO `sys_module_lang` VALUES ('role_add', 'en', 'Add/edit');
INSERT INTO `sys_module_lang` VALUES ('role_delete', '', '删除');
INSERT INTO `sys_module_lang` VALUES ('role_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('role_list', '', '角色管理');
INSERT INTO `sys_module_lang` VALUES ('role_list', 'en', 'Role management');
INSERT INTO `sys_module_lang` VALUES ('system_menu', '', '系统维护');
INSERT INTO `sys_module_lang` VALUES ('system_menu', 'en', 'System maintenance');
INSERT INTO `sys_module_lang` VALUES ('tag_add', '', '增加/修改');
INSERT INTO `sys_module_lang` VALUES ('tag_add', 'en', 'Add/edit');
INSERT INTO `sys_module_lang` VALUES ('tag_delete', '', '删除');
INSERT INTO `sys_module_lang` VALUES ('tag_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('tag_list', '', '标签管理');
INSERT INTO `sys_module_lang` VALUES ('tag_list', 'en', 'Tag management');
INSERT INTO `sys_module_lang` VALUES ('tag_type_delete', '', '删除');
INSERT INTO `sys_module_lang` VALUES ('tag_type_delete', 'en', 'test');
INSERT INTO `sys_module_lang` VALUES ('tag_type_list', '', '标签分类');
INSERT INTO `sys_module_lang` VALUES ('tag_type_list', 'en', 'Tag type');
INSERT INTO `sys_module_lang` VALUES ('tag_type_save', '', '增加/修改');
INSERT INTO `sys_module_lang` VALUES ('tag_type_save', 'en', 'test');
INSERT INTO `sys_module_lang` VALUES ('task_add', '', '增加/修改');
INSERT INTO `sys_module_lang` VALUES ('task_add', 'en', 'Add/edit');
INSERT INTO `sys_module_lang` VALUES ('task_delete', '', '删除');
INSERT INTO `sys_module_lang` VALUES ('task_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('task_list', '', '任务计划');
INSERT INTO `sys_module_lang` VALUES ('task_list', 'en', 'Task management');
INSERT INTO `sys_module_lang` VALUES ('task_pause', '', '暂停');
INSERT INTO `sys_module_lang` VALUES ('task_pause', 'en', 'Pause');
INSERT INTO `sys_module_lang` VALUES ('task_recreate', '', '重新初始化');
INSERT INTO `sys_module_lang` VALUES ('task_recreate', 'en', 'Recreate');
INSERT INTO `sys_module_lang` VALUES ('task_resume', '', '恢复');
INSERT INTO `sys_module_lang` VALUES ('task_resume', 'en', 'Resume');
INSERT INTO `sys_module_lang` VALUES ('task_runonce', '', '立刻执行');
INSERT INTO `sys_module_lang` VALUES ('task_runonce', 'en', 'Run once');
INSERT INTO `sys_module_lang` VALUES ('task_template_content', '', '修改');
INSERT INTO `sys_module_lang` VALUES ('task_template_content', 'en', 'Edit');
INSERT INTO `sys_module_lang` VALUES ('task_template_delete', '', '删除');
INSERT INTO `sys_module_lang` VALUES ('task_template_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('task_template_fragment', '', '任务计划脚本片段');
INSERT INTO `sys_module_lang` VALUES ('task_template_fragment', 'en', 'Task script fragment');
INSERT INTO `sys_module_lang` VALUES ('task_template_help', '', '帮助');
INSERT INTO `sys_module_lang` VALUES ('task_template_help', 'en', 'help');
INSERT INTO `sys_module_lang` VALUES ('task_template_list', '', '任务计划脚本');
INSERT INTO `sys_module_lang` VALUES ('task_template_list', 'en', 'Task template management');
INSERT INTO `sys_module_lang` VALUES ('template_content', '', '修改');
INSERT INTO `sys_module_lang` VALUES ('template_content', 'en', 'Edit');
INSERT INTO `sys_module_lang` VALUES ('template_content-type', '', '选择content-type');
INSERT INTO `sys_module_lang` VALUES ('template_content-type', 'en', 'Select content-type');
INSERT INTO `sys_module_lang` VALUES ('template_content_form', '', '内容投稿表单');
INSERT INTO `sys_module_lang` VALUES ('template_content_form', 'en', 'Content contribute form');
INSERT INTO `sys_module_lang` VALUES ('template_data_dictionary', '', '选择数据字典');
INSERT INTO `sys_module_lang` VALUES ('template_data_dictionary', 'en', 'Select data dictionary');
INSERT INTO `sys_module_lang` VALUES ('template_delete', '', '删除');
INSERT INTO `sys_module_lang` VALUES ('template_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('template_demo', '', '模板示例');
INSERT INTO `sys_module_lang` VALUES ('template_demo', 'en', 'Template example');
INSERT INTO `sys_module_lang` VALUES ('template_fragment', '', '模板片段');
INSERT INTO `sys_module_lang` VALUES ('template_fragment', 'en', 'Template fragment');
INSERT INTO `sys_module_lang` VALUES ('template_help', '', '模板帮助');
INSERT INTO `sys_module_lang` VALUES ('template_help', 'en', 'Template help');
INSERT INTO `sys_module_lang` VALUES ('template_list', '', '模板文件管理');
INSERT INTO `sys_module_lang` VALUES ('template_list', 'en', 'Template management');
INSERT INTO `sys_module_lang` VALUES ('template_metadata', '', '修改元数据');
INSERT INTO `sys_module_lang` VALUES ('template_metadata', 'en', 'Edit metadata');
INSERT INTO `sys_module_lang` VALUES ('template_place', '', '页面片段');
INSERT INTO `sys_module_lang` VALUES ('template_place', 'en', 'Page fragment');
INSERT INTO `sys_module_lang` VALUES ('template_place_form', '', '页面片段投稿表单');
INSERT INTO `sys_module_lang` VALUES ('template_place_form', 'en', 'Page fragment data contribute form');
INSERT INTO `sys_module_lang` VALUES ('template_upload', '', '上传模板');
INSERT INTO `sys_module_lang` VALUES ('template_upload', 'en', 'Upload template');
INSERT INTO `sys_module_lang` VALUES ('template_website_file', '', '网站文件');
INSERT INTO `sys_module_lang` VALUES ('template_website_file', 'en', 'Website file');
INSERT INTO `sys_module_lang` VALUES ('user_add', '', '增加/修改');
INSERT INTO `sys_module_lang` VALUES ('user_add', 'en', 'Add/edit');
INSERT INTO `sys_module_lang` VALUES ('user_disable', '', '禁用');
INSERT INTO `sys_module_lang` VALUES ('user_disable', 'en', 'Disable');
INSERT INTO `sys_module_lang` VALUES ('user_enable', '', '启用');
INSERT INTO `sys_module_lang` VALUES ('user_enable', 'en', 'Enable');
INSERT INTO `sys_module_lang` VALUES ('user_list', '', '用户管理');
INSERT INTO `sys_module_lang` VALUES ('user_list', 'en', 'User management');
INSERT INTO `sys_module_lang` VALUES ('user_menu', '', '用户管理');
INSERT INTO `sys_module_lang` VALUES ('user_menu', 'en', 'User maintenance');
INSERT INTO `sys_module_lang` VALUES ('webfile_content', '', '修改文件');
INSERT INTO `sys_module_lang` VALUES ('webfile_content', 'en', 'Edit file');
INSERT INTO `sys_module_lang` VALUES ('webfile_directory', '', '新建目录');
INSERT INTO `sys_module_lang` VALUES ('webfile_directory', 'en', 'Create Directory');
INSERT INTO `sys_module_lang` VALUES ('webfile_list', '', '网站文件管理');
INSERT INTO `sys_module_lang` VALUES ('webfile_list', 'en', 'Website file management');
INSERT INTO `sys_module_lang` VALUES ('webfile_unzip', '', '解压缩');
INSERT INTO `sys_module_lang` VALUES ('webfile_unzip', 'en', 'Decompress');
INSERT INTO `sys_module_lang` VALUES ('webfile_upload', '', '上传');
INSERT INTO `sys_module_lang` VALUES ('webfile_upload', 'en', 'Upload');
INSERT INTO `sys_module_lang` VALUES ('webfile_zip', '', '压缩');
INSERT INTO `sys_module_lang` VALUES ('webfile_zip', 'en', 'Compress');
INSERT INTO `sys_module_lang` VALUES ('word_list', '', '搜索词管理');
INSERT INTO `sys_module_lang` VALUES ('word_list', 'en', 'Search word management');

ALTER TABLE `sys_module` DROP COLUMN `name`,
    DROP INDEX `parent_id`,
    DROP INDEX `url`,
    ADD INDEX `parent_id`(`parent_id`, `menu`);

INSERT INTO `sys_module` VALUES ('app_add', 'sysApp/add', 'sysApp/save', NULL, 'app_list', 0, 0);
INSERT INTO `sys_module` VALUES ('app_client_disable', NULL, 'sysAppClient/disable', NULL, 'app_client_list', 0, 0);
INSERT INTO `sys_module` VALUES ('app_client_enable', NULL, 'sysAppClient/enable', NULL, 'app_client_list', 0, 0);
INSERT INTO `sys_module` VALUES ('app_client_list', 'sysAppClient/list', NULL, '<i class=\"icon-coffee icon-large\"></i>', 'user_menu', 1, 4);
INSERT INTO `sys_module` VALUES ('app_delete', NULL, 'sysApp/delete', NULL, 'app_list', 0, 0);
INSERT INTO `sys_module` VALUES ('app_list', 'sysApp/list', NULL, '<i class=\"icon-linux icon-large\"></i>', 'system_menu', 1, 5);
INSERT INTO `sys_module` VALUES ('category', NULL, NULL, '<i class=\"icon-folder-open icon-large\"></i>', NULL, 1, 5);
INSERT INTO `sys_module` VALUES ('category_add', 'cmsCategory/add', 'cmsCategory/addMore,cmsTemplate/lookup,cmsCategory/categoryPath,cmsCategory/contentPath,file/doUpload,cmsCategory/save', '', 'category_menu', 0, 0);
INSERT INTO `sys_module` VALUES ('category_delete', NULL, 'cmsCategory/delete', '', 'category_menu', 0, 0);
INSERT INTO `sys_module` VALUES ('category_extend', NULL, NULL, '<i class=\"icon-road icon-large\"></i>', 'category', 1, 2);
INSERT INTO `sys_module` VALUES ('category_menu', 'cmsCategory/list', NULL, '<i class=\"icon-folder-open icon-large\"></i>', 'category', 1, 1);
INSERT INTO `sys_module` VALUES ('category_move', 'cmsCategory/moveParameters', 'cmsCategory/move,cmsCategory/lookup', '', 'category_menu', 0, 0);
INSERT INTO `sys_module` VALUES ('category_publish', 'cmsCategory/publishParameters', 'cmsCategory/publish', '', 'category_menu', 0, 0);
INSERT INTO `sys_module` VALUES ('category_push', 'cmsCategory/push_page', 'cmsPlace/push,cmsPlace/add,cmsPlace/save', '', 'category_menu', 0, 0);
INSERT INTO `sys_module` VALUES ('category_type_add', 'cmsCategoryType/add', 'cmsCategoryType/save', NULL, 'category_type_list', 0, 0);
INSERT INTO `sys_module` VALUES ('category_type_change', 'cmsCategory/changeTypeParameters', 'cmsCategory/changeType', '', 'category_menu', 0, 0);
INSERT INTO `sys_module` VALUES ('category_type_delete', NULL, 'cmsCategoryType/delete', NULL, 'category_type_list', 0, 0);
INSERT INTO `sys_module` VALUES ('category_type_list', 'cmsCategoryType/list', NULL, '<i class=\"icon-road icon-large\"></i>', 'category_extend', 1, 1);
INSERT INTO `sys_module` VALUES ('clearcache', NULL, 'clearCache', '', NULL, 0, 10);
INSERT INTO `sys_module` VALUES ('config_add', 'sysConfig/add', 'sysConfig/save', NULL, 'config_list', 0, 0);
INSERT INTO `sys_module` VALUES ('config_data_delete', NULL, 'sysConfigData/delete', NULL, 'config_data_list', 0, 0);
INSERT INTO `sys_module` VALUES ('config_data_edit', 'sysConfigData/edit', 'sysConfigData/save', NULL, 'config_data_list', 0, 0);
INSERT INTO `sys_module` VALUES ('config_data_list', 'sysConfigData/list', NULL, '<i class=\"icon-cog icon-large\"></i>', 'system_menu', 1, 1);
INSERT INTO `sys_module` VALUES ('config_delete', NULL, 'sysConfig/delete', NULL, 'config_list', 0, 0);
INSERT INTO `sys_module` VALUES ('config_list', 'sysConfig/list', NULL, '<i class=\"icon-cogs icon-large\"></i>', 'config_menu', 1, 2);
INSERT INTO `sys_module` VALUES ('config_list_data_dictionary', 'cmsDictionary/lookup', NULL, NULL, 'config_list', 0, 0);
INSERT INTO `sys_module` VALUES ('config_menu', NULL, NULL, '<i class=\"icon-gear icon-large\"></i>', 'develop', 1, 2);
INSERT INTO `sys_module` VALUES ('content', NULL, NULL, '<i class=\"icon-book icon-large\"></i>', NULL, 1, 2);
INSERT INTO `sys_module` VALUES ('content_add', 'cmsContent/add', 'cmsContent/addMore,cmsContent/save,ueditor', '', 'content_menu', 1, 0);
INSERT INTO `sys_module` VALUES ('content_check', NULL, 'cmsContent/check', '', 'content_menu', 1, 0);
INSERT INTO `sys_module` VALUES ('content_delete', NULL, 'cmsContent/delete', '', 'content_menu', 1, 0);
INSERT INTO `sys_module` VALUES ('content_extend', NULL, NULL, '<i class=\"icon-road icon-large\"></i>', 'content', 1, 1);
INSERT INTO `sys_module` VALUES ('content_menu', 'cmsContent/list', 'sysUser/lookup', '<i class=\"icon-book icon-large\"></i>', 'content', 1, 0);
INSERT INTO `sys_module` VALUES ('content_move', 'cmsContent/moveParameters', 'cmsContent/move', '', 'content_menu', 1, 0);
INSERT INTO `sys_module` VALUES ('content_publish', NULL, 'cmsContent/publish', '', 'content_menu', 1, 0);
INSERT INTO `sys_module` VALUES ('content_push', 'cmsContent/push', 'cmsContent/push_content,cmsContent/push_content_list,cmsContent/push_to_content,cmsContent/push_page,cmsContent/push_page_list,cmsPlace/add,cmsPlace/save,cmsContent/related,cmsContent/unrelated,cmsPlace/delete', '', 'content_menu', 1, 0);
INSERT INTO `sys_module` VALUES ('content_recycle_delete', NULL, 'cmsContent/realDelete', NULL, 'content_recycle_list', 0, 0);
INSERT INTO `sys_module` VALUES ('content_recycle_list', 'cmsRecycleContent/list', 'sysUser/lookup', '<i class=\"icon-trash icon-large\"></i>', 'content_extend', 1, 3);
INSERT INTO `sys_module` VALUES ('content_recycle_recycle', NULL, 'cmsContent/recycle', NULL, 'content_recycle_list', 0, 0);
INSERT INTO `sys_module` VALUES ('content_refresh', NULL, 'cmsContent/refresh', '', 'content_menu', 1, 0);
INSERT INTO `sys_module` VALUES ('content_select_category', 'cmsCategory/lookup', NULL, NULL, 'content_add', 0, 0);
INSERT INTO `sys_module` VALUES ('content_select_category_type', 'cmsCategoryType/lookup', NULL, NULL, 'content_add', 0, 0);
INSERT INTO `sys_module` VALUES ('content_select_content', 'cmsContent/lookup', 'cmsContent/lookup_list', NULL, 'content_add', 0, 0);
INSERT INTO `sys_module` VALUES ('content_select_tag_type', 'cmsTagType/lookup', NULL, NULL, 'content_add', 0, 0);
INSERT INTO `sys_module` VALUES ('content_select_template', 'cmsTemplate/lookup', NULL, NULL, 'content_add', 0, 0);
INSERT INTO `sys_module` VALUES ('content_select_user', 'sysUser/lookup', NULL, NULL, 'content_add', 0, 0);
INSERT INTO `sys_module` VALUES ('content_sort', 'cmsContent/sortParameters', 'cmsContent/sort', '', 'content_menu', 0, 0);
INSERT INTO `sys_module` VALUES ('content_uncheck', NULL, 'cmsContent/uncheck', '', 'content_menu', 0, 0);
INSERT INTO `sys_module` VALUES ('content_view', 'cmsContent/view', NULL, '', 'content_menu', 0, 0);
INSERT INTO `sys_module` VALUES ('dept_add', 'sysDept/add', 'sysDept/lookup,sysUser/lookup,sysDept/save', NULL, 'dept_list', 0, 0);
INSERT INTO `sys_module` VALUES ('dept_delete', NULL, 'sysDept/delete', NULL, 'dept_list', 0, 0);
INSERT INTO `sys_module` VALUES ('dept_list', 'sysDept/list', 'sysDept/lookup,sysUser/lookup', '<i class=\"icon-group icon-large\"></i>', 'user_menu', 1, 2);
INSERT INTO `sys_module` VALUES ('dept_user_list', 'sysDept/userList', 'sysDept/addUser,sysDept/saveUser,sysDept/enableUser,sysDept/disableUser', NULL, 'dept_list', 0, 0);
INSERT INTO `sys_module` VALUES ('develop', NULL, NULL, '<i class=\"icon-folder-close-alt icon-large\"></i>', NULL, 1, 7);
INSERT INTO `sys_module` VALUES ('dictionary_add', 'cmsDictionary/add', 'cmsDictionary/save', NULL, 'dictionary_list', 0, 0);
INSERT INTO `sys_module` VALUES ('dictionary_delete', NULL, 'cmsDictionary/delete', NULL, 'dictionary_list', 0, 0);
INSERT INTO `sys_module` VALUES ('dictionary_list', 'cmsDictionary/list', NULL, '<i class=\"icon-book icon-large\"></i>', 'system_menu', 1, 4);
INSERT INTO `sys_module` VALUES ('domain_config', 'sysDomain/config', 'sysDomain/saveConfig,cmsTemplate/directoryLookup,cmsTemplate/lookup', NULL, 'domain_list', 0, 0);
INSERT INTO `sys_module` VALUES ('domain_list', 'sysDomain/domainList', NULL, '<i class=\"icon-qrcode icon-large\"></i>', 'system_menu', 1, 3);
INSERT INTO `sys_module` VALUES ('file_menu', NULL, NULL, '<i class=\"icon-folder-close-alt icon-large\"></i>', 'develop', 1, 1);
INSERT INTO `sys_module` VALUES ('log_login', 'log/login', 'sysUser/lookup', '<i class=\"icon-signin icon-large\"></i>', 'log_menu', 1, 3);
INSERT INTO `sys_module` VALUES ('log_login_delete', NULL, 'logLogin/delete', NULL, 'log_login', 0, 0);
INSERT INTO `sys_module` VALUES ('log_menu', NULL, NULL, '<i class=\"icon-list-alt icon-large\"></i>', 'maintenance', 1, 3);
INSERT INTO `sys_module` VALUES ('log_operate', 'log/operate', 'sysUser/lookup', '<i class=\"icon-list-alt icon-large\"></i>', 'log_menu', 1, 2);
INSERT INTO `sys_module` VALUES ('log_operate_delete', NULL, 'logOperate/delete', NULL, 'log_operate', 0, 0);
INSERT INTO `sys_module` VALUES ('log_operate_view', 'log/operateView', NULL, NULL, 'log_operate', 0, 0);
INSERT INTO `sys_module` VALUES ('log_task', 'log/task', 'sysUser/lookup', '<i class=\"icon-time icon-large\"></i>', 'log_menu', 1, 4);
INSERT INTO `sys_module` VALUES ('log_task_delete', NULL, 'logTask/delete', NULL, 'log_task', 0, 0);
INSERT INTO `sys_module` VALUES ('log_task_view', 'log/taskView', NULL, NULL, 'log_task', 0, 0);
INSERT INTO `sys_module` VALUES ('log_upload', 'log/upload', 'sysUser/lookup', '<i class=\"icon-list-alt icon-large\"></i>', 'log_menu', 1, 1);
INSERT INTO `sys_module` VALUES ('maintenance', NULL, NULL, '<i class=\"icon-cogs icon-large\"></i>', NULL, 1, 6);
INSERT INTO `sys_module` VALUES ('model_add', 'cmsModel/add', 'cmsModel/save,cmsTemplate/lookup', NULL, 'model_list', 0, 0);
INSERT INTO `sys_module` VALUES ('model_delete', NULL, 'cmsModel/delete', NULL, 'model_list', 0, 0);
INSERT INTO `sys_module` VALUES ('model_list', 'cmsModel/list', NULL, '<i class=\"icon-th-large icon-large\"></i>', 'config_menu', 1, 1);
INSERT INTO `sys_module` VALUES ('myself', NULL, NULL, '<i class=\"icon-user icon-large\"></i>', NULL, 1, 1);
INSERT INTO `sys_module` VALUES ('myself_content', 'myself/contentList', NULL, '<i class=\"icon-book icon-large\"></i>', 'myself_menu', 1, 2);
INSERT INTO `sys_module` VALUES ('myself_content_add', 'cmsContent/add', 'cmsContent/addMore,file/doUpload,cmsContent/lookup,cmsContent/lookup_list,cmsContent/save,ueditor,ckeditor/upload', NULL, 'myself_content', 0, 0);
INSERT INTO `sys_module` VALUES ('myself_content_delete', NULL, 'cmsContent/delete', NULL, 'myself_content', 0, 0);
INSERT INTO `sys_module` VALUES ('myself_content_publish', NULL, 'cmsContent/publish', NULL, 'myself_content', 0, 0);
INSERT INTO `sys_module` VALUES ('myself_content_push', 'cmsContent/push', 'cmsContent/push_content,cmsContent/push_content_list,cmsContent/push_to_content,cmsContent/push_page,cmsContent/push_page_list,cmsContent/push_to_place,cmsContent/related', NULL, 'myself_content', 0, 0);
INSERT INTO `sys_module` VALUES ('myself_content_refresh', NULL, 'cmsContent/refresh', NULL, 'myself_content', 0, 0);
INSERT INTO `sys_module` VALUES ('myself_log_login', 'myself/logLogin', NULL, '<i class=\"icon-signin icon-large\"></i>', 'myself_menu', 1, 4);
INSERT INTO `sys_module` VALUES ('myself_log_operate', 'myself/logOperate', NULL, '<i class=\"icon-list-alt icon-large\"></i>', 'myself_menu', 1, 3);
INSERT INTO `sys_module` VALUES ('myself_menu', NULL, NULL, '<i class=\"icon-user icon-large\"></i>', 'myself', 1, 0);
INSERT INTO `sys_module` VALUES ('myself_password', 'myself/password', 'changePassword', '<i class=\"icon-key icon-large\"></i>', 'myself_menu', 1, 1);
INSERT INTO `sys_module` VALUES ('myself_token', 'myself/userTokenList', NULL, '<i class=\"icon-unlock-alt icon-large\"></i>', 'myself_menu', 1, 5);
INSERT INTO `sys_module` VALUES ('page', NULL, NULL, '<i class=\"icon-globe icon-large\"></i>', NULL, 1, 3);
INSERT INTO `sys_module` VALUES ('page_list', 'cmsPage/list', 'cmsPage/metadata,sysUser/lookup,cmsContent/lookup,cmsContent/lookup_list,cmsCategory/lookup', '<i class=\"icon-globe icon-large\"></i>', 'page_menu', 1, 1);
INSERT INTO `sys_module` VALUES ('page_menu', NULL, NULL, '<i class=\"icon-globe icon-large\"></i>', 'page', 1, 0);
INSERT INTO `sys_module` VALUES ('page_metadata', 'cmsPage/metadata', 'cmsPage/save', NULL, 'page_list', 0, 0);
INSERT INTO `sys_module` VALUES ('page_publish', NULL, 'cmsTemplate/publish', NULL, 'page_list', 0, 0);
INSERT INTO `sys_module` VALUES ('page_save', NULL, 'cmsPage/save,file/doUpload,cmsPage/clearCache', NULL, 'page_list', 0, 0);
INSERT INTO `sys_module` VALUES ('page_select_category', 'cmsCategory/lookup', NULL, NULL, 'page_list', 0, 0);
INSERT INTO `sys_module` VALUES ('page_select_category_type', 'cmsCategoryType/lookup', NULL, NULL, 'page_list', 0, 0);
INSERT INTO `sys_module` VALUES ('page_select_content', 'cmsContent/lookup', 'cmsContent/lookup_list', NULL, 'page_list', 0, 0);
INSERT INTO `sys_module` VALUES ('page_select_tag_type', 'cmsTagType/lookup', NULL, NULL, 'page_list', 0, 0);
INSERT INTO `sys_module` VALUES ('page_select_template', 'cmsTemplate/lookup', NULL, NULL, 'page_list', 0, 0);
INSERT INTO `sys_module` VALUES ('page_select_user', 'sysUser/lookup', NULL, NULL, 'page_list', 0, 0);
INSERT INTO `sys_module` VALUES ('place_add', 'cmsPlace/add', 'cmsContent/lookup,cmsPlace/lookup,cmsPlace/lookup_content_list,file/doUpload,cmsPlace/save', NULL, 'place_list', 0, 0);
INSERT INTO `sys_module` VALUES ('place_check', NULL, 'cmsPlace/check', NULL, 'place_list', 0, 0);
INSERT INTO `sys_module` VALUES ('place_clear', NULL, 'cmsPlace/clear', NULL, 'place_list', 0, 0);
INSERT INTO `sys_module` VALUES ('place_data_list', 'cmsPlace/dataList', NULL, NULL, 'place_list', 0, 1);
INSERT INTO `sys_module` VALUES ('place_delete', NULL, 'cmsPlace/delete', NULL, 'place_list', 0, 0);
INSERT INTO `sys_module` VALUES ('place_list', 'cmsPlace/list', 'sysUser/lookup,cmsPlace/data_list', '<i class=\"icon-list-alt icon-large\"></i>', 'page_menu', 1, 1);
INSERT INTO `sys_module` VALUES ('place_publish', 'cmsPlace/publish_place', 'cmsTemplate/publishPlace', NULL, 'place_list', 0, 0);
INSERT INTO `sys_module` VALUES ('place_refresh', NULL, 'cmsPlace/refresh', NULL, 'place_list', 0, 0);
INSERT INTO `sys_module` VALUES ('place_template_content', 'placeTemplate/content', 'cmsTemplate/help,cmsTemplate/chipLookup,cmsWebFile/lookup,cmsWebFile/contentForm,placeTemplate/form', NULL, 'place_template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('place_template_data_dictionary', 'cmsDictionary/lookup', NULL, NULL, 'place_template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('place_template_fragment', 'cmsTemplate/ftlLookup', NULL, NULL, 'place_template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('place_template_help', 'cmsTemplate/help', NULL, NULL, 'place_template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('place_template_list', 'placeTemplate/list', NULL, '<i class=\"icon-list-alt icon-large\"></i>', 'file_menu', 1, 2);
INSERT INTO `sys_module` VALUES ('place_template_metadata', 'placeTemplate/metadata', 'cmsTemplate/savePlaceMetaData', NULL, 'place_template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('place_template_place', 'placeTemplate/lookup', NULL, NULL, 'place_template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('place_template_webfile', 'cmsWebFile/lookup', NULL, NULL, 'place_template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('place_view', 'cmsPlace/view', NULL, NULL, 'place_list', 0, 0);
INSERT INTO `sys_module` VALUES ('report_user', 'report/user', NULL, '<i class=\"icon-male icon-large\"></i>', 'user_menu', 1, 5);
INSERT INTO `sys_module` VALUES ('role_add', 'sysRole/add', 'sysRole/save', NULL, 'role_list', 0, 0);
INSERT INTO `sys_module` VALUES ('role_delete', NULL, 'sysRole/delete', NULL, 'role_list', 0, 0);
INSERT INTO `sys_module` VALUES ('role_list', 'sysRole/list', NULL, '<i class=\"icon-user-md icon-large\"></i>', 'user_menu', 1, 3);
INSERT INTO `sys_module` VALUES ('system_menu', NULL, NULL, '<i class=\"icon-cogs icon-large\"></i>', 'maintenance', 1, 2);
INSERT INTO `sys_module` VALUES ('tag_add', 'cmsTag/add', 'cmsTagType/lookup,cmsTag/save', NULL, 'tag_list', 0, 0);
INSERT INTO `sys_module` VALUES ('tag_delete', NULL, 'cmsTag/delete', NULL, 'tag_list', 0, 0);
INSERT INTO `sys_module` VALUES ('tag_list', 'cmsTag/list', 'cmsTagType/lookup', '<i class=\"icon-tag icon-large\"></i>', 'content_extend', 1, 1);
INSERT INTO `sys_module` VALUES ('tag_type_delete', NULL, 'cmsTagType/delete', NULL, 'tag_type_list', 0, 0);
INSERT INTO `sys_module` VALUES ('tag_type_list', 'cmsTagType/list', NULL, '<i class=\"icon-tags icon-large\"></i>', 'category_extend', 1, 2);
INSERT INTO `sys_module` VALUES ('tag_type_save', 'cmsTagType/add', 'cmsTagType/save', NULL, 'tag_type_list', 0, 0);
INSERT INTO `sys_module` VALUES ('task_add', 'sysTask/add', 'sysTask/save,sysTask/example,taskTemplate/lookup', NULL, 'task_list', 0, 0);
INSERT INTO `sys_module` VALUES ('task_delete', NULL, 'sysTask/delete', NULL, 'task_list', 0, 0);
INSERT INTO `sys_module` VALUES ('task_list', 'sysTask/list', NULL, '<i class=\"icon-time icon-large\"></i>', 'system_menu', 1, 2);
INSERT INTO `sys_module` VALUES ('task_pause', NULL, 'sysTask/pause', NULL, 'task_list', 0, 0);
INSERT INTO `sys_module` VALUES ('task_recreate', NULL, 'sysTask/recreate', NULL, 'task_list', 0, 0);
INSERT INTO `sys_module` VALUES ('task_resume', NULL, 'sysTask/resume', NULL, 'task_list', 0, 0);
INSERT INTO `sys_module` VALUES ('task_runonce', NULL, 'sysTask/runOnce', NULL, 'task_list', 0, 0);
INSERT INTO `sys_module` VALUES ('task_template_content', 'taskTemplate/content', 'taskTemplate/save,taskTemplate/chipLookup,cmsTemplate/help,placeTemplate/form,cmsWebFile/contentForm', NULL, 'task_template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('task_template_delete', NULL, 'taskTemplate/delete', NULL, 'task_template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('task_template_fragment', 'taskTemplate/chipLookup', NULL, NULL, 'task_template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('task_template_help', 'cmsTemplate/help', NULL, NULL, 'task_template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('task_template_list', 'taskTemplate/list', NULL, '<i class=\"icon-time icon-large\"></i>', 'file_menu', 1, 3);
INSERT INTO `sys_module` VALUES ('template_content', 'cmsTemplate/content', 'cmsTemplate/save,cmsTemplate/chipLookup,cmsWebFile/lookup,placeTemplate/form,cmsWebFile/contentForm,cmsTemplate/demo,cmsTemplate/help,cmsTemplate/upload,cmsTemplate/doUpload', NULL, 'template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('template_content-type', 'cmsTemplate/contentTypeLookup', NULL, NULL, 'template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('template_content_form', 'cmsTemplate/contentForm', NULL, NULL, 'template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('template_data_dictionary', 'cmsDictionary/lookup', NULL, NULL, 'template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('template_delete', NULL, 'cmsTemplate/delete', NULL, 'template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('template_demo', 'cmsTemplate/demo', NULL, NULL, 'template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('template_fragment', 'cmsTemplate/ftlLookup', NULL, NULL, 'template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('template_help', 'cmsTemplate/help', NULL, NULL, 'template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('template_list', 'cmsTemplate/list', 'cmsTemplate/directory', '<i class=\"icon-code icon-large\"></i>', 'file_menu', 1, 1);
INSERT INTO `sys_module` VALUES ('template_metadata', 'cmsTemplate/metadata', 'cmsTemplate/saveMetadata', NULL, 'template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('template_place', 'placeTemplate/lookup', NULL, NULL, 'template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('template_place_form', 'placeTemplate/form', NULL, NULL, 'template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('template_upload', 'cmsTemplate/upload', 'cmsTemplate/doUpload', NULL, 'template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('template_website_file', 'cmsWebFile/lookup', NULL, NULL, 'template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('user_add', 'sysUser/add', 'sysDept/lookup,sysUser/save', NULL, 'user_list', 0, 0);
INSERT INTO `sys_module` VALUES ('user_disable', NULL, 'sysUser/disable', NULL, 'user_list', 0, 0);
INSERT INTO `sys_module` VALUES ('user_enable', NULL, 'sysUser/enable', NULL, 'user_list', 0, 0);
INSERT INTO `sys_module` VALUES ('user_list', 'sysUser/list', NULL, '<i class=\"icon-user icon-large\"></i>', 'user_menu', 1, 1);
INSERT INTO `sys_module` VALUES ('user_menu', NULL, NULL, '<i class=\"icon-user icon-large\"></i>', 'maintenance', 1, 1);
INSERT INTO `sys_module` VALUES ('webfile_content', 'cmsWebFile/content', 'cmsWebFile/save', NULL, 'webfile_list', 0, 0);
INSERT INTO `sys_module` VALUES ('webfile_directory', 'cmsWebFile/directory', 'cmsWebFile/createDirectory', NULL, 'webfile_list', 0, 0);
INSERT INTO `sys_module` VALUES ('webfile_list', 'cmsWebFile/list', NULL, '<i class=\"icon-globe icon-large\"></i>', 'file_menu', 1, 4);
INSERT INTO `sys_module` VALUES ('webfile_unzip', NULL, 'cmsWebFile/unzip,cmsWebFile/unzipHere', NULL, 'webfile_list', 0, 0);
INSERT INTO `sys_module` VALUES ('webfile_upload', 'cmsWebFile/upload', 'cmsWebFile/doUpload', NULL, 'webfile_list', 0, 0);
INSERT INTO `sys_module` VALUES ('webfile_zip', NULL, 'cmsWebFile/zip', NULL, 'webfile_list', 0, 0);
INSERT INTO `sys_module` VALUES ('word_list', 'cmsWord/list', NULL, '<i class=\"icon-search icon-large\"></i>', 'content_extend', 1, 2);

-- 20180609 --
ALTER TABLE `log_upload`
    CHANGE COLUMN `image` `file_type` varchar(20) NOT NULL COMMENT '文件类型' AFTER `channel`,
    ADD COLUMN `original_name` varchar(255) NULL COMMENT '原文件名' AFTER `channel`,
    DROP INDEX `image`,
    ADD INDEX `file_type`(`file_type`);
UPDATE `log_upload` SET file_type = 'image' WHERE file_type = '1';
UPDATE `log_upload` SET file_type = 'image' WHERE file_path like '%.png' or file_path like '%.jpg' or file_path like '%.gif' or file_path like '%.bmp';
UPDATE `log_upload` SET file_type = 'video' WHERE file_path like '%.mp4' or file_path like '%.3gp';
UPDATE `log_upload` SET file_type = 'other' WHERE file_type = '0';
-- 20180612 --
DROP TABLE IF EXISTS `cms_lottery`;
DROP TABLE IF EXISTS `cms_lottery_user`;
DROP TABLE IF EXISTS `cms_vote`;
DROP TABLE IF EXISTS `cms_vote_item`;
DROP TABLE IF EXISTS `cms_vote_user`;
-- 20180622 --
ALTER TABLE `cms_dictionary`
    ADD COLUMN `site_id` smallint(6) NOT NULL DEFAULT 1 COMMENT '站点ID' AFTER `id`,
    DROP INDEX `multiple`,
    ADD INDEX `siteId`(`site_id`, `multiple`);
-- 20180706 --
ALTER TABLE `sys_extend_field` MODIFY COLUMN `dictionary_id` bigint(20) NULL DEFAULT NULL COMMENT '数据字典ID' AFTER `default_value`;
ALTER TABLE `sys_extend_field` DROP COLUMN `dictionary_type`;
-- 20180711 --
ALTER TABLE `sys_site`
    ADD COLUMN `parent_id` smallint(6) DEFAULT NULL COMMENT '父站点ID' AFTER `id`,
    MODIFY COLUMN `name` varchar(50) NOT NULL COMMENT '站点名' AFTER `parent_id`;
-- 20180714 --
UPDATE `sys_module` SET `authorized_url` =  'cmsContent/addMore,file/doUpload,cmsContent/lookup,cmsContent/lookup_list,cmsContent/save,ueditor,ckeditor/upload,kindeditor/upload' WHERE `id` ='content_add';
UPDATE `sys_module` SET `authorized_url` =  'cmsContent/addMore,file/doUpload,cmsContent/lookup,cmsContent/lookup_list,cmsContent/save,ueditor,ckeditor/upload,kindeditor/upload' WHERE `id` ='myself_content_add';
UPDATE `sys_module` SET `authorized_url` =  'sysConfigData/save,sysConfigData/edit',url = NULL WHERE `id` ='config_data_edit';
-- 20180811 --
ALTER TABLE `sys_dept` ADD COLUMN `owns_all_config` tinyint(1) NOT NULL DEFAULT 1 COMMENT '拥有全部配置权限' AFTER `owns_all_page`;
CREATE TABLE `sys_dept_config` (
  `dept_id` int(11) NOT NULL COMMENT '部门ID',
  `config` varchar(100) NOT NULL COMMENT '配置',
  PRIMARY KEY (`dept_id`,`config`) 
) COMMENT='部门配置';
ALTER TABLE `sys_user` ADD COLUMN `owns_all_content` tinyint(1) NOT NULL DEFAULT 1 COMMENT '拥有所有内容权限' AFTER `dept_id`;
UPDATE `sys_user` SET `owns_all_content` = '0' WHERE `superuser_access` = '0';
-- 20180813 --
UPDATE `sys_module` SET `authorized_url` =  'cmsWebFile/unzip',url = 'cmsWebFile/unzipParameters' WHERE `id` ='webfile_unzip';
-- 20180820 --
UPDATE `sys_module` SET `authorized_url` =  'cmsPlace/check,cmsPlace/uncheck' WHERE `id` ='place_check';
-- 20180821 --
UPDATE `sys_module` SET `authorized_url` =  'cmsTemplate/help,cmsTemplate/savePlace,cmsTemplate/chipLookup,cmsWebFile/lookup,cmsWebFile/contentForm,placeTemplate/form' WHERE `id` ='place_template_content';
DELETE FROM `sys_email_token`;
ALTER TABLE `sys_email_token` ADD COLUMN `expiry_date` datetime(0) NOT NULL COMMENT '过期日期' AFTER `create_date`;
ALTER TABLE `sys_user_token` ADD COLUMN `expiry_date` datetime(0) DEFAULT NULL COMMENT '过期日期' AFTER `create_date`;
ALTER TABLE `sys_app_token` ADD COLUMN `expiry_date` datetime(0) DEFAULT NULL COMMENT '过期日期' AFTER `create_date`;
ALTER TABLE `sys_app` ADD COLUMN `expiry_minutes` int(0) NULL COMMENT '过期时间' AFTER `authorized_apis`;
UPDATE `sys_user_token` SET `expiry_date` = date_add(`create_date`, interval 30 day);
UPDATE `sys_app_token` SET `expiry_date` = date_add(`create_date`, interval 30 minute);
UPDATE `sys_app` SET `expiry_minutes` = '30';
INSERT INTO `sys_module` VALUES ('app_issue', 'sysApp/issueParameters', 'sysAppToken/issue', NULL, 'app_list', 0, 0);
INSERT INTO `sys_module_lang` VALUES ('app_issue', '', '颁发授权');
INSERT INTO `sys_module_lang` VALUES ('app_issue', 'en', 'Issue authorization');
UPDATE `sys_module` SET `attached` =  '<i class=\"icon-key icon-large\"></i>' WHERE `id` ='myself';
UPDATE `sys_module` SET `attached` =  '<i class=\"icon-file-text-alt icon-large\"></i>' WHERE `id` ='content';
UPDATE `sys_module` SET `attached` =  '<i class=\"icon-folder-open-alt icon-large\"></i>' WHERE `id` ='category';
UPDATE `sys_module` SET `attached` =  '<i class=\"icon-puzzle-piece icon-large\"></i>' WHERE `id` ='develop';
UPDATE `sys_module` SET `attached` =  '<i class=\"icon-tablet icon-large\"></i>' WHERE `id` ='page';
UPDATE `sys_module_lang` SET `value` =  'Add' WHERE `lang` ='en' and module_id = 'myself_content_add';
UPDATE `sys_module_lang` SET `value` =  'Delete' WHERE `lang` ='en' and module_id = 'myself_content_delete';
UPDATE `sys_module_lang` SET `value` =  'Publish' WHERE `lang` ='en' and module_id = 'myself_content_publish';
UPDATE `sys_module_lang` SET `value` =  'Push' WHERE `lang` ='en' and module_id = 'myself_content_push';
UPDATE `sys_module_lang` SET `value` =  'Refresh' WHERE `lang` ='en' and module_id = 'myself_content_refresh';
-- 20181022 --
UPDATE `sys_module_lang` SET `lang` =  'zh' WHERE `lang` ='';
INSERT INTO `sys_module_lang` VALUES ('app_add', 'ja', '追加/変更');
INSERT INTO `sys_module_lang` VALUES ('app_client_disable', 'ja', '禁止');
INSERT INTO `sys_module_lang` VALUES ('app_client_enable', 'ja', 'オン');
INSERT INTO `sys_module_lang` VALUES ('app_client_list', 'ja', 'クライアント管理');
INSERT INTO `sys_module_lang` VALUES ('app_issue', 'ja', '発行権限');
INSERT INTO `sys_module_lang` VALUES ('app_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('app_list', 'ja', 'app権限');
INSERT INTO `sys_module_lang` VALUES ('category', 'ja', '分類');
INSERT INTO `sys_module_lang` VALUES ('category_add', 'ja', '追加/変更');
INSERT INTO `sys_module_lang` VALUES ('category_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('category_extend', 'ja', '分類拡張');
INSERT INTO `sys_module_lang` VALUES ('category_menu', 'ja', '分類管理');
INSERT INTO `sys_module_lang` VALUES ('category_move', 'ja', '移動');
INSERT INTO `sys_module_lang` VALUES ('category_publish', 'ja', '生成');
INSERT INTO `sys_module_lang` VALUES ('category_push', 'ja', 'おすすめ');
INSERT INTO `sys_module_lang` VALUES ('category_type_add', 'ja', '追加/変更');
INSERT INTO `sys_module_lang` VALUES ('category_type_change', 'ja', 'タイプ変更');
INSERT INTO `sys_module_lang` VALUES ('category_type_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('category_type_list', 'ja', '分類タイプ');
INSERT INTO `sys_module_lang` VALUES ('clearcache', 'ja', 'キャッシュをリフレッシュする');
INSERT INTO `sys_module_lang` VALUES ('config_add', 'ja', '追加/変更');
INSERT INTO `sys_module_lang` VALUES ('config_data_delete', 'ja', 'データをクリア');
INSERT INTO `sys_module_lang` VALUES ('config_data_edit', 'ja', '変更');
INSERT INTO `sys_module_lang` VALUES ('config_data_list', 'ja', 'サイト設定');
INSERT INTO `sys_module_lang` VALUES ('config_delete', 'ja', '設定を削除');
INSERT INTO `sys_module_lang` VALUES ('config_list', 'ja', 'サイト設定管理');
INSERT INTO `sys_module_lang` VALUES ('config_list_data_dictionary', 'ja', 'データ辞書を選択');
INSERT INTO `sys_module_lang` VALUES ('config_menu', 'ja', '設定管理');
INSERT INTO `sys_module_lang` VALUES ('content', 'ja', 'コンテンツ');
INSERT INTO `sys_module_lang` VALUES ('content_add', 'ja', '追加/変更');
INSERT INTO `sys_module_lang` VALUES ('content_check', 'ja', '審査');
INSERT INTO `sys_module_lang` VALUES ('content_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('content_extend', 'ja', 'コンテンツ拡張');
INSERT INTO `sys_module_lang` VALUES ('content_menu', 'ja', 'コンテンツ管理');
INSERT INTO `sys_module_lang` VALUES ('content_move', 'ja', '移動');
INSERT INTO `sys_module_lang` VALUES ('content_publish', 'ja', '生成');
INSERT INTO `sys_module_lang` VALUES ('content_push', 'ja', 'おすすめ');
INSERT INTO `sys_module_lang` VALUES ('content_recycle_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('content_recycle_list', 'ja', 'コンテンツごみ箱');
INSERT INTO `sys_module_lang` VALUES ('content_recycle_recycle', 'ja', '取り戻し');
INSERT INTO `sys_module_lang` VALUES ('content_refresh', 'ja', 'リフレッシュ');
INSERT INTO `sys_module_lang` VALUES ('content_select_category', 'ja', '分類を選択');
INSERT INTO `sys_module_lang` VALUES ('content_select_category_type', 'ja', '分類タイプを選択');
INSERT INTO `sys_module_lang` VALUES ('content_select_content', 'ja', 'コンテンツを選択');
INSERT INTO `sys_module_lang` VALUES ('content_select_tag_type', 'ja', 'タグの種類を選択');
INSERT INTO `sys_module_lang` VALUES ('content_select_template', 'ja', 'テンプレートを選択');
INSERT INTO `sys_module_lang` VALUES ('content_select_user', 'ja', 'ユーザーを選択');
INSERT INTO `sys_module_lang` VALUES ('content_sort', 'ja', 'トッピング');
INSERT INTO `sys_module_lang` VALUES ('content_uncheck', 'ja', '審査を取り消す');
INSERT INTO `sys_module_lang` VALUES ('content_view', 'ja', '見る');
INSERT INTO `sys_module_lang` VALUES ('dept_add', 'ja', '追加/変更');
INSERT INTO `sys_module_lang` VALUES ('dept_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('dept_list', 'ja', '部門管理');
INSERT INTO `sys_module_lang` VALUES ('dept_user_list', 'ja', '人事管理');
INSERT INTO `sys_module_lang` VALUES ('develop', 'ja', '開発');
INSERT INTO `sys_module_lang` VALUES ('dictionary_add', 'ja', '追加/変更');
INSERT INTO `sys_module_lang` VALUES ('dictionary_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('dictionary_list', 'ja', 'データ辞書');
INSERT INTO `sys_module_lang` VALUES ('domain_config', 'ja', '変更');
INSERT INTO `sys_module_lang` VALUES ('domain_list', 'ja', 'ドメイン名をバインド');
INSERT INTO `sys_module_lang` VALUES ('file_menu', 'ja', 'ファイル管理');
INSERT INTO `sys_module_lang` VALUES ('log_login', 'ja', 'ログインログ');
INSERT INTO `sys_module_lang` VALUES ('log_login_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('log_menu', 'ja', 'ログ管理');
INSERT INTO `sys_module_lang` VALUES ('log_operate', 'ja', '操作ログ');
INSERT INTO `sys_module_lang` VALUES ('log_operate_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('log_operate_view', 'ja', '見る');
INSERT INTO `sys_module_lang` VALUES ('log_task', 'ja', 'タスク計画ログ');
INSERT INTO `sys_module_lang` VALUES ('log_task_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('log_task_view', 'ja', '見る');
INSERT INTO `sys_module_lang` VALUES ('log_upload', 'ja', 'ファイルアップロードログ');
INSERT INTO `sys_module_lang` VALUES ('maintenance', 'ja', '維持');
INSERT INTO `sys_module_lang` VALUES ('model_add', 'ja', '追加/変更');
INSERT INTO `sys_module_lang` VALUES ('model_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('model_list', 'ja', 'コンテンツモデル管理');
INSERT INTO `sys_module_lang` VALUES ('myself', 'ja', '個人');
INSERT INTO `sys_module_lang` VALUES ('myself_content', 'ja', 'マイコンテンツ');
INSERT INTO `sys_module_lang` VALUES ('myself_content_add', 'ja', '変更');
INSERT INTO `sys_module_lang` VALUES ('myself_content_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('myself_content_publish', 'ja', '生成');
INSERT INTO `sys_module_lang` VALUES ('myself_content_push', 'ja', 'おすすめ');
INSERT INTO `sys_module_lang` VALUES ('myself_content_refresh', 'ja', 'リフレッシュ');
INSERT INTO `sys_module_lang` VALUES ('myself_log_login', 'ja', 'マイログインログ');
INSERT INTO `sys_module_lang` VALUES ('myself_log_operate', 'ja', 'マイ操作ログ');
INSERT INTO `sys_module_lang` VALUES ('myself_menu', 'ja', '私に関連する情報');
INSERT INTO `sys_module_lang` VALUES ('myself_password', 'ja', 'パスワードを変更');
INSERT INTO `sys_module_lang` VALUES ('myself_token', 'ja', '私のログイン授権');
INSERT INTO `sys_module_lang` VALUES ('page', 'ja', 'ページ');
INSERT INTO `sys_module_lang` VALUES ('page_list', 'ja', 'ページ管理');
INSERT INTO `sys_module_lang` VALUES ('page_menu', 'ja', 'ページのメンテナンス');
INSERT INTO `sys_module_lang` VALUES ('page_metadata', 'ja', 'メタデータ管理');
INSERT INTO `sys_module_lang` VALUES ('page_publish', 'ja', 'ページを生成する');
INSERT INTO `sys_module_lang` VALUES ('page_save', 'ja', 'ページ設定を保存');
INSERT INTO `sys_module_lang` VALUES ('page_select_category', 'ja', '分類を選択');
INSERT INTO `sys_module_lang` VALUES ('page_select_category_type', 'ja', '分類タイプを選択');
INSERT INTO `sys_module_lang` VALUES ('page_select_content', 'ja', 'コンテンツを選択');
INSERT INTO `sys_module_lang` VALUES ('page_select_tag_type', 'ja', 'タグの種類を選択');
INSERT INTO `sys_module_lang` VALUES ('page_select_template', 'ja', '选择模板テンプレートを選択');
INSERT INTO `sys_module_lang` VALUES ('page_select_user', 'ja', 'ユーザーを選択');
INSERT INTO `sys_module_lang` VALUES ('place_add', 'ja', '推奨ビットデータの追加/変更');
INSERT INTO `sys_module_lang` VALUES ('place_check', 'ja', '推奨ビットデータを確認する');
INSERT INTO `sys_module_lang` VALUES ('place_clear', 'ja', '推奨ビットデータのクリア');
INSERT INTO `sys_module_lang` VALUES ('place_data_list', 'ja', '推奨ビットデータ');
INSERT INTO `sys_module_lang` VALUES ('place_delete', 'ja', '推奨ビットデータを削除する');
INSERT INTO `sys_module_lang` VALUES ('place_list', 'ja', 'ページフラグメント管理');
INSERT INTO `sys_module_lang` VALUES ('place_publish', 'ja', 'リリース');
INSERT INTO `sys_module_lang` VALUES ('place_refresh', 'ja', '刷新推荐位数据推奨ビットデータをリフレッシュする');
INSERT INTO `sys_module_lang` VALUES ('place_template_content', 'ja', '変更');
INSERT INTO `sys_module_lang` VALUES ('place_template_data_dictionary', 'ja', 'データ辞書を選択');
INSERT INTO `sys_module_lang` VALUES ('place_template_fragment', 'ja', 'テンプレートフラグメント');
INSERT INTO `sys_module_lang` VALUES ('place_template_help', 'ja', 'テンプレートのヘルプ');
INSERT INTO `sys_module_lang` VALUES ('place_template_list', 'ja', 'ページフラグメントテンプレート');
INSERT INTO `sys_module_lang` VALUES ('place_template_metadata', 'ja', 'メタデータの変更');
INSERT INTO `sys_module_lang` VALUES ('place_template_place', 'ja', 'ページフラグメント');
INSERT INTO `sys_module_lang` VALUES ('place_template_webfile', 'ja', 'ウェブサイトファイル');
INSERT INTO `sys_module_lang` VALUES ('place_view', 'ja', '推奨ビットデータを見る');
INSERT INTO `sys_module_lang` VALUES ('report_user', 'ja', 'ユーザーデータの監視');
INSERT INTO `sys_module_lang` VALUES ('role_add', 'ja', '追加/変更');
INSERT INTO `sys_module_lang` VALUES ('role_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('role_list', 'ja', '役割管理');
INSERT INTO `sys_module_lang` VALUES ('system_menu', 'ja', 'システムメンテナンス');
INSERT INTO `sys_module_lang` VALUES ('tag_add', 'ja', '追加/変更');
INSERT INTO `sys_module_lang` VALUES ('tag_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('tag_list', 'ja', 'ラベル管理');
INSERT INTO `sys_module_lang` VALUES ('tag_type_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('tag_type_list', 'ja', 'タグの分類');
INSERT INTO `sys_module_lang` VALUES ('tag_type_save', 'ja', '追加/変更');
INSERT INTO `sys_module_lang` VALUES ('task_add', 'ja', '追加/変更');
INSERT INTO `sys_module_lang` VALUES ('task_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('task_list', 'ja', 'タスク計画');
INSERT INTO `sys_module_lang` VALUES ('task_pause', 'ja', '停止');
INSERT INTO `sys_module_lang` VALUES ('task_recreate', 'ja', 'リセット');
INSERT INTO `sys_module_lang` VALUES ('task_resume', 'ja', '回復');
INSERT INTO `sys_module_lang` VALUES ('task_runonce', 'ja', '実行');
INSERT INTO `sys_module_lang` VALUES ('task_template_content', 'ja', '変更');
INSERT INTO `sys_module_lang` VALUES ('task_template_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('task_template_fragment', 'ja', 'タスク計画スクリプト断片');
INSERT INTO `sys_module_lang` VALUES ('task_template_help', 'ja', 'ヘルプ');
INSERT INTO `sys_module_lang` VALUES ('task_template_list', 'ja', 'タスク計画スクリプト');
INSERT INTO `sys_module_lang` VALUES ('template_content', 'ja', '変更');
INSERT INTO `sys_module_lang` VALUES ('template_content-type', 'ja', 'content-typeを選択');
INSERT INTO `sys_module_lang` VALUES ('template_content_form', 'ja', 'コンテンツ送信フォーム');
INSERT INTO `sys_module_lang` VALUES ('template_data_dictionary', 'ja', 'データ辞書を選択');
INSERT INTO `sys_module_lang` VALUES ('template_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('template_demo', 'ja', 'テンプレートの例');
INSERT INTO `sys_module_lang` VALUES ('template_fragment', 'ja', 'テンプレートフラグメント');
INSERT INTO `sys_module_lang` VALUES ('template_help', 'ja', 'テンプレートヘルプ');
INSERT INTO `sys_module_lang` VALUES ('template_list', 'ja', 'テンプレートファイル管理');
INSERT INTO `sys_module_lang` VALUES ('template_metadata', 'ja', 'メタデータの変更');
INSERT INTO `sys_module_lang` VALUES ('template_place', 'ja', 'ページフラグメント');
INSERT INTO `sys_module_lang` VALUES ('template_place_form', 'ja', 'ページフラグメント提出フォーム');
INSERT INTO `sys_module_lang` VALUES ('template_upload', 'ja', 'テンプレートをアップロードする');
INSERT INTO `sys_module_lang` VALUES ('template_website_file', 'ja', 'ウェブサイトファイル');
INSERT INTO `sys_module_lang` VALUES ('user_add', 'ja', '追加/変更');
INSERT INTO `sys_module_lang` VALUES ('user_disable', 'ja', '禁止');
INSERT INTO `sys_module_lang` VALUES ('user_enable', 'ja', 'オン');
INSERT INTO `sys_module_lang` VALUES ('user_list', 'ja', 'ユーザー管理');
INSERT INTO `sys_module_lang` VALUES ('user_menu', 'ja', 'ユーザー管理');
INSERT INTO `sys_module_lang` VALUES ('webfile_content', 'ja', 'ファイルの変更');
INSERT INTO `sys_module_lang` VALUES ('webfile_directory', 'ja', '目録を作成');
INSERT INTO `sys_module_lang` VALUES ('webfile_list', 'ja', 'ウェブサイトのファイル管理');
INSERT INTO `sys_module_lang` VALUES ('webfile_unzip', 'ja', '解凍');
INSERT INTO `sys_module_lang` VALUES ('webfile_upload', 'ja', 'アップロード');
INSERT INTO `sys_module_lang` VALUES ('webfile_zip', 'ja', '圧縮');
INSERT INTO `sys_module_lang` VALUES ('word_list', 'ja', '検索ワード管理');
-- 2018-11-06 --
ALTER TABLE `cms_category`
    MODIFY COLUMN `path` varchar(1000) DEFAULT NULL COMMENT '首页路径' AFTER `template_path`,
    MODIFY COLUMN `url` varchar(1000) DEFAULT NULL COMMENT '首页地址' AFTER `has_static`,
    MODIFY COLUMN `content_path` varchar(1000) DEFAULT NULL COMMENT '内容路径' AFTER `url`;
ALTER TABLE `cms_content`
    MODIFY COLUMN `url` varchar(1000) DEFAULT NULL COMMENT '地址' AFTER `has_static`;
ALTER TABLE `cms_content_attribute`
    MODIFY COLUMN `source_url` varchar(1000) DEFAULT NULL COMMENT '来源地址' AFTER `source`;
ALTER TABLE `cms_place`
    MODIFY COLUMN `url` varchar(1000) default NULL COMMENT '超链接' AFTER `title`;
ALTER TABLE `cms_content_related`
    MODIFY COLUMN `url` varchar(1000) default NULL COMMENT '推荐链接地址' AFTER `user_id`;
-- 2018-11-07 --
CREATE TABLE `cms_comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `site_id` smallint(6) NOT NULL COMMENT '站点ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `content_id` bigint(20) NOT NULL COMMENT '文章内容',
  `check_user_id` bigint(20) DEFAULT NULL COMMENT '审核用户',
  `check_date` datetime DEFAULT NULL COMMENT '审核日期',
  `update_date` datetime DEFAULT NULL COMMENT '更新日期',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `status` int(11) NOT NULL COMMENT '状态：1、已发布 2、待审核',
  `disabled` tinyint(1) NOT NULL COMMENT '已禁用',
  `text` text COMMENT '内容',
  PRIMARY KEY (`id`),
  KEY `site_id` (`site_id`,`content_id`,`status`,`disabled`, `create_date`)
) COMMENT='评论';
INSERT INTO `sys_module` VALUES ('comment_list', 'cmsComment/list', 'sysUser/lookup', '<i class=\"icon-comment icon-large\"></i>', 'content_extend', 1, 4);
INSERT INTO `sys_module` VALUES ('comment_check', NULL, 'cmsComment/check', NULL, 'comment_list', 0, 0);
INSERT INTO `sys_module` VALUES ('comment_uncheck', NULL, 'cmsComment/uncheck', NULL, 'comment_list', 0, 0);
INSERT INTO `sys_module` VALUES ('comment_delete', NULL, 'cmsComment/delete', NULL, 'comment_list', 0, 0);

INSERT INTO `sys_module_lang` VALUES ('comment_list', 'zh', '评论管理');
INSERT INTO `sys_module_lang` VALUES ('comment_list', 'en', 'Comment management');
INSERT INTO `sys_module_lang` VALUES ('comment_list', 'ja', 'コメント管理');
INSERT INTO `sys_module_lang` VALUES ('comment_check', 'zh', '审核');
INSERT INTO `sys_module_lang` VALUES ('comment_check', 'en', 'Check');
INSERT INTO `sys_module_lang` VALUES ('comment_check', 'ja', '審査');
INSERT INTO `sys_module_lang` VALUES ('comment_uncheck', 'zh', '取消审核');
INSERT INTO `sys_module_lang` VALUES ('comment_uncheck', 'en', 'Uncheck');
INSERT INTO `sys_module_lang` VALUES ('comment_uncheck', 'ja', '審査を取り消す');
INSERT INTO `sys_module_lang` VALUES ('comment_delete', 'zh', '删除');
INSERT INTO `sys_module_lang` VALUES ('comment_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('comment_delete', 'ja', '削除');
-- 2018-11-09 --
UPDATE `sys_module` SET `attached` = replace(replace(`attached`,'<i class=\"',''),' icon-large\"></i>','');
ALTER TABLE  `sys_module`
    MODIFY COLUMN `attached` varchar(50) default NULL COMMENT '标题附加' AFTER `authorized_url`;
ALTER TABLE `sys_user` 
    ADD COLUMN `salt` varchar(20) NULL COMMENT '混淆码,为空时则密码为md5,为10位时sha512(sha512(password)+salt)' AFTER `password`,
    ADD COLUMN `weak_password` tinyint(1) NOT NULL DEFAULT 0 COMMENT '弱密码' AFTER `salt`,
    MODIFY COLUMN `password` varchar(128) NOT NULL COMMENT '密码' AFTER `name`;
-- 2018-12-07 --
ALTER TABLE `cms_content_file`
    CHANGE COLUMN `image` `file_type` varchar(20) NOT NULL COMMENT '文件类型' AFTER `file_path`,
    CHANGE COLUMN `size` `file_size` bigint(20) NOT NULL COMMENT '文件大小' AFTER `file_type`,
    DROP INDEX `image`,
    DROP INDEX `size`,
    ADD INDEX `file_type`(`file_type`),
    ADD INDEX `file_size` (`file_size`);
UPDATE `cms_content_file` SET file_type = 'image' WHERE file_type = '1';
UPDATE `cms_content_file` SET file_type = 'image' WHERE file_path like '%.png' or file_path like '%.jpg' or file_path like '%.gif' or file_path like '%.bmp';
UPDATE `cms_content_file` SET file_type = 'video' WHERE file_path like '%.mp4' or file_path like '%.3gp';
UPDATE `cms_content_file` SET file_type = 'other' WHERE file_type = '0';
-- 2018-12-20 --
INSERT INTO `sys_module` VALUES ('content_export', NULL, 'cmsContent/export', '', 'content_menu', 1, 0);
INSERT INTO `sys_module_lang` VALUES ('content_export', 'ja', '輸出');
INSERT INTO `sys_module_lang` VALUES ('content_export', 'zh', '导出');
INSERT INTO `sys_module_lang` VALUES ('content_export', 'en', 'Export');
ALTER TABLE `cms_place` ADD COLUMN `check_user_id` bigint(20) NULL COMMENT '审核用户' AFTER `user_id`;
ALTER TABLE `cms_place` 
    DROP INDEX `publish_date`,
    DROP INDEX `site_id`,
    DROP INDEX `item_type`,
    DROP INDEX `user_id`,
    DROP INDEX `path`,
    DROP INDEX `disabled`,
    DROP INDEX `create_date`,
    DROP INDEX `status`,
    DROP INDEX `item_id`,
    ADD INDEX `publish_date`(`publish_date`, `create_date`) ,
    ADD INDEX `site_id`(`site_id`, `path`, `status`, `disabled`),
    ADD INDEX `item_type`(`item_type`, `item_id`) ,
    ADD INDEX `user_id`(`user_id`, `check_user_id`) ;
UPDATE `sys_module` SET `authorized_url` =  'cmsPlace/dataList,cmsPlace/export' WHERE `id` ='place_data_list';
UPDATE `sys_module` SET `authorized_url` =  'sysUser/lookup' WHERE `id` ='place_list';
-- 2018-12-22 --
ALTER TABLE `cms_comment` 
    ADD COLUMN `reply_id` bigint(20) NULL COMMENT '回复ID' AFTER `user_id`,
    DROP INDEX `site_id`,
    ADD INDEX `site_id`(`site_id`, `content_id`, `status`, `disabled`),
    ADD INDEX `update_date`(`update_date`, `create_date`),
    ADD INDEX `reply_id`(`site_id`, `reply_id`);
ALTER TABLE `cms_comment` 
    ADD COLUMN `reply_user_id` bigint(20) NULL COMMENT '回复用户ID' AFTER `reply_id`,
    DROP INDEX `reply_id`,
    ADD INDEX `reply_id`(`site_id`, `reply_user_id`, `reply_id`);
-- 2018-12-28 --
ALTER TABLE `cms_content` DROP INDEX `status`,
    ADD COLUMN `expiry_date` datetime NULL COMMENT '过期日期' AFTER `publish_date`,
    ADD INDEX `status` (`site_id`,`status`,`category_id`,`disabled`,`model_id`,`parent_id`,`sort`,`publish_date`,`expiry_date`);
ALTER TABLE `cms_place` DROP INDEX `publish_date`,
    ADD COLUMN `expiry_date` datetime NULL COMMENT '过期日期' AFTER `publish_date`,
    ADD INDEX `publish_date` (`publish_date`,`create_date`,`expiry_date`);
-- 2019-01-01 --
update cms_category set code=CONCAT(code,id) where (site_id,code) in (
    select * from (
        SELECT site_id,code FROM `cms_category` group by site_id,code having count(*) > 1
    ) a
) and id not in (
    select * from (
        select min(id) from cms_category where (site_id,code) in (
        SELECT site_id,code FROM `cms_category` group by site_id,code having count(*) > 1
        ) group by site_id,code
    ) b
);
ALTER TABLE `cms_category`
    MODIFY COLUMN `code` varchar(50) NOT NULL COMMENT '编码' AFTER `tag_type_ids`,
    DROP INDEX `site_id`,
    DROP INDEX `parent_id`,
    DROP INDEX `disabled`,
    DROP INDEX `type_id`,
    DROP INDEX `allow_contribute`,
    DROP INDEX `hidden`,
    ADD INDEX `type_id`(`type_id`, `allow_contribute`),
    ADD INDEX `site_id`(`site_id`, `parent_id`, `hidden`, `disabled`),
    ADD UNIQUE INDEX `code`(`site_id`, `code`);
ALTER TABLE `cms_content` 
    ADD COLUMN `quote_content_id` bigint(20) NULL COMMENT '引用内容ID' AFTER `parent_id`,
    ADD INDEX `quote_content_id`(`site_id`, `quote_content_id`);
-- 2019-01-11 --
UPDATE `sys_module` SET `authorized_url` =  'cmsWebFile/doUpload,cmsWebFile/check' WHERE `id` ='webfile_upload';
-- 2019-01-22 --
ALTER TABLE `cms_content` 
    ADD COLUMN `dictionar_values` text default NULL COMMENT '数据字典值' AFTER `tag_ids`;
-- 2019-01-24 --
ALTER TABLE `cms_dictionary` 
    MODIFY COLUMN `id` varchar(20) NOT NULL FIRST,
    DROP PRIMARY KEY,
    ADD PRIMARY KEY (`id`, `site_id`);
ALTER TABLE `cms_dictionary_data` 
    MODIFY COLUMN `dictionary_id` varchar(20) NOT NULL COMMENT '字典' FIRST,
    ADD COLUMN `site_id` smallint(0) NOT NULL COMMENT '站点ID' AFTER `dictionary_id`,
    DROP PRIMARY KEY,
    ADD PRIMARY KEY (`dictionary_id`, `site_id`, `value`);
update cms_dictionary_data a set a.site_id = (select site_id from cms_dictionary b where a.dictionary_id = b.id);
ALTER TABLE `sys_extend_field` 
    MODIFY COLUMN `dictionary_id` varchar(20) NULL DEFAULT NULL COMMENT '数据字典ID' AFTER `default_value`;
-- 2019-01-24 --
UPDATE `sys_module` SET `authorized_url` =  'cmsCategory/addMore,cmsCategory/virify,cmsTemplate/lookup,cmsCategory/categoryPath,cmsCategory/contentPath,file/doUpload,cmsCategory/save' WHERE `id` ='category_add';
UPDATE `sys_module` SET `authorized_url` =  'cmsDictionary/save,cmsDictionary/virify' WHERE `id` ='dictionary_add';
-- 2019-01-29 --
INSERT INTO `sys_module` VALUES ('myself_device', 'myself/userDeviceList', 'sysAppClient/enable,sysAppClient/disable', 'icon-linux', 'myself_menu', 1, 5);
UPDATE `sys_module` SET `authorized_url` =  'sysUserToken/delete' WHERE `id` ='myself_token';
INSERT INTO `sys_module_lang`(`module_id`, `lang`, `value`) VALUES ('myself_device', 'en', 'My device');
INSERT INTO `sys_module_lang`(`module_id`, `lang`, `value`) VALUES ('myself_device', 'ja', '私の端末');
INSERT INTO `sys_module_lang`(`module_id`, `lang`, `value`) VALUES ('myself_device', 'zh', '我的设备');
ALTER TABLE `sys_app_client` 
    ADD COLUMN `id` bigint(20) NOT NULL AUTO_INCREMENT FIRST,
    DROP PRIMARY KEY,
    ADD PRIMARY KEY (`id`),
    ADD UNIQUE INDEX(`site_id`, `channel`, `uuid`);
-- 2019-02-15 --
ALTER TABLE `cms_place` 
    MODIFY COLUMN `path` varchar(100) NOT NULL COMMENT '模板路径' AFTER `site_id`;
DELETE FROM `cms_word` WHERE LENGTH(name) > 100;
ALTER TABLE `cms_word` 
    MODIFY COLUMN `name` varchar(100) NOT NULL COMMENT '名称' AFTER `site_id`;
ALTER TABLE `sys_dept_page` 
    MODIFY COLUMN `page` varchar(100) NOT NULL COMMENT '页面' AFTER `dept_id`;
ALTER TABLE `sys_domain` 
    MODIFY COLUMN `name` varchar(100) NOT NULL COMMENT '域名' FIRST,
    MODIFY COLUMN `path` varchar(100) NULL DEFAULT NULL COMMENT '路径' AFTER `wild`;
ALTER TABLE `sys_role_authorized` 
    MODIFY COLUMN `url` varchar(100) NOT NULL COMMENT '授权地址' AFTER `role_id`;
-- 2019-02-19 --
ALTER TABLE `cms_content_attribute` 
    ADD COLUMN `search_text` longtext NULL COMMENT '全文索引文本' AFTER `data`;
ALTER TABLE `sys_extend_field` 
    ADD COLUMN `searchable` tinyint(1) NOT NULL COMMENT '是否可搜索' AFTER `required`,
    MODIFY COLUMN `maxlength` int(11) NULL DEFAULT NULL COMMENT '最大长度' AFTER `searchable`;
-- 2019-02-22 --
UPDATE `sys_module` SET `parent_id` = 'config_menu' where id = 'domain_list';
UPDATE `sys_module` SET `authorized_url` =  'cmsPlace/export',`url` = 'cmsPlace/dataList' WHERE `id` ='place_data_list';