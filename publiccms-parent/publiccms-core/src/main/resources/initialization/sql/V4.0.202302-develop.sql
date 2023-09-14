-- 2023-02-27 --
ALTER TABLE `log_operate` MODIFY COLUMN `content` longtext default NULL COMMENT '内容' AFTER `create_date`;
ALTER TABLE `log_upload`
    ADD COLUMN `privatefile` tinyint(1) NOT NULL DEFAULT 0 COMMENT '私有文件' AFTER `original_name`,
    DROP INDEX `log_upload_file_type`,
    ADD INDEX `log_upload_file_type` (`site_id`, `privatefile`, `file_type`, `file_size`);
-- 2023-03-09 --
UPDATE `sys_module` SET `authorized_url`= 'ueditor,tinymce/upload,tinymce/imageList,ckeditor/upload,cmsWebFile/browse,file/doImport' WHERE `id` ='editor';
-- 2023-03-11 --
UPDATE `sys_module` SET `authorized_url`= 'cmsTemplate/doUpload,cmsTemplate/import,cmsTemplate/doImport,cmsTemplate/sitefileList,cmsTemplate/viewSitefile,cmsTemplate/visitSitefileImage' WHERE `id` ='template_import';
-- 2023-03-20 --
UPDATE `sys_module` SET `authorized_url`= 'visit/exportDay' WHERE `id` ='visit_day';
UPDATE `sys_module` SET `authorized_url`= 'visit/view,visit/exportHistory' WHERE `id` ='visit_history';
UPDATE `sys_module` SET `authorized_url`= 'visit/exportItem' WHERE `id` ='visit_item';
UPDATE `sys_module` SET `authorized_url`= 'visit/exportSession' WHERE `id` ='visit_session';
UPDATE `sys_module` SET `authorized_url`= 'visit/exportUrl' WHERE `id` ='visit_url';
ALTER TABLE `visit_session`
    DROP INDEX `visit_session_visit_date`,
    ADD INDEX `visit_session_visit_date` (`site_id`, `visit_date`, `session_id`, `ip`, `last_visit_date`);
ALTER TABLE `visit_url`
    DROP INDEX `visit_url_pv`,
    ADD INDEX `visit_url_pv` (`site_id`, `visit_date`, `pv`);
-- 2023-03-24 --
UPDATE `sys_module` SET `authorized_url`= 'cmsCategory/addMore,cmsCategory/virify,cmsCategory/rebuildChildIds,cmsCategory/batchPublish,cmsCategory/batchCopy,cmsCategory/batchCreate,cmsCategory/batchSave,cmsCategory/seo,cmsCategory/saveSeo,cmsCategory/categoryPath,cmsCategory/contentPath,cmsCategory/save' WHERE `id` ='category_add';
ALTER TABLE `visit_history`
    ADD COLUMN `user_id` bigint(20) DEFAULT NULL COMMENT '用户' AFTER `visit_hour`,
    DROP INDEX `visit_history_create_date`,
    DROP INDEX `visit_history_session_id`,
    ADD INDEX `visit_history_create_date` (`site_id`, `create_date`, `session_id`, `ip`),
    ADD INDEX `visit_history_user_id` (`site_id`, `create_date`, `user_id`),
    ADD INDEX `visit_history_item_type` (`site_id`, `visit_date`, `item_type`);
-- 2023-03-26 --
UPDATE `sys_module` SET `attached` =  'bi bi-code-square' WHERE `id` ='template_list';
UPDATE `sys_module` SET `authorized_url`= 'cmsModel/categoryList,cmsModel/template' WHERE `id` ='model_list';
-- 2023-03-27 --
ALTER TABLE `cms_content` DROP COLUMN `contribute`;
ALTER TABLE `sys_task` ADD COLUMN `multi_node` tinyint(1) NOT NULL COMMENT '多节点执行' AFTER `status`;
-- 2023-03-31 --
UPDATE `sys_module_lang` SET `value` =  'Export' WHERE `lang` ='en' and module_id = 'dictionary_export';
UPDATE `sys_module_lang` SET `value` =  '輸出' WHERE `lang` ='ja' and module_id = 'dictionary_export';
UPDATE `sys_module_lang` SET `value` =  '导出' WHERE `lang` ='zh' and module_id = 'dictionary_export';
DELETE FROM `sys_module_lang` WHERE `module_id` ='refund_view';
INSERT INTO `sys_module_lang` VALUES ('record_view', 'en', 'View');
INSERT INTO `sys_module_lang` VALUES ('record_view', 'ja', '見る');
INSERT INTO `sys_module_lang` VALUES ('record_view', 'zh', '查看');
ALTER TABLE `cms_content_attribute` DROP COLUMN `extends_text`;
-- 2023-04-16 --
ALTER TABLE `cms_survey` ADD COLUMN  `allow_anonymous` tinyint(1) NOT NULL COMMENT '允许匿名' AFTER `end_date`;
ALTER TABLE `cms_vote` ADD COLUMN  `allow_anonymous` tinyint(1) NOT NULL COMMENT '允许匿名' AFTER `description`;
ALTER TABLE `cms_user_survey` ADD COLUMN  `anonymous` tinyint(1) NOT NULL COMMENT '匿名' AFTER `site_id`;
ALTER TABLE `cms_user_vote` ADD COLUMN  `anonymous` tinyint(1) NOT NULL COMMENT '匿名' AFTER `user_id`;
-- 05-09 --
CREATE TABLE `cms_user_collection`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户',
  `content_id` bigint(20) NOT NULL COMMENT '内容',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY (`user_id`, `content_id`),
  KEY `cms_user_collection_user_id`(`user_id`, `create_date`)
) COMMENT = '用户收藏表';
ALTER TABLE `cms_content` ADD COLUMN `collections` int(11) NOT NULL COMMENT '收藏数' AFTER `clicks`;
-- 07-02 --
ALTER TABLE `cms_place`
    ADD COLUMN `max_clicks` int(11) NOT NULL COMMENT '最大点击数' AFTER `clicks`,
    MODIFY COLUMN `status` int(11) NOT NULL COMMENT '状态：0、草稿 1、已发布 2、待审核 3、已下架' after `expiry_date`;
-- 08-04 --
DROP TABLE IF EXISTS `sys_user_attribute`;
CREATE TABLE `sys_user_attribute` (
  `user_id` bigint(20) NOT NULL,
  `search_text` longtext NULL COMMENT '全文索引文本',
  `dictionary_values` text NULL COMMENT '数据字典值',
  `extends_fields` text NULL COMMENT '扩展文本字段',
  `data` longtext COMMENT '数据JSON',
  PRIMARY KEY  (`user_id`)
) COMMENT='用户扩展';
-- 08-07 --
CREATE TABLE `sys_workflow` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `description` varchar(300) DEFAULT NULL COMMENT '描述',
  `start_step_id` bigint(20) DEFAULT NULL COMMENT '开始步骤',
  `disabled` tinyint(1) NOT NULL COMMENT '已禁用',
  PRIMARY KEY (`id`),
  KEY `sys_workflow_disabled` (`site_id`, `disabled`)
) COMMENT='工作流';
CREATE TABLE `sys_workflow_process` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `item_type` varchar(50) NOT NULL COMMENT '项目类型',
  `item_id` varchar(100) NOT NULL COMMENT '项目',
  `step_id` bigint(20) NOT NULL COMMENT '步骤',
  `operate` varchar(20) NOT NULL COMMENT '操作(check:审核,reject:驳回,delete:删除)',
  `reason` varchar(255) DEFAULT NULL COMMENT '理由',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`),
  KEY `sys_workflow_process_content_id` (`site_id`,`item_type`,`operate`,`create_date`) 
) COMMENT='工作流流程';
CREATE TABLE `sys_workflow_step` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `workflow_id` int(11) NOT NULL COMMENT '工作流',
  `role_id` int(11) DEFAULT NULL COMMENT '角色',
  `dept_id` int(11) DEFAULT NULL COMMENT '部门',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户',
  `prev_step_id` bigint(20) DEFAULT NULL COMMENT '上一步',
  `next_step_id` bigint(20) DEFAULT NULL COMMENT '下一步',
  `sort` int(11) NOT NULL COMMENT '排序',
  PRIMARY KEY (`id`),
  KEY `sys_workflow_step_workflow_id` (`workflow_id`,`sort`) 
) COMMENT='工作流步骤';
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
CREATE TABLE `trade_cart` (
  `id` bigint(100) NOT NULL AUTO_INCREMENT,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户',
  `session_id` varchar(50) DEFAULT NULL COMMENT '会话',
  `content_id` bigint(20) NOT NULL COMMENT '内容',
  `product_id` bigint(20) NOT NULL COMMENT '商品',
  `counts` int(11) NOT NULL COMMENT '数量',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`),
  KEY `trade_cart_user_id` (`site_id`,`user_id`,`create_date`),
  KEY `trade_cart_session_id` (`site_id`,`session_id`,`create_date`)
) COMMENT='购物车';
CREATE TABLE `trade_coupon` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `category_id` int(11) DEFAULT NULL COMMENT '分类',
  `content_id` bigint(20) DEFAULT NULL COMMENT '内容',
  `start_date` datetime NOT NULL COMMENT '开始时间',
  `expiry_date` datetime DEFAULT NULL COMMENT '结束时间',
  `starting_amount` decimal(10,2) DEFAULT NULL COMMENT '起始金额',
  `discount` decimal(10,1) DEFAULT NULL COMMENT '折扣优惠',
  `price` decimal(10,2) DEFAULT NULL COMMENT '优惠券价格',
  `type` int(11) NOT NULL COMMENT '类型(1折扣,2免运费,3满减)',
  `redeem_code` varchar(255) DEFAULT NULL COMMENT '兑换码',
  `duration` int(11) NOT NULL COMMENT '有效天数',
  `quantity` int(11) NOT NULL COMMENT '优惠券数量',
  `create_date` varchar(255) DEFAULT NULL COMMENT '开始时间',
  `disabled` tinyint(1) NOT NULL COMMENT '已禁用',
  PRIMARY KEY (`id`),
  KEY `trade_coupon_category_id` (`site_id`,`category_id`,`start_date`,`expiry_date`,`disabled`)
  KEY `trade_coupon_content_id` (`site_id`,`content_id`,`start_date`,`expiry_date`,`disabled`)
) COMMENT='优惠券';
CREATE TABLE `trade_express` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `code` varchar(50) NOT NULL COMMENT '编码',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `sort` int(11) NOT NULL COMMENT '排序',
  PRIMARY KEY (`id`),
  KEY `trade_express_sort` (`site_id`,`sort`)
) COMMENT='物流';
CREATE TABLE `trade_freight` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `country` varchar(40) DEFAULT NULL COMMENT '国家',
  `province` varchar(40) DEFAULT NULL COMMENT '省份',
  `city` varchar(40) DEFAULT NULL COMMENT '所在城市',
  `price` decimal(10,2) DEFAULT NULL COMMENT '运费价格',
  `free_price` decimal(10,2) DEFAULT NULL COMMENT '免邮价格',
  PRIMARY KEY (`id`),
  KEY `trade_freight_site_id` (`site_id`,`country`,`province`,`city`)
) COMMENT='运费';
CREATE TABLE `trade_user_coupon` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) NOT NULL COMMENT '用户',
  `coupon_id` varchar(255) NOT NULL COMMENT '优惠券',
  `status` int(11) NOT NULL COMMENT '状态(1有效,2已使用)',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `start_date` varchar(255) NOT NULL COMMENT '开始时间',
  `expiry_date` varchar(255) DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`),
  KEY `trade_user_coupon_status` (`user_id`, `status`, `start_date`, `expiry_date`, `price`)
) COMMENT='用户优惠券';
-- 08-14 --
ALTER TABLE `cms_category` ADD COLUMN `workflow_id` int(11) default NULL COMMENT '审核流程' AFTER `disabled`;
