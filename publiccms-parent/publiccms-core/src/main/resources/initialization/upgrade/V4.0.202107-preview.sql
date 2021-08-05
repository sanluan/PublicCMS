UPDATE `sys_module` SET `authorized_url` = 'cmsContent/addMore,file/doUpload,file/doImport,cmsContent/lookup,cmsContent/lookup_list,cmsContent/save,ueditor,ckeditor/upload,kindeditor/upload,file/doBatchUpload' WHERE `id` ='content_add';
UPDATE `sys_module` SET `authorized_url` = 'file/doUpload,file/doImport,cmsContent/lookup,cmsContent/lookup_list,cmsContent/save,ueditor,ckeditor/upload,kindeditor/upload,file/doBatchUpload' WHERE `id` ='myself_content_add';
DROP TABLE IF EXISTS `sys_site_datasource`;
CREATE TABLE `sys_site_datasource` (
  `site_id` smallint(11) NOT NULL COMMENT '站点ID',
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
ALTER TABLE `sys_site` 
	ADD COLUMN `directory` varchar(50) NULL COMMENT '目录' AFTER `parent_id`,
	DROP INDEX `sys_site_parent_id`,
	ADD UNIQUE INDEX `sys_site_parent_id`(`parent_id`, `directory`);
ALTER TABLE `sys_domain` 
    ADD COLUMN `multiple` tinyint(1) NOT NULL COMMENT '站点群' AFTER `wild`;