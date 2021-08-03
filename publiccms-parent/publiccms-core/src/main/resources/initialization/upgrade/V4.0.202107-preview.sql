UPDATE `sys_module` SET `authorized_url` = 'cmsContent/addMore,file/doUpload,file/doImport,cmsContent/lookup,cmsContent/lookup_list,cmsContent/save,ueditor,ckeditor/upload,kindeditor/upload,file/doBatchUpload' WHERE `id` ='content_add';
UPDATE `sys_module` SET `authorized_url` = 'file/doUpload,file/doImport,cmsContent/lookup,cmsContent/lookup_list,cmsContent/save,ueditor,ckeditor/upload,kindeditor/upload,file/doBatchUpload' WHERE `id` ='myself_content_add';
DROP TABLE IF EXISTS `sys_site_datasource`;
CREATE TABLE `sys_site_datasource` (
  `site_id` smallint(11) NOT NULL COMMENT '站点ID',
  `datasource` varchar(50) NOT NULL COMMENT '数据源名称',
  PRIMARY KEY (`site_id`,`datasource`),
  KEY `sys_site_datasource_datasource` (`datasource`) 
) COMMENT='站点数据源';
ALTER TABLE `cms_content_file` 
	DROP INDEX `cms_content_file_content_id`,
	DROP INDEX `cms_content_file_sort`,
	DROP INDEX `cms_content_file_user_id`,
	ADD INDEX `cms_content_file_content_id`(`content_id`, `sort`);
ALTER TABLE `cms_content` 
    MODIFY COLUMN `id` bigint(20) NOT NULL FIRST;
ALTER TABLE `cms_comment` 
    MODIFY COLUMN `id` bigint(20) NOT NULL FIRST;