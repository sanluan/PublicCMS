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
  KEY `log_visit_session_id` (`site_id`,`session_id`,`visit_date`,`create_date`,`ip`) USING BTREE
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
-- 2021-06-08 --
INSERT INTO `sys_module` VALUES ('repo_sync', 'sysRepoSync/sync', NULL, 'icon-refresh', 'file_menu', 1, 5);
INSERT INTO `sys_module_lang` VALUES ('repo_sync', 'en', 'Repo Sync');
INSERT INTO `sys_module_lang` VALUES ('repo_sync', 'ja', '倉庫同期');
INSERT INTO `sys_module_lang` VALUES ('repo_sync', 'zh', '仓库同步');

-- 2021-06-14 --
UPDATE `sys_module` SET `authorized_url` = 'cmsTemplate/save,cmsTemplate/chipLookup,cmsWebFile/lookup,placeTemplate/form,cmsTemplate/contentForm,cmsCategory/contributeForm,cmsTemplate/demo,cmsTemplate/help,cmsTemplate/upload,cmsTemplate/doUpload' WHERE `id` ='template_content';