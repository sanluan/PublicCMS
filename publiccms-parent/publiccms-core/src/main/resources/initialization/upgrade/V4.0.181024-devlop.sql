-- 2018-11-06 --
ALTER TABLE `cms_category`
    MODIFY COLUMN `path` varchar(1000) NOT NULL COMMENT '首页路径' AFTER `template_path`,
    MODIFY COLUMN `url` varchar(1000) NOT NULL COMMENT '首页地址' AFTER `has_static`,
    MODIFY COLUMN `content_path` varchar(1000) NOT NULL COMMENT '内容路径' AFTER `url`;
ALTER TABLE `cms_content`
    MODIFY COLUMN `url` varchar(1000) NOT NULL COMMENT '地址' AFTER `has_static`;
ALTER TABLE `cms_content_attribute`
    MODIFY COLUMN `source_url` varchar(1000) NOT NULL COMMENT '来源地址' AFTER `source`;
ALTER TABLE `cms_place`
    MODIFY COLUMN `url` varchar(1000) default NULL COMMENT '超链接' AFTER `title`;
ALTER TABLE `cms_content_related`
    MODIFY COLUMN `url` varchar(1000) default NULL COMMENT '推荐链接地址' AFTER `user_id`;
-- 2018-11-07 --
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
--2018-11-09--
UPDATE `sys_module` SET `attached` = replace(replace(`attached`,'<i class=\"',''),' icon-large\"></i>','');
ALTER TABLE  `sys_module`
    MODIFY COLUMN `attached` varchar(50) default NULL COMMENT '标题附加' AFTER `authorized_url`;
ALTER TABLE `sys_user` 
    ADD COLUMN `salt` varchar(20) NULL COMMENT '混淆码,为空时则密码为md5,不为空时为sha2(sha2(password)+salt)' AFTER `password`,
    ADD COLUMN `weak_password` tinyint(1) NOT NULL DEFAULT 0 COMMENT '弱密码' AFTER `salt`,
    MODIFY COLUMN `password` varchar(128) NOT NULL COMMENT '密码' AFTER `name`;