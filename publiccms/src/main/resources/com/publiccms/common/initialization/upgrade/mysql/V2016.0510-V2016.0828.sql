-- 20160519 --

CREATE TABLE IF NOT EXISTS `log_upload` (
  `id` bigint(20) NOT NULL auto_increment,
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `channel` varchar(50) NOT NULL COMMENT '操作取到',
  `image` tinyint(1) NOT NULL COMMENT '图片',
  `ip` varchar(64) default NULL COMMENT 'IP',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `file_path` varchar(500) NOT NULL COMMENT '文件路径',
  PRIMARY KEY  (`id`),
  KEY `user_id` (`user_id`),
  KEY `create_date` (`create_date`),
  KEY `ip` (`ip`),
  KEY `site_id` (`site_id`),
  KEY `channel` (`channel`),
  KEY `image` (`image`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='上传日志' AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `sys_cluster` (
  `uuid` varchar(40) NOT NULL COMMENT 'uuid',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `heartbeat_date` datetime NOT NULL COMMENT '心跳时间',
  `master` tinyint(1) NOT NULL COMMENT '是否管理',
  PRIMARY KEY  (`uuid`),
  KEY `create_date` (`create_date`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='服务器集群' ;

UPDATE  `sys_moudle` SET  `parent_id` =  '30' WHERE  `sys_moudle`.`parent_id` =29;
insert into `sys_moudle`(`id`,`name`,`url`,`authorized_url`,`parent_id`,`sort`) values ('40','修改模板元数据','cmsTemplate/metadata','cmsTemplate/saveMetadata','39','0');
UPDATE  `sys_moudle` SET  `url` =  'cmsTemplate/content',`authorized_url` =  'cmsTemplate/save,cmsTemplate/chipLookup' WHERE  `sys_moudle`.`id` =41;
-- 20160723 --
ALTER TABLE `sys_domain`  DROP `login_path`,  DROP `register_path`;
UPDATE `sys_site` SET `site_path` = `dynamic_path` where `use_static`= 0;
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE IF NOT EXISTS `sys_config` (
  `id` int(11) NOT NULL auto_increment COMMENT 'ID',
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `code` varchar(50) NOT NULL COMMENT '配置项编码',
  `subcode` varchar(50) NOT NULL COMMENT '子编码',
  `data` longtext NOT NULL COMMENT '值',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `site_id` (`site_id`,`code`,`subcode`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='站点配置' AUTO_INCREMENT=1 ;

-- 20160727 --
ALTER TABLE `cms_category` ADD COLUMN `only_url`  tinyint(1) NOT NULL COMMENT '外链' AFTER `path`;
ALTER TABLE `sys_moudle` ADD COLUMN `attached`  varchar(300) DEFAULT NULL COMMENT '标题附加' AFTER `authorized_url`;
ALTER TABLE `sys_task` ADD COLUMN `update_date`  datetime DEFAULT NULL COMMENT '更新时间' AFTER `file_path`, ADD INDEX `update_date` (`update_date`) ;
UPDATE  `sys_moudle` SET  `name` =  '内容扩展', `attached`  = '<i class=\"icon-road icon-large\"></i>' WHERE  `sys_moudle`.`id` =13;
UPDATE `sys_moudle` set `name` = '个人', `attached` = '<i class="icon-user icon-large"></i>' WHERE `id` = 1;
UPDATE `sys_moudle` set `name` = '内容', `attached` = '<i class="icon-book icon-large"></i>' WHERE `id` = 2;
UPDATE `sys_moudle` set `name` = '分类', `attached` = '<i class="icon-folder-open icon-large"></i>' WHERE `id` = 3;
UPDATE `sys_moudle` set `name` = '页面', `attached` = '<i class="icon-globe icon-large"></i>' WHERE `id` = 4;
UPDATE `sys_moudle` set `name` = '维护', `attached` = '<i class="icon-cog icon-large"></i>' WHERE `id` = 5;
UPDATE `sys_moudle` set `name` = '与我相关', `attached` = '<i class="icon-user icon-large"></i>' WHERE `id` = 6;
UPDATE `sys_moudle` set `name` = '修改密码', `attached` = '<i class="icon-key icon-large"></i>' WHERE `id` = 7;
UPDATE `sys_moudle` set `name` = '我的内容', `attached` = '<i class="icon-book icon-large"></i>' WHERE `id` = 8;
UPDATE `sys_moudle` set `name` = '我的操作日志', `attached` = '<i class="icon-list-alt icon-large"></i>' WHERE `id` = 9;
UPDATE `sys_moudle` set `name` = '我的登陆日志', `attached` = '<i class="icon-signin icon-large"></i>' WHERE `id` = 10;
UPDATE `sys_moudle` set `name` = ' 我的登陆授权', `attached` = '<i class="icon-unlock-alt icon-large"></i>' WHERE `id` = 11;
UPDATE `sys_moudle` set `name` = '内容管理', `attached` = '<i class="icon-book icon-large"></i>' WHERE `id` = 12;
UPDATE `sys_moudle` set `name` = '内容扩展', `attached` = '<i class="icon-road icon-large"></i>' WHERE `id` = 13;
UPDATE `sys_moudle` set `name` = '标签管理', `attached` = '<i class="icon-tag icon-large"></i>' WHERE `id` = 14;
UPDATE `sys_moudle` set `name` = '分类管理', `attached` = '<i class="icon-folder-open icon-large"></i>' WHERE `id` = 24;
UPDATE `sys_moudle` set `name` = '页面管理', `attached` = '<i class="icon-globe icon-large"></i>' WHERE `id` = 30;
UPDATE `sys_moudle` set `name` = '分类扩展', `attached` = '<i class="icon-road icon-large"></i>' WHERE `id` = 31;
UPDATE `sys_moudle` set `name` = '分类类型', `attached` = '<i class="icon-road icon-large"></i>' WHERE `id` = 32;
UPDATE `sys_moudle` set `name` = '标签分类', `attached` = '<i class="icon-tags icon-large"></i>' WHERE `id` = 33;
UPDATE `sys_moudle` set `name` = '模板管理', `attached` = '<i class="icon-code icon-large"></i>' WHERE `id` = 38;
UPDATE `sys_moudle` set `name` = '页面模板', `attached` = '<i class="icon-globe icon-large"></i>' WHERE `id` = 39;
UPDATE `sys_moudle` set `name` = '搜索词管理', `attached` = '<i class="icon-search icon-large"></i>' WHERE `id` = 44;
UPDATE `sys_moudle` set `name` = '用户管理', `attached` = '<i class="icon-user icon-large"></i>' WHERE `id` = 61;
UPDATE `sys_moudle` set `name` = '系统维护', `attached` = '<i class="icon-cogs icon-large"></i>' WHERE `id` = 62;
UPDATE `sys_moudle` set `name` = '日志管理', `attached` = '<i class="icon-list-alt icon-large"></i>' WHERE `id` = 63;
UPDATE `sys_moudle` set `name` = '操作日志', `attached` = '<i class="icon-list-alt icon-large"></i>' WHERE `id` = 64;
UPDATE `sys_moudle` set `name` = '登录日志', `attached` = '<i class="icon-signin icon-large"></i>' WHERE `id` = 65;
UPDATE `sys_moudle` set `name` = '任务计划日志', `attached` = '<i class="icon-time icon-large"></i>' WHERE `id` = 66;
UPDATE `sys_moudle` set `name` = '用户管理', `attached` = '<i class="icon-user icon-large"></i>' WHERE `id` = 71;
UPDATE `sys_moudle` set `name` = '部门管理', `attached` = '<i class="icon-group icon-large"></i>' WHERE `id` = 72;
UPDATE `sys_moudle` set `name` = '角色管理', `attached` = '<i class="icon-user-md icon-large"></i>' WHERE `id` = 73;
UPDATE `sys_moudle` set `name` = '内容模型管理', `attached` = '<i class="icon-th-large icon-large"></i>' WHERE `id` = 81;
UPDATE `sys_moudle` set `name` = '任务计划', `attached` = '<i class="icon-time icon-large"></i>' WHERE `id` = 82;
UPDATE `sys_moudle` set `name` = 'FTP用户', `attached` = '<i class="icon-folder-open-alt icon-large"></i>' WHERE `id` = 83;
UPDATE `sys_moudle` set `name` = '动态域名', `attached` = '<i class="icon-qrcode icon-large"></i>' WHERE `id` = 84;
UPDATE `sys_moudle` set `name` = '任务计划脚本', `attached` = '<i class="icon-time icon-large"></i>' WHERE `id` = 85;
UPDATE `sys_moudle` set `name` = '用户登录授权', `attached` = '<i class="icon-unlock-alt icon-large"></i>' WHERE `id` = 88;
ALTER TABLE `cms_tag` MODIFY COLUMN `id`  bigint NOT NULL AUTO_INCREMENT FIRST, ADD COLUMN `search_count`  int NOT NULL  COMMENT '搜索次数' AFTER `type_id`;
ALTER TABLE `cms_content` MODIFY COLUMN `id`  bigint NOT NULL AUTO_INCREMENT FIRST ,MODIFY COLUMN `parent_id`  bigint DEFAULT NULL COMMENT '父内容ID' AFTER `model_id`;
ALTER TABLE `cms_content_attribute` MODIFY COLUMN `content_id`  bigint NOT NULL FIRST ;
ALTER TABLE `cms_content_file` MODIFY COLUMN `id`  bigint NOT NULL AUTO_INCREMENT FIRST ,MODIFY COLUMN `content_id`  bigint NOT NULL COMMENT '内容' AFTER `id`,MODIFY COLUMN `user_id`  bigint NOT NULL COMMENT '用户' AFTER `content_id`;
ALTER TABLE `cms_content_related` MODIFY COLUMN `id`  bigint NOT NULL AUTO_INCREMENT FIRST ,MODIFY COLUMN `content_id`  bigint NOT NULL COMMENT '内容' AFTER `id`,MODIFY COLUMN `related_content_id`  bigint NULL DEFAULT NULL COMMENT '推荐内容' AFTER `content_id`,MODIFY COLUMN `user_id`  bigint NOT NULL COMMENT '推荐用户' AFTER `related_content_id`;
ALTER TABLE `cms_content_tag` MODIFY COLUMN `id`  bigint NOT NULL AUTO_INCREMENT FIRST ,MODIFY COLUMN `tag_id` bigint NOT NULL COMMENT '标签ID' AFTER `id`,MODIFY COLUMN `content_id`  bigint NOT NULL COMMENT '内容ID' AFTER `tag_id`;
INSERT INTO `sys_moudle` VALUES ('44', '搜索词管理', 'cmsWord/list', null, '<i class=\"icon-search icon-large\"></i>', '13', '0');
INSERT INTO `sys_moudle` VALUES (101, '配置中心', 'sysConfig/list', 'sysConfig/subcode', '<i class=\"icon-cogs icon-large\"></i>', 62, 0);
UPDATE `cms_content` set tag_ids = replace(tag_ids,',',' ') WHERE `tag_ids` is not NULL;
ALTER TABLE `log_login` MODIFY COLUMN `id`  bigint NOT NULL AUTO_INCREMENT FIRST ,MODIFY COLUMN `user_id`  bigint NULL DEFAULT NULL COMMENT '用户ID' AFTER `name`;
ALTER TABLE `log_operate` MODIFY COLUMN `id`  bigint NOT NULL AUTO_INCREMENT FIRST ,MODIFY COLUMN `user_id`  bigint NOT NULL COMMENT '用户ID' AFTER `site_id`;
ALTER TABLE `log_task` MODIFY COLUMN `id`  bigint NOT NULL AUTO_INCREMENT FIRST ;
ALTER TABLE `log_upload` MODIFY COLUMN `id`  bigint NOT NULL AUTO_INCREMENT FIRST ,MODIFY COLUMN `user_id`  bigint NOT NULL COMMENT '用户ID' AFTER `site_id`;
ALTER TABLE `cms_content` MODIFY COLUMN `tag_ids`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '标签' AFTER `description`;
ALTER TABLE `cms_category` MODIFY COLUMN `tag_type_ids`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '标签分类' AFTER `child_ids`;
ALTER TABLE `plugin_vote_item` MODIFY COLUMN `id`  bigint NOT NULL AUTO_INCREMENT FIRST ;
ALTER TABLE `plugin_vote_item_attribute` MODIFY COLUMN `vote_item_id`  bigint NOT NULL COMMENT '选项ID' FIRST ;
ALTER TABLE `plugin_vote_user` MODIFY COLUMN `id`  bigint NOT NULL AUTO_INCREMENT FIRST ,MODIFY COLUMN `user_id`  bigint NOT NULL COMMENT '用户ID' AFTER `lottery_id`,MODIFY COLUMN `item_ids`  text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '投票选项' AFTER `user_id`;
ALTER TABLE `plugin_lottery_user` MODIFY COLUMN `id`  bigint NOT NULL AUTO_INCREMENT FIRST,MODIFY COLUMN `user_id`  bigint NOT NULL COMMENT '用户ID' AFTER `lottery_id`,DROP COLUMN `attribute_id`;
ALTER TABLE `plugin_lottery_user_attribute` MODIFY COLUMN `lottery_user_id`  bigint NOT NULL COMMENT '抽奖用户ID' FIRST ;
ALTER TABLE `cms_word` MODIFY COLUMN `id`  bigint NOT NULL AUTO_INCREMENT FIRST ,MODIFY COLUMN `search_count`  int(11) NOT NULL COMMENT '搜索次数' AFTER `site_id`,DROP INDEX `count` ,ADD INDEX `search_count` USING BTREE (`search_count`);
ALTER TABLE `cms_place` MODIFY COLUMN `id`  bigint NOT NULL AUTO_INCREMENT FIRST ,MODIFY COLUMN `user_id`  bigint NOT NULL COMMENT '提交用户' AFTER `path`;
ALTER TABLE `cms_place_attribute` MODIFY COLUMN `place_id`  bigint NOT NULL COMMENT '位置ID' FIRST ;
ALTER TABLE `sys_user` MODIFY COLUMN `id`  bigint NOT NULL AUTO_INCREMENT FIRST ;
ALTER TABLE `sys_user_token` MODIFY COLUMN `user_id`  bigint NOT NULL COMMENT '用户ID' AFTER `site_id`;
ALTER TABLE `sys_email_token` MODIFY COLUMN `user_id`  bigint NOT NULL COMMENT '用户ID' AFTER `auth_token`;
ALTER TABLE `sys_dept` MODIFY COLUMN `user_id`  bigint NULL DEFAULT NULL COMMENT '负责人' AFTER `description`;
ALTER TABLE `sys_role_user` MODIFY COLUMN `user_id`  bigint NOT NULL COMMENT '用户ID' AFTER `role_id`;
ALTER TABLE `home_message` MODIFY COLUMN `id`  bigint NOT NULL AUTO_INCREMENT FIRST ,MODIFY COLUMN `user_id`  bigint NOT NULL COMMENT '所属用户' AFTER `id`,MODIFY COLUMN `send_user_id`  bigint NOT NULL COMMENT '发送用户' AFTER `user_id`,MODIFY COLUMN `receive_user_id`  bigint NOT NULL COMMENT '接收用户' AFTER `send_user_id`,MODIFY COLUMN `message_id`  bigint NULL DEFAULT NULL COMMENT '关联消息' AFTER `receive_user_id`,MODIFY COLUMN `create_date`  datetime NOT NULL COMMENT '创建日期' AFTER `channel`;
ALTER TABLE `cms_content` MODIFY COLUMN `user_id`  bigint NOT NULL COMMENT '发表用户' AFTER `title`, MODIFY COLUMN `check_user_id`  bigint NULL DEFAULT NULL COMMENT '审核用户' AFTER `user_id`;
ALTER TABLE `sys_app_client` MODIFY COLUMN `id`  bigint NOT NULL AUTO_INCREMENT FIRST ,MODIFY COLUMN `user_id`  bigint NULL DEFAULT NULL COMMENT '绑定用户' AFTER `uuid`,MODIFY COLUMN `last_login_ip`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上次登录IP' AFTER `last_login_date`;
-- 20160806 --
ALTER TABLE  `log_upload` ADD  `file_size` BIGINT NOT NULL COMMENT  '文件大小' AFTER  `image` ,ADD INDEX (  `file_size` );
INSERT INTO `sys_moudle` VALUES (60, '文件上传日志', 'log/upload', 'sysUser/lookup', '<i class=\"icon-list-alt icon-large\"></i>', 63, 0);
