-- 2025-12-05 --
UPDATE sys_module SET has_child = 1 WHERE id = 'myself_profile';
-- 2025-12-16 --
UPDATE sys_module SET authorized_url = 'cmsContent/preview,cmsContent/previewBeforeSave' WHERE id = 'content_view';
-- 2025-12-28 --
INSERT INTO `sys_module` VALUES ('content_quote', 'cmsContent/quoteParameters', 'cmsContent/quote', NULL, 'content_list', 0, 0, 0);
ALTER TABLE `sys_extend_field` ADD COLUMN `multiple_lang` tinyint(1) NOT NULL DEFAULT 0 COMMENT '多语言' AFTER `multiple`;
INSERT INTO `sys_module_lang` VALUES ('content_quote', 'en', 'Quote');
INSERT INTO `sys_module_lang` VALUES ('content_quote', 'ja', '引用');
INSERT INTO `sys_module_lang` VALUES ('content_quote', 'zh', '引用');
-- 2026-01-04 --
ALTER TABLE `cms_category` ADD COLUMN `lang` varchar(20) default NULL COMMENT '语言' AFTER `parent_id`;
ALTER TABLE `cms_comment` ADD COLUMN `lang` varchar(20) default NULL COMMENT '语言' AFTER `content_id`;
ALTER TABLE `cms_content_file` ADD COLUMN `lang` varchar(20) default NULL COMMENT '语言' AFTER `content_id`;
ALTER TABLE `cms_content` ADD COLUMN `lang` varchar(20) default NULL COMMENT '语言' AFTER `parent_id`;
ALTER TABLE `cms_editor_history` ADD COLUMN `lang` varchar(20) default NULL COMMENT '语言' AFTER `field_name`;
ALTER TABLE `cms_place`
 ADD COLUMN `lang` varchar(20) default NULL COMMENT '语言' AFTER `item_id`,
 DROP INDEX `cms_place_site_id`,
 ADD INDEX  `cms_place_site_id` (`site_id`, `path`, `lang` ,`status`, `disabled`);
-- ----------------------------
-- Table structure for cms_language
-- ----------------------------
DROP TABLE IF EXISTS `cms_language`;
CREATE TABLE `cms_language` (
  `code` varchar(20) NOT NULL COMMENT '编码',
  `site_id` smallint NOT NULL COMMENT '站点',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `cover` varchar(255) DEFAULT NULL COMMENT '封面图',
  `html_lang` varchar(20) DEFAULT NULL COMMENT '页面语言',
  `sort` int NOT NULL DEFAULT '0' COMMENT '顺序',
  PRIMARY KEY (`code`,`site_id`)
) COMMENT='语言';
INSERT INTO `sys_module` VALUES ('lang_add', 'cmsLanguage/add', 'cmsLanguage/save,cmsLanguage/virify', NULL, 'lang_list', 0, 0, 0);
INSERT INTO `sys_module` VALUES ('lang_delete',  NULL,'cmsLanguage/delete', NULL, 'lang_list', 0, 0, 0);
INSERT INTO `sys_module` VALUES ('lang_list', 'cmsLanguage/list', NULL, 'bi bi-globe', 'config', 1, 1, 5);
INSERT INTO `sys_module_lang` VALUES ('lang_add', 'en', 'Add/edit');
INSERT INTO `sys_module_lang` VALUES ('lang_add', 'ja', '追加/変更');
INSERT INTO `sys_module_lang` VALUES ('lang_add', 'zh', '增加/修改');
INSERT INTO `sys_module_lang` VALUES ('lang_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('lang_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('lang_delete', 'zh', '删除');
INSERT INTO `sys_module_lang` VALUES ('lang_list', 'en', 'Language Management');
INSERT INTO `sys_module_lang` VALUES ('lang_list', 'ja', '言語管理');
INSERT INTO `sys_module_lang` VALUES ('lang_list', 'zh', '语言管理');
-- ----------------------------
-- Table structure for cms_category_lang
-- ----------------------------
DROP TABLE IF EXISTS `cms_category_lang`;
CREATE TABLE `cms_category_lang` (
  `category_id` int(11) NOT NULL,
  `lang` varchar(20) NOT NULL COMMENT '语言',
  `url` varchar(1000) DEFAULT NULL COMMENT '链接地址',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `title` varchar(80) DEFAULT NULL COMMENT '标题',
  `keywords` varchar(100) DEFAULT NULL COMMENT '关键词',
  `description` varchar(300) DEFAULT NULL COMMENT '描述',
  `data` longtext COMMENT '数据JSON',
  PRIMARY KEY (`category_id`,`lang`)
) COMMENT='分类多语言';

-- ----------------------------
-- Table structure for cms_content_lang
-- ----------------------------
DROP TABLE IF EXISTS `cms_content_lang`;
CREATE TABLE `cms_content_lang` (
  `content_id` bigint(20) NOT NULL,
  `lang` varchar(50) NOT NULL COMMENT '语言',
  `url` varchar(1000) DEFAULT NULL COMMENT '链接地址',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `author` varchar(50) DEFAULT NULL COMMENT '作者',
  `editor` varchar(50) DEFAULT NULL COMMENT '编辑',
  `description` varchar(300) DEFAULT NULL COMMENT '描述',
  `cover` varchar(255) DEFAULT NULL COMMENT '封面图',
  `data` longtext COMMENT '数据JSON',
  `text` longtext COMMENT '内容',
  PRIMARY KEY (`content_id`,`lang`)
) COMMENT='内容多语言';
-- 2026-01-08 --
ALTER TABLE `cms_content_attribute` MODIFY COLUMN `word_count` int NOT NULL DEFAULT 0 COMMENT '字数' AFTER `text`;
-- 2026-01-22 --
UPDATE sys_module SET parent_id = 'page_preview' WHERE id = 'page_diy_buttons';
UPDATE sys_module SET sort = 1 WHERE id = 'page_diy_buttons';
UPDATE sys_module SET has_child = 1 WHERE id = 'page_preview';
-- 2026-01-25 --
ALTER TABLE `cms_content_related` COMMENT = '内容推荐';
-- ----------------------------
-- Table structure for cms_content_source
-- ----------------------------
DROP TABLE IF EXISTS `cms_content_source`;
CREATE TABLE `cms_content_source` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `site_id` smallint NOT NULL COMMENT '站点',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `url` varchar(255) DEFAULT NULL COMMENT '地址',
  `initial` varchar(5) DEFAULT NULL COMMENT '缩写',
  `user_id` bigint DEFAULT NULL COMMENT '创建用户',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`),
  KEY `cms_content_source_user_id` (`site_id`,`user_id`,`create_date`),
  KEY `cms_content_source_initial` (`site_id`,`initial`)
) COMMENT='内容来源';
INSERT INTO `sys_module` VALUES ('select_content_source', 'cmsContentSource/lookup', NULL, NULL, 'common', 0, 0, 0);
INSERT INTO `sys_module` VALUES ('content_source', 'cmsContentSource/list', 'cmsContentSource/add,cmsContentSource/save,cmsContentSource/delete', NULL, 'content_list', 0, 0, 0);
INSERT INTO `sys_module_lang` VALUES ('content_source', 'en', 'Source Management');
INSERT INTO `sys_module_lang` VALUES ('content_source', 'ja', 'ソース管理');
INSERT INTO `sys_module_lang` VALUES ('content_source', 'zh', '来源管理');
INSERT INTO `sys_module_lang` VALUES ('select_content_source', 'en', 'Select source');
INSERT INTO `sys_module_lang` VALUES ('select_content_source', 'ja', 'ソースを選択');
INSERT INTO `sys_module_lang` VALUES ('select_content_source', 'zh', '选择来源');
-- 2026-01-30 --
ALTER TABLE `log_login` DROP COLUMN `error_password`; 
-- 2026-05-08 --
UPDATE sys_module SET authorized_url = 'cmsCategory/addMore,cmsCategory/virify,cmsCategory/rebuildChildIds,cmsCategory/addLang,cmsCategoryLang/save,cmsCategory/batchPublish,cmsCategory/batchCopy,cmsCategory/batchCreate,cmsCategory/batchSave,cmsCategory/seo,cmsCategory/saveSeo,cmsCategory/categoryPath,cmsCategory/contentPath,cmsCategory/save' WHERE id = 'category_add';
UPDATE sys_module SET authorized_url = 'cmsContent/addMore,cmsContent/save,cmsContent/addLang,cmsContentLang/save' WHERE id = 'content_add';
UPDATE sys_module SET authorized_url = 'sysConfig/save,sysConfig/virify' WHERE id = 'config_add';
UPDATE sys_module SET authorized_url = 'cmsModel/save,cmsModel/virify,cmsModel/rebuildSearchText,cmsModel/batchPublish' WHERE id = 'model_add';
-- 2026-05-18 --
ALTER TABLE `sys_user_setting` MODIFY COLUMN `data` longtext NOT NULL COMMENT '值' AFTER `code`;
INSERT INTO `sys_module` VALUES ('content_uncheck_list', 'cmsContent/uncheck_list', NULL, NULL, 'content_list', 0, 0, 0);
INSERT INTO `sys_module_lang` VALUES ('content_uncheck_list', 'en', 'Pending');
INSERT INTO `sys_module_lang` VALUES ('content_uncheck_list', 'ja', '審査待ち');
INSERT INTO `sys_module_lang` VALUES ('content_uncheck_list', 'zh', '待审核内容');
UPDATE sys_module SET url = NULL WHERE id = 'content_check';
-- 2026-05-24 --
ALTER TABLE `cms_dictionary_data` add `langdata` longtext COMMENT '语言JSON' AFTER `text`;
ALTER TABLE `cms_word`
  ADD `lang` varchar(50) DEFAULT NULL COMMENT '语言' AFTER `name`,
  DROP INDEX `cms_word_hidden`,
  ADD INDEX  `cms_word_hidden` (`site_id`,`lang`, `hidden`);
-- 2026-05-26 --
DELETE FROM sys_module WHERE module_id in ('myself_content_view','myself_process_view');
-- 2026-06-08 --
ALTER TABLE `cms_category` ADD COLUMN `parent_ids` text NULL COMMENT '所有父分类' AFTER `type_id`;