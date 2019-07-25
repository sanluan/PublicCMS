-- 2019-03-29 --
ALTER TABLE `cms_content_file`
    ADD COLUMN `width` int(0) NULL COMMENT '宽度' AFTER `file_size`,
    ADD COLUMN `height` int(0) NULL COMMENT '高度' AFTER `width`;
ALTER TABLE `log_upload`
    ADD COLUMN `width` int(0) NULL COMMENT '宽度' AFTER `file_size`,
    ADD COLUMN `height` int(0) NULL COMMENT '高度' AFTER `width`;
-- 2019-06-15 --
-- ----------------------------
-- Table structure for trade_account
-- ----------------------------
DROP TABLE IF EXISTS `trade_account`;
CREATE TABLE `trade_account`  (
  `id` bigint(20) NOT NULL COMMENT '用户ID',
  `site_id` smallint(6) NOT NULL COMMENT '站点ID',
  `amount` decimal(10, 2) NOT NULL COMMENT '金额',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`),
  INDEX `site_id`(`site_id`, `update_date`) 
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
  `status` int(10) NOT NULL COMMENT '类型:0预充值,1消费,2充值,3退款',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`),
  KEY `site_id` (`site_id`,`account_id`,`status`,`create_date`)
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
  `ip` varchar(64) NOT NULL COMMENT 'IP地址',
  `status` int(10) NOT NULL COMMENT '状态:0待支付,1已支付,2待退款,3退款成功',
  `processed` tinyint(1) NOT NULL COMMENT '已处理',
  `update_date` datetime DEFAULT NULL COMMENT '更新日期',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `process_date` datetime DEFAULT NULL COMMENT '处理日期',
  `payment_date` datetime(0) NULL DEFAULT NULL COMMENT '支付日期',
  PRIMARY KEY (`id`),
  INDEX `account_type`(`account_type`, `account_serial_number`),
  INDEX `site_id`(`site_id`, `user_id`, `status`, `create_date`),
  INDEX `trade_type`(`trade_type`, `serial_number`)
) COMMENT = '支付订单';

-- ----------------------------
-- Table structure for trade_order_history
-- ----------------------------
DROP TABLE IF EXISTS `trade_order_history`;
CREATE TABLE `trade_order_history`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `site_id` smallint(6) NOT NULL COMMENT '站点ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `create_date` datetime(0) NOT NULL COMMENT '创建日期',
  `operate` varchar(100) NOT NULL COMMENT '操作',
  `content` text COMMENT '内容',
  PRIMARY KEY (`id`),
  KEY `site_id` (`site_id`,`order_id`,`operate`),
  KEY `create_date` (`create_date`)
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
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  `refund_user_id` bigint(20) NULL DEFAULT NULL COMMENT '退款操作人员',
  `refund_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '退款金额',
  `status` int(10) NOT NULL COMMENT '状态:0待退款,1已退款,2取消退款,3拒绝退款,4退款失败',
  `reply` varchar(255) NULL DEFAULT NULL COMMENT '回复',
  `create_date` datetime(0) NOT NULL COMMENT '创建日期',
  `processing_date` datetime(0) NULL DEFAULT NULL COMMENT '处理日期',
  PRIMARY KEY (`id`),
  INDEX `order_id`(`order_id`, `status`, `create_date`)
) COMMENT = '退款申请';

INSERT INTO `sys_module` VALUES ('account_history_list', 'tradeAccountHistory/list', 'sysUser/lookup', 'icon-book', 'trade_menu', 1, 5);
INSERT INTO `sys_module` VALUES ('account_list', 'tradeAccount/list', NULL, 'icon-credit-card', 'trade_menu', 1, 4);
INSERT INTO `sys_module` VALUES ('account_add', 'tradeAccount/add', 'tradeAccount/save', '', 'account_list', 0, 1);
INSERT INTO `sys_module` VALUES ('account_recharge', 'tradeAccount/rechargeParameters', 'tradeAccount/recharge', '', 'account_list', 0, 2);
INSERT INTO `sys_module` VALUES ('order_history_list', 'tradeOrderHistory/list', NULL, 'icon-calendar', 'trade_menu', 1, 2);
INSERT INTO `sys_module` VALUES ('order_list', 'tradeOrder/list', 'sysUser/lookup', 'icon-barcode', 'trade_menu', 1, 1);
INSERT INTO `sys_module` VALUES ('refund_list', 'tradeRefund/list', 'sysUser/lookup', 'icon-signout', 'trade_menu', 1, 3);
INSERT INTO `sys_module` VALUES ('refund_refund', 'tradeRefund/refundParameters', 'tradeRefund/refund', '', 'refund_list', 0, 1);
INSERT INTO `sys_module` VALUES ('trade_menu', NULL, NULL, 'icon-money', 'maintenance', 1, 0);
INSERT INTO `sys_module_lang` VALUES ('trade_menu', 'en', 'Trade menegent');
INSERT INTO `sys_module_lang` VALUES ('trade_menu', 'ja', 'ビジネス管理');
INSERT INTO `sys_module_lang` VALUES ('trade_menu', 'zh', '商务管理');
INSERT INTO `sys_module_lang` VALUES ('account_history_list', 'en', 'Account History');
INSERT INTO `sys_module_lang` VALUES ('account_history_list', 'ja', 'アカウントの履歴');
INSERT INTO `sys_module_lang` VALUES ('account_history_list', 'zh', '账户历史');
INSERT INTO `sys_module_lang` VALUES ('account_list', 'en', 'Account management');
INSERT INTO `sys_module_lang` VALUES ('account_list', 'ja', 'アカウント管理');
INSERT INTO `sys_module_lang` VALUES ('account_list', 'zh', '账户管理');
INSERT INTO `sys_module_lang` VALUES ('order_history_list', 'en', 'Order history');
INSERT INTO `sys_module_lang` VALUES ('order_history_list', 'ja', 'オーダー履歴');
INSERT INTO `sys_module_lang` VALUES ('order_history_list', 'zh', '订单历史');
INSERT INTO `sys_module_lang` VALUES ('order_list', 'en', 'Order management');
INSERT INTO `sys_module_lang` VALUES ('order_list', 'ja', 'オーダー管理');
INSERT INTO `sys_module_lang` VALUES ('order_list', 'zh', '订单管理');
INSERT INTO `sys_module_lang` VALUES ('refund_list', 'en', 'Refund management');
INSERT INTO `sys_module_lang` VALUES ('refund_list', 'ja', '払い戻し管理');
INSERT INTO `sys_module_lang` VALUES ('refund_list', 'zh', '退款管理');
INSERT INTO `sys_module_lang` VALUES ('account_add', 'en', 'Add');
INSERT INTO `sys_module_lang` VALUES ('account_add', 'ja', '追加');
INSERT INTO `sys_module_lang` VALUES ('account_add', 'zh', '增加');
INSERT INTO `sys_module_lang` VALUES ('account_recharge', 'en', 'Recharge');
INSERT INTO `sys_module_lang` VALUES ('account_recharge', 'ja', 'チャージ');
INSERT INTO `sys_module_lang` VALUES ('account_recharge', 'zh', '充值');
INSERT INTO `sys_module_lang` VALUES ('refund_refund', 'en', 'Refund');
INSERT INTO `sys_module_lang` VALUES ('refund_refund', 'ja', '払い戻し');
INSERT INTO `sys_module_lang` VALUES ('refund_refund', 'zh', '退款');
UPDATE `sys_module` SET `url` = 'cmsCategory/lookupByModelId' WHERE `id` ='content_select_category';
-- 2019-07-25 --
ALTER TABLE `sys_cluster` 
	MODIFY COLUMN `cms_version` varchar(20) NULL DEFAULT NULL COMMENT '版本' AFTER `master`,
	ADD COLUMN `revision` varchar(20) NULL COMMENT '修订' AFTER `cms_version`;
