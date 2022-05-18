-- 2022-04-25 --
UPDATE `sys_module` SET `url` = 'cmsPlace/metadata' WHERE `id` ='place_publish';
-- 2022-05-02 --
UPDATE `sys_module` SET `sort` = 4 WHERE `id` ='task_template_list';
UPDATE `sys_module` SET `sort` = 5 WHERE `id` ='webfile_list';
UPDATE `sys_module` SET `sort` = 2 WHERE `id` ='place_list';
INSERT INTO `sys_module` VALUES ('diy_list', 'cmsDiy/list', 'cmsDiy/region,cmsDiy/layout,cmsDiy/module,placeTemplate/lookupPlace,cmsCategoryType/lookup', 'icon-dashboard', 'file_menu', 1, 5);
INSERT INTO `sys_module_lang` VALUES ('diy_list', 'en', 'Page visualization management');
INSERT INTO `sys_module_lang` VALUES ('diy_list', 'ja', 'ページ視覚化管理');
INSERT INTO `sys_module_lang` VALUES ('diy_list', 'zh', '页面可视化管理');
ALTER TABLE `sys_user`
    CHANGE COLUMN `nick_name` `nickname` varchar(45) NOT NULL COMMENT '昵称' AFTER `weak_password`;
-- 2022-05-05 --
INSERT INTO `sys_module` VALUES ('page_diy', 'cmsPage/diy', 'cmsPage/region,cmsPage/style,cmsDiy/save', 'bi bi-palette', 'page_menu', 1, 3);
INSERT INTO `sys_module_lang` VALUES ('page_diy', 'en', 'Visualized page');
INSERT INTO `sys_module_lang` VALUES ('page_diy', 'ja', '視覚化されたページ');
INSERT INTO `sys_module_lang` VALUES ('page_diy', 'zh', '页面可视化');
-- 2022-05-10 --
CREATE TABLE `cms_content_text_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content_id` bigint(20) NOT NULL COMMENT '内容',
  `field_name` varchar(100) NOT NULL COMMENT '字段名',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `user_id` bigint(20) NOT NULL COMMENT '修改用户',
  `text` longtext NOT NULL COMMENT '文本',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `cms_content_history_content_id` (`content_id`,`field_name`,`create_date`,`user_id`)
) COMMENT='内容扩展';
INSERT INTO `sys_module` VALUES ('content_text_history', 'cmsContentTextHistory/lookup', 'cmsContentTextHistory/use', NULL, 'content_add', 0, 0);
INSERT INTO `sys_module_lang` VALUES ('content_text_history', 'en', 'Modify records');
INSERT INTO `sys_module_lang` VALUES ('content_text_history', 'ja', 'レコードを変更する');
INSERT INTO `sys_module_lang` VALUES ('content_text_history', 'zh', '修改记录');
-- 2022-05-18 --
ALTER TABLE `visit_history`
    DROP INDEX `visit_visit_date`,
    DROP INDEX `visit_session_id`,
    ADD INDEX `visit_history_visit_date` (`site_id`,`visit_date`,`visit_hour`),
    ADD INDEX `visit_history_session_id` (`site_id`,`session_id`,`visit_date`,`create_date`);
ALTER TABLE `visit_item`
    DROP INDEX `visit_session_id`,
    ADD INDEX `visit_item_session_id` (`site_id`,`visit_date`,`item_type`, `item_id`, `pv`);
ALTER TABLE `visit_session`
    DROP INDEX `visit_visit_date`,
    ADD INDEX `visit_session_visit_date` (`site_id`,`visit_date`,`session_id`,`last_visit_date`);
ALTER TABLE `visit_url`
    DROP INDEX `visit_session_id`,
    ADD INDEX `visit_url_pv`(`site_id`, `visit_date`, `pv`);