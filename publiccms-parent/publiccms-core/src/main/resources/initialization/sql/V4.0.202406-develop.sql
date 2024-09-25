-- 2024-09-25 --
UPDATE `sys_module` SET `authorized_url`= 'cmsPlace/push,cmsPlace/add,cmsPlace/save,cmsContent/push_content,cmsContent/push_content_list,cmsContent/push_to_content,cmsContent/push_to_relation,cmsContent/related,cmsContent/unrelated,cmsPlace/delete' WHERE `id` ='content_push';
UPDATE `sys_module` SET `authorized_url`= 'cmsTemplate/save,cmsTemplate/saveMetaData,cmsWebFile/lookup,cmsTemplate/help' WHERE `id` ='template_content';
DROP TABLE IF EXISTS `sys_user_attribute`;
CREATE TABLE `sys_user_attribute` (
  `user_id` bigint(20) NOT NULL,
  `settings` text NULL COMMENT '设置JSON',
  `data` longtext COMMENT '数据JSON',
  PRIMARY KEY  (`user_id`)
) COMMENT='用户扩展';