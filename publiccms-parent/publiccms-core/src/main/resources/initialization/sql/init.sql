SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for cms_category
-- ----------------------------
DROP TABLE IF EXISTS `cms_category`;
CREATE TABLE `cms_category` (
  `id` int(11) NOT NULL auto_increment,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `parent_id` int(11) default NULL COMMENT '父分类',
  `type_id` varchar(20) default NULL COMMENT '分类类型',
  `child_ids` text COMMENT '所有子分类',
  `tag_type_ids` text default NULL COMMENT '标签分类',
  `code` varchar(50) NOT NULL COMMENT '编码',
  `custom_path` tinyint(1) NOT NULL default 1 COMMENT '自定义访问路径',
  `template_path` varchar(255) default NULL COMMENT '模板路径',
  `path` varchar(1000) DEFAULT NULL COMMENT '首页路径',
  `only_url` tinyint(1) NOT NULL DEFAULT 0 COMMENT '外链',
  `has_static` tinyint(1) NOT NULL DEFAULT 0 COMMENT '已经静态化',
  `url` varchar(1000) default NULL COMMENT '首页地址',
  `custom_content_path` tinyint(1) NOT NULL default 1 COMMENT '自定义内容访问路径',
  `content_path` varchar(1000) default NULL COMMENT '内容路径',
  `contain_child` tinyint(1) NOT NULL DEFAULT 1 COMMENT '包含子分类内容',
  `page_size` int(11) default NULL DEFAULT 20 COMMENT '每页数据条数',
  `allow_contribute` tinyint(1) NOT NULL DEFAULT 0 COMMENT '允许投稿',
  `sort` int(11) NOT NULL default '0' COMMENT '顺序',
  `hidden` tinyint(1) NOT NULL DEFAULT 0 COMMENT '隐藏',
  `disabled` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  `workflow_id` int(11) default NULL COMMENT '审核流程',
  `extend_id` int(11) default NULL COMMENT '扩展',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `cms_category_code` (`site_id`, `code`),
  KEY `cms_category_sort` (`sort`),
  KEY `cms_category_type_id` (`type_id`, `allow_contribute`),
  KEY `cms_category_site_id` (`site_id`, `parent_id`, `hidden`, `disabled`)
) COMMENT='分类';

-- ----------------------------
-- Table structure for cms_category_attribute
-- ----------------------------
DROP TABLE IF EXISTS `cms_category_attribute`;
CREATE TABLE `cms_category_attribute` (
  `category_id` int(11) NOT NULL COMMENT '分类',
  `title` varchar(80) default NULL COMMENT '标题',
  `keywords` varchar(100) default NULL COMMENT '关键词',
  `description` varchar(300) default NULL COMMENT '描述',
  `data` longtext COMMENT '数据JSON',
  PRIMARY KEY  (`category_id`)
) COMMENT='分类扩展';

-- ----------------------------
-- Table structure for cms_category_model
-- ----------------------------
DROP TABLE IF EXISTS `cms_category_model`;
CREATE TABLE `cms_category_model` (
  `category_id` int(11) NOT NULL COMMENT '分类',
  `model_id` varchar(20) NOT NULL COMMENT '模型编码',
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `custom_content_path` tinyint(1) NOT NULL default 0 COMMENT '自定义内容访问路径',
  `template_path` varchar(200) default NULL COMMENT '内容模板路径',
  `content_path` varchar(1000) default NULL COMMENT '内容路径',
  PRIMARY KEY  (`category_id`, `model_id`),
  KEY `cms_category_model_site_id`(`site_id`, `model_id`)
) COMMENT='分类模型';
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
  `scores` int(11) NOT NULL default 0 COMMENT '分数',
  `content_id` bigint(20) NOT NULL COMMENT '文章内容',
  `ip` varchar(130) NOT NULL COMMENT 'IP',
  `check_user_id` bigint(20) DEFAULT NULL COMMENT '审核用户',
  `check_date` datetime DEFAULT NULL COMMENT '审核日期',
  `update_date` datetime DEFAULT NULL COMMENT '更新日期',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `status` int(11) NOT NULL default 1 COMMENT '状态：1、已发布 2、待审核',
  `disabled` tinyint(1) NOT NULL default 0 COMMENT '已禁用',
  `text` text COMMENT '内容',
  PRIMARY KEY (`id`),
  KEY `cms_comment_site_id` (`site_id`, `content_id`, `status`, `disabled`),
  KEY `cms_comment_reply_id` (`site_id`, `reply_user_id`, `reply_id`),
  KEY `cms_comment_user_id` (`site_id`, `user_id`, `status`, `disabled`)
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
  `quote_content_id` bigint(20) NULL COMMENT '引用内容(当父内容不为空时为顶级内容)',
  `copied` tinyint(1) NOT NULL default 0 COMMENT '是否转载',
  `author` varchar(50) default NULL COMMENT '作者',
  `editor` varchar(50) default NULL COMMENT '编辑',
  `only_url` tinyint(1) NOT NULL default 0 COMMENT '外链',
  `has_images` tinyint(1) NOT NULL default 0 COMMENT '拥有图片列表',
  `has_files` tinyint(1) NOT NULL default 0 COMMENT '拥有附件列表',
  `has_products` tinyint(1) NOT NULL default 0 COMMENT '拥有产品列表',
  `has_static` tinyint(1) NOT NULL default 0 COMMENT '已经静态化',
  `url` varchar(1000) default NULL COMMENT '地址',
  `description` varchar(300) default NULL COMMENT '简介',
  `tag_ids` text default NULL COMMENT '标签',
  `cover` varchar(255) default NULL COMMENT '封面',
  `childs` int(11) NOT NULL default 0 COMMENT '子内容数',
  `scores` int(11) NOT NULL default 0 COMMENT '总分数',
  `score_users` int(11) NOT NULL default 0 COMMENT '评分人数',
  `score` decimal(10, 2) NOT NULL default 0 COMMENT '分数',
  `comments` int(11) NOT NULL default 0 COMMENT '评论数',
  `clicks` int(11) NOT NULL default 0 COMMENT '点击数',
  `collections` int(11) NOT NULL default 0 COMMENT '收藏数',
  `publish_date` datetime NOT NULL COMMENT '发布日期',
  `expiry_date` datetime default NULL COMMENT '过期日期',
  `check_date` datetime default NULL COMMENT '审核日期',
  `update_user_id` bigint(20) DEFAULT NULL COMMENT '更新用户',
  `update_date` datetime default NULL COMMENT '更新日期',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `sort` int(11) NOT NULL default '0' COMMENT '顺序',
  `status` int(11) NOT NULL default 1 COMMENT '状态：0、草稿 1、已发布 2、待审核',
  `disabled` tinyint(1) NOT NULL default 0 COMMENT '是否删除',
  PRIMARY KEY  (`id`),
  KEY `cms_content_parent_id` (`site_id`, `parent_id`, `disabled`, `sort`, `publish_date`),
  KEY `cms_content_disabled` (`site_id`, `disabled`, `category_id`, `model_id`),
  KEY `cms_content_status` (`site_id`, `status`, `parent_id`, `category_id`, `disabled`, `model_id`, `publish_date`, `expiry_date`, `sort`),
  KEY `cms_content_category_id` (`site_id`, `category_id`, `parent_id`, `disabled`),
  KEY `cms_content_quote_content_id` (`site_id`, `quote_content_id`)
) COMMENT='内容';
-- ----------------------------
-- Table structure for cms_content_attribute
-- ----------------------------
DROP TABLE IF EXISTS `cms_content_attribute`;
CREATE TABLE `cms_content_attribute` (
  `content_id` bigint(20) NOT NULL,
  `source` varchar(50) default NULL COMMENT '内容来源',
  `source_url` varchar(1000) default NULL COMMENT '来源地址',
  `data` longtext COMMENT '数据JSON',
  `search_text` longtext NULL COMMENT '全文索引文本',
  `dictionary_values` text NULL COMMENT '数据字典值',
  `files_text` text NULL COMMENT '附件文本',
  `min_price` decimal(10, 2) NULL COMMENT '最低价格',
  `max_price` decimal(10, 2) NULL COMMENT '最高价格',
  `text` longtext COMMENT '内容',
  `word_count` int(11) NOT NULL COMMENT '字数',
  PRIMARY KEY  (`content_id`)
) COMMENT='内容扩展';

-- ----------------------------
-- Table structure for cms_content_file
-- ----------------------------
DROP TABLE IF EXISTS `cms_content_file`;
CREATE TABLE `cms_content_file` (
  `id` bigint(20) NOT NULL auto_increment,
  `content_id` bigint(20) NOT NULL COMMENT '内容',
  `user_id` bigint(20) NOT NULL COMMENT '用户',
  `file_path` varchar(255) NOT NULL COMMENT '文件路径',
  `file_type` varchar(20) NOT NULL COMMENT '文件类型',
  `file_size` bigint(20) NULL COMMENT '文件大小',
  `width` int(11) NULL COMMENT '宽度',
  `height` int(11) NULL COMMENT '高度',
  `clicks` int(11) NOT NULL COMMENT '点击数',
  `sort` int(11) NOT NULL COMMENT '排序',
  `description` varchar(300) default NULL COMMENT '描述',
  PRIMARY KEY  (`id`),
  KEY `cms_content_file_content_id` (`content_id`, `sort`),
  KEY `cms_content_file_file_type`(`file_type`),
  KEY `cms_content_file_file_size` (`file_size`),
  KEY `cms_content_file_clicks` (`clicks`)
) COMMENT='内容附件';
-- ----------------------------
-- Table structure for cms_content_product
-- ----------------------------
DROP TABLE IF EXISTS `cms_content_product`;
CREATE TABLE `cms_content_product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `content_id` bigint(20) NOT NULL COMMENT '内容',
  `user_id` bigint(20) NOT NULL COMMENT '用户',
  `cover` varchar(255) DEFAULT NULL COMMENT '封面图',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `price` decimal(10,2) NOT NULL COMMENT '价格',
  `min_quantity` int(11) DEFAULT NULL COMMENT '最小购买数量',
  `max_quantity` int(11) DEFAULT NULL COMMENT '最大购买数量',
  `inventory` int(11) NOT NULL COMMENT '库存',
  `sales` int(11) NOT NULL COMMENT '销量',
  PRIMARY KEY (`id`),
  KEY `cms_content_product_content_id` (`site_id`, `content_id`),
  KEY `cms_content_product_user_id` (`site_id`, `user_id`),
  KEY `cms_content_product_sales` (`site_id`, `sales`),
  KEY `cms_content_product_inventory` (`site_id`, `inventory`),
  KEY `cms_content_product_price` (`site_id`, `price`)
) COMMENT='内容商品';
-- ----------------------------
-- Table structure for cms_content_related
-- ----------------------------
DROP TABLE IF EXISTS `cms_content_related`;
CREATE TABLE `cms_content_related` (
  `id` bigint(20) NOT NULL auto_increment,
  `content_id` bigint(20) NOT NULL COMMENT '内容',
  `relation_type` varchar(20) DEFAULT NULL COMMENT '关系类型',
  `relation` varchar(50) DEFAULT NULL COMMENT '关系',
  `related_content_id` bigint(20) default NULL COMMENT '推荐内容',
  `user_id` bigint(20) NOT NULL COMMENT '推荐用户',
  `url` varchar(1000) default NULL COMMENT '推荐链接地址',
  `title` varchar(255) default NULL COMMENT '推荐标题',
  `description` varchar(300) default NULL COMMENT '推荐简介',
  `sort` int(11) NOT NULL COMMENT '排序',
  PRIMARY KEY  (`id`),
  KEY `cms_content_related_content_id`(`content_id`, `relation_type`, `relation`,  `sort`),
  KEY `cms_content_related_related_content_id` (`related_content_id`,`relation_type`, `relation` )
) COMMENT='推荐推荐';
-- ----------------------------
-- Table structure for cms_dictionary
-- ----------------------------
DROP TABLE IF EXISTS `cms_dictionary`;
CREATE TABLE `cms_dictionary` (
  `id` varchar(20) NOT NULL,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `child_depth` int(10) NOT NULL COMMENT '子级深度',
  PRIMARY KEY (`id`, `site_id`),
  KEY `cms_dictionary_site_id` (`site_id`)
) COMMENT='字典';

-- ----------------------------
-- Table structure for cms_dictionary_data
-- ----------------------------
DROP TABLE IF EXISTS `cms_dictionary_data`;
CREATE TABLE `cms_dictionary_data` (
  `dictionary_id` varchar(20) NOT NULL COMMENT '字典',
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `parent_value` varchar(50) NULL COMMENT '父值',
  `value` varchar(50) NOT NULL COMMENT '值',
  `text` varchar(100) NOT NULL COMMENT '文字',
  `sort` int(11) NOT NULL default '0' COMMENT '顺序',
  PRIMARY KEY  (`dictionary_id`, `site_id`, `value`),
  KEY `cms_dictionary_parent_value`(`dictionary_id`, `site_id`, `parent_value`)
) COMMENT='字典数据';
-- ----------------------------
-- Table structure for cms_dictionary_exclude
-- ----------------------------
DROP TABLE IF EXISTS `cms_dictionary_exclude`;
CREATE TABLE `cms_dictionary_exclude` (
  `dictionary_id` varchar(20) NOT NULL COMMENT '字典',
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `exclude_dictionary_id` varchar(20) NOT NULL COMMENT '排除的数据字典',
  PRIMARY KEY (`dictionary_id`, `site_id`, `exclude_dictionary_id`),
  KEY `cms_dictionary_exclude_dictionary_id` (`dictionary_id`, `site_id`)
) COMMENT='字典数据排除规则';
-- ----------------------------
-- Table structure for cms_dictionary_exclude_value
-- ----------------------------
DROP TABLE IF EXISTS `cms_dictionary_exclude_value`;
CREATE TABLE `cms_dictionary_exclude_value` (
  `dictionary_id` varchar(20) NOT NULL COMMENT '字典',
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `exclude_dictionary_id` varchar(20) NOT NULL COMMENT '排除的数据字典',
  `value` varchar(50) NOT NULL COMMENT '值',
  `exclude_values` text default NULL COMMENT '排除的值',
  PRIMARY KEY (`dictionary_id`, `site_id`, `exclude_dictionary_id`, `value`),
  KEY `cms_dictionary_exclude_value_dictionary_id` (`dictionary_id`, `site_id`)
)COMMENT='字典数据排除规则值';
-- ----------------------------
-- Table structure for cms_editor_history
-- ----------------------------
DROP TABLE IF EXISTS `cms_editor_history`;
CREATE TABLE `cms_editor_history` (
  `id` bigint(20) NOT NULL auto_increment,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `item_type` varchar(50) NOT NULL COMMENT '数据类型',
  `item_id` varchar(100) NOT NULL COMMENT '数据id',
  `field_name` varchar(50) NOT NULL COMMENT '字段名',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `user_id` bigint(20) NOT NULL COMMENT '修改用户',
  `text` longtext NOT NULL COMMENT '文本',
  PRIMARY KEY (`id`),
  KEY `cms_editor_history_item_id` (`site_id`, `item_type`, `item_id`, `field_name`, `create_date`)
) COMMENT='内容扩展';
-- ----------------------------
-- Table structure for cms_place
-- ----------------------------
DROP TABLE IF EXISTS `cms_place`;
CREATE TABLE `cms_place` (
  `id` bigint(20) NOT NULL auto_increment,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `path` varchar(100) NOT NULL COMMENT '模板路径',
  `user_id` bigint(20) default NULL COMMENT '提交用户',
  `check_user_id` bigint(20) default NULL COMMENT '审核用户',
  `item_type` varchar(50) default NULL COMMENT '推荐项目类型',
  `item_id` bigint(20) default NULL COMMENT '推荐项目',
  `title` varchar(255) NOT NULL COMMENT '标题',
  `url` varchar(1000) default NULL COMMENT '超链接',
  `description` varchar(300) default NULL COMMENT '简介',
  `cover` varchar(255) default NULL COMMENT '封面图',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `publish_date` datetime NOT NULL COMMENT '发布日期',
  `expiry_date` datetime default NULL COMMENT '过期日期',
  `status` int(11) NOT NULL COMMENT '状态：0、草稿 1、已发布 2、待审核 3、已下架',
  `clicks` int(11) NOT NULL COMMENT '点击数',
  `max_clicks` int(11) NOT NULL COMMENT '最大点击数',
  `disabled` tinyint(1) NOT NULL COMMENT '已禁用',
  PRIMARY KEY  (`id`),
  KEY `cms_place_clicks` (`clicks`),
  KEY `cms_place_site_id` (`site_id`, `path`, `status`, `disabled`),
  KEY `cms_place_item_type` (`item_type`, `item_id`),
  KEY `cms_place_user_id` (`user_id`, `check_user_id`),
  KEY `cms_place_publish_date` (`publish_date`, `create_date`, `expiry_date`)
) COMMENT='推荐位数据';

-- ----------------------------
-- Table structure for cms_place_attribute
-- ----------------------------
DROP TABLE IF EXISTS `cms_place_attribute`;
CREATE TABLE `cms_place_attribute` (
  `place_id` bigint(20) NOT NULL COMMENT '位置',
  `data` longtext COMMENT '数据JSON',
  PRIMARY KEY  (`place_id`)
) COMMENT='推荐位数据扩展';

-- ----------------------------
-- Table structure for cms_survey
-- ----------------------------
DROP TABLE IF EXISTS `cms_survey`;
CREATE TABLE `cms_survey` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `user_id` bigint(20) NOT NULL COMMENT '用户',
  `survey_type` varchar(20) NOT NULL COMMENT '问卷类型',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `description` varchar(300) NOT NULL COMMENT '描述',
  `votes` int(11) NOT NULL COMMENT '投票数',
  `start_date` datetime NOT NULL COMMENT '开始日期',
  `end_date` datetime DEFAULT NULL COMMENT '结束日期',
  `allow_anonymous` tinyint(1) NOT NULL COMMENT '允许匿名',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `disabled` tinyint(1) NOT NULL COMMENT '是否禁用',
  PRIMARY KEY (`id`),
  KEY `cms_survey_site_id` (`site_id`, `survey_type`, `start_date`, `disabled`, `create_date`),
  KEY `cms_survey_user_id` (`user_id`, `votes`)
) COMMENT='问卷调查';


-- ----------------------------
-- Table structure for cms_survey_question
-- ----------------------------
DROP TABLE IF EXISTS `cms_survey_question`;
CREATE TABLE `cms_survey_question` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `survey_id` bigint(20) NOT NULL COMMENT '问卷',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `score` int(11) DEFAULT NULL COMMENT '分数',
  `question_type` varchar(20) NOT NULL COMMENT '问题类型',
  `cover` varchar(255) NOT NULL COMMENT '图片',
  `answer` varchar(255) DEFAULT NULL COMMENT '答案',
  `sort` int(11) NOT NULL COMMENT '排序',
  PRIMARY KEY (`id`),
  KEY `cms_survey_question_survey_id` (`survey_id`, `sort`),
  KEY `cms_survey_question_question_type` (`survey_id`, `question_type`, `sort`)
) COMMENT='问卷调查问题';

-- ----------------------------
-- Table structure for cms_survey_question_item
-- ----------------------------
DROP TABLE IF EXISTS `cms_survey_question_item`;
CREATE TABLE `cms_survey_question_item` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `question_id` bigint(20) NOT NULL COMMENT '问题',
  `votes` int(11) NOT NULL COMMENT '投票数',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `sort` int(11) NOT NULL COMMENT '排序',
  PRIMARY KEY (`id`),
  KEY `cms_survey_question_item_question_id` (`question_id`, `sort`),
  KEY `cms_survey_question_item_votes` (`question_id`, `votes`)
) COMMENT='问卷调查选项';
-- ----------------------------
-- Table structure for cms_tag
-- ----------------------------
DROP TABLE IF EXISTS `cms_tag`;
CREATE TABLE `cms_tag` (
  `id` bigint(20) NOT NULL auto_increment,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `type_id` int(11) default NULL COMMENT '分类',
  `search_count` int(11) NOT NULL COMMENT '搜索次数',
  PRIMARY KEY  (`id`),
  KEY `cms_tag_site_id` (`site_id`, `name`),
  KEY `cms_tag_type_id` (`type_id`)
) COMMENT='标签';

-- ----------------------------
-- Table structure for cms_tag_type
-- ----------------------------
DROP TABLE IF EXISTS `cms_tag_type`;
CREATE TABLE `cms_tag_type` (
  `id` int(11) NOT NULL auto_increment,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `count` int(11) NOT NULL COMMENT '标签数',
  PRIMARY KEY  (`id`),
  KEY `cms_tag_type_site_id` (`site_id`, `name`)
) COMMENT='标签类型';

-- ----------------------------
-- Table structure for cms_user_collection
-- ----------------------------
DROP TABLE IF EXISTS `cms_user_collection`;
CREATE TABLE `cms_user_collection`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户',
  `content_id` bigint(20) NOT NULL COMMENT '内容',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY (`user_id`, `content_id`),
  KEY `cms_user_collection_user_id`(`user_id`, `create_date`)
) COMMENT = '用户收藏表';

-- ----------------------------
-- Table structure for cms_user_score
-- ----------------------------
DROP TABLE IF EXISTS `cms_user_score`;
CREATE TABLE `cms_user_score`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户',
  `item_type` varchar(50) NOT NULL COMMENT '类型',
  `item_id` bigint(20) NOT NULL COMMENT '项目',
  `score` int(11) NOT NULL COMMENT '分数',
  `ip` varchar(130) NOT NULL COMMENT 'IP',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY (`user_id`, `item_type`, `item_id`),
  KEY `cms_user_score_item_type`(`item_type`, `item_id`, `create_date`),
  KEY `cms_user_score_user_id`(`user_id`, `item_type`, `create_date`)
) COMMENT = '用户评分表';

-- ----------------------------
-- Table structure for cms_user_survey
-- ----------------------------
DROP TABLE IF EXISTS `cms_user_survey`;
CREATE TABLE `cms_user_survey` (
  `user_id` bigint(20) NOT NULL COMMENT '用户',
  `survey_id` bigint(20) NOT NULL COMMENT '问卷',
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `anonymous` tinyint(1) NOT NULL COMMENT '匿名',
  `score` int(11) DEFAULT NULL COMMENT '分数',
  `ip` varchar(130) NOT NULL COMMENT 'IP',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY (`user_id`, `survey_id`),
  KEY `cms_user_survey_site_id` (`site_id`, `score`, `create_date`)
) COMMENT='用户问卷';

-- ----------------------------
-- Table structure for cms_user_survey_question
-- ----------------------------
DROP TABLE IF EXISTS `cms_user_survey_question`;
CREATE TABLE `cms_user_survey_question` (
  `user_id` bigint(20) NOT NULL COMMENT '用户',
  `question_id` bigint(20) NOT NULL COMMENT '问题',
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `survey_id` bigint(20) NOT NULL COMMENT '问卷',
  `answer` varchar(255) DEFAULT NULL COMMENT '答案',
  `score` int(11) DEFAULT NULL COMMENT '分数',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY (`user_id`, `question_id`),
  KEY `cms_user_survey_question_site_id` (`site_id`, `survey_id`, `create_date`)
) COMMENT='用户问卷答案';

-- ----------------------------
-- Table structure for cms_user_vote
-- ----------------------------
DROP TABLE IF EXISTS `cms_user_vote`;
CREATE TABLE `cms_user_vote`  (
  `vote_id` bigint(20) NOT NULL COMMENT '投票',
  `user_id` bigint(20) NOT NULL COMMENT '用户',
  `anonymous` tinyint(1) NOT NULL COMMENT '匿名',
  `item_id` bigint(20) NOT NULL COMMENT '投票选项',
  `ip` varchar(130) NOT NULL COMMENT 'IP',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY (`user_id`, `vote_id`),
  KEY `cms_user_vote_vote_id`(`vote_id`, `ip`, `create_date`)
) COMMENT='投票用户';

-- ----------------------------
-- Table structure for cms_vote
-- ----------------------------
DROP TABLE IF EXISTS `cms_vote`;
CREATE TABLE `cms_vote`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `start_date` datetime NOT NULL COMMENT '开始日期',
  `end_date` datetime NULL COMMENT '结束日期',
  `votes` int(11) NOT NULL COMMENT '总票数',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `description` varchar(300) NULL DEFAULT NULL COMMENT '描述',
  `allow_anonymous` tinyint(1) NOT NULL COMMENT '允许匿名',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `disabled` tinyint(1) NOT NULL COMMENT '已禁用',
  PRIMARY KEY (`id`),
  KEY `cms_vote_site_id`(`site_id`, `start_date`, `disabled`)
)  COMMENT='投票';

-- ----------------------------
-- Table structure for cms_vote_item
-- ----------------------------
DROP TABLE IF EXISTS `cms_vote_item`;
CREATE TABLE `cms_vote_item`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `vote_id` bigint(20) NOT NULL COMMENT '投票',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `votes` int(11) NOT NULL COMMENT '票数',
  `sort` int(11) NOT NULL COMMENT '顺序',
  PRIMARY KEY (`id`),
  KEY `cms_vote_item_vote_id`(`vote_id`, `sort`)
)  COMMENT='投票选型';

-- ----------------------------
-- Table structure for cms_word
-- ----------------------------
DROP TABLE IF EXISTS `cms_word`;
CREATE TABLE `cms_word` (
  `id` bigint(20) NOT NULL auto_increment,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `search_count` int(11) NOT NULL COMMENT '搜索次数',
  `hidden` tinyint(1) NOT NULL COMMENT '隐藏',
  `ip` varchar(130) NOT NULL COMMENT 'IP',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `cms_word_name` (`site_id`, `name`),
  KEY `cms_word_hidden` (`site_id`, `hidden`)
) COMMENT='搜索词';

-- ----------------------------
-- Table structure for log_login
-- ----------------------------
DROP TABLE IF EXISTS `log_login`;
CREATE TABLE `log_login` (
  `id` bigint(20) NOT NULL auto_increment,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `name` varchar(50) NOT NULL COMMENT '用户名',
  `user_id` bigint(20) default NULL COMMENT '用户',
  `ip` varchar(130) NOT NULL COMMENT 'IP',
  `channel` varchar(50) NOT NULL COMMENT '登录渠道',
  `result` tinyint(1) NOT NULL COMMENT '结果',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `error_password` varchar(255) default NULL COMMENT '错误密码',
  PRIMARY KEY  (`id`),
  KEY `log_login_result` (`site_id`, `result`, `create_date`),
  KEY `log_login_user_id` (`site_id`, `user_id`, `create_date`),
  KEY `log_login_ip` (`site_id`, `ip`, `create_date`),
  KEY `log_login_channel` (`site_id`, `channel`, `create_date`)
) COMMENT='登录日志';

-- ----------------------------
-- Table structure for log_operate
-- ----------------------------
DROP TABLE IF EXISTS `log_operate`;
CREATE TABLE `log_operate` (
  `id` bigint(20) NOT NULL auto_increment,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `user_id` bigint(20) default NULL COMMENT '用户',
  `dept_id` int(11) default NULL COMMENT '部门',
  `channel` varchar(50) NOT NULL COMMENT '操作渠道',
  `operate` varchar(40) NOT NULL COMMENT '操作',
  `ip` varchar(130) default NULL COMMENT 'IP',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `content` longtext default NULL COMMENT '内容',
  PRIMARY KEY  (`id`),
  KEY `log_operate_user_id` (`site_id`, `user_id`, `dept_id`),
  KEY `log_operate_operate` (`site_id`, `operate`, `create_date`),
  KEY `log_operate_ip` (`site_id`, `ip`, `create_date`),
  KEY `log_operate_channel` (`site_id`, `channel`, `create_date`)
) COMMENT='操作日志';

-- ----------------------------
-- Table structure for log_task
-- ----------------------------
DROP TABLE IF EXISTS `log_task`;
CREATE TABLE `log_task` (
  `id` bigint(20) NOT NULL auto_increment,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `task_id` int(11) NOT NULL COMMENT '任务',
  `begintime` datetime NOT NULL COMMENT '开始时间',
  `endtime` datetime default NULL COMMENT '结束时间',
  `success` tinyint(1) NOT NULL COMMENT '执行成功',
  `result` longtext COMMENT '执行结果',
  PRIMARY KEY  (`id`),
  KEY `log_task_task_id` (`site_id`, `task_id`, `begintime`),
  KEY `log_task_success` (`site_id`, `success`, `begintime`)
) COMMENT='任务计划日志';

-- ----------------------------
-- Table structure for log_upload
-- ----------------------------
DROP TABLE IF EXISTS `log_upload`;
CREATE TABLE `log_upload` (
  `id` bigint(20) NOT NULL auto_increment,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `user_id` bigint(20) NOT NULL COMMENT '用户',
  `channel` varchar(50) NOT NULL COMMENT '操作渠道',
  `original_name` varchar(255) DEFAULT NULL COMMENT '原文件名',
  `privatefile` tinyint(1) NOT NULL DEFAULT 0 COMMENT '私有文件',
  `file_type` varchar(20) NOT NULL COMMENT '文件类型',
  `file_size` bigint(20) NULL COMMENT '文件大小',
  `width` int(11) NULL COMMENT '宽度',
  `height` int(11) NULL COMMENT '高度',
  `ip` varchar(130) default NULL COMMENT 'IP',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `file_path` varchar(500) NOT NULL COMMENT '文件路径',
  PRIMARY KEY  (`id`),
  KEY `log_upload_user_id` (`site_id`, `user_id`, `create_date`),
  KEY `log_upload_ip` (`site_id`, `ip`, `create_date`),
  KEY `log_upload_channel` (`site_id`, `channel`, `create_date`),
  KEY `log_upload_file_type` (`site_id`, `privatefile`, `file_type`, `file_size`)
) COMMENT='上传日志';
-- ----------------------------
-- Table structure for sys_app
-- ----------------------------
DROP TABLE IF EXISTS `sys_app`;
CREATE TABLE `sys_app` (
  `id` int(11) NOT NULL auto_increment,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `channel` varchar(50) NOT NULL COMMENT '渠道',
  `app_key` varchar(50) NOT NULL COMMENT 'APP key',
  `app_secret` varchar(50) NOT NULL COMMENT 'APP secret',
  `authorized_apis`  text NULL COMMENT '授权API',
  `expiry_minutes` int(11) DEFAULT NULL COMMENT '过期时间',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `sys_app_key` (`app_key`),
  KEY `sys_app_site_id` (`site_id`)
) COMMENT='应用';

-- ----------------------------
-- Table structure for sys_app_client
-- ----------------------------
DROP TABLE IF EXISTS `sys_app_client`;
CREATE TABLE `sys_app_client` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `channel` varchar(20) NOT NULL COMMENT '渠道',
  `uuid` varchar(50) NOT NULL COMMENT '唯一标识',
  `user_id` bigint(20) DEFAULT NULL COMMENT '绑定用户',
  `client_version` varchar(50) DEFAULT NULL COMMENT '版本',
  `last_login_date` datetime DEFAULT NULL COMMENT '上次登录时间',
  `last_login_ip` varchar(130) DEFAULT NULL COMMENT '上次登录IP',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `disabled` tinyint(1) NOT NULL COMMENT '是否禁用',
  PRIMARY KEY (`id`),
  UNIQUE KEY `sys_app_client_site_id` (`site_id`, `channel`, `uuid`),
  KEY `sys_app_client_user_id` (`user_id`, `disabled`, `create_date`),
  KEY `sys_app_client_disabled`(`site_id`, `disabled`, `create_date`)
) COMMENT='应用客户端';

-- ----------------------------
-- Table structure for sys_app_token
-- ----------------------------
DROP TABLE IF EXISTS `sys_app_token`;
CREATE TABLE `sys_app_token` (
  `auth_token` varchar(40) NOT NULL COMMENT '授权验证',
  `app_id` int(11) NOT NULL COMMENT '应用',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `expiry_date` datetime DEFAULT NULL COMMENT '过期日期',
  PRIMARY KEY  (`auth_token`),
  KEY `sys_app_token_app_id`(`app_id`, `create_date`),
  KEY `sys_app_token_expiry_date`(`expiry_date`)
) COMMENT='应用授权';

-- ----------------------------
-- Table structure for sys_cluster
-- ----------------------------
DROP TABLE IF EXISTS `sys_cluster`;
CREATE TABLE `sys_cluster` (
  `uuid` varchar(40) NOT NULL COMMENT 'uuid',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `heartbeat_date` datetime NOT NULL COMMENT '心跳时间',
  `master` tinyint(1) NOT NULL COMMENT '是否管理',
  `cms_version` varchar(20) default NULL COMMENT '版本',
  `revision` varchar(20) NULL COMMENT '修订',
  PRIMARY KEY  (`uuid`),
  KEY `sys_cluster_create_date` (`create_date`),
  KEY `sys_cluster_heartbeat_date` (`heartbeat_date`, `master`)
) COMMENT='服务器集群';

-- ----------------------------
-- Table structure for sys_config_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_config_data`;
CREATE TABLE `sys_config_data` (
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `code` varchar(50) NOT NULL COMMENT '配置项编码',
  `data` longtext NOT NULL COMMENT '值',
  PRIMARY KEY  (`site_id`, `code`)
) COMMENT='站点配置';
-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `id` int(11) NOT NULL auto_increment,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `code` varchar(50) NOT NULL COMMENT '编码',
  `parent_id` int(11) default NULL COMMENT '父部门',
  `description` varchar(300) default NULL COMMENT '描述',
  `user_id` bigint(20) default NULL COMMENT '负责人',
  `max_sort` INT NOT NULL DEFAULT 1000 COMMENT  '最大内容置顶级别',
  `owns_all_category` tinyint(1) NOT NULL COMMENT '拥有全部分类权限',
  `owns_all_page` tinyint(1) NOT NULL COMMENT '拥有全部页面权限',
  `owns_all_config` tinyint(1) NOT NULL DEFAULT '1' COMMENT '拥有全部配置权限',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `sys_dept_code`(`site_id`, `code`),
  KEY `sys_dept_site_id` (`site_id`)
) AUTO_INCREMENT=2 COMMENT='部门';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES ('1', '1', 'Technical department', '1', null, '', '1', '1000', '1', '1', '1');

-- ----------------------------
-- Table structure for sys_dept_item
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept_item`;
CREATE TABLE `sys_dept_item` (
  `dept_id` int(11) NOT NULL COMMENT '部门',
  `item_type` varchar(50) NOT NULL DEFAULT 'page' COMMENT '项目类型' ,
  `item_id` varchar(100) NOT NULL COMMENT '项目',
  PRIMARY KEY  (`dept_id`, `item_type`, `item_id`),
  KEY `sys_dept_item_item_id` (`item_type`, `item_id`)
) COMMENT='部门数据项';


-- ----------------------------
-- Table structure for sys_domain
-- ----------------------------
DROP TABLE IF EXISTS `sys_domain`;
CREATE TABLE `sys_domain` (
  `name` varchar(100) NOT NULL COMMENT '域名',
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `wild` tinyint(1) NOT NULL COMMENT '通配域名',
  `multiple` tinyint(1) NOT NULL COMMENT '站点群',
  `path` varchar(100) default NULL COMMENT '路径',
  PRIMARY KEY  (`name`),
  KEY `sys_domain_site_id` (`site_id`)
) COMMENT='域名';

-- ----------------------------
-- Records of sys_domain
-- ----------------------------
INSERT INTO `sys_domain` VALUES ('dev.publiccms.com', '1', '1', '0', '');
INSERT INTO `sys_domain` VALUES ('localhost', '1', '1', '0', '');


-- ----------------------------
-- Table structure for sys_email_token
-- ----------------------------
DROP TABLE IF EXISTS `sys_email_token`;
CREATE TABLE `sys_email_token` (
  `auth_token` varchar(40) NOT NULL COMMENT '验证码',
  `user_id` bigint(20) NOT NULL COMMENT '用户',
  `email` varchar(100) NOT NULL COMMENT '邮件地址',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `expiry_date` datetime NOT NULL COMMENT '过期日期',
  PRIMARY KEY  (`auth_token`),
  KEY `sys_email_token_expiry_date`(`expiry_date`),
  KEY `sys_email_token_user_id`(`user_id`, `create_date`)
) COMMENT='邮件地址验证日志';

-- ----------------------------
-- Table structure for sys_extend
-- ----------------------------
DROP TABLE IF EXISTS `sys_extend`;
CREATE TABLE `sys_extend` (
  `id` int(11) NOT NULL auto_increment,
  `item_type` varchar(20) NOT NULL COMMENT '扩展类型',
  `item_id` int(11) NOT NULL COMMENT '扩展项目',
  PRIMARY KEY  (`id`)
) COMMENT='扩展';

-- ----------------------------
-- Table structure for sys_extend_field
-- ----------------------------
DROP TABLE IF EXISTS `sys_extend_field`;
CREATE TABLE `sys_extend_field` (
  `extend_id` int(11) NOT NULL COMMENT '扩展',
  `code` varchar(20) NOT NULL COMMENT '编码',
  `required` tinyint(1) NOT NULL COMMENT '是否必填',
  `searchable` tinyint(1) NOT NULL COMMENT '是否可搜索',
  `sortable` varchar(20) DEFAULT NULL  COMMENT '排序字段',
  `maxlength` int(11) default NULL COMMENT '最大长度',
  `width` int(11) default NULL COMMENT '高度',
  `height` int(11) default NULL COMMENT '宽度',
  `name` varchar(20) NOT NULL COMMENT '名称',
  `description` varchar(100) default NULL COMMENT '解释',
  `input_type` varchar(20) NOT NULL COMMENT '表单类型',
  `default_value` varchar(50) default NULL COMMENT '默认值',
  `dictionary_id` varchar(20) default NULL COMMENT '数据字典',
  `multiple` tinyint(1) NOT NULL COMMENT '多选',
  `sort` int(11) NOT NULL default '0' COMMENT '顺序',
  PRIMARY KEY  (`extend_id`, `code`),
  KEY `sys_extend_field_input_type` (`extend_id`, `input_type`, `searchable`),
  KEY `sys_extend_field_sort` (`sort`)
) COMMENT='扩展字段';
-- ----------------------------
-- Table structure for sys_lock
-- ----------------------------
DROP TABLE IF EXISTS `sys_lock`;
CREATE TABLE `sys_lock` (
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `item_type` varchar(50) NOT NULL COMMENT '类型',
  `item_id` varchar(130) NOT NULL COMMENT '项目',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户',
  `count` int(11) NOT NULL COMMENT '锁定次数',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY (`site_id`, `item_type`, `item_id`),
  KEY `sys_lock_item_type` (`site_id`, `item_type`, `create_date`)
) COMMENT='锁';
-- ----------------------------
-- Table structure for sys_module
-- ----------------------------
DROP TABLE IF EXISTS `sys_module`;
CREATE TABLE `sys_module` (
  `id` varchar(30) NOT NULL,
  `url` varchar(255) default NULL COMMENT '链接地址',
  `authorized_url` text COMMENT '授权地址',
  `attached` varchar(50) default NULL COMMENT '标题附加',
  `parent_id` varchar(30) default NULL COMMENT '父模块',
  `menu` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否菜单',
  `sort` int(11) NOT NULL COMMENT '排序',
  PRIMARY KEY  (`id`),
  KEY `sys_module_parent_id` (`parent_id`, `menu`),
  KEY `sys_module_sort` (`sort`)
) COMMENT='模块';

-- ----------------------------
-- Records of sys_module
-- ----------------------------
INSERT INTO `sys_module` VALUES ('account_add', 'tradeAccount/add', 'tradeAccount/save', NULL, 'account_list', 0, 1);
INSERT INTO `sys_module` VALUES ('account_history_list', 'tradeAccountHistory/list', NULL, 'icon-book', 'trade', 1, 7);
INSERT INTO `sys_module` VALUES ('account_list', 'tradeAccount/list', NULL, 'icon-credit-card', 'trade', 1, 6);
INSERT INTO `sys_module` VALUES ('account_recharge', 'tradeAccount/rechargeParameters', 'tradeAccount/recharge', NULL, 'account_list', 0, 2);
INSERT INTO `sys_module` VALUES ('app_add', 'sysApp/add', 'sysApp/save', NULL, 'app_list', 0, 0);
INSERT INTO `sys_module` VALUES ('app_client_disable', NULL, 'sysAppClient/disable', NULL, 'app_client_list', 0, 0);
INSERT INTO `sys_module` VALUES ('app_client_enable', NULL, 'sysAppClient/enable', NULL, 'app_client_list', 0, 0);
INSERT INTO `sys_module` VALUES ('app_client_list', 'sysAppClient/list', NULL, 'icon-coffee', 'user', 1, 4);
INSERT INTO `sys_module` VALUES ('app_delete', NULL, 'sysApp/delete', NULL, 'app_list', 0, 0);
INSERT INTO `sys_module` VALUES ('app_issue', 'sysApp/issueParameters', 'sysAppToken/issue,sysAppToken/delete,sysAppToken/list', NULL, 'app_list', 0, 0);
INSERT INTO `sys_module` VALUES ('app_list', 'sysApp/list', NULL, 'icon-linux', 'system', 1, 5);
INSERT INTO `sys_module` VALUES ('app_view', 'sysApp/view', NULL, NULL, 'app_list', 0, 0);
INSERT INTO `sys_module` VALUES ('category_add', 'cmsCategory/add', 'cmsCategory/addMore,cmsCategory/virify,cmsCategory/rebuildChildIds,cmsCategory/batchPublish,cmsCategory/batchCopy,cmsCategory/batchCreate,cmsCategory/batchSave,cmsCategory/seo,cmsCategory/saveSeo,cmsCategory/categoryPath,cmsCategory/contentPath,cmsCategory/save', NULL, 'category_list', 0, 0);
INSERT INTO `sys_module` VALUES ('category_delete', NULL, 'cmsCategory/delete', NULL, 'category_list', 0, 0);
INSERT INTO `sys_module` VALUES ('category_export', NULL, 'cmsCategory/export', NULL, 'category_list', 0, 0);
INSERT INTO `sys_module` VALUES ('category_import', 'cmsCategory/import', 'cmsCategory/doImport', NULL, 'category_list', 0, 0);
INSERT INTO `sys_module` VALUES ('category_list', 'cmsCategory/list', NULL, 'icon-folder-open', 'content', 1, 4);
INSERT INTO `sys_module` VALUES ('category_move', 'cmsCategory/moveParameters', 'cmsCategory/move', NULL, 'category_list', 0, 0);
INSERT INTO `sys_module` VALUES ('category_publish', 'cmsCategory/publishParameters', 'cmsCategory/publish', NULL, 'category_list', 0, 0);
INSERT INTO `sys_module` VALUES ('category_push', 'cmsCategory/push_page', 'cmsPlace/push,cmsPlace/add,cmsPlace/save', NULL, 'category_list', 0, 0);
INSERT INTO `sys_module` VALUES ('category_type_add', 'cmsCategoryType/add', 'cmsCategoryType/save,cmsCategory/categoryPath,cmsCategory/contentPath', NULL, 'category_type_list', 0, 0);
INSERT INTO `sys_module` VALUES ('category_type_change', 'cmsCategory/changeTypeParameters', 'cmsCategory/changeType', NULL, 'category_list', 0, 0);
INSERT INTO `sys_module` VALUES ('category_type_delete', NULL, 'cmsCategoryType/delete', NULL, 'category_type_list', 0, 0);
INSERT INTO `sys_module` VALUES ('category_type_list', 'cmsCategoryType/list', 'cmsCategoryType/categoryList', 'icon-road', 'config', 1, 2);
INSERT INTO `sys_module` VALUES ('clearcache', NULL, 'clearCache', NULL, 'common', 0, 0);
INSERT INTO `sys_module` VALUES ('comment_check', NULL, 'cmsComment/check', NULL, 'comment_list', 0, 0);
INSERT INTO `sys_module` VALUES ('comment_delete', NULL, 'cmsComment/delete', NULL, 'comment_list', 0, 0);
INSERT INTO `sys_module` VALUES ('comment_edit', 'cmsComment/edit', 'cmsComment/save', NULL, 'comment_list', 0, 0);
INSERT INTO `sys_module` VALUES ('comment_list', 'cmsComment/list', NULL, 'icon-comment', 'content', 1, 3);
INSERT INTO `sys_module` VALUES ('comment_reply', 'cmsComment/reply', 'cmsComment/save', NULL, 'comment_list', 0, 0);
INSERT INTO `sys_module` VALUES ('comment_uncheck', NULL, 'cmsComment/uncheck', NULL, 'comment_list', 0, 0);
INSERT INTO `sys_module` VALUES ('common', NULL, NULL, NULL, NULL, 0, 0);
INSERT INTO `sys_module` VALUES ('config', NULL, NULL, 'bi bi-nut', NULL, 1, 9);
INSERT INTO `sys_module` VALUES ('config_add', 'sysConfig/add', 'sysConfig/save', NULL, 'config_list', 0, 0);
INSERT INTO `sys_module` VALUES ('config_data_delete', NULL, 'sysConfigData/delete', NULL, 'config_data_list', 0, 0);
INSERT INTO `sys_module` VALUES ('config_data_edit', NULL, 'sysConfigData/save,sysConfigData/edit', NULL, 'config_data_list', 0, 0);
INSERT INTO `sys_module` VALUES ('config_data_export', NULL, 'sysConfigData/export', NULL, 'config_data_list', 0, 0);
INSERT INTO `sys_module` VALUES ('config_data_list', 'sysConfigData/list', NULL, 'icon-cog', 'system', 1, 1);
INSERT INTO `sys_module` VALUES ('config_data_import', 'sysConfigData/import', 'sysConfigData/doImport', NULL, 'config_data_list', 0, 0);
INSERT INTO `sys_module` VALUES ('config_delete', NULL, 'sysConfig/delete', NULL, 'config_list', 0, 0);
INSERT INTO `sys_module` VALUES ('config_list', 'sysConfig/list', NULL, 'icon-cogs', 'config', 1, 3);
INSERT INTO `sys_module` VALUES ('content', NULL, NULL, 'bi bi-file-post', NULL, 1, 2);
INSERT INTO `sys_module` VALUES ('content_add', 'cmsContent/add', 'cmsContent/addMore,cmsContent/save', NULL, 'content_list', 0, 0);
INSERT INTO `sys_module` VALUES ('content_change_model', 'cmsContent/changeModelParameters', 'cmsContent/changeModel', NULL, 'content_list', 0, 0);
INSERT INTO `sys_module` VALUES ('content_check', 'cmsContent/uncheck_list', 'cmsContent/check,cmsContent/reject', 'icon-check-sign', 'content', 1, 1);
INSERT INTO `sys_module` VALUES ('content_delete', NULL, 'cmsContent/delete', NULL, 'content_list', 0, 0);
INSERT INTO `sys_module` VALUES ('content_distribute', 'cmsCategory/lookupBySiteId', 'cmsContent/distribute', NULL, 'content_list', 0, 0);
INSERT INTO `sys_module` VALUES ('content_export', 'cmsContent/export', 'cmsContent/exportExcel,cmsContent/exportData', NULL, 'content_list', 0, 0);
INSERT INTO `sys_module` VALUES ('content_form', 'cmsTemplate/contentForm', 'cmsCategory/contributeForm', NULL, 'common', 0, 0);
INSERT INTO `sys_module` VALUES ('content_import', 'cmsContent/import', 'cmsContent/doImport', NULL, 'content_list', 0, 0);
INSERT INTO `sys_module` VALUES ('content_list', 'cmsContent/list', NULL, 'icon-book', 'content', 1, 0);
INSERT INTO `sys_module` VALUES ('content_move', 'cmsContent/moveParameters', 'cmsContent/move', NULL, 'content_list', 0, 0);
INSERT INTO `sys_module` VALUES ('content_publish', NULL, 'cmsContent/publish', NULL, 'content_list', 0, 0);
INSERT INTO `sys_module` VALUES ('content_push', 'cmsContent/push', 'cmsPlace/push,cmsPlace/add,cmsPlace/save,cmsContent/push_content,cmsContent/push_content_list,cmsContent/push_to_content,cmsContent/push_to_relation,cmsContent/related,cmsContent/unrelated,cmsPlace/delete', NULL, 'content_list', 0, 0);
INSERT INTO `sys_module` VALUES ('content_recycle_delete', NULL, 'cmsContent/realDelete', NULL, 'content_recycle_list', 0, 0);
INSERT INTO `sys_module` VALUES ('content_recycle_list', 'cmsRecycleContent/list', NULL, 'icon-trash', 'content', 1, 11);
INSERT INTO `sys_module` VALUES ('content_recycle_recycle', NULL, 'cmsContent/recycle', NULL, 'content_recycle_list', 0, 0);
INSERT INTO `sys_module` VALUES ('content_refresh', NULL, 'cmsContent/refresh', NULL, 'content_list', 0, 0);
INSERT INTO `sys_module` VALUES ('content_search', 'cmsContent/search', 'cmsContent/view', 'icon-search', 'content', 1, 2);
INSERT INTO `sys_module` VALUES ('content_sort', 'cmsContent/sortParameters', 'cmsContent/sort', NULL, 'content_list', 0, 0);
INSERT INTO `sys_module` VALUES ('content_uncheck', NULL, 'cmsContent/uncheck', NULL, 'content_list', 0, 0);
INSERT INTO `sys_module` VALUES ('content_view', 'cmsContent/view', NULL, NULL, 'content_list', 0, 0);
INSERT INTO `sys_module` VALUES ('dept_add', 'sysDept/add', 'sysDept/save,sysDept/virify', NULL, 'dept_list', 0, 0);
INSERT INTO `sys_module` VALUES ('dept_delete', NULL, 'sysDept/delete', NULL, 'dept_list', 0, 0);
INSERT INTO `sys_module` VALUES ('dept_list', 'sysDept/list', NULL, 'icon-group', 'user', 1, 2);
INSERT INTO `sys_module` VALUES ('dept_user_list', 'sysDept/userList', 'sysDept/addUser,sysDept/saveUser,sysDept/enableUser,sysDept/disableUser', NULL, 'dept_list', 0, 0);
INSERT INTO `sys_module` VALUES ('dictionary_add', 'cmsDictionary/add', 'cmsDictionary/addChild,cmsDictionary/exclude,cmsDictionary/excludeTree,cmsDictionary/excludeValue,cmsDictionaryExclude/save,cmsDictionaryExcludeValue/save,cmsDictionary/save,cmsDictionary/virify', NULL, 'dictionary_list', 0, 0);
INSERT INTO `sys_module` VALUES ('dictionary_delete', NULL, 'cmsDictionary/delete', NULL, 'dictionary_list', 0, 0);
INSERT INTO `sys_module` VALUES ('dictionary_export', NULL, 'cmsDictionary/export', NULL, 'dictionary_list', 0, 0);
INSERT INTO `sys_module` VALUES ('dictionary_import', 'cmsDictionary/import', 'cmsDictionary/doImport', NULL, 'dictionary_list', 0, 0);
INSERT INTO `sys_module` VALUES ('dictionary_list', 'cmsDictionary/list', NULL, 'icon-book', 'system', 1, 4);
INSERT INTO `sys_module` VALUES ('diy_list', 'cmsDiy/list', 'cmsDiy/region,cmsDiy/layout,cmsDiy/module,cmsDiy/saveRegion,cmsDiy/saveLayout,cmsDiy/saveModule,cmsDiy/delete', 'icon-dashboard', 'file', 1, 3);
INSERT INTO `sys_module` VALUES ('domain_config', 'sysDomain/config', 'sysDomain/saveConfig,cmsTemplate/directoryLookup', NULL, 'domain_list', 0, 0);
INSERT INTO `sys_module` VALUES ('domain_list', 'sysDomain/domainList', NULL, 'icon-qrcode', 'config', 1, 4);
INSERT INTO `sys_module` VALUES ('editor', NULL, 'ueditor,tinymce/upload,tinymce/imageList,ckeditor/upload,cmsWebFile/browse,file/doImport', NULL, 'common', 0, 0);
INSERT INTO `sys_module` VALUES ('editor_history', 'cmsEditorHistory/lookup', 'cmsEditorHistory/use,cmsEditorHistory/compare', NULL, 'common', 0, 0);
INSERT INTO `sys_module` VALUES ('file', NULL, NULL, 'bi bi-file-earmark-text', NULL, 1, 7);
INSERT INTO `sys_module` VALUES ('file_history', 'cmsFileHistory/list', 'cmsFileHistory/use,cmsFileHistory/compare', NULL, 'template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('file_recycle', 'cmsFileBackup/list', 'cmsFileBackup/content,cmsFileBackup/recycle', NULL, 'template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('file_upload', NULL, 'file/doUpload,file/doBatchUpload', NULL, 'common', 0, 0);
INSERT INTO `sys_module` VALUES ('log_login', 'log/login', NULL, 'icon-signin', 'operation', 1, 3);
INSERT INTO `sys_module` VALUES ('log_login_delete', NULL, 'logLogin/delete', NULL, 'log_login', 0, 0);
INSERT INTO `sys_module` VALUES ('log_operate', 'log/operate', NULL, 'icon-list-alt', 'operation', 1, 2);
INSERT INTO `sys_module` VALUES ('log_operate_delete', NULL, 'logOperate/delete', NULL, 'log_operate', 0, 0);
INSERT INTO `sys_module` VALUES ('log_operate_view', 'log/operateView', NULL, NULL, 'log_operate', 0, 0);
INSERT INTO `sys_module` VALUES ('log_task', 'log/task', NULL, 'icon-time', 'operation', 1, 4);
INSERT INTO `sys_module` VALUES ('log_task_delete', NULL, 'logTask/delete', NULL, 'log_task', 0, 0);
INSERT INTO `sys_module` VALUES ('log_task_view', 'log/taskView', NULL, NULL, 'log_task', 0, 0);
INSERT INTO `sys_module` VALUES ('log_upload', 'log/upload', 'logUpload/delete', 'icon-list-alt', 'operation', 1, 1);
INSERT INTO `sys_module` VALUES ('log_workload', 'cmsContent/workload', NULL, 'bi bi-calendar-heart', 'operation', 1, 0);
INSERT INTO `sys_module` VALUES ('model_add', 'cmsModel/add', 'cmsModel/save,cmsModel/rebuildSearchText,cmsModel/batchPublish', NULL, 'model_list', 0, 0);
INSERT INTO `sys_module` VALUES ('model_delete', NULL, 'cmsModel/delete', NULL, 'model_list', 0, 0);
INSERT INTO `sys_module` VALUES ('model_list', 'cmsModel/list', 'cmsModel/categoryList,cmsModel/template', 'icon-th-large', 'config', 1, 1);
INSERT INTO `sys_module` VALUES ('myself', NULL, NULL, 'icon-key', NULL, 1, 10);
INSERT INTO `sys_module` VALUES ('myself_content', 'myself/contentList', NULL, 'icon-book', 'myself', 1, 2);
INSERT INTO `sys_module` VALUES ('myself_content_add', 'cmsContent/add', 'cmsContent/save', NULL, 'myself_content', 0, 0);
INSERT INTO `sys_module` VALUES ('myself_content_delete', NULL, 'cmsContent/delete', NULL, 'myself_content', 0, 0);
INSERT INTO `sys_module` VALUES ('myself_content_publish', NULL, 'cmsContent/publish', NULL, 'myself_content', 0, 0);
INSERT INTO `sys_module` VALUES ('myself_content_push', 'cmsContent/push', 'cmsContent/push_content,cmsContent/push_content_list,cmsContent/push_to_content,cmsContent/push_page,cmsContent/push_page_list,cmsContent/push_to_place,cmsContent/related', NULL, 'myself_content', 0, 0);
INSERT INTO `sys_module` VALUES ('myself_content_refresh', NULL, 'cmsContent/refresh', NULL, 'myself_content', 0, 0);
INSERT INTO `sys_module` VALUES ('myself_device', 'myself/userDeviceList', 'sysAppClient/enable,sysAppClient/disable', 'icon-linux', 'myself', 1, 5);
INSERT INTO `sys_module` VALUES ('myself_log_login', 'myself/logLogin', NULL, 'icon-signin', 'myself', 1, 4);
INSERT INTO `sys_module` VALUES ('myself_log_operate', 'myself/logOperate', NULL, 'icon-list-alt', 'myself', 1, 3);
INSERT INTO `sys_module` VALUES ('myself_password', 'myself/password', 'changePassword', 'icon-key', 'myself', 1, 1);
INSERT INTO `sys_module` VALUES ('myself_profile', 'myself/profile', 'sysUser/update,file/doUpload', 'icon-user', 'myself', 1, 0);
INSERT INTO `sys_module` VALUES ('myself_token', 'myself/userTokenList', 'sysUserToken/delete', 'icon-unlock-alt', 'myself', 1, 5);
INSERT INTO `sys_module` VALUES ('operation', NULL, NULL, 'bi bi-binoculars-fill', NULL, 1, 7);
INSERT INTO `sys_module` VALUES ('order_confirm', 'tradeOrder/confirmParameters', 'tradeOrder/confirm', NULL, 'order_list', 0, 0);
INSERT INTO `sys_module` VALUES ('order_history_list', 'tradeOrderHistory/list', NULL, 'icon-calendar', 'trade', 1, 2);
INSERT INTO `sys_module` VALUES ('order_list', 'tradeOrder/list', NULL, 'icon-barcode', 'trade', 1, 1);
INSERT INTO `sys_module` VALUES ('order_process', 'tradeOrder/processParameters', 'tradeOrder/process,tradeOrder/invalid,tradeOrder/close,tradeOrder/export', NULL, 'order_list', 0, 0);
INSERT INTO `sys_module` VALUES ('order_view', 'tradeOrder/view', NULL, NULL, 'order_list', 0, 0);
INSERT INTO `sys_module` VALUES ('page', NULL, NULL, 'icon-tablet', NULL, 1, 3);
INSERT INTO `sys_module` VALUES ('page_diy', 'cmsPage/diy', 'cmsPage/region,cmsDiy/save', 'bi bi-palette', 'page', 1, 3);
INSERT INTO `sys_module` VALUES ('page_diy_buttons', 'cmsDiy/buttons', NULL, NULL, 'page_diy', 0, 3);
INSERT INTO `sys_module` VALUES ('page_diy_region', 'cmsPage/region', NULL, NULL, 'page_diy', 0, 1);
INSERT INTO `sys_module` VALUES ('page_list', 'cmsPage/list', 'cmsPage/metadata', 'icon-globe', 'page', 1, 1);
INSERT INTO `sys_module` VALUES ('page_metadata', 'cmsPage/metadata', 'cmsPage/save', NULL, 'page_list', 0, 0);
INSERT INTO `sys_module` VALUES ('page_preview', 'cmsDiy/preview', NULL, 'bi bi-palette2', 'page', 1, 0);
INSERT INTO `sys_module` VALUES ('page_publish', NULL, 'cmsTemplate/publish', NULL, 'page_list', 0, 0);
INSERT INTO `sys_module` VALUES ('page_save', NULL, 'cmsPage/save,cmsPage/clearCache', NULL, 'page_list', 0, 0);
INSERT INTO `sys_module` VALUES ('payment_history_list', 'tradePaymentHistory/list', 'tradePaymentHistory/view', 'icon-exchange', 'trade', 1, 4);
INSERT INTO `sys_module` VALUES ('payment_list', 'tradePayment/list', 'tradePayment/refund,tradePayment/refuse', 'icon-money', 'trade', 1, 3);
INSERT INTO `sys_module` VALUES ('place_add', 'cmsPlace/add', 'cmsPlace/lookup,cmsPlace/lookup_content_list,cmsPlace/save', NULL, 'place_list', 0, 0);
INSERT INTO `sys_module` VALUES ('place_check', NULL, 'cmsPlace/check,cmsPlace/uncheck', NULL, 'place_list', 0, 0);
INSERT INTO `sys_module` VALUES ('place_clear', NULL, 'cmsPlace/clear', NULL, 'place_list', 0, 0);
INSERT INTO `sys_module` VALUES ('place_data_list', 'cmsPlace/dataList', 'cmsPlace/export', NULL, 'place_list', 0, 1);
INSERT INTO `sys_module` VALUES ('place_delete', NULL, 'cmsPlace/delete', NULL, 'place_list', 0, 0);
INSERT INTO `sys_module` VALUES ('place_form', 'placeTemplate/form', NULL, NULL, 'common', 0, 0);
INSERT INTO `sys_module` VALUES ('place_list', 'cmsPlace/list', NULL, 'icon-list-alt', 'page', 1, 2);
INSERT INTO `sys_module` VALUES ('place_publish', 'cmsPlace/metadata', 'cmsTemplate/publishPlace', NULL, 'place_list', 0, 0);
INSERT INTO `sys_module` VALUES ('place_refresh', NULL, 'cmsPlace/refresh', NULL, 'place_list', 0, 0);
INSERT INTO `sys_module` VALUES ('place_template_content', 'placeTemplate/content', 'cmsTemplate/help,cmsTemplate/savePlace,cmsWebFile/lookup', NULL, 'place_template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('place_template_help', 'cmsTemplate/help', NULL, NULL, 'place_template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('place_template_list', 'placeTemplate/list', 'placeTemplate/directory', 'icon-list-alt', 'file', 1, 2);
INSERT INTO `sys_module` VALUES ('place_template_metadata', 'placeTemplate/metadata', 'cmsTemplate/savePlaceMetaData,cmsTemplate/deletePlace,cmsTemplate/createDirectory', NULL, 'place_template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('place_view', 'cmsPlace/view', NULL, NULL, 'place_list', 0, 0);
INSERT INTO `sys_module` VALUES ('product_add', 'cmsContentProduct/add', 'cmsContentProduct/save', NULL, 'product_list', 1, 0);
INSERT INTO `sys_module` VALUES ('product_list', 'cmsContentProduct/list', NULL, 'icon-truck', 'trade', 1, 1);
INSERT INTO `sys_module` VALUES ('record_add', 'sysRecord/add', 'sysRecord/save', NULL, 'record_list', 0, 0);
INSERT INTO `sys_module` VALUES ('record_delete', NULL, 'sysRecord/delete', NULL, 'record_list', 0, 0);
INSERT INTO `sys_module` VALUES ('record_list', 'sysRecord/list', NULL, 'bi bi-receipt', 'system', 1, 6);
INSERT INTO `sys_module` VALUES ('record_view', 'sysRecord/view', NULL, NULL, 'record_list', 0, 0);
INSERT INTO `sys_module` VALUES ('refund_list', 'tradeRefund/list', NULL, 'icon-signout', 'trade', 1, 5);
INSERT INTO `sys_module` VALUES ('refund_refund', 'tradeRefund/refundParameters', 'tradeOrder/refund', NULL, 'refund_list', 0, 0);
INSERT INTO `sys_module` VALUES ('refund_refuse', 'tradeRefund/refuseParameters', 'tradeOrder/refuse', NULL, 'refund_list', 0, 0);
INSERT INTO `sys_module` VALUES ('report_user', 'report/user', NULL, 'icon-male', 'user', 1, 5);
INSERT INTO `sys_module` VALUES ('report_visit', 'report/visit', NULL, 'bi bi-pie-chart', 'operation', 1, 0);
INSERT INTO `sys_module` VALUES ('role_add', 'sysRole/add', 'sysRole/save', NULL, 'role_list', 0, 0);
INSERT INTO `sys_module` VALUES ('role_delete', NULL, 'sysRole/delete', NULL, 'role_list', 0, 0);
INSERT INTO `sys_module` VALUES ('role_list', 'sysRole/list', NULL, 'icon-user-md', 'user', 1, 3);
INSERT INTO `sys_module` VALUES ('select_category', 'cmsCategory/lookup', 'cmsCategory/lookupByModelId', NULL, 'common', 0, 0);
INSERT INTO `sys_module` VALUES ('select_category_type', 'cmsCategoryType/lookup', NULL, NULL, 'common', 0, 0);
INSERT INTO `sys_module` VALUES ('select_content', 'cmsContent/lookup', 'cmsContent/lookup_list,cmsContent/contentImage', NULL, 'common', 0, 0);
INSERT INTO `sys_module` VALUES ('select_dept', 'sysDept/lookup', NULL, NULL, 'common', 0, 0);
INSERT INTO `sys_module` VALUES ('select_dictionary', 'cmsDictionary/lookup', NULL, NULL, 'common', 0, 0);
INSERT INTO `sys_module` VALUES ('select_fragment', 'cmsTemplate/ftlLookup', NULL, NULL, 'common', 0, 0);
INSERT INTO `sys_module` VALUES ('select_place', 'placeTemplate/lookup', 'placeTemplate/lookupPlace', NULL, 'common', 0, 0);
INSERT INTO `sys_module` VALUES ('select_survey', 'cmsSurvey/lookup', NULL, NULL, 'common', 0, 0);
INSERT INTO `sys_module` VALUES ('select_tag', 'cmsTag/lookup', NULL, NULL, 'common', 0, 0);
INSERT INTO `sys_module` VALUES ('select_tag_type', 'cmsTagType/lookup', NULL, NULL, 'common', 0, 0);
INSERT INTO `sys_module` VALUES ('select_template', 'cmsTemplate/lookup', NULL, NULL, 'common', 0, 0);
INSERT INTO `sys_module` VALUES ('select_user', 'sysUser/lookup', 'sysUser/lookup_list', NULL, 'common', 0, 0);
INSERT INTO `sys_module` VALUES ('select_vote', 'cmsVote/lookup', NULL, NULL, 'common', 0, 0);
INSERT INTO `sys_module` VALUES ('survey_add', 'cmsSurvey/add', 'cmsSurvey/save', NULL, 'survey_list', 0, 0);
INSERT INTO `sys_module` VALUES ('survey_delete', NULL, 'cmsSurvey/delete', NULL, 'survey_list', 0, 0);
INSERT INTO `sys_module` VALUES ('survey_list', 'cmsSurvey/list', NULL, 'icon-list-ul', 'content', 1, 8);
INSERT INTO `sys_module` VALUES ('survey_question_list', 'cmsSurveyQuestion/list', 'cmsSurveyQuestion/add,cmsSurveyQuestion/save,cmsSurveyQuestion/delete', NULL, 'survey_list', 0, 0);
INSERT INTO `sys_module` VALUES ('survey_user_list', 'cmsUserSurvey/list', 'cmsUserSurvey/add,cmsUserSurvey/save', NULL, 'survey_list', 0, 0);
INSERT INTO `sys_module` VALUES ('survey_view', 'cmsSurvey/view', NULL, NULL, 'survey_list', 0, 0);
INSERT INTO `sys_module` VALUES ('system', NULL, NULL, 'icon-cogs', NULL, 1, 6);
INSERT INTO `sys_module` VALUES ('tag_add', 'cmsTag/add', 'cmsTag/save', NULL, 'tag_list', 0, 0);
INSERT INTO `sys_module` VALUES ('tag_delete', NULL, 'cmsTag/delete', NULL, 'tag_list', 0, 0);
INSERT INTO `sys_module` VALUES ('tag_list', 'cmsTag/list', NULL, 'icon-tag', 'content', 1, 5);
INSERT INTO `sys_module` VALUES ('tag_type_delete', NULL, 'cmsTagType/delete', NULL, 'tag_type_list', 0, 0);
INSERT INTO `sys_module` VALUES ('tag_type_list', 'cmsTagType/list', NULL, 'icon-tags', 'content', 1, 6);
INSERT INTO `sys_module` VALUES ('tag_type_save', 'cmsTagType/add', 'cmsTagType/save', NULL, 'tag_type_list', 0, 0);
INSERT INTO `sys_module` VALUES ('task_add', 'sysTask/add', 'sysTask/save,sysTask/example,taskTemplate/lookup', NULL, 'task_list', 0, 0);
INSERT INTO `sys_module` VALUES ('task_delete', NULL, 'sysTask/delete', NULL, 'task_list', 0, 0);
INSERT INTO `sys_module` VALUES ('task_list', 'sysTask/list', NULL, 'icon-time', 'system', 1, 2);
INSERT INTO `sys_module` VALUES ('task_pause', NULL, 'sysTask/pause,sysTask/interrupt', NULL, 'task_list', 0, 0);
INSERT INTO `sys_module` VALUES ('task_recreate', NULL, 'sysTask/recreate', NULL, 'task_list', 0, 0);
INSERT INTO `sys_module` VALUES ('task_resume', NULL, 'sysTask/resume', NULL, 'task_list', 0, 0);
INSERT INTO `sys_module` VALUES ('task_runonce', NULL, 'sysTask/runOnce', NULL, 'task_list', 0, 0);
INSERT INTO `sys_module` VALUES ('task_template_content', 'taskTemplate/content', 'taskTemplate/save,taskTemplate/upload,taskTemplate/doUpload,taskTemplate/export,taskTemplate/chipLookup,cmsTemplate/help', NULL, 'task_template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('task_template_delete', NULL, 'taskTemplate/delete', NULL, 'task_template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('task_template_fragment', 'taskTemplate/chipLookup', NULL, NULL, 'task_template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('task_template_help', 'cmsTemplate/help', NULL, NULL, 'task_template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('task_template_list', 'taskTemplate/list', NULL, 'icon-time', 'file', 1, 4);
INSERT INTO `sys_module` VALUES ('template_content', 'cmsTemplate/content', 'cmsTemplate/save,cmsTemplate/saveMetaData,cmsWebFile/lookup,cmsTemplate/help', NULL, 'template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('template_content-type', 'cmsTemplate/contentTypeLookup', NULL, NULL, 'template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('template_export', 'cmsTemplate/export', 'cmsTemplate/export', NULL, 'template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('template_delete', NULL, 'cmsTemplate/delete', NULL, 'template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('template_demo', 'cmsTemplate/demo', NULL, NULL, 'template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('template_fragment', 'cmsTemplate/ftlLookup', NULL, NULL, 'template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('template_help', 'cmsTemplate/help', NULL, NULL, 'template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('template_import', 'cmsTemplate/upload', 'cmsTemplate/doUpload,cmsTemplate/import,cmsTemplate/doImport,cmsTemplate/sitefileList,cmsTemplate/viewSitefile,cmsTemplate/visitSitefileImage', NULL, 'template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('template_list', 'cmsTemplate/list', 'cmsTemplate/directory', 'bi bi-code-square', 'file', 1, 1);
INSERT INTO `sys_module` VALUES ('template_metadata', 'cmsTemplate/metadata', 'cmsTemplate/saveMetaData,cmsTemplate/createDirectory', NULL, 'template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('template_search', 'cmsTemplate/search', 'cmsTemplate/replace', NULL, 'template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('template_upload', 'cmsTemplate/upload', 'cmsTemplate/doUpload', NULL, 'template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('template_website_file', 'cmsWebFile/lookup', NULL, NULL, 'template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('trade', NULL, NULL, 'bi bi-cart4', NULL, 1, 4);
INSERT INTO `sys_module` VALUES ('user', NULL, NULL, 'bi bi-person-circle', NULL, 1, 5);
INSERT INTO `sys_module` VALUES ('user_add', 'sysUser/add', 'sysDept/lookup,sysUser/save', NULL, 'user_list', 0, 0);
INSERT INTO `sys_module` VALUES ('user_disable', NULL, 'sysUser/disable', NULL, 'user_list', 0, 0);
INSERT INTO `sys_module` VALUES ('user_enable', NULL, 'sysUser/enable', NULL, 'user_list', 0, 0);
INSERT INTO `sys_module` VALUES ('user_list', 'sysUser/list', NULL, 'icon-user', 'user', 1, 1);
INSERT INTO `sys_module` VALUES ('visit_day', 'visit/day', 'visit/exportDay', 'icon-calendar', 'operation', 1, 3);
INSERT INTO `sys_module` VALUES ('visit_history', 'visit/history', 'visit/view,visit/exportHistory', 'icon-bolt', 'operation', 1, 1);
INSERT INTO `sys_module` VALUES ('visit_item', 'visit/item', 'visit/exportItem', 'icon-flag-checkered', 'operation', 1, 5);
INSERT INTO `sys_module` VALUES ('visit_session', 'visit/session', 'visit/exportSession', 'icon-comment-alt', 'operation', 1, 2);
INSERT INTO `sys_module` VALUES ('visit_url', 'visit/url', 'visit/exportUrl', 'icon-link', 'operation', 1, 4);
INSERT INTO `sys_module` VALUES ('vote_add', 'cmsVote/add', 'cmsVote/save', NULL, 'content_vote', 0, 0);
INSERT INTO `sys_module` VALUES ('vote_delete', NULL, 'cmsVote/delete', NULL, 'content_vote', 0, 0);
INSERT INTO `sys_module` VALUES ('vote_list', 'cmsVote/list', NULL, 'icon-ticket', 'content', 1, 7);
INSERT INTO `sys_module` VALUES ('vote_view', 'cmsVote/view', NULL, NULL, 'content_vote', 0, 0);
INSERT INTO `sys_module` VALUES ('webfile_content', 'cmsWebFile/content', 'cmsWebFile/save,cmsWebFile/delete', NULL, 'webfile_list', 0, 0);
INSERT INTO `sys_module` VALUES ('webfile_directory', 'cmsWebFile/directory', 'cmsWebFile/createDirectory', NULL, 'webfile_list', 0, 0);
INSERT INTO `sys_module` VALUES ('webfile_list', 'cmsWebFile/list', NULL, 'icon-globe', 'file', 1, 5);
INSERT INTO `sys_module` VALUES ('webfile_unzip', 'cmsWebFile/unzipParameters', 'cmsWebFile/unzip', NULL, 'webfile_list', 0, 0);
INSERT INTO `sys_module` VALUES ('webfile_upload', 'cmsWebFile/upload', 'cmsWebFile/doUpload,cmsWebFile/uploadIco,cmsWebFile/doUpload,cmsWebFile/doUploadIco,cmsWebFile/check', NULL, 'webfile_list', 0, 0);
INSERT INTO `sys_module` VALUES ('webfile_zip', NULL, 'cmsWebFile/zip', NULL, 'webfile_list', 0, 0);
INSERT INTO `sys_module` VALUES ('word_list', 'cmsWord/list', 'cmsWord/hidden,cmsWord/delete,cmsWord/show,cmsWord/add,cmsWord/save', 'bi bi-search-heart', 'content', 1, 10);

-- ----------------------------
-- Table structure for sys_module_lang
-- ----------------------------
DROP TABLE IF EXISTS `sys_module_lang`;
CREATE TABLE `sys_module_lang`  (
  `module_id` varchar(30) NOT NULL COMMENT '模块',
  `lang` varchar(20) NOT NULL COMMENT '语言',
  `value` varchar(100) NULL DEFAULT NULL COMMENT '值',
  PRIMARY KEY (`module_id`, `lang`),
  KEY `sys_module_lang_lang`(`lang`)
) COMMENT = '模块语言';

-- ----------------------------
-- Records of sys_module_lang
-- ----------------------------
INSERT INTO `sys_module_lang` VALUES ('account_add', 'en', 'Add');
INSERT INTO `sys_module_lang` VALUES ('account_add', 'ja', '追加');
INSERT INTO `sys_module_lang` VALUES ('account_add', 'zh', '增加');
INSERT INTO `sys_module_lang` VALUES ('account_history_list', 'en', 'Account History');
INSERT INTO `sys_module_lang` VALUES ('account_history_list', 'ja', 'アカウントの履歴');
INSERT INTO `sys_module_lang` VALUES ('account_history_list', 'zh', '账户历史');
INSERT INTO `sys_module_lang` VALUES ('account_list', 'en', 'Account management');
INSERT INTO `sys_module_lang` VALUES ('account_list', 'ja', 'アカウント管理');
INSERT INTO `sys_module_lang` VALUES ('account_list', 'zh', '账户管理');
INSERT INTO `sys_module_lang` VALUES ('account_recharge', 'en', 'Recharge');
INSERT INTO `sys_module_lang` VALUES ('account_recharge', 'ja', 'チャージ');
INSERT INTO `sys_module_lang` VALUES ('account_recharge', 'zh', '充值');
INSERT INTO `sys_module_lang` VALUES ('app_add', 'en', 'Add/edit');
INSERT INTO `sys_module_lang` VALUES ('app_add', 'ja', '追加/変更');
INSERT INTO `sys_module_lang` VALUES ('app_add', 'zh', '增加/修改');
INSERT INTO `sys_module_lang` VALUES ('app_client_disable', 'en', 'Disable');
INSERT INTO `sys_module_lang` VALUES ('app_client_disable', 'ja', '禁止');
INSERT INTO `sys_module_lang` VALUES ('app_client_disable', 'zh', '禁用');
INSERT INTO `sys_module_lang` VALUES ('app_client_enable', 'en', 'Enable');
INSERT INTO `sys_module_lang` VALUES ('app_client_enable', 'ja', 'オン');
INSERT INTO `sys_module_lang` VALUES ('app_client_enable', 'zh', '启用');
INSERT INTO `sys_module_lang` VALUES ('app_client_list', 'en', 'Application client management');
INSERT INTO `sys_module_lang` VALUES ('app_client_list', 'ja', 'クライアント管理');
INSERT INTO `sys_module_lang` VALUES ('app_client_list', 'zh', '客户端管理');
INSERT INTO `sys_module_lang` VALUES ('app_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('app_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('app_delete', 'zh', '删除');
INSERT INTO `sys_module_lang` VALUES ('app_issue', 'en', 'Issue authorization');
INSERT INTO `sys_module_lang` VALUES ('app_issue', 'ja', '発行権限');
INSERT INTO `sys_module_lang` VALUES ('app_issue', 'zh', '颁发授权');
INSERT INTO `sys_module_lang` VALUES ('app_list', 'en', 'Application Authorization');
INSERT INTO `sys_module_lang` VALUES ('app_list', 'ja', 'app権限');
INSERT INTO `sys_module_lang` VALUES ('app_list', 'zh', '应用授权');
INSERT INTO `sys_module_lang` VALUES ('app_view', 'en', 'View');
INSERT INTO `sys_module_lang` VALUES ('app_view', 'ja', '見る');
INSERT INTO `sys_module_lang` VALUES ('app_view', 'zh', '查看');
INSERT INTO `sys_module_lang` VALUES ('category_add', 'en', 'Add/edit');
INSERT INTO `sys_module_lang` VALUES ('category_add', 'ja', '追加/変更');
INSERT INTO `sys_module_lang` VALUES ('category_add', 'zh', '增加/修改');
INSERT INTO `sys_module_lang` VALUES ('category_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('category_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('category_delete', 'zh', '删除');
INSERT INTO `sys_module_lang` VALUES ('category_export', 'en', 'Export');
INSERT INTO `sys_module_lang` VALUES ('category_export', 'ja', '輸出');
INSERT INTO `sys_module_lang` VALUES ('category_export', 'zh', '导出');
INSERT INTO `sys_module_lang` VALUES ('category_import', 'en', 'Import');
INSERT INTO `sys_module_lang` VALUES ('category_import', 'ja', '導入');
INSERT INTO `sys_module_lang` VALUES ('category_import', 'zh', '导入');
INSERT INTO `sys_module_lang` VALUES ('category_list', 'en', 'Category management');
INSERT INTO `sys_module_lang` VALUES ('category_list', 'ja', '分類管理');
INSERT INTO `sys_module_lang` VALUES ('category_list', 'zh', '分类管理');
INSERT INTO `sys_module_lang` VALUES ('category_move', 'en', 'Move');
INSERT INTO `sys_module_lang` VALUES ('category_move', 'ja', '移動');
INSERT INTO `sys_module_lang` VALUES ('category_move', 'zh', '移动');
INSERT INTO `sys_module_lang` VALUES ('category_publish', 'en', 'Publish');
INSERT INTO `sys_module_lang` VALUES ('category_publish', 'ja', '生成');
INSERT INTO `sys_module_lang` VALUES ('category_publish', 'zh', '生成');
INSERT INTO `sys_module_lang` VALUES ('category_push', 'en', 'Push');
INSERT INTO `sys_module_lang` VALUES ('category_push', 'ja', 'おすすめ');
INSERT INTO `sys_module_lang` VALUES ('category_push', 'zh', '推荐');
INSERT INTO `sys_module_lang` VALUES ('category_type_add', 'en', 'Add/edit');
INSERT INTO `sys_module_lang` VALUES ('category_type_add', 'ja', '追加/変更');
INSERT INTO `sys_module_lang` VALUES ('category_type_add', 'zh', '增加/修改');
INSERT INTO `sys_module_lang` VALUES ('category_type_change', 'en', 'Change category type');
INSERT INTO `sys_module_lang` VALUES ('category_type_change', 'ja', 'タイプ変更');
INSERT INTO `sys_module_lang` VALUES ('category_type_change', 'zh', '修改类型');
INSERT INTO `sys_module_lang` VALUES ('category_type_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('category_type_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('category_type_delete', 'zh', '删除');
INSERT INTO `sys_module_lang` VALUES ('category_type_list', 'en', 'Category type management');
INSERT INTO `sys_module_lang` VALUES ('category_type_list', 'ja', '分類タイプ管理');
INSERT INTO `sys_module_lang` VALUES ('category_type_list', 'zh', '分类类型管理');
INSERT INTO `sys_module_lang` VALUES ('clearcache', 'en', 'Clear cache');
INSERT INTO `sys_module_lang` VALUES ('clearcache', 'ja', 'キャッシュをリフレッシュする');
INSERT INTO `sys_module_lang` VALUES ('clearcache', 'zh', '刷新缓存');
INSERT INTO `sys_module_lang` VALUES ('comment_check', 'en', 'Check');
INSERT INTO `sys_module_lang` VALUES ('comment_check', 'ja', '審査');
INSERT INTO `sys_module_lang` VALUES ('comment_check', 'zh', '审核');
INSERT INTO `sys_module_lang` VALUES ('comment_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('comment_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('comment_delete', 'zh', '删除');
INSERT INTO `sys_module_lang` VALUES ('comment_edit', 'en', 'Edit');
INSERT INTO `sys_module_lang` VALUES ('comment_edit', 'ja', '変更');
INSERT INTO `sys_module_lang` VALUES ('comment_edit', 'zh', '修改');
INSERT INTO `sys_module_lang` VALUES ('comment_list', 'en', 'Comment management');
INSERT INTO `sys_module_lang` VALUES ('comment_list', 'ja', 'コメント管理');
INSERT INTO `sys_module_lang` VALUES ('comment_list', 'zh', '评论管理');
INSERT INTO `sys_module_lang` VALUES ('comment_reply', 'en', 'Reply');
INSERT INTO `sys_module_lang` VALUES ('comment_reply', 'ja', '応答');
INSERT INTO `sys_module_lang` VALUES ('comment_reply', 'zh', '回复');
INSERT INTO `sys_module_lang` VALUES ('comment_uncheck', 'en', 'Uncheck');
INSERT INTO `sys_module_lang` VALUES ('comment_uncheck', 'ja', '審査を取り消す');
INSERT INTO `sys_module_lang` VALUES ('comment_uncheck', 'zh', '取消审核');
INSERT INTO `sys_module_lang` VALUES ('common', 'en', 'Common');
INSERT INTO `sys_module_lang` VALUES ('common', 'ja', '共通');
INSERT INTO `sys_module_lang` VALUES ('common', 'zh', '通用');
INSERT INTO `sys_module_lang` VALUES ('config', 'en', 'Config');
INSERT INTO `sys_module_lang` VALUES ('config', 'ja', '設定');
INSERT INTO `sys_module_lang` VALUES ('config', 'zh', '设置');
INSERT INTO `sys_module_lang` VALUES ('config_add', 'en', 'Add/edit');
INSERT INTO `sys_module_lang` VALUES ('config_add', 'ja', '追加/変更');
INSERT INTO `sys_module_lang` VALUES ('config_add', 'zh', '添加/修改');
INSERT INTO `sys_module_lang` VALUES ('config_data_delete', 'en', 'Clear config data');
INSERT INTO `sys_module_lang` VALUES ('config_data_delete', 'ja', 'データをクリア');
INSERT INTO `sys_module_lang` VALUES ('config_data_delete', 'zh', '清空配置');
INSERT INTO `sys_module_lang` VALUES ('config_data_edit', 'en', 'Edit');
INSERT INTO `sys_module_lang` VALUES ('config_data_edit', 'ja', '変更');
INSERT INTO `sys_module_lang` VALUES ('config_data_edit', 'zh', '修改');
INSERT INTO `sys_module_lang` VALUES ('config_data_export', 'en', 'Export');
INSERT INTO `sys_module_lang` VALUES ('config_data_export', 'ja', '輸出');
INSERT INTO `sys_module_lang` VALUES ('config_data_export', 'zh', '导出');
INSERT INTO `sys_module_lang` VALUES ('config_data_import', 'en', 'Import');
INSERT INTO `sys_module_lang` VALUES ('config_data_import', 'ja', '導入');
INSERT INTO `sys_module_lang` VALUES ('config_data_import', 'zh', '导入');
INSERT INTO `sys_module_lang` VALUES ('config_data_list', 'en', 'Site configuration');
INSERT INTO `sys_module_lang` VALUES ('config_data_list', 'ja', 'サイト設定');
INSERT INTO `sys_module_lang` VALUES ('config_data_list', 'zh', '站点配置');
INSERT INTO `sys_module_lang` VALUES ('config_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('config_delete', 'ja', '設定を削除');
INSERT INTO `sys_module_lang` VALUES ('config_delete', 'zh', '删除配置');
INSERT INTO `sys_module_lang` VALUES ('config_list', 'en', 'Site config management');
INSERT INTO `sys_module_lang` VALUES ('config_list', 'ja', 'サイト設定管理');
INSERT INTO `sys_module_lang` VALUES ('config_list', 'zh', '站点配置管理');
INSERT INTO `sys_module_lang` VALUES ('content', 'en', 'Content');
INSERT INTO `sys_module_lang` VALUES ('content', 'ja', 'コンテンツ');
INSERT INTO `sys_module_lang` VALUES ('content', 'zh', '内容');
INSERT INTO `sys_module_lang` VALUES ('content_add', 'en', 'Add/edit');
INSERT INTO `sys_module_lang` VALUES ('content_add', 'ja', '追加/変更');
INSERT INTO `sys_module_lang` VALUES ('content_add', 'zh', '增加/修改');
INSERT INTO `sys_module_lang` VALUES ('content_change_model', 'en', 'Modify content model');
INSERT INTO `sys_module_lang` VALUES ('content_change_model', 'ja', 'コンテンツモデルを修正します');
INSERT INTO `sys_module_lang` VALUES ('content_change_model', 'zh', '修改内容模型');
INSERT INTO `sys_module_lang` VALUES ('content_check', 'en', 'Content check');
INSERT INTO `sys_module_lang` VALUES ('content_check', 'ja', '内容の審査');
INSERT INTO `sys_module_lang` VALUES ('content_check', 'zh', '内容审核');
INSERT INTO `sys_module_lang` VALUES ('content_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('content_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('content_delete', 'zh', '删除');
INSERT INTO `sys_module_lang` VALUES ('content_distribute', 'en', 'Distribute');
INSERT INTO `sys_module_lang` VALUES ('content_distribute', 'ja', '分布');
INSERT INTO `sys_module_lang` VALUES ('content_distribute', 'zh', '分发');
INSERT INTO `sys_module_lang` VALUES ('content_export', 'en', 'Export');
INSERT INTO `sys_module_lang` VALUES ('content_export', 'ja', '輸出');
INSERT INTO `sys_module_lang` VALUES ('content_export', 'zh', '导出');
INSERT INTO `sys_module_lang` VALUES ('content_form', 'en', 'Content contribute form');
INSERT INTO `sys_module_lang` VALUES ('content_form', 'ja', 'コンテンツ送信フォーム');
INSERT INTO `sys_module_lang` VALUES ('content_form', 'zh', '内容投稿表单');
INSERT INTO `sys_module_lang` VALUES ('content_import', 'en', 'Import');
INSERT INTO `sys_module_lang` VALUES ('content_import', 'ja', '導入');
INSERT INTO `sys_module_lang` VALUES ('content_import', 'zh', '导入');
INSERT INTO `sys_module_lang` VALUES ('content_list', 'en', 'Content management');
INSERT INTO `sys_module_lang` VALUES ('content_list', 'ja', 'コンテンツ管理');
INSERT INTO `sys_module_lang` VALUES ('content_list', 'zh', '内容管理');
INSERT INTO `sys_module_lang` VALUES ('content_move', 'en', 'Move');
INSERT INTO `sys_module_lang` VALUES ('content_move', 'ja', '移動');
INSERT INTO `sys_module_lang` VALUES ('content_move', 'zh', '移动');
INSERT INTO `sys_module_lang` VALUES ('content_publish', 'en', 'Publish');
INSERT INTO `sys_module_lang` VALUES ('content_publish', 'ja', '生成');
INSERT INTO `sys_module_lang` VALUES ('content_publish', 'zh', '生成');
INSERT INTO `sys_module_lang` VALUES ('content_push', 'en', 'Push');
INSERT INTO `sys_module_lang` VALUES ('content_push', 'ja', 'おすすめ');
INSERT INTO `sys_module_lang` VALUES ('content_push', 'zh', '推荐');
INSERT INTO `sys_module_lang` VALUES ('content_recycle_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('content_recycle_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('content_recycle_delete', 'zh', '删除');
INSERT INTO `sys_module_lang` VALUES ('content_recycle_list', 'en', 'Content recycle');
INSERT INTO `sys_module_lang` VALUES ('content_recycle_list', 'ja', 'コンテンツごみ箱');
INSERT INTO `sys_module_lang` VALUES ('content_recycle_list', 'zh', '内容回收站');
INSERT INTO `sys_module_lang` VALUES ('content_recycle_recycle', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('content_recycle_recycle', 'ja', '取り戻し');
INSERT INTO `sys_module_lang` VALUES ('content_recycle_recycle', 'zh', '还原');
INSERT INTO `sys_module_lang` VALUES ('content_refresh', 'en', 'Refresh');
INSERT INTO `sys_module_lang` VALUES ('content_refresh', 'ja', 'リフレッシュ');
INSERT INTO `sys_module_lang` VALUES ('content_refresh', 'zh', '刷新');
INSERT INTO `sys_module_lang` VALUES ('content_search', 'en', 'Content search');
INSERT INTO `sys_module_lang` VALUES ('content_search', 'ja', 'コンテンツ検索');
INSERT INTO `sys_module_lang` VALUES ('content_search', 'zh', '内容搜索');
INSERT INTO `sys_module_lang` VALUES ('content_sort', 'en', 'Sort');
INSERT INTO `sys_module_lang` VALUES ('content_sort', 'ja', 'トッピング');
INSERT INTO `sys_module_lang` VALUES ('content_sort', 'zh', '置顶');
INSERT INTO `sys_module_lang` VALUES ('content_uncheck', 'en', 'Uncheck');
INSERT INTO `sys_module_lang` VALUES ('content_uncheck', 'ja', '審査を取り消す');
INSERT INTO `sys_module_lang` VALUES ('content_uncheck', 'zh', '撤销审核');
INSERT INTO `sys_module_lang` VALUES ('content_view', 'en', 'View');
INSERT INTO `sys_module_lang` VALUES ('content_view', 'ja', '見る');
INSERT INTO `sys_module_lang` VALUES ('content_view', 'zh', '查看');
INSERT INTO `sys_module_lang` VALUES ('dept_add', 'en', 'Add/edit');
INSERT INTO `sys_module_lang` VALUES ('dept_add', 'ja', '追加/変更');
INSERT INTO `sys_module_lang` VALUES ('dept_add', 'zh', '增加/修改');
INSERT INTO `sys_module_lang` VALUES ('dept_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('dept_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('dept_delete', 'zh', '删除');
INSERT INTO `sys_module_lang` VALUES ('dept_list', 'en', 'Department management');
INSERT INTO `sys_module_lang` VALUES ('dept_list', 'ja', '部門管理');
INSERT INTO `sys_module_lang` VALUES ('dept_list', 'zh', '部门管理');
INSERT INTO `sys_module_lang` VALUES ('dept_user_list', 'en', 'Department user management');
INSERT INTO `sys_module_lang` VALUES ('dept_user_list', 'ja', '人事管理');
INSERT INTO `sys_module_lang` VALUES ('dept_user_list', 'zh', '人员管理');
INSERT INTO `sys_module_lang` VALUES ('dictionary_add', 'en', 'Add/edit');
INSERT INTO `sys_module_lang` VALUES ('dictionary_add', 'ja', '追加/変更');
INSERT INTO `sys_module_lang` VALUES ('dictionary_add', 'zh', '添加/修改');
INSERT INTO `sys_module_lang` VALUES ('dictionary_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('dictionary_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('dictionary_delete', 'zh', '删除');
INSERT INTO `sys_module_lang` VALUES ('dictionary_export', 'en', 'Export');
INSERT INTO `sys_module_lang` VALUES ('dictionary_export', 'ja', '輸出');
INSERT INTO `sys_module_lang` VALUES ('dictionary_export', 'zh', '导出');
INSERT INTO `sys_module_lang` VALUES ('dictionary_import', 'en', 'Import');
INSERT INTO `sys_module_lang` VALUES ('dictionary_import', 'ja', '導入');
INSERT INTO `sys_module_lang` VALUES ('dictionary_import', 'zh', '导入');
INSERT INTO `sys_module_lang` VALUES ('dictionary_list', 'en', 'Dictionary management');
INSERT INTO `sys_module_lang` VALUES ('dictionary_list', 'ja', 'データ辞書');
INSERT INTO `sys_module_lang` VALUES ('dictionary_list', 'zh', '数据字典');
INSERT INTO `sys_module_lang` VALUES ('diy_list', 'en', 'Page visualization management');
INSERT INTO `sys_module_lang` VALUES ('diy_list', 'ja', 'ページ視覚化管理');
INSERT INTO `sys_module_lang` VALUES ('diy_list', 'zh', '页面可视化管理');
INSERT INTO `sys_module_lang` VALUES ('domain_config', 'en', 'Edit');
INSERT INTO `sys_module_lang` VALUES ('domain_config', 'ja', '変更');
INSERT INTO `sys_module_lang` VALUES ('domain_config', 'zh', '修改');
INSERT INTO `sys_module_lang` VALUES ('domain_list', 'en', 'Domain management');
INSERT INTO `sys_module_lang` VALUES ('domain_list', 'ja', 'ドメイン名をバインド');
INSERT INTO `sys_module_lang` VALUES ('domain_list', 'zh', '绑定域名');
INSERT INTO `sys_module_lang` VALUES ('editor', 'en', 'editor');
INSERT INTO `sys_module_lang` VALUES ('editor', 'ja', 'エディタ');
INSERT INTO `sys_module_lang` VALUES ('editor', 'zh', '编辑器');
INSERT INTO `sys_module_lang` VALUES ('editor_history', 'en', 'Modify records');
INSERT INTO `sys_module_lang` VALUES ('editor_history', 'ja', 'レコードを変更する');
INSERT INTO `sys_module_lang` VALUES ('editor_history', 'zh', '修改记录');
INSERT INTO `sys_module_lang` VALUES ('file', 'en', 'File');
INSERT INTO `sys_module_lang` VALUES ('file', 'ja', 'ファイル');
INSERT INTO `sys_module_lang` VALUES ('file', 'zh', '文件');
INSERT INTO `sys_module_lang` VALUES ('file_history', 'en', 'File modification history');
INSERT INTO `sys_module_lang` VALUES ('file_history', 'ja', 'ファイル変更履歴');
INSERT INTO `sys_module_lang` VALUES ('file_history', 'zh', '文件修改历史');
INSERT INTO `sys_module_lang` VALUES ('file_recycle', 'en', 'File recycle bin');
INSERT INTO `sys_module_lang` VALUES ('file_recycle', 'ja', 'ファイルのごみ箱');
INSERT INTO `sys_module_lang` VALUES ('file_recycle', 'zh', '文件回收站');
INSERT INTO `sys_module_lang` VALUES ('file_upload', 'en', 'File upload');
INSERT INTO `sys_module_lang` VALUES ('file_upload', 'ja', 'ファイルのアップロードです');
INSERT INTO `sys_module_lang` VALUES ('file_upload', 'zh', '文件上传');
INSERT INTO `sys_module_lang` VALUES ('log_login', 'en', 'Login log');
INSERT INTO `sys_module_lang` VALUES ('log_login', 'ja', 'ログインログ');
INSERT INTO `sys_module_lang` VALUES ('log_login', 'zh', '登录日志');
INSERT INTO `sys_module_lang` VALUES ('log_login_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('log_login_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('log_login_delete', 'zh', '删除');
INSERT INTO `sys_module_lang` VALUES ('log_operate', 'en', 'Operate log');
INSERT INTO `sys_module_lang` VALUES ('log_operate', 'ja', '操作ログ');
INSERT INTO `sys_module_lang` VALUES ('log_operate', 'zh', '操作日志');
INSERT INTO `sys_module_lang` VALUES ('log_operate_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('log_operate_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('log_operate_delete', 'zh', '删除');
INSERT INTO `sys_module_lang` VALUES ('log_operate_view', 'en', 'View');
INSERT INTO `sys_module_lang` VALUES ('log_operate_view', 'ja', '見る');
INSERT INTO `sys_module_lang` VALUES ('log_operate_view', 'zh', '查看');
INSERT INTO `sys_module_lang` VALUES ('log_task', 'en', 'Task log');
INSERT INTO `sys_module_lang` VALUES ('log_task', 'ja', 'タスク計画ログ');
INSERT INTO `sys_module_lang` VALUES ('log_task', 'zh', '任务计划日志');
INSERT INTO `sys_module_lang` VALUES ('log_task_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('log_task_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('log_task_delete', 'zh', '删除');
INSERT INTO `sys_module_lang` VALUES ('log_task_view', 'en', 'View');
INSERT INTO `sys_module_lang` VALUES ('log_task_view', 'ja', '見る');
INSERT INTO `sys_module_lang` VALUES ('log_task_view', 'zh', '查看');
INSERT INTO `sys_module_lang` VALUES ('log_upload', 'en', 'Upload log');
INSERT INTO `sys_module_lang` VALUES ('log_upload', 'ja', 'ファイルアップロードログ');
INSERT INTO `sys_module_lang` VALUES ('log_upload', 'zh', '文件上传日志');
INSERT INTO `sys_module_lang` VALUES ('log_workload', 'en', 'Workload');
INSERT INTO `sys_module_lang` VALUES ('log_workload', 'ja', 'ワークロード');
INSERT INTO `sys_module_lang` VALUES ('log_workload', 'zh', '工作量统计');
INSERT INTO `sys_module_lang` VALUES ('system', 'en', 'System');
INSERT INTO `sys_module_lang` VALUES ('system', 'ja', 'システム');
INSERT INTO `sys_module_lang` VALUES ('system', 'zh', '系统');
INSERT INTO `sys_module_lang` VALUES ('model_add', 'en', 'Add/edit');
INSERT INTO `sys_module_lang` VALUES ('model_add', 'ja', '追加/変更');
INSERT INTO `sys_module_lang` VALUES ('model_add', 'zh', '增加/修改');
INSERT INTO `sys_module_lang` VALUES ('model_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('model_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('model_delete', 'zh', '删除');
INSERT INTO `sys_module_lang` VALUES ('model_list', 'en', 'Model management');
INSERT INTO `sys_module_lang` VALUES ('model_list', 'ja', 'コンテンツモデル管理');
INSERT INTO `sys_module_lang` VALUES ('model_list', 'zh', '内容模型管理');
INSERT INTO `sys_module_lang` VALUES ('myself', 'en', 'Myself');
INSERT INTO `sys_module_lang` VALUES ('myself', 'ja', '私の');
INSERT INTO `sys_module_lang` VALUES ('myself', 'zh', '我的');
INSERT INTO `sys_module_lang` VALUES ('myself_content', 'en', 'My content');
INSERT INTO `sys_module_lang` VALUES ('myself_content', 'ja', 'マイコンテンツ');
INSERT INTO `sys_module_lang` VALUES ('myself_content', 'zh', '我的内容');
INSERT INTO `sys_module_lang` VALUES ('myself_content_add', 'en', 'Add');
INSERT INTO `sys_module_lang` VALUES ('myself_content_add', 'ja', '変更');
INSERT INTO `sys_module_lang` VALUES ('myself_content_add', 'zh', '修改');
INSERT INTO `sys_module_lang` VALUES ('myself_content_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('myself_content_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('myself_content_delete', 'zh', '删除');
INSERT INTO `sys_module_lang` VALUES ('myself_content_publish', 'en', 'Publish');
INSERT INTO `sys_module_lang` VALUES ('myself_content_publish', 'ja', '生成');
INSERT INTO `sys_module_lang` VALUES ('myself_content_publish', 'zh', '生成');
INSERT INTO `sys_module_lang` VALUES ('myself_content_push', 'en', 'Push');
INSERT INTO `sys_module_lang` VALUES ('myself_content_push', 'ja', 'おすすめ');
INSERT INTO `sys_module_lang` VALUES ('myself_content_push', 'zh', '推荐');
INSERT INTO `sys_module_lang` VALUES ('myself_content_refresh', 'en', 'Refresh');
INSERT INTO `sys_module_lang` VALUES ('myself_content_refresh', 'ja', 'リフレッシュ');
INSERT INTO `sys_module_lang` VALUES ('myself_content_refresh', 'zh', '刷新');
INSERT INTO `sys_module_lang` VALUES ('myself_device', 'en', 'My device');
INSERT INTO `sys_module_lang` VALUES ('myself_device', 'ja', '私の端末');
INSERT INTO `sys_module_lang` VALUES ('myself_device', 'zh', '我的设备');
INSERT INTO `sys_module_lang` VALUES ('myself_log_login', 'en', 'My login log');
INSERT INTO `sys_module_lang` VALUES ('myself_log_login', 'ja', 'マイログインログ');
INSERT INTO `sys_module_lang` VALUES ('myself_log_login', 'zh', '我的登录日志');
INSERT INTO `sys_module_lang` VALUES ('myself_log_operate', 'en', 'My operate log');
INSERT INTO `sys_module_lang` VALUES ('myself_log_operate', 'ja', 'マイ操作ログ');
INSERT INTO `sys_module_lang` VALUES ('myself_log_operate', 'zh', '我的操作日志');
INSERT INTO `sys_module_lang` VALUES ('myself_password', 'en', 'Change password');
INSERT INTO `sys_module_lang` VALUES ('myself_password', 'ja', 'パスワードを変更');
INSERT INTO `sys_module_lang` VALUES ('myself_password', 'zh', '修改密码');
INSERT INTO `sys_module_lang` VALUES ('myself_profile', 'en', 'Modify personal information');
INSERT INTO `sys_module_lang` VALUES ('myself_profile', 'ja', '個人情報を変更する');
INSERT INTO `sys_module_lang` VALUES ('myself_profile', 'zh', '修改个人信息');
INSERT INTO `sys_module_lang` VALUES ('myself_token', 'en', 'My login token');
INSERT INTO `sys_module_lang` VALUES ('myself_token', 'ja', '私のログイン授権');
INSERT INTO `sys_module_lang` VALUES ('myself_token', 'zh', '我的登录授权');
INSERT INTO `sys_module_lang` VALUES ('operation', 'en', 'Operation');
INSERT INTO `sys_module_lang` VALUES ('operation', 'ja', '操作');
INSERT INTO `sys_module_lang` VALUES ('operation', 'zh', '运营');
INSERT INTO `sys_module_lang` VALUES ('order_confirm', 'en', 'Confirm order');
INSERT INTO `sys_module_lang` VALUES ('order_confirm', 'ja', '注文の確認');
INSERT INTO `sys_module_lang` VALUES ('order_confirm', 'zh', '确认订单');
INSERT INTO `sys_module_lang` VALUES ('order_history_list', 'en', 'Order history');
INSERT INTO `sys_module_lang` VALUES ('order_history_list', 'ja', 'オーダー履歴');
INSERT INTO `sys_module_lang` VALUES ('order_history_list', 'zh', '订单历史');
INSERT INTO `sys_module_lang` VALUES ('order_list', 'en', 'Order management');
INSERT INTO `sys_module_lang` VALUES ('order_list', 'ja', 'オーダー管理');
INSERT INTO `sys_module_lang` VALUES ('order_list', 'zh', '订单管理');
INSERT INTO `sys_module_lang` VALUES ('order_process', 'en', 'Process order');
INSERT INTO `sys_module_lang` VALUES ('order_process', 'ja', 'プロセスオーダー');
INSERT INTO `sys_module_lang` VALUES ('order_process', 'zh', '处理订单');
INSERT INTO `sys_module_lang` VALUES ('order_view', 'en', 'View');
INSERT INTO `sys_module_lang` VALUES ('order_view', 'ja', '見る');
INSERT INTO `sys_module_lang` VALUES ('order_view', 'zh', '查看');
INSERT INTO `sys_module_lang` VALUES ('page', 'en', 'Page');
INSERT INTO `sys_module_lang` VALUES ('page', 'ja', 'ページ');
INSERT INTO `sys_module_lang` VALUES ('page', 'zh', '页面');
INSERT INTO `sys_module_lang` VALUES ('page_diy', 'en', 'Visualized page');
INSERT INTO `sys_module_lang` VALUES ('page_diy', 'ja', '視覚化されたページ');
INSERT INTO `sys_module_lang` VALUES ('page_diy', 'zh', '页面可视化');
INSERT INTO `sys_module_lang` VALUES ('page_diy_buttons', 'en', 'Button');
INSERT INTO `sys_module_lang` VALUES ('page_diy_buttons', 'ja', 'ボタン');
INSERT INTO `sys_module_lang` VALUES ('page_diy_buttons', 'zh', '按钮');
INSERT INTO `sys_module_lang` VALUES ('page_diy_region', 'en', 'Region');
INSERT INTO `sys_module_lang` VALUES ('page_diy_region', 'ja', '領域');
INSERT INTO `sys_module_lang` VALUES ('page_diy_region', 'zh', '区域');
INSERT INTO `sys_module_lang` VALUES ('page_list', 'en', 'Page management');
INSERT INTO `sys_module_lang` VALUES ('page_list', 'ja', 'ページ管理');
INSERT INTO `sys_module_lang` VALUES ('page_list', 'zh', '页面管理');
INSERT INTO `sys_module_lang` VALUES ('page_metadata', 'en', 'Metadata management');
INSERT INTO `sys_module_lang` VALUES ('page_metadata', 'ja', 'メタデータ管理');
INSERT INTO `sys_module_lang` VALUES ('page_metadata', 'zh', '元数据管理');
INSERT INTO `sys_module_lang` VALUES ('page_publish', 'en', 'Publish');
INSERT INTO `sys_module_lang` VALUES ('page_publish', 'ja', 'ページを生成する');
INSERT INTO `sys_module_lang` VALUES ('page_publish', 'zh', '生成页面');
INSERT INTO `sys_module_lang` VALUES ('page_preview', 'en', 'Quick Maintenance');
INSERT INTO `sys_module_lang` VALUES ('page_preview', 'ja', 'クイックメンテナンス');
INSERT INTO `sys_module_lang` VALUES ('page_preview', 'zh', '快捷维护');
INSERT INTO `sys_module_lang` VALUES ('page_save', 'en', 'Save configuration');
INSERT INTO `sys_module_lang` VALUES ('page_save', 'ja', 'ページ設定を保存');
INSERT INTO `sys_module_lang` VALUES ('page_save', 'zh', '保存页面配置');
INSERT INTO `sys_module_lang` VALUES ('payment_history_list', 'en', 'Payment history');
INSERT INTO `sys_module_lang` VALUES ('payment_history_list', 'ja', '支払歴');
INSERT INTO `sys_module_lang` VALUES ('payment_history_list', 'zh', '支付历史');
INSERT INTO `sys_module_lang` VALUES ('payment_list', 'en', 'Payment management');
INSERT INTO `sys_module_lang` VALUES ('payment_list', 'ja', '支払い管理');
INSERT INTO `sys_module_lang` VALUES ('payment_list', 'zh', '支付管理');
INSERT INTO `sys_module_lang` VALUES ('place_add', 'en', 'Add/edit');
INSERT INTO `sys_module_lang` VALUES ('place_add', 'ja', '推奨ビットデータの追加/変更');
INSERT INTO `sys_module_lang` VALUES ('place_add', 'zh', '增加/修改推荐位数据');
INSERT INTO `sys_module_lang` VALUES ('place_check', 'en', 'Check');
INSERT INTO `sys_module_lang` VALUES ('place_check', 'ja', '推奨ビットデータを確認する');
INSERT INTO `sys_module_lang` VALUES ('place_check', 'zh', '审核推荐位数据');
INSERT INTO `sys_module_lang` VALUES ('place_clear', 'en', 'Clear');
INSERT INTO `sys_module_lang` VALUES ('place_clear', 'ja', '推奨ビットデータのクリア');
INSERT INTO `sys_module_lang` VALUES ('place_clear', 'zh', '清空推荐位数据');
INSERT INTO `sys_module_lang` VALUES ('place_data_list', 'en', 'Page fragment data');
INSERT INTO `sys_module_lang` VALUES ('place_data_list', 'ja', '推奨ビットデータ');
INSERT INTO `sys_module_lang` VALUES ('place_data_list', 'zh', '推荐位数据');
INSERT INTO `sys_module_lang` VALUES ('place_delete', 'en', 'data');
INSERT INTO `sys_module_lang` VALUES ('place_delete', 'ja', '推奨ビットデータを削除する');
INSERT INTO `sys_module_lang` VALUES ('place_delete', 'zh', '删除推荐位数据');
INSERT INTO `sys_module_lang` VALUES ('place_form', 'en', 'Page fragment data contribute form');
INSERT INTO `sys_module_lang` VALUES ('place_form', 'ja', 'ページフラグメント提出フォーム');
INSERT INTO `sys_module_lang` VALUES ('place_form', 'zh', '页面片段投稿表单');
INSERT INTO `sys_module_lang` VALUES ('place_list', 'en', 'Page fragment management');
INSERT INTO `sys_module_lang` VALUES ('place_list', 'ja', 'ページフラグメント管理');
INSERT INTO `sys_module_lang` VALUES ('place_list', 'zh', '页面片段管理');
INSERT INTO `sys_module_lang` VALUES ('place_publish', 'en', 'Publish');
INSERT INTO `sys_module_lang` VALUES ('place_publish', 'ja', 'リリース');
INSERT INTO `sys_module_lang` VALUES ('place_publish', 'zh', '发布');
INSERT INTO `sys_module_lang` VALUES ('place_refresh', 'en', 'Refresh');
INSERT INTO `sys_module_lang` VALUES ('place_refresh', 'ja', '刷新推荐位数据推奨ビットデータをリフレッシュする');
INSERT INTO `sys_module_lang` VALUES ('place_refresh', 'zh', '刷新推荐位数据');
INSERT INTO `sys_module_lang` VALUES ('place_template_content', 'en', 'Edit template');
INSERT INTO `sys_module_lang` VALUES ('place_template_content', 'ja', '変更');
INSERT INTO `sys_module_lang` VALUES ('place_template_content', 'zh', '修改');
INSERT INTO `sys_module_lang` VALUES ('place_template_help', 'en', 'Template help');
INSERT INTO `sys_module_lang` VALUES ('place_template_help', 'ja', 'テンプレートのヘルプ');
INSERT INTO `sys_module_lang` VALUES ('place_template_help', 'zh', '模板帮助');
INSERT INTO `sys_module_lang` VALUES ('place_template_list', 'en', 'Page fragment template');
INSERT INTO `sys_module_lang` VALUES ('place_template_list', 'ja', 'ページフラグメントテンプレート');
INSERT INTO `sys_module_lang` VALUES ('place_template_list', 'zh', '页面片段模板');
INSERT INTO `sys_module_lang` VALUES ('place_template_metadata', 'en', 'Edit metadata');
INSERT INTO `sys_module_lang` VALUES ('place_template_metadata', 'ja', 'メタデータの変更');
INSERT INTO `sys_module_lang` VALUES ('place_template_metadata', 'zh', '修改元数据');
INSERT INTO `sys_module_lang` VALUES ('place_view', 'en', 'View');
INSERT INTO `sys_module_lang` VALUES ('place_view', 'ja', '推奨ビットデータを見る');
INSERT INTO `sys_module_lang` VALUES ('place_view', 'zh', '查看推荐位数据');
INSERT INTO `sys_module_lang` VALUES ('product_add', 'en', 'Edit');
INSERT INTO `sys_module_lang` VALUES ('product_add', 'ja', '変更');
INSERT INTO `sys_module_lang` VALUES ('product_add', 'zh', '修改');
INSERT INTO `sys_module_lang` VALUES ('product_list', 'en', 'Product management');
INSERT INTO `sys_module_lang` VALUES ('product_list', 'ja', '製品管理');
INSERT INTO `sys_module_lang` VALUES ('product_list', 'zh', '产品管理');
INSERT INTO `sys_module_lang` VALUES ('record_add', 'en', 'Edit');
INSERT INTO `sys_module_lang` VALUES ('record_add', 'ja', '変更');
INSERT INTO `sys_module_lang` VALUES ('record_add', 'zh', '修改');
INSERT INTO `sys_module_lang` VALUES ('record_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('record_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('record_delete', 'zh', '删除');
INSERT INTO `sys_module_lang` VALUES ('record_list', 'en', 'Custom record management');
INSERT INTO `sys_module_lang` VALUES ('record_list', 'ja', 'カスタムレコード管理');
INSERT INTO `sys_module_lang` VALUES ('record_list', 'zh', '自定义记录管理');
INSERT INTO `sys_module_lang` VALUES ('record_view', 'en', 'View');
INSERT INTO `sys_module_lang` VALUES ('record_view', 'ja', '見る');
INSERT INTO `sys_module_lang` VALUES ('record_view', 'zh', '查看');
INSERT INTO `sys_module_lang` VALUES ('refund_list', 'en', 'Refund management');
INSERT INTO `sys_module_lang` VALUES ('refund_list', 'ja', '払い戻し管理');
INSERT INTO `sys_module_lang` VALUES ('refund_list', 'zh', '退款管理');
INSERT INTO `sys_module_lang` VALUES ('refund_refund', 'en', 'Refund');
INSERT INTO `sys_module_lang` VALUES ('refund_refund', 'ja', '払い戻し');
INSERT INTO `sys_module_lang` VALUES ('refund_refund', 'zh', '退款');
INSERT INTO `sys_module_lang` VALUES ('refund_refuse', 'en', 'Refuse');
INSERT INTO `sys_module_lang` VALUES ('refund_refuse', 'ja', 'ごみ');
INSERT INTO `sys_module_lang` VALUES ('refund_refuse', 'zh', '拒绝');
INSERT INTO `sys_module_lang` VALUES ('report_user', 'en', 'User report');
INSERT INTO `sys_module_lang` VALUES ('report_user', 'ja', 'ユーザーデータの監視');
INSERT INTO `sys_module_lang` VALUES ('report_user', 'zh', '用户数据监控');
INSERT INTO `sys_module_lang` VALUES ('report_visit', 'en', 'Visit report');
INSERT INTO `sys_module_lang` VALUES ('report_visit', 'ja', 'アクセス監視');
INSERT INTO `sys_module_lang` VALUES ('report_visit', 'zh', '用户访问监控');
INSERT INTO `sys_module_lang` VALUES ('role_add', 'en', 'Add/edit');
INSERT INTO `sys_module_lang` VALUES ('role_add', 'ja', '追加/変更');
INSERT INTO `sys_module_lang` VALUES ('role_add', 'zh', '增加/修改');
INSERT INTO `sys_module_lang` VALUES ('role_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('role_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('role_delete', 'zh', '删除');
INSERT INTO `sys_module_lang` VALUES ('role_list', 'en', 'Role management');
INSERT INTO `sys_module_lang` VALUES ('role_list', 'ja', '役割管理');
INSERT INTO `sys_module_lang` VALUES ('role_list', 'zh', '角色管理');
INSERT INTO `sys_module_lang` VALUES ('select_category', 'en', 'Select category');
INSERT INTO `sys_module_lang` VALUES ('select_category', 'ja', '分類を選択');
INSERT INTO `sys_module_lang` VALUES ('select_category', 'zh', '选择分类');
INSERT INTO `sys_module_lang` VALUES ('select_category_type', 'en', 'Select category type');
INSERT INTO `sys_module_lang` VALUES ('select_category_type', 'ja', '分類タイプを選択');
INSERT INTO `sys_module_lang` VALUES ('select_category_type', 'zh', '选择分类类型');
INSERT INTO `sys_module_lang` VALUES ('select_content', 'en', 'Select content');
INSERT INTO `sys_module_lang` VALUES ('select_content', 'ja', 'コンテンツを選択');
INSERT INTO `sys_module_lang` VALUES ('select_content', 'zh', '选择内容');
INSERT INTO `sys_module_lang` VALUES ('select_dept', 'en', 'Select department');
INSERT INTO `sys_module_lang` VALUES ('select_dept', 'ja', '部門を選びます');
INSERT INTO `sys_module_lang` VALUES ('select_dept', 'zh', '选择部门');
INSERT INTO `sys_module_lang` VALUES ('select_dictionary', 'en', 'Select data dictionary');
INSERT INTO `sys_module_lang` VALUES ('select_dictionary', 'ja', 'データ辞書を選択');
INSERT INTO `sys_module_lang` VALUES ('select_dictionary', 'zh', '选择数据字典');
INSERT INTO `sys_module_lang` VALUES ('select_fragment', 'en', 'Template fragment');
INSERT INTO `sys_module_lang` VALUES ('select_fragment', 'ja', 'テンプレートフラグメント');
INSERT INTO `sys_module_lang` VALUES ('select_fragment', 'zh', '模板片段');
INSERT INTO `sys_module_lang` VALUES ('select_place', 'en', 'Select page fragment');
INSERT INTO `sys_module_lang` VALUES ('select_place', 'ja', 'ページのセグメントを選択します');
INSERT INTO `sys_module_lang` VALUES ('select_place', 'zh', '选择页面片段');
INSERT INTO `sys_module_lang` VALUES ('select_survey', 'en', 'Select survey');
INSERT INTO `sys_module_lang` VALUES ('select_survey', 'ja', 'アンケート選択');
INSERT INTO `sys_module_lang` VALUES ('select_survey', 'zh', '选择问卷');
INSERT INTO `sys_module_lang` VALUES ('select_tag', 'en', 'Select tag');
INSERT INTO `sys_module_lang` VALUES ('select_tag', 'ja', 'タグを選択');
INSERT INTO `sys_module_lang` VALUES ('select_tag', 'zh', '选择标签');
INSERT INTO `sys_module_lang` VALUES ('select_tag_type', 'en', 'Select tag type');
INSERT INTO `sys_module_lang` VALUES ('select_tag_type', 'ja', 'タグの種類を選択');
INSERT INTO `sys_module_lang` VALUES ('select_tag_type', 'zh', '选择标签类型');
INSERT INTO `sys_module_lang` VALUES ('select_template', 'en', 'Select template');
INSERT INTO `sys_module_lang` VALUES ('select_template', 'ja', 'テンプレートを選択');
INSERT INTO `sys_module_lang` VALUES ('select_template', 'zh', '选择模板');
INSERT INTO `sys_module_lang` VALUES ('select_user', 'en', 'Select user');
INSERT INTO `sys_module_lang` VALUES ('select_user', 'ja', 'ユーザーを選択');
INSERT INTO `sys_module_lang` VALUES ('select_user', 'zh', '选择用户');
INSERT INTO `sys_module_lang` VALUES ('select_vote', 'en', 'Select vote');
INSERT INTO `sys_module_lang` VALUES ('select_vote', 'ja', '投票を選択');
INSERT INTO `sys_module_lang` VALUES ('select_vote', 'zh', '选择投票');
INSERT INTO `sys_module_lang` VALUES ('survey_add', 'en', 'Add/edit');
INSERT INTO `sys_module_lang` VALUES ('survey_add', 'ja', '追加/変更');
INSERT INTO `sys_module_lang` VALUES ('survey_add', 'zh', '增加/修改');
INSERT INTO `sys_module_lang` VALUES ('survey_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('survey_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('survey_delete', 'zh', '删除');
INSERT INTO `sys_module_lang` VALUES ('survey_list', 'en', 'Survey management');
INSERT INTO `sys_module_lang` VALUES ('survey_list', 'ja', 'アンケート管理');
INSERT INTO `sys_module_lang` VALUES ('survey_list', 'zh', '问卷调查管理');
INSERT INTO `sys_module_lang` VALUES ('survey_question_list', 'en', 'Question management');
INSERT INTO `sys_module_lang` VALUES ('survey_question_list', 'ja', '質問管理');
INSERT INTO `sys_module_lang` VALUES ('survey_question_list', 'zh', '问题管理');
INSERT INTO `sys_module_lang` VALUES ('survey_user_list', 'en', 'Answer management');
INSERT INTO `sys_module_lang` VALUES ('survey_user_list', 'ja', '回答管理');
INSERT INTO `sys_module_lang` VALUES ('survey_user_list', 'zh', '答案管理');
INSERT INTO `sys_module_lang` VALUES ('survey_view', 'en', 'View');
INSERT INTO `sys_module_lang` VALUES ('survey_view', 'ja', '見る');
INSERT INTO `sys_module_lang` VALUES ('survey_view', 'zh', '查看');
INSERT INTO `sys_module_lang` VALUES ('tag_add', 'en', 'Add/edit');
INSERT INTO `sys_module_lang` VALUES ('tag_add', 'ja', '追加/変更');
INSERT INTO `sys_module_lang` VALUES ('tag_add', 'zh', '增加/修改');
INSERT INTO `sys_module_lang` VALUES ('tag_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('tag_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('tag_delete', 'zh', '删除');
INSERT INTO `sys_module_lang` VALUES ('tag_list', 'en', 'Tag management');
INSERT INTO `sys_module_lang` VALUES ('tag_list', 'ja', 'ラベル管理');
INSERT INTO `sys_module_lang` VALUES ('tag_list', 'zh', '标签管理');
INSERT INTO `sys_module_lang` VALUES ('tag_type_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('tag_type_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('tag_type_delete', 'zh', '删除');
INSERT INTO `sys_module_lang` VALUES ('tag_type_list', 'en', 'Tag type');
INSERT INTO `sys_module_lang` VALUES ('tag_type_list', 'ja', 'タグの分類');
INSERT INTO `sys_module_lang` VALUES ('tag_type_list', 'zh', '标签分类');
INSERT INTO `sys_module_lang` VALUES ('tag_type_save', 'en', 'Add/edit');
INSERT INTO `sys_module_lang` VALUES ('tag_type_save', 'ja', '追加/変更');
INSERT INTO `sys_module_lang` VALUES ('tag_type_save', 'zh', '增加/修改');
INSERT INTO `sys_module_lang` VALUES ('task_add', 'en', 'Add/edit');
INSERT INTO `sys_module_lang` VALUES ('task_add', 'ja', '追加/変更');
INSERT INTO `sys_module_lang` VALUES ('task_add', 'zh', '增加/修改');
INSERT INTO `sys_module_lang` VALUES ('task_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('task_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('task_delete', 'zh', '删除');
INSERT INTO `sys_module_lang` VALUES ('task_list', 'en', 'Task management');
INSERT INTO `sys_module_lang` VALUES ('task_list', 'ja', 'タスク計画');
INSERT INTO `sys_module_lang` VALUES ('task_list', 'zh', '任务计划');
INSERT INTO `sys_module_lang` VALUES ('task_pause', 'en', 'Pause');
INSERT INTO `sys_module_lang` VALUES ('task_pause', 'ja', '停止');
INSERT INTO `sys_module_lang` VALUES ('task_pause', 'zh', '暂停');
INSERT INTO `sys_module_lang` VALUES ('task_recreate', 'en', 'Recreate');
INSERT INTO `sys_module_lang` VALUES ('task_recreate', 'ja', 'リセット');
INSERT INTO `sys_module_lang` VALUES ('task_recreate', 'zh', '重新初始化');
INSERT INTO `sys_module_lang` VALUES ('task_resume', 'en', 'Resume');
INSERT INTO `sys_module_lang` VALUES ('task_resume', 'ja', '回復');
INSERT INTO `sys_module_lang` VALUES ('task_resume', 'zh', '恢复');
INSERT INTO `sys_module_lang` VALUES ('task_runonce', 'en', 'Run once');
INSERT INTO `sys_module_lang` VALUES ('task_runonce', 'ja', '実行');
INSERT INTO `sys_module_lang` VALUES ('task_runonce', 'zh', '立刻执行');
INSERT INTO `sys_module_lang` VALUES ('task_template_content', 'en', 'Edit');
INSERT INTO `sys_module_lang` VALUES ('task_template_content', 'ja', '変更');
INSERT INTO `sys_module_lang` VALUES ('task_template_content', 'zh', '修改');
INSERT INTO `sys_module_lang` VALUES ('task_template_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('task_template_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('task_template_delete', 'zh', '删除');
INSERT INTO `sys_module_lang` VALUES ('task_template_fragment', 'en', 'Task script fragment');
INSERT INTO `sys_module_lang` VALUES ('task_template_fragment', 'ja', 'タスク計画スクリプト断片');
INSERT INTO `sys_module_lang` VALUES ('task_template_fragment', 'zh', '任务计划脚本片段');
INSERT INTO `sys_module_lang` VALUES ('task_template_help', 'en', 'help');
INSERT INTO `sys_module_lang` VALUES ('task_template_help', 'ja', 'ヘルプ');
INSERT INTO `sys_module_lang` VALUES ('task_template_help', 'zh', '帮助');
INSERT INTO `sys_module_lang` VALUES ('task_template_list', 'en', 'Task template management');
INSERT INTO `sys_module_lang` VALUES ('task_template_list', 'ja', 'タスク計画スクリプト');
INSERT INTO `sys_module_lang` VALUES ('task_template_list', 'zh', '任务计划脚本');
INSERT INTO `sys_module_lang` VALUES ('template_content', 'en', 'Edit');
INSERT INTO `sys_module_lang` VALUES ('template_content', 'ja', '変更');
INSERT INTO `sys_module_lang` VALUES ('template_content', 'zh', '修改');
INSERT INTO `sys_module_lang` VALUES ('template_content-type', 'en', 'Select content-type');
INSERT INTO `sys_module_lang` VALUES ('template_content-type', 'ja', 'content-typeを選択');
INSERT INTO `sys_module_lang` VALUES ('template_content-type', 'zh', '选择content-type');
INSERT INTO `sys_module_lang` VALUES ('template_export', 'en', 'Export');
INSERT INTO `sys_module_lang` VALUES ('template_export', 'ja', '輸出');
INSERT INTO `sys_module_lang` VALUES ('template_export', 'zh', '导出');
INSERT INTO `sys_module_lang` VALUES ('template_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('template_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('template_delete', 'zh', '删除');
INSERT INTO `sys_module_lang` VALUES ('template_demo', 'en', 'Template example');
INSERT INTO `sys_module_lang` VALUES ('template_demo', 'ja', 'テンプレートの例');
INSERT INTO `sys_module_lang` VALUES ('template_demo', 'zh', '模板示例');
INSERT INTO `sys_module_lang` VALUES ('template_fragment', 'en', 'Template fragment');
INSERT INTO `sys_module_lang` VALUES ('template_fragment', 'ja', 'テンプレートフラグメント');
INSERT INTO `sys_module_lang` VALUES ('template_fragment', 'zh', '模板片段');
INSERT INTO `sys_module_lang` VALUES ('template_help', 'en', 'Template help');
INSERT INTO `sys_module_lang` VALUES ('template_help', 'ja', 'テンプレートヘルプ');
INSERT INTO `sys_module_lang` VALUES ('template_help', 'zh', '模板帮助');
INSERT INTO `sys_module_lang` VALUES ('template_import', 'en', 'Import');
INSERT INTO `sys_module_lang` VALUES ('template_import', 'ja', '導入');
INSERT INTO `sys_module_lang` VALUES ('template_import', 'zh', '导入');
INSERT INTO `sys_module_lang` VALUES ('template_list', 'en', 'Template management');
INSERT INTO `sys_module_lang` VALUES ('template_list', 'ja', 'テンプレートファイル管理');
INSERT INTO `sys_module_lang` VALUES ('template_list', 'zh', '模板文件管理');
INSERT INTO `sys_module_lang` VALUES ('template_metadata', 'en', 'Edit metadata');
INSERT INTO `sys_module_lang` VALUES ('template_metadata', 'ja', 'メタデータの変更');
INSERT INTO `sys_module_lang` VALUES ('template_metadata', 'zh', '修改元数据');
INSERT INTO `sys_module_lang` VALUES ('template_search', 'en', 'Search');
INSERT INTO `sys_module_lang` VALUES ('template_search', 'ja', '検索');
INSERT INTO `sys_module_lang` VALUES ('template_search', 'zh', '搜索');
INSERT INTO `sys_module_lang` VALUES ('template_upload', 'en', 'Upload template');
INSERT INTO `sys_module_lang` VALUES ('template_upload', 'ja', 'テンプレートをアップロードする');
INSERT INTO `sys_module_lang` VALUES ('template_upload', 'zh', '上传模板');
INSERT INTO `sys_module_lang` VALUES ('template_website_file', 'en', 'Website file');
INSERT INTO `sys_module_lang` VALUES ('template_website_file', 'ja', 'ウェブサイトファイル');
INSERT INTO `sys_module_lang` VALUES ('template_website_file', 'zh', '网站文件');
INSERT INTO `sys_module_lang` VALUES ('trade', 'en', 'Trade');
INSERT INTO `sys_module_lang` VALUES ('trade', 'ja', 'ビジネス');
INSERT INTO `sys_module_lang` VALUES ('trade', 'zh', '商务');
INSERT INTO `sys_module_lang` VALUES ('user', 'en', 'User');
INSERT INTO `sys_module_lang` VALUES ('user', 'ja', 'ユーザー');
INSERT INTO `sys_module_lang` VALUES ('user', 'zh', '用户');
INSERT INTO `sys_module_lang` VALUES ('user_add', 'en', 'Add/edit');
INSERT INTO `sys_module_lang` VALUES ('user_add', 'ja', '追加/変更');
INSERT INTO `sys_module_lang` VALUES ('user_add', 'zh', '增加/修改');
INSERT INTO `sys_module_lang` VALUES ('user_disable', 'en', 'Disable');
INSERT INTO `sys_module_lang` VALUES ('user_disable', 'ja', '禁止');
INSERT INTO `sys_module_lang` VALUES ('user_disable', 'zh', '禁用');
INSERT INTO `sys_module_lang` VALUES ('user_enable', 'en', 'Enable');
INSERT INTO `sys_module_lang` VALUES ('user_enable', 'ja', 'オン');
INSERT INTO `sys_module_lang` VALUES ('user_enable', 'zh', '启用');
INSERT INTO `sys_module_lang` VALUES ('user_list', 'en', 'User management');
INSERT INTO `sys_module_lang` VALUES ('user_list', 'ja', 'ユーザー管理');
INSERT INTO `sys_module_lang` VALUES ('user_list', 'zh', '用户管理');
INSERT INTO `sys_module_lang` VALUES ('visit_day', 'en', 'Daily visit log');
INSERT INTO `sys_module_lang` VALUES ('visit_day', 'ja', '毎日の訪問ログ');
INSERT INTO `sys_module_lang` VALUES ('visit_day', 'zh', '日访问日志');
INSERT INTO `sys_module_lang` VALUES ('visit_history', 'en', 'Visit log');
INSERT INTO `sys_module_lang` VALUES ('visit_history', 'ja', 'アクセスログ');
INSERT INTO `sys_module_lang` VALUES ('visit_history', 'zh', '访问日志');
INSERT INTO `sys_module_lang` VALUES ('visit_item', 'en', 'Item visit log');
INSERT INTO `sys_module_lang` VALUES ('visit_item', 'ja', 'アイテム訪問ログ');
INSERT INTO `sys_module_lang` VALUES ('visit_item', 'zh', '项目访问日志');
INSERT INTO `sys_module_lang` VALUES ('visit_session', 'en', 'Visit session');
INSERT INTO `sys_module_lang` VALUES ('visit_session', 'ja', 'アクセスセッション');
INSERT INTO `sys_module_lang` VALUES ('visit_session', 'zh', '访问日志会话');
INSERT INTO `sys_module_lang` VALUES ('visit_url', 'en', 'Page visit log');
INSERT INTO `sys_module_lang` VALUES ('visit_url', 'ja', 'ページアクセスログ');
INSERT INTO `sys_module_lang` VALUES ('visit_url', 'zh', '页面访问日志');
INSERT INTO `sys_module_lang` VALUES ('vote_add', 'en', 'Add/edit');
INSERT INTO `sys_module_lang` VALUES ('vote_add', 'ja', '追加/変更');
INSERT INTO `sys_module_lang` VALUES ('vote_add', 'zh', '增加/修改');
INSERT INTO `sys_module_lang` VALUES ('vote_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('vote_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('vote_delete', 'zh', '删除');
INSERT INTO `sys_module_lang` VALUES ('vote_list', 'en', 'Voting Management');
INSERT INTO `sys_module_lang` VALUES ('vote_list', 'ja', '投票管理');
INSERT INTO `sys_module_lang` VALUES ('vote_list', 'zh', '投票管理');
INSERT INTO `sys_module_lang` VALUES ('vote_view', 'en', 'View');
INSERT INTO `sys_module_lang` VALUES ('vote_view', 'ja', '見る');
INSERT INTO `sys_module_lang` VALUES ('vote_view', 'zh', '查看');
INSERT INTO `sys_module_lang` VALUES ('webfile_content', 'en', 'Edit file');
INSERT INTO `sys_module_lang` VALUES ('webfile_content', 'ja', 'ファイルの変更');
INSERT INTO `sys_module_lang` VALUES ('webfile_content', 'zh', '修改文件');
INSERT INTO `sys_module_lang` VALUES ('webfile_directory', 'en', 'Create Directory');
INSERT INTO `sys_module_lang` VALUES ('webfile_directory', 'ja', '目録を作成');
INSERT INTO `sys_module_lang` VALUES ('webfile_directory', 'zh', '新建目录');
INSERT INTO `sys_module_lang` VALUES ('webfile_list', 'en', 'Website file management');
INSERT INTO `sys_module_lang` VALUES ('webfile_list', 'ja', 'ウェブサイトのファイル管理');
INSERT INTO `sys_module_lang` VALUES ('webfile_list', 'zh', '网站文件管理');
INSERT INTO `sys_module_lang` VALUES ('webfile_unzip', 'en', 'Decompress');
INSERT INTO `sys_module_lang` VALUES ('webfile_unzip', 'ja', '解凍');
INSERT INTO `sys_module_lang` VALUES ('webfile_unzip', 'zh', '解压缩');
INSERT INTO `sys_module_lang` VALUES ('webfile_upload', 'en', 'Upload');
INSERT INTO `sys_module_lang` VALUES ('webfile_upload', 'ja', 'アップロード');
INSERT INTO `sys_module_lang` VALUES ('webfile_upload', 'zh', '上传');
INSERT INTO `sys_module_lang` VALUES ('webfile_zip', 'en', 'Compress');
INSERT INTO `sys_module_lang` VALUES ('webfile_zip', 'ja', '圧縮');
INSERT INTO `sys_module_lang` VALUES ('webfile_zip', 'zh', '压缩');
INSERT INTO `sys_module_lang` VALUES ('word_list', 'en', 'Search word management');
INSERT INTO `sys_module_lang` VALUES ('word_list', 'ja', '検索ワード管理');
INSERT INTO `sys_module_lang` VALUES ('word_list', 'zh', '搜索词管理');
-- ----------------------------
-- Table structure for sys_record
-- ----------------------------
DROP TABLE IF EXISTS `sys_record`;
CREATE TABLE `sys_record` (
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `code` varchar(50) NOT NULL COMMENT '记录编码',
  `data` longtext NOT NULL COMMENT '数据',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `update_date` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY  (`site_id`, `code`),
  KEY `sys_record_site_id` (`site_id`,`create_date`)
) COMMENT='自定义记录';
-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` int(11) NOT NULL auto_increment,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `owns_all_right` tinyint(1) NOT NULL COMMENT '拥有全部权限',
  `show_all_module` tinyint(1) NOT NULL COMMENT '显示全部模块',
  PRIMARY KEY  (`id`),
  KEY `sys_role_site_id` (`site_id`)
) AUTO_INCREMENT=2 COMMENT='角色';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '1', 'superuser', '1', '0');

-- ----------------------------
-- Table structure for sys_role_authorized
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_authorized`;
CREATE TABLE `sys_role_authorized` (
  `role_id` int(11) NOT NULL COMMENT '角色',
  `url` varchar(100) NOT NULL COMMENT '授权地址',
  PRIMARY KEY  (`role_id`, `url`)
) COMMENT='角色授权地址';

-- ----------------------------
-- Records of sys_role_authorized
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role_module
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_module`;
CREATE TABLE `sys_role_module` (
  `role_id` int(11) NOT NULL COMMENT '角色',
  `module_id` varchar(30) NOT NULL COMMENT '模块',
  PRIMARY KEY  (`role_id`, `module_id`),
  KEY `sys_role_module_module_id` (`module_id`)
) COMMENT='角色授权模块';

-- ----------------------------
-- Records of sys_role_module
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_user`;
CREATE TABLE `sys_role_user` (
  `role_id` int(11) NOT NULL COMMENT '角色',
  `user_id` bigint(20) NOT NULL COMMENT '用户',
  PRIMARY KEY  (`role_id`, `user_id`),
  KEY `sys_role_user_user_id` (`user_id`)
) COMMENT='用户角色';

-- ----------------------------
-- Records of sys_role_user
-- ----------------------------
INSERT INTO `sys_role_user` VALUES ('1', '1');

-- ----------------------------
-- Table structure for sys_site
-- ----------------------------
DROP TABLE IF EXISTS `sys_site`;
CREATE TABLE `sys_site` (
  `id` smallint(6) NOT NULL auto_increment,
  `parent_id` smallint(6) DEFAULT NULL COMMENT '父站点',
  `directory` varchar(50) NULL COMMENT '目录',
  `name` varchar(50) NOT NULL COMMENT '站点名',
  `use_static` tinyint(1) NOT NULL COMMENT '启用静态化',
  `site_path` varchar(255) NOT NULL COMMENT '站点地址',
  `use_ssi` tinyint(1) NOT NULL COMMENT '启用服务器端包含',
  `dynamic_path` varchar(255) NOT NULL COMMENT '动态站点地址',
  `disabled` tinyint(1) NOT NULL COMMENT '禁用',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `sys_site_parent_id` (`parent_id`, `directory`),
  KEY `sys_site_disabled` (`disabled`)
) AUTO_INCREMENT=2 COMMENT='站点';

-- ----------------------------
-- Records of sys_site
-- ----------------------------
INSERT INTO `sys_site` VALUES ('1', null ,null ,'PublicCMS', '0', '//dev.publiccms.com:8080/webfile/', '0', '//dev.publiccms.com:8080/', '0');
-- ----------------------------
-- Table structure for sys_task
-- ----------------------------
DROP TABLE IF EXISTS `sys_task`;
CREATE TABLE `sys_task` (
  `id` int(11) NOT NULL auto_increment,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `status` int(11) NOT NULL COMMENT '状态',
  `multi_node` tinyint(1) NOT NULL COMMENT '多节点执行',
  `cron_expression` varchar(50) NOT NULL COMMENT '表达式',
  `description` varchar(300) default NULL COMMENT '描述',
  `file_path` varchar(255) default NULL COMMENT '文件路径',
  `update_date` datetime default NULL COMMENT '更新时间',
  PRIMARY KEY  (`id`),
  KEY `sys_task_status` (`status`),
  KEY `sys_task_site_id` (`site_id`),
  KEY `sys_task_update_date` (`update_date`)
) COMMENT='任务计划';

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL auto_increment,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `name` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(150) NOT NULL COMMENT '混淆码.密码',
  `weak_password` tinyint(1) NOT NULL DEFAULT '0' COMMENT '弱密码',
  `nickname` varchar(45) NOT NULL COMMENT '昵称',
  `cover` varchar(255) default NULL COMMENT '封面',
  `dept_id` int(11) default NULL COMMENT '部门',
  `content_permissions` int(11) NOT NULL COMMENT '内容权限(0仅自己,1所有人,2本部门)',
  `roles` text COMMENT '角色',
  `email` varchar(100) default NULL COMMENT '邮箱地址',
  `email_checked` tinyint(1) NOT NULL COMMENT '已验证邮箱',
  `superuser` tinyint(1) NOT NULL COMMENT '是否管理员',
  `disabled` tinyint(1) NOT NULL COMMENT '是否禁用',
  `last_login_date` datetime default NULL COMMENT '最后登录日期',
  `last_login_ip` varchar(130) default NULL COMMENT '最后登录ip',
  `login_count` int(11) NOT NULL COMMENT '登录次数',
  `registered_date` datetime default NULL COMMENT '注册日期',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `sys_user_name`(`site_id`, `name`),
  KEY `sys_user_email`(`site_id`, `email`, `email_checked`),
  KEY `sys_user_disabled`(`site_id`, `disabled`),
  KEY `sys_user_dept_id`(`site_id`, `registered_date`, `disabled`)
) AUTO_INCREMENT=2 COMMENT='用户';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', '1', 'admin', '0123456789.2134b56595c73a647716b0a8e33f9d50243fb1c1a088597ba5aa6d9ccadacbd8fc8307bda2adfc8362abe611420bd48263bdcfd91c1c26566ad3a29d79cffd9c', 1, 'admin', NULL, '1', '1', '1', 'master@sanluan.com', '0', '1', '0', '2019-01-01 00:00:00', '127.0.0.1', '0', '2019-01-01 00:00:00');

-- ----------------------------
-- Table structure for sys_user_attribute
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_attribute`;
CREATE TABLE `sys_user_attribute` (
  `user_id` bigint(20) NOT NULL,
  `settings` text NULL COMMENT '扩展文本字段',
  `data` longtext COMMENT '数据JSON',
  PRIMARY KEY  (`user_id`)
) COMMENT='用户扩展';

-- ----------------------------
-- Table structure for sys_user_token
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_token`;
CREATE TABLE `sys_user_token` (
  `auth_token` varchar(40) NOT NULL COMMENT '登录授权',
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `user_id` bigint(20) NOT NULL COMMENT '用户',
  `channel` varchar(50) NOT NULL COMMENT '渠道',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `expiry_date` datetime DEFAULT NULL COMMENT '过期日期',
  `login_ip` varchar(130) NOT NULL COMMENT '登录IP',
  PRIMARY KEY  (`auth_token`),
  KEY `sys_user_token_site_id`(`site_id`, `user_id`, `create_date`),
  KEY `sys_user_token_expiry_date`(`expiry_date`),
  KEY `sys_user_token_user_id`(`user_id`)
) COMMENT='用户令牌';

-- ----------------------------
-- Table structure for trade_account
-- ----------------------------
DROP TABLE IF EXISTS `trade_account`;
CREATE TABLE `trade_account`  (
  `id` bigint(20) NOT NULL COMMENT '用户',
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `amount` decimal(10, 2) NOT NULL COMMENT '金额',
  `update_date` datetime NULL DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`),
  KEY `trade_account_site_id`(`site_id`, `update_date`)
) COMMENT = '资金账户';

-- ----------------------------
-- Table structure for trade_account_history
-- ----------------------------
DROP TABLE IF EXISTS `trade_account_history`;
CREATE TABLE `trade_account_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `serial_number` varchar(100) NOT NULL COMMENT '流水号',
  `account_id` bigint(20) NOT NULL COMMENT '账户',
  `user_id` bigint(20) DEFAULT NULL COMMENT '操作用户',
  `amount_change` decimal(10,2) NOT NULL COMMENT '变动金额',
  `amount` decimal(10,2) NOT NULL COMMENT '变动金额',
  `balance` decimal(10,2) NOT NULL COMMENT '变动金额',
  `status` int(11) NOT NULL COMMENT '类型:0预充值,1消费,2充值,3退款',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`),
  KEY `trade_account_history_site_id` (`site_id`, `account_id`, `status`),
  KEY `trade_account_history_create_date` (`site_id`, `create_date`)
) COMMENT='账户流水';

-- ----------------------------
-- Table structure for trade_address
-- ----------------------------
DROP TABLE IF EXISTS `trade_address`;
CREATE TABLE `trade_address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `user_id` bigint(20) NOT NULL COMMENT '用户',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `addressee` varchar(50) DEFAULT NULL COMMENT '收件人',
  `telephone` varchar(50) DEFAULT NULL COMMENT '电话',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`)
) COMMENT='用户地址';

-- ----------------------------
-- Table structure for trade_payment
-- ----------------------------
DROP TABLE IF EXISTS `trade_payment`;
CREATE TABLE `trade_payment`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `user_id` bigint(20) NOT NULL COMMENT '用户',
  `amount` decimal(10, 2) NOT NULL COMMENT '金额',
  `description` varchar(255) NULL DEFAULT NULL COMMENT '描述',
  `trade_type` varchar(20) NOT NULL COMMENT '订单类型',
  `serial_number` varchar(100) NOT NULL COMMENT '订单流水',
  `account_type` varchar(20) NOT NULL COMMENT '支付账户类型',
  `account_serial_number` varchar(100) NULL DEFAULT NULL COMMENT '支付账号流水',
  `ip` varchar(130) NOT NULL COMMENT 'IP地址',
  `status` int(11) NOT NULL COMMENT '状态:0待支付,1已支付,2待退款,3已退款,4已关闭',
  `processed` tinyint(1) NOT NULL COMMENT '已处理',
  `process_user_id` bigint(20) NULL COMMENT '处理用户',
  `update_date` datetime DEFAULT NULL COMMENT '更新日期',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `process_date` datetime DEFAULT NULL COMMENT '处理日期',
  `payment_date` datetime NULL DEFAULT NULL COMMENT '支付日期',
  PRIMARY KEY (`id`),
  KEY `trade_payment_account_type`(`site_id`, `account_type`, `account_serial_number`),
  KEY `trade_payment_site_id`(`site_id`, `user_id`, `status`),
  KEY `trade_payment_trade_type`(`site_id`, `trade_type`, `serial_number`),
  KEY `trade_payment_create_date` (`site_id`, `create_date`)
) COMMENT = '支付订单';

-- ----------------------------
-- Table structure for trade_payment_history
-- ----------------------------
DROP TABLE IF EXISTS `trade_payment_history`;
CREATE TABLE `trade_payment_history`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `payment_id` bigint(20) NOT NULL COMMENT '订单',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `operate` varchar(100) NOT NULL COMMENT '操作',
  `content` text COMMENT '内容',
  PRIMARY KEY (`id`),
  KEY `trade_payment_history_site_id` (`site_id`, `payment_id`, `operate`),
  KEY `trade_payment_history_create_date` (`site_id`, `create_date`)
) COMMENT = '支付订单流水';
-- ----------------------------
-- Table structure for trade_order
-- ----------------------------
DROP TABLE IF EXISTS `trade_order`;
CREATE TABLE `trade_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `user_id` bigint(20) NOT NULL COMMENT '用户',
  `title` varchar(255) NOT NULL COMMENT '标题',
  `amount` decimal(10,2) NOT NULL COMMENT '总金额',
  `payment_id` bigint(20) DEFAULT NULL COMMENT '支付订单',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `addressee` varchar(100) DEFAULT NULL COMMENT '收件人',
  `telephone` varchar(100) DEFAULT NULL COMMENT '电话',
  `ip` varchar(130) NOT NULL COMMENT 'IP地址',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `status` int(11) NOT NULL COMMENT '状态:0待确认,1无效订单,2已付款,3已退款,4已关闭',
  `confirmed` tinyint(1) NOT NULL COMMENT '已确认',
  `processed` tinyint(1) NOT NULL COMMENT '已处理',
  `process_user_id` bigint(20) NULL COMMENT '处理用户',
  `process_info` varchar(255) DEFAULT NULL COMMENT '处理信息',
  `update_date` datetime DEFAULT NULL COMMENT '更新日期',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `process_date` datetime DEFAULT NULL COMMENT '处理日期',
  `payment_date` datetime DEFAULT NULL COMMENT '支付日期',
  PRIMARY KEY (`id`),
  KEY `trade_order_site_id` (`site_id`, `user_id`, `status`),
  KEY `trade_order_create_date` (`site_id`, `create_date`),
  KEY `trade_order_payment_id` (`site_id`, `payment_id`)
) COMMENT='产品订单';
-- ----------------------------
-- Table structure for trade_order_history
-- ----------------------------
DROP TABLE IF EXISTS `trade_order_history`;
CREATE TABLE `trade_order_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `order_id` bigint(20) NOT NULL COMMENT '订单',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `operate` varchar(100) NOT NULL COMMENT '操作',
  `content` text COMMENT '内容',
  PRIMARY KEY (`id`),
  KEY `trade_order_history_site_id` (`site_id`, `order_id`, `operate`),
  KEY `trade_order_history_create_date` (`site_id`, `create_date`)
) COMMENT='订单流水';
-- ----------------------------
-- Table structure for trade_order_product
-- ----------------------------
DROP TABLE IF EXISTS `trade_order_product`;
CREATE TABLE `trade_order_product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `order_id` bigint(20) NOT NULL COMMENT '用户',
  `content_id` bigint(20) NOT NULL COMMENT '内容',
  `product_id` bigint(20) NOT NULL COMMENT '产品',
  `price` decimal(10,2) NOT NULL COMMENT '价格',
  `quantity` int(11) NOT NULL COMMENT '数量',
  `amount` decimal(10,2) NOT NULL COMMENT '总金额',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `trade_order_product_site_id` (`site_id`, `order_id`)
) COMMENT='产品订单';
-- ----------------------------
-- Table structure for trade_refund
-- ----------------------------
DROP TABLE IF EXISTS `trade_refund`;
CREATE TABLE `trade_refund` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `site_id` smallint(0) NOT NULL COMMENT '站点',
  `payment_id` bigint(20) NOT NULL COMMENT '订单',
  `user_id` bigint(20) NOT NULL COMMENT '用户',
  `amount` decimal(10,2) NOT NULL COMMENT '申请退款金额',
  `reason` varchar(255) DEFAULT NULL COMMENT '退款原因',
  `update_date` datetime DEFAULT NULL COMMENT '更新日期',
  `refund_user_id` bigint(20) DEFAULT NULL COMMENT '退款操作人员',
  `refund_amount` decimal(10,2) DEFAULT NULL COMMENT '退款金额',
  `status` int(11) NOT NULL COMMENT '状态:0待退款,1已退款,2取消退款,3拒绝退款,4退款失败',
  `reply` varchar(255) DEFAULT NULL COMMENT '回复',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `processing_date` datetime DEFAULT NULL COMMENT '处理日期',
  PRIMARY KEY (`id`),
  KEY `trade_refund_create_date` (`site_id`, `create_date`),
  KEY `trade_refund_user_id` (`site_id`, `user_id`, `status`)
) COMMENT='退款申请';
-- ----------------------------
-- Table structure for visit_day
-- ----------------------------
DROP TABLE IF EXISTS `visit_day`;
CREATE TABLE `visit_day` (
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `visit_date` date NOT NULL COMMENT '日期',
  `visit_hour` tinyint(4) NOT NULL COMMENT '小时',
  `pv` bigint(20) NOT NULL COMMENT 'Page Views',
  `uv` bigint(20) DEFAULT NULL COMMENT 'User Views',
  `ipviews` bigint(20) DEFAULT NULL COMMENT 'IP数',
  PRIMARY KEY (`site_id`, `visit_date`, `visit_hour`),
  KEY `visit_session_id` (`site_id`, `visit_date`)
) COMMENT = '访问汇总';

-- ----------------------------
-- Table structure for visit_history
-- ----------------------------
DROP TABLE IF EXISTS `visit_history`;
CREATE TABLE `visit_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `session_id` varchar(50) NOT NULL COMMENT '会话',
  `visit_date` date NOT NULL COMMENT '访问日期',
  `visit_hour` tinyint(4) NOT NULL COMMENT '访问小时',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户',
  `ip` varchar(130) NOT NULL COMMENT 'IP',
  `user_agent` varchar(500) DEFAULT NULL COMMENT 'User Agent',
  `url` varchar(2048) NOT NULL COMMENT '访问路径',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `screen_width` int(11) DEFAULT NULL COMMENT '屏幕宽度',
  `screen_height` int(11) DEFAULT NULL COMMENT '屏幕高度',
  `referer_url` varchar(2048) DEFAULT NULL COMMENT '来源URL',
  `item_type` varchar(50) DEFAULT NULL COMMENT '项目类型',
  `item_id` varchar(50) DEFAULT NULL COMMENT '项目',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`),
  KEY `visit_history_create_date` (`site_id`, `create_date`, `session_id`, `ip`),
  KEY `visit_history_user_id` (`site_id`, `create_date`, `user_id`),
  KEY `visit_history_visit_date` (`site_id`, `visit_date`, `visit_hour`),
  KEY `visit_history_item_type` (`site_id`, `visit_date`, `item_type`)
) COMMENT='访问日志';

-- ----------------------------
-- Table structure for visit_item
-- ----------------------------
DROP TABLE IF EXISTS `visit_item`;
CREATE TABLE `visit_item` (
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `visit_date` date NOT NULL COMMENT '日期',
  `item_type` varchar(50) NOT NULL COMMENT '项目类型',
  `item_id` varchar(50) NOT NULL COMMENT '项目',
  `pv` bigint(20) NOT NULL COMMENT 'Page Views',
  `uv` bigint(20) DEFAULT NULL COMMENT 'User Views',
  `ipviews` bigint(20) DEFAULT NULL COMMENT 'IP数',
  PRIMARY KEY (`site_id`, `visit_date`, `item_type`, `item_id`),
  KEY `visit_item_visit_date` (`site_id`, `visit_date`, `item_type`, `item_id`, `pv`)
) COMMENT='项目访问汇总';

-- ----------------------------
-- Table structure for visit_session
-- ----------------------------
DROP TABLE IF EXISTS `visit_session`;
CREATE TABLE `visit_session` (
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `session_id` varchar(50) NOT NULL COMMENT '会话',
  `visit_date` date NOT NULL COMMENT '日期',
  `last_visit_date` datetime DEFAULT NULL COMMENT '上次访问日期',
  `first_visit_date` datetime DEFAULT NULL COMMENT '首次访问日期',
  `ip` varchar(130) NOT NULL COMMENT 'IP',
  `pv` bigint(20) NOT NULL COMMENT 'PV',
  PRIMARY KEY (`site_id`, `session_id`, `visit_date`),
  KEY `visit_session_visit_date` (`site_id`, `visit_date`, `session_id`, `ip`, `last_visit_date`)
) COMMENT = '访问会话';

-- ----------------------------
-- Table structure for visit_url
-- ----------------------------
DROP TABLE IF EXISTS `visit_url`;
CREATE TABLE `visit_url` (
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `visit_date` date NOT NULL COMMENT '日期',
  `url_md5` varchar(50) NOT NULL COMMENT 'URL MD5',
  `url_sha` varchar(100) NOT NULL COMMENT 'URL SHA',
  `url` varchar(2048) NOT NULL COMMENT 'URL',
  `pv` bigint(20) NOT NULL COMMENT 'Page Views',
  `uv` bigint(20) DEFAULT NULL COMMENT 'User Views',
  `ipviews` bigint(20) DEFAULT NULL COMMENT 'IP数',
  PRIMARY KEY (`site_id`, `visit_date`, `url_md5`, `url_sha`),
  KEY `visit_url_pv` (`site_id`, `visit_date`, `pv`)
) COMMENT='页面访问汇总';
SET FOREIGN_KEY_CHECKS = 1;