UPDATE `sys_module` SET `authorized_url` = 'sysUser/lookup,cmsContent/recycle,cmsContent/realDelete' WHERE  `id` = 117;
ALTER TABLE `sys_user` MODIFY COLUMN `last_login_ip` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后登录ip' AFTER `last_login_date`;
ALTER TABLE `sys_user_token` MODIFY COLUMN `login_ip` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登陆IP' AFTER `create_date`;
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
ALTER TABLE `sys_extend_field` DROP COLUMN `dictionary_type`;
INSERT INTO `sys_module` VALUES ('141', '修改内容模型', 'cmsContent/changeModelParameters', 'cmsContent/changeModel', null, '12', '0', '0');
