-- 2020-06-23 --
ALTER TABLE `cms_tag` 
    DROP INDEX `cms_tag_site_id`,
    ADD INDEX `cms_tag_site_id`(`site_id`, `name`),
    ADD INDEX  `cms_tag_type_id` (`type_id`);
ALTER TABLE `cms_tag_type`
    DROP INDEX `cms_tag_type_site_id`, 
    ADD INDEX `cms_tag_type_site_id` (`site_id`,`name`);
-- 2020-07-27 --
DELETE FROM `sys_module` WHERE id = 'category';
DELETE FROM `sys_module_lang` WHERE module_id = 'category';
UPDATE `sys_module` SET `parent_id` =  'content' WHERE `id` ='category_extend';
UPDATE `sys_module` SET `id` =  'category_list', `parent_id` = 'content_menu',`sort` = '1' WHERE `id` ='category_menu';
UPDATE `sys_module_lang` SET `module_id` =  'category_list' WHERE `module_id` ='category_menu';
UPDATE `sys_module` SET `parent_id` =  'category_list' WHERE `parent_id` ='category_menu';
UPDATE `sys_module` SET `parent_id` =  'content_list' WHERE `parent_id` ='content_menu';
UPDATE `sys_module` SET `parent_id` =  'content_menu' WHERE `parent_id` ='content_extend';
DELETE FROM `sys_module_lang` WHERE `module_id` = 'content_extend';
DELETE FROM `sys_module` WHERE `id` = 'content_extend';
UPDATE `sys_module` SET `parent_id` =  'content_menu', `sort` = '2' WHERE `id` ='category_list';
UPDATE `sys_module` SET `authorized_url` =  NULL, `url` = NULL WHERE `id` ='content_menu';
INSERT INTO `sys_module` VALUES ('content_list', 'cmsContent/list', 'sysUser/lookup', 'icon-book', 'content_menu', 1, 0);
UPDATE `sys_module` SET  `sort` = '1' where `id` = 'comment_list';
UPDATE `sys_module` SET  `sort` = '3' where `id` = 'tag_list';
UPDATE `sys_module` SET  `sort` = '4' where `id` = 'word_list';
UPDATE `sys_module` SET  `sort` = '5' where `id` = 'content_vote';
UPDATE `sys_module` SET  `sort` = '6' where `id` = 'content_recycle_list';
UPDATE `sys_module` SET  `attached` = NULL where `id` = 'content_add';
UPDATE `sys_module` SET  `menu` = '0' where `parent_id` = 'content_list';
UPDATE `sys_module` SET  `sort` = '1' where `id` = 'category_extend';
INSERT INTO `sys_module_lang` VALUES ('content_list', 'en', 'Content management');
INSERT INTO `sys_module_lang` VALUES ('content_list', 'ja', 'コンテンツ管理');
INSERT INTO `sys_module_lang` VALUES ('content_list', 'zh', '内容管理');
ALTER TABLE  `cms_content` DROP INDEX  `cms_content_quote_content_id`;