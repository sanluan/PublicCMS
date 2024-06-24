-- 20180711 --
ALTER TABLE `sys_site`
    ADD COLUMN `parent_id` smallint(6) DEFAULT NULL COMMENT '父站点ID' AFTER `id`,
    MODIFY COLUMN `name` varchar(50) NOT NULL COMMENT '站点名' AFTER `parent_id`;
-- 20180714 --
UPDATE `sys_module` SET `authorized_url` =  'cmsContent/addMore,file/doUpload,cmsContent/lookup,cmsContent/lookup_list,cmsContent/save,ueditor,ckeditor/upload,kindeditor/upload' WHERE `id` ='content_add';
UPDATE `sys_module` SET `authorized_url` =  'cmsContent/addMore,file/doUpload,cmsContent/lookup,cmsContent/lookup_list,cmsContent/save,ueditor,ckeditor/upload,kindeditor/upload' WHERE `id` ='myself_content_add';
UPDATE `sys_module` SET `authorized_url` =  'sysConfigData/save,sysConfigData/edit',url = NULL WHERE `id` ='config_data_edit';
-- 20180811 --
ALTER TABLE `sys_dept` ADD COLUMN `owns_all_config` tinyint(1) NOT NULL DEFAULT 1 COMMENT '拥有全部配置权限' AFTER `owns_all_page`;
CREATE TABLE `sys_dept_config` (
  `dept_id` int(11) NOT NULL COMMENT '部门ID',
  `config` varchar(100) NOT NULL COMMENT '配置',
  PRIMARY KEY (`dept_id`,`config`) 
) COMMENT='部门配置';
ALTER TABLE `sys_user` ADD COLUMN `owns_all_content` tinyint(1) NOT NULL DEFAULT 1 COMMENT '拥有所有内容权限' AFTER `dept_id`;
UPDATE `sys_user` SET `owns_all_content` = '0' WHERE `superuser_access` = '0';
-- 20180813 --
UPDATE `sys_module` SET `authorized_url` =  'cmsWebFile/unzip',url = 'cmsWebFile/unzipParameters' WHERE `id` ='webfile_unzip';
-- 20180820 --
UPDATE `sys_module` SET `authorized_url` =  'cmsPlace/check,cmsPlace/uncheck' WHERE `id` ='place_check';
-- 20180821 --
UPDATE `sys_module` SET `authorized_url` =  'cmsTemplate/help,cmsTemplate/savePlace,cmsTemplate/chipLookup,cmsWebFile/lookup,cmsWebFile/contentForm,placeTemplate/form' WHERE `id` ='place_template_content';
DELETE FROM `sys_email_token`;
ALTER TABLE `sys_email_token` ADD COLUMN `expiry_date` datetime(0) NOT NULL COMMENT '过期日期' AFTER `create_date`;
ALTER TABLE `sys_user_token` ADD COLUMN `expiry_date` datetime(0) DEFAULT NULL COMMENT '过期日期' AFTER `create_date`;
ALTER TABLE `sys_app_token` ADD COLUMN `expiry_date` datetime(0) DEFAULT NULL COMMENT '过期日期' AFTER `create_date`;
ALTER TABLE `sys_app` ADD COLUMN `expiry_minutes` int(0) NULL COMMENT '过期时间' AFTER `authorized_apis`;
UPDATE `sys_user_token` SET `expiry_date` = date_add(`create_date`, interval 30 day);
UPDATE `sys_app_token` SET `expiry_date` = date_add(`create_date`, interval 30 minute);
UPDATE `sys_app` SET `expiry_minutes` = '30';
INSERT INTO `sys_module` VALUES ('app_issue', 'sysApp/issueParameters', 'sysAppToken/issue', NULL, 'app_list', 0, 0);
INSERT INTO `sys_module_lang` VALUES ('app_issue', '', '颁发授权');
INSERT INTO `sys_module_lang` VALUES ('app_issue', 'en', 'Issue authorization');
UPDATE `sys_module` SET `attached` =  '<i class=\"icon-key icon-large\"></i>' WHERE `id` ='myself';
UPDATE `sys_module` SET `attached` =  '<i class=\"icon-file-text-alt icon-large\"></i>' WHERE `id` ='content';
UPDATE `sys_module` SET `attached` =  '<i class=\"icon-folder-open-alt icon-large\"></i>' WHERE `id` ='category';
UPDATE `sys_module` SET `attached` =  '<i class=\"icon-puzzle-piece icon-large\"></i>' WHERE `id` ='develop';
UPDATE `sys_module` SET `attached` =  '<i class=\"icon-tablet icon-large\"></i>' WHERE `id` ='page';