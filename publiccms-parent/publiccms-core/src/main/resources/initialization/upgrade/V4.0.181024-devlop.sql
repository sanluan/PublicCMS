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
  KEY `site_id` (`site_id`,`content_id`,`status`,`disabled`, `create_date`),
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
ALTER TABLE `cms_content` DROP INDEX `publish_date`,
    ADD COLUMN `expiry_date` datetime NULL COMMENT '过期日期' AFTER `publish_date`,
    ADD INDEX `publish_date` (`publish_date`,`create_date`,`expiry_date`);