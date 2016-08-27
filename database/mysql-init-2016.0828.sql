SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for cms_category
-- ----------------------------
DROP TABLE IF EXISTS `cms_category`;
CREATE TABLE `cms_category` (
  `id` int(11) NOT NULL auto_increment,
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `parent_id` int(11) default NULL COMMENT '父分类ID',
  `type_id` int(11) default NULL COMMENT '分类类型',
  `child_ids` text COMMENT '所有子分类ID',
  `tag_type_ids` text default NULL COMMENT '标签分类',
  `code` varchar(50) default NULL COMMENT '编码',
  `template_path` varchar(255) default NULL COMMENT '模板路径',
  `path` varchar(2000) NOT NULL COMMENT '首页路径',
  `only_url` tinyint(1) NOT NULL COMMENT '外链',
  `has_static` tinyint(1) NOT NULL COMMENT '已经静态化',
  `url` varchar(2048) default NULL COMMENT '首页地址',
  `content_path` varchar(500) default NULL COMMENT '内容路径',
  `page_size` int(11) default NULL COMMENT '每页数据条数',
  `allow_contribute` tinyint(1) NOT NULL COMMENT '允许投稿',
  `sort` int(11) NOT NULL default '0' COMMENT '顺序',
  `hidden` tinyint(1) NOT NULL COMMENT '隐藏',
  `disabled` tinyint(1) NOT NULL COMMENT '是否删除',
  `contents` int(11) NOT NULL default '0' COMMENT '内容数',
  `extend_id` int(11) default NULL COMMENT '扩展ID',
  PRIMARY KEY  (`id`),
  KEY `parent_id` (`parent_id`),
  KEY `disabled` (`disabled`),
  KEY `sort` (`sort`),
  KEY `site_id` (`site_id`),
  KEY `type_id` (`type_id`),
  KEY `allow_contribute` (`allow_contribute`),
  KEY `hidden` (`hidden`)
) ENGINE=MyISAM AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COMMENT='分类';

-- ----------------------------
-- Records of cms_category
-- ----------------------------
INSERT INTO `cms_category` VALUES ('1', '1', '演示', null, null, '17,15,12,9,8,7,6,18', '', 'demonstrate', '/category/parent.html', '${category.code}/index.html', '0', '1', '//www.publiccms.com/demonstrate/index.html', '${content.publishDate?string(\'yyyy/MM/dd\')}/${content.id}.html', '10', '0', '0', '0', '0', '0', null);
INSERT INTO `cms_category` VALUES ('6', '1', '汽车', '1', null, null, '', 'car', '/category/list.html', '${category.code}/index.html', '0', '1', '//www.publiccms.com/car/index.html', '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', '10', '0', '0', '0', '0', '0', null);
INSERT INTO `cms_category` VALUES ('7', '1', '社会', '1', null, null, '', 'social', '/category/list.html', '${category.code}/index.html', '0', '1', '//www.publiccms.com/social/index.html', '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', '10', '0', '0', '0', '0', '0', null);
INSERT INTO `cms_category` VALUES ('8', '1', '美图', '1', null, null, '', 'picture', '/category/list.html', '${category.code}/index.html', '0', '1', '//www.publiccms.com/picture/index.html', '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', '10', '0', '0', '0', '0', '0', null);
INSERT INTO `cms_category` VALUES ('9', '1', '系统介绍', '1', null, null, '', 'introduction', '/category/list.html', '${category.code}/index.html', '0', '1', '//www.publiccms.com/introduction/index.html', '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', '10', '0', '0', '0', '0', '2', null);
INSERT INTO `cms_category` VALUES ('12', '1', '文章', '1', null, null, '', 'article', '/category/list.html', '${category.code}/index.html', '0', '1', '//www.publiccms.com/article/index.html', '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', '20', '0', '0', '0', '0', '0', null);
INSERT INTO `cms_category` VALUES ('11', '1', '测试', null, null, null, null, 'test', '/category/parent.html', '${category.code}/index.html', '0', '0', 'test/index.html', '${content.publishDate?string(\'yyyy/MM/dd\')}/${content.id}.html', '20', '0', '0', '0', '1', '0', null);
INSERT INTO `cms_category` VALUES ('13', '1', '下载', null, null, null, null, 'download', '', 'https://github.com/sanluan/PublicCMS', '0',  '0', 'https://github.com/sanluan/PublicCMS', '', '20', '0', '0', '0', '1', '0', null);
INSERT INTO `cms_category` VALUES ('14', '1', '图书', '1', null, null, null, 'book', '/category/parent.html', 'demonstrate/${category.code}/index.html', '0', '0', 'demonstrate/book/index.html', '${content.publishDate?string(\'yyyy/MM/dd\')}/${content.id}.html', '20', '0', '0', '0', '1', '0', null);
INSERT INTO `cms_category` VALUES ('15', '1', '小说', '1', null, null, '', 'novel', '/category/list.html', '${category.code}/index.html', '0', '1', '//www.publiccms.com/novel/index.html', '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', '20', '0', '0', '0', '0', '0', null);
INSERT INTO `cms_category` VALUES ('16', '1', 'OSChina下载', '13', null, null, null, 'download', '', 'http://git.oschina.net/sanluan/PublicCMS', '0', '0', 'http://git.oschina.net/sanluan/PublicCMS', '', '20', '0', '0', '0', '1', '0', null);
INSERT INTO `cms_category` VALUES ('17', '1', '科技', '1', null, null, '', 'science', '/category/list.html', '${category.code}/index.html', '0', '1', '//www.publiccms.com/science/index.html', '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', '20', '0', '0', '0', '0', '0', null);
INSERT INTO `cms_category` VALUES ('18', '1', '商品', '1', null, null, '', 'product', '/category/product_list.html', '${category.code}/index.html', '0', '1', '//www.publiccms.com/product/index.html', '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', '10', '0', '0', '0', '0', '-3', null);
INSERT INTO `cms_category` VALUES ('19', '1', '案例', null, null, null, '', 'case', '/category/parent.html', '${category.code}/index.html', '0', '1', '//www.publiccms.com/case/index.html', '${content.publishDate?string(\'yyyy/MM/dd\')}/${content.id}.html', '20', '0', '0', '0', '0', '2', '2');

-- ----------------------------
-- Table structure for cms_category_attribute
-- ----------------------------
DROP TABLE IF EXISTS `cms_category_attribute`;
CREATE TABLE `cms_category_attribute` (
  `category_id` int(11) NOT NULL COMMENT '分类ID',
  `title` varchar(80) default NULL COMMENT '标题',
  `keywords` varchar(100) default NULL COMMENT '关键词',
  `description` varchar(300) default NULL COMMENT '描述',
  `data` longtext COMMENT '数据JSON',
  PRIMARY KEY  (`category_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='分类扩展';

-- ----------------------------
-- Records of cms_category_attribute
-- ----------------------------
INSERT INTO `cms_category_attribute` VALUES ('3', null, null, null, '{}');
INSERT INTO `cms_category_attribute` VALUES ('1', '演示', 'PublicCMS,如何使用', 'PublicCMS如何使用', null);
INSERT INTO `cms_category_attribute` VALUES ('2', null, null, null, '{}');
INSERT INTO `cms_category_attribute` VALUES ('4', null, null, null, '{}');
INSERT INTO `cms_category_attribute` VALUES ('5', null, null, null, '{}');
INSERT INTO `cms_category_attribute` VALUES ('6', '汽车 - PublicCMS', '汽车,car', '汽车', null);
INSERT INTO `cms_category_attribute` VALUES ('7', '社会', '社会', '社会', null);
INSERT INTO `cms_category_attribute` VALUES ('8', '美图', '美图,美女', '美图美女', null);
INSERT INTO `cms_category_attribute` VALUES ('9', '系统介绍', 'PublicCMS,系统介绍', 'PublicCMS系统介绍', null);
INSERT INTO `cms_category_attribute` VALUES ('10', null, null, null, '{}');
INSERT INTO `cms_category_attribute` VALUES ('11', null, null, null, '{}');
INSERT INTO `cms_category_attribute` VALUES ('12', '文章', '文章', '文章', null);
INSERT INTO `cms_category_attribute` VALUES ('13', null, null, null, '{}');
INSERT INTO `cms_category_attribute` VALUES ('14', null, null, null, '{}');
INSERT INTO `cms_category_attribute` VALUES ('15', '小说', '小说,在线阅读', '小说,在线阅读', null);
INSERT INTO `cms_category_attribute` VALUES ('16', null, null, null, '{}');
INSERT INTO `cms_category_attribute` VALUES ('17', '科技', '科技', '科技频道', null);
INSERT INTO `cms_category_attribute` VALUES ('18', '商品', '商品,导购', '商品', null);
INSERT INTO `cms_category_attribute` VALUES ('19', '案例', 'PublicCMS案例', 'PublicCMS案例', null);

-- ----------------------------
-- Table structure for cms_category_model
-- ----------------------------
DROP TABLE IF EXISTS `cms_category_model`;
CREATE TABLE `cms_category_model` (
  `id` int(11) NOT NULL auto_increment,
  `category_id` int(11) NOT NULL COMMENT '分类ID',
  `model_id` int(11) NOT NULL COMMENT '模型ID',
  `template_path` varchar(200) default NULL COMMENT '内容模板路径',
  PRIMARY KEY  (`id`),
  KEY `category_id` (`category_id`),
  KEY `model_id` (`model_id`)
) ENGINE=MyISAM AUTO_INCREMENT=48 DEFAULT CHARSET=utf8 COMMENT='分类模型';

-- ----------------------------
-- Records of cms_category_model
-- ----------------------------
INSERT INTO `cms_category_model` VALUES ('1', '9', '1', '/system/article.html');
INSERT INTO `cms_category_model` VALUES ('2', '8', '3', '/system/picture.html');
INSERT INTO `cms_category_model` VALUES ('3', '7', '3', '/system/picture.html');
INSERT INTO `cms_category_model` VALUES ('4', '7', '1', '/system/article.html');
INSERT INTO `cms_category_model` VALUES ('46', '6', '2', '');
INSERT INTO `cms_category_model` VALUES ('6', '12', '2', '');
INSERT INTO `cms_category_model` VALUES ('7', '12', '1', '/system/article.html');
INSERT INTO `cms_category_model` VALUES ('8', '15', '4', '/system/book.html');
INSERT INTO `cms_category_model` VALUES ('9', '15', '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES ('10', '15', '5', '');
INSERT INTO `cms_category_model` VALUES ('11', '9', '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES ('12', '9', '5', '');
INSERT INTO `cms_category_model` VALUES ('13', '9', '3', '/system/picture.html');
INSERT INTO `cms_category_model` VALUES ('14', '9', '2', '');
INSERT INTO `cms_category_model` VALUES ('15', '16', '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES ('16', '16', '5', '');
INSERT INTO `cms_category_model` VALUES ('17', '6', '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES ('18', '6', '5', '');
INSERT INTO `cms_category_model` VALUES ('47', '6', '1', '/system/article.html');
INSERT INTO `cms_category_model` VALUES ('45', '6', '3', '/system/picture.html');
INSERT INTO `cms_category_model` VALUES ('21', '8', '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES ('22', '8', '5', '');
INSERT INTO `cms_category_model` VALUES ('23', '7', '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES ('24', '7', '5', '');
INSERT INTO `cms_category_model` VALUES ('25', '17', '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES ('26', '17', '5', '');
INSERT INTO `cms_category_model` VALUES ('27', '17', '3', '/system/picture.html');
INSERT INTO `cms_category_model` VALUES ('28', '17', '2', '');
INSERT INTO `cms_category_model` VALUES ('29', '17', '1', '/system/article.html');
INSERT INTO `cms_category_model` VALUES ('30', '7', '2', '');
INSERT INTO `cms_category_model` VALUES ('31', '14', '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES ('32', '14', '5', '');
INSERT INTO `cms_category_model` VALUES ('33', '12', '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES ('34', '12', '5', '');
INSERT INTO `cms_category_model` VALUES ('35', '1', '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES ('36', '1', '5', '');
INSERT INTO `cms_category_model` VALUES ('37', '18', '8', '');
INSERT INTO `cms_category_model` VALUES ('38', '18', '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES ('39', '18', '5', '');
INSERT INTO `cms_category_model` VALUES ('41', '19', '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES ('42', '19', '5', '');
INSERT INTO `cms_category_model` VALUES ('43', '19', '2', '');
INSERT INTO `cms_category_model` VALUES ('44', '18', '7', '');

-- ----------------------------
-- Table structure for cms_category_type
-- ----------------------------
DROP TABLE IF EXISTS `cms_category_type`;
CREATE TABLE `cms_category_type` (
  `id` int(11) NOT NULL auto_increment,
  `siteId` int(11) NOT NULL COMMENT '站点ID',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `sort` int(11) NOT NULL COMMENT '排序',
  `extend_id` int(11) default NULL COMMENT '扩展ID',
  PRIMARY KEY  (`id`),
  KEY `siteId` (`siteId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of cms_category_type
-- ----------------------------

-- ----------------------------
-- Table structure for cms_content
-- ----------------------------
DROP TABLE IF EXISTS `cms_content`;
CREATE TABLE `cms_content` (
  `id` bigint(20) NOT NULL auto_increment,
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `title` varchar(255) NOT NULL COMMENT '标题',
  `user_id` bigint(20) NOT NULL COMMENT '发表用户',
  `check_user_id` bigint(20) default NULL COMMENT '审核用户',
  `category_id` int(11) NOT NULL COMMENT '分类',
  `model_id` int(11) NOT NULL COMMENT '模型',
  `parent_id` bigint(20) default NULL COMMENT '父内容ID',
  `copied` tinyint(1) NOT NULL COMMENT '是否转载',
  `author` varchar(50) default NULL COMMENT '作者',
  `editor` varchar(50) default NULL COMMENT '编辑',
  `only_url` tinyint(1) NOT NULL COMMENT '外链',
  `has_images` tinyint(1) NOT NULL COMMENT '拥有图片列表',
  `has_files` tinyint(1) NOT NULL COMMENT '拥有附件列表',
  `has_static` tinyint(1) NOT NULL COMMENT '已经静态化',
  `url` varchar(2048) default NULL COMMENT '地址',
  `description` varchar(300) default NULL COMMENT '简介',
  `tag_ids` text default NULL COMMENT '标签',
  `cover` varchar(255) default NULL COMMENT '封面',
  `childs` int(11) NOT NULL COMMENT '内容页数',
  `scores` int(11) NOT NULL COMMENT '分数',
  `comments` int(11) NOT NULL COMMENT '评论数',
  `clicks` int(11) NOT NULL COMMENT '点击数',
  `publish_date` datetime NOT NULL COMMENT '发布日期',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `status` int(11) NOT NULL COMMENT '状态：0、草稿 1、已发布 2、待审核',
  `disabled` tinyint(1) NOT NULL COMMENT '是否删除',
  PRIMARY KEY  (`id`),
  KEY `publish_date` (`publish_date`),
  KEY `user_id` (`user_id`),
  KEY `category_id` (`category_id`),
  KEY `model_id` (`model_id`),
  KEY `parent_id` (`parent_id`),
  KEY `status` (`status`),
  KEY `childs` (`childs`),
  KEY `scores` (`scores`),
  KEY `comments` (`comments`),
  KEY `clicks` (`clicks`),
  KEY `title` (`title`),
  KEY `check_user_id` (`check_user_id`),
  KEY `site_id` (`site_id`),
  KEY `has_files` (`has_files`),
  KEY `has_images` (`has_images`),
  KEY `only_url` (`only_url`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='内容';

-- ----------------------------
-- Records of cms_content
-- ----------------------------
INSERT INTO `cms_content` VALUES ('1', '1', 'PublicCMS合作伙伴招募', '1', '1', '9', '1', null, '0', '', '', '0', '0', '0', '1', '//www.publiccms.com/introduction/2015/11-10/194.html', 'Public CMS V1.0 8月6号预发布，10月9号发布第一份文档，已经积累了超出作者预期的用户数量。作为技能比较单一的技术人员，我一个人开发的Public CMS有着各种局限性，因此诚邀各位加入。', '1', '2015/11/10/12-05-5404301838588841.jpg', '0', '0', '0', '808', '2015-11-10 12:05:58', '2015-11-10 12:05:58', '1', '0');
INSERT INTO `cms_content` VALUES ('2', '1', 'PublicCMS 2016新版本即将发布', '1', '1', '9', '1', null, '0', '', '', '0', '0', '0', '1', '//www.publiccms.com/introduction/2016/03-21/215.html', '经过三个多月的研发，PublicCMS 2016即将发布。现在已经进入内测阶段，诚邀技术人员加入到测试与新版体验中。', '1', '2016/03/09/10-39-540052697476660.png', '0', '0', '0', '250', '2016-03-21 22:47:31', '2016-03-09 10:39:56', '1', '0');
INSERT INTO `cms_content` VALUES ('3', '1', 'Apache FreeMarker从入门到精通教程', '1', '1', '9', '2', null, '0', '湖水没了', '', '1', '0', '0', '0', 'http://www.elsyy.com/course/6841', 'PublicCMS的作者，签约的一个FreeMarker课程', '', '2016/03/05/15-56-080730-1247100853.jpg', '0', '0', '0', '2', '2016-03-05 15:56:13', '2016-03-05 15:56:13', '1', '0');

-- ----------------------------
-- Table structure for cms_content_attribute
-- ----------------------------
DROP TABLE IF EXISTS `cms_content_attribute`;
CREATE TABLE `cms_content_attribute` (
  `content_id` bigint(20) NOT NULL,
  `source` varchar(50) default NULL COMMENT '内容来源',
  `source_url` varchar(2048) default NULL COMMENT '来源地址',
  `data` longtext COMMENT '数据JSON',
  `text` longtext COMMENT '内容',
  `word_count` int(11) NOT NULL COMMENT '字数',
  PRIMARY KEY  (`content_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='内容扩展';

-- ----------------------------
-- Records of cms_content_attribute
-- ----------------------------
INSERT INTO `cms_content_attribute` VALUES ('1', '', '', '{}', '<p style=\"text-indent: 2em;\">Public CMS V1.0 8月6号预发布<span style=\"text-indent: 32px;\">，</span>10月9号发布第一份文档<span style=\"text-indent: 32px;\">，</span>已经积累了超出作者预期的用户数量。作为技能比较单一的技术人员，我一个人开发的Public CMS有着各种局限性，因此诚邀各位加入<span style=\"text-indent: 32px;\">，</span>共同维护这一产品并制定今后的发展方向等。</p><p style=\"text-indent: 2em;\">Public CMS的QQ群目前已经有了70人<span style=\"text-indent: 32px;\">，</span>群号：191381542。偶尔在其他技术类的QQ群竟能遇到Public CMS的用户，让我欣喜不已，同时也知道原来除了Public CMS的交流群中的群友，PublicCMS还有很多没加群的用户。为了更好的交流，大家可以加群。</p><p style=\"text-indent: 2em;\">以下是我一些初步的想法：</p><h3 style=\"text-indent: 2em;\">技术方向<br/></h3><p>&nbsp; &nbsp; 短期（一年左右）内Public CMS的大致发展方向主要集中在功能完善上，包括：</p><ul class=\" list-paddingleft-2\" style=\"list-style-type: disc;\"><li><p style=\"text-indent: 2em;\">后台UI：功能加强，浏览器兼容性完善。或者寻找其他更完善的UI替换掉现有的dwz。</p></li><li><p style=\"text-indent: 2em;\">后台功能：内容维护扩展；页面元数据扩展；分类等排序；推荐位类型扩展；推荐位可选数据类型扩充；模板在线开发功能完善；统计；附件管理等。</p></li><li><p style=\"text-indent: 2em;\">前台模板:前台模板丰富性，美观度提升。</p></li><li><p style=\"text-indent: 2em;\">纯动态站点屏蔽静态化方面配置方面的完善。<br/></p></li></ul><p>&nbsp; &nbsp; 长期规划：多站点，集群，云端内容共享，模板定制平台，二次开发代码在线定制生成等</p><h3 style=\"text-indent: 2em;\">文档方面</h3><ul class=\" list-paddingleft-2\" style=\"list-style-type: disc;\"><li><p style=\"text-indent: 2em;\">在现有文档基础上完善操作步骤细节，二次开发部分完善。</p></li><li><p style=\"text-indent: 2em;\">以Public CMS为基础产品，结合其他产品完成满足不同业务场景的解决方案级文档。</p></li><li><p style=\"text-indent: 2em;\">Public CMS相关的第三放产品的使用、配置、二次开发手册。</p></li><li><p style=\"text-indent: 2em;\">Public CMS产品使用过程中的问题库建设。</p></li><li><p style=\"text-indent: 2em;\">开发或者使用其他BBS架设社区。</p></li></ul><h3 style=\"text-indent: 2em;\">商务方面</h3><p style=\"text-indent: 2em;\">纯公益的行为是不能长久的。Public CMS本身将永久免费开源，不收取任何授权费用，允许用户自由修改开发。在此原则下，可以在模板定制，功能定制，项目承接，技术培训，产品使用培训，或开发商业版产品等方式尝试创收。</p>', '1717');
INSERT INTO `cms_content_attribute` VALUES ('2', '', '', '{}', '<p style=\"text-indent: 2em;\">经过三个多月的研发，PublicCMS 2016即将发布。现在已经进入内测阶段，诚邀技术人员加入到测试与新版体验中。</p><p>&nbsp;&nbsp;&nbsp;&nbsp;需要注意的是现在的版本并不是稳定版，请不要使用在正式项目中。<br/></p><p>&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"http://git.oschina.net/sanluan/PublicCMS-preview\" _src=\"http://git.oschina.net/sanluan/PublicCMS-preview\">http://git.oschina.net/sanluan/PublicCMS-preview</a></p>', '355');
INSERT INTO `cms_content_attribute` VALUES ('3', '', '', '{}', null, '0');

-- ----------------------------
-- Table structure for cms_content_file
-- ----------------------------
DROP TABLE IF EXISTS `cms_content_file`;
CREATE TABLE `cms_content_file` (
  `id` bigint(20) NOT NULL auto_increment,
  `content_id` bigint(20) NOT NULL COMMENT '内容',
  `user_id` bigint(20) NOT NULL COMMENT '用户',
  `file_path` varchar(255) NOT NULL COMMENT '文件路径',
  `image` tinyint(1) NOT NULL COMMENT '是否图片',
  `size` int(11) NOT NULL COMMENT '大小',
  `clicks` int(11) NOT NULL COMMENT '点击数',
  `sort` int(11) NOT NULL COMMENT '排序',
  `description` varchar(300) default NULL COMMENT '描述',
  PRIMARY KEY  (`id`),
  KEY `content_id` (`content_id`),
  KEY `sort` (`sort`),
  KEY `image` (`image`),
  KEY `size` (`size`),
  KEY `clicks` (`clicks`),
  KEY `user_id` (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='内容附件';

-- ----------------------------
-- Table structure for cms_content_related
-- ----------------------------
DROP TABLE IF EXISTS `cms_content_related`;
CREATE TABLE `cms_content_related` (
  `id` bigint(20) NOT NULL auto_increment,
  `content_id` bigint(20) NOT NULL COMMENT '内容',
  `related_content_id` bigint(20) default NULL COMMENT '推荐内容',
  `user_id` bigint(20) NOT NULL COMMENT '推荐用户',
  `url` varchar(2048) default NULL COMMENT '推荐链接地址',
  `title` varchar(255) default NULL COMMENT '推荐标题',
  `description` varchar(300) default NULL COMMENT '推荐简介',
  `clicks` int(11) NOT NULL COMMENT '点击数',
  `sort` int(11) NOT NULL COMMENT '排序',
  PRIMARY KEY  (`id`),
  KEY `content_id` (`content_id`),
  KEY `related_content_id` (`related_content_id`),
  KEY `sort` (`sort`),
  KEY `user_id` (`user_id`),
  KEY `clicks` (`clicks`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='推荐推荐';

-- ----------------------------
-- Table structure for cms_content_tag
-- ----------------------------
DROP TABLE IF EXISTS `cms_content_tag`;
CREATE TABLE `cms_content_tag` (
  `id` bigint(20) NOT NULL auto_increment,
  `tag_id` bigint(20) NOT NULL COMMENT '标签ID',
  `content_id` bigint(20) NOT NULL COMMENT '内容ID',
  PRIMARY KEY  (`id`),
  KEY `tag_id` (`tag_id`),
  KEY `content_id` (`content_id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='内容标签';

-- ----------------------------
-- Records of cms_content_tag
-- ----------------------------
INSERT INTO `cms_content_tag` VALUES ('1', '1', '1');
INSERT INTO `cms_content_tag` VALUES ('2', '1', '2');
INSERT INTO `cms_content_tag` VALUES ('3', '2', '3');

-- ----------------------------
-- Table structure for cms_model
-- ----------------------------
DROP TABLE IF EXISTS `cms_model`;
CREATE TABLE `cms_model` (
  `id` int(11) NOT NULL auto_increment,
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `parent_id` int(11) default NULL COMMENT '父模型',
  `name` varchar(50) NOT NULL COMMENT '内容模型名称',
  `template_path` varchar(200) default NULL COMMENT '默认内容模板路径',
  `has_child` tinyint(1) NOT NULL COMMENT '拥有子模型',
  `only_url` tinyint(1) NOT NULL default '0' COMMENT '是链接',
  `has_images` tinyint(1) NOT NULL COMMENT '拥有图片列表',
  `has_files` tinyint(1) NOT NULL COMMENT '拥有附件列表',
  `disabled` tinyint(1) NOT NULL COMMENT '是否删除',
  `extend_id` int(11) default NULL COMMENT '扩展ID',
  PRIMARY KEY  (`id`),
  KEY `disabled` (`disabled`),
  KEY `parent_id` (`parent_id`),
  KEY `has_child` (`has_child`),
  KEY `site_id` (`site_id`),
  KEY `has_images` (`has_images`),
  KEY `has_files` (`has_files`),
  KEY `only_url` (`only_url`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='模型';

-- ----------------------------
-- Records of cms_model
-- ----------------------------
INSERT INTO `cms_model` VALUES ('1', '1', null, '文章', '/system/article.html', '0', '0', '0', '0', '0', null);
INSERT INTO `cms_model` VALUES ('2', '1', null, '链接', '', '0', '1', '0', '0', '0', null);
INSERT INTO `cms_model` VALUES ('3', '1', null, '图集', '/system/picture.html', '0', '0', '1', '0', '0', null);
INSERT INTO `cms_model` VALUES ('4', '1', null, '图书', '/system/book.html', '1', '0', '0', '0', '0', null);
INSERT INTO `cms_model` VALUES ('5', '1', '4', '卷', '', '1', '0', '0', '0', '0', null);
INSERT INTO `cms_model` VALUES ('6', '1', '5', '章节', '/system/chapter.html', '0', '0', '0', '0', '0', null);
INSERT INTO `cms_model` VALUES ('7', '1', null, '商品', '', '0', '1', '0', '0', '0', '1');

-- ----------------------------
-- Table structure for cms_place
-- ----------------------------
DROP TABLE IF EXISTS `cms_place`;
CREATE TABLE `cms_place` (
  `id` bigint(20) NOT NULL auto_increment,
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `path` varchar(255) NOT NULL COMMENT '模板路径',
  `user_id` bigint(20) NOT NULL COMMENT '提交用户',
  `item_type` varchar(50) default NULL COMMENT '推荐项目类型',
  `item_id` int(11) default NULL COMMENT '推荐项目ID',
  `title` varchar(255) NOT NULL COMMENT '标题',
  `url` varchar(2048) default NULL COMMENT '超链接',
  `cover` varchar(255) default NULL COMMENT '封面图',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `publish_date` datetime NOT NULL COMMENT '发布日期',
  `status` int(11) NOT NULL COMMENT '状态：0、前台提交 1、已发布 ',
  `clicks` int(11) NOT NULL COMMENT '点击数',
  `disabled` tinyint(1) NOT NULL COMMENT '已禁用',
  PRIMARY KEY  (`id`),
  KEY `path` (`path`),
  KEY `disabled` (`disabled`),
  KEY `publish_date` (`publish_date`),
  KEY `create_date` (`create_date`),
  KEY `site_id` (`site_id`),
  KEY `status` (`status`),
  KEY `item_id` (`item_id`),
  KEY `item_type` (`item_type`),
  KEY `user_id` (`user_id`),
  KEY `clicks` (`clicks`)
) ENGINE=MyISAM AUTO_INCREMENT=37 DEFAULT CHARSET=utf8 COMMENT='页面数据';

-- ----------------------------
-- Records of cms_place
-- ----------------------------
INSERT INTO `cms_place` VALUES ('1', '1', '/index.html/94fe86e5-45b3-4896-823a-37c6d7d6c578.html', '1', 'content', '142', 'PublicCMS后台截图', '//www.publiccms.com/introduction/2015/08-11/142.html', '2015/11/15/17-35-240834-18490682.jpg', '2016-03-21 21:25:19', '2016-03-21 21:24:54', '1', '6', '0');
INSERT INTO `cms_place` VALUES ('2', '1', '/index.html/94fe86e5-45b3-4896-823a-37c6d7d6c578.html', '1', 'content', '159', '美食', '//www.publiccms.com/picture/2015/08-13/159.html', '2015/11/15/17-35-150887-240130090.jpg', '2016-03-21 21:26:26', '2016-03-21 21:26:08', '1', '4', '0');
INSERT INTO `cms_place` VALUES ('3', '1', '/index.html/94fe86e5-45b3-4896-823a-37c6d7d6c578.html', '1', 'content', '9', '昂科拉', '//www.publiccms.com/car/2015/08-06/9.html', '2015/11/15/17-35-0606061972977756.jpg', '2016-03-21 21:28:57', '2016-03-21 21:28:36', '1', '8', '0');
INSERT INTO `cms_place` VALUES ('4', '1', '/index.html/94fe86e5-45b3-4896-823a-37c6d7d6c578.html', '1', 'content', '179', 'PublicCMS系统使用手册下载', '//www.publiccms.com/introduction/2015/10-09/179.html', '2015/11/15/17-34-560426-203327271.jpg', '2016-03-21 21:30:25', '2016-03-21 21:43:45', '1', '18', '0');
INSERT INTO `cms_place` VALUES ('5', '1', '/index.html/94fe86e5-45b3-4896-823a-37c6d7d6c578.html', '1', 'content', '195', '我们的婚纱照', '//www.publiccms.com/picture/2015/11-15/195.html', '2015/11/15/17-34-450591-326203189.jpg', '2016-03-21 21:31:04', '2016-03-20 21:30:46', '1', '4', '0');
INSERT INTO `cms_place` VALUES ('6', '1', '/index.html/11847f87-7f1b-4891-ace4-818659ce397b.html', '1', 'custom', null, 'Public CMS QQ群', 'http://shang.qq.com/wpa/qunwpa?idkey=8a633f84fb2475068182d3c447319977faca6a14dc3acf8017a160d65962a175', '', '2016-03-21 22:10:33', '2016-03-21 22:10:26', '1', '3', '0');
INSERT INTO `cms_place` VALUES ('7', '1', '/index.html/11847f87-7f1b-4891-ace4-818659ce397b.html', '1', 'custom', null, 'FreeMarker语法在线测试', 'http://sanluan.com/freemarker_test.html', '', '2016-03-21 22:11:57', '2016-03-21 22:11:47', '1', '4', '0');
INSERT INTO `cms_place` VALUES ('8', '1', '/index.html/11847f87-7f1b-4891-ace4-818659ce397b.html', '1', 'custom', null, '百度搜索：PublicCMS', 'https://www.baidu.com/s?wd=publiccms', '', '2016-03-21 22:12:12', '2016-03-21 22:12:00', '1', '3', '0');
INSERT INTO `cms_place` VALUES ('9', '1', '/index.html/11847f87-7f1b-4891-ace4-818659ce397b.html', '1', 'custom', null, 'FreeMarker2.3.23中文手册', 'http://www.kerneler.com/freemarker2.3.23/', '', '2016-03-21 22:12:24', '2016-03-21 22:12:14', '1', '5', '0');
INSERT INTO `cms_place` VALUES ('10', '1', '/index.html/11847f87-7f1b-4891-ace4-818659ce397b.html', '1', 'custom', null, 'FreeMarker2.3.23视频教程', 'http://www.elsyy.com/course/6841', '', '2016-03-21 22:12:51', '2016-03-21 22:12:37', '1', '4', '0');
INSERT INTO `cms_place` VALUES ('11', '1', '/index.html/5cf1b463-8d14-4ba4-a904-890ec224dc99.html', '1', 'custom', null, '管理后台', '//cms.publiccms.com/admin/', '', '2016-03-21 22:13:54', '2016-03-21 22:13:49', '1', '0', '1');
INSERT INTO `cms_place` VALUES ('12', '1', '/index.html/5cf1b463-8d14-4ba4-a904-890ec224dc99.html', '1', 'custom', null, '后台UI', '//image.publiccms.com/ui/', '', '2016-03-21 22:14:06', '2016-03-21 22:13:56', '1', '22', '0');
INSERT INTO `cms_place` VALUES ('13', '1', '/index.html/c6ae8ea8-103d-4c93-8ff2-79d67a38b3ae.html', '1', 'custom', null, '洪越源码', 'http://www.softhy.net/soft/36775.htm', '', '2016-03-23 11:03:50', '2016-03-23 11:03:31', '1', '3', '0');
INSERT INTO `cms_place` VALUES ('14', '1', '/index.html/c6ae8ea8-103d-4c93-8ff2-79d67a38b3ae.html', '1', 'custom', null, 'ASP300源码', 'http://www.asp300.com/SoftView/13/SoftView_59265.html', '', '2016-03-23 11:04:10', '2016-03-23 11:03:53', '1', '2', '0');
INSERT INTO `cms_place` VALUES ('15', '1', '/index.html/c6ae8ea8-103d-4c93-8ff2-79d67a38b3ae.html', '1', 'custom', null, '脚本之家', 'http://www.jb51.net/codes/389534.html', '', '2016-03-23 11:04:24', '2016-03-23 11:04:11', '1', '3', '0');
INSERT INTO `cms_place` VALUES ('16', '1', '/index.html/c6ae8ea8-103d-4c93-8ff2-79d67a38b3ae.html', '1', 'custom', null, '站长之家下载', 'http://down.chinaz.com/soft/37488.htm', '', '2016-03-23 11:04:42', '2016-03-23 11:04:33', '1', '3', '0');
INSERT INTO `cms_place` VALUES ('17', '1', '/index.html/d1bef19f-ec32-4c3b-90f9-b25ca0fe19e3.html', '1', 'custom', null, '成品网站模板超市', 'http://demo.edge-cloud.cn/', '', '2016-03-23 11:12:03', '2016-03-23 11:12:32', '1', '6', '1');
INSERT INTO `cms_place` VALUES ('18', '1', '/index.html/d1bef19f-ec32-4c3b-90f9-b25ca0fe19e3.html', '1', 'custom', null, 'QQ联系作者', 'http://wpa.qq.com/msgrd?v=3&uin=315415433&site=qq&menu=yes', '', '2016-03-23 11:12:23', '2016-03-23 11:12:05', '1', '4', '0');
INSERT INTO `cms_place` VALUES ('19', '1', '/index.html/895b6167-c2ce-43ad-b936-b1a10cd1ad5d.html', '1', 'custom', null, 'PublicCMS@Github', 'https://github.com/sanluan/PublicCMS.', '', '2016-03-23 11:13:33', '2016-03-23 11:13:25', '1', '3', '0');
INSERT INTO `cms_place` VALUES ('20', '1', '/index.html/895b6167-c2ce-43ad-b936-b1a10cd1ad5d.html', '1', 'custom', null, 'PublicCMS@开源中国', 'http://git.oschina.net/sanluan/PublicCMS', '', '2016-03-23 11:13:48', '2016-03-23 11:13:35', '1', '5', '0');
INSERT INTO `cms_place` VALUES ('21', '1', '/index.html/895b6167-c2ce-43ad-b936-b1a10cd1ad5d.html', '1', 'custom', null, 'PublicCMS@CSDN', 'https://code.csdn.net/zyyy358/publiccms', '', '2016-03-23 11:14:03', '2016-03-23 11:13:50', '1', '3', '0');
INSERT INTO `cms_place` VALUES ('22', '1', '/index.html/895b6167-c2ce-43ad-b936-b1a10cd1ad5d.html', '1', 'custom', null, 'PublicCMS-preview@开源中国', 'http://git.oschina.net/sanluan/PublicCMS-preview', '', '2016-03-23 11:14:30', '2016-03-23 11:14:09', '1', '5', '0');
INSERT INTO `cms_place` VALUES ('23', '1', '/index.html/cfdc226d-8abc-48ec-810d-f3941b175b20.html', '1', 'custom', null, '搞机哥-博客', 'http://www.gaojig.com/', '', '2016-03-23 11:15:16', '2016-03-23 11:15:07', '1', '14', '0');
INSERT INTO `cms_place` VALUES ('24', '1', '/index.html/cfdc226d-8abc-48ec-810d-f3941b175b20.html', '1', 'custom', null, '锋云科技', 'http://www.edge-cloud.cn/', '', '2016-03-23 11:15:28', '2016-03-23 11:15:21', '1', '20', '0');
INSERT INTO `cms_place` VALUES ('25', '1', '/category/list.html/3435e9a7-565a-4f93-8670-9c272a1d51cc.html', '1', 'content', '4', '唯美动漫图片', '//www.publiccms.com/8/4.html', '2015/08/07/11-24-1308292097994334.jpg', '2016-03-23 11:22:57', '2016-03-23 11:22:04', '1', '4', '0');
INSERT INTO `cms_place` VALUES ('26', '1', '/category/list.html/3435e9a7-565a-4f93-8670-9c272a1d51cc.html', '1', 'content', '9', '昂科拉', '//www.publiccms.com/6/9.html', '2015/08/07/11-24-3602801209954489.jpg', '2016-03-23 11:23:55', '2016-03-23 11:23:31', '1', '2', '0');
INSERT INTO `cms_place` VALUES ('27', '1', '/category/list.html/49d393ca-f0f1-4723-a9b0-6f9b6d7cc04d.html', '1', 'content', '19', '微软：不要在Win10中使用第三方“隐私保护”工具', '//www.publiccms.com/2015/08/06/19.html', '', '2016-03-23 11:27:26', '2016-03-23 11:27:06', '1', '0', '0');
INSERT INTO `cms_place` VALUES ('28', '1', '/category/list.html/49d393ca-f0f1-4723-a9b0-6f9b6d7cc04d.html', '1', 'content', '30', '女子吃了泡发2天的木耳 致多器官衰竭不治身亡', '//www.publiccms.com/2015/08-07/30.html', '', '2016-03-23 11:27:42', '2016-03-23 11:27:28', '1', '3', '0');
INSERT INTO `cms_place` VALUES ('29', '1', '/category/list.html/49d393ca-f0f1-4723-a9b0-6f9b6d7cc04d.html', '1', 'content', '22', '江苏仪征新集一玩具厂起大火 火光冲天', '//www.publiccms.com/7/22.html', '', '2016-03-23 11:27:55', '2016-03-23 11:27:44', '1', '3', '0');
INSERT INTO `cms_place` VALUES ('30', '1', '/category/list.html/49d393ca-f0f1-4723-a9b0-6f9b6d7cc04d.html', '1', 'content', '142', 'PublicCMS后台截图', '//www.publiccms.com/9/142.html', '', '2016-03-23 11:28:08', '2016-03-23 11:27:57', '1', '6', '0');
INSERT INTO `cms_place` VALUES ('31', '1', '/category/list.html/49d393ca-f0f1-4723-a9b0-6f9b6d7cc04d.html', '1', 'content', '18', 'PublicCMS进入测试阶段，即将正式发布', '//www.publiccms.com/9/18.html', '', '2016-03-23 11:28:21', '2016-03-23 11:28:14', '1', '7', '0');
INSERT INTO `cms_place` VALUES ('32', '1', '/category/list.html/49d393ca-f0f1-4723-a9b0-6f9b6d7cc04d.html', '1', 'content', '217', '酷冷至尊烈焰枪旗舰版机械键盘 有线104键游戏全背光 樱桃轴正品', 'http://s.click.taobao.com/t?e=m%3D2%26s%3Dk%2FRaMwaPpnYcQipKwQzePOeEDrYVVa64K7Vc7tFgwiFRAdhuF14FMV3pVOinSGgeRitN3%2FurF3zO1KWqeCJhFmPYiLpdxhFe%2B6GA20g%2FvatSQhIbbzwChQUDqeizZVd13GFiMU8U2DTHAGIcyZQCxSGFCzYOOqAQ&pvid=50_106.2.199.138_346_1458707425019', '', '2016-03-28 11:21:01', '2016-03-28 11:17:37', '1', '3', '0');
INSERT INTO `cms_place` VALUES ('33', '1', '/index.html/cfdc226d-8abc-48ec-810d-f3941b175b20.html', '1', 'custom', null, 'BD工具网', 'http://www.bdtool.net/', '', '2016-03-28 14:29:39', '2016-03-28 14:29:34', '1', '23', '0');
INSERT INTO `cms_place` VALUES ('34', '1', '/index.html/cfdc226d-8abc-48ec-810d-f3941b175b20.html', '1', 'custom', null, '在线项目计划', 'http://www.oiplan.com/user/index.do', '', '2016-03-28 16:35:01', '2016-03-28 16:34:48', '1', '25', '0');
INSERT INTO `cms_place` VALUES ('35', '1', '/index.html/5cf1b463-8d14-4ba4-a904-890ec224dc99.html', '1', 'custom', null, '动态站点', '//cms.publiccms.com/', '', '2016-03-31 18:50:06', '2016-03-31 18:49:54', '1', '16', '1');
INSERT INTO `cms_place` VALUES ('36', '1', '/index.html/d1bef19f-ec32-4c3b-90f9-b25ca0fe19e3.html', '1', 'custom', null, '成品网站模板超市', 'http://www.edge-cloud.cn/wangzhanjianshe-30-1.html', '', '2016-04-13 16:05:14', '2016-04-13 16:04:55', '1', '2', '0');

-- ----------------------------
-- Table structure for cms_place_attribute
-- ----------------------------
DROP TABLE IF EXISTS `cms_place_attribute`;
CREATE TABLE `cms_place_attribute` (
  `place_id` bigint(20) NOT NULL COMMENT '位置ID',
  `data` longtext COMMENT '数据JSON',
  PRIMARY KEY  (`place_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='推荐位数据扩展';

-- ----------------------------
-- Records of cms_place_attribute
-- ----------------------------
INSERT INTO `cms_place_attribute` VALUES ('1', '{}');
INSERT INTO `cms_place_attribute` VALUES ('2', '{}');
INSERT INTO `cms_place_attribute` VALUES ('3', '{}');
INSERT INTO `cms_place_attribute` VALUES ('4', '{}');
INSERT INTO `cms_place_attribute` VALUES ('5', '{}');
INSERT INTO `cms_place_attribute` VALUES ('6', '{}');
INSERT INTO `cms_place_attribute` VALUES ('7', '{}');
INSERT INTO `cms_place_attribute` VALUES ('8', '{}');
INSERT INTO `cms_place_attribute` VALUES ('9', '{}');
INSERT INTO `cms_place_attribute` VALUES ('10', '{}');
INSERT INTO `cms_place_attribute` VALUES ('11', '{}');
INSERT INTO `cms_place_attribute` VALUES ('12', '{}');
INSERT INTO `cms_place_attribute` VALUES ('13', '{}');
INSERT INTO `cms_place_attribute` VALUES ('14', '{}');
INSERT INTO `cms_place_attribute` VALUES ('15', '{}');
INSERT INTO `cms_place_attribute` VALUES ('16', '{}');
INSERT INTO `cms_place_attribute` VALUES ('17', '{}');
INSERT INTO `cms_place_attribute` VALUES ('18', '{}');
INSERT INTO `cms_place_attribute` VALUES ('19', '{}');
INSERT INTO `cms_place_attribute` VALUES ('20', '{}');
INSERT INTO `cms_place_attribute` VALUES ('21', '{}');
INSERT INTO `cms_place_attribute` VALUES ('22', '{}');
INSERT INTO `cms_place_attribute` VALUES ('23', '{}');
INSERT INTO `cms_place_attribute` VALUES ('24', '{}');
INSERT INTO `cms_place_attribute` VALUES ('25', '{}');
INSERT INTO `cms_place_attribute` VALUES ('26', '{}');
INSERT INTO `cms_place_attribute` VALUES ('27', '{}');
INSERT INTO `cms_place_attribute` VALUES ('28', '{}');
INSERT INTO `cms_place_attribute` VALUES ('29', '{}');
INSERT INTO `cms_place_attribute` VALUES ('30', '{}');
INSERT INTO `cms_place_attribute` VALUES ('31', '{}');
INSERT INTO `cms_place_attribute` VALUES ('32', '{}');
INSERT INTO `cms_place_attribute` VALUES ('33', '{}');
INSERT INTO `cms_place_attribute` VALUES ('34', '{}');
INSERT INTO `cms_place_attribute` VALUES ('35', '{}');
INSERT INTO `cms_place_attribute` VALUES ('36', '{}');

-- ----------------------------
-- Table structure for cms_tag
-- ----------------------------
DROP TABLE IF EXISTS `cms_tag`;
CREATE TABLE `cms_tag` (
  `id` bigint(20) NOT NULL auto_increment,
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `type_id` int(11) default NULL COMMENT '分类ID',
  `search_count` int(11) NOT NULL COMMENT '搜索次数',
  PRIMARY KEY  (`id`),
  KEY `site_id` (`site_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='标签';

-- ----------------------------
-- Records of cms_tag
-- ----------------------------

INSERT INTO `cms_tag` VALUES ('1', '1', 'PublicCMS', null, 0);
INSERT INTO `cms_tag` VALUES ('2', '1', 'FreeMarker', null, 0);

-- ----------------------------
-- Table structure for cms_tag_type
-- ----------------------------
DROP TABLE IF EXISTS `cms_tag_type`;
CREATE TABLE `cms_tag_type` (
  `id` int(11) NOT NULL auto_increment,
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `count` int(11) NOT NULL COMMENT '标签数',
  PRIMARY KEY  (`id`),
  KEY `site_id` (`site_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='标签类型';

-- ----------------------------
-- Table structure for cms_word
-- ----------------------------
DROP TABLE IF EXISTS `cms_word`;
CREATE TABLE `cms_word` (
  `id` bigint(20) NOT NULL auto_increment,
  `name` varchar(255) NOT NULL COMMENT '名称',
  `site_id` int(11) NOT NULL COMMENT '站点',
  `search_count` int(11) NOT NULL COMMENT '搜索次数',
  `hidden` tinyint(1) NOT NULL COMMENT '隐藏',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `name` (`name`,`site_id`),
  KEY `hidden` (`hidden`),
  KEY `search_count` (`search_count`),
  KEY `create_date` (`create_date`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of cms_word
-- ----------------------------

-- ----------------------------
-- Table structure for home_message
-- ----------------------------
DROP TABLE IF EXISTS `home_message`;
CREATE TABLE `home_message` (
  `id` bigint(20) NOT NULL auto_increment,
  `user_id` bigint(20) NOT NULL COMMENT '所属用户',
  `send_user_id` bigint(20) NOT NULL COMMENT '发送用户',
  `receive_user_id` bigint(20) NOT NULL COMMENT '接收用户',
  `message_id` bigint(20) default NULL COMMENT '关联消息',
  `channel` varchar(50) NOT NULL COMMENT '渠道',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `content` text NOT NULL COMMENT '消息',
  PRIMARY KEY  (`id`),
  KEY `user_id` (`user_id`),
  KEY `create_date` (`create_date`),
  KEY `message_id` (`message_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='用户消息';

-- ----------------------------
-- Records of home_message
-- ----------------------------

-- ----------------------------
-- Table structure for log_login
-- ----------------------------
DROP TABLE IF EXISTS `log_login`;
CREATE TABLE `log_login` (
  `id` bigint(20) NOT NULL auto_increment,
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `name` varchar(50) NOT NULL COMMENT '用户名',
  `user_id` bigint(20) default NULL COMMENT '用户ID',
  `ip` varchar(64) NOT NULL COMMENT 'IP',
  `channel` varchar(50) NOT NULL default 'web' COMMENT '登陆渠道',
  `result` tinyint(1) NOT NULL COMMENT '结果',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `error_password` varchar(100) default NULL COMMENT '错误密码',
  PRIMARY KEY  (`id`),
  KEY `result` (`result`),
  KEY `user_id` (`user_id`),
  KEY `create_date` (`create_date`),
  KEY `ip` (`ip`),
  KEY `site_id` (`site_id`),
  KEY `channel` (`channel`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='登陆日志';

-- ----------------------------
-- Records of log_login
-- ----------------------------

-- ----------------------------
-- Table structure for log_operate
-- ----------------------------
DROP TABLE IF EXISTS `log_operate`;
CREATE TABLE `log_operate` (
  `id` bigint(20) NOT NULL auto_increment,
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `channel` varchar(50) NOT NULL COMMENT '操作取到',
  `operate` varchar(40) NOT NULL COMMENT '操作',
  `ip` varchar(64) default NULL COMMENT 'IP',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `content` varchar(500) NOT NULL COMMENT '内容',
  PRIMARY KEY  (`id`),
  KEY `user_id` (`user_id`),
  KEY `operate` (`operate`),
  KEY `create_date` (`create_date`),
  KEY `ip` (`ip`),
  KEY `site_id` (`site_id`),
  KEY `channel` (`channel`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='操作日志';

-- ----------------------------
-- Records of log_operate
-- ----------------------------

-- ----------------------------
-- Table structure for log_task
-- ----------------------------
DROP TABLE IF EXISTS `log_task`;
CREATE TABLE `log_task` (
  `id` bigint(20) NOT NULL auto_increment,
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `task_id` int(11) NOT NULL COMMENT '任务',
  `begintime` datetime NOT NULL COMMENT '开始时间',
  `endtime` datetime default NULL COMMENT '结束时间',
  `success` tinyint(1) NOT NULL COMMENT '执行成功',
  `result` longtext COMMENT '执行结果',
  PRIMARY KEY  (`id`),
  KEY `task_id` (`task_id`),
  KEY `success` (`success`),
  KEY `site_id` (`site_id`),
  KEY `begintime` (`begintime`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='任务计划日志';

-- ----------------------------
-- Records of log_task

-- ----------------------------
-- Table structure for log_upload
-- ----------------------------
DROP TABLE IF EXISTS `log_upload`;
CREATE TABLE `log_upload` (
  `id` bigint(20) NOT NULL auto_increment,
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `channel` varchar(50) NOT NULL COMMENT '操作取到',
  `image` tinyint(1) NOT NULL COMMENT '图片',
  `file_size` BIGINT(20) NOT NULL COMMENT '文件大小',
  `ip` varchar(64) default NULL COMMENT 'IP',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `file_path` varchar(500) NOT NULL COMMENT '文件路径',
  PRIMARY KEY  (`id`),
  KEY `user_id` (`user_id`),
  KEY `create_date` (`create_date`),
  KEY `ip` (`ip`),
  KEY `site_id` (`site_id`),
  KEY `channel` (`channel`),
  KEY `image` (`image`),
  KEY `file_size` (`file_size`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='上传日志' AUTO_INCREMENT=1 ;

-- ----------------------------
-- Records of log_upload
-- ----------------------------

-- ----------------------------
-- Table structure for plugin_lottery
-- ----------------------------
DROP TABLE IF EXISTS `plugin_lottery`;
CREATE TABLE `plugin_lottery` (
  `id` int(11) NOT NULL auto_increment,
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `start_date` datetime NOT NULL COMMENT '开始日期',
  `end_date` datetime NOT NULL COMMENT '结束日期',
  `interval_hour` int(11) NOT NULL COMMENT '抽奖间隔小时',
  `gift` int(11) NOT NULL COMMENT '每次可抽奖数量',
  `total_gift` int(11) NOT NULL COMMENT '奖品总数',
  `last_gift` int(11) NOT NULL COMMENT '剩余数量',
  `lottery_count` int(11) NOT NULL COMMENT '可抽奖次数',
  `fractions` int(11) NOT NULL COMMENT '概率分子',
  `numerator` int(11) NOT NULL COMMENT '概率分母',
  `url` varchar(2048) default NULL COMMENT '地址',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `description` varchar(300) default NULL COMMENT '描述',
  `disabled` tinyint(1) NOT NULL COMMENT '是否禁用',
  `extend_id` int(11) default NULL COMMENT '扩展ID',
  PRIMARY KEY  (`id`),
  KEY `start_date` (`start_date`,`end_date`),
  KEY `disabled` (`disabled`),
  KEY `site_id` (`site_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of plugin_lottery
-- ----------------------------

-- ----------------------------
-- Table structure for plugin_lottery_user
-- ----------------------------
DROP TABLE IF EXISTS `plugin_lottery_user`;
CREATE TABLE `plugin_lottery_user` (
  `id` bigint(20) NOT NULL auto_increment,
  `lottery_id` int(11) NOT NULL COMMENT '抽奖ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `winning` tinyint(1) NOT NULL COMMENT '是否中奖',
  `ip` varchar(64) NOT NULL COMMENT 'IP',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY  (`id`),
  KEY `lottery_id` (`lottery_id`),
  KEY `user_id` (`user_id`),
  KEY `winning` (`winning`),
  KEY `create_date` (`create_date`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of plugin_lottery_user
-- ----------------------------

-- ----------------------------
-- Table structure for plugin_lottery_user_attribute
-- ----------------------------
DROP TABLE IF EXISTS `plugin_lottery_user_attribute`;
CREATE TABLE `plugin_lottery_user_attribute` (
  `lottery_user_id` bigint(20) NOT NULL COMMENT '抽奖用户ID',
  `data` longtext COMMENT '数据JSON',
  PRIMARY KEY  (`lottery_user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='抽奖用户扩展';

-- ----------------------------
-- Records of plugin_lottery_user_attribute
-- ----------------------------

-- ----------------------------
-- Table structure for plugin_site
-- ----------------------------
DROP TABLE IF EXISTS `plugin_site`;
CREATE TABLE `plugin_site` (
  `id` int(11) NOT NULL auto_increment,
  `site_id` int(11) NOT NULL COMMENT '站点',
  `plugin_code` varchar(50) NOT NULL COMMENT '插件',
  `widget_template` varchar(255) default NULL COMMENT '内容插件模板',
  `static_template` varchar(255) default NULL COMMENT '静态化模板',
  `path` varchar(2000) default NULL COMMENT '静态化路径',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `site_id` (`site_id`,`plugin_code`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of plugin_site
-- ----------------------------

-- ----------------------------
-- Table structure for plugin_vote
-- ----------------------------
DROP TABLE IF EXISTS `plugin_vote`;
CREATE TABLE `plugin_vote` (
  `id` int(11) NOT NULL auto_increment,
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `start_date` datetime NOT NULL COMMENT '开始日期',
  `end_date` datetime NOT NULL COMMENT '结束日期',
  `interval_hour` int(11) NOT NULL COMMENT '投票间隔小时',
  `max_vote` int(11) NOT NULL COMMENT '最大投票数',
  `anonymous` tinyint(1) NOT NULL COMMENT '匿名投票',
  `user_counts` int(11) NOT NULL COMMENT '参与用户数',
  `url` varchar(2048) NOT NULL COMMENT '地址',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `description` varchar(300) default NULL COMMENT '描述',
  `disabled` tinyint(1) NOT NULL COMMENT '已禁用',
  `item_extend_id` int(11) NOT NULL COMMENT '扩展ID',
  PRIMARY KEY  (`id`),
  KEY `disabled` (`disabled`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of plugin_vote
-- ----------------------------

-- ----------------------------
-- Table structure for plugin_vote_item
-- ----------------------------
DROP TABLE IF EXISTS `plugin_vote_item`;
CREATE TABLE `plugin_vote_item` (
  `id` bigint(20) NOT NULL auto_increment,
  `vote_id` int(11) NOT NULL COMMENT '投票',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `description` varchar(300) default NULL COMMENT '描述',
  `scores` int(11) NOT NULL COMMENT '票数',
  `sort` int(11) NOT NULL COMMENT '顺序',
  PRIMARY KEY  (`id`),
  KEY `lottery_id` (`vote_id`),
  KEY `user_id` (`title`),
  KEY `create_date` (`sort`),
  KEY `scores` (`scores`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of plugin_vote_item
-- ----------------------------

-- ----------------------------
-- Table structure for plugin_vote_item_attribute
-- ----------------------------
DROP TABLE IF EXISTS `plugin_vote_item_attribute`;
CREATE TABLE `plugin_vote_item_attribute` (
  `vote_item_id` bigint(20) NOT NULL COMMENT '选项ID',
  `data` longtext COMMENT '数据JSON',
  PRIMARY KEY  (`vote_item_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='投票选项扩展';

-- ----------------------------
-- Records of plugin_vote_item_attribute
-- ----------------------------

-- ----------------------------
-- Table structure for plugin_vote_user
-- ----------------------------
DROP TABLE IF EXISTS `plugin_vote_user`;
CREATE TABLE `plugin_vote_user` (
  `id` bigint(20) NOT NULL auto_increment,
  `lottery_id` int(11) NOT NULL COMMENT '抽奖ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `item_ids` text NOT NULL COMMENT '投票选项',
  `ip` varchar(64) NOT NULL COMMENT 'IP',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY  (`id`),
  KEY `lottery_id` (`lottery_id`),
  KEY `user_id` (`user_id`),
  KEY `create_date` (`create_date`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of plugin_vote_user
-- ----------------------------

-- ----------------------------
-- Table structure for sys_app
-- ----------------------------
DROP TABLE IF EXISTS `sys_app`;
CREATE TABLE `sys_app` (
  `id` int(11) NOT NULL auto_increment,
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `channel` varchar(50) NOT NULL COMMENT '渠道',
  `app_key` varchar(50) NOT NULL COMMENT 'APP key',
  `app_secret` varchar(50) NOT NULL COMMENT 'APP secret',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `key` (`app_key`),
  KEY `site_id` (`site_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_app
-- ----------------------------

-- ----------------------------
-- Table structure for sys_app_client
-- ----------------------------
DROP TABLE IF EXISTS `sys_app_client`;
CREATE TABLE `sys_app_client` (
  `id` bigint(20) NOT NULL auto_increment,
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `channel` varchar(20) NOT NULL COMMENT '渠道',
  `uuid` varchar(50) NOT NULL COMMENT '唯一标识',
  `user_id` bigint(20) default NULL COMMENT '绑定用户',
  `client_version` varchar(50) NOT NULL COMMENT '版本',
  `allow_push` tinyint(1) NOT NULL COMMENT '允许推送',
  `push_token` varchar(50) default NULL COMMENT '推送授权码',
  `last_login_date` datetime default NULL COMMENT '上次登录时间',
  `last_login_ip` varchar(64) default NULL COMMENT '上次登录IP',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `disabled` tinyint(1) NOT NULL COMMENT '是否禁用',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `site_id` (`site_id`,`uuid`,`channel`),
  KEY `user_id` (`user_id`),
  KEY `disabled` (`disabled`),
  KEY `create_date` (`create_date`),
  KEY `allow_push` (`allow_push`),
  KEY `channel` (`channel`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_app_client
-- ----------------------------

-- ----------------------------
-- Table structure for sys_app_token
-- ----------------------------
DROP TABLE IF EXISTS `sys_app_token`;
CREATE TABLE `sys_app_token` (
  `auth_token` varchar(40) NOT NULL COMMENT '授权验证',
  `app_id` int(11) NOT NULL COMMENT '应用ID',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY  (`auth_token`),
  KEY `app_id` (`app_id`),
  KEY `create_date` (`create_date`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_app_token
-- ----------------------------

-- ----------------------------
-- Table structure for sys_cluster
-- ----------------------------
DROP TABLE IF EXISTS `sys_cluster`;
CREATE TABLE `sys_cluster` (
  `uuid` varchar(40) NOT NULL COMMENT 'uuid',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `heartbeat_date` datetime NOT NULL COMMENT '心跳时间',
  `master` tinyint(1) NOT NULL COMMENT '是否管理',
  PRIMARY KEY  (`uuid`),
  KEY `create_date` (`create_date`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='服务器集群';

-- ----------------------------
-- Records of sys_cluster
-- ----------------------------

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
  `id` int(11) NOT NULL auto_increment COMMENT 'ID',
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `code` varchar(50) NOT NULL COMMENT '配置项编码',
  `subcode` varchar(50) NOT NULL COMMENT '子编码',
  `data` longtext NOT NULL COMMENT '值',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `site_id` (`site_id`,`code`,`subcode`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='站点配置' AUTO_INCREMENT=1 ;

-- ----------------------------
-- Records of sys_config
-- ----------------------------

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `id` int(11) NOT NULL auto_increment,
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `parent_id` int(11) default NULL COMMENT '父部门ID',
  `description` varchar(300) default NULL COMMENT '描述',
  `user_id` bigint(20) default NULL COMMENT '负责人',
  `owns_all_category` tinyint(1) NOT NULL COMMENT '拥有全部分类权限',
  `owns_all_page` tinyint(1) NOT NULL COMMENT '拥有全部页面权限',
  PRIMARY KEY  (`id`),
  KEY `site_id` (`site_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='部门';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES ('1', '1', '总公司', null, '集团总公司', '1', '1', '1');
INSERT INTO `sys_dept` VALUES ('2', '2', '技术部', null, '', '3', '1', '1');

-- ----------------------------
-- Table structure for sys_dept_category
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept_category`;
CREATE TABLE `sys_dept_category` (
  `id` int(11) NOT NULL auto_increment,
  `dept_id` int(11) NOT NULL COMMENT '部门ID',
  `category_id` int(11) NOT NULL COMMENT '分类ID',
  PRIMARY KEY  (`id`),
  KEY `dept_id` (`dept_id`),
  KEY `category_id` (`category_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='部门分类';

-- ----------------------------
-- Records of sys_dept_category
-- ----------------------------

-- ----------------------------
-- Table structure for sys_dept_page
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept_page`;
CREATE TABLE `sys_dept_page` (
  `id` int(11) NOT NULL auto_increment,
  `dept_id` int(11) NOT NULL COMMENT '部门ID',
  `page` varchar(255) NOT NULL COMMENT '页面',
  PRIMARY KEY  (`id`),
  KEY `dept_id` (`dept_id`),
  KEY `page` (`page`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='部门页面';

-- ----------------------------
-- Records of sys_dept_page
-- ----------------------------

-- ----------------------------
-- Table structure for sys_domain
-- ----------------------------
DROP TABLE IF EXISTS `sys_domain`;
CREATE TABLE `sys_domain` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(255) NOT NULL COMMENT '域名',
  `site_id` int(11) NOT NULL COMMENT '站点',
  `path` varchar(255) default NULL COMMENT '路径',
  PRIMARY KEY  (`id`),
  KEY `site_id` (`site_id`),
  KEY `name` (`name`)
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='域名';

-- ----------------------------
-- Records of sys_domain
-- ----------------------------
INSERT INTO `sys_domain` VALUES ('1', 'dev.publiccms.com', '1', '');
INSERT INTO `sys_domain` VALUES ('2', 'dev.publiccms.com:8080', '1', '');
INSERT INTO `sys_domain` VALUES ('3', 'member.dev.publiccms.com', '1', '/member/');
INSERT INTO `sys_domain` VALUES ('4', 'member.dev.publiccms.com:8080', '1', '/member/');
INSERT INTO `sys_domain` VALUES ('5', 'search.dev.publiccms.com', '1', '/search/');
INSERT INTO `sys_domain` VALUES ('6', 'search.dev.publiccms.com:8080', '1', '/search/');
INSERT INTO `sys_domain` VALUES ('7', 'site2.dev.publiccms.com', '2', '');
INSERT INTO `sys_domain` VALUES ('8', 'site2.dev.publiccms.com:8080', '2', '');

-- ----------------------------
-- Table structure for sys_email_token
-- ----------------------------
DROP TABLE IF EXISTS `sys_email_token`;
CREATE TABLE `sys_email_token` (
  `auth_token` varchar(40) NOT NULL COMMENT '验证码',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `email` varchar(100) NOT NULL COMMENT '邮件地址',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  PRIMARY KEY  (`auth_token`),
  KEY `create_date` (`create_date`),
  KEY `user_id` (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='邮件地址验证日志';

-- ----------------------------
-- Records of sys_email_token
-- ----------------------------

-- ----------------------------
-- Table structure for sys_extend
-- ----------------------------
DROP TABLE IF EXISTS `sys_extend`;
CREATE TABLE `sys_extend` (
  `id` int(11) NOT NULL auto_increment,
  `item_type` varchar(20) NOT NULL COMMENT '扩展类型',
  `item_id` int(11) NOT NULL COMMENT '扩展项目ID',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_extend
-- ----------------------------
INSERT INTO `sys_extend` VALUES ('1', 'model', '7');
INSERT INTO `sys_extend` VALUES ('2', 'category', '19');

-- ----------------------------
-- Table structure for sys_extend_field
-- ----------------------------
DROP TABLE IF EXISTS `sys_extend_field`;
CREATE TABLE `sys_extend_field` (
  `id` int(11) NOT NULL auto_increment,
  `extend_id` int(11) NOT NULL COMMENT '扩展ID',
  `required` tinyint(1) NOT NULL COMMENT '是否必填',
  `name` varchar(20) NOT NULL COMMENT '名称',
  `description` varchar(100) default NULL COMMENT '解释',
  `code` varchar(20) NOT NULL COMMENT '编码',
  `input_type` varchar(20) NOT NULL COMMENT '表单类型',
  `default_value` varchar(50) default NULL COMMENT '默认值',
  PRIMARY KEY  (`id`),
  KEY `item_id` (`extend_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='扩展';

-- ----------------------------
-- Records of sys_extend_field
-- ----------------------------
INSERT INTO `sys_extend_field` VALUES ('2', '1', '1', '价格', '', 'price', 'number', '');

-- ----------------------------
-- Table structure for sys_ftp_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_ftp_user`;
CREATE TABLE `sys_ftp_user` (
  `id` int(11) NOT NULL auto_increment,
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `name` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(32) NOT NULL COMMENT '密码',
  `path` varchar(255) default NULL COMMENT '路径',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `site_id` (`site_id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_ftp_user
-- ----------------------------
INSERT INTO `sys_ftp_user` VALUES ('1', '1', 'admin', '21232f297a57a5a743894a0e4a801fc3', null);

-- ----------------------------
-- Table structure for sys_moudle
-- ----------------------------
DROP TABLE IF EXISTS `sys_moudle`;
CREATE TABLE `sys_moudle` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(100) NOT NULL COMMENT '名称',
  `url` varchar(255) default NULL COMMENT '链接地址',
  `authorized_url` text COMMENT '授权地址',
  `attached` varchar(300) default NULL COMMENT '标题附加',
  `parent_id` int(11) default NULL COMMENT '父模块',
  `sort` int(11) NOT NULL COMMENT '排序',
  PRIMARY KEY  (`id`),
  KEY `url` (`url`),
  KEY `parent_id` (`parent_id`),
  KEY `sort` (`sort`)
) ENGINE=MyISAM AUTO_INCREMENT=108 DEFAULT CHARSET=utf8 COMMENT='模块';

-- ----------------------------
-- Records of sys_moudle
-- ----------------------------
INSERT INTO `sys_moudle` VALUES (1, '个人', NULL, NULL, '<i class=\"icon-user icon-large\"></i>', NULL, 0);
INSERT INTO `sys_moudle` VALUES (2, '内容', NULL, NULL, '<i class=\"icon-book icon-large\"></i>', NULL, 0);
INSERT INTO `sys_moudle` VALUES (3, '分类', NULL, NULL, '<i class=\"icon-folder-open icon-large\"></i>', NULL, 0);
INSERT INTO `sys_moudle` VALUES (4, '页面', NULL, NULL, '<i class=\"icon-globe icon-large\"></i>', NULL, 0);
INSERT INTO `sys_moudle` VALUES (5, '维护', NULL, NULL, '<i class=\"icon-cog icon-large\"></i>', NULL, 0);
INSERT INTO `sys_moudle` VALUES (6, '与我相关', NULL, NULL, '<i class=\"icon-user icon-large\"></i>', 1, 0);
INSERT INTO `sys_moudle` VALUES (7, '修改密码', 'myself/password', 'changePassword', '<i class=\"icon-key icon-large\"></i>', 6, 0);
INSERT INTO `sys_moudle` VALUES (8, '我的内容', 'myself/contentList', NULL, '<i class=\"icon-book icon-large\"></i>', 6, 0);
INSERT INTO `sys_moudle` VALUES (9, '我的操作日志', 'myself/logOperate', NULL, '<i class=\"icon-list-alt icon-large\"></i>', 6, 0);
INSERT INTO `sys_moudle` VALUES (10, '我的登陆日志', 'myself/logLogin', NULL, '<i class=\"icon-signin icon-large\"></i>', 6, 0);
INSERT INTO `sys_moudle` VALUES (11, ' 我的登陆授权', 'myself/userTokenList', NULL, '<i class=\"icon-unlock-alt icon-large\"></i>', 6, 0);
INSERT INTO `sys_moudle` VALUES (12, '内容管理', 'cmsContent/list', 'sysUser/lookup', '<i class=\"icon-book icon-large\"></i>', 2, 0);
INSERT INTO `sys_moudle` VALUES (13, '内容扩展', NULL, NULL, '<i class=\"icon-road icon-large\"></i>', 2, 0);
INSERT INTO `sys_moudle` VALUES (14, '标签管理', 'cmsTag/list', 'cmsTagType/lookup', '<i class=\"icon-tag icon-large\"></i>', 13, 0);
INSERT INTO `sys_moudle` VALUES (15, '增加/修改', 'cmsTag/add', 'cmsTagType/lookup,cmsTag/save', NULL, 14, 0);
INSERT INTO `sys_moudle` VALUES (16, '删除', NULL, 'cmsTag/delete', NULL, 14, 0);
INSERT INTO `sys_moudle` VALUES (17, '增加/修改', 'cmsContent/add', 'cmsContent/addMore,cmsContent/lookup,cmsContent/lookup_list,cmsContent/save,ueditor', NULL, 12, 0);
INSERT INTO `sys_moudle` VALUES (18, '删除', NULL, 'cmsContent/delete', NULL, 12, 0);
INSERT INTO `sys_moudle` VALUES (19, '审核', NULL, 'cmsContent/check', NULL, 12, 0);
INSERT INTO `sys_moudle` VALUES (20, '刷新', NULL, 'cmsContent/refresh', NULL, 12, 0);
INSERT INTO `sys_moudle` VALUES (21, '生成', NULL, 'cmsContent/publish', NULL, 12, 0);
INSERT INTO `sys_moudle` VALUES (22, '移动', 'cmsContent/moveParameters', 'cmsContent/move', NULL, 12, 0);
INSERT INTO `sys_moudle` VALUES (23, '推荐', 'cmsContent/push', 'cmsContent/push_content,cmsContent/push_content_list,cmsContent/push_to_content,cmsContent/push_page,cmsContent/push_page_list,cmsPage/placeDataAdd,cmsPlace/save,cmsContent/related', NULL, 12, 0);
INSERT INTO `sys_moudle` VALUES (24, '分类管理', 'cmsCategory/list', NULL, '<i class=\"icon-folder-open icon-large\"></i>', 3, 0);
INSERT INTO `sys_moudle` VALUES (25, '增加/修改', 'cmsCategory/add', 'cmsCategory/addMore,cmsTemplate/lookup,cmsCategory/categoryPath,cmsCategory/contentPath,file/doUpload,cmsCategory/save', NULL, 24, 0);
INSERT INTO `sys_moudle` VALUES (26, '删除', NULL, 'cmsCategory/delete', NULL, 24, 0);
INSERT INTO `sys_moudle` VALUES (27, '生成', 'cmsCategory/publishParameters', 'cmsCategory/publish', NULL, 24, 0);
INSERT INTO `sys_moudle` VALUES (28, '移动', 'cmsCategory/moveParameters', 'cmsCategory/move,cmsCategory/lookup', NULL, 24, 0);
INSERT INTO `sys_moudle` VALUES (29, '推荐', 'cmsCategory/push_page', 'cmsCategory/push_page_list,cmsPage/placeDataAdd,cmsPlace/save', NULL, 24, 0);
INSERT INTO `sys_moudle` VALUES (30, '页面管理', 'cmsPage/placeList', 'sysUser/lookup,sysUser/lookup_content_list,cmsPage/placeDataList,cmsPage/placeDataAdd,cmsPlace/save,cmsTemplate/publishPlace,cmsPage/publishPlace,cmsPage/push_page,cmsPage/push_page_list', '<i class=\"icon-globe icon-large\"></i>', 4, 0);
INSERT INTO `sys_moudle` VALUES (31, '分类扩展', NULL, NULL, '<i class=\"icon-road icon-large\"></i>', 3, 0);
INSERT INTO `sys_moudle` VALUES (32, '分类类型', 'cmsCategoryType/list', NULL, '<i class=\"icon-road icon-large\"></i>', 31, 0);
INSERT INTO `sys_moudle` VALUES (33, '标签分类', 'cmsTagType/list', NULL, '<i class=\"icon-tags icon-large\"></i>', 31, 0);
INSERT INTO `sys_moudle` VALUES (34, '增加/修改', 'cmsTagType/add', 'cmsTagType/save', NULL, 33, 0);
INSERT INTO `sys_moudle` VALUES (35, '删除', NULL, 'cmsTagType/delete', NULL, 33, 0);
INSERT INTO `sys_moudle` VALUES (36, '增加/修改', 'cmsCategoryType/add', 'cmsCategoryType/save', NULL, 32, 0);
INSERT INTO `sys_moudle` VALUES (37, '删除', NULL, 'cmsCategoryType/delete', NULL, 32, 0);
INSERT INTO `sys_moudle` VALUES (38, '模板管理', NULL, NULL, '<i class=\"icon-code icon-large\"></i>', 5, 0);
INSERT INTO `sys_moudle` VALUES (39, '页面模板', 'cmsTemplate/list', 'cmsTemplate/directory', '<i class=\"icon-globe icon-large\"></i>', 38, 0);
INSERT INTO `sys_moudle` VALUES (40, '修改模板元数据', 'cmsTemplate/metadata', 'cmsTemplate/saveMetadata', NULL, 39, 0);
INSERT INTO `sys_moudle` VALUES (41, '修改模板', 'cmsTemplate/content', 'cmsTemplate/save,cmsTemplate/chipLookup', NULL, 39, 0);
INSERT INTO `sys_moudle` VALUES (42, '修改推荐位', 'cmsTemplate/placeList', 'cmsTemplate/placeMetadata,cmsTemplate/placeContent,cmsTemplate/placeForm,cmsTemplate/saveMetadata,cmsTemplate/createPlace', NULL, 39, 0);
INSERT INTO `sys_moudle` VALUES (43, '删除模板', NULL, 'cmsTemplate/delete', NULL, 39, 0);
INSERT INTO `sys_moudle` VALUES (44, '搜索词管理', 'cmsWord/list', NULL, '<i class=\"icon-search icon-large\"></i>', 13, 0);
INSERT INTO `sys_moudle` VALUES (47, '生成页面', NULL, 'cmsTemplate/publish', NULL, 30, 0);
INSERT INTO `sys_moudle` VALUES (48, '保存页面元数据', '', 'cmsPage/saveMetaData,file/doUpload,cmsPage/clearCache', NULL, 30, 0);
INSERT INTO `sys_moudle` VALUES (49, '增加/修改推荐位数据', 'cmsPage/placeDataAdd', 'cmsContent/lookup,cmsPage/lookup_content_list,file/doUpload,cmsPlace/save', NULL, 30, 0);
INSERT INTO `sys_moudle` VALUES (50, '删除推荐位数据', NULL, 'cmsPlace/delete', NULL, 30, 0);
INSERT INTO `sys_moudle` VALUES (51, '刷新推荐位数据', NULL, 'cmsPlace/refresh', NULL, 30, 0);
INSERT INTO `sys_moudle` VALUES (52, '审核推荐位数据', NULL, 'cmsPlace/check', NULL, 30, 0);
INSERT INTO `sys_moudle` VALUES (53, '发布推荐位', NULL, 'cmsTemplate/publishPlace', NULL, 30, 0);
INSERT INTO `sys_moudle` VALUES (54, '清空推荐位数据', NULL, 'cmsPlace/clear', NULL, 30, 0);
INSERT INTO `sys_moudle` VALUES (60, '文件上传日志', 'log/upload', 'sysUser/lookup', '<i class=\"icon-list-alt icon-large\"></i>', 63, 0);
INSERT INTO `sys_moudle` VALUES (61, '用户管理', NULL, NULL, '<i class=\"icon-user icon-large\"></i>', 5, 0);
INSERT INTO `sys_moudle` VALUES (62, '系统维护', NULL, NULL, '<i class=\"icon-cogs icon-large\"></i>', 5, 0);
INSERT INTO `sys_moudle` VALUES (63, '日志管理', NULL, NULL, '<i class=\"icon-list-alt icon-large\"></i>', 5, 0);
INSERT INTO `sys_moudle` VALUES (64, '操作日志', 'log/operate', 'sysUser/lookup', '<i class=\"icon-list-alt icon-large\"></i>', 63, 0);
INSERT INTO `sys_moudle` VALUES (65, '登录日志', 'log/login', 'sysUser/lookup', '<i class=\"icon-signin icon-large\"></i>', 63, 0);
INSERT INTO `sys_moudle` VALUES (66, '任务计划日志', 'log/task', 'sysUser/lookup', '<i class=\"icon-time icon-large\"></i>', 63, 0);
INSERT INTO `sys_moudle` VALUES (67, '删除', NULL, 'logOperate/delete', NULL, 64, 0);
INSERT INTO `sys_moudle` VALUES (68, '删除', NULL, 'logLogin/delete', NULL, 65, 0);
INSERT INTO `sys_moudle` VALUES (69, '删除', NULL, 'logTask/delete', NULL, 66, 0);
INSERT INTO `sys_moudle` VALUES (70, '查看', 'log/taskView', NULL, NULL, 66, 0);
INSERT INTO `sys_moudle` VALUES (71, '用户管理', 'sysUser/list', NULL, '<i class=\"icon-user icon-large\"></i>', 61, 0);
INSERT INTO `sys_moudle` VALUES (72, '部门管理', 'sysDept/list', 'sysDept/lookup,sysUser/lookup', '<i class=\"icon-group icon-large\"></i>', 61, 0);
INSERT INTO `sys_moudle` VALUES (73, '角色管理', 'sysRole/list', NULL, '<i class=\"icon-user-md icon-large\"></i>', 61, 0);
INSERT INTO `sys_moudle` VALUES (74, '增加/修改', 'sysUser/add', 'sysDept/lookup,sysUser/save', NULL, 71, 0);
INSERT INTO `sys_moudle` VALUES (75, '启用', NULL, 'sysUser/enable', NULL, 71, 0);
INSERT INTO `sys_moudle` VALUES (76, '禁用', NULL, 'sysUser/disable', NULL, 71, 0);
INSERT INTO `sys_moudle` VALUES (77, '增加/修改', 'sysDept/add', 'sysDept/lookup,sysUser/lookup,sysDept/save', NULL, 72, 0);
INSERT INTO `sys_moudle` VALUES (78, '删除', NULL, 'sysDept/delete', NULL, 72, 0);
INSERT INTO `sys_moudle` VALUES (79, '增加/修改', 'sysRole/add', 'sysRole/save', NULL, 73, 0);
INSERT INTO `sys_moudle` VALUES (80, '删除', NULL, 'sysRole/delete', NULL, 73, 0);
INSERT INTO `sys_moudle` VALUES (81, '内容模型管理', 'cmsModel/list', NULL, '<i class=\"icon-th-large icon-large\"></i>', 62, 0);
INSERT INTO `sys_moudle` VALUES (82, '任务计划', 'sysTask/list', NULL, '<i class=\"icon-time icon-large\"></i>', 62, 0);
INSERT INTO `sys_moudle` VALUES (83, 'FTP用户', 'cmsFtpUser/list', NULL, '<i class=\"icon-folder-open-alt icon-large\"></i>', 62, 0);
INSERT INTO `sys_moudle` VALUES (84, '动态域名', 'cmsDomain/list', NULL, '<i class=\"icon-qrcode icon-large\"></i>', 62, 0);
INSERT INTO `sys_moudle` VALUES (85, '任务计划脚本', 'taskTemplate/list', NULL, '<i class=\"icon-time icon-large\"></i>', 38, 0);
INSERT INTO `sys_moudle` VALUES (86, '修改脚本', 'taskTemplate/metadata', 'cmsTemplate/saveMetadata,taskTemplate/content,cmsTemplate/save,taskTemplate/chipLookup', NULL, 85, 0);
INSERT INTO `sys_moudle` VALUES (87, '删除脚本', NULL, 'cmsTemplate/delete', NULL, 85, 0);
INSERT INTO `sys_moudle` VALUES (88, '用户登录授权', 'sysUserToken/list', 'sysUser/lookup', '<i class=\"icon-unlock-alt icon-large\"></i>', 61, 0);
INSERT INTO `sys_moudle` VALUES (89, '删除', NULL, 'sysUserToken/delete', NULL, 88, 0);
INSERT INTO `sys_moudle` VALUES (90, '增加/修改', 'cmsModel/add', 'cmsModel/save,cmsTemplate/lookup', NULL, 81, 0);
INSERT INTO `sys_moudle` VALUES (91, '删除', NULL, 'cmsModel/delete', NULL, 81, 0);
INSERT INTO `sys_moudle` VALUES (92, '增加/修改', 'sysTask/add', 'sysTask/save,sysTask/example,taskTemplate/lookup', NULL, 82, 0);
INSERT INTO `sys_moudle` VALUES (93, '删除', NULL, 'sysTask/delete', NULL, 82, 0);
INSERT INTO `sys_moudle` VALUES (94, '立刻执行', NULL, 'sysTask/runOnce', NULL, 82, 0);
INSERT INTO `sys_moudle` VALUES (95, '暂停', NULL, 'sysTask/pause', NULL, 82, 0);
INSERT INTO `sys_moudle` VALUES (96, '恢复', NULL, 'sysTask/resume', NULL, 82, 0);
INSERT INTO `sys_moudle` VALUES (97, '重新初始化', NULL, 'sysTask/recreate', NULL, 82, 0);
INSERT INTO `sys_moudle` VALUES (98, '增加/修改', 'cmsFtpUser/add', 'cmsFtpUser/save', NULL, 83, 0);
INSERT INTO `sys_moudle` VALUES (99, '删除', NULL, 'cmsFtpUser/delete', NULL, 83, 0);
INSERT INTO `sys_moudle` VALUES (100, '修改', 'cmsDomain/add', 'cmsDomain/save,cmsTemplate/directoryLookup,cmsTemplate/lookup', NULL, 84, 0);
INSERT INTO `sys_moudle` VALUES (101, '配置中心', 'sysConfig/list', 'sysConfig/subcode', '<i class=\"icon-cogs icon-large\"></i>', 62, 0);
INSERT INTO `sys_moudle` VALUES (102, '修改', 'cmsContent/add', 'cmsContent/addMore,file/doUpload,cmsContent/lookup,cmsContent/lookup_list,cmsContent/save,ueditor', NULL, 8, 0);
INSERT INTO `sys_moudle` VALUES (103, '删除', NULL, 'cmsContent/delete', NULL, 8, 0);
INSERT INTO `sys_moudle` VALUES (104, '刷新', NULL, 'cmsContent/refresh', NULL, 8, 0);
INSERT INTO `sys_moudle` VALUES (105, '生成', NULL, 'cmsContent/publish', NULL, 8, 0);
INSERT INTO `sys_moudle` VALUES (106, '推荐', 'cmsContent/push', 'cmsContent/push_content,cmsContent/push_content_list,cmsContent/push_to_content,cmsContent/push_page,cmsContent/push_page_list,cmsContent/push_to_place,cmsContent/related', NULL, 8, 0);
INSERT INTO `sys_moudle` VALUES (107, '推荐位数据列表', 'cmsPage/placeDataList', NULL, NULL, 29, 0);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` int(11) NOT NULL auto_increment,
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `owns_all_right` tinyint(1) NOT NULL COMMENT '拥有全部权限',
  `show_all_moudle` tinyint(1) NOT NULL COMMENT '显示全部模块',
  PRIMARY KEY  (`id`),
  KEY `site_id` (`site_id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='角色';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '1', '超级管理员', '1', '0');
INSERT INTO `sys_role` VALUES ('2', '1', '测试管理员', '0', '1');
INSERT INTO `sys_role` VALUES ('3', '2', '站长', '1', '0');

-- ----------------------------
-- Table structure for sys_role_authorized
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_authorized`;
CREATE TABLE `sys_role_authorized` (
  `id` int(11) NOT NULL auto_increment,
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `url` varchar(255) NOT NULL COMMENT '授权地址',
  PRIMARY KEY  (`id`),
  KEY `role_id` (`role_id`),
  KEY `url` (`url`)
) ENGINE=MyISAM AUTO_INCREMENT=56 DEFAULT CHARSET=utf8 COMMENT='角色授权地址';

-- ----------------------------
-- Records of sys_role_authorized
-- ----------------------------
INSERT INTO `sys_role_authorized` VALUES ('1', '2', 'taskTemplate/metadata');
INSERT INTO `sys_role_authorized` VALUES ('2', '2', 'sysRole/add');
INSERT INTO `sys_role_authorized` VALUES ('3', '2', 'myself/contentList');
INSERT INTO `sys_role_authorized` VALUES ('5', '2', 'cmsTag/list');
INSERT INTO `sys_role_authorized` VALUES ('6', '2', 'myself/userTokenList');
INSERT INTO `sys_role_authorized` VALUES ('7', '2', 'sysTask/list');
INSERT INTO `sys_role_authorized` VALUES ('8', '2', 'cmsTagType/list');
INSERT INTO `sys_role_authorized` VALUES ('9', '2', 'log/login');
INSERT INTO `sys_role_authorized` VALUES ('10', '2', 'cmsCategoryType/add');
INSERT INTO `sys_role_authorized` VALUES ('11', '2', 'sysDept/list');
INSERT INTO `sys_role_authorized` VALUES ('12', '2', 'sysUserToken/list');
INSERT INTO `sys_role_authorized` VALUES ('13', '2', 'cmsTag/add');
INSERT INTO `sys_role_authorized` VALUES ('15', '2', 'sysUser/list');
INSERT INTO `sys_role_authorized` VALUES ('16', '2', 'cmsPage/placeDataAdd');
INSERT INTO `sys_role_authorized` VALUES ('18', '2', 'cmsContent/moveParameters');
INSERT INTO `sys_role_authorized` VALUES ('19', '2', 'cmsCategory/moveParameters');
INSERT INTO `sys_role_authorized` VALUES ('20', '2', 'sysUser/add');
INSERT INTO `sys_role_authorized` VALUES ('21', '2', 'myself/logLogin');
INSERT INTO `sys_role_authorized` VALUES ('22', '2', 'myself/logOperate');
INSERT INTO `sys_role_authorized` VALUES ('25', '2', 'myself/password');
INSERT INTO `sys_role_authorized` VALUES ('26', '2', 'cmsCategory/list');
INSERT INTO `sys_role_authorized` VALUES ('28', '2', 'log/operate');
INSERT INTO `sys_role_authorized` VALUES ('29', '2', 'cmsCategory/publishParameters');
INSERT INTO `sys_role_authorized` VALUES ('30', '2', 'cmsCategory/add');
INSERT INTO `sys_role_authorized` VALUES ('31', '2', 'cmsDomain/list');
INSERT INTO `sys_role_authorized` VALUES ('34', '2', 'sysRole/list');
INSERT INTO `sys_role_authorized` VALUES ('35', '2', 'cmsModel/list');
INSERT INTO `sys_role_authorized` VALUES ('36', '2', 'cmsFtpUser/add');
INSERT INTO `sys_role_authorized` VALUES ('37', '2', 'sysTask/add');
INSERT INTO `sys_role_authorized` VALUES ('38', '2', 'cmsDomain/add');
INSERT INTO `sys_role_authorized` VALUES ('39', '2', 'cmsTagType/add');
INSERT INTO `sys_role_authorized` VALUES ('40', '2', 'sysDept/add');
INSERT INTO `sys_role_authorized` VALUES ('41', '2', 'cmsFtpUser/list');
INSERT INTO `sys_role_authorized` VALUES ('42', '2', 'cmsContent/list');
INSERT INTO `sys_role_authorized` VALUES ('44', '2', 'log/taskView');
INSERT INTO `sys_role_authorized` VALUES ('45', '2', 'cmsCategoryType/list');
INSERT INTO `sys_role_authorized` VALUES ('46', '2', 'cmsModel/add');
INSERT INTO `sys_role_authorized` VALUES ('47', '2', 'cmsContent/add');
INSERT INTO `sys_role_authorized` VALUES ('48', '2', 'taskTemplate/list');
INSERT INTO `sys_role_authorized` VALUES ('49', '2', 'cmsContent/push');
INSERT INTO `sys_role_authorized` VALUES ('50', '2', 'log/task');
INSERT INTO `sys_role_authorized` VALUES ('51', '2', 'cmsPage/placeDataList');
INSERT INTO `sys_role_authorized` VALUES ('52', '2', 'cmsTemplate/metadata');
INSERT INTO `sys_role_authorized` VALUES ('53', '2', 'cmsPage/placeList');
INSERT INTO `sys_role_authorized` VALUES ('54', '2', 'cmsTemplate/list');
INSERT INTO `sys_role_authorized` VALUES ('55', '2', 'cmsTemplate/placeList');

-- ----------------------------
-- Table structure for sys_role_moudle
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_moudle`;
CREATE TABLE `sys_role_moudle` (
  `id` int(11) NOT NULL auto_increment,
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `moudle_id` int(11) NOT NULL COMMENT '模块ID',
  PRIMARY KEY  (`id`),
  KEY `role_id` (`role_id`),
  KEY `moudle_id` (`moudle_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='角色授权模块';

-- ----------------------------
-- Records of sys_role_moudle
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_user`;
CREATE TABLE `sys_role_user` (
  `id` int(11) NOT NULL auto_increment,
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  PRIMARY KEY  (`id`),
  KEY `role_id` (`role_id`),
  KEY `user_id` (`user_id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='用户角色';

-- ----------------------------
-- Records of sys_role_user
-- ----------------------------
INSERT INTO `sys_role_user` VALUES ('1', '1', '1');
INSERT INTO `sys_role_user` VALUES ('2', '2', '2');
INSERT INTO `sys_role_user` VALUES ('3', '3', '3');

-- ----------------------------
-- Table structure for sys_site
-- ----------------------------
DROP TABLE IF EXISTS `sys_site`;
CREATE TABLE `sys_site` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(50) NOT NULL,
  `use_static` tinyint(1) NOT NULL COMMENT '启用静态化',
  `site_path` varchar(255) NOT NULL COMMENT '站点地址',
  `use_ssi` tinyint(1) NOT NULL COMMENT '启用服务器端包含',
  `dynamic_path` varchar(255) NOT NULL COMMENT '动态站点地址',
  `resource_path` varchar(255) NOT NULL COMMENT '资源站点地址',
  `disabled` tinyint(1) NOT NULL COMMENT '禁用',
  PRIMARY KEY  (`id`),
  KEY `disabled` (`disabled`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='站点';

-- ----------------------------
-- Records of sys_site
-- ----------------------------
INSERT INTO `sys_site` VALUES ('1', 'PublicCMS', '1', '//www.dev.publiccms.com/', '0', '//dev.publiccms.com/', '//image.publiccms.com/', '0');
INSERT INTO `sys_site` VALUES ('2', '演示站点1', '0', '//site2.dev.publiccms.com/', '0', 'site2.dev.publiccms.com', '//resource.site2.dev.publiccms.com/', '0');

-- ----------------------------
-- Table structure for sys_task
-- ----------------------------
DROP TABLE IF EXISTS `sys_task`;
CREATE TABLE `sys_task` (
  `id` int(11) NOT NULL auto_increment,
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `status` int(11) NOT NULL COMMENT '状态',
  `cron_expression` varchar(50) NOT NULL COMMENT '表达式',
  `description` varchar(300) default NULL COMMENT '描述',
  `file_path` varchar(255) default NULL COMMENT '文件路径',
  `update_date` datetime default NULL COMMENT '更新时间',
  PRIMARY KEY  (`id`),
  KEY `status` (`status`),
  KEY `site_id` (`site_id`),
  KEY `update_date` (`update_date`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='任务计划';

-- ----------------------------
-- Records of sys_task
-- ----------------------------
INSERT INTO `sys_task` VALUES ('1', '1', '重新生成所有页面', '0', '0 0/2 * * ?', '重新生成所有页面', '/publishPage.task', NULL);
INSERT INTO `sys_task` VALUES ('2', '1', '重建索引', '0', '0 0 1 1 ? 2099', '重建全部索引', '/reCreateIndex.task', NULL);
INSERT INTO `sys_task` VALUES ('3', '1', '清理日志', '0', '0 0 1 * ?', '清理三个月以前的日志', '/clearLog.task', NULL);
INSERT INTO `sys_task` VALUES ('4', '1', '重新生成内容页面', '0', '0 0 1 1 ? 2099', '重新生成内容页面', '/publishContent.task', NULL);
INSERT INTO `sys_task` VALUES ('5', '1', '重新生成所有分类页面', '0', '0 0/6 * * ?', '重新生成所有分类页面', '/publishCategory.task', NULL);
INSERT INTO `sys_task` VALUES ('7', '1', '重新生成全站', '0', '0 0 1 1 ? 2099', '重新生成全站', '/publishAll.task', NULL);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL auto_increment,
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `name` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(32) NOT NULL COMMENT '密码',
  `nick_name` varchar(45) NOT NULL COMMENT '昵称',
  `dept_id` int(11) default NULL COMMENT '部门',
  `roles` text COMMENT '角色',
  `email` varchar(100) default NULL COMMENT '邮箱地址',
  `email_checked` tinyint(1) NOT NULL COMMENT '已验证邮箱',
  `superuser_access` tinyint(1) NOT NULL COMMENT '是否管理员',
  `disabled` tinyint(1) NOT NULL COMMENT '是否禁用',
  `last_login_date` datetime default NULL COMMENT '最后登录日期',
  `last_login_ip` varchar(20) default NULL COMMENT '最后登录ip',
  `login_count` int(11) NOT NULL COMMENT '登录次数',
  `registered_date` datetime default NULL COMMENT '注册日期',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `name` (`name`,`site_id`),
  UNIQUE KEY `nick_name` (`nick_name`,`site_id`),
  KEY `email` (`email`),
  KEY `disabled` (`disabled`),
  KEY `lastLoginDate` (`last_login_date`),
  KEY `email_checked` (`email_checked`),
  KEY `dept_id` (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='用户';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', '1', 'admin', '21232f297a57a5a743894a0e4a801fc3', '管理员', '1', '1', 'master@sanluan.com', '0', '1', '0', '2016-04-05 14:41:23', '127.0.0.1', '48', '2016-03-22 00:00:00');
INSERT INTO `sys_user` VALUES ('2', '1', 'test', '098f6bcd4621d373cade4e832627b4f6', '演示账号', '1', '2', 'test@test.com', '0', '1', '0', '2016-03-24 18:20:41', '112.23.82.255', '5455', '2016-03-22 00:00:00');
INSERT INTO `sys_user` VALUES ('3', '2', 'admin', '21232f297a57a5a743894a0e4a801fc3', 'admin', '2', '3', '', '0', '1', '0', '2016-03-23 11:51:11', '106.2.199.138', '6', '2016-03-22 17:42:26');


-- ----------------------------
-- Table structure for sys_user_token
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_token`;
CREATE TABLE `sys_user_token` (
  `auth_token` varchar(40) NOT NULL COMMENT '登陆授权',
  `site_id` int(11) NOT NULL COMMENT '站点ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `channel` varchar(50) NOT NULL COMMENT '渠道',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `login_ip` varchar(20) NOT NULL COMMENT '登陆IP',
  PRIMARY KEY  (`auth_token`),
  KEY `user_id` (`user_id`),
  KEY `create_date` (`create_date`),
  KEY `channel` (`channel`),
  KEY `site_id` (`site_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='用户令牌';

-- ----------------------------
-- Records of sys_user_token
-- ----------------------------
