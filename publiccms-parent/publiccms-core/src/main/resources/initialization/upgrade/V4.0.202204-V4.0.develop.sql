-- 2022-04-25 --
UPDATE `sys_module` SET `url` = 'cmsPlace/metadata' WHERE `id` ='place_publish';
-- 2022-05-02 --
UPDATE `sys_module` SET `sort` = 4 WHERE `id` ='task_template_list';
UPDATE `sys_module` SET `sort` = 5 WHERE `id` ='webfile_list';
UPDATE `sys_module` SET `sort` = 2 WHERE `id` ='place_list';
INSERT INTO `sys_module` VALUES ('diy_list', 'diy/list', 'diy/layout,diy/module,placeTemplate/lookupPlace', 'icon-dashboard', 'file_menu', 1, 5);
INSERT INTO `sys_module_lang` VALUES ('diy_list', 'en', 'Page visualization management');
INSERT INTO `sys_module_lang` VALUES ('diy_list', 'ja', 'ページ視覚化管理');
INSERT INTO `sys_module_lang` VALUES ('diy_list', 'zh', '页面可视化管理');
ALTER TABLE `sys_user`
    CHANGE COLUMN `nick_name` `nickname` varchar(45) NOT NULL COMMENT '昵称' AFTER `weak_password`;
