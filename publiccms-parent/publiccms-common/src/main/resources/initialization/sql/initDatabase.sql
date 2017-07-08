SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for cms_category
-- ----------------------------
DROP TABLE IF EXISTS `cms_category`;
CREATE TABLE `cms_category` (
  `id` int(11) NOT NULL auto_increment,
  `site_id` int(11) NOT NULL COMMENT '站点ID',
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
  `contents` int(11) NOT NULL default '0' COMMENT '内容数',
  `extend_id` int(11) default NULL COMMENT '扩展ID',
  PRIMARY KEY  (`id`),
  KEY `parent_id` (`parent_id`),
  KEY `disabled` (`disabled`),
  KEY `sort` (`sort`),
  KEY `site_id` (`site_id`),
  KEY `type_id` (`type_id`),
  KEY `allow_contribute` (`allow_contribute`),
  KEY `hidden` (`hidden`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='分类';

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='分类扩展';

-- ----------------------------
-- Table structure for cms_category_model
-- ----------------------------
DROP TABLE IF EXISTS `cms_category_model`;
CREATE TABLE `cms_category_model` (
  `category_id` int(11) NOT NULL COMMENT '分类ID',
  `model_id` varchar(20) NOT NULL COMMENT '模型编码',
  `template_path` varchar(200) default NULL COMMENT '内容模板路径',
  PRIMARY KEY  (`category_id`,`model_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='分类模型';

-- ----------------------------
-- Table structure for cms_category_type
-- ----------------------------
DROP TABLE IF EXISTS `cms_category_type`;
CREATE TABLE `cms_category_type` (
  `id` int(11) NOT NULL auto_increment,
  `siteId` int(11) NOT NULL COMMENT '站点ID',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `sort` int(11) NOT NULL COMMENT '排序',
  `extend_id` int(11) default NULL COMMENT '扩展ID',
  PRIMARY KEY  (`id`),
  KEY `siteId` (`siteId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for cms_content
-- ----------------------------
DROP TABLE IF EXISTS `cms_content`;
CREATE TABLE `cms_content` (
  `id` bigint(20) NOT NULL auto_increment,
  `site_id` int(11) NOT NULL COMMENT '站点ID',
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
  `childs` int(11) NOT NULL COMMENT '内容页数',
  `scores` int(11) NOT NULL COMMENT '分数',
  `comments` int(11) NOT NULL COMMENT '评论数',
  `clicks` int(11) NOT NULL COMMENT '点击数',
  `publish_date` datetime NOT NULL COMMENT '发布日期',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `sort` int(11) NOT NULL default '0' COMMENT '顺序',
  `status` int(11) NOT NULL COMMENT '状态：0、草稿 1、已发布 2、待审核',
  `disabled` tinyint(1) NOT NULL COMMENT '是否删除',
  PRIMARY KEY  (`id`),
  KEY `publish_date` (`publish_date`),
  KEY `user_id` (`user_id`),
  KEY `category_id` (`category_id`),
  KEY `model_id` (`model_id`),
  KEY `parent_id` (`parent_id`),
  KEY `status` (`status`),
  KEY `sort` (`sort`),
  KEY `childs` (`childs`),
  KEY `scores` (`scores`),
  KEY `comments` (`comments`),
  KEY `clicks` (`clicks`),
  KEY `title` (`title`),
  KEY `check_user_id` (`check_user_id`),
  KEY `site_id` (`site_id`),
  KEY `has_files` (`has_files`),
  KEY `has_images` (`has_images`),
  KEY `only_url` (`only_url`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='内容';

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='内容扩展';

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='内容附件';

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='推荐推荐';

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='字典';

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

-- ----------------------------
-- Table structure for cms_lottery
-- ----------------------------
DROP TABLE IF EXISTS `cms_lottery`;
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
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for cms_lottery_user_attribute
-- ----------------------------
DROP TABLE IF EXISTS `cms_lottery_user_attribute`;
CREATE TABLE `cms_lottery_user_attribute` (
  `lottery_user_id` bigint(20) NOT NULL COMMENT '抽奖用户ID',
  `data` longtext COMMENT '数据JSON',
  PRIMARY KEY  (`lottery_user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='抽奖用户扩展';

-- ----------------------------
-- Table structure for cms_place
-- ----------------------------
DROP TABLE IF EXISTS `cms_place`;
CREATE TABLE `cms_place` (
  `id` bigint(20) NOT NULL auto_increment,
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `path` varchar(255) NOT NULL COMMENT '模板路径',
  `user_id` bigint(20) default NULL COMMENT '提交用户',
  `item_type` varchar(50) default NULL COMMENT '推荐项目类型',
  `item_id` int(11) default NULL COMMENT '推荐项目ID',
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
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='页面数据';

-- ----------------------------
-- Table structure for cms_place_attribute
-- ----------------------------
DROP TABLE IF EXISTS `cms_place_attribute`;
CREATE TABLE `cms_place_attribute` (
  `place_id` bigint(20) NOT NULL COMMENT '位置ID',
  `data` longtext COMMENT '数据JSON',
  PRIMARY KEY  (`place_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='推荐位数据扩展';

-- ----------------------------
-- Table structure for cms_tag
-- ----------------------------
DROP TABLE IF EXISTS `cms_tag`;
CREATE TABLE `cms_tag` (
  `id` bigint(20) NOT NULL auto_increment,
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `type_id` int(11) default NULL COMMENT '分类ID',
  `search_count` int(11) NOT NULL COMMENT '搜索次数',
  PRIMARY KEY  (`id`),
  KEY `site_id` (`site_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='标签';

-- ----------------------------
-- Table structure for cms_tag_type
-- ----------------------------
DROP TABLE IF EXISTS `cms_tag_type`;
CREATE TABLE `cms_tag_type` (
  `id` int(11) NOT NULL auto_increment,
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `count` int(11) NOT NULL COMMENT '标签数',
  PRIMARY KEY  (`id`),
  KEY `site_id` (`site_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='标签类型';

-- ----------------------------
-- Table structure for cms_vote
-- ----------------------------
DROP TABLE IF EXISTS `cms_vote`;
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
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for cms_vote_item_attribute
-- ----------------------------
DROP TABLE IF EXISTS `cms_vote_item_attribute`;
CREATE TABLE `cms_vote_item_attribute` (
  `vote_item_id` bigint(20) NOT NULL COMMENT '选项ID',
  `data` longtext COMMENT '数据JSON',
  PRIMARY KEY  (`vote_item_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='投票选项扩展';

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for cms_word
-- ----------------------------
DROP TABLE IF EXISTS `cms_word`;
CREATE TABLE `cms_word` (
  `id` bigint(20) NOT NULL auto_increment,
  `name` varchar(255) NOT NULL COMMENT '名称',
  `site_id` int(11) NOT NULL COMMENT '站点',
  `search_count` int(11) NOT NULL COMMENT '搜索次数',
  `hidden` tinyint(1) NOT NULL COMMENT '隐藏',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `name` (`name`,`site_id`),
  KEY `hidden` (`hidden`),
  KEY `create_date` (`create_date`),
  KEY `search_count` (`search_count`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for home_active
-- ----------------------------
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

-- ----------------------------
-- Table structure for home_article
-- ----------------------------
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

-- ----------------------------
-- Table structure for home_article_content
-- ----------------------------
DROP TABLE IF EXISTS `home_article_content`;
CREATE TABLE `home_article_content` (
  `article_id` bigint(20) NOT NULL COMMENT '文章ID',
  `content` longtext COMMENT '内容',
  PRIMARY KEY  (`article_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='文章内容';

-- ----------------------------
-- Table structure for home_attention
-- ----------------------------
DROP TABLE IF EXISTS `home_attention`;
CREATE TABLE `home_attention` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `attention_id` bigint(20) NOT NULL COMMENT '关注ID',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY  (`user_id`,`attention_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='空间关注';

-- ----------------------------
-- Table structure for home_broadcast
-- ----------------------------
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

-- ----------------------------
-- Table structure for home_comment
-- ----------------------------
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

-- ----------------------------
-- Table structure for home_comment_content
-- ----------------------------
DROP TABLE IF EXISTS `home_comment_content`;
CREATE TABLE `home_comment_content` (
  `comment_id` bigint(20) NOT NULL,
  `content` longtext,
  PRIMARY KEY  (`comment_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='评论内容';

-- ----------------------------
-- Table structure for home_dialog
-- ----------------------------
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

-- ----------------------------
-- Table structure for home_directory
-- ----------------------------
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

-- ----------------------------
-- Table structure for home_file
-- ----------------------------
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

-- ----------------------------
-- Table structure for home_friend
-- ----------------------------
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

-- ----------------------------
-- Table structure for home_friend_apply
-- ----------------------------
DROP TABLE IF EXISTS `home_friend_apply`;
CREATE TABLE `home_friend_apply` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `friend_id` bigint(20) NOT NULL COMMENT '好友ID',
  `message` varchar(300) NOT NULL COMMENT '消息',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY  (`user_id`,`friend_id`),
  KEY `create_date` (`create_date`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='好友申请';

-- ----------------------------
-- Table structure for home_group
-- ----------------------------
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

-- ----------------------------
-- Table structure for home_group_active
-- ----------------------------
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

-- ----------------------------
-- Table structure for home_group_apply
-- ----------------------------
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

-- ----------------------------
-- Table structure for home_group_post
-- ----------------------------
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

-- ----------------------------
-- Table structure for home_group_post_content
-- ----------------------------
DROP TABLE IF EXISTS `home_group_post_content`;
CREATE TABLE `home_group_post_content` (
  `post_id` bigint(20) NOT NULL,
  `content` longtext,
  PRIMARY KEY  (`post_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='帖子内容';

-- ----------------------------
-- Table structure for home_group_user
-- ----------------------------
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

-- ----------------------------
-- Table structure for home_message
-- ----------------------------
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

-- ----------------------------
-- Table structure for home_score
-- ----------------------------
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

-- ----------------------------
-- Table structure for home_user
-- ----------------------------
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

-- ----------------------------
-- Table structure for log_login
-- ----------------------------
DROP TABLE IF EXISTS `log_login`;
CREATE TABLE `log_login` (
  `id` bigint(20) NOT NULL auto_increment,
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `name` varchar(50) NOT NULL COMMENT '用户名',
  `user_id` bigint(20) default NULL COMMENT '用户ID',
  `ip` varchar(64) NOT NULL COMMENT 'IP',
  `channel` varchar(50) NOT NULL COMMENT '登陆渠道',
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
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='登陆日志';

-- ----------------------------
-- Table structure for log_operate
-- ----------------------------
DROP TABLE IF EXISTS `log_operate`;
CREATE TABLE `log_operate` (
  `id` bigint(20) NOT NULL auto_increment,
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `user_id` bigint(20) default NULL COMMENT '用户ID',
  `channel` varchar(50) NOT NULL COMMENT '操作取到',
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
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='操作日志';

-- ----------------------------
-- Table structure for log_task
-- ----------------------------
DROP TABLE IF EXISTS `log_task`;
CREATE TABLE `log_task` (
  `id` bigint(20) NOT NULL auto_increment,
  `site_id` int(11) NOT NULL COMMENT '站点ID',
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
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='任务计划日志';

-- ----------------------------
-- Table structure for log_upload
-- ----------------------------
DROP TABLE IF EXISTS `log_upload`;
CREATE TABLE `log_upload` (
  `id` bigint(20) NOT NULL auto_increment,
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `channel` varchar(50) NOT NULL COMMENT '操作取到',
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
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='上传日志';

-- ----------------------------
-- Table structure for sys_app
-- ----------------------------
DROP TABLE IF EXISTS `sys_app`;
CREATE TABLE `sys_app` (
  `id` int(11) NOT NULL auto_increment,
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `channel` varchar(50) NOT NULL COMMENT '渠道',
  `app_key` varchar(50) NOT NULL COMMENT 'APP key',
  `app_secret` varchar(50) NOT NULL COMMENT 'APP secret',
  `authorized_apis`  text NULL COMMENT '授权API',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `key` (`app_key`),
  KEY `site_id` (`site_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_app_client
-- ----------------------------
DROP TABLE IF EXISTS `sys_app_client`;
CREATE TABLE `sys_app_client` (
  `site_id` int(11) NOT NULL COMMENT '站点ID',
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
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='服务器集群';

-- ----------------------------
-- Table structure for sys_config_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_config_data`;
CREATE TABLE `sys_config_data` (
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `code` varchar(50) NOT NULL COMMENT '配置项编码',
  `data` longtext NOT NULL COMMENT '值',
  PRIMARY KEY  (`site_id`,`code`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='站点配置';

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `id` int(11) NOT NULL auto_increment,
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `parent_id` int(11) default NULL COMMENT '父部门ID',
  `description` varchar(300) default NULL COMMENT '描述',
  `user_id` bigint(20) default NULL COMMENT '负责人',
  `max_sort` INT NOT NULL DEFAULT 1000 COMMENT  '最大内容置顶级别',
  `owns_all_category` tinyint(1) NOT NULL COMMENT '拥有全部分类权限',
  `owns_all_page` tinyint(1) NOT NULL COMMENT '拥有全部页面权限',
  PRIMARY KEY  (`id`),
  KEY `site_id` (`site_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='部门';

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='部门分类';

-- ----------------------------
-- Table structure for sys_dept_page
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept_page`;
CREATE TABLE `sys_dept_page` (
  `dept_id` int(11) NOT NULL COMMENT '部门ID',
  `page` varchar(255) NOT NULL COMMENT '页面',
  PRIMARY KEY  (`dept_id`,`page`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='部门页面';

-- ----------------------------
-- Table structure for sys_domain
-- ----------------------------
DROP TABLE IF EXISTS `sys_domain`;
CREATE TABLE `sys_domain` (
  `name` varchar(255) NOT NULL COMMENT '域名',
  `site_id` int(11) NOT NULL COMMENT '站点',
  `wild` tinyint(1) NOT NULL COMMENT '通配域名',
  `path` varchar(255) default NULL COMMENT '路径',
  PRIMARY KEY  (`name`),
  KEY `site_id` (`site_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='域名';

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='邮件地址验证日志';

-- ----------------------------
-- Table structure for sys_extend
-- ----------------------------
DROP TABLE IF EXISTS `sys_extend`;
CREATE TABLE `sys_extend` (
  `id` int(11) NOT NULL auto_increment,
  `item_type` varchar(20) NOT NULL COMMENT '扩展类型',
  `item_id` int(11) NOT NULL COMMENT '扩展项目ID',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

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
  `dictionary_type` varchar(20) default NULL,
  `dictionary_id` varchar(20) default NULL,
  `sort` int(11) NOT NULL default '0' COMMENT '顺序',
  PRIMARY KEY  (`extend_id`,`code`),
  KEY `sort` (`sort`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='扩展';

-- ----------------------------
-- Table structure for sys_moudle
-- ----------------------------
DROP TABLE IF EXISTS `sys_moudle`;
CREATE TABLE `sys_moudle` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(100) NOT NULL COMMENT '名称',
  `url` varchar(255) default NULL COMMENT '链接地址',
  `authorized_url` text COMMENT '授权地址',
  `attached` varchar(300) default NULL COMMENT '标题附加',
  `parent_id` int(11) default NULL COMMENT '父模块',
  `menu` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否菜单',
  `sort` int(11) NOT NULL COMMENT '排序',
  PRIMARY KEY  (`id`),
  KEY `url` (`url`),
  KEY `parent_id` (`parent_id`),
  KEY `sort` (`sort`)
) ENGINE=MyISAM AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='模块';

-- ----------------------------
-- Records of sys_moudle
-- ----------------------------
INSERT INTO `sys_moudle` VALUES ('1', '个人', null, null, '<i class=\"icon-user icon-large\"></i>', null, '1', '0');
INSERT INTO `sys_moudle` VALUES ('2', '内容', null, null, '<i class=\"icon-book icon-large\"></i>', null, '1', '0');
INSERT INTO `sys_moudle` VALUES ('3', '分类', null, null, '<i class=\"icon-folder-open icon-large\"></i>', null, '1', '1');
INSERT INTO `sys_moudle` VALUES ('4', '页面', null, null, '<i class=\"icon-globe icon-large\"></i>', null, '1', '0');
INSERT INTO `sys_moudle` VALUES ('5', '维护', null, null, '<i class=\"icon-cogs icon-large\"></i>', null, '1', '1');
INSERT INTO `sys_moudle` VALUES ('6', '与我相关', null, null, '<i class=\"icon-user icon-large\"></i>', '1', '1', '0');
INSERT INTO `sys_moudle` VALUES ('7', '修改密码', 'myself/password', 'changePassword', '<i class=\"icon-key icon-large\"></i>', '6', '1', '0');
INSERT INTO `sys_moudle` VALUES ('8', '我的内容', 'myself/contentList', null, '<i class=\"icon-book icon-large\"></i>', '6', '1', '0');
INSERT INTO `sys_moudle` VALUES ('9', '我的操作日志', 'myself/logOperate', null, '<i class=\"icon-list-alt icon-large\"></i>', '6', '1', '0');
INSERT INTO `sys_moudle` VALUES ('10', '我的登陆日志', 'myself/logLogin', null, '<i class=\"icon-signin icon-large\"></i>', '6', '1', '0');
INSERT INTO `sys_moudle` VALUES ('11', '我的登陆授权', 'myself/userTokenList', null, '<i class=\"icon-unlock-alt icon-large\"></i>', '6', '1', '0');
INSERT INTO `sys_moudle` VALUES ('12', '内容管理', 'cmsContent/list', 'sysUser/lookup', '<i class=\"icon-book icon-large\"></i>', '2', '1', '0');
INSERT INTO `sys_moudle` VALUES ('13', '内容扩展', null, null, '<i class=\"icon-road icon-large\"></i>', '2', '1', '0');
INSERT INTO `sys_moudle` VALUES ('14', '标签管理', 'cmsTag/list', 'cmsTagType/lookup', '<i class=\"icon-tag icon-large\"></i>', '13', '1', '0');
INSERT INTO `sys_moudle` VALUES ('15', '增加/修改', 'cmsTag/add', 'cmsTagType/lookup,cmsTag/save', null, '14', '1', '0');
INSERT INTO `sys_moudle` VALUES ('16', '删除', null, 'cmsTag/delete', null, '14', '1', '0');
INSERT INTO `sys_moudle` VALUES ('17', '增加/修改', 'cmsContent/add', 'cmsContent/addMore,cmsContent/lookup,cmsContent/lookup_list,cmsContent/save,ueditor', null, '12', '1', '0');
INSERT INTO `sys_moudle` VALUES ('18', '删除', null, 'cmsContent/delete', null, '12', '1', '0');
INSERT INTO `sys_moudle` VALUES ('19', '审核', null, 'cmsContent/check', null, '12', '1', '0');
INSERT INTO `sys_moudle` VALUES ('20', '刷新', null, 'cmsContent/refresh', null, '12', '1', '0');
INSERT INTO `sys_moudle` VALUES ('21', '生成', null, 'cmsContent/publish', null, '12', '1', '0');
INSERT INTO `sys_moudle` VALUES ('22', '移动', 'cmsContent/moveParameters', 'cmsContent/move', null, '12', '1', '0');
INSERT INTO `sys_moudle` VALUES ('23', '推荐', 'cmsContent/push', 'cmsContent/push_content,cmsContent/push_content_list,cmsContent/push_to_content,cmsContent/push_page,cmsContent/push_page_list,cmsPlace/add,cmsPlace/save,cmsContent/related', null, '12', '1', '0');
INSERT INTO `sys_moudle` VALUES ('24', '分类管理', 'cmsCategory/list', null, '<i class=\"icon-folder-open icon-large\"></i>', '3', '1', '0');
INSERT INTO `sys_moudle` VALUES ('25', '增加/修改', 'cmsCategory/add', 'cmsCategory/addMore,cmsTemplate/lookup,cmsCategory/categoryPath,cmsCategory/contentPath,file/doUpload,cmsCategory/save', null, '24', '1', '0');
INSERT INTO `sys_moudle` VALUES ('26', '删除', null, 'cmsCategory/delete', null, '24', '1', '0');
INSERT INTO `sys_moudle` VALUES ('27', '生成', 'cmsCategory/publishParameters', 'cmsCategory/publish', null, '24', '1', '0');
INSERT INTO `sys_moudle` VALUES ('28', '移动', 'cmsCategory/moveParameters', 'cmsCategory/move,cmsCategory/lookup', null, '24', '1', '0');
INSERT INTO `sys_moudle` VALUES ('29', '推荐', 'cmsCategory/push_page', 'cmsPlace/push,cmsPlace/add,cmsPlace/save', null, '24', '1', '0');
INSERT INTO `sys_moudle` VALUES ('30', '页面管理', null, null, '<i class=\"icon-globe icon-large\"></i>', '4', '1', '0');
INSERT INTO `sys_moudle` VALUES ('31', '分类扩展', null, null, '<i class=\"icon-road icon-large\"></i>', '3', '1', '0');
INSERT INTO `sys_moudle` VALUES ('32', '分类类型', 'cmsCategoryType/list', null, '<i class=\"icon-road icon-large\"></i>', '31', '1', '0');
INSERT INTO `sys_moudle` VALUES ('33', '标签分类', 'cmsTagType/list', null, '<i class=\"icon-tags icon-large\"></i>', '31', '1', '0');
INSERT INTO `sys_moudle` VALUES ('34', '增加/修改', 'cmsTagType/add', 'cmsTagType/save', null, '33', '1', '0');
INSERT INTO `sys_moudle` VALUES ('35', '删除', null, 'cmsTagType/delete', null, '33', '1', '0');
INSERT INTO `sys_moudle` VALUES ('36', '增加/修改', 'cmsCategoryType/add', 'cmsCategoryType/save', null, '32', '1', '0');
INSERT INTO `sys_moudle` VALUES ('37', '删除', null, 'cmsCategoryType/delete', null, '32', '1', '0');
INSERT INTO `sys_moudle` VALUES ('38', '文件管理', null, null, '<i class=\"icon-folder-close-alt icon-large\"></i>', '5', '1', '0');
INSERT INTO `sys_moudle` VALUES ('39', '模板文件管理', 'cmsTemplate/list', 'cmsTemplate/directory', '<i class=\"icon-code icon-large\"></i>', '38', '1', '0');
INSERT INTO `sys_moudle` VALUES ('40', '修改模板元数据', 'cmsTemplate/metadata', 'cmsTemplate/saveMetadata', null, '39', '1', '0');
INSERT INTO `sys_moudle` VALUES ('41', '修改模板', 'cmsTemplate/content', 'cmsTemplate/save,cmsTemplate/chipLookup,cmsWebFile/lookup,placeTemplate/form,cmsWebFile/contentForm,cmsTemplate/demo,cmsTemplate/help,cmsTemplate/upload,cmsTemplate/doUpload', null, '39', '1', '0');
INSERT INTO `sys_moudle` VALUES ('42', '页面片段模板', 'placeTemplate/list', null, '<i class=\"icon-list-alt icon-large\"></i>', '38', '1', '0');
INSERT INTO `sys_moudle` VALUES ('43', '删除模板', null, 'cmsTemplate/delete', null, '39', '1', '0');
INSERT INTO `sys_moudle` VALUES ('44', '搜索词管理', 'cmsWord/list', null, '<i class=\"icon-search icon-large\"></i>', '109', '1', '0');
INSERT INTO `sys_moudle` VALUES ('45', '运营', null, null, '<i class=\"icon-user icon-large\"></i>', null, '1', '0');
INSERT INTO `sys_moudle` VALUES ('46', '系统管理', NULL, NULL, '<i class=\"icon-cog icon-large\"></i>', '45', '1', '0');
INSERT INTO `sys_moudle` VALUES ('47', '生成页面', null, 'cmsTemplate/publish', null, '112', '1', '0');
INSERT INTO `sys_moudle` VALUES ('48', '保存页面元数据', '', 'cmsPage/save,file/doUpload,cmsPage/clearCache', null, '112', '1', '0');
INSERT INTO `sys_moudle` VALUES ('49', '增加/修改推荐位数据', 'cmsPlace/add', 'cmsContent/lookup,cmsPlace/lookup,cmsPlace/lookup_content_list,file/doUpload,cmsPlace/save', null, '107', '1', '0');
INSERT INTO `sys_moudle` VALUES ('50', '删除推荐位数据', null, 'cmsPlace/delete', null, '107', '1', '0');
INSERT INTO `sys_moudle` VALUES ('51', '刷新推荐位数据', null, 'cmsPlace/refresh', null, '107', '1', '0');
INSERT INTO `sys_moudle` VALUES ('52', '审核推荐位数据', null, 'cmsPlace/check', null, '107', '1', '0');
INSERT INTO `sys_moudle` VALUES ('53', '发布推荐位', null, 'cmsTemplate/publishPlace', null, '107', '1', '0');
INSERT INTO `sys_moudle` VALUES ('54', '清空推荐位数据', null, 'cmsPlace/clear', null, '107', '1', '0');
INSERT INTO `sys_moudle` VALUES ('55', '查看推荐位源码', 'cmsTemplate/placeContent', null, null, '39', '1', '0');
INSERT INTO `sys_moudle` VALUES ('56', '应用授权', 'sysApp/list', NULL, '<i class=\"icon-linux icon-large\"></i>', '62', '1', '0');
INSERT INTO `sys_moudle` VALUES ('57', '增加/修改', 'sysApp/add', NULL, '', '56', '1', '0');
INSERT INTO `sys_moudle` VALUES ('58', '保存', NULL, 'sysApp/save', '', '56', '1', '0');
INSERT INTO `sys_moudle` VALUES ('59', '删除', NULL, 'sysApp/delete', NULL, '56', '1', '0');
INSERT INTO `sys_moudle` VALUES ('60', '文件上传日志', 'log/upload', 'sysUser/lookup', '<i class=\"icon-list-alt icon-large\"></i>', '63', '1', '0');
INSERT INTO `sys_moudle` VALUES ('61', '用户管理', null, null, '<i class=\"icon-user icon-large\"></i>', '5', '1', '0');
INSERT INTO `sys_moudle` VALUES ('62', '系统维护', null, null, '<i class=\"icon-cogs icon-large\"></i>', '5', '1', '0');
INSERT INTO `sys_moudle` VALUES ('63', '日志管理', null, null, '<i class=\"icon-list-alt icon-large\"></i>', '5', '1', '0');
INSERT INTO `sys_moudle` VALUES ('64', '操作日志', 'log/operate', 'sysUser/lookup', '<i class=\"icon-list-alt icon-large\"></i>', '63', '1', '0');
INSERT INTO `sys_moudle` VALUES ('65', '登录日志', 'log/login', 'sysUser/lookup', '<i class=\"icon-signin icon-large\"></i>', '63', '1', '0');
INSERT INTO `sys_moudle` VALUES ('66', '任务计划日志', 'log/task', 'sysUser/lookup', '<i class=\"icon-time icon-large\"></i>', '63', '1', '0');
INSERT INTO `sys_moudle` VALUES ('67', '删除', null, 'logOperate/delete', null, '64', '1', '0');
INSERT INTO `sys_moudle` VALUES ('68', '删除', null, 'logLogin/delete', null, '65', '1', '0');
INSERT INTO `sys_moudle` VALUES ('69', '删除', null, 'logTask/delete', null, '66', '1', '0');
INSERT INTO `sys_moudle` VALUES ('70', '查看', 'log/taskView', null, null, '66', '1', '0');
INSERT INTO `sys_moudle` VALUES ('71', '用户管理', 'sysUser/list', null, '<i class=\"icon-user icon-large\"></i>', '61', '1', '0');
INSERT INTO `sys_moudle` VALUES ('72', '部门管理', 'sysDept/list', 'sysDept/lookup,sysUser/lookup', '<i class=\"icon-group icon-large\"></i>', '61', '1', '0');
INSERT INTO `sys_moudle` VALUES ('73', '角色管理', 'sysRole/list', null, '<i class=\"icon-user-md icon-large\"></i>', '61', '1', '0');
INSERT INTO `sys_moudle` VALUES ('74', '增加/修改', 'sysUser/add', 'sysDept/lookup,sysUser/save', null, '71', '1', '0');
INSERT INTO `sys_moudle` VALUES ('75', '启用', null, 'sysUser/enable', null, '71', '1', '0');
INSERT INTO `sys_moudle` VALUES ('76', '禁用', null, 'sysUser/disable', null, '71', '1', '0');
INSERT INTO `sys_moudle` VALUES ('77', '增加/修改', 'sysDept/add', 'sysDept/lookup,sysUser/lookup,sysDept/save', null, '72', '1', '0');
INSERT INTO `sys_moudle` VALUES ('78', '删除', null, 'sysDept/delete', null, '72', '1', '0');
INSERT INTO `sys_moudle` VALUES ('79', '增加/修改', 'sysRole/add', 'sysRole/save', null, '73', '1', '0');
INSERT INTO `sys_moudle` VALUES ('80', '删除', null, 'sysRole/delete', null, '73', '1', '0');
INSERT INTO `sys_moudle` VALUES ('81', '内容模型管理', 'cmsModel/list', null, '<i class=\"icon-th-large icon-large\"></i>', '38', '1', '0');
INSERT INTO `sys_moudle` VALUES ('82', '任务计划', 'sysTask/list', null, '<i class=\"icon-time icon-large\"></i>', '62', '1', '0');
INSERT INTO `sys_moudle` VALUES ('83', '系统监控', 'report/cms', NULL, '<i class=\"icon-check-sign icon-large\"></i>', '46', '1', '0');
INSERT INTO `sys_moudle` VALUES ('84', '动态域名', 'sysDomain/domainList', null, '<i class=\"icon-qrcode icon-large\"></i>', '62', '1', '0');
INSERT INTO `sys_moudle` VALUES ('85', '任务计划脚本', 'taskTemplate/list', null, '<i class=\"icon-time icon-large\"></i>', '38', '1', '0');
INSERT INTO `sys_moudle` VALUES ('86', '修改脚本', 'taskTemplate/metadata', 'cmsTemplate/saveMetadata,taskTemplate/content,cmsTemplate/save,taskTemplate/chipLookup', null, '85', '1', '0');
INSERT INTO `sys_moudle` VALUES ('87', '删除脚本', null, 'cmsTemplate/delete', null, '85', '1', '0');
INSERT INTO `sys_moudle` VALUES ('88', '客户端管理', 'sysAppClient/list', NULL, '<i class=\"icon-coffee icon-large\"></i>', '61', '1', '0');
INSERT INTO `sys_moudle` VALUES ('89', '启用', NULL, 'sysAppClient/enable', NULL, '88', '1', '0');
INSERT INTO `sys_moudle` VALUES ('90', '增加/修改', 'cmsModel/add', 'cmsModel/save,cmsTemplate/lookup', null, '81', '1', '0');
INSERT INTO `sys_moudle` VALUES ('91', '删除', null, 'cmsModel/delete', null, '81', '1', '0');
INSERT INTO `sys_moudle` VALUES ('92', '增加/修改', 'sysTask/add', 'sysTask/save,sysTask/example,taskTemplate/lookup', null, '82', '1', '0');
INSERT INTO `sys_moudle` VALUES ('93', '删除', null, 'sysTask/delete', null, '82', '1', '0');
INSERT INTO `sys_moudle` VALUES ('94', '立刻执行', null, 'sysTask/runOnce', null, '82', '1', '0');
INSERT INTO `sys_moudle` VALUES ('95', '暂停', null, 'sysTask/pause', null, '82', '1', '0');
INSERT INTO `sys_moudle` VALUES ('96', '恢复', null, 'sysTask/resume', null, '82', '1', '0');
INSERT INTO `sys_moudle` VALUES ('97', '重新初始化', null, 'sysTask/recreate', null, '82', '1', '0');
INSERT INTO `sys_moudle` VALUES ('98', '禁用', NULL, 'sysAppClient/disable', NULL, '88', '1', '0');
INSERT INTO `sys_moudle` VALUES ('99', '抽奖管理', 'cmsLottery/list', NULL, '<i class=\"icon-ticket icon-large\"></i>', '109', '1', '0');
INSERT INTO `sys_moudle` VALUES ('100', '修改', 'sysDomain/config', 'sysDomain/saveConfig,cmsTemplate/directoryLookup,cmsTemplate/lookup', null, '84', '1', '0');
INSERT INTO `sys_moudle` VALUES ('101', '站点配置', 'sysConfigData/list', null, '<i class=\"icon-cog icon-large\"></i>', '46', '1', '0');
INSERT INTO `sys_moudle` VALUES ('102', '修改', 'cmsContent/add', 'cmsContent/addMore,file/doUpload,cmsContent/lookup,cmsContent/lookup_list,cmsContent/save,ueditor', null, '8', '1', '0');
INSERT INTO `sys_moudle` VALUES ('103', '删除', null, 'cmsContent/delete', null, '8', '1', '0');
INSERT INTO `sys_moudle` VALUES ('104', '刷新', null, 'cmsContent/refresh', null, '8', '1', '0');
INSERT INTO `sys_moudle` VALUES ('105', '生成', null, 'cmsContent/publish', null, '8', '1', '0');
INSERT INTO `sys_moudle` VALUES ('106', '推荐', 'cmsContent/push', 'cmsContent/push_content,cmsContent/push_content_list,cmsContent/push_to_content,cmsContent/push_page,cmsContent/push_page_list,cmsContent/push_to_place,cmsContent/related', null, '8', '1', '0');
INSERT INTO `sys_moudle` VALUES ('107', '页面片段管理', 'cmsPlace/list', 'sysUser/lookup,cmsPlace/data_list', '<i class=\"icon-list-alt icon-large\"></i>', '30', '1', '0');
INSERT INTO `sys_moudle` VALUES ('108', '增加/修改', 'cmsLottery/add', NULL, NULL, '1007', '1', '0');
INSERT INTO `sys_moudle` VALUES ('109', '运营管理', null, null, '<i class=\"icon-home icon-large\"></i>', '45', '1', '0');
INSERT INTO `sys_moudle` VALUES ('110', '修改模板元数据', 'placeTemplate/metadata', 'cmsTemplate/savePlaceMetaData', NULL, '42', '1', '0');
INSERT INTO `sys_moudle` VALUES ('111', '修改模板', 'placeTemplate/content', 'cmsTemplate/help,cmsTemplate/chipLookup,cmsWebFile/lookup,cmsWebFile/contentForm,placeTemplate/form', NULL, '42', '1', '0');
INSERT INTO `sys_moudle` VALUES ('112', '页面管理', 'cmsPage/list', 'cmsPage/metadata,sysUser/lookup,cmsContent/lookup,cmsContent/lookup_list,cmsCategory/lookup', '<i class=\"icon-globe icon-large\"></i>', '30', '1', '0');
INSERT INTO `sys_moudle` VALUES ('113', '刷新缓存', NULL, 'clearCache', '', NULL, '0', '1');
INSERT INTO `sys_moudle` VALUES ('114', '查看', 'cmsContent/view', null, null, '12', '0', '0');
INSERT INTO `sys_moudle` VALUES ('115', '查看', 'cmsPlace/view', null, null, '107', '0', '0');
INSERT INTO `sys_moudle` VALUES ('116', '修改类型', 'cmsCategory/changeTypeParameters', 'cmsCategory/changeType', null, '24', '0', '0');
INSERT INTO `sys_moudle` VALUES ('117', '内容回收站', 'cmsRecycleContent/list', 'sysUser/lookup', '<i class=\"icon-trash icon-large\"></i>', '13', '1', '0');
INSERT INTO `sys_moudle` VALUES ('118', '删除', NULL, 'cmsContent/realDelete', NULL, '155', '0', '0');
INSERT INTO `sys_moudle` VALUES ('119', '还原', NULL, 'cmsContent/recycle', NULL, '155', '0', '0');
INSERT INTO `sys_moudle` VALUES ('120', '置顶', 'cmsContent/sortParameters', 'cmsContent/sort', NULL, '12', '0', '0');
INSERT INTO `sys_moudle` VALUES ('121', '人员管理', 'sysDept/userList', 'sysDept/addUser,sysDept/saveUser,sysDept/enableUser,sysDept/disableUser', NULL, '72', '0', '0');
INSERT INTO `sys_moudle` VALUES ('122', '数据字典管理', 'cmsDictionary/list', null, '<i class=\"icon-book icon-large\"></i>', '62', '1', '0');
INSERT INTO `sys_moudle` VALUES ('123', '添加', 'cmsDictionary/add', 'cmsDictionary/save', null, '122', '0', '0');
INSERT INTO `sys_moudle` VALUES ('124', '删除', null, 'cmsDictionary/delete', null, '122', '0', '0');
INSERT INTO `sys_moudle` VALUES ('130', '评论管理', 'homeComment/list', null, '<i class=\"icon-comment-alt icon-large\"></i>', '109', '1', '0');
INSERT INTO `sys_moudle` VALUES ('131', '网站文件管理', 'cmsWebFile/list', null, '<i class=\"icon-globe icon-large\"></i>', '38', '1', '0');
INSERT INTO `sys_moudle` VALUES ('132', '新建目录', 'cmsWebFile/directory', 'cmsWebFile/createDirectory', null, '131', '1', '0');
INSERT INTO `sys_moudle` VALUES ('133', '上传文件', 'cmsWebFile/upload', 'cmsWebFile/doUpload', null, '131', '1', '0');
INSERT INTO `sys_moudle` VALUES ('134', '压缩', null, 'cmsWebFile/zip', null, '131', '1', '0');
INSERT INTO `sys_moudle` VALUES ('135', '解压缩', null, 'cmsWebFile/unzip,cmsWebFile/unzipHere', null, '131', '1', '0');
INSERT INTO `sys_moudle` VALUES ('136', '节点管理', 'sysCluster/list', NULL, '<i class=\"icon-code-fork icon-large\"></i>', '62', '1', '0');
INSERT INTO `sys_moudle` VALUES ('138', '修改配置', 'sysConfigData/edit', 'sysConfigData/save', null, '101', '1', '0');
INSERT INTO `sys_moudle` VALUES ('139', '清空配置', null, 'sysConfigData/delete', null, '101', '1', '0');
INSERT INTO `sys_moudle` VALUES ('140', '站点配置管理', 'sysConfig/list', null, '<i class=\"icon-cogs icon-large\"></i>', '62', '1', '0');
INSERT INTO `sys_moudle` VALUES ('142', '保存配置', null, 'sysConfig/save', null, '140', '1', '0');
INSERT INTO `sys_moudle` VALUES ('143', '修改配置', 'sysConfig/add', null, null, '140', '1', '0');
INSERT INTO `sys_moudle` VALUES ('144', '删除配置', null, 'sysConfig/delete', null, '140', '1', '0');
INSERT INTO `sys_moudle` VALUES ('145', '保存', NULL, 'cmsLottery/save', NULL, '1007', '1', '0');
INSERT INTO `sys_moudle` VALUES ('146', '删除', NULL, 'cmsLottery/delete', NULL, '1007', '1', '0');
INSERT INTO `sys_moudle` VALUES ('147', '抽奖用户管理', 'cmsLotteryUser/list', 'sysUser/lookup', '<i class=\"icon-smile icon-large\"></i>', '109', '1', '0');
INSERT INTO `sys_moudle` VALUES ('148', '删除', NULL, 'cmsLotteryUser/delete', NULL, '1011', '1', '0');
INSERT INTO `sys_moudle` VALUES ('149', '投票管理', 'cmsVote/list', NULL, '<i class=\"icon-hand-right icon-large\"></i>', '109', '1', '0');
INSERT INTO `sys_moudle` VALUES ('150', '增加/修改', 'cmsVote/add', NULL, NULL, '1013', '1', '0');
INSERT INTO `sys_moudle` VALUES ('151', '保存', NULL, 'cmsVote/save', NULL, '1013', '1', '0');
INSERT INTO `sys_moudle` VALUES ('152', '删除', NULL, 'cmsVote/delete', NULL, '1013', '1', '0');
INSERT INTO `sys_moudle` VALUES ('153', '查看', 'cmsVote/view', NULL, NULL, '1013', '1', '0');
INSERT INTO `sys_moudle` VALUES ('154', '投票用户', 'cmsVoteUser/list', 'sysUser/lookup', NULL, '1013', '1', '0');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` int(11) NOT NULL auto_increment,
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `owns_all_right` tinyint(1) NOT NULL COMMENT '拥有全部权限',
  `show_all_moudle` tinyint(1) NOT NULL COMMENT '显示全部模块',
  PRIMARY KEY  (`id`),
  KEY `site_id` (`site_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='角色';

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
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='角色授权地址';

-- ----------------------------
-- Records of sys_role_authorized
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role_moudle
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_moudle`;
CREATE TABLE `sys_role_moudle` (
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `moudle_id` int(11) NOT NULL COMMENT '模块ID',
  PRIMARY KEY  (`role_id`,`moudle_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='角色授权模块';

-- ----------------------------
-- Records of sys_role_moudle
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_user`;
CREATE TABLE `sys_role_user` (
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  PRIMARY KEY  (`role_id`,`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='用户角色';

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
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(50) NOT NULL,
  `use_static` tinyint(1) NOT NULL COMMENT '启用静态化',
  `site_path` varchar(255) NOT NULL COMMENT '站点地址',
  `use_ssi` tinyint(1) NOT NULL COMMENT '启用服务器端包含',
  `dynamic_path` varchar(255) NOT NULL COMMENT '动态站点地址',
  `disabled` tinyint(1) NOT NULL COMMENT '禁用',
  PRIMARY KEY  (`id`),
  KEY `disabled` (`disabled`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='站点';

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
  `site_id` int(11) NOT NULL COMMENT '站点ID',
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
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='任务计划';

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
  `site_id` int(11) NOT NULL COMMENT '站点ID',
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
  `last_login_ip` varchar(20) default NULL COMMENT '最后登录ip',
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', '1', 'admin', '21232f297a57a5a743894a0e4a801fc3', '管理员', '1', '1', 'master@sanluan.com', '0', '1', '0', '2017-01-01 00:00:00', '127.0.0.1', '0', '2017-01-01 00:00:00');
INSERT INTO `sys_user` VALUES ('2', '2', 'admin', '21232f297a57a5a743894a0e4a801fc3', 'admin', '2', '3', '', '0', '1', '0', '2017-01-01 00:00:00', '127.0.0.1', '0', '2017-01-01 00:00:00');


-- ----------------------------
-- Table structure for sys_user_token
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_token`;
CREATE TABLE `sys_user_token` (
  `auth_token` varchar(40) NOT NULL COMMENT '登陆授权',
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `channel` varchar(50) NOT NULL COMMENT '渠道',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `login_ip` varchar(20) NOT NULL COMMENT '登陆IP',
  PRIMARY KEY  (`auth_token`),
  KEY `user_id` (`user_id`),
  KEY `create_date` (`create_date`),
  KEY `channel` (`channel`),
  KEY `site_id` (`site_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='用户令牌';

-- ----------------------------
-- Records of sys_user_token
-- ----------------------------
