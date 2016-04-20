-- 20160414 --
update `cms_category` set extend_id = NULL where extend_id = 0;
update `cms_category_type` set extend_id = NULL where extend_id = 0;
update `cms_model` set extend_id = NULL where extend_id = 0;
ALTER TABLE  `sys_user_token` ADD  `site_id` INT NOT NULL COMMENT  '站点ID' AFTER  `auth_token` , ADD INDEX (  `site_id` );
ALTER TABLE  `sys_extend` ADD  `item_type` varchar(20) NOT NULL COMMENT '扩展类型' AFTER  `id`,ADD  `item_id` INT NOT NULL COMMENT  '扩展项目ID' AFTER  `item_type`;
UPDATE  `public_cms`.`sys_extend` SET  `item_type` =  'model',`item_id` =  7 WHERE  `sys_extend`.`id` =1;
UPDATE  `public_cms`.`sys_extend` SET  `item_type` =  'category',`item_id` =  19 WHERE  `sys_extend`.`id` =2;
UPDATE  `public_cms`.`sys_extend` SET  `item_type` =  'category',`item_id` =  20 WHERE  `sys_extend`.`id` =3;
-- 20160416 --
ALTER TABLE  `sys_site` ADD  `use_static` TINYINT( 1 ) NOT NULL COMMENT  '启用静态化' AFTER  `name`;
ALTER TABLE  `sys_site` ADD  `use_ssi` TINYINT( 1 ) NOT NULL COMMENT  '启用服务器端包含' AFTER  `site_path`;
ALTER TABLE  `cms_page_data` CHANGE  `user_id`  `user_id` INT( 11 ) NOT NULL COMMENT  '提交用户';
ALTER TABLE `cms_page_data` DROP `type`;
ALTER TABLE `sys_dept_page` DROP `type`;
-- 20160419 --
RENAME TABLE  `public_cms`.`cms_page_data` TO  `public_cms`.`cms_place` ;
RENAME TABLE  `public_cms`.`cms_page_data_attribute` TO  `public_cms`.`cms_place_attribute` ;
ALTER TABLE  `cms_place_attribute` CHANGE  `page_data_id`  `place_id` INT( 11 ) NOT NULL COMMENT  '位置ID';
update `sys_moudle` set `authorized_url`='cmsPlace/clear' where `id`=54;
update `sys_moudle` set `authorized_url`='cmsPlace/check' where `id`=52;
update `sys_moudle` set `authorized_url`='cmsPlace/refresh' where `id`=51;
update `sys_moudle` set `authorized_url`='cmsPlace/delete' where `id`=50;
update `sys_moudle` set `authorized_url`='cmsContent/lookup,cmsPage/lookup_content_list,file/doUpload,cmsPlace/save' where `id`=49;
update `sys_moudle` set `authorized_url`='cmsPage/saveMetaData,file/doUpload,cmsPage/clearCache' where `id`=48;