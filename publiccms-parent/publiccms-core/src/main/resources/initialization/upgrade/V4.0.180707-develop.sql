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
update `sys_user` set `owns_all_content` = '0' where `superuser_access` = '0';