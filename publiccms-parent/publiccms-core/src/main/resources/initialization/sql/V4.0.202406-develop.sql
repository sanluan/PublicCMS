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
-- 2024-11-04 --
ALTER TABLE `cms_dictionary_exclude`
    DROP INDEX `cms_dictionary_parent_value`,
    ADD INDEX `cms_dictionary_exclude_dictionary_id` (`dictionary_id`, `site_id`);
ALTER TABLE `cms_dictionary_exclude_value`
    DROP INDEX `cms_dictionary_parent_value`,
    ADD INDEX `cms_dictionary_exclude_value_dictionary_id` (`dictionary_id`, `site_id`);
ALTER TABLE `visit_item`
    DROP INDEX `visit_item_session_id`,
    ADD INDEX `visit_item_visit_date` (`site_id`, `visit_date`, `item_type`, `item_id`, `pv`);
-- 2024-11-07 --
UPDATE `sys_module` SET parent_id = 'vote_list' WHERE parent_id = 'content_vote';
-- 2024-11-28 --
DELETE FROM sys_module WHERE id IN ('log_login_delete','log_operate_delete');
DELETE FROM sys_module_lang WHERE module_id IN ('log_login_delete','log_operate_delete');