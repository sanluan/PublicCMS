-- 20160829 --
INSERT INTO `sys_moudle` VALUES (55, '查看推荐位源码', 'cmsTemplate/placeContent', NULL, NULL, '39', '0');

-- 20161011 --
ALTER TABLE `log_operate` MODIFY COLUMN `user_id`  bigint(20) NULL COMMENT '用户ID' AFTER `site_id`;
-- 20161109 --
DROP TABLE IF EXISTS `plugin_site`;
-- 20161112 --
INSERT INTO `sys_moudle` VALUES (45, '用户', null, null, '<i class=\"icon-user icon-large\"></i>', null, 0);
INSERT INTO `sys_moudle` VALUES (109, '空间管理', null, null, '<i class=\"icon-home icon-large\"></i>', 45, 0);
DELETE FROM `sys_moudle` WHERE id = 88;
UPDATE `sys_moudle` SET parent_id = 30 WHERE id = 107;

-- 20161119 --
ALTER TABLE `log_operate` CHANGE  `content` `content` text NOT NULL COMMENT '内容';
ALTER TABLE `cms_category_model` DROP `id`;
ALTER TABLE `cms_category_model` ADD PRIMARY KEY (  `category_id` ,  `model_id` ) ,DROP INDEX `category_id`, DROP INDEX `model_id`;
ALTER TABLE `cms_content_related` DROP INDEX  `user_id` , DROP INDEX  `clicks` , DROP INDEX  `sort` , ADD INDEX  `user_id` (`content_id`,`related_content_id`,`user_id`,`clicks`,`sort`);
ALTER TABLE `cms_content_tag` DROP `id`;
ALTER TABLE `cms_content_tag` ADD PRIMARY KEY (  `tag_id` ,  `content_id` ) ;
ALTER TABLE `cms_content_tag` DROP INDEX `content_id`,DROP INDEX  `tag_id`;
ALTER TABLE `cms_content_related` DROP INDEX `content_id`, DROP INDEX `related_content_id`;

ALTER TABLE `sys_dept_category` DROP `id`,DROP INDEX  `dept_id` ,DROP INDEX  `category_id` ,ADD PRIMARY KEY (  `dept_id` ,  `category_id` ) ;
ALTER TABLE `sys_dept_page` DROP `id`,DROP INDEX  `dept_id` ,DROP INDEX  `page` ,ADD PRIMARY KEY (  `dept_id` ,  `page` ) ;
ALTER TABLE `sys_role_authorized` DROP `id`,DROP INDEX  `role_id` ,DROP INDEX  `url` ,ADD PRIMARY KEY (  `role_id` ,  `url` ) ;
ALTER TABLE `sys_role_moudle` DROP `id`,DROP INDEX  `role_id` ,DROP INDEX  `moudle_id` ,ADD PRIMARY KEY (  `role_id` ,  `moudle_id` ) ;
ALTER TABLE `sys_role_user` DROP `id`,DROP INDEX  `role_id` ,DROP INDEX  `user_id` ,ADD PRIMARY KEY (  `role_id` ,  `user_id` ) ;

DROP TABLE IF EXISTS `plugin_lottery`;
DROP TABLE IF EXISTS `plugin_lottery_user`;
DROP TABLE IF EXISTS `plugin_lottery_user_attribute`;
DROP TABLE IF EXISTS `plugin_vote`;
DROP TABLE IF EXISTS `plugin_vote_item`;
DROP TABLE IF EXISTS `plugin_vote_item_attribute`;
DROP TABLE IF EXISTS `plugin_vote_user`;

CREATE TABLE `cms_lottery` (
  `id` int(11) NOT NULL auto_increment,
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `start_date` datetime NOT NULL COMMENT '开始日期',
  `end_date` datetime NOT NULL COMMENT '结束日期',
  `interval_hour` int(11) NOT NULL COMMENT '抽奖间隔小时',
  `gift` int(11) NOT NULL COMMENT '每次可抽奖数量',
  `total_gift` int(11) NOT NULL COMMENT '奖品总数',
  `last_gift` int(11) NOT NULL COMMENT '剩余数量',
  `lottery_count` int(11) NOT NULL COMMENT '可抽奖次数',
  `fractions` int(11) NOT NULL COMMENT '概率分子',
  `numerator` int(11) NOT NULL COMMENT '概率分母',
  `url` varchar(2048) default NULL COMMENT '地址',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `description` varchar(300) default NULL COMMENT '描述',
  `disabled` tinyint(1) NOT NULL COMMENT '是否禁用',
  `extend_id` int(11) default NULL COMMENT '扩展ID',
  PRIMARY KEY  (`id`),
  KEY `start_date` (`site_id`,`start_date`,`end_date`,`disabled`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE `cms_lottery_user` (
  `id` bigint(20) NOT NULL auto_increment COMMENT 'ID',
  `lottery_id` bigint(20) NOT NULL COMMENT '抽奖ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `winning` tinyint(1) NOT NULL COMMENT '是否中奖',
  `confirmed` tinyint(1) NOT NULL COMMENT '已确认',
  `confirm_date` datetime default NULL COMMENT '确认日期',
  `ip` varchar(64) NOT NULL COMMENT 'IP',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY  (`id`),
  KEY `lottery_id` (`lottery_id`,`user_id`,`winning`,`confirmed`,`create_date`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE `cms_lottery_user_attribute` (
  `lottery_user_id` bigint(20) NOT NULL COMMENT '抽奖用户ID',
  `data` longtext COMMENT '数据JSON',
  PRIMARY KEY  (`lottery_user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='抽奖用户扩展';

CREATE TABLE `cms_vote` (
  `id` int(11) NOT NULL auto_increment,
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `start_date` datetime NOT NULL COMMENT '开始日期',
  `end_date` datetime NOT NULL COMMENT '结束日期',
  `interval_hour` int(11) NOT NULL COMMENT '投票间隔小时',
  `max_vote` int(11) NOT NULL COMMENT '最大投票数',
  `anonymous` tinyint(1) NOT NULL COMMENT '匿名投票',
  `user_counts` int(11) NOT NULL COMMENT '参与用户数',
  `url` varchar(2048) NOT NULL COMMENT '地址',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `description` varchar(300) default NULL COMMENT '描述',
  `disabled` tinyint(1) NOT NULL COMMENT '已禁用',
  `item_extend_id` int(11) NOT NULL COMMENT '扩展ID',
  PRIMARY KEY  (`id`),
  KEY `disabled` (`site_id`,`start_date`,`end_date`,`disabled`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE `cms_vote_item` (
  `id` bigint(20) NOT NULL auto_increment,
  `vote_id` int(11) NOT NULL COMMENT '投票',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `description` varchar(300) default NULL COMMENT '描述',
  `scores` int(11) NOT NULL COMMENT '票数',
  `sort` int(11) NOT NULL COMMENT '顺序',
  PRIMARY KEY  (`id`),
  KEY `vote_id` (`vote_id`,`scores`,`sort`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE `cms_vote_item_attribute` (
  `vote_item_id` bigint(20) NOT NULL COMMENT '选项ID',
  `data` longtext COMMENT '数据JSON',
  PRIMARY KEY  (`vote_item_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='投票选项扩展';

DROP TABLE IF EXISTS `cms_vote_user`;
CREATE TABLE `cms_vote_user` (
  `id` bigint(20) NOT NULL auto_increment COMMENT 'ID',
  `vote_id` int(11) NOT NULL COMMENT '投票ID',
  `user_id` bigint(20) NOT NULL default '0' COMMENT '用户ID',
  `item_ids` text NOT NULL COMMENT '投票选项',
  `ip` varchar(64) NOT NULL COMMENT 'IP',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY  (`id`),
  KEY `vote_id` (`vote_id`,`user_id`,`ip`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

ALTER TABLE `sys_extend_field` DROP COLUMN `id`,
MODIFY COLUMN `code`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编码' AFTER `extend_id`,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`extend_id`, `code`),
ADD COLUMN `maxlength`  int NULL AFTER `required`,
ADD COLUMN `dictionary_type`  varchar(20) NULL AFTER `default_value`,ADD COLUMN `dictionary_id`  varchar(20) NULL AFTER `dictionary_type`,
ADD COLUMN `sort`  int(11) NOT NULL DEFAULT 0 COMMENT '顺序' AFTER `dictionary_id`,
DROP INDEX `item_id`,
ADD INDEX (`sort`);

INSERT INTO `sys_moudle` VALUES (130, '评论管理', 'homeComment/list', null, '<i class=\"icon-comment-alt icon-large\"></i>', '109', '0');

DROP TABLE IF EXISTS `home_active`;
CREATE TABLE `home_active` (
  `id` bigint(20) NOT NULL auto_increment COMMENT 'ID',
  `item_type` varchar(20) NOT NULL COMMENT '项目类型',
  `item_id` bigint(20) NOT NULL COMMENT '项目ID',
  `user_id` bigint(20) NOT NULL COMMENT '发布用户',
  `create_date` datetime NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `item_type` (`user_id`,`item_type`,`item_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='空间动态';


DROP TABLE IF EXISTS `home_article`;
CREATE TABLE `home_article` (
  `id` bigint(20) NOT NULL auto_increment COMMENT 'ID',
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `directory_id` bigint(20) default NULL COMMENT '目录ID',
  `title` varchar(255) NOT NULL COMMENT '标题',
  `user_id` bigint(20) NOT NULL COMMENT '发布用户',
  `cover` varchar(255) default NULL COMMENT '封面图',
  `scores` int(11) NOT NULL COMMENT '分数',
  `comments` int(11) NOT NULL COMMENT '评论数',
  `clicks` int(11) NOT NULL COMMENT '点击数',
  `last_comment_id` bigint(20) NOT NULL COMMENT '最新回复',
  `best_comment_id` bigint(20) NOT NULL COMMENT '最佳回复',
  `disabled` tinyint(1) NOT NULL COMMENT '已禁用',
  `create_date` datetime NOT NULL COMMENT '发布日期',
  PRIMARY KEY  (`id`),
  KEY `site_id` (`site_id`,`directory_id`,`user_id`,`create_date`),
  KEY `best_comment_id` (`best_comment_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='空间文章';


DROP TABLE IF EXISTS `home_article_content`;
CREATE TABLE `home_article_content` (
  `article_id` bigint(20) NOT NULL COMMENT '文章ID',
  `content` longtext COMMENT '内容',
  PRIMARY KEY  (`article_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='文章内容';


DROP TABLE IF EXISTS `home_attention`;
CREATE TABLE `home_attention` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `attention_id` bigint(20) NOT NULL COMMENT '关注ID',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY  (`user_id`,`attention_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='空间关注';


DROP TABLE IF EXISTS `home_broadcast`;
CREATE TABLE `home_broadcast` (
  `id` bigint(20) NOT NULL auto_increment COMMENT 'ID',
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `scores` int(11) NOT NULL COMMENT '分数',
  `reposts` int(11) NOT NULL COMMENT '转发数',
  `comments` int(11) NOT NULL COMMENT '评论数',
  `message` varchar(300) NOT NULL COMMENT '消息',
  `reposted` tinyint(1) NOT NULL COMMENT '转发',
  `repost_id` bigint(20) NOT NULL COMMENT '转发广播ID',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `disabled` tinyint(1) NOT NULL COMMENT '已禁用',
  PRIMARY KEY  (`id`),
  KEY `reposted` (`reposted`,`repost_id`),
  KEY `site_id` (`site_id`,`user_id`,`create_date`,`disabled`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='空间广播';


DROP TABLE IF EXISTS `home_comment`;
CREATE TABLE `home_comment` (
  `id` bigint(20) NOT NULL auto_increment COMMENT 'ID',
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `item_type` varchar(20) NOT NULL COMMENT '项目类型',
  `item_id` bigint(20) NOT NULL COMMENT '项目ID',
  `scores` int(11) NOT NULL COMMENT '分数',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `disabled` tinyint(1) NOT NULL COMMENT '已禁用',
  PRIMARY KEY  (`id`),
  KEY `site_id` (`site_id`,`user_id`,`item_type`,`item_id`,`disabled`,`create_date`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='评论';


DROP TABLE IF EXISTS `home_comment_content`;
CREATE TABLE `home_comment_content` (
  `comment_id` bigint(20) NOT NULL,
  `content` longtext,
  PRIMARY KEY  (`comment_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='评论内容';


DROP TABLE IF EXISTS `home_dialog`;
CREATE TABLE `home_dialog` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `item_type` varchar(20) NOT NULL COMMENT '项目类型',
  `item_id` bigint(20) NOT NULL COMMENT '项目ID',
  `messages` int(11) NOT NULL COMMENT '消息数',
  `last_message_date` datetime NOT NULL COMMENT '最新消息日期',
  `readed_message_date` datetime NOT NULL COMMENT '阅读日期',
  `disabled` tinyint(1) NOT NULL COMMENT '已禁用',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY  (`user_id`,`item_type`,`item_id`),
  KEY `last_message_date` (`disabled`,`last_message_date`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='对话';


DROP TABLE IF EXISTS `home_directory`;
CREATE TABLE `home_directory` (
  `id` bigint(20) NOT NULL auto_increment COMMENT 'ID',
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `user_id` bigint(20) NOT NULL COMMENT '发布用户',
  `title` varchar(255) NOT NULL COMMENT '标题',
  `cover` varchar(255) default NULL COMMENT '封面图',
  `files` int(11) NOT NULL COMMENT '文件数',
  `secret` tinyint(1) NOT NULL COMMENT '私密目录',
  `create_date` datetime NOT NULL COMMENT '发布日期',
  `disabled` tinyint(1) NOT NULL COMMENT '已禁用',
  PRIMARY KEY  (`id`),
  KEY `site_id` (`site_id`,`user_id`,`create_date`,`disabled`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='空间目录';


DROP TABLE IF EXISTS `home_file`;
CREATE TABLE `home_file` (
  `id` bigint(20) NOT NULL auto_increment COMMENT 'ID',
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `user_id` bigint(20) NOT NULL COMMENT '发布用户',
  `directory_id` bigint(20) default NULL COMMENT '目录',
  `title` varchar(255) NOT NULL COMMENT '标题',
  `file_path` varchar(255) NOT NULL COMMENT '封面图',
  `image` tinyint(1) NOT NULL COMMENT '是否图片',
  `file_size` int(11) NOT NULL COMMENT '文件大小',
  `scores` int(11) NOT NULL COMMENT '分数',
  `comments` int(11) NOT NULL COMMENT '评论数',
  `create_date` datetime NOT NULL COMMENT '发布日期',
  `disabled` tinyint(1) NOT NULL COMMENT '已禁用',
  PRIMARY KEY  (`id`),
  KEY `site_id` (`site_id`,`user_id`,`directory_id`,`image`,`create_date`,`disabled`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='空间文件';


DROP TABLE IF EXISTS `home_friend`;
CREATE TABLE `home_friend` (
  `id` bigint(20) NOT NULL auto_increment,
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `friend_id` bigint(20) NOT NULL COMMENT '好友ID',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `remark_name` varchar(100) NOT NULL COMMENT '备注名',
  PRIMARY KEY  (`id`),
  KEY `user_id` (`user_id`,`friend_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='好友';


DROP TABLE IF EXISTS `home_friend_apply`;
CREATE TABLE `home_friend_apply` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `friend_id` bigint(20) NOT NULL COMMENT '好友ID',
  `message` varchar(300) NOT NULL COMMENT '消息',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY  (`user_id`,`friend_id`),
  KEY `create_date` (`create_date`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='好友申请';


DROP TABLE IF EXISTS `home_group`;
CREATE TABLE `home_group` (
  `id` bigint(20) NOT NULL auto_increment COMMENT 'ID',
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `name` varchar(255) NOT NULL,
  `description` varchar(300) default NULL,
  `users` int(11) NOT NULL,
  `create_date` datetime NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `site_id` (`site_id`,`user_id`,`users`,`create_date`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='群组';


DROP TABLE IF EXISTS `home_group_active`;
CREATE TABLE `home_group_active` (
  `id` bigint(20) NOT NULL auto_increment COMMENT 'ID',
  `group_id` bigint(11) NOT NULL COMMENT '站点ID',
  `item_type` varchar(20) NOT NULL COMMENT '项目类型',
  `item_id` bigint(20) NOT NULL COMMENT '项目ID',
  `user_id` bigint(20) NOT NULL COMMENT '发布用户',
  `create_date` datetime NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `item_type` (`group_id`,`user_id`,`item_type`,`item_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='空间动态';


DROP TABLE IF EXISTS `home_group_apply`;
CREATE TABLE `home_group_apply` (
  `group_id` bigint(20) NOT NULL COMMENT '群组ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `message` text NOT NULL COMMENT '消息',
  `disabled` tinyint(1) NOT NULL COMMENT '已禁用',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY  (`group_id`,`user_id`),
  KEY `create_date` (`create_date`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='群组申请';


DROP TABLE IF EXISTS `home_group_post`;
CREATE TABLE `home_group_post` (
  `id` bigint(20) NOT NULL auto_increment COMMENT 'ID',
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `group_id` bigint(20) default NULL COMMENT '群组ID',
  `title` varchar(255) NOT NULL COMMENT '标题',
  `user_id` bigint(20) NOT NULL COMMENT '发布用户',
  `scores` int(11) NOT NULL COMMENT '分数',
  `comments` int(11) NOT NULL COMMENT '评论数',
  `clicks` int(11) NOT NULL COMMENT '点击数',
  `create_date` datetime NOT NULL COMMENT '发布日期',
  `disabled` tinyint(1) NOT NULL COMMENT '已禁用',
  PRIMARY KEY  (`id`),
  KEY `site_id` (`site_id`,`group_id`,`user_id`,`disabled`,`create_date`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='群组帖子';


DROP TABLE IF EXISTS `home_group_post_content`;
CREATE TABLE `home_group_post_content` (
  `post_id` bigint(20) NOT NULL,
  `content` longtext,
  PRIMARY KEY  (`post_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='帖子内容';


DROP TABLE IF EXISTS `home_group_user`;
CREATE TABLE `home_group_user` (
  `group_id` bigint(20) NOT NULL COMMENT '群组ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `superuser_access` tinyint(1) NOT NULL COMMENT '管理员',
  `disabled` tinyint(1) NOT NULL COMMENT '已禁用',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY  (`group_id`,`user_id`),
  KEY `create_date` (`create_date`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='群组用户';


DROP TABLE IF EXISTS `home_message`;
CREATE TABLE `home_message` (
  `id` bigint(20) NOT NULL auto_increment,
  `user_id` bigint(20) NOT NULL COMMENT '所属用户',
  `item_type` varchar(20) NOT NULL COMMENT '项目类型',
  `item_id` bigint(20) NOT NULL COMMENT '项目ID',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `content` longtext NOT NULL COMMENT '内容',
  PRIMARY KEY  (`id`),
  KEY `create_date` (`create_date`),
  KEY `user_id` (`user_id`,`item_type`,`item_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='用户消息';


DROP TABLE IF EXISTS `home_score`;
CREATE TABLE `home_score` (
  `id` bigint(20) NOT NULL auto_increment,
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `item_type` varchar(20) NOT NULL COMMENT '项目类型',
  `item_id` bigint(20) NOT NULL COMMENT '项目ID',
  `score` int(11) NOT NULL COMMENT '分数',
  `ip` varchar(64) NOT NULL COMMENT 'IP地址',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY  (`id`),
  KEY `site_id` (`site_id`,`user_id`,`item_type`,`item_id`,`create_date`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='评分';


DROP TABLE IF EXISTS `home_user`;
CREATE TABLE `home_user` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `title` varchar(255) default NULL COMMENT '标题',
  `signature` varchar(300) default NULL,
  `friends` int(11) NOT NULL COMMENT '好友数',
  `messages` int(11) NOT NULL COMMENT '消息数',
  `questions` int(11) NOT NULL COMMENT '问题数',
  `answers` int(11) NOT NULL COMMENT '回答数',
  `articles` int(11) NOT NULL COMMENT '文章数',
  `clicks` int(11) NOT NULL COMMENT '点击数数',
  `broadcasts` int(11) NOT NULL COMMENT '广播数',
  `comments` int(11) NOT NULL COMMENT '评论数',
  `attention_ids` text COMMENT '关注用户',
  `attentions` int(11) NOT NULL COMMENT '关注数',
  `fans` int(11) NOT NULL COMMENT '粉丝数',
  `last_login_date` datetime default NULL COMMENT '上次登陆日期',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `disabled` tinyint(1) NOT NULL COMMENT '已禁用',
  PRIMARY KEY  (`user_id`),
  KEY `site_id` (`site_id`,`last_login_date`,`create_date`,`disabled`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='用户空间';

DROP TABLE IF EXISTS `cms_dictionary`;
CREATE TABLE `cms_dictionary` (
  `id` bigint(20) NOT NULL auto_increment,
  `name` varchar(100) NOT NULL COMMENT '名称',
  `multiple` tinyint(1) NOT NULL COMMENT '允许多选',
  PRIMARY KEY  (`id`),
  KEY `multiple` (`multiple`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='字典';

-- ----------------------------
-- Records of cms_dictionary
-- ----------------------------

-- ----------------------------
-- Table structure for cms_dictionary_data
-- ----------------------------
DROP TABLE IF EXISTS `cms_dictionary_data`;
CREATE TABLE `cms_dictionary_data` (
  `dictionary_id` bigint(20) NOT NULL COMMENT '字典',
  `value` varchar(50) NOT NULL COMMENT '值',
  `text` varchar(100) NOT NULL COMMENT '文字',
  PRIMARY KEY  (`dictionary_id`,`value`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='字典数据';

-- 20161203--

UPDATE `sys_moudle` SET name = '模板文件管理',attached = '<i class="icon-code icon-large"></i>' WHERE id = 39;
UPDATE `sys_moudle` SET authorized_url = 'cmsTemplate/placeMetadata,cmsTemplate/placeContent,cmsTemplate/placeForm,cmsTemplate/saveMetadata,cmsTemplate/createPlace' WHERE id = 42;
UPDATE `sys_moudle` SET url = null WHERE id = 53;
UPDATE `sys_moudle` SET name = '文件管理',attached='<i class="icon-folder-close-alt icon-large"></i>' WHERE id = 38;

-- 20161206 --
UPDATE `sys_moudle` SET parent_id = 38 WHERE id = 81;
ALTER TABLE `sys_config` DROP COLUMN `id`,DROP PRIMARY KEY,DROP INDEX `site_id`,ADD PRIMARY KEY (`site_id`, `code`, `subcode`);
ALTER TABLE `sys_config` RENAME `sys_config_data`;

-- 20161208 --
DELETE FROM `sys_moudle` WHERE id = 88;
DELETE FROM `sys_moudle` WHERE id = 89;

-- 20161209 --
ALTER TABLE `cms_category_model` CHANGE COLUMN `model_id` `model_id`  varchar(20) NOT NULL COMMENT '模型编码' AFTER `category_id`;
ALTER TABLE `cms_content` MODIFY COLUMN `model_id`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模型' AFTER `category_id`;

UPDATE `sys_moudle` SET url = 'sysConfigData/list', authorized_url=null,name = '站点配置',attached='<i class=\"icon-cog icon-large\"></i>' WHERE id = 101;

ALTER TABLE `sys_config_data` CHANGE COLUMN `subcode` `item_code`  varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '配置项编码' AFTER `code`;

-- 20161210 --
ALTER TABLE `sys_cluster` ADD COLUMN `cms_version`  varchar(20) NULL AFTER `master`;

-- 20161215 --
ALTER TABLE `sys_domain` DROP COLUMN `id`,DROP PRIMARY KEY,DROP INDEX `name`,ADD PRIMARY KEY (`name`);
ALTER TABLE `sys_site` DROP COLUMN `resource_path`;
INSERT INTO `sys_moudle` VALUES ('131', '网站文件管理', 'cmsWebFile/list', null, '<i class=\"icon-globe icon-large\"></i>', '38', '0');
INSERT INTO `sys_moudle` VALUES ('132', '新建目录', 'cmsWebFile/directory', 'cmsWebFile/createDirectory', null, '131', '0');
INSERT INTO `sys_moudle` VALUES ('133', '上传文件', 'cmsWebFile/upload', 'cmsWebFile/doUpload', null, '131', '0');
INSERT INTO `sys_moudle` VALUES ('134', '压缩', null, 'cmsWebFile/zip', null, '131', '0');
INSERT INTO `sys_moudle` VALUES ('135', '解压缩', null, 'cmsWebFile/unzip,cmsWebFile/unzipHere', null, '131', '0');
INSERT INTO `sys_moudle` VALUES ('136', '节点管理', 'sysCluster/list', NULL, '<i class=\"icon-code-fork icon-large\"></i>', '62', '0');
INSERT INTO `sys_moudle` VALUES ('138', '修改配置', 'sysConfigData/edit', 'sysConfigData/save', null, '101', '0');
INSERT INTO `sys_moudle` VALUES ('139', '清空配置', null, 'sysConfigData/delete', null, '101', '0');
INSERT INTO `sys_moudle` VALUES ('140', '站点配置管理', 'sysConfig/list', null, '<i class=\"icon-cogs icon-large\"></i>', '38', '0');
INSERT INTO `sys_moudle` VALUES ('142', '保存配置', null, 'sysConfig/save', null, '140', '0');
INSERT INTO `sys_moudle` VALUES ('143', '修改配置', 'sysConfig/add', null, null, '140', '0');
INSERT INTO `sys_moudle` VALUES ('144', '删除配置', null, 'sysConfig/delete', null, '140', '0');
DROP TABLE IF EXISTS `sys_ftp_user`;
ALTER TABLE `sys_app_client`  DROP COLUMN `allow_push`, DROP COLUMN `push_token` ,DROP COLUMN `id`, DROP PRIMARY KEY, ADD PRIMARY KEY (`site_id`, `channel`, `uuid`),DROP INDEX `site_id`,DROP INDEX `disabled`,DROP INDEX `create_date`,DROP INDEX `channel`, DROP INDEX `user_id` ,ADD INDEX `user_id` (`user_id`, `disabled`, `create_date`),MODIFY COLUMN `client_version` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '版本' AFTER `user_id`;
ALTER TABLE `sys_app` ADD COLUMN `authorized_apis`  text NULL COMMENT '授权API' AFTER `app_secret`;
UPDATE `sys_moudle` SET name = '运营' WHERE id = 45;
UPDATE `sys_moudle` SET parent_id = 5 WHERE id = 61;
DELETE FROM `sys_moudle` where `id` = 137;
DELETE FROM `sys_moudle` where `id` = 141;
DELETE FROM `sys_moudle` where `id` = 83;
DELETE FROM `sys_moudle` where `id` = 98;
DELETE FROM `sys_moudle` where `id` = 99;
INSERT INTO `sys_moudle` VALUES ('46', '报表管理', NULL, NULL, '<i class=\"icon-sort-by-attributes-alt icon-large\"></i>', '45', '0');
INSERT INTO `sys_moudle` VALUES ('83', '系统监控', 'report/cms', NULL, '<i class=\"icon-check-sign icon-large\"></i>', '46', '0');
INSERT INTO `sys_moudle` VALUES ('56', '应用授权', 'sysApp/list', NULL, '<i class=\"icon-linux icon-large\"></i>', '62', '0');
INSERT INTO `sys_moudle` VALUES ('57', '增加/修改', 'sysApp/add', NULL, '', '56', '0');
INSERT INTO `sys_moudle` VALUES ('58', '保存', NULL, 'sysApp/save', '', '56', '0');
INSERT INTO `sys_moudle` VALUES ('59', '删除', NULL, 'sysApp/delete', NULL, '56', '0');
INSERT INTO `sys_moudle` VALUES ('88', '客户端管理', 'sysAppClient/list', NULL, '<i class=\"icon-coffee icon-large\"></i>', '61', '0');
INSERT INTO `sys_moudle` VALUES ('89', '启用', NULL, 'sysAppClient/enable', NULL, '88', '0');
INSERT INTO `sys_moudle` VALUES ('98', '禁用', NULL, 'sysAppClient/disable', NULL, '88', '0');
INSERT INTO `sys_moudle` VALUES ('99', '抽奖管理', 'cmsLottery/list', NULL, '<i class=\"icon-ticket icon-large\"></i>', '109', '0');
INSERT INTO `sys_moudle` VALUES ('108', '增加/修改', 'cmsLottery/add', NULL, NULL, '1007', '0');
INSERT INTO `sys_moudle` VALUES ('145', '保存', NULL, 'cmsLottery/save', NULL, '1007', '0');
INSERT INTO `sys_moudle` VALUES ('146', '删除', NULL, 'cmsLottery/delete', NULL, '1007', '0');
INSERT INTO `sys_moudle` VALUES ('147', '抽奖用户管理', 'cmsLotteryUser/list', 'sysUser/lookup', '<i class=\"icon-smile icon-large\"></i>', '109', '0');
INSERT INTO `sys_moudle` VALUES ('148', '删除', NULL, 'cmsLotteryUser/delete', NULL, '1011', '0');
INSERT INTO `sys_moudle` VALUES ('149', '投票管理', 'cmsVote/list', NULL, '<i class=\"icon-hand-right icon-large\"></i>', '109', '0');
INSERT INTO `sys_moudle` VALUES ('150', '增加/修改', 'cmsVote/add', NULL, NULL, '1013', '0');
INSERT INTO `sys_moudle` VALUES ('151', '保存', NULL, 'cmsVote/save', NULL, '1013', '0');
INSERT INTO `sys_moudle` VALUES ('152', '删除', NULL, 'cmsVote/delete', NULL, '1013', '0');
INSERT INTO `sys_moudle` VALUES ('153', '查看', 'cmsVote/view', NULL, NULL, '1013', '0');
INSERT INTO `sys_moudle` VALUES ('154', '投票用户', 'cmsVoteUser/list', 'sysUser/lookup', NULL, '1013', '0');
ALTER TABLE `sys_moudle` ORDER BY  `id`;
UPDATE `sys_moudle` SET name = '我的登陆授权' where id = 11;
UPDATE `sys_moudle` SET authorized_url='cmsTemplate/save,cmsTemplate/chipLookup,cmsResource/lookup,cmsWebFile/lookup,cmsTemplate/demo,cmsTemplate/help,cmsTemplate/upload,cmsTemplate/doUpload' WHERE id = 41;
UPDATE `sys_moudle` SET authorized_url='cmsContent/lookup,cmsPage/lookup,cmsPage/lookup_content_list,file/doUpload,cmsPlace/save' WHERE id = 49;
DELETE FROM sys_config_data;
ALTER TABLE `sys_config_data` DROP COLUMN `item_code`,DROP PRIMARY KEY,ADD PRIMARY KEY (`site_id`, `code`);
-- 20170214 --
UPDATE `sys_moudle` SET sort = 1 where id = 3;
UPDATE `sys_moudle` SET sort = 1 where id = 5;
UPDATE `sys_moudle` SET parent_id = 46 where id = 101;
UPDATE `sys_moudle` SET attached = '<i class="icon-cogs icon-large"></i>' where id = 5;
UPDATE `sys_moudle` SET attached = '<i class="icon-cog icon-large"></i>',name='系统管理' where id = 46;
ALTER TABLE `sys_domain` ADD COLUMN `wild` tinyint(1) NOT NULL COMMENT '通配域名' AFTER `site_id`;
UPDATE `sys_moudle` SET parent_id = 109 where id = 44;
UPDATE `sys_moudle` SET parent_id = 109 where id = 99;
UPDATE `sys_moudle` SET parent_id = 109 where id = 147;
UPDATE `sys_moudle` SET parent_id = 109 where id = 149;
UPDATE `sys_moudle` SET name = '运营管理' where id = 109;
DELETE FROM `sys_extend_field` WHERE extend_id IN (SELECT extend_id FROM `sys_extend` WHERE item_type = 'model');
DELETE FROM `sys_extend` WHERE item_type = 'model';
DROP TABLE cms_model;
-- 20170305 --
ALTER TABLE `cms_category` ADD COLUMN `contain_child` tinyint(1) NOT NULL DEFAULT 1 COMMENT '包含子分类内容' AFTER `content_path`;
ALTER TABLE `sys_moudle` ADD COLUMN `menu` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否菜单' AFTER `parent_id`;