-- 2021-07-30 --
UPDATE `sys_module` SET `authorized_url` = 'cmsContent/addMore,file/doUpload,file/doImport,cmsContent/lookup,cmsContent/lookup_list,cmsContent/save,ueditor,ckeditor/upload,kindeditor/upload,file/doBatchUpload' WHERE `id` ='content_add';
UPDATE `sys_module` SET `authorized_url` = 'file/doUpload,file/doImport,cmsContent/lookup,cmsContent/lookup_list,cmsContent/save,ueditor,ckeditor/upload,kindeditor/upload,file/doBatchUpload' WHERE `id` ='myself_content_add';
-- 2021-08-02 --
DROP TABLE IF EXISTS `sys_site_datasource`;
CREATE TABLE `sys_site_datasource` (
  `site_id` smallint(6) NOT NULL COMMENT '站点ID',
  `datasource` varchar(50) NOT NULL COMMENT '数据源名称',
  PRIMARY KEY (`site_id`,`datasource`),
  KEY `sys_site_datasource_datasource` (`datasource`)
) COMMENT='站点数据源';
DROP TABLE IF EXISTS `sys_datasource`;
CREATE TABLE `sys_datasource` (
  `name` varchar(50) NOT NULL COMMENT '名称',
  `config` varchar(1000) NOT NULL COMMENT '配置',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `update_date` datetime DEFAULT NULL COMMENT '更新日期',
  `disabled` tinyint(1) NOT NULL COMMENT '禁用',
  PRIMARY KEY (`name`)
) COMMENT='数据源';
ALTER TABLE `cms_content_file`
	DROP INDEX `cms_content_file_content_id`,
	DROP INDEX `cms_content_file_sort`,
	DROP INDEX `cms_content_file_user_id`,
	ADD INDEX `cms_content_file_content_id`(`content_id`, `sort`);
ALTER TABLE `cms_content`
    MODIFY COLUMN `id` bigint(20) NOT NULL FIRST;
ALTER TABLE `cms_comment`
    MODIFY COLUMN `id` bigint(20) NOT NULL FIRST;
-- 2021-08-06 --
ALTER TABLE `sys_site` 
	ADD COLUMN `directory` varchar(50) NULL COMMENT '目录' AFTER `parent_id`,
	DROP INDEX `sys_site_parent_id`,
	ADD UNIQUE INDEX `sys_site_parent_id`(`parent_id`, `directory`);
ALTER TABLE `sys_domain` 
    ADD COLUMN `multiple` tinyint(1) NOT NULL COMMENT '站点群' AFTER `wild`;

CREATE TABLE `log_visit_item` (
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `visit_date` date NOT NULL COMMENT '日期',
  `item_type` varchar(50) NOT NULL COMMENT '项目类型',
  `item_id` varchar(50) NOT NULL COMMENT '项目',
  `pv` bigint(20) NOT NULL COMMENT 'Page Views',
  `uv` bigint(20) DEFAULT NULL COMMENT 'User Views',
  `ipviews` bigint(20) DEFAULT NULL COMMENT 'IP数',
  PRIMARY KEY (`site_id`,`visit_date`,`item_type`,`item_id`),
  KEY `log_visit_session_id` (`site_id`,`visit_date`,`item_type`,`item_id`, `pv`)
) COMMENT='项目访问汇总';
CREATE TABLE `log_visit_url` (
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `visit_date` date NOT NULL COMMENT '日期',
  `url_md5` varchar(50) NOT NULL COMMENT 'URL MD5',
  `url_sha` varchar(100) NOT NULL COMMENT 'URL SHA',
  `url` varchar(2048) NOT NULL COMMENT 'URL',
  `pv` bigint(20) NOT NULL COMMENT 'Page Views',
  `uv` bigint(20) DEFAULT NULL COMMENT 'User Views',
  `ipviews` bigint(20) DEFAULT NULL COMMENT 'IP数',
  PRIMARY KEY (`site_id`,`visit_date`,`url_md5`,`url_sha`),
  KEY `log_visit_session_id` (`site_id`,`visit_date`,`pv`)
) COMMENT='页面访问汇总';
INSERT INTO `sys_module` VALUES ('log_visit_item', 'log/visitItem', NULL, 'icon-flag-checkered', 'log_menu', 1, 9);
INSERT INTO `sys_module` VALUES ('log_visit_url', 'log/visitUrl', NULL, 'icon-link', 'log_menu', 1, 8);
INSERT INTO `sys_module_lang` VALUES ('log_visit_item', 'en', 'Item visit log');
INSERT INTO `sys_module_lang` VALUES ('log_visit_item', 'ja', 'アイテム訪問ログ');
INSERT INTO `sys_module_lang` VALUES ('log_visit_item', 'zh', '项目访问日志');
INSERT INTO `sys_module_lang` VALUES ('log_visit_url', 'en', 'Page visit log');
INSERT INTO `sys_module_lang` VALUES ('log_visit_url', 'ja', 'ページアクセスログ');
INSERT INTO `sys_module_lang` VALUES ('log_visit_url', 'zh', '页面访问日志');
-- 2021-08-21 --
ALTER TABLE `cms_comment`
  ADD COLUMN `scores` int(11) NOT NULL COMMENT '分数' AFTER `replies`,
  DROP INDEX `cms_comment_update_date`,
  ADD INDEX `cms_comment_update_date` (`update_date`,`create_date`,`replies`,`scores`);
-- 2021-09-03 --
INSERT INTO `sys_module` VALUES ('content_distribute', 'cmsCategory/lookupBySiteId', 'cmsContent/distribute', '', 'content_list', 0, 0);
INSERT INTO `sys_module_lang` VALUES ('content_distribute', 'en', 'Distribute');
INSERT INTO `sys_module_lang` VALUES ('content_distribute', 'ja', '分布');
INSERT INTO `sys_module_lang` VALUES ('content_distribute', 'zh', '分发');
-- 2021-09-20 --
UPDATE `sys_module_lang` SET `value` =  'Product management' WHERE `lang` ='en' and module_id = 'product_list';
UPDATE `sys_module_lang` SET `value` =  '製品管理' WHERE `lang` ='ja' and module_id = 'product_list';
UPDATE `sys_module_lang` SET `value` =  '产品管理' WHERE `lang` ='zh' and module_id = 'product_list';
-- 2021-09-23 --

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
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `disabled` tinyint(1) NOT NULL COMMENT '是否禁用',
  PRIMARY KEY (`id`),
  KEY `cms_survey_site_id` (`site_id`,`survey_type`,`start_date`,`disabled`,`create_date`),
  KEY `cms_survey_user_id` (`user_id`,`votes`)
) COMMENT='问卷调查';

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
  KEY `cms_survey_question_survey_id` (`survey_id`,`sort`),
  KEY `cms_survey_question_question_type` (`survey_id`,`question_type`,`sort`)
) COMMENT='问卷调查问题';

DROP TABLE IF EXISTS `cms_survey_question_item`;
CREATE TABLE `cms_survey_question_item` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `question_id` bigint(20) NOT NULL COMMENT '问题',
  `votes` int(11) NOT NULL COMMENT '投票数',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `sort` int(11) NOT NULL COMMENT '排序',
  PRIMARY KEY (`id`),
  KEY `cms_survey_question_item_question_id` (`question_id`,`sort`),
  KEY `cms_survey_question_item_votes` (`question_id`,`votes`)
) COMMENT='问卷调查选项';

DROP TABLE IF EXISTS `cms_user_survey`;
CREATE TABLE `cms_user_survey` (
  `user_id` bigint(20) NOT NULL COMMENT '用户',
  `survey_id` bigint(20) NOT NULL COMMENT '问卷',
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `score` int(11) DEFAULT NULL COMMENT '分数',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY (`user_id`,`survey_id`),
  KEY `cms_user_survey_site_id` (`site_id`,`score`,`create_date`)
) COMMENT='用户问卷';

DROP TABLE IF EXISTS `cms_user_survey_question`;
CREATE TABLE `cms_user_survey_question` (
  `user_id` bigint(20) NOT NULL COMMENT '用户',
  `question_id` bigint(20) NOT NULL COMMENT '问题',
  `site_id` smallint(6) NOT NULL COMMENT '站点',
  `survey_id` bigint(20) NOT NULL COMMENT '问卷',
  `answer` varchar(255) DEFAULT NULL COMMENT '答案',
  `score` int(11) DEFAULT NULL COMMENT '分数',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY (`user_id`,`question_id`),
  KEY `cms_user_survey_site_id` (`site_id`,`survey_id` ,`score`,`create_date`)
) COMMENT='用户问卷答案';
-- 2021-09-24 --
ALTER TABLE `log_operate` 
    ADD COLUMN `dept_id` int(11) NULL COMMENT '部门' AFTER `user_id`,
    DROP INDEX `log_operate_user_id`,
    ADD INDEX `log_operate_user_id`(`user_id`, `dept_id`);
INSERT INTO `sys_module` VALUES ('survey_add', 'cmsSurvey/add', 'cmsSurvey/save', NULL, 'survey_list', 0, 0);
INSERT INTO `sys_module` VALUES ('survey_delete', NULL, 'cmsSurvey/delete', NULL, 'survey_list', 0, 0);
INSERT INTO `sys_module` VALUES ('survey_list', 'cmsSurvey/list', NULL, 'icon-list-ul', 'content_menu', 1, 6);
INSERT INTO `sys_module` VALUES ('survey_question_list', 'cmsSurveyQuestion/list', 'cmsSurveyQuestion/add,cmsSurveyQuestion/save,cmsSurveyQuestion/delete', NULL, 'survey_list', 0, 0);
INSERT INTO `sys_module` VALUES ('survey_user_list', 'cmsUserSurvey/list', 'cmsUserSurvey/add,cmsUserSurvey/save', NULL, 'survey_list', 0, 0);
INSERT INTO `sys_module` VALUES ('survey_view', 'cmsSurvey/view', NULL, NULL, 'survey_list', 0, 0);
INSERT INTO `sys_module_lang` VALUES ('survey_list', 'en', 'Survey management');
INSERT INTO `sys_module_lang` VALUES ('survey_list', 'ja', 'アンケート管理');
INSERT INTO `sys_module_lang` VALUES ('survey_list', 'zh', '问卷调查管理');
INSERT INTO `sys_module_lang` VALUES ('survey_add', 'zh', '增加/修改');
INSERT INTO `sys_module_lang` VALUES ('survey_add', 'en', 'Add/edit');
INSERT INTO `sys_module_lang` VALUES ('survey_add', 'ja', '追加/変更');
INSERT INTO `sys_module_lang` VALUES ('survey_delete', 'zh', '删除');
INSERT INTO `sys_module_lang` VALUES ('survey_delete', 'en', 'Delete');
INSERT INTO `sys_module_lang` VALUES ('survey_delete', 'ja', '削除');
INSERT INTO `sys_module_lang` VALUES ('survey_question_list', 'zh', '问题管理');
INSERT INTO `sys_module_lang` VALUES ('survey_question_list', 'en', 'Question management');
INSERT INTO `sys_module_lang` VALUES ('survey_question_list', 'ja', '質問管理');
INSERT INTO `sys_module_lang` VALUES ('survey_user_list', 'zh', '答案管理');
INSERT INTO `sys_module_lang` VALUES ('survey_user_list', 'en', 'Answer management');
INSERT INTO `sys_module_lang` VALUES ('survey_user_list', 'ja', '回答管理');
INSERT INTO `sys_module_lang` VALUES ('survey_view', 'zh', '查看');
INSERT INTO `sys_module_lang` VALUES ('survey_view', 'en', 'View');
INSERT INTO `sys_module_lang` VALUES ('survey_view', 'ja', '見る');
UPDATE `sys_module` SET `sort` = '8' WHERE `id` = 'word_list';
UPDATE `sys_module` SET `sort` = '9' WHERE `id` = 'content_recycle_list';
-- 2021-09-25 --
INSERT INTO `sys_module` VALUES ('log_workload', 'log/workload', NULL, 'icon-truck', 'log_menu', 1, 0);
INSERT INTO `sys_module_lang` VALUES ('log_workload', 'en', 'Workload');
INSERT INTO `sys_module_lang` VALUES ('log_workload', 'ja', 'ワークロード');
INSERT INTO `sys_module_lang` VALUES ('log_workload', 'zh', '工作量统计');
-- 2021-11-13 --
ALTER TABLE `sys_user` DROP INDEX `sys_user_nick_name`;
UPDATE `sys_module` SET `authorized_url` = 'tradeAccount/save,sysUser/lookup,sysUser/lookup_list' WHERE `id` ='account_add';
UPDATE `sys_module` SET `authorized_url` = 'sysUser/lookup,sysUser/lookup_list' WHERE `id` ='account_history_list';
UPDATE `sys_module` SET `authorized_url` = 'sysUser/lookup,sysUser/lookup_list' WHERE `id` ='comment_list';
UPDATE `sys_module` SET `authorized_url` = NULL WHERE `id` ='content_list';
UPDATE `sys_module` SET `authorized_url` = 'sysUser/lookup,sysUser/lookup_list' WHERE `id` ='content_recycle_list';
UPDATE `sys_module` SET `authorized_url` = 'sysUser/lookup_list' WHERE `id` ='content_select_user';
UPDATE `sys_module` SET `authorized_url` = 'sysDept/lookup,sysUser/lookup,sysUser/lookup_list,sysDept/save' WHERE `id` ='dept_add';
UPDATE `sys_module` SET `authorized_url` = 'sysDept/lookup,sysUser/lookup,sysUser/lookup_list' WHERE `id` ='dept_list';
UPDATE `sys_module` SET `authorized_url` = 'sysUser/lookup,sysUser/lookup_list' WHERE `id` ='log_login';
UPDATE `sys_module` SET `authorized_url` = 'sysUser/lookup,sysUser/lookup_list' WHERE `id` ='log_operate';
UPDATE `sys_module` SET `authorized_url` = NULL WHERE `id` ='log_task';
UPDATE `sys_module` SET `authorized_url` = 'sysUser/lookup,sysUser/lookup_list' WHERE `id` ='log_upload';
UPDATE `sys_module` SET `authorized_url` = 'sysUser/lookup,sysUser/lookup_list' WHERE `id` ='order_list';
UPDATE `sys_module` SET `authorized_url` = 'cmsPage/metadata,cmsContent/lookup,cmsContent/lookup_list,cmsCategory/lookup' WHERE `id` ='page_list';
UPDATE `sys_module` SET `authorized_url` = 'sysUser/lookup_list' WHERE `id` ='page_select_user';
UPDATE `sys_module` SET `authorized_url` = 'sysUser/lookup,sysUser/lookup_list' WHERE `id` ='payment_list';
UPDATE `sys_module` SET `authorized_url` = 'sysUser/lookup,sysUser/lookup_list' WHERE `id` ='place_list';
UPDATE `sys_module` SET `authorized_url` = 'sysUser/lookup,sysUser/lookup_list' WHERE `id` ='refund_list';
ALTER TABLE `sys_dept` ADD COLUMN `code` varchar(50) NOT NULL COMMENT '编码' AFTER `name`;
UPDATE `sys_dept` SET `code` = id;
ALTER TABLE `sys_dept` ADD UNIQUE INDEX `sys_dept_code`(`site_id`, `code`);
ALTER TABLE `cms_content` ADD COLUMN `dept_id` int(11) default NULL COMMENT '所属部门' AFTER `user_id`;
ALTER TABLE `cms_content_file` MODIFY COLUMN `file_size` bigint(20) NULL COMMENT '文件大小' AFTER `file_type`;
ALTER TABLE `log_upload` MODIFY COLUMN `file_size` bigint(20) NULL COMMENT '文件大小' AFTER `file_type`;
-- 2022-01-01 --
ALTER TABLE `sys_extend_field`
    ADD COLUMN `width` int(11) default NULL COMMENT '高度' AFTER `maxlength`,
    ADD COLUMN `height` int(11) default NULL COMMENT '宽度' AFTER `width`;
-- 2022-0114 --
UPDATE `sys_module` SET `authorized_url` = 'sysDept/lookup,sysUser/lookup,sysUser/lookup_list,sysDept/save,sysDept/virify' WHERE `id` ='dept_add';
