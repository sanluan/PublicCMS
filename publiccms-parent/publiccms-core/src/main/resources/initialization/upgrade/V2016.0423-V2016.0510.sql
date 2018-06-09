-- 20160414 --
UPDATE `cms_category` SET extend_id = NULL WHERE extend_id = 0;
UPDATE `cms_category_type` SET extend_id = NULL WHERE extend_id = 0;
UPDATE `cms_model` SET extend_id = NULL WHERE extend_id = 0;
ALTER TABLE  `sys_user_token` ADD  `site_id` INT NOT NULL COMMENT  '站点ID' AFTER  `auth_token` , ADD INDEX (  `site_id` );
ALTER TABLE  `sys_extend` ADD  `item_type` varchar(20) NOT NULL COMMENT '扩展类型' AFTER  `id`,ADD  `item_id` INT NOT NULL COMMENT  '扩展项目ID' AFTER  `item_type`;
UPDATE  `sys_extend` SET  `item_type` =  'model',`item_id` =  7 WHERE  `sys_extend`.`id` =1;
UPDATE  `sys_extend` SET  `item_type` =  'category',`item_id` =  19 WHERE  `sys_extend`.`id` =2;
UPDATE  `sys_extend` SET  `item_type` =  'category',`item_id` =  20 WHERE  `sys_extend`.`id` =3;
-- 20160416 --
ALTER TABLE  `sys_site` ADD  `use_static` TINYINT( 1 ) NOT NULL COMMENT  '启用静态化' AFTER  `name`;
ALTER TABLE  `sys_site` ADD  `use_ssi` TINYINT( 1 ) NOT NULL COMMENT  '启用服务器端包含' AFTER  `site_path`;
ALTER TABLE  `cms_page_data` CHANGE  `user_id`  `user_id` INT( 11 ) NOT NULL COMMENT  '提交用户';
ALTER TABLE `cms_page_data` DROP `type`;
ALTER TABLE `sys_dept_page` DROP `type`;
-- 20160419 --
RENAME TABLE  `cms_page_data` TO  `cms_place` ;
RENAME TABLE  `cms_page_data_attribute` TO  `cms_place_attribute` ;
ALTER TABLE  `cms_place_attribute` CHANGE  `page_data_id`  `place_id` INT( 11 ) NOT NULL COMMENT  '位置ID';
UPDATE `sys_moudle` SET `authorized_url`='cmsPlace/clear' WHERE `id`=54;
UPDATE `sys_moudle` SET `authorized_url`='cmsPlace/check' WHERE `id`=52;
UPDATE `sys_moudle` SET `authorized_url`='cmsPlace/refresh' WHERE `id`=51;
UPDATE `sys_moudle` SET `authorized_url`='cmsPlace/delete' WHERE `id`=50;
UPDATE `sys_moudle` SET `authorized_url`='cmsContent/lookup,cmsPage/lookup_content_list,file/doUpload,cmsPlace/save' where `id`=49;
UPDATE `sys_moudle` SET `authorized_url`='cmsPage/saveMetaData,file/doUpload,cmsPage/clearCache' WHERE `id`=48;
-- 20160504 --
ALTER TABLE  `cms_content_attribute` CHANGE  `text`  `text` LONGTEXT CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT  '内容';
-- 20160506 --
ALTER TABLE  `cms_category` CHANGE  `english_name`  `code` VARCHAR( 50 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT  '编码';
ALTER TABLE  `cms_content_attribute` CHANGE  `text`  `text` LONGTEXT CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT  '内容';
UPDATE `cms_category` SET path = replace(path,'englishName','code'),content_path=replace(content_path,'englishName','code');
-- 20160509 --
ALTER TABLE  `cms_category` CHANGE  `content_path`  `content_path` VARCHAR( 500 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT  '内容路径';
ALTER TABLE  `cms_category` CHANGE  `template_path`  `template_path` VARCHAR( 255 ) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT  '模板路径';
INSERT INTO `sys_moudle` VALUES ('30', '<i class=\"icon-globe icon-large\"></i> 页面管理', 'cmsPage/placeList', 'sysUser/lookup,sysUser/lookup_content_list,cmsPage/placeDataList,cmsPage/placeDataAdd,cmsPlace/save,cmsTemplate/publishPlace,cmsPage/publishPlace,cmsPage/push_page,cmsPage/push_page_list', '4', '0');
UPDATE  `sys_moudle` SET `authorized_url` ='cmsContent/push_content,cmsContent/push_content_list,cmsContent/push_to_content,cmsContent/push_page,cmsContent/push_page_list,cmsPage/placeDataAdd,cmsPlace/save,cmsContent/related' WHERE id = 23;
DELETE FROM `sys_moudle`  WHERE id = 29;
INSERT INTO `sys_moudle` VALUES ('29', '推荐', 'cmsCategory/push_page', 'cmsCategory/push_page_list,cmsPage/placeDataAdd,cmsPlace/save', '24', '0');