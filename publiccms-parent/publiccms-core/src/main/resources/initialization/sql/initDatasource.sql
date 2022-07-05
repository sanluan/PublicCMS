-- ----------------------------
-- Table structure for cms_comment
-- ----------------------------
DROP TABLE IF EXISTS `cms_comment`;
CREATE TABLE `cms_comment` (
  `id` bigint(20) NOT NULL,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `user_id` bigint(20) NOT NULL COMMENT '用户',
  `reply_id` bigint(20) DEFAULT NULL COMMENT '回复评论',
  `reply_user_id` bigint(20) DEFAULT NULL COMMENT '回复用户',
  `replies` int(11) NOT NULL default 0 COMMENT '回复数',
  `scores` int(11) NOT NULL COMMENT '分数',
  `content_id` bigint(20) NOT NULL COMMENT '文章内容',
  `check_user_id` bigint(20) DEFAULT NULL COMMENT '审核用户',
  `check_date` datetime DEFAULT NULL COMMENT '审核日期',
  `update_date` datetime DEFAULT NULL COMMENT '更新日期',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `status` int(11) NOT NULL COMMENT '状态：1、已发布 2、待审核',
  `disabled` tinyint(1) NOT NULL COMMENT '已禁用',
  `text` text COMMENT '内容',
  PRIMARY KEY (`id`),
  KEY `cms_comment_site_id` (`site_id`,`content_id`,`status`,`disabled`),
  KEY `cms_comment_update_date` (`update_date`,`create_date`,`replies`,`scores`),
  KEY `cms_comment_reply_id` (`site_id`,`reply_user_id`,`reply_id`)
) COMMENT='评论';
-- ----------------------------
-- Table structure for cms_content
-- ----------------------------
DROP TABLE IF EXISTS `cms_content`;
CREATE TABLE `cms_content` (
  `id` bigint(20) NOT NULL,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `title` varchar(255) NOT NULL COMMENT '标题',
  `user_id` bigint(20) NOT NULL COMMENT '发表用户',
  `dept_id` int(11) default NULL COMMENT '所属部门',
  `check_user_id` bigint(20) default NULL COMMENT '审核用户',
  `category_id` int(11) NOT NULL COMMENT '分类',
  `model_id` varchar(20) NOT NULL COMMENT '模型',
  `parent_id` bigint(20) default NULL COMMENT '父内容',
  `quote_content_id` bigint(20) NULL COMMENT '引用内容',
  `copied` tinyint(1) NOT NULL COMMENT '是否转载',
  `contribute` tinyint(1) NOT NULL default 0 COMMENT '是否投稿',
  `author` varchar(50) default NULL COMMENT '作者',
  `editor` varchar(50) default NULL COMMENT '编辑',
  `only_url` tinyint(1) NOT NULL COMMENT '外链',
  `has_images` tinyint(1) NOT NULL COMMENT '拥有图片列表',
  `has_files` tinyint(1) NOT NULL COMMENT '拥有附件列表',
  `has_products` tinyint(1) NOT NULL COMMENT '拥有产品列表',
  `has_static` tinyint(1) NOT NULL COMMENT '已经静态化',
  `url` varchar(1000) default NULL COMMENT '地址',
  `description` varchar(300) default NULL COMMENT '简介',
  `tag_ids` text default NULL COMMENT '标签',
  `dictionary_values` text default NULL COMMENT '数据字典值',
  `cover` varchar(255) default NULL COMMENT '封面',
  `childs` int(11) NOT NULL COMMENT '子内容数',
  `scores` int(11) NOT NULL COMMENT '总分数',
  `score_users` int(11) NOT NULL COMMENT '评分人数',
  `score` decimal(10, 2) NOT NULL COMMENT '分数',
  `comments` int(11) NOT NULL COMMENT '评论数',
  `clicks` int(11) NOT NULL COMMENT '点击数',
  `publish_date` datetime NOT NULL COMMENT '发布日期',
  `expiry_date` datetime default NULL COMMENT '过期日期',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '更新用户',
  `check_date` datetime default NULL COMMENT '审核日期',
  `update_date` datetime default NULL COMMENT '更新日期',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `sort` int(11) NOT NULL default '0' COMMENT '顺序',
  `status` int(11) NOT NULL COMMENT '状态：0、草稿 1、已发布 2、待审核',
  `disabled` tinyint(1) NOT NULL COMMENT '是否删除',
  PRIMARY KEY  (`id`),
  KEY `cms_content_disabled` (`site_id`, `parent_id`, `disabled`, `sort`, `publish_date`)
  KEY `cms_content_status` (`site_id`,`status`,`category_id`,`disabled`,`model_id`,`parent_id`,`sort`,`publish_date`,`expiry_date`),
  KEY `cms_content_quote_content_id` (`site_id`, `quote_content_id`)
) COMMENT='内容';