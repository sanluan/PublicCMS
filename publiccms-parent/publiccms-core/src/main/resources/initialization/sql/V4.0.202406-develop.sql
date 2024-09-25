-- 2024-09-25 --
UPDATE `sys_module` SET `authorized_url`= 'cmsPlace/push,cmsPlace/add,cmsPlace/save,cmsContent/push_content,cmsContent/push_content_list,cmsContent/push_to_content,cmsContent/push_to_relation,cmsContent/related,cmsContent/unrelated,cmsPlace/delete' WHERE `id` ='content_push';
UPDATE `sys_module` SET `authorized_url`= 'cmsTemplate/save,cmsTemplate/saveMetaData,cmsWebFile/lookup,cmsTemplate/help' WHERE `id` ='template_content';
