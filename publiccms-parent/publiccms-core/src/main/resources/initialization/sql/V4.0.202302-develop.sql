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
-- 08-07 --
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
-- 08-14 --
ALTER TABLE `cms_category` ADD COLUMN `workflow_id` int(11) default NULL COMMENT '审核流程' AFTER `disabled`;
-- 09-29 --
ALTER TABLE `cms_editor_history` MODIFY COLUMN `item_id` varchar(100) NOT NULL COMMENT '数据id' AFTER `item_type`;
-- 12-15 --
DROP TABLE IF EXISTS `sys_user_attribute`;
-- 2024-01-02 --
ALTER TABLE `cms_content` 
  MODIFY COLUMN `copied` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否转载' AFTER `quote_content_id`,
  MODIFY COLUMN `only_url` tinyint(1) NOT NULL DEFAULT 0 COMMENT '外链' AFTER `editor`,
  MODIFY COLUMN `has_images` tinyint(1) NOT NULL DEFAULT 0 COMMENT '拥有图片列表' AFTER `only_url`,
  MODIFY COLUMN `has_files` tinyint(1) NOT NULL DEFAULT 0 COMMENT '拥有附件列表' AFTER `has_images`,
  MODIFY COLUMN `has_products` tinyint(1) NOT NULL DEFAULT 0 COMMENT '拥有产品列表' AFTER `has_files`,
  MODIFY COLUMN `has_static` tinyint(1) NOT NULL DEFAULT 0 COMMENT '已经静态化' AFTER `has_products`,
  MODIFY COLUMN `childs` int(11) NOT NULL DEFAULT 0 COMMENT '子内容数' AFTER `cover`,
  MODIFY COLUMN `scores` int(11) NOT NULL DEFAULT 0 COMMENT '总分数' AFTER `childs`,
  MODIFY COLUMN `score_users` int(11) NOT NULL DEFAULT 0 COMMENT '评分人数' AFTER `scores`,
  MODIFY COLUMN `score` decimal(10, 2) NOT NULL DEFAULT 0 COMMENT '分数' AFTER `score_users`,
  MODIFY COLUMN `comments` int(11) NOT NULL DEFAULT 0 COMMENT '评论数' AFTER `score`,
  MODIFY COLUMN `clicks` int(11) NOT NULL DEFAULT 0 COMMENT '点击数' AFTER `comments`,
  MODIFY COLUMN `collections` int(11) NOT NULL default 0 COMMENT '收藏数' AFTER `clicks`,
  MODIFY COLUMN `status` int(11) NOT NULL DEFAULT 1 COMMENT '状态：0、草稿 1、已发布 2、待审核' AFTER `sort`,
  MODIFY COLUMN `disabled` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除' AFTER `status`;
ALTER TABLE `cms_category`
  MODIFY COLUMN `only_url` tinyint(1) NOT NULL DEFAULT 0 COMMENT '外链' AFTER `path`,
  MODIFY COLUMN `has_static` tinyint(1) NOT NULL DEFAULT 0 COMMENT '已经静态化' AFTER `only_url`,
  MODIFY COLUMN `page_size` int(11) default NULL DEFAULT 20 COMMENT '每页数据条数' AFTER `contain_child`,
  MODIFY COLUMN `allow_contribute` tinyint(1) NOT NULL DEFAULT 0 COMMENT '允许投稿' AFTER `page_size`,
  MODIFY COLUMN `hidden` tinyint(1) NOT NULL DEFAULT 0 COMMENT '隐藏' AFTER `sort`,
  MODIFY COLUMN `disabled` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除' AFTER `hidden`;
ALTER TABLE `cms_comment`
  MODIFY COLUMN `scores` int(11) NOT NULL default 0 COMMENT '分数' AFTER `replies`,
  MODIFY COLUMN `status` int(11) NOT NULL default 1 COMMENT '状态：1、已发布 2、待审核' AFTER `create_date`,
  MODIFY COLUMN `disabled` tinyint(1) NOT NULL default 0 COMMENT '已禁用' AFTER `status`;
-- 2024-01-30 --
UPDATE `sys_module` SET `authorized_url`= 'sysConfigData/export',url = NULL WHERE `id` ='config_data_export';
-- 2024-03-15 --
ALTER TABLE `cms_content_attribute` DROP COLUMN `extends_fields`;
DELETE FROM sys_module WHERE id IN ('category_extend','content_menu','config_menu','file_menu','log_menu','system_menu','user_menu','myself_menu','page_menu','visit_menu');
DELETE FROM sys_module_lang WHERE module_id IN ('category_extend','content_menu','config_menu','file_menu','log_menu','system_menu','user_menu','myself_menu','page_menu','visit_menu');
UPDATE sys_module SET parent_id='content' WHERE parent_id IN ('content_menu','category_extend');
UPDATE sys_module SET parent_id='myself' WHERE parent_id = 'myself_menu';
UPDATE sys_module SET sort=sort+10  WHERE parent_id = 'config_menu';
UPDATE sys_module SET parent_id='develop' WHERE parent_id IN ('config_menu','file_menu');
UPDATE sys_module SET sort=sort+10  WHERE parent_id = 'system_menu';
UPDATE sys_module SET parent_id='system' WHERE parent_id IN ('system_menu','user_menu');
UPDATE sys_module SET parent_id='page' WHERE parent_id = 'page_menu';
INSERT INTO `sys_module` VALUES ('operation', NULL, NULL, 'bi bi-binoculars-fill', NULL, 1, 6);
INSERT INTO `sys_module_lang` VALUES ('operation', 'en', 'Operation');
INSERT INTO `sys_module_lang` VALUES ('operation', 'ja', '操作');
INSERT INTO `sys_module_lang` VALUES ('operation', 'zh', '运营');
UPDATE sys_module SET parent_id='operation' where parent_id in ('visit_menu','log_menu');
UPDATE sys_module SET parent_id=NULL,id='trade',sort=4,attached='bi bi-cart4' where id = 'trade_menu';
UPDATE sys_module_lang SET module_id='trade' where module_id = 'trade_menu';
UPDATE sys_module SET parent_id='trade' where parent_id = 'trade_menu';
UPDATE sys_module SET parent_id='trade',sort=1 where id = 'product_list';
UPDATE sys_module_lang SET value='Trade' where module_id ='trade' and lang= 'en';  
UPDATE sys_module_lang SET value='ビジネス' where module_id ='trade' and lang= 'ja';
UPDATE sys_module_lang SET value='商务' where module_id ='trade' and lang= 'zh';
UPDATE sys_module SET sort=5,id ='system' where id = 'maintenance';
UPDATE sys_module_lang SET value='System',module_id ='system' where module_id ='maintenance' and lang= 'en';  
UPDATE sys_module_lang SET value='システム',module_id ='system' where module_id ='maintenance' and lang= 'ja';
UPDATE sys_module_lang SET value='系统',module_id ='system' where module_id ='maintenance' and lang= 'zh';
UPDATE sys_module SET sort=8 WHERE id = 'myself';
UPDATE sys_module SET attached='bi bi-pie-chart' WHERE id = 'report_visit';
UPDATE sys_module SET sort=0,menu=1,id ='page_preview',parent_id='page',attached='bi bi-palette2' where id = 'page_diy_preview';
UPDATE sys_module_lang SET module_id='page_preview' where module_id = 'page_diy_preview';