SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";
DROP DATABASE `public_cms`;
CREATE DATABASE `public_cms` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `public_cms`;

DROP TABLE IF EXISTS `cms_category`;
CREATE TABLE IF NOT EXISTS `cms_category` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(50) NOT NULL COMMENT '名称',
  `parent_id` int(11) default NULL COMMENT '父分类ID',
  `child_ids` text COMMENT '所有子分类ID',
  `english_name` varchar(50) default NULL COMMENT '英文名',
  `template_path` varchar(255) NOT NULL COMMENT '模板路径',
  `path` varchar(500) NOT NULL COMMENT '首页路径',
  `url` varchar(255) default NULL COMMENT '首页地址',
  `content_path` varchar(500) NOT NULL COMMENT '内容路径',
  `page_size` int(11) default NULL COMMENT '每页数据条数',
  `disabled` tinyint(1) NOT NULL COMMENT '是否删除',
  `contents` int(11) NOT NULL default '0' COMMENT '内容数',
  `extend1` varchar(100) default NULL,
  `extend2` varchar(100) default NULL,
  `extend3` varchar(100) default NULL,
  `extend4` varchar(100) default NULL,
  PRIMARY KEY  (`id`),
  KEY `parent_id` (`parent_id`),
  KEY `disabled` (`disabled`),
  KEY `extend1` (`extend1`),
  KEY `extend2` (`extend2`),
  KEY `extend3` (`extend3`),
  KEY `extend4` (`extend4`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=11 ;

INSERT INTO `cms_category` (`id`, `name`, `parent_id`, `child_ids`, `english_name`, `template_path`, `path`, `url`, `content_path`, `page_size`, `disabled`, `contents`, `extend1`, `extend2`, `extend3`, `extend4`) VALUES
(1, '演示', NULL, '9,8,7,6', 'demonstrate', '/category/parent.html', '${category.englishName}.html', 'demonstrate.html', '${content.publishDate?string(''yyyy/MM/dd'')}/${content.id}.html', 10, 0, 0, NULL, NULL, NULL, NULL),
(6, '外链演示', 1, NULL, 'link', '/category/list.html', '${category.englishName}.html', 'link.html', '${content.categoryId}/${content.id}.html', 10, 0, 1, NULL, NULL, NULL, NULL),
(7, '文章图集', 1, NULL, 'article_picture', '/category/list.html', '${category.englishName}.html', 'article_picture.html', '${content.categoryId}/${content.id}.html', 10, 0, 0, NULL, NULL, NULL, NULL),
(8, '图集演示', 1, NULL, 'picture', '/category/list.html', '${category.englishName}.html', 'picture.html', '${content.categoryId}/${content.id}.html', 10, 0, 2, NULL, NULL, NULL, NULL),
(9, '文章演示', 1, NULL, 'article', '/category/list.html', '${category.englishName}.html', 'article.html', '${content.categoryId}/${content.id}.html', 10, 0, 0, NULL, NULL, NULL, NULL);

DROP TABLE IF EXISTS `cms_category_attribute`;
CREATE TABLE IF NOT EXISTS `cms_category_attribute` (
  `category_id` int(11) NOT NULL,
  `data` text COMMENT '数据JSON',
  PRIMARY KEY  (`category_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

INSERT INTO `cms_category_attribute` (`category_id`, `data`) VALUES
(0, '{}'),
(3, '{}'),
(1, '{}'),
(2, '{}'),
(4, '{}'),
(5, '{}'),
(6, '{}'),
(7, '{}'),
(8, '{}'),
(9, '{}'),
(10, '{}');

DROP TABLE IF EXISTS `cms_category_model`;
CREATE TABLE IF NOT EXISTS `cms_category_model` (
  `id` int(11) NOT NULL auto_increment,
  `category_id` int(11) NOT NULL COMMENT '分类ID',
  `model_id` int(11) NOT NULL COMMENT '模型ID',
  `template_path` varchar(200) NOT NULL COMMENT '内容模板路径',
  PRIMARY KEY  (`id`),
  KEY `category_id` (`category_id`),
  KEY `model_id` (`model_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=6 ;

INSERT INTO `cms_category_model` (`id`, `category_id`, `model_id`, `template_path`) VALUES
(1, 9, 1, '/system/article.html'),
(2, 8, 3, '/system/picture.html'),
(3, 7, 3, '/system/picture.html'),
(4, 7, 1, '/system/article.html'),
(5, 6, 2, '');

DROP TABLE IF EXISTS `cms_content`;
CREATE TABLE IF NOT EXISTS `cms_content` (
  `id` int(11) NOT NULL auto_increment,
  `title` varchar(255) NOT NULL,
  `user_id` int(11) NOT NULL COMMENT '发表用户',
  `dept_id` int(11) default NULL COMMENT '部门ID',
  `category_id` int(11) NOT NULL COMMENT '分类',
  `model_id` int(11) NOT NULL COMMENT '模型',
  `parent_id` int(11) default NULL COMMENT '父内容ID',
  `is_copied` tinyint(1) NOT NULL COMMENT '是否转载',
  `source` varchar(50) default NULL COMMENT '来源',
  `source_url` varchar(255) default NULL COMMENT '来源地址',
  `author` varchar(50) default NULL COMMENT '作者',
  `editor` varchar(50) default NULL COMMENT '编辑',
  `url` varchar(255) default NULL COMMENT '地址',
  `description` varchar(300) default NULL COMMENT '简介',
  `tags` varchar(100) default NULL COMMENT '标签',
  `cover` varchar(255) default NULL COMMENT '封面',
  `childs` int(11) NOT NULL COMMENT '内容页数',
  `scores` int(11) NOT NULL COMMENT '分数',
  `comments` int(11) NOT NULL COMMENT '评论数',
  `clicks` int(11) NOT NULL COMMENT '点击数',
  `publish_date` datetime NOT NULL COMMENT '发布日期',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `status` int(11) NOT NULL COMMENT '状态：0、草稿 1、已发布 2、待审核',
  `disabled` tinyint(1) NOT NULL COMMENT '是否删除',
  `extend1` varchar(100) default NULL,
  `extend2` varchar(100) default NULL,
  `extend3` varchar(100) default NULL,
  `extend4` varchar(100) default NULL,
  `model_extend1` varchar(100) default NULL,
  `model_extend2` varchar(100) default NULL,
  `model_extend3` varchar(100) default NULL,
  `model_extend4` varchar(100) default NULL,
  PRIMARY KEY  (`id`),
  KEY `publish_date` (`publish_date`),
  KEY `user_id` (`user_id`),
  KEY `category_id` (`category_id`),
  KEY `model_id` (`model_id`),
  KEY `extend1` (`extend1`),
  KEY `extend2` (`extend2`),
  KEY `extend3` (`extend3`),
  KEY `extend4` (`extend4`),
  KEY `model_extend1` (`model_extend1`),
  KEY `model_extend2` (`model_extend2`),
  KEY `model_extend3` (`model_extend3`),
  KEY `model_extend4` (`model_extend4`),
  KEY `parent_id` (`parent_id`),
  KEY `status` (`status`),
  KEY `childs` (`childs`),
  KEY `scores` (`scores`),
  KEY `comments` (`comments`),
  KEY `clicks` (`clicks`),
  KEY `title` (`title`),
  KEY `dept_id` (`dept_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;


DROP TABLE IF EXISTS `cms_content_attribute`;
CREATE TABLE IF NOT EXISTS `cms_content_attribute` (
  `content_id` int(11) NOT NULL,
  `data` text COMMENT '数据JSON',
  `text` text COMMENT '内容',
  `word_count` int(11) NOT NULL COMMENT '字数',
  PRIMARY KEY  (`content_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `cms_content_tag`;
CREATE TABLE IF NOT EXISTS `cms_content_tag` (
  `id` int(11) NOT NULL auto_increment,
  `tag_id` int(11) NOT NULL,
  `content_id` int(11) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `tag_id` (`tag_id`),
  KEY `content_id` (`content_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=24 ;

INSERT INTO `cms_content_tag` (`id`, `tag_id`, `content_id`) VALUES
(4, 4, 4),
(2, 2, 3),
(3, 3, 3),
(5, 5, 4),
(6, 6, 4),
(7, 7, 5),
(17, 7, 512),
(13, 8, 514),
(16, 8, 513),
(23, 9, 516);

DROP TABLE IF EXISTS `cms_extend`;
CREATE TABLE IF NOT EXISTS `cms_extend` (
  `id` int(11) NOT NULL auto_increment,
  `item_type` int(11) NOT NULL COMMENT '扩展项目类型,1为模型,2为分类',
  `item_id` int(11) NOT NULL COMMENT '扩展项目ID',
  `extend_type` int(11) NOT NULL COMMENT '扩展类型：1内容，2分类',
  `is_custom` tinyint(1) NOT NULL default '0',
  `is_required` tinyint(1) NOT NULL,
  `name` varchar(20) NOT NULL COMMENT '名称',
  `description` varchar(100) default NULL COMMENT '解释',
  `code` varchar(20) NOT NULL COMMENT '编码',
  `input_type` varchar(20) NOT NULL COMMENT '表单类型',
  `default_value` varchar(50) default NULL COMMENT '默认值',
  PRIMARY KEY  (`id`),
  KEY `item_id` (`item_id`),
  KEY `extend_type` (`extend_type`),
  KEY `is_custom` (`is_custom`),
  KEY `item_type` (`item_type`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=30 ;

DROP TABLE IF EXISTS `cms_form`;
CREATE TABLE IF NOT EXISTS `cms_form` (
  `id` int(11) NOT NULL auto_increment,
  `title` varchar(100) NOT NULL,
  `data` text,
  `create_date` datetime NOT NULL,
  `disabled` tinyint(1) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `create_date` (`create_date`),
  KEY `disabled` (`disabled`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

DROP TABLE IF EXISTS `cms_model`;
CREATE TABLE IF NOT EXISTS `cms_model` (
  `id` int(11) NOT NULL auto_increment,
  `parent_id` int(11) default NULL COMMENT '父模型',
  `name` varchar(20) NOT NULL COMMENT '内容模型名称',
  `template_path` varchar(200) default NULL COMMENT '默认内容模板路径',
  `has_child` tinyint(1) NOT NULL default '0' COMMENT '拥有章节',
  `is_url` tinyint(1) NOT NULL default '0' COMMENT '是链接',
  `is_images` tinyint(1) NOT NULL default '0' COMMENT '是图集',
  `is_part` tinyint(1) NOT NULL default '0' COMMENT '是否内容片段',
  `disabled` tinyint(1) NOT NULL COMMENT '是否删除',
  PRIMARY KEY  (`id`),
  KEY `disabled` (`disabled`),
  KEY `is_url` (`is_url`),
  KEY `is_images` (`is_images`),
  KEY `parent_id` (`parent_id`),
  KEY `has_child` (`has_child`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;

INSERT INTO `cms_model` (`id`, `parent_id`, `name`, `template_path`, `has_child`, `is_url`, `is_images`, `disabled`) VALUES
(1, NULL, '文章', '/system/article.html', 0, 0, 0, 0),
(2, NULL, '链接', '', 0, 1, 0, 0),
(3, NULL, '图集', '/system/picture.html', 1, 0, 1, 0);

DROP TABLE IF EXISTS `cms_tag`;
CREATE TABLE IF NOT EXISTS `cms_tag` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(50) NOT NULL,
  `category_id` int(11) default NULL,
  `type_id` int(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=10 ;

INSERT INTO `cms_tag` (`id`, `name`, `category_id`, `type_id`) VALUES
(1, '城市', NULL, NULL),
(2, '宗教', NULL, NULL),
(3, '泰拳', NULL, NULL),
(4, '漂流', NULL, NULL),
(5, '湿透', NULL, NULL),
(6, '随便', NULL, 3),
(7, '捕鱼', NULL, NULL),
(8, '啊啊啊', NULL, NULL),
(9, '外链', NULL, NULL);

DROP TABLE IF EXISTS `cms_tag_type`;
CREATE TABLE IF NOT EXISTS `cms_tag_type` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(50) NOT NULL,
  `count` int(11) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;

INSERT INTO `cms_tag_type` (`id`, `name`, `count`) VALUES
(1, '人物', 0),
(2, '地理位置', 0),
(3, '品牌', 0);

DROP TABLE IF EXISTS `log_email_check`;
CREATE TABLE IF NOT EXISTS `log_email_check` (
  `id` int(11) NOT NULL auto_increment,
  `user_id` int(11) NOT NULL,
  `email` varchar(100) NOT NULL,
  `code` varchar(40) NOT NULL,
  `create_date` datetime NOT NULL,
  `checked` tinyint(1) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `code` (`code`),
  KEY `user_id` (`user_id`),
  KEY `email` (`email`),
  KEY `create_date` (`create_date`),
  KEY `checked` (`checked`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

DROP TABLE IF EXISTS `log_login`;
CREATE TABLE IF NOT EXISTS `log_login` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(45) NOT NULL,
  `user_id` int(11) default NULL,
  `ip` varchar(20) NOT NULL,
  `result` tinyint(1) NOT NULL,
  `create_date` datetime NOT NULL,
  `error_password` varchar(100) default NULL,
  PRIMARY KEY  (`id`),
  KEY `result` (`result`),
  KEY `user_id` (`user_id`),
  KEY `create_date` (`create_date`),
  KEY `ip` (`ip`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

DROP TABLE IF EXISTS `log_operate`;
CREATE TABLE IF NOT EXISTS `log_operate` (
  `id` int(11) NOT NULL auto_increment,
  `user_id` int(11) NOT NULL,
  `operate` varchar(20) NOT NULL,
  `ip` varchar(20) default NULL,
  `create_date` datetime NOT NULL,
  `content` varchar(500) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `user_id` (`user_id`),
  KEY `operate` (`operate`),
  KEY `create_date` (`create_date`),
  KEY `ip` (`ip`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

DROP TABLE IF EXISTS `log_task`;
CREATE TABLE IF NOT EXISTS `log_task` (
  `id` int(11) NOT NULL auto_increment,
  `task_id` int(11) NOT NULL,
  `begintime` datetime NOT NULL,
  `endtime` datetime NOT NULL,
  `result` varchar(500) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `task_id` (`task_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;


DROP TABLE IF EXISTS `system_dept`;
CREATE TABLE IF NOT EXISTS `system_dept` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(50) NOT NULL COMMENT '名称',
  `parent_id` int(11) default NULL COMMENT '父部门ID',
  `description` varchar(300) default NULL COMMENT '描述',
  `user_id` int(11) default NULL COMMENT '负责人',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

INSERT INTO `system_dept` (`id`, `name`, `parent_id`, `description`, `user_id`) VALUES
(1, '总公司', NULL, '集团总公司', 1);

DROP TABLE IF EXISTS `system_moudle`;
CREATE TABLE IF NOT EXISTS `system_moudle` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(100) NOT NULL,
  `url` varchar(255) NOT NULL,
  `authorized_url` text COMMENT '授权地址',
  `parent_id` int(11) default NULL,
  PRIMARY KEY  (`id`),
  KEY `url` (`url`),
  KEY `parent_id` (`parent_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=38 ;

INSERT INTO `system_moudle` (`id`, `name`, `url`, `authorized_url`, `parent_id`) VALUES
(1, '我的', '', 'menus,index,include_page/main', NULL),
(2, '内容', '', NULL, NULL),
(3, '分类', '', NULL, NULL),
(4, '页面', '', NULL, NULL),
(32, '任务计划', '', NULL, 6),
(6, '维护', '', NULL, NULL),
(7, '用户', '', NULL, NULL),
(8, '与我相关', 'myself/list', '', 1),
(9, '修改密码', 'myself/password', 'changePassword', 8),
(10, '我的已发布', 'myself/list?queryNavTabId=myself&queryStatus=1', NULL, 8),
(11, '我的待审核', 'myself/list?queryNavTabId=myself&queryStatus=2', NULL, 8),
(12, '我的草稿', 'myself/list?queryNavTabId=myself&queryStatus=0', NULL, 8),
(14, '内容管理', 'cmsContent/list', 'file/upload,cmsTag/suggest', 2),
(15, '分类管理', 'cmsCategory/list', NULL, 3),
(16, '页面管理', 'cmsPage/list', 'file/upload', 4),
(17, '模板管理', 'cmsTemplate/list', NULL, 18),
(18, '系统维护', '', NULL, 6),
(19, '模块管理', 'systemMoudle/list', NULL, 18),
(20, '添加修改', 'systemMoudle/save', NULL, 19),
(21, '删除', 'systemMoudle/delete', NULL, 19),
(22, '用户管理', '', NULL, 7),
(23, '用户管理', 'systemUser/list', NULL, 22),
(24, '部门管理', 'systemDept/list', NULL, 22),
(25, '角色管理', 'systemRole/list', NULL, 22),
(26, '日志管理', '', NULL, 7),
(27, '操作日志', 'logOperate/list', NULL, 26),
(28, '登录日志', 'logLogin/list', NULL, 26),
(29, '邮件验证日志', 'logEmailCheck/list', NULL, 26),
(30, '模型管理', 'cmsModel/list', NULL, 18),
(33, '任务计划', 'systemTask/list', NULL, 32),
(34, '任务计划日志', 'logTask/list', NULL, 32),
(35, '标签管理', '', '', 2),
(36, '标签管理', 'cmsTag/list', 'cmsTagType/lookup,cmsCategory/lookup,cmsTag/save', 35),
(37, '标签分类', 'cmsTagType/list', 'cmsTagType/save', 35);

DROP TABLE IF EXISTS `system_role`;
CREATE TABLE IF NOT EXISTS `system_role` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(50) NOT NULL COMMENT '名称',
  `owns_all_right` tinyint(1) NOT NULL COMMENT '拥有全部权限',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

INSERT INTO `system_role` (`id`, `name`, `owns_all_right`) VALUES
(1, '超级管理员', 1),
(2, '测试管理员', 0);

DROP TABLE IF EXISTS `system_role_authorized`;
CREATE TABLE IF NOT EXISTS `system_role_authorized` (
  `id` int(11) NOT NULL auto_increment,
  `role_id` int(11) NOT NULL,
  `authorized_url` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `role_id` (`role_id`),
  KEY `authorized_url` (`authorized_url`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=11 ;

INSERT INTO `system_role_authorized` (`id`, `role_id`, `authorized_url`) VALUES
(1, 2, 'menus'),
(2, 2, 'index'),
(3, 2, 'myself/password'),
(4, 2, 'file/upload'),
(5, 2, 'myself/list'),
(6, 2, 'include_page/main'),
(7, 2, 'myself/list?queryNavTabId=myself&queryStatus=0'),
(8, 2, 'cmsContent/list'),
(9, 2, 'myself/list?queryNavTabId=myself&queryStatus=2'),
(10, 2, 'myself/list?queryNavTabId=myself&queryStatus=1');

DROP TABLE IF EXISTS `system_role_moudle`;
CREATE TABLE IF NOT EXISTS `system_role_moudle` (
  `id` int(11) NOT NULL auto_increment,
  `role_id` int(11) NOT NULL,
  `moudle_id` int(11) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `role_id` (`role_id`),
  KEY `moudle_id` (`moudle_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=29 ;

INSERT INTO `system_role_moudle` (`id`, `role_id`, `moudle_id`) VALUES
(28, 2, 2),
(27, 2, 10),
(26, 2, 9),
(25, 2, 8),
(24, 2, 1),
(23, 2, 2),
(22, 2, 10),
(21, 2, 9),
(20, 2, 8),
(19, 2, 1),
(11, 2, 1),
(12, 2, 8),
(13, 2, 9),
(14, 2, 10),
(15, 2, 11),
(16, 2, 12),
(17, 2, 2),
(18, 2, 14);

DROP TABLE IF EXISTS `system_role_user`;
CREATE TABLE IF NOT EXISTS `system_role_user` (
  `id` int(11) NOT NULL auto_increment,
  `role_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `role_id` (`role_id`),
  KEY `user_id` (`user_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=5 ;

INSERT INTO `system_role_user` (`id`, `role_id`, `user_id`) VALUES
(2, 1, 1),
(3, 2, 2);

DROP TABLE IF EXISTS `system_task`;
CREATE TABLE IF NOT EXISTS `system_task` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(50) NOT NULL,
  `status` int(11) NOT NULL,
  `cron_expression` varchar(50) NOT NULL,
  `description` varchar(300) default NULL,
  `content` varchar(500) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `status` (`status`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=6 ;

INSERT INTO `system_task` (`id`, `name`, `status`, `cron_expression`, `description`, `content`) VALUES
(1, '重新生成所有页面', 0, '0 0 0 * * ?', '重新生成所有页面', '<@_staticPage path="/include/">\r\n错误片段：\r\n<#list messageList as error>\r\n${error!}\r\n<#else>\r\n没有错误\r\n</#list>\r\n</@_staticPage>\r\n<@_staticPage>\r\n错误页面：\r\n<#list messageList as error>\r\n${error!}\r\n<#else>\r\n没有错误\r\n</#list>\r\n</@_staticPage>'),
(2, '重建索引', 0, '0 0 0 1 1 ? 2099', '重建全部索引', '<@_indexContent>\r\n<#list messageList as message>\r\n${message!}\r\n</#list>\r\n</@_indexContent >'),
(3, '清理日志', 0, '0 0 0 1 * ?', '清理三个月以前的日志', '<@_clearLog>\r\n<#list messageList as message>\r\n${message!}\r\n</#list>\r\n</@_clearLog>'),
(4, '重新生成内容页面', 0, '0 0 0 1 1 ? 2099', '重新生成内容页面', '<#assign pageCount=50/>\n<#assign dealCount=0/>\n<@_contentList pageIndex=1 count=pageCount>\n  <#list 1..page.totalPage as n>\n    <@_contentList pageIndex=n count=pageCount>\n      <#list page.list as a>\n        <@_staticContent id=a.id><#assign dealCount++/></@_staticContent>\n      </#list>\n    </@_contentList>\n  </#list>\n</@_contentList>\n共生成${dealCount}条内容静态页面'),
(5, '重新生成所有分类页面', 0, '0 0/10 * * * ?', '重新生成所有分类页面', '<#assign dealCount=0/>\r\n<#macro deal parentId>\r\n<@_categoryList parentId=parentId count=100>\r\n  <#list page.list as a>\r\n<@_contentList categoryId=a.id containChild=true endPublishDate=.now count=a.pageSize>\r\n    <@_staticCategory id=a.id totalPage=page.totalPage><#assign dealCount++/></@_staticCategory>\r\n</@_contentList>\r\n    <#if a.childIds?has_content><@deal a.id/></#if>\r\n  </#list>\r\n</@_categoryList>\r\n</#macro>\r\n<@deal ''''/>\r\n共生成${dealCount}个分类静态页面');

DROP TABLE IF EXISTS `system_user`;
CREATE TABLE IF NOT EXISTS `system_user` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(45) NOT NULL COMMENT '用户名',
  `password` varchar(32) NOT NULL COMMENT '密码',
  `nick_name` varchar(45) NOT NULL COMMENT '昵称',
  `dept_id` int(11) default NULL COMMENT '部门',
  `roles` text COMMENT '角色',
  `email` varchar(100) default NULL COMMENT '邮箱地址',
  `email_checked` tinyint(1) NOT NULL COMMENT '已验证邮箱',
  `superuser_access` tinyint(1) NOT NULL COMMENT '是否管理员',
  `disabled` tinyint(1) NOT NULL COMMENT '是否禁用',
  `auth_token` varchar(40) default NULL COMMENT '登录授权码',
  `last_login_date` datetime default NULL COMMENT '最后登录日期',
  `last_login_ip` varchar(20) default NULL COMMENT '最后登录ip',
  `login_count` int(11) NOT NULL COMMENT '登录次数',
  `date_registered` datetime default NULL COMMENT '注册日期',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `name` (`name`),
  UNIQUE KEY `nick_name` (`nick_name`),
  UNIQUE KEY `auth_token` (`auth_token`),
  KEY `login` (`name`),
  KEY `alias` (`nick_name`),
  KEY `email` (`email`),
  KEY `disabled` (`disabled`),
  KEY `lastLoginDate` (`last_login_date`),
  KEY `email_checked` (`email_checked`),
  KEY `dept_id` (`dept_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

INSERT INTO `system_user` (`id`, `name`, `password`, `nick_name`, `dept_id`, `roles`, `email`, `email_checked`, `superuser_access`, `disabled`, `auth_token`, `last_login_date`, `last_login_ip`, `login_count`, `date_registered`) VALUES
(1, 'admin', '21232f297a57a5a743894a0e4a801fc3', '管理员', 1, '1', 'master@sanluan.com', 0, 1, 0, '07db1a09-2dcc-4fe0-9683-38d339237ab9', '2015-08-05 17:56:04', '127.0.0.1', 16, '2015-07-06 00:00:00'),
(2, 'aaaa', '21232f297a57a5a743894a0e4a801fc3', 'aaa', 1, '2', 'aaa@aa.com', 0, 1, 0, 'a8c06076-bde4-4853-b8e0-42e936576dd3', '2015-07-28 15:55:54', '127.0.0.1', 0, '2015-07-06 00:00:00');
