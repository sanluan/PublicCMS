SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for cms_category
-- ----------------------------
DROP TABLE IF EXISTS `cms_category`;
CREATE TABLE `cms_category` (
  `id` int(11) NOT NULL auto_increment,
  `site_id` smallint(6) NOT NULL COMMENT '站点ID',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `parent_id` int(11) default NULL COMMENT '父分类ID',
  `type_id` int(11) default NULL COMMENT '分类类型',
  `child_ids` text COMMENT '所有子分类ID',
  `tag_type_ids` text default NULL COMMENT '标签分类',
  `code` varchar(50) default NULL COMMENT '编码',
  `template_path` varchar(255) default NULL COMMENT '模板路径',
  `path` varchar(2000) NOT NULL COMMENT '首页路径',
  `only_url` tinyint(1) NOT NULL COMMENT '外链',
  `has_static` tinyint(1) NOT NULL COMMENT '已经静态化',
  `url` varchar(2048) default NULL COMMENT '首页地址',
  `content_path` varchar(500) default NULL COMMENT '内容路径',
  `contain_child` tinyint(1) NOT NULL DEFAULT 1 COMMENT '包含子分类内容',
  `page_size` int(11) default NULL COMMENT '每页数据条数',
  `allow_contribute` tinyint(1) NOT NULL COMMENT '允许投稿',
  `sort` int(11) NOT NULL default '0' COMMENT '顺序',
  `hidden` tinyint(1) NOT NULL COMMENT '隐藏',
  `disabled` tinyint(1) NOT NULL COMMENT '是否删除',
  `extend_id` int(11) default NULL COMMENT '扩展ID',
  PRIMARY KEY  (`id`),
  KEY `parent_id` (`parent_id`),
  KEY `disabled` (`disabled`),
  KEY `sort` (`sort`),
  KEY `site_id` (`site_id`),
  KEY `type_id` (`type_id`),
  KEY `allow_contribute` (`allow_contribute`),
  KEY `hidden` (`hidden`)
) DEFAULT CHARSET=utf8 COMMENT='分类';

-- ----------------------------
-- Table structure for cms_category_attribute
-- ----------------------------
DROP TABLE IF EXISTS `cms_category_attribute`;
CREATE TABLE `cms_category_attribute` (
  `category_id` int(11) NOT NULL COMMENT '分类ID',
  `title` varchar(80) default NULL COMMENT '标题',
  `keywords` varchar(100) default NULL COMMENT '关键词',
  `description` varchar(300) default NULL COMMENT '描述',
  `data` longtext COMMENT '数据JSON',
  PRIMARY KEY  (`category_id`)
) DEFAULT CHARSET=utf8 COMMENT='分类扩展';

-- ----------------------------
-- Table structure for cms_category_model
-- ----------------------------
DROP TABLE IF EXISTS `cms_category_model`;
CREATE TABLE `cms_category_model` (
  `category_id` int(11) NOT NULL COMMENT '分类ID',
  `model_id` varchar(20) NOT NULL COMMENT '模型编码',
  `template_path` varchar(200) default NULL COMMENT '内容模板路径',
  PRIMARY KEY  (`category_id`,`model_id`)
) DEFAULT CHARSET=utf8 COMMENT='分类模型';

-- ----------------------------
-- Table structure for cms_category_type
-- ----------------------------
DROP TABLE IF EXISTS `cms_category_type`;
CREATE TABLE `cms_category_type` (
  `id` int(11) NOT NULL auto_increment,
  `site_id` smallint(6) NOT NULL COMMENT '站点ID',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `sort` int(11) NOT NULL COMMENT '排序',
  `extend_id` int(11) default NULL COMMENT '扩展ID',
  PRIMARY KEY  (`id`),
  KEY `site_id` (`site_id`)
) DEFAULT CHARSET=utf8 COMMENT='分类类型';

-- ----------------------------
-- Table structure for cms_content
-- ----------------------------
DROP TABLE IF EXISTS `cms_content`;
CREATE TABLE `cms_content` (
  `id` bigint(20) NOT NULL auto_increment,
  `site_id` smallint(6) NOT NULL COMMENT '站点ID',
  `title` varchar(255) NOT NULL COMMENT '标题',
  `user_id` bigint(20) NOT NULL COMMENT '发表用户',
  `check_user_id` bigint(20) default NULL COMMENT '审核用户',
  `category_id` int(11) NOT NULL COMMENT '分类',
  `model_id` varchar(20) NOT NULL COMMENT '模型',
  `parent_id` bigint(20) default NULL COMMENT '父内容ID',
  `copied` tinyint(1) NOT NULL COMMENT '是否转载',
  `author` varchar(50) default NULL COMMENT '作者',
  `editor` varchar(50) default NULL COMMENT '编辑',
  `only_url` tinyint(1) NOT NULL COMMENT '外链',
  `has_images` tinyint(1) NOT NULL COMMENT '拥有图片列表',
  `has_files` tinyint(1) NOT NULL COMMENT '拥有附件列表',
  `has_static` tinyint(1) NOT NULL COMMENT '已经静态化',
  `url` varchar(2048) default NULL COMMENT '地址',
  `description` varchar(300) default NULL COMMENT '简介',
  `tag_ids` text default NULL COMMENT '标签',
  `cover` varchar(255) default NULL COMMENT '封面',
  `childs` int(11) NOT NULL COMMENT '子内容数',
  `scores` int(11) NOT NULL COMMENT '分数',
  `comments` int(11) NOT NULL COMMENT '评论数',
  `clicks` int(11) NOT NULL COMMENT '点击数',
  `publish_date` datetime NOT NULL COMMENT '发布日期',
  `check_date` datetime default NULL COMMENT '审核日期',
  `update_date` datetime default NULL COMMENT '更新日期',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `sort` int(11) NOT NULL default '0' COMMENT '顺序',
  `status` int(11) NOT NULL COMMENT '状态：0、草稿 1、已发布 2、待审核',
  `disabled` tinyint(1) NOT NULL COMMENT '是否删除',
  PRIMARY KEY  (`id`),
  KEY `check_date` (`check_date`,`update_date`),
  KEY `scores` (`scores`,`comments`,`clicks`),
  KEY `status` (`site_id`,`status`,`category_id`,`disabled`,`model_id`,`parent_id`,`sort`,`publish_date`),
  KEY `only_url` (`only_url`,`has_images`,`has_files`,`user_id`)
) DEFAULT CHARSET=utf8 COMMENT='内容';

-- ----------------------------
-- Table structure for cms_content_attribute
-- ----------------------------
DROP TABLE IF EXISTS `cms_content_attribute`;
CREATE TABLE `cms_content_attribute` (
  `content_id` bigint(20) NOT NULL,
  `source` varchar(50) default NULL COMMENT '内容来源',
  `source_url` varchar(2048) default NULL COMMENT '来源地址',
  `data` longtext COMMENT '数据JSON',
  `text` longtext COMMENT '内容',
  `word_count` int(11) NOT NULL COMMENT '字数',
  PRIMARY KEY  (`content_id`)
) DEFAULT CHARSET=utf8 COMMENT='内容扩展';

-- ----------------------------
-- Table structure for cms_content_file
-- ----------------------------
DROP TABLE IF EXISTS `cms_content_file`;
CREATE TABLE `cms_content_file` (
  `id` bigint(20) NOT NULL auto_increment,
  `content_id` bigint(20) NOT NULL COMMENT '内容',
  `user_id` bigint(20) NOT NULL COMMENT '用户',
  `file_path` varchar(255) NOT NULL COMMENT '文件路径',
  `image` tinyint(1) NOT NULL COMMENT '是否图片',
  `size` int(11) NOT NULL COMMENT '大小',
  `clicks` int(11) NOT NULL COMMENT '点击数',
  `sort` int(11) NOT NULL COMMENT '排序',
  `description` varchar(300) default NULL COMMENT '描述',
  PRIMARY KEY  (`id`),
  KEY `content_id` (`content_id`),
  KEY `sort` (`sort`),
  KEY `image` (`image`),
  KEY `size` (`size`),
  KEY `clicks` (`clicks`),
  KEY `user_id` (`user_id`)
) DEFAULT CHARSET=utf8 COMMENT='内容附件';

-- ----------------------------
-- Table structure for cms_content_related
-- ----------------------------
DROP TABLE IF EXISTS `cms_content_related`;
CREATE TABLE `cms_content_related` (
  `id` bigint(20) NOT NULL auto_increment,
  `content_id` bigint(20) NOT NULL COMMENT '内容',
  `related_content_id` bigint(20) default NULL COMMENT '推荐内容',
  `user_id` bigint(20) NOT NULL COMMENT '推荐用户',
  `url` varchar(2048) default NULL COMMENT '推荐链接地址',
  `title` varchar(255) default NULL COMMENT '推荐标题',
  `description` varchar(300) default NULL COMMENT '推荐简介',
  `clicks` int(11) NOT NULL COMMENT '点击数',
  `sort` int(11) NOT NULL COMMENT '排序',
  PRIMARY KEY  (`id`),
  KEY `user_id` (`content_id`,`related_content_id`,`user_id`,`clicks`,`sort`)
) DEFAULT CHARSET=utf8 COMMENT='推荐推荐';

-- ----------------------------
-- Table structure for cms_dictionary
-- ----------------------------
DROP TABLE IF EXISTS `cms_dictionary`;
CREATE TABLE `cms_dictionary` (
  `id` bigint(20) NOT NULL auto_increment,
  `name` varchar(100) NOT NULL COMMENT '名称',
  `multiple` tinyint(1) NOT NULL COMMENT '允许多选',
  PRIMARY KEY  (`id`),
  KEY `multiple` (`multiple`)
) DEFAULT CHARSET=utf8 COMMENT='字典';

-- ----------------------------
-- Table structure for cms_dictionary_data
-- ----------------------------
DROP TABLE IF EXISTS `cms_dictionary_data`;
CREATE TABLE `cms_dictionary_data` (
  `dictionary_id` bigint(20) NOT NULL COMMENT '字典',
  `value` varchar(50) NOT NULL COMMENT '值',
  `text` varchar(100) NOT NULL COMMENT '文字',
  PRIMARY KEY  (`dictionary_id`,`value`)
) DEFAULT CHARSET=utf8 COMMENT='字典数据';

-- ----------------------------
-- Table structure for cms_lottery
-- ----------------------------
DROP TABLE IF EXISTS `cms_lottery`;
CREATE TABLE `cms_lottery` (
  `id` int(11) NOT NULL auto_increment,
  `site_id` smallint(6) NOT NULL COMMENT '站点ID',
  `start_date` datetime NOT NULL COMMENT '开始日期',
  `end_date` datetime NOT NULL COMMENT '结束日期',
  `total_gift` int(11) NOT NULL COMMENT '奖品总数',
  `last_gift` int(11) NOT NULL COMMENT '剩余数量',
  `lottery_count` int(11) NOT NULL COMMENT '可抽奖次数',
  `fractions` int(11) NOT NULL COMMENT '概率分子',
  `numerator` int(11) NOT NULL COMMENT '概率分母',
  `url` varchar(2048) default NULL COMMENT '地址',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `description` varchar(300) default NULL COMMENT '描述',
  `disabled` tinyint(1) NOT NULL COMMENT '是否禁用',
  PRIMARY KEY  (`id`),
  KEY `start_date` (`site_id`,`start_date`,`end_date`,`disabled`)
) DEFAULT CHARSET=utf8 COMMENT='抽奖';

-- ----------------------------
-- Table structure for cms_lottery_user
-- ----------------------------
DROP TABLE IF EXISTS `cms_lottery_user`;
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
) DEFAULT CHARSET=utf8 COMMENT='抽奖用户';

-- ----------------------------
-- Table structure for cms_place
-- ----------------------------
DROP TABLE IF EXISTS `cms_place`;
CREATE TABLE `cms_place` (
  `id` bigint(20) NOT NULL auto_increment,
  `site_id` smallint(6) COMMENT '站点ID',
  `path` varchar(255) NOT NULL COMMENT '模板路径',
  `user_id` bigint(20) default NULL COMMENT '提交用户',
  `item_type` varchar(50) default NULL COMMENT '推荐项目类型',
  `item_id` bigint(20) default NULL COMMENT '推荐项目ID',
  `title` varchar(255) NOT NULL COMMENT '标题',
  `url` varchar(2048) default NULL COMMENT '超链接',
  `cover` varchar(255) default NULL COMMENT '封面图',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `publish_date` datetime NOT NULL COMMENT '发布日期',
  `status` int(11) NOT NULL COMMENT '状态：0、前台提交 1、已发布 ',
  `clicks` int(11) NOT NULL COMMENT '点击数',
  `disabled` tinyint(1) NOT NULL COMMENT '已禁用',
  PRIMARY KEY  (`id`),
  KEY `path` (`path`),
  KEY `disabled` (`disabled`),
  KEY `publish_date` (`publish_date`),
  KEY `create_date` (`create_date`),
  KEY `site_id` (`site_id`),
  KEY `status` (`status`),
  KEY `item_id` (`item_id`),
  KEY `item_type` (`item_type`),
  KEY `user_id` (`user_id`),
  KEY `clicks` (`clicks`)
) DEFAULT CHARSET=utf8 COMMENT='页面数据';

-- ----------------------------
-- Table structure for cms_place_attribute
-- ----------------------------
DROP TABLE IF EXISTS `cms_place_attribute`;
CREATE TABLE `cms_place_attribute` (
  `place_id` bigint(20) NOT NULL COMMENT '位置ID',
  `data` longtext COMMENT '数据JSON',
  PRIMARY KEY  (`place_id`)
) DEFAULT CHARSET=utf8 COMMENT='推荐位数据扩展';

-- ----------------------------
-- Table structure for cms_tag
-- ----------------------------
DROP TABLE IF EXISTS `cms_tag`;
CREATE TABLE `cms_tag` (
  `id` bigint(20) NOT NULL auto_increment,
  `site_id` smallint(6) NOT NULL COMMENT '站点ID',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `type_id` int(11) default NULL COMMENT '分类ID',
  `search_count` int(11) NOT NULL COMMENT '搜索次数',
  PRIMARY KEY  (`id`),
  KEY `site_id` (`site_id`)
) DEFAULT CHARSET=utf8 COMMENT='标签';

-- ----------------------------
-- Table structure for cms_tag_type
-- ----------------------------
DROP TABLE IF EXISTS `cms_tag_type`;
CREATE TABLE `cms_tag_type` (
  `id` int(11) NOT NULL auto_increment,
  `site_id` smallint(6) NOT NULL COMMENT '站点ID',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `count` int(11) NOT NULL COMMENT '标签数',
  PRIMARY KEY  (`id`),
  KEY `site_id` (`site_id`)
) DEFAULT CHARSET=utf8 COMMENT='标签类型';

-- ----------------------------
-- Table structure for cms_vote
-- ----------------------------
DROP TABLE IF EXISTS `cms_vote`;
CREATE TABLE `cms_vote` (
  `id` int(11) NOT NULL auto_increment,
  `site_id` smallint(6) NOT NULL COMMENT '站点ID',
  `end_date` datetime NOT NULL COMMENT '结束日期',
  `max_vote` int(11) NOT NULL COMMENT '最大投票数',
  `user_counts` int(11) NOT NULL COMMENT '参与用户数',
  `url` varchar(2048) NOT NULL COMMENT '地址',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `description` varchar(300) default NULL COMMENT '描述',
  `disabled` tinyint(1) NOT NULL COMMENT '已禁用',
  PRIMARY KEY  (`id`),
  KEY `disabled` (`site_id`,`end_date`,`disabled`)
) DEFAULT CHARSET=utf8 COMMENT='投票';

-- ----------------------------
-- Table structure for cms_vote_item
-- ----------------------------
DROP TABLE IF EXISTS `cms_vote_item`;
CREATE TABLE `cms_vote_item` (
  `id` bigint(20) NOT NULL auto_increment,
  `vote_id` int(11) NOT NULL COMMENT '投票',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `description` varchar(300) default NULL COMMENT '描述',
  `scores` int(11) NOT NULL COMMENT '票数',
  `sort` int(11) NOT NULL COMMENT '顺序',
  PRIMARY KEY  (`id`),
  KEY `vote_id` (`vote_id`,`scores`,`sort`)
) DEFAULT CHARSET=utf8 COMMENT='投票选项';

-- ----------------------------
-- Table structure for cms_vote_user
-- ----------------------------
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
) DEFAULT CHARSET=utf8 COMMENT='投票用户';

-- ----------------------------
-- Table structure for cms_word
-- ----------------------------
DROP TABLE IF EXISTS `cms_word`;
CREATE TABLE `cms_word` (
  `id` bigint(20) NOT NULL auto_increment,
  `name` varchar(255) NOT NULL COMMENT '名称',
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `search_count` int(11) NOT NULL COMMENT '搜索次数',
  `hidden` tinyint(1) NOT NULL COMMENT '隐藏',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `name` (`name`,`site_id`),
  KEY `hidden` (`hidden`),
  KEY `create_date` (`create_date`),
  KEY `search_count` (`search_count`)
) DEFAULT CHARSET=utf8 COMMENT='搜索词';

-- ----------------------------
-- Table structure for log_login
-- ----------------------------
DROP TABLE IF EXISTS `log_login`;
CREATE TABLE `log_login` (
  `id` bigint(20) NOT NULL auto_increment,
  `site_id` smallint(6) NOT NULL COMMENT '站点ID',
  `name` varchar(50) NOT NULL COMMENT '用户名',
  `user_id` bigint(20) default NULL COMMENT '用户ID',
  `ip` varchar(64) NOT NULL COMMENT 'IP',
  `channel` varchar(50) NOT NULL COMMENT '登录渠道',
  `result` tinyint(1) NOT NULL COMMENT '结果',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `error_password` varchar(100) default NULL COMMENT '错误密码',
  PRIMARY KEY  (`id`),
  KEY `result` (`result`),
  KEY `user_id` (`user_id`),
  KEY `create_date` (`create_date`),
  KEY `ip` (`ip`),
  KEY `site_id` (`site_id`),
  KEY `channel` (`channel`)
) DEFAULT CHARSET=utf8 COMMENT='登录日志';

-- ----------------------------
-- Table structure for log_operate
-- ----------------------------
DROP TABLE IF EXISTS `log_operate`;
CREATE TABLE `log_operate` (
  `id` bigint(20) NOT NULL auto_increment,
  `site_id` smallint(6) NOT NULL COMMENT '站点ID',
  `user_id` bigint(20) default NULL COMMENT '用户ID',
  `channel` varchar(50) NOT NULL COMMENT '操作渠道',
  `operate` varchar(40) NOT NULL COMMENT '操作',
  `ip` varchar(64) default NULL COMMENT 'IP',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `content` text NOT NULL COMMENT '内容',
  PRIMARY KEY  (`id`),
  KEY `user_id` (`user_id`),
  KEY `operate` (`operate`),
  KEY `create_date` (`create_date`),
  KEY `ip` (`ip`),
  KEY `site_id` (`site_id`),
  KEY `channel` (`channel`)
) DEFAULT CHARSET=utf8 COMMENT='操作日志';

-- ----------------------------
-- Table structure for log_task
-- ----------------------------
DROP TABLE IF EXISTS `log_task`;
CREATE TABLE `log_task` (
  `id` bigint(20) NOT NULL auto_increment,
  `site_id` smallint(6) NOT NULL COMMENT '站点ID',
  `task_id` int(11) NOT NULL COMMENT '任务',
  `begintime` datetime NOT NULL COMMENT '开始时间',
  `endtime` datetime default NULL COMMENT '结束时间',
  `success` tinyint(1) NOT NULL COMMENT '执行成功',
  `result` longtext COMMENT '执行结果',
  PRIMARY KEY  (`id`),
  KEY `task_id` (`task_id`),
  KEY `success` (`success`),
  KEY `site_id` (`site_id`),
  KEY `begintime` (`begintime`)
) DEFAULT CHARSET=utf8 COMMENT='任务计划日志';

-- ----------------------------
-- Table structure for log_upload
-- ----------------------------
DROP TABLE IF EXISTS `log_upload`;
CREATE TABLE `log_upload` (
  `id` bigint(20) NOT NULL auto_increment,
  `site_id` smallint(6) NOT NULL COMMENT '站点ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `channel` varchar(50) NOT NULL COMMENT '操作渠道',
  `image` tinyint(1) NOT NULL COMMENT '图片',
  `file_size` bigint(20) NOT NULL COMMENT '文件大小',
  `ip` varchar(64) default NULL COMMENT 'IP',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `file_path` varchar(500) NOT NULL COMMENT '文件路径',
  PRIMARY KEY  (`id`),
  KEY `user_id` (`user_id`),
  KEY `create_date` (`create_date`),
  KEY `ip` (`ip`),
  KEY `site_id` (`site_id`),
  KEY `channel` (`channel`),
  KEY `image` (`image`),
  KEY `file_size` (`file_size`)
) DEFAULT CHARSET=utf8 COMMENT='上传日志';

-- ----------------------------
-- Table structure for sys_app
-- ----------------------------
DROP TABLE IF EXISTS `sys_app`;
CREATE TABLE `sys_app` (
  `id` int(11) NOT NULL auto_increment,
  `site_id` smallint(6) NOT NULL COMMENT '站点ID',
  `channel` varchar(50) NOT NULL COMMENT '渠道',
  `app_key` varchar(50) NOT NULL COMMENT 'APP key',
  `app_secret` varchar(50) NOT NULL COMMENT 'APP secret',
  `authorized_apis`  text NULL COMMENT '授权API',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `key` (`app_key`),
  KEY `site_id` (`site_id`)
) DEFAULT CHARSET=utf8 COMMENT='应用';

-- ----------------------------
-- Table structure for sys_app_client
-- ----------------------------
DROP TABLE IF EXISTS `sys_app_client`;
CREATE TABLE `sys_app_client` (
  `site_id` smallint(6) NOT NULL COMMENT '站点ID',
  `channel` varchar(20) NOT NULL COMMENT '渠道',
  `uuid` varchar(50) NOT NULL COMMENT '唯一标识',
  `user_id` bigint(20) default NULL COMMENT '绑定用户',
  `client_version` varchar(50) default NULL COMMENT '版本',
  `last_login_date` datetime default NULL COMMENT '上次登录时间',
  `last_login_ip` varchar(64) default NULL COMMENT '上次登录IP',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `disabled` tinyint(1) NOT NULL COMMENT '是否禁用',
  PRIMARY KEY  (`site_id`,`channel`,`uuid`),
  KEY `user_id` (`user_id`,`disabled`,`create_date`)
) DEFAULT CHARSET=utf8 COMMENT='应用客户端';

-- ----------------------------
-- Table structure for sys_app_token
-- ----------------------------
DROP TABLE IF EXISTS `sys_app_token`;
CREATE TABLE `sys_app_token` (
  `auth_token` varchar(40) NOT NULL COMMENT '授权验证',
  `app_id` int(11) NOT NULL COMMENT '应用ID',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY  (`auth_token`),
  KEY `app_id` (`app_id`),
  KEY `create_date` (`create_date`)
) DEFAULT CHARSET=utf8 COMMENT='应用授权';

-- ----------------------------
-- Table structure for sys_cluster
-- ----------------------------
DROP TABLE IF EXISTS `sys_cluster`;
CREATE TABLE `sys_cluster` (
  `uuid` varchar(40) NOT NULL COMMENT 'uuid',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `heartbeat_date` datetime NOT NULL COMMENT '心跳时间',
  `master` tinyint(1) NOT NULL COMMENT '是否管理',
  `cms_version` varchar(20) default NULL,
  PRIMARY KEY  (`uuid`),
  KEY `create_date` (`create_date`)
) DEFAULT CHARSET=utf8 COMMENT='服务器集群';

-- ----------------------------
-- Table structure for sys_config_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_config_data`;
CREATE TABLE `sys_config_data` (
  `site_id` smallint(6) NOT NULL COMMENT '站点ID',
  `code` varchar(50) NOT NULL COMMENT '配置项编码',
  `data` longtext NOT NULL COMMENT '值',
  PRIMARY KEY  (`site_id`,`code`)
) DEFAULT CHARSET=utf8 COMMENT='站点配置';

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `id` int(11) NOT NULL auto_increment,
  `site_id` smallint(6) NOT NULL COMMENT '站点ID',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `parent_id` int(11) default NULL COMMENT '父部门ID',
  `description` varchar(300) default NULL COMMENT '描述',
  `user_id` bigint(20) default NULL COMMENT '负责人',
  `max_sort` INT NOT NULL DEFAULT 1000 COMMENT  '最大内容置顶级别',
  `owns_all_category` tinyint(1) NOT NULL COMMENT '拥有全部分类权限',
  `owns_all_page` tinyint(1) NOT NULL COMMENT '拥有全部页面权限',
  PRIMARY KEY  (`id`),
  KEY `site_id` (`site_id`)
) AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='部门';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES ('1', '1', '技术部', null, '', '1', '1000', '1', '1');
INSERT INTO `sys_dept` VALUES ('2', '2', '技术部', null, '', '3', '1000', '1', '1');

-- ----------------------------
-- Table structure for sys_dept_category
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept_category`;
CREATE TABLE `sys_dept_category` (
  `dept_id` int(11) NOT NULL COMMENT '部门ID',
  `category_id` int(11) NOT NULL COMMENT '分类ID',
  PRIMARY KEY  (`dept_id`,`category_id`)
) DEFAULT CHARSET=utf8 COMMENT='部门分类';

-- ----------------------------
-- Table structure for sys_dept_page
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept_page`;
CREATE TABLE `sys_dept_page` (
  `dept_id` int(11) NOT NULL COMMENT '部门ID',
  `page` varchar(255) NOT NULL COMMENT '页面',
  PRIMARY KEY  (`dept_id`,`page`)
) DEFAULT CHARSET=utf8 COMMENT='部门页面';

-- ----------------------------
-- Table structure for sys_domain
-- ----------------------------
DROP TABLE IF EXISTS `sys_domain`;
CREATE TABLE `sys_domain` (
  `name` varchar(255) NOT NULL COMMENT '域名',
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `wild` tinyint(1) NOT NULL COMMENT '通配域名',
  `path` varchar(255) default NULL COMMENT '路径',
  PRIMARY KEY  (`name`),
  KEY `site_id` (`site_id`)
) DEFAULT CHARSET=utf8 COMMENT='域名';

-- ----------------------------
-- Records of sys_domain
-- ----------------------------
INSERT INTO `sys_domain` VALUES ('dev.publiccms.com', '1', '0', '');
INSERT INTO `sys_domain` VALUES ('member.dev.publiccms.com', '1', '0', '/member/');
INSERT INTO `sys_domain` VALUES ('search.dev.publiccms.com', '1', '0', '/search/');
INSERT INTO `sys_domain` VALUES ('site2.dev.publiccms.com', '2', '0', '');

-- ----------------------------
-- Table structure for sys_email_token
-- ----------------------------
DROP TABLE IF EXISTS `sys_email_token`;
CREATE TABLE `sys_email_token` (
  `auth_token` varchar(40) NOT NULL COMMENT '验证码',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `email` varchar(100) NOT NULL COMMENT '邮件地址',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY  (`auth_token`),
  KEY `create_date` (`create_date`),
  KEY `user_id` (`user_id`)
) DEFAULT CHARSET=utf8 COMMENT='邮件地址验证日志';

-- ----------------------------
-- Table structure for sys_extend
-- ----------------------------
DROP TABLE IF EXISTS `sys_extend`;
CREATE TABLE `sys_extend` (
  `id` int(11) NOT NULL auto_increment,
  `item_type` varchar(20) NOT NULL COMMENT '扩展类型',
  `item_id` int(11) NOT NULL COMMENT '扩展项目ID',
  PRIMARY KEY  (`id`)
) DEFAULT CHARSET=utf8 COMMENT='扩展';

-- ----------------------------
-- Table structure for sys_extend_field
-- ----------------------------
DROP TABLE IF EXISTS `sys_extend_field`;
CREATE TABLE `sys_extend_field` (
  `extend_id` int(11) NOT NULL COMMENT '扩展ID',
  `code` varchar(20) NOT NULL COMMENT '编码',
  `required` tinyint(1) NOT NULL COMMENT '是否必填',
  `maxlength` int(11) default NULL,
  `name` varchar(20) NOT NULL COMMENT '名称',
  `description` varchar(100) default NULL COMMENT '解释',
  `input_type` varchar(20) NOT NULL COMMENT '表单类型',
  `default_value` varchar(50) default NULL COMMENT '默认值',
  `dictionary_id` varchar(20) default NULL,
  `sort` int(11) NOT NULL default '0' COMMENT '顺序',
  PRIMARY KEY  (`extend_id`,`code`),
  KEY `sort` (`sort`)
) DEFAULT CHARSET=utf8 COMMENT='扩展字段';

-- ----------------------------
-- Table structure for sys_module
-- ----------------------------
DROP TABLE IF EXISTS `sys_module`;
CREATE TABLE `sys_module` (
  `id` varchar(30) NOT NULL auto_increment,
  `name` varchar(100) NOT NULL COMMENT '名称',
  `url` varchar(255) default NULL COMMENT '链接地址',
  `authorized_url` text COMMENT '授权地址',
  `attached` varchar(300) default NULL COMMENT '标题附加',
  `parent_id` varchar(30) default NULL COMMENT '父模块',
  `menu` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否菜单',
  `sort` int(11) NOT NULL COMMENT '排序',
  PRIMARY KEY  (`id`),
  KEY `url` (`url`),
  KEY `parent_id` (`parent_id`),
  KEY `sort` (`sort`)
) AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='模块';

-- ----------------------------
-- Records of sys_module
-- ----------------------------
INSERT INTO `sys_module` VALUES ('myself', '个人', null, null, '<i class=\"icon-user icon-large\"></i>', null, '1', '0');
INSERT INTO `sys_module` VALUES ('content', '内容', null, null, '<i class=\"icon-book icon-large\"></i>', null, '1', '0');
INSERT INTO `sys_module` VALUES ('category', '分类', null, null, '<i class=\"icon-folder-open icon-large\"></i>', null, '1', '1');
INSERT INTO `sys_module` VALUES ('page', '页面', null, null, '<i class=\"icon-globe icon-large\"></i>', null, '1', '0');
INSERT INTO `sys_module` VALUES ('maintain', '维护', null, null, '<i class=\"icon-cogs icon-large\"></i>', null, '1', '1');
INSERT INTO `sys_module` VALUES ('myself_menu', '与我相关', null, null, '<i class=\"icon-user icon-large\"></i>', '1', '1', '0');
INSERT INTO `sys_module` VALUES ('myself_password', '修改密码', 'myself/password', 'changePassword', '<i class=\"icon-key icon-large\"></i>', '6', '1', '0');
INSERT INTO `sys_module` VALUES ('myself_content', '我的内容', 'myself/contentList', null, '<i class=\"icon-book icon-large\"></i>', '6', '1', '0');
INSERT INTO `sys_module` VALUES ('myself_operate', '我的操作日志', 'myself/logOperate', null, '<i class=\"icon-list-alt icon-large\"></i>', '6', '1', '0');
INSERT INTO `sys_module` VALUES ('myself_login', '我的登录日志', 'myself/logLogin', null, '<i class=\"icon-signin icon-large\"></i>', '6', '1', '0');
INSERT INTO `sys_module` VALUES ('myself_token', '我的登录授权', 'myself/userTokenList', null, '<i class=\"icon-unlock-alt icon-large\"></i>', '6', '1', '0');
INSERT INTO `sys_module` VALUES ('content_list', '内容管理', 'cmsContent/list', 'sysUser/lookup', '<i class=\"icon-book icon-large\"></i>', '2', '1', '0');
INSERT INTO `sys_module` VALUES ('content_extend', '内容扩展', null, null, '<i class=\"icon-road icon-large\"></i>', '2', '1', '0');
INSERT INTO `sys_module` VALUES ('tag_list', '标签管理', 'cmsTag/list', 'cmsTagType/lookup', '<i class=\"icon-tag icon-large\"></i>', '13', '1', '0');
INSERT INTO `sys_module` VALUES ('tag_add', '增加/修改', 'cmsTag/add', 'cmsTagType/lookup,cmsTag/save', null, '14', '1', '0');
INSERT INTO `sys_module` VALUES ('tag_delete', '删除', null, 'cmsTag/delete', null, '14', '1', '0');
INSERT INTO `sys_module` VALUES ('content_add', '增加/修改', 'cmsContent/add', 'cmsContent/addMore,cmsContent/lookup,cmsContent/lookup_list,cmsContent/save,ueditor', null, '12', '1', '0');
INSERT INTO `sys_module` VALUES ('content_delete', '删除', null, 'cmsContent/delete', null, '12', '1', '0');
INSERT INTO `sys_module` VALUES ('content_check', '审核', null, 'cmsContent/check', null, '12', '1', '0');
INSERT INTO `sys_module` VALUES ('content_refresh', '刷新', null, 'cmsContent/refresh', null, '12', '1', '0');
INSERT INTO `sys_module` VALUES ('content_publish', '生成', null, 'cmsContent/publish', null, '12', '1', '0');
INSERT INTO `sys_module` VALUES ('content_move', '移动', 'cmsContent/moveParameters', 'cmsContent/move', null, '12', '1', '0');
INSERT INTO `sys_module` VALUES ('content_push', '推荐', 'cmsContent/push', 'cmsContent/push_content,cmsContent/push_content_list,cmsContent/push_to_content,cmsContent/push_page,cmsContent/push_page_list,cmsPlace/add,cmsPlace/save,cmsContent/related,cmsContent/unrelated,cmsPlace/delete', null, '12', '1', '0');
INSERT INTO `sys_module` VALUES ('category_list', '分类管理', 'cmsCategory/list', null, '<i class=\"icon-folder-open icon-large\"></i>', '3', '1', '0');
INSERT INTO `sys_module` VALUES ('category_add', '增加/修改', 'cmsCategory/add', 'cmsCategory/addMore,cmsTemplate/lookup,cmsCategory/categoryPath,cmsCategory/contentPath,file/doUpload,cmsCategory/save', null, '24', '1', '0');
INSERT INTO `sys_module` VALUES ('category_delete', '删除', null, 'cmsCategory/delete', null, '24', '1', '0');
INSERT INTO `sys_module` VALUES ('category_publish', '生成', 'cmsCategory/publishParameters', 'cmsCategory/publish', null, '24', '1', '0');
INSERT INTO `sys_module` VALUES ('category_move', '移动', 'cmsCategory/moveParameters', 'cmsCategory/move,cmsCategory/lookup', null, '24', '1', '0');
INSERT INTO `sys_module` VALUES ('category_push', '推荐', 'cmsCategory/push_page', 'cmsPlace/push,cmsPlace/add,cmsPlace/save', null, '24', '1', '0');
INSERT INTO `sys_module` VALUES ('page_menu', '页面管理', null, null, '<i class=\"icon-globe icon-large\"></i>', '4', '1', '0');
INSERT INTO `sys_module` VALUES ('category_extend', '分类扩展', null, null, '<i class=\"icon-road icon-large\"></i>', '3', '1', '0');
INSERT INTO `sys_module` VALUES ('category_type', '分类类型', 'cmsCategoryType/list', null, '<i class=\"icon-road icon-large\"></i>', '31', '1', '0');
INSERT INTO `sys_module` VALUES ('tag_type_list', '标签分类', 'cmsTagType/list', null, '<i class=\"icon-tags icon-large\"></i>', '31', '1', '0');
INSERT INTO `sys_module` VALUES ('tag_type_add', '增加/修改', 'cmsTagType/add', 'cmsTagType/save', null, '33', '1', '0');
INSERT INTO `sys_module` VALUES ('tag_type_delete', '删除', null, 'cmsTagType/delete', null, '33', '1', '0');
INSERT INTO `sys_module` VALUES ('category_type_add', '增加/修改', 'cmsCategoryType/add', 'cmsCategoryType/save', null, '32', '1', '0');
INSERT INTO `sys_module` VALUES ('category_type_delete', '删除', null, 'cmsCategoryType/delete', null, '32', '1', '0');
INSERT INTO `sys_module` VALUES ('file_menu', '文件管理', null, null, '<i class=\"icon-folder-close-alt icon-large\"></i>', '126', '1', '0');
INSERT INTO `sys_module` VALUES ('template_list', '模板文件管理', 'cmsTemplate/list', 'cmsTemplate/directory', '<i class=\"icon-code icon-large\"></i>', '38', '1', '0');
INSERT INTO `sys_module` VALUES ('template_metadata', '修改模板元数据', 'cmsTemplate/metadata', 'cmsTemplate/saveMetadata', null, '39', '1', '0');
INSERT INTO `sys_module` VALUES ('template_content', '修改模板', 'cmsTemplate/content', 'cmsTemplate/save,cmsTemplate/chipLookup,cmsWebFile/lookup,placeTemplate/form,cmsWebFile/contentForm,cmsTemplate/demo,cmsTemplate/help,cmsTemplate/upload,cmsTemplate/doUpload', null, '39', '1', '0');
INSERT INTO `sys_module` VALUES ('place_template_list', '页面片段模板', 'placeTemplate/list', null, '<i class=\"icon-list-alt icon-large\"></i>', '38', '1', '0');
INSERT INTO `sys_module` VALUES ('template_delete', '删除模板', null, 'cmsTemplate/delete', null, '39', '1', '0');
INSERT INTO `sys_module` VALUES ('word_list', '搜索词管理', 'cmsWord/list', null, '<i class=\"icon-search icon-large\"></i>', '109', '1', '0');
INSERT INTO `sys_module` VALUES ('operation', '运营', null, null, '<i class=\"icon-user icon-large\"></i>', null, '1', '0');
INSERT INTO `sys_module` VALUES ('system_menu', '系统管理', NULL, NULL, '<i class=\"icon-cog icon-large\"></i>', '45', '1', '0');
INSERT INTO `sys_module` VALUES ('template_publish', '生成页面', null, 'cmsTemplate/publish', null, '112', '1', '0');
INSERT INTO `sys_module` VALUES ('page_save', '保存页面元数据', '', 'cmsPage/save,file/doUpload,cmsPage/clearCache', null, '112', '1', '0');
INSERT INTO `sys_module` VALUES ('place_add', '增加/修改推荐位数据', 'cmsPlace/add', 'cmsContent/lookup,cmsPlace/lookup,cmsPlace/lookup_content_list,file/doUpload,cmsPlace/save', null, '107', '1', '0');
INSERT INTO `sys_module` VALUES ('place_delete', '删除推荐位数据', null, 'cmsPlace/delete', null, '107', '1', '0');
INSERT INTO `sys_module` VALUES ('place_refresh', '刷新推荐位数据', null, 'cmsPlace/refresh', null, '107', '1', '0');
INSERT INTO `sys_module` VALUES ('place_check', '审核推荐位数据', null, 'cmsPlace/check', null, '107', '1', '0');
INSERT INTO `sys_module` VALUES ('place_publish', '发布推荐位', 'cmsPlace/publish_place', 'cmsTemplate/publishPlace', null, '107', '1', '0');
INSERT INTO `sys_module` VALUES ('place_clear', '清空推荐位数据', null, 'cmsPlace/clear', null, '107', '1', '0');
INSERT INTO `sys_module` VALUES ('place_template', '查看推荐位源码', 'cmsTemplate/placeContent', null, null, '39', '1', '0');
INSERT INTO `sys_module` VALUES ('app_list', '应用授权', 'sysApp/list', NULL, '<i class=\"icon-linux icon-large\"></i>', '62', '1', '0');
INSERT INTO `sys_module` VALUES ('app_add', '增加/修改', 'sysApp/add', NULL, '', '56', '1', '0');
INSERT INTO `sys_module` VALUES ('app_save', '保存', NULL, 'sysApp/save', '', '56', '1', '0');
INSERT INTO `sys_module` VALUES ('app_delete', '删除', NULL, 'sysApp/delete', NULL, '56', '1', '0');
INSERT INTO `sys_module` VALUES ('log_upload', '文件上传日志', 'log/upload', 'sysUser/lookup', '<i class=\"icon-list-alt icon-large\"></i>', '63', '1', '0');
INSERT INTO `sys_module` VALUES ('user_menu', '用户管理', null, null, '<i class=\"icon-user icon-large\"></i>', '5', '1', '0');
INSERT INTO `sys_module` VALUES ('maintain_menu', '系统维护', null, null, '<i class=\"icon-cogs icon-large\"></i>', '5', '1', '0');
INSERT INTO `sys_module` VALUES ('log_menu', '日志管理', null, null, '<i class=\"icon-list-alt icon-large\"></i>', '5', '1', '0');
INSERT INTO `sys_module` VALUES ('log_operate', '操作日志', 'log/operate', 'sysUser/lookup', '<i class=\"icon-list-alt icon-large\"></i>', '63', '1', '0');
INSERT INTO `sys_module` VALUES ('log_login', '登录日志', 'log/login', 'sysUser/lookup', '<i class=\"icon-signin icon-large\"></i>', '63', '1', '0');
INSERT INTO `sys_module` VALUES ('log_task', '任务计划日志', 'log/task', 'sysUser/lookup', '<i class=\"icon-time icon-large\"></i>', '63', '1', '0');
INSERT INTO `sys_module` VALUES ('log_operate_delete', '删除', null, 'logOperate/delete', null, '64', '1', '0');
INSERT INTO `sys_module` VALUES ('log_login_delete', '删除', null, 'logLogin/delete', null, '65', '1', '0');
INSERT INTO `sys_module` VALUES ('log_task_delete', '删除', null, 'logTask/delete', null, '66', '1', '0');
INSERT INTO `sys_module` VALUES ('log_task_view', '查看', 'log/taskView', null, null, '66', '1', '0');
INSERT INTO `sys_module` VALUES ('user_list', '用户管理', 'sysUser/list', null, '<i class=\"icon-user icon-large\"></i>', '61', '1', '0');
INSERT INTO `sys_module` VALUES ('dept_list', '部门管理', 'sysDept/list', 'sysDept/lookup,sysUser/lookup', '<i class=\"icon-group icon-large\"></i>', '61', '1', '0');
INSERT INTO `sys_module` VALUES ('role_list', '角色管理', 'sysRole/list', null, '<i class=\"icon-user-md icon-large\"></i>', '61', '1', '0');
INSERT INTO `sys_module` VALUES ('user_add', '增加/修改', 'sysUser/add', 'sysDept/lookup,sysUser/save', null, '71', '1', '0');
INSERT INTO `sys_module` VALUES ('user_enable', '启用', null, 'sysUser/enable', null, '71', '1', '0');
INSERT INTO `sys_module` VALUES ('user_disable', '禁用', null, 'sysUser/disable', null, '71', '1', '0');
INSERT INTO `sys_module` VALUES ('dept_add', '增加/修改', 'sysDept/add', 'sysDept/lookup,sysUser/lookup,sysDept/save', null, '72', '1', '0');
INSERT INTO `sys_module` VALUES ('dept_delete', '删除', null, 'sysDept/delete', null, '72', '1', '0');
INSERT INTO `sys_module` VALUES ('role_add', '增加/修改', 'sysRole/add', 'sysRole/save', null, '73', '1', '0');
INSERT INTO `sys_module` VALUES ('role_delete', '删除', null, 'sysRole/delete', null, '73', '1', '0');
INSERT INTO `sys_module` VALUES ('model_list', '内容模型管理', 'cmsModel/list', null, '<i class=\"icon-th-large icon-large\"></i>', '38', '1', '0');
INSERT INTO `sys_module` VALUES ('task_list', '任务计划', 'sysTask/list', null, '<i class=\"icon-time icon-large\"></i>', '62', '1', '0');
INSERT INTO `sys_module` VALUES ('report_cms', '系统监控', 'report/cms', NULL, '<i class=\"icon-check-sign icon-large\"></i>', '46', '1', '0');
INSERT INTO `sys_module` VALUES ('domain_list', '动态域名', 'sysDomain/domainList', null, '<i class=\"icon-qrcode icon-large\"></i>', '62', '1', '0');
INSERT INTO `sys_module` VALUES ('task_template_list', '任务计划脚本', 'taskTemplate/list', null, '<i class=\"icon-time icon-large\"></i>', '38', '1', '0');
INSERT INTO `sys_module` VALUES ('task_template_metadata', '修改脚本', 'taskTemplate/metadata', 'cmsTemplate/saveMetadata,taskTemplate/content,cmsTemplate/save,taskTemplate/chipLookup', null, '85', '1', '0');
INSERT INTO `sys_module` VALUES ('task_template_delete', '删除脚本', null, 'cmsTemplate/delete', null, '85', '1', '0');
INSERT INTO `sys_module` VALUES ('app_client_list', '客户端管理', 'sysAppClient/list', NULL, '<i class=\"icon-coffee icon-large\"></i>', '61', '1', '0');
INSERT INTO `sys_module` VALUES ('app_client_enable', '启用', NULL, 'sysAppClient/enable', NULL, '88', '1', '0');
INSERT INTO `sys_module` VALUES ('model_add', '增加/修改', 'cmsModel/add', 'cmsModel/save,cmsTemplate/lookup', null, '81', '1', '0');
INSERT INTO `sys_module` VALUES ('model_delete', '删除', null, 'cmsModel/delete', null, '81', '1', '0');
INSERT INTO `sys_module` VALUES ('task_add', '增加/修改', 'sysTask/add', 'sysTask/save,sysTask/example,taskTemplate/lookup', null, '82', '1', '0');
INSERT INTO `sys_module` VALUES ('task_delete', '删除', null, 'sysTask/delete', null, '82', '1', '0');
INSERT INTO `sys_module` VALUES ('task_runonce', '立刻执行', null, 'sysTask/runOnce', null, '82', '1', '0');
INSERT INTO `sys_module` VALUES ('task_pause', '暂停', null, 'sysTask/pause', null, '82', '1', '0');
INSERT INTO `sys_module` VALUES ('task_resume', '恢复', null, 'sysTask/resume', null, '82', '1', '0');
INSERT INTO `sys_module` VALUES ('task_recreate', '重新初始化', null, 'sysTask/recreate', null, '82', '1', '0');
INSERT INTO `sys_module` VALUES ('app_client_disable', '禁用', NULL, 'sysAppClient/disable', NULL, '88', '1', '0');
INSERT INTO `sys_module` VALUES ('lottery_list', '抽奖管理', 'cmsLottery/list', NULL, '<i class=\"icon-ticket icon-large\"></i>', '109', '1', '0');
INSERT INTO `sys_module` VALUES ('domain_config', '修改', 'sysDomain/config', 'sysDomain/saveConfig,cmsTemplate/directoryLookup,cmsTemplate/lookup', null, '84', '1', '0');
INSERT INTO `sys_module` VALUES ('config_list', '站点配置', 'sysConfigData/list', null, '<i class=\"icon-cog icon-large\"></i>', '46', '1', '0');
INSERT INTO `sys_module` VALUES ('content_add', '修改', 'cmsContent/add', 'cmsContent/addMore,file/doUpload,cmsContent/lookup,cmsContent/lookup_list,cmsContent/save,ueditor,ckeditor/upload', null, '8', '1', '0');
INSERT INTO `sys_module` VALUES ('content_delete', '删除', null, 'cmsContent/delete', null, '8', '1', '0');
INSERT INTO `sys_module` VALUES ('content_refresh', '刷新', null, 'cmsContent/refresh', null, '8', '1', '0');
INSERT INTO `sys_module` VALUES ('105', '生成', null, 'cmsContent/publish', null, '8', '1', '0');
INSERT INTO `sys_module` VALUES ('106', '推荐', 'cmsContent/push', 'cmsContent/push_content,cmsContent/push_content_list,cmsContent/push_to_content,cmsContent/push_page,cmsContent/push_page_list,cmsContent/push_to_place,cmsContent/related', null, '8', '1', '0');
INSERT INTO `sys_module` VALUES ('107', '页面片段管理', 'cmsPlace/list', 'sysUser/lookup,cmsPlace/data_list', '<i class=\"icon-list-alt icon-large\"></i>', '30', '1', '0');
INSERT INTO `sys_module` VALUES ('108', '增加/修改', 'cmsLottery/add', NULL, NULL, '1007', '1', '0');
INSERT INTO `sys_module` VALUES ('109', '运营管理', null, null, '<i class=\"icon-home icon-large\"></i>', '45', '1', '0');
INSERT INTO `sys_module` VALUES ('110', '修改模板元数据', 'placeTemplate/metadata', 'cmsTemplate/savePlaceMetaData', NULL, '42', '1', '0');
INSERT INTO `sys_module` VALUES ('111', '修改模板', 'placeTemplate/content', 'cmsTemplate/help,cmsTemplate/chipLookup,cmsWebFile/lookup,cmsWebFile/contentForm,placeTemplate/form', NULL, '42', '1', '0');
INSERT INTO `sys_module` VALUES ('112', '页面管理', 'cmsPage/list', 'cmsPage/metadata,sysUser/lookup,cmsContent/lookup,cmsContent/lookup_list,cmsCategory/lookup', '<i class=\"icon-globe icon-large\"></i>', '30', '1', '0');
INSERT INTO `sys_module` VALUES ('113', '刷新缓存', NULL, 'clearCache', '', NULL, '0', '1');
INSERT INTO `sys_module` VALUES ('114', '查看', 'cmsContent/view', null, null, '12', '0', '0');
INSERT INTO `sys_module` VALUES ('115', '查看', 'cmsPlace/view', null, null, '107', '0', '0');
INSERT INTO `sys_module` VALUES ('116', '修改类型', 'cmsCategory/changeTypeParameters', 'cmsCategory/changeType', null, '24', '0', '0');
INSERT INTO `sys_module` VALUES ('117', '内容回收站', 'cmsRecycleContent/list', 'sysUser/lookup,cmsContent/recycle,cmsContent/realDelete', '<i class=\"icon-trash icon-large\"></i>', '13', '1', '0');
INSERT INTO `sys_module` VALUES ('118', '删除', NULL, 'cmsContent/realDelete', NULL, '117', '0', '0');
INSERT INTO `sys_module` VALUES ('119', '还原', NULL, 'cmsContent/recycle', NULL, '117', '0', '0');
INSERT INTO `sys_module` VALUES ('120', '置顶', 'cmsContent/sortParameters', 'cmsContent/sort', NULL, '12', '0', '0');
INSERT INTO `sys_module` VALUES ('121', '人员管理', 'sysDept/userList', 'sysDept/addUser,sysDept/saveUser,sysDept/enableUser,sysDept/disableUser', NULL, '72', '0', '0');
INSERT INTO `sys_module` VALUES ('122', '数据字典', 'cmsDictionary/list', null, '<i class=\"icon-book icon-large\"></i>', '62', '1', '0');
INSERT INTO `sys_module` VALUES ('123', '添加', 'cmsDictionary/add', 'cmsDictionary/save', null, '122', '0', '0');
INSERT INTO `sys_module` VALUES ('124', '删除', null, 'cmsDictionary/delete', null, '122', '0', '0');
INSERT INTO `sys_module` VALUES ('125', '撤销审核', null, 'cmsContent/uncheck', null, '12', '0', '0');
INSERT INTO `sys_module` VALUES ('126', '文件', null, null, '<i class=\"icon-folder-close-alt icon-large\"></i>', null, '1', '1');
INSERT INTO `sys_module` VALUES ('127', '推荐位数据', 'cmsPlace/dataList', null, null , '107', '1', '1');
INSERT INTO `sys_module` VALUES ('128', '用户数据监控', 'report/user', NULL, '<i class=\"icon-male icon-large\"></i>', '46', '1', '0');
INSERT INTO `sys_module` VALUES ('131', '网站文件管理', 'cmsWebFile/list', null, '<i class=\"icon-globe icon-large\"></i>', '38', '1', '0');
INSERT INTO `sys_module` VALUES ('132', '新建目录', 'cmsWebFile/directory', 'cmsWebFile/createDirectory', null, '131', '1', '0');
INSERT INTO `sys_module` VALUES ('133', '上传文件', 'cmsWebFile/upload', 'cmsWebFile/doUpload', null, '131', '1', '0');
INSERT INTO `sys_module` VALUES ('134', '压缩', null, 'cmsWebFile/zip', null, '131', '1', '0');
INSERT INTO `sys_module` VALUES ('135', '解压缩', null, 'cmsWebFile/unzip,cmsWebFile/unzipHere', null, '131', '1', '0');
INSERT INTO `sys_module` VALUES ('136', '节点管理', 'sysCluster/list', NULL, '<i class=\"icon-code-fork icon-large\"></i>', '62', '1', '0');
INSERT INTO `sys_module` VALUES ('138', '修改配置', 'sysConfigData/edit', 'sysConfigData/save', null, '101', '1', '0');
INSERT INTO `sys_module` VALUES ('139', '清空配置', null, 'sysConfigData/delete', null, '101', '1', '0');
INSERT INTO `sys_module` VALUES ('140', '站点配置', 'sysConfig/list', null, '<i class=\"icon-cogs icon-large\"></i>', '62', '1', '0');
INSERT INTO `sys_module` VALUES ('141', '修改内容模型', 'cmsContent/changeModelParameters', 'cmsContent/changeModel', null, '12', '0', '0');
INSERT INTO `sys_module` VALUES ('142', '保存配置', null, 'sysConfig/save', null, '140', '1', '0');
INSERT INTO `sys_module` VALUES ('143', '修改配置', 'sysConfig/add', null, null, '140', '1', '0');
INSERT INTO `sys_module` VALUES ('144', '删除配置', null, 'sysConfig/delete', null, '140', '1', '0');
INSERT INTO `sys_module` VALUES ('145', '保存', NULL, 'cmsLottery/save', NULL, '99', '1', '0');
INSERT INTO `sys_module` VALUES ('146', '删除', NULL, 'cmsLottery/delete', NULL, '99', '1', '0');
INSERT INTO `sys_module` VALUES ('147', '抽奖用户管理', 'cmsLotteryUser/list', 'sysUser/lookup', '<i class=\"icon-smile icon-large\"></i>', '109', '1', '0');
INSERT INTO `sys_module` VALUES ('148', '删除', NULL, 'cmsLotteryUser/delete', NULL, '1011', '1', '0');
INSERT INTO `sys_module` VALUES ('149', '投票管理', 'cmsVote/list', NULL, '<i class=\"icon-hand-right icon-large\"></i>', '109', '1', '0');
INSERT INTO `sys_module` VALUES ('150', '增加/修改', 'cmsVote/add', NULL, NULL, '149', '1', '0');
INSERT INTO `sys_module` VALUES ('151', '保存', NULL, 'cmsVote/save', NULL, '149', '1', '0');
INSERT INTO `sys_module` VALUES ('152', '删除', NULL, 'cmsVote/delete', NULL, '149', '1', '0');
INSERT INTO `sys_module` VALUES ('153', '查看', 'cmsVote/view', NULL, NULL, '149', '1', '0');
INSERT INTO `sys_module` VALUES ('154', '投票用户', 'cmsVoteUser/list', 'sysUser/lookup', NULL, '149', '1', '0');

-- ----------------------------
-- Table structure for sys_module_lang
-- ----------------------------
CREATE TABLE `sys_module_lang` (
  `module_id` varchar(30) NOT NULL COMMENT '模块ID',
  `lang` varchar(20) NOT NULL COMMENT '语言',
  `value` varchar(100) DEFAULT NULL COMMENT '值',
  PRIMARY KEY (`module_id`,`lang`) USING BTREE
) DEFAULT CHARSET=utf8 COMMENT='模块语言';

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` int(11) NOT NULL auto_increment,
  `site_id` smallint(6) NOT NULL COMMENT '站点ID',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `owns_all_right` tinyint(1) NOT NULL COMMENT '拥有全部权限',
  `show_all_module` tinyint(1) NOT NULL COMMENT '显示全部模块',
  PRIMARY KEY  (`id`),
  KEY `site_id` (`site_id`)
) AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='角色';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '1', '超级管理员', '1', '0');
INSERT INTO `sys_role` VALUES ('2', '2', '站长', '1', '0');

-- ----------------------------
-- Table structure for sys_role_authorized
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_authorized`;
CREATE TABLE `sys_role_authorized` (
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `url` varchar(255) NOT NULL COMMENT '授权地址',
  PRIMARY KEY  (`role_id`,`url`)
) DEFAULT CHARSET=utf8 COMMENT='角色授权地址';

-- ----------------------------
-- Records of sys_role_authorized
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role_module
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_module`;
CREATE TABLE `sys_role_module` (
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `module_id` varchar(30) NOT NULL COMMENT '模块ID',
  PRIMARY KEY  (`role_id`,`module_id`)
) DEFAULT CHARSET=utf8 COMMENT='角色授权模块';

-- ----------------------------
-- Records of sys_role_module
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_user`;
CREATE TABLE `sys_role_user` (
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  PRIMARY KEY  (`role_id`,`user_id`)
) DEFAULT CHARSET=utf8 COMMENT='用户角色';

-- ----------------------------
-- Records of sys_role_user
-- ----------------------------
INSERT INTO `sys_role_user` VALUES ('1', '1');
INSERT INTO `sys_role_user` VALUES ('2', '2');

-- ----------------------------
-- Table structure for sys_site
-- ----------------------------
DROP TABLE IF EXISTS `sys_site`;
CREATE TABLE `sys_site` (
  `id` smallint(6) NOT NULL auto_increment,
  `name` varchar(50) NOT NULL,
  `use_static` tinyint(1) NOT NULL COMMENT '启用静态化',
  `site_path` varchar(255) NOT NULL COMMENT '站点地址',
  `use_ssi` tinyint(1) NOT NULL COMMENT '启用服务器端包含',
  `dynamic_path` varchar(255) NOT NULL COMMENT '动态站点地址',
  `disabled` tinyint(1) NOT NULL COMMENT '禁用',
  PRIMARY KEY  (`id`),
  KEY `disabled` (`disabled`)
) AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='站点';

-- ----------------------------
-- Records of sys_site
-- ----------------------------
INSERT INTO `sys_site` VALUES ('1', 'PublicCMS', '1', '//dev.publiccms.com:8080/publiccms/webfile/', '0', '//dev.publiccms.com:8080/publiccms/', '0');
INSERT INTO `sys_site` VALUES ('2', '演示站点1', '0', '//site2.dev.publiccms.com:8080/publiccms/webfile/', '0', '//site2.dev.publiccms.com:8080/publiccms/', '0');

-- ----------------------------
-- Table structure for sys_task
-- ----------------------------
DROP TABLE IF EXISTS `sys_task`;
CREATE TABLE `sys_task` (
  `id` int(11) NOT NULL auto_increment,
  `site_id` smallint(6) NOT NULL COMMENT '站点ID',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `status` int(11) NOT NULL COMMENT '状态',
  `cron_expression` varchar(50) NOT NULL COMMENT '表达式',
  `description` varchar(300) default NULL COMMENT '描述',
  `file_path` varchar(255) default NULL COMMENT '文件路径',
  `update_date` datetime default NULL COMMENT '更新时间',
  PRIMARY KEY  (`id`),
  KEY `status` (`status`),
  KEY `site_id` (`site_id`),
  KEY `update_date` (`update_date`)
) AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='任务计划';

-- ----------------------------
-- Records of sys_task
-- ----------------------------
INSERT INTO `sys_task` VALUES ('1', '1', '重新生成所有页面', '0', '0 0/2 * * ?', '重新生成所有页面', '/publishPage.task', NULL);
INSERT INTO `sys_task` VALUES ('2', '1', '重建索引', '0', '0 0 1 1 ? 2099', '重建全部索引', '/reCreateIndex.task', NULL);
INSERT INTO `sys_task` VALUES ('3', '1', '清理日志', '0', '0 0 1 * ?', '清理三个月以前的日志', '/clearLog.task', NULL);
INSERT INTO `sys_task` VALUES ('4', '1', '重新生成内容页面', '0', '0 0 1 1 ? 2099', '重新生成内容页面', '/publishContent.task', NULL);
INSERT INTO `sys_task` VALUES ('5', '1', '重新生成所有分类页面', '0', '0 0/6 * * ?', '重新生成所有分类页面', '/publishCategory.task', NULL);
INSERT INTO `sys_task` VALUES ('7', '1', '重新生成全站', '0', '0 0 1 1 ? 2099', '重新生成全站', '/publishAll.task', NULL);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL auto_increment,
  `site_id` smallint(6) NOT NULL COMMENT '站点ID',
  `name` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(32) NOT NULL COMMENT '密码',
  `nick_name` varchar(45) NOT NULL COMMENT '昵称',
  `dept_id` int(11) default NULL COMMENT '部门',
  `roles` text COMMENT '角色',
  `email` varchar(100) default NULL COMMENT '邮箱地址',
  `email_checked` tinyint(1) NOT NULL COMMENT '已验证邮箱',
  `superuser_access` tinyint(1) NOT NULL COMMENT '是否管理员',
  `disabled` tinyint(1) NOT NULL COMMENT '是否禁用',
  `last_login_date` datetime default NULL COMMENT '最后登录日期',
  `last_login_ip` varchar(64) default NULL COMMENT '最后登录ip',
  `login_count` int(11) NOT NULL COMMENT '登录次数',
  `registered_date` datetime default NULL COMMENT '注册日期',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `name` (`name`,`site_id`),
  UNIQUE KEY `nick_name` (`nick_name`,`site_id`),
  KEY `email` (`email`),
  KEY `disabled` (`disabled`),
  KEY `lastLoginDate` (`last_login_date`),
  KEY `email_checked` (`email_checked`),
  KEY `dept_id` (`dept_id`)
) AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', '1', 'admin', '21232f297a57a5a743894a0e4a801fc3', '管理员', '1', '1', 'master@sanluan.com', '0', '1', '0', '2017-01-01 00:00:00', '127.0.0.1', '0', '2017-01-01 00:00:00');
INSERT INTO `sys_user` VALUES ('2', '2', 'admin', '21232f297a57a5a743894a0e4a801fc3', 'admin', '2', '2', '', '0', '1', '0', '2017-01-01 00:00:00', '127.0.0.1', '0', '2017-01-01 00:00:00');


-- ----------------------------
-- Table structure for sys_user_token
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_token`;
CREATE TABLE `sys_user_token` (
  `auth_token` varchar(40) NOT NULL COMMENT '登录授权',
  `site_id` smallint(6) NOT NULL COMMENT '站点ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `channel` varchar(50) NOT NULL COMMENT '渠道',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `login_ip` varchar(64) NOT NULL COMMENT '登录IP',
  PRIMARY KEY  (`auth_token`),
  KEY `user_id` (`user_id`),
  KEY `create_date` (`create_date`),
  KEY `channel` (`channel`),
  KEY `site_id` (`site_id`)
) DEFAULT CHARSET=utf8 COMMENT='用户令牌';

-- ----------------------------
-- Records of sys_user_token
-- ----------------------------
