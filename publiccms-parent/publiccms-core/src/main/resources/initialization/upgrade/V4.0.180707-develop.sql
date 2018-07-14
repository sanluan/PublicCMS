-- 20180711 --
ALTER TABLE `sys_site`
    ADD COLUMN `parent_id` smallint(6) DEFAULT NULL COMMENT '父站点ID' AFTER `id`,
    MODIFY COLUMN `name` varchar(50) NOT NULL COMMENT '站点名' AFTER `parent_id`;
-- 20180714 --
UPDATE `sys_moudle` SET `authorized_url` =  'cmsContent/addMore,file/doUpload,cmsContent/lookup,cmsContent/lookup_list,cmsContent/save,ueditor,ckeditor/upload,kindeditor/upload' WHERE  `sys_moudle`.`id` ='content_add';
UPDATE `sys_moudle` SET `authorized_url` =  'cmsContent/addMore,file/doUpload,cmsContent/lookup,cmsContent/lookup_list,cmsContent/save,ueditor,ckeditor/upload,kindeditor/upload' WHERE  `sys_moudle`.`id` ='myself_content_add';
