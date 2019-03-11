-- ----------------------------
-- Records of cms_category
-- ----------------------------
INSERT INTO `cms_category` VALUES ('1', '1', '演示', null, null, '17,15,12,9,8,7,6,18', '', 'demonstrate', '/category/parent.html', '${category.code}/index.html', '0', '1', '//www.publiccms.com/demonstrate/index.html', '${content.publishDate?string(\'yyyy/MM/dd\')}/${content.id}.html', '1', '10', '0', '0', '0', '0', null);
INSERT INTO `cms_category` VALUES ('6', '1', '汽车', '1', null, null, '', 'car', '/category/list.html', '${category.code}/index.html', '0', '1', '//www.publiccms.com/car/index.html', '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', '1', '10', '0', '0', '0', '0', null);
INSERT INTO `cms_category` VALUES ('7', '1', '社会', '1', null, null, '', 'social', '/category/list.html', '${category.code}/index.html', '0', '1', '//www.publiccms.com/social/index.html', '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', '1', '10', '0', '0', '0', '0', null);
INSERT INTO `cms_category` VALUES ('8', '1', '美图', '1', null, null, '', 'picture', '/category/list.html', '${category.code}/index.html', '0', '1', '//www.publiccms.com/picture/index.html', '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', '1', '10', '0', '0', '0', '0', null);
INSERT INTO `cms_category` VALUES ('9', '1', '系统介绍', '1', null, null, '', 'introduction', '/category/list.html', '${category.code}/index.html', '0', '1', '//www.publiccms.com/introduction/index.html', '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', '1', '10', '0', '0', '0', '0', null);
INSERT INTO `cms_category` VALUES ('12', '1', '文章', '1', null, null, '', 'article', '/category/list.html', '${category.code}/index.html', '0', '1', '//www.publiccms.com/article/index.html', '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', '1', '20', '0', '0', '0', '0', null);
INSERT INTO `cms_category` VALUES ('11', '1', '测试', null, null, null, null, 'test', '/category/parent.html', '${category.code}/index.html', '0', '0', 'test/index.html', '${content.publishDate?string(\'yyyy/MM/dd\')}/${content.id}.html', '1', '20', '0', '0', '0', '1', null);
INSERT INTO `cms_category` VALUES ('13', '1', '下载', null, null, null, null, 'download', '', 'https://github.com/sanluan/PublicCMS', '0',  '0', 'https://github.com/sanluan/PublicCMS', '', '1', '20', '0', '0', '0', '1', null);
INSERT INTO `cms_category` VALUES ('14', '1', '图书', '1', null, null, null, 'book', '/category/parent.html', 'demonstrate/${category.code}/index.html', '0', '0', 'demonstrate/book/index.html', '${content.publishDate?string(\'yyyy/MM/dd\')}/${content.id}.html', '1', '20', '0', '0', '0', '1', null);
INSERT INTO `cms_category` VALUES ('15', '1', '小说', '1', null, null, '', 'novel', '/category/list.html', '${category.code}/index.html', '0', '1', '//www.publiccms.com/novel/index.html', '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', '1', '20', '0', '0', '0', '0', null);
INSERT INTO `cms_category` VALUES ('16', '1', 'OSChina下载', '13', null, null, null, 'download', '', 'http://git.oschina.net/sanluan/PublicCMS', '0', '0', 'http://git.oschina.net/sanluan/PublicCMS', '', '1', '20', '0', '0', '0', '1', null);
INSERT INTO `cms_category` VALUES ('17', '1', '科技', '1', null, null, '', 'science', '/category/list.html', '${category.code}/index.html', '0', '1', '//www.publiccms.com/science/index.html', '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', '1', '20', '0', '0', '0', '0', null);
INSERT INTO `cms_category` VALUES ('19', '1', '案例', null, null, null, '', 'case', '/category/parent.html', '${category.code}/index.html', '0', '1', '//www.publiccms.com/case/index.html', '${content.publishDate?string(\'yyyy/MM/dd\')}/${content.id}.html', '1', '20', '0', '0', '0', '0', null);
INSERT INTO `cms_category` VALUES (71, 2, '中文栏目', NULL, NULL, '73,75,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,120', '', 'zh', NULL, '#', 1, 0, '#', '', 0, 20, 0, 0, 1, 0, null);
INSERT INTO `cms_category` VALUES (72, 2, '英文栏目', NULL, NULL, '74,76,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,121', '', 'en', NULL, '#', 1, 0, '#', '', 0, 20, 0, 0, 1, 0, null);
INSERT INTO `cms_category` VALUES (73, 2, '集团简介', 71, NULL, '75,77,78,79,80,81', '', 'about', NULL, '<@_categoryList parentId=category.id><#if page.totalCount gt 0>category/${page.list[0].id}</#if></@_categoryList>', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/category/75', '', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (74, 2, 'Introduction', 72, NULL, '76,99,100,101,102,103', '', 'introduction', NULL, '<@_categoryList parentId=category.id><#if page.totalCount gt 0>en/category/${page.list[0].id}</#if></@_categoryList>', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/en/category/76', '', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (75, 2, '集团简介', 73, 1, NULL, '', 'about', NULL, 'category/${category.id}', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/category/75', 'content/${content.id}', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (76, 2, 'Introduction', 74, 1, NULL, '', 'about', NULL, 'en/category/${category.id}', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/en/category/76', 'en/content/${content.id}', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (77, 2, '核心团队', 73, 1, NULL, '', 'tuandui', NULL, 'category/${category.id}', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/category/77', '', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (78, 2, '组织架构', 73, 1, NULL, '', 'jiagou', NULL, 'category/${category.id}', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/category/78', '', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (79, 2, '集团荣誉', 73, 1, NULL, '', 'rongyu', NULL, 'category/${category.id}', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/category/79', '', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (80, 2, '社会责任', 73, 1, NULL, '', 'zeren', NULL, 'category/${category.id}', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/category/80', '', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (81, 2, '发展目标', 73, 1, NULL, '', 'mubiao', NULL, 'category/${category.id}', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/category/81', '', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (82, 2, '新闻资讯', 71, NULL, '83,84', '', 'news', NULL, '<@_categoryList parentId=category.id><#if page.totalCount gt 0>news/${page.list[0].id}</#if></@_categoryList>', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/news/83', '', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (83, 2, '行业资讯', 82, NULL, NULL, '', 'hangye', NULL, 'news/${category.id}', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/news/83', 'content/${content.id}', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (84, 2, '公司新闻', 82, NULL, NULL, '', 'gongsi', NULL, 'news/${category.id}', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/news/84', 'content/${content.id}', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (85, 2, '集团产业', 71, NULL, '86,87,88,89,90,91', '', 'industry', NULL, '<@_categoryList parentId=category.id><#if page.totalCount gt 0>category/${page.list[0].id}</#if></@_categoryList>', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/category/86', '', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (86, 2, '投资管理', 85, 1, NULL, '', 'touzi', NULL, 'category/${category.id}', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/category/86', '', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (87, 2, '置业建设', 85, 1, NULL, '', 'jianshe', NULL, 'category/${category.id}', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/category/87', '', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (88, 2, '能源光电', 85, 1, NULL, '', 'nengyuan', NULL, 'category/${category.id}', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/category/88', '', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (89, 2, '现代物流', 85, 1, NULL, '', 'wuliu', NULL, 'category/${category.id}', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/category/89', '', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (90, 2, '生物制药', 85, 1, NULL, '', 'zhiyao', NULL, 'category/${category.id}', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/category/90', '', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (91, 2, '现代农业', 85, 1, NULL, '', 'nongye', NULL, 'category/${category.id}', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/category/91', '', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (92, 2, '人才中心', 71, NULL, '93,94,95,96', '', 'rencai', NULL, '<@_categoryList parentId=category.id><#if page.totalCount gt 0>news/${page.list[0].id}</#if></@_categoryList>', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/news/93', '', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (93, 2, '人才招聘', 92, NULL, NULL, '', 'zhaopin', NULL, 'news/${category.id}', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/news/93', 'content/${content.id}', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (94, 2, '人才理念', 92, 1, NULL, '', 'linian', NULL, 'category/${category.id}', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/category/94', '', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (95, 2, '薪酬福利', 92, 1, NULL, '', 'fuli', NULL, 'category/${category.id}', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/category/95', '', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (96, 2, '职业发展', 92, 1, NULL, '', 'fazhan', NULL, 'category/${category.id}', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/category/96', '', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (97, 2, '联系我们', 71, NULL, '120', '', 'linaxi', NULL, '<@_categoryList parentId=category.id><#if page.totalCount gt 0>category/${page.list[0].id}</#if></@_categoryList>', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/category/120', '', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (98, 2, '联系我们', 97, 1, NULL, '', 'lianxi', NULL, 'category/${category.id}', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/category/98', '', 0, 20, 0, 0, 0, 1, null);
INSERT INTO `cms_category` VALUES (99, 2, 'Core team', 74, 1, NULL, '', 'team', NULL, 'en/category/${category.id}', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/en/category/99', '', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (100, 2, 'Organization', 74, 1, NULL, '', 'organization', NULL, 'en/category/${category.id}', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/en/category/100', '', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (101, 2, 'Group honor', 74, 1, NULL, '', 'honor', NULL, 'en/category/${category.id}', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/en/category/101', '', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (102, 2, 'Social responsibility', 74, 1, NULL, '', 'responsibility', NULL, 'en/category/${category.id}', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/en/category/102', '', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (103, 2, 'Development goals', 74, 1, NULL, '', 'goals', NULL, 'en/category/${category.id}', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/en/category/103', '', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (104, 2, 'News', 72, NULL, '105,106', '', 'news', NULL, '<@_categoryList parentId=category.id><#if page.totalCount gt 0>en/news/${page.list[0].id}</#if></@_categoryList>', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/en/news/105', '', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (105, 2, 'Industry News', 104, NULL, NULL, '', 'industry', NULL, 'en/news/${category.id}', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/en/news/105', '', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (106, 2, 'Company news', 104, NULL, NULL, '', 'company', NULL, 'en/news/${category.id}', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/en/news/106', '', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (107, 2, 'Industry', 72, NULL, '108,109,110,111,112,113', '', 'group_industry', NULL, '<@_categoryList parentId=category.id><#if page.totalCount gt 0>en/category/${page.list[0].id}</#if></@_categoryList>', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/en/category/108', '', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (108, 2, 'Investment Management', 107, 1, NULL, '', 'investment', NULL, 'en/category/${category.id}', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/en/category/108', '', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (109, 2, 'Home ownership', 107, 1, NULL, '', 'ownership', NULL, 'en/category/${category.id}', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/en/category/109', '', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (110, 2, 'Energy photoelectric', 107, 1, NULL, '', 'photoelectric', NULL, 'en/category/${category.id}', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/en/category/110', '', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (111, 2, 'Modern logistics', 107, 1, NULL, '', 'logistics', NULL, 'en/category/${category.id}', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/en/category/111', '', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (112, 2, 'Biopharmaceutical', 107, 1, NULL, '', 'biopharmaceutical', NULL, 'en/category/${category.id}', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/en/category/112', '', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (113, 2, 'Modern agriculture', 107, 1, NULL, '', 'agriculture', NULL, 'en/category/${category.id}', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/en/category/113', '', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (114, 2, 'Talent', 72, NULL, '115,116,117,118', '', 'talent', NULL, '<@_categoryList parentId=category.id><#if page.totalCount gt 0>en/news/${page.list[0].id}</#if></@_categoryList>', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/en/news/115', '', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (115, 2, 'Recruitment', 114, NULL, NULL, '', 'recruitment', NULL, 'en/news/${category.id}', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/en/news/115', '', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (116, 2, 'Talent Concept', 114, 1, NULL, '', 'concept', NULL, 'en/category/${category.id}', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/en/category/116', '', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (117, 2, 'Remuneration and benefits', 114, 1, NULL, '', 'benefits', NULL, 'en/category/${category.id}', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/en/category/117', '', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (118, 2, 'Career Development', 114, 1, NULL, '', 'career', NULL, 'en/category/${category.id}', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/en/category/118', '', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (119, 2, 'Contact', 72, NULL, '121', '', 'contact_us', NULL, '<@_categoryList parentId=category.id><#if page.totalCount gt 0>en/category/${page.list[0].id}</#if></@_categoryList>', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/en/category/121', '', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (120, 2, '联系我们', 97, 1, NULL, '', 'contact_us', NULL, 'category/${category.id}', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/category/120', '', 0, 20, 0, 0, 0, 0, null);
INSERT INTO `cms_category` VALUES (121, 2, 'Contact us', 119, 1, NULL, '', 'contact_us', NULL, 'en/category/${category.id}', 0, 0, '//site2.dev.publiccms.com:8080/publiccms/en/category/121', '', 0, 20, 0, 0, 0, 0, null);

-- ----------------------------
-- Records of cms_category_attribute
-- ----------------------------
INSERT INTO `cms_category_attribute` VALUES ('1', '演示', 'PublicCMS,如何使用', 'PublicCMS如何使用', null);
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
INSERT INTO `cms_category_attribute` VALUES ('19', '案例', 'PublicCMS案例', 'PublicCMS案例', null);
INSERT INTO `cms_category_attribute` VALUES (71, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (72, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (73, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (74, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (75, '', '', '', '{\"article\":\"<p>阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (76, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (77, '', '', '', '{\"article\":\"<p>核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (78, '', '', '', '{\"article\":\"<p>组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (79, '', '', '', '{\"article\":\"<h2 class=\\\"R-h2\\\" style=\\\"padding: 0px 0px 0px 15px; margin: 0px; clear: both; overflow: hidden; height: 21px; line-height: 21px; border-bottom: 1px solid rgb(51, 51, 153); font-family: ΢���ź�; font-size: 14px; background: url(&quot;../images/r_top.gif&quot;) 2px center no-repeat rgb(255, 255, 255); color: rgb(68, 68, 68); white-space: normal;\\\"><a href=\\\"file:///E:/Users/kerneler/OneDrive/publiccms/html/%E8%93%9D%E8%89%B2%E9%9B%86%E5%9B%A2%E5%85%AC%E5%8F%B8PC%E7%AB%99%E7%82%B9%E9%9D%99%E6%80%81%E9%A1%B5/about_3.html\\\" title=\\\"集团荣誉\\\" style=\\\"padding: 0px; margin: 0px; text-decoration-line: none; font-size: 12px; color: rgb(68, 68, 68);\\\">集团荣誉</a></h2><h2 class=\\\"R-h2\\\" style=\\\"padding: 0px 0px 0px 15px; margin: 0px; clear: both; overflow: hidden; height: 21px; line-height: 21px; border-bottom: 1px solid rgb(51, 51, 153); font-family: ΢���ź�; font-size: 14px; background: url(&quot;../images/r_top.gif&quot;) 2px center no-repeat rgb(255, 255, 255); color: rgb(68, 68, 68); white-space: normal;\\\"><a href=\\\"file:///E:/Users/kerneler/OneDrive/publiccms/html/%E8%93%9D%E8%89%B2%E9%9B%86%E5%9B%A2%E5%85%AC%E5%8F%B8PC%E7%AB%99%E7%82%B9%E9%9D%99%E6%80%81%E9%A1%B5/about_3.html\\\" title=\\\"集团荣誉\\\" style=\\\"padding: 0px; margin: 0px; text-decoration-line: none; font-size: 12px; color: rgb(68, 68, 68);\\\">集团荣誉</a></h2><h2 class=\\\"R-h2\\\" style=\\\"padding: 0px 0px 0px 15px; margin: 0px; clear: both; overflow: hidden; height: 21px; line-height: 21px; border-bottom: 1px solid rgb(51, 51, 153); font-family: ΢���ź�; font-size: 14px; background: url(&quot;../images/r_top.gif&quot;) 2px center no-repeat rgb(255, 255, 255); color: rgb(68, 68, 68); white-space: normal;\\\"><a href=\\\"file:///E:/Users/kerneler/OneDrive/publiccms/html/%E8%93%9D%E8%89%B2%E9%9B%86%E5%9B%A2%E5%85%AC%E5%8F%B8PC%E7%AB%99%E7%82%B9%E9%9D%99%E6%80%81%E9%A1%B5/about_3.html\\\" title=\\\"集团荣誉\\\" style=\\\"padding: 0px; margin: 0px; text-decoration-line: none; font-size: 12px; color: rgb(68, 68, 68);\\\">集团荣誉</a></h2><h2 class=\\\"R-h2\\\" style=\\\"padding: 0px 0px 0px 15px; margin: 0px; clear: both; overflow: hidden; height: 21px; line-height: 21px; border-bottom: 1px solid rgb(51, 51, 153); font-family: ΢���ź�; font-size: 14px; background: url(&quot;../images/r_top.gif&quot;) 2px center no-repeat rgb(255, 255, 255); color: rgb(68, 68, 68); white-space: normal;\\\"><a href=\\\"file:///E:/Users/kerneler/OneDrive/publiccms/html/%E8%93%9D%E8%89%B2%E9%9B%86%E5%9B%A2%E5%85%AC%E5%8F%B8PC%E7%AB%99%E7%82%B9%E9%9D%99%E6%80%81%E9%A1%B5/about_3.html\\\" title=\\\"集团荣誉\\\" style=\\\"padding: 0px; margin: 0px; text-decoration-line: none; font-size: 12px; color: rgb(68, 68, 68);\\\">集团荣誉</a></h2><h2 class=\\\"R-h2\\\" style=\\\"padding: 0px 0px 0px 15px; margin: 0px; clear: both; overflow: hidden; height: 21px; line-height: 21px; border-bottom: 1px solid rgb(51, 51, 153); font-family: ΢���ź�; font-size: 14px; background: url(&quot;../images/r_top.gif&quot;) 2px center no-repeat rgb(255, 255, 255); color: rgb(68, 68, 68); white-space: normal;\\\"><a href=\\\"file:///E:/Users/kerneler/OneDrive/publiccms/html/%E8%93%9D%E8%89%B2%E9%9B%86%E5%9B%A2%E5%85%AC%E5%8F%B8PC%E7%AB%99%E7%82%B9%E9%9D%99%E6%80%81%E9%A1%B5/about_3.html\\\" title=\\\"集团荣誉\\\" style=\\\"padding: 0px; margin: 0px; text-decoration-line: none; font-size: 12px; color: rgb(68, 68, 68);\\\">集团荣誉</a></h2><h2 class=\\\"R-h2\\\" style=\\\"padding: 0px 0px 0px 15px; margin: 0px; clear: both; overflow: hidden; height: 21px; line-height: 21px; border-bottom: 1px solid rgb(51, 51, 153); font-family: ΢���ź�; font-size: 14px; background: url(&quot;../images/r_top.gif&quot;) 2px center no-repeat rgb(255, 255, 255); color: rgb(68, 68, 68); white-space: normal;\\\"><a href=\\\"file:///E:/Users/kerneler/OneDrive/publiccms/html/%E8%93%9D%E8%89%B2%E9%9B%86%E5%9B%A2%E5%85%AC%E5%8F%B8PC%E7%AB%99%E7%82%B9%E9%9D%99%E6%80%81%E9%A1%B5/about_3.html\\\" title=\\\"集团荣誉\\\" style=\\\"padding: 0px; margin: 0px; text-decoration-line: none; font-size: 12px; color: rgb(68, 68, 68);\\\">集团荣誉</a></h2><h2 class=\\\"R-h2\\\" style=\\\"padding: 0px 0px 0px 15px; margin: 0px; clear: both; overflow: hidden; height: 21px; line-height: 21px; border-bottom: 1px solid rgb(51, 51, 153); font-family: ΢���ź�; font-size: 14px; background: url(&quot;../images/r_top.gif&quot;) 2px center no-repeat rgb(255, 255, 255); color: rgb(68, 68, 68); white-space: normal;\\\"><a href=\\\"file:///E:/Users/kerneler/OneDrive/publiccms/html/%E8%93%9D%E8%89%B2%E9%9B%86%E5%9B%A2%E5%85%AC%E5%8F%B8PC%E7%AB%99%E7%82%B9%E9%9D%99%E6%80%81%E9%A1%B5/about_3.html\\\" title=\\\"集团荣誉\\\" style=\\\"padding: 0px; margin: 0px; text-decoration-line: none; font-size: 12px; color: rgb(68, 68, 68);\\\">集团荣誉</a></h2><h2 class=\\\"R-h2\\\" style=\\\"padding: 0px 0px 0px 15px; margin: 0px; clear: both; overflow: hidden; height: 21px; line-height: 21px; border-bottom: 1px solid rgb(51, 51, 153); font-family: ΢���ź�; font-size: 14px; background: url(&quot;../images/r_top.gif&quot;) 2px center no-repeat rgb(255, 255, 255); color: rgb(68, 68, 68); white-space: normal;\\\"><a href=\\\"file:///E:/Users/kerneler/OneDrive/publiccms/html/%E8%93%9D%E8%89%B2%E9%9B%86%E5%9B%A2%E5%85%AC%E5%8F%B8PC%E7%AB%99%E7%82%B9%E9%9D%99%E6%80%81%E9%A1%B5/about_3.html\\\" title=\\\"集团荣誉\\\" style=\\\"padding: 0px; margin: 0px; text-decoration-line: none; font-size: 12px; color: rgb(68, 68, 68);\\\">集团荣誉</a></h2><h2 class=\\\"R-h2\\\" style=\\\"padding: 0px 0px 0px 15px; margin: 0px; clear: both; overflow: hidden; height: 21px; line-height: 21px; border-bottom: 1px solid rgb(51, 51, 153); font-family: ΢���ź�; font-size: 14px; background: url(&quot;../images/r_top.gif&quot;) 2px center no-repeat rgb(255, 255, 255); color: rgb(68, 68, 68); white-space: normal;\\\"><a href=\\\"file:///E:/Users/kerneler/OneDrive/publiccms/html/%E8%93%9D%E8%89%B2%E9%9B%86%E5%9B%A2%E5%85%AC%E5%8F%B8PC%E7%AB%99%E7%82%B9%E9%9D%99%E6%80%81%E9%A1%B5/about_3.html\\\" title=\\\"集团荣誉\\\" style=\\\"padding: 0px; margin: 0px; text-decoration-line: none; font-size: 12px; color: rgb(68, 68, 68);\\\">集团荣誉</a></h2><h2 class=\\\"R-h2\\\" style=\\\"padding: 0px 0px 0px 15px; margin: 0px; clear: both; overflow: hidden; height: 21px; line-height: 21px; border-bottom: 1px solid rgb(51, 51, 153); font-family: ΢���ź�; font-size: 14px; background: url(&quot;../images/r_top.gif&quot;) 2px center no-repeat rgb(255, 255, 255); color: rgb(68, 68, 68); white-space: normal;\\\"><a href=\\\"file:///E:/Users/kerneler/OneDrive/publiccms/html/%E8%93%9D%E8%89%B2%E9%9B%86%E5%9B%A2%E5%85%AC%E5%8F%B8PC%E7%AB%99%E7%82%B9%E9%9D%99%E6%80%81%E9%A1%B5/about_3.html\\\" title=\\\"集团荣誉\\\" style=\\\"padding: 0px; margin: 0px; text-decoration-line: none; font-size: 12px; color: rgb(68, 68, 68);\\\">集团荣誉</a></h2><h2 class=\\\"R-h2\\\" style=\\\"padding: 0px 0px 0px 15px; margin: 0px; clear: both; overflow: hidden; height: 21px; line-height: 21px; border-bottom: 1px solid rgb(51, 51, 153); font-family: ΢���ź�; font-size: 14px; background: url(&quot;../images/r_top.gif&quot;) 2px center no-repeat rgb(255, 255, 255); color: rgb(68, 68, 68); white-space: normal;\\\"><a href=\\\"file:///E:/Users/kerneler/OneDrive/publiccms/html/%E8%93%9D%E8%89%B2%E9%9B%86%E5%9B%A2%E5%85%AC%E5%8F%B8PC%E7%AB%99%E7%82%B9%E9%9D%99%E6%80%81%E9%A1%B5/about_3.html\\\" title=\\\"集团荣誉\\\" style=\\\"padding: 0px; margin: 0px; text-decoration-line: none; font-size: 12px; color: rgb(68, 68, 68);\\\">集团荣誉</a></h2><h2 class=\\\"R-h2\\\" style=\\\"padding: 0px 0px 0px 15px; margin: 0px; clear: both; overflow: hidden; height: 21px; line-height: 21px; border-bottom: 1px solid rgb(51, 51, 153); font-family: ΢���ź�; font-size: 14px; background: url(&quot;../images/r_top.gif&quot;) 2px center no-repeat rgb(255, 255, 255); color: rgb(68, 68, 68); white-space: normal;\\\"><a href=\\\"file:///E:/Users/kerneler/OneDrive/publiccms/html/%E8%93%9D%E8%89%B2%E9%9B%86%E5%9B%A2%E5%85%AC%E5%8F%B8PC%E7%AB%99%E7%82%B9%E9%9D%99%E6%80%81%E9%A1%B5/about_3.html\\\" title=\\\"集团荣誉\\\" style=\\\"padding: 0px; margin: 0px; text-decoration-line: none; font-size: 12px; color: rgb(68, 68, 68);\\\">集团荣誉</a><a href=\\\"file:///E:/Users/kerneler/OneDrive/publiccms/html/%E8%93%9D%E8%89%B2%E9%9B%86%E5%9B%A2%E5%85%AC%E5%8F%B8PC%E7%AB%99%E7%82%B9%E9%9D%99%E6%80%81%E9%A1%B5/about_3.html\\\" title=\\\"集团荣誉\\\" style=\\\"padding: 0px; margin: 0px; text-decoration-line: none; font-size: 12px; color: rgb(68, 68, 68);\\\">集团荣誉</a></h2><p><br/></p>\"}');
INSERT INTO `cms_category_attribute` VALUES (80, '', '', '', '{\"article\":\"<p>社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (81, '', '', '', '{\"article\":\"<h2 class=\\\"R-h2\\\" style=\\\"padding: 0px 0px 0px 15px; margin: 0px; clear: both; overflow: hidden; height: 21px; line-height: 21px; border-bottom: 1px solid rgb(51, 51, 153); font-family: ΢���ź�; font-size: 14px; background: url(&quot;../images/r_top.gif&quot;) 2px center no-repeat rgb(255, 255, 255); color: rgb(68, 68, 68); white-space: normal;\\\"><a href=\\\"file:///E:/Users/kerneler/OneDrive/publiccms/html/%E8%93%9D%E8%89%B2%E9%9B%86%E5%9B%A2%E5%85%AC%E5%8F%B8PC%E7%AB%99%E7%82%B9%E9%9D%99%E6%80%81%E9%A1%B5/about_5.html\\\" title=\\\"发展目标\\\" style=\\\"padding: 0px; margin: 0px; text-decoration-line: none; font-size: 12px; color: rgb(68, 68, 68);\\\">发展目标</a></h2><h2 class=\\\"R-h2\\\" style=\\\"padding: 0px 0px 0px 15px; margin: 0px; clear: both; overflow: hidden; height: 21px; line-height: 21px; border-bottom: 1px solid rgb(51, 51, 153); font-family: ΢���ź�; font-size: 14px; background: url(&quot;../images/r_top.gif&quot;) 2px center no-repeat rgb(255, 255, 255); color: rgb(68, 68, 68); white-space: normal;\\\"><a href=\\\"file:///E:/Users/kerneler/OneDrive/publiccms/html/%E8%93%9D%E8%89%B2%E9%9B%86%E5%9B%A2%E5%85%AC%E5%8F%B8PC%E7%AB%99%E7%82%B9%E9%9D%99%E6%80%81%E9%A1%B5/about_5.html\\\" title=\\\"发展目标\\\" style=\\\"padding: 0px; margin: 0px; text-decoration-line: none; font-size: 12px; color: rgb(68, 68, 68);\\\">发展目标</a></h2><h2 class=\\\"R-h2\\\" style=\\\"padding: 0px 0px 0px 15px; margin: 0px; clear: both; overflow: hidden; height: 21px; line-height: 21px; border-bottom: 1px solid rgb(51, 51, 153); font-family: ΢���ź�; font-size: 14px; background: url(&quot;../images/r_top.gif&quot;) 2px center no-repeat rgb(255, 255, 255); color: rgb(68, 68, 68); white-space: normal;\\\"><a href=\\\"file:///E:/Users/kerneler/OneDrive/publiccms/html/%E8%93%9D%E8%89%B2%E9%9B%86%E5%9B%A2%E5%85%AC%E5%8F%B8PC%E7%AB%99%E7%82%B9%E9%9D%99%E6%80%81%E9%A1%B5/about_5.html\\\" title=\\\"发展目标\\\" style=\\\"padding: 0px; margin: 0px; text-decoration-line: none; font-size: 12px; color: rgb(68, 68, 68);\\\">发展目标</a></h2><h2 class=\\\"R-h2\\\" style=\\\"padding: 0px 0px 0px 15px; margin: 0px; clear: both; overflow: hidden; height: 21px; line-height: 21px; border-bottom: 1px solid rgb(51, 51, 153); font-family: ΢���ź�; font-size: 14px; background: url(&quot;../images/r_top.gif&quot;) 2px center no-repeat rgb(255, 255, 255); color: rgb(68, 68, 68); white-space: normal;\\\"><a href=\\\"file:///E:/Users/kerneler/OneDrive/publiccms/html/%E8%93%9D%E8%89%B2%E9%9B%86%E5%9B%A2%E5%85%AC%E5%8F%B8PC%E7%AB%99%E7%82%B9%E9%9D%99%E6%80%81%E9%A1%B5/about_5.html\\\" title=\\\"发展目标\\\" style=\\\"padding: 0px; margin: 0px; text-decoration-line: none; font-size: 12px; color: rgb(68, 68, 68);\\\">发展目标</a></h2><h2 class=\\\"R-h2\\\" style=\\\"padding: 0px 0px 0px 15px; margin: 0px; clear: both; overflow: hidden; height: 21px; line-height: 21px; border-bottom: 1px solid rgb(51, 51, 153); font-family: ΢���ź�; font-size: 14px; background: url(&quot;../images/r_top.gif&quot;) 2px center no-repeat rgb(255, 255, 255); color: rgb(68, 68, 68); white-space: normal;\\\"><a href=\\\"file:///E:/Users/kerneler/OneDrive/publiccms/html/%E8%93%9D%E8%89%B2%E9%9B%86%E5%9B%A2%E5%85%AC%E5%8F%B8PC%E7%AB%99%E7%82%B9%E9%9D%99%E6%80%81%E9%A1%B5/about_5.html\\\" title=\\\"发展目标\\\" style=\\\"padding: 0px; margin: 0px; text-decoration-line: none; font-size: 12px; color: rgb(68, 68, 68);\\\">发展目标</a></h2><h2 class=\\\"R-h2\\\" style=\\\"padding: 0px 0px 0px 15px; margin: 0px; clear: both; overflow: hidden; height: 21px; line-height: 21px; border-bottom: 1px solid rgb(51, 51, 153); font-family: ΢���ź�; font-size: 14px; background: url(&quot;../images/r_top.gif&quot;) 2px center no-repeat rgb(255, 255, 255); color: rgb(68, 68, 68); white-space: normal;\\\"><a href=\\\"file:///E:/Users/kerneler/OneDrive/publiccms/html/%E8%93%9D%E8%89%B2%E9%9B%86%E5%9B%A2%E5%85%AC%E5%8F%B8PC%E7%AB%99%E7%82%B9%E9%9D%99%E6%80%81%E9%A1%B5/about_5.html\\\" title=\\\"发展目标\\\" style=\\\"padding: 0px; margin: 0px; text-decoration-line: none; font-size: 12px; color: rgb(68, 68, 68);\\\">发展目标</a></h2><h2 class=\\\"R-h2\\\" style=\\\"padding: 0px 0px 0px 15px; margin: 0px; clear: both; overflow: hidden; height: 21px; line-height: 21px; border-bottom: 1px solid rgb(51, 51, 153); font-family: ΢���ź�; font-size: 14px; background: url(&quot;../images/r_top.gif&quot;) 2px center no-repeat rgb(255, 255, 255); color: rgb(68, 68, 68); white-space: normal;\\\"><a href=\\\"file:///E:/Users/kerneler/OneDrive/publiccms/html/%E8%93%9D%E8%89%B2%E9%9B%86%E5%9B%A2%E5%85%AC%E5%8F%B8PC%E7%AB%99%E7%82%B9%E9%9D%99%E6%80%81%E9%A1%B5/about_5.html\\\" title=\\\"发展目标\\\" style=\\\"padding: 0px; margin: 0px; text-decoration-line: none; font-size: 12px; color: rgb(68, 68, 68);\\\">发展目标</a></h2><h2 class=\\\"R-h2\\\" style=\\\"padding: 0px 0px 0px 15px; margin: 0px; clear: both; overflow: hidden; height: 21px; line-height: 21px; border-bottom: 1px solid rgb(51, 51, 153); font-family: ΢���ź�; font-size: 14px; background: url(&quot;../images/r_top.gif&quot;) 2px center no-repeat rgb(255, 255, 255); color: rgb(68, 68, 68); white-space: normal;\\\"><a href=\\\"file:///E:/Users/kerneler/OneDrive/publiccms/html/%E8%93%9D%E8%89%B2%E9%9B%86%E5%9B%A2%E5%85%AC%E5%8F%B8PC%E7%AB%99%E7%82%B9%E9%9D%99%E6%80%81%E9%A1%B5/about_5.html\\\" title=\\\"发展目标\\\" style=\\\"padding: 0px; margin: 0px; text-decoration-line: none; font-size: 12px; color: rgb(68, 68, 68);\\\">发展目标</a></h2><h2 class=\\\"R-h2\\\" style=\\\"padding: 0px 0px 0px 15px; margin: 0px; clear: both; overflow: hidden; height: 21px; line-height: 21px; border-bottom: 1px solid rgb(51, 51, 153); font-family: ΢���ź�; font-size: 14px; background: url(&quot;../images/r_top.gif&quot;) 2px center no-repeat rgb(255, 255, 255); color: rgb(68, 68, 68); white-space: normal;\\\"><a href=\\\"file:///E:/Users/kerneler/OneDrive/publiccms/html/%E8%93%9D%E8%89%B2%E9%9B%86%E5%9B%A2%E5%85%AC%E5%8F%B8PC%E7%AB%99%E7%82%B9%E9%9D%99%E6%80%81%E9%A1%B5/about_5.html\\\" title=\\\"发展目标\\\" style=\\\"padding: 0px; margin: 0px; text-decoration-line: none; font-size: 12px; color: rgb(68, 68, 68);\\\">发展目标</a></h2><p><br/></p>\"}');
INSERT INTO `cms_category_attribute` VALUES (82, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (83, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (84, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (85, '', '', '', '{\"article\":\"<p>集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (86, '', '', '', '{\"article\":\"<p>投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (87, '', '', '', '{\"article\":\"<p>置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (88, '', '', '', '{\"article\":\"<p>能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (104, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (105, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (106, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (89, '', '', '', '{\"article\":\"<p>现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (90, '', '', '', '{\"article\":\"<p>生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (91, '', '', '', '{\"article\":\"<p>现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (92, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (93, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (94, '', '', '', '{\"article\":\"<p>人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (95, '', '', '', '{\"article\":\"<p>薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (96, '', '', '', '{\"article\":\"<p>职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (97, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (98, '', '', '', '{\"article\":\"<p>联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (99, '', '', '', '{\"article\":\"<p>Core teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore team</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (100, '', '', '', '{\"article\":\"<p>OrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganization</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (101, '', '', '', '{\"article\":\"<p>Group honor&nbsp;Group honor&nbsp;Group honor&nbsp;Group honor&nbsp;Group honor&nbsp;Group honor&nbsp;Group honor&nbsp;Group honor&nbsp;Group honor&nbsp;Group honor&nbsp;Group honor&nbsp;Group honor&nbsp;Group honor&nbsp;Group honor&nbsp;Group honor&nbsp;Group honor Group honor Group honor Group honor Group honor Group honor Group honor&nbsp;Group honor&nbsp;Group honor Group honor Group honor&nbsp;Group honor&nbsp;Group honor&nbsp;Group honor Group honor Group honor Group honor Group honor&nbsp;</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (102, '', '', '', '{\"article\":\"<p>Social responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibility</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (103, '', '', '', '{\"article\":\"<p>Development goals&nbsp;Development goals&nbsp;Development goals&nbsp;Development goals&nbsp;Development goals&nbsp;Development goals&nbsp;Development goals&nbsp;Development goals&nbsp;Development goals&nbsp;Development goals&nbsp;Development goals&nbsp;Development goals&nbsp;Development goals Development goals Development goals Development goals Development goals Development goals Development goals&nbsp;Development goals&nbsp;Development goals Development goals Development goals&nbsp;Development goals&nbsp;Development goals Development goals Development goals&nbsp;Development goals&nbsp;Development goals&nbsp;</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (107, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (108, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (109, '', '', '', '{\"article\":\"<p>Home ownership&nbsp;Home ownership&nbsp;Home ownership&nbsp;Home ownership&nbsp;Home ownership&nbsp;Home ownership&nbsp;Home ownership&nbsp;Home ownership&nbsp;Home ownership&nbsp;Home ownership&nbsp;Home ownership&nbsp;Home ownership&nbsp;Home ownership&nbsp;Home ownership&nbsp;Home ownership&nbsp;Home ownership&nbsp;</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (110, '', '', '', '{\"article\":\"<p>Energy photoelectric&nbsp;Energy photoelectric&nbsp;Energy photoelectric&nbsp;Energy photoelectric&nbsp;Energy photoelectric&nbsp;Energy photoelectric&nbsp;Energy photoelectric&nbsp;Energy photoelectric&nbsp;Energy photoelectric&nbsp;Energy photoelectric&nbsp;Energy photoelectric&nbsp;Energy photoelectric&nbsp;Energy photoelectric&nbsp;Energy photoelectric&nbsp;</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (111, '', '', '', '{\"article\":\"<p>Modern logistics&nbsp;Modern logistics&nbsp;Modern logistics&nbsp;Modern logistics&nbsp;Modern logistics&nbsp;Modern logistics&nbsp;Modern logistics&nbsp;Modern logistics&nbsp;Modern logistics&nbsp;Modern logistics&nbsp;Modern logistics&nbsp;Modern logistics&nbsp;Modern logistics&nbsp;Modern logistics&nbsp;Modern logistics&nbsp;Modern logistics&nbsp;Modern logistics&nbsp;Modern logistics&nbsp;Modern logistics&nbsp;Modern logistics&nbsp;Modern logistics&nbsp;</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (112, '', '', '', '{\"article\":\"<p>Biopharmaceutical&nbsp;Biopharmaceutical&nbsp;Biopharmaceutical&nbsp;Biopharmaceutical&nbsp;Biopharmaceutical&nbsp;Biopharmaceutical&nbsp;Biopharmaceutical&nbsp;Biopharmaceutical&nbsp;Biopharmaceutical&nbsp;Biopharmaceutical&nbsp;Biopharmaceutical&nbsp;Biopharmaceutical&nbsp;Biopharmaceutical&nbsp;Biopharmaceutical&nbsp;Biopharmaceutical&nbsp;Biopharmaceutical Biopharmaceutical&nbsp;Biopharmaceutical&nbsp;Biopharmaceutical&nbsp;</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (113, '', '', '', '{\"article\":\"<p>Modern agriculture&nbsp;Modern agriculture&nbsp;Modern agriculture&nbsp;Modern agriculture&nbsp;Modern agriculture&nbsp;Modern agriculture&nbsp;Modern agriculture&nbsp;Modern agriculture&nbsp;Modern agriculture&nbsp;Modern agriculture&nbsp;Modern agriculture&nbsp;Modern agriculture&nbsp;Modern agriculture&nbsp;Modern agriculture&nbsp;Modern agriculture&nbsp;Modern agriculture&nbsp;Modern agriculture&nbsp;Modern agriculture&nbsp;</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (114, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (115, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (116, '', '', '', '{\"article\":\"<p>Talent Concept&nbsp;Talent Concept&nbsp;Talent Concept&nbsp;Talent Concept&nbsp;Talent Concept&nbsp;Talent Concept&nbsp;Talent Concept&nbsp;Talent Concept&nbsp;Talent Concept&nbsp;Talent Concept&nbsp;Talent Concept&nbsp;Talent Concept&nbsp;Talent Concept&nbsp;Talent Concept&nbsp;Talent Concept&nbsp;Talent Concept&nbsp;Talent Concept&nbsp;Talent Concept&nbsp;Talent Concept&nbsp;Talent Concept&nbsp;Talent Concept&nbsp;Talent Concept&nbsp;Talent Concept&nbsp;</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (117, '', '', '', '{\"article\":\"<p>Remuneration and benefits&nbsp;Remuneration and benefits&nbsp;Remuneration and benefits&nbsp;Remuneration and benefits&nbsp;Remuneration and benefits&nbsp;Remuneration and benefits&nbsp;Remuneration and benefits&nbsp;Remuneration and benefits&nbsp;Remuneration and benefits&nbsp;Remuneration and benefits&nbsp;Remuneration and benefits&nbsp;Remuneration and benefits&nbsp;Remuneration and benefits&nbsp;Remuneration and benefits&nbsp;Remuneration and benefits&nbsp;Remuneration and benefits&nbsp;Remuneration and benefits&nbsp;Remuneration and benefits&nbsp;Remuneration and benefits&nbsp;Remuneration and benefits&nbsp;</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (118, '', '', '', '{\"article\":\"<p>Career Development&nbsp;Career Development&nbsp;Career Development&nbsp;Career Development&nbsp;Career Development&nbsp;Career Development&nbsp;Career Development&nbsp;Career Development&nbsp;Career Development&nbsp;Career Development&nbsp;Career Development&nbsp;Career Development&nbsp;Career Development&nbsp;Career Development&nbsp;Career Development&nbsp;Career Development&nbsp;Career Development&nbsp;Career Development&nbsp;Career Development&nbsp;Career Development&nbsp;Career Development&nbsp;</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (119, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (120, '', '', '', '{\"article\":\"<p>联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (121, '', '', '', '{\"article\":\"<p>Contact us&nbsp;Contact us&nbsp;Contact us&nbsp;Contact us&nbsp;Contact us&nbsp;Contact us&nbsp;Contact us&nbsp;Contact us&nbsp;Contact us&nbsp;Contact us&nbsp;Contact us&nbsp;Contact us&nbsp;Contact us&nbsp;Contact us&nbsp;Contact us&nbsp;Contact us&nbsp;Contact us&nbsp;Contact us&nbsp;Contact us&nbsp;</p>\"}');

-- ----------------------------
-- Records of cms_category_model
-- ----------------------------

INSERT INTO `cms_category_model` VALUES ('1', '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES ('1', '5', '');
INSERT INTO `cms_category_model` VALUES ('9', '1', '/system/article.html');
INSERT INTO `cms_category_model` VALUES ('8', '3', '/system/picture.html');
INSERT INTO `cms_category_model` VALUES ('7', '3', '/system/picture.html');
INSERT INTO `cms_category_model` VALUES ('7', '1', '/system/article.html');
INSERT INTO `cms_category_model` VALUES ('6', '2', '');
INSERT INTO `cms_category_model` VALUES ('12', '2', '');
INSERT INTO `cms_category_model` VALUES ('12', '1', '/system/article.html');
INSERT INTO `cms_category_model` VALUES ('15', '4', '/system/book.html');
INSERT INTO `cms_category_model` VALUES ('15', '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES ('15', '5', '');
INSERT INTO `cms_category_model` VALUES ('9', '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES ('9', '5', '');
INSERT INTO `cms_category_model` VALUES ('9', '3', '/system/picture.html');
INSERT INTO `cms_category_model` VALUES ('9', '2', '');
INSERT INTO `cms_category_model` VALUES ('16', '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES ('16', '5', '');
INSERT INTO `cms_category_model` VALUES ('6', '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES ('6', '5', '');
INSERT INTO `cms_category_model` VALUES ('6', '1', '/system/article.html');
INSERT INTO `cms_category_model` VALUES ('6', '3', '/system/picture.html');
INSERT INTO `cms_category_model` VALUES ('8', '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES ('8', '5', '');
INSERT INTO `cms_category_model` VALUES ('7', '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES ('7', '5', '');
INSERT INTO `cms_category_model` VALUES ('17', '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES ('17', '5', '');
INSERT INTO `cms_category_model` VALUES ('17', '3', '/system/picture.html');
INSERT INTO `cms_category_model` VALUES ('17', '2', '');
INSERT INTO `cms_category_model` VALUES ('17', '1', '/system/article.html');
INSERT INTO `cms_category_model` VALUES ('7', '2', '');
INSERT INTO `cms_category_model` VALUES ('14', '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES ('14', '5', '');
INSERT INTO `cms_category_model` VALUES ('12', '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES ('12', '5', '');
INSERT INTO `cms_category_model` VALUES ('18', '8', '');
INSERT INTO `cms_category_model` VALUES ('18', '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES ('18', '5', '');
INSERT INTO `cms_category_model` VALUES ('18', '7', '');
INSERT INTO `cms_category_model` VALUES ('19', '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES ('19', '5', '');
INSERT INTO `cms_category_model` VALUES ('19', '2', '');
INSERT INTO `cms_category_model` VALUES (76, 'article', NULL);
INSERT INTO `cms_category_model` VALUES (83, 'article', NULL);
INSERT INTO `cms_category_model` VALUES (84, 'article', NULL);
INSERT INTO `cms_category_model` VALUES (93, 'article', NULL);

-- ----------------------------
-- Records of cms_category_type
-- ----------------------------
INSERT INTO `cms_category_type` VALUES (1, 2, '带文章的分类', 0, 1);

-- ----------------------------
-- Records of cms_place
-- ----------------------------
INSERT INTO `cms_place` VALUES ('1', '1', '/index.html/94fe86e5-45b3-4896-823a-37c6d7d6c578.html', '1', 'content', '142', 'PublicCMS后台截图', '//www.publiccms.com/introduction/2015/08-11/142.html', 'upload/2017/01-15/17-35-240834-18490682.jpg', '2016-03-21 21:25:19', '2016-03-21 21:24:54', NULL, '1', '6', '0');
INSERT INTO `cms_place` VALUES ('2', '1', '/index.html/94fe86e5-45b3-4896-823a-37c6d7d6c578.html', '1', 'content', '159', '美食', '//www.publiccms.com/picture/2015/08-13/159.html', 'upload/2017/01-15/17-35-150887-240130090.jpg', '2016-03-21 21:26:26', '2016-03-21 21:26:08', NULL, '1', '4', '0');
INSERT INTO `cms_place` VALUES ('3', '1', '/index.html/94fe86e5-45b3-4896-823a-37c6d7d6c578.html', '1', 'content', '9', '昂科拉', '//www.publiccms.com/car/2015/08-06/9.html', 'upload/2017/01-15/17-35-0606061972977756.jpg', '2016-03-21 21:28:57', '2016-03-21 21:28:36', NULL, '1', '8', '0');
INSERT INTO `cms_place` VALUES ('4', '1', '/index.html/94fe86e5-45b3-4896-823a-37c6d7d6c578.html', '1', 'content', '179', 'PublicCMS系统使用手册下载', '//www.publiccms.com/introduction/2015/10-09/179.html', 'upload/2017/01-15/21-49-430428-1960110600.jpg', '2016-03-21 21:30:25', '2016-03-21 21:43:45', NULL, '1', '18', '0');
INSERT INTO `cms_place` VALUES ('5', '1', '/index.html/94fe86e5-45b3-4896-823a-37c6d7d6c578.html', '1', 'content', '195', '我们的婚纱照', '//www.publiccms.com/picture/2015/11-15/195.html', 'upload/2017/01-15/17-34-450591-326203189.jpg', '2016-03-21 21:31:04', '2016-03-20 21:30:46', NULL, '1', '4', '0');
INSERT INTO `cms_place` VALUES ('6', '1', '/category/list.html/3435e9a7-565a-4f93-8670-9c272a1d51cc.html', '1', 'content', '4', '唯美动漫图片', '//www.publiccms.com/8/4.html', 'upload/2017/01-15/11-24-1308292097994334.jpg', '2016-03-23 11:22:57', '2016-03-23 11:22:04', NULL, '1', '4', '0');
INSERT INTO `cms_place` VALUES ('7', '1', '/category/list.html/3435e9a7-565a-4f93-8670-9c272a1d51cc.html', '1', 'content', '9', '昂科拉', '//www.publiccms.com/6/9.html', 'upload/2017/01-15/11-24-3602801209954489.jpg', '2016-03-23 11:23:55', '2016-03-23 11:23:31', NULL, '1', '2', '0');
INSERT INTO `cms_place` VALUES ('8', '1', '/category/list.html/49d393ca-f0f1-4723-a9b0-6f9b6d7cc04d.html', '1', 'content', '19', '微软：不要在Win10中使用第三方“隐私保护”工具', '//www.publiccms.com/2015/08/06/19.html', '', '2016-03-23 11:27:26', '2016-03-23 11:27:06', NULL, '1', '0', '0');
INSERT INTO `cms_place` VALUES ('9', '1', '/category/list.html/49d393ca-f0f1-4723-a9b0-6f9b6d7cc04d.html', '1', 'content', '30', '女子吃了泡发2天的木耳 致多器官衰竭不治身亡', '//www.publiccms.com/2015/08-07/30.html', '', '2016-03-23 11:27:42', '2016-03-23 11:27:28', NULL, '1', '3', '0');
INSERT INTO `cms_place` VALUES ('10', '1', '/category/list.html/49d393ca-f0f1-4723-a9b0-6f9b6d7cc04d.html', '1', 'content', '22', '江苏仪征新集一玩具厂起大火 火光冲天', '//www.publiccms.com/7/22.html', '', '2016-03-23 11:27:55', '2016-03-23 11:27:44', NULL, '1', '3', '0');
INSERT INTO `cms_place` VALUES ('11', '1', '/category/list.html/49d393ca-f0f1-4723-a9b0-6f9b6d7cc04d.html', '1', 'content', '142', 'PublicCMS后台截图', '//www.publiccms.com/9/142.html', '', '2016-03-23 11:28:08', '2016-03-23 11:27:57', NULL, '1', '6', '0');
INSERT INTO `cms_place` VALUES ('12', '1', '/category/list.html/49d393ca-f0f1-4723-a9b0-6f9b6d7cc04d.html', '1', 'content', '18', 'PublicCMS进入测试阶段，即将正式发布', '//www.publiccms.com/9/18.html', '', '2016-03-23 11:28:21', '2016-03-23 11:28:14', NULL, '1', '7', '0');
INSERT INTO `cms_place` VALUES ('13', '1', '/category/list.html/49d393ca-f0f1-4723-a9b0-6f9b6d7cc04d.html', '1', 'content', '217', '酷冷至尊烈焰枪旗舰版机械键盘 有线104键游戏全背光 樱桃轴正品', 'http://s.click.taobao.com/t?e=m%3D2%26s%3Dk%2FRaMwaPpnYcQipKwQzePOeEDrYVVa64K7Vc7tFgwiFRAdhuF14FMV3pVOinSGgeRitN3%2FurF3zO1KWqeCJhFmPYiLpdxhFe%2B6GA20g%2FvatSQhIbbzwChQUDqeizZVd13GFiMU8U2DTHAGIcyZQCxSGFCzYOOqAQ&pvid=50_106.2.199.138_346_1458707425019', '', '2016-03-28 11:21:01', '2016-03-28 11:17:37', NULL, '1', '3', '0');
INSERT INTO `cms_place` VALUES (14, 2, '/ab53b388-be0e-4674-b631-e1de625c74ac.html', 2, 'custom', NULL, '投资管理', '#', 'assets/images/201210310952338421.gif', '2018-06-30 10:27:04', '2018-06-30 10:26:49', NULL, 1, 0, 0);
INSERT INTO `cms_place` VALUES (15, 2, '/ab53b388-be0e-4674-b631-e1de625c74ac.html', 2, 'custom', NULL, '置业建设', '#', 'assets/images/201210310953075326.gif', '2018-06-30 10:27:31', '2018-06-30 10:27:10', NULL, 1, 0, 0);
INSERT INTO `cms_place` VALUES (16, 2, '/ab53b388-be0e-4674-b631-e1de625c74ac.html', 2, 'custom', NULL, '能源光电', '#', 'assets/images/201210310953287112.gif', '2018-06-30 10:28:57', '2018-06-30 10:28:40', NULL, 1, 0, 0);
INSERT INTO `cms_place` VALUES (17, 2, '/ab53b388-be0e-4674-b631-e1de625c74ac.html', 2, 'custom', NULL, '现代物流', '#', 'assets/images/201210310953526760.gif', '2018-06-30 10:29:30', '2018-06-30 10:29:03', NULL, 1, 0, 0);
INSERT INTO `cms_place` VALUES (18, 2, '/ab53b388-be0e-4674-b631-e1de625c74ac.html', 2, 'custom', NULL, '生物制药', '#', 'assets/images/201209291145162884.gif', '2018-06-30 10:30:19', '2018-06-30 10:30:00', NULL, 1, 21, 0);
INSERT INTO `cms_place` VALUES (19, 2, '/ab53b388-be0e-4674-b631-e1de625c74ac.html', 2, 'custom', NULL, '现代农业', '#', 'assets/images/201210091631452563.gif', '2018-06-30 10:30:35', '2018-06-30 10:30:26', NULL, 1, 0, 0);
INSERT INTO `cms_place` VALUES (20, 2, '/505ddbed-f6ff-4a53-b5a8-0b2d7479a2ec.html', 2, 'custom', NULL, 'PublicCMS', 'http://www.publiccms.com/', '', '2018-06-30 10:32:37', '2018-06-30 10:32:25', NULL, 1, 2, 0);
INSERT INTO `cms_place` VALUES (21, 2, '/e2ef0223-ddd3-4a95-bc65-c7eb796c911a.html', 2, 'custom', NULL, 'OA', '#', 'assets/images/l_1.gif', '2018-07-03 17:43:36', '2018-07-03 17:43:12', NULL, 1, 0, 0);
INSERT INTO `cms_place` VALUES (22, 2, '/e2ef0223-ddd3-4a95-bc65-c7eb796c911a.html', 2, 'custom', NULL, '联系我们', '#', 'assets/images/l_2.gif', '2018-07-03 17:45:32', '2018-07-03 17:45:11', NULL, 1, 0, 0);

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
INSERT INTO `cms_place_attribute` VALUES (14, '{}');
INSERT INTO `cms_place_attribute` VALUES (15, '{}');
INSERT INTO `cms_place_attribute` VALUES (16, '{}');
INSERT INTO `cms_place_attribute` VALUES (17, '{}');
INSERT INTO `cms_place_attribute` VALUES (18, '{}');
INSERT INTO `cms_place_attribute` VALUES (19, '{}');
INSERT INTO `cms_place_attribute` VALUES (20, '{}');
INSERT INTO `cms_place_attribute` VALUES (21, '{}');
INSERT INTO `cms_place_attribute` VALUES (22, '{}');

-- ----------------------------
-- Records of sys_config_data
-- ----------------------------
INSERT INTO `sys_config_data`(`site_id`, `code`, `data`) VALUES (2, 'siteAttribute', '{\"logo\":\"assets/images/logo.gif\",\"parentId\":\"71\",\"parentId_en\":\"72\"}');

-- ----------------------------
-- Records of sys_extend
-- ----------------------------
INSERT INTO `sys_extend` VALUES (1, 'categoryType', 1);

-- ----------------------------
-- Records of sys_extend_field
-- ----------------------------
INSERT INTO `sys_extend_field` VALUES (1, 'article', 1, NULL, '内容', '', 'editor', '', NULL, 0);

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES ('2', '2', '技术部', null, '', '3', '1000', '1', '1', '1');

-- ----------------------------
-- Records of sys_domain
-- ----------------------------
INSERT INTO `sys_domain` VALUES ('site2.dev.publiccms.com', '2', '1', '');

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('2', '2', '站长', '1', '0');

-- ----------------------------
-- Records of sys_role_user
-- ----------------------------
INSERT INTO `sys_role_user` VALUES ('2', '2');

-- ----------------------------
-- Records of sys_site
-- ----------------------------
INSERT INTO `sys_site` VALUES ('2', null ,'演示站点1', '0', '//site2.dev.publiccms.com:8080/publiccms/webfile/', '0', '//site2.dev.publiccms.com:8080/publiccms/', '0');

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('2', '2', 'admin', '21232f297a57a5a743894a0e4a801fc3', NULL, 1, 'admin', '2', '1', '2', '', '0', '1', '0', '2019-01-01 00:00:00', '127.0.0.1', '0', '2019-01-01 00:00:00');