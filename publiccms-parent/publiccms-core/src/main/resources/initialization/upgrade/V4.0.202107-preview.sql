-- 2021-07-30 --
UPDATE `sys_module` SET `authorized_url` = 'cmsContent/addMore,file/doUpload,file/doImport,cmsContent/lookup,cmsContent/lookup_list,cmsContent/save,ueditor,ckeditor/upload,kindeditor/upload,file/doBatchUpload' WHERE `id` ='content_add';
UPDATE `sys_module` SET `authorized_url` = 'file/doUpload,file/doImport,cmsContent/lookup,cmsContent/lookup_list,cmsContent/save,ueditor,ckeditor/upload,kindeditor/upload,file/doBatchUpload' WHERE `id` ='myself_content_add';
-- 2021-08-02 --
DROP TABLE IF EXISTS `sys_site_datasource`;
CREATE TABLE `sys_site_datasource` (
  `site_id` smallint(11) NOT NULL COMMENT '站点ID',
  `datasource` varchar(50) NOT NULL COMMENT '数据源名称',
  PRIMARY KEY (`site_id`,`datasource`),
  KEY `sys_site_datasource_datasource` (`datasource`)
) COMMENT='站点数据源';
DROP TABLE IF EXISTS `sys_datasource`;
CREATE TABLE `sys_datasource` (
  `name` varchar(50) NOT NULL COMMENT '名称',
  `config` varchar(1000) NOT NULL COMMENT '配置',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `update_date` datetime DEFAULT NULL COMMENT '更新日期',
  `disabled` tinyint(1) NOT NULL COMMENT '禁用',
  PRIMARY KEY (`name`)
) COMMENT='数据源';
ALTER TABLE `cms_content_file`
	DROP INDEX `cms_content_file_content_id`,
	DROP INDEX `cms_content_file_sort`,
	DROP INDEX `cms_content_file_user_id`,
	ADD INDEX `cms_content_file_content_id`(`content_id`, `sort`);
ALTER TABLE `cms_content`
    MODIFY COLUMN `id` bigint(20) NOT NULL FIRST;
ALTER TABLE `cms_comment`
    MODIFY COLUMN `id` bigint(20) NOT NULL FIRST;
-- 2021-08-06 --
ALTER TABLE `sys_site` 
	ADD COLUMN `directory` varchar(50) NULL COMMENT '目录' AFTER `parent_id`,
	DROP INDEX `sys_site_parent_id`,
	ADD UNIQUE INDEX `sys_site_parent_id`(`parent_id`, `directory`);
ALTER TABLE `sys_domain` 
    ADD COLUMN `multiple` tinyint(1) NOT NULL COMMENT '站点群' AFTER `wild`;

CREATE TABLE `log_visit_item` (
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `visit_date` date NOT NULL COMMENT '日期',
  `item_type` varchar(50) NOT NULL COMMENT '项目类型',
  `item_id` varchar(50) NOT NULL COMMENT '项目',
  `pv` bigint(20) NOT NULL COMMENT 'Page Views',
  `uv` bigint(20) DEFAULT NULL COMMENT 'User Views',
  `ipviews` bigint(20) DEFAULT NULL COMMENT 'IP数',
  PRIMARY KEY (`site_id`,`visit_date`,`item_type`,`item_id`),
  KEY `log_visit_session_id` (`site_id`,`visit_date`,`item_type`,`item_id`, `pv`)
) COMMENT='项目访问汇总';
CREATE TABLE `log_visit_url` (
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `visit_date` date NOT NULL COMMENT '日期',
  `url_md5` varchar(50) NOT NULL COMMENT 'URL MD5',
  `url_sha` varchar(100) NOT NULL COMMENT 'URL SHA',
  `url` varchar(2048) NOT NULL COMMENT 'URL',
  `pv` bigint(20) NOT NULL COMMENT 'Page Views',
  `uv` bigint(20) DEFAULT NULL COMMENT 'User Views',
  `ipviews` bigint(20) DEFAULT NULL COMMENT 'IP数',
  PRIMARY KEY (`site_id`,`visit_date`,`url_md5`,`url_sha`),
  KEY `log_visit_session_id` (`site_id`,`visit_date`,`pv`)
) COMMENT='页面访问汇总';
INSERT INTO `sys_module` VALUES ('log_visit_item', 'log/visitItem', NULL, 'icon-flag-checkered', 'log_menu', 1, 9);
INSERT INTO `sys_module` VALUES ('log_visit_url', 'log/visitUrl', NULL, 'icon-link', 'log_menu', 1, 8);
INSERT INTO `sys_module_lang` VALUES ('log_visit_item', 'en', 'Item visit log');
INSERT INTO `sys_module_lang` VALUES ('log_visit_item', 'ja', 'アイテム訪問ログ');
INSERT INTO `sys_module_lang` VALUES ('log_visit_item', 'zh', '项目访问日志');
INSERT INTO `sys_module_lang` VALUES ('log_visit_url', 'en', 'Page visit log');
INSERT INTO `sys_module_lang` VALUES ('log_visit_url', 'ja', 'ページアクセスログ');
INSERT INTO `sys_module_lang` VALUES ('log_visit_url', 'zh', '页面访问日志');
-- 2021-08-21 --
ALTER TABLE `cms_comment` ADD COLUMN `scores` int(11) NOT NULL COMMENT '分数' AFTER `replies`;