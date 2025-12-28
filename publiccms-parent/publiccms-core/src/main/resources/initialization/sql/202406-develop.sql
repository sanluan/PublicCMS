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
CREATE TABLE `cms_category_lang` (
  `category_id` int(11) NOT NULL,
  `lang` varchar(20) NOT NULL COMMENT '语言',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `title` varchar(80) DEFAULT NULL COMMENT '标题',
  `keywords` varchar(100) DEFAULT NULL COMMENT '关键词',
  `description` varchar(300) DEFAULT NULL COMMENT '描述',
  `data` longtext COMMENT '数据JSON',
  PRIMARY KEY (`category_id`,`lang`)
) COMMENT='分类多语言';
CREATE TABLE `cms_content_lang` (
  `content_id` bigint(20) NOT NULL,
  `lang` varchar(50) NOT NULL COMMENT '语言',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `author` varchar(50) DEFAULT NULL COMMENT '作者',
  `editor` varchar(50) DEFAULT NULL COMMENT '编辑',
  `description` varchar(300) DEFAULT NULL COMMENT '描述',
  `cover` varchar(255) DEFAULT NULL COMMENT '封面图',
  `data` longtext COMMENT '数据JSON',
  `text` longtext COMMENT '内容',
  PRIMARY KEY (`content_id`,`lang`)
) COMMENT='内容多语言';