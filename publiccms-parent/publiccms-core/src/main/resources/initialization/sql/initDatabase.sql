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
  `code` varchar(50) NOT NULL COMMENT '编码',
  `template_path` varchar(255) default NULL COMMENT '模板路径',
  `path` varchar(1000) DEFAULT NULL COMMENT '首页路径',
  `only_url` tinyint(1) NOT NULL COMMENT '外链',
  `has_static` tinyint(1) NOT NULL COMMENT '已经静态化',
  `url` varchar(1000) default NULL COMMENT '首页地址',
  `content_path` varchar(1000) default NULL COMMENT '内容路径',
  `contain_child` tinyint(1) NOT NULL DEFAULT 1 COMMENT '包含子分类内容',
  `page_size` int(11) default NULL COMMENT '每页数据条数',
  `allow_contribute` tinyint(1) NOT NULL COMMENT '允许投稿',
  `sort` int(11) NOT NULL default '0' COMMENT '顺序',
  `hidden` tinyint(1) NOT NULL COMMENT '隐藏',
  `disabled` tinyint(1) NOT NULL COMMENT '是否删除',
  `extend_id` int(11) default NULL COMMENT '扩展ID',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `cms_category_code` (`site_id`,`code`),
  KEY `cms_category_sort` (`sort`),
  KEY `cms_category_type_id` (`type_id`,`allow_contribute`),
  KEY `cms_category_site_id` (`site_id`,`parent_id`,`hidden`,`disabled`)
) COMMENT='分类';

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
) COMMENT='分类扩展';

-- ----------------------------
-- Table structure for cms_category_model
-- ----------------------------
DROP TABLE IF EXISTS `cms_category_model`;
CREATE TABLE `cms_category_model` (
  `category_id` int(11) NOT NULL COMMENT '分类ID',
  `model_id` varchar(20) NOT NULL COMMENT '模型编码',
  `template_path` varchar(200) default NULL COMMENT '内容模板路径',
  PRIMARY KEY  (`category_id`,`model_id`)
) COMMENT='分类模型';

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
  KEY `cms_category_type_site_id` (`site_id`)
) COMMENT='分类类型';
-- ----------------------------
-- Table structure for cms_comment
-- ----------------------------
DROP TABLE IF EXISTS `cms_comment`;
CREATE TABLE `cms_comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `site_id` smallint(6) NOT NULL COMMENT '站点ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `reply_id` bigint(20) DEFAULT NULL COMMENT '回复ID',
  `reply_user_id` bigint(20) DEFAULT NULL COMMENT '回复用户ID',
  `replies` int(11) NOT NULL default 0 COMMENT '回复数',
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
  KEY `cms_comment_update_date` (`update_date`,`create_date`),
  KEY `cms_comment_reply_id` (`site_id`,`reply_user_id`,`reply_id`)
) COMMENT='评论';
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
  `quote_content_id` bigint(20) NULL COMMENT '引用内容ID',
  `copied` tinyint(1) NOT NULL COMMENT '是否转载',
  `contribute` tinyint(1) NOT NULL default 0 COMMENT '是否投稿',
  `author` varchar(50) default NULL COMMENT '作者',
  `editor` varchar(50) default NULL COMMENT '编辑',
  `only_url` tinyint(1) NOT NULL COMMENT '外链',
  `has_images` tinyint(1) NOT NULL COMMENT '拥有图片列表',
  `has_files` tinyint(1) NOT NULL COMMENT '拥有附件列表',
  `has_static` tinyint(1) NOT NULL COMMENT '已经静态化',
  `url` varchar(1000) default NULL COMMENT '地址',
  `description` varchar(300) default NULL COMMENT '简介',
  `tag_ids` text default NULL COMMENT '标签',
  `dictionary_values` text default NULL COMMENT '数据字典值',
  `cover` varchar(255) default NULL COMMENT '封面',
  `childs` int(11) NOT NULL COMMENT '子内容数',
  `scores` int(11) NOT NULL COMMENT '分数',
  `comments` int(11) NOT NULL COMMENT '评论数',
  `clicks` int(11) NOT NULL COMMENT '点击数',
  `publish_date` datetime NOT NULL COMMENT '发布日期',
  `expiry_date` datetime default NULL COMMENT '过期日期',
  `check_date` datetime default NULL COMMENT '审核日期',
  `update_date` datetime default NULL COMMENT '更新日期',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `sort` int(11) NOT NULL default '0' COMMENT '顺序',
  `status` int(11) NOT NULL COMMENT '状态：0、草稿 1、已发布 2、待审核',
  `disabled` tinyint(1) NOT NULL COMMENT '是否删除',
  PRIMARY KEY  (`id`),
  KEY `cms_content_check_date` (`check_date`,`update_date`),
  KEY `cms_content_scores` (`scores`,`comments`,`clicks`),
  KEY `cms_content_only_url` (`only_url`,`has_images`,`has_files`,`user_id`),
  KEY `cms_content_status` (`site_id`,`status`,`category_id`,`disabled`,`model_id`,`parent_id`,`sort`,`publish_date`,`expiry_date`),
  KEY `cms_content_quote_content_id`(`site_id`, `quote_content_id`)
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
  `file_size` bigint(20) NOT NULL COMMENT '文件大小',
  `width` int(11) NULL COMMENT '宽度',
  `height` int(11) NULL COMMENT '高度',
  `clicks` int(11) NOT NULL COMMENT '点击数',
  `sort` int(11) NOT NULL COMMENT '排序',
  `description` varchar(300) default NULL COMMENT '描述',
  PRIMARY KEY  (`id`),
  KEY `cms_content_file_content_id` (`content_id`),
  KEY `cms_content_file_sort` (`sort`),
  KEY `cms_content_file_file_type`(`file_type`),
  KEY `cms_content_file_file_size` (`file_size`),
  KEY `cms_content_file_clicks` (`clicks`),
  KEY `cms_content_file_user_id` (`user_id`)
) COMMENT='内容附件';

-- ----------------------------
-- Table structure for cms_content_related
-- ----------------------------
DROP TABLE IF EXISTS `cms_content_related`;
CREATE TABLE `cms_content_related` (
  `id` bigint(20) NOT NULL auto_increment,
  `content_id` bigint(20) NOT NULL COMMENT '内容',
  `related_content_id` bigint(20) default NULL COMMENT '推荐内容',
  `user_id` bigint(20) NOT NULL COMMENT '推荐用户',
  `url` varchar(1000) default NULL COMMENT '推荐链接地址',
  `title` varchar(255) default NULL COMMENT '推荐标题',
  `description` varchar(300) default NULL COMMENT '推荐简介',
  `sort` int(11) NOT NULL COMMENT '排序',
  PRIMARY KEY  (`id`),
  KEY `cms_content_related_user_id` (`content_id`,`related_content_id`,`user_id`,`sort`),
  KEY `cms_content_related_related_content_id` (`related_content_id`)
) COMMENT='推荐推荐';

-- ----------------------------
-- Table structure for cms_dictionary
-- ----------------------------
DROP TABLE IF EXISTS `cms_dictionary`;
CREATE TABLE `cms_dictionary` (
  `id` varchar(20) NOT NULL,
  `site_id` smallint(6) NOT NULL COMMENT '站点ID',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `multiple` tinyint(1) NOT NULL COMMENT '允许多选',
  PRIMARY KEY (`id`,`site_id`),
  KEY `cms_dictionary_site_id` (`site_id`,`multiple`)
) COMMENT='字典';

-- ----------------------------
-- Table structure for cms_dictionary_data
-- ----------------------------
DROP TABLE IF EXISTS `cms_dictionary_data`;
CREATE TABLE `cms_dictionary_data` (
  `dictionary_id` varchar(20) NOT NULL COMMENT '字典',
  `site_id` smallint(6) NOT NULL COMMENT '站点ID',
  `value` varchar(50) NOT NULL COMMENT '值',
  `text` varchar(100) NOT NULL COMMENT '文字',
  PRIMARY KEY  (`dictionary_id`,`site_id`,`value`)
) COMMENT='字典数据';

-- ----------------------------
-- Table structure for cms_place
-- ----------------------------
DROP TABLE IF EXISTS `cms_place`;
CREATE TABLE `cms_place` (
  `id` bigint(20) NOT NULL auto_increment,
  `site_id` smallint(6) NOT NULL COMMENT '站点ID',
  `path` varchar(100) NOT NULL COMMENT '模板路径',
  `user_id` bigint(20) default NULL COMMENT '提交用户',
  `check_user_id` bigint(20) default NULL COMMENT '审核用户',
  `item_type` varchar(50) default NULL COMMENT '推荐项目类型',
  `item_id` bigint(20) default NULL COMMENT '推荐项目ID',
  `title` varchar(255) NOT NULL COMMENT '标题',
  `url` varchar(1000) default NULL COMMENT '超链接',
  `cover` varchar(255) default NULL COMMENT '封面图',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `publish_date` datetime NOT NULL COMMENT '发布日期',
  `expiry_date` datetime default NULL COMMENT '过期日期',
  `status` int(11) NOT NULL COMMENT '状态：0、前台提交 1、已发布 ',
  `clicks` int(11) NOT NULL COMMENT '点击数',
  `disabled` tinyint(1) NOT NULL COMMENT '已禁用',
  PRIMARY KEY  (`id`),
  KEY `cms_place_clicks` (`clicks`),
  KEY `cms_place_site_id` (`site_id`,`path`,`status`,`disabled`),
  KEY `cms_place_item_type` (`item_type`,`item_id`),
  KEY `cms_place_user_id` (`user_id`,`check_user_id`),
  KEY `cms_place_publish_date` (`publish_date`,`create_date`,`expiry_date`)
) COMMENT='页面数据';

-- ----------------------------
-- Table structure for cms_place_attribute
-- ----------------------------
DROP TABLE IF EXISTS `cms_place_attribute`;
CREATE TABLE `cms_place_attribute` (
  `place_id` bigint(20) NOT NULL COMMENT '位置ID',
  `data` longtext COMMENT '数据JSON',
  PRIMARY KEY  (`place_id`)
) COMMENT='推荐位数据扩展';

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
  KEY `cms_tag_site_id` (`site_id`)
) COMMENT='标签';

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
  KEY `cms_tag_type_site_id` (`site_id`)
) COMMENT='标签类型';


-- ----------------------------
-- Table structure for cms_user_score
-- ----------------------------
DROP TABLE IF EXISTS `cms_user_score`;
CREATE TABLE `cms_user_score`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `item_type` varchar(50) NOT NULL COMMENT '类型',
  `item_id` bigint(20) NOT NULL COMMENT '项目ID',
  `create_date` datetime NOT NULL,
  PRIMARY KEY (`user_id`, `item_type`, `item_id`),
  INDEX `cms_user_score_item_type`(`item_type`, `item_id`, `create_date`),
  INDEX `cms_user_score_user_id`(`user_id`, `create_date`)
) COMMENT = '用户评分表';

-- ----------------------------
-- Table structure for cms_user_vote
-- ----------------------------
DROP TABLE IF EXISTS `cms_user_vote`;
CREATE TABLE `cms_user_vote`  (
`vote_id` bigint(20) NOT NULL COMMENT '投票ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `item_id` bigint(20) NOT NULL COMMENT '投票选项',
  `ip` varchar(130) NOT NULL COMMENT 'IP',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY (`user_id`, `vote_id`),
  INDEX `cms_user_vote_vote_id`(`vote_id`, `ip`, `create_date`)
) COMMENT='投票用户';

-- ----------------------------
-- Table structure for cms_vote
-- ----------------------------
DROP TABLE IF EXISTS `cms_vote`;
CREATE TABLE `cms_vote`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `site_id` smallint(6) NOT NULL COMMENT '站点ID',
  `start_date` datetime NOT NULL COMMENT '开始日期',
  `end_date` datetime NULL COMMENT '结束日期',
  `scores` int(11) NOT NULL COMMENT '总票数',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `description` varchar(300) NULL DEFAULT NULL COMMENT '描述',
  `create_date` datetime NOT NULL,
  `disabled` tinyint(1) NOT NULL COMMENT '已禁用',
  PRIMARY KEY (`id`),
  INDEX `cms_vote_site_id`(`site_id`, `start_date`, `disabled`)
)  COMMENT='投票';

-- ----------------------------
-- Table structure for cms_vote_item
-- ----------------------------
DROP TABLE IF EXISTS `cms_vote_item`;
CREATE TABLE `cms_vote_item`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `vote_id` bigint(20) NOT NULL COMMENT '投票',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `scores` int(11) NOT NULL COMMENT '票数',
  `sort` int(11) NOT NULL COMMENT '顺序',
  PRIMARY KEY (`id`),
  INDEX `cms_vote_item_vote_id`(`vote_id`, `scores`, `sort`)
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
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `cms_word_name` (`name`,`site_id`),
  KEY `cms_word_hidden` (`hidden`),
  KEY `cms_word_create_date` (`create_date`),
  KEY `cms_word_search_count` (`search_count`),
  KEY `cms_word_site_id` (`site_id`)
) COMMENT='搜索词';

-- ----------------------------
-- Table structure for log_login
-- ----------------------------
DROP TABLE IF EXISTS `log_login`;
CREATE TABLE `log_login` (
  `id` bigint(20) NOT NULL auto_increment,
  `site_id` smallint(6) NOT NULL COMMENT '站点ID',
  `name` varchar(50) NOT NULL COMMENT '用户名',
  `user_id` bigint(20) default NULL COMMENT '用户ID',
  `ip` varchar(130) NOT NULL COMMENT 'IP',
  `channel` varchar(50) NOT NULL COMMENT '登录渠道',
  `result` tinyint(1) NOT NULL COMMENT '结果',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `error_password` varchar(100) default NULL COMMENT '错误密码',
  PRIMARY KEY  (`id`),
  KEY `log_login_result` (`result`),
  KEY `log_login_user_id` (`user_id`),
  KEY `log_login_create_date` (`create_date`),
  KEY `log_login_ip` (`ip`),
  KEY `log_login_site_id` (`site_id`),
  KEY `log_login_channel` (`channel`)
) COMMENT='登录日志';

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
  `ip` varchar(130) default NULL COMMENT 'IP',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `content` text NOT NULL COMMENT '内容',
  PRIMARY KEY  (`id`),
  KEY `log_operate_user_id` (`user_id`),
  KEY `log_operate_operate` (`operate`),
  KEY `log_operate_create_date` (`create_date`),
  KEY `log_operate_ip` (`ip`),
  KEY `log_operate_site_id` (`site_id`),
  KEY `log_operate_channel` (`channel`)
) COMMENT='操作日志';

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
  KEY `log_task_task_id` (`task_id`),
  KEY `log_task_success` (`success`),
  KEY `log_task_site_id` (`site_id`),
  KEY `log_task_begintime` (`begintime`)
) COMMENT='任务计划日志';

-- ----------------------------
-- Table structure for log_upload
-- ----------------------------
DROP TABLE IF EXISTS `log_upload`;
CREATE TABLE `log_upload` (
  `id` bigint(20) NOT NULL auto_increment,
  `site_id` smallint(6) NOT NULL COMMENT '站点ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `channel` varchar(50) NOT NULL COMMENT '操作渠道',
  `original_name` varchar(255) DEFAULT NULL COMMENT '原文件名',
  `file_type` varchar(20) NOT NULL COMMENT '文件类型',
  `file_size` bigint(20) NOT NULL COMMENT '文件大小',
  `width` int(11) NULL COMMENT '宽度',
  `height` int(11) NULL COMMENT '高度',
  `ip` varchar(130) default NULL COMMENT 'IP',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `file_path` varchar(500) NOT NULL COMMENT '文件路径',
  PRIMARY KEY  (`id`),
  KEY `log_upload_user_id` (`user_id`),
  KEY `log_upload_create_date` (`create_date`),
  KEY `log_upload_ip` (`ip`),
  KEY `log_upload_site_id` (`site_id`),
  KEY `log_upload_channel` (`channel`),
  KEY `log_upload_file_type` (`file_type`),
  KEY `log_upload_file_size` (`file_size`)
) COMMENT='上传日志';


-- ----------------------------
-- Table structure for trade_account
-- ----------------------------
DROP TABLE IF EXISTS `trade_account`;
CREATE TABLE `trade_account`  (
  `id` bigint(20) NOT NULL COMMENT '用户ID',
  `site_id` smallint(6) NOT NULL COMMENT '站点ID',
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
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `site_id` smallint(6) NOT NULL COMMENT '站点ID',
  `serial_number` varchar(100) NOT NULL COMMENT '流水号',
  `account_id` bigint(20) NOT NULL COMMENT '账户ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '操作用户ID',
  `amount_change` decimal(10,2) NOT NULL COMMENT '变动金额',
  `amount` decimal(10,2) NOT NULL COMMENT '变动金额',
  `balance` decimal(10,2) NOT NULL COMMENT '变动金额',
  `status` int(11) NOT NULL COMMENT '类型:0预充值,1消费,2充值,3退款',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`),
  KEY `trade_account_history_site_id` (`site_id`,`account_id`,`status`),
  KEY `trade_account_history_create_date` (`create_date`)
) COMMENT='账户流水';
-- ----------------------------
-- Table structure for trade_order
-- ----------------------------
DROP TABLE IF EXISTS `trade_order`;
CREATE TABLE `trade_order`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `amount` decimal(10, 2) NOT NULL COMMENT '金额',
  `description` varchar(255) NULL DEFAULT NULL COMMENT '描述',
  `trade_type` varchar(20) NOT NULL COMMENT '订单类型',
  `serial_number` varchar(100) NOT NULL COMMENT '订单流水',
  `account_type` varchar(20) NOT NULL COMMENT '支付账户类型',
  `account_serial_number` varchar(100) NULL DEFAULT NULL COMMENT '支付账号流水',
  `ip` varchar(130) NOT NULL COMMENT 'IP地址',
  `status` int(11) NOT NULL COMMENT '状态:0待支付,1已支付,2待退款,3退款成功',
  `processed` tinyint(1) NOT NULL COMMENT '已处理',
  `update_date` datetime DEFAULT NULL COMMENT '更新日期',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `process_date` datetime DEFAULT NULL COMMENT '处理日期',
  `payment_date` datetime NULL DEFAULT NULL COMMENT '支付日期',
  PRIMARY KEY (`id`),
  KEY `trade_order_account_type`(`account_type`, `account_serial_number`),
  KEY `trade_order_site_id`(`site_id`, `user_id`, `status`),
  KEY `trade_order_trade_type`(`trade_type`, `serial_number`),
  KEY `trade_order_create_date` (`create_date`)
) COMMENT = '支付订单';

-- ----------------------------
-- Table structure for trade_order_history
-- ----------------------------
DROP TABLE IF EXISTS `trade_order_history`;
CREATE TABLE `trade_order_history`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `site_id` smallint(6) NOT NULL COMMENT '站点ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `operate` varchar(100) NOT NULL COMMENT '操作',
  `content` text COMMENT '内容',
  PRIMARY KEY (`id`),
  KEY `trade_order_history_site_id` (`site_id`,`order_id`,`operate`),
  KEY `trade_order_history_create_date` (`create_date`)
) COMMENT = '订单流水';

-- ----------------------------
-- Table structure for trade_refund
-- ----------------------------
DROP TABLE IF EXISTS `trade_refund`;
CREATE TABLE `trade_refund`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `amount` decimal(10, 2) NOT NULL COMMENT '申请退款金额',
  `reason` varchar(255) NULL DEFAULT NULL COMMENT '退款原因',
  `update_date` datetime NULL DEFAULT NULL COMMENT '更新日期',
  `refund_user_id` bigint(20) NULL DEFAULT NULL COMMENT '退款操作人员',
  `refund_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '退款金额',
  `status` int(11) NOT NULL COMMENT '状态:0待退款,1已退款,2取消退款,3拒绝退款,4退款失败',
  `reply` varchar(255) NULL DEFAULT NULL COMMENT '回复',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `processing_date` datetime NULL DEFAULT NULL COMMENT '处理日期',
  PRIMARY KEY (`id`),
  KEY `trade_refund_order_id`(`order_id`, `status`),
  KEY `trade_refund_create_date` (`create_date`)
) COMMENT = '退款申请';

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
  `site_id` smallint(6) NOT NULL COMMENT '站点ID',
  `channel` varchar(20) NOT NULL COMMENT '渠道',
  `uuid` varchar(50) NOT NULL COMMENT '唯一标识',
  `user_id` bigint(20) DEFAULT NULL COMMENT '绑定用户',
  `client_version` varchar(50) DEFAULT NULL COMMENT '版本',
  `last_login_date` datetime DEFAULT NULL COMMENT '上次登录时间',
  `last_login_ip` varchar(130) DEFAULT NULL COMMENT '上次登录IP',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `disabled` tinyint(1) NOT NULL COMMENT '是否禁用',
  PRIMARY KEY (`id`),
  UNIQUE KEY `sys_app_client_site_id` (`site_id`,`channel`,`uuid`),
  KEY `sys_app_client_user_id` (`user_id`,`disabled`,`create_date`) 
) COMMENT='应用客户端';

-- ----------------------------
-- Table structure for sys_app_token
-- ----------------------------
DROP TABLE IF EXISTS `sys_app_token`;
CREATE TABLE `sys_app_token` (
  `auth_token` varchar(40) NOT NULL COMMENT '授权验证',
  `app_id` int(11) NOT NULL COMMENT '应用ID',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `expiry_date` datetime DEFAULT NULL COMMENT '过期日期',
  PRIMARY KEY  (`auth_token`),
  KEY `sys_app_token_app_id` (`app_id`),
  KEY `sys_app_token_create_date` (`create_date`)
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
  `site_id` smallint(6) NOT NULL COMMENT '站点ID',
  `code` varchar(50) NOT NULL COMMENT '配置项编码',
  `data` longtext NOT NULL COMMENT '值',
  PRIMARY KEY  (`site_id`,`code`)
) COMMENT='站点配置';

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
  `owns_all_config` tinyint(1) NOT NULL DEFAULT '1' COMMENT '拥有全部配置权限',
  PRIMARY KEY  (`id`),
  KEY `sys_dept_site_id` (`site_id`)
) AUTO_INCREMENT=3 COMMENT='部门';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES ('1', '1', 'Technical department', null, '', '1', '1000', '1', '1', '1');

-- ----------------------------
-- Table structure for sys_dept_category
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept_category`;
CREATE TABLE `sys_dept_category` (
  `dept_id` int(11) NOT NULL COMMENT '部门ID',
  `category_id` int(11) NOT NULL COMMENT '分类ID',
  PRIMARY KEY  (`dept_id`,`category_id`)
) COMMENT='部门分类';


-- ----------------------------
-- Table structure for sys_dept_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept_config`;
CREATE TABLE `sys_dept_config` (
  `dept_id` int(11) NOT NULL COMMENT '部门ID',
  `config` varchar(100) NOT NULL COMMENT '配置',
  PRIMARY KEY (`dept_id`,`config`) 
) COMMENT='部门配置';

-- ----------------------------
-- Table structure for sys_dept_page
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept_page`;
CREATE TABLE `sys_dept_page` (
  `dept_id` int(11) NOT NULL COMMENT '部门ID',
  `page` varchar(100) NOT NULL COMMENT '页面',
  PRIMARY KEY  (`dept_id`,`page`),
  KEY `sys_dept_page_page` (`page`)
) COMMENT='部门页面';


-- ----------------------------
-- Table structure for sys_domain
-- ----------------------------
DROP TABLE IF EXISTS `sys_domain`;
CREATE TABLE `sys_domain` (
  `name` varchar(100) NOT NULL COMMENT '域名',
  `site_id` smallint(6) NOT NULL COMMENT '站点ID',
  `wild` tinyint(1) NOT NULL COMMENT '通配域名',
  `path` varchar(100) default NULL COMMENT '路径',
  PRIMARY KEY  (`name`),
  KEY `sys_domain_site_id` (`site_id`)
) COMMENT='域名';

-- ----------------------------
-- Records of sys_domain
-- ----------------------------
INSERT INTO `sys_domain` VALUES ('dev.publiccms.com', '1', '1', '');
INSERT INTO `sys_domain` VALUES ('localhost', '1', '1', '');


-- ----------------------------
-- Table structure for sys_email_token
-- ----------------------------
DROP TABLE IF EXISTS `sys_email_token`;
CREATE TABLE `sys_email_token` (
  `auth_token` varchar(40) NOT NULL COMMENT '验证码',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `email` varchar(100) NOT NULL COMMENT '邮件地址',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `expiry_date` datetime NOT NULL COMMENT '过期日期',
  PRIMARY KEY  (`auth_token`),
  KEY `sys_email_token_create_date` (`create_date`),
  KEY `sys_email_token_user_id` (`user_id`)
) COMMENT='邮件地址验证日志';

-- ----------------------------
-- Table structure for sys_extend
-- ----------------------------
DROP TABLE IF EXISTS `sys_extend`;
CREATE TABLE `sys_extend` (
  `id` int(11) NOT NULL auto_increment,
  `item_type` varchar(20) NOT NULL COMMENT '扩展类型',
  `item_id` int(11) NOT NULL COMMENT '扩展项目ID',
  PRIMARY KEY  (`id`)
) COMMENT='扩展';

-- ----------------------------
-- Table structure for sys_extend_field
-- ----------------------------
DROP TABLE IF EXISTS `sys_extend_field`;
CREATE TABLE `sys_extend_field` (
  `extend_id` int(11) NOT NULL COMMENT '扩展ID',
  `code` varchar(20) NOT NULL COMMENT '编码',
  `required` tinyint(1) NOT NULL COMMENT '是否必填',
  `searchable` tinyint(1) NOT NULL COMMENT '是否可搜索',
  `maxlength` int(11) default NULL COMMENT '最大长度',
  `name` varchar(20) NOT NULL COMMENT '名称',
  `description` varchar(100) default NULL COMMENT '解释',
  `input_type` varchar(20) NOT NULL COMMENT '表单类型',
  `default_value` varchar(50) default NULL COMMENT '默认值',
  `dictionary_id` varchar(20) default NULL COMMENT '数据字典ID',
  `sort` int(11) NOT NULL default '0' COMMENT '顺序',
  PRIMARY KEY  (`extend_id`,`code`),
  KEY `sys_extend_field_sort` (`sort`)
) COMMENT='扩展字段';

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
  KEY `sys_module_parent_id` (`parent_id`,`menu`),
  KEY `sys_module_sort` (`sort`)
) COMMENT='模块';

-- ----------------------------
-- Records of sys_module
-- ----------------------------
INSERT INTO `sys_module` VALUES ('account_add', 'tradeAccount/add', 'tradeAccount/save', '', 'account_list', 0, 1);
INSERT INTO `sys_module` VALUES ('account_history_list', 'tradeAccountHistory/list', 'sysUser/lookup', 'icon-book', 'trade_menu', 1, 5);
INSERT INTO `sys_module` VALUES ('account_list', 'tradeAccount/list', NULL, 'icon-credit-card', 'trade_menu', 1, 4);
INSERT INTO `sys_module` VALUES ('account_recharge', 'tradeAccount/rechargeParameters', 'tradeAccount/recharge', '', 'account_list', 0, 2);
INSERT INTO `sys_module` VALUES ('app_add', 'sysApp/add', 'sysApp/save', NULL, 'app_list', 0, 0);
INSERT INTO `sys_module` VALUES ('app_client_disable', NULL, 'sysAppClient/disable', NULL, 'app_client_list', 0, 0);
INSERT INTO `sys_module` VALUES ('app_client_enable', NULL, 'sysAppClient/enable', NULL, 'app_client_list', 0, 0);
INSERT INTO `sys_module` VALUES ('app_client_list', 'sysAppClient/list', NULL, 'icon-coffee', 'user_menu', 1, 4);
INSERT INTO `sys_module` VALUES ('app_delete', NULL, 'sysApp/delete', NULL, 'app_list', 0, 0);
INSERT INTO `sys_module` VALUES ('app_issue', 'sysApp/issueParameters', 'sysAppToken/issue', NULL, 'app_list', 0, 0);
INSERT INTO `sys_module` VALUES ('app_list', 'sysApp/list', NULL, 'icon-linux', 'system_menu', 1, 5);
INSERT INTO `sys_module` VALUES ('category', NULL, NULL, 'icon-folder-open-alt', NULL, 1, 5);
INSERT INTO `sys_module` VALUES ('category_add', 'cmsCategory/add', 'cmsCategory/addMore,cmsCategory/virify,cmsTemplate/lookup,cmsCategory/categoryPath,cmsCategory/contentPath,file/doUpload,cmsCategory/save', '', 'category_menu', 0, 0);
INSERT INTO `sys_module` VALUES ('category_delete', NULL, 'cmsCategory/delete', '', 'category_menu', 0, 0);
INSERT INTO `sys_module` VALUES ('category_extend', NULL, NULL, 'icon-road', 'category', 1, 2);
INSERT INTO `sys_module` VALUES ('category_menu', 'cmsCategory/list', NULL, 'icon-folder-open', 'category', 1, 1);
INSERT INTO `sys_module` VALUES ('category_move', 'cmsCategory/moveParameters', 'cmsCategory/move,cmsCategory/lookup', '', 'category_menu', 0, 0);
INSERT INTO `sys_module` VALUES ('category_publish', 'cmsCategory/publishParameters', 'cmsCategory/publish', '', 'category_menu', 0, 0);
INSERT INTO `sys_module` VALUES ('category_push', 'cmsCategory/push_page', 'cmsPlace/push,cmsPlace/add,cmsPlace/save', '', 'category_menu', 0, 0);
INSERT INTO `sys_module` VALUES ('category_type_add', 'cmsCategoryType/add', 'cmsCategoryType/save', NULL, 'category_type_list', 0, 0);
INSERT INTO `sys_module` VALUES ('category_type_change', 'cmsCategory/changeTypeParameters', 'cmsCategory/changeType', '', 'category_menu', 0, 0);
INSERT INTO `sys_module` VALUES ('category_type_delete', NULL, 'cmsCategoryType/delete', NULL, 'category_type_list', 0, 0);
INSERT INTO `sys_module` VALUES ('category_type_list', 'cmsCategoryType/list', NULL, 'icon-road', 'category_extend', 1, 1);
INSERT INTO `sys_module` VALUES ('clearcache', NULL, 'clearCache', '', NULL, 0, 10);
INSERT INTO `sys_module` VALUES ('comment_check', NULL, 'cmsComment/check', NULL, 'comment_list', 0, 0);
INSERT INTO `sys_module` VALUES ('comment_delete', NULL, 'cmsComment/delete', NULL, 'comment_list', 0, 0);
INSERT INTO `sys_module` VALUES ('comment_edit', 'cmsComment/edit', 'cmsComment/save', NULL, 'comment_list', 0, 0);
INSERT INTO `sys_module` VALUES ('comment_list', 'cmsComment/list', 'sysUser/lookup', 'icon-comment', 'content_extend', 1, 4);
INSERT INTO `sys_module` VALUES ('comment_reply', 'cmsComment/reply', 'cmsComment/save', NULL, 'comment_list', 0, 0);
INSERT INTO `sys_module` VALUES ('comment_uncheck', NULL, 'cmsComment/uncheck', NULL, 'comment_list', 0, 0);
INSERT INTO `sys_module` VALUES ('config_add', 'sysConfig/add', 'sysConfig/save', NULL, 'config_list', 0, 0);
INSERT INTO `sys_module` VALUES ('config_data_delete', NULL, 'sysConfigData/delete', NULL, 'config_data_list', 0, 0);
INSERT INTO `sys_module` VALUES ('config_data_edit', NULL, 'sysConfigData/save,sysConfigData/edit', NULL, 'config_data_list', 0, 0);
INSERT INTO `sys_module` VALUES ('config_data_list', 'sysConfigData/list', NULL, 'icon-cog', 'system_menu', 1, 1);
INSERT INTO `sys_module` VALUES ('config_delete', NULL, 'sysConfig/delete', NULL, 'config_list', 0, 0);
INSERT INTO `sys_module` VALUES ('config_list', 'sysConfig/list', NULL, 'icon-cogs', 'config_menu', 1, 2);
INSERT INTO `sys_module` VALUES ('config_list_data_dictionary', 'cmsDictionary/lookup', NULL, NULL, 'config_list', 0, 0);
INSERT INTO `sys_module` VALUES ('config_menu', NULL, NULL, 'icon-gear', 'develop', 1, 2);
INSERT INTO `sys_module` VALUES ('content', NULL, NULL, 'icon-file-text-alt', NULL, 1, 2);
INSERT INTO `sys_module` VALUES ('content_add', 'cmsContent/add', 'cmsContent/addMore,file/doUpload,cmsContent/lookup,cmsContent/lookup_list,cmsContent/save,ueditor,ckeditor/upload,kindeditor/upload,file/doBatchUpload', '', 'content_menu', 1, 0);
INSERT INTO `sys_module` VALUES ('content_check', NULL, 'cmsContent/check,cmsContent/reject', '', 'content_menu', 1, 0);
INSERT INTO `sys_module` VALUES ('content_delete', NULL, 'cmsContent/delete', '', 'content_menu', 1, 0);
INSERT INTO `sys_module` VALUES ('content_export', NULL, 'cmsContent/export', '', 'content_menu', 1, 0);
INSERT INTO `sys_module` VALUES ('content_extend', NULL, NULL, 'icon-road', 'content', 1, 1);
INSERT INTO `sys_module` VALUES ('content_menu', 'cmsContent/list', 'sysUser/lookup', 'icon-book', 'content', 1, 0);
INSERT INTO `sys_module` VALUES ('content_move', 'cmsContent/moveParameters', 'cmsContent/move', '', 'content_menu', 1, 0);
INSERT INTO `sys_module` VALUES ('content_publish', NULL, 'cmsContent/publish', '', 'content_menu', 1, 0);
INSERT INTO `sys_module` VALUES ('content_push', 'cmsContent/push', 'cmsContent/push_content,cmsContent/push_content_list,cmsContent/push_to_content,cmsContent/push_page,cmsContent/push_page_list,cmsPlace/add,cmsPlace/save,cmsContent/related,cmsContent/unrelated,cmsPlace/delete', '', 'content_menu', 1, 0);
INSERT INTO `sys_module` VALUES ('content_recycle_delete', NULL, 'cmsContent/realDelete', NULL, 'content_recycle_list', 0, 0);
INSERT INTO `sys_module` VALUES ('content_recycle_list', 'cmsRecycleContent/list', 'sysUser/lookup', 'icon-trash', 'content_extend', 1, 3);
INSERT INTO `sys_module` VALUES ('content_recycle_recycle', NULL, 'cmsContent/recycle', NULL, 'content_recycle_list', 0, 0);
INSERT INTO `sys_module` VALUES ('content_refresh', NULL, 'cmsContent/refresh', '', 'content_menu', 1, 0);
INSERT INTO `sys_module` VALUES ('content_select_category', 'cmsCategory/lookupByModelId', NULL, NULL, 'content_add', 0, 0);
INSERT INTO `sys_module` VALUES ('content_select_category_type', 'cmsCategoryType/lookup', NULL, NULL, 'content_add', 0, 0);
INSERT INTO `sys_module` VALUES ('content_select_content', 'cmsContent/lookup', 'cmsContent/lookup_list', NULL, 'content_add', 0, 0);
INSERT INTO `sys_module` VALUES ('content_select_tag', 'cmsTag/lookup', NULL, NULL, 'content_add', 0, 0);
INSERT INTO `sys_module` VALUES ('content_select_tag_type', 'cmsTagType/lookup', NULL, NULL, 'content_add', 0, 0);
INSERT INTO `sys_module` VALUES ('content_select_template', 'cmsTemplate/lookup', NULL, NULL, 'content_add', 0, 0);
INSERT INTO `sys_module` VALUES ('content_select_user', 'sysUser/lookup', NULL, NULL, 'content_add', 0, 0);
INSERT INTO `sys_module` VALUES ('content_select_vote', 'cmsVote/lookup', NULL, NULL, 'content_add', 0, 0);
INSERT INTO `sys_module` VALUES ('content_sort', 'cmsContent/sortParameters', 'cmsContent/sort', '', 'content_menu', 0, 0);
INSERT INTO `sys_module` VALUES ('content_uncheck', NULL, 'cmsContent/uncheck', '', 'content_menu', 0, 0);
INSERT INTO `sys_module` VALUES ('content_view', 'cmsContent/view', NULL, '', 'content_menu', 0, 0);
INSERT INTO `sys_module` VALUES ('content_vote', 'cmsVote/list', NULL, 'icon-ticket', 'content_extend', 1, 4);
INSERT INTO `sys_module` VALUES ('content_vote_add', 'cmsVote/add', 'cmsVote/save', NULL, 'content_vote', 0, 0);
INSERT INTO `sys_module` VALUES ('content_vote_delete', NULL, 'cmsVote/delete', NULL, 'content_vote', 0, 0);
INSERT INTO `sys_module` VALUES ('content_vote_view', 'cmsVote/view', NULL, NULL, 'content_vote', 0, 0);
INSERT INTO `sys_module` VALUES ('dept_add', 'sysDept/add', 'sysDept/lookup,sysUser/lookup,sysDept/save', NULL, 'dept_list', 0, 0);
INSERT INTO `sys_module` VALUES ('dept_delete', NULL, 'sysDept/delete', NULL, 'dept_list', 0, 0);
INSERT INTO `sys_module` VALUES ('dept_list', 'sysDept/list', 'sysDept/lookup,sysUser/lookup', 'icon-group', 'user_menu', 1, 2);
INSERT INTO `sys_module` VALUES ('dept_user_list', 'sysDept/userList', 'sysDept/addUser,sysDept/saveUser,sysDept/enableUser,sysDept/disableUser', NULL, 'dept_list', 0, 0);
INSERT INTO `sys_module` VALUES ('develop', NULL, NULL, 'icon-puzzle-piece', NULL, 1, 7);
INSERT INTO `sys_module` VALUES ('dictionary_add', 'cmsDictionary/add', 'cmsDictionary/save,cmsDictionary/virify', NULL, 'dictionary_list', 0, 0);
INSERT INTO `sys_module` VALUES ('dictionary_delete', NULL, 'cmsDictionary/delete', NULL, 'dictionary_list', 0, 0);
INSERT INTO `sys_module` VALUES ('dictionary_list', 'cmsDictionary/list', NULL, 'icon-book', 'system_menu', 1, 4);
INSERT INTO `sys_module` VALUES ('domain_config', 'sysDomain/config', 'sysDomain/saveConfig,cmsTemplate/directoryLookup,cmsTemplate/lookup', NULL, 'domain_list', 0, 0);
INSERT INTO `sys_module` VALUES ('domain_list', 'sysDomain/domainList', NULL, 'icon-qrcode', 'config_menu', 1, 3);
INSERT INTO `sys_module` VALUES ('file_menu', NULL, NULL, 'icon-folder-close-alt', 'develop', 1, 1);
INSERT INTO `sys_module` VALUES ('log_login', 'log/login', 'sysUser/lookup', 'icon-signin', 'log_menu', 1, 3);
INSERT INTO `sys_module` VALUES ('log_login_delete', NULL, 'logLogin/delete', NULL, 'log_login', 0, 0);
INSERT INTO `sys_module` VALUES ('log_menu', NULL, NULL, 'icon-list-alt', 'maintenance', 1, 3);
INSERT INTO `sys_module` VALUES ('log_operate', 'log/operate', 'sysUser/lookup', 'icon-list-alt', 'log_menu', 1, 2);
INSERT INTO `sys_module` VALUES ('log_operate_delete', NULL, 'logOperate/delete', NULL, 'log_operate', 0, 0);
INSERT INTO `sys_module` VALUES ('log_operate_view', 'log/operateView', NULL, NULL, 'log_operate', 0, 0);
INSERT INTO `sys_module` VALUES ('log_task', 'log/task', 'sysUser/lookup', 'icon-time', 'log_menu', 1, 4);
INSERT INTO `sys_module` VALUES ('log_task_delete', NULL, 'logTask/delete', NULL, 'log_task', 0, 0);
INSERT INTO `sys_module` VALUES ('log_task_view', 'log/taskView', NULL, NULL, 'log_task', 0, 0);
INSERT INTO `sys_module` VALUES ('log_upload', 'log/upload', 'sysUser/lookup', 'icon-list-alt', 'log_menu', 1, 1);
INSERT INTO `sys_module` VALUES ('maintenance', NULL, NULL, 'icon-cogs', NULL, 1, 6);
INSERT INTO `sys_module` VALUES ('model_add', 'cmsModel/add', 'cmsModel/save,cmsTemplate/lookup', NULL, 'model_list', 0, 0);
INSERT INTO `sys_module` VALUES ('model_delete', NULL, 'cmsModel/delete', NULL, 'model_list', 0, 0);
INSERT INTO `sys_module` VALUES ('model_list', 'cmsModel/list', NULL, 'icon-th-large', 'config_menu', 1, 1);
INSERT INTO `sys_module` VALUES ('myself', NULL, NULL, 'icon-key', NULL, 1, 1);
INSERT INTO `sys_module` VALUES ('myself_content', 'myself/contentList', NULL, 'icon-book', 'myself_menu', 1, 2);
INSERT INTO `sys_module` VALUES ('myself_content_add', 'cmsContent/add', 'cmsContent/addMore,file/doUpload,cmsContent/lookup,cmsContent/lookup_list,cmsContent/save,ueditor,ckeditor/upload,kindeditor/upload', NULL, 'myself_content', 0, 0);
INSERT INTO `sys_module` VALUES ('myself_content_delete', NULL, 'cmsContent/delete', NULL, 'myself_content', 0, 0);
INSERT INTO `sys_module` VALUES ('myself_content_publish', NULL, 'cmsContent/publish', NULL, 'myself_content', 0, 0);
INSERT INTO `sys_module` VALUES ('myself_content_push', 'cmsContent/push', 'cmsContent/push_content,cmsContent/push_content_list,cmsContent/push_to_content,cmsContent/push_page,cmsContent/push_page_list,cmsContent/push_to_place,cmsContent/related', NULL, 'myself_content', 0, 0);
INSERT INTO `sys_module` VALUES ('myself_content_refresh', NULL, 'cmsContent/refresh', NULL, 'myself_content', 0, 0);
INSERT INTO `sys_module` VALUES ('myself_device', 'myself/userDeviceList', 'sysAppClient/enable,sysAppClient/disable', 'icon-linux', 'myself_menu', 1, 5);
INSERT INTO `sys_module` VALUES ('myself_log_login', 'myself/logLogin', NULL, 'icon-signin', 'myself_menu', 1, 4);
INSERT INTO `sys_module` VALUES ('myself_log_operate', 'myself/logOperate', NULL, 'icon-list-alt', 'myself_menu', 1, 3);
INSERT INTO `sys_module` VALUES ('myself_menu', NULL, NULL, 'icon-user', 'myself', 1, 0);
INSERT INTO `sys_module` VALUES ('myself_password', 'myself/password', 'changePassword', 'icon-key', 'myself_menu', 1, 1);
INSERT INTO `sys_module` VALUES ('myself_token', 'myself/userTokenList', 'sysUserToken/delete', 'icon-unlock-alt', 'myself_menu', 1, 5);
INSERT INTO `sys_module` VALUES ('order_history_list', 'tradeOrderHistory/list', NULL, 'icon-calendar', 'trade_menu', 1, 2);
INSERT INTO `sys_module` VALUES ('order_list', 'tradeOrder/list', 'sysUser/lookup', 'icon-barcode', 'trade_menu', 1, 1);
INSERT INTO `sys_module` VALUES ('page', NULL, NULL, 'icon-tablet', NULL, 1, 3);
INSERT INTO `sys_module` VALUES ('page_list', 'cmsPage/list', 'cmsPage/metadata,sysUser/lookup,cmsContent/lookup,cmsContent/lookup_list,cmsCategory/lookup', 'icon-globe', 'page_menu', 1, 1);
INSERT INTO `sys_module` VALUES ('page_menu', NULL, NULL, 'icon-globe', 'page', 1, 0);
INSERT INTO `sys_module` VALUES ('page_metadata', 'cmsPage/metadata', 'cmsPage/save', NULL, 'page_list', 0, 0);
INSERT INTO `sys_module` VALUES ('page_publish', NULL, 'cmsTemplate/publish', NULL, 'page_list', 0, 0);
INSERT INTO `sys_module` VALUES ('page_save', NULL, 'cmsPage/save,file/doUpload,cmsPage/clearCache', NULL, 'page_list', 0, 0);
INSERT INTO `sys_module` VALUES ('page_select_category', 'cmsCategory/lookup', NULL, NULL, 'page_list', 0, 0);
INSERT INTO `sys_module` VALUES ('page_select_category_type', 'cmsCategoryType/lookup', NULL, NULL, 'page_list', 0, 0);
INSERT INTO `sys_module` VALUES ('page_select_content', 'cmsContent/lookup', 'cmsContent/lookup_list', NULL, 'page_list', 0, 0);
INSERT INTO `sys_module` VALUES ('page_select_tag_type', 'cmsTagType/lookup', NULL, NULL, 'page_list', 0, 0);
INSERT INTO `sys_module` VALUES ('page_select_template', 'cmsTemplate/lookup', NULL, NULL, 'page_list', 0, 0);
INSERT INTO `sys_module` VALUES ('page_select_user', 'sysUser/lookup', NULL, NULL, 'page_list', 0, 0);
INSERT INTO `sys_module` VALUES ('place_add', 'cmsPlace/add', 'cmsContent/lookup,cmsPlace/lookup,cmsPlace/lookup_content_list,file/doUpload,cmsPlace/save', NULL, 'place_list', 0, 0);
INSERT INTO `sys_module` VALUES ('place_check', NULL, 'cmsPlace/check,cmsPlace/uncheck', NULL, 'place_list', 0, 0);
INSERT INTO `sys_module` VALUES ('place_clear', NULL, 'cmsPlace/clear', NULL, 'place_list', 0, 0);
INSERT INTO `sys_module` VALUES ('place_data_list', 'cmsPlace/dataList', 'cmsPlace/export', NULL, 'place_list', 0, 1);
INSERT INTO `sys_module` VALUES ('place_delete', NULL, 'cmsPlace/delete', NULL, 'place_list', 0, 0);
INSERT INTO `sys_module` VALUES ('place_list', 'cmsPlace/list', 'sysUser/lookup', 'icon-list-alt', 'page_menu', 1, 1);
INSERT INTO `sys_module` VALUES ('place_publish', 'cmsPlace/publish_place', 'cmsTemplate/publishPlace', NULL, 'place_list', 0, 0);
INSERT INTO `sys_module` VALUES ('place_refresh', NULL, 'cmsPlace/refresh', NULL, 'place_list', 0, 0);
INSERT INTO `sys_module` VALUES ('place_template_content', 'placeTemplate/content', 'cmsTemplate/help,cmsTemplate/savePlace,cmsTemplate/chipLookup,cmsWebFile/lookup,cmsWebFile/contentForm,placeTemplate/form', NULL, 'place_template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('place_template_data_dictionary', 'cmsDictionary/lookup', NULL, NULL, 'place_template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('place_template_fragment', 'cmsTemplate/ftlLookup', NULL, NULL, 'place_template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('place_template_help', 'cmsTemplate/help', NULL, NULL, 'place_template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('place_template_list', 'placeTemplate/list', NULL, 'icon-list-alt', 'file_menu', 1, 2);
INSERT INTO `sys_module` VALUES ('place_template_metadata', 'placeTemplate/metadata', 'cmsTemplate/savePlaceMetaData', NULL, 'place_template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('place_template_place', 'placeTemplate/lookup', NULL, NULL, 'place_template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('place_template_webfile', 'cmsWebFile/lookup', NULL, NULL, 'place_template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('place_view', 'cmsPlace/view', NULL, NULL, 'place_list', 0, 0);
INSERT INTO `sys_module` VALUES ('refund_list', 'tradeRefund/list', 'sysUser/lookup', 'icon-signout', 'trade_menu', 1, 3);
INSERT INTO `sys_module` VALUES ('refund_refund', 'tradeRefund/refundParameters', 'tradeRefund/refund', '', 'refund_list', 0, 1);
INSERT INTO `sys_module` VALUES ('report_user', 'report/user', NULL, 'icon-male', 'user_menu', 1, 5);
INSERT INTO `sys_module` VALUES ('role_add', 'sysRole/add', 'sysRole/save', NULL, 'role_list', 0, 0);
INSERT INTO `sys_module` VALUES ('role_delete', NULL, 'sysRole/delete', NULL, 'role_list', 0, 0);
INSERT INTO `sys_module` VALUES ('role_list', 'sysRole/list', NULL, 'icon-user-md', 'user_menu', 1, 3);
INSERT INTO `sys_module` VALUES ('system_menu', NULL, NULL, 'icon-cogs', 'maintenance', 1, 2);
INSERT INTO `sys_module` VALUES ('tag_add', 'cmsTag/add', 'cmsTagType/lookup,cmsTag/save', NULL, 'tag_list', 0, 0);
INSERT INTO `sys_module` VALUES ('tag_delete', NULL, 'cmsTag/delete', NULL, 'tag_list', 0, 0);
INSERT INTO `sys_module` VALUES ('tag_list', 'cmsTag/list', 'cmsTagType/lookup', 'icon-tag', 'content_extend', 1, 1);
INSERT INTO `sys_module` VALUES ('tag_type_delete', NULL, 'cmsTagType/delete', NULL, 'tag_type_list', 0, 0);
INSERT INTO `sys_module` VALUES ('tag_type_list', 'cmsTagType/list', NULL, 'icon-tags', 'category_extend', 1, 2);
INSERT INTO `sys_module` VALUES ('tag_type_save', 'cmsTagType/add', 'cmsTagType/save', NULL, 'tag_type_list', 0, 0);
INSERT INTO `sys_module` VALUES ('task_add', 'sysTask/add', 'sysTask/save,sysTask/example,taskTemplate/lookup', NULL, 'task_list', 0, 0);
INSERT INTO `sys_module` VALUES ('task_delete', NULL, 'sysTask/delete', NULL, 'task_list', 0, 0);
INSERT INTO `sys_module` VALUES ('task_list', 'sysTask/list', NULL, 'icon-time', 'system_menu', 1, 2);
INSERT INTO `sys_module` VALUES ('task_pause', NULL, 'sysTask/pause', NULL, 'task_list', 0, 0);
INSERT INTO `sys_module` VALUES ('task_recreate', NULL, 'sysTask/recreate', NULL, 'task_list', 0, 0);
INSERT INTO `sys_module` VALUES ('task_resume', NULL, 'sysTask/resume', NULL, 'task_list', 0, 0);
INSERT INTO `sys_module` VALUES ('task_runonce', NULL, 'sysTask/runOnce', NULL, 'task_list', 0, 0);
INSERT INTO `sys_module` VALUES ('task_template_content', 'taskTemplate/content', 'taskTemplate/save,taskTemplate/chipLookup,cmsTemplate/help,placeTemplate/form,cmsWebFile/contentForm', NULL, 'task_template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('task_template_delete', NULL, 'taskTemplate/delete', NULL, 'task_template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('task_template_fragment', 'taskTemplate/chipLookup', NULL, NULL, 'task_template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('task_template_help', 'cmsTemplate/help', NULL, NULL, 'task_template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('task_template_list', 'taskTemplate/list', NULL, 'icon-time', 'file_menu', 1, 3);
INSERT INTO `sys_module` VALUES ('template_content', 'cmsTemplate/content', 'cmsTemplate/save,cmsTemplate/chipLookup,cmsWebFile/lookup,placeTemplate/form,cmsWebFile/contentForm,cmsTemplate/demo,cmsTemplate/help,cmsTemplate/upload,cmsTemplate/doUpload', NULL, 'template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('template_content-type', 'cmsTemplate/contentTypeLookup', NULL, NULL, 'template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('template_content_form', 'cmsTemplate/contentForm', NULL, NULL, 'template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('template_data_dictionary', 'cmsDictionary/lookup', NULL, NULL, 'template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('template_delete', NULL, 'cmsTemplate/delete', NULL, 'template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('template_demo', 'cmsTemplate/demo', NULL, NULL, 'template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('template_fragment', 'cmsTemplate/ftlLookup', NULL, NULL, 'template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('template_help', 'cmsTemplate/help', NULL, NULL, 'template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('template_list', 'cmsTemplate/list', 'cmsTemplate/directory', 'icon-code', 'file_menu', 1, 1);
INSERT INTO `sys_module` VALUES ('template_metadata', 'cmsTemplate/metadata', 'cmsTemplate/saveMetadata', NULL, 'template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('template_place', 'placeTemplate/lookup', NULL, NULL, 'template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('template_place_form', 'placeTemplate/form', NULL, NULL, 'template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('template_upload', 'cmsTemplate/upload', 'cmsTemplate/doUpload', NULL, 'template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('template_website_file', 'cmsWebFile/lookup', NULL, NULL, 'template_list', 0, 0);
INSERT INTO `sys_module` VALUES ('trade_menu', NULL, NULL, 'icon-money', 'maintenance', 0, 4);
INSERT INTO `sys_module` VALUES ('user_add', 'sysUser/add', 'sysDept/lookup,sysUser/save', NULL, 'user_list', 0, 0);
INSERT INTO `sys_module` VALUES ('user_disable', NULL, 'sysUser/disable', NULL, 'user_list', 0, 0);
INSERT INTO `sys_module` VALUES ('user_enable', NULL, 'sysUser/enable', NULL, 'user_list', 0, 0);
INSERT INTO `sys_module` VALUES ('user_list', 'sysUser/list', NULL, 'icon-user', 'user_menu', 1, 1);
INSERT INTO `sys_module` VALUES ('user_menu', NULL, NULL, 'icon-user', 'maintenance', 1, 1);
INSERT INTO `sys_module` VALUES ('webfile_content', 'cmsWebFile/content', 'cmsWebFile/save', NULL, 'webfile_list', 0, 0);
INSERT INTO `sys_module` VALUES ('webfile_directory', 'cmsWebFile/directory', 'cmsWebFile/createDirectory', NULL, 'webfile_list', 0, 0);
INSERT INTO `sys_module` VALUES ('webfile_list', 'cmsWebFile/list', NULL, 'icon-globe', 'file_menu', 1, 4);
INSERT INTO `sys_module` VALUES ('webfile_unzip', 'cmsWebFile/unzipParameters', 'cmsWebFile/unzip', NULL, 'webfile_list', 0, 0);
INSERT INTO `sys_module` VALUES ('webfile_upload', 'cmsWebFile/upload', 'cmsWebFile/doUpload,cmsWebFile/check', NULL, 'webfile_list', 0, 0);
INSERT INTO `sys_module` VALUES ('webfile_zip', NULL, 'cmsWebFile/zip', NULL, 'webfile_list', 0, 0);
INSERT INTO `sys_module` VALUES ('word_list', 'cmsWord/list', NULL, 'icon-search', 'content_extend', 1, 2);

-- ----------------------------
-- Table structure for sys_module_lang
-- ----------------------------
DROP TABLE IF EXISTS `sys_module_lang`;
CREATE TABLE `sys_module_lang`  (
  `module_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模块ID',
  `lang` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '语言',
  `value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '值',
  PRIMARY KEY (`module_id`, `lang`) USING BTREE,
  INDEX `sys_module_lang_lang`(`lang`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '模块语言' ROW_FORMAT = Compact;

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
INSERT INTO `sys_module_lang` VALUES ('category', 'en', 'Category');
INSERT INTO `sys_module_lang` VALUES ('category', 'ja', '分類');
INSERT INTO `sys_module_lang` VALUES ('category', 'zh', '分类');
INSERT INTO `sys_module_lang` VALUES ('category_add', 'en', 'Add/edit');
INSERT INTO `sys_module_lang` VALUES ('category_add', 'ja', '追加/変更');
INSERT INTO `sys_module_lang` VALUES ('category_add', 'zh', '增加/修改');
INSERT INTO `sys_module_lang` VALUES ('category_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('category_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('category_delete', 'zh', '删除');
INSERT INTO `sys_module_lang` VALUES ('category_extend', 'en', 'Category extension');
INSERT INTO `sys_module_lang` VALUES ('category_extend', 'ja', '分類拡張');
INSERT INTO `sys_module_lang` VALUES ('category_extend', 'zh', '分类扩展');
INSERT INTO `sys_module_lang` VALUES ('category_menu', 'en', 'Category management');
INSERT INTO `sys_module_lang` VALUES ('category_menu', 'ja', '分類管理');
INSERT INTO `sys_module_lang` VALUES ('category_menu', 'zh', '分类管理');
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
INSERT INTO `sys_module_lang` VALUES ('category_type_list', 'en', 'Category type');
INSERT INTO `sys_module_lang` VALUES ('category_type_list', 'ja', '分類タイプ');
INSERT INTO `sys_module_lang` VALUES ('category_type_list', 'zh', '分类类型');
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
INSERT INTO `sys_module_lang` VALUES ('config_add', 'en', 'Add/edit');
INSERT INTO `sys_module_lang` VALUES ('config_add', 'ja', '追加/変更');
INSERT INTO `sys_module_lang` VALUES ('config_add', 'zh', '添加/修改');
INSERT INTO `sys_module_lang` VALUES ('config_data_delete', 'en', 'Clear config data');
INSERT INTO `sys_module_lang` VALUES ('config_data_delete', 'ja', 'データをクリア');
INSERT INTO `sys_module_lang` VALUES ('config_data_delete', 'zh', '清空配置');
INSERT INTO `sys_module_lang` VALUES ('config_data_edit', 'en', 'Edit');
INSERT INTO `sys_module_lang` VALUES ('config_data_edit', 'ja', '変更');
INSERT INTO `sys_module_lang` VALUES ('config_data_edit', 'zh', '修改');
INSERT INTO `sys_module_lang` VALUES ('config_data_list', 'en', 'Site configuration');
INSERT INTO `sys_module_lang` VALUES ('config_data_list', 'ja', 'サイト設定');
INSERT INTO `sys_module_lang` VALUES ('config_data_list', 'zh', '站点配置');
INSERT INTO `sys_module_lang` VALUES ('config_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('config_delete', 'ja', '設定を削除');
INSERT INTO `sys_module_lang` VALUES ('config_delete', 'zh', '删除配置');
INSERT INTO `sys_module_lang` VALUES ('config_list', 'en', 'Site config management');
INSERT INTO `sys_module_lang` VALUES ('config_list', 'ja', 'サイト設定管理');
INSERT INTO `sys_module_lang` VALUES ('config_list', 'zh', '站点配置管理');
INSERT INTO `sys_module_lang` VALUES ('config_list_data_dictionary', 'en', 'Select data dictionary');
INSERT INTO `sys_module_lang` VALUES ('config_list_data_dictionary', 'ja', 'データ辞書を選択');
INSERT INTO `sys_module_lang` VALUES ('config_list_data_dictionary', 'zh', '选择数据字典');
INSERT INTO `sys_module_lang` VALUES ('config_menu', 'en', 'Configuration management');
INSERT INTO `sys_module_lang` VALUES ('config_menu', 'ja', '設定管理');
INSERT INTO `sys_module_lang` VALUES ('config_menu', 'zh', '配置管理');
INSERT INTO `sys_module_lang` VALUES ('content', 'en', 'Content');
INSERT INTO `sys_module_lang` VALUES ('content', 'ja', 'コンテンツ');
INSERT INTO `sys_module_lang` VALUES ('content', 'zh', '内容');
INSERT INTO `sys_module_lang` VALUES ('content_add', 'en', 'Add/edit');
INSERT INTO `sys_module_lang` VALUES ('content_add', 'ja', '追加/変更');
INSERT INTO `sys_module_lang` VALUES ('content_add', 'zh', '增加/修改');
INSERT INTO `sys_module_lang` VALUES ('content_check', 'en', 'Check');
INSERT INTO `sys_module_lang` VALUES ('content_check', 'ja', '審査');
INSERT INTO `sys_module_lang` VALUES ('content_check', 'zh', '审核');
INSERT INTO `sys_module_lang` VALUES ('content_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('content_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('content_delete', 'zh', '删除');
INSERT INTO `sys_module_lang` VALUES ('content_export', 'en', 'Export');
INSERT INTO `sys_module_lang` VALUES ('content_export', 'ja', '輸出');
INSERT INTO `sys_module_lang` VALUES ('content_export', 'zh', '导出');
INSERT INTO `sys_module_lang` VALUES ('content_extend', 'en', 'Content extension');
INSERT INTO `sys_module_lang` VALUES ('content_extend', 'ja', 'コンテンツ拡張');
INSERT INTO `sys_module_lang` VALUES ('content_extend', 'zh', '内容扩展');
INSERT INTO `sys_module_lang` VALUES ('content_menu', 'en', 'Content management');
INSERT INTO `sys_module_lang` VALUES ('content_menu', 'ja', 'コンテンツ管理');
INSERT INTO `sys_module_lang` VALUES ('content_menu', 'zh', '内容管理');
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
INSERT INTO `sys_module_lang` VALUES ('content_select_category', 'en', 'Select category');
INSERT INTO `sys_module_lang` VALUES ('content_select_category', 'ja', '分類を選択');
INSERT INTO `sys_module_lang` VALUES ('content_select_category', 'zh', '选择分类');
INSERT INTO `sys_module_lang` VALUES ('content_select_category_type', 'en', 'Select category type');
INSERT INTO `sys_module_lang` VALUES ('content_select_category_type', 'ja', '分類タイプを選択');
INSERT INTO `sys_module_lang` VALUES ('content_select_category_type', 'zh', '选择分类类型');
INSERT INTO `sys_module_lang` VALUES ('content_select_content', 'en', 'Select content');
INSERT INTO `sys_module_lang` VALUES ('content_select_content', 'ja', 'コンテンツを選択');
INSERT INTO `sys_module_lang` VALUES ('content_select_content', 'zh', '选择内容');
INSERT INTO `sys_module_lang` VALUES ('content_select_tag', 'en', 'Select tag');
INSERT INTO `sys_module_lang` VALUES ('content_select_tag', 'ja', 'タグを選択');
INSERT INTO `sys_module_lang` VALUES ('content_select_tag', 'zh', '选择标签');
INSERT INTO `sys_module_lang` VALUES ('content_select_tag_type', 'en', 'Select tag type');
INSERT INTO `sys_module_lang` VALUES ('content_select_tag_type', 'ja', 'タグの種類を選択');
INSERT INTO `sys_module_lang` VALUES ('content_select_tag_type', 'zh', '选择标签类型');
INSERT INTO `sys_module_lang` VALUES ('content_select_template', 'en', 'Select template');
INSERT INTO `sys_module_lang` VALUES ('content_select_template', 'ja', 'テンプレートを選択');
INSERT INTO `sys_module_lang` VALUES ('content_select_template', 'zh', '选择模板');
INSERT INTO `sys_module_lang` VALUES ('content_select_user', 'en', 'Select user');
INSERT INTO `sys_module_lang` VALUES ('content_select_user', 'ja', 'ユーザーを選択');
INSERT INTO `sys_module_lang` VALUES ('content_select_user', 'zh', '选择用户');
INSERT INTO `sys_module_lang` VALUES ('content_select_vote', 'en', 'Select vote');
INSERT INTO `sys_module_lang` VALUES ('content_select_vote', 'ja', '投票を選択');
INSERT INTO `sys_module_lang` VALUES ('content_select_vote', 'zh', '选择投票');
INSERT INTO `sys_module_lang` VALUES ('content_sort', 'en', 'Sort');
INSERT INTO `sys_module_lang` VALUES ('content_sort', 'ja', 'トッピング');
INSERT INTO `sys_module_lang` VALUES ('content_sort', 'zh', '置顶');
INSERT INTO `sys_module_lang` VALUES ('content_uncheck', 'en', 'Uncheck');
INSERT INTO `sys_module_lang` VALUES ('content_uncheck', 'ja', '審査を取り消す');
INSERT INTO `sys_module_lang` VALUES ('content_uncheck', 'zh', '撤销审核');
INSERT INTO `sys_module_lang` VALUES ('content_view', 'en', 'View');
INSERT INTO `sys_module_lang` VALUES ('content_view', 'ja', '見る');
INSERT INTO `sys_module_lang` VALUES ('content_view', 'zh', '查看');
INSERT INTO `sys_module_lang` VALUES ('content_vote', 'en', 'Voting Management');
INSERT INTO `sys_module_lang` VALUES ('content_vote', 'ja', '投票管理');
INSERT INTO `sys_module_lang` VALUES ('content_vote', 'zh', '投票管理');
INSERT INTO `sys_module_lang` VALUES ('content_vote_add', 'en', 'Add/edit');
INSERT INTO `sys_module_lang` VALUES ('content_vote_add', 'ja', '追加/変更');
INSERT INTO `sys_module_lang` VALUES ('content_vote_add', 'zh', '增加/修改');
INSERT INTO `sys_module_lang` VALUES ('content_vote_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('content_vote_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('content_vote_delete', 'zh', '删除');
INSERT INTO `sys_module_lang` VALUES ('content_vote_view', 'en', 'View');
INSERT INTO `sys_module_lang` VALUES ('content_vote_view', 'ja', '見る');
INSERT INTO `sys_module_lang` VALUES ('content_vote_view', 'zh', '查看');
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
INSERT INTO `sys_module_lang` VALUES ('develop', 'en', 'Develop');
INSERT INTO `sys_module_lang` VALUES ('develop', 'ja', '開発');
INSERT INTO `sys_module_lang` VALUES ('develop', 'zh', '开发');
INSERT INTO `sys_module_lang` VALUES ('dictionary_add', 'en', 'Add/edit');
INSERT INTO `sys_module_lang` VALUES ('dictionary_add', 'ja', '追加/変更');
INSERT INTO `sys_module_lang` VALUES ('dictionary_add', 'zh', '添加/修改');
INSERT INTO `sys_module_lang` VALUES ('dictionary_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('dictionary_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('dictionary_delete', 'zh', '删除');
INSERT INTO `sys_module_lang` VALUES ('dictionary_list', 'en', 'Dictionary management');
INSERT INTO `sys_module_lang` VALUES ('dictionary_list', 'ja', 'データ辞書');
INSERT INTO `sys_module_lang` VALUES ('dictionary_list', 'zh', '数据字典');
INSERT INTO `sys_module_lang` VALUES ('domain_config', 'en', 'Edit');
INSERT INTO `sys_module_lang` VALUES ('domain_config', 'ja', '変更');
INSERT INTO `sys_module_lang` VALUES ('domain_config', 'zh', '修改');
INSERT INTO `sys_module_lang` VALUES ('domain_list', 'en', 'Domain management');
INSERT INTO `sys_module_lang` VALUES ('domain_list', 'ja', 'ドメイン名をバインド');
INSERT INTO `sys_module_lang` VALUES ('domain_list', 'zh', '绑定域名');
INSERT INTO `sys_module_lang` VALUES ('file_menu', 'en', 'File maintenance');
INSERT INTO `sys_module_lang` VALUES ('file_menu', 'ja', 'ファイル管理');
INSERT INTO `sys_module_lang` VALUES ('file_menu', 'zh', '文件管理');
INSERT INTO `sys_module_lang` VALUES ('log_login', 'en', 'Login log');
INSERT INTO `sys_module_lang` VALUES ('log_login', 'ja', 'ログインログ');
INSERT INTO `sys_module_lang` VALUES ('log_login', 'zh', '登录日志');
INSERT INTO `sys_module_lang` VALUES ('log_login_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('log_login_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('log_login_delete', 'zh', '删除');
INSERT INTO `sys_module_lang` VALUES ('log_menu', 'en', 'Log management');
INSERT INTO `sys_module_lang` VALUES ('log_menu', 'ja', 'ログ管理');
INSERT INTO `sys_module_lang` VALUES ('log_menu', 'zh', '日志管理');
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
INSERT INTO `sys_module_lang` VALUES ('maintenance', 'en', 'Maintain');
INSERT INTO `sys_module_lang` VALUES ('maintenance', 'ja', '維持');
INSERT INTO `sys_module_lang` VALUES ('maintenance', 'zh', '维护');
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
INSERT INTO `sys_module_lang` VALUES ('myself', 'ja', '個人');
INSERT INTO `sys_module_lang` VALUES ('myself', 'zh', '个人');
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
INSERT INTO `sys_module_lang` VALUES ('myself_menu', 'en', 'My account');
INSERT INTO `sys_module_lang` VALUES ('myself_menu', 'ja', '私に関連する情報');
INSERT INTO `sys_module_lang` VALUES ('myself_menu', 'zh', '与我相关');
INSERT INTO `sys_module_lang` VALUES ('myself_password', 'en', 'Change password');
INSERT INTO `sys_module_lang` VALUES ('myself_password', 'ja', 'パスワードを変更');
INSERT INTO `sys_module_lang` VALUES ('myself_password', 'zh', '修改密码');
INSERT INTO `sys_module_lang` VALUES ('myself_token', 'en', 'My login token');
INSERT INTO `sys_module_lang` VALUES ('myself_token', 'ja', '私のログイン授権');
INSERT INTO `sys_module_lang` VALUES ('myself_token', 'zh', '我的登录授权');
INSERT INTO `sys_module_lang` VALUES ('order_history_list', 'en', 'Order history');
INSERT INTO `sys_module_lang` VALUES ('order_history_list', 'ja', 'オーダー履歴');
INSERT INTO `sys_module_lang` VALUES ('order_history_list', 'zh', '订单历史');
INSERT INTO `sys_module_lang` VALUES ('order_list', 'en', 'Order management');
INSERT INTO `sys_module_lang` VALUES ('order_list', 'ja', 'オーダー管理');
INSERT INTO `sys_module_lang` VALUES ('order_list', 'zh', '订单管理');
INSERT INTO `sys_module_lang` VALUES ('page', 'en', 'Page');
INSERT INTO `sys_module_lang` VALUES ('page', 'ja', 'ページ');
INSERT INTO `sys_module_lang` VALUES ('page', 'zh', '页面');
INSERT INTO `sys_module_lang` VALUES ('page_list', 'en', 'Page management');
INSERT INTO `sys_module_lang` VALUES ('page_list', 'ja', 'ページ管理');
INSERT INTO `sys_module_lang` VALUES ('page_list', 'zh', '页面管理');
INSERT INTO `sys_module_lang` VALUES ('page_menu', 'en', 'Page maintenance');
INSERT INTO `sys_module_lang` VALUES ('page_menu', 'ja', 'ページのメンテナンス');
INSERT INTO `sys_module_lang` VALUES ('page_menu', 'zh', '页面维护');
INSERT INTO `sys_module_lang` VALUES ('page_metadata', 'en', 'Metadata management');
INSERT INTO `sys_module_lang` VALUES ('page_metadata', 'ja', 'メタデータ管理');
INSERT INTO `sys_module_lang` VALUES ('page_metadata', 'zh', '元数据管理');
INSERT INTO `sys_module_lang` VALUES ('page_publish', 'en', 'Publish');
INSERT INTO `sys_module_lang` VALUES ('page_publish', 'ja', 'ページを生成する');
INSERT INTO `sys_module_lang` VALUES ('page_publish', 'zh', '生成页面');
INSERT INTO `sys_module_lang` VALUES ('page_save', 'en', 'Save configuration');
INSERT INTO `sys_module_lang` VALUES ('page_save', 'ja', 'ページ設定を保存');
INSERT INTO `sys_module_lang` VALUES ('page_save', 'zh', '保存页面配置');
INSERT INTO `sys_module_lang` VALUES ('page_select_category', 'en', 'Select category');
INSERT INTO `sys_module_lang` VALUES ('page_select_category', 'ja', '分類を選択');
INSERT INTO `sys_module_lang` VALUES ('page_select_category', 'zh', '选择分类');
INSERT INTO `sys_module_lang` VALUES ('page_select_category_type', 'en', 'Select category type');
INSERT INTO `sys_module_lang` VALUES ('page_select_category_type', 'ja', '分類タイプを選択');
INSERT INTO `sys_module_lang` VALUES ('page_select_category_type', 'zh', '选择分类类型');
INSERT INTO `sys_module_lang` VALUES ('page_select_content', 'en', 'Select content');
INSERT INTO `sys_module_lang` VALUES ('page_select_content', 'ja', 'コンテンツを選択');
INSERT INTO `sys_module_lang` VALUES ('page_select_content', 'zh', '选择内容');
INSERT INTO `sys_module_lang` VALUES ('page_select_tag_type', 'en', 'Select tag type');
INSERT INTO `sys_module_lang` VALUES ('page_select_tag_type', 'ja', 'タグの種類を選択');
INSERT INTO `sys_module_lang` VALUES ('page_select_tag_type', 'zh', '选择标签类型');
INSERT INTO `sys_module_lang` VALUES ('page_select_template', 'en', 'Select template');
INSERT INTO `sys_module_lang` VALUES ('page_select_template', 'ja', '选择模板テンプレートを選択');
INSERT INTO `sys_module_lang` VALUES ('page_select_template', 'zh', '选择模板');
INSERT INTO `sys_module_lang` VALUES ('page_select_user', 'en', 'Select user');
INSERT INTO `sys_module_lang` VALUES ('page_select_user', 'ja', 'ユーザーを選択');
INSERT INTO `sys_module_lang` VALUES ('page_select_user', 'zh', '选择用户');
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
INSERT INTO `sys_module_lang` VALUES ('place_template_data_dictionary', 'en', 'Select data dictionary');
INSERT INTO `sys_module_lang` VALUES ('place_template_data_dictionary', 'ja', 'データ辞書を選択');
INSERT INTO `sys_module_lang` VALUES ('place_template_data_dictionary', 'zh', '选择数据字典');
INSERT INTO `sys_module_lang` VALUES ('place_template_fragment', 'en', 'Template fragment');
INSERT INTO `sys_module_lang` VALUES ('place_template_fragment', 'ja', 'テンプレートフラグメント');
INSERT INTO `sys_module_lang` VALUES ('place_template_fragment', 'zh', '模板片段');
INSERT INTO `sys_module_lang` VALUES ('place_template_help', 'en', 'Template help');
INSERT INTO `sys_module_lang` VALUES ('place_template_help', 'ja', 'テンプレートのヘルプ');
INSERT INTO `sys_module_lang` VALUES ('place_template_help', 'zh', '模板帮助');
INSERT INTO `sys_module_lang` VALUES ('place_template_list', 'en', 'Page fragment template');
INSERT INTO `sys_module_lang` VALUES ('place_template_list', 'ja', 'ページフラグメントテンプレート');
INSERT INTO `sys_module_lang` VALUES ('place_template_list', 'zh', '页面片段模板');
INSERT INTO `sys_module_lang` VALUES ('place_template_metadata', 'en', 'Edit metadata');
INSERT INTO `sys_module_lang` VALUES ('place_template_metadata', 'ja', 'メタデータの変更');
INSERT INTO `sys_module_lang` VALUES ('place_template_metadata', 'zh', '修改元数据');
INSERT INTO `sys_module_lang` VALUES ('place_template_place', 'en', 'Page fragment');
INSERT INTO `sys_module_lang` VALUES ('place_template_place', 'ja', 'ページフラグメント');
INSERT INTO `sys_module_lang` VALUES ('place_template_place', 'zh', '页面片段');
INSERT INTO `sys_module_lang` VALUES ('place_template_webfile', 'en', 'Website file');
INSERT INTO `sys_module_lang` VALUES ('place_template_webfile', 'ja', 'ウェブサイトファイル');
INSERT INTO `sys_module_lang` VALUES ('place_template_webfile', 'zh', '网站文件');
INSERT INTO `sys_module_lang` VALUES ('place_view', 'en', 'View');
INSERT INTO `sys_module_lang` VALUES ('place_view', 'ja', '推奨ビットデータを見る');
INSERT INTO `sys_module_lang` VALUES ('place_view', 'zh', '查看推荐位数据');
INSERT INTO `sys_module_lang` VALUES ('refund_list', 'en', 'Refund management');
INSERT INTO `sys_module_lang` VALUES ('refund_list', 'ja', '払い戻し管理');
INSERT INTO `sys_module_lang` VALUES ('refund_list', 'zh', '退款管理');
INSERT INTO `sys_module_lang` VALUES ('refund_refund', 'en', 'Refund');
INSERT INTO `sys_module_lang` VALUES ('refund_refund', 'ja', '払い戻し');
INSERT INTO `sys_module_lang` VALUES ('refund_refund', 'zh', '退款');
INSERT INTO `sys_module_lang` VALUES ('report_user', 'en', 'User report');
INSERT INTO `sys_module_lang` VALUES ('report_user', 'ja', 'ユーザーデータの監視');
INSERT INTO `sys_module_lang` VALUES ('report_user', 'zh', '用户数据监控');
INSERT INTO `sys_module_lang` VALUES ('role_add', 'en', 'Add/edit');
INSERT INTO `sys_module_lang` VALUES ('role_add', 'ja', '追加/変更');
INSERT INTO `sys_module_lang` VALUES ('role_add', 'zh', '增加/修改');
INSERT INTO `sys_module_lang` VALUES ('role_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('role_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('role_delete', 'zh', '删除');
INSERT INTO `sys_module_lang` VALUES ('role_list', 'en', 'Role management');
INSERT INTO `sys_module_lang` VALUES ('role_list', 'ja', '役割管理');
INSERT INTO `sys_module_lang` VALUES ('role_list', 'zh', '角色管理');
INSERT INTO `sys_module_lang` VALUES ('system_menu', 'en', 'System maintenance');
INSERT INTO `sys_module_lang` VALUES ('system_menu', 'ja', 'システムメンテナンス');
INSERT INTO `sys_module_lang` VALUES ('system_menu', 'zh', '系统维护');
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
INSERT INTO `sys_module_lang` VALUES ('template_content_form', 'en', 'Content contribute form');
INSERT INTO `sys_module_lang` VALUES ('template_content_form', 'ja', 'コンテンツ送信フォーム');
INSERT INTO `sys_module_lang` VALUES ('template_content_form', 'zh', '内容投稿表单');
INSERT INTO `sys_module_lang` VALUES ('template_data_dictionary', 'en', 'Select data dictionary');
INSERT INTO `sys_module_lang` VALUES ('template_data_dictionary', 'ja', 'データ辞書を選択');
INSERT INTO `sys_module_lang` VALUES ('template_data_dictionary', 'zh', '选择数据字典');
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
INSERT INTO `sys_module_lang` VALUES ('template_list', 'en', 'Template management');
INSERT INTO `sys_module_lang` VALUES ('template_list', 'ja', 'テンプレートファイル管理');
INSERT INTO `sys_module_lang` VALUES ('template_list', 'zh', '模板文件管理');
INSERT INTO `sys_module_lang` VALUES ('template_metadata', 'en', 'Edit metadata');
INSERT INTO `sys_module_lang` VALUES ('template_metadata', 'ja', 'メタデータの変更');
INSERT INTO `sys_module_lang` VALUES ('template_metadata', 'zh', '修改元数据');
INSERT INTO `sys_module_lang` VALUES ('template_place', 'en', 'Page fragment');
INSERT INTO `sys_module_lang` VALUES ('template_place', 'ja', 'ページフラグメント');
INSERT INTO `sys_module_lang` VALUES ('template_place', 'zh', '页面片段');
INSERT INTO `sys_module_lang` VALUES ('template_place_form', 'en', 'Page fragment data contribute form');
INSERT INTO `sys_module_lang` VALUES ('template_place_form', 'ja', 'ページフラグメント提出フォーム');
INSERT INTO `sys_module_lang` VALUES ('template_place_form', 'zh', '页面片段投稿表单');
INSERT INTO `sys_module_lang` VALUES ('template_upload', 'en', 'Upload template');
INSERT INTO `sys_module_lang` VALUES ('template_upload', 'ja', 'テンプレートをアップロードする');
INSERT INTO `sys_module_lang` VALUES ('template_upload', 'zh', '上传模板');
INSERT INTO `sys_module_lang` VALUES ('template_website_file', 'en', 'Website file');
INSERT INTO `sys_module_lang` VALUES ('template_website_file', 'ja', 'ウェブサイトファイル');
INSERT INTO `sys_module_lang` VALUES ('template_website_file', 'zh', '网站文件');
INSERT INTO `sys_module_lang` VALUES ('trade_menu', 'en', 'Trade menegent');
INSERT INTO `sys_module_lang` VALUES ('trade_menu', 'ja', 'ビジネス管理');
INSERT INTO `sys_module_lang` VALUES ('trade_menu', 'zh', '商务管理');
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
INSERT INTO `sys_module_lang` VALUES ('user_menu', 'en', 'User maintenance');
INSERT INTO `sys_module_lang` VALUES ('user_menu', 'ja', 'ユーザー管理');
INSERT INTO `sys_module_lang` VALUES ('user_menu', 'zh', '用户管理');
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
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `url` varchar(100) NOT NULL COMMENT '授权地址',
  PRIMARY KEY  (`role_id`,`url`)
) COMMENT='角色授权地址';

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
  PRIMARY KEY  (`role_id`,`module_id`),
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
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  PRIMARY KEY  (`role_id`,`user_id`),
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
  `parent_id` smallint(6) DEFAULT NULL COMMENT '父站点ID',
  `name` varchar(50) NOT NULL COMMENT '站点名',
  `use_static` tinyint(1) NOT NULL COMMENT '启用静态化',
  `site_path` varchar(255) NOT NULL COMMENT '站点地址',
  `use_ssi` tinyint(1) NOT NULL COMMENT '启用服务器端包含',
  `dynamic_path` varchar(255) NOT NULL COMMENT '动态站点地址',
  `disabled` tinyint(1) NOT NULL COMMENT '禁用',
  PRIMARY KEY  (`id`),
  KEY `sys_site_disabled` (`disabled`),
  KEY `sys_site_parent_id` (`parent_id`)
) AUTO_INCREMENT=2 COMMENT='站点';

-- ----------------------------
-- Records of sys_site
-- ----------------------------
INSERT INTO `sys_site` VALUES ('1', null ,'PublicCMS', '0', '//dev.publiccms.com:8080/publiccms/webfile/', '0', '//dev.publiccms.com:8080/publiccms/', '0');

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
  KEY `sys_task_status` (`status`),
  KEY `sys_task_site_id` (`site_id`),
  KEY `sys_task_update_date` (`update_date`)
) AUTO_INCREMENT=8 COMMENT='任务计划';

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL auto_increment,
  `site_id` smallint(6) NOT NULL COMMENT '站点ID',
  `name` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(128) NOT NULL COMMENT '密码',
  `salt` varchar(20) DEFAULT NULL COMMENT '混淆码,为空时则密码为md5,为10位时sha512(sha512(password)+salt)',
  `weak_password` tinyint(1) NOT NULL DEFAULT '0' COMMENT '弱密码',
  `nick_name` varchar(45) NOT NULL COMMENT '昵称',
  `dept_id` int(11) default NULL COMMENT '部门',
  `owns_all_content` tinyint(1) NOT NULL DEFAULT '1' COMMENT '拥有所有内容权限',
  `roles` text COMMENT '角色',
  `email` varchar(100) default NULL COMMENT '邮箱地址',
  `email_checked` tinyint(1) NOT NULL COMMENT '已验证邮箱',
  `superuser_access` tinyint(1) NOT NULL COMMENT '是否管理员',
  `disabled` tinyint(1) NOT NULL COMMENT '是否禁用',
  `last_login_date` datetime default NULL COMMENT '最后登录日期',
  `last_login_ip` varchar(130) default NULL COMMENT '最后登录ip',
  `login_count` int(11) NOT NULL COMMENT '登录次数',
  `registered_date` datetime default NULL COMMENT '注册日期',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `sys_user_name` (`name`,`site_id`),
  UNIQUE KEY `sys_user_nick_name` (`nick_name`,`site_id`),
  KEY `sys_user_email` (`email`),
  KEY `sys_user_disabled` (`disabled`),
  KEY `sys_user_lastLoginDate` (`last_login_date`),
  KEY `sys_user_email_checked` (`email_checked`),
  KEY `sys_user_dept_id` (`dept_id`),
  KEY `sys_user_site_id` (`site_id`)
) AUTO_INCREMENT=2 COMMENT='用户';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', '1', 'admin', '21232f297a57a5a743894a0e4a801fc3', NULL, 1, 'admin', '1', '1', '1', 'master@sanluan.com', '0', '1', '0', '2019-01-01 00:00:00', '127.0.0.1', '0', '2019-01-01 00:00:00');


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
  `expiry_date` datetime DEFAULT NULL COMMENT '过期日期',
  `login_ip` varchar(130) NOT NULL COMMENT '登录IP',
  PRIMARY KEY  (`auth_token`),
  KEY `sys_user_token_user_id` (`user_id`),
  KEY `sys_user_token_create_date` (`create_date`),
  KEY `sys_user_token_channel` (`channel`),
  KEY `sys_user_token_site_id` (`site_id`)
) COMMENT='用户令牌';

-- ----------------------------
-- Records of sys_user_token
-- ----------------------------
