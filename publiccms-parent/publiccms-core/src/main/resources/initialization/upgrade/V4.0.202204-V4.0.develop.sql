-- 2022-04-25 --
UPDATE `sys_module` SET `url` = 'cmsPlace/metadata' WHERE `id` ='place_publish';
-- 2022-05-02 --
UPDATE `sys_module` SET `sort` = 4 WHERE `id` ='task_template_list';
UPDATE `sys_module` SET `sort` = 5 WHERE `id` ='webfile_list';
UPDATE `sys_module` SET `sort` = 2 WHERE `id` ='place_list';
INSERT INTO `sys_module` VALUES ('diy_list', 'diy/list', NULL, 'icon-dashboard', 'file_menu', 1, 5);
INSERT INTO `sys_module` VALUES ('diy', 'diy/index', NULL, 'bi bi-palette', 'page_menu', 1, 3);
INSERT INTO `sys_module_lang` VALUES ('diy_list', 'en', 'Visual management');
INSERT INTO `sys_module_lang` VALUES ('diy_list', 'ja', '視覚的管理');
INSERT INTO `sys_module_lang` VALUES ('diy_list', 'zh', '可视化管理');
INSERT INTO `sys_module_lang` VALUES ('diy', 'en', 'Visualized page');
INSERT INTO `sys_module_lang` VALUES ('diy', 'ja', '視覚化されたページ');
INSERT INTO `sys_module_lang` VALUES ('diy', 'zh', '页面可视化');
ALTER TABLE `sys_user`
    CHANGE COLUMN `nick_name` `nickname` varchar(45) NOT NULL COMMENT '昵称' AFTER `weak_password`;
