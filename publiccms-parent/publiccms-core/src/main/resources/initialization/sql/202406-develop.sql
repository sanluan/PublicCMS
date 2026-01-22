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
INSERT INTO `sys_module` VALUES ('lang_add', 'cmsLanguage/add', 'cmsLanguage/save', NULL, 'lang_list', 0, 0, 0);
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

