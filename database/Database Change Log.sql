ALTER TABLE  `cms_model` ADD  `is_part` TINYINT( 1 ) NOT NULL DEFAULT  '0' COMMENT  '是否内容片段' AFTER  `is_images`;
-- 20150904 --
delete from cms_content where model_id in (select id from cms_model where is_images = 1) and parent_id is not null;
-- 20150908 请注意执行上面这条SQL之前请先编辑一遍所有图集 --
RENAME TABLE  `system_dept` TO  `sys_dept`;
RENAME TABLE  `system_task` TO  `sys_task`;
RENAME TABLE  `system_user` TO  `sys_user`;
RENAME TABLE  `system_role_user` TO  `sys_role_user`;
RENAME TABLE  `system_role_moudle` TO  `sys_role_moudle`;
RENAME TABLE  `system_role_authorized` TO  `sys_role_authorized`;
RENAME TABLE  `system_role` TO  `sys_role`;
RENAME TABLE  `system_moudle` TO  `sys_moudle`;
update `sys_moudle` set `url`=replace(`url`,'system','sys'),`authorized_url`=replace(`authorized_url`,'system','sys');
update  `sys_role_authorized` set `authorized_url`=replace(`authorized_url`,'system','sys');
-- 20151120--