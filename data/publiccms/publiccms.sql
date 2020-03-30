-- ----------------------------
-- Records of cms_category
-- ----------------------------
INSERT INTO `cms_category` VALUES (1, 3, '演示', NULL, NULL, '17,15,12,9,8,7,6,18', '', 'demonstrate', '/category/parent.html', '${category.code}/index.html', 0, 1, 'demonstrate/index.html', '${content.publishDate?string(\'yyyy/MM/dd\')}/${content.id}.html', 1, 10, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (6, 3, '汽车', 1, NULL, NULL, '', 'car', '/category/list.html', '${category.code}/index.html', 0, 1, 'car/index.html', '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', 1, 10, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (7, 3, '社会', 1, NULL, NULL, '', 'social', '/category/list.html', '${category.code}/index.html', 0, 1, 'social/index.html', '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', 1, 10, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (8, 3, '美图', 1, NULL, NULL, '', 'picture', '/category/list.html', '${category.code}/index.html', 0, 1, 'picture/index.html', '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', 1, 10, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (9, 3, '系统介绍', 1, NULL, NULL, '', 'introduction', '/category/list.html', '${category.code}/index.html', 0, 1, 'introduction/index.html', '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', 1, 10, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (12, 3, '文章', 1, NULL, NULL, '', 'article', '/category/list.html', '${category.code}/index.html', 0, 1, 'article/index.html', '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', 1, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (15, 3, '小说', 1, NULL, NULL, '', 'novel', '/category/list.html', '${category.code}/index.html', 0, 1, 'novel/index.html', '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', 1, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (17, 3, '科技', 1, NULL, NULL, '', 'science', '/category/list.html', '${category.code}/index.html', 0, 1, 'science/index.html', '${category.code}/${content.publishDate?string(\'yyyy/MM-dd\')}/${content.id}.html', 1, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (19, 3, '案例', NULL, NULL, NULL, '', 'case', '/category/parent.html', '${category.code}/index.html', 0, 1, 'case/index.html', '${content.publishDate?string(\'yyyy/MM/dd\')}/${content.id}.html', 1, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (71, 2, '中文栏目', NULL, NULL, '73,75,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,120', '', 'zh', NULL, '#', 1, 0, '#', '', 0, 20, 0, 0, 1, 0, NULL);
INSERT INTO `cms_category` VALUES (72, 2, '英文栏目', NULL, NULL, '74,76,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,121', '', 'en', NULL, '#', 1, 0, '#', '', 0, 20, 0, 0, 1, 0, NULL);
INSERT INTO `cms_category` VALUES (73, 2, '集团简介', 71, NULL, '75,77,78,79,80,81', '', 'about', NULL, '<@_categoryList parentId=category.id><#if page.totalCount gt 0>category/${page.list[0].id}</#if></@_categoryList>', 0, 0, 'category/75', '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (74, 2, 'Introduction', 72, NULL, '76,99,100,101,102,103', '', 'introduction', NULL, '<@_categoryList parentId=category.id><#if page.totalCount gt 0>en/category/${page.list[0].id}</#if></@_categoryList>', 0, 0, 'en/category/76', '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (75, 2, '集团简介', 73, 1, NULL, '', 'aboutUS', NULL, 'category/${category.id}', 0, 0, 'category/75', 'content/${content.id}', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (76, 2, 'Introduction', 74, 1, NULL, '', 'introductionUS', NULL, 'en/category/${category.id}', 0, 0, 'en/category/76', 'en/content/${content.id}', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (77, 2, '核心团队', 73, 1, NULL, '', 'tuandui', NULL, 'category/${category.id}', 0, 0, 'category/77', '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (78, 2, '组织架构', 73, 1, NULL, '', 'jiagou', NULL, 'category/${category.id}', 0, 0, 'category/78', '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (79, 2, '集团荣誉', 73, 1, NULL, '', 'rongyu', NULL, 'category/${category.id}', 0, 0, 'category/79', '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (80, 2, '社会责任', 73, 1, NULL, '', 'zeren', NULL, 'category/${category.id}', 0, 0, 'category/80', '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (81, 2, '发展目标', 73, 1, NULL, '', 'mubiao', NULL, 'category/${category.id}', 0, 0, 'category/81', '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (82, 2, '新闻资讯', 71, NULL, '83,84', '', 'news', NULL, '<@_categoryList parentId=category.id><#if page.totalCount gt 0>news/${page.list[0].id}</#if></@_categoryList>', 0, 0, 'news/83', '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (83, 2, '行业资讯', 82, NULL, NULL, '', 'hangye', NULL, 'news/${category.id}', 0, 0, 'news/83', 'content/${content.id}', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (84, 2, '公司新闻', 82, NULL, NULL, '', 'gongsi', NULL, 'news/${category.id}', 0, 0, 'news/84', 'content/${content.id}', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (85, 2, '集团产业', 71, NULL, '86,87,88,89,90,91', '', 'industry', NULL, '<@_categoryList parentId=category.id><#if page.totalCount gt 0>category/${page.list[0].id}</#if></@_categoryList>', 0, 0, 'category/86', '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (86, 2, '投资管理', 85, 1, NULL, '', 'touzi', NULL, 'category/${category.id}', 0, 0, 'category/86', '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (87, 2, '置业建设', 85, 1, NULL, '', 'jianshe', NULL, 'category/${category.id}', 0, 0, 'category/87', '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (88, 2, '能源光电', 85, 1, NULL, '', 'nengyuan', NULL, 'category/${category.id}', 0, 0, 'category/88', '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (89, 2, '现代物流', 85, 1, NULL, '', 'wuliu', NULL, 'category/${category.id}', 0, 0, 'category/89', '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (90, 2, '生物制药', 85, 1, NULL, '', 'zhiyao', NULL, 'category/${category.id}', 0, 0, 'category/90', '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (91, 2, '现代农业', 85, 1, NULL, '', 'nongye', NULL, 'category/${category.id}', 0, 0, 'category/91', '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (92, 2, '人才中心', 71, NULL, '93,94,95,96', '', 'rencai', NULL, '<@_categoryList parentId=category.id><#if page.totalCount gt 0>news/${page.list[0].id}</#if></@_categoryList>', 0, 0, 'news/93', '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (93, 2, '人才招聘', 92, NULL, NULL, '', 'zhaopin', NULL, 'news/${category.id}', 0, 0, 'news/93', 'content/${content.id}', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (94, 2, '人才理念', 92, 1, NULL, '', 'linian', NULL, 'category/${category.id}', 0, 0, 'category/94', '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (95, 2, '薪酬福利', 92, 1, NULL, '', 'fuli', NULL, 'category/${category.id}', 0, 0, 'category/95', '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (96, 2, '职业发展', 92, 1, NULL, '', 'fazhan', NULL, 'category/${category.id}', 0, 0, 'category/96', '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (97, 2, '联系我们', 71, NULL, '120', '', 'linaxi', NULL, '<@_categoryList parentId=category.id><#if page.totalCount gt 0>category/${page.list[0].id}</#if></@_categoryList>', 0, 0, 'category/120', '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (99, 2, 'Core team', 74, 1, NULL, '', 'team', NULL, 'en/category/${category.id}', 0, 0, 'en/category/99', '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (100, 2, 'Organization', 74, 1, NULL, '', 'organization', NULL, 'en/category/${category.id}', 0, 0, 'en/category/100', '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (101, 2, 'Group honor', 74, 1, NULL, '', 'honor', NULL, 'en/category/${category.id}', 0, 0, 'en/category/101', '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (102, 2, 'Social responsibility', 74, 1, NULL, '', 'responsibility', NULL, 'en/category/${category.id}', 0, 0, 'en/category/102', '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (103, 2, 'Development goals', 74, 1, NULL, '', 'goals', NULL, 'en/category/${category.id}', 0, 0, 'en/category/103', '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (104, 2, 'News', 72, NULL, '105,106', '', 'news_en', NULL, '<@_categoryList parentId=category.id><#if page.totalCount gt 0>en/news/${page.list[0].id}</#if></@_categoryList>', 0, 0, 'en/news/105', '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (105, 2, 'Industry News', 104, NULL, NULL, '', 'industry_en', NULL, 'en/news/${category.id}', 0, 0, 'en/news/105', '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (106, 2, 'Company news', 104, NULL, NULL, '', 'company', NULL, 'en/news/${category.id}', 0, 0, 'en/news/106', '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (107, 2, 'Industry', 72, NULL, '108,109,110,111,112,113', '', 'group_industry', NULL, '<@_categoryList parentId=category.id><#if page.totalCount gt 0>en/category/${page.list[0].id}</#if></@_categoryList>', 0, 0, 'en/category/108', '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (108, 2, 'Investment Management', 107, 1, NULL, '', 'investment', NULL, 'en/category/${category.id}', 0, 0, 'en/category/108', '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (109, 2, 'Home ownership', 107, 1, NULL, '', 'ownership', NULL, 'en/category/${category.id}', 0, 0, 'en/category/109', '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (110, 2, 'Energy photoelectric', 107, 1, NULL, '', 'photoelectric', NULL, 'en/category/${category.id}', 0, 0, 'en/category/110', '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (111, 2, 'Modern logistics', 107, 1, NULL, '', 'logistics', NULL, 'en/category/${category.id}', 0, 0, 'en/category/111', '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (112, 2, 'Biopharmaceutical', 107, 1, NULL, '', 'biopharmaceutical', NULL, 'en/category/${category.id}', 0, 0, 'en/category/112', '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (113, 2, 'Modern agriculture', 107, 1, NULL, '', 'agriculture', NULL, 'en/category/${category.id}', 0, 0, 'en/category/113', '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (114, 2, 'Talent', 72, NULL, '115,116,117,118', '', 'talent', NULL, '<@_categoryList parentId=category.id><#if page.totalCount gt 0>en/news/${page.list[0].id}</#if></@_categoryList>', 0, 0, 'en/news/115', '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (115, 2, 'Recruitment', 114, NULL, NULL, '', 'recruitment', NULL, 'en/news/${category.id}', 0, 0, 'en/news/115', '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (116, 2, 'Talent Concept', 114, 1, NULL, '', 'concept', NULL, 'en/category/${category.id}', 0, 0, 'en/category/116', '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (117, 2, 'Remuneration and benefits', 114, 1, NULL, '', 'benefits', NULL, 'en/category/${category.id}', 0, 0, 'en/category/117', '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (118, 2, 'Career Development', 114, 1, NULL, '', 'career', NULL, 'en/category/${category.id}', 0, 0, 'en/category/118', '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (119, 2, 'Contact', 72, NULL, '121', '', 'contact', NULL, '<@_categoryList parentId=category.id><#if page.totalCount gt 0>en/category/${page.list[0].id}</#if></@_categoryList>', 0, 0, 'en/category/121', '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (120, 2, '联系我们', 97, 1, NULL, '', 'contact_us', NULL, 'category/${category.id}', 0, 0, 'category/120', '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (121, 2, 'Contact us', 119, 1, NULL, '', 'contact_us_en', NULL, 'en/category/${category.id}', 0, 0, 'en/category/121', '', 0, 20, 0, 0, 0, 0, NULL);

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
INSERT INTO `cms_category_attribute` VALUES (19, '案例', 'PublicCMS案例', 'PublicCMS案例', NULL);
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

-- ----------------------------
-- Records of cms_category_model
-- ----------------------------

INSERT INTO `cms_category_model` VALUES (6, '1', '/system/article.html');
INSERT INTO `cms_category_model` VALUES (6, '2', '');
INSERT INTO `cms_category_model` VALUES (6, '3', '/system/picture.html');
INSERT INTO `cms_category_model` VALUES (6, '5', '');
INSERT INTO `cms_category_model` VALUES (6, '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES (7, '1', '/system/article.html');
INSERT INTO `cms_category_model` VALUES (7, '2', '');
INSERT INTO `cms_category_model` VALUES (7, '3', '/system/picture.html');
INSERT INTO `cms_category_model` VALUES (7, '5', '');
INSERT INTO `cms_category_model` VALUES (7, '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES (8, '3', '/system/picture.html');
INSERT INTO `cms_category_model` VALUES (8, '5', '');
INSERT INTO `cms_category_model` VALUES (8, '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES (9, '1', '/system/article.html');
INSERT INTO `cms_category_model` VALUES (9, '2', '');
INSERT INTO `cms_category_model` VALUES (9, '3', '/system/picture.html');
INSERT INTO `cms_category_model` VALUES (9, '5', '');
INSERT INTO `cms_category_model` VALUES (9, '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES (12, '1', '/system/article.html');
INSERT INTO `cms_category_model` VALUES (12, '2', '');
INSERT INTO `cms_category_model` VALUES (12, '5', '');
INSERT INTO `cms_category_model` VALUES (12, '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES (14, '5', '');
INSERT INTO `cms_category_model` VALUES (14, '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES (15, '4', '/system/book.html');
INSERT INTO `cms_category_model` VALUES (15, '5', '');
INSERT INTO `cms_category_model` VALUES (15, '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES (16, '5', '');
INSERT INTO `cms_category_model` VALUES (16, '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES (17, '1', '/system/article.html');
INSERT INTO `cms_category_model` VALUES (17, '2', '');
INSERT INTO `cms_category_model` VALUES (17, '3', '/system/picture.html');
INSERT INTO `cms_category_model` VALUES (17, '5', '');
INSERT INTO `cms_category_model` VALUES (17, '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES (18, '5', '');
INSERT INTO `cms_category_model` VALUES (18, '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES (18, '7', '');
INSERT INTO `cms_category_model` VALUES (18, '8', '');
INSERT INTO `cms_category_model` VALUES (19, '2', '');
INSERT INTO `cms_category_model` VALUES (19, '5', '');
INSERT INTO `cms_category_model` VALUES (19, '6', '/system/chapter.html');
INSERT INTO `cms_category_model` VALUES (76, 'article', NULL);
INSERT INTO `cms_category_model` VALUES (83, 'article', NULL);
INSERT INTO `cms_category_model` VALUES (84, 'article', NULL);
INSERT INTO `cms_category_model` VALUES (93, 'article', NULL);

-- ----------------------------
-- Records of cms_category_type
-- ----------------------------
INSERT INTO `cms_category_type` VALUES (1, 2, '带文章的分类', 0, 1);

-- ----------------------------
-- Records of cms_content
-- ----------------------------
INSERT INTO `cms_content` VALUES (1, 3, 'PublicCMS官网模板使用说明', 3, 3, 12, '1', NULL, NULL, 0, 0, NULL, '', 0, 0, 0, 1, 'article/2020/01-01/1.html', 'PublicCMS官网模板是一组静态化模板', '', NULL, NULL, 0, 1, 0, 79, '2020-01-01 00:00:00', NULL, '2020-01-01 00:00:00', NULL, '2020-01-01 00:00:00', 0, 1, 0);
INSERT INTO `cms_content` VALUES (2, 2, '企业中英文站点模板使用说明', 2, 2, 84, 'article', NULL, NULL, 0, 0, NULL, NULL, 0, 0, 0, 0, 'content/2', '企业中英文站点模板中主要有首页模板index.html,文章模板content.html，分类单页模板category.html，分类内容列表页news.html\n\n分为中英文两套模板\n\n所有模板均为动态模板，文章，分类模板接受参数为id,pageIndex等，既支持rest风格url如 news/1_12,又支持传统参数类型如news.html?id=1&pageIndex=12\n\n在分类中对带有文章的分类可以使用category.html，对内容列表可以使用news.html，填写分类访问路径为 模板名/${category.id} 或 模板名.html?id=${category.id}', '', NULL, NULL, 0, 0, 0, 1, '2020-01-01 00:00:00', NULL, '2020-01-01 00:00:00', NULL, '2020-01-01 00:00:00', 0, 1, 0);

-- ----------------------------
-- Records of cms_content_attribute
-- ----------------------------
INSERT INTO `cms_content_attribute` VALUES (1, NULL, NULL, NULL, '    PublicCMS官网模板是一组静态化模板，它是现在PublicCMS官网所采用的模板category目录下为分类模板，ftl目录下为模板片段，member下是动态模板有登录、注册、评论、个人页面等search是搜索模板，system下是四种内容的模板，index.html是首页模板即支持动态也可以静态化，sitemap.xml是站点地图模板当您新建一个分类时，分类静态化模板可以再category下面选择其中一个，并设置静态化的访问路径内容模板可以再system下面选择一个，并设置静态化访问路径。其中article.html 为文章，picture.html为图集，book.html、chapter.html为图书、章节', '<p>&nbsp;&nbsp;&nbsp;&nbsp;PublicCMS官网模板是一组静态化模板，它是现在PublicCMS官网所采用的模板</p><p><img src=\"//site3.dev.publiccms.com:8080/publiccms/webfile/upload/2020/03-24/12-28-500720-90407063.png\" title=\"1.png\" alt=\"1.png\"/></p><p>category目录下为分类模板，ftl目录下为模板片段，member下是动态模板有登录、注册、评论、个人页面等</p><p>search是搜索模板，system下是四种内容的模板，index.html是首页模板即支持动态也可以静态化，sitemap.xml是站点地图模板</p><p>当您新建一个分类时，分类静态化模板可以再category下面选择其中一个，并设置静态化的访问路径</p><p><img src=\"//site3.dev.publiccms.com:8080/publiccms/webfile/upload/2020/03-24/12-32-2905711856271141.png\" title=\"2.png\" alt=\"2.png\"/></p><p>内容模板可以再system下面选择一个，并设置静态化访问路径。其中article.html 为文章，picture.html为图集，book.html、chapter.html为图书、章节</p><p><img src=\"//site3.dev.publiccms.com:8080/publiccms/webfile/upload/2020/03-24/12-32-36030545988429.png\" title=\"3.png\" alt=\"3.png\"/></p>', 318);
INSERT INTO `cms_content_attribute` VALUES (2, NULL, NULL, NULL, NULL, '<p>企业中英文站点模板中主要有首页模板index.html,文章模板content.html，分类单页模板category.html，分类内容列表页news.html</p>\n\n<p>分为中英文两套模板</p>\n\n<p>所有模板均为动态模板，文章，分类模板接受参数为id,pageIndex等，既支持rest风格url如 news/1_12,又支持传统参数类型如news.html?id=1&amp;pageIndex=12</p>\n\n<p>在分类中对带有文章的分类可以使用category.html，对内容列表可以使用news.html，填写分类访问路径为 模板名/${category.id} 或 模板名.html?id=${category.id}</p>\n', 301);

-- ----------------------------
-- Records of cms_place
-- ----------------------------
INSERT INTO `cms_place` VALUES (1, 3, '/index.html/94fe86e5-45b3-4896-823a-37c6d7d6c578.html', 3, 3, 'custom', NULL, 'Public CMS启动流程图', '//www.publiccms.com/version/2016/11-24/252.html', '//www.publiccms.com/2016/11/29/15-25-440266448369330.jpg', '2020-01-01 00:00:00', '2020-01-01 00:00:00', NULL, 1, 6, 0);
INSERT INTO `cms_place` VALUES (2, 3, '/index.html/94fe86e5-45b3-4896-823a-37c6d7d6c578.html', 3, 3, 'custom', NULL, '美食', '//www.publiccms.com/picture/2015/08-13/159.html', '//www.publiccms.com/2015/11/15/17-35-150887-240130090.jpg', '2020-01-01 00:00:00', '2020-01-01 00:00:00', NULL, 1, 4, 0);
INSERT INTO `cms_place` VALUES (3, 3, '/index.html/94fe86e5-45b3-4896-823a-37c6d7d6c578.html', 3, 3, 'custom', NULL, '昂科拉', '//www.publiccms.com/car/2015/08-06/9.html', '//www.publiccms.com/2015/11/15/17-35-0606061972977756.jpg', '2020-01-01 00:00:00', '2020-01-01 00:00:00', NULL, 1, 8, 0);
INSERT INTO `cms_place` VALUES (4, 3, '/index.html/94fe86e5-45b3-4896-823a-37c6d7d6c578.html', 3, 3, 'custom', NULL, 'PublicCMS系统使用手册下载', '//www.publiccms.com/help/2015/10-09/179.html', '//www.publiccms.com/2015/11/15/17-34-560426-203327271.jpg', '2020-01-01 00:00:00', '2020-01-01 00:00:00', NULL, 1, 18, 0);
INSERT INTO `cms_place` VALUES (6, 3, '/category/list.html/3435e9a7-565a-4f93-8670-9c272a1d51cc.html', 3, 3, 'custom', NULL, '唯美动漫图片', '//www.publiccms.com/picture/2015/08-06/4.html', '//www.publiccms.com/2015/08/07/11-24-1308292097994334.jpg', '2020-01-01 00:00:00', '2020-01-01 00:00:00', NULL, 1, 4, 0);
INSERT INTO `cms_place` VALUES (7, 3, '/category/list.html/3435e9a7-565a-4f93-8670-9c272a1d51cc.html', 3, 3, 'custom', NULL, '昂科拉', '//www.publiccms.com/car/2015/08-06/9.html', '//www.publiccms.com/2015/08/07/11-24-3602801209954489.jpg', '2020-01-01 00:00:00', '2020-01-01 00:00:00', NULL, 1, 2, 0);
INSERT INTO `cms_place` VALUES (13, 2, '/ab53b388-be0e-4674-b631-e1de625c74ac.html', 2, 2, 'custom', NULL, '投资管理', '#', 'assets/images/201210310952338421.gif', '2020-01-01 00:00:00', '2020-01-01 00:00:00', NULL, 1, 0, 0);
INSERT INTO `cms_place` VALUES (14, 2, '/ab53b388-be0e-4674-b631-e1de625c74ac.html', 2, 2, 'custom', NULL, '置业建设', '#', 'assets/images/201210310953075326.gif', '2020-01-01 00:00:00', '2020-01-01 00:00:00', NULL, 1, 0, 0);
INSERT INTO `cms_place` VALUES (15, 2, '/ab53b388-be0e-4674-b631-e1de625c74ac.html', 2, 2, 'custom', NULL, '能源光电', '#', 'assets/images/201210310953287112.gif', '2020-01-01 00:00:00', '2020-01-01 00:00:00', NULL, 1, 0, 0);
INSERT INTO `cms_place` VALUES (16, 2, '/ab53b388-be0e-4674-b631-e1de625c74ac.html', 2, 2, 'custom', NULL, '现代物流', '#', 'assets/images/201210310953526760.gif', '2020-01-01 00:00:00', '2020-01-01 00:00:00', NULL, 1, 0, 0);
INSERT INTO `cms_place` VALUES (17, 2, '/ab53b388-be0e-4674-b631-e1de625c74ac.html', 2, 2, 'custom', NULL, '生物制药', '#', 'assets/images/201209291145162884.gif', '2020-01-01 00:00:00', '2020-01-01 00:00:00', NULL, 1, 21, 0);
INSERT INTO `cms_place` VALUES (18, 2, '/ab53b388-be0e-4674-b631-e1de625c74ac.html', 2, 2, 'custom', NULL, '现代农业', '#', 'assets/images/201210091631452563.gif', '2020-01-01 00:00:00', '2020-01-01 00:00:00', NULL, 1, 0, 0);
INSERT INTO `cms_place` VALUES (19, 2, '/505ddbed-f6ff-4a53-b5a8-0b2d7479a2ec.html', 2, 2, 'custom', NULL, 'PublicCMS', 'http://www.publiccms.com/', '', '2020-01-01 00:00:00', '2020-01-01 00:00:00', NULL, 1, 3, 0);
INSERT INTO `cms_place` VALUES (20, 2, '/e2ef0223-ddd3-4a95-bc65-c7eb796c911a.html', 2, 2, 'custom', NULL, 'OA', '#', 'assets/images/l_1.gif', '2020-01-01 00:00:00', '2020-01-01 00:00:00', NULL, 1, 0, 0);
INSERT INTO `cms_place` VALUES (21, 2, '/e2ef0223-ddd3-4a95-bc65-c7eb796c911a.html', 2, 2, 'custom', NULL, '联系我们', '#', 'assets/images/l_2.gif', '2020-01-01 00:00:00', '2020-01-01 00:00:00', NULL, 1, 0, 0);
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

-- ----------------------------
-- Records of sys_config_data
-- ----------------------------
INSERT INTO `sys_config_data` VALUES (2, 'siteAttribute', '{\"logo\":\"assets/images/logo.gif\",\"parentId\":\"71\",\"parentId_en\":\"72\"}');
INSERT INTO `sys_config_data` VALUES (3, 'cors', '{\"allowed_origins\":\"*\",\"allowed_methods\":\"*\",\"allowed_headers\":\"*\",\"exposed_headers\":\"\",\"allow_credentials\":\"true\",\"max_age\":\"\"}');
INSERT INTO `sys_config_data` VALUES (3, 'site', '{\"return_url\":\"http://search.site3.dev.publiccms.com:8080/publiccms/\",\"register_url\":\"http://site3.dev.publiccms.com:8080/publiccms/register.html\",\"login_path\":\"http://site3.dev.publiccms.com:8080/publiccms/login.html\",\"slogan\":\"与先进同行\",\"statistics\":\"\",\"approve\":\"\"}');
INSERT INTO `sys_config_data` VALUES (3, 'siteAttribute', '{\"logo\":\"\",\"square_logo\":\"\",\"searchPath\":\"//search.site3.dev.publiccms.com:8080/publiccms/\"}');

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (2, 2, '技术部', NULL, '', 2, 1000, 1, 1, 1);
INSERT INTO `sys_dept` VALUES (3, 3, '技术部', NULL, '', 3, 1000, 1, 1, 1);

-- ----------------------------
-- Records of sys_domain
-- ----------------------------
INSERT INTO `sys_domain` VALUES ('search.site3.dev.publiccms.com', 3, 0, '/search/');
INSERT INTO `sys_domain` VALUES ('site2.dev.publiccms.com', 2, 1, '');
INSERT INTO `sys_domain` VALUES ('site3.dev.publiccms.com', 3, 0, '/member/');

-- ----------------------------
-- Records of sys_extend
-- ----------------------------
INSERT INTO `sys_extend` VALUES (1, 'categoryType', 1);

-- ----------------------------
-- Records of sys_extend_field
-- ----------------------------
INSERT INTO `sys_extend_field` VALUES (1, 'article', 1, 0, NULL, '内容', '', 'editor', '', NULL, 0);

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (2, 2, '站长', 1, 0);
INSERT INTO `sys_role` VALUES (3, 3, '站长', 1, 1);

-- ----------------------------
-- Records of sys_role_user
-- ----------------------------
INSERT INTO `sys_role_user` VALUES (2, 2);
INSERT INTO `sys_role_user` VALUES (3, 3);

-- ----------------------------
-- Records of sys_site
-- ----------------------------
INSERT INTO `sys_site` VALUES (2, NULL, '企业中英文站点', 0, '//site2.dev.publiccms.com:8080/publiccms/webfile/', 0, '//site2.dev.publiccms.com:8080/publiccms/', 0);
INSERT INTO `sys_site` VALUES (3, NULL, 'PublicCMS官网', 1, '//site3.dev.publiccms.com:8080/publiccms/webfile/', 0, '//site3.dev.publiccms.com:8080/publiccms/', 0);

-- ----------------------------
-- Records of sys_task
-- ----------------------------
INSERT INTO `sys_task` VALUES (1, 3, '重新生成所有页面', 0, '0 0/2 * * ?', '重新生成所有页面', '/publishPage.task', NULL);
INSERT INTO `sys_task` VALUES (2, 3, '重建索引', 0, '0 0 1 1 ? 2099', '重建全部索引', '/reCreateIndex.task', NULL);
INSERT INTO `sys_task` VALUES (3, 3, '清理日志', 0, '0 0 1 * ?', '清理三个月以前的日志', '/clearLog.task', NULL);
INSERT INTO `sys_task` VALUES (4, 3, '重新生成内容页面', 0, '0 0 1 1 ? 2099', '重新生成内容页面', '/publishContent.task', NULL);
INSERT INTO `sys_task` VALUES (5, 3, '重新生成所有分类页面', 0, '0 0/6 * * ?', '重新生成所有分类页面', '/publishCategory.task', NULL);
INSERT INTO `sys_task` VALUES (7, 3, '重新生成全站', 0, '0 0 1 1 ? 2099', '重新生成全站', '/publishAll.task', NULL);

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (2, 2, 'admin', '21232f297a57a5a743894a0e4a801fc3', NULL, 1, 'admin', 2, 1, '2', '', 0, 1, 0, '2020-01-01 00:00:00', '127.0.0.1', 0, '2020-01-01 00:00:00');
INSERT INTO `sys_user` VALUES (3, 3, 'admin', '21232f297a57a5a743894a0e4a801fc3', NULL, 1, 'admin', 3, 1, '3', '', 0, 1, 0, '2020-01-01 00:00:00', '127.0.0.1', 0, '2020-01-01 00:00:00');
