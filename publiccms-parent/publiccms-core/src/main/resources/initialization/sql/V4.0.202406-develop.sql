-- 2024-09-25 --
UPDATE sys_module SET authorized_url= 'cmsPlace/push,cmsPlace/add,cmsPlace/save,cmsContent/push_content,cmsContent/push_content_list,cmsContent/push_to_content,cmsContent/push_to_relation,cmsContent/related,cmsContent/unrelated,cmsPlace/delete' WHERE id ='content_push';
UPDATE sys_module SET authorized_url= 'cmsTemplate/save,cmsTemplate/saveMetaData,cmsWebFile/lookup,cmsTemplate/help' WHERE id ='template_content';
DROP TABLE IF EXISTS `sys_user_attribute`;
CREATE TABLE `sys_user_attribute` (
  `user_id` bigint(20) NOT NULL,
  `settings` text NULL COMMENT '设置JSON',
  `data` longtext COMMENT '数据JSON',
  PRIMARY KEY  (`user_id`)
) COMMENT='用户扩展';
-- 2024-11-04 --
ALTER TABLE `cms_dictionary_exclude`
    DROP INDEX `cms_dictionary_parent_value`,
    ADD INDEX `cms_dictionary_exclude_dictionary_id` (`dictionary_id`, `site_id`);
ALTER TABLE `cms_dictionary_exclude_value`
    DROP INDEX `cms_dictionary_parent_value`,
    ADD INDEX `cms_dictionary_exclude_value_dictionary_id` (`dictionary_id`, `site_id`);
ALTER TABLE `visit_item`
    DROP INDEX `visit_item_session_id`,
    ADD INDEX `visit_item_visit_date` (`site_id`, `visit_date`, `item_type`, `item_id`, `pv`);
-- 2024-11-07 --
UPDATE sys_module SET parent_id = 'vote_list' WHERE parent_id = 'content_vote';
-- 2024-11-28 --
DELETE FROM sys_module WHERE id IN ('log_login_delete','log_operate_delete');
DELETE FROM sys_module_lang WHERE module_id IN ('log_login_delete','log_operate_delete');
-- 2025-03-03 --
UPDATE sys_module SET parent_id = 'tag_list',sort=0, attached=NULL WHERE id = 'tag_type_list';
UPDATE sys_module SET parent_id = 'content_list',sort=0, attached=NULL WHERE id = 'content_recycle_list';
-- 2025-03-11 --
ALTER TABLE `sys_site` 
    ADD COLUMN `has_child` tinyint(1) NOT NULL COMMENT '拥有子站点' AFTER `dynamic_path`,
    ADD COLUMN `multiple` tinyint(1) NOT NULL COMMENT '站点群' AFTER `has_child`;
UPDATE sys_site s1, sys_site s2 SET s1.has_child = 1 WHERE s1.id = s2.parent_id;
UPDATE sys_site SET multiple = 1 WHERE id in(SELECT site_id FROM sys_domain WHERE multiple = 1);
ALTER TABLE `sys_domain` DROP COLUMN `multiple`;
ALTER TABLE `sys_dept` 
    ADD COLUMN `has_child` tinyint(1) NOT NULL COMMENT '拥有子部门' AFTER `user_id`;
UPDATE sys_dept d1,sys_dept d2 SET d1.has_child = 1 WHERE d1.id = d2.parent_id;
ALTER TABLE `sys_module` 
    ADD COLUMN `has_child` tinyint(1) NOT NULL COMMENT '拥有子模块' AFTER `menu`;
UPDATE sys_module m1,sys_module m2 SET m1.has_child = 1 WHERE m1.id = m2.parent_id;
-- 2025-03-12 --
UPDATE sys_module SET parent_id = 'myself_profile',sort=0, attached=NULL WHERE id = 'myself_password';
INSERT INTO sys_module VALUES ('category_add_more', 'cmsCategory/addMore', NULL, NULL, 'category_list', 0, 0, 0);
UPDATE sys_module SET parent_id = 'order_list', sort=0, authorized_url='sysUser/lookup', attached=NULL WHERE id = 'order_history_list';
UPDATE sys_module SET parent_id = 'account_list', sort=0, attached=NULL WHERE id = 'account_history_list';
UPDATE sys_module SET parent_id = 'trade_payment', sort=0, attached=NULL WHERE id = 'payment_history_list';
INSERT INTO sys_module VALUES ('task_template_list_export', NULL, 'taskTemplate/export', NULL, 'task_template_list', 0, 0, 0);
UPDATE sys_module SET authorized_url = 'taskTemplate/save,taskTemplate/upload,taskTemplate/doUpload,taskTemplate/chipLookup,cmsTemplate/help' WHERE id = 'task_template_content';
UPDATE sys_module SET authorized_url = 'sysUser/lookup' WHERE id = 'payment_list';
UPDATE sys_module SET authorized_url = 'cmsTemplate/saveMetadata,cmsTemplate/createDirectory' WHERE id = 'template_metadata';
UPDATE sys_module SET authorized_url = 'cmsTemplate/help,cmsTemplate/savePlace,cmsWebFile/lookup' WHERE id = 'place_template_content';
UPDATE sys_module SET url = NULL WHERE id = 'template_export';
INSERT INTO sys_module_lang VALUES ('category_add_more', 'en', 'Add/edit');
INSERT INTO sys_module_lang VALUES ('category_add_more', 'ja', '追加/変更');
INSERT INTO sys_module_lang VALUES ('category_add_more', 'zh', '增加/修改');
INSERT INTO sys_module_lang VALUES ('task_template_list_export', 'en', 'Export');
INSERT INTO sys_module_lang VALUES ('task_template_list_export', 'ja', '輸出');
INSERT INTO sys_module_lang VALUES ('task_template_list_export', 'zh', '导出');
INSERT INTO sys_module VALUES ('select_role', 'sysRole/lookup', NULL, NULL, 'common', 1, 0, 0);
INSERT INTO sys_module_lang VALUES ('select_role', 'en', 'Select role');
INSERT INTO sys_module_lang VALUES ('select_role', 'ja', '役割を選択');
INSERT INTO sys_module_lang VALUES ('select_role', 'zh', '选择角色');
INSERT INTO sys_module VALUES ('select_workflow', 'sysWorkflow/lookup', NULL, NULL, 'common', 1, 0, 0);
INSERT INTO sys_module_lang VALUES ('select_workflow', 'en', 'Select workflow');
INSERT INTO sys_module_lang VALUES ('select_workflow', 'ja', 'ワークフローを選択');
INSERT INTO sys_module_lang VALUES ('select_workflow', 'zh', '选择流程');
INSERT INTO sys_module VALUES ('system_workflow', 'sysWorkflow/list', NULL, 'bi bi-diagram-3', 'system', 1, 1, 0);
INSERT INTO sys_module VALUES ('system_workflow_add', 'sysWorkflow/add', 'sysWorkflow/save', NULL, 'system_workflow', 0, 0, 0);
INSERT INTO sys_module VALUES ('system_workflow_delete', NULL, 'sysWorkflow/delete', NULL, 'system_workflow', 1, 0, 0);
INSERT INTO sys_module_lang VALUES ('system_workflow', 'en', 'Workflow');
INSERT INTO sys_module_lang VALUES ('system_workflow', 'ja', 'ワークフロー');
INSERT INTO sys_module_lang VALUES ('system_workflow', 'zh', '工作流程');
INSERT INTO sys_module_lang VALUES ('system_workflow_add', 'en', 'Add');
INSERT INTO sys_module_lang VALUES ('system_workflow_add', 'ja', '追加');
INSERT INTO sys_module_lang VALUES ('system_workflow_add', 'zh', '增加');
INSERT INTO sys_module_lang VALUES ('system_workflow_delete', 'en', 'Delete');
INSERT INTO sys_module_lang VALUES ('system_workflow_delete', 'ja', '削除');
INSERT INTO sys_module_lang VALUES ('system_workflow_delete', 'zh', '删除');
UPDATE sys_module SET sort=8 WHERE id ='file';
UPDATE sys_module SET parent_id = 'report_visit', attached=NULL WHERE id in ('visit_day','visit_history','visit_item','visit_session','visit_url');
UPDATE sys_module SET parent_id = 'operation', sort=0 WHERE id = 'report_user';
-- 2025-03-13 --
ALTER TABLE `sys_dept`
    DROP INDEX `sys_dept_site_id`,
    ADD INDEX `sys_dept_site_id` (`site_id`,`parent_id`),
    ADD INDEX `sys_dept_user_id` (`site_id`,`user_id`);

DROP TABLE IF EXISTS `sys_workflow`;
CREATE TABLE `sys_workflow` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `description` varchar(300) DEFAULT NULL COMMENT '描述',
  `start_step_id` bigint(20) DEFAULT NULL COMMENT '开始步骤',
  `disabled` tinyint(1) NOT NULL COMMENT '已禁用',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`),
  KEY `sys_workflow_disabled` (`site_id`,`disabled`)
) COMMENT='工作流';

DROP TABLE IF EXISTS `sys_workflow_process`;
CREATE TABLE `sys_workflow_process` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `workflow_id` int(11) NOT NULL COMMENT '工作流',
  `title` varchar(255) NOT NULL COMMENT '标题',
  `item_type` varchar(50) NOT NULL COMMENT '项目类型',
  `item_id` varchar(100) NOT NULL COMMENT '项目',
  `step_id` int(11) NOT NULL COMMENT '当前步骤',
  `role_id` int(11) DEFAULT NULL COMMENT '角色',
  `dept_id` int(11) DEFAULT NULL COMMENT '部门',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户',
  `closed` tinyint(1) NOT NULL COMMENT '已关闭',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `update_date` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`),
  KEY `sys_workflow_process_item_id` (`site_id`,`item_type`,`item_id`,`create_date`),
  KEY `sys_workflow_process_user_id` (`site_id`,`role_id`,`dept_id`,`user_id` , `closed`)
) COMMENT='工作流流程';

DROP TABLE IF EXISTS `sys_workflow_process_history`;
CREATE TABLE `sys_workflow_process_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `process_id` bigint(20) NOT NULL COMMENT '流程',
  `step_id` bigint(20) NOT NULL COMMENT '步骤',
  `user_id` bigint(20) NOT NULL COMMENT '用户',
  `operate` varchar(20) NOT NULL COMMENT '操作(check:审核,reject:驳回)',
  `reason` varchar(255) DEFAULT NULL COMMENT '理由',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`),
  KEY `sys_workflow_process_content_id` (`process_id`),
  KEY `sys_workflow_process_user_id` (`user_id`)
) COMMENT='工作流流程步骤';

DROP TABLE IF EXISTS `sys_workflow_process_item`;
CREATE TABLE `sys_workflow_process_item` (
  `item_type` varchar(50) NOT NULL COMMENT '项目类型',
  `item_id` varchar(100) NOT NULL COMMENT '项目',
  `process_id` bigint(20) NOT NULL COMMENT '流程',
  PRIMARY KEY (`item_type`,`item_id`)
) COMMENT='工作流流程项目';

DROP TABLE IF EXISTS `sys_workflow_step`;
CREATE TABLE `sys_workflow_step` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `workflow_id` int(11) NOT NULL COMMENT '工作流',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `role_id` int(11) DEFAULT NULL COMMENT '角色',
  `dept_id` int(11) DEFAULT NULL COMMENT '部门',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户',
  `next_step_id` bigint(20) DEFAULT NULL COMMENT '下一步',
  `sort` int(11) NOT NULL COMMENT '排序',
  PRIMARY KEY (`id`),
  KEY `sys_workflow_step_workflow_id` (`workflow_id`,`sort`)
) COMMENT='工作流步骤';

DROP TABLE IF EXISTS `trade_cart`;
CREATE TABLE `trade_cart` (
  `id` bigint(100) NOT NULL AUTO_INCREMENT,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户',
  `session_id` varchar(50) DEFAULT NULL COMMENT '会话',
  `content_id` bigint(20) NOT NULL COMMENT '内容',
  `product_id` bigint(20) NOT NULL COMMENT '商品',
  `price` decimal(10,2) NOT NULL COMMENT '价格',
  `counts` int(11) NOT NULL COMMENT '数量',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`),
  KEY `trade_cart_user_id` (`site_id`,`user_id`,`create_date`),
  KEY `trade_cart_session_id` (`site_id`,`session_id`,`create_date`)
) COMMENT='购物车';

DROP TABLE IF EXISTS `trade_coupon`;
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
  KEY `trade_coupon_category_id` (`site_id`,`category_id`,`start_date`,`expiry_date`,`disabled`),
  KEY `trade_coupon_content_id` (`site_id`,`content_id`,`start_date`,`expiry_date`,`disabled`)
) COMMENT='优惠券';

DROP TABLE IF EXISTS `trade_express`;
CREATE TABLE `trade_express` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `code` varchar(50) NOT NULL COMMENT '编码',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `sort` int(11) NOT NULL COMMENT '排序',
  PRIMARY KEY (`id`),
  KEY `trade_express_sort` (`site_id`,`sort`)
) COMMENT='物流';

DROP TABLE IF EXISTS `trade_freight`;
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

INSERT INTO sys_module VALUES ('user_list_view', 'sysUser/user_list', NULL, NULL, 'user_list', 0, 0, 0);
INSERT INTO sys_module VALUES ('content_list_view', 'cmsContent/content_list', NULL, NULL, 'content_list', 0, 0, 0);
INSERT INTO sys_module VALUES ('category_list_view', 'cmsCategory/category_list', NULL, NULL, 'category_list', 0, 0, 0);
INSERT INTO sys_module VALUES ('myself_dept', 'myself/myDept', NULL, 'icon-group', 'myself', 1, 1, 0);
INSERT INTO sys_module VALUES ('myself_dept_user_list', 'myself/dept/userList', 'sysDept/enableUser,sysDept/disableUser', NULL, 'myself_dept', 0, 0, 0);
INSERT INTO sys_module VALUES ('myself_dept_user_add', 'myself/dept/addUser', 'sysDept/saveUser', NULL, 'myself_dept', 0, 0, 0);
DELETE FROM sys_module WHERE id = 'dept_user_list';
DELETE FROM sys_module_lang WHERE module_id = 'dept_user_list';
INSERT INTO sys_module_lang VALUES ('myself_dept', 'en', 'My department');
INSERT INTO sys_module_lang VALUES ('myself_dept', 'ja', '私の部署');
INSERT INTO sys_module_lang VALUES ('myself_dept', 'zh', '我的部门');
INSERT INTO sys_module_lang VALUES ('myself_dept_user_list', 'en', 'Department user management');
INSERT INTO sys_module_lang VALUES ('myself_dept_user_list', 'ja', '人事管理');
INSERT INTO sys_module_lang VALUES ('myself_dept_user_list', 'zh', '人员管理');
INSERT INTO sys_module_lang VALUES ('myself_dept_user_add', 'en', 'Department user add');
INSERT INTO sys_module_lang VALUES ('myself_dept_user_add', 'ja', '人事追加');
INSERT INTO sys_module_lang VALUES ('myself_dept_user_add', 'zh', '人员增加');
INSERT INTO sys_module_lang VALUES ('user_list_view', 'en', 'List view');
INSERT INTO sys_module_lang VALUES ('user_list_view', 'ja', 'リスト表示');
INSERT INTO sys_module_lang VALUES ('user_list_view', 'zh', '列表查看');
INSERT INTO sys_module_lang VALUES ('content_list_view', 'en', 'List view');
INSERT INTO sys_module_lang VALUES ('content_list_view', 'ja', 'リスト表示');
INSERT INTO sys_module_lang VALUES ('content_list_view', 'zh', '列表查看');
INSERT INTO sys_module_lang VALUES ('category_list_view', 'en', 'List view');
INSERT INTO sys_module_lang VALUES ('category_list_view', 'ja', 'リスト表示');
INSERT INTO sys_module_lang VALUES ('category_list_view', 'zh', '列表查看');
INSERT INTO sys_module VALUES ('visit_overview', 'visit/overview', NULL, NULL, 'report_visit', 1, 0, 0);
INSERT INTO sys_module_lang VALUES ('visit_overview', 'en', 'Visit report');
INSERT INTO sys_module_lang VALUES ('visit_overview', 'ja', 'アクセス監視');
INSERT INTO sys_module_lang VALUES ('visit_overview', 'zh', '用户访问监控');
-- 2025-03-17 --
DROP TABLE IF EXISTS `sys_user_setting`;
CREATE TABLE `sys_user_setting` (
  `user_id` bigint(20) NOT NULL COMMENT '用户',
  `code` varchar(50) NOT NULL COMMENT '编码',
  `data` longblob NOT NULL COMMENT '值',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `update_date` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`user_id`,`code`)
);
ALTER TABLE `sys_user_attribute`
    DROP COLUMN `settings`,
    ADD COLUMN `create_date` datetime DEFAULT NULL COMMENT '创建日期' AFTER `data`;
    ADD COLUMN `update_date` datetime DEFAULT NULL COMMENT '更新日期' AFTER `create_date`;
ALTER TABLE `sys_config_data` 
    ADD COLUMN `create_date` datetime DEFAULT NULL COMMENT '创建日期' AFTER `data`;
    ADD COLUMN `update_date` datetime DEFAULT NULL COMMENT '更新日期' AFTER `create_date`;
ALTER TABLE `cms_place` 
    ADD COLUMN `update_date` datetime DEFAULT NULL COMMENT '更新日期' AFTER `max_clicks`;
ALTER TABLE `sys_lock` 
    ADD COLUMN `update_date` datetime DEFAULT NULL COMMENT '更新日期' AFTER `create_date`;
ALTER TABLE `sys_user` 
    ADD COLUMN `update_date` datetime DEFAULT NULL COMMENT '更新日期' AFTER `registered_date`;
-- 2025-03-18 --
UPDATE sys_module SET sort=sort+1 WHERE id in ('content_search','comment_list','category_list','tag_list');
INSERT INTO sys_module VALUES ('myself_content_view', 'cmsContent/view', NULL, NULL, 'myself_content', 1, 0, 0);
INSERT INTO sys_module VALUES ('myself_process_view', 'sysWorkflowProcess/view', NULL, NULL, 'myself_content', 1, 0, 0);
INSERT INTO sys_module VALUES ('process_handle', 'sysWorkflowProcess/processParameters', 'sysWorkflowProcess/handle', NULL, 'process_list', 1, 0, 0);
INSERT INTO sys_module VALUES ('process_list', 'sysWorkflowProcess/list', NULL, 'bi bi-ui-checks', 'content', 1, 1, 2);
INSERT INTO sys_module VALUES ('process_view', 'sysWorkflowProcess/view', NULL, NULL, 'process_list', 1, 0, 0);
INSERT INTO `sys_module_lang` VALUES ('myself_content_view', 'en', 'View');
INSERT INTO `sys_module_lang` VALUES ('myself_content_view', 'ja', '見る');
INSERT INTO `sys_module_lang` VALUES ('myself_content_view', 'zh', '查看');
INSERT INTO `sys_module_lang` VALUES ('myself_process_view', 'en', 'View');
INSERT INTO `sys_module_lang` VALUES ('myself_process_view', 'ja', '見る');
INSERT INTO `sys_module_lang` VALUES ('myself_process_view', 'zh', '查看');
INSERT INTO `sys_module_lang` VALUES ('process_handle', 'en', 'Handle');
INSERT INTO `sys_module_lang` VALUES ('process_handle', 'ja', 'ハンドル');
INSERT INTO `sys_module_lang` VALUES ('process_handle', 'zh', '处理');
INSERT INTO `sys_module_lang` VALUES ('process_list', 'en', 'Review Process');
INSERT INTO `sys_module_lang` VALUES ('process_list', 'ja', 'レビュープロセス');
INSERT INTO `sys_module_lang` VALUES ('process_list', 'zh', '审核流程');
INSERT INTO `sys_module_lang` VALUES ('process_view', 'en', 'View');
INSERT INTO `sys_module_lang` VALUES ('process_view', 'ja', '見る');
INSERT INTO `sys_module_lang` VALUES ('process_view', 'zh', '查看');