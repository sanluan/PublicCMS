-- ----------------------------
-- Records of cms_category
-- ----------------------------
INSERT INTO `cms_category` VALUES (1, 3, '演示', NULL, NULL, '6,7,8,9,12,15,17', '', 'demonstrate', 1, '/category/parent.html', '${category.code}/index.html', 0, 1, 'demonstrate/index.html', 1, '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', 1, 10, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (6, 3, '汽车', 1, NULL, NULL, '', 'car', 1, '/category/list.html', '${category.code}/index.html', 0, 1, 'car/index.html', 1, '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', 1, 10, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (7, 3, '社会', 1, NULL, NULL, '', 'social', 1, '/category/list.html', '${category.code}/index.html', 0, 1, 'social/index.html', 1, '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', 1, 10, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (8, 3, '美图', 1, NULL, NULL, '', 'picture', 1, '/category/list.html', '${category.code}/index.html', 0, 1, 'picture/index.html', 1, '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', 1, 10, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (9, 3, '系统介绍', 1, NULL, NULL, '', 'introduction', 1, '/category/list.html', '${category.code}/index.html', 0, 1, 'introduction/index.html', 1, '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', 1, 10, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (12, 3, '文章', 1, NULL, NULL, '', 'article', 1, '/category/list.html', '${category.code}/index.html', 0, 1, 'article/index.html', 1, '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', 1, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (15, 3, '小说', 1, NULL, NULL, '', 'novel', 1, '/category/list.html', '${category.code}/index.html', 0, 1, 'novel/index.html', 0, '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', 1, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (17, 3, '产品', 1, NULL, NULL, '', 'product', 1, '/category/list.html', '${category.code}/index.html', 0, 1, 'product/index.html', 0, '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', 1, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (19, 3, '案例', NULL, NULL, NULL, '', 'case', 1, '/category/parent.html', '${category.code}/index.html', 0, 1, 'case/index.html', 1, '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', 1, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (20, 4, '功能演示', NULL, 'banner', '21,22,23,24,25,26', '', 'demonstrate', 1, '/category/list.html', '${category.code}/index.html', 0, 1, 'demonstrate/index.html', 1, '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', 1, 10, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (21, 4, '汽车', 20, NULL, NULL, '', 'car', 1, '/category/list.html', '${category.code}/index.html', 0, 1, 'car/index.html', 1, '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', 1, 10, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (22, 4, '商品', 20, NULL, NULL, '', 'social', 1, '/category/list.html', '${category.code}/index.html', 0, 1, 'social/index.html', 1, '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', 1, 10, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (23, 4, '美图', 20, NULL, NULL, '', 'picture', 1, '/category/list.html', '${category.code}/index.html', 0, 1, 'picture/index.html', 1, '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', 1, 10, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (24, 4, '介绍', 20, NULL, NULL, '', 'introduction', 1, '/category/list.html', '${category.code}/index.html', 0, 1, 'introduction/index.html', 1, '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', 1, 10, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (25, 4, '文章', 20, NULL, NULL, '', 'article', 1, '/category/list.html', '${category.code}/index.html', 0, 1, 'article/index.html', 1, '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', 1, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (26, 4, '小说', 20, NULL, NULL, '', 'novel', 1, '/category/list.html', '${category.code}/index.html', 0, 1, 'novel/index.html', 1, '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', 1, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (27, 4, '手册', NULL, NULL, '174', '', 'science', 1, '', '<@cms.contentList categoryId=category.id pageSize=1 orderType=\'asc\'><#list page.list as a>${a.url}</#list></@cms.contentList>', 0, 0, '', 1, '${category.code}/${content.id}.html', 1, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (28, 4, '案例', NULL, 'banner', NULL, '', 'case', 1, '/category/case.html', '${category.code}/index.html', 0, 1, 'case/index.html', 1, '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', 1, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (29, 4, '问答', NULL, 'banner', '33', '', 'support', 1, '/category/qa.html', '${category.code}/', 0, 1, 'support/', 1, '${category.code}/${content.publishDate?string(\'yyyy-MM-dd\')}/${content.id}.html', 0, 20, 0, 7, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (30, 4, '演示', NULL, 'banner', '34', '', 'demo', 1, '/category/demo.html', '${category.code}/', 0, 1, 'demo/', 1, '', 0, 20, 0, 6, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (31, 4, '下载', NULL, 'banner', NULL, '', 'download', 1, '/category/download.html', 'download.html', 0, 1, 'download.html', 1, '#', 0, 20, 0, 3, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (32, 4, '价格', NULL, 'banner', NULL, '', 'price', 1, '/category/price.html', '${category.code}.html', 0, 1, 'price.html', 1, '${category.code}/${content.publishDate?string(\'yyyy-MM-dd\')}/${content.id}.html', 0, 20, 0, 4, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (33, 4, 'GITEE', 29, 'cover', NULL, '', 'gitee', 1, '', 'https://gitee.com/sanluan/PublicCMS/issues', 1, 0, 'https://gitee.com/sanluan/PublicCMS/issues', 1, '', 0, 20, 0, 3, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (34, 4, '后台功能演示', 30, 'cover', NULL, '', 'admin', 1, '', 'https://cms.publiccms.com/admin/', 1, 0, 'https://cms.publiccms.com/admin/', 1, '', 0, 20, 0, 1, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (71, 2, '中文栏目', NULL, NULL, '73,75,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,120', '', 'zh', 1, NULL, '#', 1, 0, '#', 1, '', 0, 20, 0, 0, 1, 0, NULL);
INSERT INTO `cms_category` VALUES (72, 2, '英文栏目', NULL, NULL, '74,76,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,121', '', 'en', 1, NULL, '#', 1, 0, '#', 1, '', 0, 20, 0, 0, 1, 0, NULL);
INSERT INTO `cms_category` VALUES (73, 2, '集团简介', 71, NULL, '75,77,78,79,80,81', '', 'about', 1, NULL, '<@_categoryList parentId=category.id><#if page.totalCount gt 0>category/${page.list[0].id}</#if></@_categoryList>', 0, 0, 'category/75', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (74, 2, 'Introduction', 72, NULL, '76,99,100,101,102,103', '', 'introduction', 1, NULL, '<@_categoryList parentId=category.id><#if page.totalCount gt 0>en/category/${page.list[0].id}</#if></@_categoryList>', 0, 0, 'en/category/76', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (75, 2, '集团简介', 73, '1', NULL, '', 'aboutUS', 0, NULL, 'category/${category.id}', 0, 0, 'category/75', 1, 'content/${content.id}', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (76, 2, 'Introduction', 74, '1', NULL, '', 'introductionUS', 0, NULL, 'en/category/${category.id}', 0, 0, 'category/76', 0, 'en/content/${content.id}', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (77, 2, '核心团队', 73, '1', NULL, '', 'tuandui', 0, NULL, 'category/${category.id}', 0, 0, 'category/77', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (78, 2, '组织架构', 73, '1', NULL, '', 'jiagou', 0, NULL, 'category/${category.id}', 0, 0, 'category/78', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (79, 2, '集团荣誉', 73, '1', NULL, '', 'rongyu', 0, NULL, 'category/${category.id}', 0, 0, 'category/79', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (80, 2, '社会责任', 73, '1', NULL, '', 'zeren', 0, NULL, 'category/${category.id}', 0, 0, 'category/80', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (81, 2, '发展目标', 73, '1', NULL, '', 'mubiao', 0, NULL, 'category/${category.id}', 0, 0, 'category/81', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (82, 2, '新闻资讯', 71, NULL, '83,84', '', 'news', 1, NULL, '<@_categoryList parentId=category.id><#if page.totalCount gt 0>news/${page.list[0].id}</#if></@_categoryList>', 0, 0, 'news/83', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (83, 2, '行业资讯', 82, NULL, NULL, '', 'hangye', 0, NULL, 'news/${category.id}', 0, 0, 'news/83', 0, 'content/${content.id}', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (84, 2, '公司新闻', 82, NULL, NULL, '', 'gongsi', 0, NULL, 'news/${category.id}', 0, 0, 'news/84', 0, 'content/${content.id}', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (85, 2, '集团产业', 71, NULL, '86,87,88,89,90,91', '', 'industry', 1, NULL, '<@_categoryList parentId=category.id><#if page.totalCount gt 0>category/${page.list[0].id}</#if></@_categoryList>', 0, 0, 'category/86', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (86, 2, '投资管理', 85, '1', NULL, '', 'touzi', 0, NULL, 'category/${category.id}', 0, 0, 'category/86', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (87, 2, '置业建设', 85, '1', NULL, '', 'jianshe', 0, NULL, 'category/${category.id}', 0, 0, 'category/87', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (88, 2, '能源光电', 85, '1', NULL, '', 'nengyuan', 0, NULL, 'category/${category.id}', 0, 0, 'category/88', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (89, 2, '现代物流', 85, '1', NULL, '', 'wuliu', 0, NULL, 'category/${category.id}', 0, 0, 'category/89', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (90, 2, '生物制药', 85, '1', NULL, '', 'zhiyao', 0, NULL, 'category/${category.id}', 0, 0, 'category/90', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (91, 2, '现代农业', 85, '1', NULL, '', 'nongye', 0, NULL, 'category/${category.id}', 0, 0, 'category/91', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (92, 2, '人才中心', 71, NULL, '93,94,95,96', '', 'rencai', 1, NULL, '<@_categoryList parentId=category.id><#if page.totalCount gt 0>news/${page.list[0].id}</#if></@_categoryList>', 0, 0, 'news/93', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (93, 2, '人才招聘', 92, NULL, NULL, '', 'zhaopin', 0, NULL, 'news/${category.id}', 0, 0, 'news/93', 1, 'content/${content.id}', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (94, 2, '人才理念', 92, '1', NULL, '', 'linian', 0, NULL, 'category/${category.id}', 0, 0, 'category/94', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (95, 2, '薪酬福利', 92, '1', NULL, '', 'fuli', 0, NULL, 'category/${category.id}', 0, 0, 'category/95', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (96, 2, '职业发展', 92, '1', NULL, '', 'fazhan', 0, NULL, 'category/${category.id}', 0, 0, 'category/96', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (97, 2, '联系我们', 71, NULL, '120', '', 'linaxi', 1, NULL, '<@_categoryList parentId=category.id><#if page.totalCount gt 0>category/${page.list[0].id}</#if></@_categoryList>', 0, 0, 'category/120', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (99, 2, 'Core team', 74, '1', NULL, '', 'team', 1, NULL, 'en/category/${category.id}', 0, 0, 'en/category/99', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (100, 2, 'Organization', 74, '1', NULL, '', 'organization', 1, NULL, 'en/category/${category.id}', 0, 0, 'en/category/100', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (101, 2, 'Group honor', 74, '1', NULL, '', 'honor', 1, NULL, 'en/category/${category.id}', 0, 0, 'en/category/101', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (102, 2, 'Social responsibility', 74, '1', NULL, '', 'responsibility', 1, NULL, 'en/category/${category.id}', 0, 0, 'en/category/102', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (103, 2, 'Development goals', 74, '1', NULL, '', 'goals', 1, NULL, 'en/category/${category.id}', 0, 0, 'en/category/103', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (104, 2, 'News', 72, NULL, '105,106', '', 'news_en', 1, NULL, '<@_categoryList parentId=category.id><#if page.totalCount gt 0>en/news/${page.list[0].id}</#if></@_categoryList>', 0, 0, 'en/news/105', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (105, 2, 'Industry News', 104, NULL, NULL, '', 'industry_en', 1, NULL, 'en/news/${category.id}', 0, 0, 'en/news/105', 0, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (106, 2, 'Company news', 104, NULL, NULL, '', 'company', 1, NULL, 'en/news/${category.id}', 0, 0, 'en/news/106', 0, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (107, 2, 'Industry', 72, NULL, '108,109,110,111,112,113', '', 'group_industry', 1, NULL, '<@_categoryList parentId=category.id><#if page.totalCount gt 0>en/category/${page.list[0].id}</#if></@_categoryList>', 0, 0, 'en/category/108', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (108, 2, 'Investment Management', 107, '1', NULL, '', 'investment', 1, NULL, 'en/category/${category.id}', 0, 0, 'en/category/108', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (109, 2, 'Home ownership', 107, '1', NULL, '', 'ownership', 1, NULL, 'en/category/${category.id}', 0, 0, 'en/category/109', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (110, 2, 'Energy photoelectric', 107, '1', NULL, '', 'photoelectric', 1, NULL, 'en/category/${category.id}', 0, 0, 'en/category/110', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (111, 2, 'Modern logistics', 107, '1', NULL, '', 'logistics', 1, NULL, 'en/category/${category.id}', 0, 0, 'en/category/111', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (112, 2, 'Biopharmaceutical', 107, '1', NULL, '', 'biopharmaceutical', 1, NULL, 'en/category/${category.id}', 0, 0, 'en/category/112', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (113, 2, 'Modern agriculture', 107, '1', NULL, '', 'agriculture', 1, NULL, 'en/category/${category.id}', 0, 0, 'en/category/113', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (114, 2, 'Talent', 72, NULL, '115,116,117,118', '', 'talent', 1, NULL, '<@_categoryList parentId=category.id><#if page.totalCount gt 0>en/news/${page.list[0].id}</#if></@_categoryList>', 0, 0, 'en/news/115', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (115, 2, 'Recruitment', 114, NULL, NULL, '', 'recruitment', 1, NULL, 'en/news/${category.id}', 0, 0, 'en/news/115', 0, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (116, 2, 'Talent Concept', 114, '1', NULL, '', 'concept', 1, NULL, 'en/category/${category.id}', 0, 0, 'en/category/116', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (117, 2, 'Remuneration and benefits', 114, '1', NULL, '', 'benefits', 1, NULL, 'en/category/${category.id}', 0, 0, 'en/category/117', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (118, 2, 'Career Development', 114, '1', NULL, '', 'career', 1, NULL, 'en/category/${category.id}', 0, 0, 'en/category/118', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (119, 2, 'Contact', 72, NULL, '121', '', 'contact', 1, NULL, '<@_categoryList parentId=category.id><#if page.totalCount gt 0>en/category/${page.list[0].id}</#if></@_categoryList>', 0, 0, 'en/category/121', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (120, 2, '联系我们', 97, '1', NULL, '', 'contact_us', 1, NULL, 'category/${category.id}', 0, 0, 'category/120', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (121, 2, 'Contact us', 119, '1', NULL, '', 'contact_us_en', 1, NULL, 'en/category/${category.id}', 0, 0, 'en/category/121', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (174, 4, '第一章', 27, 'cover', NULL, '', 'guide1', 1, '', '<@cms.contentList categoryId=category.id pageSize=1 orderType=\'asc\'><#list page.list as a>${a.url}</#list></@cms.contentList>', 0, 0, '//site4.dev.publiccms.com:8080/webfile/guide1/7033375351103295488.html', 1, '${category.code}/${content.id}.html', 1, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (175, 3, '手册', NULL, '', '176', '', 'guide', 1, '', '<@cms.contentList categoryId=category.id pageSize=1><#list page.list as a>${a.url}</#list></@cms.contentList>', 0, 0, '', 1, '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', 1, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (176, 3, '第一章', 175, '', NULL, '', 'guide1', 1, '', '<@cms.contentList categoryId=category.id pageSize=1><#list page.list as a>${a.url}</#list></@cms.contentList>', 0, 0, '', 0, '', 0, 20, 0, 0, 0, 0, NULL);

-- ----------------------------
-- Records of cms_category_attribute
-- ----------------------------
INSERT INTO `cms_category_attribute` VALUES (1, '演示', 'PublicCMS,如何使用', 'PublicCMS如何使用', NULL);
INSERT INTO `cms_category_attribute` VALUES (6, '汽车 - PublicCMS', '汽车,car', '汽车', NULL);
INSERT INTO `cms_category_attribute` VALUES (7, '社会', '社会', '社会', NULL);
INSERT INTO `cms_category_attribute` VALUES (8, '美图', '美图,美女', '美图美女', NULL);
INSERT INTO `cms_category_attribute` VALUES (9, '系统介绍', 'PublicCMS,系统介绍', 'PublicCMS系统介绍', NULL);
INSERT INTO `cms_category_attribute` VALUES (12, '文章', '文章', '文章', NULL);
INSERT INTO `cms_category_attribute` VALUES (15, '小说', '小说,在线阅读', '小说,在线阅读', NULL);
INSERT INTO `cms_category_attribute` VALUES (17, '科技', '科技', '科技频道', NULL);
INSERT INTO `cms_category_attribute` VALUES (19, '案例', 'PublicCMS案例', '全球不同规模的企事业单位都在使用 PublicCMS', '{\"left_height\":\"69\",\"left_image\":\"assets/img/banner-case-1.svg\",\"right_height\":\"79\",\"right_image\":\"assets/img/banner-case-2.svg\"}');
INSERT INTO `cms_category_attribute` VALUES (20, '功能演示', 'PublicCMS,如何使用', 'PublicCMS如何使用', '{\"left_height\":\"75\",\"left_image\":\"assets/img/banner-intro-1.svg\",\"right_height\":\"80\",\"right_image\":\"assets/img/banner-intro-2.svg\"}');
INSERT INTO `cms_category_attribute` VALUES (21, '汽车 - PublicCMS', '汽车,car', '汽车', NULL);
INSERT INTO `cms_category_attribute` VALUES (22, '商品', '商品', '商品', NULL);
INSERT INTO `cms_category_attribute` VALUES (23, '美图', '美图,美女', '美图美女', NULL);
INSERT INTO `cms_category_attribute` VALUES (24, '介绍', 'PublicCMS,系统介绍', 'PublicCMS系统介绍', NULL);
INSERT INTO `cms_category_attribute` VALUES (25, '文章', '文章', '文章', NULL);
INSERT INTO `cms_category_attribute` VALUES (26, '小说', '小说,在线阅读', '小说,在线阅读', NULL);
INSERT INTO `cms_category_attribute` VALUES (27, '手册', '手册', '手册', NULL);
INSERT INTO `cms_category_attribute` VALUES (28, '案例', 'PublicCMS案例', 'PublicCMS案例', '{\"left_height\":\"69\",\"left_image\":\"assets/img/banner-case-1.svg\",\"right_height\":\"79\",\"right_image\":\"assets/img/banner-case-1.svg\"}');
INSERT INTO `cms_category_attribute` VALUES (29, '问答', '问答', '更多 PublicCMS 的技术文章，欢迎各平台创作者贡献PublicCMS文章、视频、模版等', '{\"left_height\":\"79\",\"left_image\":\"assets/img/banner-qa-1.svg\",\"right_height\":\"86\",\"right_image\":\"assets/img/banner-qa-2.svg\"}');
INSERT INTO `cms_category_attribute` VALUES (30, 'PublicCMS 的演示', 'PublicCMS 的演示', 'PublicCMS 的演示', '{\"left_height\":\"52\",\"left_image\":\"assets/img/banner-demo-1.svg\",\"right_height\":\"88\",\"right_image\":\"assets/img/banner-demo-2.svg\"}');
INSERT INTO `cms_category_attribute` VALUES (31, 'PublicCMS下载', 'PublicCMS下载,PublicCMS价格,PublicCMS购买', 'PublicCMS 的应用程序、源码等', '{\"left_height\":\"77\",\"left_image\":\"assets/img/banner-download-1.svg\",\"right_height\":\"83\",\"right_image\":\"assets/img/banner-download-2.svg\"}');
INSERT INTO `cms_category_attribute` VALUES (32, 'PublicCMS价格', 'PublicCMS价格,PublicCMS购买,制作网站价格,软件定制开发', 'PublicCMS产品价格', '{\"left_height\":\"85\",\"left_image\":\"assets/img/banner-price-1.svg\",\"right_height\":\"94\",\"right_image\":\"assets/img/banner-price-2.svg\"}');
INSERT INTO `cms_category_attribute` VALUES (33, '', '', '', '{\"cover\":\"assets/img/qa-gitee.png\"}');
INSERT INTO `cms_category_attribute` VALUES (34, '', '', '', '{\"cover\":\"assets/img/logo-original.png\"}');
INSERT INTO `cms_category_attribute` VALUES (71, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (72, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (73, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (74, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (75, '', '', '', '{\"article\":\"<p>阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (76, '', '', '', '{\"article\":\"\"}');
INSERT INTO `cms_category_attribute` VALUES (77, '', '', '', '{\"article\":\"<p>核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (78, '', '', '', '{\"article\":\"<p>组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (79, '', '', '', '{\"article\":\"<h2 class=\\\"R-h2\\\" style=\\\"padding: 0px 0px 0px 15px; margin: 0px; clear: both; overflow: hidden; height: 21px; line-height: 21px; border-bottom: 1px solid rgb(51, 51, 153); font-family: ΢���ź�; font-size: 14px; background: url(&quot;../images/r_top.gif&quot;) 2px center no-repeat rgb(255, 255, 255); color: rgb(68, 68, 68); white-space: normal;\\\">集团荣誉</h2><p><br/></p>\"}');
INSERT INTO `cms_category_attribute` VALUES (80, '', '', '', '{\"article\":\"<p>社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (81, '', '', '', '{\"article\":\"<h2 class=\\\"R-h2\\\" style=\\\"padding: 0px 0px 0px 15px; margin: 0px; clear: both; overflow: hidden; height: 21px; line-height: 21px; border-bottom: 1px solid rgb(51, 51, 153); font-family: ΢���ź�; font-size: 14px; background: url(&quot;../images/r_top.gif&quot;) 2px center no-repeat rgb(255, 255, 255); color: rgb(68, 68, 68); white-space: normal;\\\">发展目标</h2><p><br/></p>\"}');
INSERT INTO `cms_category_attribute` VALUES (82, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (83, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (84, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (85, '', '', '', '{\"article\":\"<p>集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (86, '', '', '', '{\"article\":\"<p>投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (87, '', '', '', '{\"article\":\"<p>置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (88, '', '', '', '{\"article\":\"<p>能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (89, '', '', '', '{\"article\":\"<p>现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (90, '', '', '', '{\"article\":\"<p>生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (91, '', '', '', '{\"article\":\"<p>现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (92, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (93, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (94, '', '', '', '{\"article\":\"<p>人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (95, '', '', '', '{\"article\":\"<p>薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (96, '', '', '', '{\"article\":\"<p>职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (97, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (99, '', '', '', '{\"article\":\"<p>Core teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore team</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (100, '', '', '', '{\"article\":\"<p>OrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganization</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (101, '', '', '', '{\"article\":\"<p>Group honor&nbsp;Group honor&nbsp;Group honor&nbsp;Group honor&nbsp;Group honor&nbsp;Group honor&nbsp;Group honor&nbsp;Group honor&nbsp;Group honor&nbsp;Group honor&nbsp;Group honor&nbsp;Group honor&nbsp;Group honor&nbsp;Group honor&nbsp;Group honor&nbsp;Group honor Group honor Group honor Group honor Group honor Group honor Group honor&nbsp;Group honor&nbsp;Group honor Group honor Group honor&nbsp;Group honor&nbsp;Group honor&nbsp;Group honor Group honor Group honor Group honor Group honor&nbsp;</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (102, '', '', '', '{\"article\":\"<p>Social responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibility</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (103, '', '', '', '{\"article\":\"<p>Development goals&nbsp;Development goals&nbsp;Development goals&nbsp;Development goals&nbsp;Development goals&nbsp;Development goals&nbsp;Development goals&nbsp;Development goals&nbsp;Development goals&nbsp;Development goals&nbsp;Development goals&nbsp;Development goals&nbsp;Development goals Development goals Development goals Development goals Development goals Development goals Development goals&nbsp;Development goals&nbsp;Development goals Development goals Development goals&nbsp;Development goals&nbsp;Development goals Development goals Development goals&nbsp;Development goals&nbsp;Development goals&nbsp;</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (104, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (105, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (106, '', '', '', NULL);
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
INSERT INTO `cms_category_attribute` VALUES (174, '手册', '手册', '手册', '{\"cover\":\"assets/img/icon-guide-1.svg\"}');
INSERT INTO `cms_category_attribute` VALUES (175, '案例', 'PublicCMS案例', '全球不同规模的企事业单位都在使用 PublicCMS', NULL);
INSERT INTO `cms_category_attribute` VALUES (176, '', '', '', NULL);

-- ----------------------------
-- Records of cms_category_model
-- ----------------------------

INSERT INTO `cms_category_model` VALUES (6, '1', 3, 0, '/system/article.html', NULL);
INSERT INTO `cms_category_model` VALUES (6, '2', 3, 0, '', NULL);
INSERT INTO `cms_category_model` VALUES (6, '3', 3, 0, '/system/picture.html', NULL);
INSERT INTO `cms_category_model` VALUES (7, '1', 3, 0, '/system/article.html', NULL);
INSERT INTO `cms_category_model` VALUES (7, '2', 3, 0, '', NULL);
INSERT INTO `cms_category_model` VALUES (7, '3', 3, 0, '/system/picture.html', NULL);
INSERT INTO `cms_category_model` VALUES (8, '3', 3, 0, '/system/picture.html', NULL);
INSERT INTO `cms_category_model` VALUES (9, '1', 3, 0, '/system/article.html', NULL);
INSERT INTO `cms_category_model` VALUES (9, '2', 3, 0, '', NULL);
INSERT INTO `cms_category_model` VALUES (9, '3', 3, 0, '/system/picture.html', NULL);
INSERT INTO `cms_category_model` VALUES (12, '1', 3, 0, '/system/article.html', '');
INSERT INTO `cms_category_model` VALUES (12, '2', 3, 0, NULL, NULL);
INSERT INTO `cms_category_model` VALUES (15, 'book', 3, 0, '/system/book.html', '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html');
INSERT INTO `cms_category_model` VALUES (15, 'chapter', 3, 0, '', '');
INSERT INTO `cms_category_model` VALUES (15, 'volume', 3, 0, '', '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html');
INSERT INTO `cms_category_model` VALUES (17, '2', 3, 0, NULL, NULL);
INSERT INTO `cms_category_model` VALUES (17, 'product', 3, 0, '/system/product.html', '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html');
INSERT INTO `cms_category_model` VALUES (19, '2', 3, 0, '', NULL);
INSERT INTO `cms_category_model` VALUES (21, 'article', 4, 0, '/system/article.html', NULL);
INSERT INTO `cms_category_model` VALUES (21, 'link', 4, 0, '', NULL);
INSERT INTO `cms_category_model` VALUES (21, 'picture', 4, 0, '/system/picture.html', NULL);
INSERT INTO `cms_category_model` VALUES (22, 'link', 4, 0, '', NULL);
INSERT INTO `cms_category_model` VALUES (22, 'product', 4, 0, '/system/product.html', NULL);
INSERT INTO `cms_category_model` VALUES (23, 'picture', 4, 0, '/system/picture.html', NULL);
INSERT INTO `cms_category_model` VALUES (24, 'article', 4, 0, '/system/article.html', NULL);
INSERT INTO `cms_category_model` VALUES (24, 'link', 4, 0, '', NULL);
INSERT INTO `cms_category_model` VALUES (24, 'picture', 4, 0, '/system/picture.html', NULL);
INSERT INTO `cms_category_model` VALUES (25, 'article', 4, 0, '/system/article.html', NULL);
INSERT INTO `cms_category_model` VALUES (25, 'link', 4, 0, '', NULL);
INSERT INTO `cms_category_model` VALUES (26, 'book', 4, 0, '/system/book.html', NULL);
INSERT INTO `cms_category_model` VALUES (26, 'chapter', 4, 0, '/system/chapter.html', NULL);
INSERT INTO `cms_category_model` VALUES (26, 'volume', 4, 0, '', NULL);
INSERT INTO `cms_category_model` VALUES (28, 'article', 4, 0, '/system/article.html', '');
INSERT INTO `cms_category_model` VALUES (28, 'link', 4, 0, NULL, NULL);
INSERT INTO `cms_category_model` VALUES (31, 'dir', 4, 0, '', NULL);
INSERT INTO `cms_category_model` VALUES (83, 'article', 2, 0, NULL, '');
INSERT INTO `cms_category_model` VALUES (84, 'article', 2, 0, NULL, '');
INSERT INTO `cms_category_model` VALUES (93, 'article', 2, 0, NULL, '');
INSERT INTO `cms_category_model` VALUES (105, 'article', 2, 0, NULL, '');
INSERT INTO `cms_category_model` VALUES (106, 'article', 2, 0, NULL, '');
INSERT INTO `cms_category_model` VALUES (115, 'article', 2, 0, NULL, '');
INSERT INTO `cms_category_model` VALUES (122, 'article', 1, 0, NULL, '');
INSERT INTO `cms_category_model` VALUES (123, 'article', 1, 0, NULL, '');
INSERT INTO `cms_category_model` VALUES (174, 'guide', 4, 0, '/system/guide.html', '');
INSERT INTO `cms_category_model` VALUES (175, '2', 3, 0, NULL, NULL);
INSERT INTO `cms_category_model` VALUES (176, 'guide', 3, 0, '/system/guide.html', '');

-- ----------------------------
-- Records of cms_content
-- ----------------------------
INSERT INTO `cms_content` VALUES (1, 2, '企业中英文站点模板使用说明', 2, 2, 2, 84, 'article', NULL, NULL, 0, NULL, NULL, 0, 0, 0, 0, 0, 'content/1', '企业中英文站点模板中主要有首页模板index.html,文章模板content.html，分类单页模板category.html，分类内容列表页news.html\n\n分为中英文两套模板\n\n所有模板均为动态模板，文章，分类模板接受参数为id,pageIndex等，既支持rest风格url如 news/1_12,又支持传统参数类型如news.html?id=1&pageIndex=12\n\n在分类中对带有文章的分类可以使用category.html，对内容列表可以使用news.html，填写分类访问路径为 模板名/${category.id} 或 模板名.html?id=${category.id}', NULL, NULL, 0, 0, 0, 0.00, 0, 0, '2020-01-01 00:00:00', NULL, '2020-01-01 00:00:00', NULL, NULL, '2020-01-01 00:00:00', 0, 1, 0);
INSERT INTO `cms_content` VALUES (2, 3, 'PublicCMS官网模板使用说明', 3, 3, 3, 12, '1', NULL, NULL, 0, NULL, '', 0, 0, 0, 0, 1, 'article/2020/01-01/2.html', 'PublicCMS官网模板是一组静态化模板', '', NULL, 0, 0, 0, 0.00, 0, 0, '2020-01-01 00:00:00', NULL, '2020-01-01 00:00:00', NULL, NULL, '2020-01-01 00:00:00', 0, 1, 0);
INSERT INTO `cms_content` VALUES (3, 4, 'PublicCMS官网模板使用说明', 4, 4, 4, 25, 'article', NULL, NULL, 0, NULL, '', 0, 0, 0, 0, 1, 'article/2020/01-01/3.html', 'PublicCMS官网模板是一组静态化模板', '', NULL, 0, 0, 0, 0.00, 0, 0, '2020-01-01 00:00:00', NULL, '2020-01-01 00:00:00', NULL, NULL, '2020-01-01 00:00:00', 0, 1, 0);
INSERT INTO `cms_content` VALUES (4, 4, '应用程序', 4, 4, 4, 31, 'dir', NULL, NULL, 0, NULL, NULL, 0, 0, 0, 0, 0, '#', 'PublicCMS 的应用程序', '', 'assets/img/icon-download-program.svg', 0, 0, 0, 0.00, 0, 0, '2022-01-08 18:23:59', NULL, '2022-01-09 11:48:07', NULL, NULL, '2022-01-08 18:23:55', 0, 1, 0);

-- ----------------------------
-- Records of cms_content_attribute
-- ----------------------------
INSERT INTO `cms_content_attribute` VALUES (1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '<p>企业中英文站点模板中主要有首页模板index.html,文章模板content.html，分类单页模板category.html，分类内容列表页news.html</p>\n\n<p>分为中英文两套模板</p>\n\n<p>所有模板均为动态模板，文章，分类模板接受参数为id,pageIndex等，既支持rest风格url如 news/1_12,又支持传统参数类型如news.html?id=1&amp;pageIndex=12</p>\n\n<p>在分类中对带有文章的分类可以使用category.html，对内容列表可以使用news.html，填写分类访问路径为 模板名/${category.id} 或 模板名.html?id=${category.id}</p>\n', 301);
INSERT INTO `cms_content_attribute` VALUES (2, NULL, NULL, NULL, '    PublicCMS官网模板是一组静态化模板，它是现在PublicCMS官网所采用的模板category目录下为分类模板，ftl目录下为模板片段，member下是动态模板有登录、注册、评论、个人页面等search是搜索模板，system下是四种内容的模板，index.html是首页模板即支持动态也可以静态化，sitemap.xml是站点地图模板当您新建一个分类时，分类静态化模板可以再category下面选择其中一个，并设置静态化的访问路径内容模板可以再system下面选择一个，并设置静态化访问路径。其中article.html 为文章，picture.html为图集，book.html、chapter.html为图书、章节', NULL, NULL, NULL, NULL, NULL, '<p>&nbsp;&nbsp;&nbsp;&nbsp;PublicCMS官网模板是一组静态化模板，它是现在PublicCMS官网所采用的模板</p><p><img src=\"//site3.dev.publiccms.com:8080/webfile/upload/2020/03-24/12-28-500720-90407063.png\" title=\"1.png\" alt=\"1.png\"/></p><p>category目录下为分类模板，ftl目录下为模板片段，member下是动态模板有登录、注册、评论、个人页面等</p><p>search是搜索模板，system下是四种内容的模板，index.html是首页模板即支持动态也可以静态化，sitemap.xml是站点地图模板</p><p>当您新建一个分类时，分类静态化模板可以再category下面选择其中一个，并设置静态化的访问路径</p><p><img src=\"//site3.dev.publiccms.com:8080/webfile/upload/2020/03-24/12-32-2905711856271141.png\" title=\"2.png\" alt=\"2.png\"/></p><p>内容模板可以再system下面选择一个，并设置静态化访问路径。其中article.html 为文章，picture.html为图集，book.html、chapter.html为图书、章节</p><p><img src=\"//site3.dev.publiccms.com:8080/webfile/upload/2020/03-24/12-32-36030545988429.png\" title=\"3.png\" alt=\"3.png\"/></p>', 318);
INSERT INTO `cms_content_attribute` VALUES (3, NULL, NULL, NULL, '    PublicCMS官网模板是一组静态化模板，它是现在PublicCMS官网所采用的模板category目录下为分类模板，ftl目录下为模板片段，member下是动态模板有登录、注册、评论、个人页面等search是搜索模板，system下是四种内容的模板，index.html是首页模板即支持动态也可以静态化，sitemap.xml是站点地图模板当您新建一个分类时，分类静态化模板可以再category下面选择其中一个，并设置静态化的访问路径内容模板可以再system下面选择一个，并设置静态化访问路径。其中article.html 为文章，picture.html为图集，book.html、chapter.html为图书、章节', NULL, NULL, NULL, NULL, NULL, '<p>&nbsp;&nbsp;&nbsp;&nbsp;PublicCMS官网模板是一组静态化模板，它是现在PublicCMS官网所采用的模板</p><p><img src=\"//site4.dev.publiccms.com:8080/webfile/upload/2020/03-24/12-28-500720-90407063.png\" title=\"1.png\" alt=\"1.png\"/></p><p>category目录下为分类模板，ftl目录下为模板片段，member下是动态模板有登录、注册、评论、个人页面等</p><p>search是搜索模板，system下是四种内容的模板，index.html是首页模板即支持动态也可以静态化，sitemap.xml是站点地图模板</p><p>当您新建一个分类时，分类静态化模板可以再category下面选择其中一个，并设置静态化的访问路径</p><p><img src=\"//site4.dev.publiccms.com:8080/webfile/upload/2020/03-24/12-32-2905711856271141.png\" title=\"2.png\" alt=\"2.png\"/></p><p>内容模板可以再system下面选择一个，并设置静态化访问路径。其中article.html 为文章，picture.html为图集，book.html、chapter.html为图书、章节</p><p><img src=\"//site4.dev.publiccms.com:8080/webfile/upload/2020/03-24/12-32-36030545988429.png\" title=\"3.png\" alt=\"3.png\"/></p>', 318);
INSERT INTO `cms_content_attribute` VALUES (4, NULL, NULL, '{\"dir\":\"download/software\"}', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);

-- ----------------------------
-- Records of cms_place
-- ----------------------------
INSERT INTO `cms_place` VALUES (1, 2, '/ab53b388-be0e-4674-b631-e1de625c74ac.html', 2, 2, 'custom', NULL, '投资管理', '#', NULL, 'assets/images/201210310952338421.gif', '2020-01-01 00:00:00', '2020-01-01 00:00:00', NULL, 1, 0, 0);
INSERT INTO `cms_place` VALUES (2, 2, '/ab53b388-be0e-4674-b631-e1de625c74ac.html', 2, 2, 'custom', NULL, '置业建设', '#', NULL, 'assets/images/201210310953075326.gif', '2020-01-01 00:00:00', '2020-01-01 00:00:00', NULL, 1, 0, 0);
INSERT INTO `cms_place` VALUES (3, 2, '/ab53b388-be0e-4674-b631-e1de625c74ac.html', 2, 2, 'custom', NULL, '能源光电', '#', NULL, 'assets/images/201210310953287112.gif', '2020-01-01 00:00:00', '2020-01-01 00:00:00', NULL, 1, 0, 0);
INSERT INTO `cms_place` VALUES (4, 2, '/ab53b388-be0e-4674-b631-e1de625c74ac.html', 2, 2, 'custom', NULL, '现代物流', '#', NULL, 'assets/images/201210310953526760.gif', '2020-01-01 00:00:00', '2020-01-01 00:00:00', NULL, 1, 0, 0);
INSERT INTO `cms_place` VALUES (5, 2, '/ab53b388-be0e-4674-b631-e1de625c74ac.html', 2, 2, 'custom', NULL, '生物制药', '#', NULL, 'assets/images/201209291145162884.gif', '2020-01-01 00:00:00', '2020-01-01 00:00:00', NULL, 1, 0, 0);
INSERT INTO `cms_place` VALUES (6, 2, '/ab53b388-be0e-4674-b631-e1de625c74ac.html', 2, 2, 'custom', NULL, '现代农业', '#', NULL, 'assets/images/201210091631452563.gif', '2020-01-01 00:00:00', '2020-01-01 00:00:00', NULL, 1, 0, 0);
INSERT INTO `cms_place` VALUES (7, 2, '/505ddbed-f6ff-4a53-b5a8-0b2d7479a2ec.html', 2, 2, 'custom', NULL, 'PublicCMS', 'http://www.publiccms.com/', NULL, '', '2020-01-01 00:00:00', '2020-01-01 00:00:00', NULL, 1, 0, 0);
INSERT INTO `cms_place` VALUES (8, 2, '/e2ef0223-ddd3-4a95-bc65-c7eb796c911a.html', 2, 2, 'custom', NULL, 'OA', '#', NULL, 'assets/images/l_1.gif', '2020-01-01 00:00:00', '2020-01-01 00:00:00', NULL, 1, 0, 0);
INSERT INTO `cms_place` VALUES (9, 2, '/e2ef0223-ddd3-4a95-bc65-c7eb796c911a.html', 2, 2, 'custom', NULL, '联系我们', '#', NULL, 'assets/images/l_2.gif', '2020-01-01 00:00:00', '2020-01-01 00:00:00', NULL, 1, 0, 0);
INSERT INTO `cms_place` VALUES (10, 3, '/94fe86e5-45b3-4896-823a-37c6d7d6c578.html', 3, 3, 'custom', NULL, 'Public CMS启动流程图', '//www.publiccms.com/version/2016/11-24/252.html', NULL, '//www.publiccms.com/2016/11/29/15-25-440266448369330.jpg', '2020-01-01 00:00:00', '2020-01-01 00:00:00', NULL, 1, 0, 0);
INSERT INTO `cms_place` VALUES (11, 3, '/94fe86e5-45b3-4896-823a-37c6d7d6c578.html', 3, 3, 'custom', NULL, '美食', '//www.publiccms.com/picture/2015/08-13/159.html', NULL, '//www.publiccms.com/2015/11/15/17-35-150887-240130090.jpg', '2020-01-01 00:00:00', '2020-01-01 00:00:00', NULL, 1, 0, 0);
INSERT INTO `cms_place` VALUES (12, 3, '/94fe86e5-45b3-4896-823a-37c6d7d6c578.html', 3, 3, 'custom', NULL, '昂科拉', '//www.publiccms.com/car/2015/08-06/9.html', NULL, '//www.publiccms.com/2015/11/15/17-35-0606061972977756.jpg', '2020-01-01 00:00:00', '2020-01-01 00:00:00', NULL, 1, 8, 0);
INSERT INTO `cms_place` VALUES (13, 3, '/94fe86e5-45b3-4896-823a-37c6d7d6c578.html', 3, 3, 'custom', NULL, 'PublicCMS系统使用手册下载', '//www.publiccms.com/help/2015/10-09/179.html', NULL, '//www.publiccms.com/2015/11/15/17-34-560426-203327271.jpg', '2020-01-01 00:00:00', '2020-01-01 00:00:00', NULL, 1, 0, 0);
INSERT INTO `cms_place` VALUES (14, 3, '/3435e9a7-565a-4f93-8670-9c272a1d51cc.html', 3, 3, 'custom', NULL, '唯美动漫图片', '//www.publiccms.com/picture/2015/08-06/4.html', NULL, '//www.publiccms.com/2015/08/07/11-24-1308292097994334.jpg', '2020-01-01 00:00:00', '2020-01-01 00:00:00', NULL, 1, 0, 0);
INSERT INTO `cms_place` VALUES (15, 3, '/3435e9a7-565a-4f93-8670-9c272a1d51cc.html', 3, 3, 'custom', NULL, '昂科拉', '//www.publiccms.com/car/2015/08-06/9.html', NULL, '//www.publiccms.com/2015/08/07/11-24-3602801209954489.jpg', '2020-01-01 00:00:00', '2020-01-01 00:00:00', NULL, 1, 0, 0);
INSERT INTO `cms_place` VALUES (16, 3, '/d0e3dd81-02e5-45d6-a1da-1e652c3ec882.html', 3, 3, 'category', 12, '文章', NULL, NULL, NULL, '2022-04-01 00:00:00', '2022-04-01 00:00:00', NULL, 1, 0, 0);
INSERT INTO `cms_place` VALUES (17, 4, '/8c6844d3-5c59-4ef9-b7fe-895937e66e29.html', 4, 4, 'category', 24, '介绍', NULL, NULL, NULL, '2022-04-01 00:00:00', '2022-04-01 00:00:00', NULL, 1, 0, 0);
INSERT INTO `cms_place` VALUES (18, 4, '/8c6844d3-5c59-4ef9-b7fe-895937e66e29.html', 4, 4, 'category', 25, '文章', NULL, NULL, NULL, '2022-04-01 00:00:00', '2022-04-01 00:00:00', NULL, 1, 0, 0);
INSERT INTO `cms_place` VALUES (19, 4, '/cfdc226d-8abc-48ec-810d-f3941b175b20.html', 1, NULL, 'custom', NULL, 'PublicCMS', 'http://www.publiccms.com/', NULL, NULL, '2022-04-01 00:00:00', '2022-04-01 00:00:00', NULL, 1, 0, 0);
INSERT INTO `cms_place` VALUES (20, 4, '/3fccf48a-3b30-4aa1-80f0-0b860eebe7d4.html', 1, 1, 'custom', NULL, '静态化服务', NULL, NULL, 'assets/img/index-intro-3.svg', '2022-02-15 12:40:07', '2022-02-15 12:39:45', NULL, 1, 0, 0);
INSERT INTO `cms_place` VALUES (21, 4, '/3fccf48a-3b30-4aa1-80f0-0b860eebe7d4.html', 1, 1, 'custom', NULL, '完整可定制化', NULL, NULL, 'assets/img/index-intro-2.svg', '2022-02-15 12:40:28', '2022-02-15 12:40:11', NULL, 1, 0, 0);
INSERT INTO `cms_place` VALUES (22, 4, '/3fccf48a-3b30-4aa1-80f0-0b860eebe7d4.html', 1, 1, 'custom', NULL, '高性能架构', NULL, NULL, 'assets/img/index-intro-1.svg', '2022-02-15 12:40:46', '2022-02-15 12:40:30', NULL, 1, 0, 0);
INSERT INTO `cms_place` VALUES (23, 4, '/footer/0f5a4595-4cd8-4be2-94e8-acb5d5e47e89.html', 1, 1, 'custom', NULL, '一群191381542', 'https://qm.qq.com/cgi-bin/qm/qr?k=xoxCUvv7bDCFQ8AAqaoWB1JsLz0L90qn', NULL, NULL, '2022-02-24 18:11:58', '2022-02-24 18:11:39', NULL, 1, 131, 0);
INSERT INTO `cms_place` VALUES (24, 4, '/footer/0f5a4595-4cd8-4be2-94e8-acb5d5e47e89.html', 1, 1, 'custom', NULL, '二群481589563', 'https://qm.qq.com/cgi-bin/qm/qr?k=x15JZdCp8vWlxV1mMoMTyrHzMqw3dmI1', NULL, NULL, '2022-02-24 18:12:15', '2022-02-24 18:11:39', NULL, 1, 122, 0);
INSERT INTO `cms_place` VALUES (25, 4, '/footer/0f5a4595-4cd8-4be2-94e8-acb5d5e47e89.html', 1, 1, 'custom', NULL, '三群638756883', 'https://qm.qq.com/cgi-bin/qm/qr?k=VogNtcpFOLxvjtvzUcAElZOK-KC4To_u', NULL, NULL, '2022-02-24 18:12:35', '2022-02-24 18:11:39', NULL, 1, 121, 0);
INSERT INTO `cms_place` VALUES (26, 4, '/footer/0f5a4595-4cd8-4be2-94e8-acb5d5e47e89.html', 1, 1, 'custom', NULL, '四群930992232', 'https://qm.qq.com/cgi-bin/qm/qr?k=lsFbfVpj3yqWuY92GYkOG1esbyPNS7O3', NULL, NULL, '2022-02-24 18:12:56', '2022-02-24 18:11:39', NULL, 1, 120, 0);
INSERT INTO `cms_place` VALUES (27, 4, '/footer/21c06930-d5ad-4727-87bb-005104dff25e.html', 1, 1, 'custom', NULL, '18600980373', NULL, NULL, NULL, '2022-02-24 18:16:10', '2022-02-24 18:16:07', NULL, 1, 0, 0);
INSERT INTO `cms_place` VALUES (28, 4, '/footer/4afbbf15-b77e-4ab6-ab21-44b82c096230.html', 1, NULL, 'custom', NULL, '数据结构', 'https://www.publiccms.com/upload/data-dictionary.html', NULL, NULL, '2022-02-24 18:18:14', '2022-02-24 18:17:56', NULL, 1, 78, 0);
INSERT INTO `cms_place` VALUES (29, 4, '/footer/4afbbf15-b77e-4ab6-ab21-44b82c096230.html', 1, 1, 'custom', NULL, 'Java API', 'https://www.publiccms.com/upload/api/overview-summary.html', NULL, NULL, '2022-02-24 18:19:21', '2022-02-24 18:17:56', NULL, 1, 61, 0);
INSERT INTO `cms_place` VALUES (30, 4, '/footer/4afbbf15-b77e-4ab6-ab21-44b82c096230.html', 1, 1, 'custom', NULL, 'FreeMarker在线测试', 'https://www.sanluan.com/freemarker_test.html', NULL, NULL, '2022-02-24 18:19:39', '2022-02-24 18:17:56', NULL, 1, 77, 0);
INSERT INTO `cms_place` VALUES (31, 4, '/footer/4afbbf15-b77e-4ab6-ab21-44b82c096230.html', 1, 1, 'custom', NULL, 'FreeMarker文档', 'http://www.kerneler.com/freemarker2.3.23/', NULL, NULL, '2022-02-24 18:19:55', '2022-02-24 18:17:56', NULL, 1, 75, 0);
INSERT INTO `cms_place` VALUES (32, 4, '/footer/4afbbf15-b77e-4ab6-ab21-44b82c096230.html', 1, 1, 'custom', NULL, 'SEO效果体验', 'https://tongji.baidu.com/web/welcome/ico?s=bd81f02e5329554415de9ee15f916a98', NULL, NULL, '2022-02-24 18:20:11', '2022-02-24 18:17:56', NULL, 1, 83, 0);
INSERT INTO `cms_place` VALUES (33, 4, '/footer/9eedb96c-4f47-422e-8020-81a4b362e0a6.html', 1, 1, 'custom', NULL, '津ICP备19003922号-2', 'https://beian.miit.gov.cn/', NULL, NULL, '2022-02-24 18:23:15', '2022-02-24 18:22:56', NULL, 1, 109, 0);

-- ----------------------------
-- Records of cms_place_attribute
-- ----------------------------
INSERT INTO `cms_place_attribute` VALUES (1, '{}');
INSERT INTO `cms_place_attribute` VALUES (2, '{}');
INSERT INTO `cms_place_attribute` VALUES (3, '{}');
INSERT INTO `cms_place_attribute` VALUES (4, '{}');
INSERT INTO `cms_place_attribute` VALUES (5, '{}');
INSERT INTO `cms_place_attribute` VALUES (6, '{}');
INSERT INTO `cms_place_attribute` VALUES (7, '{}');
INSERT INTO `cms_place_attribute` VALUES (8, '{}');
INSERT INTO `cms_place_attribute` VALUES (9, '{}');
INSERT INTO `cms_place_attribute` VALUES (10, '{}');
INSERT INTO `cms_place_attribute` VALUES (11, '{}');
INSERT INTO `cms_place_attribute` VALUES (12, '{}');
INSERT INTO `cms_place_attribute` VALUES (13, '{}');
INSERT INTO `cms_place_attribute` VALUES (14, '{}');
INSERT INTO `cms_place_attribute` VALUES (15, '{}');
INSERT INTO `cms_place_attribute` VALUES (16, '{}');
INSERT INTO `cms_place_attribute` VALUES (17, '{}');
INSERT INTO `cms_place_attribute` VALUES (18, '{}');
INSERT INTO `cms_place_attribute` VALUES (19, '{}');
INSERT INTO `cms_place_attribute` VALUES (20, '{}');
INSERT INTO `cms_place_attribute` VALUES (21, '{}');
INSERT INTO `cms_place_attribute` VALUES (22, '{}');
INSERT INTO `cms_place_attribute` VALUES (23, '{}');
INSERT INTO `cms_place_attribute` VALUES (24, '{}');
INSERT INTO `cms_place_attribute` VALUES (25, '{}');
INSERT INTO `cms_place_attribute` VALUES (26, '{}');
INSERT INTO `cms_place_attribute` VALUES (27, '{}');
INSERT INTO `cms_place_attribute` VALUES (28, '{}');
INSERT INTO `cms_place_attribute` VALUES (29, '{}');
INSERT INTO `cms_place_attribute` VALUES (30, '{}');
INSERT INTO `cms_place_attribute` VALUES (31, '{}');
INSERT INTO `cms_place_attribute` VALUES (32, '{}');
INSERT INTO `cms_place_attribute` VALUES (33, '{}');
INSERT INTO `cms_place_attribute` VALUES (34, '{}');
-- ----------------------------
-- Records of sys_config_data
-- ----------------------------
INSERT INTO `sys_config_data` VALUES (2, 'site', '{\"register_url\":\"\",\"login_path\":\"\",\"category_path\":\"news/${category.id}\",\"default_content_status\":\"2\",\"default_content_user\":\"\",\"comment_need_check\":\"true\",\"max_scores\":\"5\",\"statistics\":\"\"}');
INSERT INTO `sys_config_data` VALUES (2, 'siteAttribute', '{\"logo\":\"assets/images/logo.gif\",\"parentId\":\"71\",\"parentId_en\":\"72\"}');
INSERT INTO `sys_config_data` VALUES (3, 'cors', '{\"allowed_origins\":\"*\",\"allowed_methods\":\"*\",\"allowed_headers\":\"*\",\"exposed_headers\":\"\",\"allow_credentials\":\"true\",\"max_age\":\"\"}');
INSERT INTO `sys_config_data` VALUES (3, 'site', '{\"site_exclude_module\":\"page_diy,page_diy_preview,myself_device,app_client_list\",\"register_url\":\"http://site3.dev.publiccms.com:8080/register.html\",\"login_path\":\"http://site3.dev.publiccms.com:8080/login.html\",\"category_template_path\":\"/category/list.html\",\"category_path\":\"${category.code}/\",\"default_content_status\":\"2\",\"default_content_user\":\"\",\"comment_need_check\":\"true\",\"max_scores\":\"5\",\"static_after_comment\":\"false\",\"static_after_score\":\"false\",\"statistics\":\"\",\"approve\":\"\"}');
INSERT INTO `sys_config_data` VALUES (3, 'siteAttribute', '{\"logo\":\"\",\"square_logo\":\"\",\"searchPath\":\"//search.site3.dev.publiccms.com:8080/\"}');
INSERT INTO `sys_config_data` VALUES (4, 'cors', '{\"allowed_origins\":\"*\",\"allowed_methods\":\"*\",\"allowed_headers\":\"*\",\"exposed_headers\":\"\",\"allow_credentials\":\"true\",\"max_age\":\"\"}');
INSERT INTO `sys_config_data` VALUES (4, 'site', '{\"register_url\":\"http://site4.dev.publiccms.com:8080/member/register.html\",\"login_path\":\"http://site4.dev.publiccms.com:8080/member/login.html\",\"category_template_path\":\"category.html\",\"category_path\":\"<@cms.category id=category.parentId>${object.code}/</@cms.category>${category.code}/\",\"default_content_status\":\"2\",\"default_content_user\":\"\",\"comment_need_check\":\"true\",\"max_scores\":\"5\",\"static_after_comment\":\"false\",\"static_after_score\":\"false\"}');
INSERT INTO `sys_config_data` VALUES (4, 'siteAttribute', '{\"logo\":\"\",\"square_logo\":\"\"}');

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (2, 2, '技术部', '2', NULL, '', 2, 1000, 1, 1, 1);
INSERT INTO `sys_dept` VALUES (3, 3, '技术部', '3', NULL, '', 3, 1000, 1, 1, 1);
INSERT INTO `sys_dept` VALUES (4, 4, '技术部', '4', NULL, '', 4, 1000, 1, 1, 1);

-- ----------------------------
-- Records of sys_domain
-- ----------------------------
INSERT INTO `sys_domain` VALUES ('dev.publiccms.com', 1, 1, 0, '');
INSERT INTO `sys_domain` VALUES ('localhost', 1, 1, 0, '');
INSERT INTO `sys_domain` VALUES ('site2.dev.publiccms.com', 2, 1, 0, '');
INSERT INTO `sys_domain` VALUES ('site3.dev.publiccms.com', 3, 0, 0, '');
INSERT INTO `sys_domain` VALUES ('site4.dev.publiccms.com', 4, 0, 0, '');

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (2, 2, '站长', 1, 0);
INSERT INTO `sys_role` VALUES (3, 3, '站长', 1, 1);
INSERT INTO `sys_role` VALUES (4, 4, '站长', 1, 1);

-- ----------------------------
-- Records of sys_role_user
-- ----------------------------
INSERT INTO `sys_role_user` VALUES (2, 2);
INSERT INTO `sys_role_user` VALUES (3, 3);
INSERT INTO `sys_role_user` VALUES (4, 4);

-- ----------------------------
-- Records of sys_site
-- ----------------------------
INSERT INTO `sys_site` VALUES (2, NULL, NULL, '企业中英文站点', 0, '//site2.dev.publiccms.com:8080/webfile/', 0, '//site2.dev.publiccms.com:8080/', 0);
INSERT INTO `sys_site` VALUES (3, NULL, NULL, 'PublicCMS官网', 1, '//site3.dev.publiccms.com:8080/webfile/', 0, '//site3.dev.publiccms.com:8080/', 0);
INSERT INTO `sys_site` VALUES (4, NULL, NULL, 'PublicCMS官网2022年新版', 1, '//site4.dev.publiccms.com:8080/webfile/', 0, '//site4.dev.publiccms.com:8080/', 0);

-- ----------------------------
-- Records of sys_task
-- ----------------------------
INSERT INTO `sys_task` VALUES (1, 3, '重新生成所有页面', 0, '0/20 * * * ?', '每20分钟执行', '/publishPage.task', NULL);
INSERT INTO `sys_task` VALUES (2, 3, '重建索引', 0, '0 0 1 1 ? 2099', '手动执行', '/reCreateIndex.task', NULL);
INSERT INTO `sys_task` VALUES (3, 3, '清理三个月以前日志', 0, '0 0 1 * ?', '每月1号凌晨执行', '/clearLog.task', NULL);
INSERT INTO `sys_task` VALUES (4, 3, '重新生成内容页面', 0, '0 0 1 1 ? 2099', '手动执行', '/publishContent.task', NULL);
INSERT INTO `sys_task` VALUES (5, 3, '重新生成所有分类页面', 0, '0 * * * ?', '每小时执行', '/publishCategory.task', NULL);
INSERT INTO `sys_task` VALUES (7, 3, '重新生成全站', 0, '0 0 1 1 ? 2099', '手动执行', '/publishAll.task', NULL);
INSERT INTO `sys_task` VALUES (8, 3, '重新生成昨天文章的上一篇文章', 0, '	1 0 * * ?', '	每天凌晨0:1执行', '/nextContent.task', NULL);
INSERT INTO `sys_task` VALUES (9, 4, '重新生成所有页面', 0, '0/20 * * * ?', '每20分钟执行', '/publishPage.task', NULL);
INSERT INTO `sys_task` VALUES (10, 4, '重新生成内容页面', 0, '0 0 1 1 ? 2099', '手动执行', '/publishContent.task', NULL);
INSERT INTO `sys_task` VALUES (11, 4, '重新生成所有分类页面', 0, '0 * * * ?', '每小时执行', '/publishCategory.task', NULL);
INSERT INTO `sys_task` VALUES (12, 4, '重新生成全站', 0, '0 0 1 1 ? 2099', '手动执行', '/publishAll.task', NULL);
INSERT INTO `sys_task` VALUES (13, 4, '重新生成昨天文章的上一篇文章', 0, '	1 0 * * ?', '	每天凌晨0:1执行', '/nextContent.task', NULL);

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (2, 2, 'admin', '0123456789.2134b56595c73a647716b0a8e33f9d50243fb1c1a088597ba5aa6d9ccadacbd8fc8307bda2adfc8362abe611420bd48263bdcfd91c1c26566ad3a29d79cffd9c', 1, 'admin', NULL, 2, 1, '2', '', 0, 1, 0, '2020-01-01 00:00:00', '127.0.0.1', 0, '2020-01-01 00:00:00');
INSERT INTO `sys_user` VALUES (3, 3, 'admin', '0123456789.2134b56595c73a647716b0a8e33f9d50243fb1c1a088597ba5aa6d9ccadacbd8fc8307bda2adfc8362abe611420bd48263bdcfd91c1c26566ad3a29d79cffd9c', 1, 'admin', NULL, 3, 1, '3', '', 0, 1, 0, '2020-01-01 00:00:00', '127.0.0.1', 0, '2020-01-01 00:00:00');
INSERT INTO `sys_user` VALUES (4, 4, 'admin', '0123456789.2134b56595c73a647716b0a8e33f9d50243fb1c1a088597ba5aa6d9ccadacbd8fc8307bda2adfc8362abe611420bd48263bdcfd91c1c26566ad3a29d79cffd9c', 1, 'admin', NULL, 4, 1, '4', '', 0, 1, 0, '2022-01-01 00:00:00', '127.0.0.1', 0, '2022-01-01 00:00:00');
