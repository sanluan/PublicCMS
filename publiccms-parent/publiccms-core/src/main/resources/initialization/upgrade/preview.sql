-- 2021-01-14 --
ALTER TABLE  `cms_content`
    ADD INDEX `cms_content_quote_content_id`(`site_id`, `quote_content_id`);
CREATE TABLE `log_visit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `site_id` smallint(6) NOT NULL COMMENT '站点ID',
  `session_id` varchar(50) NOT NULL COMMENT '会话ID',
  `visit_date` date NOT NULL COMMENT '访问日期',
  `visit_hour` tinyint(4) NOT NULL COMMENT '访问小时',
  `ip` varchar(130) NOT NULL COMMENT 'IP',
  `user_agent` varchar(500) DEFAULT NULL COMMENT 'User Agent',
  `url` varchar(2048) NOT NULL COMMENT '访问路径',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `screen_width` int(11) DEFAULT NULL COMMENT '屏幕宽度',
  `screen_height` int(11) DEFAULT NULL COMMENT '屏幕高度',
  `referer_url` varchar(2048) DEFAULT NULL COMMENT '来源URL',
  `item_type` varchar(50) DEFAULT NULL COMMENT '项目类型',
  `item_id` varchar(50) DEFAULT NULL COMMENT '项目ID',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`),
  KEY `log_visit_visit_date` (`site_id`,`visit_date`,`visit_hour`),
  KEY `log_visit_session_id` (`site_id`,`session_id`,`visit_date`,`create_date`,`ip`)
) COMMENT='访问日志';

CREATE TABLE `log_visit_day` (
  `site_id` smallint(6) NOT NULL COMMENT '站点ID',
  `visit_date` date NOT NULL COMMENT '日期',
  `visit_hour` tinyint(4) NOT NULL COMMENT '小时',
  `pv` bigint(20) NOT NULL COMMENT 'Page Views',
  `uv` bigint(20) DEFAULT NULL COMMENT 'User Views',
  `ipviews` bigint(20) DEFAULT NULL COMMENT 'IP数',
  PRIMARY KEY (`site_id`,`visit_date`,`visit_hour`),
  KEY `log_visit_session_id` (`site_id`,`visit_date`)
)  COMMENT = '访问汇总';

CREATE TABLE `log_visit_session` (
  `site_id` smallint(6) NOT NULL COMMENT '站点ID',
  `session_id` varchar(50) NOT NULL COMMENT '会话ID',
  `visit_date` date NOT NULL COMMENT '日期',
  `last_visit_date` datetime DEFAULT NULL COMMENT '上次访问日期',
  `first_visit_date` datetime DEFAULT NULL COMMENT '首次访问日期',
  `ip` varchar(130) NOT NULL COMMENT 'IP',
  `pv` bigint(20) NOT NULL COMMENT 'PV',
  PRIMARY KEY (`site_id`,`session_id`,`visit_date`),
  KEY `log_visit_visit_date` (`site_id`,`visit_date`,`ip`)
)  COMMENT = '访问会话';

-- 2021-03-25 --
UPDATE `sys_module` SET `authorized_url` =  'cmsContent/push_content,cmsContent/push_content_list,cmsContent/push_to_content,cmsContent/push_page,cmsContent/push_page_list,cmsPlace/add,cmsPlace/save,cmsContent/related,cmsContent/unrelated,cmsPlace/delete,cmsPlace/push' WHERE `id` ='content_push';
-- 20210329 --
ALTER TABLE `log_login` MODIFY COLUMN `error_password` varchar(255) default NULL COMMENT '错误密码' AFTER `create_date`;
-- 2021-05-26 --
INSERT INTO `sys_module` VALUES ('log_visit', 'log/visit', 'log/visitView', 'icon-bolt', 'log_menu', 1, 5);
INSERT INTO `sys_module` VALUES ('log_visit_day', 'log/visitDay', NULL, 'icon-calendar', 'log_menu', 1, 7);
INSERT INTO `sys_module` VALUES ('log_visit_session', 'log/visitSession', NULL, 'icon-comment-alt', 'log_menu', 1, 6);
INSERT INTO `sys_module_lang` VALUES ('log_visit', 'en', 'Visit log');
INSERT INTO `sys_module_lang` VALUES ('log_visit', 'ja', 'アクセスログ');
INSERT INTO `sys_module_lang` VALUES ('log_visit', 'zh', '访问日志');
INSERT INTO `sys_module_lang` VALUES ('log_visit_day', 'en', 'Daily visit log');
INSERT INTO `sys_module_lang` VALUES ('log_visit_day', 'ja', '毎日の訪問ログ');
INSERT INTO `sys_module_lang` VALUES ('log_visit_day', 'zh', '日访问日志');
INSERT INTO `sys_module_lang` VALUES ('log_visit_session', 'en', 'Visit session');
INSERT INTO `sys_module_lang` VALUES ('log_visit_session', 'ja', 'アクセスセッション');
INSERT INTO `sys_module_lang` VALUES ('log_visit_session', 'zh', '访问日志会话');
-- 2021-06-25 --
UPDATE `sys_module` SET `authorized_url` = 'tradeOrder/refund' WHERE `id` ='refund_refund';
UPDATE `sys_module` SET `authorized_url` = 'cmsTemplate/save,cmsTemplate/chipLookup,cmsWebFile/lookup,placeTemplate/form,cmsTemplate/contentForm,cmsCategory/contributeForm,cmsTemplate/demo,cmsTemplate/help,cmsTemplate/upload,cmsTemplate/doUpload,cmsTemplate/export' WHERE `id` ='template_content';
UPDATE `sys_module` SET `authorized_url` = 'taskTemplate/save,taskTemplate/upload,taskTemplate/doUpload,taskTemplate/export,taskTemplate/chipLookup,cmsTemplate/help' WHERE `id` ='task_template_content';
-- 2021-06026 --
CREATE TABLE `cms_content_product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `site_id` smallint(6) NOT NULL COMMENT '站点ID',
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
INSERT INTO `sys_module` VALUES ('product_list', 'cmsContentProduct/list', NULL, 'icon-truck', 'content_menu', 1, 4);
INSERT INTO `sys_module` VALUES ('product_add', 'cmsContentProduct/add', 'cmsContentProduct/save', NULL, 'product_list', 1, 0);
INSERT INTO `sys_module_lang` VALUES ('product_list', 'en', 'Product management');
INSERT INTO `sys_module_lang` VALUES ('product_list', 'ja', '製品管理');
INSERT INTO `sys_module_lang` VALUES ('product_list', 'zh', '产品管理');
INSERT INTO `sys_module_lang` VALUES ('product_add', 'en', 'Edit');
INSERT INTO `sys_module_lang` VALUES ('product_add', 'ja', '変更');
INSERT INTO `sys_module_lang` VALUES ('product_add', 'zh', '修改');
UPDATE `sys_module` SET `sort` = '6' WHERE `id` = 'word_list';
UPDATE `sys_module` SET `sort` = '7' WHERE `id` = 'content_recycle_list';
RENAME TABLE `trade_order` TO `trade_payment`;
RENAME TABLE `trade_order_history` TO `trade_payment_history`;
ALTER TABLE `trade_refund` 
    CHANGE COLUMN `order_id` `payment_id` bigint(20) NOT NULL COMMENT '订单ID' AFTER `id`;
ALTER TABLE `trade_payment_history` CHANGE COLUMN `order_id` `payment_id` bigint(20) NOT NULL COMMENT '订单ID' AFTER `site_id`;
ALTER TABLE `trade_payment` 
  DROP INDEX `trade_order_account_type`,
  DROP INDEX `trade_order_site_id`,
  DROP INDEX `trade_order_trade_type`,
  DROP INDEX `trade_order_create_date`,
  ADD INDEX `trade_payment_account_type`(`account_type`, `account_serial_number`) ,
  ADD INDEX `trade_payment_site_id`(`site_id`, `user_id`, `status`) ,
  ADD INDEX `trade_payment_trade_type`(`trade_type`, `serial_number`) ,
  ADD INDEX `trade_payment_create_date`(`create_date`);
ALTER TABLE `trade_payment_history` 
  DROP INDEX `trade_order_history_site_id`,
  DROP INDEX `trade_order_history_create_date`,
  ADD INDEX `trade_payment_history_site_id`(`site_id`, `payment_id`, `operate`),
  ADD INDEX `trade_payment_history_create_date`(`create_date`);
CREATE TABLE `trade_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `amount` decimal(10,2) NOT NULL COMMENT '总金额',
  `payment_id` bigint(20) DEFAULT NULL COMMENT '支付订单ID',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `addressee` varchar(100) DEFAULT NULL COMMENT '收件人',
  `telephone` varchar(100) DEFAULT NULL COMMENT '电话',
  `ip` varchar(130) NOT NULL COMMENT 'IP地址',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `status` int(11) NOT NULL COMMENT '状态:0待确认,1无效订单,2已付款,3已退款,4已关闭',
  `confirmed` tinyint(1) NOT NULL COMMENT '已确认',
  `processed` tinyint(1) NOT NULL COMMENT '已处理',
  `process_info` varchar(255) DEFAULT NULL COMMENT '处理信息',
  `update_date` datetime DEFAULT NULL COMMENT '更新日期',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `process_date` datetime DEFAULT NULL COMMENT '处理日期',
  `payment_date` datetime DEFAULT NULL COMMENT '支付日期',
  PRIMARY KEY (`id`),
  KEY `trade_order_site_id` (`site_id`,`user_id`,`status`),
  KEY `trade_order_create_date` (`create_date`),
  KEY `trade_order_payment_id` (`site_id`,`payment_id`)
) COMMENT='产品订单';
CREATE TABLE `trade_order_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `site_id` smallint(6) NOT NULL COMMENT '站点ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `operate` varchar(100) NOT NULL COMMENT '操作',
  `content` text COMMENT '内容',
  PRIMARY KEY (`id`),
  KEY `trade_order_history_site_id` (`site_id`,`order_id`,`operate`),
  KEY `trade_order_history_create_date` (`create_date`)
) COMMENT='订单流水';
CREATE TABLE `trade_order_product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `order_id` bigint(20) NOT NULL COMMENT '用户id',
  `content_id` bigint(20) NOT NULL COMMENT '内容ID',
  `product_id` bigint(20) NOT NULL COMMENT '产品ID',
  `price` decimal(10,2) NOT NULL COMMENT '价格',
  `quantity` int(11) NOT NULL COMMENT '数量',
  `amount` decimal(10,2) NOT NULL COMMENT '总金额',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `trade_order_product_site_id` (`site_id`,`order_id`)
) COMMENT='产品订单';
ALTER TABLE `cms_content` 
    ADD COLUMN `has_products` tinyint(1) NOT NULL COMMENT '拥有产品列表' AFTER `has_files`,
    DROP INDEX `cms_content_only_url`,
    ADD INDEX `cms_content_only_url`(`only_url`, `has_images`, `has_files`, `has_products`, `user_id`) ;
ALTER TABLE `trade_payment` 
    ADD COLUMN `process_user_id` bigint(20) NULL COMMENT '处理用户ID' AFTER `processed`;
ALTER TABLE `trade_order` 
    ADD COLUMN `process_user_id` bigint(20) NULL COMMENT '处理用户ID' AFTER `processed`;
-- 2021-06-28 --
ALTER TABLE `trade_refund` 
    ADD COLUMN `site_id` smallint(0) NOT NULL COMMENT '站点ID' AFTER `id`,
    ADD COLUMN `user_id` bigint(20) NOT NULL COMMENT '用户ID' AFTER `payment_id`,
    DROP INDEX `trade_refund_order_id`;
ALTER TABLE `cms_content_related` 
    DROP INDEX `cms_content_related_user_id`,
    ADD INDEX `cms_content_related_user_id`(`content_id`, `sort`);
ALTER TABLE `trade_refund` 
    ADD INDEX `trade_refund_user_id`(`user_id`, `payment_id`, `status`);
-- 2021-06-30 --
UPDATE `sys_module` SET `sort` = '7' WHERE `id` = 'account_history_list';
UPDATE `sys_module` SET `sort` = '6' WHERE `id` = 'account_list';
UPDATE `sys_module` SET `sort` = '5' WHERE `id` = 'refund_list';
INSERT INTO `sys_module` VALUES ('payment_history_list', 'tradePaymentHistory/list', NULL, 'icon-exchange', 'trade_menu', 1, 4);
INSERT INTO `sys_module_lang` VALUES ('payment_history_list', 'en', 'Payment history');
INSERT INTO `sys_module_lang` VALUES ('payment_history_list', 'ja', '支払歴');
INSERT INTO `sys_module_lang` VALUES ('payment_history_list', 'zh', '支付历史');
ALTER TABLE `trade_payment` 
    CHANGE COLUMN  `status` `status` int(11) NOT NULL COMMENT '状态:0待支付,1已支付,2待退款,3已退款,4已关闭' after `ip`;
-- 2021-07-01 --
ALTER TABLE `trade_order` 
    ADD COLUMN `title` varchar(255) NOT NULL COMMENT '标题' AFTER `user_id`;
-- 2021-07-03 --
UPDATE `sys_module` SET `authorized_url` = 'tradeAccount/save,sysUser/lookup' WHERE `id` ='account_add';
INSERT INTO `sys_module` VALUES ('order_confirm', 'tradeOrder/confirmParameters', 'tradeOrder/confirm', NULL, 'order_list', 0, 0);
INSERT INTO `sys_module` VALUES ('order_process', 'tradeOrder/processParameters', 'tradeOrder/process', NULL, 'order_list', 0, 0);
INSERT INTO `sys_module` VALUES ('order_view', 'tradeOrder/view', NULL, NULL, 'order_list', 0, 0);
INSERT INTO `sys_module_lang` VALUES ('order_confirm', 'en', 'Confirm order');
INSERT INTO `sys_module_lang` VALUES ('order_confirm', 'ja', '注文の確認');
INSERT INTO `sys_module_lang` VALUES ('order_confirm', 'zh', '确认订单');
INSERT INTO `sys_module_lang` VALUES ('order_process', 'en', 'Process order');
INSERT INTO `sys_module_lang` VALUES ('order_process', 'ja', 'プロセスオーダー');
INSERT INTO `sys_module_lang` VALUES ('order_process', 'zh', '处理订单');
INSERT INTO `sys_module_lang` VALUES ('order_view', 'en', 'View');
INSERT INTO `sys_module_lang` VALUES ('order_view', 'ja', '見る');
INSERT INTO `sys_module_lang` VALUES ('order_view', 'zh', '查看');
INSERT INTO `sys_module` VALUES ('payment_list', 'tradePayment/list', 'sysUser/lookup', 'icon-money', 'trade_menu', 1, 3);
INSERT INTO `sys_module_lang` VALUES ('payment_list', 'en', 'Payment management');
INSERT INTO `sys_module_lang` VALUES ('payment_list', 'ja', '支払い管理');
INSERT INTO `sys_module_lang` VALUES ('payment_list', 'zh', '支付管理');
UPDATE `sys_module` SET `authorized_url` = 'tradePaymentHistory/view' WHERE `id` ='payment_history_list';
INSERT INTO `sys_module` VALUES ('refund_refuse', 'tradeRefund/refuseParameters', 'tradeOrder/refuse', '', 'refund_list', 0, 0);
INSERT INTO `sys_module_lang` VALUES ('refund_refuse', 'en', 'Refuse');
INSERT INTO `sys_module_lang` VALUES ('refund_refuse', 'ja', 'ごみ');
INSERT INTO `sys_module_lang` VALUES ('refund_refuse', 'zh', '拒绝');
-- 2021-07-04 --
UPDATE `sys_module` SET `menu` = '1' WHERE `id` ='trade_menu';
-- 2021-06-08 --
DELETE FROM `sys_module` WHERE `id` ='repo_sync';
DELETE FROM `sys_module_lang` WHERE `module_id` ='repo_sync';