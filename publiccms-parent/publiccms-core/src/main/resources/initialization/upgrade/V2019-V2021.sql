-- 2019-03-29 --
ALTER TABLE `cms_content_file`
    ADD COLUMN `width` int(11) NULL COMMENT '宽度' AFTER `file_size`,
    ADD COLUMN `height` int(11) NULL COMMENT '高度' AFTER `width`;
ALTER TABLE `log_upload`
    ADD COLUMN `width` int(11) NULL COMMENT '宽度' AFTER `file_size`,
    ADD COLUMN `height` int(11) NULL COMMENT '高度' AFTER `width`;
-- 2019-06-15 --
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

INSERT INTO `sys_module` VALUES ('account_history_list', 'tradeAccountHistory/list', 'sysUser/lookup', 'icon-book', 'trade_menu', 1, 5);
INSERT INTO `sys_module` VALUES ('account_list', 'tradeAccount/list', NULL, 'icon-credit-card', 'trade_menu', 1, 4);
INSERT INTO `sys_module` VALUES ('account_add', 'tradeAccount/add', 'tradeAccount/save', '', 'account_list', 0, 1);
INSERT INTO `sys_module` VALUES ('account_recharge', 'tradeAccount/rechargeParameters', 'tradeAccount/recharge', '', 'account_list', 0, 2);
INSERT INTO `sys_module` VALUES ('order_history_list', 'tradeOrderHistory/list', NULL, 'icon-calendar', 'trade_menu', 1, 2);
INSERT INTO `sys_module` VALUES ('order_list', 'tradeOrder/list', 'sysUser/lookup', 'icon-barcode', 'trade_menu', 1, 1);
INSERT INTO `sys_module` VALUES ('refund_list', 'tradeRefund/list', 'sysUser/lookup', 'icon-signout', 'trade_menu', 1, 3);
INSERT INTO `sys_module` VALUES ('refund_refund', 'tradeRefund/refundParameters', 'tradeRefund/refund', '', 'refund_list', 0, 1);
INSERT INTO `sys_module` VALUES ('trade_menu', NULL, NULL, 'icon-money', 'maintenance', 0, 4);
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
-- 2019-08-22 --
ALTER TABLE `cms_content` 
CHANGE COLUMN `dictionar_values` `dictionary_values` text  NULL COMMENT '数据字典值' AFTER `tag_ids`;
-- 2019-11-21 --
ALTER TABLE `cms_content` 
    ADD COLUMN `contribute` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否投稿' AFTER `copied`;
ALTER TABLE `log_login` 
    MODIFY COLUMN `ip` varchar(130) NOT NULL COMMENT 'IP' AFTER `user_id`;
ALTER TABLE `log_operate` 
    MODIFY COLUMN `ip` varchar(130) NULL DEFAULT NULL COMMENT 'IP' AFTER `operate`;
ALTER TABLE `log_upload` 
    MODIFY COLUMN `ip` varchar(130) NULL DEFAULT NULL COMMENT 'IP' AFTER `height`;
UPDATE `sys_module` SET `authorized_url` = 'cmsContent/check,cmsContent/reject' WHERE `id` ='content_check';
-- 2020-02-21 --
ALTER TABLE  `cms_category`
  DROP INDEX  `code`,
  DROP INDEX  `sort`,
  DROP INDEX  `type_id`,
  DROP INDEX  `site_id`;
ALTER TABLE  `cms_category_type`
  DROP INDEX  `site_id`;
ALTER TABLE  `cms_comment`
  DROP INDEX  `site_id`,
  DROP INDEX  `update_date`,
  DROP INDEX  `reply_id`;
ALTER TABLE  `cms_content`
  DROP INDEX  `check_date`,
  DROP INDEX  `scores`,
  DROP INDEX  `only_url`,
  DROP INDEX  `status`,
  DROP INDEX  `quote_content_id`;
ALTER TABLE  `cms_content_file`
  DROP INDEX  `content_id`,
  DROP INDEX  `sort`,
  DROP INDEX  `file_type`,
  DROP INDEX  `file_size`,
  DROP INDEX  `clicks`,
  DROP INDEX  `user_id`;
ALTER TABLE  `cms_content_related`
  DROP INDEX  `user_id`;
ALTER TABLE  `cms_dictionary`
  DROP INDEX  `site_id`;
ALTER TABLE  `cms_place`
  DROP INDEX  `clicks`,
  DROP INDEX  `site_id`,
  DROP INDEX  `item_type`,
  DROP INDEX  `user_id`,
  DROP INDEX  `publish_date`;
ALTER TABLE  `cms_tag`
  DROP INDEX  `site_id`;
ALTER TABLE  `cms_tag_type`
  DROP INDEX  `site_id`;
ALTER TABLE  `cms_word`
  DROP INDEX  `name`,
  DROP INDEX  `hidden`,
  DROP INDEX  `create_date`,
  DROP INDEX  `search_count`;
ALTER TABLE  `log_login`
  DROP INDEX  `result`,
  DROP INDEX  `user_id`,
  DROP INDEX  `create_date`,
  DROP INDEX  `ip`,
  DROP INDEX  `site_id`,
  DROP INDEX  `channel`;
ALTER TABLE  `log_operate`
  DROP INDEX  `user_id`,
  DROP INDEX  `operate`,
  DROP INDEX  `create_date`,
  DROP INDEX  `ip`,
  DROP INDEX  `site_id`,
  DROP INDEX  `channel`;
ALTER TABLE  `log_task`
  DROP INDEX  `task_id`,
  DROP INDEX  `success`,
  DROP INDEX  `site_id`,
  DROP INDEX  `begintime`;
ALTER TABLE  `log_upload`
  DROP INDEX  `user_id`,
  DROP INDEX  `create_date`,
  DROP INDEX  `ip`,
  DROP INDEX  `site_id`,
  DROP INDEX  `channel`,
  DROP INDEX  `file_type`,
  DROP INDEX  `file_size`;
ALTER TABLE  `sys_app`
  DROP INDEX  `key`,
  DROP INDEX  `site_id`;
ALTER TABLE  `sys_app_client`
  DROP INDEX  `site_id`,
  DROP INDEX  `user_id`;
ALTER TABLE  `sys_app_token`
  DROP INDEX  `app_id`,
  DROP INDEX  `create_date`;
ALTER TABLE  `sys_cluster`
  DROP INDEX  `create_date`;
ALTER TABLE  `sys_dept`
  DROP INDEX  `site_id`;
ALTER TABLE  `sys_domain`
  DROP INDEX  `site_id`;
ALTER TABLE  `sys_email_token`
  DROP INDEX  `create_date`,
  DROP INDEX  `user_id`;
ALTER TABLE  `sys_extend_field`
  DROP INDEX  `sort`;
ALTER TABLE  `sys_module`
  DROP INDEX  `parent_id`,
  DROP INDEX  `sort`;
ALTER TABLE  `sys_role`
  DROP INDEX  `site_id`;
ALTER TABLE  `sys_site`
  DROP INDEX  `disabled`;
ALTER TABLE  `sys_task`
  DROP INDEX  `status`,
  DROP INDEX  `site_id`,
  DROP INDEX  `update_date`;
ALTER TABLE  `sys_user`
  DROP INDEX  `name`,
  DROP INDEX  `nick_name`,
  DROP INDEX  `email`,
  DROP INDEX  `disabled`,
  DROP INDEX  `lastLoginDate`,
  DROP INDEX  `email_checked`,
  DROP INDEX  `dept_id`;
ALTER TABLE  `sys_user_token`
  DROP INDEX  `user_id`,
  DROP INDEX  `create_date`,
  DROP INDEX  `channel`,
  DROP INDEX  `site_id`;
ALTER TABLE  `cms_category`
  ADD UNIQUE INDEX `cms_category_code` (`site_id`,`code`),
  ADD INDEX `cms_category_sort` (`sort`),
  ADD INDEX `cms_category_type_id` (`type_id`,`allow_contribute`),
  ADD INDEX `cms_category_site_id` (`site_id`,`parent_id`,`hidden`,`disabled`);
ALTER TABLE  `cms_category_type`
  ADD INDEX `cms_category_type_site_id` (`site_id`);
ALTER TABLE  `cms_comment`
  ADD INDEX `cms_comment_site_id` (`site_id`,`content_id`,`status`,`disabled`),
  ADD INDEX `cms_comment_update_date` (`update_date`,`create_date`),
  ADD INDEX `cms_comment_reply_id` (`site_id`,`reply_user_id`,`reply_id`);
ALTER TABLE  `cms_content`
  ADD INDEX `cms_content_check_date` (`check_date`,`update_date`),
  ADD INDEX `cms_content_scores` (`scores`,`comments`,`clicks`),
  ADD INDEX `cms_content_only_url` (`only_url`,`has_images`,`has_files`,`user_id`),
  ADD INDEX `cms_content_status` (`site_id`,`status`,`category_id`,`disabled`,`model_id`,`parent_id`,`sort`,`publish_date`,`expiry_date`),
  ADD INDEX `cms_content_quote_content_id`(`site_id`, `quote_content_id`);
ALTER TABLE  `cms_content_file`
  ADD INDEX `cms_content_file_content_id` (`content_id`),
  ADD INDEX `cms_content_file_sort` (`sort`),
  ADD INDEX `cms_content_file_file_type`(`file_type`),
  ADD INDEX `cms_content_file_file_size` (`file_size`),
  ADD INDEX `cms_content_file_clicks` (`clicks`),
  ADD INDEX `cms_content_file_user_id` (`user_id`);
ALTER TABLE  `cms_dictionary`
  ADD INDEX `cms_dictionary_site_id` (`site_id`,`multiple`);
ALTER TABLE  `cms_place`
  ADD INDEX `cms_place_clicks` (`clicks`),
  ADD INDEX `cms_place_site_id` (`site_id`,`path`,`status`,`disabled`),
  ADD INDEX `cms_place_item_type` (`item_type`,`item_id`),
  ADD INDEX `cms_place_user_id` (`user_id`,`check_user_id`),
  ADD INDEX `cms_place_publish_date` (`publish_date`,`create_date`,`expiry_date`);
ALTER TABLE  `cms_tag`
  ADD INDEX `cms_tag_site_id` (`site_id`);
ALTER TABLE  `cms_tag_type`
  ADD INDEX `cms_tag_type_site_id` (`site_id`);
ALTER TABLE  `cms_word`
  ADD UNIQUE INDEX `cms_word_name` (`name`,`site_id`),
  ADD INDEX `cms_word_hidden` (`hidden`),
  ADD INDEX `cms_word_create_date` (`create_date`),
  ADD INDEX `cms_word_search_count` (`search_count`);
ALTER TABLE  `log_login`
  ADD INDEX `log_login_result` (`result`),
  ADD INDEX `log_login_user_id` (`user_id`),
  ADD INDEX `log_login_create_date` (`create_date`),
  ADD INDEX `log_login_ip` (`ip`),
  ADD INDEX `log_login_site_id` (`site_id`),
  ADD INDEX `log_login_channel` (`channel`);
ALTER TABLE  `log_operate`
  ADD INDEX `log_operate_user_id` (`user_id`),
  ADD INDEX `log_operate_operate` (`operate`),
  ADD INDEX `log_operate_create_date` (`create_date`),
  ADD INDEX `log_operate_ip` (`ip`),
  ADD INDEX `log_operate_site_id` (`site_id`),
  ADD INDEX `log_operate_channel` (`channel`);
ALTER TABLE  `log_task`
  ADD INDEX `log_task_task_id` (`task_id`),
  ADD INDEX `log_task_success` (`success`),
  ADD INDEX `log_task_site_id` (`site_id`),
  ADD INDEX `log_task_begintime` (`begintime`);
ALTER TABLE  `log_upload`
  ADD INDEX `log_upload_user_id` (`user_id`),
  ADD INDEX `log_upload_create_date` (`create_date`),
  ADD INDEX `log_upload_ip` (`ip`),
  ADD INDEX `log_upload_site_id` (`site_id`),
  ADD INDEX `log_upload_channel` (`channel`),
  ADD INDEX `log_upload_file_type` (`file_type`),
  ADD INDEX `log_upload_file_size` (`file_size`);
ALTER TABLE  `sys_app`
  ADD UNIQUE INDEX `sys_app_key` (`app_key`),
  ADD INDEX `sys_app_site_id` (`site_id`);
ALTER TABLE  `sys_app_client`
  ADD UNIQUE INDEX `sys_app_client_site_id` (`site_id`,`channel`,`uuid`),
  ADD INDEX `sys_app_client_user_id` (`user_id`,`disabled`,`create_date`) ;
ALTER TABLE  `sys_app_token`
  ADD INDEX `sys_app_token_app_id` (`app_id`),
  ADD INDEX `sys_app_token_create_date` (`create_date`);
ALTER TABLE  `sys_cluster`
  ADD INDEX `sys_cluster_create_date` (`create_date`);
ALTER TABLE  `sys_dept`
  ADD INDEX `sys_dept_site_id` (`site_id`);
ALTER TABLE  `sys_domain`
  ADD INDEX `sys_domain_site_id` (`site_id`);
ALTER TABLE  `sys_email_token`
  ADD INDEX `sys_email_token_create_date` (`create_date`),
  ADD INDEX `sys_email_token_user_id` (`user_id`);
ALTER TABLE  `sys_extend_field`
  ADD INDEX `sys_extend_field_sort` (`sort`);
ALTER TABLE  `sys_module`
  ADD INDEX `sys_module_parent_id` (`parent_id`,`menu`),
  ADD INDEX `sys_module_sort` (`sort`);
ALTER TABLE  `sys_role`
  ADD INDEX `sys_role_site_id` (`site_id`);
ALTER TABLE  `sys_site`
  ADD INDEX `sys_site_disabled` (`disabled`);
ALTER TABLE  `sys_task`
  ADD INDEX `sys_task_status` (`status`),
  ADD INDEX `sys_task_site_id` (`site_id`),
  ADD INDEX `sys_task_update_date` (`update_date`);
ALTER TABLE  `sys_user`
  ADD UNIQUE INDEX `sys_user_name` (`name`,`site_id`),
  ADD UNIQUE INDEX `sys_user_nick_name` (`nick_name`,`site_id`),
  ADD INDEX `sys_user_email` (`email`),
  ADD INDEX `sys_user_disabled` (`disabled`),
  ADD INDEX `sys_user_lastLoginDate` (`last_login_date`),
  ADD INDEX `sys_user_email_checked` (`email_checked`),
  ADD INDEX `sys_user_dept_id` (`dept_id`);
ALTER TABLE  `sys_user_token`
  ADD INDEX `sys_user_token_user_id` (`user_id`),
  ADD INDEX `sys_user_token_create_date` (`create_date`),
  ADD INDEX `sys_user_token_channel` (`channel`),
  ADD INDEX `sys_user_token_site_id` (`site_id`);
UPDATE `sys_module` SET `authorized_url` = 'cmsContent/addMore,file/doUpload,cmsContent/lookup,cmsContent/lookup_list,cmsContent/save,ueditor,ckeditor/upload,kindeditor/upload,file/doBatchUpload' WHERE `id` = 'content_add';
UPDATE `sys_module_lang` SET `value` =  'Maintain' WHERE `lang` ='en' and module_id = 'maintenance';
UPDATE `sys_module_lang` SET `value` =  'Develop' WHERE `lang` ='en' and module_id = 'develop';
-- 2020-03-01 --
ALTER TABLE `cms_comment` 
    ADD COLUMN `replies` int(11) NOT NULL DEFAULT 0 COMMENT '回复数' AFTER `reply_user_id`;
UPDATE `cms_comment` a INNER JOIN ( SELECT count( * ) count, reply_id FROM `cms_comment` WHERE reply_id IS NOT NULL GROUP BY reply_id ) b ON a.id = b.reply_id 
    SET a.replies = b.count;
INSERT INTO `sys_module` VALUES ('comment_edit', 'cmsComment/edit', 'cmsComment/save', NULL, 'comment_list', 0, 0);
INSERT INTO `sys_module` VALUES ('comment_reply', 'cmsComment/reply', 'cmsComment/save', NULL, 'comment_list', 0, 0);
INSERT INTO `sys_module_lang` VALUES ('comment_edit', 'zh', '修改');
INSERT INTO `sys_module_lang` VALUES ('comment_edit', 'en', 'Edit');
INSERT INTO `sys_module_lang` VALUES ('comment_edit', 'ja', '変更');
INSERT INTO `sys_module_lang` VALUES ('comment_reply', 'zh', '回复');
INSERT INTO `sys_module_lang` VALUES ('comment_reply', 'en', 'Reply');
INSERT INTO `sys_module_lang` VALUES ('comment_reply', 'ja', '応答');
-- 2020-03-18 --
ALTER TABLE `cms_word`
  ADD INDEX `cms_word_site_id` (`site_id`);
ALTER TABLE `sys_cluster`
  ADD INDEX `sys_cluster_heartbeat_date` (`heartbeat_date`, `master`);
ALTER TABLE `sys_dept_page`
  ADD INDEX `sys_dept_page_page` (`page`);
ALTER TABLE `sys_module_lang`
  ADD INDEX `sys_module_lang_lang` (`lang`);
ALTER TABLE `sys_role_module`
  ADD INDEX `sys_role_module_module_id` (`module_id`);
ALTER TABLE `sys_role_user`
  ADD INDEX `sys_role_user_user_id` (`user_id`);
ALTER TABLE `sys_site`
  ADD INDEX `sys_site_parent_id` (`parent_id`);
ALTER TABLE `sys_user`
  ADD INDEX `sys_user_site_id` (`site_id`);
  
-- 2020-03-26 --

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

ALTER TABLE `cms_content_related`
  DROP COLUMN `clicks`,
  ADD INDEX `cms_content_related_user_id` (`content_id`,`related_content_id`,`user_id`,`sort`),
  ADD INDEX `cms_content_related_related_content_id` (`related_content_id`);
INSERT INTO `sys_module` VALUES ('content_vote', 'cmsVote/list', NULL, 'icon-ticket', 'content_extend', 1, 4);
INSERT INTO `sys_module` VALUES ('content_vote_add', 'cmsVote/add', 'cmsVote/save', NULL, 'content_vote', 0, 0);
INSERT INTO `sys_module` VALUES ('content_vote_delete', NULL, 'cmsVote/delete', NULL, 'content_vote', 0, 0);
INSERT INTO `sys_module` VALUES ('content_vote_view', 'cmsVote/view', NULL, NULL, 'content_vote', 0, 0);
INSERT INTO `sys_module_lang` VALUES ('content_vote', 'zh', '投票管理');
INSERT INTO `sys_module_lang` VALUES ('content_vote', 'en', 'Voting Management');
INSERT INTO `sys_module_lang` VALUES ('content_vote', 'ja', '投票管理');
INSERT INTO `sys_module_lang` VALUES ('content_vote_add', 'zh', '增加/修改');
INSERT INTO `sys_module_lang` VALUES ('content_vote_add', 'en', 'Add/edit');
INSERT INTO `sys_module_lang` VALUES ('content_vote_add', 'ja', '追加/変更');
INSERT INTO `sys_module_lang` VALUES ('content_vote_delete', 'zh', '删除');
INSERT INTO `sys_module_lang` VALUES ('content_vote_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('content_vote_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('content_vote_view', 'zh', '查看');
INSERT INTO `sys_module_lang` VALUES ('content_vote_view', 'en', 'View');
INSERT INTO `sys_module_lang` VALUES ('content_vote_view', 'ja', '見る');

UPDATE `sys_module_lang` SET `value` =  'Delete' WHERE `lang` ='en' and module_id = 'category_type_delete';
UPDATE `sys_module_lang` SET `value` =  'Delete' WHERE `lang` ='en' and module_id = 'content_recycle_delete';
UPDATE `sys_module_lang` SET `value` =  'Delete' WHERE `lang` ='en' and module_id = 'content_recycle_recycle';
UPDATE `sys_module_lang` SET `value` =  'Delete' WHERE `lang` ='en' and module_id = 'tag_type_delete';
UPDATE `sys_module_lang` SET `value` =  'Add/edit' WHERE `lang` ='en' and module_id = 'tag_type_save';
UPDATE `sys_module_lang` SET `value` =  'Add/edit' WHERE `lang` ='en' and module_id = 'category_type_add';

INSERT INTO `sys_module` VALUES ('content_select_tag', 'cmsTag/lookup', NULL, NULL, 'content_add', 0, 0);
INSERT INTO `sys_module` VALUES ('content_select_vote', 'cmsVote/lookup', NULL, NULL, 'content_add', 0, 0);

INSERT INTO `sys_module_lang` VALUES ('content_select_tag', 'zh', '选择标签');
INSERT INTO `sys_module_lang` VALUES ('content_select_tag', 'en', 'Select tag');
INSERT INTO `sys_module_lang` VALUES ('content_select_tag', 'ja', 'タグを選択');
INSERT INTO `sys_module_lang` VALUES ('content_select_vote', 'zh', '选择投票');
INSERT INTO `sys_module_lang` VALUES ('content_select_vote', 'en', 'Select vote');
INSERT INTO `sys_module_lang` VALUES ('content_select_vote', 'ja', '投票を選択');

ALTER TABLE `sys_user_token` 
    MODIFY COLUMN `login_ip` varchar(130) NOT NULL COMMENT '登录IP' AFTER `expiry_date`;
ALTER TABLE `sys_user` 
    MODIFY COLUMN `last_login_ip` varchar(130) DEFAULT NULL COMMENT '最后登录ip' AFTER `last_login_date`;
ALTER TABLE `sys_app_client` 
    MODIFY COLUMN `last_login_ip` varchar(130) DEFAULT NULL COMMENT '上次登录IP' AFTER `last_login_date`;

-- 2020-06-23 --
ALTER TABLE `cms_tag` 
    DROP INDEX `cms_tag_site_id`,
    ADD INDEX `cms_tag_site_id`(`site_id`, `name`),
    ADD INDEX  `cms_tag_type_id` (`type_id`);
ALTER TABLE `cms_tag_type`
    DROP INDEX `cms_tag_type_site_id`, 
    ADD INDEX `cms_tag_type_site_id` (`site_id`,`name`);
-- 2020-07-27 --
DELETE FROM `sys_module` WHERE id = 'category';
DELETE FROM `sys_module_lang` WHERE module_id = 'category';
UPDATE `sys_module` SET `parent_id` =  'content' WHERE `id` ='category_extend';
UPDATE `sys_module` SET `id` =  'category_list', `parent_id` = 'content_menu',`sort` = '1' WHERE `id` ='category_menu';
UPDATE `sys_module_lang` SET `module_id` =  'category_list' WHERE `module_id` ='category_menu';
UPDATE `sys_module` SET `parent_id` =  'category_list' WHERE `parent_id` ='category_menu';
UPDATE `sys_module` SET `parent_id` =  'content_list' WHERE `parent_id` ='content_menu';
UPDATE `sys_module` SET `parent_id` =  'content_menu' WHERE `parent_id` ='content_extend';
DELETE FROM `sys_module_lang` WHERE `module_id` = 'content_extend';
DELETE FROM `sys_module` WHERE `id` = 'content_extend';
UPDATE `sys_module` SET `parent_id` =  'content_menu', `sort` = '2' WHERE `id` ='category_list';
UPDATE `sys_module` SET `authorized_url` =  NULL, `url` = NULL WHERE `id` ='content_menu';
INSERT INTO `sys_module` VALUES ('content_list', 'cmsContent/list', 'sysUser/lookup', 'icon-book', 'content_menu', 1, 0);
UPDATE `sys_module` SET  `sort` = '1' where `id` = 'comment_list';
UPDATE `sys_module` SET  `sort` = '3' where `id` = 'tag_list';
UPDATE `sys_module` SET  `sort` = '4' where `id` = 'word_list';
UPDATE `sys_module` SET  `sort` = '5' where `id` = 'content_vote';
UPDATE `sys_module` SET  `sort` = '6' where `id` = 'content_recycle_list';
UPDATE `sys_module` SET  `attached` = NULL where `id` = 'content_add';
UPDATE `sys_module` SET  `menu` = '0' where `parent_id` = 'content_list';
UPDATE `sys_module` SET  `sort` = '1' where `id` = 'category_extend';
INSERT INTO `sys_module_lang` VALUES ('content_list', 'en', 'Content management');
INSERT INTO `sys_module_lang` VALUES ('content_list', 'ja', 'コンテンツ管理');
INSERT INTO `sys_module_lang` VALUES ('content_list', 'zh', '内容管理');
ALTER TABLE  `cms_content` DROP INDEX  `cms_content_quote_content_id`;
UPDATE `sys_module_lang` SET `value` = '私の' where `module_id` = 'myself' and `lang` = 'ja';
UPDATE `sys_module_lang` SET `value` = '我的' where `module_id` = 'myself' and `lang` = 'zh';
-- 2021-01-14 --
ALTER TABLE  `cms_content`
    ADD INDEX `cms_content_quote_content_id`(`site_id`, `quote_content_id`);
CREATE TABLE `log_visit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `session_id` varchar(50) NOT NULL COMMENT '会话',
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
  `item_id` varchar(50) DEFAULT NULL COMMENT '项目',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`),
  KEY `log_visit_visit_date` (`site_id`,`visit_date`,`visit_hour`),
  KEY `log_visit_session_id` (`site_id`,`session_id`,`visit_date`,`create_date`,`ip`)
) COMMENT='访问日志';

CREATE TABLE `log_visit_day` (
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `visit_date` date NOT NULL COMMENT '日期',
  `visit_hour` tinyint(4) NOT NULL COMMENT '小时',
  `pv` bigint(20) NOT NULL COMMENT 'Page Views',
  `uv` bigint(20) DEFAULT NULL COMMENT 'User Views',
  `ipviews` bigint(20) DEFAULT NULL COMMENT 'IP数',
  PRIMARY KEY (`site_id`,`visit_date`,`visit_hour`),
  KEY `log_visit_session_id` (`site_id`,`visit_date`)
)  COMMENT = '访问汇总';

CREATE TABLE `log_visit_session` (
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `session_id` varchar(50) NOT NULL COMMENT '会话',
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
    CHANGE COLUMN `order_id` `payment_id` bigint(20) NOT NULL COMMENT '订单' AFTER `id`;
ALTER TABLE `trade_payment_history` CHANGE COLUMN `order_id` `payment_id` bigint(20) NOT NULL COMMENT '订单' AFTER `site_id`;
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
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `user_id` bigint(20) NOT NULL COMMENT '用户',
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
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `order_id` bigint(20) NOT NULL COMMENT '订单',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `operate` varchar(100) NOT NULL COMMENT '操作',
  `content` text COMMENT '内容',
  PRIMARY KEY (`id`),
  KEY `trade_order_history_site_id` (`site_id`,`order_id`,`operate`),
  KEY `trade_order_history_create_date` (`create_date`)
) COMMENT='订单流水';
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
  KEY `trade_order_product_site_id` (`site_id`,`order_id`)
) COMMENT='产品订单';
ALTER TABLE `cms_content` 
    ADD COLUMN `has_products` tinyint(1) NOT NULL COMMENT '拥有产品列表' AFTER `has_files`,
    DROP INDEX `cms_content_only_url`,
    ADD INDEX `cms_content_only_url`(`only_url`, `has_images`, `has_files`, `has_products`, `user_id`) ;
ALTER TABLE `trade_payment` 
    ADD COLUMN `process_user_id` bigint(20) NULL COMMENT '处理用户' AFTER `processed`;
ALTER TABLE `trade_order` 
    ADD COLUMN `process_user_id` bigint(20) NULL COMMENT '处理用户' AFTER `processed`;
-- 2021-06-28 --
ALTER TABLE `trade_refund` 
    ADD COLUMN `site_id` smallint(0) NOT NULL COMMENT '站点' AFTER `id`,
    ADD COLUMN `user_id` bigint(20) NOT NULL COMMENT '用户' AFTER `payment_id`,
    DROP INDEX `trade_refund_order_id`;
ALTER TABLE `cms_content_related` 
    DROP INDEX `cms_content_related_user_id`,
    ADD INDEX `cms_content_related_content_id`(`content_id`, `sort`);
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
-- 2021-07-09 --
ALTER TABLE `sys_extend_field` 
  ADD INDEX `sys_extend_field_input_type` (`extend_id`, `input_type`,`searchable`);
-- 2021-07-11 --
INSERT INTO `sys_module` VALUES ('report_visit', 'report/visit', NULL, 'icon-bolt', 'user_menu', 1, 6);
INSERT INTO `sys_module_lang` VALUES ('report_visit', 'en', 'Visit report');
INSERT INTO `sys_module_lang` VALUES ('report_visit', 'ja', 'アクセス監視');
INSERT INTO `sys_module_lang` VALUES ('report_visit', 'zh', '网站访问监控');
-- 221-07-20 --
ALTER TABLE `trade_payment_history` COMMENT = '支付订单流水';