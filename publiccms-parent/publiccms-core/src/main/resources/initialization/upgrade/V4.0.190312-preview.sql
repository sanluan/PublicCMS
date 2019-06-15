-- 2019-03-29 --
ALTER TABLE `cms_content_file`
    ADD COLUMN `width` int(0) NULL COMMENT '宽度' AFTER `file_size`,
    ADD COLUMN `height` int(0) NULL COMMENT '高度' AFTER `width`;
ALTER TABLE `log_upload`
    ADD COLUMN `width` int(0) NULL COMMENT '宽度' AFTER `file_size`,
    ADD COLUMN `height` int(0) NULL COMMENT '高度' AFTER `width`;
-- 2019-06-15 --
DROP TABLE IF EXISTS `trade_order`;
CREATE TABLE `trade_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `amount` decimal(10,2) NOT NULL COMMENT '总价',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `trade_type` varchar(20) NOT NULL COMMENT '订单类型',
  `serial_number` varchar(100) NOT NULL COMMENT '订单流水',
  `account_type` varchar(20) NOT NULL COMMENT '支付账户类型',
  `account_serial_number` varchar(100) DEFAULT NULL COMMENT '支付账号流水',
  `ip` varchar(64) NOT NULL COMMENT 'IP地址',
  `status` int(10) NOT NULL COMMENT '状态:0待支付,1已支付,2待退款,3退款成功',
  `update_date` datetime DEFAULT NULL COMMENT '更新日期',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `payment_date` datetime DEFAULT NULL COMMENT '支付日期',
  PRIMARY KEY (`id`),
  KEY `account_type` (`account_type`,`account_serial_number`),
  KEY `site_id` (`site_id`,`user_id`,`status`,`create_date`),
  KEY `trade_type` (`trade_type`,`serial_number`)
) ;
DROP TABLE IF EXISTS `trade_refund`;
CREATE TABLE `trade_refund` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `amount` decimal(10,2) NOT NULL COMMENT '申请退款金额',
  `reason` varchar(255) DEFAULT NULL COMMENT '退款原因',
  `update_date` datetime DEFAULT NULL COMMENT '更新日期',
  `refund_user_id` bigint(20) DEFAULT NULL COMMENT '退款操作人员',
  `refund_amount` decimal(10,2) DEFAULT NULL COMMENT '退款金额',
  `status` int(10) NOT NULL COMMENT '状态:0待退款,1已退款,2取消退款,3拒绝退款,4退款失败',
  `reply` varchar(255) DEFAULT NULL COMMENT '回复',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `processing_date` datetime DEFAULT NULL COMMENT '处理日期',
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`,`status`,`create_date`)
) ;
