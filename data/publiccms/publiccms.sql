-- ----------------------------
-- Records of cms_category
-- ----------------------------
INSERT INTO `cms_category` VALUES (1, 2, '中文栏目', NULL, NULL, '3,5,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,49', '', 'zh', 1, NULL, '#', 1, 0, '#', 1, '', 0, 20, 0, 0, 1, 0, NULL);
INSERT INTO `cms_category` VALUES (2, 2, '英文栏目', NULL, NULL, '4,6,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,50', '', 'en', 1, NULL, '#', 1, 0, '#', 1, '', 0, 20, 0, 0, 1, 0, NULL);
INSERT INTO `cms_category` VALUES (3, 2, '集团简介', 1, NULL, '5,7,8,9,10,11', '', 'about', 1, NULL, '<@_categoryList parentId=category.id><#if page.totalCount gt 0>category/${page.list[0].id}</#if></@_categoryList>', 0, 0, 'category/5', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (4, 2, 'Introduction', 2, NULL, '6,28,29,30,31,32', '', 'introduction', 1, NULL, '<@_categoryList parentId=category.id><#if page.totalCount gt 0>en/category/${page.list[0].id}</#if></@_categoryList>', 0, 0, 'en/category/6', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (5, 2, '集团简介', 3, '1', NULL, '', 'aboutUS', 0, NULL, 'category/${category.id}', 0, 0, 'category/5', 1, 'content/${content.id}', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (6, 2, 'Introduction', 4, '1', NULL, '', 'introductionUS', 0, NULL, 'en/category/${category.id}', 0, 0, 'category/6', 0, 'en/content/${content.id}', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (7, 2, '核心团队', 3, '1', NULL, '', 'tuandui', 0, NULL, 'category/${category.id}', 0, 0, 'category/7', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (8, 2, '组织架构', 3, '1', NULL, '', 'jiagou', 0, NULL, 'category/${category.id}', 0, 0, 'category/8', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (9, 2, '集团荣誉', 3, '1', NULL, '', 'rongyu', 0, NULL, 'category/${category.id}', 0, 0, 'category/9', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (10, 2, '社会责任', 3, '1', NULL, '', 'zeren', 0, NULL, 'category/${category.id}', 0, 0, 'category/10', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (11, 2, '发展目标', 3, '1', NULL, '', 'mubiao', 0, NULL, 'category/${category.id}', 0, 0, 'category/11', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (12, 2, '新闻资讯', 1, NULL, '13,14', '', 'news', 1, NULL, '<@_categoryList parentId=category.id><#if page.totalCount gt 0>news/${page.list[0].id}</#if></@_categoryList>', 0, 0, 'news/13', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (13, 2, '行业资讯', 12, NULL, NULL, '', 'hangye', 0, NULL, 'news/${category.id}', 0, 0, 'news/13', 0, 'content/${content.id}', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (14, 2, '公司新闻', 12, NULL, NULL, '', 'gongsi', 0, NULL, 'news/${category.id}', 0, 0, 'news/14', 0, 'content/${content.id}', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (15, 2, '集团产业', 1, NULL, '16,17,18,19,20,21', '', 'industry', 1, NULL, '<@_categoryList parentId=category.id><#if page.totalCount gt 0>category/${page.list[0].id}</#if></@_categoryList>', 0, 0, 'category/16', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (16, 2, '投资管理', 15, '1', NULL, '', 'touzi', 0, NULL, 'category/${category.id}', 0, 0, 'category/16', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (17, 2, '置业建设', 15, '1', NULL, '', 'jianshe', 0, NULL, 'category/${category.id}', 0, 0, 'category/17', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (18, 2, '能源光电', 15, '1', NULL, '', 'nengyuan', 0, NULL, 'category/${category.id}', 0, 0, 'category/18', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (19, 2, '现代物流', 15, '1', NULL, '', 'wuliu', 0, NULL, 'category/${category.id}', 0, 0, 'category/19', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (20, 2, '生物制药', 15, '1', NULL, '', 'zhiyao', 0, NULL, 'category/${category.id}', 0, 0, 'category/20', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (21, 2, '现代农业', 15, '1', NULL, '', 'nongye', 0, NULL, 'category/${category.id}', 0, 0, 'category/21', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (22, 2, '人才中心', 1, NULL, '23,24,25,26', '', 'rencai', 1, NULL, '<@_categoryList parentId=category.id><#if page.totalCount gt 0>news/${page.list[0].id}</#if></@_categoryList>', 0, 0, 'news/23', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (23, 2, '人才招聘', 22, NULL, NULL, '', 'zhaopin', 0, NULL, 'news/${category.id}', 0, 0, 'news/23', 1, 'content/${content.id}', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (24, 2, '人才理念', 22, '1', NULL, '', 'linian', 0, NULL, 'category/${category.id}', 0, 0, 'category/24', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (25, 2, '薪酬福利', 22, '1', NULL, '', 'fuli', 0, NULL, 'category/${category.id}', 0, 0, 'category/25', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (26, 2, '职业发展', 22, '1', NULL, '', 'fazhan', 0, NULL, 'category/${category.id}', 0, 0, 'category/26', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (27, 2, '联系我们', 1, NULL, '49', '', 'linaxi', 1, NULL, '<@_categoryList parentId=category.id><#if page.totalCount gt 0>category/${page.list[0].id}</#if></@_categoryList>', 0, 0, 'category/49', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (28, 2, 'Core team', 4, '1', NULL, '', 'team', 1, NULL, 'en/category/${category.id}', 0, 0, 'en/category/28', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (29, 2, 'Organization', 4, '1', NULL, '', 'organization', 1, NULL, 'en/category/${category.id}', 0, 0, 'en/category/29', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (30, 2, 'Group honor', 4, '1', NULL, '', 'honor', 1, NULL, 'en/category/${category.id}', 0, 0, 'en/category/30', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (31, 2, 'Social responsibility', 4, '1', NULL, '', 'responsibility', 1, NULL, 'en/category/${category.id}', 0, 0, 'en/category/31', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (32, 2, 'Development goals', 4, '1', NULL, '', 'goals', 1, NULL, 'en/category/${category.id}', 0, 0, 'en/category/32', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (33, 2, 'News', 2, NULL, '34,35', '', 'news_en', 1, NULL, '<@_categoryList parentId=category.id><#if page.totalCount gt 0>en/news/${page.list[0].id}</#if></@_categoryList>', 0, 0, 'en/news/34', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (34, 2, 'Industry News', 33, NULL, NULL, '', 'industry_en', 1, NULL, 'en/news/${category.id}', 0, 0, 'en/news/34', 0, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (35, 2, 'Company news', 33, NULL, NULL, '', 'company', 1, NULL, 'en/news/${category.id}', 0, 0, 'en/news/35', 0, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (36, 2, 'Industry', 2, NULL, '37,38,39,40,41,42', '', 'group_industry', 1, NULL, '<@_categoryList parentId=category.id><#if page.totalCount gt 0>en/category/${page.list[0].id}</#if></@_categoryList>', 0, 0, 'en/category/37', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (37, 2, 'Investment Management', 36, '1', NULL, '', 'investment', 1, NULL, 'en/category/${category.id}', 0, 0, 'en/category/37', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (38, 2, 'Home ownership', 36, '1', NULL, '', 'ownership', 1, NULL, 'en/category/${category.id}', 0, 0, 'en/category/38', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (39, 2, 'Energy photoelectric', 36, '1', NULL, '', 'photoelectric', 1, NULL, 'en/category/${category.id}', 0, 0, 'en/category/39', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (40, 2, 'Modern logistics', 36, '1', NULL, '', 'logistics', 1, NULL, 'en/category/${category.id}', 0, 0, 'en/category/40', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (41, 2, 'Biopharmaceutical', 36, '1', NULL, '', 'biopharmaceutical', 1, NULL, 'en/category/${category.id}', 0, 0, 'en/category/41', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (42, 2, 'Modern agriculture', 36, '1', NULL, '', 'agriculture', 1, NULL, 'en/category/${category.id}', 0, 0, 'en/category/42', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (43, 2, 'Talent', 2, NULL, '44,45,46,47', '', 'talent', 1, NULL, '<@_categoryList parentId=category.id><#if page.totalCount gt 0>en/news/${page.list[0].id}</#if></@_categoryList>', 0, 0, 'en/news/44', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (44, 2, 'Recruitment', 43, NULL, NULL, '', 'recruitment', 1, NULL, 'en/news/${category.id}', 0, 0, 'en/news/44', 0, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (45, 2, 'Talent Concept', 43, '1', NULL, '', 'concept', 1, NULL, 'en/category/${category.id}', 0, 0, 'en/category/45', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (46, 2, 'Remuneration and benefits', 43, '1', NULL, '', 'benefits', 1, NULL, 'en/category/${category.id}', 0, 0, 'en/category/46', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (47, 2, 'Career Development', 43, '1', NULL, '', 'career', 1, NULL, 'en/category/${category.id}', 0, 0, 'en/category/47', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (48, 2, 'Contact', 2, NULL, '50', '', 'contact', 1, NULL, '<@_categoryList parentId=category.id><#if page.totalCount gt 0>en/category/${page.list[0].id}</#if></@_categoryList>', 0, 0, 'en/category/50', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (49, 2, '联系我们', 27, '1', NULL, '', 'contact_us', 1, NULL, 'category/${category.id}', 0, 0, 'category/49', 1, '', 0, 20, 0, 0, 0, 0, NULL);
INSERT INTO `cms_category` VALUES (50, 2, 'Contact us', 48, '1', NULL, '', 'contact_us_en', 1, NULL, 'en/category/${category.id}', 0, 0, 'en/category/50', 1, '', 0, 20, 0, 0, 0, 0, NULL);

-- ----------------------------
-- Records of cms_category_attribute
-- ----------------------------
INSERT INTO `cms_category_attribute` VALUES (1, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (2, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (3, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (4, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (5, '', '', '', '{\"article\":\"<p>阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬阿斯蒂芬</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (6, '', '', '', '{\"article\":\"\"}');
INSERT INTO `cms_category_attribute` VALUES (7, '', '', '', '{\"article\":\"<p>核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队核心团队</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (8, '', '', '', '{\"article\":\"<p>组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构组织架构</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (9, '', '', '', '{\"article\":\"<h2 class=\\\"R-h2\\\" style=\\\"padding: 0px 0px 0px 15px; margin: 0px; clear: both; overflow: hidden; height: 21px; line-height: 21px; border-bottom: 1px solid rgb(51, 51, 153); font-family: ΢���ź�; font-size: 14px; background: url(&quot;../images/r_top.gif&quot;) 2px center no-repeat rgb(255, 255, 255); color: rgb(68, 68, 68); white-space: normal;\\\">集团荣誉</h2><p><br/></p>\"}');
INSERT INTO `cms_category_attribute` VALUES (10, '', '', '', '{\"article\":\"<p>社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任社会责任</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (11, '', '', '', '{\"article\":\"<h2 class=\\\"R-h2\\\" style=\\\"padding: 0px 0px 0px 15px; margin: 0px; clear: both; overflow: hidden; height: 21px; line-height: 21px; border-bottom: 1px solid rgb(51, 51, 153); font-family: ΢���ź�; font-size: 14px; background: url(&quot;../images/r_top.gif&quot;) 2px center no-repeat rgb(255, 255, 255); color: rgb(68, 68, 68); white-space: normal;\\\">发展目标</h2><p><br/></p>\"}');
INSERT INTO `cms_category_attribute` VALUES (12, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (13, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (14, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (15, '', '', '', '{\"article\":\"<p>集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业集团产业</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (16, '', '', '', '{\"article\":\"<p>投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理投资管理</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (17, '', '', '', '{\"article\":\"<p>置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设置业建设</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (18, '', '', '', '{\"article\":\"<p>能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电能源光电</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (19, '', '', '', '{\"article\":\"<p>现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流现代物流</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (20, '', '', '', '{\"article\":\"<p>生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药生物制药</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (21, '', '', '', '{\"article\":\"<p>现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业现代农业</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (22, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (23, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (24, '', '', '', '{\"article\":\"<p>人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念人才理念</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (25, '', '', '', '{\"article\":\"<p>薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利薪酬福利</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (26, '', '', '', '{\"article\":\"<p>职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展职业发展</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (27, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (28, '', '', '', '{\"article\":\"<p>Core teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore teamCore team</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (29, '', '', '', '{\"article\":\"<p>OrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganizationOrganization</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (30, '', '', '', '{\"article\":\"<p>Group honor&nbsp;Group honor&nbsp;Group honor&nbsp;Group honor&nbsp;Group honor&nbsp;Group honor&nbsp;Group honor&nbsp;Group honor&nbsp;Group honor&nbsp;Group honor&nbsp;Group honor&nbsp;Group honor&nbsp;Group honor&nbsp;Group honor&nbsp;Group honor&nbsp;Group honor Group honor Group honor Group honor Group honor Group honor Group honor&nbsp;Group honor&nbsp;Group honor Group honor Group honor&nbsp;Group honor&nbsp;Group honor&nbsp;Group honor Group honor Group honor Group honor Group honor&nbsp;</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (31, '', '', '', '{\"article\":\"<p>Social responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibilitySocial responsibility</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (32, '', '', '', '{\"article\":\"<p>Development goals&nbsp;Development goals&nbsp;Development goals&nbsp;Development goals&nbsp;Development goals&nbsp;Development goals&nbsp;Development goals&nbsp;Development goals&nbsp;Development goals&nbsp;Development goals&nbsp;Development goals&nbsp;Development goals&nbsp;Development goals Development goals Development goals Development goals Development goals Development goals Development goals&nbsp;Development goals&nbsp;Development goals Development goals Development goals&nbsp;Development goals&nbsp;Development goals Development goals Development goals&nbsp;Development goals&nbsp;Development goals&nbsp;</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (33, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (34, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (35, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (36, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (37, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (38, '', '', '', '{\"article\":\"<p>Home ownership&nbsp;Home ownership&nbsp;Home ownership&nbsp;Home ownership&nbsp;Home ownership&nbsp;Home ownership&nbsp;Home ownership&nbsp;Home ownership&nbsp;Home ownership&nbsp;Home ownership&nbsp;Home ownership&nbsp;Home ownership&nbsp;Home ownership&nbsp;Home ownership&nbsp;Home ownership&nbsp;Home ownership&nbsp;</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (39, '', '', '', '{\"article\":\"<p>Energy photoelectric&nbsp;Energy photoelectric&nbsp;Energy photoelectric&nbsp;Energy photoelectric&nbsp;Energy photoelectric&nbsp;Energy photoelectric&nbsp;Energy photoelectric&nbsp;Energy photoelectric&nbsp;Energy photoelectric&nbsp;Energy photoelectric&nbsp;Energy photoelectric&nbsp;Energy photoelectric&nbsp;Energy photoelectric&nbsp;Energy photoelectric&nbsp;</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (40, '', '', '', '{\"article\":\"<p>Modern logistics&nbsp;Modern logistics&nbsp;Modern logistics&nbsp;Modern logistics&nbsp;Modern logistics&nbsp;Modern logistics&nbsp;Modern logistics&nbsp;Modern logistics&nbsp;Modern logistics&nbsp;Modern logistics&nbsp;Modern logistics&nbsp;Modern logistics&nbsp;Modern logistics&nbsp;Modern logistics&nbsp;Modern logistics&nbsp;Modern logistics&nbsp;Modern logistics&nbsp;Modern logistics&nbsp;Modern logistics&nbsp;Modern logistics&nbsp;Modern logistics&nbsp;</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (41, '', '', '', '{\"article\":\"<p>Biopharmaceutical&nbsp;Biopharmaceutical&nbsp;Biopharmaceutical&nbsp;Biopharmaceutical&nbsp;Biopharmaceutical&nbsp;Biopharmaceutical&nbsp;Biopharmaceutical&nbsp;Biopharmaceutical&nbsp;Biopharmaceutical&nbsp;Biopharmaceutical&nbsp;Biopharmaceutical&nbsp;Biopharmaceutical&nbsp;Biopharmaceutical&nbsp;Biopharmaceutical&nbsp;Biopharmaceutical&nbsp;Biopharmaceutical Biopharmaceutical&nbsp;Biopharmaceutical&nbsp;Biopharmaceutical&nbsp;</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (42, '', '', '', '{\"article\":\"<p>Modern agriculture&nbsp;Modern agriculture&nbsp;Modern agriculture&nbsp;Modern agriculture&nbsp;Modern agriculture&nbsp;Modern agriculture&nbsp;Modern agriculture&nbsp;Modern agriculture&nbsp;Modern agriculture&nbsp;Modern agriculture&nbsp;Modern agriculture&nbsp;Modern agriculture&nbsp;Modern agriculture&nbsp;Modern agriculture&nbsp;Modern agriculture&nbsp;Modern agriculture&nbsp;Modern agriculture&nbsp;Modern agriculture&nbsp;</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (43, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (44, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (45, '', '', '', '{\"article\":\"<p>Talent Concept&nbsp;Talent Concept&nbsp;Talent Concept&nbsp;Talent Concept&nbsp;Talent Concept&nbsp;Talent Concept&nbsp;Talent Concept&nbsp;Talent Concept&nbsp;Talent Concept&nbsp;Talent Concept&nbsp;Talent Concept&nbsp;Talent Concept&nbsp;Talent Concept&nbsp;Talent Concept&nbsp;Talent Concept&nbsp;Talent Concept&nbsp;Talent Concept&nbsp;Talent Concept&nbsp;Talent Concept&nbsp;Talent Concept&nbsp;Talent Concept&nbsp;Talent Concept&nbsp;Talent Concept&nbsp;</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (46, '', '', '', '{\"article\":\"<p>Remuneration and benefits&nbsp;Remuneration and benefits&nbsp;Remuneration and benefits&nbsp;Remuneration and benefits&nbsp;Remuneration and benefits&nbsp;Remuneration and benefits&nbsp;Remuneration and benefits&nbsp;Remuneration and benefits&nbsp;Remuneration and benefits&nbsp;Remuneration and benefits&nbsp;Remuneration and benefits&nbsp;Remuneration and benefits&nbsp;Remuneration and benefits&nbsp;Remuneration and benefits&nbsp;Remuneration and benefits&nbsp;Remuneration and benefits&nbsp;Remuneration and benefits&nbsp;Remuneration and benefits&nbsp;Remuneration and benefits&nbsp;Remuneration and benefits&nbsp;</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (47, '', '', '', '{\"article\":\"<p>Career Development&nbsp;Career Development&nbsp;Career Development&nbsp;Career Development&nbsp;Career Development&nbsp;Career Development&nbsp;Career Development&nbsp;Career Development&nbsp;Career Development&nbsp;Career Development&nbsp;Career Development&nbsp;Career Development&nbsp;Career Development&nbsp;Career Development&nbsp;Career Development&nbsp;Career Development&nbsp;Career Development&nbsp;Career Development&nbsp;Career Development&nbsp;Career Development&nbsp;Career Development&nbsp;</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (48, '', '', '', NULL);
INSERT INTO `cms_category_attribute` VALUES (49, '', '', '', '{\"article\":\"<p>联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们联系我们</p>\"}');
INSERT INTO `cms_category_attribute` VALUES (50, '', '', '', '{\"article\":\"<p>Contact us&nbsp;Contact us&nbsp;Contact us&nbsp;Contact us&nbsp;Contact us&nbsp;Contact us&nbsp;Contact us&nbsp;Contact us&nbsp;Contact us&nbsp;Contact us&nbsp;Contact us&nbsp;Contact us&nbsp;Contact us&nbsp;Contact us&nbsp;Contact us&nbsp;Contact us&nbsp;Contact us&nbsp;Contact us&nbsp;Contact us&nbsp;</p>\"}');

-- ----------------------------
-- Records of cms_category_model
-- ----------------------------

INSERT INTO `cms_category_model` VALUES (13, 'article', 2, 0, NULL, '');
INSERT INTO `cms_category_model` VALUES (14, 'article', 2, 0, NULL, '');
INSERT INTO `cms_category_model` VALUES (23, 'article', 2, 0, NULL, '');
INSERT INTO `cms_category_model` VALUES (34, 'article', 2, 0, NULL, '');
INSERT INTO `cms_category_model` VALUES (35, 'article', 2, 0, NULL, '');
INSERT INTO `cms_category_model` VALUES (44, 'article', 2, 0, NULL, '');

-- ----------------------------
-- Records of cms_content
-- ----------------------------
INSERT INTO `cms_content` VALUES (1, 2, '企业中英文站点模板使用说明', 2, 2, 2, 14, 'article', NULL, NULL, 0, NULL, NULL, 0, 0, 0, 0, 0, 'content/1', '企业中英文站点模板中主要有首页模板index.html,文章模板content.html，分类单页模板category.html，分类内容列表页news.html\n\n分为中英文两套模板\n\n所有模板均为动态模板，文章，分类模板接受参数为id,pageIndex等，既支持rest风格url如 news/1_12,又支持传统参数类型如news.html?id=1&pageIndex=12\n\n在分类中对带有文章的分类可以使用category.html，对内容列表可以使用news.html，填写分类访问路径为 模板名/${category.id} 或 模板名.html?id=${category.id}', NULL, NULL, 0, 0, 0, 0.00, 0, 0, '2020-01-01 00:00:00', NULL, '2020-01-01 00:00:00', NULL, NULL, '2020-01-01 00:00:00', 0, 1, 0);

-- ----------------------------
-- Records of cms_content_attribute
-- ----------------------------
INSERT INTO `cms_content_attribute` VALUES (1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '<p>企业中英文站点模板中主要有首页模板index.html,文章模板content.html，分类单页模板category.html，分类内容列表页news.html</p>\n\n<p>分为中英文两套模板</p>\n\n<p>所有模板均为动态模板，文章，分类模板接受参数为id,pageIndex等，既支持rest风格url如 news/1_12,又支持传统参数类型如news.html?id=1&amp;pageIndex=12</p>\n\n<p>在分类中对带有文章的分类可以使用category.html，对内容列表可以使用news.html，填写分类访问路径为 模板名/${category.id} 或 模板名.html?id=${category.id}</p>\n', 301);

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
-- ----------------------------
-- Records of sys_config_data
-- ----------------------------
INSERT INTO `sys_config_data` VALUES (2, 'site', '{\"register_url\":\"\",\"login_path\":\"\",\"category_path\":\"news/${category.id}\",\"default_content_status\":\"2\",\"default_content_user\":\"\",\"comment_need_check\":\"true\",\"max_scores\":\"5\",\"statistics\":\"\"}');
INSERT INTO `sys_config_data` VALUES (2, 'siteAttribute', '{\"logo\":\"assets/images/logo.gif\",\"parentId\":\"71\",\"parentId_en\":\"72\"}');

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (2, 2, '技术部', '2', NULL, '', 2, 1000, 1, 1, 1);

-- ----------------------------
-- Records of sys_domain
-- ----------------------------
INSERT INTO `sys_domain` VALUES ('site2.dev.publiccms.com', 2, 1, 0, '');

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (2, 2, '站长', 1, 0);

-- ----------------------------
-- Records of sys_role_user
-- ----------------------------
INSERT INTO `sys_role_user` VALUES (2, 2);

-- ----------------------------
-- Records of sys_site
-- ----------------------------
INSERT INTO `sys_site` VALUES (2, NULL, NULL, '企业中英文站点', 0, '//site2.dev.publiccms.com:8080/webfile/', 0, '//site2.dev.publiccms.com:8080/', 0);

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (2, 2, 'admin', '0123456789.2134b56595c73a647716b0a8e33f9d50243fb1c1a088597ba5aa6d9ccadacbd8fc8307bda2adfc8362abe611420bd48263bdcfd91c1c26566ad3a29d79cffd9c', 1, 'admin', NULL, 2, 1, '2', '', 0, 1, 0, '2020-01-01 00:00:00', '127.0.0.1', 0, '2020-01-01 00:00:00');
